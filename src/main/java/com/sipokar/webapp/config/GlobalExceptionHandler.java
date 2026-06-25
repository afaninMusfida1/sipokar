package com.sipokar.webapp.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSize(
            MaxUploadSizeExceededException ex,
            HttpServletRequest request,
            Model model) {

        model.addAttribute("error", "Ukuran file maksimal 5MB");
        return "umkm/daftar";
    }

    @ExceptionHandler(MultipartException.class)
    public String handleMultipart(
            MultipartException ex,
            HttpServletRequest request,
            Model model) {

        model.addAttribute("error", "Ukuran file terlalu besar atau format tidak valid");
        return "umkm/daftar";
    }
}