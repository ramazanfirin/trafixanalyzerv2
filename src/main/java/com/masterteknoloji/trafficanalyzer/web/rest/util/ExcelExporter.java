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
 
public class ExcelExporter {
    
	public ExcelExporter() {
    }
 
    public XSSFSheet createSheet(XSSFWorkbook workbook,String sheetName) {
    	XSSFSheet sheet = workbook.createSheet(sheetName);
    
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
        
        return sheet;
    }
    
    public void writeData(XSSFWorkbook workbook,XSSFSheet sheet,List<VideoRecordSummaryVM> dataList) {
    	int rowCount = 1;
    	 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        Long totalCount = 0l;
        for (VideoRecordSummaryVM videoRecordSummaryVM : dataList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, videoRecordSummaryVM.getDate(), style);
            createCell(row, columnCount++, videoRecordSummaryVM.getDirectionName(), style);
            createCell(row, columnCount++, videoRecordSummaryVM.getType(), style);
            createCell(row, columnCount++, videoRecordSummaryVM.getCount().toString(), style);
            totalCount=totalCount+videoRecordSummaryVM.getCount().longValue();
             
        }
        Row row = sheet.createRow(rowCount++);
        int columnCount = 0;
        createCell(row, columnCount++, "", style);
        createCell(row, columnCount++, "", style);
        createCell(row, columnCount++, "TOTAL", style);
        createCell(row, columnCount++, String.valueOf(totalCount), style);
    }
     
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
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
     
    
     
    public void export(XSSFWorkbook workbook,HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
}