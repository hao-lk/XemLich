package com.example.ac13c.xemlch.ui.main

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.ac13c.xemlch.R
import com.example.ac13c.xemlch.local.Alarm

/**
 * Created by ac13c on 1/15/2018.
 */
class CalendarEventAdapter(private var eventList: List<Alarm>) : RecyclerView.Adapter<CalendarEventAdapter.ViewHolder>() {

    lateinit var onItemClick: OnItemClick

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.imgDelE?.setOnClickListener({ v ->
            onItemClick.onImage(eventList[position])
        })
        holder?.tvTitle?.text = eventList[position].title
        holder?.tvDay?.text = "${eventList[position].day}/${eventList[position].month}/${eventList[position].year}"
        holder?.tvTime?.text = "${eventList[position].hour} : ${eventList[position].minute}"
        holder?.tvTitle?.setOnClickListener({ view ->
            onItemClick.onItemClickDetail(eventList[position])
        })

    }

    fun clickItem(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_event, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    fun setList(eventList: List<Alarm>) {
        this.eventList = eventList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView
        val tvTime: TextView
        val tvDay: TextView
        val imgDelE: ImageView

        init {
            tvTitle = itemView.findViewById(R.id.tvTitleEvent)
            imgDelE = itemView.findViewById(R.id.imgEventDel)
            tvDay = itemView.findViewById(R.id.tvDayEvent)
            tvTime = itemView.findViewById(R.id.tvTimeEvent)
        }
    }

    interface OnItemClick {
        fun onImage(alarm: Alarm) {}
        fun onItemClickDetail(alarm: Alarm) {}
    }
}