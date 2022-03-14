package com.example.kotlinjdslexample

import com.linecorp.kotlinjdsl.query.creator.SubqueryCreator
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.Persistence

@Configuration
class ReactiveQueryConfiguration {
    @Bean
    fun mutinySessionFactory(): Mutiny.SessionFactory =
        Persistence.createEntityManagerFactory("book").unwrap(Mutiny.SessionFactory::class.java)

    @Bean
    fun queryFactory(sessionFactory: Mutiny.SessionFactory, subqueryCreator: SubqueryCreator): SpringDataHibernateMutinyReactiveQueryFactory {
        return SpringDataHibernateMutinyReactiveQueryFactory(
            sessionFactory = sessionFactory,
            subqueryCreator = subqueryCreator
        )
    }
}