package com.shevelev.comics_viewer.dal;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.shevelev.comics_viewer.common.helpers.CollectionsHelper;
import com.shevelev.comics_viewer.dal.dto.Option;
import com.shevelev.comics_viewer.dal.entities.DbOption;

import java.util.ArrayList;
import java.util.List;

/**
 * Dal for innerOptionsCollection
 */
public class OptionsDal implements IOptionsDal
{
    /**
     * @return false - fail
     */
    private void add(Option option)
    {
        new DbOption(option).save();
    }

    /**
     * @return false - fail
     */
    private void update(Option option)
    {
        ArrayList<DbOption> dbResult = new Select().from(DbOption.class).where("Key = ?", option.key).execute();

        if (dbResult.size() == 0)
            return;

        DbOption dbOption = dbResult.get(0);
        dbOption.value = option.value;
        dbOption.save();
    }

    public boolean update(List<Option> optionsToAdd, List<Option> optionsToUpdate)
    {
        ActiveAndroid.beginTransaction();
        try
        {
            for(Option option : optionsToAdd)
                add(option);

            for(Option option : optionsToUpdate)
                update(option);

            ActiveAndroid.setTransactionSuccessful();
            return true;
        }
        catch (Exception ex)
        {
            Log.e("CV", "exception", ex);
            return false;
        }
        finally
        {
            ActiveAndroid.endTransaction();
        }
    }

    /**
     * @return false - fail
     */
    @Override
    public boolean delete(int[] keys)
    {
        ActiveAndroid.beginTransaction();
        try
        {
            for(int key : keys)
                new Delete().from(DbOption.class).where("Key = ?", key).execute();

            return true;
        }
        catch (Exception ex)
        {
            Log.e("CV", "exception", ex);
            return false;
        }
        finally
        {
            ActiveAndroid.endTransaction();
        }
    }

    /**
     * @return null - fail
     */
    @Override
    public List<Option> getAll()
    {
        try
        {
            ArrayList<DbOption> dbResult=new Select().from(DbOption.class).execute();
            return CollectionsHelper.transform(dbResult, item -> new Option(item));
        }
        catch (Exception ex)
        {
            Log.e("CV", "exception", ex);
            return null;
        }
    }
}