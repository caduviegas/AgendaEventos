package com.innaval.agendaeventos.presentation.ui.eventlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.innaval.agendaeventos.R
import com.innaval.agendaeventos.domain.model.EventBO
import com.innaval.agendaeventos.presentation.utils.toDayMonthYear
import com.squareup.picasso.Picasso
import java.lang.Math.max


class EventsAdapter(
    private val activity: AppCompatActivity,
    private val events: List<EventBO>,
    private val eventClickListener: (event: EventBO) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (events.isEmpty()) {
            EmptyHolder(inflateAdapter(R.layout.item_list_empty_events, parent))
        } else {
            EventHolder(inflateAdapter(R.layout.item_list_event, parent))
        }
    }

    private fun inflateAdapter(layoutRes: Int, parent: ViewGroup): View {
        return LayoutInflater.from(activity).inflate(layoutRes, parent, false)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is EventHolder) {
            val event = events.get(position)
            holder.bindValues(event, eventClickListener)
        }
    }

    override fun getItemCount(): Int = max(events.size, 1)

    class EmptyHolder(itemView: View) : ViewHolder(itemView)

    class EventHolder(
        itemView: View,
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle),
        var tvDate: TextView = itemView.findViewById(R.id.tvDate),
        var ivImage: ImageView = itemView.findViewById(R.id.ivImage)
    ) : ViewHolder(itemView) {

        fun bindValues(
            event: EventBO,
            userClickListener: (event: EventBO) -> Unit
        ) {
            tvTitle.text = event.title
            tvDate.text = event.date.toDayMonthYear()
            Picasso.get()
                .load(event.imageUrl)
                .fit()
                .placeholder(R.drawable.bg_events)
                .into(ivImage)
            itemView.setOnClickListener { userClickListener.invoke(event) }
        }
    }
}