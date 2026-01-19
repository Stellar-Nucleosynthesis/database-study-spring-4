package org.example.practice1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Lorry extends Vehicle {
    private Integer massKg;
    private Integer capacityKg;
}
