package com.pandacorp.knowui.data.mappers

import com.pandacorp.knowui.data.models.FactDataItem
import com.pandacorp.knowui.domain.models.FactItem

class FactMapper {
    private fun toFactItem(factDataItem: FactDataItem): FactItem =
        FactItem(factDataItem.contentEnglish, tags = factDataItem.tags, imagePath = factDataItem.imagePath)

    fun listToFactItem(list: List<FactDataItem>): List<FactItem> = list.map {
        toFactItem(it)
    }
}