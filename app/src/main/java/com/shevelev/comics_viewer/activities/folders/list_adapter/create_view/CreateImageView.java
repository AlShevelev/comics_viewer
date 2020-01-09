package com.shevelev.comics_viewer.activities.folders.list_adapter.create_view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.common.dialogs.ZoomedPagePreviewDialog;
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo;
import com.shevelev.comics_viewer.activities.folders.file_system.folders_tree.FoldersTreeItem;
import com.shevelev.comics_viewer.common.helpers.ScreenHelper;

public class CreateImageView extends CreateViewBase
{
    public CreateImageView(Activity context, LayoutInflater _inflater, int parentViewWidth)
    {
        super(context, _inflater, parentViewWidth);
    }

    @Override
    public View CreateView(DiskItemInfo diskItem, FoldersTreeItem foldersTreeItem)
    {
        ViewHolder viewHolder = getView(R.layout.folders_list_item);

        viewHolder.text.setText(diskItem.getDisplayName());

        viewHolder.image.setImageResource(R.drawable.ic_image);
        viewHolder.checkBox.setVisibility(View.INVISIBLE);

        viewHolder.view.setOnClickListener(v -> showImage(diskItem));

        return viewHolder.view;
    }

    private void showImage(DiskItemInfo diskItem)
    {
        ZoomedPagePreviewDialog dialog=new ZoomedPagePreviewDialog(
                context,
                diskItem.getFullname(),
                ScreenHelper.getScreenSize(context).scale(0.85f));
        dialog.show();
    }
}
