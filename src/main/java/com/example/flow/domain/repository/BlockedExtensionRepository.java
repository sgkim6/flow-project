package com.example.flow.domain.repository;

import com.example.flow.domain.entity.BlockedExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlockedExtensionRepository extends JpaRepository<BlockedExtension, Long> {
    Optional<BlockedExtension> findByName(String name);
    Optional<BlockedExtension> findByNameAndIsValidTrue(String name);
    boolean existsByNameAndPinnedTrue(String name);
    long countByPinnedFalseAndIsValidTrue();
    List<BlockedExtension> findAllByPinnedTrueOrderByIdAsc();
    List<BlockedExtension> findAllByPinnedFalseAndIsValidTrueOrderByNameAsc();
}
