package com.example.demo.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class UploadExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    public String handleMultipartException(MultipartException ex, RedirectAttributes redirectAttributes) {
        // Provide a friendly flash message and redirect back to upload page
        String msg = "Upload failed";
        if (ex.getCause() != null && ex.getCause().getMessage() != null) {
            msg = ex.getCause().getMessage();
        } else if (ex.getMessage() != null) {
            msg = ex.getMessage();
        }

        // Simplify message for max size
        if (msg.toLowerCase().contains("size") || msg.toLowerCase().contains("max")) {
            msg = "File is too large. Maximum allowed size is 10MB.";
        }

        redirectAttributes.addFlashAttribute("uploadStatus", "error");
        redirectAttributes.addFlashAttribute("uploadError", msg);
        return "redirect:/memes/upload";
    }
}
