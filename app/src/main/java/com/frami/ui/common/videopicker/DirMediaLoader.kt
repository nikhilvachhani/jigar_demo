package com.frami.ui.common.videopicker

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns
import java.util.*

object DirMediaLoader {

    val COUNT = "count"
    val DIR_PROJECTION = arrayOf(
        FileColumns._ID,
        FileColumns.SIZE,
        FileColumns.BUCKET_ID,
        FileColumns.BUCKET_DISPLAY_NAME,
        FileColumns.MEDIA_TYPE
    )

     val COLUMN_ID = 0
     val COLUMN_SIZE = 1
     val COLUMN_BUCKET_ID = 2
     val COLUMN_BUCKET_DISPLAY_NAME = 3
     val COLUMN_MEDIA_TYPE = 4
     val COLUMN_COUNT = 5
     fun getDirProjection(): Array<String> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return DIR_PROJECTION
        }
        val projection: MutableList<String> = ArrayList()
        Collections.addAll(projection, *DIR_PROJECTION)
        projection.add("COUNT(*) as " + COUNT)
        return projection.toTypedArray()
    }
    fun getDirs(data: Cursor): List<Dir> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return getDirsQ(data)
        }
        val dirs: MutableList<Dir> = ArrayList()
        if (data.moveToFirst()) do {
            val mediaDir = Dir()
            mediaDir.id = data.getInt(COLUMN_BUCKET_ID).toLong()
            mediaDir.name = data.getString(COLUMN_BUCKET_DISPLAY_NAME)
            mediaDir.count = data.getInt(COLUMN_COUNT)
            val preview = getPreview(data)
            mediaDir.preview = preview.toString()
            dirs.add(mediaDir)
        } while (data.moveToNext())

        return dirs
    }

    private fun getDirsQ(data: Cursor): List<Dir> {
        val dirs = HashMap<Long, Dir?>()
        if (data.moveToFirst()) do {
            val dirId = data.getInt(COLUMN_BUCKET_ID).toLong()
            var mediaDir = dirs[dirId]
            if (mediaDir == null) {
                mediaDir = Dir()
                mediaDir.id = dirId
                mediaDir.name = data.getString(COLUMN_BUCKET_DISPLAY_NAME)
                val preview = getPreview(data)
                mediaDir.preview = preview.toString()
                mediaDir.count = 1
                dirs[dirId] = mediaDir
            } else {
                mediaDir.count = (mediaDir.count + 1)
            }
        } while (data.moveToNext())
        return ArrayList<Dir>(dirs.values)
    }

    private fun getPreview(cursor: Cursor): Uri? {
        val id = cursor.getLong(COLUMN_ID)
        val mediaType = cursor.getInt(COLUMN_MEDIA_TYPE)
        if (id <= 0) return null
        val contentUri: Uri = if (mediaType == FileColumns.MEDIA_TYPE_IMAGE) {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        } else if (mediaType == FileColumns.MEDIA_TYPE_VIDEO) {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        } else {
            return null
        }
        return ContentUris.withAppendedId(contentUri, id)
    }

    fun getVideoUri(path: String) = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,path.toLong())
    fun getImageUri(path: String) = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,path.toLong())
    fun getAudioUri(path: String) = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,path.toLong())


}