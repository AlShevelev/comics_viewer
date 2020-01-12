package com.shevelev.comics_viewer.activities.main

import com.shevelev.comics_viewer.dal.dto.Comics
import java.util.*

/**
 * Data for comics sort
 */
class ComicsSortInfo(val comparator: Comparator<Comics>,
                     /** Need revers order      */
                     val isReverse: Boolean)