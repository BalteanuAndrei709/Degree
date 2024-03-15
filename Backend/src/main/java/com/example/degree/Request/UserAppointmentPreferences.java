package com.example.degree.Request;


import com.example.degree.Entity.Specialization;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAppointmentPreferences {

        public LocalDateTime dayFrom;
        public LocalDateTime dayTo;

        @Enumerated(EnumType.STRING)
        public Specialization specialization;

        public String medicName;

}
