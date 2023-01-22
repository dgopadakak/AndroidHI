package com.example.androidhi

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidhi.databinding.ActivityMainBinding
import com.example.androidhi.firm.Pharmacy
import com.example.androidhi.firm.PharmacyChain
import com.example.androidhi.firm.PharmacyChainOperator
import com.example.androidhi.firm.dbWithRoom.App
import com.example.androidhi.firm.dbWithRoom.AppDatabase
import com.example.androidhi.firm.dbWithRoom.PharmacyChainOperatorDao
import com.example.androidhi.forRecyclerView.CustomRecyclerAdapterForExams
import com.example.androidhi.forRecyclerView.RecyclerItemClickListener
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    PharmacyDetailsDialogFragment.OnInputListenerSortId
{
    private val gsonBuilder = GsonBuilder()
    private val gson: Gson = gsonBuilder.create()
    private val serverIP = "192.168.1.69"
    private val serverPort = 8888
    private lateinit var connection: Connection
    private var connectionStage: Int = 0
    private var startTime: Long = 0

    private lateinit var db: AppDatabase
    private lateinit var pcoDao: PharmacyChainOperatorDao

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var nv: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewForStart: TextView
    private lateinit var recyclerViewExams: RecyclerView
    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts
            .StartActivityForResult())
    { result ->
        if (result.resultCode == Activity.RESULT_OK)
        {
            val data: Intent? = result.data
            processOnActivityResult(data)
        }
    }

    private var pco: PharmacyChainOperator = PharmacyChainOperator()
    private var currentChainID: Int = -1
    private var currentPharmacyID: Int = -1
    private var waitingForUpdate: Boolean = false
    private lateinit var groupTitle: String

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        drawerLayout = binding.drawerLayout
        nv = binding.navView
        nv.setNavigationItemSelectedListener(this)
        toolbar = findViewById(R.id.toolbar)
        toolbar.apply { setNavigationIcon(R.drawable.ic_my_menu) }
        toolbar.setNavigationOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        textViewForStart = findViewById(R.id.textViewForStart)
        textViewForStart.visibility = View.INVISIBLE
        progressBar = findViewById(R.id.progressBar)
        recyclerViewExams = findViewById(R.id.recyclerViewExams)
        recyclerViewExams.visibility = View.INVISIBLE
        recyclerViewExams.layoutManager = LinearLayoutManager(this)

        recyclerViewExams.addOnItemTouchListener(
            RecyclerItemClickListener(
                recyclerViewExams,
                object : RecyclerItemClickListener.OnItemClickListener
                {
                    override fun onItemClick(view: View, position: Int)
                    {
                        currentPharmacyID = position
                        Snackbar.make(findViewById(R.id.app_bar_main),
                                "С ${pco.getExam(currentChainID, currentPharmacyID).timeOpen} " +
                                        "по ${pco.getExam(currentChainID, currentPharmacyID)
                                            .timeClose}", Snackbar.LENGTH_LONG)
                                .show()
                    }
                    override fun onItemLongClick(view: View, position: Int)
                    {
                        currentPharmacyID = position
                        val examDetails = PharmacyDetailsDialogFragment()
                        val tempExam = pco.getExam(currentChainID, currentPharmacyID)
                        val bundle = Bundle()
                        bundle.putString("name", tempExam.nameOfPharmacy)
                        bundle.putString("address", tempExam.address)
                        bundle.putString("number", tempExam.num.toString())
                        bundle.putString("timeOpen", tempExam.timeOpen)
                        bundle.putString("timeClose", tempExam.timeClose)
                        bundle.putString("parking", tempExam.parkingSpaces.toString())
                        bundle.putString("delivery", tempExam.isDelivery.toString())
                        bundle.putString("comment", tempExam.comment)
                        bundle.putString("connection", connectionStage.toString())
                        examDetails.arguments = bundle
                        examDetails.show(fragmentManager, "MyCustomDialog")
                    }
                }
            )
        )

        db = App.instance?.database!!
        pcoDao = db.groupOperatorDao()
        startTime = System.currentTimeMillis()
        connection = Connection(serverIP, serverPort, "REFRESH", this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean
    {
        if (currentChainID != -1 && connectionStage == 1)
        {
            menu.getItem(0).isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        val id = item.itemId
        if (id == R.id.action_add)
        {
            val intent = Intent()
            intent.setClass(this, EditPharmacyActivity::class.java)
            intent.putExtra("action", 1)
            resultLauncher.launch(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    internal inner class Connection(
        private val SERVER_IP: String,
        private val SERVER_PORT: Int,
        private val refreshCommand: String,
        private val activity: Activity
    ) {
        private var outputServer: PrintWriter? = null
        private var inputServer: BufferedReader? = null
        var thread1: Thread? = null
        private var threadT: Thread? = null

        internal inner class Thread1Server : Runnable {
            override fun run()
            {
                val socket: Socket
                try {
                    socket = Socket(SERVER_IP, SERVER_PORT)
                    outputServer = PrintWriter(socket.getOutputStream())
                    inputServer = BufferedReader(InputStreamReader(socket.getInputStream()))
                    Thread(Thread2Server()).start()
                    sendDataToServer(refreshCommand)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        internal inner class Thread2Server : Runnable {
            override fun run() {
                while (true) {
                    try {
                        val message = inputServer!!.readLine()
                        if (message != null)
                        {
                            activity.runOnUiThread { processingInputStream(message) }
                        } else {
                            thread1 = Thread(Thread1Server())
                            thread1!!.start()
                            return
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        internal inner class Thread3Server(private val message: String) : Runnable
        {
            override fun run()
            {
                outputServer!!.write(message)
                outputServer!!.flush()
            }
        }

        internal inner class ThreadT : Runnable
        {
            override fun run() {
                while (true)
                {
                    if (System.currentTimeMillis() - startTime > 5000L && connectionStage == 0)
                    {
                        activity.runOnUiThread { Snackbar.make(findViewById(R.id.app_bar_main),
                            "Подключиться не удалось!\n" +
                                    "Будет использоваться локальная база данных.",
                            Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.YELLOW)
                            .show() }
                        connectionStage = -1
                        activity.runOnUiThread { progressBar.visibility = View.INVISIBLE
                            textViewForStart.visibility = View.VISIBLE }
                        pco = pcoDao.getById(1)
                        for (i in 0 until pco.getPharmacyChains().size)
                        {
                            activity.runOnUiThread { nv.menu.add(0, i, 0,
                                pco.getPharmacyChains()[i].name as CharSequence) }
                        }
                    }
                }
            }
        }

        fun sendDataToServer(text: String)
        {
            Thread(Thread3Server(text + "\n")).start()
        }

        private fun processingInputStream(text: String)
        {
            pcoDao.delete(PharmacyChainOperator())
            val tempGo: PharmacyChainOperator = gson.fromJson(text, PharmacyChainOperator::class.java)
            pcoDao.insert(tempGo)

            if (connectionStage != 1)
            {
                Snackbar.make(findViewById(R.id.app_bar_main),
                    "Успешно подключено!\n" +
                            "Будут использоваться серверные данные.",
                    Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.GREEN)
                    .show()
            }

            progressBar.visibility = View.INVISIBLE
            if (connectionStage == 0)
            {
                textViewForStart.visibility = View.VISIBLE
            }
            for (i in 0 until pco.getPharmacyChains().size)
            {
                nv.menu.removeItem(i)
            }
            val tempArrayListPharmacyChains: ArrayList<PharmacyChain> = /*dbh.getAllData()*/tempGo.getPharmacyChains()
            pco.setPharmacyChains(tempArrayListPharmacyChains)
            for (i in 0 until tempArrayListPharmacyChains.size)
            {
                nv.menu.add(
                    0, i, 0,
                    tempArrayListPharmacyChains[i].name as CharSequence
                )
            }
            if (waitingForUpdate || connectionStage == -1)
            {
                waitingForUpdate = false
                if (currentChainID != -1)
                {
                    recyclerViewExams.adapter = CustomRecyclerAdapterForExams(
                        pco.getPharmaciesNames(currentChainID),
                        pco.getPharmaciesAddresses(currentChainID),
                        pco.getPharmaciesNumbers(currentChainID)
                    )
                }
            }
            connectionStage = 1
        }

        init {
            thread1 = Thread(Thread1Server())
            thread1!!.start()
            threadT = Thread(ThreadT())
            threadT!!.start()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Snackbar.make(findViewById(R.id.app_bar_main),
            "Выбрана сеть: ${item.title}.",
            Snackbar.LENGTH_LONG)
            .show()
        drawerLayout.closeDrawer(GravityCompat.START)
        textViewForStart.visibility = View.INVISIBLE
        groupTitle = "Сеть ${item.title}"
        toolbar.title = groupTitle
        invalidateOptionsMenu()
        currentChainID = item.itemId
        recyclerViewExams.adapter = CustomRecyclerAdapterForExams(
            pco.getPharmaciesNames(currentChainID),
            pco.getPharmaciesAddresses(currentChainID),
            pco.getPharmaciesNumbers(currentChainID))
        recyclerViewExams.visibility = View.VISIBLE
        return true
    }

    fun delExam()
    {
        connection.sendDataToServer("d$currentChainID,$currentPharmacyID")
        waitingForUpdate = true
    }

    override fun sendInputSortId(sortId: Int)
    {
        if (sortId > -1 && sortId < 8)      // Сортировка
        {
            pco.sortExams(currentChainID, sortId)
            if (connectionStage == 1)
            {
                connection.sendDataToServer("u" + gson.toJson(pco))
            }
            toolbar.title = when (sortId)
            {
                0 -> "$groupTitle сорт. Назввние"
                1 -> "$groupTitle сорт. Адрес"
                2 -> "$groupTitle сорт. №"
                3 -> "$groupTitle сорт. Вр. Откр."
                4 -> "$groupTitle сорт. Вр. Закр."
                5 -> "$groupTitle сорт. Парк. м."
                6 -> "$groupTitle сорт. Доставка"
                7 -> "$groupTitle сорт. Описание"
                else -> groupTitle
            }
        }
        if (sortId == 8)        // Удаление
        {
            val manager: FragmentManager = supportFragmentManager
            val myDialogFragmentDelPharmacy = MyDialogFragmentDelPharmacy()
            val bundle = Bundle()
            bundle.putString("name", pco.getExam(currentChainID, currentPharmacyID).nameOfPharmacy)
            myDialogFragmentDelPharmacy.arguments = bundle
            myDialogFragmentDelPharmacy.show(manager, "myDialog")
        }
        if (sortId == 9)        // Изменение
        {
            val tempExam = pco.getExam(currentChainID, currentPharmacyID)
            val intent = Intent()
            intent.setClass(this, EditPharmacyActivity::class.java)
            intent.putExtra("action", 2)
            intent.putExtra("name", tempExam.nameOfPharmacy)
            intent.putExtra("address", tempExam.address)
            intent.putExtra("number", tempExam.num.toString())
            intent.putExtra("timeOpen", tempExam.timeOpen)
            intent.putExtra("timeClose", tempExam.timeClose)
            intent.putExtra("parking", tempExam.parkingSpaces.toString())
            intent.putExtra("delivery", tempExam.isDelivery.toString())
            intent.putExtra("comment", tempExam.comment)
            resultLauncher.launch(intent)
        }
        recyclerViewExams.adapter = CustomRecyclerAdapterForExams(
            pco.getPharmaciesNames(currentChainID),
            pco.getPharmaciesAddresses(currentChainID),
            pco.getPharmaciesNumbers(currentChainID))
    }

    private fun processOnActivityResult(data: Intent?)
    {
        val action = data!!.getIntExtra("action", -1)
        val examName = data.getStringExtra("name")
        val teacherName = data.getStringExtra("address")
        val auditory = data.getIntExtra("number", -1)
        val date = data.getStringExtra("timeOpen")
        val time = data.getStringExtra("timeClose")
        val people = data.getIntExtra("parking", -1)
        val abstract = data.getIntExtra("delivery", 0)
        val comment = data.getStringExtra("comment")
        val tempPharmacy = Pharmacy(examName!!, teacherName!!, auditory, date!!, time!!, people
            , abstract, comment!!)
        val tempExamJSON: String = gson.toJson(tempPharmacy)

        if (action == 1)
        {
            val tempStringToSend = "a${pco.getPharmacyChains()[currentChainID].name}##$tempExamJSON"
            connection.sendDataToServer(tempStringToSend)
            waitingForUpdate = true
        }
        if (action == 2)
        {
            val tempStringToSend = "e$currentChainID,$currentPharmacyID##$tempExamJSON"
            connection.sendDataToServer(tempStringToSend)
            waitingForUpdate = true
        }
        if (action == -1)
        {
            Snackbar.make(findViewById(R.id.app_bar_main),
                "Ошибка добавления/изменения!",
                Snackbar.LENGTH_LONG)
                .setBackgroundTint(Color.RED)
                .show()
        }
    }
}