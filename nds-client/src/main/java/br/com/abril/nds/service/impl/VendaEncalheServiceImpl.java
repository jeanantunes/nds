package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.model.estoque.VendaProduto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.repository.VendaProdutoEncalheRepository;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.VendaEncalheService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
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
	private VendaProdutoEncalheRepository vendaProdutoRepository;
	
	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRespository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/**
	 * Obtém dados da venda encalhe por id
	 * @param idCota
	 * @param dataInicio
	 * @param dataFim
	 * @return List<SlipVendaEncalheDTO>
	 */
	private List<SlipVendaEncalheDTO> obtemDadosSlip(List<VendaEncalheDTO> listaVendas,Long numeroCota){

		SlipVendaEncalheDTO slipVendaEncalhe = null;
		List<SlipVendaEncalheDTO> listaSlipVendaEncalhe = new ArrayList<SlipVendaEncalheDTO>();

		List<VendaProduto> listaVe = getVendasProduto(listaVendas, numeroCota);

		if (listaVe!=null && listaVe.size()>0) {
			
			Integer quantidadeTotalVista = 0;
			BigDecimal valorTotalVista = BigDecimal.ZERO;
			Integer quantidadeTotalPrazo = 0;
			BigDecimal valorTotalPrazo = BigDecimal.ZERO;
			
			for (VendaProduto itemVE:listaVe){
			
				slipVendaEncalhe = new SlipVendaEncalheDTO();
				
				slipVendaEncalhe.setNomeCota(itemVE.getCota().getPessoa().getNome());
				slipVendaEncalhe.setNumeroCota(itemVE.getCota().getNumeroCota().toString());
				slipVendaEncalhe.setNumeroBox(itemVE.getCota().getBox().getCodigo());
				slipVendaEncalhe.setDescricaoBox(itemVE.getCota().getBox().getTipoBox().name());
				slipVendaEncalhe.setData( DateUtil.formatarDataPTBR(itemVE.getDataVenda()));
				slipVendaEncalhe.setHora(DateUtil.formatarData(itemVE.getHorarioVenda(),"HH:mm"));
				slipVendaEncalhe.setUsuario(itemVE.getUsuario().getNome());
				
				slipVendaEncalhe.setCodigo(itemVE.getProdutoEdicao().getProduto().getCodigo());
				slipVendaEncalhe.setProduto(itemVE.getProdutoEdicao().getProduto().getNome());
				slipVendaEncalhe.setEdicao(itemVE.getProdutoEdicao().getNumeroEdicao().toString());
				slipVendaEncalhe.setQuantidade(itemVE.getQntProduto().toString());
				slipVendaEncalhe.setPreco( CurrencyUtil.formatarValor(itemVE.getProdutoEdicao().getPrecoVenda().subtract(itemVE.getProdutoEdicao().getDesconto())));
				slipVendaEncalhe.setTotal(CurrencyUtil.formatarValor(itemVE.getValorTotalVenda()));
				
				quantidadeTotalVista += itemVE.getQntProduto().intValue();
				valorTotalVista = valorTotalVista.add(itemVE.getValorTotalVenda());
				
				/*quantidadeTotalPrazo += 15;
				valorTotalPrazo += 300;*/
		
				slipVendaEncalhe.setQuantidadeTotalVista(quantidadeTotalVista.toString());
				slipVendaEncalhe.setValorTotalVista(valorTotalVista.toString());
				
				slipVendaEncalhe.setQuantidadeTotalPrazo(quantidadeTotalPrazo.toString());
				slipVendaEncalhe.setValorTotalPrazo(valorTotalPrazo.toString());
				
				slipVendaEncalhe.setQuantidadeTotalGeral((quantidadeTotalVista + quantidadeTotalPrazo)+"");
				slipVendaEncalhe.setValorTotalGeral(CurrencyUtil.formatarValor(valorTotalVista.add(valorTotalPrazo)));
	
				listaSlipVendaEncalhe.add(slipVendaEncalhe);	
			}
		}
		
		return listaSlipVendaEncalhe;
	}

	private List<VendaProduto> getVendasProduto(List<VendaEncalheDTO> vendas, Long numeroCota){
		
		List<Long> idNumeroEdicao= new ArrayList<Long>();
		ProdutoEdicao produtoEdicao  = null;
		
		for(VendaEncalheDTO vnd : vendas){
			produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(vnd.getCodigoProduto(), vnd.getNumeroEdicao());
			idNumeroEdicao.add(produtoEdicao.getId());
		}
		
		return vendaProdutoRepository.buscarVendaProdutoEncalhe(numeroCota,new Date(), idNumeroEdicao.toArray(new Long[]{}));
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
	
	@Transactional(readOnly = true)
	public byte[] geraImpressaoVendaEncalhe(List<VendaEncalheDTO> listaVendas,Long numeroCota){
		
		byte[] relatorio=null;

		List<SlipVendaEncalheDTO> listaSlipVendaEncalheDTO = this.obtemDadosSlip(listaVendas,numeroCota); 

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
		
		return vendaEncalheDTO;
	}

	@Override
	@Transactional
	public void efetivarVendaEncalhe(List<VendaEncalheDTO> vendaEncalheDTO, Long numeroCota, Date dataVencimentoDebito, Usuario usuario) {
				
		for(VendaEncalheDTO vnd : vendaEncalheDTO){
			criarVendaEncalhe(vnd, numeroCota, dataVencimentoDebito, usuario);
		}
	}
	
	private void criarVendaEncalhe(VendaEncalheDTO vnd,Long numeroCota, Date dataVencimentoDebito, Usuario usuario){
		
		//FIXME venda de encalhe, falta gerar os movimentos financeiros
		
		TipoMovimentoEstoque tipoMovimento = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.VENDA_ENCALHE);
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(vnd.getCodigoProduto(), vnd.getNumeroEdicao());
			
		MovimentoEstoque movimentoEstoque = movimentoEstoqueService.gerarMovimentoEstoque(produtoEdicao.getId(), usuario.getId(), 
																			 new BigDecimal(vnd.getQntProduto()),tipoMovimento);
			
		VendaProduto vendaProduto = getVendaProduto(vnd,numeroCota, usuario,dataVencimentoDebito,produtoEdicao);
		vendaProduto.setMovimentoEstoque(new HashSet<MovimentoEstoque>());
		vendaProduto.getMovimentoEstoque().add(movimentoEstoque);
			
		vendaProdutoRepository.adicionar(vendaProduto);

	}
	
	private VendaProduto getVendaProduto(VendaEncalheDTO vendaDTO, Long numeroCota, Usuario usuario, Date dataVencimentoDebito,ProdutoEdicao produtoEdicao){
		
		VendaProduto venda = new VendaProduto();
		
		venda.setCota(cotaRepository.obterPorNumerDaCota(numeroCota.intValue()));
		
		venda.setProdutoEdicao(produtoEdicao);
		venda.setUsuario(getUsuarioSincronizado(usuario.getId()));
		venda.setDataVencimentoDebito(dataVencimentoDebito);
		venda.setDataVenda(new Date());
		venda.setHorarioVenda(new Date());
		venda.setQntProduto(vendaDTO.getQntProduto());
		venda.setTipoVenda(vendaDTO.getTipoVendaEncalhe());
		venda.setValorTotalVenda( produtoEdicao.getPrecoVenda().subtract(produtoEdicao.getDesconto()).multiply(new BigDecimal(vendaDTO.getQntProduto())));
		
		return venda;
	}

	@Override
	@Transactional
	public void excluirVendaEncalhe(Long idVendaEncalhe) {
		
		//FIXME venda de encalhe - falta fazer as validações nos movimentos financeiros 
		
		VendaProduto vendaProduto  =  vendaProdutoRepository.buscarPorId(idVendaEncalhe);
		
		MovimentoEstoque movimento = processarAtualizcaoMovimentoEstoque(new BigDecimal(vendaProduto.getQntProduto()),
																		BigDecimal.ZERO, vendaProduto.getProdutoEdicao().getId(),
																		vendaProduto.getUsuario().getId());
		vendaProduto.getMovimentoEstoque().add(movimento);
		
		vendaProdutoRepository.remover(vendaProduto);
	}

	@Override
	@Transactional
	public void alterarVendaEncalhe(VendaEncalheDTO vendaEncalheDTO, Date dataVencimentoDebito, Usuario usuario ) {
		//FIXME venda de encalhe - falta fazer as validaçoes financeiras, e suas alterações
		
		VendaProduto vendaProduto  =  vendaProdutoRepository.buscarPorId(vendaEncalheDTO.getIdVenda());
		
		BigDecimal qntAtualProduto = new BigDecimal(vendaProduto.getQntProduto());
		BigDecimal qntNovaProduto = new BigDecimal(vendaEncalheDTO.getQntProduto());
		
		if(qntAtualProduto.compareTo(qntNovaProduto) != 0){
			
			MovimentoEstoque movimento =  processarAtualizcaoMovimentoEstoque(qntAtualProduto,qntNovaProduto,vendaProduto.getProdutoEdicao().getId(),usuario.getId());
		    vendaProduto.getMovimentoEstoque().add(movimento);
		}

		ProdutoEdicao produtoEdicao = vendaProduto.getProdutoEdicao();
		
		vendaProduto.setQntProduto(vendaEncalheDTO.getQntProduto());
		vendaProduto.setValorTotalVenda( produtoEdicao.getPrecoVenda().subtract(produtoEdicao.getDesconto()).multiply(new BigDecimal(vendaEncalheDTO.getQntProduto())));
		vendaProduto.setDataVencimentoDebito(dataVencimentoDebito);
		vendaProduto.setDataVenda(new Date());
		vendaProduto.setHorarioVenda(new Date());
		vendaProduto.setUsuario(getUsuarioSincronizado(usuario.getId()));
		
		vendaProdutoRepository.merge(vendaProduto);
	}
	
	private Usuario getUsuarioSincronizado(Long idUsuario){
		
		return usuarioRepository.buscarPorId(idUsuario);
	}
	
	private MovimentoEstoque processarAtualizcaoMovimentoEstoque(BigDecimal qntProdutoAtual,BigDecimal qntProdutoNovo,Long idProdutoEdicao, Long idUsuario){
		
		BigDecimal quantidadeProdutoAlterada = BigDecimal.ZERO;
		TipoMovimentoEstoque tipoMovimento = null;
		//Se a quantidade de produto nova informada for maior que a quantidade atual, gera movimento de venda de encalhe
		
		if(qntProdutoNovo.compareTo(qntProdutoAtual)>0){
			
			quantidadeProdutoAlterada = qntProdutoNovo.subtract(qntProdutoAtual);
			
			tipoMovimento = tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.VENDA_ENCALHE);
		}
		
		//Se a quantidade de produto nova informada for menor que a quantidade atual, gera movimento de estorno de venda de encalhe
		else if (qntProdutoNovo.compareTo(qntProdutoAtual)<0){
			
			quantidadeProdutoAlterada = qntProdutoAtual.subtract(qntProdutoNovo);
			
			tipoMovimento = tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE);
		}
		
		MovimentoEstoque movimentoEstoque = movimentoEstoqueService.gerarMovimentoEstoque(idProdutoEdicao,idUsuario,quantidadeProdutoAlterada,tipoMovimento); 
		
		return movimentoEstoque;
	}

	@Override
	public byte[] gerarSlipVendaEncalhe(Long idVendaEncalhe) {
		//FIXME venda de encalhe
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
	public byte[] geraImpressaoVendaSuplementar(List<VendaEncalheDTO> listaVendas,Long numeroCota){
		
		byte[] relatorio=null;

		List<SlipVendaEncalheDTO> listaSlipVendaEncalheDTO = this.obtemDadosSlip(listaVendas,numeroCota); 

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
				vendaEncalheDTO.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
				vendaEncalheDTO.setNomeProduto(produtoEdicao.getProduto().getNome());
				vendaEncalheDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
				vendaEncalheDTO.setPrecoCapa(produtoEdicao.getPrecoVenda().subtract(produtoEdicao.getDesconto()));				
				vendaEncalheDTO.setCodigoBarras(produtoEdicao.getCodigoDeBarras());
				
				vendaEncalheDTO.setFormaVenda( (produtoEdicao.getProduto()!= null 
												&& produtoEdicao.getProduto().getFormaComercializacao()!= null)
						? produtoEdicao.getProduto().getFormaComercializacao():null);
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
					&& estoqueProduto.getQtdeDevolucaoEncalhe()!= null
					&& estoqueProduto.getQtdeDevolucaoEncalhe().compareTo(BigDecimal.ZERO) > 0){
			
				qntProduto = estoqueProduto.getQtdeDevolucaoEncalhe().intValue();
			}
			else if (TipoVendaEncalhe.SUPLEMENTAR.equals(tipoVendaEncalhe)
					&& estoqueProduto.getQtdeSuplementar()!= null
					&& estoqueProduto.getQtdeSuplementar().compareTo(BigDecimal.ZERO) > 0){
				
				qntProduto = estoqueProduto.getQtdeSuplementar().intValue();
			}
		}
		
		return qntProduto;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<VendaEncalheDTO> buscarVendasProduto(FiltroVendaEncalheDTO filtro) {
		
		return vendaProdutoRepository.buscarVendasEncalhe(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public Long buscarQntVendasProduto(FiltroVendaEncalheDTO filtro) {
		
		return vendaProdutoRepository.buscarQntVendaEncalhe(filtro);
	}

}
