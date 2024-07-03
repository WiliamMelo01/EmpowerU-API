package org.wiliammelo.empoweru.configuration;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Azure Blob Storage beans.
 * This class is responsible for creating and configuring beans related to Azure Blob Storage,
 * allowing for easy injection and management within the Spring application context.
 */
@Configuration
public class AzureBlobBeans {

    // Connection string to Azure Blob Storage, injected from application properties
    @Value("${spring.cloud.azure.storage.blob.connection-string}")
    private String connectionString;

    /**
     * Creates a BlobContainerClient bean.
     * This bean is configured to interact with the "videos" blob container in Azure Blob Storage.
     *
     * @return A BlobContainerClient instance configured with the connection string and container name.
     */
    @Bean
    BlobContainerClient blobContainerClient() {
        return new BlobServiceClientBuilder()
                .connectionString(this.connectionString)
                .buildClient().getBlobContainerClient("videos");
    }

}
