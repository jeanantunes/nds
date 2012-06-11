package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.SlipVendaEncalheDTO;
import br.com.abril.nds.dto.VendaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaEncalheDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.model.estoque.VendaProduto;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.VendaProdutoRepository;
import br.com.abril.nds.service.VendaEncalheService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TipoMensagem;

/**
 * 
 * Classe de implementação de serviços referentes a vendas de encalhe
 * 
 * @author Discover Technology
 *
 */

@Service
public class VendaEncalheServiceImpl implements VendaEncalheService {
	
	@Autowired
	private VendaProdutoRepository vendaProdutoRepository;
	
	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRespository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
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
		

		VendaProduto ve = new VendaProduto();//TO-DO: OBTER VENDA ENCALHE POR ID(idVendaEncalhe)
		
		SlipVendaEncalheDTO slipVendaEncalhe = null;
		List<SlipVendaEncalheDTO> listaSlipVendaEncalhe = new ArrayList<SlipVendaEncalheDTO>();
		
		List<VendaProduto> listaVe = new ArrayList<VendaProduto>();//TO-DO: OBTER VENDAS DE ENCALHE POR ID_COTA E RANGE DE DATAS
		
		
		
		//SIMULAÇÃO DE VENDAS DE ENCALHE ENCONTRADAS
		VendaProduto veTeste01 = new VendaProduto();
		VendaProduto veTeste02 = new VendaProduto();
		VendaProduto veTeste03 = new VendaProduto();
		VendaProduto veTeste04 = new VendaProduto();
		VendaProduto veTeste05 = new VendaProduto();
		VendaProduto veTeste06 = new VendaProduto();
		VendaProduto veTeste07 = new VendaProduto();
		VendaProduto veTeste08 = new VendaProduto();
		VendaProduto veTeste09 = new VendaProduto();
		VendaProduto veTeste10 = new VendaProduto();
		listaVe = Arrays.asList(veTeste01,veTeste02,veTeste03,veTeste04,veTeste05,veTeste06,veTeste07,veTeste08,veTeste09,veTeste10);
		

		
		if (listaVe!=null && listaVe.size()>0) {
			
			Double quantidadeTotalVista = 0d;
			Double valorTotalVista = 0d;
			Double quantidadeTotalPrazo = 0d;
			Double valorTotalPrazo = 0d;
			
			for (VendaProduto itemVE:listaVe){
			
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
 
	@Override
	@Transactional(readOnly=true)
	public VendaEncalheDTO buscarVendaEncalhe(Long idVendaEncalhe) {
		
		VendaEncalheDTO vendaEncalheDTO = vendaProdutoRepository.buscarVendaProdutoEncalhe(idVendaEncalhe);
		
		if(vendaEncalheDTO!= null){
			
			ProdutoEdicao produtoEdicao  =  produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(vendaEncalheDTO.getCodigoProduto(), 
																											 vendaEncalheDTO.getNumeroEdicao().longValue());
			
			if(produtoEdicao!= null){
					
				Integer qntProduto = getQntProdutoEstoque(vendaEncalheDTO.getTipoVendaEncalhe(), produtoEdicao.getId());
			
				vendaEncalheDTO.setQntDisponivelProduto(qntProduto);
			}
		}

		/*vendaEncalheDTO.setCodigoProduto("1");
		vendaEncalheDTO.setNomeProduto("Veja");
		vendaEncalheDTO.setNumeroEdicao(123);
		vendaEncalheDTO.setPrecoCapa(BigDecimal.TEN);
		
		vendaEncalheDTO.setCodigoBarras("ABC");
		vendaEncalheDTO.setFormaVenda("A Vista"); //formaComercializacao
		vendaEncalheDTO.setCodBox("bOX 12");
		vendaEncalheDTO.setNomeCota("Jose");
		vendaEncalheDTO.setNumeroCota(123);
		vendaEncalheDTO.setDataVencimentoDebito(new Date());
		vendaEncalheDTO.setDataVenda(new Date());
		vendaEncalheDTO.setValoTotalProduto(new BigDecimal(100));
		vendaEncalheDTO.setQntProduto(10);
		vendaEncalheDTO.setIdVenda(10L);*/
		
		return vendaEncalheDTO;
	
	}

	@Override
	public void efetivarVendaEncalhe(VendaEncalheDTO vendaEncalheDTO) {
		//TODO venda de encalhe
		
	}

	@Override
	public void excluirVendaEncalhe(Long idVendaEncalhe) {
		//TODO venda de encalhe
		
	}

	@Override
	public void alterarVendaEncalhe(VendaEncalheDTO vendaEncalheDTO) {
		//TODO venda de encalhe
		
	}

	@Override
	public byte[] gerarSlipVendaEncalhe(Long idVendaEncalhe) {
		//TODO venda de encalhe
		return null;
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
	
	@Override
	@Transactional(readOnly=true)
	public VendaEncalheDTO buscarProdutoComEstoque(String codigoProduto,Long numeroEdicao, TipoVendaEncalhe tipoVendaEncalhe){
		
		VendaEncalheDTO vendaEncalheDTO = new VendaEncalheDTO();
		
		ProdutoEdicao produtoEdicao  =  produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao);
		
		if(produtoEdicao!= null){
				
			Integer qntProduto = getQntProdutoEstoque(tipoVendaEncalhe, produtoEdicao.getId());
			
			if(qntProduto!= null){
			    
				vendaEncalheDTO.setQntDisponivelProduto(qntProduto);
				vendaEncalheDTO.setCodigoProduto(produtoEdicao.getCodigo());
				vendaEncalheDTO.setNomeProduto(produtoEdicao.getProduto().getNome());
				vendaEncalheDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao().intValue());
				vendaEncalheDTO.setPrecoCapa(produtoEdicao.getPrecoVenda().subtract(produtoEdicao.getDesconto()));				
				vendaEncalheDTO.setCodigoBarras(produtoEdicao.getCodigoDeBarras());
				
				vendaEncalheDTO.setFormaVenda( (produtoEdicao.getProduto()!= null 
												&& produtoEdicao.getProduto().getFormaComercializacao()!= null)
						? produtoEdicao.getProduto().getFormaComercializacao().getValue():"");
			}
			else{
				throw new ValidacaoException(TipoMensagem.WARNING,"Não existe produto disponível em estoque para venda de encalhe!");
			}
		}
		
		return vendaEncalheDTO;
	}
	
	private Integer getQntProdutoEstoque(TipoVendaEncalhe tipoVendaEncalhe, Long idProdutoEdicao ){
		
		EstoqueProduto estoqueProduto = estoqueProdutoRespository.buscarEstoquePorProduto(idProdutoEdicao);
		
		Integer qntProduto = null;
		
		if(estoqueProduto!= null ){
			
			if(TipoVendaEncalhe.ENCALHE.equals(tipoVendaEncalhe)
					&& BigDecimal.ZERO.compareTo(estoqueProduto.getQtdeDevolucaoEncalhe()) > 0){
			
				qntProduto = estoqueProduto.getQtdeDevolucaoEncalhe().intValue();
			}
			else if (TipoVendaEncalhe.ENCALHE.equals(tipoVendaEncalhe)
					&& BigDecimal.ZERO.compareTo(estoqueProduto.getQtdeSuplementar()) > 0){
				
				qntProduto = estoqueProduto.getQtdeSuplementar().intValue();
			}
		}
		
		return qntProduto;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<VendaEncalheDTO> buscarVendasProduto(FiltroVendaEncalheDTO filtro) {
		
		/*List<VendaEncalheDTO> lista = new ArrayList<VendaEncalheDTO>();
		
		for(int i=0;i<15;i++){
		
			VendaEncalheDTO vendaEncalheDTO = new VendaEncalheDTO();
			
			vendaEncalheDTO.setIdVenda(new Long(i));
			vendaEncalheDTO.setDataVenda(new Date());
			vendaEncalheDTO.setCodigoProduto("10" + i);
			vendaEncalheDTO.setNomeCota("Jose da Silva" + i);
			vendaEncalheDTO.setNomeProduto("Veja" + i);
			vendaEncalheDTO.setNumeroCota(123);
			vendaEncalheDTO.setNumeroEdicao(123);
			vendaEncalheDTO.setPrecoCapa(BigDecimal.TEN);
			vendaEncalheDTO.setPrecoDesconto(BigDecimal.TEN);
			vendaEncalheDTO.setQntProduto(10);
			vendaEncalheDTO.setTipoVendaEncalhe(TipoVendaEncalhe.ENCALHE);
			vendaEncalheDTO.setValoTotalProduto(BigDecimal.TEN);
			
			lista.add(vendaEncalheDTO);
		}
		return lista;*/
		
		return vendaProdutoRepository.buscarVendasEncalhe(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public Long buscarQntVendasProduto(FiltroVendaEncalheDTO filtro) {
		
		return vendaProdutoRepository.buscarQntVendaEncalhe(filtro);
	}

}
