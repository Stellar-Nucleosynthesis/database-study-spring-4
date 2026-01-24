package org.example.practice2.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "person_full")
public class PersonFull {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "salary_rounded")
    private Long salaryRounded;
}