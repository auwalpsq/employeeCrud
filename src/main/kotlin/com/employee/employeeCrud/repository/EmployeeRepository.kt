package com.employee.employeeCrud.repository

import com.employee.employeeCrud.entity.EmployeeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : JpaRepository<EmployeeEntity, Long> {
    // Using a simpler method name
    fun findByFirstNameContainingOrSurnameContaining(
        firstName: String,
        surname: String
    ): List<EmployeeEntity>

    // Alternative: Using @Query
    @Query("SELECT e FROM EmployeeEntity e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(e.surname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    fun searchByName(@Param("searchTerm") searchTerm: String): List<EmployeeEntity>
}