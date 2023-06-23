package com.brieuclf.library.domain

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import io.kotest.core.spec.style.FunSpec

class ArchUnitSpringExclusionTest : FunSpec({

    test("Project does not contain spring classes") {

        ClassFileImporter().withImportOption(DoNotIncludeTests()).importPackages("com.brieuclf.library.domain")
            .let {
                classes()
                    .should()
                    .onlyDependOnClassesThat()
                    .resideOutsideOfPackages(
                        "org.springframework.."
                    )
                    .check(it)
            }

    }
})