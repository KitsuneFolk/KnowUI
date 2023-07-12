package com.pandacorp.knowui.data.mappers

import com.pandacorp.knowui.data.models.FactDataItem
import com.pandacorp.knowui.domain.models.FactItem

class FactMapper {
    fun toFactItem(factDataItem: FactDataItem): FactItem =
        FactItem(factDataItem.contentEnglish)

    fun toFactDataItem(factItem: FactItem): FactDataItem =
        FactDataItem(factItem.contentEnglish)
}