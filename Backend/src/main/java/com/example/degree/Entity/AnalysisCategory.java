package com.example.degree.Entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "analysis_category")
public class AnalysisCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    String name;
}
