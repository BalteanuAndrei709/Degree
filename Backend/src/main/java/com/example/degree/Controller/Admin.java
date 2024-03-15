package com.example.degree.Controller;


import com.example.degree.Response.AgeStats;
import com.example.degree.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class Admin {

    @Autowired
    private AdminService adminService;

    @PostMapping("/generateCalendar")
    public ResponseEntity<Object> generateCalendar(){
        return ResponseEntity.ok(adminService.createSchedule());
    }

    @PostMapping("/addCountyPrefixes")
    public ResponseEntity<Object> addCountyPrefixes(){
        return ResponseEntity.ok(adminService.addCountyPrefixes());
    }


}
