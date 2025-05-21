package com.DouglasDias.MVCGenerator.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ProjectRequestModel {
    private List<EntityDefinitionModel> entities;
    private Map<String, Boolean> options;

    @NotBlank
    private String applicationName;

    @NotBlank
    private String groupId;

    @NotBlank
    private String artifactId;

    private String basePackage;
}
