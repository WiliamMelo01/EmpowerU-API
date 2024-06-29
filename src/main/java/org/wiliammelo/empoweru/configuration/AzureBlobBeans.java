package org.wiliammelo.empoweru.configuration;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureBlobBeans {

    @Value("${spring.cloud.azure.storage.blob.connection-string}")
    private String connectionString;

    @Bean
    BlobContainerClient blobContainerClient() {
        return new BlobServiceClientBuilder()
                .connectionString(this.connectionString)
                .buildClient().getBlobContainerClient("videos");
    }

}
