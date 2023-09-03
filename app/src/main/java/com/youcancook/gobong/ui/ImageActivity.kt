package com.youcancook.gobong.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.core.app.ActivityCompat
import com.canhub.cropper.CropImage.ActivityResult
import com.canhub.cropper.CropImageActivity
import com.canhub.cropper.CropImageView
import com.youcancook.gobong.R
import com.youcancook.gobong.databinding.ExtendedActivityBinding

internal class ImageActivity : CropImageActivity() {

    companion object {
        fun start(activity: Activity) {
            ActivityCompat.startActivity(
                activity,
                Intent(activity, ImageActivity::class.java),
                null,
            )
        }

        fun getImageByteArray(context: Context, uri: Uri?): ByteArray? {
            uri ?: return null
            val inputStream = context.contentResolver.openInputStream(uri)
            val cropImageByteArray = inputStream?.readBytes() ?: return null

            inputStream.close()
            return cropImageByteArray
        }

        val IMAGE_DATA_TAG = "imageData"
        val PHOTO = "photo"
        val PROFILE = "profile"
        val PHOTO_SIZE = "profile"
    }

    private lateinit var binding: ExtendedActivityBinding
    private var photoSize = PHOTO

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ExtendedActivityBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)

        intent?.getStringExtra(PHOTO_SIZE)?.let {
            binding.cropImageView.setAspectRatio(1, 1)
        }

        binding.saveBtn.setOnClickListener { cropImage() }
        binding.backBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.rotateText.setOnClickListener { onRotateClick() }

        binding.cropImageView.setOnCropWindowChangedListener {
            updateExpectedImageSize()
        }

        setCropImageView(binding.cropImageView)
    }

    fun setImageSize(size: String) {
        photoSize = size
    }

    override fun onSetImageUriComplete(
        view: CropImageView,
        uri: Uri,
        error: Exception?,
    ) {
        super.onSetImageUriComplete(view, uri, error)

        updateRotationCounter()
        updateExpectedImageSize()
    }

    private fun updateExpectedImageSize() {
        binding.expectedImageSize.text = binding.cropImageView.expectedImageSize().toString()
    }

    override fun setContentView(view: View) {
        super.setContentView(binding.root)
    }

    private fun updateRotationCounter() {
        binding.rotateText.text =
            getString(R.string.rotation_value, binding.cropImageView.rotatedDegrees.toString())
    }

    override fun onPickImageResult(resultUri: Uri?) {
        super.onPickImageResult(resultUri)

        if (resultUri != null) {
            binding.cropImageView.setImageUriAsync(resultUri)
        }
    }

    override fun getResultIntent(uri: Uri?, error: java.lang.Exception?, sampleSize: Int): Intent {
        val result = super.getResultIntent(uri, error, sampleSize)
        // Adding some more information.
        return result.putExtra("EXTRA_KEY", "Extra data")
    }

    override fun setResult(uri: Uri?, error: Exception?, sampleSize: Int) {
        val result = ActivityResult(
            originalUri = binding.cropImageView.imageUri,
            uriContent = uri,
            error = error,
            cropPoints = binding.cropImageView.cropPoints,
            cropRect = binding.cropImageView.cropRect,
            rotation = binding.cropImageView.rotatedDegrees,
            wholeImageRect = binding.cropImageView.wholeImageRect,
            sampleSize = sampleSize,
        )

        binding.cropImageView.setImageUriAsync(result.uriContent)

        Log.e("GOBONG", "uri ${uri} ")

        if (uri == null) {
            finish()
            return
        }

        setResult(
            RESULT_OK, Intent().putExtra(IMAGE_DATA_TAG, uri.toString())
        )
        finish()
    }

    override fun setResultCancel() {
        super.setResultCancel()
    }

    override fun updateMenuItemIconColor(menu: Menu, itemId: Int, color: Int) {
        super.updateMenuItemIconColor(menu, itemId, color)
    }

    private fun onRotateClick() {
        binding.cropImageView.rotateImage(90)
        updateRotationCounter()
    }
}
