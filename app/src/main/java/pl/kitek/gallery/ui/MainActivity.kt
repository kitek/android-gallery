package pl.kitek.gallery.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.Window
import kotlinx.android.synthetic.main.activity_main.*
import pl.kitek.gallery.R
import pl.kitek.gallery.data.DataSource
import pl.kitek.gallery.data.GalleryItem
import pl.kitek.gallery.ui.adapter.ImageGridAdapter


class MainActivity : AppCompatActivity(), ImageGridAdapter.OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imagesRv.setHasFixedSize(true)
        imagesRv.layoutManager = GridLayoutManager(this, 2)
        imagesRv.adapter = ImageGridAdapter(DataSource.ITEMS, this)
        imagesRv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        imagesRv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
    }

    override fun onClick(item: GalleryItem, view: View) {
        val intent = Intent(this, ImageActivity::class.java)
        intent.putExtra(ImageActivity.IMG_TRANSITION, ViewCompat.getTransitionName(view))
        intent.putExtra(ImageActivity.IMG_URL, item.fullURL)

        var bundle: Bundle? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val navigationBar = findViewById(android.R.id.navigationBarBackground)
            val p1 = Pair.create(view, ViewCompat.getTransitionName(view))
            val p2 = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME)
            bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1).toBundle()
        }

        startActivity(intent, bundle)
    }
}
