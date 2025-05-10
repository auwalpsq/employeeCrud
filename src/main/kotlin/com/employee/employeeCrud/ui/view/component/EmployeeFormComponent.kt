package com.employee.employeeCrud.ui.view.component

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.EmailField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.shared.Registration
import com.employee.employeeCrud.entity.EmployeeEntity
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import com.vaadin.flow.component.textfield.TextFieldVariant
import com.vaadin.flow.data.binder.ValidationException
import com.vaadin.flow.data.validator.RegexpValidator
import com.vaadin.flow.theme.lumo.LumoUtility
import com.vaadin.flow.theme.lumo.LumoUtility.AlignContent.CENTER
import java.time.LocalDate

class EmployeeFormComponent : VerticalLayout() {
    private val firstName = TextField("First Name")
    private val surname = TextField("Surname")
    private val otherNames = TextField("Other Names")
    private val gender = RadioButtonGroup<String>("Gender")
    private val phoneNumber = TextField("Phone Number").apply {
        //addThemeVariants(TextFieldVariant.LUMO_SMALL)
        placeholder = "Enter phone number"
        // Add pattern for phone number validation
        pattern = "^[+]?[(]?[0-9]{1,4}[)]?[-\\s\\./0-9]*$"
        // Helper text to show the expected format
        helperText = "Format: +1234567890 or (123) 456-7890"
    }

    private val email = EmailField("Email")
    private val dateOfBirth = DatePicker("Date of Birth")
    private val joiningDate = DatePicker("Joining Date")
    private val streetAddress = TextField("Street Address")
    private val city = TextField("City")
    private val state = TextField("State")
    private val postalCode = TextField("Postal Code")

    private val saveButton = Button("Save").apply {
        addThemeVariants(ButtonVariant.LUMO_PRIMARY)
    }
    private val deleteButton = Button("Delete").apply {
        addThemeVariants(ButtonVariant.LUMO_ERROR)
    }
    private val closeButton = Button("Cancel").apply {
        addThemeVariants(ButtonVariant.LUMO_TERTIARY)
    }

    private val binder = Binder(EmployeeEntity::class.java)
    var employee: EmployeeEntity?  = null
        set(value) {
            field = value
            binder.readBean(value)
            deleteButton.isVisible = value?.getId() != null
        }

    init {
        addClassName("employee-form")
        setupForm()
        add(createFormLayout(), createButtonLayout())
    }

    private fun setupForm() {
        gender.setItems("Male", "Female", "Other")
        dateOfBirth.max = LocalDate.now().minusYears(18)
        joiningDate.max = LocalDate.now()

        // Configure binder
        binder.forField(firstName)
            .asRequired("First name is required")
            .bind(EmployeeEntity::getFirstName, EmployeeEntity::setFirstName)

        binder.forField(surname)
            .asRequired("Surname is required")
            .bind(EmployeeEntity::getSurname, EmployeeEntity::setSurname)

        binder.forField(otherNames)
            .bind(EmployeeEntity::getOtherNames, EmployeeEntity::setOtherNames)

        binder.forField(gender)
            .asRequired("Gender is required")
            .bind(EmployeeEntity::getGender, EmployeeEntity::setGender)
        binder.forField(phoneNumber)
            .withValidator(
                RegexpValidator(
                    "Please enter a valid phone number",
                    "^[+]?[(]?[0-9]{1,4}[)]?[-\\s\\./0-9]*$"
                )
            )
            .bind(EmployeeEntity::getPhoneNumber, EmployeeEntity::setPhoneNumber)


        binder.forField(email)
            .asRequired("Email is required")
            .withValidator({ it.contains("@") }, "Must be a valid email address")
            .bind(EmployeeEntity::getEmail, EmployeeEntity::setEmail)

        binder.forField(dateOfBirth)
            .asRequired("Date of birth is required")
            .withValidator({ date ->
                date?.until(LocalDate.now())?.years ?: 0 >= 18
            }, "Employee must be at least 18 years old")
            .bind(EmployeeEntity::getDateOfBirth, EmployeeEntity::setDateOfBirth)

        binder.forField(joiningDate)
            .asRequired("Joining date is required")
            .bind(EmployeeEntity::getJoiningDate, EmployeeEntity::setJoiningDate)

        binder.forField(streetAddress)
            .bind(EmployeeEntity::getStreetAddress, EmployeeEntity::setStreetAddress)

        binder.forField(city)
            .bind(EmployeeEntity::getCity, EmployeeEntity::setCity)

        binder.forField(state)
            .bind(EmployeeEntity::getState, EmployeeEntity::setState)

        binder.forField(postalCode)
            .bind(EmployeeEntity::getPostalCode, EmployeeEntity::setPostalCode)

        // Button listeners
        saveButton.addClickListener { validateAndSave() }
        deleteButton.addClickListener { employee?.let { it1 -> fireEvent(DeleteEvent(this, it1)) } }
        closeButton.addClickListener { fireEvent(CloseEvent(this)) }
    }

    private fun createFormLayout(): FormLayout {
        return FormLayout().apply {
            add(
                firstName, surname, otherNames,
                gender, phoneNumber, email, dateOfBirth,
                joiningDate, streetAddress, city,
                state, postalCode
            )

            setResponsiveSteps(
                FormLayout.ResponsiveStep("0", 1),
                FormLayout.ResponsiveStep("500px", 2)
            )

            setColspan(streetAddress, 2)
        }
    }

    private fun createButtonLayout(): Component {
        return HorizontalLayout(saveButton, deleteButton, closeButton).apply {
            addClassNames("button-layout", LumoUtility.AlignContent.CENTER)
            setSizeFull()
        }
    }

    private fun validateAndSave() {
        try {
            binder.validate() // Validate before attempting to write
            if (binder.isValid) {
                binder.writeBean(employee)
                fireEvent(SaveEvent(this, employee))
            }
        } catch (e: ValidationException) {
            val errorMessages = e.fieldValidationErrors
                .mapNotNull { it.message }
                .joinToString("\n")
            Notification.show(
                "Please fix the following errors:\n$errorMessages",
                3000,
                Notification.Position.MIDDLE
            )
        } catch (e: Exception) {
            Notification.show(
                "Error saving employee: ${e.message}",
                3000,
                Notification.Position.MIDDLE
            )
            e.printStackTrace()
        }
    }


        // Events
    abstract class EmployeeFormEvent(source: EmployeeFormComponent) : ComponentEvent<EmployeeFormComponent>(source, false)

    class SaveEvent(source: EmployeeFormComponent, val employee: EmployeeEntity?) : EmployeeFormEvent(source)
    class DeleteEvent(source: EmployeeFormComponent, val employee: EmployeeEntity?) : EmployeeFormEvent(source)
    class CloseEvent(source: EmployeeFormComponent) : EmployeeFormEvent(source)

    fun addSaveListener(listener: ComponentEventListener<SaveEvent>): Registration =
        addListener(SaveEvent::class.java, listener)

    fun addDeleteListener(listener: ComponentEventListener<DeleteEvent>): Registration =
        addListener(DeleteEvent::class.java, listener)

    fun addCloseListener(listener: ComponentEventListener<CloseEvent>): Registration =
        addListener(CloseEvent::class.java, listener)
}