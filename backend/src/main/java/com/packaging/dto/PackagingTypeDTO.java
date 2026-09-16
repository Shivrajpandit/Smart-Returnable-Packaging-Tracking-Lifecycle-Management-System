package com.packaging.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PackagingTypeDTO {

    private Integer typeId;

    @NotBlank(message = "Packaging type name is required")
    private String typeName;

    private String description;

    @NotNull(message = "Capacity is required")
    @DecimalMin(value = "0.01", message = "Capacity must be greater than 0")
    private BigDecimal capacityKg;

    public PackagingTypeDTO() {}

    public PackagingTypeDTO(Integer typeId, String typeName, String description, BigDecimal capacityKg) {
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
