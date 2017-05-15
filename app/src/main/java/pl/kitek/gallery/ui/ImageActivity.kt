package pl.kitek.gallery.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_gallery.*
import pl.kitek.gallery.R
import pl.kitek.gallery.data.DataSource
import pl.kitek.gallery.ui.MainActivity.Companion.EXTRA_CURRENT_ALBUM_POSITION
import pl.kitek.gallery.ui.MainActivity.Companion.EXTRA_STARTING_ALBUM_POSITION
import pl.kitek.gallery.ui.adapter.ImagePagerAdapter

class ImageActivity : AppCompatActivity() {

    private var mIsReturning: Boolean = false
    private var mStartingPosition: Int = 0
    private var mCurrentPosition: Int = 0

    private var imagePagerAdapter: ImagePagerAdapter? = null
    private val enterElementCallback: SharedElementCallback = object : SharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
            if (mIsReturning) {
                val sharedElement = imagePagerAdapter?.getView(mCurrentPosition)

                if (mStartingPosition != mCurrentPosition) {
                    names.clear()
                    names.add(ViewCompat.getTransitionName(sharedElement))

                    sharedElements.clear()
                    sharedElements.put(ViewCompat.getTransitionName(sharedElement), sharedElement!!)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        ActivityCompat.postponeEnterTransition(this)
        ActivityCompat.setEnterSharedElementCallback(this, enterElementCallback)
        setupToolBar()

        val index = DataSource.ITEMS.indexOfFirst { it.id == intent.getIntExtra(ITEM_ID, 0) }
        mStartingPosition = if (index > 0) index else 0
        if (savedInstanceState == null) {
            mCurrentPosition = mStartingPosition
        } else {
            mCurrentPosition = savedInstanceState.getInt(STATE_CURRENT_PAGE_POSITION)
        }

        imagePagerAdapter = ImagePagerAdapter(this, DataSource.ITEMS, mCurrentPosition)
        viewPager.adapter = imagePagerAdapter
        viewPager.currentItem = mCurrentPosition
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                mCurrentPosition = position
            }
        })


    }

    override fun finishAfterTransition() {
        mIsReturning = true
        val data = Intent()
        data.putExtra(EXTRA_STARTING_ALBUM_POSITION, mStartingPosition)
        data.putExtra(EXTRA_CURRENT_ALBUM_POSITION, mCurrentPosition)
        setResult(Activity.RESULT_OK, data)
        super.finishAfterTransition()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                android.R.id.home -> {
                    supportFinishAfterTransition()
                    return true
                }
                else -> {
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
        }
    }

    companion object {
        const val ITEM_ID = "itemId"

        private const val STATE_CURRENT_PAGE_POSITION = "state_current_page_position"
    }
}
