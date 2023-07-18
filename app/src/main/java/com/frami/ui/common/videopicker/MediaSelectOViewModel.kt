package com.frami.ui.common.videopicker

import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns
import android.provider.MediaStore.MediaColumns
import androidx.core.content.ContentResolverCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.*

class MediaSelectOViewModel(
    private val context: Application
) : ViewModel() {
    private val uiScope = CoroutineScope(Dispatchers.IO)

    private var projection: Array<String>
    private var sortOrder: String? = null
    private var selection: String? = null
    private var uri: Uri? = null
    init{
        val selectionBuilder = StringBuilder(200)
        selectionBuilder.append("(")
//        if (selectionBuilder[selectionBuilder.length - 1] != '(') selectionBuilder.append(" or ")
//        selectionBuilder.append(FileColumns.MEDIA_TYPE).append(" = ").append(FileColumns.MEDIA_TYPE_IMAGE)

        if (selectionBuilder[selectionBuilder.length - 1] != '(') selectionBuilder.append(" or ")
        selectionBuilder.append(FileColumns.MEDIA_TYPE).append(" = ").append(FileColumns.MEDIA_TYPE_VIDEO)
        selectionBuilder.append(" and ").append(FileColumns.SIZE).append(" < ")//.append(Constants.FileSize)
        selectionBuilder.append(") and ")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            selectionBuilder.append(MediaColumns.BUCKET_ID).append(" IS NOT NULL) GROUP BY (").append(MediaColumns.BUCKET_ID)
        } else {
            selectionBuilder.append(MediaColumns.BUCKET_ID).append(" IS NOT NULL")
        }

        this.projection = DirMediaLoader.getDirProjection()
        this.sortOrder = MediaColumns.DATE_MODIFIED + " DESC"
        this.selection = selectionBuilder.toString()
        uri = MediaStore.Files.getContentUri("external")
    }

    // get folder of video and images
    fun getMediaDirs(list: (List<Dir>) -> Unit) {
        uiScope.launch {
            flow {
                emit(fetchDirs())
            }.catch {
                it.printStackTrace()
            }.collect {
                list(it)
            }
        }
    }
    fun fetchDirs(): List<Dir> {
        val data = ContentResolverCompat.query(context.contentResolver, uri, projection,selection, null,"$sortOrder", null)
        return DirMediaLoader.getDirs(data)
    }

    // get video and images
    fun getMediaFromGallery(
        bucketID: Long?,
        isVideo: Boolean,
        list: (List<MediaGalleryModel>) -> Unit
    ) {
        uiScope.launch {
            flow {
                if (isVideo){
                    emit(fetchVideo(bucketID))
                }else{
                    emit(fetchImages(bucketID))
                }
            }.catch {
                it.printStackTrace()
            }.collect {
                list(it)
            }
        }
    }
    fun fetchVideo(bucketID: Long?) : List<MediaGalleryModel>{
        val cursor: Cursor? = getVideoCursor(bucketID)
        if (cursor != null ) {
            val galleryImageUrls = ArrayList<MediaGalleryModel>(cursor.count)
                while (cursor.moveToNext()){
                    val dataColumnIndex = cursor.getColumnIndex(MediaColumns._ID) //get column index
                    var duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION))
                    val size = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.SIZE))
                    val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.MIME_TYPE))

                    var durationFormated = ""
                    if (!duration.isNullOrBlank()){
                        durationFormated = FilesUtil.getFormatedDuration(duration.toLong())
                    }
                    if (!durationFormated.isNullOrEmpty() && mimeType=="video/mp4"){
                        galleryImageUrls.add(MediaGalleryModel(DirMediaLoader.getVideoUri(cursor.getString(dataColumnIndex)).toString(), "", duration.toLong(), durationFormated,size.toLong(), MediaConstant.MediaTypeVideo))
                    }
                }
            cursor.close()
            return galleryImageUrls
        }
        return emptyList()
    }
    fun fetchImages(bucketID: Long?) : List<MediaGalleryModel>{
        val cursor: Cursor? = getImagesCursor(bucketID)
        if (cursor != null ) {
            val galleryImageUrls = ArrayList<MediaGalleryModel>(cursor.count)
            while (cursor.moveToNext()){
                val dataColumnIndex = cursor.getColumnIndex(MediaColumns._ID) //get column index
                galleryImageUrls.add(MediaGalleryModel(DirMediaLoader.getImageUri(cursor.getString(dataColumnIndex)).toString(), "",0,"",0, MediaConstant.MediaTypeImage))
            }
            cursor.close()
            return galleryImageUrls
        }
        return emptyList()
    }
    private fun getVideoCursor(bucketID: Long?): Cursor? {
        val externalUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val columns = arrayOf(MediaColumns._ID, MediaColumns.DATE_MODIFIED,MediaStore.Video.VideoColumns.DURATION,MediaStore.Video.VideoColumns.SIZE,MediaStore.Video.VideoColumns.MIME_TYPE)
        val selectionBuilder = StringBuilder(200)
        selectionBuilder.append(FileColumns.SIZE).append(" < ").append(Constants.FileSize)
        selectionBuilder.append(" and ").append(FileColumns.DURATION).append(" > ").append(Constants.VideoFileDuration)
        // selectionBuilder.append(" and ").append(FileColumns.MIME_TYPE).append(" = ").append("video/mp4") // Very Slow after adding this Filter.
        if (bucketID == null){
            return context.contentResolver.query(externalUri, columns, selectionBuilder.toString(), null, MediaColumns.DATE_MODIFIED + " DESC")//get all data in Cursor by sorting in DESC order
        }else{
            selectionBuilder.append(" and ").append(MediaStore.Video.Media.BUCKET_ID).append(" = ").append(bucketID)
            return context.contentResolver.query(externalUri, columns, selectionBuilder.toString(), null, MediaColumns.DATE_MODIFIED + " DESC")//get all data in Cursor by sorting in DESC order
        }

    }
    private fun getImagesCursor(bucketID: Long?): Cursor? {
        val externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val columns = arrayOf(MediaColumns._ID, MediaColumns.DATE_MODIFIED,MediaStore.Video.VideoColumns.SIZE)
        val selectionBuilder = StringBuilder(200)
        selectionBuilder.append(FileColumns.SIZE).append(" < ").append(Constants.FileSize)
        if (bucketID == null){
            return context.contentResolver.query(externalUri, columns, selectionBuilder.toString(), null, MediaColumns.DATE_MODIFIED + " DESC")//get all data in Cursor by sorting in DESC order
        }else{
            selectionBuilder.append(" and ").append(MediaStore.Video.Media.BUCKET_ID).append(" = ").append(bucketID)
            return context.contentResolver.query(externalUri, columns, selectionBuilder.toString(), null, MediaColumns.DATE_MODIFIED + " DESC")//get all data in Cursor by sorting in DESC order
        }
    }



}