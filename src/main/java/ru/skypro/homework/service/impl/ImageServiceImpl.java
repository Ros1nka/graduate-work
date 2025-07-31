package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.FilePathException;
import ru.skypro.homework.exception.ImageUploadException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ImageServiceImpl implements ru.skypro.homework.service.ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Override
    public byte[] uploadAndSaveImage(String directory, String fileNamePrefix, MultipartFile image)
            throws FilePathException {

        validateImage(image);

        String extension = getFileExtension(image.getOriginalFilename());
        Path filePath = Path.of(directory, fileNamePrefix + extension);

        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new FilePathException("Failed to make directories or delete existing files");
        }

        try (InputStream is = image.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE, TRUNCATE_EXISTING);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {

            bis.transferTo(bos);

        } catch (IOException e) {
            logger.error("Failed to upload image to path: {}", filePath, e);
            throw new ImageUploadException("Failed to upload image");
        }

        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new ImageUploadException("Failed to read uploaded image");
        }
    }

    private void validateImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image cannot be null or empty");
        }

        String extension = getFileExtension(image.getOriginalFilename());
        if (!(".jpg".equals(extension) || ".jpeg".equals(extension) || ".png".equals(extension))) {
            throw new ImageUploadException("Unsupported image format. Only JPG, JPEG, PNG are allowed.");
        }
    }

    @Override
    public String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.')).toLowerCase();
    }
}
