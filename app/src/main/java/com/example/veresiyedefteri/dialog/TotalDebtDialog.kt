package com.example.veresiyedefteri.dialog

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.veresiyedefteri.R


class TotalDebtDialog(val clickListener: (String) -> Unit) :
    DialogFragment() {

    companion object {
        const val tag: String = "TotalDebpDialog"
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
        val view = inflater.inflate(R.layout.dialog_total_debt, container, false)

        view.findViewById<TextView>(R.id.tvDialogTotalDebtYes).setOnClickListener {
            clickListener("yes")
            this.dialog?.dismiss()
        }

        view.findViewById<TextView>(R.id.tvDialogTotalDebtNo).setOnClickListener {
            this.dialog?.dismiss()
        }
        window = dialog?.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)


        return view
    }
}