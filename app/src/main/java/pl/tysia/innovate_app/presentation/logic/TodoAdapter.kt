package pl.tysia.innovate_app.presentation.logic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import pl.tysia.innovate_app.R
import pl.tysia.innovate_app.data.model.Task
import java.text.SimpleDateFormat
import java.util.*

class TodoAdapter(private var items : MutableList<Task>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    private var adapterListener : TodoAdapterListener? = null

    inner class TodoViewHolder internal constructor(v: View) : ViewHolder(v){
        var title: TextView = v.findViewById(R.id.title_tv)
        var date: TextView = v.findViewById(R.id.date_tv)
        var description: TextView = v.findViewById(R.id.description_tv)
        var back: View = v.findViewById(R.id.back)
        var deleteButton: ImageButton = v.findViewById(R.id.delete_button)
        var editButton: ImageButton = v.findViewById(R.id.edit_button)
        var completedCheckBox: CheckBox = v.findViewById(R.id.completed_cb)


        init {
            deleteButton.setOnClickListener {
                adapterListener?.onItemDeleteClicked(items[adapterPosition])
            }


            editButton.setOnClickListener {
                adapterListener?.onItemEditClicked(items[adapterPosition])
            }

            completedCheckBox.setOnClickListener {
                items[adapterPosition].completed = completedCheckBox.isChecked
                adapterListener?.onItemCompleteClicked(items[adapterPosition])
            }
        }

    }

    fun setAdapterListener(listener : TodoAdapterListener){
        adapterListener = listener
    }

    fun removeItem(item : Task){
        items.remove(item)
        notifyDataSetChanged()
    }

    fun clear(){
        items.clear()
        notifyDataSetChanged()
    }

    fun addAll(items : List<Task>){
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun add(item : Task){
        items.add(item)
        notifyDataSetChanged()
    }

    fun isEmpty() = items.isEmpty()
    fun size() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_todo, parent, false)

        v.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )

        return TodoViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = items[position]

        if(item.deadlineAt != null){
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val deadlineAt = format.format(item.deadlineAt!!)
            holder.date.text = deadlineAt

            holder.date.visibility = View.VISIBLE
        }else holder.date.visibility = View.GONE


        if (item.priority)
            holder.title.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,
                R.drawable.ic_star, 0)
        else
            holder.title.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)

        holder.description.visibility =
            if(item.description == null) View.GONE
            else View.VISIBLE


        holder.title.text = item.title
        holder.description.text = item.description
        holder.completedCheckBox.isChecked = item.completed
    }

    interface TodoAdapterListener{
        fun onItemDeleteClicked(task: Task)

        fun onItemEditClicked(task: Task)

        fun onItemCompleteClicked(task: Task)
    }

}