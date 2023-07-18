package com.frami.utils.files

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.frami.BuildConfig
import com.frami.R
import com.frami.ui.common.videopicker.Constants
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class FilesUtil {
    companion object {
        val canvasFolder = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + Constants.appFolderName + File.separator
                    + Constants.canvasFolder
        )
        val audioFolder = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + Constants.appFolderName + File.separator
                    + Constants.recordedFiles
        )
        val imagesFolder = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + Constants.appFolderName + File.separator
                    + Constants.imagesFolder
        )
        val faviconFolder = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + Constants.appFolderName + File.separator
                    + Constants.faviconFolder
        )
        val profileFolder =
            File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + Constants.appFolderName
            )
        val meaningsImageFolder = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + Constants.appFolderName + File.separator
                    + Constants.meaningImagesFolder
        )
        val quotesFolder = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + Constants.appFolderName + File.separator
                    + Constants.quotesFolder
        )
        val documentsFolder = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + Constants.appFolderName + File.separator
                    + Constants.documentsFolder
        )
        val magDocOnlineFolder = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + Constants.appFolderName + File.separator
                    + Constants.magDocOnlineFolder
        )
        val offlinePagesFolder = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + Constants.appFolderName + File.separator
                    + Constants.offlinePages
        )
        val videoSavedFolder = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + Constants.appFolderName + File.separator
                    + Constants.SavedFolder
        )
        val videoDowloadedFolder = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + File.separator + Constants.appFolderName
        )
        val dowloadedFolder =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path)
        val wallapaperFolder =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path)

        fun getNewCanvasFile(): File {
            createFolders()
            val dateTime = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.ENGLISH).format(Date())
            val file = File(canvasFolder, "$dateTime.png")
            if (!file.exists()) {
                file.createNewFile()
            }
            return file
        }

        fun getNewRecordingFile(): String {
            val dateTime = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.ENGLISH).format(Date())
            val filePath = audioFolder.absolutePath + "/$dateTime.wav"
            val file = File(filePath)
            file.createNewFile()
            return file.absolutePath
        }

        fun bitmapToFile(context: Context, bitmap: Bitmap): File? { // File name like "image.png"
            //create a file to write bitmap data
            var file: File? = null
            return try {
                file = File(
                    "${context.getExternalFilesDir(null)?.absolutePath}/${context.getString(R.string.app_name)}" + File.separator + System.currentTimeMillis() + ".jpeg"
                )
                file.createNewFile()

                //Convert bitmap to byte array
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, bos) // YOU can also save it in JPEG
                val bitmapdata = bos.toByteArray()

                //write the bytes in file
                val fos = FileOutputStream(file)
                fos.write(bitmapdata)
                fos.flush()
                fos.close()
                file
            } catch (e: Exception) {
                e.printStackTrace()
                file // it will return null
            }
        }

        fun getFileNameIfIncrements(filePath: String): String {
            var file = File(filePath)
            try {
                val fileName = file.name
                val name = fileName.substring(0, fileName.lastIndexOf("."))
                val extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length)
                var count = 1
                while (file.exists()) {
                    file = File(file.parent + File.separator + name + "_" + count + "." + extension)
                    count++
                }
                return file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return filePath
        }

        fun createFolders() {
            if (!canvasFolder.exists()) canvasFolder.mkdirs()
            if (!profileFolder.exists()) canvasFolder.mkdirs()
            if (!audioFolder.exists()) audioFolder.mkdirs()
            if (!imagesFolder.exists()) imagesFolder.mkdirs()
            if (!faviconFolder.exists()) faviconFolder.mkdirs()
            if (!quotesFolder.exists()) quotesFolder.mkdirs()
            if (!documentsFolder.exists()) documentsFolder.mkdirs()
            if (!meaningsImageFolder.exists()) meaningsImageFolder.mkdirs()
            if (!videoSavedFolder.exists()) videoSavedFolder.mkdirs()
            else {
                meaningsImageFolder.deleteRecursively()
                meaningsImageFolder.mkdirs()
            }

        }

        fun deleteFile(path: String) {
            try {
                File(path).delete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun getBitmapFromView(view: View): Bitmap {
            try {
                //Define a bitmap with the same size as the view
                val returnedBitmap =
                    Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                //Bind a canvas to it
                val canvas = Canvas(returnedBitmap)
                //Get the view's background
                val bgDrawable = view.background
                if (bgDrawable != null)
                //has background drawable, then draw it on the canvas
                    bgDrawable.draw(canvas)
                else
                //does not have background drawable, then draw white background on the canvas
                    canvas.drawColor(Color.WHITE)
                // draw the view on the canvas
                view.draw(canvas)
                //return the bitmap
                return returnedBitmap
            } catch (e: Exception) {
                e.printStackTrace()
                return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            }
        }

        fun saveViewAndGetBitmap(view: View, word: String): Uri? {
            return try {
                val mContext = view.context
                val bitmap = getBitmapFromView(view)
                val cachePath = File(mContext.cacheDir, Constants.meaningImagesFolder)
                if (!cachePath.exists()) cachePath.mkdirs()
                val filePath = cachePath.toString() + "/mtapp_${UUID.randomUUID()}.jpg"
                val fos = FileOutputStream(filePath)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos)
                fos.close()

                val contentUri: Uri? = FileProvider.getUriForFile(
                    mContext, BuildConfig.APPLICATION_ID + ".provider",
                    File(filePath)
                )
                contentUri
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun saveBitmapAndGetUri(bitmap: Bitmap): Uri? {
            createFolders()
            return try {
                val filePath = meaningsImageFolder.absolutePath + "/mtapp_${UUID.randomUUID()}.jpg"
                val file = File(filePath)
                val fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos)
                fos.close()
                filePath.toUri()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun saveBitmapAndGetPath(bitmap: Bitmap): String {
            createFolders()
            val dateTime = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.ENGLISH).format(Date())
            val filePath = imagesFolder.absolutePath + "/$dateTime${UUID.randomUUID()}.jpg"
            val file = File(filePath)
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            return filePath
        }


        fun saveBookBitmapAndGetPath(bitmap: Bitmap?, context: Context): String {
            val filePath = getBookPdfPosterPath(context)
            val file = File(filePath)
            val fos = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 50, fos)
            fos.flush()
            fos.close()
            return filePath
        }

        fun getWallpaperBitmapPath(context: Context): String {
            val filePath =
                context.getExternalFilesDir(Constants.HOME_SCREEN_WALLPAPER_FILE_NAME)!!.path + "/Wallpaper.jpg"
            return filePath
        }

        fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
            try {
                val width = bm.width
                val height = bm.height
                var finalHeight = newHeight
                var finalWidth = newWidth
                val scaleWidth = newWidth.toFloat() / width
                val scaleHeight = newHeight.toFloat() / height
                // CREATE A MATRIX FOR THE MANIPULATION
                val matrix = Matrix()
                // RESIZE THE BIT MAP
                matrix.postScale(scaleWidth, scaleHeight)

                // "RECREATE" THE NEW BITMAP
                val resizedBitmap: Bitmap = if (width >= height) {
                    return try {
                        var xOffset = (width - newWidth) / 2
                        if (newWidth > width) {
                            xOffset = 0
                            finalWidth = width
                        }
                        var yOffset = (height - newHeight) / 2
                        if (newHeight > height) {
                            yOffset = 0
                            finalHeight = height
                        }
                        Bitmap.createBitmap(bm, xOffset, yOffset, finalWidth, finalHeight)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)
                    }
                } else {
                    Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)
                }
                bm.recycle()
                return resizedBitmap
            } catch (e: Exception) {
                e.printStackTrace()
                return bm
            }
        }

        fun saveBitmapAndGetPathWEBP(bitmap: Bitmap, context: Context): String {
            val filePath =
                context.getExternalFilesDir(Constants.sortVideoPosterImage)!!.path + "/${System.currentTimeMillis()}_${
                    randomString(6)
                }.WEBP"
            val file = File(filePath)
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.WEBP, 20, fos)
            fos.flush()
            fos.close()
            return filePath
        }

        fun saveBookBitmapAndGetPathWEBP(bitmap: Bitmap, context: Context): String {
            val filePath =
                context.getExternalFilesDir(Constants.pdfBookPosterImage)!!.path + "/${System.currentTimeMillis()}_${
                    randomString(6)
                }.WEBP"
            val file = File(filePath)
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.WEBP, 80, fos)
            fos.flush()
            fos.close()
            return filePath
        }

        fun saveProfileBitmapAndGetPath(bitmap: Bitmap, context: Context): String {
            val filePath =
                context.cacheDir.path + "/${System.currentTimeMillis()}_${randomString(6)}.WEBP"
            val file = File(filePath)
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.WEBP, 10, fos)
            fos.flush()
            fos.close()
            return filePath
        }

        fun saveFaviconAndGetPath(bitmap: Bitmap, domainName: String): String {
            createFolders()
            val filePath = faviconFolder.absolutePath + "/$domainName"
            val file = File(filePath)
            if (!file.exists()) {
                val fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
                fos.close()
            }
            return filePath
        }

        // save video poster and get path
        fun getVideoPosterPath(context: Context): String {
            val path = context.getExternalFilesDir(Constants.sortVideoPosterImage)!!.path
            val dateTime = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.ENGLISH).format(Date())
            return path + "/${dateTime}_${randomString(6)}.jpeg"
        }

        // save pdf poster and get path
        fun getBookPdfPosterPath(context: Context): String {
            val path = context.getExternalFilesDir(Constants.pdfBookPosterImage)!!.path
            val dateTime = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.ENGLISH).format(Date())
            return path + "/${dateTime}_${randomString(6)}.jpeg"
        }

        fun randomString(len: Int): String {
            val random = Random()
            val tempStr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            val sb = StringBuilder(len)
            for (i in 0 until len) {
                sb.append(tempStr[random.nextInt(tempStr.length)])
            }
            return sb.toString()
        }

        //        // generate file path of add text on video
