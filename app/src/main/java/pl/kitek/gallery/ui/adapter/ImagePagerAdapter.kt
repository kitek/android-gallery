package pl.kitek.gallery.ui.adapter

import android.app.Activity
import android.support.v4.app.ActivityCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewCompat
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import pl.kitek.gallery.data.GalleryItem
import timber.log.Timber

class ImagePagerAdapter(private val activity: Activity,
                        private val items: List<GalleryItem>,
                        private val currentPos: Int) : PagerAdapter() {


    private val views = SparseArray<View?>(items.size)

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val item = items[position]
        val imageView = ImageView(collection.context)
        ViewCompat.setTransitionName(imageView, GalleryItem.transitionName(item.id))
        views.put(position, imageView)

        Picasso.with(collection.context)
                .load(item.fullURL)
                .noFade()
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        if (position == currentPos) {
                            imageView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                                override fun onPreDraw(): Boolean {
                                    imageView.viewTreeObserver.removeOnPreDrawListener(this)
                                    ActivityCompat.startPostponedEnterTransition(activity)
                                    return true
                                }
                            })
                        }
                    }

                    override fun onError() {
                        Timber.d("FAILED: ${item.id} ")
//                        ActivityCompat.startPostponedEnterTransition(activity)
                    }
                })

        collection.addView(imageView)
        return imageView
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        views.removeAt(position)
        collection.removeView(view as View)
    }

    override fun isViewFromObject(view: View?, `object`: Any?) = view === `object`
    override fun getCount() = items.size
    fun getView(position: Int): View? = views.get(position)

}
