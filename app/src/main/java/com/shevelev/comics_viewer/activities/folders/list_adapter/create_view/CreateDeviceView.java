package com.shevelev.comics_viewer.activities.folders.list_adapter.create_view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo;
import com.shevelev.comics_viewer.activities.folders.file_system.folders_tree.FoldersTreeItem;
import com.shevelev.comics_viewer.activities.folders.IActivityFoldersActions;

public class CreateDeviceView extends CreateViewBase
{
    private IActivityFoldersActions actions;

    public CreateDeviceView(Activity context, LayoutInflater _inflater, int parentViewWidth, IActivityFoldersActions actions)
    {
        super(context, _inflater, parentViewWidth);

        this.actions = actions;
    }

    @Override
    public View CreateView(DiskItemInfo diskItem, FoldersTreeItem foldersTreeItem)
    {
        boolean disabled=foldersTreeItem==null || !foldersTreeItem.getIsActive();

        if(!disabled)
        {
            ViewHolder viewHolder = getView(R.layout.folders_list_item);
            viewHolder.text.setText(diskItem.getDisplayName());

            viewHolder.checkBox.setVisibility(View.INVISIBLE);

            viewHolder.image.setImageResource(R.drawable.ic_cell_phone2);


//            viewHolder.image.setOnClickListener(v -> actions.FolderTaped(diskItem.getId(), false));
//            viewHolder.text.setOnClickListener(v -> actions.FolderTaped(diskItem.getId(), false));
            viewHolder.view.setOnClickListener(v -> actions.FolderTaped(diskItem.getId(), false));

            return viewHolder.view;
        }
        else
        {
            ViewHolder viewHolder = getView(R.layout.folders_list_item_disabled);
            viewHolder.text.setText(diskItem.getDisplayName());

            viewHolder.checkBox.setVisibility(View.INVISIBLE);

            viewHolder.image.setImageResource(R.drawable.ic_cell_phone2_disabled);

            viewHolder.image.setOnClickListener(v -> {} );
            viewHolder.text.setOnClickListener(v -> {});
            viewHolder.view.setOnClickListener(v -> { });

            return viewHolder.view;
        }
    }
}