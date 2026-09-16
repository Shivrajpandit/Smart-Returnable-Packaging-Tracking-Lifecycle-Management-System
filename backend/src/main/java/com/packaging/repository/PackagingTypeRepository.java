package com.packaging.repository;

import com.packaging.entity.PackagingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackagingTypeRepository extends JpaRepository<PackagingType, Integer> {
    Optional<PackagingType> findByTypeName(String typeName);
    boolean existsByTypeName(String typeName);
}
