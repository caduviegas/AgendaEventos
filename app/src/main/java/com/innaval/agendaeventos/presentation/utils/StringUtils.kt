package com.innaval.agendaeventos.presentation.utils

import android.content.Context
import java.text.NumberFormat
import java.util.*

fun List<String>.splitStringList(
    context: Context,
    emptyListString: Int,
    fullListString: Int
): String {
    return if (isEmpty()) {
        context.getString(emptyListString)
    } else {
        context.getString(
            fullListString,
            joinToString { it.capitalize() }
        )
    }
}

fun Double.formatMonetary(): String{
    val format = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2
    format.currency = Currency.getInstance("BRL")
    return format.format(this)
}