package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.NotaFiscalEntradaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.service.exception.ChamadaEncalheCotaInexistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheExistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheFinalizadaException;
import br.com.abril.nds.service.exception.EncalheExcedeReparteException;
import br.com.abril.nds.service.exception.EncalheSemPermissaoSalvarException;
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
	
	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired
	private PoliticaCobrancaService politicaCobrancaService;
	
	@Autowired
	private GerarCobrancaService gerarCobrancaService;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Autowired
	private NotaFiscalEntradaRepository notaFiscalEntradaRepository;
	
	@Autowired
	private ItemNotaFiscalEntradaRepository itemNotaFiscalEntradaRepository;
	
	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRepository;
	
//	@Autowired
//	private EstoqueProdutoRespository estoqueProdutoRepository;
	
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
	private String obterBoxPadraoUsuario(Long idUsuario) {
		
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
		
		if(controleConferenciaEncalheCota != null && StatusOperacao.CONCLUIDO.equals(controleConferenciaEncalheCota.getStatus())) {
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
	
	
	/**
	 * Valida a existência de chamada de encalhe de acordo com a
	 * cota e produtoEdicao cuja dataRecolhimento esteja dentro da 
	 * faixa aceitavel (de acordo com  parâmetro do Distribuidor e dataOperacao atual).
	 * 
	 * Se encontrada, será retornada esta chamadaEncalhe para o produtoEdicao em questão.
	 * 
	 * @param numeroCota
	 * @param produtoEdicao
	 * 
	 * @return ChamadaEncalhe
	 * 
	 * @throws ChamadaEncalheCotaInexistenteException
	 */
	private ChamadaEncalhe validarExistenciaChamadaEncalheParaCotaProdutoEdicao(Integer numeroCota, ProdutoEdicao produtoEdicao) throws ChamadaEncalheCotaInexistenteException {
		
		if(produtoEdicao.isParcial()) {

			boolean encalheConferido = false;
			
			boolean indPesquisaCEFutura = false;
			
			Distribuidor distribuidor = distribuidorService.obter();
			
			Date dataOperacao = distribuidor.getDataOperacao();
			
			List<ChamadaEncalheCota> listaChamadaEncalheCota = chamadaEncalheCotaRepository.
					obterListaChamaEncalheCota(numeroCota, dataOperacao, produtoEdicao.getId(), indPesquisaCEFutura, encalheConferido);

			if(listaChamadaEncalheCota == null || listaChamadaEncalheCota.isEmpty()) {
				throw new ChamadaEncalheCotaInexistenteException();
			}
			
			return listaChamadaEncalheCota.get(0).getChamadaEncalhe();
			
		} else {

			boolean encalheConferido = false;
			
			boolean indPesquisaCEFutura = true;
			
			Date dataRecolhimentoReferencia = obterDataRecolhimentoReferencia();
			
			List<ChamadaEncalheCota> listaChamadaEncalheCota = chamadaEncalheCotaRepository.
					obterListaChamaEncalheCota(numeroCota, dataRecolhimentoReferencia, produtoEdicao.getId(), indPesquisaCEFutura, encalheConferido);
			
			if(listaChamadaEncalheCota == null || listaChamadaEncalheCota.isEmpty()) {
				throw new ChamadaEncalheCotaInexistenteException();
			}
			
			return listaChamadaEncalheCota.get(0).getChamadaEncalhe();
			
		}
		
	}
	
	public boolean verificarCotaEmiteNFe() {
		return false;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#obterInfoConferenciaEncalheCota(java.lang.Integer)
	 */
	@Transactional(readOnly = true)
	public InfoConferenciaEncalheCota obterInfoConferenciaEncalheCota(Integer numeroCota) {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		boolean aceitaJuramentado = distribuidor.isAceitaJuramentado();
		
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
			
			infoConfereciaEncalheCota.setEncalhe(null);
			
			
		} else {
			
			infoConfereciaEncalheCota.setEncalhe(BigDecimal.ZERO);
			
		}
		
		List<Long> listaIdProdutoEdicao = 
				chamadaEncalheCotaRepository.obterListaIdProdutoEdicaoChamaEncalheCota(numeroCota, dataOperacao, false, false);
		BigDecimal reparte = estoqueProdutoCotaRepository.obterValorTotalReparteCota(numeroCota, listaIdProdutoEdicao, distribuidor.getId());
		
		BigDecimal totalDebitoCreditoCota = null;
		BigDecimal valorPagar = null;
		BigDecimal valorVendaDia = null;
		
		
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
		infoConfereciaEncalheCota.setDistribuidorAceitaJuramentado(aceitaJuramentado);
		
		return infoConfereciaEncalheCota;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#obterDetalheConferenciaEncalhe(java.lang.Integer, java.lang.Long, java.lang.Long)
	 */
	@Transactional
	public ConferenciaEncalheDTO obterDetalheConferenciaEncalhe(Integer numeroCota, Long idConferenciaEncalhe, Long idProdutoEdicao) {
		
		ConferenciaEncalheDTO conferenciaEncalheDTO = new ConferenciaEncalheDTO();
		
		if(idConferenciaEncalhe!=null) {
		
			ConferenciaEncalhe conferenciaEncalhe = conferenciaEncalheRepository.buscarPorId(idConferenciaEncalhe);
			
			if(conferenciaEncalhe == null) {
				
				throw new IllegalStateException("Conferência de encalhe não encontrada");
				
			}
			
			conferenciaEncalheDTO.setIdConferenciaEncalhe(conferenciaEncalhe.getId());
			
			conferenciaEncalheDTO.setObservacao(conferenciaEncalhe.getObservacao());
			
		}
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		carregarValorDesconto(produtoEdicao, numeroCota);
		
		conferenciaEncalheDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
		
		conferenciaEncalheDTO.setPrecoCapa(produtoEdicao.getPrecoVenda());
		
		conferenciaEncalheDTO.setDesconto(produtoEdicao.getDesconto());
		
		conferenciaEncalheDTO.setPacotePadrao(produtoEdicao.getPacotePadrao());
		
		conferenciaEncalheDTO.setNomeProduto(produtoEdicao.getProduto().getNome());
		
		conferenciaEncalheDTO.setPossuiBrinde(produtoEdicao.isPossuiBrinde());
		
		conferenciaEncalheDTO.setNomeEditor(produtoEdicao.getProduto().getEditor().getNome());
		
		conferenciaEncalheDTO.setNomeEditor(produtoEdicao.getProduto().getFornecedor().getJuridica().getNome());
		
		
		
		return conferenciaEncalheDTO;
		
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
			
			ChamadaEncalhe chamadaEncalhe = this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(numeroCota, produtoEdicao);
			
			Integer dia = obterQtdeDiaAposDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
			
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
			produtoEdicaoDTO.setDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
			produtoEdicaoDTO.setTipoChamadaEncalhe(chamadaEncalhe.getTipoChamadaEncalhe());
			
			
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
			
			ChamadaEncalhe chamadaEncalhe = this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(numeroCota, produtoEdicao);

			Integer dia = obterQtdeDiaAposDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
			
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
			produtoEdicaoDTO.setDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
			produtoEdicaoDTO.setTipoChamadaEncalhe(chamadaEncalhe.getTipoChamadaEncalhe());
			
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
		
			ChamadaEncalhe chamadaEncalhe = this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(numeroCota, produtoEdicao);

			Integer dia = obterQtdeDiaAposDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
			
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
			produtoEdicaoDTO.setDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
			produtoEdicaoDTO.setTipoChamadaEncalhe(chamadaEncalhe.getTipoChamadaEncalhe());
			
			Integer sequenciaMatriz = produtoEdicaoRepository.obterCodigoMatrizPorProdutoEdicao(produtoEdicao.getId());
			produtoEdicaoDTO.setSequenciaMatriz(sequenciaMatriz);
			
		}
		
		return produtoEdicaoDTO;
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
	
	@Transactional
	public void finalizarConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario) {
	
		if(	controleConfEncalheCota.getId() != null) {
			
			ControleConferenciaEncalheCota controleConferenciaEncalheCotaFromBD = 
					controleConferenciaEncalheCotaRepository.buscarPorId(controleConfEncalheCota.getId());
			
			if(StatusOperacao.CONCLUIDO.equals(controleConferenciaEncalheCotaFromBD.getStatus())) {
				resetarDadosFinanceirosConferenciaEncalheCota(controleConferenciaEncalheCotaFromBD);
			}
			
		} 			
		
		inserirDadosConferenciaEncalhe(controleConfEncalheCota, listaConferenciaEncalhe, listaIdConferenciaEncalheParaExclusao, usuario, StatusOperacao.CONCLUIDO);
		
		gerarDiferencas();
		
		Set<String> nossoNumeroCollection = gerarCobranca(controleConfEncalheCota);
		
		gerarDocumentosConferenciaEncalhe(controleConfEncalheCota, nossoNumeroCollection);
		
	}
	
	/**
	 * Gera o movimento financeiro referente a operação de conferência de encalhe e
	 * em seguida dispara componentes responsáveis pela geração da cobrança.
	 * 
	 * @param controleConferenciaEncalheCota
	 * 
	 * @return Set - String
	 */
	private Set<String> gerarCobranca(ControleConferenciaEncalheCota controleConferenciaEncalheCota) {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Long idControleConferenciaEncalheCota = controleConferenciaEncalheCota.getId();
		
		List<MovimentoEstoqueCota> movimentosEstoqueCotaOperacaoConferenciaEncalhe = 
				movimentoEstoqueCotaRepository.obterListaMovimentoEstoqueCotaParaOperacaoConferenciaEncalhe(idControleConferenciaEncalheCota);
		
		BigDecimal valorTotalEncalheOperacaoConferenciaEncalhe = 
				conferenciaEncalheRepository.obterValorTotalEncalheOperacaoConferenciaEncalhe(idControleConferenciaEncalheCota, distribuidor.getId());
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
		
		MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();
		
		movimentoFinanceiroCotaDTO.setCota(controleConferenciaEncalheCota.getCota());
		movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiro);
		movimentoFinanceiroCotaDTO.setUsuario(controleConferenciaEncalheCota.getUsuario());
		movimentoFinanceiroCotaDTO.setValor(valorTotalEncalheOperacaoConferenciaEncalhe);
		movimentoFinanceiroCotaDTO.setDataOperacao(controleConferenciaEncalheCota.getDataOperacao());
		movimentoFinanceiroCotaDTO.setBaixaCobranca(null);
		movimentoFinanceiroCotaDTO.setDataVencimento(controleConferenciaEncalheCota.getDataOperacao());
		movimentoFinanceiroCotaDTO.setDataAprovacao(controleConferenciaEncalheCota.getDataOperacao());
		movimentoFinanceiroCotaDTO.setDataCriacao(controleConferenciaEncalheCota.getDataOperacao());
		movimentoFinanceiroCotaDTO.setObservacao(null);
		movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
		movimentoFinanceiroCotaDTO.setAprovacaoAutomatica(true);
		movimentoFinanceiroCotaDTO.setLancamentoManual(false);
		movimentoFinanceiroCotaDTO.setMovimentos(movimentosEstoqueCotaOperacaoConferenciaEncalhe);
		
		movimentoFinanceiroCotaService.gerarMovimentoFinanceiroDebitoCredito(movimentoFinanceiroCotaDTO);

		Set<String> nossoNumeroCollection = gerarCobrancaService.gerarCobranca(
				controleConferenciaEncalheCota.getCota().getId(), 
				controleConferenciaEncalheCota.getUsuario().getId());
		
		return nossoNumeroCollection;
		
	}

	
	/**
	 * Faz o cancelamento de dados financeiros relativos a 
	 * operação de conferência de encalhe em questão.
	 * 
	 * @param controleConferenciaEncalheCota
	 */
	private void resetarDadosFinanceirosConferenciaEncalheCota(ControleConferenciaEncalheCota controleConferenciaEncalheCota) {
		
		List<ConferenciaEncalhe> listaConferenciaEncalhe = controleConferenciaEncalheCota.getConferenciasEncalhe();
		
		if( listaConferenciaEncalhe==null || listaConferenciaEncalhe.isEmpty() ) {
			throw new IllegalStateException("Nenhum registro de conferencia de encalhe, não foi possível fazer reabertura.");
		}
		
		MovimentoEstoqueCota movimentoEstoqueCota = listaConferenciaEncalhe.get(0).getMovimentoEstoqueCota();

		MovimentoFinanceiroCota movimentoFinanceiroCota = 
				movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCotaParaMovimentoEstoqueCota(movimentoEstoqueCota.getId());
		
		gerarCobrancaService.cancelarDividaCobranca(movimentoFinanceiroCota.getId());
		
		movimentoFinanceiroCotaRepository.remover(movimentoFinanceiroCota);
		
		
	}
	
	private void inserirDadosConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario,
			StatusOperacao statusOperacao) {
		
	    Date dataRecolhimentoReferencia = obterDataRecolhimentoReferencia();
		
		Integer numeroCota = controleConfEncalheCota.getCota().getNumeroCota();
		
		atualizarCabecalhoNotaFiscalEntradaCota(controleConfEncalheCota);
		
		atualizarItensNotaFiscalEntradaCota(
				controleConfEncalheCota.getNotaFiscalEntradaCota(), 
				listaConferenciaEncalhe);
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				obterControleConferenciaEncalheCotaParaConfEncalhe(controleConfEncalheCota, statusOperacao, usuario);
		
		Date dataCriacao = new Date();

		
		Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque = 
				obterMapaTipoMovimentoEstoque();


		for(ConferenciaEncalheDTO conferenciaEncalheDTO : listaConferenciaEncalhe) {
			
			if(conferenciaEncalheDTO.getIdConferenciaEncalhe()!=null) {

				ConferenciaEncalhe conferenciaEncalheFromDB = conferenciaEncalheRepository.buscarPorId(conferenciaEncalheDTO.getIdConferenciaEncalhe());
				
				MovimentoEstoqueCota movimentoEstoqueCota = null;
				
				MovimentoEstoque movimentoEstoque = null;
				
				if(StatusOperacao.CONCLUIDO.equals(statusOperacao)) {
					
					movimentoEstoqueCota = conferenciaEncalheFromDB.getMovimentoEstoqueCota();
					
					movimentoEstoque = conferenciaEncalheFromDB.getMovimentoEstoque();
					
					if(movimentoEstoqueCota!=null) {
					
						atualizarMovimentoEstoqueCota(movimentoEstoqueCota, conferenciaEncalheDTO, mapaTipoMovimentoEstoque);
					
					} else {
						
						movimentoEstoqueCota = criarNovoRegistroMovimentoEstoqueCota(
								controleConferenciaEncalheCota, 
								conferenciaEncalheDTO, 
								numeroCota, 
								dataRecolhimentoReferencia, 
								dataCriacao, 
								mapaTipoMovimentoEstoque, 
								usuario);
					}
					
					if(movimentoEstoque!=null) {
					
						atualizarMovimentoEstoque(movimentoEstoque, conferenciaEncalheDTO, mapaTipoMovimentoEstoque);
					
					} else {
						
						movimentoEstoque = criarNovoRegistroMovimentoEstoque(
								controleConferenciaEncalheCota, 
								conferenciaEncalheDTO, 
								numeroCota, 
								dataRecolhimentoReferencia, 
								dataCriacao, 
								mapaTipoMovimentoEstoque, 
								usuario);
						
					}
					
				}
				
				atualizarRegistroConferenciaEncalhe(
						conferenciaEncalheDTO, 
						conferenciaEncalheFromDB,
						movimentoEstoqueCota,
						movimentoEstoque);
				
				
			} else {
				
				MovimentoEstoqueCota movimentoEstoqueCota = null;
				
				MovimentoEstoque movimentoEstoque = null;
				
				if(StatusOperacao.CONCLUIDO.equals(statusOperacao)) {
					
					 movimentoEstoqueCota = criarNovoRegistroMovimentoEstoqueCota(
								controleConferenciaEncalheCota, 
								conferenciaEncalheDTO, 
								numeroCota, 
								dataRecolhimentoReferencia, 
								dataCriacao, 
								mapaTipoMovimentoEstoque, 
								usuario);
					 
					 movimentoEstoque = criarNovoRegistroMovimentoEstoque(
								controleConferenciaEncalheCota, 
								conferenciaEncalheDTO, 
								numeroCota, 
								dataRecolhimentoReferencia, 
								dataCriacao, 
								mapaTipoMovimentoEstoque, 
								usuario);
				}
				
				criarNovoRegistroConferenciaEncalhe(
						controleConferenciaEncalheCota, 
						conferenciaEncalheDTO, 
						numeroCota, 
						dataRecolhimentoReferencia, 
						movimentoEstoqueCota,
						movimentoEstoque);
				
			}
			
		}
		
		if(listaIdConferenciaEncalheParaExclusao!=null && !listaIdConferenciaEncalheParaExclusao.isEmpty()) {
		
			for(Long idConferenciaEncalheExclusao : listaIdConferenciaEncalheParaExclusao) {
				excluirRegistroConferenciaEncalhe(idConferenciaEncalheExclusao);
			}
			
		}
		
	}
	
	/**
	 * Obtém o tipo de movimento de estoque do distribuidor.
	 * 
	 * @param juramentada
	 * @param dataRecolhimentoDistribuidor
	 * @param dataConferenciaEncalhe
	 * @param mapaTipoMovimentoEstoque
	 * 
	 * @return TipoMovimentoEstoque
	 */
	private TipoMovimentoEstoque obterTipoMovimentoEstoqueDistribuidor(
			boolean juramentada,
			Date dataRecolhimentoDistribuidor,
			Date dataConferenciaEncalhe,
			Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque) {
		
		if(juramentada) {
			return mapaTipoMovimentoEstoque.get(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE_JURAMENTADO);
		}
		
		if(isDataRecolhimentoDistribuidorMenorIgualDataConferenciaEncalhe(dataRecolhimentoDistribuidor, dataConferenciaEncalhe)) {
			
			return mapaTipoMovimentoEstoque.get(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE);
			
		} else {
			
			return mapaTipoMovimentoEstoque.get(GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO);
			
		}
		
		
	}
	
	
	/**
	 * Verifica se a dataRecolhimentoDistribuidor é menor ou igual a dataConferenciaEncalhe.
	 * 
	 * @param dataRecolhimentoDistribuidor
	 * @param dataConferenciaEncalhe
	 * 
	 * @return boolean
	 */
	private boolean isDataRecolhimentoDistribuidorMenorIgualDataConferenciaEncalhe(Date dataRecolhimentoDistribuidor, Date dataConferenciaEncalhe) {
			
		dataRecolhimentoDistribuidor =  DateUtil.removerTimestamp(dataRecolhimentoDistribuidor);
			
		dataConferenciaEncalhe = DateUtil.removerTimestamp(dataConferenciaEncalhe);
			
		return (dataRecolhimentoDistribuidor.compareTo(dataConferenciaEncalhe)<=0);
		
	}
	
	
	
	/**
	 * Obtém um mapa de tipoMovimentoEstoque utilizados 
	 * pela funcionalidade de conferência de encalhe.
	 * 
	 * @return Map<GrupoMovimentoEstoque, TipoMovimentoEstoque>
	 */
	private Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> obterMapaTipoMovimentoEstoque() {
		
		Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque = new HashMap<GrupoMovimentoEstoque, TipoMovimentoEstoque>();
		
		mapaTipoMovimentoEstoque.put(
				GrupoMovimentoEstoque.ENVIO_ENCALHE, 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_ENCALHE));
		
		mapaTipoMovimentoEstoque.put(
				GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE, 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE));
		
		mapaTipoMovimentoEstoque.put(
				GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE_JURAMENTADO, 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE_JURAMENTADO));
		
		mapaTipoMovimentoEstoque.put(
				GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO, 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO));

		return mapaTipoMovimentoEstoque;
		
	}
	
	/**
	 * Atualiza os dados da notaFiscalEntradaCota relacionada com 
	 * uma operação de conferência de encalhe.
	 * 
	 * @param controleConferenciaEncalheCota
	 */
	private void atualizarCabecalhoNotaFiscalEntradaCota(ControleConferenciaEncalheCota controleConferenciaEncalheCota) {
		
		if(controleConferenciaEncalheCota.getNotaFiscalEntradaCota() == null) {
			return;
		}
		
		if(controleConferenciaEncalheCota.getNotaFiscalEntradaCota().getId()!=null) {
			
			notaFiscalEntradaRepository.alterar(controleConferenciaEncalheCota.getNotaFiscalEntradaCota());
			
		} else {
			
			notaFiscalEntradaRepository.adicionar(controleConferenciaEncalheCota.getNotaFiscalEntradaCota());
			
		}
		
		
	}
	
	/**
	 * Atualiza os itens relativos a uma notaFiscalEntradaCota.
	 */
	private void atualizarItensNotaFiscalEntradaCota(
			NotaFiscalEntradaCota notaFiscalEntradaCota,
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe) {
		
		if(notaFiscalEntradaCota == null || notaFiscalEntradaCota.getId() == null) {
			return;
		}
		
		Long idNotaFiscalEntrada = notaFiscalEntradaCota.getId();
		
		List<ItemNotaFiscalEntrada> itensNotaFiscalEntradaCota = 
				itemNotaFiscalEntradaRepository.buscarItensPorIdNota(idNotaFiscalEntrada);
		
		if(itensNotaFiscalEntradaCota!= null && !itensNotaFiscalEntradaCota.isEmpty()) {
			for(ItemNotaFiscalEntrada itemNFEntrada : itensNotaFiscalEntradaCota) {
				itemNotaFiscalEntradaRepository.remover(itemNFEntrada);
			}
		}
		
		if(listaConferenciaEncalhe != null && !listaConferenciaEncalhe.isEmpty()) {
			
			for(ConferenciaEncalheDTO conferenciaEncalhe : listaConferenciaEncalhe) {

				ProdutoEdicao produtoEdicao = new ProdutoEdicao();
				produtoEdicao.setId(conferenciaEncalhe.getIdProdutoEdicao());
				
				ItemNotaFiscalEntrada item = new ItemNotaFiscalEntrada();
				item.setNotaFiscal(notaFiscalEntradaCota);
				item.setQtde(conferenciaEncalhe.getQtdInformada());
				item.setProdutoEdicao(produtoEdicao);
				
			}
			
		}
		
	}
	
	
	@Transactional
	public void salvarDadosConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario) throws EncalheSemPermissaoSalvarException, ConferenciaEncalheFinalizadaException {

		validarConferenciaEncalheReaberta(controleConfEncalheCota.getId());
		
		validarPermissaoSalvarConferenciaEncalhe(listaConferenciaEncalhe);
		
		inserirDadosConferenciaEncalhe(controleConfEncalheCota, listaConferenciaEncalhe, listaIdConferenciaEncalheParaExclusao, usuario, StatusOperacao.EM_ANDAMENTO);
		
	}
	
	/**
	 * Se uma conferência de encalhe ja foi finalizada e depois reaberta, a mesma
	 * não poderá ser somente salva após alterações. O usuário devera invocar a ação
	 * finalizarConferencia para que os dados de cobrança sejam regerados.
	 * 
	 * @param idControleConferenciaEncalheCota
	 * @throws ConferenciaEncalheFinalizadaException
	 */
	private void validarConferenciaEncalheReaberta(Long idControleConferenciaEncalheCota) throws ConferenciaEncalheFinalizadaException {
		
		if(idControleConferenciaEncalheCota == null) {
			return;
		}
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = controleConferenciaEncalheCotaRepository.buscarPorId(idControleConferenciaEncalheCota);
		
		if(StatusOperacao.CONCLUIDO.equals(controleConferenciaEncalheCota.getStatus())){
			
			throw new ConferenciaEncalheFinalizadaException();
			
		}
		
	}
	
	public void validarQtdeEncalheExcedeQtdeReparte(Integer numeroCota, Long idProdutoEdicao, BigDecimal qtdeExemplarEncalhe) throws EncalheExcedeReparteException {
		
		//TODO implementar este método
		
		//sera necessario identificar qual o valor do reparte para a cota e produtoEdicao em questao.
		
		//porem temos o problema de um relancamento de produto edicao.
				
	}
	
	/**
	 * Verifica se os itens da conferenciaEncalhe são todos referentes ao CHAMADÃO, do contrário,
	 * não será permitido salvar a conferência.
	 * 
	 * @param listaConferenciaEncalhe
	 */
	private void validarPermissaoSalvarConferenciaEncalhe(List<ConferenciaEncalheDTO> listaConferenciaEncalhe) throws EncalheSemPermissaoSalvarException {
		
		if(listaConferenciaEncalhe == null || listaConferenciaEncalhe.isEmpty()) {
			return;
		}
		
		for(ConferenciaEncalheDTO conferencia : listaConferenciaEncalhe) {
			
			if(!TipoChamadaEncalhe.CHAMADAO.equals(conferencia.getTipoChamadaEncalhe()))  {
				
				throw new EncalheSemPermissaoSalvarException("Não é possível salvar conferência de encalhe, o produto \"" + 
															 conferencia.getNomeProduto() +  
															"\" não pertence a um \"CHAMADÃO\"");
				
			}
			
		}
		
	}
	
	/**
	 * Exclui um registros de ConferenciaEncalhe e movimentos relacionados como 
	 * MovimentoEstoqueCota e MovimentoEstoque.
	 * 
	 * @param idConferenciaEncalhe
	 */
	private void excluirRegistroConferenciaEncalhe(Long idConferenciaEncalhe) {
		
		ConferenciaEncalhe conferenciaEncalhe = conferenciaEncalheRepository.buscarPorId(idConferenciaEncalhe);
		
		MovimentoEstoqueCota movimentoEstoqueCota = conferenciaEncalhe.getMovimentoEstoqueCota();
		if(movimentoEstoqueCota!=null){
			excluirRegistroMovimentoEstoqueCota(movimentoEstoqueCota);
		}
		
		
		MovimentoEstoque movimentoEstoque = conferenciaEncalhe.getMovimentoEstoque();
		if(movimentoEstoque!=null){
			excluirRegistroMovimentoEstoque(movimentoEstoque);
		}
		
		conferenciaEncalheRepository.remover(conferenciaEncalhe);
		
	}
	
	/**
	 * Exclui registro de MovimentoEstoqueCota alterando consequentemente
	 * o EstoqueProdutoCota relativo.
	 * 
	 * @param movimentoEstoqueCota
	 */
	private void excluirRegistroMovimentoEstoqueCota(MovimentoEstoqueCota movimentoEstoqueCota) {
		
		if(movimentoEstoqueCota.getQtde() != null && (movimentoEstoqueCota.getQtde().compareTo(BigDecimal.ZERO) != 0)) {

			EstoqueProdutoCota estoqueProdutoCota = movimentoEstoqueCota.getEstoqueProdutoCota();
			
			if(estoqueProdutoCota != null) {

				BigDecimal qtdeDevolvidaOriginal = estoqueProdutoCota.getQtdeDevolvida() == null ? BigDecimal.ZERO : estoqueProdutoCota.getQtdeDevolvida();
				
				estoqueProdutoCota.setQtdeDevolvida(qtdeDevolvidaOriginal.subtract(movimentoEstoqueCota.getQtde()));

				estoqueProdutoCotaRepository.alterar(estoqueProdutoCota);
				
			}
			
		}
		
		movimentoEstoqueCotaRepository.remover(movimentoEstoqueCota);
		
	}

	/**
	 * Exclui um registro de MovimentoEstoque alterando consequentemente
	 * o EstoqueProduto relativo.
	 * 
	 * @param movimentoEstoque
	 */
	private void excluirRegistroMovimentoEstoque(MovimentoEstoque movimentoEstoque) {
		
		if(movimentoEstoque.getQtde() != null && (movimentoEstoque.getQtde().compareTo(BigDecimal.ZERO) != 0)) {

			GrupoMovimentoEstoque grupoMovimentoEstoqueExcluido = ((TipoMovimentoEstoque)movimentoEstoque.getTipoMovimento()).getGrupoMovimentoEstoque();
			
			EstoqueProduto estoqueProduto = movimentoEstoque.getEstoqueProduto();
			
			if(estoqueProduto != null) {
				
				if(GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO.equals(grupoMovimentoEstoqueExcluido)) {

					BigDecimal qtdeOriginal = estoqueProduto.getQtdeSuplementar() == null ? BigDecimal.ZERO : estoqueProduto.getQtdeSuplementar();
					
					estoqueProduto.setQtdeSuplementar(qtdeOriginal.subtract(movimentoEstoque.getQtde()));

					estoqueProdutoRepository.alterar(estoqueProduto);

					
				} else {
					
					BigDecimal qtdeOriginal = estoqueProduto.getQtde() == null ? BigDecimal.ZERO : estoqueProduto.getQtde();
					
					estoqueProduto.setQtde(qtdeOriginal.subtract(movimentoEstoque.getQtde()));

					estoqueProdutoRepository.alterar(estoqueProduto);
					
				}
				
			}
			
		}
		
		movimentoEstoqueRepository.remover(movimentoEstoque);
		
	}

	
	/**
	 * Atualiza um registro existente de ConferenciaEncalhe.
	 * 
	 * @param conferenciaEncalheDTO
	 * @param conferenciaEncalheFromDB
	 * @param movimentoEstoqueCota
	 * @param movimentoEstoque
	 */
	private void atualizarRegistroConferenciaEncalhe(
			ConferenciaEncalheDTO conferenciaEncalheDTO, 
			ConferenciaEncalhe conferenciaEncalheFromDB,
			MovimentoEstoqueCota movimentoEstoqueCota,
			MovimentoEstoque movimentoEstoque) {
		
		conferenciaEncalheFromDB.setObservacao(conferenciaEncalheDTO.getObservacao());
		conferenciaEncalheFromDB.setJuramentada(conferenciaEncalheDTO.isJuramentada());
		conferenciaEncalheFromDB.setQtdeInformada(conferenciaEncalheDTO.getQtdInformada());
		conferenciaEncalheFromDB.setMovimentoEstoqueCota(movimentoEstoqueCota);
		conferenciaEncalheFromDB.setMovimentoEstoque(movimentoEstoque);
		
		conferenciaEncalheRepository.alterar(conferenciaEncalheFromDB);
		
	}
	
	/**
	 * Cria um novo registro de MovimentoEstoqueCota.
	 * 
	 * @param controleConferenciaEncalheCota
	 * @param conferenciaEncalheDTO
	 * @param numeroCota
	 * @param dataRecolhimentoReferencia
	 * @param dataCriacao
	 * @param mapaTipoMovimentoEstoque
	 * @param usuario
	 * 
	 * @return MovimentoEstoqueCota
	 */
	private MovimentoEstoqueCota criarNovoRegistroMovimentoEstoqueCota(
			ControleConferenciaEncalheCota controleConferenciaEncalheCota,
			ConferenciaEncalheDTO conferenciaEncalheDTO,
			Integer numeroCota, 
			Date dataRecolhimentoReferencia,
			Date dataCriacao,
			Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque,
			Usuario usuario) {
		
		ChamadaEncalheCota chamadaEncalheCota = 
				obterChamadaEncalheCotaParaConfEncalhe(
						numeroCota, 
						dataRecolhimentoReferencia, 
						conferenciaEncalheDTO.getIdProdutoEdicao());
		Cota cota = chamadaEncalheCota.getCota();
		Lancamento lancamento = obterLancamentoParaConfEncalhe(chamadaEncalheCota.getChamadaEncalhe().getDataRecolhimento(), conferenciaEncalheDTO.getIdProdutoEdicao());
		ProdutoEdicao produtoEdicao = lancamento.getProdutoEdicao();

		TipoMovimentoEstoque tipoMovimentoEstoqueCota = mapaTipoMovimentoEstoque.get(GrupoMovimentoEstoque.ENVIO_ENCALHE);
		
		MovimentoEstoqueCota movimentoEstoqueCota = 
				movimentoEstoqueService.gerarMovimentoCota(
						null, 
						produtoEdicao.getId(), 
						cota.getId(), 
						usuario.getId(), 
						conferenciaEncalheDTO.getQtdExemplar(), 
						tipoMovimentoEstoqueCota);
		
		return movimentoEstoqueCota;
		
		
		
	}
	
	/**
	 * Cria um novo registro de MovimentoEstoque.
	 * 
	 * @param controleConferenciaEncalheCota
	 * @param conferenciaEncalheDTO
	 * @param numeroCota
	 * @param dataRecolhimentoReferencia
	 * @param dataCriacao
	 * @param mapaTipoMovimentoEstoque
	 * @param usuario
	 * 
	 * @return MovimentoEstoque
	 */
	private MovimentoEstoque criarNovoRegistroMovimentoEstoque(
			ControleConferenciaEncalheCota controleConferenciaEncalheCota,
			ConferenciaEncalheDTO conferenciaEncalheDTO,
			Integer numeroCota, 
			Date dataRecolhimentoReferencia,
			Date dataCriacao,
			Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque,
			Usuario usuario) {
		
		TipoMovimentoEstoque tipoMovimentoEstoque = 
				obterTipoMovimentoEstoqueDistribuidor(
						conferenciaEncalheDTO.isJuramentada(), 
						conferenciaEncalheDTO.getDataRecolhimento(),
						dataCriacao, 
						mapaTipoMovimentoEstoque);
		
		ChamadaEncalheCota chamadaEncalheCota = 
				obterChamadaEncalheCotaParaConfEncalhe(
						numeroCota, 
						dataRecolhimentoReferencia, 
						conferenciaEncalheDTO.getIdProdutoEdicao());
		
		Lancamento lancamento = obterLancamentoParaConfEncalhe(chamadaEncalheCota.getChamadaEncalhe().getDataRecolhimento(), conferenciaEncalheDTO.getIdProdutoEdicao());
		
		ProdutoEdicao produtoEdicao = lancamento.getProdutoEdicao();
		
		MovimentoEstoque movimentoEstoque = movimentoEstoqueService.gerarMovimentoEstoque(
				conferenciaEncalheDTO.getDataRecolhimento(), 
				produtoEdicao.getId(), 
				usuario.getId(), 
				conferenciaEncalheDTO.getQtdExemplar(), 
				tipoMovimentoEstoque);
		
		return movimentoEstoque;
		
	}
	
	/**
	 * Cria um novo registro de ConferenciaEncalhe.
	 * 
	 * @param controleConferenciaEncalheCota
	 * @param conferenciaEncalheDTO
	 * @param numeroCota
	 * @param dataRecolhimentoReferencia
	 * @param movimentoEstoqueCota
	 * @param movimentoEstoque
	 */
	private void criarNovoRegistroConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConferenciaEncalheCota,
			ConferenciaEncalheDTO conferenciaEncalheDTO,
			Integer numeroCota, 
			Date dataRecolhimentoReferencia,
			MovimentoEstoqueCota movimentoEstoqueCota,
			MovimentoEstoque movimentoEstoque) {
	
		ChamadaEncalheCota chamadaEncalheCota = 
				obterChamadaEncalheCotaParaConfEncalhe(
						numeroCota, 
						dataRecolhimentoReferencia, 
						conferenciaEncalheDTO.getIdProdutoEdicao());
		
		
		ConferenciaEncalhe conferenciaEncalhe = new ConferenciaEncalhe();
		
		conferenciaEncalhe.setChamadaEncalheCota(chamadaEncalheCota);
		
		conferenciaEncalhe.setControleConferenciaEncalheCota(controleConferenciaEncalheCota);
		
		conferenciaEncalhe.setMovimentoEstoqueCota(movimentoEstoqueCota);
		
		conferenciaEncalhe.setMovimentoEstoque(movimentoEstoque);
		
		conferenciaEncalhe.setJuramentada(conferenciaEncalheDTO.isJuramentada());
		
		conferenciaEncalhe.setObservacao(conferenciaEncalheDTO.getObservacao());
		
		conferenciaEncalhe.setQtdeInformada(conferenciaEncalheDTO.getQtdInformada());
		
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
	
	/**
	 * Caso o parâmetro ctrlConfEncalheCota conter um id, sera obtido do banco de dados um registro 
	 * ControleConferenciaEncalheCota referente ao mesmo, senão, sera criado um novo registro.
	 * 
	 * @param ctrlConfEncalheCota
	 * @param statusOperacao
	 * @param usuario
	 * 
	 * @return ControleConferenciaEncalheCota
	 */
	private ControleConferenciaEncalheCota obterControleConferenciaEncalheCotaParaConfEncalhe(  
			ControleConferenciaEncalheCota ctrlConfEncalheCota, StatusOperacao statusOperacao, Usuario usuario) {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Cota cota = cotaRepository.obterPorNumerDaCota(ctrlConfEncalheCota.getCota().getNumeroCota());
		
		if(ctrlConfEncalheCota.getId()!=null) { 
			
			ControleConferenciaEncalheCota controleConferenciaEncalheCotaFromBD = 			
					controleConferenciaEncalheCotaRepository.buscarPorId(ctrlConfEncalheCota.getId());
			
			controleConferenciaEncalheCotaFromBD.setStatus(statusOperacao);
			controleConferenciaEncalheCotaFromBD.setUsuario(usuario);
			
			return controleConferenciaEncalheCotaRepository.merge(controleConferenciaEncalheCotaFromBD);
			
		}
		
		ctrlConfEncalheCota.setUsuario(usuario);
		ctrlConfEncalheCota.setCota(cota);
		ctrlConfEncalheCota.setDataOperacao(distribuidor.getDataOperacao());
		ctrlConfEncalheCota.setStatus(statusOperacao);
		
		controleConferenciaEncalheCotaRepository.adicionar(ctrlConfEncalheCota);
		
		return ctrlConfEncalheCota;
	}
	
	

	/**
	 * Atualiza o registro de MovimentoEstoqueCota assim como o 
	 * EstoqueProdutoCota relativo ao mesmo.
	 * 
	 * @param movimentoEstoqueCota
	 * @param conferenciaEncalheDTO
	 * @param mapaTipoMovimentoEstoque
	 */
	private void atualizarMovimentoEstoqueCota(
			MovimentoEstoqueCota movimentoEstoqueCota, 
			ConferenciaEncalheDTO conferenciaEncalheDTO, 
			Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque) {
		
		BigDecimal oldQtdeMovEstoqueCota = movimentoEstoqueCota.getQtde();
		
		BigDecimal newQtdeMovEstoquecota = conferenciaEncalheDTO.getQtdExemplar();
		
		movimentoEstoqueCota.setQtde(newQtdeMovEstoquecota);
		
		movimentoEstoqueCotaRepository.alterar(movimentoEstoqueCota);
		
		EstoqueProdutoCota estoqueProdutoCota =  movimentoEstoqueCota.getEstoqueProdutoCota();
		
		BigDecimal qtdDevolvida = 
				estoqueProdutoCota.getQtdeDevolvida() != null ? 
					estoqueProdutoCota.getQtdeDevolvida() : BigDecimal.ZERO;
					
		qtdDevolvida = qtdDevolvida.subtract(oldQtdeMovEstoqueCota).add(newQtdeMovEstoquecota);
		
		estoqueProdutoCota.setQtdeDevolvida(qtdDevolvida);
		
		estoqueProdutoCotaRepository.alterar(estoqueProdutoCota);
		
	}
	
	/**
	 * Atualiza registro de MovimentoEstoque bem como o registro 
	 * de EstoqueProduto relacionado.
	 * 
	 * @param movimentoEstoque
	 * @param conferenciaEncalheDTO
	 * @param mapaTipoMovimentoEstoque
	 */
	private void atualizarMovimentoEstoque(
			MovimentoEstoque movimentoEstoque, 
			ConferenciaEncalheDTO conferenciaEncalheDTO, 
			Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque) {
		
		BigDecimal oldQtdeMovEstoque = movimentoEstoque.getQtde();
		
		BigDecimal newQtdeMovEstoque = conferenciaEncalheDTO.getQtdExemplar();
		
		movimentoEstoque.setQtde(newQtdeMovEstoque);
		
		GrupoMovimentoEstoque grupoMovimentoEstoque = ((TipoMovimentoEstoque)movimentoEstoque.getTipoMovimento()).getGrupoMovimentoEstoque();
		
		movimentoEstoqueRepository.alterar(movimentoEstoque);
		
		EstoqueProduto estoqueProduto =  movimentoEstoque.getEstoqueProduto();
		
		if(estoqueProduto != null) {
			
			if(GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO.equals(grupoMovimentoEstoque)) {

				BigDecimal qtdeOriginal = estoqueProduto.getQtdeSuplementar() == null ? BigDecimal.ZERO : estoqueProduto.getQtdeSuplementar();
				estoqueProduto.setQtdeSuplementar(qtdeOriginal.subtract(oldQtdeMovEstoque));

				estoqueProduto.setQtdeSuplementar(estoqueProduto.getQtdeSuplementar().add(newQtdeMovEstoque));
				estoqueProdutoRepository.alterar(estoqueProduto);

				
			} else {
				
				BigDecimal qtdeOriginal = estoqueProduto.getQtde() == null ? BigDecimal.ZERO : estoqueProduto.getQtde();
				estoqueProduto.setQtde(qtdeOriginal.subtract(oldQtdeMovEstoque));

				estoqueProduto.setQtde(estoqueProduto.getQtde().add(newQtdeMovEstoque));
				estoqueProdutoRepository.alterar(estoqueProduto);
				
			}
			
		}
		
		estoqueProdutoRepository.alterar(estoqueProduto);
		
	}
	
	
	private void gerarDiferencas() {
		
		//TODO: invocar workflow das diferenças.
		
		//Pagina 4 paragrafo 4
		
	}
	
   /*
	* Apos finalizar conferencia de encalhe sera verificado        
	* quais documentos serao gerados e se os mesmos serao impressos
	* ou enviados por email.                                       
	*/
	private void gerarDocumentosConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConferenciaEncalheCota, 
			Set<String> nossoNumeroCollection) {
		
		PoliticaCobranca politicaCobranca = politicaCobrancaService.obterPoliticaCobrancaPrincipal();
		
		FormaEmissao formaEmissao = politicaCobranca.getFormaEmissao();
		
		switch(formaEmissao) {
		
			case INDIVIDUAL_AGREGADA:
				//Individual - Agregada a C.E.
			break;
			
			case INDIVIDUAL_BOX:
				//TODO gerar arquivo ...
			break;
			
			case EM_MASSA:
				//Em massa - Após geração de dívida
			break;
			
			case NAO_IMPRIME:
				//Não imprime
			break;
		
		}
		
		boolean indEnviaEmail = politicaCobranca.getFormaCobranca().isRecebeCobrancaEmail();
		
		gerarSlip();
		
		gerarBoleto();
		
		gerarRecibo();
		
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
