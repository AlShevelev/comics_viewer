package com.shevelev.comics_viewer.common.func_interfaces;

/**
 * Analog of Action from .NET
 */
public interface IActionOneArgs<TSource>
{
    void process(TSource t);
}
