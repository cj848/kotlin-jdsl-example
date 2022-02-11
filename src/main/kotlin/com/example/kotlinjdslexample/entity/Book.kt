package com.example.kotlinjdslexample.entity

import javax.persistence.*

@Entity
@Table(name = "book")
data class Book(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column
    val name: String,

    @Embedded
    val meta: BookMeta
)

@Embeddable
data class BookMeta(
    var isbn10: String,
    var isbn13: String,
    var subTitle: String,
    var seriesInformation: String,
    var author: String,
    var contributors: String,
    var publisher: String,
    var keywords: String
)