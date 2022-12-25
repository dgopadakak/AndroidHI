package com.example.androidhi

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class PharmacyDetailsDialogFragment: android.app.DialogFragment()
{
    private val exceptionTag = "PharmacyDetailsDialogFragment"

    interface OnInputListenerSortId
    {
        fun sendInputSortId(sortId: Int)
    }

    lateinit var onInputListenerSortId: OnInputListenerSortId

    private lateinit var textViewNameTitle: TextView
    private lateinit var textViewName: TextView
    private lateinit var textViewAddressTitle: TextView
    private lateinit var textViewAddress: TextView
    private lateinit var textViewNumTitle: TextView
    private lateinit var textViewNum: TextView
    private lateinit var textViewOpenTitle: TextView
    private lateinit var textViewOpen: TextView
    private lateinit var textViewCloseTitle: TextView
    private lateinit var textViewClose: TextView
    private lateinit var textViewParkingTitle: TextView
    private lateinit var textViewParking: TextView
    private lateinit var textViewDeliveryTitle: TextView
    private lateinit var textViewDelivery: TextView
    private lateinit var textViewCommentTitle: TextView
    private lateinit var textViewComment: TextView
    private lateinit var buttonDel: Button
    private lateinit var buttonEdit: Button
    private lateinit var buttonCancel: Button
    private lateinit var buttonOk: Button
    private lateinit var textViewCurrSort: TextView

    private var currentIdForSort: Int = -1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        val view: View = inflater!!.inflate(R.layout.pharmacy_details, container, false)
        textViewNameTitle = view.findViewById(R.id.textViewExamNameTitle)
        textViewName = view.findViewById(R.id.textViewExamName)
        textViewAddressTitle = view.findViewById(R.id.textViewTeacherNameTitle)
        textViewAddress = view.findViewById(R.id.textViewTeacherName)
        textViewNumTitle = view.findViewById(R.id.textViewAuditoryTitle)
        textViewNum = view.findViewById(R.id.textViewAuditory)
        textViewOpenTitle = view.findViewById(R.id.textViewDateTitle)
        textViewOpen = view.findViewById(R.id.textViewDate)
        textViewCloseTitle = view.findViewById(R.id.textViewTimeTitle)
        textViewClose = view.findViewById(R.id.textViewTime)
        textViewParkingTitle = view.findViewById(R.id.textViewPeopleTitle)
        textViewParking = view.findViewById(R.id.textViewPeople)
        textViewDeliveryTitle = view.findViewById(R.id.textViewAbstractTitle)
        textViewDelivery = view.findViewById(R.id.textViewAbstract)
        textViewCommentTitle = view.findViewById(R.id.textViewCommentTitle)
        textViewComment = view.findViewById(R.id.textViewComment)
        buttonDel = view.findViewById(R.id.button_details_delete)
        buttonEdit = view.findViewById(R.id.button_details_edit)
        buttonCancel = view.findViewById(R.id.button_cancel)
        buttonOk = view.findViewById(R.id.button_details_ok)
        textViewCurrSort = view.findViewById(R.id.textViewCurrentSort)

        textViewNameTitle.setOnLongClickListener { setSortId(0) }
        textViewName.setOnLongClickListener { setSortId(0) }
        textViewAddressTitle.setOnLongClickListener { setSortId(1) }
        textViewAddress.setOnLongClickListener { setSortId(1) }
        textViewNumTitle.setOnLongClickListener { setSortId(2) }
        textViewNum.setOnLongClickListener { setSortId(2) }
        textViewOpenTitle.setOnLongClickListener { setSortId(3) }
        textViewOpen.setOnLongClickListener { setSortId(3) }
        textViewCloseTitle.setOnLongClickListener { setSortId(4) }
        textViewClose.setOnLongClickListener { setSortId(4) }
        textViewParkingTitle.setOnLongClickListener { setSortId(5) }
        textViewParking.setOnLongClickListener { setSortId(5) }
        textViewDeliveryTitle.setOnLongClickListener { setSortId(6) }
        textViewDelivery.setOnLongClickListener { setSortId(6) }
        textViewCommentTitle.setOnLongClickListener { setSortId(7) }
        textViewComment.setOnLongClickListener { setSortId(7) }

        buttonDel.setOnClickListener { returnDel() }
        buttonEdit.setOnClickListener { returnEdit() }
        buttonCancel.setOnClickListener { dialog.dismiss() }
        buttonOk.setOnClickListener { returnIdForSort() }

        val arguments: Bundle = getArguments()
        textViewName.text = arguments.getString("name")
        textViewAddress.text = arguments.getString("address")
        textViewNum.text = arguments.getString("number")
        textViewOpen.text = arguments.getString("timeOpen")
        textViewClose.text = arguments.getString("timeClose")
        textViewParking.text = arguments.getString("parking")
        if (arguments.getString("delivery") == "1")
        {
            textViewDelivery.text = "да"
        }
        else
        {
            textViewDelivery.text = "нет"
        }
        textViewComment.text = arguments.getString("comment")
        if (arguments.getString("connection") != "1")
        {
            buttonDel.isEnabled = false
            buttonEdit.isEnabled = false
        }

        return view
    }

    override fun onAttach(activity: Activity?)
    {
        super.onAttach(activity)
        try {
            onInputListenerSortId = getActivity() as OnInputListenerSortId
        }
        catch (e: ClassCastException)
        {
            Log.e(exceptionTag, "onAttach: ClassCastException: " + e.message)
        }
    }

    private fun setSortId(id: Int): Boolean
    {
        currentIdForSort = id
        if (currentIdForSort == 0)
        {
            textViewCurrSort.text = "Сортировка по названию"
        }
        else if (currentIdForSort == 1)
        {
            textViewCurrSort.text = "Сортировка по адресу"
        }
        else if (currentIdForSort == 2)
        {
            textViewCurrSort.text = "Сортировка по номеру"
        }
        else if (currentIdForSort == 3)
        {
            textViewCurrSort.text = "Сортировка по времени открытия"
        }
        else if (currentIdForSort == 4)
        {
            textViewCurrSort.text = "Сортировка по времени закрытия"
        }
        else if (currentIdForSort == 5)
        {
            textViewCurrSort.text = "Сортировка по количеству парковочных мест"
        }
        else if (currentIdForSort == 6)
        {
            textViewCurrSort.text = "Сортировка по доставке"
        }
        else if (currentIdForSort == 7)
        {
            textViewCurrSort.text = "Сортировка по описанию"
        }
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(200
            , VibrationEffect.DEFAULT_AMPLITUDE))
        return true
    }

    private fun returnIdForSort()
    {
        onInputListenerSortId.sendInputSortId(currentIdForSort)
        dialog.dismiss()
    }

    private fun returnDel()
    {
        currentIdForSort = 8
        returnIdForSort()
    }

    private fun returnEdit()
    {
        currentIdForSort = 9
        returnIdForSort()
    }
}