package pl.kitek.gallery.ui

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_gallery.*
import pl.kitek.gallery.R

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        ViewCompat.setTransitionName(imageView, intent.getStringExtra(IMG_TRANSITION))
        supportPostponeEnterTransition()

        Picasso.with(this)
                .load(intent.getStringExtra(IMG_URL))
                .noFade()
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        supportStartPostponedEnterTransition()
                    }

                    override fun onError() {
                        supportStartPostponedEnterTransition()
                    }
                })
    }

    companion object {
        const val IMG_URL = "imageURL"
        const val IMG_TRANSITION = "imageTransition"
    }
}
