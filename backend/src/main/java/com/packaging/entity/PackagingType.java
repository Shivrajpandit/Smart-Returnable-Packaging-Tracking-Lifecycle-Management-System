package com.packaging.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "packaging_types")
public class PackagingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "type_name", nullable = false, unique = true, length = 100)
    private String typeName;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "capacity_kg", nullable = false, precision = 10, scale = 2)
    private BigDecimal capacityKg;

    public PackagingType() {}

    public PackagingType(Integer typeId, String typeName, String description, BigDecimal capacityKg) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.description = description;
        this.capacityKg = capacityKg;
    }

    public Integer getTypeId() { return typeId; }
    public void setTypeId(Integer typeId) { this.typeId = typeId; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getCapacityKg() { return capacityKg; }
    public void setCapacityKg(BigDecimal capacityKg) { this.capacityKg = capacityKg; }
}
