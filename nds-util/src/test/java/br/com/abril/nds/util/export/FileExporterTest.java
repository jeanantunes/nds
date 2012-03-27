package br.com.abril.nds.util.export;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import br.com.abril.nds.util.export.FileExporter.FileType;


public class FileExporterTest {
	
	@Test
	public void exportXLS() throws IOException {
		
		OutputStream outputStream = new FileOutputStream("target/teste" + new Date().getTime() + ".xls");
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		ndsFileHeader.setNomeDistribuidor("Treelog S/A. Logística e Distribuição - SP ");
		ndsFileHeader.setCnpjDistribuidor("00.000.000/00001-00");
		ndsFileHeader.setData(new Date());
		ndsFileHeader.setNomeUsuario("Antonio Carlos Pereira");
		
		ExportFilterTestVO exportFilterTestVO = 
			new ExportFilterTestVO("1111111111111111", "2", "3", "4", "5", "6", "7");
		
		List<ExportTestVO> listaExportTestVOs = new ArrayList<ExportTestVO>();
		
		for (int i = 0; i < 5000; i++) {
			
			listaExportTestVOs.add(
				new ExportTestVO("" + i, "" + i, "" + i, "" + i, "" + i,
						"" + i, "" + i, "" + i, "" + i, "" + i));
		}
		
		ExportFooterTestVO exportFooterTestVO = 
			new ExportFooterTestVO(
				new BigDecimal("999.99999"), new BigDecimal("999.99999"), 999L, 
					999L, 999, new BigDecimal("999.99999"));
		
		FileExporter.to("teste", FileType.XLS).inOutputStream(
			ndsFileHeader, exportFilterTestVO, exportFooterTestVO, 
				listaExportTestVOs, ExportTestVO.class, outputStream);
	}
	
	@Test
	public void exportSmallXLS() throws IOException {
		
		OutputStream outputStream = new FileOutputStream("target/testeSmall" + new Date().getTime() + ".xls");
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		ndsFileHeader.setNomeDistribuidor("Treelog S/A. Logística e Distribuição - SP ");
		ndsFileHeader.setCnpjDistribuidor("00.000.000/00001-00");
		ndsFileHeader.setData(new Date());
		ndsFileHeader.setNomeUsuario("Antonio Carlos Pereira");
		
		ExportFilterTestVO exportFilterTestVO = 
			new ExportFilterTestVO("1111111111111111", "2", "3", "4", "5", "6", "7");
		
		List<ExportTestSmallVO> listaExportTestSmallVOs = new ArrayList<ExportTestSmallVO>();
		
		for (int i = 0; i < 10; i++) {
			
			listaExportTestSmallVOs.add(
				new ExportTestSmallVO("" + i, "" + i, "" + i, "" + i, "" + i));
		}
		
		ExportFooterSmallTestVO exportFooterSmallTestVO = 
			new ExportFooterSmallTestVO(
				new BigDecimal("999.99999"), new BigDecimal("999.99999"), 999L);
		
		FileExporter.to("teste", FileType.XLS).inOutputStream(
			ndsFileHeader, exportFilterTestVO, exportFooterSmallTestVO, 
				listaExportTestSmallVOs, ExportTestSmallVO.class, outputStream);
	}

}
