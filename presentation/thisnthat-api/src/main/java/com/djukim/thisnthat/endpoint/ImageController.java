package com.djukim.thisnthat.endpoint;

import com.djukim.thisnthat.request.ImageRequest;
import com.djukim.thisnthat.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/image")
    public String test(@RequestBody ImageRequest request) {
        imageService.imagePublisher(request.toDomain());

        return request.getRequestId();
    }

    @GetMapping("/image-health")
    public String health() {
        return "success";
    }
}
