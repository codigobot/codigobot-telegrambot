package com.codigobot.telegram.rss;

import java.util.Comparator;

public class EpisodeComparator implements Comparator<Episode> {
    @Override
    public int compare(Episode o1, Episode o2) {
        return o2.getNumber().compareTo(o1.getNumber());
    }
}
