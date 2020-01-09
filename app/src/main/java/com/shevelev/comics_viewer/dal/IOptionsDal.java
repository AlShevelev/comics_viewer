package com.shevelev.comics_viewer.dal;

import com.shevelev.comics_viewer.dal.dto.Option;

import java.util.List;

/**
 * Dal for innerOptionsCollection
 */
public interface IOptionsDal
{
    /**
     * @return false - fail
     */
    boolean update(List<Option> optionsToAdd, List<Option> optionsToUpdate);

    /**
     * @return false - fail
     */
    boolean delete(int[] keys);

    /**
     * @return null - fail
     */
    List<Option> getAll();
}
