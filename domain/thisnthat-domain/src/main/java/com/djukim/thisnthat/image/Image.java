package com.djukim.thisnthat.image;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    private String url;
    private String fileId;
    private int retryCount;

    public void updateUrl(String url) {
        this.url = url;
    }

    public void addRetryCount() {
        this.retryCount++;
    }

    public ImageConsumerEvent toEvent() {
        return ImageConsumerEvent.builder().url(url).fileId(fileId).build();
    }


}
