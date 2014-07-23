package br.com.abril.nds.service.impl;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.client.vo.ImpressaoBandeiraVO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.service.ChamadaEncalheService;
import br.com.abril.nds.service.EmissaoBandeiraService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.util.JasperUtil;


@Service
public class EmissaoBandeiraServiceImpl implements  EmissaoBandeiraService {
	
	@Autowired
	private ChamadaEncalheService chamadaEncalheService;
	
	@Autowired
	protected ParametrosDistribuidorService parametrosDistribuidorService;

	@Override
	public byte[] imprimirBandeira(Integer semana, Integer numeroPallets,
			Date dataEnvio, Long forncedor) throws Exception {
		
		List<FornecedorDTO> lista = chamadaEncalheService.obterDadosFornecedoresParaImpressaoBandeira(semana, forncedor);
		List<ImpressaoBandeiraVO> listaRelatorio = new ArrayList<ImpressaoBandeiraVO>(); 
		
		for (FornecedorDTO bandeiraDTO : lista){
			for (int i=1; i<=numeroPallets;i++ ){
				listaRelatorio.add(new ImpressaoBandeiraVO(bandeiraDTO,i+" / "+numeroPallets, semana,
						dataEnvio));
			}
		}
	    
		return this.gerarRelatorio(listaRelatorio);
	}

	@Override
	public byte[] imprimirBandeiraManual(String semana, Integer numeroPallets,
			String fornecedor, String praca,
			String canal, String dataEnvio, String titulo) throws Exception {
		
		List<ImpressaoBandeiraVO> listaRelatorio = new ArrayList<ImpressaoBandeiraVO>(); 
		
		for (int i=1; i<=numeroPallets;i++ ){
			listaRelatorio.add(new ImpressaoBandeiraVO(fornecedor, semana, praca, canal,
					i+" / "+numeroPallets, dataEnvio, titulo));
		}
				
		return this.gerarRelatorio(listaRelatorio);
	}
	
	private byte[] gerarRelatorio(List<ImpressaoBandeiraVO> listaRelatorio) throws URISyntaxException, JRException {
		
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaRelatorio); 
		
	    URL url = Thread.currentThread().getContextClassLoader().getResource("/reports/emissao_bandeira.jasper");
		
		String path = url.toURI().getPath();
		
		InputStream inputStream = parametrosDistribuidorService.getLogotipoDistribuidor();
		
		if(inputStream == null) {
			inputStream = new ByteArrayInputStream(new byte[0]);
		}
		
		Image image = JasperUtil.getImagemRelatorio(inputStream);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("LOGO_DISTRIBUIDOR", image);
		
		return JasperRunManager.runReportToPdf(path, parameters,ds);
	}

}
