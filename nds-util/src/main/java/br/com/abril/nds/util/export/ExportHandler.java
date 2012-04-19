package br.com.abril.nds.util.export;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export.Alignment;

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
			
			sortModel(exportModel);
			
			return exportModel;
			
		} catch (Exception e) {
			
			throw new RuntimeException("Erro ao exportar modelo!", e);
		}
	}
	
	private static void sortModel(ExportModel exportModel) {
		
		if (exportModel == null) {
			
			return;
		}
		
		sortList(exportModel.getFilters());
		sortList(exportModel.getHeaders());
		
		List<ExportRow> rows = exportModel.getRows();
		
		if (rows != null) {
			
			for (ExportRow row : rows) {
				
				sortList(row.getColumns());
			}
		}
	}
	
	private static <T extends Comparable<T>> void sortList(List<T> list) {
		
		if (list == null) {
			
			return;
		}
		
		Collections.sort(list);
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
		
		exportFilter.setExhibitionOrder(exportAnnotation.exhibitionOrder());
	
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

				Object exportObject = method.invoke(footer, new Object[] {});

				processExportFooter(exportFooters, exportAnnotation, exportObject);
			}
		}

		for (Field field : footer.getClass().getDeclaredFields()) {

			Export exportAnnotation = field.getAnnotation(Export.class);

			if (exportAnnotation != null) {

				field.setAccessible(true);

				Object exportObject = field.get(footer);
				
				processExportFooter(exportFooters, exportAnnotation, exportObject);
			}
		}

		return exportFooters;
	}

	private static void processExportFooter(List<ExportFooter> exportFooters, 
										    Export exportAnnotation,
										    Object exportObject) {
		
		if (exportObject instanceof Map) {
			 
			@SuppressWarnings("unchecked")
			Map<String, Object> footerMap = (Map<String, Object>) exportObject;
			
			if (exportAnnotation.label() != null && !exportAnnotation.label().trim().isEmpty()) {
				
				exportFooters.add(
					generateExportFooter(
						null, exportAnnotation.label(), 
							exportAnnotation.alignment(), exportAnnotation.alignWithHeader()));
			}
			
			for (Map.Entry<String, Object> entry : footerMap.entrySet()) {

				exportFooters.add(
					generateExportFooter(
						entry.getValue(), entry.getKey(), 
							exportAnnotation.alignment(), null));
			}
		} else {

			exportFooters.add(
				generateExportFooter(
					exportObject, exportAnnotation.label(), 
						exportAnnotation.alignment(), exportAnnotation.alignWithHeader()));
		}
	}
	
	private static ExportFooter generateExportFooter(Object value, 
												     String label, 
												     Alignment alignment, 
												     String alignWithHeader) {

		ExportFooter exportFooter = new ExportFooter();
		
		exportFooter.setLabel(label);
		
		exportFooter.setValue(getExportValue(value));
		
		exportFooter.setAlignment(alignment);
		
		exportFooter.setHeaderToAlign(alignWithHeader);
		
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

			return new ExportColumn(
				getExportValue(methodReturn), exportAnnotation.alignment(), exportAnnotation.exhibitionOrder());
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

			return new ExportColumn(
				getExportValue(fieldValue), exportAnnotation.alignment(), exportAnnotation.exhibitionOrder());
		}
		
		return null;
	}
	
	private static String getExportValue(Object value) {
		
		String exportValue = "";
		
		if (value != null) {

			if (value instanceof BigDecimal) {
				
				exportValue = ((BigDecimal) value).toString();
				
			} else if (value instanceof Date) {
				
				exportValue = DateUtil.formatarDataPTBR((Date) value);
				
			} else {
				
				exportValue = value.toString();
			}
		}
		
		return exportValue;
	}
	
	private static void processHeader(Export exportAnnotation, 
									  List<ExportHeader> exportHeaders) {
		
		ExportHeader exportHeader = new ExportHeader(exportAnnotation.label());
		
		if (!exportHeaders.contains(exportHeader)) {
			
			exportHeader.setAlignment(exportAnnotation.alignment());
			
			exportHeader.setExhibitionOrder(exportAnnotation.exhibitionOrder());
			
			exportHeaders.add(exportHeader);
		}
	}

}
