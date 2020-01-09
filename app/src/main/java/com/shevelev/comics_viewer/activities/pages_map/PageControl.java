package com.shevelev.comics_viewer.activities.pages_map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs;
import com.shevelev.comics_viewer.common.helpers.BitmapsHelper;
import com.shevelev.comics_viewer.common.helpers.files.AppPrivateFilesHelper;
import com.shevelev.comics_viewer.common.helpers.ToastsHelper;
import com.shevelev.comics_viewer.dal.dto.Page;

import java.util.Date;

public class PageControl  extends LinearLayout
{
    private Page pageInfo;

    private int pageIndex;
    private int activePageIndex;

    private IActionOneArgs<Integer> onChangeActivePage;          // Callback when active page changed (param: page index)
    private IActionOneArgs<Integer> onZoomPage;                  // Callback when click zoom icon (param: page index)

    private Date lastClickMoment;

    public PageControl(
            Activity activity,
            Page pageInfo,
            int maxPageWidth,
            int number,
            int activePageIndex,
            IActionOneArgs<Integer> onChangeActivePage,
            IActionOneArgs<Integer> onZoomPage)
    {
        super(activity);

        this.pageInfo = pageInfo;
        this.pageIndex = number-1;
        this.activePageIndex = activePageIndex;
        this.onChangeActivePage = onChangeActivePage;
        this.onZoomPage = onZoomPage;

        inflate(activity, R.layout.pages_map_one_page, this);

        TextView pageText = (TextView)findViewById(R.id.pageText);
        pageText.setText(String.format(activity.getResources().getString(R.string.pageTitle), number));

        ImageView coverImage = (ImageView)findViewById(R.id.pageImage);
        Bitmap bmp = BitmapsHelper.loadFromFile(AppPrivateFilesHelper.getFullName(pageInfo.getPreviewFileName()));
        coverImage.setImageBitmap(bmp);

        ImageView zoomIcon = (ImageView)findViewById(R.id.pageZoomIcon);
        zoomIcon.setImageResource(R.drawable.ic_zoom_in_yellow_48dp);

        LinearLayout rootView = (LinearLayout)findViewById(R.id.rootControl);
        if(activePageIndex!=number-1)               // Set active page
            rootView.setBackground(null);

        ViewGroup.LayoutParams pc = new ViewGroup.LayoutParams(maxPageWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(pc);

        setOnClickListener(v -> { onClick(v); });
        zoomIcon.setOnClickListener(v -> { onZoomIconClick(v); });
    }

    private void onClick(View view)
    {
        if(needChangeCurrentPage())
            onChangeActivePage.process(pageIndex);         // change active page
    }

    /**
     * Calculate condition for changing current page
     */
    private boolean needChangeCurrentPage()
    {
        if(activePageIndex==pageIndex)
            return false;

        if(lastClickMoment==null)
        {
            lastClickMoment = new Date();
            ToastsHelper.Show(R.string.message_tap_again_to_change_page, ToastsHelper.Duration.Short, ToastsHelper.Position.Bottom);
        }
        else
        {
            Date now = new Date();
            long timeDelta=now.getTime()-lastClickMoment.getTime();

            if(timeDelta <= 1500)           // 1s.
            {
                lastClickMoment = null;
                return true;
            }
            else
            {
                lastClickMoment = new Date();
                ToastsHelper.Show(R.string.message_tap_again_to_change_page, ToastsHelper.Duration.Short, ToastsHelper.Position.Bottom);
            }
       }

        return false;
    }

    private void onZoomIconClick(View view)
    {
        onZoomPage.process(pageIndex);
    }
}
