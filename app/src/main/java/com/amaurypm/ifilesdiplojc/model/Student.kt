package com.amaurypm.ifilesdiplojc.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "student")
data class Student(
    @field:Element(name = "id")
    var id: Long = System.currentTimeMillis(),
    @field:Element(name = "name")
    var name: String? = null
)