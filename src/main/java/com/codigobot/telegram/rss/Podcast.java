package com.codigobot.telegram.rss;

import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Podcast {

    @NonNull
    private List<Episode> episodes = new ArrayList<>();

    @NonNull
    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(@NonNull List<Episode> episodes) {
        this.episodes = episodes;
    }
}
