package com.example.kotlinjdslexample

data class FindBookSpec(
    val name: String,
    val publisher: String? = null
)