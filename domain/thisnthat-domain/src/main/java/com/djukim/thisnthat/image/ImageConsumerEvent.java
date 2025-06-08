package com.djukim.thisnthat.image;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ImageConsumerEvent {
    private String url;
    private String fileId;


}
