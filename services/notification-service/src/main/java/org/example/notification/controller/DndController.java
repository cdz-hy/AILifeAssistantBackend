package org.example.notification.controller;

import org.example.notification.dto.DndRequest;
import org.example.notification.entity.NotificationDnd;
import org.example.notification.service.DndService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dnd")
public class DndController {

    @Autowired
    private DndService dndService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<String> setDndSettings(@PathVariable Long userId,
                                                 @RequestBody DndRequest request) {
        try {
            dndService.setDndSettings(userId, request);
            return ResponseEntity.ok("DND settings updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update DND settings: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<NotificationDnd> getDndSettings(@PathVariable Long userId) {
        try {
            NotificationDnd dndSettings = dndService.getDndSettings(userId);
            return ResponseEntity.ok(dndSettings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    // 添加 PUT 方法
    @PutMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> updateDndSettings(@PathVariable Long userId,
                                                                 @RequestBody DndRequest request) {
        try {
            dndService.setDndSettings(userId, request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "DND settings updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to update DND settings: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}