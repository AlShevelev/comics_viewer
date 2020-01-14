package com.shevelev.comics_viewer.dal

object DalFacade {
    val Comics: IComicsDal = ComicsDal()
    val Options: IOptionsDal = OptionsDal()
}