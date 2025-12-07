package com.example.flow.domain.controller;

import com.example.flow.domain.dto.BlockedExtensionListResponse;
import com.example.flow.domain.dto.BlockedExtensionRequest;
import com.example.flow.domain.dto.FileValidationRequest;
import com.example.flow.domain.entity.BlockedExtension;
import com.example.flow.domain.service.BlockedExtensionService;
import com.example.flow.global.dto.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blocked-extensions")
public class BlockedExtensionController {

    private final BlockedExtensionService blockedExtensionService;

    @PostMapping
    public ApiResult<BlockedExtension> create(@Valid @RequestBody BlockedExtensionRequest request) {
        BlockedExtension saved = blockedExtensionService.create(request);
        return ApiResult.succeed(saved, "차단 확장자를 추가했습니다.");
    }

    @GetMapping
    public ApiResult<BlockedExtensionListResponse> getExtensions() {
        return ApiResult.succeed(blockedExtensionService.getExtensionList(), "확장자 목록을 조회했습니다.");
    }

    @DeleteMapping("/{name}")
    public ApiResult<Void> delete(@PathVariable String name) {
        blockedExtensionService.delete(name);
        return ApiResult.succeed(null, "확장자를 삭제했습니다.");
    }

    @PostMapping("/validate")
    public ApiResult<Void> validateFile(@Valid @RequestBody FileValidationRequest request) {
        blockedExtensionService.validateFileName(request);
        return ApiResult.succeed(null, "업로드 완료!.");
    }
}
