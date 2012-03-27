package br.com.abril.nds.util.export;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.util.DateUtil;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFExporter implements Exporter {
	
	private static Font headerBoldFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
	
	private static Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 11);
	
	private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
	
	private static Font valuesFont = new Font(Font.FontFamily.TIMES_ROMAN, 11);
	
	private static Font tableHeaderFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.WHITE);
	
	private static Font footerLabelFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
	
	private static BaseColor headerBaseColor = new BaseColor(79, 129, 189);
	
	private static BaseColor rowBaseColor = new BaseColor(220, 230, 241);
	
	public <T, F, FT> void inOutputStream(String name,
										  NDSFileHeader ndsFileHeader,
										  F filter,
										  FT footer,
										  List<T> dataList, 
										  Class<T> listClass,
										  OutputStream outputStream) {
		
		ExportModel exportModel = ExportHandler.generateExportModel(filter, footer, dataList, listClass);
		
		if (exportModel == null) {
			
			return;
		}
		
		try {
	
	        Document document = new Document(PageSize.A4.rotate());

	        PdfWriter.getInstance(document, outputStream);
	
	        document.open();
	        
	        this.processReportHeader(document, ndsFileHeader);
	        
	        this.processFilters(exportModel, document);
	        
	        this.processRows(exportModel, document);

	        document.close();
	        
		} catch (Exception e) {
			
			throw new RuntimeException("Erro ao gerar arquivo PDF!", e);
		}
	}
	
	private void processReportHeader(Document document, 
									 NDSFileHeader ndsFileHeader) throws MalformedURLException, 
																		 IOException, 
																		 DocumentException {
		
		if (ndsFileHeader == null) {
			
			return;
		}
		
		PdfPTable headerPdfTable = new PdfPTable(3);
		
		headerPdfTable.setWidthPercentage(100F);
		headerPdfTable.setWidths(new int[] {20, 50, 50});
		
		PdfPCell headerPdfCell = new PdfPCell();
		
		headerPdfCell.setFixedHeight(68);
		headerPdfCell.setBorder(0);
		
		headerPdfCell.addElement(new Chunk(this.createBackgroundImage(), -3F, -48F));
		headerPdfCell.addElement(new Chunk(this.createLogotipoImage(), -3F, -30F));
		
		headerPdfTable.addCell(headerPdfCell);
		
		PdfPCell dadosDistribuidorPdfCell = new PdfPCell();
		
		dadosDistribuidorPdfCell.setFixedHeight(68);
		dadosDistribuidorPdfCell.setBorder(0);
		
		Paragraph dadosDistribuidorParagraph = new Paragraph();
		
		String nomeDistribuidor = StringUtils.defaultString(ndsFileHeader.getNomeDistribuidor());
		String cnpjDistribuidor = StringUtils.defaultString(ndsFileHeader.getCnpjDistribuidor());
		
		dadosDistribuidorParagraph.add(new Paragraph(nomeDistribuidor, headerBoldFont));
		dadosDistribuidorParagraph.add(new Paragraph("CNPJ: " + cnpjDistribuidor, headerFont));
		
		dadosDistribuidorPdfCell.addElement(dadosDistribuidorParagraph);
		
		headerPdfTable.addCell(dadosDistribuidorPdfCell);
		
		PdfPCell outrosDadosPdfCell = new PdfPCell();
		
		outrosDadosPdfCell.setFixedHeight(68);
		outrosDadosPdfCell.setBorder(0);
		
		Paragraph outrosDadosParagraph = new Paragraph();
		
		String dia = ndsFileHeader.getData() == null ? "" : DateUtil.formatarDataPTBR(ndsFileHeader.getData());
		String nomeUsuario = "\n"  + StringUtils.defaultString(ndsFileHeader.getNomeUsuario());
		
		Phrase diaPhrase = new Phrase("Dia: ", headerBoldFont);
		
		Phrase dataPhrase = new Phrase(new Chunk(dia, headerFont));
		
		outrosDadosParagraph.add(diaPhrase);
		outrosDadosParagraph.add(dataPhrase);
		outrosDadosParagraph.add(new Paragraph(nomeUsuario, headerFont));
		
		outrosDadosPdfCell.addElement(outrosDadosParagraph);
		
		headerPdfTable.addCell(outrosDadosPdfCell);
		
		document.add(headerPdfTable);
	}
	
	private Image createBackgroundImage() throws IOException, BadElementException {
		
		InputStream inputStream = 
			Thread.currentThread().getContextClassLoader().getResourceAsStream("bg_header.jpg");

		Image backgroundImage = Image.getInstance(ImageIO.read(inputStream), null);
		
		backgroundImage.scaleAbsolute(770, 68);
				
		inputStream.close();
		
		return backgroundImage;
	}
	
	private Image createLogotipoImage() throws IOException, BadElementException {
		
		InputStream inputStream = 
			Thread.currentThread().getContextClassLoader().getResourceAsStream("logo_sistema.png");

		Image logotipoImage = Image.getInstance(ImageIO.read(inputStream), null);
		
		logotipoImage.scalePercent(100F);
		
		inputStream.close();
		
		return logotipoImage;
	}
	
	private void processFilters(ExportModel exportModel, Document document) throws DocumentException {
		
		List<ExportFilter> exportFilters = exportModel.getFilters();
        
        if (exportFilters != null && !exportFilters.isEmpty()) {
        	
        	document.add(Chunk.NEWLINE);
        	
    		document.add(new Paragraph("Filtro de Pesquisa", titleFont));
    		
    		PdfPTable filterPdfTable = new PdfPTable(6);
    		
    		filterPdfTable.setSpacingBefore(10F);
    		
    		filterPdfTable.setSpacingAfter(10F);
    		
    		filterPdfTable.setWidthPercentage(100F);

        	for (ExportFilter exportFilter : exportFilters) {
        		
        		PdfPCell filterLabelPdfCell = new PdfPCell();
        		
        		filterLabelPdfCell.setBorder(0);
        		
        		Paragraph filterLabelParagraph = 
        			new Paragraph(StringUtils.defaultString(exportFilter.getLabel()) + ": ", valuesFont);
        		
        		filterLabelPdfCell.addElement(filterLabelParagraph);
        		
        		filterPdfTable.addCell(filterLabelPdfCell);
        		
        		PdfPCell filterValuePdfCell = new PdfPCell();
        		
        		filterValuePdfCell.setBorder(0);
        		
        		Paragraph filterValueParagraph = 
        			new Paragraph(StringUtils.defaultString(exportFilter.getValue()), valuesFont);
        		
        		filterValueParagraph.setAlignment(exportFilter.getAlignment().getValue());
        		
        		filterValuePdfCell.addElement(filterValueParagraph);
        		
        		filterPdfTable.addCell(filterValuePdfCell);
        	}
        	
        	filterPdfTable.getDefaultCell().setBorder(0);
        	
        	filterPdfTable.completeRow();
        	
        	document.add(filterPdfTable);
        }
	}
	
	private void processRows(ExportModel exportModel, Document document) throws DocumentException {
		
		document.add(new Paragraph("Itens Pesquisados", titleFont));
		
		PdfPTable pdfTable = new PdfPTable(exportModel.getHeaders().size());
        
		pdfTable.setWidthPercentage(100F);
		
		pdfTable.setSpacingBefore(10F);
		
		pdfTable.setSpacingAfter(10F);
		
        pdfTable.setHeaderRows(1);
        
        for (ExportHeader exportHeader : exportModel.getHeaders()) {
        	
        	PdfPCell pdfCell = new PdfPCell();
        	
        	pdfCell.setBackgroundColor(headerBaseColor);
    		
    		Paragraph paragraph = 
    			new Paragraph(StringUtils.defaultString(exportHeader.getValue()), tableHeaderFont);
    		
    		paragraph.setAlignment(exportHeader.getAlignment().getValue());
    		
    		pdfCell.addElement(paragraph);
    		
    		pdfTable.addCell(pdfCell);
        }
        
        int rowNum = 0;
        
        for (ExportRow exportRow : exportModel.getRows()) {
        	
        	for (ExportColumn exportColumn : exportRow.getColumns()) {
        		
        		PdfPCell pdfCell = new PdfPCell();
        		
        		if ((rowNum % 2) == 0) {
        			
        			pdfCell.setBackgroundColor(rowBaseColor);        			
        		}
        		
        		Paragraph paragraph = 
        			new Paragraph(StringUtils.defaultString(exportColumn.getValue()));

        		paragraph.setAlignment(exportColumn.getAlignment().getValue());
        		
        		pdfCell.addElement(paragraph);
        		
        		pdfTable.addCell(pdfCell);
        	}
        	
        	rowNum++;
        }

        this.processFooters(rowNum, pdfTable, exportModel, document);
		
		document.add(pdfTable);
	}
	
	private void processFooters(int rowNum,
								PdfPTable pdfTable, 
								ExportModel exportModel,
								Document document) throws DocumentException {
		
		List<ExportHeader> exportHeaders = exportModel.getHeaders();
		
		this.createFooterSeparationLine(pdfTable, exportHeaders);
		
		List<ExportFooter> exportFooters = exportModel.getFooters();

        if (exportFooters != null && !exportFooters.isEmpty()) {
    		
    		int cellNum = 0;
    		
    		int footerCellCount = 0;
    		
        	for (ExportFooter exportFooter : exportFooters) {
        		
        		if (!exportFooter.getHeaderToAlign().trim().isEmpty()) {
        			
        			Integer headerIndex = null; 
    				
    				int headerCount = 0;
    				
    				for (ExportHeader exportHeader : exportHeaders) {
    					
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
        		
        		if (cellNum > 0) {

    				while (footerCellCount <= (cellNum - 1)) {
    				
    					PdfPCell emptyCell = new PdfPCell();
    					
    					if ((rowNum % 2) == 0) {
    	        			
    						emptyCell.setBackgroundColor(rowBaseColor);        			
    	        		}
    					
    					pdfTable.addCell(emptyCell);
    					
    					footerCellCount++;
    				}
        		}
        		
        		if (!exportFooter.getLabel().trim().isEmpty()) {
    				
        			PdfPCell footerLabelPdfCell = new PdfPCell();
        			   
            		Paragraph footerLabelParagraph = 
            			new Paragraph(StringUtils.defaultString(exportFooter.getLabel() + ":"), footerLabelFont);
            		
            		footerLabelPdfCell.addElement(footerLabelParagraph);
            		
            		if ((rowNum % 2) == 0) {
	        			
            			footerLabelPdfCell.setBackgroundColor(rowBaseColor);        			
	        		}
            		
            		pdfTable.addCell(footerLabelPdfCell);
            		
            		footerCellCount++;
    			}

        		PdfPCell footerValuePdfCell = new PdfPCell();
        		
        		Paragraph footerValueParagraph = new Paragraph(exportFooter.getValue());
        		
        		footerValueParagraph.setAlignment(exportFooter.getAlignment().getValue());
        		
        		footerValuePdfCell.addElement(footerValueParagraph);
        		
        		if ((rowNum % 2) == 0) {
        			
        			footerValuePdfCell.setBackgroundColor(rowBaseColor);        			
        		}
        		
        		pdfTable.addCell(footerValuePdfCell);
        		
        		footerCellCount++;
        	}
        	
        	if ((rowNum % 2) == 0) {
        	
        		pdfTable.getDefaultCell().setBackgroundColor(rowBaseColor);
        	}
        	
        	pdfTable.completeRow();
        }
	}
	
	private void createFooterSeparationLine(PdfPTable pdfTable, 
											List<ExportHeader> exportHeaders) {
		
		if (exportHeaders == null || exportHeaders.isEmpty()) {
			
			return;
		}
		
		for (int i = 0; i < exportHeaders.size(); i++) {
			
			PdfPCell emptyCell = new PdfPCell();
			
			emptyCell.setFixedHeight(20);
			
			pdfTable.addCell(emptyCell);
		}
	}

}
