package com.backend.liaison_system.zone.controller;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.zone.dto.NewZone;
import com.backend.liaison_system.zone.entity.Zone;
import com.backend.liaison_system.zone.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "liaison/api/v1/zones")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @PostMapping(path = "/{admin-id}")
    public ResponseEntity<Response<?>> createNewZone(
            @PathVariable("admin-id") String id,
            @RequestBody List<NewZone> zones,
            @RequestParam boolean internship
    ) throws LiaisonException {
        return zoneService.createNewZone(id, zones, internship);
    }

    @GetMapping(path = "/{admin-id}")
    public ResponseEntity<Response<Zone>> getAllZones(
            @PathVariable("admin-id") String adminId,
            @RequestParam boolean internship
    ) {
        return zoneService.getAllZones(adminId, internship);
    }
}
