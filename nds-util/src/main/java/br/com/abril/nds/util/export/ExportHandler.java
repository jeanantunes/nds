package br.com.abril.nds.util.export;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.DateUtil;

public class ExportHandler {
	
	public static <T, F, FT> ExportModel generateExportModel(F filter,
														 	 FT footer,
														 	 List<T> exportableList,
														 	 Class<T> exportableListClass) {
		
		if (exportableListClass == null 
				|| !exportableListClass.isAnnotationPresent(Exportable.class)) {
		
			throw new RuntimeException("A classe utilizada não é exportável!");
		}
	
		try {
			ExportModel exportModel = new ExportModel();
			
			List<ExportFilter> exportFilters = generateExportFilters(filter);
			
			List<ExportFooter> exportFooters = generateExportFooters(footer);
			
			List<ExportHeader> exportHeaders = new ArrayList<ExportHeader>();
			
			List<ExportRow> exportRows = new ArrayList<ExportRow>();
			
			for (T exportable : exportableList) {
				
				ExportRow exportRow = new ExportRow();
				
				List<ExportColumn> exportColumns = new ArrayList<ExportColumn>();
				
				for (Method method : exportableListClass.getMethods()) {
					
					ExportColumn exportColumn = 
						generateExportColumnFromMethod(method, exportHeaders, exportable);
					
					if (exportColumn != null) {
						
						exportColumns.add(exportColumn);
					}
				}
	
				for (Field field : exportableListClass.getDeclaredFields()) {
					
					ExportColumn exportColumn = 
						generateExportColumnFromField(field, exportHeaders, exportable);
					
					if (exportColumn != null) {
						
						exportColumns.add(exportColumn);
					}
				}
				
				exportRow.setColumns(exportColumns);
				
				exportRows.add(exportRow);
			}
			
			exportModel.setFilters(exportFilters);
			
			exportModel.setHeaders(exportHeaders);
			
			exportModel.setRows(exportRows);
			
			exportModel.setFooters(exportFooters);
			
			return exportModel;
			
		} catch (Exception e) {
			
			throw new RuntimeException("Erro ao exportar modelo!", e);
		}
	}
	
	private static <F> List<ExportFilter> generateExportFilters(F filter) 
																throws IllegalArgumentException, 
																	   IllegalAccessException, 
																	   InvocationTargetException {
		
		if (filter == null) {
			
			return null;
		}
		
		if (!filter.getClass().isAnnotationPresent(Exportable.class)) {
			
			throw new RuntimeException("A classe de filtro utilizada não é exportável!");
		}
		
		List<ExportFilter> exportFilters = new ArrayList<ExportFilter>();
		
		for (Method method : filter.getClass().getMethods()) {
			
			Export exportAnnotation = method.getAnnotation(Export.class);
			
			if (exportAnnotation != null) {
				
				Object methodReturn = method.invoke(filter, new Object[]{});
			
				exportFilters.add(generateExportFilter(methodReturn, exportAnnotation));
			}
		}
		
		for (Field field : filter.getClass().getDeclaredFields()) {
			
			Export exportAnnotation = field.getAnnotation(Export.class);
			
			if (exportAnnotation != null) {

				field.setAccessible(true);
				
				Object fieldValue = field.get(filter);
				
				exportFilters.add(generateExportFilter(fieldValue, exportAnnotation));
			}
		}
		
		return exportFilters;
	}
	
	private static ExportFilter generateExportFilter(Object value, Export exportAnnotation) {
		
		ExportFilter exportFilter = new ExportFilter();
		
		exportFilter.setLabel(exportAnnotation.label());
		
		exportFilter.setValue(getExportValue(value));
		
		exportFilter.setAlignment(exportAnnotation.alignment());
	
		return exportFilter;
	}
	
	private static <FT> List<ExportFooter> generateExportFooters(FT footer)
																 throws IllegalArgumentException, 
																        IllegalAccessException,
																	    InvocationTargetException {

		if (footer == null) {

			return null;
		}

		if (!footer.getClass().isAnnotationPresent(Exportable.class)) {

			throw new RuntimeException("A classe de rodapé utilizada não é exportável!");
		}

		List<ExportFooter> exportFooters = new ArrayList<ExportFooter>();

		for (Method method : footer.getClass().getMethods()) {

			Export exportAnnotation = method.getAnnotation(Export.class);

			if (exportAnnotation != null) {

				Object methodReturn = method.invoke(footer, new Object[] {});

				exportFooters.add(generateExportFooter(methodReturn, exportAnnotation));
			}
		}

		for (Field field : footer.getClass().getDeclaredFields()) {

			Export exportAnnotation = field.getAnnotation(Export.class);

			if (exportAnnotation != null) {

				field.setAccessible(true);

				Object fieldValue = field.get(footer);

				exportFooters.add(generateExportFooter(fieldValue, exportAnnotation));
			}
		}

		return exportFooters;
	}

	private static ExportFooter generateExportFooter(Object value, Export exportAnnotation) {
	
		ExportFooter exportFooter = new ExportFooter();
		
		exportFooter.setLabel(exportAnnotation.label());
		
		exportFooter.setValue(getExportValue(value));
		
		exportFooter.setAlignment(exportAnnotation.alignment());
		
		exportFooter.setHeaderToAlign(exportAnnotation.alignWithHeader());
		
		return exportFooter;
	}
	
	private static <T> ExportColumn generateExportColumnFromMethod(Method method, 
																   List<ExportHeader> exportHeaders,
																   T exportable) 
																   throws IllegalArgumentException, 
																  		  IllegalAccessException, 
																  		  InvocationTargetException {
		
		Export exportAnnotation = method.getAnnotation(Export.class);
		
		if (exportAnnotation != null) {
			
			processHeader(exportAnnotation, exportHeaders);
			
			Object methodReturn = method.invoke(exportable, new Object[]{});

			return new ExportColumn(getExportValue(methodReturn), exportAnnotation.alignment());
		}
		
		return null;
	}
	
	private static <T> ExportColumn generateExportColumnFromField(Field field, 
																  List<ExportHeader> exportHeaders,
																  T exportable) throws IllegalArgumentException, 
																 					   IllegalAccessException {
		
		Export exportAnnotation = field.getAnnotation(Export.class);
		
		if (exportAnnotation != null) {
			
			processHeader(exportAnnotation, exportHeaders);
			
			field.setAccessible(true);
			
			Object fieldValue = field.get(exportable);

			return new ExportColumn(getExportValue(fieldValue), exportAnnotation.alignment());
		}
		
		return null;
	}
	
	private static String getExportValue(Object value) {
		
		String columnValue = null;
		
		if (value != null) {

			if (value instanceof BigDecimal) {
				
				columnValue = ((BigDecimal) value).toString();
				
			} else if (value instanceof Date) {
				
				columnValue = DateUtil.formatarDataPTBR((Date) value);
				
			} else {
				
				columnValue = value.toString();
			}
		}
		
		return columnValue;
	}
	
	private static void processHeader(Export exportAnnotation, 
									  List<ExportHeader> exportHeaders) {
		
		ExportHeader exportHeader = new ExportHeader(exportAnnotation.label());
		
		if (!exportHeaders.contains(exportHeader)) {
			
			exportHeader.setAlignment(exportAnnotation.alignment());
			
			exportHeaders.add(exportHeader);
		}
	}

}
