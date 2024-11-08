package export;

import model.IntakeData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.opencsv.CSVWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DataExporter {
    private static final DateTimeFormatter DATE_FORMAT = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void exportToExcel(List<IntakeData> data, Path destination) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Intake Data");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Date", "Category", "Customer", "Company", "WJID", "INC", 
                              "Location", "Duration (min)"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            
            // Add data rows
            int rowNum = 1;
            for (IntakeData intake : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(intake.timestamp().format(DATE_FORMAT));
                row.createCell(1).setCellValue(intake.category().toString());
                row.createCell(2).setCellValue(intake.customerName() + " " + intake.customerLastName());
                row.createCell(3).setCellValue(intake.company());
                row.createCell(4).setCellValue(intake.categorySpecificData().getOrDefault("WJID", ""));
                row.createCell(5).setCellValue(intake.categorySpecificData().getOrDefault("INC", ""));
                row.createCell(6).setCellValue(intake.categorySpecificData().getOrDefault("location", ""));
                row.createCell(7).setCellValue(calculateDuration(intake));
            }
            
            // Autosize columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(destination.toFile())) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            throw new ExportException("Failed to export to Excel", e);
        }
    }

    public void exportToCSV(List<IntakeData> data, Path destination) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(destination.toFile()))) {
            // Write header
            String[] headers = {"Date", "Category", "Customer", "Company", "WJID", "INC", 
                              "Location", "Duration (min)"};
            writer.writeNext(headers);
            
            // Write data
            for (IntakeData intake : data) {
                String[] row = {
                    intake.timestamp().format(DATE_FORMAT),
                    intake.category().toString(),
                    intake.customerName() + " " + intake.customerLastName(),
                    intake.company(),
                    intake.categorySpecificData().getOrDefault("WJID", ""),
                    intake.categorySpecificData().getOrDefault("INC", ""),
                    intake.categorySpecificData().getOrDefault("location", ""),
                    String.valueOf(calculateDuration(intake))
                };
                writer.writeNext(row);
            }
        } catch (IOException e) {
            throw new ExportException("Failed to export to CSV", e);
        }
    }

    private long calculateDuration(IntakeData intake) {
        return java.time.Duration.between(intake.startTime(), intake.endTime()).toMinutes();
    }
} 