package com.example.flow.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlockedExtensionListResponse {
    private final List<PinnedExtensionInfo> pinnedExtensions;
    private final List<String> customExtensions;
}
