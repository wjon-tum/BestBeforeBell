package de.techwende.bestbeforebell.domain.model

import java.time.LocalDate

data class Product(
    val id: Long,
    val name: String,
    val bestBefore: LocalDate,
    val quantity: Int
)
