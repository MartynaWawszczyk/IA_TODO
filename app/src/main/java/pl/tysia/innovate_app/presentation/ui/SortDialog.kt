package pl.tysia.innovate_app.presentation.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import pl.tysia.innovate_app.R
import pl.tysia.innovate_app.data.model.SortingMethod


class SortDialog : DialogFragment(), View.OnClickListener {
    private var sortingSelectedListener : OnSortingSelected? = null

    var sortingMethod = SortingMethod()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activity is OnSortingSelected)
            sortingSelectedListener = activity as OnSortingSelected

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.sorting_dialog, container, false)

        val applyButton = view.findViewById<Button>(R.id.apply_button)

        val sortingGroup = view.findViewById<RadioGroup>(R.id.sorting_group)
        sortingGroup.children.forEach {
            if (it is RadioButton) {
                it.setOnClickListener(this@SortDialog)
                setDrawable(it)
            }
        }

        applyButton.setOnClickListener {
            sortingSelectedListener?.sortingSelected()

            dismiss()

        }

        return view
    }

    override fun onClick(view : View?) {

        sortingMethod.sortingType = when(view?.id){
            R.id.end_date_sort -> SortingMethod.SortingType.DEADLINE
            R.id.complete_date_sort -> SortingMethod.SortingType.COMPLETED_AT
            R.id.create_date_sort -> SortingMethod.SortingType.INSERTED_AT
            R.id.update_date_sort -> SortingMethod.SortingType.UPDATED_AT
            else -> SortingMethod.SortingType.TITLE
        }

        val radioButton = view as RadioButton

        setDrawable(radioButton)

    }

    private fun setDrawable(radioButton: RadioButton){
        val drawable = if (sortingMethod.sortDirection == SortingMethod.SortingDirection.ASCENDING)
            R.drawable.ic_north_east
        else R.drawable.ic_south_east

        radioButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, drawable, 0)

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
        fun newInstance() = SortDialog()
    }

    interface OnSortingSelected{
        fun sortingSelected()
    }


}
