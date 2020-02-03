package ru.fds.tavrzcms3.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FilesService {
    File uploadFile(MultipartFile file, String prefixName) throws IOException;
}
