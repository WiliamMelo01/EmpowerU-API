package org.wiliammelo.empoweru.clients;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service class for uploading files to Azure Blob Storage.
 * Implements the FileUploader interface to provide a method for uploading files.
 */
@Service
@AllArgsConstructor
public class AzureFileUploader implements FileUploader {

    private final BlobContainerClient blobContainerClient;

    /**
     * Uploads a file to Azure Blob Storage and returns the URL of the uploaded file.
     *
     * @param file The MultipartFile to be uploaded.
     * @return The URL of the uploaded file in Azure Blob Storage.
     * @throws IOException If an I/O error occurs during file upload.
     */
    @Override
    public String upload(MultipartFile file) throws IOException {

        String blobName = "/videos/" + file.getOriginalFilename();

        BlobClient blobClient = this.blobContainerClient.getBlobClient(blobName);

        blobClient.upload(file.getInputStream(), file.getSize(), true);

        return blobClient.getBlobUrl();
    }
}
