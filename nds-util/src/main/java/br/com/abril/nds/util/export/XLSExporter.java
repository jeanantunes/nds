package br.com.abril.nds.util.export;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFTextbox;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.util.IOUtils;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.ImageUtil;
import br.com.abril.nds.util.ImageUtil.FormatoImagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.Export.Alignment;

/**
 * Classe responsável pela exportação de arquivos XLS.
 * 
 * @author Discover Technology
 * 
 */
public class XLSExporter implements Exporter {
	
	private CellStyle headerColumnCellStyle;
	
	private CellStyle headerColumnCellStyleRight;
	
	private CellStyle headerColumnCellStyleCenter;
	
	private CellStyle rowColumnCellStyle;
	
	private CellStyle rowColumnCellStyleRight;
	
	private CellStyle rowColumnCellStyleCenter;
	
	private CellStyle lastRowColumnCellStyle;
	
	private CellStyle lastRowColumnCellStyleRight;
	
	private CellStyle lastRowColumnCellStyleCenter;
	
	private CellStyle evenRowColumnCellStyle;
	
	private CellStyle evenRowColumnCellStyleRight;
	
	private CellStyle evenRowColumnCellStyleCenter;
	
	private CellStyle evenLastRowColumnCellStyle;
	
	private CellStyle evenLastRowColumnCellStyleRight;
	
	private CellStyle evenLastRowColumnCellStyleCenter;
	
	private CellStyle footerCellStyle;
	
	private CellStyle footerCellStyleRight;
	
	private CellStyle footerCellStyleCenter;
	
	private CellStyle labelFooterCellStyle;
	
	private CellStyle labelFooterCellStyleRight;
	
	private CellStyle labelFooterCellStyleCenter;
	
	private DataFormat brazilianCurrencyDF;
	
	private DataFormat decimalValueDF;
	
	private static final String decimalValueMask = "0.00";
	
	private static final String brazilianCurrencyMask = "R$ #,##0.00";
	

