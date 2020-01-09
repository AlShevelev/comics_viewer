package com.shevelev.comics_viewer.activities.folders.list_adapter.create_view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo;
import com.shevelev.comics_viewer.activities.folders.file_system.folders_tree.FoldersTreeItem;

public class CreateFileView extends CreateViewBase
{
    public CreateFileView(Activity context, LayoutInflater inflater, int parentViewWidth)
    {
        super(context, inflater, parentViewWidth);
    }

    @Override
    public View CreateView(DiskItemInfo diskItem, FoldersTreeItem foldersTreeItem)
    {
        ViewHolder viewHolder = getView(R.layout.folders_list_item);

        viewHolder.text.setText(diskItem.getDisplayName());

        viewHolder.image.setImageResource(R.drawable.ic_file);
        viewHolder.checkBox.setVisibility(View.INVISIBLE);

        viewHolder.image.setOnClickListener(v -> { });            // For prevent clicking track on checked item
        viewHolder.text.setOnClickListener(v -> { });

        return viewHolder.view;
    }
}
