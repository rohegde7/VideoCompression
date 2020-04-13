package com.rohegde7.videocompression.livedataenums

enum class VideoCompressionAction(var message: String) {

    COMPRESS_REQUESTED(""),
    DISPLAY_PROGRESS("Converting video.."),
    COMPRESS_SUCCESS("Convertion successful"),
    FAILURE("UnExpected Error")
}