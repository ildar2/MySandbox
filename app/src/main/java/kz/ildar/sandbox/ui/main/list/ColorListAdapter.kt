package kz.ildar.sandbox.ui.main.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kz.ildar.sandbox.R
import kz.ildar.sandbox.utils.DisplayAdapter
import kz.ildar.sandbox.utils.DisplayItem
import kz.ildar.sandbox.utils.DisplayViewHolder

class ColorListAdapter : DisplayAdapter() {

    @Suppress("UNCHECKED_CAST")
    override fun createViewHolder(view: View, viewType: Int) = when (viewType) {
        R.layout.item_color -> ColorDisplay.ViewHolder(view)
        else -> throw RuntimeException("Unknown viewType: $viewType. You should modify createViewHolder")
    } as DisplayViewHolder<DisplayItem>
}

data class ColorDisplay(
    val name: String,
    val hexString: String,
    val color: Int,
    val click: (ColorDisplay) -> Unit
) : DisplayItem(R.layout.item_color) {
    class ViewHolder(itemView: View) : DisplayViewHolder<ColorDisplay>(itemView) {
        override fun bind(item: ColorDisplay) {
            itemView.findViewById<ImageView>(R.id.colorView).setBackgroundColor(item.color)
            itemView.findViewById<TextView>(R.id.hexView).text = item.hexString
            itemView.findViewById<TextView>(R.id.nameView).text = item.name
            itemView.setOnClickListener { item.click.invoke(item) }
        }
    }
}