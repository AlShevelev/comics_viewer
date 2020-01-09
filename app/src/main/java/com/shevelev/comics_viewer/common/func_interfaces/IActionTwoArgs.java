package com.shevelev.comics_viewer.common.func_interfaces;

/**
 * Analog of Action from .NET
 */
public interface IActionTwoArgs<TSource1, TSource2>
{
    void process(TSource1 t1, TSource2 t2);
}
