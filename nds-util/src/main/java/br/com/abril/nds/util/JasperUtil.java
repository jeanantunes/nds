package br.com.abril.nds.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRProperties;
import br.com.abril.nds.util.export.ExportModel;
import br.com.abril.nds.util.export.FileExporter.FileType;

public class JasperUtil {

	public JasperUtil() { }

	private JasperPrint getRelatorioPreenchido(ExportModel exportModel,
											   Map<String, Object> parametros,
											   InputStream fileInputStream) {

		if (exportModel == null) {
			
			throw new RuntimeException("O data source deste relatório está vazio.");
		}

		try {

			List<ExportModel> datasource = new ArrayList<ExportModel>();
			
			datasource.add(exportModel);
			
			JRDataSource jrDataSource = new JRBeanCollectionDataSource(datasource);

			JasperPrint jasperPrint = 
				JasperFillManager.fillReport(
					fileInputStream, parametros, jrDataSource);

			return jasperPrint;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void exportarXLS(ExportModel exportModel,
							Map<String, Object> parametros,
							InputStream fileInputStream, 
							OutputStream outputStream,
							String reportName) {

		try {
			
			JRXlsExporter xlsExporter = new JRXlsExporter();

			JRProperties.setProperty(JExcelApiExporterParameter.PROPERTY_DETECT_CELL_TYPE,  true);
			
			JRProperties.setProperty(JExcelApiExporterParameter.PROPERTY_SHEET_NAMES_PREFIX,  reportName);
			
			JasperPrint jasperPrint = getRelatorioPreenchido(exportModel, parametros, fileInputStream);
			
			xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

			xlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			
			xlsExporter.exportReport();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] exportarPDF(ExportModel exportModel,
							  Map<String, Object> parametros,
							  InputStream fileInputStream)  {

		try {

			JasperPrint jasperPrint = getRelatorioPreenchido(exportModel, parametros, fileInputStream);
			
			byte[] stream = JasperExportManager.exportReportToPdf(jasperPrint);

			return stream;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void exportReport(FileType fileType, 
							 ExportModel exportModel,
							 Map<String, Object> parametros,
							 InputStream pathRelatorio,
							 String reportName,
							 HttpServletResponse response) throws Exception {
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		reportName += DateUtil.formatarDataPTBR(new Date()) + "." + fileType.getExtension();
		
		byte[] byteArray = null;
		
		if (FileType.XLS.equals(fileType)) {

			exportarXLS(exportModel, parametros, pathRelatorio, outputStream, reportName);

			byteArray = outputStream.toByteArray();

		} else if (FileType.PDF.equals(fileType)) {

			byteArray = exportarPDF(exportModel, parametros, pathRelatorio);
		}
		
		realizarTransferencia(byteArray, response, reportName, fileType);
	}
	
	public void exportReport(FileType fileType, 
							 ExportModel exportModel,
							 Map<String, Object> parametros,
							 InputStream pathRelatorio,
							 String reportName,
							 OutputStream outputStream) throws Exception {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		reportName += DateUtil.formatarData(new Date(), "dd-MM-yyyy") + fileType.getExtension();

		byte[] byteArray = null;

		if (FileType.XLS.equals(fileType)) {

			exportarXLS(exportModel, parametros, pathRelatorio, byteArrayOutputStream, reportName);

			byteArray = byteArrayOutputStream.toByteArray();

		} else if (FileType.PDF.equals(fileType)) {

			byteArray = exportarPDF(exportModel, parametros, pathRelatorio);
		}

		realizarTransferencia(byteArray, outputStream, reportName, fileType);
	}

	private void realizarTransferencia(byte[] byteArray, 
									   HttpServletResponse response,
									   String fileName, 
									   FileType fileType) throws Exception {
		
		if (byteArray != null && byteArray.length > 0) {
			 
			response.setContentType(fileType.getContentType());
			response.setHeader("Content-disposition","attachment; filename=" + fileName); 
			response.setContentLength(byteArray.length); 

			realizarTransferencia(byteArray, response.getOutputStream(), fileName, fileType);
		} 
	}
	
	private void realizarTransferencia(byte[] byteArray,
									  OutputStream outputStream, 
									  String fileName, 
									  FileType fileType) throws Exception {

		if (byteArray != null && byteArray.length > 0) {

			outputStream.write(byteArray, 0, byteArray.length);

			outputStream.flush();

			outputStream.close();
		}
	}

}