package com.shevelev.comics_viewer.activities.folders;

import android.app.Activity;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.common.custom_controls.ProgressBar;
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo;
import com.shevelev.comics_viewer.activities.folders.file_system.folders_tree.FoldersTree;
import com.shevelev.comics_viewer.activities.folders.list_adapter.FoldersListAdapter;

import java.util.List;

/**
 * Main view for folders list
 */
public class FoldersView extends RelativeLayout
{
    private ListView foldersList;           // List with folders
    private ProgressBar previewProgressBar;

    private final IActivityFoldersActions actions;

    private FoldersListAdapter listAdapter;

    private FoldersTree foldersTree;

    private Activity context;

    public FoldersView(Activity context, int layoutId, IActivityFoldersActions actions, FoldersTree foldersTree)
    {
        super(context);

        this.context = context;
        this.actions =actions;
        this.foldersTree = foldersTree;

        inflate(context, layoutId, this);
        foldersList = (ListView) findViewById(R.id.lvFolders);
        previewProgressBar=(ProgressBar)findViewById(R.id.previewProgressBar);
    }

    public void updateDiskItems(List<DiskItemInfo> diskItems)
    {
        listAdapter = new FoldersListAdapter(context, diskItems, actions, foldersTree);
        foldersList.setAdapter(listAdapter);
    }

    public void showProgress()
    {
        previewProgressBar.show();
    }

    public void hideProgress()
    {
        previewProgressBar.hide();
    }
}
