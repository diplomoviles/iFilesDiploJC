package com.amaurypm.ifilesdiplojc.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Student(
    var id: Long = System.currentTimeMillis(),
    var name: String? = null,
    var lastname: String? = null
)