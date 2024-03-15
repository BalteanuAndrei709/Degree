package com.example.degree.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "analysis")
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "analysis_category_id",  referencedColumnName = "id")
    private AnalysisCategory analysisCategory;

    private String name;
}
