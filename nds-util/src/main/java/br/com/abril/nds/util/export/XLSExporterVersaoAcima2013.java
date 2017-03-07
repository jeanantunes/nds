package br.com.abril.nds.util.export;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTextBox;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
public class XLSExporterVersaoAcima2013 implements Exporter {

    private XSSFCellStyle headerColumnCellStyle;

    private XSSFCellStyle headerColumnCellStyleRight;

    private XSSFCellStyle headerColumnCellStyleCenter;
    
    private XSSFCellStyle footerCellStyle;	

    private XSSFCellStyle footerCellStyleRight;

    private XSSFCellStyle footerCellStyleCenter;

    private XSSFCellStyle labelFooterCellStyle;

    private XSSFCellStyle labelFooterCellStyleRight;

    private XSSFCellStyle labelFooterCellStyleCenter;

    private DataFormat brazilianCurrencyDF = null;

    private DataFormat decimalValueDF = null;


    private final Map<CellStyleKey, XSSFCellStyle> mapCellStyle = new HashMap<CellStyleKey, XSSFCellStyle>();

    private static final String decimalValueMask = "0.00";

    private static final String brazilianCurrencyMask = "R$ #,##0.00";

    @Override
    public <T, F, FT> void inOutputStream(final String name, final NDSFileHeader ndsFileHeader, final F filter,
            final FT footer, final List<T> dataList, final Class<T> listClass, final OutputStream outputStream) {

        try {

            final ExportModel exportModel = ExportHandler.generateExportModel(filter, footer, dataList, listClass);

            if (exportModel == null) {

                return;
            }

            final XSSFWorkbook workbook = new XSSFWorkbook();

            final XSSFSheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(name));

            sheet.getPrintSetup().setLandscape(true);

           // this.configurePalletes(sheet);

            final XSSFCreationHelper creationHelper = workbook.getCreationHelper();

            int lastRowNum = this.createSheetFilter(sheet, creationHelper, exportModel.getFilters());

            List<Integer> autoSizeColumns = new ArrayList<>();
            
            lastRowNum =
            	this.createSheetMainDataHeaders(
            		sheet, creationHelper, exportModel.getHeaders(), lastRowNum, autoSizeColumns);

            lastRowNum = this.createSheetMainDataRows(sheet, exportModel.getRows(), lastRowNum);

            for (Integer autoSizeColumn : autoSizeColumns) {
				
				sheet.autoSizeColumn(autoSizeColumn);
			}
            
            this.createSheetFooter(sheet, exportModel.getFooters(), exportModel.getHeaders(), lastRowNum);

            this.createSheetHeader(sheet, creationHelper, ndsFileHeader);

            workbook.write(outputStream);

            outputStream.close();

        } catch (final Exception e) {

        	throw new RuntimeException("Erro ao gerar arquivo XLS!", e);
            
        }
    }
    
    private void createSheetHeader(final XSSFSheet sheet, final XSSFCreationHelper creationHelper,
            final NDSFileHeader ndsFileHeader) throws IOException {

        this.createHeaderBackground(sheet, creationHelper);

        this.createHeaderLogo(sheet, creationHelper, ndsFileHeader.getLogo());

        this.createHeaderTextBoxDadosDistribuidor(sheet, ndsFileHeader);

        this.createHeaderTextBoxOutrosDados(sheet, ndsFileHeader);
    }

    private int createSheetFilter(final XSSFSheet sheet, final XSSFCreationHelper creationHelper,
            final List<ExportFilter> filters) {

        final int startRowNum = 5;

        if (filters == null || filters.isEmpty()) {

            return startRowNum - 1;
        }

        final XSSFRow row = sheet.createRow(startRowNum);

        final XSSFCell cell = row.createCell(0);

        cell.setCellValue(this.getRichTextString("Filtro de Pesquisa", creationHelper, sheet, (short) 14, "Calibri",
                true, false));

        sheet.addMergedRegion(new CellRangeAddress(startRowNum, startRowNum, 0, 2));

        final int filterCellOffset = 9;

        int filterCells = 0;

        XSSFRow filterRow = null;

        for (final ExportFilter exportFilter : filters) {

            final String filterString = exportFilter.getLabel() + ": "
                + StringUtils.defaultString(exportFilter.getValue());

            final RichTextString richTextFiltro = this.getRichTextString(filterString, creationHelper, sheet,
                    (short) 11, "Calibri", false, false);

            if (filterRow == null) {

                filterRow = sheet.createRow(startRowNum + 1);

                filterCells = 0;

            } else if (filterCells % filterCellOffset == 0) {

                filterRow = sheet.createRow(filterRow.getRowNum() + 1);

                filterCells = 0;
            }

            final XSSFCell filterCell = filterRow.createCell(filterCells);

            filterCell.setCellValue(richTextFiltro);

            sheet.addMergedRegion(new CellRangeAddress(filterRow.getRowNum(), filterRow.getRowNum(), filterCells,
                    filterCells + 1));

            filterCells = filterCells + 3;
        }

        return filterRow.getRowNum();
    }

    private int createSheetMainDataHeaders(final XSSFSheet sheet, final XSSFCreationHelper creationHelper,
            final List<ExportHeader> headers, final int lastRowNum, List<Integer> autoSizeColumns) {

        final int startRowNum = lastRowNum + 2;

        XSSFRow row = sheet.createRow(startRowNum);

        XSSFCell cell = row.createCell(0);

        cell.setCellValue(this.getRichTextString("Itens Pesquisados", creationHelper, sheet, (short) 14, "Calibri",
                true, false));

        sheet.addMergedRegion(new CellRangeAddress(startRowNum, startRowNum, 0, 2));

        if (headers == null || headers.isEmpty()) {

            return startRowNum;
        }

        row = sheet.createRow(startRowNum + 1);

        int cellNum = 0;

        XSSFCell firstCell = null;

        XSSFCell lastCell = null;

        for (final ExportHeader exportHeader : headers) {

            final String headerString = exportHeader.getValue() == null ? "" : exportHeader.getValue();

            cell = row.createCell(cellNum);

            if (cellNum == 0) {

                firstCell = cell;
            }

            cell.setCellValue(headerString);

            final XSSFCellStyle cellStyle = this.getHeaderColumnCellStyle(sheet, exportHeader.getAlignment());

            cell.setCellStyle(null);

            lastCell = cell;

            final int totalUniversal = 4000 * headers.size();

            final float patternPercentage = (float) 400000 / totalUniversal;

            final int width = exportHeader.getWidthPercent() == null ? 4000 : Math.round(exportHeader.getWidthPercent()
                * 4000 / patternPercentage);

            sheet.setColumnWidth(cellNum, width);

            if (exportHeader.getXlsAutoSize()) {
    			
				autoSizeColumns.add(cell.getColumnIndex());
			}
            
            cellNum++;
        }

        final CellReference firstCellReference = new CellReference(firstCell);

        final CellReference lastCellReference = new CellReference(lastCell);

        sheet.setAutoFilter(CellRangeAddress.valueOf(firstCellReference.formatAsString() + ":"
            + lastCellReference.formatAsString()));

        return startRowNum + 1;
    }

    private int createSheetMainDataRows(final XSSFSheet sheet, final List<ExportRow> rows, final int lastRowNum) {

        if (rows == null || rows.isEmpty()) {

            return lastRowNum;

        }

        int startRowNum = lastRowNum + 1;
        int rowNum = 0;

        for (final ExportRow exportRow : rows) {

            final XSSFRow row = sheet.createRow(startRowNum++);

            int cellNum = 0;
            final int columnsSize = exportRow.getColumns().size();

            for (final ExportColumn exportColumn : exportRow.getColumns()) {

                final String columnString = exportColumn.getValue() == null ? "" : exportColumn.getValue();

                final XSSFCell cell = row.createCell(cellNum++);

                final XSSFCellStyle cellStyle = this.getRowColumnCellStyle(sheet, new CellStyleKey(rowNum % 2 != 0,
                        columnsSize == cellNum, exportColumn.getAlignment(), exportColumn.getColumnType()));

                carregarValorDaCelulaEFormatacao(sheet, cell, exportColumn.getColumnType(), columnString, cellStyle);

            }
            rowNum++;

        }

        return startRowNum;
    }

	private void carregarValorDaCelulaEFormatacao(final Sheet sheet, final XSSFCell cell, final ColumnType columType,
            String columnString, final XSSFCellStyle cellStyle) {

        if (ColumnType.NUMBER.equals(columType) || ColumnType.INTEGER.equals(columType)
            || ColumnType.DECIMAL.equals(columType) || ColumnType.MOEDA.equals(columType)) {

            cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);

            try {

                if (columnString.contains(",")) {
                    columnString = columnString.replace(".", "").replace(",", ".");
                    cell.setCellValue(Double.valueOf(columnString));
                } else {

                    if (Util.isLong(columnString)) {
                        cell.setCellValue(Long.parseLong(columnString));
                    } else {
                        cell.setCellValue(columnString);
                    }
                }

            } catch (final NumberFormatException e) {

                cell.setCellValue(columnString);

            }

        } else {

            cell.setCellValue(columnString);

        }



        cell.setCellStyle(cellStyle);

    }

    private void createSheetFooter(final XSSFSheet sheet, final List<ExportFooter> footers,
            final List<ExportHeader> headers, final int lastRowNum) {

        if (footers == null || footers.isEmpty()) {

            return;
        }

        int newRowNum = lastRowNum + 2;

        XSSFRow row = sheet.createRow(newRowNum);

        int cellNum = 0;

        for (final ExportFooter exportFooter : footers) {

            if (exportFooter.getHeaderToAlign() != null && !exportFooter.getHeaderToAlign().trim().isEmpty()) {

                Integer headerIndex = null;

                int headerCount = 0;

                for (final ExportHeader exportHeader : headers) {

                    if (exportHeader.getValue().equals(exportFooter.getHeaderToAlign())) {

                        headerIndex = headerCount;

                        break;
                    }

                    headerCount++;
                }

                if (headerIndex != null) {

                    cellNum = headerIndex;
                }
            }

            final boolean hasLabel = !exportFooter.getLabel().trim().isEmpty();

            if (hasLabel) {

				final XSSFCell labelCell = row.createCell(cellNum);

				final XSSFCellStyle cellStyle =
					this.getFooterCellStyle(sheet, true, exportFooter.getAlignment());

				labelCell.setCellStyle(cellStyle);

				labelCell.setCellValue(exportFooter.getLabel() + ": " + exportFooter.getValue());

			} else {

				final XSSFCell cell = row.createCell(cellNum);

				final XSSFCellStyle cellStyle =
					this.getFooterCellStyle(sheet, false, exportFooter.getAlignment());

				carregarValorDaCelulaEFormatacao(sheet, cell,
												 exportFooter.getColumnType(), exportFooter.getValue(),
												 cellStyle);
			}
            
            if (exportFooter.isVerticalPrinting()) {

                row = sheet.createRow(++newRowNum);

                if (cellNum > 0) {

                    cellNum--;
                }
            }
        }
    }

    private void createHeaderTextBoxDadosDistribuidor(final XSSFSheet sheet, final NDSFileHeader ndsFileHeader) {

        final XSSFDrawing patriarch = (XSSFDrawing) sheet.createDrawingPatriarch();

        final XSSFClientAnchor clientAnchorDistribuidor = new XSSFClientAnchor();

        clientAnchorDistribuidor.setCol1(0);
        clientAnchorDistribuidor.setCol2(8);

        clientAnchorDistribuidor.setRow1(1);
        clientAnchorDistribuidor.setRow2(4);

        clientAnchorDistribuidor.setDx1(800);
        clientAnchorDistribuidor.setDy1(0);

        clientAnchorDistribuidor.setDx2(0);
        clientAnchorDistribuidor.setDy2(0);

        final XSSFTextBox textBoxDadosDistribuidor = patriarch.createTextbox(clientAnchorDistribuidor);

        textBoxDadosDistribuidor.setNoFill(true);

        textBoxDadosDistribuidor.setLineStyle(XSSFTextBox.EMU_PER_PIXEL);

        final String nomeDistribuidor = StringUtils.defaultString(ndsFileHeader.getNomeDistribuidor());
        final String cnpjDistribuidor = StringUtils.defaultString(ndsFileHeader.getCnpjDistribuidor());

        String dadosDistribuidor = nomeDistribuidor;

        if (!cnpjDistribuidor.isEmpty()) {

            dadosDistribuidor += "\nCNPJ: " + cnpjDistribuidor;
        }

        final XSSFRichTextString richTextDadosDistribuidor = new XSSFRichTextString(dadosDistribuidor);

        final XSSFFont font = this.getFont(sheet, "Calibri", (short) 11, true, false);

        richTextDadosDistribuidor.applyFont(0, nomeDistribuidor.length(), font);

        // textBoxDadosDistribuidor.setString(richTextDadosDistribuidor);
    }

    private void createHeaderTextBoxOutrosDados(final XSSFSheet sheet, final NDSFileHeader ndsFileHeader) {

        final XSSFDrawing patriarch = (XSSFDrawing) sheet.createDrawingPatriarch();

        final XSSFClientAnchor clientAnchorOutros = new XSSFClientAnchor();

        clientAnchorOutros.setCol1(5);
        clientAnchorOutros.setCol2(8);

        clientAnchorOutros.setRow1(1);
        clientAnchorOutros.setRow2(4);

        clientAnchorOutros.setDx1(500);
        clientAnchorOutros.setDy1(0);

        clientAnchorOutros.setDx2(0);
        clientAnchorOutros.setDy2(0);

        final XSSFTextBox textBoxOutrosDados = patriarch.createTextbox(clientAnchorOutros);

        textBoxOutrosDados.setNoFill(true);

        textBoxOutrosDados.setLineStyle(XSSFTextBox.EMU_PER_PIXEL);

        final String dataAtual = ndsFileHeader.getData() == null ? "" : DateUtil.formatarDataPTBR(ndsFileHeader
                .getData());

        final String labelDia = "Dia: ";

        final String nomeUsuario = ndsFileHeader.getNomeUsuario() == null ? "" : ndsFileHeader.getNomeUsuario();

        String outrosDados;

        if (!dataAtual.isEmpty()) {

            outrosDados = labelDia + dataAtual + "\n" + nomeUsuario;

        } else {

            outrosDados = "\n" + nomeUsuario;
        }

        final XSSFRichTextString richTextDadosDistribuidor = new XSSFRichTextString(outrosDados);

        final XSSFFont font = this.getFont(sheet, "Calibri", (short) 11, true, false);

        if (!dataAtual.isEmpty()) {

            richTextDadosDistribuidor.applyFont(0, labelDia.length(), font);
        }

        //textBoxOutrosDados.setString(richTextDadosDistribuidor);
    }

    private void createHeaderLogo(final XSSFSheet sheet, final XSSFCreationHelper creationHelper, final InputStream logo) {

        if (logo == null) {
            return;
        }

        final byte[] bytes = ImageUtil.redimensionar(logo, 80, 70, FormatoImagem.PNG);

        final int pictureIdx = sheet.getWorkbook().addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);

        final Drawing drawing = sheet.createDrawingPatriarch();

        final XSSFClientAnchor anchor = creationHelper.createClientAnchor();

        anchor.setCol1(0);
        anchor.setCol2(0);

        anchor.setRow1(0);
        anchor.setRow2(0);

        anchor.setDx1(0);
        anchor.setDy1(0);

        anchor.setDx2(0);
        anchor.setDy2(0);

        final Picture picture = drawing.createPicture(anchor, pictureIdx);

        picture.getPreferredSize().setAnchorType(XSSFClientAnchor.MOVE_AND_RESIZE);
    }

    private void createHeaderBackground(final Sheet sheet, final XSSFCreationHelper creationHelper) throws IOException {

        final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                "bg_header.jpg");

        final byte[] bytes = IOUtils.toByteArray(inputStream);

        final int pictureIdx = sheet.getWorkbook().addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_JPEG);

        inputStream.close();

        final Drawing drawing = sheet.createDrawingPatriarch();

        final XSSFClientAnchor anchor = creationHelper.createClientAnchor();

        anchor.setCol1(0);
        anchor.setCol2(0);

        anchor.setRow1(0);
        anchor.setRow2(3);

        anchor.setDx1(7000);
        anchor.setDx2(0);

        anchor.setDy1(200);
        anchor.setDy2(0);

        final Picture picture = drawing.createPicture(anchor, pictureIdx);

        picture.resize();
    }

    private XSSFFont getFont(final XSSFSheet sheet, final String name, final short size, final boolean bold,
            final boolean italic) {

        final XSSFFont font = sheet.getWorkbook().createFont();

        if (bold) {

            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        }

        font.setItalic(italic);

        font.setFontHeightInPoints(size);

        font.setFontName(name);

        return font;
    }

    private XSSFRichTextString getRichTextString(final String string, final XSSFCreationHelper creationHelper,
            final XSSFSheet sheet, final short fontSize, final String fontName, final boolean bold, final boolean italic) {

        final XSSFRichTextString richTextString = creationHelper.createRichTextString(string);

        richTextString.applyFont(this.getFont(sheet, fontName, fontSize, bold, italic));

        return richTextString;
    }

    private XSSFCellStyle getHeaderColumnCellStyle(final XSSFSheet sheet, final Alignment alignment) {

        switch (alignment) {

        case CENTER:

            if (headerColumnCellStyleCenter != null) {

                return (XSSFCellStyle) headerColumnCellStyleCenter;
            }

            break;

        case RIGHT:

            if (headerColumnCellStyleRight != null) {

                return (XSSFCellStyle) headerColumnCellStyleRight;
            }

            break;

        case LEFT:

            if (headerColumnCellStyle != null) {

                return (XSSFCellStyle) headerColumnCellStyle;
            }

            break;
        }

        //final CellStyle style = sheet.getWorkbook().createCellStyle();
        
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle cellStyle = wb.createCellStyle();
        final short fontIndex = cellStyle.getFontIndex();
        
        
        byte[] rgb = new byte[3];
        rgb[0] = (byte) 242; // red
        rgb[1] = (byte) 220; // green
        rgb[2] = (byte) 219; // blue
        XSSFColor myColor = new XSSFColor(rgb); // #f2dcdb
        cellStyle.setFillForegroundColor(myColor);
        
        cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
        cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBottomBorderColor(HSSFColor.LIGHT_BLUE.index);
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setLeftBorderColor(HSSFColor.LIGHT_BLUE.index);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setRightBorderColor(HSSFColor.LIGHT_BLUE.index);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        cellStyle.setTopBorderColor(HSSFColor.LIGHT_BLUE.index);
        
        cellStyle.setAlignment(this.getAlignment(alignment));

        final XSSFFont fontArial = this.getFont(sheet, "Arial", (short) 10, true, false);

        fontArial.setColor(HSSFColor.WHITE.index);
        
        cellStyle.setFont(fontArial);

        switch (alignment) {

        case CENTER:
            headerColumnCellStyleCenter = cellStyle;
            break;
        case RIGHT:
            headerColumnCellStyleRight = cellStyle;
            break;
        case LEFT:
            headerColumnCellStyle = cellStyle;
            break;
        }

        return cellStyle;
    }

    private XSSFCellStyle getRowColumnCellStyle(final XSSFSheet sheet, final CellStyleKey styleKey) {

        if (mapCellStyle.containsKey(styleKey)) {
            return mapCellStyle.get(styleKey);
        }

        final XSSFCellStyle style = sheet.getWorkbook().createCellStyle();

        mapCellStyle.put(styleKey, style);

        if (!styleKey.isEven()) {
        	style.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
            style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        }

        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setTopBorderColor(HSSFColor.BLUE.index);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBottomBorderColor(HSSFColor.BLUE.index);

        if (!styleKey.isLastCell()) {
            style.setBorderRight(XSSFCellStyle.BORDER_THIN);
            style.setRightBorderColor(HSSFColor.BLUE.index);
        }

        style.setAlignment(this.getAlignment(styleKey.getAlignment()));

        style.setFont(this.getFont(sheet, "Calibri", (short) 11, false, false));

        if (ColumnType.MOEDA.equals(styleKey.getColumType())) {
            if (brazilianCurrencyDF == null) {
                brazilianCurrencyDF = sheet.getWorkbook().createDataFormat();
            }
            style.setDataFormat(brazilianCurrencyDF.getFormat(brazilianCurrencyMask));

        } else if (ColumnType.DECIMAL.equals(styleKey.getColumType())) {

            if (decimalValueDF == null) {
                decimalValueDF = sheet.getWorkbook().createDataFormat();
            }
            style.setDataFormat(decimalValueDF.getFormat(decimalValueMask));
        }

        return style;
    }

    private XSSFCellStyle getFooterCellStyle(final XSSFSheet sheet, final boolean isLabel, final Alignment alignment) {

        switch (alignment) {

        case CENTER:

            if (isLabel && labelFooterCellStyleCenter != null) {

                return labelFooterCellStyleCenter;

            } else if (!isLabel && footerCellStyleCenter != null) {

                return footerCellStyleCenter;
            }

            break;

        case RIGHT:

            if (isLabel && labelFooterCellStyleRight != null) {

                return labelFooterCellStyleRight;

            } else if (!isLabel && footerCellStyleRight != null) {

                return footerCellStyleRight;
            }

            break;

        case LEFT:

            if (isLabel && labelFooterCellStyle != null) {

                return labelFooterCellStyle;

            } else if (!isLabel && footerCellStyle != null) {

                return footerCellStyle;
            }

            break;
        }

        final XSSFCellStyle style = sheet.getWorkbook().createCellStyle();
        
        style.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setTopBorderColor(HSSFColor.BLACK.index);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setRightBorderColor(HSSFColor.BLACK.index);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        
        if (!isLabel) {

            style.setAlignment(this.getAlignment(alignment));
        }

        style.setFont(this.getFont(sheet, "Calibri", (short) 11, isLabel, false));

        if (isLabel) {

            labelFooterCellStyle = style;

        } else {

            footerCellStyle = style;
        }

        switch (alignment) {

        case CENTER:

            if (isLabel) {

                labelFooterCellStyleCenter = style;

            } else {

                footerCellStyleCenter = style;
            }

            break;

        case RIGHT:

            if (isLabel) {

                labelFooterCellStyleRight = style;

            } else {

                footerCellStyleRight = style;
            }

            break;

        case LEFT:

            if (isLabel) {

                labelFooterCellStyle = style;

            } else {

                footerCellStyle = style;
            }

            break;
        }

        return style;
    }

//    private void configurePalletes(final Sheet sheet) {
//
//        final XSSPalette palette = ((XSSFWorkbook) sheet.getWorkbook()).getCustomPalette();
//
//        palette.setColorAtIndex(HSSFColor.LIGHT_BLUE.index, (byte) 220, (byte) 230, (byte) 241);
//
//        palette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 79, (byte) 129, (byte) 189);
//    }

    @SuppressWarnings("incomplete-switch")
    private short getAlignment(final Alignment alignment) {

        final short poiAlignment = XSSFCellStyle.ALIGN_LEFT;

        if (alignment != null) {

            switch (alignment) {

            case CENTER:
                return XSSFCellStyle.ALIGN_CENTER;
            case RIGHT:
                return XSSFCellStyle.ALIGN_RIGHT;
            }
        }

        return poiAlignment;
    }
}