package com.example.kotlinjdslexample

import com.example.kotlinjdslexample.entity.BookMeta

data class CreateBookSpec(
    val name: String,
    val meta: BookMeta
)