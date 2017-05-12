package pl.kitek.gallery.ui.adapter

import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import pl.kitek.gallery.data.GalleryItem
import pl.kitek.gallery.ui.adapter.ImageGridAdapter.ViewHolder
import pl.kitek.gallery.ui.view.AspectRatioImageView

class ImageGridAdapter(val items: List<GalleryItem>,
                       val onItemClickListener: OnItemClickListener? = null) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        (holder as ViewHolder).bind(items[position], onItemClickListener)
    }

    override fun getItemCount() = items.size

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            AspectRatioImageView(parent.context)
                    .apply { scaleType = ImageView.ScaleType.CENTER_CROP }) {

        fun bind(item: GalleryItem, onItemClickListener: OnItemClickListener?) {
            itemView.setOnClickListener({ onItemClickListener?.onClick(item, it) })
            ViewCompat.setTransitionName(itemView, GalleryItem.transitionName(item.id))
            Picasso.with(itemView.context).load(item.thumbnailURL).into(itemView as ImageView)
        }
    }

    interface OnItemClickListener {
        fun onClick(item: GalleryItem, view: View)
    }
}
