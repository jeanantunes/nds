package br.com.abril.nds.util.export;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.util.DateUtil;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Classe responsável pela exportação de arquivos PDF.
 * 
 * @author Discover Technology
 *
 */
public class PDFExporter implements Exporter {
	
	private static Font headerBoldFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
	
	private static Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 11);
	
	private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
	
	private static Font valuesFont = new Font(Font.FontFamily.TIMES_ROMAN, 11);
	
	private static Font tableHeaderFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD, BaseColor.WHITE);
	
	private static Font footerLabelFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
	
	private static Font rowValuesFont = new Font(Font.FontFamily.TIMES_ROMAN, 9);
	
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

	        PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
	
	        HeaderFooter event = new HeaderFooter();

	        pdfWriter.setBoxSize("footer", new Rectangle(250, 24, 559, 788));
	        
	        pdfWriter.setPageEvent(event);
	        
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
		
		Image logo  = this.createLogotipoImage(ndsFileHeader.getLogo());
		
		if(logo!= null){
			headerPdfCell.addElement(new Chunk(logo, -3F, -30F));
		}
		
		headerPdfTable.addCell(headerPdfCell);
		
		PdfPCell dadosDistribuidorPdfCell = new PdfPCell();
		
		dadosDistribuidorPdfCell.setFixedHeight(68);
		dadosDistribuidorPdfCell.setBorder(0);
		
		Paragraph dadosDistribuidorParagraph = new Paragraph();
		
		String nomeDistribuidor = StringUtils.defaultString(ndsFileHeader.getNomeDistribuidor());
		String cnpjDistribuidor = StringUtils.defaultString(ndsFileHeader.getCnpjDistribuidor());
		
		dadosDistribuidorParagraph.add(new Paragraph(nomeDistribuidor, headerBoldFont));
		
		if (!cnpjDistribuidor.isEmpty()) {
			
			dadosDistribuidorParagraph.add(new Paragraph("CNPJ: " + cnpjDistribuidor, headerFont));
		}
		
		dadosDistribuidorPdfCell.addElement(dadosDistribuidorParagraph);
		
		headerPdfTable.addCell(dadosDistribuidorPdfCell);
		
		PdfPCell outrosDadosPdfCell = new PdfPCell();
		
		outrosDadosPdfCell.setFixedHeight(68);
		outrosDadosPdfCell.setBorder(0);
		
		Paragraph outrosDadosParagraph = new Paragraph();
		
		String dia = ndsFileHeader.getData() == null ? "" : DateUtil.formatarDataPTBR(ndsFileHeader.getData());
		String nomeUsuario = "\n"  + StringUtils.defaultString(ndsFileHeader.getNomeUsuario());
		
		Phrase diaPhrase = null;
		
		if (!dia.isEmpty()) {
			
			diaPhrase = new Phrase("Dia: ", headerBoldFont);
		}
		
		Phrase dataPhrase = new Phrase(new Chunk(dia, headerFont));
		
		if (diaPhrase != null) {
			
			outrosDadosParagraph.add(diaPhrase);
		}
		
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
	
	private Image createLogotipoImage(InputStream logo) throws IOException, BadElementException {
		
		if(logo == null){
			return null;
		}
		
		Image logotipoImage = Image.getInstance(ImageIO.read(logo), null);
		
		logotipoImage.scalePercent(100F);
		
		logotipoImage.scaleAbsolute(120f, 68f);
		
		logo.close();
		
		return logotipoImage;
	}
	
	private void processFilters(ExportModel exportModel, Document document) throws DocumentException {
		
		List<ExportFilter> exportFilters = exportModel.getFilters();
        
        if (exportFilters != null && !exportFilters.isEmpty()) {
        	
        	document.add(Chunk.NEWLINE);
        	
    		document.add(new Paragraph("Filtro de Pesquisa", titleFont));
    		
    		PdfPTable filterPdfTable = new PdfPTable(exportModel.getFilters().size());
    		
    		filterPdfTable.setSpacingBefore(10F);
    		
    		filterPdfTable.setSpacingAfter(10F);
    		
    		filterPdfTable.setWidthPercentage(100F);
    		
    		int headerSize = 0;
            
            Float[] exportWidths = new Float[exportModel.getFilters().size()];

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
        		
        		exportWidths[headerSize] = exportFilter.getWidthPercent();

        		headerSize++;
        		
        		if (headerSize == exportModel.getFilters().size()) {
        		
        			filterPdfTable.completeRow();
        		}
        	}
        	
        	float[] widths = calculateWidths(exportWidths);

            if (widths != null) {
            	
            	filterPdfTable.setWidths(widths);
            }
        	
        	filterPdfTable.getDefaultCell().setBorder(0);
        	
        	filterPdfTable.completeRow();
        	
        	document.add(filterPdfTable);
        }
	}
	
	private void processRows(ExportModel exportModel, Document document) throws DocumentException {
		
		document.add(new Paragraph("Itens Pesquisados", titleFont));
		
		PdfPTable pdfTable = new PdfPTable(this.getDataTableSize(exportModel));

		pdfTable.setWidthPercentage(100F);
		
        pdfTable.setHeaderRows(1);
        
        int headerSize = 0;
        
        Float[] exportWidths = new Float[exportModel.getHeaders().size()];

		for (ExportHeader exportHeader : exportModel.getHeaders()) {
        	
        	PdfPCell pdfCell = new PdfPCell();
        	
        	pdfCell.setBackgroundColor(headerBaseColor);
    		
    		Paragraph paragraph = 
    			new Paragraph(StringUtils.defaultString(exportHeader.getValue()), tableHeaderFont);
    		
    		paragraph.setAlignment(exportHeader.getAlignment().getValue());
    		
    		pdfCell.addElement(paragraph);
    		
    		pdfTable.addCell(pdfCell);

    		exportWidths[headerSize] = exportHeader.getWidthPercent();

    		headerSize++;
    		
    		if (headerSize == exportModel.getHeaders().size()) {
    		
    			pdfTable.completeRow();
    		}
        }
        
		float[] widths = calculateWidths(exportWidths);

        if (widths != null) {

        	pdfTable.setWidths(widths);
        }

        pdfTable.setSpacingBefore(10F);
		
		pdfTable.setSpacingAfter(10F);

		int rowNum = 0;

        for (ExportRow exportRow : exportModel.getRows()) {
        	
        	int lineSize = 0;
        	
        	for (ExportColumn exportColumn : exportRow.getColumns()) {
        		
        		PdfPCell pdfCell = new PdfPCell();
        		
        		if ((rowNum % 2) == 0) {
        			
        			pdfCell.setBackgroundColor(rowBaseColor);        			
        		}
        		
        		Paragraph paragraph = null;
        		
        		Float fontSize = exportColumn.getFontSize();
        		if (fontSize != null && fontSize != 0){
        			
        			Font font = new Font(Font.FontFamily.TIMES_ROMAN, fontSize);
        			paragraph = new Paragraph(StringUtils.defaultString(exportColumn.getValue()), font);
        		} else {
        			paragraph = new Paragraph(StringUtils.defaultString(exportColumn.getValue()), rowValuesFont);
        		}

        		paragraph.setAlignment(exportColumn.getAlignment().getValue());
        		
        		pdfCell.addElement(paragraph);
        		
        		pdfTable.addCell(pdfCell);
        		
        		lineSize++;
        		
        		if (lineSize == exportModel.getHeaders().size()) {
        		
        			pdfTable.completeRow();
        		}
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
		
		List<ExportFooter> exportFooters = exportModel.getFooters();

        if (exportFooters != null && !exportFooters.isEmpty()) {
    		
        	this.createFooterSeparationLine(pdfTable, exportHeaders);
        	
        	int footerCount = 0;
        	
    		int cellNum = 0;
    		
    		int footerCellCount = 0;
    		
        	for (ExportFooter exportFooter : exportFooters) {
        		
        		footerCount++;
        		
        		if (exportFooter.getHeaderToAlign() != null 
        				&& !exportFooter.getHeaderToAlign().trim().isEmpty()) {
        			
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

    					emptyCell.setBackgroundColor(rowBaseColor);        			

    					pdfTable.addCell(emptyCell);
    					
    					footerCellCount++;
    				}
        		}

        		boolean hasLabel = !exportFooter.getLabel().trim().isEmpty();
        		
        		if (hasLabel) {
    				
        			PdfPCell footerLabelPdfCell = new PdfPCell();
        			   
        			String label = StringUtils.defaultString(exportFooter.getLabel());
        			
            		Paragraph footerLabelParagraph = new Paragraph(label, footerLabelFont);
            		
            		footerLabelPdfCell.addElement(footerLabelParagraph);

            		footerLabelPdfCell.setBackgroundColor(rowBaseColor);        			
            		
            		pdfTable.addCell(footerLabelPdfCell);
            		
            		footerCellCount++;
    			}

        		PdfPCell footerValuePdfCell = new PdfPCell();
        		
        		Float fontSize = exportFooter.getFontSize();
    			
    			Paragraph footerValueParagraph;
    			
        		if (fontSize != null && fontSize != 0){
        			
        			Font font = new Font(Font.FontFamily.TIMES_ROMAN, fontSize);
        			footerValueParagraph = new Paragraph(StringUtils.defaultString(exportFooter.getValue()), font);
        		} else {
        			
        			footerValueParagraph = new Paragraph(StringUtils.defaultString(exportFooter.getValue()));
        		}
        		
        		footerValueParagraph.setAlignment(exportFooter.getAlignment().getValue());
        		
        		footerValuePdfCell.addElement(footerValueParagraph);
        		
        		footerValuePdfCell.setBackgroundColor(rowBaseColor);        			

        		pdfTable.addCell(footerValuePdfCell);
        		
        		footerCellCount++;
        		
        		if (exportFooter.isVerticalPrinting()
        				&& footerCount < exportFooters.size()) {

        			pdfTable.completeRow();
        			
    				rowNum++;
    				
    				footerCellCount--;
    				
    				if (hasLabel) {
    					
    					footerCellCount--;
    				}
    				
    				int newCellCount = 0;
    				
    				while (newCellCount <= (footerCellCount - 1)) {
        				
    					PdfPCell emptyCell = new PdfPCell();

    					emptyCell.setBackgroundColor(rowBaseColor);        			

    					pdfTable.addCell(emptyCell);
    					
    					newCellCount++;
    				}
    			}
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
		
		pdfTable.completeRow();
	}
	
	private int getDataTableSize(ExportModel exportModel) {
		
		int headerSize = 0;
		
		if (exportModel.getHeaders() != null && !exportModel.getHeaders().isEmpty()) {
			
			headerSize = exportModel.getHeaders().size();
		}
		
		int footerSize = 0;
		
		if (exportModel.getFooters() != null 
				&& !exportModel.getFooters().isEmpty()) {
			
			for (ExportFooter exportFooter : exportModel.getFooters()) {
				
				if (exportFooter.isVerticalPrinting()) {
					
					continue;
				}
				
				if (!exportFooter.getLabel().trim().isEmpty()) {
					
					footerSize++;
				}
				
				footerSize++;
			}
		}
		
		return footerSize > headerSize ? footerSize : headerSize;
	}
	
	/** Inner class to add a header and a footer. */
    class HeaderFooter extends PdfPageEventHelper {
    	
        /** Alternating phrase for the header. */
        Phrase[] header = new Phrase[2];
        
        /** Current page number (will be reset for every chapter). */
        int pagenumber;
 
        /**
         * Initialize one of the headers.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onOpenDocument(PdfWriter writer, Document document) {
            header[0] = new Phrase("Relatorio");
        }
 
        /**
         * Initialize one of the headers, based on the chapter title;
         * reset the page number.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onChapter(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document, float,
         *      com.itextpdf.text.Paragraph)
         */
        public void onChapter(PdfWriter writer, Document document,
                float paragraphPosition, Paragraph title) {
        	
            header[1] = new Phrase(title.getContent());
            pagenumber = 1;
        }
 
        /**
         * Increase the page number.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onStartPage(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onStartPage(PdfWriter writer, Document document) {
        	
            pagenumber++;
        }
 
        /**
         * Adds the header and the footer.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onEndPage(PdfWriter writer, Document document) {
        	
            Rectangle rect = writer.getBoxSize("footer");
            
            switch(writer.getPageNumber() % 2) {
            case 0:
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_RIGHT, header[0],
                        rect.getRight(), rect.getTop(), 0);
                break;
            case 1:
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_LEFT, header[1],
                        rect.getLeft(), rect.getTop(), 0);
                break;
            }
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(String.format("Página %d", pagenumber)),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
        }
    }
    
    private float[] calculateWidths(Float[] widths) {
    	
    	//É usado o valor "80", devido às margens utilizadas na criação do relatório.

    	List<Integer> nullWidths = new ArrayList<Integer>();

    	int indexNullWidths = 0;
    	
    	float total = 0f;

    	float[] newWidths = new float[widths.length];

    	for (Float width : widths) {

    		if (width == null) {

    			nullWidths.add(indexNullWidths);

    		} else {

    			width = widths[indexNullWidths] = width * 80 / 100;

    			total += width;
    		}

    		indexNullWidths ++;
    	}
    	
    	if (nullWidths.size() == widths.length) {
    		
    		return null;
    	}

    	if (((!nullWidths.isEmpty()) && total == 80f) || total > 80f) {
    		
    		throw new RuntimeException("A largura total das colunas ultrapassa o valor de 100%.");
    	}

    	float patternValue = (80f - total) / nullWidths.size();

    	for (int index = 0; index < widths.length; index ++) {
    		
    		if (nullWidths.contains(index)) {

    			newWidths[index] = patternValue;
    		
    		} else {
    			
    			newWidths[index] = widths[index];
    		}
    	}

    	return newWidths;
    }
}
