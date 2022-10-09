package com.example.shoppingmall_restapi.service.image;

import com.example.shoppingmall_restapi.exception.FileUploadFailureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class LocalFileService implements FileService{

    //    @Value("${upload.image.location}")
    private String location = "/Users/kimtaesoo/Desktop/image/";

    @PostConstruct
    void postConstruct() {
        File dir = new File(location);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    @Override
    public void upload(MultipartFile file, String filename) {
        try {
            file.transferTo(new File(location + filename));
        } catch(IOException e) {
            throw new FileUploadFailureException();
        }
    }

    @Override
    public void delete(String filename) {
        new File(location + filename).delete();
    }
}


