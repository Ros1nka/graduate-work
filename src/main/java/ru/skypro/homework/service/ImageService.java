package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.FilePathException;

import java.io.IOException;

public interface ImageService {

    byte[] uploadAndSaveImage(String directory, String fileNamePrefix, MultipartFile image)
            throws FilePathException;

    String getFileExtension(String filename);

    byte[] getAdImage(int id) throws IOException;

    byte[] getAdImage(String filename) throws IOException;

    byte[] getUserImage(String filename) throws IOException;

    byte[] getUserImage(int id) throws IOException;
}