	@Override
	public <T, F, FT> void inOutputStream(String name,
										  NDSFileHeader ndsFileHeader,
										  F filter,
										  FT footer,
										  List<T> dataList,
										  Class<T> listClass,
										  OutputStream outputStream) {
		
		try {
			
			ExportModel exportModel = 
				ExportHandler.generateExportModel(filter, footer, dataList, listClass);
			
			if (exportModel == null) {
				
				return;
			}
			
			HSSFWorkbook workbook = new HSSFWorkbook();
			
			Sheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(name));
			
			sheet.getPrintSetup().setLandscape(true);
			
			this.configurePalletes(sheet);
			
			CreationHelper creationHelper = workbook.getCreationHelper();
			
			int lastRowNum = this.createSheetFilter(sheet, creationHelper, exportModel.getFilters());
			
			lastRowNum = this.createSheetMainDataHeaders(
				sheet, creationHelper, exportModel.getHeaders(), lastRowNum);
			
			lastRowNum = this.createSheetMainDataRows(sheet, exportModel.getRows(), lastRowNum);
			
			this.createSheetFooter(sheet, exportModel.getFooters(), exportModel.getHeaders(), lastRowNum);
			
			this.createSheetHeader(sheet, creationHelper, ndsFileHeader);
			
			workbook.write(outputStream);
			
			outputStream.close();
			
		} catch (Exception e) {
			
			throw new RuntimeException("Erro ao gerar arquivo XLS!", e);
		}
	}
	
	private void createSheetHeader(Sheet sheet,
								   CreationHelper creationHelper,
								   NDSFileHeader ndsFileHeader) throws IOException {

		this.createHeaderBackground(sheet, creationHelper);
		
		this.createHeaderLogo(sheet, creationHelper, ndsFileHeader.getLogo());
		
		this.createHeaderTextBoxDadosDistribuidor(sheet, ndsFileHeader);
		
		this.createHeaderTextBoxOutrosDados(sheet, ndsFileHeader);
	}
	
	private int createSheetFilter(Sheet sheet,
			   					  CreationHelper creationHelper,
			   					  List<ExportFilter> filters) {

		int startRowNum = 5;
		
		if (filters == null || filters.isEmpty()) {
			
			return startRowNum - 1;
		}
		
		Row row = sheet.createRow(startRowNum);
		
		Cell cell = row.createCell(0);
		
		cell.setCellValue(
			this.getRichTextString(
				"Filtro de Pesquisa", creationHelper, sheet, (short) 14, "Calibri", true, false));
		
		sheet.addMergedRegion(new CellRangeAddress(startRowNum, startRowNum, 0, 2));
		
		int filterCellOffset = 9;
		
		int filterCells = 0;
		
		Row filterRow = null;
		
		for (ExportFilter exportFilter : filters) {
			
			String filterString = 
				exportFilter.getLabel() + ": " + StringUtils.defaultString(exportFilter.getValue());
			
			RichTextString richTextFiltro =
				this.getRichTextString(
					filterString, creationHelper, sheet, (short) 11, "Calibri", false, false);
			
			if (filterRow == null) {
				
				filterRow = sheet.createRow(startRowNum + 1);
				
				filterCells = 0;
				
			} else if ((filterCells % filterCellOffset) == 0) {

				filterRow = sheet.createRow(filterRow.getRowNum() + 1);
				
				filterCells = 0;
			}

			Cell filterCell = filterRow.createCell(filterCells);
			
			filterCell.setCellValue(richTextFiltro);

			sheet.addMergedRegion(
				new CellRangeAddress(filterRow.getRowNum(), filterRow.getRowNum(), filterCells, filterCells + 1));
			
			filterCells = filterCells + 3;
		}
		
		return filterRow.getRowNum();
	}
	
	private int createSheetMainDataHeaders(Sheet sheet,
										   CreationHelper creationHelper,
										   List<ExportHeader> headers,
										   int lastRowNum) {

		int startRowNum = lastRowNum + 2;
		
		Row row = sheet.createRow(startRowNum);
		
		Cell cell = row.createCell(0);
		
		cell.setCellValue(
			this.getRichTextString(
				"Itens Pesquisados", creationHelper, sheet, (short) 14, "Calibri", true, false));
		
		sheet.addMergedRegion(new CellRangeAddress(startRowNum, startRowNum, 0, 2));
		
		if (headers == null || headers.isEmpty()) {
			
			return startRowNum;
		}
		
		row = sheet.createRow(startRowNum + 1);
		
		int cellNum = 0;
		
		Cell firstCell = null;

		Cell lastCell = null;
		
		for (ExportHeader exportHeader : headers) {
			
			String headerString = exportHeader.getValue() == null ? "" : exportHeader.getValue();

			cell = row.createCell(cellNum);
			
			if (cellNum == 0) {
				
				firstCell = cell;
			}
			
			cell.setCellValue(headerString);
			
			CellStyle cellStyle = this.getHeaderColumnCellStyle(sheet, exportHeader.getAlignment());
			
			cell.setCellStyle(cellStyle);

			lastCell = cell;

			int totalUniversal = 4000 * headers.size();
			
            float patternPercentage = (float) 400000 / totalUniversal;
			
			int width = exportHeader.getWidthPercent() == null ? 4000 
						: Math.round(exportHeader.getWidthPercent() * 4000 / patternPercentage);
			
			sheet.setColumnWidth(cellNum, width);
			
			cellNum++;
		}
		
		CellReference firstCellReference = new CellReference(firstCell);
		
		CellReference lastCellReference = new CellReference(lastCell);

		sheet.setAutoFilter(
			CellRangeAddress.valueOf(
				firstCellReference.formatAsString() + ":" + lastCellReference.formatAsString()));
		
		return startRowNum + 1;
	}

	private int createSheetMainDataRows(Sheet sheet,
									    List<ExportRow> rows,
									    int lastRowNum) {
		
		if (rows == null || rows.isEmpty()) {
					
			return lastRowNum;
			
		}
		
		int startRowNum = lastRowNum + 1;
		
		int rowNum = 0;
		
		for (ExportRow exportRow : rows) {
			
			Row row = sheet.createRow(startRowNum++);
			
			int columnsSize = exportRow.getColumns().size();
			
			int cellNum = 0;
			
			for (ExportColumn exportColumn : exportRow.getColumns()) {
				
				String columnString = exportColumn.getValue() == null ? "" : exportColumn.getValue();
				
				Cell cell = row.createCell(cellNum++);
				
				CellStyle cellStyle = this.getRowColumnCellStyle(
						sheet, ((rowNum % 2) != 0), (columnsSize == cellNum),
						exportColumn.getAlignment());
				
				carregarValorDaCelulaEFormatacao(
						sheet,
						cell, 
						exportColumn.getColumnType(), 
						columnString,
						cellStyle);
			
			}
			
			rowNum++;
		}
		
		return startRowNum;
	}
	
	
	private void carregarValorDaCelulaEFormatacao(
			Sheet sheet, 
			Cell cell, 
			ColumType columType, 
			String columnString, 
			CellStyle cellStyle) {
	
		if(	ColumType.NUMBER.equals(columType)  || 
			ColumType.INTEGER.equals(columType) || 
			ColumType.DECIMAL.equals(columType) || 
			ColumType.MOEDA.equals(columType) ) {
				
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				
				try {
					
					if(columnString.contains(",")) {
						columnString = columnString.replace(".", "").replace(",",".");
						cell.setCellValue(Double.valueOf(columnString));
					} else {
						
						if(Util.isLong(columnString)) {
							cell.setCellValue(Long.parseLong(columnString));
						} else {
							cell.setCellValue(columnString);
						}
					}
					
				} catch(Exception e) {
					
					cell.setCellValue(columnString);
					
				}
				
				
			} else {
				
				cell.setCellValue(columnString);
				
			}
							
			
			if(brazilianCurrencyDF == null) {
				brazilianCurrencyDF = sheet.getWorkbook().createDataFormat();
			}
			
			if(decimalValueDF == null) {
				decimalValueDF = sheet.getWorkbook().createDataFormat();
			}
			
			if (ColumType.DECIMAL.equals(columType)) {
				
				CellStyle style = sheet.getWorkbook().createCellStyle();
				style.cloneStyleFrom(cellStyle);
				style.setDataFormat(decimalValueDF.getFormat(decimalValueMask));
				cellStyle = style;
				
			} else if(ColumType.MOEDA.equals(columType)) {

				CellStyle style = sheet.getWorkbook().createCellStyle();
				style.cloneStyleFrom(cellStyle);
				style.setDataFormat(brazilianCurrencyDF.getFormat(brazilianCurrencyMask));
				cellStyle = style;
				
			}
			
			cell.setCellStyle(cellStyle);
		
		
	}
	
	private void createSheetFooter(Sheet sheet,
								   List<ExportFooter> footers,
								   List<ExportHeader> headers,
								   int lastRowNum) {
		
		if (footers == null || footers.isEmpty()) {
			
			return;
		}
		
		int newRowNum = lastRowNum + 2;
		
		Row row = sheet.createRow(newRowNum);
		
		int cellNum = 0;
		
		for (ExportFooter exportFooter : footers) {
			
			if (exportFooter.getHeaderToAlign() != null 
					&& !exportFooter.getHeaderToAlign().trim().isEmpty()) {

				Integer headerIndex = null; 
				
				int headerCount = 0;
				
				for (ExportHeader exportHeader : headers) {
					
					if (exportHeader.getValue().equals(exportFooter.getHeaderToAlign())) {
						
						headerIndex = headerCount;
						
						break;
					}
					
					headerCount++;
				}
				
				if (headerIndex != null) {
					
					if (!exportFooter.getLabel().trim().isEmpty()) {
						
						headerIndex--;
						
					}
					
					cellNum = headerIndex;
				}
			}
			
			boolean hasLabel = !exportFooter.getLabel().trim().isEmpty();
			
			if (hasLabel) {
				
				Cell labelCell = row.createCell(++cellNum);
				
				labelCell.setCellValue(exportFooter.getLabel());
				
				CellStyle cellStyle = this.getFooterCellStyle(sheet, true, exportFooter.getAlignment());
				
				labelCell.setCellStyle(cellStyle);
			}
			
			Cell cell = row.createCell(cellNum);
			
			CellStyle cellStyle = this.getFooterCellStyle(sheet, false, exportFooter.getAlignment());

			carregarValorDaCelulaEFormatacao(
					sheet, 
					cell, 
					exportFooter.getColumnType(), 
					exportFooter.getValue(), 
					cellStyle);
			
			cellNum++;
			
			if (exportFooter.isVerticalPrinting()) {
				
				row = sheet.createRow(++newRowNum);
				
				cellNum--;
				
				if (hasLabel) {
					
					cellNum--;
				}
			}
		}
	}
	
	private void createHeaderTextBoxDadosDistribuidor(Sheet sheet, NDSFileHeader ndsFileHeader) {
		
		HSSFPatriarch patriarch = (HSSFPatriarch) sheet.createDrawingPatriarch();

		HSSFClientAnchor clientAnchorDistribuidor = new HSSFClientAnchor();
		
		clientAnchorDistribuidor.setCol1(0);
		clientAnchorDistribuidor.setCol2(8);
		
		clientAnchorDistribuidor.setRow1(1);
		clientAnchorDistribuidor.setRow2(4);
		
		clientAnchorDistribuidor.setDx1(800);
		clientAnchorDistribuidor.setDy1(0);
		
		clientAnchorDistribuidor.setDx2(0);
		clientAnchorDistribuidor.setDy2(0);
		
		HSSFTextbox textBoxDadosDistribuidor = patriarch.createTextbox(clientAnchorDistribuidor);
		
		textBoxDadosDistribuidor.setNoFill(true);
		
		textBoxDadosDistribuidor.setLineStyle(HSSFTextbox.LINESTYLE_NONE);
	    
	    String nomeDistribuidor = StringUtils.defaultString(ndsFileHeader.getNomeDistribuidor());
	    String cnpjDistribuidor = StringUtils.defaultString(ndsFileHeader.getCnpjDistribuidor());
	    
	    String dadosDistribuidor = nomeDistribuidor;
	    
	    if (!cnpjDistribuidor.isEmpty()) {
	    	
	    	dadosDistribuidor += "\nCNPJ: " + cnpjDistribuidor;
	    }
	    
	    HSSFRichTextString richTextDadosDistribuidor = new HSSFRichTextString(dadosDistribuidor);

	    Font font = this.getFont(sheet, "Calibri", (short) 11, true, false);
	    
	    richTextDadosDistribuidor.applyFont(0, nomeDistribuidor.length(), font);
	    
	    textBoxDadosDistribuidor.setString(richTextDadosDistribuidor);
	}
	
	private void createHeaderTextBoxOutrosDados(Sheet sheet, NDSFileHeader ndsFileHeader) {
		
		HSSFPatriarch patriarch = (HSSFPatriarch) sheet.createDrawingPatriarch();

		HSSFClientAnchor clientAnchorOutros = new HSSFClientAnchor();
		
		clientAnchorOutros.setCol1(5);
		clientAnchorOutros.setCol2(8);
		
		clientAnchorOutros.setRow1(1);
		clientAnchorOutros.setRow2(4);
		
		clientAnchorOutros.setDx1(500);
		clientAnchorOutros.setDy1(0);
		
		clientAnchorOutros.setDx2(0);
		clientAnchorOutros.setDy2(0);
		
		HSSFTextbox textBoxOutrosDados = patriarch.createTextbox(clientAnchorOutros);
		
		textBoxOutrosDados.setNoFill(true);
		
		textBoxOutrosDados.setLineStyle(HSSFTextbox.LINESTYLE_NONE);
	    
	    String dataAtual = ndsFileHeader.getData() == null 
	    				 ? "" : DateUtil.formatarDataPTBR(ndsFileHeader.getData());
	    
	    String labelDia = "Dia: ";
	    
	    String nomeUsuario = ndsFileHeader.getNomeUsuario() == null ? ""  : ndsFileHeader.getNomeUsuario();
	    
	    String outrosDados;
	    
	    if (!dataAtual.isEmpty()) {
	    	
	    	outrosDados = labelDia + dataAtual + "\n" + nomeUsuario;
	    	
	    } else {
	    	
	    	outrosDados = "\n" + nomeUsuario;
	    }
	    
	    HSSFRichTextString richTextDadosDistribuidor = new HSSFRichTextString(outrosDados);

	    Font font = this.getFont(sheet, "Calibri", (short) 11, true, false);
	    
	    if (!dataAtual.isEmpty()) {
	    	
	    	richTextDadosDistribuidor.applyFont(0, labelDia.length(), font);
	    }
	    
	    textBoxOutrosDados.setString(richTextDadosDistribuidor);
	}
	
	private void createHeaderLogo(Sheet sheet,
								  CreationHelper creationHelper, InputStream logo){
		
		if(logo == null){
			return;
		}
		
		byte[] bytes = ImageUtil.redimensionar(logo,80, 70,FormatoImagem.PNG);
		
	    int pictureIdx = sheet.getWorkbook().addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
	   
	    Drawing drawing = sheet.createDrawingPatriarch();

	    ClientAnchor anchor = creationHelper.createClientAnchor();
	    
		anchor.setCol1(0);
		anchor.setCol2(0);
		
		anchor.setRow1(0);
		anchor.setRow2(0);
		
		anchor.setDx1(0);
		anchor.setDy1(0);
		
		anchor.setDx2(0);
		anchor.setDy2(0);
	    
	    Picture picture = drawing.createPicture(anchor, pictureIdx);
	    
	    picture.getPreferredSize().setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
	}
	
	private void createHeaderBackground(Sheet sheet,
										CreationHelper creationHelper) throws IOException {

		InputStream inputStream = 
			Thread.currentThread().getContextClassLoader().getResourceAsStream("bg_header.jpg");
		
		byte[] bytes = IOUtils.toByteArray(inputStream);
		
		int pictureIdx = sheet.getWorkbook().addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
		
		inputStream.close();
		
		Drawing drawing = sheet.createDrawingPatriarch();
		
		ClientAnchor anchor = creationHelper.createClientAnchor();
		
		anchor.setCol1(0);
		anchor.setCol2(0);
		
		anchor.setRow1(0);
		anchor.setRow2(3);
		
		anchor.setDx1(7000);
		anchor.setDx2(0);
		
		anchor.setDy1(200);
		anchor.setDy2(0);
		
		Picture picture = drawing.createPicture(anchor, pictureIdx);
		
		picture.resize();
	}
	
	private Font getFont(Sheet sheet, String name, short size, boolean bold, boolean italic) {
		
		Font font = sheet.getWorkbook().createFont();
		
		if (bold) {
			
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		}
	
		font.setItalic(italic);
	    
	    font.setFontHeightInPoints(size);
	    
	    font.setFontName(name);
	    
	    return font;
	}
	
	private RichTextString getRichTextString(String string, 
											 CreationHelper creationHelper,
											 Sheet sheet,
											 short fontSize,
											 String fontName,
											 boolean bold, 
											 boolean italic) {
		
		RichTextString richTextString = creationHelper.createRichTextString(string);
		
		richTextString.applyFont(this.getFont(sheet, fontName, fontSize, bold, italic));

		return richTextString;
	}
	
	private CellStyle getHeaderColumnCellStyle(Sheet sheet, Alignment alignment) {
	    
		switch (alignment) {
	    
	    	case CENTER:
	    		
	    		if (this.headerColumnCellStyleCenter != null) {
	    			
	    			return this.headerColumnCellStyleCenter;
	    		}
	    		
	    		break;
	    		
	    	case RIGHT:
	    		
	    		if (this.headerColumnCellStyleRight != null) {
	    			
	    			return this.headerColumnCellStyleRight;
	    		}
	    		
	    		break;
	    		
	    	case LEFT:
	    		
	    		if (this.headerColumnCellStyle != null) {
	    			
	    			return this.headerColumnCellStyle;
	    		}
	    		
	    		break;
	    }
		
	    CellStyle style = sheet.getWorkbook().createCellStyle();
		
		style.setFillForegroundColor(HSSFColor.BLUE.index);
		
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    
	    style.setBorderBottom(CellStyle.BORDER_THIN);
	    style.setBottomBorderColor(HSSFColor.LIGHT_BLUE.index);
	    style.setBorderLeft(CellStyle.BORDER_THIN);
	    style.setLeftBorderColor(HSSFColor.LIGHT_BLUE.index);
	    style.setBorderRight(CellStyle.BORDER_THIN);
	    style.setRightBorderColor(HSSFColor.LIGHT_BLUE.index);
	    style.setBorderTop(CellStyle.BORDER_THIN);
	    style.setTopBorderColor(HSSFColor.LIGHT_BLUE.index);
	    
	    style.setAlignment(this.getAlignment(alignment));

	    Font font = this.getFont(sheet, "Arial", (short) 10, true, false);
	    
	    font.setColor(HSSFColor.WHITE.index);
	    
	    style.setFont(font);
	    
	    switch (alignment) {
	    
	    	case CENTER:
	    		this.headerColumnCellStyleCenter = style;
	    		break;
	    	case RIGHT:
	    		this.headerColumnCellStyleRight = style;
	    		break;
	    	case LEFT:
	    		this.headerColumnCellStyle = style;
	    		break;
	    }

	    return style;
	}
	
	private CellStyle getRowColumnCellStyle(Sheet sheet, boolean even, boolean lastCell, Alignment alignment) {
		
		switch (alignment) {
	    
	    	case CENTER:

	    		if (even 
	    				&& lastCell
	    				&& this.evenLastRowColumnCellStyleCenter != null) {
	    			
	    			return this.evenLastRowColumnCellStyleCenter;
	    			
	    		} else if (even
	    					&& !lastCell
	    					&& this.evenRowColumnCellStyleCenter != null) {
	    			
	    			return this.evenRowColumnCellStyleCenter;
	    			
	    		} else if (lastCell
	    					&& !even
	    					&& this.lastRowColumnCellStyleCenter != null) {
	    			
	    			return this.lastRowColumnCellStyleCenter;
	    			
	    		} else if (!even && !lastCell && this.rowColumnCellStyleCenter != null) {
	    			
	    			return this.rowColumnCellStyleCenter;
	    		}
	    		
	    		break;
	    		
	    	case RIGHT:
	    		
	    		if (even 
	    				&& lastCell
	    				&& this.evenLastRowColumnCellStyleRight != null) {
	    			
	    			return this.evenLastRowColumnCellStyleRight;
	    			
	    		} else if (even
	    					&& !lastCell
	    					&& this.evenRowColumnCellStyleRight != null) {
	    			
	    			return this.evenRowColumnCellStyleRight;
	    			
	    		} else if (lastCell
	    					&& !even
	    					&& this.lastRowColumnCellStyleRight != null) {
	    			
	    			return this.lastRowColumnCellStyleRight;
	    			
	    		} else if (!even && !lastCell && this.rowColumnCellStyleRight != null) {
	    			
	    			return this.rowColumnCellStyleRight;
	    		}
	    		
	    		break;
	    		
	    	case LEFT:
	    		
	    		if (even 
	    				&& lastCell
	    				&& this.evenLastRowColumnCellStyle != null) {
	    			
	    			return this.evenLastRowColumnCellStyle;
	    			
	    		} else if (even
	    					&& !lastCell
	    					&& this.evenRowColumnCellStyle != null) {
	    			
	    			return this.evenRowColumnCellStyle;
	    			
	    		} else if (lastCell
	    					&& !even
	    					&& this.lastRowColumnCellStyle != null) {
	    			
	    			return this.lastRowColumnCellStyle;
	    			
	    		} else if (!even && !lastCell && this.rowColumnCellStyle != null) {
	    			
	    			return this.rowColumnCellStyle;
	    		}
	    		
	    		break;
	    }

		CellStyle style = sheet.getWorkbook().createCellStyle();
		
		if (!even) {

			style.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
			
		    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		
		style.setBorderTop(CellStyle.BORDER_THIN);
	    style.setTopBorderColor(HSSFColor.BLUE.index);
		style.setBorderBottom(CellStyle.BORDER_THIN);
	    style.setBottomBorderColor(HSSFColor.BLUE.index);
		
		if (lastCell) {
			
			style.setBorderRight(CellStyle.BORDER_THIN);
		    style.setRightBorderColor(HSSFColor.BLUE.index);
		}
		
		style.setAlignment(this.getAlignment(alignment));
		
		style.setFont(this.getFont(sheet, "Calibri", (short) 11, false, false));
	    
		switch (alignment) {
	    
	    	case CENTER:
	    		
	    		if (even 
	    				&& lastCell) {
	    			
	    			this.evenLastRowColumnCellStyleCenter = style;
	    			
	    		} else if (even
	    					&& !lastCell) {
	    			
	    			this.evenRowColumnCellStyleCenter = style;
	    			
	    		} else if (lastCell 
	    					&& !even) {
	    			
	    			this.lastRowColumnCellStyleCenter = style;
	    			
	    		} else {
	    			
	    			this.rowColumnCellStyleCenter = style;
	    		}
	    		
	    		break;
	    		
	    	case RIGHT:
	    		
	    		if (even 
	    				&& lastCell) {
	    			
	    			this.evenLastRowColumnCellStyleRight = style;
	    			
	    		} else if (even
	    					&& !lastCell) {
	    			
	    			this.evenRowColumnCellStyleRight = style;
	    			
	    		} else if (lastCell 
	    					&& !even) {
	    			
	    			this.lastRowColumnCellStyleRight = style;
	    			
	    		} else {
	    			
	    			this.rowColumnCellStyleRight = style;
	    		}
	    		
	    		break;
	    		
	    	case LEFT:

	    		if (even 
	    				&& lastCell) {
	    			
	    			this.evenLastRowColumnCellStyle = style;
	    			
	    		} else if (even
	    					&& !lastCell) {
	    			
	    			this.evenRowColumnCellStyle = style;
	    			
	    		} else if (lastCell 
	    					&& !even) {
	    			
	    			this.lastRowColumnCellStyle = style;
	    			
	    		} else {
	    			
	    			this.rowColumnCellStyle = style;
	    		}
	    		
	    		break;
	    }

	    return style;
	}
	
	private CellStyle getFooterCellStyle(Sheet sheet, boolean isLabel, Alignment alignment) {
		
		switch (alignment) {
	    
	    	case CENTER:
	    		
	    		if (isLabel 
	    				&& this.labelFooterCellStyleCenter != null) {
	    			
	    			return this.labelFooterCellStyleCenter;
	    			
	    		} else if (!isLabel && this.footerCellStyleCenter != null) {
	    			
	    			return this.footerCellStyleCenter;
	    		}
	    		
	    		break;
	    		
	    	case RIGHT:
	    		
	    		if (isLabel 
	    				&& this.labelFooterCellStyleRight != null) {
	    			
	    			return this.labelFooterCellStyleRight;
	    			
	    		} else if (!isLabel && this.footerCellStyleRight != null) {
	    			
	    			return this.footerCellStyleRight;
	    		}
	    		
	    		break;
	    		
	    	case LEFT:
	    		
	    		if (isLabel 
	    				&& this.labelFooterCellStyle != null) {
	    			
	    			return this.labelFooterCellStyle;
	    			
	    		} else if (!isLabel && this.footerCellStyle != null) {
	    			
	    			return this.footerCellStyle;
	    		}
	    		
	    		break;
	    }

		CellStyle style = sheet.getWorkbook().createCellStyle();
		
		style.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
		
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		style.setBorderTop(CellStyle.BORDER_THIN);
	    style.setTopBorderColor(HSSFColor.BLACK.index);
		style.setBorderBottom(CellStyle.BORDER_THIN);
	    style.setBottomBorderColor(HSSFColor.BLACK.index);
	    style.setBorderRight(CellStyle.BORDER_THIN);
	    style.setRightBorderColor(HSSFColor.BLACK.index);
	    style.setBorderLeft(CellStyle.BORDER_THIN);
	    style.setLeftBorderColor(HSSFColor.BLACK.index);
		
	    if (!isLabel) {
	    
	    	style.setAlignment(this.getAlignment(alignment));
	    }
	    
		style.setFont(this.getFont(sheet, "Calibri", (short) 11, isLabel, false));
	    
		if (isLabel) {
			
			this.labelFooterCellStyle = style;
			
		} else {
			
			this.footerCellStyle = style;
		}
		
		switch (alignment) {
	    
	    	case CENTER:
	    		
	    		if (isLabel) {
	    			
	    			this.labelFooterCellStyleCenter = style;
	    			
	    		} else {
	    			
	    			this.footerCellStyleCenter = style;
	    		}
	    		
	    		break;
	    		
	    	case RIGHT:
	    		
	    		if (isLabel) {
	    			
	    			this.labelFooterCellStyleRight = style;
	    			
	    		} else {
	    			
	    			this.footerCellStyleRight = style;
	    		}
	    		
	    		break;
	    		
	    	case LEFT:
	    		
	    		if (isLabel) {
	    			
	    			this.labelFooterCellStyle = style;
	    			
	    		} else {
	    			
	    			this.footerCellStyle = style;
	    		}
	    		
	    		break;
	    }
		
	    return style;
	}
	
	private void configurePalletes(Sheet sheet) {
		
		HSSFPalette palette = ((HSSFWorkbook) sheet.getWorkbook()).getCustomPalette();
		
	    palette.setColorAtIndex(HSSFColor.LIGHT_BLUE.index, (byte) 220, (byte) 230, (byte) 241);

	    palette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 79, (byte) 129, (byte) 189);
	}
	
	@SuppressWarnings("incomplete-switch")
	private short getAlignment(Alignment alignment) {

		short poiAlignment = CellStyle.ALIGN_LEFT;
		
		if (alignment != null) {
		
			switch (alignment) {
			
		    	case CENTER:
		    		return CellStyle.ALIGN_CENTER;
		    	case RIGHT:
		    		return CellStyle.ALIGN_RIGHT;
			}
		}
		
		return poiAlignment;
	}
}
