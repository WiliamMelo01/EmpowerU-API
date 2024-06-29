package org.wiliammelo.empoweru.fileUpload;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AzureFileUploader implements FileUploader{

    @Autowired
    private BlobContainerClient blobContainerClient;

    @Override
    public String upload(MultipartFile file) throws IOException {

        String blobName = "/videos/" + file.getOriginalFilename();

        BlobClient blobClient = this.blobContainerClient.getBlobClient(blobName);

        blobClient.upload(file.getInputStream(), file.getSize(), true);

        return blobClient.getBlobUrl();
    }
}
