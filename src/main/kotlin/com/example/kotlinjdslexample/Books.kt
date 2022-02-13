package com.example.kotlinjdslexample

import com.example.kotlinjdslexample.entity.Book
import com.example.kotlinjdslexample.entity.BookMeta
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.from.associate
import com.linecorp.kotlinjdsl.querydsl.where.WhereDsl
import com.linecorp.kotlinjdsl.spring.data.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import javax.persistence.EntityManager

@RestController
@RequestMapping("/api/v1/books")
class BookController(
    private val bookService: BookService,
) {
    @PostMapping
    fun createBook(@RequestBody spec: BookService.CreateBookSpec): ResponseEntity<Long> {
        val book = bookService.create(spec)

        return ResponseEntity.ok(book.id)
    }

    @GetMapping("/{bookId}")
    fun findById(@PathVariable bookId: Long): ResponseEntity<Book> {
        val book = bookService.findById(bookId)

        return ResponseEntity.ok(book)
    }

    @GetMapping("/{bookId}/toVO")
    fun findByIdToVO(@PathVariable bookId: Long): ResponseEntity<NameIsbnVO> {
        val book = bookService.findByIdToVO(bookId)

        return ResponseEntity.ok(book)
    }

    @GetMapping
    fun findAll(@RequestParam("name") name: String): ResponseEntity<List<Book>> {
        val books = bookService.findAll(BookService.FindBookSpec(name = name))

        return ResponseEntity.ok(books)
    }

    @GetMapping("/paging")
    fun findAllByPaging(
        pageable: Pageable,
        @RequestParam("name") name: String,
        @RequestParam("publisher") publisher: String? = null
    ): ResponseEntity<Page<Book>> {
        val books = bookService.findAll(BookService.FindBookSpec(name = name, publisher = publisher), pageable)

        return ResponseEntity.ok(books)
    }

    @PutMapping
    fun update(@RequestBody spec: BookService.UpdateBookSpec): ResponseEntity<Int> {
        val updatedRow = bookService.update(spec)

        return ResponseEntity.ok(updatedRow)
    }

    @DeleteMapping
    fun delete(@RequestBody spec: BookService.FindBookSpec): ResponseEntity<Int> {
        val updatedRow = bookService.delete(spec)

        return ResponseEntity.ok(updatedRow)
    }
}

data class NameIsbnVO(
    val name: String,
    val isbn10: String,
    val isbn13: String
)

@Service
@Transactional
class BookService(
    private val entityManager: EntityManager,
    private val queryFactory: SpringDataQueryFactory,
) {
    fun create(spec: CreateBookSpec): Book {
        return Book(name = spec.name, meta = spec.meta).also {
            entityManager.persist(it)
        }
    }

    fun findById(id: Long): Book {
        return queryFactory.singleQuery {
            select(entity(Book::class))
            from(entity(Book::class))
            where(col(Book::id).equal(id))
        }
    }

    fun findByIdToVO(id: Long): NameIsbnVO {
        return queryFactory.singleQuery {
            selectMulti(col(Book::name), col(BookMeta::isbn10), col(BookMeta::isbn13))
            from(entity(Book::class))
            associate(Book::meta)
            where(col(Book::id).equal(id))
        }
    }

    fun findAll(spec: FindBookSpec): List<Book> {
        return queryFactory.listQuery {
            select(entity(Book::class))
            from(entity(Book::class))
            where(findSpec(spec))
        }
    }

    fun update(spec: UpdateBookSpec): Int {
        return queryFactory.updateQuery<Book> {
            associate(Book::meta)
            where(findSpec(spec.findBookSpec))
            set(col(Book::name), spec.name)
        }.executeUpdate()
    }

    fun delete(spec: FindBookSpec): Int {
        return queryFactory.deleteQuery<Book> {
            associate(Book::meta)
            where(findSpec(spec))
        }.executeUpdate()
    }

    /**
     * if you want reuse Predicates, extract Expressions and make an Extension like this methods
     * WhereDsl.xxxx
     */
    private fun WhereDsl.findSpec(spec: FindBookSpec) =
        and(
            col(Book::name).like("%${spec.name}%"),
            spec.publisher?.let { col(BookMeta::publisher).equal(spec.publisher) }
        )

    fun findAll(spec: FindBookSpec, pageable: Pageable): Page<Book> {
        return queryFactory.pageQuery(pageable) {
            select(entity(Book::class))
            from(entity(Book::class))
            where(findSpec(spec))
        }
    }

    data class CreateBookSpec(
        val name: String,
        val meta: BookMeta
    )

    data class FindBookSpec(
        val name: String,
        val publisher: String? = null
    )

    data class UpdateBookSpec(
        val findBookSpec: FindBookSpec,
        val name: String
    )
}
