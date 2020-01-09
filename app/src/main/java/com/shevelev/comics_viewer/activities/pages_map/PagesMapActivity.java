package com.shevelev.comics_viewer.activities.pages_map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.activities.ActivityCodes;
import com.shevelev.comics_viewer.activities.ActivityResultCodes;
import com.shevelev.comics_viewer.activities.view_comics.ActivityParamCodes;
import com.shevelev.comics_viewer.common.dialogs.ZoomedPagePreviewDialog;
import com.shevelev.comics_viewer.common.helpers.files.AppPrivateFilesHelper;
import com.shevelev.comics_viewer.dal.DalFacade;
import com.shevelev.comics_viewer.dal.dto.Comics;
import com.shevelev.comics_viewer.dal.dto.Page;

import java.util.List;

/**
 * Activity for map of pages
 */
public class PagesMapActivity extends AppCompatActivity
{
    private long comicsId;

    private int activePageIndex;

    private PagesMapView view;

    private List<Page> pages;

    /**
     * Start activity
     */
    public static void start(Activity parentActivity, long comicsId)
    {
        Intent intent = new Intent(parentActivity, PagesMapActivity.class);              // Start view comics

        Bundle b = new Bundle();
        b.putLong(ActivityParamCodes.IdOfComics, comicsId);
        intent.putExtras(b);

        parentActivity.startActivityForResult(intent, ActivityCodes.PagesMapActivity);
    }

    /**
     * Parse result of activity
     */
    public static int parseResult(Intent data)
    {
        return data.getIntExtra(ActivityResultCodes.activePageIndex, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        comicsId = getIntent().getExtras().getLong(ActivityParamCodes.IdOfComics);
        Comics comics=DalFacade.Comics.getComicsById(comicsId);
        pages=DalFacade.Comics.getPages(comicsId);

        activePageIndex = comics.lastViewedPageIndex;

        view = new PagesMapView(this, comics, pages, (newActivePageIndex)-> onChangeActivePage(newActivePageIndex), (pageIndex)-> onZoomPage(pageIndex));
        setContentView(view);
    }

    /**
     * Change active page from PageControl
     */
    private void onChangeActivePage(int newActivePageIndex)
    {
        if(DalFacade.Comics.updateLastViewedPageIndex(comicsId, newActivePageIndex))
        {
            activePageIndex = newActivePageIndex;
            view.updatePageControls(newActivePageIndex);
        }
    }

    /**
     * Click zoom icon in PageControl
     */
    private void onZoomPage(int pageIndex)
    {
        ZoomedPagePreviewDialog dialog=new ZoomedPagePreviewDialog(
                this,
                AppPrivateFilesHelper.getFullName(pages.get(pageIndex).fileName),
                view.getSize().scale(0.9f));
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pages_map, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_close:
            {
                prepareToFinish();
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        prepareToFinish();
        super.onBackPressed();              // call finish() and close acivity
    }

    private void prepareToFinish()
    {
        Intent intent = new Intent();
        intent.putExtra(ActivityResultCodes.activePageIndex, activePageIndex);
        setResult(RESULT_OK, intent);
    }
}
