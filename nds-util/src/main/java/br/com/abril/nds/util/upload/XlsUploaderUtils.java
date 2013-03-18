package br.com.abril.nds.util.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public abstract class XlsUploaderUtils {
	
	public static List<KeyValue> returnKeyValueFromXls(File file) {
		
		List<KeyValue> list = new ArrayList<KeyValue>();
		
		try {
			
			FileInputStream xls = new FileInputStream(file);
			HSSFWorkbook workbook = new HSSFWorkbook(xls);
			HSSFSheet sheet = workbook.getSheetAt(0);
			
			int header = 0;
			int content = 1;
			
			Iterator<Row> rowIterator = sheet.rowIterator();
			while (rowIterator.hasNext()) {
				Row row = (Row) rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = (Cell) cellIterator.next();
					if (sheet.getRow(content) != null) {
						KeyValue keyValue = new KeyValue();
						keyValue.setKey(sheet.getRow(header).getCell(cell.getColumnIndex()).getStringCellValue());
						keyValue.setValue(returnCellValue(sheet.getRow(content).getCell(cell.getColumnIndex())));
						list.add(keyValue);
					}
				}
				content++;
			}
			
			xls.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	private static Object returnCellValue(Cell cell) {
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue();
			case Cell.CELL_TYPE_NUMERIC:
				return cell.getNumericCellValue();
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
		}
		return null;
	}	
}
