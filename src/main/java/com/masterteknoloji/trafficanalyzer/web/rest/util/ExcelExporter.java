package com.masterteknoloji.trafficanalyzer.web.rest.util;
 
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.masterteknoloji.trafficanalyzer.web.rest.vm.VideoRecordSummaryVM;
 
public class ExcelExporter {
    
	SimpleDateFormat dateFormatForTime = new SimpleDateFormat("HH:mm:ss");
	
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
    
    public void writeImage(XSSFWorkbook workbook,XSSFSheet sheet,byte[] image) {
    	final int pictureIndex = workbook.addPicture(image, Workbook.PICTURE_TYPE_PNG);
    	
    	CreationHelper helper = workbook.getCreationHelper();
    	   //Creates the top-level drawing patriarch.
    	   Drawing drawing = sheet.createDrawingPatriarch();

    	   //Create an anchor that is attached to the worksheet
    	   ClientAnchor anchor = helper.createClientAnchor();
    	   
    	   anchor.setCol1(1); //Column B
    	   anchor.setRow1(2); //Row 3
    	   anchor.setCol2(2); //Column C
    	   anchor.setRow2(3); //Row 4

    	   Picture pict = drawing.createPicture(anchor, pictureIndex);
    	   pict.resize();
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
        	cell.setCellValue(dateFormatForTime.format(value));
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