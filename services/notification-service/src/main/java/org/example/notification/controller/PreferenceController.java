package org.example.notification.controller;


import org.example.notification.dto.PreferenceRequest;
import org.example.notification.entity.NotificationPreference;
import org.example.notification.service.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/preferences")
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<String> setPreference(@PathVariable Long userId,
                                                @RequestBody PreferenceRequest request) {
        try {
            preferenceService.setUserPreference(userId, request);
            return ResponseEntity.ok("Preference set successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to set preference: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationPreference>> getUserPreferences(@PathVariable Long userId) {
        try {
            List<NotificationPreference> preferences = preferenceService.getUserPreferences(userId);
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    // 更新偏好设置
    @PutMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> updatePreference(@PathVariable Long userId,
                                                                @RequestBody PreferenceRequest request) {
        try {
            System.out.println("更新用户偏好 - userId: " + userId + ", request: " + request);
            preferenceService.setUserPreference(userId, request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Preference updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("更新用户偏好失败 - userId: " + userId + ", error: " + e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to update preference: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // 删除偏好设置
    @DeleteMapping("/{preferenceId}")
    public ResponseEntity<Map<String, Object>> deletePreference(@PathVariable Long preferenceId) {
        try {
            System.out.println("删除偏好设置 - preferenceId: " + preferenceId);
            boolean deleted = preferenceService.deletePreference(preferenceId);

            if (deleted) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Preference deleted successfully");
                response.put("deletedId", preferenceId);
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Preference not found or delete failed");
                return ResponseEntity.status(404).body(errorResponse);
            }
        } catch (Exception e) {
            System.out.println("删除偏好设置失败 - preferenceId: " + preferenceId + ", error: " + e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to delete preference: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // 删除用户的所有偏好设置
    @DeleteMapping("/user/{userId}/all")
    public ResponseEntity<Map<String, Object>> deleteAllUserPreferences(@PathVariable Long userId) {
        try {
            System.out.println("删除用户所有偏好设置 - userId: " + userId);
            int deletedCount = preferenceService.deleteAllUserPreferences(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Deleted " + deletedCount + " preferences");
            response.put("deletedCount", deletedCount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("删除用户所有偏好设置失败 - userId: " + userId + ", error: " + e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to delete all preferences: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

}