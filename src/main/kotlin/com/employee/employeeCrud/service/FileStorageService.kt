package com.employee.employeeCrud.service

import org.apache.commons.io.FilenameUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.UUID

@Service
class FileStorageService(
    @Value("\${app.file.upload-dir:./uploads}")
    private val uploadDir: String
) {
    private val uploadPath: Path = Path.of(uploadDir)

    init {
        try {
            Files.createDirectories(uploadPath)
        } catch (e: IOException) {
            throw RuntimeException("Could not create upload directory!", e)
        }
    }

    fun storeFile(file: MultipartFile): String {
        val originalFilename = file.originalFilename ?: throw IllegalArgumentException("Filename cannot be null")
        val fileExtension = FilenameUtils.getExtension(originalFilename)

        // Generate unique filename
        val newFilename = "${UUID.randomUUID()}.$fileExtension"
        val targetLocation = uploadPath.resolve(newFilename)

        try {
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
            return newFilename
        } catch (e: IOException) {
            throw RuntimeException("Could not store file $originalFilename", e)
        }
    }

    fun deleteFile(filename: String) {
        try {
            val file = uploadPath.resolve(filename)
            Files.deleteIfExists(file)
        } catch (e: IOException) {
            throw RuntimeException("Error deleting file $filename", e)
        }
    }
}