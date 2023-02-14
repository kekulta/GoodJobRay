package ru.kekulta.goodjobray.screens.notes.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kekulta.goodjobray.R
import ru.kekulta.goodjobray.activity.data.Note
import ru.kekulta.simpleviews.widget.TextCard

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listener: NoteRecyclerClickListener? = null

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener,
        View.OnLongClickListener {
        private val textCard: TextCard = view.findViewById(R.id.textCard)

        init {
            textCard.setOnClickListener(this)
            textCard.setOnLongClickListener(this)
        }

        fun onBind() {
            with(notes[adapterPosition]) {
                textCard.text = text
                textCard.title = title
                textCard.applyChanges()
            }
        }

        override fun onClick(view: View?) {
            listener?.onClick(adapterPosition, notes[adapterPosition])
        }

        override fun onLongClick(view: View?): Boolean {
            return listener?.onLongClick(adapterPosition, notes[adapterPosition]) ?: false
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.view_note, parent, false)
        return NoteViewHolder(v)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount() = notes.size

}