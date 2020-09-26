package pl.tysia.innovate_app.presentation.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.DialogFragment
import pl.tysia.innovate_app.R


class FiltersDialog : DialogFragment() {
    private var filtersSelectedListener : OnFiltersSelected? = null

    var priority : Boolean? = null
    var completed : Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activity is OnFiltersSelected)
            filtersSelectedListener = activity as OnFiltersSelected

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.filters_dialog, container, false)

        val applyButton = view.findViewById<Button>(R.id.apply_button)

        val priorCB = view.findViewById<CheckBox>(R.id.prior_cb)
        val nonPriorCB = view.findViewById<CheckBox>(R.id.non_prior_cb)

        val completedCB = view.findViewById<CheckBox>(R.id.completed_cb)
        val notCompletedCB = view.findViewById<CheckBox>(R.id.not_completed_cb)

        applyButton.setOnClickListener {
            priority = if (priorCB.isChecked == nonPriorCB.isChecked) null
            else priorCB.isChecked

            completed = if (completedCB.isChecked == notCompletedCB.isChecked) null
            else completedCB.isChecked

            filtersSelectedListener?.filtersSelected()

            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = FiltersDialog()
    }

    interface OnFiltersSelected{
        fun filtersSelected()
    }
}
