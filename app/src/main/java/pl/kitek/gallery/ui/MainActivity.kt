package pl.kitek.gallery.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.SharedElementCallback
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.ViewTreeObserver
import kotlinx.android.synthetic.main.activity_main.*
import pl.kitek.gallery.R
import pl.kitek.gallery.data.DataSource
import pl.kitek.gallery.data.GalleryItem
import pl.kitek.gallery.ui.adapter.ImageGridAdapter
import timber.log.Timber


class MainActivity : AppCompatActivity(), ImageGridAdapter.OnItemClickListener {

    private var mTmpReenterState: Bundle? = null

    private val exitElementCallback = object : SharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
            if (mTmpReenterState != null) {
                val startingPosition = mTmpReenterState!!.getInt(EXTRA_STARTING_ALBUM_POSITION)
                val currentPosition = mTmpReenterState!!.getInt(EXTRA_CURRENT_ALBUM_POSITION)


                Timber.d("*** onExit startingPosition: $startingPosition currentPosition: $currentPosition")
                if (startingPosition != currentPosition) {


                    // If startingPosition != currentPosition the user must have swiped to a
                    // different page in the DetailsActivity. We must update the shared element
                    // so that the correct one falls into place.
                    val newTransitionName = GalleryItem.transitionName(DataSource.ITEMS[currentPosition].id)
                    val newSharedElement = imagesRv.findViewWithTag(newTransitionName)
                    if (newSharedElement != null) {
                        names.clear()
                        names.add(newTransitionName)
                        sharedElements.clear()
                        sharedElements.put(newTransitionName, newSharedElement)
                    }
                }
                mTmpReenterState = null
            } else {
                // If mTmpReenterState is null, then the activity is exiting.
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.setExitSharedElementCallback(this, exitElementCallback)

        imagesRv.setHasFixedSize(true)
        imagesRv.layoutManager = GridLayoutManager(this, 2)
        imagesRv.adapter = ImageGridAdapter(DataSource.ITEMS, this)
        imagesRv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        imagesRv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
    }

    override fun onClick(item: GalleryItem, view: View) {
        val intent = Intent(this, ImageActivity::class.java)
        intent.putExtra(ImageActivity.ITEM_ID, item.id)

        var bundle: Bundle? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val p1 = Pair.create(view, ViewCompat.getTransitionName(view))
            bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1).toBundle()
        }

        startActivity(intent, bundle)
    }


    override fun onActivityReenter(resultCode: Int, data: Intent) {
        super.onActivityReenter(resultCode, data)
        mTmpReenterState = Bundle(data.extras)
        mTmpReenterState?.let {
            val startingPosition = it.getInt(EXTRA_STARTING_ALBUM_POSITION)
            val currentPosition = it.getInt(EXTRA_CURRENT_ALBUM_POSITION)
            if (startingPosition != currentPosition) imagesRv.scrollToPosition(currentPosition)
            ActivityCompat.postponeEnterTransition(this)

            imagesRv.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    imagesRv.viewTreeObserver.removeOnPreDrawListener(this)
//                    imagesRv.requestLayout()
                    ActivityCompat.startPostponedEnterTransition(this@MainActivity)
                    return true
                }
            })
        }
    }

    companion object {
        const val EXTRA_STARTING_ALBUM_POSITION = "extra_starting_item_position"
        const val EXTRA_CURRENT_ALBUM_POSITION = "extra_current_item_position"
    }

}
