package com.DouglasDias.MVCGenerator.controller;


import com.DouglasDias.MVCGenerator.model.ProjectRequestModel;
import com.DouglasDias.MVCGenerator.service.CodeGeneratorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;

@RestController
@RequestMapping("/generate")
public class GeneratorController {
    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @PostMapping
    public ResponseEntity<Resource> generate(@Valid @RequestBody ProjectRequestModel projectRequestModel) throws Exception {
        File zipFile = codeGeneratorService.generate(projectRequestModel);

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(zipFile.toPath()));
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipFile.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
