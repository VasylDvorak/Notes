package com.example.notes

import com.example.notes.OnDialogListener
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.notes.R
import com.example.notes.DialogHeaderDrawerFragment

class DialogHeaderDrawerFragment : DialogFragment() {
    private var dialogListener: OnDialogListener? = null
    fun setOnDialogListener(dialogListener: OnDialogListener?) {
        this.dialogListener = dialogListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_drawer_header_text_dialog, container, false)
        // Запретим пользователю выходить без выбора
        isCancelable = false
        view.findViewById<View>(R.id.btn_name).setOnClickListener {
            dismiss()
            if (dialogListener != null) dialogListener!!.onDialogName()
        }
        view.findViewById<View>(R.id.btn_profession).setOnClickListener {
            dismiss()
            if (dialogListener != null) dialogListener!!.onDialogProfession()
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(): DialogHeaderDrawerFragment {
            return DialogHeaderDrawerFragment()
        }
    }
}