package com.shevelev.comics_viewer.common.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.common.helpers.BitmapsHelper;
import com.shevelev.comics_viewer.common.structs.Size;

public class ZoomedPagePreviewDialog
{
    private String _initName;

    private Button _okButton;

    private final Activity parentActivity;
    private final String fullPathToImageFile;
    private final Size dialogSize;

    private AlertDialog dialog;

    private Size imageSize;

    public ZoomedPagePreviewDialog(Activity parentActivity, String fullPathToImageFile, Size dialogSize)
    {
        this.parentActivity = parentActivity;
        this.fullPathToImageFile = fullPathToImageFile;
        this.dialogSize = dialogSize;
    }

    private AlertDialog create()
    {
        LayoutInflater inflater = parentActivity.getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);

        FrameLayout view=(FrameLayout)inflater.inflate(R.layout.dialog_preview_zoomed, null);

        Bitmap image=createBitmap();
        ImageView pageImage=((ImageView)view.findViewById(R.id.preview_page_image));
        pageImage.setImageBitmap(image);

        ImageView closeIcon=((ImageView)view.findViewById(R.id.closeIcon));
        closeIcon.setImageResource(R.drawable.ic_clear_yellow_48dp);
        closeIcon.setOnClickListener(v -> dialog.dismiss());

        builder.setView(view);

        return builder.create();
    }

    private Bitmap createBitmap()
    {
        Bitmap source=BitmapsHelper.loadFromFile(fullPathToImageFile);
        Size sourceSize=new Size(source.getWidth(), source.getHeight());

        Size targetSize=dialogSize.inscribe(sourceSize);

        imageSize = targetSize;
        Bitmap bmp=BitmapsHelper.scale(source, targetSize);

        return bmp;
    }

    public void show()
    {
        dialog = create();
        dialog.show();

//        dialog.getWindow().setLayout(imageSize.getWidth(), imageSize.getHeight());          // resize dialog to image's size
    }
}
