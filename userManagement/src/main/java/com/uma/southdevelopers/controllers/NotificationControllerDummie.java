package com.uma.southdevelopers.controllers;

import com.uma.southdevelopers.dto.NotificationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationControllerDummie {

    @PostMapping
    public ResponseEntity<?> enviarNoti(@RequestBody NotificationDTO notificationDTO){
        return ResponseEntity.ok().build();
    }
}
