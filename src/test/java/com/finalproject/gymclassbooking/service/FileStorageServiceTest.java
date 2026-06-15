package com.finalproject.gymclassbooking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileStorageServiceTest {

    @TempDir
    Path uploadDir;

    @Test
    void storesAllowedImageInUploadDirectory() throws Exception {
        FileStorageService service = new FileStorageService(uploadDir.toString());
        MockMultipartFile image = new MockMultipartFile(
                "imageFile",
                "class.png",
                "image/png",
                new byte[]{1, 2, 3}
        );

        String storedPath = service.store(image);

        assertThat(storedPath).startsWith("/uploads/").endsWith(".png");
        assertThat(Files.exists(uploadDir.resolve(storedPath.substring("/uploads/".length())))).isTrue();
    }

    @Test
    void rejectsNonImageUploads() throws Exception {
        FileStorageService service = new FileStorageService(uploadDir.toString());
        MockMultipartFile textFile = new MockMultipartFile(
                "imageFile",
                "notes.txt",
                "text/plain",
                "not an image".getBytes()
        );

        assertThatThrownBy(() -> service.store(textFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Only JPG");
    }
}
