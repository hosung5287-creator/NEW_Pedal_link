package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CyclewayController {

    private final DataSource dataSource;

    @GetMapping("/cycleway/incheon")
    public ResponseEntity<?> getIncheonCycleway() {
        List<String> features = new ArrayList<>();

        String sql = "SELECT ST_AsGeoJSON(ST_Transform(geom, 4326)) as geojson " +
                     "FROM a " +
                     "WHERE ST_IsValid(geom) = true " +
                     "LIMIT 500";  // 너무 많으면 느리니까 500개 제한

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                features.add(rs.getString("geojson"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("DB 오류: " + e.getMessage());
        }

        // GeoJSON FeatureCollection 형태로 반환
        StringBuilder sb = new StringBuilder();
        sb.append("{\"type\":\"FeatureCollection\",\"features\":[");
        for (int i = 0; i < features.size(); i++) {
            sb.append("{\"type\":\"Feature\",\"geometry\":");
            sb.append(features.get(i));
            sb.append(",\"properties\":{}}");
            if (i < features.size() - 1) sb.append(",");
        }
        sb.append("]}");

        return ResponseEntity.ok(sb.toString());
    }
}