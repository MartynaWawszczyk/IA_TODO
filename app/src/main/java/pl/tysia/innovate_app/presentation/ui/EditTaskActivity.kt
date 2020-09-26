package pl.tysia.innovate_app.presentation.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_edit_task.*
import pl.tysia.innovate_app.data.model.APIResponse
import pl.tysia.innovate_app.R
import pl.tysia.innovate_app.data.model.Task
import java.text.SimpleDateFormat
import java.util.*


class EditTaskActivity : AppCompatActivity() {
    private lateinit var task: Task

    private lateinit var viewModel: TodoViewModel

    private var deadlineDate : Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault())

    private val dateSetListener = DatePickerDialog.OnDateSetListener{
            _: DatePicker, year: Int, month: Int, day: Int ->
        run {
            deadlineDate.set(year, month, day)

            displayDate()

            val hour = deadlineDate.get(Calendar.HOUR_OF_DAY)
            val minute = deadlineDate.get(Calendar.MINUTE)

            TimePickerDialog(
                this,
                timeSetListener,
                hour,
                minute,
                DateFormat.is24HourFormat(this)
            ).show()
        }
    }


    private val timeSetListener = TimePickerDialog.OnTimeSetListener{
            _: TimePicker, hourOfDay: Int, minute: Int ->
        run {
            deadlineDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
            deadlineDate.set(Calendar.MINUTE, minute)

            displayDate()

        }
    }


    private fun displayDate(){
        val format = SimpleDateFormat("dd-MM-yyyy hh:mm", getCurrentLocale())
        val date = format.format(deadlineDate.time)

        deadline_et.setText(date)
    }

    private fun getCurrentLocale(): Locale? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales.get(0)
        } else {
            resources.configuration.locale
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        task = intent.getSerializableExtra(Task.TASK_EXTRA) as Task

        val year = deadlineDate.get(Calendar.YEAR)
        val month = deadlineDate.get(Calendar.MONTH)
        val day = deadlineDate.get(Calendar.DAY_OF_MONTH)

        deadline_et.setOnClickListener {  DatePickerDialog(this, dateSetListener, year, month, day).show() }

        viewModel = TodoViewModel()

        viewModel.taskResult.observe(this@EditTaskActivity, Observer{
            if (it is APIResponse.Update) Toast.makeText(this@EditTaskActivity, "Zaktualizowano zadanie", Toast.LENGTH_SHORT).show()
        })

        displayTask()

    }

    private fun displayTask(){
        if (task.deadlineAt != null){
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val deadlineAt = format.format(task.deadlineAt!!)
            deadline_et.setText(deadlineAt)
        }

        title_et.setText(task.title)
        description_et.setText(task.description)
        completed_cb.isChecked = task.completed
        priority_cb.isChecked = task.priority
    }

    fun onSaveClicked(view : View){
        val title = title_et.text.toString()
        val description = description_et.text.toString()
        var deadlineAt : String? = null
        val completed = completed_cb.isChecked
        val priority = priority_cb.isChecked

        if (!deadline_et.text.isNullOrBlank()){
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", getCurrentLocale())
            deadlineAt = format.format(deadlineDate.time)

        }

        viewModel.updateTask(task.id, title, description, completed, priority, deadlineAt)
        finish()
    }
}