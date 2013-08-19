package br.com.abril.nds.util.export;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.vo.ExportFilterTestVO;
import br.com.abril.nds.util.export.vo.ExportFooterSmallAlignlessTestVO;
import br.com.abril.nds.util.export.vo.ExportFooterSmallTestVO;
import br.com.abril.nds.util.export.vo.ExportFooterTestVO;
import br.com.abril.nds.util.export.vo.ExportFooterVerticalTestVO;
import br.com.abril.nds.util.export.vo.ExportTestSmallVO;
import br.com.abril.nds.util.export.vo.ExportTestVO;


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
			new ExportFilterTestVO("1111111111111111", "2", "3", "4", "5", "6", "7", "(Mar/12)");
		
		List<ExportTestVO> listaExportTestVOs = new ArrayList<ExportTestVO>();
		
		for (int i = 0; i < 5000; i++) {
			
			listaExportTestVOs.add(
				new ExportTestVO("" + i, "" + i, "" + i, "" + i, "" + i,
					"" + i, "" + i, "" + i, "" + i, "" + i, "(Mar/12)"));
		}

		ExportFooterTestVO exportFooterTestVO = 
			new ExportFooterTestVO(
				new BigDecimal("999.99999"), new BigDecimal("999.99999"), 999L, 
					999L, 999, new BigDecimal("999.99999"), BigDecimal.TEN);
		
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
			new ExportFilterTestVO("1111111111111111", "2", "3", "4", "5", "6", "7", "(Mar/12)");
		
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
	
	@Test
	public void exportSmallVerticalFooterXLS() throws IOException {
		
		OutputStream outputStream = new FileOutputStream("target/testeSmallVerticalFooter" + new Date().getTime() + ".xls");
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		ndsFileHeader.setNomeDistribuidor("Treelog S/A. Logística e Distribuição - SP ");
		ndsFileHeader.setCnpjDistribuidor("00.000.000/00001-00");
		ndsFileHeader.setData(new Date());
		ndsFileHeader.setNomeUsuario("Antonio Carlos Pereira");
		
		ExportFilterTestVO exportFilterTestVO = 
			new ExportFilterTestVO("1111111111111111", "2", "3", "4", "5", "6", "7", "(Mar/12)");
		
		List<ExportTestSmallVO> listaExportTestSmallVOs = new ArrayList<ExportTestSmallVO>();
		
		for (int i = 0; i < 10; i++) {
			
			listaExportTestSmallVOs.add(
				new ExportTestSmallVO("" + i, "" + i, "" + i, "" + i, "" + i));
		}
		
		Map<String, BigDecimal> footerMap = new HashMap<String, BigDecimal>();
		
		footerMap.put("FC", new BigDecimal("100.00"));
		footerMap.put("Dinap", new BigDecimal("200.00"));
		
		ExportFooterVerticalTestVO exportFooterVerticalTestVO = new ExportFooterVerticalTestVO(footerMap);
		
		FileExporter.to("testeSmallVerticalFooter", FileType.XLS).inOutputStream(
			ndsFileHeader, exportFilterTestVO, exportFooterVerticalTestVO, 
				listaExportTestSmallVOs, ExportTestSmallVO.class, outputStream);
	}
	
	@Test
	public void exportLargePDF() throws FileNotFoundException {
		
		OutputStream outputStream = new FileOutputStream("target/teste" + new Date().getTime() + ".pdf");
		
		ExportParameters<ExportTestVO, ExportFilterTestVO, ExportFooterTestVO> exportParameters =
			this.getExportParametersForLarge();
		
		FileExporter.to("teste", FileType.PDF).inOutputStream(
			exportParameters.getNdsFileHeader(), exportParameters.getFilter(), exportParameters.getFooter(), 
		
		exportParameters.getDataList(), exportParameters.getListClass(), outputStream);
	}
	
	@Test
	public void exportSmallPDF() throws FileNotFoundException {
		
		OutputStream outputStream = new FileOutputStream("target/testeSmall" + new Date().getTime() + ".pdf");
		
		ExportParameters<ExportTestSmallVO, ExportFilterTestVO, ExportFooterSmallTestVO> exportParameters =
			this.getExportParametersForSmall();
		
		FileExporter.to("testeSmall", FileType.PDF).inOutputStream(
			exportParameters.getNdsFileHeader(), exportParameters.getFilter(), exportParameters.getFooter(), 
				exportParameters.getDataList(), exportParameters.getListClass(), outputStream);
	}
	
	@Test
	public void exportSmallVerticalFooterPDF() throws FileNotFoundException {
		
		OutputStream outputStream = new FileOutputStream("target/testeSmallVerticalFooter" + new Date().getTime() + ".pdf");
		
		ExportParameters<ExportTestSmallVO, ExportFilterTestVO, ExportFooterVerticalTestVO> exportParameters =
			this.getExportParametersForSmallVerticalFooter();
		
		FileExporter.to("testeSmallVerticalFooter", FileType.PDF).inOutputStream(
			exportParameters.getNdsFileHeader(), exportParameters.getFilter(), exportParameters.getFooter(), 
				exportParameters.getDataList(), exportParameters.getListClass(), outputStream);
	}
	
	@Test
	public void exportSmallAlignlessPDF() throws FileNotFoundException {
		
		OutputStream outputStream = new FileOutputStream("target/testeSmallAlignless" + new Date().getTime() + ".pdf");
		
		ExportParameters<ExportTestSmallVO, ExportFilterTestVO, ExportFooterSmallAlignlessTestVO> exportParameters =
			this.getExportParametersForSmallAlignless();
		
		FileExporter.to("testeSmallAlignless", FileType.PDF).inOutputStream(
			exportParameters.getNdsFileHeader(), exportParameters.getFilter(), exportParameters.getFooter(), 
				exportParameters.getDataList(), exportParameters.getListClass(), outputStream);
	}
	
	private ExportParameters<ExportTestVO, ExportFilterTestVO, ExportFooterTestVO> getExportParametersForLarge() {
		
		ExportParameters<ExportTestVO, ExportFilterTestVO, ExportFooterTestVO> exportParameters =
			new ExportParameters<ExportTestVO, ExportFilterTestVO, ExportFooterTestVO>();
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		ndsFileHeader.setNomeDistribuidor("Treelog S/A. Logística e Distribuição - SP ");
		ndsFileHeader.setCnpjDistribuidor("00.000.000/00001-00");
		ndsFileHeader.setData(new Date());
		ndsFileHeader.setNomeUsuario("Antonio Carlos Pereira");
		
		ExportFilterTestVO exportFilterTestVO = 
			new ExportFilterTestVO("1111111111111111", "2", "3", "4", "5", "6", "7", "(Mar/12)");
		
		List<ExportTestVO> listaExportTestVOs = new ArrayList<ExportTestVO>();
		
		for (int i = 0; i < 5000; i++) {
			
			listaExportTestVOs.add(
				new ExportTestVO("" + i, "" + i, "" + i, "" + i, "" + i,
					"" + i, "" + i, "" + i, "" + i, "" + i, "(Mar/12)"));
		}
		
		ExportFooterTestVO exportFooterTestVO = 
			new ExportFooterTestVO(
				new BigDecimal("999.99999"), new BigDecimal("999.99999"), 999L, 
					999L, 999, new BigDecimal("999.99999"), BigDecimal.TEN);

		exportParameters.setNdsFileHeader(ndsFileHeader);
		exportParameters.setFilter(exportFilterTestVO);
		exportParameters.setDataList(listaExportTestVOs);
		exportParameters.setListClass(ExportTestVO.class);
		exportParameters.setFooter(exportFooterTestVO);
		
		return exportParameters;
	}
	
	private ExportParameters<ExportTestSmallVO, ExportFilterTestVO, ExportFooterSmallTestVO> getExportParametersForSmall() {
		
		ExportParameters<ExportTestSmallVO, ExportFilterTestVO, ExportFooterSmallTestVO> exportParameters =
			new ExportParameters<ExportTestSmallVO, ExportFilterTestVO, ExportFooterSmallTestVO>();
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		ndsFileHeader.setNomeDistribuidor("Treelog S/A. Logística e Distribuição - SP ");
		ndsFileHeader.setCnpjDistribuidor("00.000.000/00001-00");
		ndsFileHeader.setData(new Date());
		ndsFileHeader.setNomeUsuario("Antonio Carlos Pereira");
		
		ExportFilterTestVO exportFilterTestVO = 
			new ExportFilterTestVO("1111111111111111", "2", "3", "4", "5", "6", "7", "(Mar/12)");
		
		List<ExportTestSmallVO> listaExportTestSmallVOs = new ArrayList<ExportTestSmallVO>();
		
		for (int i = 0; i < 10; i++) {
			
			listaExportTestSmallVOs.add(
				new ExportTestSmallVO("" + i, "" + i, "" + i, "" + i, "" + i));
		}
		
		ExportFooterSmallTestVO exportFooterSmallTestVO = 
			new ExportFooterSmallTestVO(
				new BigDecimal("999.99999"), new BigDecimal("999.99999"), 999L);
		
		exportParameters.setNdsFileHeader(ndsFileHeader);
		exportParameters.setFilter(exportFilterTestVO);
		exportParameters.setDataList(listaExportTestSmallVOs);
		exportParameters.setListClass(ExportTestSmallVO.class);
		exportParameters.setFooter(exportFooterSmallTestVO);
		
		return exportParameters;
	}
	
	private ExportParameters<ExportTestSmallVO, ExportFilterTestVO, ExportFooterVerticalTestVO> getExportParametersForSmallVerticalFooter() {
		
		ExportParameters<ExportTestSmallVO, ExportFilterTestVO, ExportFooterVerticalTestVO> exportParameters =
			new ExportParameters<ExportTestSmallVO, ExportFilterTestVO, ExportFooterVerticalTestVO>();
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		ndsFileHeader.setNomeDistribuidor("Treelog S/A. Logística e Distribuição - SP ");
		ndsFileHeader.setCnpjDistribuidor("00.000.000/00001-00");
		ndsFileHeader.setData(new Date());
		ndsFileHeader.setNomeUsuario("Antonio Carlos Pereira");
		
		ExportFilterTestVO exportFilterTestVO = 
			new ExportFilterTestVO("1111111111111111", "2", "3", "4", "5", "6", "7", "(Mar/12)");
		
		List<ExportTestSmallVO> listaExportTestSmallVOs = new ArrayList<ExportTestSmallVO>();
		
		for (int i = 0; i < 10; i++) {
			
			listaExportTestSmallVOs.add(
				new ExportTestSmallVO("" + i, "" + i, "" + i, "" + i, "" + i));
		}
		
		Map<String, BigDecimal> footerMap = new HashMap<String, BigDecimal>();
		
		footerMap.put("FC", new BigDecimal("100.00"));
		footerMap.put("Dinap", new BigDecimal("200.00"));
		
		ExportFooterVerticalTestVO exportFooterVerticalTestVO = new ExportFooterVerticalTestVO(footerMap);
		
		exportParameters.setNdsFileHeader(ndsFileHeader);
		exportParameters.setFilter(exportFilterTestVO);
		exportParameters.setDataList(listaExportTestSmallVOs);
		exportParameters.setListClass(ExportTestSmallVO.class);
		exportParameters.setFooter(exportFooterVerticalTestVO);
		
		return exportParameters;
	}
	
	private ExportParameters<ExportTestSmallVO, ExportFilterTestVO, ExportFooterSmallAlignlessTestVO> getExportParametersForSmallAlignless() {
		
		ExportParameters<ExportTestSmallVO, ExportFilterTestVO, ExportFooterSmallAlignlessTestVO> exportParameters =
			new ExportParameters<ExportTestSmallVO, ExportFilterTestVO, ExportFooterSmallAlignlessTestVO>();
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		ndsFileHeader.setNomeDistribuidor("Treelog S/A. Logística e Distribuição - SP ");
		ndsFileHeader.setCnpjDistribuidor("00.000.000/00001-00");
		ndsFileHeader.setData(new Date());
		ndsFileHeader.setNomeUsuario("Antonio Carlos Pereira");
		
		ExportFilterTestVO exportFilterTestVO = 
			new ExportFilterTestVO("1111111111111111", "2", "3", "4", "5", "6", "7", "(Mar/12)");
		
		List<ExportTestSmallVO> listaExportTestSmallVOs = new ArrayList<ExportTestSmallVO>();
		
		for (int i = 0; i < 10; i++) {
			
			listaExportTestSmallVOs.add(
				new ExportTestSmallVO("" + i, "" + i, "" + i, "" + i, "" + i));
		}
		
		ExportFooterSmallAlignlessTestVO exportFooterSmallTestVO = 
			new ExportFooterSmallAlignlessTestVO(
				new BigDecimal("999.99999"), new BigDecimal("999.99999"), 999L);
		
		exportParameters.setNdsFileHeader(ndsFileHeader);
		exportParameters.setFilter(exportFilterTestVO);
		exportParameters.setDataList(listaExportTestSmallVOs);
		exportParameters.setListClass(ExportTestSmallVO.class);
		exportParameters.setFooter(exportFooterSmallTestVO);
		
		return exportParameters;
	}

}
