package com.djukim.thisnthat.request;

import com.djukim.thisnthat.image.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Setter
@Getter
@ToString
public class ImageRequest {
    private String requestId;

    public Image toDomain() {
        return Image.builder()
                .fileId(UUID.randomUUID()+requestId)
                .build();
    }
}
