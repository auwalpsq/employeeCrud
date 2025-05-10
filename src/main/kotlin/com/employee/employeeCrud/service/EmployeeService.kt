package com.employee.employeeCrud.service

import com.employee.employeeCrud.entity.EmployeeEntity
import com.employee.employeeCrud.repository.EmployeeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class EmployeeService(private val employeeRepository: EmployeeRepository) {

    fun getAllEmployees(): List<EmployeeEntity> {
        return employeeRepository.findAll()
    }

    fun createEmployee(employee: EmployeeEntity?): EmployeeEntity {
        if(employee != null) {
            return employeeRepository.save(employee as EmployeeEntity)
        }
        return TODO("Provide the return value")
    }



    fun deleteEmployee(id: Long) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id)
        } else {
            throw NoSuchElementException("Employee not found with id: $id")
        }
    }

    // Updated search method using the new repository method
    fun findByName(searchTerm: String): List<EmployeeEntity> {
        // You can use either of these methods:
        // Option 1: Using the derived query method
        return employeeRepository.findByFirstNameContainingOrSurnameContaining(searchTerm, searchTerm)
        
        // Option 2: Using the @Query method
        // return employeeRepository.searchByName(searchTerm)
    }
}