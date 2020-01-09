package com.shevelev.comics_viewer.activities.view_comics.user_actions_managing;


public interface ITransitionFunc
{
    int process(Event event, ViewStateCodes vewStateCodes);
}
