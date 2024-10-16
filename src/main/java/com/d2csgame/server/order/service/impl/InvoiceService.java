package com.d2csgame.server.order.service.impl;

import com.d2csgame.entity.Order;
import com.d2csgame.entity.OrderItem;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
public class InvoiceService {
    @Value("${file.upload-dir.invoice}")
    private String invoiceUploadDir;
    @Value("${file.upload-dir.image}")
    private String imageUploadDir;


    public String generateInvoice(Order order) throws DocumentException, IOException {
        File directory = new File(invoiceUploadDir);
        if (!directory.exists()) {
            directory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
        }
        String filePath = invoiceUploadDir + "invoice_" + order.getId() + ".pdf";
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();

        // Thêm logo (hãy thay đường dẫn bằng logo của bạn)
        Image logo = Image.getInstance(imageUploadDir + "logo.jfif");
        logo.scaleToFit(100, 100);  // Kích thước logo
        logo.setAlignment(Element.ALIGN_RIGHT);  // Canh phải
        document.add(logo);

        // Thêm tiêu đề
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLUE);
        Paragraph title = new Paragraph("INVOICE", fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" "));  // Dòng trống

        // Thông tin khách hàng
        Font fontInfo = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        document.add(new Paragraph("Customer: " + order.getUser().getUsername(), fontInfo));
        document.add(new Paragraph("Order ID: " + order.getId(), fontInfo));
        document.add(new Paragraph("Order Date: " + order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), fontInfo));
        document.add(new Paragraph("Payment Date: " + order.getPaymentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), fontInfo));

        document.add(new Paragraph(" "));  // Dòng trống

        // Tạo bảng sản phẩm
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{1, 3, 1, 2});

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        PdfPCell h1 = new PdfPCell(new Phrase("STT", headerFont));
        PdfPCell h2 = new PdfPCell(new Phrase("Product", headerFont));
        PdfPCell h3 = new PdfPCell(new Phrase("Quantity", headerFont));
        PdfPCell h4 = new PdfPCell(new Phrase("Price", headerFont));

        // Thêm màu cho header
        h1.setBackgroundColor(BaseColor.DARK_GRAY);
        h2.setBackgroundColor(BaseColor.DARK_GRAY);
        h3.setBackgroundColor(BaseColor.DARK_GRAY);
        h4.setBackgroundColor(BaseColor.DARK_GRAY);

        table.addCell(h1);
        table.addCell(h2);
        table.addCell(h3);
        table.addCell(h4);

        Set<OrderItem> items = order.getItems();
        int index = 1;
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        for (OrderItem item : items) {
            PdfPCell cell1 = new PdfPCell(new Phrase(String.valueOf(index), cellFont));
            PdfPCell cell2 = new PdfPCell(new Phrase(item.getProduct().getName(), cellFont));
            PdfPCell cell3 = new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), cellFont));
            PdfPCell cell4 = new PdfPCell(new Phrase(String.format("%.2f", item.getPrice()), cellFont));

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            index++;
        }

        document.add(table);

        // Tổng tiền
        document.add(new Paragraph(" "));
        Font fontTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.RED);
        document.add(new Paragraph("Total Amount: " + String.format("%.2f", order.getTotalAmount()), fontTotal));

        // Thêm chữ ký hình ảnh (thay đường dẫn bằng chữ ký của bạn)
        document.add(new Paragraph(" "));
        Image signature = Image.getInstance(imageUploadDir + "game.png");
        signature.scaleToFit(100, 50);  // Kích thước chữ ký
        signature.setAlignment(Element.ALIGN_RIGHT);
        document.add(signature);

        document.close();

        return filePath;
    }
}