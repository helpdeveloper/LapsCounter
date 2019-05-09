package br.com.helpdev.lapscounter.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.helpdev.lapscounter.databinding.ListActivityItemBinding
import br.com.helpdev.lapscounter.model.entity.ActivityEntity

class ListActivityAdapter(private val onClickItem: (ActivityEntity) -> Unit) :
    ListAdapter<ActivityEntity, ListActivityAdapter.ItemHolder>(ListActivityDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(ListActivityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = getItem(position)
        holder.apply {
            bind(item, onClickItem)
            itemView.tag = item.id
        }
    }

    class ItemHolder(private val binding: ListActivityItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entity: ActivityEntity, onClickItem: (ActivityEntity) -> Unit) {
            binding.apply {
                activityEntity = entity
                onClickListener = View.OnClickListener { onClickItem(entity) }
                executePendingBindings()
            }
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

