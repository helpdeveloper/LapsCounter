package br.com.helpdev.lapscounter.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.helpdev.lapscounter.R
import br.com.helpdev.lapscounter.model.entity.ActivityEntity

class ListActivityAdapter(val onClickItem: (ActivityEntity) -> Unit) : ListAdapter<ActivityEntity, ListActivityAdapter.ItemHolder>(ListActivityDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_activity_item, parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position), onClickItem)
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.name)
        private val layoutClickable: View = itemView.findViewById(R.id.layout_clickable)
        fun bind(activityEntity: ActivityEntity, onClickItem: (ActivityEntity) -> Unit) {
            textView.text = activityEntity.name
            layoutClickable.setOnClickListener { onClickItem(activityEntity) }
        }
    }
}

private class ListActivityDiff : DiffUtil.ItemCallback<ActivityEntity>() {
    override fun areItemsTheSame(oldItem: ActivityEntity, newItem: ActivityEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ActivityEntity, newItem: ActivityEntity): Boolean {
        return oldItem.name == newItem.name && oldItem.dateStarted?.time == newItem.dateStarted?.time
    }

}

