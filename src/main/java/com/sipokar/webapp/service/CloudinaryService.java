package com.sipokar.webapp.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile file, String folderName) {
        try {
            String originalName = file.getOriginalFilename();

            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folderName,
                            "resource_type", "raw",
                            "public_id", originalName,
                            "use_filename", true,
                            "unique_filename", true));

            return result.get("secure_url").toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}