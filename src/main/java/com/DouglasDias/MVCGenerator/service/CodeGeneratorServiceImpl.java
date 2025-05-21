package com.DouglasDias.MVCGenerator.service;

import com.DouglasDias.MVCGenerator.model.EntityDefinitionModel;
import com.DouglasDias.MVCGenerator.model.ProjectRequestModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
public class CodeGeneratorServiceImpl implements CodeGeneratorService {

    @Autowired
    private Configuration freemarkerConfig;


    @Override
    public File generate(ProjectRequestModel projectRequestModel) throws Exception {

        String basePackage = projectRequestModel.getBasePackage();
        String basePath = basePackage.replace(".", "/");

        // 1. CRIAR PASTA TEMPORÁRIA
        Path projectDir = Files.createTempDirectory(basePackage.replace(".", "_"));

        Path repositoryDir = Files.createDirectories(projectDir.resolve("src/main/java/" + basePath  + "/repository"));

        Path serviceDir = Files.createDirectories(projectDir.resolve("src/main/java/" + basePath  + "/service"));

        Path controllerDir = Files.createDirectories(projectDir.resolve("src/main/java/" + basePath  + "/controller"));


        // 2. CRIAR SUBPASTAS (model por enquanto)
        Path modelDir = Files.createDirectories(projectDir.resolve(
                "src/main/java/" + basePath  + "/model"));

        // 3. Gerar arquivos Model
        for (EntityDefinitionModel entity : projectRequestModel.getEntities()) {
            generateModel(entity, modelDir, basePackage);
            generateRepository(entity, repositoryDir, basePackage);
            generateServiceInterface(entity, serviceDir, basePackage);
            generateController(entity, controllerDir, basePackage);
            generateServiceImpl(entity, serviceDir, basePackage);

        }

        generatePom(
                projectDir,
                projectRequestModel.getGroupId(),
                projectRequestModel.getArtifactId()
        );


        generateMainApplication(projectDir, basePackage);
        generateApplicationProperties(projectDir, projectRequestModel.getApplicationName());

        // 4. Compactar arquivo em .ZIP
        File zipFile = File.createTempFile("generated", ".zip");
        try(ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
            zipDirectory(projectDir.toFile(), projectDir.toFile(), zipOut);


        }
        return zipFile;
    }


    private void generateModel(EntityDefinitionModel entity, Path outputDir, String basePackage) throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate("Model.ftl");
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", basePackage + ".model");
        dataModel.put("className", entity.getName());
        dataModel.put("fields", entity.getFields());

        File outputFile = outputDir.resolve(entity.getName() + ".java").toFile();
        try (Writer writer = new FileWriter(outputFile)) {
            template.process(dataModel, writer);
        }
    }

    private void generateRepository(EntityDefinitionModel entity, Path outputDir, String basePackage) throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate("Repository.ftl");

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", basePackage + ".repository");
        dataModel.put("modelPackage", basePackage + ".model");
        dataModel.put("className", entity.getName());
        System.out.println("Classname recebida: " + entity.getName());

        File outputFile = outputDir.resolve(entity.getName() + "Repository.java").toFile();
        try (Writer writer = new FileWriter(outputFile)) {
            template.process(dataModel, writer);
        }
    }

    private void generateServiceInterface(EntityDefinitionModel entity, Path outputDir, String basePackage) throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate("Service.ftl");

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", basePackage + ".service");
        dataModel.put("modelPackage", basePackage + ".model");
        dataModel.put("className", entity.getName());

        File outputFile = outputDir.resolve(entity.getName() + "Service.java").toFile();
        try (Writer writer = new FileWriter(outputFile)) {
            template.process(dataModel, writer);
        }
    }

    private void generateServiceImpl(EntityDefinitionModel entity, Path outputDir, String basePackage) throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate("ServiceImpl.ftl");

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", basePackage + ".service");
        dataModel.put("modelPackage", basePackage + ".model");
        dataModel.put("repositoryPackage", basePackage + ".repository");
        dataModel.put("className", entity.getName());

        File outputFile = outputDir.resolve(entity.getName() + "ServiceImpl.java").toFile();
        try (Writer writer = new FileWriter(outputFile)) {
            template.process(dataModel, writer);
        }
    }

    private void generateController(EntityDefinitionModel entity, Path outputDir, String basePackage) throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate("Controller.ftl");

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", basePackage + ".controller");
        dataModel.put("modelPackage", basePackage + ".model");
        dataModel.put("servicePackage", basePackage + ".service");
        dataModel.put("className", entity.getName());

        File outputFile = outputDir.resolve(entity.getName() + "Controller.java").toFile();
        try (Writer writer = new FileWriter(outputFile)) {
            template.process(dataModel, writer);
        }
    }

    private void generatePom(Path projectDir, String groupId, String artifactId) throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate("Pom.ftl");

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("groupId", groupId);
        dataModel.put("artifactId", artifactId);

        File pomFile = projectDir.resolve("pom.xml").toFile();
        try (Writer writer = new FileWriter(pomFile)) {
            template.process(dataModel, writer);
        }
    }

    private void generateMainApplication(Path outputDir, String basePackage) throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate("MainApplication.ftl");

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", basePackage);
        dataModel.put("className", "Application");

        Path packageDir = outputDir.resolve("src/main/java/" + basePackage.replace(".", "/"));
        Files.createDirectories(packageDir);

        File outputFile = packageDir.resolve("Application.java").toFile();
        try (Writer writer = new FileWriter(outputFile)) {
            template.process(dataModel, writer);
        }
    }

    private void generateApplicationProperties(Path outputDir, String applicationName) throws IOException, TemplateException {

        if (applicationName == null || applicationName.isBlank()) {
            applicationName = "Application";
        }


        Template template = freemarkerConfig.getTemplate("applicationProperties.ftl");

        Path resourcesDir = outputDir.resolve("src/main/resources");
        Files.createDirectories(resourcesDir);

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("applicationName", applicationName);


        File outputFile = resourcesDir.resolve("application.properties").toFile();
        try (Writer writer = new FileWriter(outputFile)) {
            template.process(dataModel, writer);
        }
    }

    private void zipDirectory(File rootDirectory, File sourceDirectory, ZipOutputStream zipOut) throws IOException {
        for (File file : Objects.requireNonNull(sourceDirectory.listFiles())) {
            if (file.isDirectory()) {
                zipDirectory(rootDirectory, file, zipOut);
            } else {
                String entryName = rootDirectory.toURI().relativize(file.toURI()).getPath();
                zipOut.putNextEntry(new ZipEntry(entryName));
                Files.copy(file.toPath(), zipOut);
                zipOut.closeEntry();
            }
        }
    }
}
