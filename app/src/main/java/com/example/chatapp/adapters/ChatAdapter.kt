package com.example.chatapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.model.ChatEvent
import com.example.chatapp.model.DateHeader
import com.example.chatapp.model.Message
import com.puldroid.whatsappclone.utils.formatAsTime
import kotlinx.android.synthetic.main.list_item_date_header.view.*
import kotlinx.android.synthetic.main.list_item_recieve.view.*
import kotlinx.android.synthetic.main.list_item_sent.view.*


class ChatAdapter(private val list: MutableList<ChatEvent>, private val mCurrentUid: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var highFiveClick: ((id: String, status: Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = { layout: Int ->
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
        }
        return when (viewType) {
            TEXT_MESSAGE_RECEIVED -> {
                MessageViewHolder(inflate(R.layout.list_item_recieve))
            }
            TEXT_MESSAGE_SENT -> {
                MessageViewHolder(inflate(R.layout.list_item_sent))
            }
            DATE_HEADER -> {
                DateViewHolder(inflate(R.layout.list_item_date_header))
            }
            else -> MessageViewHolder(inflate(R.layout.list_item_recieve))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is DateHeader -> {
                holder.itemView.textViewDateHeader.text =
                    item.date // We have already formatted date in Message.kt
            }
            is Message -> {
                holder.itemView.apply {
                    when (getItemViewType(position)) {
                        TEXT_MESSAGE_RECEIVED -> {
                            contentRcv.text = item.msg
                            timeRcv.text = item.sentAt.formatAsTime()
                            holder.itemView.messageCardView.setOnClickListener(object :
                                DoubleClickListener() {
                                override fun onDoubleClick(v: View?) {
                                    highFiveClick?.invoke(item.msgId, !item.liked)
                                }
                            })
                            holder.itemView.highFiveImgrec.apply {
                                isVisible = /*position == itemCount - 1 ||*/ item.liked
                                isSelected = item.liked
                                setOnClickListener {
                                    highFiveClick?.invoke(item.msgId, !isSelected)
                                }
                            }

                        }
                        TEXT_MESSAGE_SENT -> {
                            content.text = item.msg
                            time.text = item.sentAt.formatAsTime()
                            holder.itemView.highFiveImg.apply {
                                isVisible = item.liked
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    // This function will tell, at which position which view has to be shown
    // It can also be used to handle exceptions
    override fun getItemViewType(position: Int): Int {
        return when (val event = list[position]) {
            is Message -> {
                if (event.senderId == mCurrentUid) {
                    TEXT_MESSAGE_SENT
                } else {
                    TEXT_MESSAGE_RECEIVED
                }
            }
            is DateHeader -> DATE_HEADER
            else -> UNSUPPORTED
        }
    }

    class DateViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object { // companion object is like static variable of this class
        private const val UNSUPPORTED = -1
        private const val TEXT_MESSAGE_RECEIVED = 0
        private const val TEXT_MESSAGE_SENT = 1
        private const val DATE_HEADER = 2
    }

}

abstract class DoubleClickListener : View.OnClickListener {
    var lastClickTime: Long = 0
    override fun onClick(v: View?) {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            onDoubleClick(v)
            lastClickTime = 0
        }
//        else {
//            onSingleClick(v)
//        }
        lastClickTime = clickTime
    }

    //    abstract fun onSingleClick(v: View?)
    abstract fun onDoubleClick(v: View?)

    companion object {
        private const val DOUBLE_CLICK_TIME_DELTA: Long = 300 //milliseconds
    }
}