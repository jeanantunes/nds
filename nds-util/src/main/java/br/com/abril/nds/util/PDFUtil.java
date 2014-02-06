package br.com.abril.nds.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.util.IOUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFUtil {
    
    private static final Logger LOGGER = Logger.getLogger(PDFUtil.class);
	
	public static byte[] mergePDFs(List<byte[]> arquivos) {

		try {
			List<InputStream> pdfs = new ArrayList<InputStream>();

			for (byte[] byteFile : arquivos) {
				pdfs.add(new ByteArrayInputStream(byteFile));
			}
			
			return PDFUtil.concatPDFs(pdfs, true);
		
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return null;
	}

	public static byte[] concatPDFs(List<InputStream> streamOfPDFFiles, boolean paginate) throws Exception {

		File file = File.createTempFile("pdfUtil", "pdf");		
		
		
		OutputStream outputStream = new FileOutputStream(file);
		
		Document document = new Document();
		try {
			List<InputStream> pdfs = streamOfPDFFiles;
			List<PdfReader> readers = new ArrayList<PdfReader>();
			int totalPages = 0;
			Iterator<InputStream> iteratorPDFs = pdfs.iterator();

			// Create Readers for the pdfs.
			while (iteratorPDFs.hasNext()) {
				InputStream pdf = iteratorPDFs.next();
				PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);
				totalPages += pdfReader.getNumberOfPages();
			}
			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
			// data

			PdfImportedPage page;
			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();

				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					currentPageNumber++;
					page = writer.getImportedPage(pdfReader,
							pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);

					// Code for pagination.
					if (paginate) {
						cb.beginText();
						cb.setFontAndSize(bf, 9);
						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, ""
								+ currentPageNumber + " of " + totalPages, 520,
								5, 0);
						cb.endText();
					}
				}
				pageOfCurrentReaderPDF = 0;
			}
						
			outputStream.flush();
			document.close();
			outputStream.close();
			
			byte[] retorno = IOUtils.toByteArray(new FileInputStream(file));
			
			file.delete();
			
			return retorno;			
		
		} finally {
			if (document.isOpen())
				document.close();
			try {
				if (outputStream != null)
					outputStream.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
			}
		}
	}
}
