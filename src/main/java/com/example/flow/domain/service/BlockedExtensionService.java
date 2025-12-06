package com.example.flow.domain.service;

import com.example.flow.domain.dto.BlockedExtensionRequest;
import com.example.flow.domain.dto.BlockedExtensionListResponse;
import com.example.flow.domain.dto.PinnedExtensionInfo;
import com.example.flow.domain.entity.BlockedExtension;
import com.example.flow.domain.repository.BlockedExtensionRepository;
import com.example.flow.global.exception.BusinessException;
import com.example.flow.global.exception.ErrorCode;
import java.util.List;
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
        String name = request.getName();

        // - 예외처리 루틴 시작
        if (!isPinnedExtension(name)) {
            validateCountLimit(); // 갯수 체크
        }
        validateExtensionName(name); // 이름 유효성 쳌
        validateLength(name); // 이름 길이 체크
        validateDuplicate(name); // 중복검사
        // - 루틴 끝

        return blockedExtensionRepository.findByName(name)
                .filter(blockedExtension -> !blockedExtension.isValid())
                .map(blockedExtension -> {
                    blockedExtension.restore();
                    return blockedExtensionRepository.save(blockedExtension);
                })
                .orElseGet(() -> blockedExtensionRepository.save(
                        BlockedExtension.builder()
                                .name(name)
                                .pinned(false)
                                .build()
                ));
    }

    public BlockedExtensionListResponse getExtensionList() {
        List<PinnedExtensionInfo> pinnedExtensions = blockedExtensionRepository.findAllByPinnedTrueOrderByIdAsc()
                .stream()
                .map(extension -> new PinnedExtensionInfo(extension.getName(), extension.isValid()))
                .toList();

        List<String> customExtensions = blockedExtensionRepository.findAllByPinnedFalseAndIsValidTrueOrderByNameAsc()
                .stream()
                .map(BlockedExtension::getName)
                .toList();

        return new BlockedExtensionListResponse(pinnedExtensions, customExtensions);
    }

    @Transactional
    public void delete(String name) {
        BlockedExtension extension = blockedExtensionRepository.findByNameAndIsValidTrue(name)
                .orElseThrow(() -> new BusinessException(ErrorCode.EXTENSION_NOT_FOUND));
        extension.delete();
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
                .filter(BlockedExtension::isPinned)
                .ifPresent(blockedExtension -> {
                    throw new BusinessException(ErrorCode.PINNED_EXTENSION_ALREADY_EXISTS);
                });

        blockedExtensionRepository.findByNameAndIsValidTrue(name)
                .ifPresent(blockedExtension -> {
                    throw new BusinessException(ErrorCode.EXTENSION_ALREADY_EXISTS);
                });
    }

    private void validateCountLimit() {
        long count = blockedExtensionRepository.countByPinnedFalseAndIsValidTrue();
        if (count >= 200) {
            throw new BusinessException(ErrorCode.EXTENSION_LIMIT_EXCEEDED);
        }
    }

    public boolean isPinnedExtension(String name) {
        return blockedExtensionRepository.existsByNameAndPinnedTrue(name);
    }
}
