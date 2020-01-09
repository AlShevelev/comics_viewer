package com.shevelev.comics_viewer.activities.main.one_comics_working.operations;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.activities.comics_creation.ChooseComicsName;
import com.shevelev.comics_viewer.activities.comics_creation.PagesStartSorter;
import com.shevelev.comics_viewer.activities.comics_creation.SortPagesActivity;
import com.shevelev.comics_viewer.activities.main.comics_filters.ComicsViewMode;
import com.shevelev.comics_viewer.activities.main.one_comics_working.IOneComicsActivity;
import com.shevelev.comics_viewer.comics_workers.ComicsCreator;
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo;
import com.shevelev.comics_viewer.common.helpers.ScreenHelper;
import com.shevelev.comics_viewer.common.helpers.ToastsHelper;
import com.shevelev.comics_viewer.common.rhea.RheaFacade;
import com.shevelev.comics_viewer.common.rhea.RheaOperationProgressInfo;

import java.util.List;

/**
 * Operation for create comics
 */
public class CreateComicsOperation extends ComicsOperationBase
{
    private ChooseComicsName chooseComicsName;
    private ComicsCreator comicsCreator;

    public CreateComicsOperation(IOneComicsActivity activity)
    {
        super(activity);
    }

    public void preStart(String pathToFolder)
    {
        chooseComicsName = new ChooseComicsName(context, (name, isPrivate, pathToComicsFolder) -> onComicsNameChoose(name, isPrivate, pathToComicsFolder));
        chooseComicsName.startCreate(pathToFolder);        // Choose comics name
    }

    public void start(int[] diskItemsSortedIds)
    {
        comicsCreator.setDiskItems(diskItemsSortedIds);

        uiMethods.setUserActionsLock(true);
        uiMethods.setProgressState(true);

        RheaFacade.run(context, comicsCreator);
    }

    public void complete(Object result)
    {
        uiMethods.setProgressState(false);

        if(result==null)              // Shit happends
            ToastsHelper.Show(R.string.message_cant_create_comics_title, ToastsHelper.Position.Center);
        else
        {
            uiMethods.setViewMode(ComicsViewMode.All);
            uiMethods.updateBooksList((Long)result);
        }
        uiMethods.setUserActionsLock(false);
    }

    public void completeWithError()
    {
        ToastsHelper.Show(R.string.message_cant_create_comics_title, ToastsHelper.Position.Center);
    }

    public void updateProgress(RheaOperationProgressInfo progressInfo)
    {
        uiMethods.updateProgressText(String.format(context.getResources().getText(R.string.pages_processing_progress).toString(), progressInfo.value, progressInfo.total));
    }

    public void initOnRestart(RheaOperationProgressInfo progressInfo)
    {
        uiMethods.setUserActionsLock(true);
        uiMethods.setProgressState(true);
        updateProgress(progressInfo);
    }

    /**
     * When we choose comics name
     * @param name - name of comics
     * @param pathToFolder - path to comics folder
     */
    private void onComicsNameChoose(String name, boolean isPrivateComics, String pathToFolder)
    {
        List<DiskItemInfo> images= PagesStartSorter.Sort(pathToFolder);

        comicsCreator=new ComicsCreator(ComicsCreator.tag, name, isPrivateComics, images, ScreenHelper.getClientSize(context));

        SortPagesActivity.start(context, pathToFolder);                // Start pages sorting
    }
}
