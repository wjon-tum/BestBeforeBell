package de.techwende.bestbeforebell.domain.model

import java.time.LocalDate

data class Product(
    val id: Int,
    val name: String,
    val bestBefore: LocalDate
)
