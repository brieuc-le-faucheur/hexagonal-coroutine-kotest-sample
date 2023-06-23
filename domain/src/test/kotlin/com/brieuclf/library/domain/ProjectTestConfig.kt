package com.brieuclf.library.domain

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.extensions.htmlreporter.HtmlReporter
import io.kotest.extensions.junitxml.JunitXmlReporter

class ProjectTestConfig : AbstractProjectConfig() {

    override val isolationMode = IsolationMode.InstancePerTest

    override fun extensions(): List<Extension> = listOf(
        JunitXmlReporter(
            includeContainers = false,
            useTestPathAsName = true,
        ),
        HtmlReporter()
    )
}