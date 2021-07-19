package com.masterteknoloji.trafficanalyzer.web.rest.util;
 
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.masterteknoloji.trafficanalyzer.web.rest.vm.VideoRecordSummaryVM;
 
public class ReportExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<VideoRecordSummaryVM> dataList;
     
    public ReportExcelExporter(List<VideoRecordSummaryVM> listUsers) {
        this.dataList = listUsers;
        workbook = new XSSFWorkbook();
    }
 
 
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Users");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "Time", style);      
        createCell(row, 1, "Line", style);       
        createCell(row, 2, "Type", style);    
        createCell(row, 3, "Count", style);
         
    }
     
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof java.sql.Timestamp) {
            cell.setCellValue(((Timestamp) value));
        
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        for (VideoRecordSummaryVM videoRecordSummaryVM : dataList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, videoRecordSummaryVM.getDate(), style);
            createCell(row, columnCount++, videoRecordSummaryVM.getDirectionName(), style);
            createCell(row, columnCount++, videoRecordSummaryVM.getType(), style);
            createCell(row, columnCount++, videoRecordSummaryVM.getCount().toString(), style);
             
        }
    }
     
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
}