//        fun getAddTextToVideoFilePath(context : Context): String {
//            val path = context.getExternalFilesDir(Constants.sortVideoAddTextVideo)!!.path
//            return path + "/${Date().time}_${randomString(6)}.mp4"
//        }
//        fun getAddGIFToVideoFilePath(context : Context): String {
//            val path = context.getExternalFilesDir(Constants.sortVideoGIFVideo)!!.path
//            return path + "/${Date().time}_${randomString(6)}.mp4"
//        }
//        fun getMergeVideoFilePath(context : Context): String {
//            val path = context.getExternalFilesDir(Constants.sortVideoMergeVideo)!!.path
//            return path + "/${Date().time}_${randomString(6)}.mp4"
//        }
//        fun getFinalVideoFilePath(context : Context): String {
//            val path = context.getExternalFilesDir(Constants.sortVideoShared)!!.path
//            return path + "/${Date().time}_${randomString(6)}.mp4"
//        }
        fun copyAssertSamplePdfFileToExternalStorage(resourceName: String, context: Context): File {
            val path = context.getExternalFilesDir(Constants.SampleAssert)!!.path
            val pathSDCard = File(path, resourceName)
            if (!pathSDCard.exists()) {
                pathSDCard.also {
                    if (!it.exists()) {
                        val inputStream = context.assets.open(resourceName)
                        inputStream.toFile(it.path)
                    }
                }
            }
            return File(pathSDCard.path)
        }

        //        fun copyFileToExternalStorage(resourceId: Int, resourceName: String, context: Context): File {
