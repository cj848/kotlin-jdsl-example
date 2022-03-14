package com.example.kotlinjdslexample

data class UpdateBookSpec(
    val findBookSpec: FindBookSpec,
    val name: String
)