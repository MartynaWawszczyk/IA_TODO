package pl.tysia.innovate_app.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import pl.tysia.innovate_app.data.model.APIResponse
import pl.tysia.innovate_app.R
import pl.tysia.innovate_app.data.model.Task
import pl.tysia.innovate_app.presentation.logic.RecyclerMarginDecorator
import pl.tysia.innovate_app.presentation.logic.TodoAdapter
import java.util.*
import kotlin.collections.ArrayList


private const val YES_NO_DIALOG = "pl.tysia.innovate_app.new_task_dialog"
private const val FILTER_DIALOG = "pl.tysia.innovate_app.filter_dialog"
private const val SORT_DIALOG = "pl.tysia.innovate_app.sort_dialog"
private const val TASKS_BATCH_SIZE = 10

class MainActivity :
    AppCompatActivity(), TodoAdapter.TodoAdapterListener,
    FiltersDialog.OnFiltersSelected,
    SortDialog.OnSortingSelected {

    private lateinit var adapter : TodoAdapter
    private lateinit var viewModel: TodoViewModel

    private var filtersDialog: FiltersDialog = FiltersDialog.newInstance()
    private var sortDialog: SortDialog =
        SortDialog.newInstance()

    private var lastPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter =
            TodoAdapter(ArrayList())
        adapter.setAdapterListener(this)

        viewModel = TodoViewModel()

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tasks_recycler.layoutManager = linearLayoutManager

        tasks_recycler.addItemDecoration(RecyclerMarginDecorator())


        tasks_recycler.adapter = adapter

        task_name_search.afterTextChanged {
            clearAdapter()
            getTasks()
        }

        task_name_et.afterTextChanged {
            create_button.isClickable = !it.isBlank()
        }

        tasks_recycler.setOnScrollChangeListener { _, _, _, _, _ ->
            if (!tasks_recycler.canScrollVertically(RecyclerView.LAYOUT_DIRECTION_RTL)
                && adapter.size() >= TASKS_BATCH_SIZE
            ) {
                getTasks()
                lastPage++
            }
        }


        viewModel.taskResult.observe(this@MainActivity, Observer{
            if (it is APIResponse.Create) {
                Toast.makeText(this@MainActivity, getString(R.string.task_added), Toast.LENGTH_SHORT).show()
                clearAdapter()
                getTasks()
            }
            if (it is APIResponse.Delete)
                Toast.makeText(this@MainActivity, getString(R.string.task_deleted), Toast.LENGTH_SHORT).show()
            if (it is APIResponse.GetTasks) {
                adapter.addAll(it.results)
            }
        })

        viewModel.error.observe(this@MainActivity, Observer{
            Toast.makeText(this@MainActivity, getString(R.string.error_occurred), Toast.LENGTH_SHORT).show()
        })

    }

    private fun clearAdapter(){
        adapter.clear()
        lastPage = 1
    }

    override fun onResume() {
        super.onResume()

        clearAdapter()
        getTasks()
        lastPage++
    }

    fun onFilterClick(view: View){
        filtersDialog.show(supportFragmentManager,
            FILTER_DIALOG
        )
    }

    fun onSortClick(view: View){
        sortDialog.show(supportFragmentManager,
            SORT_DIALOG
        )
    }

    fun onNewTaskClicked(view: View){
        val name = task_name_et.text.toString()

        val currentTime = Calendar.getInstance().time
        val newID = "$currentTime-$name"

        viewModel.createTask(newID,name)

        task_name_et.text.clear()
    }


    override fun onItemDeleteClicked(task: Task) {
        val dialog =
            YesNoDialog.newInstance(
                getString(R.string.deleting_title),
                "Usunąć zadanie \"${task.title}\"?"
            )
        dialog.responseListener = object :
            YesNoDialog.ResponseListener {
            override fun responseGiven(response: Boolean) {
                if (response) {
                    viewModel.deleteTask(task.id)
                    adapter.removeItem(task)
                }
            }
        }
        dialog.show(supportFragmentManager,
            YES_NO_DIALOG
        )

    }

    override fun onItemEditClicked(task: Task) {
        val intent = Intent(this, EditTaskActivity::class.java)
        intent.putExtra(Task.TASK_EXTRA, task)
        startActivity(intent)
    }

    override fun onItemCompleteClicked(task: Task) {
        viewModel.updateTask(id = task.id, completed = task.completed)
    }

    override fun filtersSelected() {
        clearAdapter()
        getTasks()
    }

    override fun sortingSelected() {
        clearAdapter()
        getTasks()
    }

    private fun getTasks(){
        val title =
            if (task_name_search.text.isNullOrBlank()) null
            else task_name_search.text.toString()

        viewModel.getTasks(
            title = title,
            completed = filtersDialog.completed,
            priority = filtersDialog.priority,
            orderBy = sortDialog.sortingMethod.toString(),
            page = lastPage,
            perPage = TASKS_BATCH_SIZE
        )
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}