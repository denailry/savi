package com.simpleapp.savi.history

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.simpleapp.savi.model.record.Record
import com.simpleapp.savi.R
import com.simpleapp.savi.lib.PublicMethods
import kotlinx.android.synthetic.main.adapter_history.view.*

interface HistoryItemClickListener {
    fun onClick(id: Long)
}

class HistoryAdapter(val items: ArrayList<Record>,
                     val context: Context,
                     val listener: HistoryItemClickListener) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvActivityType = itemView.tvActivityType
        val tvActivityName = itemView.tvActivityName
        val tvActivityNote = itemView.tvActivityNote
        val tvActivityValue = itemView.tvActivityValue
        val viewRoot = itemView.viewRoot
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_history, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(items.get(position)) {
            holder.tvActivityName.text = name
            holder.tvActivityNote.text = notes
            holder.tvActivityValue.text = PublicMethods.moneyFormat(value.toString())
            if (type == Record.INCOME) {
                holder.tvActivityValue.setTextColor(context.resources.getColor(R.color.income))
                holder.tvActivityType.text = context.resources.getString(R.string.activity_type_income)
                holder.tvActivityType.setBackgroundColor(context.resources.getColor(R.color.income))
            } else {
                holder.tvActivityValue.setTextColor(context.resources.getColor(R.color.expense))
                holder.tvActivityType.text = context.resources.getString(R.string.activity_type_income)
                holder.tvActivityType.setBackgroundColor(context.resources.getColor(R.color.expense))
            }
            holder.tvActivityType.text = if (type == Record.INCOME) {"IN"} else {"EX"}
            holder.viewRoot.setOnClickListener{_ ->
                listener.onClick(id)
            }
        }
    }
}