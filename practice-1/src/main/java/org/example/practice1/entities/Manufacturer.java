package org.example.practice1.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;

@Entity
@Data
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String description;

    public static RowMapper<Manufacturer> getRowMapper(){
        return (ResultSet rs, int rowNum) -> {
            Manufacturer m = new Manufacturer();

            long idVal = rs.getLong("id");
            m.setId(rs.wasNull() ? null : idVal);

            m.setName(rs.getString("name"));
            m.setDescription(rs.getString("description"));

            return m;
        };
    }
}
