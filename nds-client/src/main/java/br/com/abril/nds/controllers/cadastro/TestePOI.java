package br.com.abril.nds.controllers.cadastro;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class TestePOI {
	
	public static void main(String[] args) {
		try {
			FileInputStream fileInputStream = new FileInputStream("poi-test.xls");
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFWorkbook workbook2 = new HSSFWorkbook();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook2.write(bos);
			HSSFSheet worksheet = workbook.getSheet("POI Worksheet");
			HSSFRow row1 = worksheet.getRow(0);
			HSSFCell cellA1 = row1.getCell((short) 0);
			String a1Val = cellA1.getStringCellValue();
			HSSFCell cellB1 = row1.getCell((short) 1);
			String b1Val = cellB1.getStringCellValue();
			HSSFCell cellC1 = row1.getCell((short) 2);
			boolean c1Val = cellC1.getBooleanCellValue();
			HSSFCell cellD1 = row1.getCell((short) 3);
			Date d1Val = cellD1.getDateCellValue();

			System.out.println("A1: " + a1Val);
			System.out.println("B1: " + b1Val);
			System.out.println("C1: " + c1Val);
			System.out.println("D1: " + d1Val);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
