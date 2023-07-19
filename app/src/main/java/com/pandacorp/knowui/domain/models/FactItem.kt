package com.pandacorp.knowui.domain.models

import android.net.Uri

data class FactItem(
    val contentEnglish: String = "",
    val imagePath: String = "",
    val imageUri: Uri? = null,
    val tags: List<String> = listOf(),
)