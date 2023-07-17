package com.example.matteomobile.exceptions;

public class MobileNotFoundException extends RuntimeException
{
    public MobileNotFoundException(Long id)
    { super("Could not find mobile " + id); }
}
