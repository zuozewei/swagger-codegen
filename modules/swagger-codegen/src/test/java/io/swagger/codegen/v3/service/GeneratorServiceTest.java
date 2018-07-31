package io.swagger.codegen.v3.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class GeneratorServiceTest {

    @Test(description = "test generator service with java 3.0")
    public void testGeneratorServiceJava3() {

        GenerationRequest request = new GenerationRequest();
        request
                .codegenVersion(GenerationRequest.CodegenVersion.V3)
                .type(GenerationRequest.Type.SERVER)
                .spec(loadSpecAsNode("3_0_0/petstore.json", false))
                .options(
                        new Options()
                                .lang("jaxrs-jersey")
                                .outputDir(getTmpFolder().getAbsolutePath())
                );
        List<File> files = new GeneratorService().generationRequest(request).generate();
        Assert.assertFalse(files.isEmpty());
    }

    @Test(description = "test generator service with java 2.0")
    public void testGeneratorServiceJava2() {

        GenerationRequest request = new GenerationRequest();
        request
                .codegenVersion(GenerationRequest.CodegenVersion.V2)
                .type(GenerationRequest.Type.SERVER)
                .spec(loadSpecAsNode("2_0/petstore.json", false))
                .options(
                        new Options()
                                .lang("jaxrs")
                                .outputDir(getTmpFolder().getAbsolutePath())
                );
        List<File> files = new GeneratorService().generationRequest(request).generate();
        Assert.assertFalse(files.isEmpty());
    }

    @Test(description = "test generator service with java 3.0 and spec 2.0")
    public void testGeneratorServiceJava3Spec2() {

        GenerationRequest request = new GenerationRequest();
        request
                .codegenVersion(GenerationRequest.CodegenVersion.V3)
                .type(GenerationRequest.Type.SERVER)
                .spec(loadSpecAsNode("2_0/petstore.json", false))
                .options(
                        new Options()
                                .lang("jaxrs-jersey")
                                .outputDir(getTmpFolder().getAbsolutePath())
                );
        List<File> files = new GeneratorService().generationRequest(request).generate();
        Assert.assertFalse(files.isEmpty());
    }

    @Test(description = "test generator service with java 3.0, spec as ref to file")
    public void testGeneratorServiceJava3RefSpec() {
        GenerationRequest request = new GenerationRequest();
        request
                .codegenVersion(GenerationRequest.CodegenVersion.V3)
                .type(GenerationRequest.Type.SERVER)
                .specURL("3_0_0/petstore.json")
                .options(
                        new Options()
                                .lang("jaxrs-jersey")
                                .outputDir(getTmpFolder().getAbsolutePath())
                );
        List<File> files = new GeneratorService().generationRequest(request).generate();
        Assert.assertFalse(files.isEmpty());
    }


    @Test(description = "test generator service with java client 3.0")
    public void testGeneratorServiceJavaClient3() {

        GenerationRequest request = new GenerationRequest();
        request
                .codegenVersion(GenerationRequest.CodegenVersion.V3)
                .type(GenerationRequest.Type.CLIENT)
                .spec(loadSpecAsNode("3_0_0/petstore.json", false))
                .options(
                        new Options()
                            .lang("java")
                            .outputDir(getTmpFolder().getAbsolutePath())
                            .artifactId("swagger-petstore-jersey2")
                            .library("jersey2")
                );
        List<File> files = new GeneratorService().generationRequest(request).generate();
        Assert.assertFalse(files.isEmpty());
    }

    @Test(description = "test generator service with java client 2.0")
    public void testGeneratorServiceJavaClient2() {
        GenerationRequest request = new GenerationRequest();
        request
                .codegenVersion(GenerationRequest.CodegenVersion.V2)
                .type(GenerationRequest.Type.CLIENT)
                .spec(loadSpecAsNode("2_0/petstore.json", false))
                .options(
                        new Options()
                                .lang("java")
                                .outputDir(getTmpFolder().getAbsolutePath())
                                .artifactId("swagger-petstore-jersey2")
                                .library("jersey2")
                );
        List<File> files = new GeneratorService().generationRequest(request).generate();
        Assert.assertFalse(files.isEmpty());
    }

    protected static File getTmpFolder() {
        try {
            File outputFolder = File.createTempFile("codegentest-", "-tmp");
            outputFolder.delete();
            outputFolder.mkdir();
            outputFolder.deleteOnExit();
            return outputFolder;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected JsonNode loadSpecAsNode(final String file, boolean yaml) {
        InputStream in = null;
        String s = "";
        try {
            in = getClass().getClassLoader().getResourceAsStream(file);
            if (yaml) {
                return Yaml.mapper().readTree(in);
            }
            return Json.mapper().readTree(in);
        } catch (Exception e) {
            throw new RuntimeException("could not load file " + file);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    private static OpenAPI parseOpenAPI(String path) {
        final ParseOptions options = new ParseOptions();
        options.setFlatten(Boolean.TRUE);
        return new OpenAPIV3Parser()
                .readLocation(path, null, options)
                .getOpenAPI();
    }
}
