package com.example.flow.domain.service;

import com.example.flow.domain.dto.BlockedExtensionRequest;
import com.example.flow.domain.entity.BlockedExtension;
import com.example.flow.domain.repository.BlockedExtensionRepository;
import com.example.flow.global.exception.BusinessException;
import com.example.flow.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlockedExtensionService {

    private final BlockedExtensionRepository blockedExtensionRepository;

    @Transactional
    public BlockedExtension create(BlockedExtensionRequest request) {
        validateExtensionName(request.getName());
        validateLength(request.getName());
        validateDuplicate(request.getName());
        BlockedExtension blockedExtension = BlockedExtension.builder()
                .name(request.getName())
                .pinned(false)
                .build();
        return blockedExtensionRepository.save(blockedExtension);
    }

    private void validateExtensionName(String name) {
        if (!name.matches("^[A-Za-z0-9]+$")) {
            throw new BusinessException(ErrorCode.INVALID_EXTENSION);
        }
    }

    private void validateLength(String name) {
        if (name.length() > 20) {
            throw new BusinessException(ErrorCode.EXTENSION_NAME_TOO_LONG);
        }
    }

    private void validateDuplicate(String name) {
        blockedExtensionRepository.findByName(name)
                .ifPresent(blockedExtension -> {
                    if (blockedExtension.isPinned()) {
                        throw new BusinessException(ErrorCode.PINNED_EXTENSION_ALREADY_EXISTS);
                    }
                    throw new BusinessException(ErrorCode.EXTENSION_ALREADY_EXISTS);
                });
    }
}
