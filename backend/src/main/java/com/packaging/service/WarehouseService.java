package com.packaging.service;

import com.packaging.dto.WarehouseDTO;
import com.packaging.entity.Warehouse;
import com.packaging.exception.ResourceNotFoundException;
import com.packaging.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Autowired
    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public Warehouse getWarehouseById(Integer warehouseId) {
        return warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + warehouseId));
    }

    @Transactional
    public Warehouse createWarehouse(WarehouseDTO dto) {
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseName(dto.getWarehouseName().trim());
        warehouse.setLocation(dto.getLocation().trim());
        warehouse.setManagerName(dto.getManagerName().trim());
        warehouse.setContactNumber(dto.getContactNumber().trim());

        return warehouseRepository.save(warehouse);
    }

    @Transactional
    public Warehouse updateWarehouse(Integer warehouseId, WarehouseDTO dto) {
        Warehouse warehouse = getWarehouseById(warehouseId);

        warehouse.setWarehouseName(dto.getWarehouseName().trim());
        warehouse.setLocation(dto.getLocation().trim());
        warehouse.setManagerName(dto.getManagerName().trim());
        warehouse.setContactNumber(dto.getContactNumber().trim());

        return warehouseRepository.save(warehouse);
    }

    @Transactional
    public void deleteWarehouse(Integer warehouseId) {
        if (!warehouseRepository.existsById(warehouseId)) {
            throw new ResourceNotFoundException("Warehouse not found with id: " + warehouseId);
        }
        warehouseRepository.deleteById(warehouseId);
    }
}
