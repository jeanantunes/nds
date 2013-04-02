package br.com.abril.nds.util.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;

/**
 * @author Thiago
 * Classe criada para fazer a engenharia reversa de uma planilha em formado XLS ou XLSX, passando as informações para um Bean anotado.
 */
public class XlsUploaderUtils {
	
	private static final String XLS = ".xls";
	private static final String XLSX = ".xlsx";
	
	/**
	 * @param clazz Bean para setar os valores do arquivo passado, nos fields anotados
	 * @param file File pode ser do tipo XLS e XLSX
	 * @return retorna uma lista de @param clazz
	 */
	public static <T> List<T> getBeanListFromXls(Class<T> clazz, File file) {
		
		List<T> list = new ArrayList<T>();
		String extension = file.getName().substring(file.getName().lastIndexOf("."));
		
		try {
			
			FileInputStream xls = new FileInputStream(file);
			
			if (extension.equals(XLS)) {
				HSSFWorkbook workbook = new HSSFWorkbook(xls);
				HSSFSheet sheet = workbook.getSheetAt(0);
				getContentUsingReflection(clazz, list, sheet);
				
			} else if (extension.equals(XLSX)) {
				XSSFWorkbook workbook = new XSSFWorkbook(xls);
				XSSFSheet sheet = workbook.getSheetAt(0);
				getContentUsingReflection(clazz, list, sheet);
			}
			
			xls.close();
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	private static <T, K extends Sheet> void getContentUsingReflection(Class<T> clazz, List<T> list, K sheet) throws InstantiationException,
			IllegalAccessException, InvocationTargetException {
		
		int header = 0;
		int content = 1;
		
		Iterator<Row> rowIterator = sheet.rowIterator();
		while (rowIterator.hasNext()) {
			
			if (sheet.getRow(content) != null) {
				
				T obj = clazz.newInstance();
				
				Row row = (Row) rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = (Cell) cellIterator.next();
					
					String xlsHeader = sheet.getRow(header).getCell(cell.getColumnIndex()).getStringCellValue();
					
					for (Field f : clazz.getDeclaredFields()) {
						if(f.isAnnotationPresent(XlsMapper.class)){
							XlsMapper mapper = f.getAnnotation(XlsMapper.class);
							if(mapper.value().equals(xlsHeader)){
								Cell cellIndex = sheet.getRow(content).getCell(cell.getColumnIndex(), Row.RETURN_BLANK_AS_NULL);
								if (cellIndex != null) {
									BeanUtils.setProperty(obj, f.getName(), returnCellValue(cellIndex));										
								}
							}
						}
					}
				}
				
				verifyObjBeforeAddToList(clazz, list, obj);
				content++;
			} else {
				break;
			}
		}
	}

	private static <T> void verifyObjBeforeAddToList(Class<T> clazz, List<T> list, T obj) throws IllegalAccessException {
		int nullable = 0;
		int annotatedFields = 0;
		Field[] fields = clazz.getDeclaredFields();
		
		for (Field f : fields) {			
			if(f.isAnnotationPresent(XlsMapper.class)){
				f.setAccessible(true);
				if (f.get(obj) == null) {
					nullable++;
				}
				annotatedFields++;
			}
		}
		
		if (nullable != annotatedFields) {
			list.add(obj);
		}
	}
	
	/**
	 * @param cell
	 * @return retorna um objeto boolan, numeric ou string de acordo com a celula recebida.
	 */
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
	
	
	public static File upLoadArquivo(UploadedFile xls) throws IOException, FileNotFoundException {
		
		File x = new File(xls.getFileName());
	    
		File destino = new File(xls.getFileName());  
	    destino.createNewFile();  
	    
	    InputStream stream = xls.getFile();  
	    IOUtils.copy(stream,new FileOutputStream(destino));
		return x;
	}
}
