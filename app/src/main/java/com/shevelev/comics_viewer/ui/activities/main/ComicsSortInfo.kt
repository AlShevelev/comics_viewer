package com.shevelev.comics_viewer.ui.activities.main

import com.shevelev.comics_viewer.dal.dto.Comics

/**
 * Data for comics sort
 */
class ComicsSortInfo(val comparator: Comparator<Comics>,
                     /** Need revers order      */
                     val isReverse: Boolean)