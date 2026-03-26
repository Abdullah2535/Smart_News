package com.smartnews.backend.controllers;


import com.smartnews.backend.dtos.PreferencesResponse;
import com.smartnews.backend.services.PreferenceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/preferences")
@RestController
@AllArgsConstructor
public class PreferencesController {
    private PreferenceService preferenceService;

    @GetMapping("")
    public ResponseEntity<PreferencesResponse> getPreferences() {
        var preferencesResponse = preferenceService.getPreferences();
        return ResponseEntity.ok(preferencesResponse);
    }
}
