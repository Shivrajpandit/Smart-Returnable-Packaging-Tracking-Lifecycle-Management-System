package com.packaging.service;

import com.packaging.dto.PackagingTypeDTO;
import com.packaging.entity.PackagingType;
import com.packaging.exception.ConflictException;
import com.packaging.exception.ResourceNotFoundException;
import com.packaging.repository.PackagingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PackagingTypeService {

    private final PackagingTypeRepository packagingTypeRepository;

    @Autowired
    public PackagingTypeService(PackagingTypeRepository packagingTypeRepository) {
        this.packagingTypeRepository = packagingTypeRepository;
    }

    public List<PackagingType> getAllTypes() {
        return packagingTypeRepository.findAll();
    }

    public PackagingType getTypeById(Integer typeId) {
        return packagingTypeRepository.findById(typeId)
                .orElseThrow(() -> new ResourceNotFoundException("Packaging type not found with id: " + typeId));
    }

    @Transactional
    public PackagingType createType(PackagingTypeDTO dto) {
        if (packagingTypeRepository.existsByTypeName(dto.getTypeName().trim())) {
            throw new ConflictException("Packaging type '" + dto.getTypeName() + "' already exists");
        }

        PackagingType type = new PackagingType();
        type.setTypeName(dto.getTypeName().trim());
        type.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
        type.setCapacityKg(dto.getCapacityKg());

        return packagingTypeRepository.save(type);
    }

    @Transactional
    public PackagingType updateType(Integer typeId, PackagingTypeDTO dto) {
        PackagingType type = getTypeById(typeId);

        type.setTypeName(dto.getTypeName().trim());
        type.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
        type.setCapacityKg(dto.getCapacityKg());

        return packagingTypeRepository.save(type);
    }

    @Transactional
    public void deleteType(Integer typeId) {
        if (!packagingTypeRepository.existsById(typeId)) {
            throw new ResourceNotFoundException("Packaging type not found with id: " + typeId);
        }
        packagingTypeRepository.deleteById(typeId);
    }
}
