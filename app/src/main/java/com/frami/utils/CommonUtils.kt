package com.frami.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.*
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.bumptech.glide.request.RequestOptions
import com.frami.BuildConfig
import com.frami.FramiApp
import com.frami.R
import com.frami.data.local.pref.AppPreferencesHelper
import com.frami.data.model.home.ActivityTypes
import com.frami.utils.extensions.loadImage
import com.frami.utils.widget.TouchImageView
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.math.RoundingMode
import java.net.InetAddress
import java.net.NetworkInterface
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


object CommonUtils {
    fun isStatusTrue(status: String, orderStatus: String?): Boolean {
        return status.equals(orderStatus, ignoreCase = true)
    }

    /**
     * Compares two version strings.
     *
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").<br></br>
     * <br></br>
     * [Original source](https://gist.github.com/anthonymonori/d9d462f2defcfd7ae1d4#gistcomment-2322555)
     *
     * @param ver1 a string of ordinal numbers separated by decimal points.
     * @param ver2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if ver1 is _numerically_ less than ver2.
     * The result is a positive integer if ver1 is _numerically_ greater than ver2.
     * The result is zero if the strings are _numerically_ equal.
     */
    fun compareVersion(ver1: String, ver2: String): Int {
        val vals1 = ver1.replaceFirst("-.*".toRegex(), "").split(".").toMutableList()
        val vals2 = ver2.replaceFirst("-.*".toRegex(), "").split(".").toMutableList()
        while (vals1.size != vals2.size) {
            if (vals1.size < vals2.size) {
                vals1.add("0")
            } else {
                vals2.add("0")
            }
        }
        var i = 0
        while (i < vals1.size && i < vals2.size && vals1[i] == vals2[i]) {
            i++
        }
        return if (i < vals1.size && i < vals2.size) {
            val diff = vals1[i].toInt().compareTo(vals2[i].toInt())
            Integer.signum(diff)
        } else {
            Integer.signum(vals1.size - vals2.size)
        }
    }

