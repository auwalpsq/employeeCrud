package com.employee.employeeCrud.ui.view.component

import com.employee.employeeCrud.entity.EmployeeEntity
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.Hr
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class EmployeeDetailDialog : Dialog() {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private val content = VerticalLayout()
    private var employee: EmployeeEntity? = null

    init {
        configureDialog()
        setupContent()
    }

    private fun configureDialog() {
        width = "600px"
        isCloseOnOutsideClick = true
        isCloseOnEsc = true
        addClassName("employee-detail-dialog")
    }

    private fun setupContent() {
        content.apply {
            setPadding(true)
            setSpacing(true)
            setSizeFull()
            style.set("max-width", "100%")
        }
        add(content)
    }

    fun showEmployee(employee: EmployeeEntity) {
        this.employee = employee
        updateContent()
        open()
    }

    private fun updateContent() {
        content.removeAll()
        employee?.let { emp ->
            content.apply {
                add(createHeader(emp))
                add(Hr())
                add(createSectionHeader("Personal Information"))
                add(createPersonalInfoSection(emp))
                add(Hr())
                add(createSectionHeader("Contact Information"))
                add(createContactInfoSection(emp))
                add(Hr())
                add(createSectionHeader("Employment Information"))
                add(createEmploymentInfoSection(emp))
                add(createDialogFooter())
            }
        }
    }

    private fun createHeader(employee: EmployeeEntity) = HorizontalLayout().apply {
        setWidthFull()
        defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER

        add(H2("Employee Details").apply {
            style.set("margin", "0")
        })

        add(Span("ID: ${employee.getId()}").apply {
            style.set("margin-left", "auto")
            style.set("color", "var(--lumo-secondary-text-color)")
        })
    }

    private fun createSectionHeader(title: String) = H3(title).apply {
        style.set("color", "var(--lumo-primary-color)")
        style.set("margin-bottom", "0.5em")
    }

    private fun createPersonalInfoSection(employee: EmployeeEntity) = VerticalLayout().apply {
        setPadding(false)
        setSpacing(true)

        add(createDetailRow("Full Name", employee.getFirstName() + " " + employee.getSurname() + " " + employee.getOtherNames()))
        add(createDetailRow("Gender", employee.getGender()))
        add(createDetailRow("Date of Birth", employee.getDateOfBirth()?.format(dateFormatter) ?: ""))
        add(createDetailRow("Age", calculateAge(employee.getDateOfBirth())))
        add(createDetailRow("State", employee.getState() ?: ""))
    }

    private fun createContactInfoSection(employee: EmployeeEntity) = VerticalLayout().apply {
        setPadding(false)
        setSpacing(true)

        add(createDetailRow("Email", employee.getEmail()))
        //add(createDetailRow("Phone", employee.phoneNumber ?: ""))
        add(createDetailRow("Address", employee.getStreetAddress() + ", " + employee.getCity()))
    }

    private fun createEmploymentInfoSection(employee: EmployeeEntity) = VerticalLayout().apply {
        setPadding(false)
        setSpacing(true)

        //add(createDetailRow("Department", employee.department ?: ""))
        //add(createDetailRow("Position", employee.position ?: ""))
        add(createDetailRow("Hire Date", employee.getJoiningDate()?.format(dateFormatter) ?: ""))
        //add(createDetailRow("Employee Status", employee.employmentStatus ?: ""))
    }

    private fun createDetailRow(label: String, value: String) = HorizontalLayout().apply {
        setWidthFull()
        defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER

        add(Span(label).apply {
            style.set("font-weight", "600")
            style.set("min-width", "150px")
        })

        add(Span(value).apply {
            style.set("color", "var(--lumo-secondary-text-color)")
        })
    }

    private fun createDialogFooter() = HorizontalLayout().apply {
        setWidthFull()
        justifyContentMode = FlexComponent.JustifyContentMode.END

        add(Button("Close") {
            close()
        }.apply {
            addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        })
    }

    private fun calculateAge(dateOfBirth: LocalDate?): String {
        return dateOfBirth?.let {
            ChronoUnit.YEARS.between(it, LocalDate.now()).toString()
        } ?: ""
    }
}