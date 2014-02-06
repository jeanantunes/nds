package br.com.abril.nds.util.upload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;

/**
 * 
 * Classe criada para fazer a engenharia reversa de uma planilha em formado XLS
 * ou XLSX, passando as informações para um Bean anotado.
 * 
 * @author Thiago
 */
public class XlsUploaderUtils {
    
    private static final Logger LOGGER = Logger.getLogger(XlsUploaderUtils.class);
	
	private static final String XLS = ".xls";
	private static final String XLSX = ".xlsx";
	
	/**
	 * @param clazz Bean para setar os valores do arquivo passado, nos fields anotados
	 * @param file File pode ser do tipo XLS e XLSX
	 * @return retorna uma lista de @param clazz
	 */
	public static <T> List<T> getBeanListFromXls(Class<T> clazz, UploadedFile file) {
		
		List<T> list = new ArrayList<T>();
		String extension = file.getFileName().substring(file.getFileName().lastIndexOf("."));
		
		try {
			if (extension.equals(XLS)) {
				HSSFWorkbook workbook = new HSSFWorkbook(file.getFile());
				HSSFSheet sheet = workbook.getSheetAt(0);
				getContentUsingReflection(clazz, list, sheet);
			} else if (extension.equals(XLSX)) {
				XSSFWorkbook workbook = new XSSFWorkbook(file.getFile());
				XSSFSheet sheet = workbook.getSheetAt(0);
				getContentUsingReflection(clazz, list, sheet);
			}
		} catch (SecurityException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (FileNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (InstantiationException e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return list;
	}

	private static <T, K extends Sheet> void getContentUsingReflection(Class<T> clazz, List<T> list, K sheet) throws InstantiationException,
			IllegalAccessException, InvocationTargetException {
		
		Iterator<Row> rowIterator = sheet.rowIterator();
        Row headerRow = getHearderRow(sheet, rowIterator);

        if (headerRow != null) {
            while (rowIterator.hasNext()) {

                T obj = clazz.newInstance();

                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    Cell headerCell = headerRow.getCell(cell.getColumnIndex());
                    if (headerCell != null) {
                        String xlsHeader = headerCell.getStringCellValue();

                        for (Field f : clazz.getDeclaredFields()) {
                            if(f.isAnnotationPresent(XlsMapper.class)){
                                XlsMapper mapper = f.getAnnotation(XlsMapper.class);
                                if(mapper.value().equals(xlsHeader)){
                                    BeanUtils.setProperty(obj, f.getName(), returnCellValue(cell, f.getGenericType()));
                                }
                            }
                        }
                    }
                }

                verifyObjBeforeAddToList(clazz, list, obj);
            }
        }
	}

    /**
     * Verifies that the first row is the header row (LOGICAL == PHYSICAL)
     * and returns it.
     * @param sheet
     * @param rowIterator
     * @return
     */
    private static Row getHearderRow(Sheet sheet, Iterator<Row> rowIterator) {
        if (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (sheet.getFirstRowNum() == row.getRowNum()) {
                return row;
            }
        }
        return null;
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
	 *
     * @param cell
     * @param genericType
     * @return retorna um objeto boolan, numeric ou string de acordo com a celula recebida.
	 */
	private static Object returnCellValue(Cell cell, Type genericType) {
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue();
			case Cell.CELL_TYPE_NUMERIC:
                if (genericType == String.class) {
                return ((Double) cell.getNumericCellValue()).intValue(); // Não
                                                                         // queremos
                                                                         // casas
                                                                         // decimais
                                                                         // nestes
                                                                         // casos.
                }
                return cell.getNumericCellValue();
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
		}
		return null;
	}
}
