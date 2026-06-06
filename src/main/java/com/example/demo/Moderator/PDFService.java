package com.example.demo.Moderator;

import com.example.demo.Moderator.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PDFService {

    @Autowired
    private ReportService reportService;

    public byte[] generateFullReportPdf(String preparedBy) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 36, 36, 64, 36);
        PdfWriter writer = PdfWriter.getInstance(doc, out);

        doc.open();

        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font normal = new Font(Font.HELVETICA, 10, Font.NORMAL);

        // Header
        Paragraph title = new Paragraph("Meme Voting Arena: Laporan Moderator", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        doc.add(new Paragraph(" "));

        // Meta
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String generatedAt = LocalDateTime.now().format(fmt);
        Paragraph meta = new Paragraph("Generated pada: " + generatedAt + "    Oleh: " + (preparedBy==null? "-" : preparedBy), normal);
        meta.setAlignment(Element.ALIGN_RIGHT);
        doc.add(meta);
        doc.add(Chunk.NEWLINE);

        // Tabel 1: Top 10 Uploaders
        doc.add(new Paragraph("1. Top 10 Uploaders", headerFont));
        doc.add(Chunk.NEWLINE);
        List<TopUploaderDTO> top = reportService.getTopUploaders(10);
        PdfPTable t1 = new PdfPTable(new float[]{1,3,4,2});
        t1.setWidthPercentage(100);
        t1.addCell(headerCell("Rank"));
        t1.addCell(headerCell("Username"));
        t1.addCell(headerCell("Name"));
        t1.addCell(headerCell("Total Upload"));
        int rank=1;
        for (TopUploaderDTO u : top) {
            t1.addCell(cell(String.valueOf(rank++), normal));
            t1.addCell(cell(u.getUsername(), normal));
            t1.addCell(cell(u.getName(), normal));
            t1.addCell(cell(String.valueOf(u.getTotalUploaded()), normal));
        }
        doc.add(t1);
        doc.add(Chunk.NEWLINE);

        // Tabel 2: Popularitas Kategori
        doc.add(new Paragraph("2. Popularitas Kategori (Total Meme)", headerFont));
        doc.add(Chunk.NEWLINE);
        List<CategoryPopularityDTO> catPop = reportService.getCategoryPopularity();
        PdfPTable t2 = new PdfPTable(new float[]{1,4,2});
        t2.setWidthPercentage(100);
        t2.addCell(headerCell("Rank"));
        t2.addCell(headerCell("Kategori"));
        t2.addCell(headerCell("Total Meme"));
        rank=1;
        for (CategoryPopularityDTO c : catPop) {
            t2.addCell(cell(String.valueOf(rank++), normal));
            t2.addCell(cell(c.getCategory(), normal));
            t2.addCell(cell(String.valueOf(c.getTotalMemes()), normal));
        }
        doc.add(t2);
        doc.add(Chunk.NEWLINE);

        // Tabel 3: Ranking Kategori berdasarkan Vote
        doc.add(new Paragraph("3. Ranking Kategori berdasarkan Vote", headerFont));
        doc.add(Chunk.NEWLINE);
        List<CategoryVotesDTO> catVotes = reportService.getCategoryVotes();
        PdfPTable t3 = new PdfPTable(new float[]{1,4,2});
        t3.setWidthPercentage(100);
        t3.addCell(headerCell("Rank"));
        t3.addCell(headerCell("Kategori"));
        t3.addCell(headerCell("Vote Total"));
        rank=1;
        for (CategoryVotesDTO c : catVotes) {
            t3.addCell(cell(String.valueOf(rank++), normal));
            t3.addCell(cell(c.getCategory(), normal));
            t3.addCell(cell(String.valueOf(c.getTotalVotes()), normal));
        }
        doc.add(t3);
        doc.add(Chunk.NEWLINE);

        // Tabel 4: Meme Trending berdasarkan Vote
        doc.add(new Paragraph("4. Meme Trending", headerFont));
        doc.add(Chunk.NEWLINE);
        List<TrendingMemeDTO> trending = reportService.getTrendingMemes(10);
        PdfPTable t4 = new PdfPTable(new float[]{1,5,2});
        t4.setWidthPercentage(100);
        t4.addCell(headerCell("Rank"));
        t4.addCell(headerCell("Title Meme"));
        t4.addCell(headerCell("Votes"));
        rank=1;
        for (TrendingMemeDTO m : trending) {
            t4.addCell(cell(String.valueOf(rank++), normal));
            t4.addCell(cell(m.getTitle(), normal));
            t4.addCell(cell(String.valueOf(m.getVotes()), normal));
        }
        doc.add(t4);
        doc.add(Chunk.NEWLINE);

        // Tabel 5: User-user paling aktif (comments + votes)
        doc.add(new Paragraph("5. User paling aktif", headerFont));
        doc.add(Chunk.NEWLINE);
        List<ActiveUserDTO> active = reportService.getMostActiveUsers(10);
        PdfPTable t5 = new PdfPTable(new float[]{1,3,2,2,2});
        t5.setWidthPercentage(100);
        t5.addCell(headerCell("Rank"));
        t5.addCell(headerCell("Username"));
        t5.addCell(headerCell("Comments"));
        t5.addCell(headerCell("Votes"));
        t5.addCell(headerCell("Activity Score"));
        rank=1;
        for (ActiveUserDTO a : active) {
            t5.addCell(cell(String.valueOf(rank++), normal));
            t5.addCell(cell(a.getUsername(), normal));
            t5.addCell(cell(String.valueOf(a.getCommentsCount()), normal));
            t5.addCell(cell(String.valueOf(a.getVotesCount()), normal));
            t5.addCell(cell(String.valueOf(a.getActivityScore()), normal));
        }
        doc.add(t5);
        doc.add(Chunk.NEWLINE);

        doc.close();
        return out.toByteArray();
    }

    private PdfPCell headerCell(String text) {
        Font f = new Font(Font.HELVETICA, 10, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Phrase(text, f));
        cell.setBackgroundColor(new Color(230,230,230));
        cell.setPadding(6);
        return cell;
    }
    private PdfPCell cell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(6);
        return cell;
    }
}
