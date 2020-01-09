package com.shevelev.comics_viewer.options;

import com.shevelev.comics_viewer.dal.dto.Option;

/**
 * Created by Syleiman on 27.12.2015.
 */
public interface IOptionsCollections
{
    /**
     * @return null if not found
     */
    String get(int key);

    /**
     * @return false if fail
     */
    void addOrUpdate(Option[] options);

    /**
     * @return false if fail
     */
    void delete(int[] keys);
}