//            val path = context.getExternalFilesDir(Constants.sortVideoAssert)!!.path
//            val pathSDCard = File(path, resourceName)
//            if (!pathSDCard.exists()) {
//                pathSDCard.also {
//                    if (!it.exists()) {
//                        val inputStream = context.resources.openRawResource(resourceId)
//                        inputStream.toFile(it.path)
//                    }
//                }
//            }
//            return File(pathSDCard.path)
//        }
        fun saveBitmapAndGetVideoPosterPath(bitmap: Bitmap?, filePath: String): String {
            val file = File(filePath)
            val fos = FileOutputStream(file)
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 50, fos)
            fos.flush()
            fos.close()
            return filePath
        }

        fun getImagePathOfVideoPosterBitmap(
            context: Context?,
            uri: Uri?,
            frame: Long,
            filePath: String
        ) {
            val options = RequestOptions().frame(frame)
            if (uri != null && context != null) {
                Glide.with(context)
                    .asBitmap()
                    .load(uri)
                    .apply(options)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            saveBitmapAndGetVideoPosterPath(resource, filePath)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) = Unit
                    })
            }
        }

        fun getFile(): File {
            createFolders()
            val dateTime = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.ENGLISH).format(Date())
            val filePath = imagesFolder.absolutePath + "/$dateTime.jpg"
            return File(filePath)
        }

        fun saveProfileAndGetPath(uri: Uri, context: Context): String {
            try {
                createFolders()
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                val filePath = profileFolder.absolutePath + "/${Constants.userProfilePhotoName}"
                val file = File(filePath)
                if (file.exists()) file.delete()
                val fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos)
                fos.close()
                return filePath
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }

        fun close(closeable: Closeable?) {
            try {
                closeable?.close()
            } catch (e: IOException) {
            }
        }

        fun saveProfileFromUrl(url: String): String {
            try {
                createFolders()
                val bitmap = getBitmapFromURL(url)
                val filePath = profileFolder.absolutePath + "/${Constants.userProfilePhotoName}"
                val file = File(filePath)
                if (file.exists()) file.delete()
                val fos = FileOutputStream(file)
                bitmap?.compress(Bitmap.CompressFormat.PNG, 50, fos)
                fos.close()
                return filePath
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }

        fun glideToBitmap(context: Context?, uri: Uri?): Bitmap? {
            var bitmap: Bitmap? = null
            if (uri != null && context != null) {
                Glide.with(context)
                    .asBitmap()
                    .load(uri)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            bitmap = resource
                        }

                        override fun onLoadCleared(placeholder: Drawable?) = Unit
                    })
            }
            return bitmap
        }

        fun getBitmapFromPath(path: String): Bitmap? {
            return try {
                val f = File(path)
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                BitmapFactory.decodeStream(FileInputStream(f), null, options)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }


        fun getBitmapFromURL(src: String): Bitmap? {
            try {
                val url = java.net.URL(src)
                val connection = url
                    .openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                return BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
        }

        fun copyFileToMagDocs(path: String): File {
            createFolders()
            val file = File(path)
            val filePath = documentsFolder.absolutePath + "/" + file.name
            val newFile = File(filePath)
            return file.copyTo(newFile, true)
        }

        fun deleteFavicon(faviconPath: String) {
            try {
                val file = File(faviconPath)
                if (file.exists()) file.delete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun getExternalLocations(context: Context): List<String> {
            val paths = ArrayList<String>()
            for (file in context.getExternalFilesDirs("external")) {
                if (file != null) {
                    val index = file.absolutePath.lastIndexOf("/Android/data")
                    if (index < 0) {
                        // Unexpected external file dir
                    } else {
                        var path = file.absolutePath.substring(0, index)
                        try {
                            path = File(path).canonicalPath
                        } catch (e: IOException) {
                            // Keep non-canonical path.
                        }
                        paths.add(path)
                    }
                }
            }
            if (paths.isEmpty()) paths.add("/storage/sdcard1")
            return paths.toList()
        }


        private fun Context.getFileFromAssets(filename: String): File {
            val file = File(cacheDir, filename)
            val assetFileDescriptor = resources.assets.openFd(filename)
            val buf = ByteArray(assetFileDescriptor.declaredLength.toInt())
            assetFileDescriptor.createInputStream().use { inputStream ->
                val bytesRead = inputStream.read(buf)
                if (bytesRead != buf.size) {
                    throw IOException("Asset read failed")
                }
                FileOutputStream(file).use { outputStream ->
                    outputStream.write(buf, 0, bytesRead)
                }
            }
            return file
        }


        // Capture image using file provider
        fun createImageFile(context: Context): File {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageName = "image_$timestamp"
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val image = File.createTempFile(imageName, ".jpg", storageDir)
            return image
        }


        suspend fun deleteAllOfflinePages() {
            try {
                offlinePagesFolder.deleteRecursively()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // generate file path of comress video
        fun getVideoTrimFilePath(uri: Uri?, context: Context): String {
            val path = context.getExternalFilesDir(Constants.sortTrimVideo)!!.path
            val dateTime = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.ENGLISH).format(Date())
            val fName = "magtapp_video_"
            val newFile = File(
                path + File.separator + fName + "_" + dateTime + "_" + UUID.randomUUID() + "." + getFileExtension(
                    context,
                    uri!!
                )
            )
            return newFile.toString()
        }

        fun getFileExtension(context: Context, uri: Uri): String {
            try {
                val extension: String?
                extension =
                    if (uri.scheme != null && uri.scheme == ContentResolver.SCHEME_CONTENT) {
                        val mime = MimeTypeMap.getSingleton()
                        mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
                    } else MimeTypeMap.getFileExtensionFromUrl(
                        Uri.fromFile(File(uri.path)).toString()
                    )
                return if (extension == null || extension.isEmpty()) ".mp4" else extension
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return "mp4"
        }


        fun getDuration(context: Activity?, videoPath: Uri?): Long {
            try {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(context, videoPath)
                val time =
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: ""
                val timeInMillisec = time.toLong()
                retriever.release()
                return timeInMillisec / 1000
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return 0
        }

        fun formatSeconds(timeInSeconds: Long): String {
            val hours = timeInSeconds / 3600
            val secondsLeft = timeInSeconds - hours * 3600
            val minutes = secondsLeft / 60
            val seconds = secondsLeft - minutes * 60
            var formattedTime = ""
            if (hours < 10 && hours != 0L) {
                formattedTime += "0"
                formattedTime += "$hours:"
            }
            if (minutes < 10) formattedTime += "0"
            formattedTime += "$minutes:"
            if (seconds < 10) formattedTime += "0"
            formattedTime += seconds
            return formattedTime
        }

        fun getFormatedDuration(millis: Long): String {
            return if (TimeUnit.MILLISECONDS.toHours(millis) > 0) {
                String.format(
                    "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(
                            millis
                        )
                    ),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            millis
                        )
                    )
                )
            } else {
                String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(
                            millis
                        )
                    ),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            millis
                        )
                    )
                )
            }
        }

        fun getFileSizeLabel(fileSize: Long): String {
            val size = fileSize.toDouble() / 1000.0 // Get size and convert bytes into KB.
            val decimalFormat = DecimalFormat("#.##")
            return if (size >= 1024) {
                val twoDigitsF: Double = java.lang.Double.valueOf(decimalFormat.format(size / 1024))
                "$twoDigitsF MB"
            } else {
                val twoDigitsF: Double = java.lang.Double.valueOf(decimalFormat.format(size))
                "$twoDigitsF KB"
            }
        }

        fun InputStream.toFile(path: String) {
            File(path).outputStream().use { this.copyTo(it) }
        }

        fun getFileMetaData(uri: Uri?, context: Context): String {
            val cursor: Cursor? =
                uri?.let { context.contentResolver.query(it, null, null, null, null, null) }
            cursor?.use {
                if (it.moveToFirst()) {
                    val displayName: String =
                        it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    return displayName
                }
            }
            return ""
        }

        fun getFileSizeMetaData(uri: Uri?, context: Context): Long {
            val cursor: Cursor? =
                uri?.let { context.contentResolver.query(it, null, null, null, null, null) }
            cursor?.use {
                if (it.moveToFirst()) {
                    val fileSize: Long =
                        it.getLong(it.getColumnIndex(OpenableColumns.SIZE))
                    return fileSize
                }
            }
            return 0
        }

        // get file path from URI
        fun getPathFromURI(context: Context, uri: Uri): String? {
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            var selection: String? = null
            var selectionArgs: Array<String>? = null
            if (isKitKat) {
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]
                    val fullPath = getPathFromExtSD(split)
                    return if (fullPath !== "") {
                        fullPath
                    } else {
                        null
                    }
                }
                if (isDownloadsDocument(uri)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val id: String
                        var cursor: Cursor? = null
                        try {
                            cursor = context.contentResolver.query(
                                uri,
                                arrayOf(MediaStore.MediaColumns.DISPLAY_NAME),
                                null,
                                null,
                                null
                            )
                            if (cursor != null && cursor.moveToFirst()) {
                                val fileName = cursor.getString(0)
                                val path = Environment.getExternalStorageDirectory()
                                    .toString() + "/Download/" + fileName
                                if (!TextUtils.isEmpty(path)) {
                                    return path
                                }
                            }
                        } finally {
                            cursor?.close()
                        }
                        id = DocumentsContract.getDocumentId(uri)
                        if (!TextUtils.isEmpty(id)) {
                            if (id.startsWith("raw:")) {
                                return id.replaceFirst("raw:".toRegex(), "")
                            }
                            val contentUriPrefixesToTry = arrayOf(
                                "content://downloads/public_downloads",
                                "content://downloads/my_downloads"
                            )
                            for (contentUriPrefix in contentUriPrefixesToTry) {
                                return try {
                                    val contentUri = ContentUris.withAppendedId(
                                        Uri.parse(contentUriPrefix),
                                        java.lang.Long.valueOf(id)
                                    )
                                    getDataColumn(context, contentUri, null, null)
                                } catch (e: NumberFormatException) {
                                    //In Android 8 and Android P the id is not a number
                                    uri.path!!.replaceFirst("^/document/raw:".toRegex(), "")
                                        .replaceFirst("^raw:".toRegex(), "")
                                }
                            }
                        }
                    } else {
                        val id = DocumentsContract.getDocumentId(uri)
                        var contentUri: Uri? = null
                        if (id.startsWith("raw:")) {
                            return id.replaceFirst("raw:".toRegex(), "")
                        }
                        try {
                            contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"),
                                java.lang.Long.valueOf(id)
                            )
                        } catch (e: NumberFormatException) {
                            e.printStackTrace()
                        }
                        if (contentUri != null) {
                            return getDataColumn(context, contentUri, null, null)
                        }
                    }
                }
                if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    selection = "_id=?"
                    selectionArgs = arrayOf(split[1])
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
                if (isGoogleDriveUri(uri)) {
                    return getDriveFilePath(context, uri)
                }
                if (isWhatsAppFile(uri)) {
                    return getFilePathForWhatsApp(context, uri)
                }
                if ("content".equals(uri.scheme, ignoreCase = true)) {
                    if (isGooglePhotosUri(uri)) {
                        return uri.lastPathSegment
                    }
                    if (isGoogleDriveUri(uri)) {
                        return getDriveFilePath(context, uri)
                    }
                    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                        // return getFilePathFromURI(context,uri);
                        copyFileToInternalStorage(context, uri, "userfiles")
                        // return getRealPathFromURI(context,uri);
                    } else {
                        getDataColumn(context, uri, null, null)
                    }
                }
                if ("file".equals(uri.scheme, ignoreCase = true)) {
                    return uri.path
                }
            } else {
                if (isWhatsAppFile(uri)) {
                    return getFilePathForWhatsApp(context, uri)
                }
                if ("content".equals(uri.scheme, ignoreCase = true)) {
                    val projection = arrayOf(
                        MediaStore.Images.Media.DATA
                    )
                    var cursor: Cursor? = null
                    try {
                        cursor = context.contentResolver
                            .query(uri, projection, selection, selectionArgs, null)
                        val column_index =
                            cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        if (cursor.moveToFirst()) {
                            return cursor.getString(column_index)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            return null
        }


        private fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        private fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        private fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        private fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }

        fun isWhatsAppFile(uri: Uri): Boolean {
            return "com.whatsapp.provider.media" == uri.authority
        }

        private fun isGoogleDriveUri(uri: Uri): Boolean {
            return "com.google.android.apps.docs.storage" == uri.authority || "com.google.android.apps.docs.storage.legacy" == uri.authority
        }

        private fun getDataColumn(
            context: Context,
            uri: Uri?,
            selection: String?,
            selectionArgs: Array<String>?
        ): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)
            try {
                cursor = context.contentResolver.query(
                    uri!!, projection,
                    selection, selectionArgs, null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }

        private fun getPathFromExtSD(pathData: Array<String>): String {
            val type = pathData[0]
            val relativePath = "/" + pathData[1]
            var fullPath = ""
            if ("primary".equals(type, ignoreCase = true)) {
                fullPath = Environment.getExternalStorageDirectory().toString() + relativePath
                if (fileExists(fullPath)) {
                    return fullPath
                }
            }
            // Environment.isExternalStorageRemovable() is `true` for external and internal storage
            // so we cannot relay on it.
            //
            // instead, for each possible path, check if file exists
            // we'll start with secondary storage as this could be our (physically) removable sd card
            fullPath = System.getenv("SECONDARY_STORAGE") + relativePath
            if (fileExists(fullPath)) {
                return fullPath
            }
            fullPath = System.getenv("EXTERNAL_STORAGE") + relativePath
            return if (fileExists(fullPath)) {
                fullPath
            } else fullPath
        }

        private fun getDriveFilePath(context: Context, uri: Uri): String {
            val returnCursor = context.contentResolver.query(uri, null, null, null, null)
            /*
             * Get the column indexes of the data in the Cursor,
             *     * move to the first row in the Cursor, get the data,
             *     * and display it.
             * */
            val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
            returnCursor.moveToFirst()
            val name = returnCursor.getString(nameIndex)
            val size = java.lang.Long.toString(returnCursor.getLong(sizeIndex))
            val file = File(context.cacheDir, name)
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(file)
                var read = 0
                val maxBufferSize = 1 * 1024 * 1024
                val bytesAvailable = inputStream!!.available()

                //int bufferSize = 1024;
                val bufferSize = Math.min(bytesAvailable, maxBufferSize)
                val buffers = ByteArray(bufferSize)
                while (inputStream.read(buffers).also { read = it } != -1) {
                    outputStream.write(buffers, 0, read)
                }
                Log.e("File Size", "Size " + file.length())
                inputStream.close()
                outputStream.close()
                Log.e("File Path", "Path " + file.path)
                Log.e("File Size", "Size " + file.length())
            } catch (e: Exception) {
                e.message?.let { Log.e("Exception", it) }
            }
            return file.path
        }

        /***
         * Used for Android Q+
         * @param uri
         * @param newDirName if you want to create a directory, you can set this variable
         * @return
         */
        fun copyFileToInternalStorage(context: Context, uri: Uri, newDirName: String): String {
            val returnCursor = context.contentResolver.query(
                uri, arrayOf(
                    OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE
                ), null, null, null
            )


            /*
             * Get the column indexes of the data in the Cursor,
             *     * move to the first row in the Cursor, get the data,
             *     * and display it.
             * */
            val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
            returnCursor.moveToFirst()
            val name = returnCursor.getString(nameIndex)
            val size = java.lang.Long.toString(returnCursor.getLong(sizeIndex))
            val output: File
            output = if (newDirName != "") {
                val dir = File(context.filesDir.toString() + "/" + newDirName)
                if (!dir.exists()) {
                    dir.mkdir()
                }
                File(context.filesDir.toString() + "/" + newDirName + "/" + name)
            } else {
                File(context.filesDir.toString() + "/" + name)
            }
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(output)
                var read = 0
                val bufferSize = 1024
                val buffers = ByteArray(bufferSize)
                while (inputStream!!.read(buffers).also { read = it } != -1) {
                    outputStream.write(buffers, 0, read)
                }
                inputStream.close()
                outputStream.close()
            } catch (e: Exception) {
                e.message?.let { Log.e("Exception", it) }
            }
            return output.path
        }

        fun moveFile(inputPath: String, inputFile: String, outputPath: String, context: Context) {
            var `in`: InputStream? = null
            var out: OutputStream? = null
            try {

                //create output directory if it doesn't exist
                val dir = File(outputPath)
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                `in` = FileInputStream(inputPath + inputFile)
                out = FileOutputStream(outputPath + inputFile)
                val buffer = ByteArray(1024)
                var read: Int
                while (`in`.read(buffer).also { read = it } != -1) {
                    out.write(buffer, 0, read)
                }
                `in`.close()
                `in` = null

                // write the output file
                out.flush()
                out.close()
                out = null

                // delete the original file
                File(inputPath + inputFile).delete()

                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(outputPath),
                    null
                ) { path, uri ->
                    Log.i("ExternalStorage", "Scanned $path:")
                    Log.i("ExternalStorage", "-> uri=$uri")
                }
            } catch (fnfe1: FileNotFoundException) {
                fnfe1.message?.let { Log.e("tag", it) }
            } catch (e: java.lang.Exception) {
                e.message?.let { Log.e("tag:::", it) }
            }
        }

        private fun getFilePathForWhatsApp(context: Context, uri: Uri): String {
            return copyFileToInternalStorage(context, uri, "whatsapp")
        }

        private fun fileExists(filePath: String): Boolean {
            val file = File(filePath)
            return file.exists()
        }

        fun getVideoCompressFilePath(context: Context, mainPath: String): File {
            val inputFile = File(mainPath)
            val path = context.getExternalFilesDir(Constants.sortCompressVideo)!!.path

            val videoFileName =
                "${System.currentTimeMillis()}_${inputFile.nameWithoutExtension}.mp4"
            return File(path + File.separator + videoFileName)
        }

        // generate file path audio Crop
        fun getAudioCropFilePath(context: Context): String {
            val path = context.getExternalFilesDir(Constants.sortVideoAudioFile)!!.path
            val newFile =
                File(path + File.separator + "/${System.currentTimeMillis()}_${randomString(6)}" + ".mp3")
            return newFile.absolutePath.toString()
        }

        // generate file path of video audio merge
        fun getVideoAudioMergeFilePath(mainPath: String, context: Context): String {
            val inputFile = File(mainPath)
            val path = context.getExternalFilesDir(Constants.sortVideoAudioMergeFile)!!.path
            val videoFileName = "${
                System.currentTimeMillis().toString() + "_" + inputFile.nameWithoutExtension
            }.mp4"
            val file = File(path + File.separator + videoFileName)
            if (file.exists()) {
                file.delete()
            }
            return file.absolutePath

        }

        // generate file path of video without music
        fun getVideoWithoutMusicFilePath(mainPath: String, context: Context): String {
            val inputFile = File(mainPath)
            val path =
                context.getExternalFilesDir(Constants.sortVideoAudioMergeFile + "/noAudio")!!.path
            val videoFileName = "${inputFile.nameWithoutExtension}.mp4"
            val file = File(path + File.separator + videoFileName)
            if (file.exists()) {
                file.delete()
            }
            return file.absolutePath

        }

        fun getVideoVolumeFilePath(mainPath: String, context: Context): String {
            val inputFile = File(mainPath)
            val path =
                context.getExternalFilesDir(Constants.sortVideoAudioMergeFile + "/volume")!!.path
            val videoFileName = "${
                System.currentTimeMillis().toString() + "_" + inputFile.nameWithoutExtension
            }.mp4"
            val file = File(path + File.separator + videoFileName)
            if (file.exists()) {
                file.delete()
            }
            return file.absolutePath

        }

        // save file to Movies or download
        fun saveVideoFile(context: Context, filePath: String?): File? {
            filePath?.let {
                val videoFile = File(filePath)
                val videoFileName = "${System.currentTimeMillis()}_${UUID.randomUUID()}.mp4"
                if (Build.VERSION.SDK_INT >= 30) {
                    val folderName = Environment.DIRECTORY_DOWNLOADS

                    val values = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, videoFileName)
                        put(MediaStore.Images.Media.MIME_TYPE, "video/mp4")
                        put(MediaStore.Images.Media.RELATIVE_PATH, folderName)
                        put(MediaStore.Images.Media.IS_PENDING, 1)
                    }

                    val collection =
                        MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                    val fileUri = context.contentResolver.insert(collection, values)

                    fileUri?.let {
                        context.contentResolver.openFileDescriptor(fileUri, "rw")
                            .use { descriptor ->
                                descriptor?.let {
                                    FileOutputStream(descriptor.fileDescriptor).use { out ->
                                        FileInputStream(videoFile).use { inputStream ->
                                            val buf = ByteArray(4096)
                                            while (true) {
                                                val sz = inputStream.read(buf)
                                                if (sz <= 0) break
                                                out.write(buf, 0, sz)
                                            }
                                        }
                                    }
                                }
                            }

                        values.clear()
                        values.put(MediaStore.Video.Media.IS_PENDING, 0)
                        context.contentResolver.update(fileUri, values, null, null)

                        return File(getPathFromURI(context, fileUri))
                    }
                } else {
                    val downloadsPath =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val desFile = File(downloadsPath, videoFileName)

                    if (desFile.exists())
                        desFile.delete()

                    try {
                        desFile.createNewFile()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    return desFile
                }
            }
            return null
        }

        // share video from exoplayer cache
