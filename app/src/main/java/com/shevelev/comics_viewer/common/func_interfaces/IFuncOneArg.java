package com.shevelev.comics_viewer.common.func_interfaces;

/**
 * Analog of Func from .NET
 */
public interface IFuncOneArg<TSource, TTarget>
{
    TTarget process(TSource t);
}
