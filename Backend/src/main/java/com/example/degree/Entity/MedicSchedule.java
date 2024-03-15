package com.example.degree.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "medic_schedule")
@Entity
public class MedicSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer day;

    @ManyToOne
    private Medic medic;

    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    private LocalTime startHour;

    private LocalTime endHour;


}
