package br.com.abril.nds.util.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class FileExporter {

	private FileType fileType;
	
	private String fileName;
	
	private FileExporter() {
		
	}
	
	public static FileExporter to(String fileName, FileType fileType) {
		
		FileExporter fileExporter = new FileExporter();
		
		fileExporter.fileName = fileName;
		
		fileExporter.fileType = fileType;
		
		return fileExporter;
	}
	
	public <T, F, FT> void inOutputStream(NDSFileHeader ndsFileHeader,
										  F filter,
										  FT footer,
										  List<T> dataList, 
										  Class<T> listClass,
										  OutputStream outputStream) {
		
		if (FileType.PDF.equals(this.fileType)) {
			
			new PDFExporter().inOutputStream(
				this.fileName, ndsFileHeader, filter, footer, dataList, listClass, outputStream);
			
		} else if (FileType.XLS.equals(this.fileType)) {
			
			new XLSExporter().inOutputStream(
				this.fileName, ndsFileHeader, filter, footer, dataList, listClass, outputStream);
			
		} else {
			
			throw new RuntimeException("Tipo de arquivo não suportado para exportação");
		}
	}
	
	public <T, F, FT> void inHTTPResponse(NDSFileHeader ndsFileHeader,
										  F filter,
										  FT footer,
										  List<T> dataList, 
										  Class<T> listClass,
										  HttpServletResponse httpServletResponse) throws IOException {
		
		this.configureHTTPReponse(httpServletResponse);
		
		this.inOutputStream(ndsFileHeader, filter, footer, dataList, listClass, httpServletResponse.getOutputStream());
	}
	
	/*
	 * Configura a resposta HTTP para retornar o arquivo exportado.
	 * 
	 * @param httpServletResponse - resposta HTTP
	 */
	private void configureHTTPReponse(HttpServletResponse httpServletResponse) {
		
		httpServletResponse.setContentType(this.fileType.getContentType());
		
		httpServletResponse.addHeader(
			"Content-Disposition", "attachment; filename=" + this.fileName + this.fileType.extension);
	}
	
	/**
	 * Enum para tipo de arquivo.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum FileType {
		
		PDF(".pdf", "application/pdf"),
		XLS(".xls", "application/vnd.ms-excel");
		
		private String extension;
		
		private String contentType;
		
		/**
		 * Construtor.
		 * 
		 * @param extension - extensão
		 * @param contentType - tipo de conteúdo
		 */
		private FileType(String extension, String contentType) {
			
			this.extension = extension;
			this.contentType = contentType;
		}

		/**
		 * @return the extension
		 */
		public String getExtension() {
			return extension;
		}

		/**
		 * @return the contentType
		 */
		public String getContentType() {
			return contentType;
		}
	}

}
