package com.frami.ui.common

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.frami.R
import com.frami.ui.common.ImagePickerActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.yalantis.ucrop.UCrop
import dagger.android.AndroidInjection
import java.io.File

class ImagePickerActivity : AppCompatActivity() {
    private var lockAspectRatio = false
    private var setBitmapMaxWidthHeight = false
    private var ASPECT_RATIO_X = 16
    private var ASPECT_RATIO_Y = 9
    private var bitmapMaxWidth = 1000
    private var bitmapMaxHeight = 1000
    private var IMAGE_COMPRESSION = 50
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_image_picker)
        val intent = intent
        if (intent == null) {
            Toast.makeText(
                applicationContext,
                getString(R.string.toast_image_intent_null),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        ASPECT_RATIO_X = intent.getIntExtra(INTENT_ASPECT_RATIO_X, ASPECT_RATIO_X)
        ASPECT_RATIO_Y = intent.getIntExtra(INTENT_ASPECT_RATIO_Y, ASPECT_RATIO_Y)
        IMAGE_COMPRESSION = intent.getIntExtra(INTENT_IMAGE_COMPRESSION_QUALITY, IMAGE_COMPRESSION)
        lockAspectRatio = intent.getBooleanExtra(INTENT_LOCK_ASPECT_RATIO, false)
        setBitmapMaxWidthHeight = intent.getBooleanExtra(INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, false)
        bitmapMaxWidth = intent.getIntExtra(INTENT_BITMAP_MAX_WIDTH, bitmapMaxWidth)
        bitmapMaxHeight = intent.getIntExtra(INTENT_BITMAP_MAX_HEIGHT, bitmapMaxHeight)
        val requestCode = intent.getIntExtra(INTENT_IMAGE_PICKER_OPTION, -1)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            takeCameraImage()
        } else {
            chooseImageFromGallery()
        }
    }

    private fun takeCameraImage() {
        Dexter.withContext(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        fileName = System.currentTimeMillis().toString() + ".jpg"
                        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        takePictureIntent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            getCacheImagePath(fileName)
                        )
                        if (takePictureIntent.resolveActivity(packageManager) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun chooseImageFromGallery() {
        Dexter.withContext(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        val pickPhoto = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> if (resultCode == RESULT_OK) {
                cropImage(getCacheImagePath(fileName))
            } else {
                setResultCancelled()
            }
            REQUEST_GALLERY_IMAGE -> if (resultCode == RESULT_OK) {
                val imageUri = data!!.data
                cropImage(imageUri)
            } else {
                setResultCancelled()
            }
            UCrop.REQUEST_CROP -> if (resultCode == RESULT_OK) {
                handleUCropResult(data)
            } else {
                setResultCancelled()
            }
            UCrop.RESULT_ERROR -> {
                val cropError = UCrop.getError(data!!)
                setResultCancelled()
            }
            else -> setResultCancelled()
        }
    }

    private fun cropImage(sourceUri: Uri?) {
        val destinationUri = Uri.fromFile(
            File(
                cacheDir, queryName(
                    contentResolver, sourceUri
                )
            )
        )
        val options = UCrop.Options()
        options.setCompressionQuality(IMAGE_COMPRESSION)

        // applying UI theme
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorAccent))
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor))
        options.setActiveControlsWidgetColor(ContextCompat.getColor(this, R.color.themeColor))
        if (lockAspectRatio) options.withAspectRatio(
            ASPECT_RATIO_X.toFloat(),
            ASPECT_RATIO_Y.toFloat()
        )
        if (setBitmapMaxWidthHeight) options.withMaxResultSize(bitmapMaxWidth, bitmapMaxHeight)
        UCrop.of(sourceUri!!, destinationUri)
            .withOptions(options)
            .start(this)
    }

    private fun handleUCropResult(data: Intent?) {
        if (data == null) {
            setResultCancelled()
            return
        }
        val resultUri = UCrop.getOutput(data)
        setResultOk(resultUri)
    }

    private fun setResultOk(imagePath: Uri?) {
        /*      Intent intent = new Intent();
        intent.putExtra("path", imagePath);
        setResult(Activity.RESULT_OK, intent);*/
        if (mPickerResultListener != null) {
            mPickerResultListener!!.onImageAvailable(imagePath)
        }
        finish()
    }

    private fun setResultCancelled() {
        /*      Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);*/
        if (mPickerResultListener != null) {
            mPickerResultListener!!.onError()
        }
        finish()
    }

    private fun getCacheImagePath(fileName: String?): Uri {
        val path = File(externalCacheDir, "camera")
        if (!path.exists()) path.mkdirs()
        val image = File(path, fileName)
        return FileProvider.getUriForFile(this@ImagePickerActivity, "$packageName.provider", image)
    }

    interface PickerResultListener {
        fun onImageAvailable(imagePath: Uri?)
        fun onError()
    }

    companion object {
        const val INTENT_IMAGE_PICKER_OPTION = "image_picker_option"
        const val INTENT_ASPECT_RATIO_X = "aspect_ratio_x"
        const val INTENT_ASPECT_RATIO_Y = "aspect_ratio_Y"
        const val INTENT_LOCK_ASPECT_RATIO = "lock_aspect_ratio"
        const val INTENT_IMAGE_COMPRESSION_QUALITY = "compression_quality"
        const val INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT = "set_bitmap_max_width_height"
        const val INTENT_BITMAP_MAX_WIDTH = "max_width"
        const val INTENT_BITMAP_MAX_HEIGHT = "max_height"
        const val REQUEST_IMAGE_CAPTURE = 0
        const val REQUEST_GALLERY_IMAGE = 1
        const val REQUEST_IMAGE = 100
        private val TAG = ImagePickerActivity::class.java.simpleName
        var fileName: String? = null
        var mPickerResultListener: PickerResultListener? = null

        //    private static void showImagePickerGalleryOptions(final Activity context) {
        //        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.CustomBottomSheetDialogTheme);
        //        bottomSheetDialog.setContentView(R.layout.dialog_twiiter_post);
        //        bottomSheetDialog.findViewById(R.id.btnCamera)
        //                .setOnClickListener(new View.OnClickListener() {
        //                    @Override
        //                    public void onClick(View v) {
        //                        bottomSheetDialog.dismiss();
        //                        mPickerResultListener.onImageAvailable(null);
        //                    }
        //                });
        //        bottomSheetDialog.findViewById(R.id.btnGallery)
        //                .setOnClickListener(new View.OnClickListener() {
        //                    @Override
        //                    public void onClick(View v) {
        //                        launchGalleryIntent(context);
        //                        bottomSheetDialog.dismiss();
        //                    }
        //                });
        //        bottomSheetDialog.show();
        //    }
        private fun showImagePickerOptions(context: Activity) {
            val bottomSheetDialog = BottomSheetDialog(context, R.style.CustomBottomSheetDialogTheme)
            bottomSheetDialog.setContentView(R.layout.dialog_image_picker)
            val btnCamera: MaterialButton =
                bottomSheetDialog.findViewById(R.id.btnCamera)!!
            val btnGallery: MaterialButton =
                bottomSheetDialog.findViewById(R.id.btnGallery)!!
            btnCamera.setOnClickListener(View.OnClickListener {
                launchCameraIntent(context)
                bottomSheetDialog.dismiss()
            })
            btnGallery.setOnClickListener(View.OnClickListener {
                launchGalleryIntent(context)
                bottomSheetDialog.dismiss()
            })
            bottomSheetDialog.show()
        }

        private fun launchCameraIntent(activity: Activity) {
            val intent = Intent(activity, ImagePickerActivity::class.java)
            intent.putExtra(INTENT_IMAGE_PICKER_OPTION, REQUEST_IMAGE_CAPTURE)

            // setting aspect ratio
            intent.putExtra(INTENT_LOCK_ASPECT_RATIO, false)
            intent.putExtra(INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
            intent.putExtra(INTENT_ASPECT_RATIO_Y, 1)

            // setting maximum bitmap width and height
            intent.putExtra(INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
            intent.putExtra(INTENT_BITMAP_MAX_WIDTH, 1000)
            intent.putExtra(INTENT_BITMAP_MAX_HEIGHT, 1000)
            activity.startActivityForResult(intent, REQUEST_IMAGE)
        }

        private fun launchGalleryIntent(activity: Activity) {
            val intent = Intent(activity, ImagePickerActivity::class.java)
            intent.putExtra(INTENT_IMAGE_PICKER_OPTION, REQUEST_GALLERY_IMAGE)

            // setting aspect ratio
            intent.putExtra(INTENT_LOCK_ASPECT_RATIO, false)
            intent.putExtra(INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
            intent.putExtra(INTENT_ASPECT_RATIO_Y, 1)
            activity.startActivityForResult(intent, REQUEST_IMAGE)
        }

        private fun queryName(resolver: ContentResolver, uri: Uri?): String {
            val returnCursor = resolver.query(uri!!, null, null, null, null)!!
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            val name = returnCursor.getString(nameIndex)
            returnCursor.close()
            return name
        }

        /**
         * Calling this will delete the images from cache directory
         * useful to clear some memory
         */
        fun clearCache(context: Context) {
            val path = File(context.externalCacheDir, "camera")
            if (path.exists() && path.isDirectory) {
                for (child in path.listFiles()) {
                    child.delete()
                }
            }
        }

        // my button click function
        fun showImageChooser(context: Activity, pickerResultListener: PickerResultListener?) {
            mPickerResultListener = pickerResultListener
            Dexter.withContext(context)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions(context)
                        } else {
                            displayNeverAskAgainDialog(context)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        }

        // my button click function
        //    public static void showTwitterPostChooser(final Activity context, PickerResultListener pickerResultListener) {
        //        mPickerResultListener = pickerResultListener;
        //        Dexter.withActivity(context)
        //                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        //                .withListener(new MultiplePermissionsListener() {
        //                    @Override
        //                    public void onPermissionsChecked(MultiplePermissionsReport report) {
        //                        if (report.areAllPermissionsGranted()) {
        //                            showImagePickerGalleryOptions(context);
        //                        } else {
        //                            displayNeverAskAgainDialog(context);
        //                        }
        //                    }
        //
        //                    @Override
        //                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
        //                        token.continuePermissionRequest();
        //                    }
        //                }).check();
        //    }
        private fun displayNeverAskAgainDialog(context: Activity) {
            val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
            builder.setMessage(context.resources.getString(R.string.permission_storage))
            builder.setCancelable(false)
            builder.setPositiveButton(context.resources.getString(R.string.permit_manually)) { dialog, which ->
                dialog.dismiss()
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
            builder.setNegativeButton(context.resources.getString(R.string.cancel), null)
            builder.show()
        }
    }
}