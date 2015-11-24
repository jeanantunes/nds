package br.com.abril.nds.util.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;

/**
 * Classe responsável pela exportação de arquivos no sistema.
 * 
 * @author Discover Technology
 *
 */
public class FileExporter {

	private FileType fileType;
	
	private String fileName;
	
	/**
	 * Construtor privado.
	 */
	private FileExporter() {
		
	}
	
	/**
	 * Obtém uma instância para exportação.
	 * 
	 * @param fileName - nome do arquivo a ser exportado
	 * @param fileType - tipo do arquivo a ser exportado
	 * 
	 * @return Instância de FileExporter
	 */
	public static FileExporter to(String fileName, FileType fileType) {
		
		FileExporter fileExporter = new FileExporter();
		
		if (fileName != null) {
			
			fileName += "-" + DateUtil.formatarData(new Date(), Constantes.DATE_PATTERN_PT_BR_FOR_FILE);
		}
		
		fileExporter.fileName = fileName;
		
		fileExporter.fileType = fileType;
		
		return fileExporter;
	}
	
	/**
	 * Realiza a exportação do arquivo via OutputStream.
	 * 
	 * @param ndsFileHeader
	 * @param filter
	 * @param footer
	 * @param dataList
	 * @param listClass
	 * @param outputStream
	 */
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

	/**
	 * @deprecated - Método depreciado, devido a refactor na utilização do footer na exportação.
	 * 				 Utilizar o método {@link FileExporter#inHTTPResponse(NDSFileHeader, Object, List, Class, HttpServletResponse)}
	 * 				 e a anotação {@link Footer} para geração de rodapé.
	 * 
	 * Realiza a exportação na resposta HTTP.
	 * 
	 * @param ndsFileHeader
	 * @param filter
	 * @param footer
	 * @param dataList
	 * @param listClass
	 * @param httpServletResponse
	 * @throws IOException
	 */
	public <T, F, FT> void inHTTPResponse(NDSFileHeader ndsFileHeader,
										  F filter,
										  FT footer,
										  List<T> dataList, 
										  Class<T> listClass,
										  HttpServletResponse httpServletResponse) throws IOException {
		
		this.configureHTTPReponse(httpServletResponse);
		
		this.inOutputStream(ndsFileHeader, filter, footer, dataList, listClass, httpServletResponse.getOutputStream());
	}
	
	public <T, F, FT> void inHTTPResponse(NDSFileHeader ndsFileHeader,
										  F filter, List<T> dataList, Class<T> listClass,
										  HttpServletResponse httpServletResponse) throws IOException {

		this.configureHTTPReponse(httpServletResponse);

		this.inOutputStream(ndsFileHeader, filter, null, dataList, listClass,
				httpServletResponse.getOutputStream());
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
		TXT(".txt", "application/text"),
		PDF(".pdf", "application/pdf"),
		XLS(".xls", "application/vnd.ms-excel"),
		XML(".xml", "text/xml"),
		DOC(".doc", "application/msword"),
		DOCX(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
		JPG(".jpg", "image/vnd.sealedmedia.softseal.jpg"),
		JPEG(".jpeg", "image/jpeg"),
		PNG(".png", "image/png"),
		GIF(".gif", "image/gif"),
		BMP(".bmp", "image/vnd.wap.wbmp"),
		JKS(".jks", "application/octet-stream"),
		RET(".RET", "application/text"),
		PFX(".pfx", "application/x-pkcs12");

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
