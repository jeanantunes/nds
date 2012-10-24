package br.com.abril.nds.service.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import br.com.abril.nds.client.vo.ImpressaoBandeiraVO;
import br.com.abril.nds.dto.FornecedoresBandeiraDTO;
import br.com.abril.nds.service.ChamadaEncalheService;
import br.com.abril.nds.service.EmissaoBandeiraService;


@Service
public class EmissaoBandeiraServiceImpl implements  EmissaoBandeiraService {
	
	@Autowired
	private ChamadaEncalheService chamadaEncalheService;

	@Override
	public byte[] imprimirBandeira(Integer semana, Integer numeroPallets) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		List<FornecedoresBandeiraDTO> lista = chamadaEncalheService.obterDadosFornecedoresParaImpressaoBandeira(semana);
		List<ImpressaoBandeiraVO> listaRelatorio = new ArrayList<ImpressaoBandeiraVO>(); 
		for (FornecedoresBandeiraDTO bandeiraDTO : lista){
			for (int i=1; i<=numeroPallets;i++ ){
				listaRelatorio.add(new ImpressaoBandeiraVO(bandeiraDTO,i+"/"+numeroPallets));
			}
				
		}
	    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaRelatorio); 
		URL url = Thread.currentThread().getContextClassLoader().getResource("/reports/emissao_bandeira.jasper");
		String path = url.toURI().getPath();
		return JasperRunManager.runReportToPdf(path, parameters,ds);
	}

	@Override
	public byte[] imprimirBandeiraManual(Integer semana, Integer numeroPallets,
			String nome, String codigoPracaNoProdin, String praca,
			String destino, String canal) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		List<ImpressaoBandeiraVO> listaRelatorio = new ArrayList<ImpressaoBandeiraVO>(); 
		for (int i=1; i<=numeroPallets;i++ ){
				listaRelatorio.add(new ImpressaoBandeiraVO(nome, semana, codigoPracaNoProdin, praca, destino, canal,i+"/"+numeroPallets));
		}
				
		
	    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaRelatorio); 
		URL url = Thread.currentThread().getContextClassLoader().getResource("/reports/emissao_bandeira.jasper");
		String path = url.toURI().getPath();
		return JasperRunManager.runReportToPdf(path, parameters,ds);
		
	}

}
