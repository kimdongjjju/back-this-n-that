package com.djukim.thisnthat.service;

import com.djukim.thisnthat.image.Image;
import com.djukim.thisnthat.image.ImagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    private final ImagePublisher imagePublisher;

    public void imagePublisher(Image image) {
        image.updateUrl("/test");
        imagePublisher.publish(image);
    }
}
