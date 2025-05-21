package com.DouglasDias.MVCGenerator.model;

import lombok.Data;
import java.util.Map;

@Data
public class EntityDefinitionModel {
    private String name;
    private Map<String, String> fields;
}
