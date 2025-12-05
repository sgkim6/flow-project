package com.example.flow.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PinnedExtensionInfo {
    private final String name;
    private final boolean valid;
}
