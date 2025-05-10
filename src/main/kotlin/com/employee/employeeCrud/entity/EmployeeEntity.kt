package com.employee.employeeCrud.entity

import jakarta.persistence.*
import jakarta.validation.constraints.*
import java.time.LocalDate

@Entity
@Table(name = "employees")
class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var id: Long? = null

    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false)
    private var firstName: String = ""

    @NotBlank(message = "Surname is required")
    @Column(name = "surname", nullable = false)
    private var surname: String = ""

    @Column(name = "other_names")
    private var otherNames: String? = null

    @NotBlank(message = "Gender is required")
    @Column(nullable = false)
    private var gender: String = ""

    @Pattern(
        regexp = "^[+]?[(]?[0-9]{1,4}[)]?[-\\s\\.0-9]{9,}$",
        message = "Please provide a valid phone number. Example: +1234567890 or (123) 456-7890"
    )
    @Column(name = "phone_number")
    private var phoneNumber: String? = null


    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(nullable = false, unique = true)
    private var email: String = ""

    @Past(message = "Date of birth must be in the past")
    @Column(name = "date_of_birth")
    private var dateOfBirth: LocalDate? = null

    @Column(name = "joining_date")
    private var joiningDate: LocalDate? = null

    @Column(name = "street_address")
    private var streetAddress: String? = null

    @Column
    private var city: String? = null

    @Column
    private var state: String? = null

    @Column(name = "postal_code")
    private var postalCode: String? = null

    // Getters and Setters
    fun getId(): Long? {
        return id
    }

    fun setId(id: Long?) {
        this.id = id
    }

    open fun getPhoneNumber(): String? {return phoneNumber}
    open fun setPhoneNumber(phoneNumber: String?){this.phoneNumber = phoneNumber}

    fun getFirstName(): String {
        return firstName
    }

    fun setFirstName(firstName: String) {
        this.firstName = firstName
    }

    fun getSurname(): String {
        return surname
    }

    fun setSurname(surname: String) {
        this.surname = surname
    }

    fun getOtherNames(): String? {
        return otherNames
    }

    fun setOtherNames(otherNames: String?) {
        this.otherNames = otherNames
    }

    fun getGender(): String {
        return gender
    }

    fun setGender(gender: String) {
        this.gender = gender
    }

    fun getEmail(): String {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getDateOfBirth(): LocalDate? {
        return dateOfBirth
    }

    fun setDateOfBirth(dateOfBirth: LocalDate?) {
        this.dateOfBirth = dateOfBirth
    }

    fun getJoiningDate(): LocalDate? {
        return joiningDate
    }

    fun setJoiningDate(joiningDate: LocalDate?) {
        this.joiningDate = joiningDate
    }

    fun getStreetAddress(): String? {
        return streetAddress
    }

    fun setStreetAddress(streetAddress: String?) {
        this.streetAddress = streetAddress
    }

    fun getCity(): String? {
        return city
    }

    fun setCity(city: String?) {
        this.city = city
    }

    fun getState(): String? {
        return state
    }

    fun setState(state: String?) {
        this.state = state
    }

    fun getPostalCode(): String? {
        return postalCode
    }

    fun setPostalCode(postalCode: String?) {
        this.postalCode = postalCode
    }

    // Override equals method
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as EmployeeEntity
        return id != null && id == that.id
    }

    // Override hashCode method
    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    // Override toString method
    override fun toString(): String {
        return "EmployeeEntity(id=$id, firstName='$firstName', surname='$surname', email='$email')"
    }
}