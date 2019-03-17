package kz.ildar.sandbox.ui.main.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_color.view.*
import kz.ildar.sandbox.R
import kz.ildar.sandbox.data.model.ColorModel
import timber.log.Timber

class ColorListAdapter(val listener: (ColorModel) -> Unit) : RecyclerView.Adapter<ColorListAdapter.ViewHolder>() {
    val items = ArrayList<ColorModel>()

    fun setItems(list: List<ColorModel>) {
        Timber.w("setItems called: ${list.size}");
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false), listener)

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ColorListAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    open class ViewHolder(itemView: View, val listener: (ColorModel) -> Unit) : RecyclerView.ViewHolder(itemView) {
        open fun bind(colorModel: ColorModel) {
            itemView.colorView.setBackgroundColor(colorModel.getColor())
            itemView.hexView.text = colorModel.getHexString()
            itemView.setOnClickListener { listener.invoke(colorModel) }
            Timber.w("bound item: ${itemView.hexView.text}");
        }
    }
}