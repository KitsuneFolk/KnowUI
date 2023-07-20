package com.pandacorp.knowui.utils

fun formatTags(tags: List<String>): String {
    val formattedString = buildString {
        tags.forEachIndexed { index, tag ->
            append("#$tag")
            if (index < tags.size - 1) {
                append(", ")
            }
        }
    }
    return formattedString
}