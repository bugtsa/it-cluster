package com.itcluster.mobile.app.ext.glide

fun LoadImageRequest.fileImage(path: String): LoadImageRequest =
        apply {
            imageSource = ImageSource.FileImage(path)
        }