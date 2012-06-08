package br.com.abril.nds.service.impl;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.abril.nds.dto.SlipVendaEncalheDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.estoque.VendaEncalhe;
import br.com.abril.nds.service.VendaEncalheService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TipoMensagem;

/**
 * 
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.estoque.VendaEncalhe}  
 * 
 * @author Discover Technology
 *
 */

@Service
public class VendaEncalheServiceImpl implements VendaEncalheService {

	
	/**
	 * Obtém dados da venda encalhe por id
	 * @param idCota
	 * @param dataInicio
	 * @param dataFim
	 * @return List<SlipVendaEncalheDTO>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<SlipVendaEncalheDTO> obtemDadosSlip(long idCota, Date dataInicio, Date dataFim){
		
		
		
		SlipVendaEncalheDTO slipVendaEncalhe = null;
		List<SlipVendaEncalheDTO> listaSlipVendaEncalhe = new ArrayList<SlipVendaEncalheDTO>();
		
		List<VendaEncalhe> listaVe = new ArrayList<VendaEncalhe>();//TO-DO: OBTER VENDAS DE ENCALHE POR ID_COTA E RANGE DE DATAS
		
		
		
		//SIMULAÇÃO DE VENDAS DE ENCALHE ENCONTRADAS
		VendaEncalhe veTeste01 = new VendaEncalhe();
		VendaEncalhe veTeste02 = new VendaEncalhe();
		VendaEncalhe veTeste03 = new VendaEncalhe();
		VendaEncalhe veTeste04 = new VendaEncalhe();
		VendaEncalhe veTeste05 = new VendaEncalhe();
		VendaEncalhe veTeste06 = new VendaEncalhe();
		VendaEncalhe veTeste07 = new VendaEncalhe();
		VendaEncalhe veTeste08 = new VendaEncalhe();
		VendaEncalhe veTeste09 = new VendaEncalhe();
		VendaEncalhe veTeste10 = new VendaEncalhe();
		listaVe = Arrays.asList(veTeste01,veTeste02,veTeste03,veTeste04,veTeste05,veTeste06,veTeste07,veTeste08,veTeste09,veTeste10);
		

		
		if (listaVe!=null && listaVe.size()>0) {
			
			Double quantidadeTotalVista = 0d;
			Double valorTotalVista = 0d;
			Double quantidadeTotalPrazo = 0d;
			Double valorTotalPrazo = 0d;
			
			for (VendaEncalhe itemVE:listaVe){
			
				slipVendaEncalhe = new SlipVendaEncalheDTO();
				
				
				
				//DADOS PARA SIMULAÇÃO - TO-DO - OBTER DO ITEM ATUAL DA ITERAÇÃO
				slipVendaEncalhe.setNomeCota("Nome da Cota");
				slipVendaEncalhe.setNumeroCota("0001");
				slipVendaEncalhe.setNumeroBox("10");
				slipVendaEncalhe.setDescricaoBox("Suplementar");
				slipVendaEncalhe.setData("01/01/2012");
				slipVendaEncalhe.setHora("12:00");
				slipVendaEncalhe.setUsuario("Nome do Usuário");
				
				slipVendaEncalhe.setCodigo("1");
				slipVendaEncalhe.setProduto("Nome Produto Edição");
				slipVendaEncalhe.setEdicao("2");
				slipVendaEncalhe.setQuantidade("15");
				slipVendaEncalhe.setPreco("R$150,00");
				slipVendaEncalhe.setTotal("R$300,00");
				
				quantidadeTotalVista += 15;
				valorTotalVista += 300;
				quantidadeTotalPrazo += 15;
				valorTotalPrazo += 300;
				
				
				
				slipVendaEncalhe.setQuantidadeTotalVista(quantidadeTotalVista.toString());
				slipVendaEncalhe.setValorTotalVista(valorTotalVista.toString());
				
				slipVendaEncalhe.setQuantidadeTotalPrazo(quantidadeTotalPrazo.toString());
				slipVendaEncalhe.setValorTotalPrazo(valorTotalPrazo.toString());
				
				slipVendaEncalhe.setQuantidadeTotalGeral(CurrencyUtil.formatarValor(quantidadeTotalVista + quantidadeTotalPrazo));
				slipVendaEncalhe.setValorTotalGeral(CurrencyUtil.formatarValor(valorTotalVista + valorTotalPrazo));
	
				listaSlipVendaEncalhe.add(slipVendaEncalhe);	
				
			}
		}
		
		return listaSlipVendaEncalhe;
	}

	

	/**
	 * Gera um relatório à partir de um Objeto com atributos e listas definidas
	 * @param list
	 * @param pathJasper
	 * @return Array de bytes do relatório gerado
	 * @throws JRException
	 * @throws URISyntaxException
	 */
	private byte[] gerarDocumentoIreport(List<SlipVendaEncalheDTO> list, String pathJasper) throws JRException, URISyntaxException{

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
		
		URL url = Thread.currentThread().getContextClassLoader().getResource(pathJasper);
		
		String path = url.toURI().getPath();
		
		return  JasperRunManager.runReportToPdf(path, null, jrDataSource);
	}
	
	
	
	/**
	 * Gera Array de Bytes do Slip de Venda de Encalhe
	 * @param idCota
	 * @param dataInicio
	 * @param dataFim
	 * @return byte[]
	 */
	@Override
	@Transactional(readOnly = true)
	public byte[] geraImpressaoVendaEncalhe(long idCota, Date dataInicio, Date dataFim){
		
		byte[] relatorio=null;

		List<SlipVendaEncalheDTO> listaSlipVendaEncalheDTO = this.obtemDadosSlip(idCota, dataInicio, dataFim); 

		try{
		    relatorio = this.gerarDocumentoIreport(listaSlipVendaEncalheDTO, "/reports/slipVendaEncalhe.jasper");
		}
		catch(Exception e){
			throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao gerar Slip de Venda de Encalhe.");
		}
		return relatorio;
	}
 
	
	
	
	/**
	 * Gera Array de Bytes do Slip de Venda de Suplementar
	 * @param idCota
	 * @param dataInicio
	 * @param dataFim
	 * @return byte[]
	 */
	@Override
	@Transactional(readOnly = true)
	public byte[] geraImpressaoVendaSuplementar(long idCota, Date dataInicio, Date dataFim){
		
		byte[] relatorio=null;

		List<SlipVendaEncalheDTO> listaSlipVendaEncalheDTO = this.obtemDadosSlip(idCota, dataInicio, dataFim); 

		try{
		    relatorio = this.gerarDocumentoIreport(listaSlipVendaEncalheDTO, "/reports/slipVendaSuplementar.jasper");
		}
		catch(Exception e){
			throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao gerar Slip de Venda de Suplementar.");
		}
		return relatorio;
	}
	
}
