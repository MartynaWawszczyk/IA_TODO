package pl.tysia.innovate_app.presentation.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import pl.tysia.innovate_app.R

private const val ARG_TITLE = "pl.tysia.innovate_app.title"
private const val ARG_QUESTION = "pl.tysia.innovate_app.question"


open class YesNoDialog : DialogFragment(), View.OnClickListener {
    var responseListener : ResponseListener? = null

    private var title: String? = null
    private var question: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            title = it.getString(ARG_TITLE)
            question = it.getString(ARG_QUESTION)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.yes_no_dialog, container, false)

        val titleTV = view.findViewById<TextView>(R.id.title_tv)
        val questionTV = view.findViewById<TextView>(R.id.question_tv)
        val yesButton = view.findViewById<Button>(R.id.yes_button)
        val noButton = view.findViewById<Button>(R.id.no_button)

        titleTV.text = title
        questionTV.text = question

        yesButton.setOnClickListener(this)
        noButton.setOnClickListener(this)

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
        fun newInstance(title : String, question : String) = YesNoDialog()
            .apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_QUESTION, question)
            }
        }
    }

    interface ResponseListener{
        fun responseGiven(response : Boolean)
    }

    override fun onClick(p0: View?) {
        responseListener?.responseGiven(p0!!.id == R.id.yes_button)
        dismiss()
    }
}

