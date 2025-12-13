package de.techwende.bestbeforebell.data.model

import java.time.LocalDate

data class Product(
    val id: Int,
    val name: String,
    val bestBefore: LocalDate,
)
