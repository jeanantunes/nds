package br.com.abril.nds.service.impl;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.DanfeDTO;
import br.com.abril.nds.dto.InfoNfeDTO;
import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.service.MonitorNFEService;

@Service
public class MonitorNFEServiceImpl implements MonitorNFEService {
	
	public InfoNfeDTO pesquisarNFe(FiltroMonitorNfeDTO filtro) {
		//TODO: remover mock apos testes
		InfoNfeDTO info = new InfoNfeDTO();
		info.setListaNfeDTO(getListaMockada());
		info.setQtdeRegistros(info.getListaNfeDTO().size());
		
		return info;
		
	}
	
	private List<NfeDTO> getListaMockada() {
		
		List<NfeDTO> lista = new ArrayList<NfeDTO>();
		
		int contador = 0;
		
		while(contador++<10) {
			
			NfeDTO nfe = new NfeDTO();
			
			nfe.setCnpjDestinatario("000000000"+contador);
			nfe.setCpfDestinatario("0000000"+contador);

			nfe.setCnpjRemetente("000000000"+contador);
			nfe.setCpfRemetente("0000000"+contador);

			nfe.setEmissao(new Date());
			nfe.setTipoEmissao("tipoEmissao_");
			nfe.setMovimentoIntegracao("movimento_");
			nfe.setNumero("000"+contador);
			nfe.setSerie("000"+contador);
			nfe.setStatusNfe("status_");
			nfe.setTipoNfe("tiponfe_");
			
			lista.add(nfe);
			
		}
		
		return lista;
		
	}
	
	private byte[] gerarDocumentoIreport(List<DanfeDTO> list) throws JRException, URISyntaxException{

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("/reports/danfe.jasper");
		
		String path = url.toURI().getPath();
		
		return  JasperRunManager.runReportToPdf(path, null, jrDataSource);
	}
	
}
