package com.example.demo.Moderator;

import com.example.demo.common.RequiredRole;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Handler pdf download
@RestController
public class PDFDownloadController {

    @Autowired
    private PDFService pdfService;

    // url
    @GetMapping("/api/reports/pdf/download")
    @RequiredRole("moderator")
    public ResponseEntity<byte[]> downloadPdf(HttpSession session) throws Exception {
        String preparedBy = null;
        Object userObj = session.getAttribute("username");
        if (userObj != null) preparedBy = userObj.toString();

        // generate pdf
        byte[] pdf = pdfService.generateFullReportPdf(preparedBy);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("moderator-report.pdf").build());
        
        // return pdf bytes
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
