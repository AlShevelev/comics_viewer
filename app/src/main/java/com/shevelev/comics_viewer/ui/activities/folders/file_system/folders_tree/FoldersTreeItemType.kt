package com.shevelev.comics_viewer.ui.activities.folders.file_system.folders_tree

enum class FoldersTreeItemType(val value: Int) {
    FOLDER(0),

    /**
     * Phone root folder without sd-card
     */
    DEVICE(1),

    /**
     * Sd-card root folder
     */
    SD_CARD(2);

    companion object {
        fun create(value: Int) = values().first { it.value == value }
    }
}