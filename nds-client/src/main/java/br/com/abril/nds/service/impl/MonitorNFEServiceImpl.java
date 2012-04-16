package br.com.abril.nds.service.impl;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import br.com.abril.nds.dto.CobrancaImpressaoDTO;
import br.com.abril.nds.dto.DanfeDTO;

public class MonitorNFEServiceImpl {
	
	
	private byte[] gerarDocumentoIreport(List<DanfeDTO> list) throws JRException{
		
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
		
		 Map<String, Object> map = null; //parametros
		 
		 return  JasperRunManager.runReportToPdf(Thread.currentThread().getContextClassLoader()
				 	.getResource("/reports/danfe.jasper").getFile(), map, jrDataSource);
	}
	
}
