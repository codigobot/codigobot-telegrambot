package com.codigobot.telegram;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.context.annotation.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(WebsiteConfigurationProperties.PREFIX)
public class WebsiteConfigurationProperties implements WebsiteConfiguration {
    public static final String PREFIX = "website";
    @NonNull
    @NotBlank
    private String rssUrl;

    @Override
    @NonNull
    public String getRssUrl() {
        return this.rssUrl;
    }

    public void setRssUrl(@NonNull String rssUrl) {
        this.rssUrl = rssUrl;
    }
}
