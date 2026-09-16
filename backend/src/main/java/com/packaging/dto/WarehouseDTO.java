package com.packaging.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class WarehouseDTO {

    private Integer warehouseId;

    @NotBlank(message = "Warehouse name is required")
    private String warehouseName;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Manager name is required")
    private String managerName;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[+0-9\\-\\s()]{7,20}$", message = "Invalid contact number format")
    private String contactNumber;

    public WarehouseDTO() {}

    public WarehouseDTO(Integer warehouseId, String warehouseName, String location, String managerName, String contactNumber) {
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.location = location;
        this.managerName = managerName;
        this.contactNumber = contactNumber;
    }

    public Integer getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Integer warehouseId) { this.warehouseId = warehouseId; }
    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
}
