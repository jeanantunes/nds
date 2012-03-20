package br.com.abril.nds.util.export;

import java.io.OutputStream;
import java.util.List;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFExporter implements Exporter {

	public <T, F, FT> void exportInOutputStream(F filter,
											    FT footer,
											    List<T> dataList, 
											    Class<T> listClass,
											    OutputStream outputStream) {
		
		ExportModel exportModel = ExportHandler.generateExportModel(filter, footer, dataList, listClass);
		
		if (exportModel == null) {
			
			return;
		}
		
		try {
		
	        Document document = new Document();

	        PdfWriter.getInstance(document, outputStream);
	
	        document.open();
	        
	        this.processFilters(exportModel, document);
	        
	        this.processRows(exportModel, document);
			
			this.processFooters(exportModel, document);
	
	        document.close();
	        
		} catch (Exception e) {
			
			throw new RuntimeException("Erro ao gerar arquivo PDF!", e);
		}
	}
	
	private void processFilters(ExportModel exportModel, Document document) throws DocumentException {
		
		List<ExportFilter> exportFilters = exportModel.getFilters();
        
        if (exportFilters != null && !exportFilters.isEmpty()) {

    		Paragraph filterParagraph = new Paragraph("Filtro de Pesquisa");
    		
    		document.add(filterParagraph);
    		
    		PdfPTable filterPdfTable = new PdfPTable(exportFilters.size());
    		
        	for (ExportFilter exportFilter : exportFilters) {
        		
        		PdfPCell filterLabelPdfCell = new PdfPCell();
        		
        		Paragraph filterLabelParagraph = new Paragraph(exportFilter.getLabel());
        		
        		filterLabelPdfCell.addElement(filterLabelParagraph);
        		
        		filterPdfTable.addCell(filterLabelPdfCell);
        		
        		PdfPCell filterValuePdfCell = new PdfPCell();
        		
        		Paragraph filterValueParagraph = new Paragraph(exportFilter.getValue());
        		
        		filterValueParagraph.setAlignment(exportFilter.getAlignment().getValue());
        		
        		filterValuePdfCell.addElement(filterValueParagraph);
        		
        		filterPdfTable.addCell(filterValuePdfCell);
        	}
        	
        	document.add(filterPdfTable);
        }
	}
	
	private void processRows(ExportModel exportModel, Document document) throws DocumentException {
		
		PdfPTable pdfTable = new PdfPTable(exportModel.getHeaders().size());
        
        pdfTable.setHeaderRows(1);
        
        for (ExportHeader exportHeader : exportModel.getHeaders()) {
        	
        	PdfPCell pdfCell = new PdfPCell();
    		
    		Paragraph paragraph = new Paragraph(exportHeader.getValue());
    		
    		paragraph.setAlignment(exportHeader.getAlignment().getValue());
    		
    		pdfCell.addElement(paragraph);
    		
    		pdfTable.addCell(pdfCell);
        }
        
        for (ExportRow exportRow : exportModel.getRows()) {
        	
        	for (ExportColumn exportColumn : exportRow.getColumns()) {
        		
        		PdfPCell pdfCell = new PdfPCell();

        		Paragraph paragraph = new Paragraph(exportColumn.getValue());

        		paragraph.setAlignment(exportColumn.getAlignment().getValue());
        		
        		pdfCell.addElement(paragraph);
        		
        		pdfTable.addCell(pdfCell);
        	}
        }
		
		document.add(pdfTable);
	}
	
	private void processFooters(ExportModel exportModel, Document document) throws DocumentException {
		
		List<ExportFooter> exportFooters = exportModel.getFooters();
        
        if (exportFooters != null && !exportFooters.isEmpty()) {
    		
    		PdfPTable footerPdfTable = new PdfPTable(exportFooters.size() * 2) ;
    		
        	for (ExportFooter exportFooter : exportFooters) {
        		
        		PdfPCell footerLabelPdfCell = new PdfPCell();
        		
        		Paragraph footerLabelParagraph = new Paragraph(exportFooter.getLabel());
        		
        		footerLabelPdfCell.addElement(footerLabelParagraph);
        		
        		footerPdfTable.addCell(footerLabelPdfCell);
        		
        		PdfPCell footerValuePdfCell = new PdfPCell();
        		
        		Paragraph footerValueParagraph = new Paragraph(exportFooter.getValue());
        		
        		footerValueParagraph.setAlignment(exportFooter.getAlignment().getValue());
        		
        		footerValuePdfCell.addElement(footerValueParagraph);
        		
        		footerPdfTable.addCell(footerValuePdfCell);
        	}
        	
        	document.add(footerPdfTable);
        }
	}

}
