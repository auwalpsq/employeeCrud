package com.employee.employeeCrud.ui.view

import com.employee.employeeCrud.entity.EmployeeEntity
import com.employee.employeeCrud.service.EmployeeService
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import com.employee.employeeCrud.ui.view.component.EmployeeFormComponent
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.Main
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.router.Menu
import com.employee.employeeCrud.ui.view.component.EmployeeDetailDialog
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import jakarta.annotation.security.PermitAll

@PermitAll
@Route
@Menu(order=1.0, icon="vaadin:user", title="Employee List")
class EmployeeListView(
    private val service: EmployeeService
) : Main() {
    private lateinit var employeeForm: EmployeeFormComponent
    private lateinit var employeeDialog: Dialog
    private val employeeDetailDialog = EmployeeDetailDialog()
    private val grid = Grid(EmployeeEntity::class.java)
    private val filterText = TextField()

    init {
        addClassName("list-view")
        setSizeFull()
        configureGrid()
        configureForm()

        val content = VerticalLayout(getToolbar(), grid)
        content.setSizeFull()
        add(content)

        updateList()
    }

    private fun configureGrid() {
        grid.addClassNames("employee-grid")
        grid.setSizeFull()

        grid.removeAllColumns()

        grid.setColumns("id", "firstName", "surname", "gender", "email", "dateOfBirth")

        grid.addComponentColumn { employee ->
            Button("View Detail").apply {
                addClassName("view-details-button")
                icon = Icon(VaadinIcon.SEARCH)
                addClickListener { employeeDetailDialog.showEmployee(employee) }
            }
        }.setHeader("Actions")

        grid.columns.forEach { it.setAutoWidth(true) }

        grid.asSingleSelect().addValueChangeListener { event ->
            editEmployee(event.value)
        }
    }

    private fun configureForm() {
        employeeForm = EmployeeFormComponent()
        employeeDialog = Dialog(employeeForm)
        employeeDialog.setWidth("50%")
        employeeForm.addSaveListener() { event ->
            saveEmployee(event.employee)
        }
        employeeForm.addDeleteListener() { event ->
            deleteEmployee(event.employee)
        }
        employeeForm.addCloseListener() { event ->
            closeEditor()
        }
    }

    private fun getToolbar(): HorizontalLayout {
        filterText.placeholder = "Filter by name..."
        filterText.valueChangeMode = ValueChangeMode.EAGER
        filterText.setSizeFull()
        filterText.addValueChangeListener { filterList() }

        val addEmployeeButton = Button("Add Employee") {
            addEmployee()
        }

        val toolbar = HorizontalLayout(H2("Employees"), filterText, addEmployeeButton)
        toolbar.addClassName("toolbar")
        return toolbar
    }

    private fun saveEmployee(employee: EmployeeEntity?) {
        try {
            service.createEmployee(employee)

            updateList()
            closeEditor()
            Notification.show("Employee saved successfully")
        } catch (e: Exception) {
            Notification.show("Error saving employee: ${e.message}")
        }
    }

    private fun deleteEmployee(employee: EmployeeEntity?) {
        try {
            employee?.getId()?.let {
                service.deleteEmployee(it)
                updateList()
                closeEditor()
                Notification.show("Employee deleted successfully")
            }
        } catch (e: Exception) {
            Notification.show("Error deleting employee: ${e.message}")
        }
    }

    private fun addEmployee() {
        grid.asSingleSelect().clear()
        editEmployee(EmployeeEntity())
    }

    private fun editEmployee(employee: EmployeeEntity?) {
        employeeForm.employee = employee
        employeeDialog.open()
        addClassName("editing")
    }

    private fun closeEditor() {
        employeeForm.employee = null
        employeeDialog.close()
        removeClassName("editing")
    }

    private fun updateList() {
        grid.setItems(service.getAllEmployees())
    }

    private fun filterList() {
        if (filterText.value.isNotEmpty()) {
            grid.setItems(service.findByName(filterText.value))
        } else {
            updateList()
        }
    }
}