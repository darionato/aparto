package com.badlydone.aparto.core;

import java.util.Comparator;

public class aPartoCompareContact implements Comparator<aPartoContactItem> {
    public int compare(aPartoContactItem o1, aPartoContactItem o2) {
        return o1.getName().compareTo(o2.getName());
    }
}