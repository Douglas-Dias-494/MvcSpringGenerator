package com.DouglasDias.MVCGenerator.service;

import com.DouglasDias.MVCGenerator.model.ProjectRequestModel;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public interface CodeGeneratorService {
    File generate(ProjectRequestModel projectRequestModel) throws Exception;
}
