package org.wiliammelo.empoweru.file_upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Interface defining the contract for file uploading services.
 * Implementations of this interface are responsible for handling the upload of files
 * to a specified storage mechanism (e.g., local storage, cloud storage).
 */
public interface FileUploader {

    /**
     * Uploads a given file and returns the URL where the file is accessible.
     *
     * @param file The file to be uploaded. This is a multipart file, typically coming from an HTTP request.
     * @return The URL of the uploaded file, indicating where the file can be accessed.
     * @throws IOException If an I/O error occurs during the file upload process.
     */
    String upload(MultipartFile file) throws IOException;
}
