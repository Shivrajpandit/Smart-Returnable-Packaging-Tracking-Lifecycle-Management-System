package com.packaging.controller;

import com.packaging.dto.PackagingTypeDTO;
import com.packaging.entity.PackagingType;
import com.packaging.service.PackagingTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packaging-types")
public class PackagingTypeController {

    private final PackagingTypeService packagingTypeService;

    @Autowired
    public PackagingTypeController(PackagingTypeService packagingTypeService) {
        this.packagingTypeService = packagingTypeService;
    }

    @GetMapping
    public ResponseEntity<List<PackagingType>> getAllTypes() {
        return ResponseEntity.ok(packagingTypeService.getAllTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackagingType> getTypeById(@PathVariable Integer id) {
        return ResponseEntity.ok(packagingTypeService.getTypeById(id));
    }

    @PostMapping
    public ResponseEntity<PackagingType> createType(@Valid @RequestBody PackagingTypeDTO dto) {
        return new ResponseEntity<>(packagingTypeService.createType(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackagingType> updateType(@PathVariable Integer id, @Valid @RequestBody PackagingTypeDTO dto) {
        return ResponseEntity.ok(packagingTypeService.updateType(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable Integer id) {
        packagingTypeService.deleteType(id);
        return ResponseEntity.noContent().build();
    }
}
