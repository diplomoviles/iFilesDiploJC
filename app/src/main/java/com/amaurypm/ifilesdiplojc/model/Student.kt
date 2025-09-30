package com.amaurypm.ifilesdiplojc.model

data class Student(
    var id: Long = System.currentTimeMillis(),
    var name: String? = null
)