package com.brieuclf.library.server

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.extensions.htmlreporter.HtmlReporter
import io.kotest.extensions.junitxml.JunitXmlReporter
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode

class ProjectTestConfig : AbstractProjectConfig() {
    override fun extensions() =
        listOf(
            SpringTestExtension(SpringTestLifecycleMode.Root),
            JunitXmlReporter(
                includeContainers = false,
                useTestPathAsName = true,
            ),
            HtmlReporter()
        )
}
