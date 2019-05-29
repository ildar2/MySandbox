package kz.ildar.sandbox.ui.main.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_color.view.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.ui.main.color.ColorMutable

class ColorListAdapter(private val listener: (ColorMutable) -> Unit) : RecyclerView.Adapter<ColorListAdapter.ViewHolder>() {
    private val items = ArrayList<ColorMutable>()

    fun setItems(list: List<ColorMutable>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false), listener)

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    open class ViewHolder(itemView: View, private val listener: (ColorMutable) -> Unit) : RecyclerView.ViewHolder(itemView) {
        open fun bind(color: ColorMutable) {
            itemView.colorView.setBackgroundColor(color.getColor())
            itemView.hexView.text = color.getHexString()
            itemView.nameView.text = color.name
            itemView.setOnClickListener { listener.invoke(color) }
        }
    }
}