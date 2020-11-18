package com.example.veresiyedefteri.dialog

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.veresiyedefteri.R


class InfoDialog(val clickListener: (String) -> Unit) :
    DialogFragment() {

    companion object {
        const val tag: String = "ConsultantHomeTherapyDialog"
        var window: Window? = null
    }

    override fun onResume() {
        super.onResume()
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setGravity(Gravity.BOTTOM)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_info, container, false)

        view.findViewById<TextView>(R.id.tvInfoDialogEdit).setOnClickListener {
            clickListener("edit")
            this.dialog?.dismiss()
        }

        view.findViewById<TextView>(R.id.tvInfoDialogDelete).setOnClickListener {
            clickListener("delete")
            this.dialog?.dismiss()
        }

        view.findViewById<TextView>(R.id.tvInfoDialogCancel).setOnClickListener {
            clickListener("cancel")
            this.dialog?.dismiss()
        }


        window = dialog?.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)


        return view
    }
}