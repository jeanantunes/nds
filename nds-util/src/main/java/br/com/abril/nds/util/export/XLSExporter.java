package br.com.abril.nds.util.export;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class XLSExporter implements Exporter {

	@Override
	public <T, F, FT> void exportInOutputStream(F filter,
												FT footer,
												List<T> dataList,
												Class<T> listClass,
												OutputStream outputStream) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			
			HSSFSheet worksheet = workbook.createSheet("POI Worksheet");
	
			HSSFRow row1 = worksheet.createRow((short) 0);
	
			HSSFCell cellA1 = row1.createCell((short) 0);
			
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellA1.setCellStyle(cellStyle);
	
			HSSFCell cellB1 = row1.createCell((short) 1);
			
			cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellB1.setCellStyle(cellStyle);
	
			HSSFCell cellC1 = row1.createCell((short) 2);
			
			cellC1.setCellValue(true);
	
			HSSFCell cellD1 = row1.createCell((short) 3);
			
			cellD1.setCellValue(new Date());
			cellStyle = workbook.createCellStyle();
			cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
			cellD1.setCellStyle(cellStyle);
	
			workbook.write(outputStream);
			
		} catch (Exception e) {
			
			throw new RuntimeException("Erro ao gerar arquivo XLS!", e);
		}
	}
	
}
