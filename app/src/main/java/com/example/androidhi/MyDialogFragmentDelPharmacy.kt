package com.example.androidhi

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MyDialogFragmentDelPharmacy: DialogFragment()
{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val arguments: Bundle? = arguments
        val pharmacyName = arguments?.getString("name")
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("Будет удалена аптека: $pharmacyName")
            .setTitle("Внимание!")
            .setPositiveButton("Продолжить"
            ) { _, _ -> (activity as MainActivity?)?.delPharmacy() }
            .setNegativeButton("Отмена") { _, _ -> }
        return builder.create()
    }
}