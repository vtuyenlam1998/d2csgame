package com.d2csgame.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileUtils {
    public static String uploadImageToFileSystem(String uploadDirectory, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String newFileName = generateTimestampedFileName(fileName);
        Path filePath = Paths.get(uploadDirectory, newFileName);
        try {
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.write(filePath, file.getBytes());
            return filePath.toAbsolutePath().toString();
        } catch (Exception e) {
            log.error("Lưu file không thành công: {}", e.getMessage());
            throw e;
        }
    }

    public static void deleteFileFromSystem(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("Không thể xóa file: {}", filePath, e);
            throw e;
        }
    }

    private static String generateTimestampedFileName(String originalFileName) {
        String uniqueId = (System.currentTimeMillis() + "" + Math.round(Math.random() * 10));

        return uniqueId + "_" + originalFileName;
    }

//    public static byte[] downloadImageFromFileSystem(String fileName) throws IOException {
//        FileData fileData = fileRepository.findByName(fileName).orElseThrow(() -> new FileNotFoundException(fileName));
//        String filePath = fileData.getFilePath();
//        byte[] files = Files.readAllBytes(new File(filePath).toPath());
//        return files;
//    }

    public static MultipartFile[] convertFileToMultipartFile(File file) throws IOException {
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "application/pdf", input);
        return new MultipartFile[]{multipartFile};
    }
}
