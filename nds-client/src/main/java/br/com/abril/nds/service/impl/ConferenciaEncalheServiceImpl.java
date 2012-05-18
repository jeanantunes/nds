package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.exception.ChamadaEncalheCotaInexistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheExistenteException;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class ConferenciaEncalheServiceImpl implements ConferenciaEncalheService {
	

	@Autowired
	private BoxRepository boxRepository;
	
	@Autowired
	private ControleConferenciaEncalheCotaRepository controleConferenciaEncalheCotaRepository;
	
	@Autowired 
	private DistribuidorService distribuidorService; 
	
	@Autowired
	private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	@Autowired
	private ConferenciaEncalheRepository conferenciaEncalheRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#obterListaBoxEncalhe()
	 */
	@Transactional(readOnly = true)
	public List<Box> obterListaBoxEncalhe(Long idUsuario) {
	
		List<Box> listaBoxEncalhe = boxRepository.obterListaBox(TipoBox.RECOLHIMENTO);
		
		if (idUsuario == null){
			
			return listaBoxEncalhe;
		}
		
		String codigoBoxPadraoUsuario = this.obterBoxPadraoUsuario(idUsuario);
		
		if (codigoBoxPadraoUsuario == null){
			
			return listaBoxEncalhe;
		}
		
		List<Box> boxes = new ArrayList<Box>();
			
		for (Box box : listaBoxEncalhe){
			
			if (box.getCodigo().equals(codigoBoxPadraoUsuario)){
				
				boxes.add(box);
				listaBoxEncalhe.remove(box);
				break;
			}
		}
		
		boxes.addAll(listaBoxEncalhe);
		
		return boxes;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#obterBoxPadraoUsuario(java.lang.Long)
	 */
	public String obterBoxPadraoUsuario(Long idUsuario) {
		
		/*List<Box> listaBoxEncalhe = boxRepository.obterBoxUsuario(idUsuario, TipoBox.RECOLHIMENTO);
		
		if(listaBoxEncalhe != null && !listaBoxEncalhe.isEmpty()) {
			
			Box boxRecolhimentoUsuario = listaBoxEncalhe.get(0);
			
			return boxRecolhimentoUsuario.getCodigo();
		}*/
		
		if (idUsuario == null){
			
			return null;
		}
		
		return this.boxRepository.obterCodigoBoxPadraoUsuario(idUsuario);
		
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#verificarChamadaEncalheCota(java.lang.Integer)
	 */
	@Transactional(readOnly = true)
	public void verificarChamadaEncalheCota(Integer numeroCota) throws ConferenciaEncalheExistenteException, ChamadaEncalheCotaInexistenteException {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Date dataOperacao = distribuidor.getDataOperacao();
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				controleConferenciaEncalheCotaRepository.obterControleConferenciaEncalheCota(numeroCota, dataOperacao);
		
		if(controleConferenciaEncalheCota != null) {
			throw new ConferenciaEncalheExistenteException();
		}
		
		Date dataRecolhimentoReferencia = obterDataRecolhimentoReferencia();
		
		boolean encalheConferido = false;
		
		boolean indPesquisaCEFutura = true;
		
		Long qtdeRegistroChamadaEncalhe = 
				chamadaEncalheCotaRepository.obterQtdListaChamaEncalheCota(numeroCota, dataRecolhimentoReferencia, null, indPesquisaCEFutura, encalheConferido);
		
		if(qtdeRegistroChamadaEncalhe == 0L) {
			throw new ChamadaEncalheCotaInexistenteException();
		}
		
	}
	
	@Transactional(readOnly = true)
	public Date validarExistenciaChamadaEncalheParaCotaProdutoEdicao(Integer numeroCota, Long idProdutoEdicao) throws ChamadaEncalheCotaInexistenteException {
		
		boolean encalheConferido = false;
		
		boolean indPesquisaCEFutura = true;

		Date dataRecolhimentoReferencia = obterDataRecolhimentoReferencia();
		
		List<ChamadaEncalheCota> listaChamadaEncalheCota = chamadaEncalheCotaRepository.
				obterListaChamaEncalheCota(numeroCota, dataRecolhimentoReferencia, idProdutoEdicao, indPesquisaCEFutura, encalheConferido);
		
		if(listaChamadaEncalheCota == null || listaChamadaEncalheCota.isEmpty()) {
			throw new ChamadaEncalheCotaInexistenteException();
		}
		
		return listaChamadaEncalheCota.get(0).getChamadaEncalhe().getDataRecolhimento();
		
		
	}


	
	public boolean verificarCotaEmiteNFe() {
		return false;
	}

	public void inserirDadosNotaFiscalCota() {
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#obterInfoConferenciaEncalheCota(java.lang.Integer)
	 */
	@Transactional(readOnly = true)
	public InfoConferenciaEncalheCota obterInfoConferenciaEncalheCota(Integer numeroCota) {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Date dataOperacao = distribuidor.getDataOperacao();
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				controleConferenciaEncalheCotaRepository.obterControleConferenciaEncalheCota(numeroCota, dataOperacao);
		
		InfoConferenciaEncalheCota infoConfereciaEncalheCota = new InfoConferenciaEncalheCota();
		
		if(controleConferenciaEncalheCota!=null) {
			
			List<ConferenciaEncalheDTO> listaConferenciaEncalheDTO = 
					conferenciaEncalheRepository.obterListaConferenciaEncalheDTO(
							controleConferenciaEncalheCota.getId(), 
							distribuidor.getId());
			
			infoConfereciaEncalheCota.setListaConferenciaEncalhe(listaConferenciaEncalheDTO);
			
			infoConfereciaEncalheCota.setEncalhe(null);//TODO: calcular este valor no front end...
			
			
		} else {
			
			infoConfereciaEncalheCota.setEncalhe(BigDecimal.ZERO);
			
		}
		
		List<Long> listaIdProdutoEdicao = 
				chamadaEncalheCotaRepository.obterListaIdProdutoEdicaoChamaEncalheCota(numeroCota, dataOperacao, false, false);
		BigDecimal reparte = estoqueProdutoCotaRepository.obterValorTotalReparteCota(numeroCota, listaIdProdutoEdicao, distribuidor.getId());
		
		BigDecimal totalDebitoCreditoCota = null;//TODO sumarizado no front end...
		BigDecimal valorPagar = null;//TODO sumarizado no front end...
		BigDecimal valorVendaDia = null;//TODO sumarizado no front end...
		
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroEnvioEncalhe = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
	
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroRecebimentoReparte = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE);

		List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados = new ArrayList<TipoMovimentoFinanceiro>();
		
		tiposMovimentoFinanceiroIgnorados.add(tipoMovimentoFinanceiroEnvioEncalhe);
		
		tiposMovimentoFinanceiroIgnorados.add(tipoMovimentoFinanceiroRecebimentoReparte);
		
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCota = 
				movimentoFinanceiroCotaRepository.obterDebitoCreditoCotaDataOperacao(
						numeroCota, 
						dataOperacao, 
						tiposMovimentoFinanceiroIgnorados);
		
		Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);
		
		infoConfereciaEncalheCota.setCota(cota);
		infoConfereciaEncalheCota.setListaDebitoCreditoCota(listaDebitoCreditoCota);
		infoConfereciaEncalheCota.setReparte(reparte);
		infoConfereciaEncalheCota.setTotalDebitoCreditoCota(totalDebitoCreditoCota);
		infoConfereciaEncalheCota.setValorPagar(valorPagar);
		infoConfereciaEncalheCota.setValorVendaDia(valorVendaDia);
		
		return infoConfereciaEncalheCota;
		
	}
	
	private void carregarValorDesconto(ProdutoEdicao produtoEdicao, Integer numeroCota) {
		
		BigDecimal hundred = new BigDecimal(100.0);
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		BigDecimal fatorDesconto = produtoEdicaoRepository.obterFatorDesconto(produtoEdicao.getId(), numeroCota, distribuidor.getId());
		
		if( fatorDesconto!=null && produtoEdicao.getPrecoVenda()!=null ) {
			
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
			
			BigDecimal valorDesconto = fatorDesconto.divide(hundred).multiply(precoVenda);
			
			produtoEdicao.setDesconto(valorDesconto);
			
		}
		
		
	}

	/**
	 * Retona a quantidade de dias que a dataOperacao é maior que a dataRecolhimentoDistribuidor mais 1.
	 * Caso a dataOperacao seja menor que a dataRecolhimentoDistribuidor retornará null. 
	 * 
	 * @param dataRecolhimentoDistribuidor
	 * 
	 * @return Integer
	 */
	private Integer obterQtdeDiaAposDataRecolhimentoDistribuidor(Date dataRecolhimentoDistribuidor) {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Date dataOperacao = distribuidor.getDataOperacao();
		
		if(dataOperacao.compareTo(dataRecolhimentoDistribuidor) < 0 ) {
			return null;
		}
		
		Long qtde = DateUtil.obterDiferencaDias(dataRecolhimentoDistribuidor, dataOperacao);
		
		Integer posicaoDia = (qtde.intValue() + 1);
		
		return posicaoDia;
		
	}
	
	@Transactional(readOnly = true)
	public ProdutoEdicaoDTO pesquisarProdutoEdicaoPorId(Integer numeroCota, Long idProdutoEdicao) throws ChamadaEncalheCotaInexistenteException {
		
		if (numeroCota == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório.");
		}
		
		if (idProdutoEdicao == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id Prdoduto Edição é obrigatório.");
		}
		
		ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		ProdutoEdicaoDTO produtoEdicaoDTO = null;
		
		if (produtoEdicao != null) {
		
			Date dataRecolhimentoDistribuidor = this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(numeroCota, idProdutoEdicao);
			
			Integer dia = obterQtdeDiaAposDataRecolhimentoDistribuidor(dataRecolhimentoDistribuidor);
			
			carregarValorDesconto(produtoEdicao, numeroCota);
			
			produtoEdicaoDTO = new ProdutoEdicaoDTO();
			
			produtoEdicaoDTO.setId(produtoEdicao.getId());
			produtoEdicaoDTO.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
			produtoEdicaoDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
			produtoEdicaoDTO.setPrecoVenda(produtoEdicao.getPrecoVenda());
			produtoEdicaoDTO.setDesconto(produtoEdicao.getDesconto());
			produtoEdicaoDTO.setPacotePadrao(produtoEdicao.getPacotePadrao());
			produtoEdicaoDTO.setPeb(produtoEdicao.getPeb());
			produtoEdicaoDTO.setPrecoCusto(produtoEdicao.getPrecoCusto());
			produtoEdicaoDTO.setPeso(produtoEdicao.getPeso());
			produtoEdicaoDTO.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
			produtoEdicaoDTO.setNomeProduto(produtoEdicao.getProduto().getNome());
			produtoEdicaoDTO.setPossuiBrinde(produtoEdicao.isPossuiBrinde());
			produtoEdicaoDTO.setExpectativaVenda(produtoEdicao.getExpectativaVenda());
			produtoEdicaoDTO.setPermiteValeDesconto(produtoEdicao.isPermiteValeDesconto());
			produtoEdicaoDTO.setParcial(produtoEdicao.isParcial());
			produtoEdicaoDTO.setDia(dia);
			produtoEdicaoDTO.setDataRecolhimentoDistribuidor(dataRecolhimentoDistribuidor);
			
			
			Integer sequenciaMatriz = produtoEdicaoRepository.obterCodigoMatrizPorProdutoEdicao(produtoEdicao.getId());
			produtoEdicaoDTO.setSequenciaMatriz(sequenciaMatriz);
			
		}
		
		return produtoEdicaoDTO;
	}
	
	@Transactional(readOnly = true)
	public ProdutoEdicaoDTO pesquisarProdutoEdicaoPorSM(Integer numeroCota, Integer sm) throws ChamadaEncalheCotaInexistenteException {
		
		if (numeroCota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório.");
		}
		
		if (sm == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "SM é obrigatório.");
		}
		
		ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.obterProdutoEdicaoPorSequenciaMatriz(sm);
		
		ProdutoEdicaoDTO produtoEdicaoDTO = null;
		
		if (produtoEdicao != null){
		
			Date dataRecolhimentoDistribuidor = this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(numeroCota, produtoEdicao.getId());

			Integer dia = obterQtdeDiaAposDataRecolhimentoDistribuidor(dataRecolhimentoDistribuidor);
			
			carregarValorDesconto(produtoEdicao, numeroCota);
			
			produtoEdicaoDTO = new ProdutoEdicaoDTO();
			
			produtoEdicaoDTO.setId(produtoEdicao.getId());
			produtoEdicaoDTO.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
			produtoEdicaoDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
			produtoEdicaoDTO.setPrecoVenda(produtoEdicao.getPrecoVenda());
			produtoEdicaoDTO.setDesconto(produtoEdicao.getDesconto());
			produtoEdicaoDTO.setPacotePadrao(produtoEdicao.getPacotePadrao());
			produtoEdicaoDTO.setPeb(produtoEdicao.getPeb());
			produtoEdicaoDTO.setPrecoCusto(produtoEdicao.getPrecoCusto());
			produtoEdicaoDTO.setPeso(produtoEdicao.getPeso());
			produtoEdicaoDTO.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
			produtoEdicaoDTO.setNomeProduto(produtoEdicao.getProduto().getNome());
			produtoEdicaoDTO.setPossuiBrinde(produtoEdicao.isPossuiBrinde());
			produtoEdicaoDTO.setExpectativaVenda(produtoEdicao.getExpectativaVenda());
			produtoEdicaoDTO.setPermiteValeDesconto(produtoEdicao.isPermiteValeDesconto());
			produtoEdicaoDTO.setParcial(produtoEdicao.isParcial());
			produtoEdicaoDTO.setDia(dia);
			produtoEdicaoDTO.setDataRecolhimentoDistribuidor(dataRecolhimentoDistribuidor);
			
			Integer sequenciaMatriz = produtoEdicaoRepository.obterCodigoMatrizPorProdutoEdicao(produtoEdicao.getId());
			produtoEdicaoDTO.setSequenciaMatriz(sequenciaMatriz);

			
		}
		
		return produtoEdicaoDTO;
	}	
	
	@Transactional(readOnly = true)
	public ProdutoEdicaoDTO pesquisarProdutoEdicaoPorCodigoDeBarras(Integer numeroCota, String codigoDeBarras) throws ChamadaEncalheCotaInexistenteException {
		
		if (numeroCota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório.");
		}
		
		if (codigoDeBarras == null || codigoDeBarras.trim().isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Código de Barras é obrigatório.");
		}
		
		ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.obterProdutoEdicaoPorCodigoBarra(codigoDeBarras);
		
		ProdutoEdicaoDTO produtoEdicaoDTO = null;
		
		if (produtoEdicao != null){
		
			Date dataRecolhimentoDistribuidor = this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(numeroCota, produtoEdicao.getId());

			Integer dia = obterQtdeDiaAposDataRecolhimentoDistribuidor(dataRecolhimentoDistribuidor);
			
			carregarValorDesconto(produtoEdicao, numeroCota);
			
			produtoEdicaoDTO = new ProdutoEdicaoDTO();
			
			produtoEdicaoDTO.setId(produtoEdicao.getId());
			produtoEdicaoDTO.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
			produtoEdicaoDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
			produtoEdicaoDTO.setPrecoVenda(produtoEdicao.getPrecoVenda());
			produtoEdicaoDTO.setDesconto(produtoEdicao.getDesconto());
			produtoEdicaoDTO.setPacotePadrao(produtoEdicao.getPacotePadrao());
			produtoEdicaoDTO.setPeb(produtoEdicao.getPeb());
			produtoEdicaoDTO.setPrecoCusto(produtoEdicao.getPrecoCusto());
			produtoEdicaoDTO.setPeso(produtoEdicao.getPeso());
			produtoEdicaoDTO.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
			produtoEdicaoDTO.setNomeProduto(produtoEdicao.getProduto().getNome());
			produtoEdicaoDTO.setPossuiBrinde(produtoEdicao.isPossuiBrinde());
			produtoEdicaoDTO.setExpectativaVenda(produtoEdicao.getExpectativaVenda());
			produtoEdicaoDTO.setPermiteValeDesconto(produtoEdicao.isPermiteValeDesconto());
			produtoEdicaoDTO.setParcial(produtoEdicao.isParcial());
			produtoEdicaoDTO.setDia(dia);
			produtoEdicaoDTO.setDataRecolhimentoDistribuidor(dataRecolhimentoDistribuidor);
			
			Integer sequenciaMatriz = produtoEdicaoRepository.obterCodigoMatrizPorProdutoEdicao(produtoEdicao.getId());
			produtoEdicaoDTO.setSequenciaMatriz(sequenciaMatriz);
			
		}
		
		return produtoEdicaoDTO;
	}
	
	
	
	/*
	 * Traz uma lista de codigoProduto - nomeProduto -  numeroEdicao
	 */
	public Object obterListaDadosProdutoEdicao(String codigoOuNome) {
		//TODO
		return null;
	}
	
	/**
	 * Retorna a dataRecolhimento referencia sendo esta igual a 
	 * dataOperacao - qtdDiasEncalheAtrasadoAceitavel(parâmetro do distribuidor). 
	 * 
	 * @return dataRecolhimentoReferencia.
	 */
	private Date obterDataRecolhimentoReferencia() {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Date dataOperacao = distribuidor.getDataOperacao();
		
		int qtdDiasEncalheAtrasadoAceitavel = distribuidor.getQtdDiasEncalheAtrasadoAceitavel();
		
		Date dataRecolhimentoReferencia = DateUtil.subtrairDias(dataOperacao, qtdDiasEncalheAtrasadoAceitavel);
		
		return dataRecolhimentoReferencia;
		
	}
	
	
	public void salvarDadosConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario) {
		
	    Date dataRecolhimentoReferencia = obterDataRecolhimentoReferencia();
		
		Integer numeroCota = controleConfEncalheCota.getCota().getNumeroCota();
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				obterControleConferenciaEncalheCotaParaConfEncalhe(controleConfEncalheCota.getId());
		
		Date dataCriacao = new Date();
		
		TipoMovimentoEstoque tipoMovimentoEstoqueEnvioEncalhe = tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_ENCALHE);
		
		//TODO - Quando utilizar estoque complementar?
		TipoMovimentoEstoque tipoMovimentoEstoqueSuplementar = null;//TODO: tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.);
		
		for(ConferenciaEncalheDTO conferenciaEncalheDTO : listaConferenciaEncalhe) {
			
			if(conferenciaEncalheDTO.getIdConferenciaEncalhe()!=null) {
				
				atualizarRegistroConferenciaEncalhe(conferenciaEncalheDTO);
				
			} else {
				
				criarNovoRegistroConferenciaEncalhe(
						controleConferenciaEncalheCota, 
						conferenciaEncalheDTO, 
						numeroCota, 
						dataRecolhimentoReferencia, 
						dataCriacao, 
						tipoMovimentoEstoqueEnvioEncalhe, 
						usuario);
				
			}
			
		}
		
	}
	
	/**
	 * Atualiza um registro existente de ConferenciaEncalhe.
	 * 
	 * @param conferenciaEncalheDTO
	 */
	private void atualizarRegistroConferenciaEncalhe(ConferenciaEncalheDTO conferenciaEncalheDTO){
		
		ConferenciaEncalhe conferenciaEncalheFromDB = conferenciaEncalheRepository.buscarPorId(conferenciaEncalheDTO.getIdConferenciaEncalhe());
		conferenciaEncalheFromDB.setObservacao(conferenciaEncalheDTO.getObservacao());
		conferenciaEncalheFromDB.setJuramentada(conferenciaEncalheDTO.isJuramentada());
		
		atualizarMovimentoEstoqueCota(conferenciaEncalheFromDB.getMovimentoEstoqueCota(), conferenciaEncalheDTO.getQtdExemplar());

		
	}
	
	/**
	 * Cria um novo registro de ConferenciaEncalhe.
	 * 
	 * @param controleConferenciaEncalheCota
	 * @param conferenciaEncalheDTO
	 * @param numeroCota
	 * @param dataRecolhimentoReferencia
	 * @param dataCriacao
	 * @param tipoMovimentoEstoque
	 * @param usuario
	 */
	private void criarNovoRegistroConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConferenciaEncalheCota,
			ConferenciaEncalheDTO conferenciaEncalheDTO,
			Integer numeroCota, 
			Date dataRecolhimentoReferencia,
			Date dataCriacao,
			TipoMovimentoEstoque tipoMovimentoEstoque,
			Usuario usuario) {
	
		ChamadaEncalheCota chamadaEncalheCota = 
				obterChamadaEncalheCotaParaConfEncalhe(
						numeroCota, 
						dataRecolhimentoReferencia, 
						conferenciaEncalheDTO.getIdProdutoEdicao());
		
		Cota cota = chamadaEncalheCota.getCota();
		Lancamento lancamento = obterLancamentoParaConfEncalhe(chamadaEncalheCota.getChamadaEncalhe().getDataRecolhimento(), conferenciaEncalheDTO.getIdProdutoEdicao());
		ProdutoEdicao produtoEdicao = lancamento.getProdutoEdicao();
		
		MovimentoEstoqueCota movimentoEstoqueCota = obterMovimentoEstoqueCotaParaConfEncalhe(
				cota, 
				produtoEdicao, 
				dataCriacao, 
				tipoMovimentoEstoque,
				conferenciaEncalheDTO.getQtdExemplar(),
				usuario);
		
		ConferenciaEncalhe conferenciaEncalhe = new ConferenciaEncalhe();
		conferenciaEncalhe.setChamadaEncalheCota(chamadaEncalheCota);
		conferenciaEncalhe.setControleConferenciaEncalheCota(controleConferenciaEncalheCota);
		conferenciaEncalhe.setLancamento(lancamento);
		conferenciaEncalhe.setMovimentoEstoqueCota(movimentoEstoqueCota);
		conferenciaEncalhe.setJuramentada(conferenciaEncalheDTO.isJuramentada());
		conferenciaEncalhe.setObservacao(conferenciaEncalheDTO.getObservacao());
		
	}
	
	
	/**
	 * Obtém o lancamento referente a dataRecolhimento e idProdutoEdicao
	 * 
	 * @param dataRecolhimento
	 * @param idProdutoEdicao
	 * 
	 * @return Lancamento
	 */
	private Lancamento obterLancamentoParaConfEncalhe(Date dataRecolhimento, Long idProdutoEdicao) {
		
		Lancamento lancamento =  lancamentoRepository.obterLancamentoPorDataRecolhimentoProdutoEdicao(dataRecolhimento, idProdutoEdicao);
		
		StringBuffer errorMsg = new StringBuffer();
		
		if(lancamento==null) {

			errorMsg.append(" Nenhum registro de Lancamento encontrado para o produto edição com id:  ");
			errorMsg.append(idProdutoEdicao);
			errorMsg.append(" com data de recolhimento: ");
			errorMsg.append(dataRecolhimento.toString());				
			
			throw new IllegalStateException(errorMsg.toString());
			
		}
		
		return lancamento;
	}
	
	/**
	 * Obtém a ChamadaEncalheCota de acordo com a cota e idProdutoEdicao 
	 * cuja dataRecolhimento da chamada de encalhe seja maior ou igual ao 
	 * parâmetro dataRecolhimentoReferencia, sendo que este parâmetro é igual
	 * a dataOperação - qtdDiasEncalheAtrasadoAceitavel(parâmetro do distribuidor).  
	 * 
	 * @param numeroCota
	 * @param dataRecolhimentoReferencia
	 * @param idProdutoEdicao
	 * 
	 * @return ChamadaEncalheCota
	 */
	private ChamadaEncalheCota obterChamadaEncalheCotaParaConfEncalhe(
			Integer numeroCota,
			Date dataRecolhimentoReferencia,
			Long idProdutoEdicao) {
		
		boolean encalheConferido = false;
		boolean indPesquisaCEFutura = true;
		
		List<ChamadaEncalheCota> listaChamadaEncalheCota = 
				chamadaEncalheCotaRepository.obterListaChamaEncalheCota(numeroCota, dataRecolhimentoReferencia, idProdutoEdicao, indPesquisaCEFutura, encalheConferido);

		StringBuffer errorMsg = new StringBuffer();

		if(listaChamadaEncalheCota==null || listaChamadaEncalheCota.isEmpty()) {

			errorMsg.append(" Nenhum registro de chamada de encalhe para cota de nº:  ");
			errorMsg.append(numeroCota);
			errorMsg.append(" para o produto edição id: ");
			errorMsg.append(idProdutoEdicao);				
			
			throw new IllegalStateException(errorMsg.toString());
			
		}
		
		if(listaChamadaEncalheCota.size()>1) {

			errorMsg.append(" Mais de um registro de chamada de encalhe para cota de nº:  ");
			errorMsg.append(numeroCota);
			errorMsg.append(" para o produto edição id: ");
			errorMsg.append(idProdutoEdicao);				
			
			throw new IllegalStateException(errorMsg.toString());
			
		}
		
		
		return listaChamadaEncalheCota.get(0);
		
		
	}
	
	private ControleConferenciaEncalheCota obterControleConferenciaEncalheCotaParaConfEncalhe(Long idControleConfEncalheCota) {
		
		return null;
	}
	
	/**
	 * Atualiza o registro de MovimentoEstoqueCota assim como o 
	 * EstoqueProdutoCota relativo ao mesmo.
	 * 
	 * @param movimentoEstoqueCota
	 * @param qtdeExemplar
	 */
	private void atualizarMovimentoEstoqueCota(MovimentoEstoqueCota movimentoEstoqueCota, BigDecimal qtdeExemplar) {
		
		movimentoEstoqueCota.setQtde(qtdeExemplar);
		
		movimentoEstoqueCotaRepository.alterar(movimentoEstoqueCota);
		
		EstoqueProdutoCota estoqueProdutoCota =  movimentoEstoqueCota.getEstoqueProdutoCota();
		
		BigDecimal qtdDevolvida = 
				estoqueProdutoCota.getQtdeDevolvida() != null ? 
					estoqueProdutoCota.getQtdeDevolvida() : BigDecimal.ZERO;
		
		estoqueProdutoCota.setQtdeDevolvida(qtdDevolvida.add(qtdeExemplar));
		
		estoqueProdutoCotaRepository.alterar(estoqueProdutoCota);
		
	}
	
	private MovimentoEstoqueCota obterMovimentoEstoqueCotaParaConfEncalhe(
			Cota cota, 
			ProdutoEdicao produtoEdicao, 
			Date dataCriacao, 
			TipoMovimentoEstoque tipoMovimentoEstoque,
			BigDecimal qtdeExemplar,
			Usuario usuario) {
		
		EstoqueProdutoCota estoqueProdutoCota = estoqueProdutoCotaRepository.buscarEstoquePorProdutEdicaoECota(
				produtoEdicao.getId(), cota.getId());
		
		if(estoqueProdutoCota == null) {
			throw new IllegalStateException("Não há registro de EstoqueProdutoCota para o produtoEdicao de id:" + produtoEdicao.getId());
		}
		
		BigDecimal qtdDevolvida = 
				estoqueProdutoCota.getQtdeDevolvida() != null ? 
					estoqueProdutoCota.getQtdeDevolvida() : BigDecimal.ZERO;
		
		estoqueProdutoCota.setQtdeDevolvida(qtdDevolvida.add(qtdeExemplar));
		
		estoqueProdutoCotaRepository.alterar(estoqueProdutoCota);
		
		MovimentoEstoqueCota movimentoEstoqueCota = new MovimentoEstoqueCota();
		
		movimentoEstoqueCota.setAprovadoAutomaticamente(true);
		movimentoEstoqueCota.setDataAprovacao(dataCriacao);
		movimentoEstoqueCota.setAprovador(null);
		movimentoEstoqueCota.setStatus(StatusAprovacao.APROVADO);
		
		movimentoEstoqueCota.setCota(cota);
		movimentoEstoqueCota.setData(dataCriacao);
		movimentoEstoqueCota.setDataCriacao(dataCriacao);
		
		movimentoEstoqueCota.setEstoqueProdutoCota(estoqueProdutoCota);
		
		movimentoEstoqueCota.setEstudoCota(null);
		movimentoEstoqueCota.setMotivo(null);
		movimentoEstoqueCota.setProdutoEdicao(produtoEdicao);
		movimentoEstoqueCota.setQtde(qtdeExemplar);
		movimentoEstoqueCota.setTipoMovimento(tipoMovimentoEstoque);
		movimentoEstoqueCota.setUsuario(usuario);
		
		movimentoEstoqueCotaRepository.adicionar(movimentoEstoqueCota);
		
		return movimentoEstoqueCota;
		
	}
	
	
	
	// (caso valor nota esteja diferente do encalhe requisitar correcao)
	public void finalizarConferenciaEncalhe(List<ConferenciaEncalheDTO> listaConferenciaEncalhe) {
		
	}
	
	private void gerarDiferencas() {
		//TODO
	}
	
	private void gerarDivida() {
		
	}
	
	private void gerarDividaChamadaEncalheAntecipada() {
		
	}
	
   /*
	* Apos finalizar conferencia de encalhe sera verificado        
	* quais documentos serao gerados e se os mesmos serao impressos
	* ou enviados por email.                                       
	*/
	private void gerarDocumentosConferenciaEncalhe() {
		//TODO
	}
	
	private void gerarSlip() {
		//TODO
	}
	
	private void gerarBoleto() {
		//TODO
	}
	
	private void gerarRecibo() {
		//TODO
	}
	
}
