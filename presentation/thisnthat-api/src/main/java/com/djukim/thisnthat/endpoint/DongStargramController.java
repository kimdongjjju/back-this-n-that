package com.djukim.thisnthat.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/post")
public class DongStargramController {

    @PostMapping("/upload")
    public ResponseEntity<Void> createPost() {
        return ResponseEntity.ok().build();
        
    }

}
