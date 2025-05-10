package com.employee.employeeCrud

import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.theme.Theme
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@Theme("default")
class EmployeeCrudApplication : AppShellConfigurator

fun main(args: Array<String>) {
	runApplication<EmployeeCrudApplication>(*args)
}
