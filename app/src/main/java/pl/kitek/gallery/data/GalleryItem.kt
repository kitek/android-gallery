package pl.kitek.gallery.data

data class GalleryItem(
        val id: Int,
        val thumbnailURL: String,
        val fullURL: String) {

    companion object {
        fun transitionName(id: Int) = "item_$id"
    }
}