//        fun renameShortVideo(context: Context,videoLink : String) : String{
//            val cacheSpan: CacheSpan? = MagtappApplication.simpleCache?.getCachedSpans(videoLink)?.pollFirst()
//            val cachedFile = cacheSpan?.file
//            return if (cacheSpan?.isCached == true && cachedFile?.exists() == true) {
//                val from = File(cachedFile.parent, cachedFile.name)
//                val to = File(cachedFile.parent, cachedFile.name+".mp4")
//                val renamed = from.renameTo(to)
//                if (renamed) {
//                    to.absolutePath
//                }else {
//                    context.toastS("you can't share this post")
//                    ""
//                }
//            }else{
//                context.toastS("Please wait video is not loaded")
//                ""
//            }
//        }

        fun getBookPath(context: Context, fileExt: String): String {
            val path =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path).path
//        val path = context.getExternalFilesDir("PDFBook")!!.path
            val dateTime = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.ENGLISH).format(Date())
            return path + "/${dateTime}_${randomString(6)}." + fileExt
        }

        fun getCurrrentDateWithId(id: String): String {
            val date = SimpleDateFormat("ddMMyyyy", Locale.ENGLISH).format(Date())
            return "${id}_${date}"
        }

        fun getRemoteBookKey(
            bookType: String,
            valueType: String
        ): String { // valueType : download = download count, time = 1st download time
            return "RemoteBook_${bookType}_$valueType"
        }

        fun getFileNameFromURL(url: String?): String? {
            if (url == null) {
                return ""
            }
            try {
                val resource = URL(url)
                val host: String = resource.getHost()
                if (host.isNotEmpty() && url.endsWith(host)) {
                    return ""
                }
            } catch (e: MalformedURLException) {
                return ""
            }
            val startIndex = url.lastIndexOf('/') + 1
            val length = url.length

            // find end index for ?
            var lastQMPos = url.lastIndexOf('?')
            if (lastQMPos == -1) {
                lastQMPos = length
            }

            // find end index for #
            var lastHashPos = url.lastIndexOf('#')
            if (lastHashPos == -1) {
                lastHashPos = length
            }

            // calculate the end index
            val endIndex = Math.min(lastQMPos, lastHashPos)
            return url.substring(startIndex, endIndex)
        }

        //Make sure to call this function on a worker thread, else it will block main thread
        @RequiresApi(Build.VERSION_CODES.Q)
        fun saveImageInQ(bitmap: Bitmap, application: Activity): Uri? {
            val filename = "IMG_${System.currentTimeMillis()}.jpg"
            var fos: OutputStream? = null
            var imageUri: Uri? = null
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.Video.Media.IS_PENDING, 1)
            }

            //use application context to get contentResolver
            val contentResolver = application.contentResolver

            contentResolver.also { resolver ->
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }

            fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 70, it) }

            contentValues.clear()
            contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
            imageUri?.let { contentResolver.update(it, contentValues, null, null) }

            return imageUri
        }
    }

}