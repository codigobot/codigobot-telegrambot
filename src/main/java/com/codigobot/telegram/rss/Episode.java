package com.codigobot.telegram.rss;

import edu.umd.cs.findbugs.annotations.NonNull;

public class Episode {

    @NonNull
    private String title;

    @NonNull
    private String url;

    @NonNull
    private String showNotes;

    @NonNull
    private Integer number;

    public Episode() {
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    @NonNull
    public String getShowNotes() {
        return showNotes;
    }

    public void setShowNotes(@NonNull String showNotes) {
        this.showNotes = showNotes;
    }

    @NonNull
    public Integer getNumber() {
        return number;
    }

    public void setNumber(@NonNull Integer number) {
        this.number = number;
    }
}
