package com.example.androidhi

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import java.time.LocalTime
import java.util.*

class EditPharmacyActivity : AppCompatActivity()
{
    private lateinit var editName: EditText
    private lateinit var editAddress: EditText
    private lateinit var editNumber: EditText
    private lateinit var editTimeOpen: EditText
    private lateinit var editTimeClose: EditText
    private lateinit var editParking: EditText
    private lateinit var editDelivery: EditText
    private lateinit var editComment: EditText

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pharmacy)

        editName = findViewById(R.id.editTextExamName)
        editAddress = findViewById(R.id.editTextTeacherName)
        editNumber = findViewById(R.id.editTextAuditory)
        editTimeOpen = findViewById(R.id.editTextDate)
        editTimeClose = findViewById(R.id.editTextTime)
        editParking = findViewById(R.id.editTextPeople)
        editDelivery = findViewById(R.id.editTextAbstract)
        editComment = findViewById(R.id.editTextComment)

        val action = intent.getSerializableExtra("action") as Int

        findViewById<Button>(R.id.button_confirm).setOnClickListener { confirmChanges(action) }

        if (action == 2)
        {
            editName.setText(intent.getSerializableExtra("name") as String)
            editAddress.setText(intent.getSerializableExtra("address") as String)
            editNumber.setText(intent.getSerializableExtra("number") as String)
            editTimeOpen.setText(intent.getSerializableExtra("timeOpen") as String)
            editTimeClose.setText(intent.getSerializableExtra("timeClose") as String)
            editParking.setText(intent.getSerializableExtra("parking") as String)
            if (intent.getSerializableExtra("delivery") as String == "1")
            {
                editDelivery.setText("да")
            }
            else
            {
                editDelivery.setText("нет")
            }
            editComment.setText(intent.getSerializableExtra("comment") as String)
        }
    }

    private fun confirmChanges(action: Int)
    {
        if (editName.text.toString() != "" && editAddress.text.toString() != ""
            && editNumber.text.toString() != "" && editTimeOpen.text.toString() != ""
            && editTimeClose.text.toString() != "" && editParking.text.toString() != ""
            && editDelivery.text.toString() != "")
        {
            if (editDelivery.text.toString().trim().lowercase(Locale.ROOT) == "да"
                || editDelivery.text.toString().trim().lowercase(Locale.ROOT) == "нет")
            {
                if (isTimeValid(editTimeOpen.text.toString().trim())
                    && isTimeValid(editTimeClose.text.toString().trim()))
                {
                    val intent = Intent(this@EditPharmacyActivity,
                        MainActivity::class.java)
                    intent.putExtra("action",    action)
                    intent.putExtra("name",      editName.text.toString().trim())
                    intent.putExtra("address",   editAddress.text.toString().trim())
                    intent.putExtra("number",    editNumber.text.toString().trim().toInt())
                    intent.putExtra("timeOpen",  editTimeOpen.text.toString().trim())
                    intent.putExtra("timeClose", editTimeClose.text.toString().trim())
                    intent.putExtra("parking",   editParking.text.toString().trim().toInt())
                    if (editDelivery.text.toString().trim().lowercase(Locale.ROOT) == "да")
                    {
                        intent.putExtra("delivery", 1)
                    }
                    else
                    {
                        intent.putExtra("delivery", 0)
                    }
                    intent.putExtra("comment", editComment.text.toString().trim())
                    setResult(RESULT_OK, intent)
                    finish()
                }
                else
                {
                    Snackbar.make(findViewById(R.id.button_confirm),
                        "Проверьте время!", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(Color.RED)
                        .show()
                }
            }
            else
            {
                Snackbar.make(findViewById(R.id.button_confirm),
                    "Поле \"Доставка\" поддерживает только " +
                            "значения \"да\" или \"нет\"!", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.RED)
                    .show()
            }
        }
        else
        {
            Snackbar.make(findViewById(R.id.button_confirm),
                "Заполните обязательные поля!", Snackbar.LENGTH_LONG)
                .setBackgroundTint(Color.RED)
                .show()
        }
    }

    private fun isTimeValid(date: String?): Boolean
    {
        return try
        {
            LocalTime.parse(date)
            true
        }
        catch (e: java.lang.Exception)
        {
            false
        }
    }
}