package com.shevelev.comics_viewer.common.func_interfaces;

/**
 * Analog of Action from .NET
 */
public interface IActionThreeArgs<TSource1, TSource2, TSource3>
{
    void process(TSource1 t1, TSource2 t2, TSource3 t3);
}
