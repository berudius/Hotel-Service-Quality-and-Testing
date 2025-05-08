package com.hotel_service;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.tngtech.archunit.core.domain.JavaClasses;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import  com.tngtech.archunit.core.domain.JavaModifier;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.processing.Generated;

import java.util.Arrays;
import java.util.List;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith;
import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.nameEndingWith;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/*
    @author Berkeshchuk
    @project App.java
    @class ProjArchitectureTests
    @version 1.0.0
    @since 5/8/2025-11.26
*/
@SpringBootTest
class ProjArchitectureTests {

    private JavaClasses applicationClasses;

    @BeforeEach
    void initialize() {
        applicationClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.hotel_service");
    }

    @Test
    void shouldFollowLayerArchitecture()  {
        layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controller").definedBy("com.hotel_service.controllers..")
                .layer("Service").definedBy("com.hotel_service.services..")
                .layer("Repository").definedBy("com.hotel_service.repository..")
                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Service")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
                .check(applicationClasses);
    }

    @Test
    void  controllerClassesShouldBeNamedXController() {
        classes()
                .that().resideInAPackage("..controllers..")
                .should()
                .haveSimpleNameEndingWith("Controller")
                .check(applicationClasses);
    }

    @Test
    void serviceClassesShouldBeNamedXService() {
        classes()
                .that().resideInAPackage("..services..")
                .should().haveSimpleNameEndingWith("Service")
                .andShould().bePublic()
                .andShould().resideInAPackage("..services..")
                .check(applicationClasses);
    }

    @Test
    void repositoryInterfacesShouldBeNamedXRepository() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().beInterfaces()
                .andShould().haveSimpleNameEndingWith("Repository")
                .andShould().bePublic()
                .andShould().resideInAPackage("..repository..")
                .check(applicationClasses);
    }


    @Test
    void dtoClassesShouldBeNamedXDTO() {
        classes()
                .that().resideInAPackage("..models_dto..")
                .and().areNotNestedClasses()
                .should().haveSimpleNameEndingWith("DTO")
                .andShould().bePublic()
                .andShould().resideInAPackage("..models_dto..")
                .check(applicationClasses);
    }


    @Test
    void repositoriesShouldNotDependOnDtoModels() {
        noClasses()
                .that().resideInAPackage("..repository..")
                .should().dependOnClassesThat()
                .resideInAPackage("..models_dto..")
                .check(applicationClasses);
    }


    @Test
    void controllersShouldBeAnnotatedWithController() {
        classes()
                .that().resideInAPackage("..controllers..")
                .should().bePublic()
                .andShould().beAnnotatedWith(Controller.class)
                .orShould().beAnnotatedWith(RestController.class)
                .check(applicationClasses);
    }



    @Test
    void anyControllerFieldsShouldNotBeAnnotatedAutowired() {
        noClasses()
                .that().resideInAPackage("..controllers..")
                .should()
                .beAnnotatedWith(Autowired.class)
                .check(applicationClasses);
    }


    @Test
    void servicesShouldBeAnnotatedWithService() {
        classes()
                .that().resideInAPackage("..services..")
                .should().beAnnotatedWith(Service.class)
                .andShould().bePublic()
                .check(applicationClasses);
    }

    @Test
    void dtoShouldNotBeAnnotatedAsEntity() {
        noClasses()
                .that().resideInAPackage("..models_dto..")
                .should().beAnnotatedWith(Entity.class)
                .check(applicationClasses);
    }

    @Test
    void modelClassesShouldBeAnnotatedWithEntity() {
        classes()
                .that().resideInAPackage("..models..")
                .should().beAnnotatedWith(Entity.class)
                .andShould().haveSimpleNameNotEndingWith("DTO")
                .andShould().bePublic()
                .check(applicationClasses);
    }

    @Test
    void entityModelsShouldNotDependOnDto() {
        noClasses()
                .that().areAnnotatedWith(Entity.class)
                .should().dependOnClassesThat()
                .resideInAPackage("..models_dto..")
                .check(applicationClasses);
    }

    @Test
    void entityFieldsShouldBePrivate() {
        fields()
                .that().areDeclaredInClassesThat()
                .resideInAPackage("..models..")
                .and().areDeclaredInClassesThat().areAnnotatedWith(Entity.class)
                .should().bePrivate()
                .check(applicationClasses);
    }


    @Test
    void dtoShouldNotDependOnModels() {
        noClasses()
                .that().resideInAPackage("..models_dto..")
                .should().dependOnClassesThat()
                .resideInAPackage("..models..")
                .check(applicationClasses);
    }

    @Test
    void controllerClassesShouldNotBePrivate() {
        noClasses()
                .that().resideInAPackage("..controllers..")
                .should().bePrivate()
                .check(applicationClasses);
    }

    @Test
    void methodsInControllersShouldNotBePrivate(){
        methods()
                .that().areDeclaredInClassesThat().resideInAPackage("..controllers..")
                .should().notBePrivate()
                .check(applicationClasses);
    }

    @Test
    void controllersShouldNotHavePublicFields() {
        fields()
                .that().areDeclaredInClassesThat().resideInAPackage("..controllers..")
                .should().notBePublic()
                .check(applicationClasses);
    }

    @Test
    void mappersShouldBeInterfaces() {
        classes()
                .that().resideInAPackage("..mappers_dto..")
                .should().beInterfaces()
                .andShould().haveSimpleNameEndingWith("Mapper")
                .andShould().bePublic()
                .check(applicationClasses.that(not(nameEndingWith("Impl"))));
    }

    @Test
    void mappersShouldOnlyBeAccessedByService() {
        layeredArchitecture()
                .consideringAllDependencies()
                .layer("Mapper").definedBy("..mappers_dto..")
                .layer("Service").definedBy("..services..")
                .whereLayer("Mapper").mayOnlyBeAccessedByLayers("Service")
                .check(applicationClasses);
    }

    @Test
    void controllersShouldNotDependOnOtherControllers() {
        noClasses()
                .that().resideInAPackage("..controllers..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..controllers..")
                .because("out of arch rules")
                .check(applicationClasses);
    }


}