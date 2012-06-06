package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.dto.SlipDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.estoque.VendaEncalhe;
import br.com.abril.nds.service.VendaEncalheService;
import br.com.abril.nds.util.DateUtil;
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
	 * @param idVendaEncalhe
	 * @return SlipDTO
	 */
	@Override
	@Transactional(readOnly = true)
	public SlipDTO obtemDadosSlip(long idVendaEncalhe){
		
		SlipDTO slipVendaEncalhe = null;
		ProdutoEdicaoSlipDTO produtoEdicaoVendaEncalhe = null;
		List<ProdutoEdicaoSlipDTO> listaProdutoEdicaoVendaEncalhe = new ArrayList<ProdutoEdicaoSlipDTO>();
		
		VendaEncalhe ve = new VendaEncalhe();//TO-DO: OBTER VENDA ENCALHE POR ID(idVendaEncalhe)
		
		if (ve!=null) {
			
			slipVendaEncalhe = new SlipDTO();
			
			slipVendaEncalhe.setNomeCota("0001 - Nome da Cota");
			slipVendaEncalhe.setCodigoBox("10 - Suplementar");
			slipVendaEncalhe.setDataConferencia(DateUtil.parseData(DateUtil.formatarDataPTBR(Calendar.getInstance().getTime()),"dd/MM/yyyy"));
			slipVendaEncalhe.setNomeUsuario("Nome do Usuário");
			
			
			
			//ÍTENS - VENDA ENCALHE - TO-DO: OBTER DADOS DE PRODUTO EDIÇÃO DA VENDA ENCALHE EM QUESTÃO
			produtoEdicaoVendaEncalhe = new ProdutoEdicaoSlipDTO();
			
			produtoEdicaoVendaEncalhe.setIdProdutoEdicao(1l);
			produtoEdicaoVendaEncalhe.setNomeProduto("Produto Edição 01");
			produtoEdicaoVendaEncalhe.setNumeroEdicao(2l);
			produtoEdicaoVendaEncalhe.setQuantidadeEfetiva(BigDecimal.TEN);
			produtoEdicaoVendaEncalhe.setPrecoVenda(BigDecimal.TEN);
			produtoEdicaoVendaEncalhe.setValorTotal(new BigDecimal(100));
			
			listaProdutoEdicaoVendaEncalhe.add(produtoEdicaoVendaEncalhe);
			listaProdutoEdicaoVendaEncalhe.add(produtoEdicaoVendaEncalhe);
			listaProdutoEdicaoVendaEncalhe.add(produtoEdicaoVendaEncalhe);
			listaProdutoEdicaoVendaEncalhe.add(produtoEdicaoVendaEncalhe);
			listaProdutoEdicaoVendaEncalhe.add(produtoEdicaoVendaEncalhe);
			
			slipVendaEncalhe.setListaProdutoEdicaoSlipDTO(listaProdutoEdicaoVendaEncalhe);
			
		}
		return slipVendaEncalhe;
	}

	

	/**
	 * Gera um relatório à partir de um Objeto com atributos e listas definidas
	 * @param list
	 * @param pathJasper
	 * @return Array de bytes do relatório gerado
	 * @throws JRException
	 * @throws URISyntaxException
	 */
	private byte[] gerarDocumentoIreport(List<SlipDTO> list, String pathJasper) throws JRException, URISyntaxException{

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
		
		URL url = Thread.currentThread().getContextClassLoader().getResource(pathJasper);
		
		String path = url.toURI().getPath();
		
		return  JasperRunManager.runReportToPdf(path, null, jrDataSource);
	}
	
	
	
	/**
	  * Gera Array de Bytes do Slip de Venda de Encalhe
	  * @param idVendaEncalhe
	  * @return byte[]
	  */
	@Override
	@Transactional(readOnly = true)
	public byte[] geraImpressaoVendaEncalhe(Long idVendaEncalhe){
		
		byte[] relatorio=null;

		SlipDTO slipVendaEncalheDTO = this.obtemDadosSlip(idVendaEncalhe);
		List<SlipDTO> listaSlipVendaEncalheDTO= new ArrayList<SlipDTO>();
		listaSlipVendaEncalheDTO.add(slipVendaEncalheDTO);
		try{
		    relatorio = this.gerarDocumentoIreport(listaSlipVendaEncalheDTO, "/reports/slipVendaEncalhe.jasper");
		}
		catch(Exception e){
			throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao gerar Slip de Venda de Encalhe.");
		}
		return relatorio;
	}
 
	
}