    fun capitalize(capString: String): String {
        val capBuffer = StringBuffer()
        val capMatcher: Matcher =
            Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString)
        while (capMatcher.find()) {
            capMatcher.appendReplacement(
                capBuffer,
                capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase()
            )
        }
        return capMatcher.appendTail(capBuffer).toString()
    }

    fun roundOffDecimal(number: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(number).toDouble()
    }

    fun getIPAddress(useIPv4: Boolean): String {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> =
                    Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(
                                    0,
                                    delim
                                ).toUpperCase()
                            }
                        }
                    }
                }
            }
        } catch (ignored: java.lang.Exception) {
        } // for now eat exceptions
        return ""
    }

    fun textAsBitmap(
        typeface: Typeface,
        text: String?,
        textSize: Float,
        width: Int,
        textColor: Int,
        bgColor: Int,
    ): Bitmap {

        val backgroundPaint = Paint()
        backgroundPaint.isAntiAlias = true
        backgroundPaint.style = Paint.Style.FILL
        backgroundPaint.color = bgColor

        val textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.textSize = textSize
        textPaint.style = Paint.Style.FILL
        textPaint.color = textColor
        textPaint.isFakeBoldText = true
        textPaint.typeface = typeface

        val image: Bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawRect(RectF(0F, 0F, width.toFloat(), width.toFloat()), backgroundPaint)

        val r = Rect()
        canvas.getClipBounds(r)
        val cHeight = r.height()
        val cWidth = r.width()
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.getTextBounds(text, 0, text!!.length, r)
        val xPos = cWidth / 2f - r.width() / 2f - r.left
        val yPos = cHeight / 2f + r.height() / 2f - r.bottom
        canvas.drawText(text.toString(), xPos, yPos, textPaint)
        return image
    }

    fun isFile(path: String?): Boolean {
        return File(path).isFile
    }

    fun log(message: String?) {
        if (BuildConfig.DEBUG) if (message != null) {
            Log.e("Frami", message.toString())
        } else {
            Log.e("Frami", "null")
        }
    }


    fun openAppInGooglePlay(context: Context) {
        val appPackageName = context.packageName
        try {
            val uri = when (BuildConfig.ENVIRONMENT) {
                AppConstants.APP_ENVIRONMENT.PRODUCTION -> Uri.parse("market://details?id=$appPackageName")
                else -> Uri.parse("https://appdistribution.firebase.google.com/testerapps/1:397860790181:android:384ff6101b63c5e24e924c/releases/2v4mij2vj7t3o")
            }
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    uri
                )
            )
        } catch (e: ActivityNotFoundException) { // if there is no Google Play on device
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    fun openUrl(mActivity: Activity, url: String?) {
        log("url>>>> $url")
        try {
            if (!TextUtils.isEmpty(url)) {
                var tempURL = url
                if (url?.contains("http") == false) {
                    tempURL = "http://$tempURL"
                }
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(tempURL)
                mActivity.startActivity(i)
            } else {
                log("URL is BLANK")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun shareContent(mActivity: Activity, shareText: String) {
        if (shareText.isEmpty()) {
            return
        }
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
//        sharingIntent.type = "image/*"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "" + shareText)
        mActivity.startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    fun shareContentWithImage(mActivity: Activity, shareText: String, imageUri: Uri? = null) {
        if (shareText.isEmpty()) {
            return
        }
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        if (imageUri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            shareIntent.type = "image/*"
        } else {
            shareIntent.type = "text/html"
        }
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        mActivity.startActivity(Intent.createChooser(shareIntent, "Share via"))
//        val sharingIntent = Intent(Intent.ACTION_SEND)
//        sharingIntent.type = "text/html"
//        sharingIntent.putExtra(Intent.EXTRA_TEXT, "" + shareText)
//        mActivity.startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    fun isValidEmail(emailId: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(emailId).matches()
    }

    fun isValidDomain(url: String?): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    fun isValidPassword(password: String?): Boolean {
        password?.let {
            val passwordPattern =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(password) != null
        } ?: return false
    }

    @SuppressLint("CheckResult")
    fun getGlideOptions(placeholder: Int): RequestOptions {
        val requestOptions = RequestOptions()
        requestOptions.placeholder(placeholder)
        requestOptions.error(placeholder)
        return requestOptions
    }

//    fun bitmapDescriptorFromVector(requireContext: Context?): BitmapDescriptor? {
//        val background = ContextCompat.getDrawable(
//            requireContext!!, R.drawable.ic_map_marker_with_border)
//        background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
//        //        Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_pin_location);
////        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
//        val bitmap = Bitmap.createBitmap(background.intrinsicWidth,
//            background.intrinsicHeight,
//            Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        background.draw(canvas)
//        //        vectorDrawable.draw(canvas);
//        return BitmapDescriptorFactory.fromBitmap(bitmap)
//    }

    fun getDeviceInfo(activity: Activity): String {
        return try {
            AppPreferencesHelper(activity, AppConstants.PREF_NAME).getDeviceInfo()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getOsVersionAndCode(): String {
        val builder = StringBuilder()
        builder.append("v" + Build.VERSION.RELEASE)
        val fields = VERSION_CODES::class.java.fields
        for (i in fields.indices) {
            val field = fields[i]
            val fieldName = field.name
            var fieldValue = -1
            try {
                fieldValue = field.getInt(Any())
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" ").append(fieldName)
                builder.append(" sdk: ").append(fieldValue)
                break
            }
        }
        return builder.toString()
    }

    fun getDeviceId(): String {
        return Settings.Secure.getString(
            FramiApp.appContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun getDeviceType(): String {
        return AppConstants.DEVICE_TYPE_ANDROID
    }

    fun getAppVersion(): String {
        return BuildConfig.VERSION_NAME
    }

    fun getDeviceName(): String? {
        return Build.MODEL
    }

    fun getYouTubeId(youTubeUrl: String?): String? {
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(youTubeUrl)
        return if (matcher.find()) {
            matcher.group()
        } else {
            ""
        }
    }

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
                    val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
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
        selectionArgs: Array<String>?,
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

    private fun getFilePathForWhatsApp(context: Context, uri: Uri): String {
        return copyFileToInternalStorage(context, uri, "whatsapp")
    }

    private fun fileExists(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists()
    }

    fun size(size: Int): String {
        var hrSize = ""
        val m = size / 1024.0
        val dec = DecimalFormat("0.00")
        hrSize =
//            if (m > 1) {
            dec.format(m) + " MB"
//        }
//        else {
//            dec.format(size.toLong()) + " KB"
//        }
        return hrSize
    }

    fun showZoomImage(activity: Activity, imageUrl: String?) {
        try {
            val inflater =
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val zoomLayout: View = inflater.inflate(R.layout.image_zoom_layout, null)
            val touchImageView: TouchImageView =
                zoomLayout.findViewById(R.id.ivZoomImage) as TouchImageView
            touchImageView.loadImage(imageUrl)
            val ibClose = zoomLayout.findViewById(R.id.ibClose) as ImageButton
            val dialog = Dialog(activity, android.R.style.Theme_Translucent)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(zoomLayout)
            ibClose.setOnClickListener { dialog.dismiss() }
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun bitmapDescriptorFromVector(requireContext: Context?): BitmapDescriptor {
        val background = ContextCompat.getDrawable(requireContext!!, R.drawable.ic_location_point)
        background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
        //        Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_pin_location);
//        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        val bitmap = Bitmap.createBitmap(
            background.intrinsicWidth,
            background.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        //        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun getThumbnailFromVideoUri(context: Context, uri: Uri): Uri? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()

        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val picturePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()

        val bitmap = picturePath?.let {
            ThumbnailUtils.createVideoThumbnail(
                it,
                MediaStore.Video.Thumbnails.MICRO_KIND
            )
        }
        log("getImageUri>>>>> ${bitmap?.let { getImageUri(context, it) }}")
        return bitmap?.let { getImageUri(context, it) }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "", null)
        return Uri.parse(path)
    }

    fun getSelectedActivityTypeName(data: List<ActivityTypes>, isShowAll: Boolean = false): String {
        if (data.isEmpty()) {
            return ""
        }
        if (data[0].name == AppConstants.KEYS.All && data[0].isSelected) {
            return data[0].name
        }
        val selectedList: List<ActivityTypes> =
            if (isShowAll) data else data.filter { s -> s.isSelected }
        val stringList: MutableList<String> = ArrayList()
        selectedList.forEach { stringList.add(it.name) }
        return when {
            selectedList.size == 1 -> selectedList[0].name
            selectedList.size > 1 -> selectedList[0].name + " AND +" + (selectedList.size - 1)
            else -> ""
        }
    }

     fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}