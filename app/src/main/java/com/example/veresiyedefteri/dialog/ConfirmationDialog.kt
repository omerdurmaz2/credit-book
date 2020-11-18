package com.example.veresiyedefteri.dialog

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.veresiyedefteri.R


class ConfirmationDialog(val clickListener: (String) -> Unit) :
    DialogFragment() {

    companion object {
        const val tag: String = "ConfirmationDialog"
        var window: Window? = null
    }

    override fun onResume() {
        super.onResume()
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setGravity(Gravity.CENTER)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_confirmation, container, false)

        view.findViewById<TextView>(R.id.tvDialogConfirmationYes).setOnClickListener {
            clickListener("yes")
            this.dialog?.dismiss()
        }

        view.findViewById<TextView>(R.id.tvDialogConfirmationNo).setOnClickListener {
            this.dialog?.dismiss()
        }
        window = dialog?.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)


        return view
    }
}