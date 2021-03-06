package com.baomidou.mybatisplus.core.pom;

import jodd.io.FileUtil;
import jodd.jerry.Jerry;
import jodd.lagarto.dom.LagartoDOMBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 检查pom依赖
 *
 * @author nieqiurong 2019/2/9.
 */
class GeneratePomTest {
    
    @Data
    @AllArgsConstructor
    private static class Dependency {
        private String artifactId;
        private String scope;
        private boolean optional;
    }
    
    @Test
    void test() throws IOException {
        InputStream inputStream = new FileInputStream("build/publications/mavenJava/pom-default.xml");
        Jerry.JerryParser jerryParser = new Jerry.JerryParser(new LagartoDOMBuilder().enableXmlMode());
        Jerry doc = jerryParser.parse(FileUtil.readUTFString(inputStream));
        Jerry dependencies = doc.$("dependencies dependency");
        Map<String, Dependency> dependenciesMap = new HashMap<>();
        dependencies.forEach($this -> {
            String artifactId = $this.$("artifactId").text();
            dependenciesMap.put(artifactId, new Dependency(artifactId, $this.$("scope").text(), Boolean.parseBoolean($this.$("optional").text())));
        });
        Dependency annotation = dependenciesMap.get("mybatis-plus-annotation");
        Assertions.assertEquals("compile", annotation.getScope());
        Assertions.assertFalse(annotation.isOptional());
        Dependency mybatis = dependenciesMap.get("mybatis");
        Assertions.assertEquals("compile", mybatis.getScope());
        Assertions.assertFalse(mybatis.isOptional());
        Dependency jsqlParser = dependenciesMap.get("jsqlparser");
        Assertions.assertEquals("compile", jsqlParser.getScope());
        Assertions.assertFalse(jsqlParser.isOptional());
        Dependency cglib = dependenciesMap.get("cglib");
        Assertions.assertEquals("compile", cglib.getScope());
        Assertions.assertTrue(cglib.isOptional());
        Dependency aop = dependenciesMap.get("spring-aop");
        Assertions.assertEquals("compile", aop.getScope());
        Assertions.assertTrue(aop.isOptional());
    }
    
}
