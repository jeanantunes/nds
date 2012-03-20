package br.com.abril.nds.util.export;

import java.io.OutputStream;
import java.util.List;

public class FileExporter implements Exporter {
	
	private static FileExporter instance;
	
	private FileType fileType;
	
	private FileExporter() {
		
	}
	
	public static FileExporter to(FileType fileType) {
		
		instance = new FileExporter();
		
		instance.fileType = fileType;
		
		return instance;
	}
	
	public <T, F, FT> void exportInOutputStream(F filter,
											    FT footer,
											    List<T> dataList, 
											    Class<T> listClass,
											    OutputStream outputStream) {
		
		if (FileType.PDF.equals(instance.fileType)) {
			
			new PDFExporter().exportInOutputStream(filter, footer, dataList, listClass, outputStream);
			
		} else if (FileType.XLS.equals(instance.fileType)) {
			
			new XLSExporter().exportInOutputStream(filter, footer, dataList, listClass, outputStream);
			
		} else {
			
			throw new RuntimeException("Tipo de arquivo não suportado para exportação");
		}
	}
	
	public enum FileType {
		
		PDF,
		XLS;
	}

}
