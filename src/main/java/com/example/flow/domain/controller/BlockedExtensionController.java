package com.example.flow.domain.controller;

import com.example.flow.domain.dto.BlockedExtensionRequest;
import com.example.flow.domain.entity.BlockedExtension;
import com.example.flow.domain.service.BlockedExtensionService;
import com.example.flow.global.dto.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}
