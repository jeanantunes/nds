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
			
			List<ExportFilter> exportFilters = obtainExportFilters(filter);
			
			List<ExportFooter> exportFooters = obtainExportFooters(footer);
			
			List<ExportHeader> exportHeaders = new ArrayList<ExportHeader>();
			
			List<ExportRow> exportRows = new ArrayList<ExportRow>();
			
			for (T exportable : exportableList) {
				
				ExportRow exportRow = new ExportRow();
				
				List<ExportColumn> exportColumns = 
					obtainExportColumns(exportableListClass, exportHeaders, exportable);
				
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

	private static <T> List<ExportColumn> obtainExportColumns(Class<T> clazz,
														      List<ExportHeader> exportHeaders,
														      T exportable) throws IllegalAccessException,
														    					   InvocationTargetException, 
														    					   IllegalArgumentException, 
														    					   NoSuchFieldException {
		
		List<ExportColumn> exportColumns = new ArrayList<ExportColumn>();
		
		if (hasExportableSuperClass(clazz)) {
			
			exportColumns.addAll(obtainExportColumns(clazz.getSuperclass(), exportHeaders, exportable));
		}
		
		for (Method method : clazz.getMethods()) {
			
			ExportColumn exportColumn = 
				generateExportColumnFromMethod(method, exportHeaders, exportable, clazz);
			
			if (exportColumn != null) {
				
				exportColumns.add(exportColumn);
			}
		}

		for (Field field : clazz.getDeclaredFields()) {
			
			ExportColumn exportColumn = 
				generateExportColumnFromField(field, exportHeaders, exportable, clazz);
			
			if (exportColumn != null) {
				
				exportColumns.add(exportColumn);
			}
		}
		
		return exportColumns;
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
	
	private static <F> List<ExportFilter> obtainExportFilters(F filter) 
															  throws IllegalArgumentException, 
																	 IllegalAccessException, 
																	 InvocationTargetException, 
																	 NoSuchFieldException, 
																	 SecurityException {

		if (filter == null) {
			
			return null;
		}
		
		if (!filter.getClass().isAnnotationPresent(Exportable.class)) {
			
			throw new RuntimeException("A classe de filtro utilizada não é exportável!");
		}
		
		return generateExportFilters(filter, filter.getClass());
	}
	
	private static <F> List<ExportFilter> generateExportFilters(F filter, 
																Class<?> clazz) 
																throws IllegalArgumentException, 
																	   IllegalAccessException, 
																	   InvocationTargetException, 
																	   NoSuchFieldException, 
																	   SecurityException {
		
		List<ExportFilter> exportFilters = new ArrayList<ExportFilter>();
		
		if (hasExportableSuperClass(clazz)) {
		
			exportFilters.addAll(generateExportFilters(filter, clazz.getSuperclass()));
		}

		for (Method method : clazz.getMethods()) {
			
			Export exportAnnotation = method.getAnnotation(Export.class);
			
			if (exportAnnotation != null) {
				
				Object methodReturn = method.invoke(filter, new Object[]{});
			
				exportFilters.add(obtainExportFilter(methodReturn, exportAnnotation, filter, clazz));
			}
		}
		
		for (Field field : clazz.getDeclaredFields()) {
			
			Export exportAnnotation = field.getAnnotation(Export.class);
			
			if (exportAnnotation != null) {

				field.setAccessible(true);
				
				Object fieldValue = field.get(filter);
				
				exportFilters.add(obtainExportFilter(fieldValue, exportAnnotation, filter, clazz));
			}
		}
		
		return exportFilters;
	}

	private static boolean hasExportableSuperClass(Class<?> clazz) {
		
		return clazz.getSuperclass() != null 
				&& clazz.getSuperclass().isAnnotationPresent(Exportable.class);
	}
	
	private static <F> ExportFilter obtainExportFilter(Object value, 
													   Export exportAnnotation,
													   F filter,
													   Class<?> clazz) throws NoSuchFieldException, 
												   						  	  SecurityException, 
												   						  	  IllegalArgumentException, 
												   						  	  IllegalAccessException {
		
		ExportFilter exportFilter = new ExportFilter();

		exportFilter.setLabel(getLabelValue(exportAnnotation, filter, clazz));
		
		exportFilter.setValue(getExportValue(value));
		
		exportFilter.setAlignment(exportAnnotation.alignment());
		
		exportFilter.setExhibitionOrder(exportAnnotation.exhibitionOrder());
	
		return exportFilter;
	}

	private static <F> String getLabelValue(Export exportAnnotation,
										    Object object, 
										    Class<?> clazz) throws NoSuchFieldException, 
										   						   IllegalAccessException {
		
		String label = exportAnnotation.label();
		
		String propertyToDynamicLabel = exportAnnotation.propertyToDynamicLabel();
		
		if (propertyToDynamicLabel != null
				&& !propertyToDynamicLabel.trim().isEmpty()) {
			
			Field dynamicField = clazz.getDeclaredField(propertyToDynamicLabel);
			
			dynamicField.setAccessible(true);
			
			Object dynamicFieldValue = dynamicField.get(object);
			
			String dynamicFieldValueToExport = getExportValue(dynamicFieldValue);
			
			label = label.concat(dynamicFieldValueToExport);
		}
		
		return label;
	}
	
	private static <FT> List<ExportFooter> obtainExportFooters(FT footer)
															   throws IllegalArgumentException, 
																      IllegalAccessException,
																      InvocationTargetException, 
																      NoSuchFieldException {

		if (footer == null) {

			return null;
		}
		
		if (!footer.getClass().isAnnotationPresent(Exportable.class)) {

			throw new RuntimeException("A classe de rodapé utilizada não é exportável!");
		}

		return generateExportFooters(footer, footer.getClass());
	}

	private static <FT> List<ExportFooter> generateExportFooters(FT footer,
																 Class<?> clazz)
																 throws IllegalAccessException, 
																  		InvocationTargetException, 
																  		NoSuchFieldException {

		List<ExportFooter> exportFooters = new ArrayList<ExportFooter>();
		
		if (hasExportableSuperClass(clazz)) {
			
			exportFooters.addAll(generateExportFooters(footer, clazz.getSuperclass()));
		}

		for (Method method : clazz.getMethods()) {

			Export exportAnnotation = method.getAnnotation(Export.class);

			if (exportAnnotation != null) {

				Object exportObject = method.invoke(footer, new Object[] {});

				processExportFooter(exportFooters, exportAnnotation, exportObject, clazz, footer);
			}
		}

		for (Field field : clazz.getDeclaredFields()) {

			Export exportAnnotation = field.getAnnotation(Export.class);

			if (exportAnnotation != null) {

				field.setAccessible(true);

				Object exportObject = field.get(footer);
				
				processExportFooter(exportFooters, exportAnnotation, exportObject, clazz, footer);
			}
		}

		return exportFooters;
	}

	@SuppressWarnings("unchecked")
	private static <FT> void processExportFooter(List<ExportFooter> exportFooters, 
										    Export exportAnnotation,
										    Object exportObject,
										    Class<?> clazz,
										    FT footer) throws NoSuchFieldException, 
										    				  IllegalAccessException {

		if (exportObject instanceof Map) {
			 
			Map<String, Object> footerMap = (Map<String, Object>) exportObject;
			
			if (exportAnnotation.label() != null && !exportAnnotation.label().trim().isEmpty()) {
				
				exportFooters.add(
					obtainExportFooter(
						null, getLabelValue(exportAnnotation, footer, clazz), 
							exportAnnotation.alignment(), 
								exportAnnotation.alignWithHeader(), false));
			}
			
			for (Map.Entry<String, Object> entry : footerMap.entrySet()) {

				exportFooters.add(
					obtainExportFooter(
						entry.getValue(), entry.getKey(), 
							exportAnnotation.alignment(), null, exportAnnotation.printVertical()));
			}
			
		} else {

			exportFooters.add(
				obtainExportFooter(
					exportObject, getLabelValue(exportAnnotation, footer, clazz), 
						exportAnnotation.alignment(), 
							exportAnnotation.alignWithHeader(), exportAnnotation.printVertical()));
		}
	}
	
	private static ExportFooter obtainExportFooter(Object value, 
												   String label, 
												   Alignment alignment, 
												   String alignWithHeader,
												   boolean verticalPrinting) {

		ExportFooter exportFooter = new ExportFooter();
		
		exportFooter.setLabel(label);
		
		exportFooter.setValue(getExportValue(value));
		
		exportFooter.setAlignment(alignment);
		
		exportFooter.setHeaderToAlign(alignWithHeader);
		
		exportFooter.setVerticalPrinting(verticalPrinting);
		
		return exportFooter;
	}
	
	private static <T> ExportColumn generateExportColumnFromMethod(Method method, 
																   List<ExportHeader> exportHeaders,
																   T exportable,
																   Class<?> clazz) 
																   throws IllegalArgumentException, 
																  		  IllegalAccessException, 
																  		  InvocationTargetException, 
																  		  NoSuchFieldException {
		
		Export exportAnnotation = method.getAnnotation(Export.class);
		
		if (exportAnnotation != null) {
			
			processHeader(exportAnnotation, exportHeaders, exportable, clazz);
			
			Object methodReturn = method.invoke(exportable, new Object[]{});

			return new ExportColumn(
				getExportValue(methodReturn), exportAnnotation.alignment(), exportAnnotation.exhibitionOrder());
		}
		
		return null;
	}
	
	private static <T> ExportColumn generateExportColumnFromField(Field field, 
																  List<ExportHeader> exportHeaders,
																  T exportable,
																  Class<?> clazz) throws IllegalArgumentException, 
																 					   	 IllegalAccessException, 
																 					   	 NoSuchFieldException {
		
		Export exportAnnotation = field.getAnnotation(Export.class);
		
		if (exportAnnotation != null) {
			
			processHeader(exportAnnotation, exportHeaders, exportable, clazz);
			
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
									  List<ExportHeader> exportHeaders,
									  Object object,
									  Class<?> clazz) throws NoSuchFieldException, IllegalAccessException {
		
		ExportHeader exportHeader = 
			new ExportHeader(getLabelValue(exportAnnotation, object, clazz));
		
		if (!exportHeaders.contains(exportHeader)) {
			
			exportHeader.setAlignment(exportAnnotation.alignment());
			
			exportHeader.setExhibitionOrder(exportAnnotation.exhibitionOrder());
			
			exportHeaders.add(exportHeader);
		}
	}

}
