package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.DocumentoConferenciaEncalheDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.dto.SlipDTO;
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
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.repository.LancamentoParcialRepository;
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
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.service.exception.ChamadaEncalheCotaInexistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheExistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheFinalizadaException;
import br.com.abril.nds.service.exception.EncalheExcedeReparteException;
import br.com.abril.nds.service.exception.EncalheRecolhimentoParcialException;
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
	
	@Autowired
	private LancamentoParcialRepository lancamentoParcialRepository;
	
	@Autowired
	private DocumentoCobrancaService documentoCobrancaService;
	
	@Autowired
	private ControleConferenciaEncalheRepository controleConferenciaEncalheRepository;
	
	
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
	 * Obs.: A conferência de encalhe de ProdutoEdicao parcial só será possível se
	 * houver chamadaEncalhe para o mesmo na dataOperacao atual, senão sera lançada 
	 * EncalheRecolhimentoParcialException.
	 * 
	 * Se encontrada, será retornada esta chamadaEncalhe para o produtoEdicao em questão.
	 * 
	 * @param numeroCota
	 * @param produtoEdicao
	 * 
	 * @return ChamadaEncalhe
	 * 
	 * @throws ChamadaEncalheCotaInexistenteException
	 * @throws EncalheRecolhimentoParcialException 
	 */
	private ChamadaEncalhe validarExistenciaChamadaEncalheParaCotaProdutoEdicao(Integer numeroCota, ProdutoEdicao produtoEdicao) throws ChamadaEncalheCotaInexistenteException, EncalheRecolhimentoParcialException {
		
		if(produtoEdicao.isParcial()) {

			boolean encalheConferido = false;
			
			boolean indPesquisaCEFutura = false;
			
			Distribuidor distribuidor = distribuidorService.obter();
			
			Date dataOperacao = distribuidor.getDataOperacao();
			
			List<ChamadaEncalheCota> listaChamadaEncalheCota = chamadaEncalheCotaRepository.
					obterListaChamaEncalheCota(numeroCota, dataOperacao, produtoEdicao.getId(), indPesquisaCEFutura, encalheConferido);

			if(listaChamadaEncalheCota == null || listaChamadaEncalheCota.isEmpty()) {
				throw new EncalheRecolhimentoParcialException();
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
			
			infoConfereciaEncalheCota.setIdControleConferenciaEncalheCota(controleConferenciaEncalheCota.getId());
			
		} else {
			
			infoConfereciaEncalheCota.setEncalhe(BigDecimal.ZERO);
			
		}
		
		List<Long> listaIdProdutoEdicao = 
				chamadaEncalheCotaRepository.obterListaIdProdutoEdicaoChamaEncalheCota(numeroCota, dataOperacao, false, false);
		
		BigDecimal reparte = BigDecimal.ZERO;
		
		if(listaIdProdutoEdicao != null && !listaIdProdutoEdicao.isEmpty()) {
			reparte = estoqueProdutoCotaRepository.obterValorTotalReparteCota(numeroCota, listaIdProdutoEdicao, distribuidor.getId());
		} 
		
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
	public ProdutoEdicaoDTO pesquisarProdutoEdicaoPorId(Integer numeroCota, Long idProdutoEdicao) throws ChamadaEncalheCotaInexistenteException, EncalheRecolhimentoParcialException {
		
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
	public ProdutoEdicaoDTO pesquisarProdutoEdicaoPorSM(Integer numeroCota, Integer sm) throws ChamadaEncalheCotaInexistenteException, EncalheRecolhimentoParcialException {
		
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
	public ProdutoEdicaoDTO pesquisarProdutoEdicaoPorCodigoDeBarras(Integer numeroCota, String codigoDeBarras) throws ChamadaEncalheCotaInexistenteException, EncalheRecolhimentoParcialException {
		
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
			Usuario usuario) throws EncalheExcedeReparteException {
	
		if(	controleConfEncalheCota.getId() != null) {
			
			ControleConferenciaEncalheCota controleConferenciaEncalheCotaFromBD = 
					controleConferenciaEncalheCotaRepository.buscarPorId(controleConfEncalheCota.getId());
			
			if(StatusOperacao.CONCLUIDO.equals(controleConferenciaEncalheCotaFromBD.getStatus())) {
				resetarDadosFinanceirosConferenciaEncalheCota(controleConferenciaEncalheCotaFromBD);
			}
			
		} 			
		
		inserirDadosConferenciaEncalhe(controleConfEncalheCota, listaConferenciaEncalhe, listaIdConferenciaEncalheParaExclusao, usuario, StatusOperacao.CONCLUIDO);
		
		executarProcedimentoDivergenciaNFEntradaDeEncalhe(controleConfEncalheCota);
		
		Set<String> nossoNumeroCollection = gerarCobranca(controleConfEncalheCota);
		
		String nossoNumero = "";
		
		if(nossoNumeroCollection!=null && !nossoNumeroCollection.isEmpty()) {
			
			nossoNumero = nossoNumeroCollection.iterator().next();
			
		}
		
		gerarDocumentosConferenciaEncalhe(controleConfEncalheCota, nossoNumero);
		
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
			StatusOperacao statusOperacao) throws EncalheExcedeReparteException {
		
	    Date dataRecolhimentoReferencia = obterDataRecolhimentoReferencia();
		
	    Distribuidor distribuidor = distribuidorService.obter();
		
		Date dataOperacao = distribuidor.getDataOperacao();
	    
		Integer numeroCota = controleConfEncalheCota.getCota().getNumeroCota();
		
		atualizarCabecalhoNotaFiscalEntradaCota(controleConfEncalheCota);
		
		atualizarItensNotaFiscalEntradaCota(
				controleConfEncalheCota.getNotaFiscalEntradaCota(), 
				listaConferenciaEncalhe);
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				obterControleConferenciaEncalheCotaAtualizado(controleConfEncalheCota, statusOperacao, usuario);
		
		Date dataCriacao = new Date();

		
		Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque = 
				obterMapaTipoMovimentoEstoque();


		for(ConferenciaEncalheDTO conferenciaEncalheDTO : listaConferenciaEncalhe) {
			
			validarQtdeEncalheExcedeQtdeReparte(
					controleConferenciaEncalheCota.getCota().getId(), 
					conferenciaEncalheDTO.getIdProdutoEdicao(),
					dataOperacao,
					conferenciaEncalheDTO.getDataRecolhimento(),
					conferenciaEncalheDTO.getQtdExemplar());

			
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
						dataCriacao,
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
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#salvarDadosConferenciaEncalhe(br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota, java.util.List, java.util.Set, br.com.abril.nds.model.seguranca.Usuario)
	 */
	@Transactional
	public void salvarDadosConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario) throws EncalheSemPermissaoSalvarException, ConferenciaEncalheFinalizadaException, EncalheExcedeReparteException {

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
	
	/**
	 * Valida se a quantidade da conferência de encalhe não excede o reparte 
	 * de um produtoEdicao para determinada cota.
	 * 
	 * @param idCota
	 * @param idProdutoEdicao
	 * @param dataOperacao
	 * @param dataRecolhimentoDistribuidor
	 * @param qtdeExemplarEncalhe
	 * 
	 * @throws EncalheExcedeReparteException
	 */
	private void validarQtdeEncalheExcedeQtdeReparte(
			Long idCota, 
			Long idProdutoEdicao, 
			Date dataOperacao,
			Date dataRecolhimentoDistribuidor,
			BigDecimal qtdeExemplarEncalhe) throws EncalheExcedeReparteException {
		
		BigDecimal qtdeReparte = obterQtdeReparteParaProdutoEdicao(
				idCota, 
				idProdutoEdicao, 
				dataOperacao, 
				dataRecolhimentoDistribuidor);
		
		if(qtdeExemplarEncalhe.compareTo(qtdeReparte)>0) {
			throw new EncalheExcedeReparteException();
		}
		
	}
	
	/**
	 * Obtém o reparte de um produtoEdicao para determinada cota.
	 * 
	 * @param idCota
	 * @param idProdutoEdicao
	 * @param dataOperacao
	 * @param dataRecolhimentoDistribuidor
	 * 
	 * @return BigDecimal
	 */
	private BigDecimal obterQtdeReparteParaProdutoEdicao(
			Long idCota, 
			Long idProdutoEdicao, 
			Date dataOperacao,
			Date dataRecolhimentoDistribuidor) {
		
		ProdutoEdicao produtoEdicao 	= produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		boolean indParcialNaoFinal = false;
		
		if(produtoEdicao.isParcial()) {
			
			LancamentoParcial lancamentoParcialFinal =  lancamentoParcialRepository.obterLancamentoParcial(produtoEdicao.getId(), dataOperacao);
			
			indParcialNaoFinal = (lancamentoParcialFinal == null);
		} 

		if(indParcialNaoFinal) {
			
			Date dataLancamento = lancamentoRepository.obterDataUltimoLancamentoParcial(idProdutoEdicao, dataOperacao);
			
			if(dataLancamento == null) {
				throw new IllegalStateException("Não foi possível validar a quantidade de " + 
												"reparte para o produtoEdicao de id: " + idProdutoEdicao);
			}
			
			BigDecimal qtdeReparte = movimentoEstoqueCotaRepository.obterQtdeMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo(
					idCota, 
					idProdutoEdicao, 
					dataLancamento, 
					dataRecolhimentoDistribuidor, 
					OperacaoEstoque.ENTRADA);

			if(qtdeReparte == null) {
				throw new IllegalStateException("Não foi possível validar a quantidade de " + 
												"reparte para o produtoEdicao de id: " + idProdutoEdicao);
			}
			
			return qtdeReparte;
			
		} else {

			EstoqueProdutoCota estoqueProdutoCota = estoqueProdutoCotaRepository.buscarEstoquePorProdutEdicaoECota(idProdutoEdicao, idCota);
			
			BigDecimal qtdeDevolvida 	= (estoqueProdutoCota.getQtdeDevolvida() == null) ? BigDecimal.ZERO : estoqueProdutoCota.getQtdeDevolvida();
			
			BigDecimal qtdeRecebida 	=  (estoqueProdutoCota.getQtdeRecebida() == null) ? BigDecimal.ZERO : estoqueProdutoCota.getQtdeRecebida();
			
			return  qtdeRecebida.subtract(qtdeDevolvida);
			
		}
		
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
		
		boolean juramentada = (conferenciaEncalheDTO.isJuramentada()) == null ? false : conferenciaEncalheDTO.isJuramentada();
		
		conferenciaEncalheFromDB.setObservacao(conferenciaEncalheDTO.getObservacao());
		conferenciaEncalheFromDB.setJuramentada(juramentada);
		conferenciaEncalheFromDB.setQtdeInformada(conferenciaEncalheDTO.getQtdInformada());
		conferenciaEncalheFromDB.setMovimentoEstoqueCota(movimentoEstoqueCota);
		conferenciaEncalheFromDB.setMovimentoEstoque(movimentoEstoque);
		conferenciaEncalheFromDB.setQtde(conferenciaEncalheDTO.getQtdExemplar());
		
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
		
		Long idCota = controleConferenciaEncalheCota.getCota().getId();
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(conferenciaEncalheDTO.getIdProdutoEdicao());

		TipoMovimentoEstoque tipoMovimentoEstoqueCota = mapaTipoMovimentoEstoque.get(GrupoMovimentoEstoque.ENVIO_ENCALHE);
		
		MovimentoEstoqueCota movimentoEstoqueCota = 
				movimentoEstoqueService.gerarMovimentoCota(
						null, 
						produtoEdicao.getId(), 
						idCota, 
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

		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(conferenciaEncalheDTO.getIdProdutoEdicao());
		
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
	 * @param dataCriacao
	 * @param numeroCota
	 * @param dataRecolhimentoReferencia
	 * @param movimentoEstoqueCota
	 * @param movimentoEstoque
	 */
	private void criarNovoRegistroConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConferenciaEncalheCota,
			ConferenciaEncalheDTO conferenciaEncalheDTO,
			Date dataCriacao,
			Integer numeroCota, 
			Date dataRecolhimentoReferencia,
			MovimentoEstoqueCota movimentoEstoqueCota,
			MovimentoEstoque movimentoEstoque) {
	
		ChamadaEncalheCota chamadaEncalheCota = 
				obterChamadaEncalheCotaParaConfEncalhe(
						numeroCota, 
						dataRecolhimentoReferencia, 
						conferenciaEncalheDTO.getIdProdutoEdicao());
		
		boolean juramentada = (conferenciaEncalheDTO.isJuramentada()) == null ? false : conferenciaEncalheDTO.isJuramentada();
		
		ConferenciaEncalhe conferenciaEncalhe = new ConferenciaEncalhe();
		
		conferenciaEncalhe.setChamadaEncalheCota(chamadaEncalheCota);
		
		conferenciaEncalhe.setControleConferenciaEncalheCota(controleConferenciaEncalheCota);
		
		conferenciaEncalhe.setMovimentoEstoqueCota(movimentoEstoqueCota);
		
		conferenciaEncalhe.setMovimentoEstoque(movimentoEstoque);
		
		conferenciaEncalhe.setJuramentada(juramentada);
		
		conferenciaEncalhe.setObservacao(conferenciaEncalheDTO.getObservacao());
		
		conferenciaEncalhe.setQtdeInformada(conferenciaEncalheDTO.getQtdInformada());
		
		conferenciaEncalhe.setQtde(conferenciaEncalheDTO.getQtdExemplar());
		
		conferenciaEncalhe.setData(dataCriacao);
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(conferenciaEncalheDTO.getIdProdutoEdicao());
		
		conferenciaEncalhe.setProdutoEdicao(produtoEdicao);
		
		conferenciaEncalheRepository.adicionar(conferenciaEncalhe);
		
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
		
		//FIXME pesquisa invalida ja que a data de recolhimento do lancamento pode ser diferende da
		// chamadaEncalheCota
		
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
	 * Obtem um registro atualizado da entidade ControleConferenciaEncalheCota.
	 * 
	 * @param ctrlConfEncalheCota
	 * @param statusOperacao
	 * @param usuario
	 * 
	 * @return ControleConferenciaEncalheCota
	 */
	private ControleConferenciaEncalheCota obterControleConferenciaEncalheCotaAtualizado(  
			ControleConferenciaEncalheCota ctrlConfEncalheCota, 
			StatusOperacao statusOperacao, 
			Usuario usuario) {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Cota cota = cotaRepository.obterPorNumerDaCota(ctrlConfEncalheCota.getCota().getNumeroCota());
		
		Date dataFinalizacao = new Date();
		
		if(ctrlConfEncalheCota.getId()!=null) { 
			
			ControleConferenciaEncalheCota controleConferenciaEncalheCotaFromBD = 			
					controleConferenciaEncalheCotaRepository.buscarPorId(ctrlConfEncalheCota.getId());
			
			controleConferenciaEncalheCotaFromBD.setStatus(statusOperacao);
			controleConferenciaEncalheCotaFromBD.setUsuario(usuario);
			controleConferenciaEncalheCotaFromBD.setDataFim(dataFinalizacao);
			
			return controleConferenciaEncalheCotaRepository.merge(controleConferenciaEncalheCotaFromBD);
			
		}
		
		ctrlConfEncalheCota.setUsuario(usuario);
		ctrlConfEncalheCota.setCota(cota);
		ctrlConfEncalheCota.setDataOperacao(distribuidor.getDataOperacao());
		ctrlConfEncalheCota.setStatus(statusOperacao);
		ctrlConfEncalheCota.setControleConferenciaEncalhe(obterControleConferenciaEncalhe(distribuidor.getDataOperacao()));
		ctrlConfEncalheCota.setDataFim(dataFinalizacao);
		
		controleConferenciaEncalheCotaRepository.adicionar(ctrlConfEncalheCota);
		
		return ctrlConfEncalheCota;
	}
	
	/**
	 * Obtém o ControleConferenciaEncalhe referente a dataOperacao atual.
	 * 
	 * @param dataOperacao
	 * 
	 * @return ControleConferenciaEncalhe
	 */
	private ControleConferenciaEncalhe obterControleConferenciaEncalhe(Date dataOperacao) {
		
		ControleConferenciaEncalhe controleConferenciaEncalhe = controleConferenciaEncalheRepository.obterControleConferenciaEncalhe(dataOperacao);
		
		if(controleConferenciaEncalhe == null) {
			
			controleConferenciaEncalhe = new ControleConferenciaEncalhe();
			
			controleConferenciaEncalhe.setData(dataOperacao);
			
			controleConferenciaEncalhe.setStatus(StatusOperacao.EM_ANDAMENTO);
			
			controleConferenciaEncalheRepository.adicionar(controleConferenciaEncalhe);
			
		}
		
		return controleConferenciaEncalhe;
		
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
	
	
	private void executarProcedimentoDivergenciaNFEntradaDeEncalhe(ControleConferenciaEncalheCota controleConferenciaEncalheCota) {
		
		//TODO: invocar workflow das diferenças.
		
		//Pagina 4 paragrafo 4
		
	}
	
   /*
	* Apos finalizar conferencia de encalhe sera verificado        
	* quais documentos serao gerados e se os mesmos serao impressos
	* ou enviados por email.                                       
	*/
	private DocumentoConferenciaEncalheDTO gerarDocumentosConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConferenciaEncalheCota, 
			String nossoNumero) {
		
		PoliticaCobranca politicaCobranca = politicaCobrancaService.obterPoliticaCobrancaPrincipal();
		
		FormaEmissao formaEmissao = politicaCobranca.getFormaEmissao();
		
		DocumentoConferenciaEncalheDTO documentoConferenciaEncalheDTO = new DocumentoConferenciaEncalheDTO();

		
		switch(formaEmissao) {
		
			case INDIVIDUAL_AGREGADA:
				//Individual - Agregada a C.E.
			break;
			
			case INDIVIDUAL_BOX:

				documentoConferenciaEncalheDTO.setDocumentoCobranca(documentoCobrancaService.gerarDocumentoCobranca(nossoNumero));
				
				documentoConferenciaEncalheDTO.setDocumentoSlip(gerarSlip(controleConferenciaEncalheCota));
				
			break;
			
			case EM_MASSA:
				//Em massa - Após geração de dívida
			break;
			
			case NAO_IMPRIME:
				//Não imprime
			break;
		
		}
		
//		boolean indEnviaEmail = politicaCobranca.getFormaCobranca().isRecebeCobrancaEmail();
		
	
		return documentoConferenciaEncalheDTO;
		
	}
	
	/**
	 * Obtém o valor devido pela cota na dataOperacao.
	 * 
	 * O valor é constituído pela valor total da chamada de encalhe e
	 * outros possíveis débitos desta cota.
	 * 
	 * @param numeroCota
	 * @param dataOperacao
	 * 
	 * @return BigDecimal
	 */
	private BigDecimal obterValorDevidoCota(Integer numeroCota, Date dataOperacao) {
		
		List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados = null;
		
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCota = 
				movimentoFinanceiroCotaRepository.
				obterDebitoCreditoSumarizadosParaCotaDataOperacao(
						numeroCota, 
						dataOperacao, 
						tiposMovimentoFinanceiroIgnorados);
		
		BigDecimal totalCredito = BigDecimal.ZERO;
		
		BigDecimal totalDebito = BigDecimal.ZERO;
		
		for(DebitoCreditoCotaDTO debitoCreditoCota : listaDebitoCreditoCota) {
			
			if(OperacaoFinaceira.CREDITO.name().equals(debitoCreditoCota.getTipoLancamento())) {
				
				totalCredito = adicionarValorBigDecimal(totalCredito, debitoCreditoCota.getValor());
				
			} else if(OperacaoFinaceira.DEBITO.name().equals(debitoCreditoCota.getTipoLancamento())) {
				
				totalDebito = adicionarValorBigDecimal(totalDebito, debitoCreditoCota.getValor());
				
			}
			
		}

		BigDecimal valorDevido = totalDebito.subtract(totalCredito);
		
		
		if(valorDevido.compareTo(BigDecimal.ZERO)>0) {
			
			return valorDevido;
			
		} else {
			
			return BigDecimal.ZERO;
			
		}
		
	}
	
	private byte[] gerarSlip(ControleConferenciaEncalheCota controleConferenciaEncalheCota) {

		Distribuidor distribuidor = distribuidorService.obter();
		Date dataOperacao = distribuidor.getDataOperacao();
		
		List<ProdutoEdicaoSlipDTO> listaProdutoEdicaoSlip = 
				conferenciaEncalheRepository.obterDadosSlipConferenciaEncalhe(
						controleConferenciaEncalheCota.getId(), 
						distribuidor.getId());
		
		Integer numeroCota 		= controleConferenciaEncalheCota.getCota().getNumeroCota();
		Long idCota				= controleConferenciaEncalheCota.getCota().getId();
		String nomeCota 		= controleConferenciaEncalheCota.getCota().getPessoa().getNome();
		Date dataConferencia 	= controleConferenciaEncalheCota.getDataOperacao();
		String codigoBox 		= controleConferenciaEncalheCota.getBox().getCodigo();
		
		
		BigDecimal qtdeTotalProdutos 	= null;
		BigDecimal valorTotalEncalhe 	= null;
		BigDecimal valorTotalPagar 		= null;
		
		for(ProdutoEdicaoSlipDTO produtoEdicaoSlip : listaProdutoEdicaoSlip) {
			
			BigDecimal reparte = obterQtdeReparteParaProdutoEdicao(
					idCota, 
					produtoEdicaoSlip.getIdProdutoEdicao(), 
					dataOperacao, 
					produtoEdicaoSlip.getDataRecolhimentoDistribuidor());
			
			produtoEdicaoSlip.setReparte(reparte);
			
			qtdeTotalProdutos = adicionarValorBigDecimal(qtdeTotalProdutos, produtoEdicaoSlip.getEncalhe());
			
			valorTotalEncalhe = adicionarValorBigDecimal(valorTotalEncalhe, produtoEdicaoSlip.getValorTotal());
			
		}
		
		SlipDTO slip = new SlipDTO();

		//TODO: pode haver produtos na operação de encalhe pertencentes
		// 		a mais de uma chamada de encalhe.
		slip.setCeJornaleiro(null);
		
		BigDecimal valorDevido	= obterValorDevidoCota(numeroCota, dataOperacao);
		
		slip.setNumeroCota(numeroCota);
		slip.setNomeCota(nomeCota);
		slip.setDataConferencia(dataConferencia);           
		slip.setCodigoBox(codigoBox);                   
		slip.setTotalProdutoDia(qtdeTotalProdutos);  
		slip.setTotalProdutos(qtdeTotalProdutos);    
		slip.setValorEncalheDia(valorTotalEncalhe);    
		slip.setValorTotalEncalhe(valorTotalEncalhe); 
		slip.setValorDevido(valorDevido);        
		slip.setValorSlip(valorDevido.subtract(valorTotalEncalhe));           
		slip.setValorTotalPagar(valorTotalPagar); 
		slip.setNumSlip(null);                 
		slip.setListaProdutoEdicaoSlipDTO(listaProdutoEdicaoSlip);

		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("NUMERO_COTA", slip.getNumeroCota());
		parameters.put("NOME_COTA", slip.getNomeCota());
		parameters.put("NUM_SLIP", slip.getNumSlip().toString());
		parameters.put("CODIGO_BOX", slip.getCodigoBox());
		parameters.put("DATA_CONFERENCIA", slip.getDataConferencia());
		parameters.put("CE_JORNALEIRO", slip.getCeJornaleiro());
		parameters.put("TOTAL_PROD_DIA", slip.getTotalProdutoDia());
		parameters.put("VALOR_ENCA_DIA", slip.getValorEncalheDia());
		parameters.put("VALOR_DEVIDO", slip.getValorDevido());
		parameters.put("VALOR_SLIP", slip.getValorSlip());
		parameters.put("TOTAL_PRODUTOS", slip.getTotalProdutos());
		parameters.put("VALOR_TOTAL_ENCA", slip.getValorTotalEncalhe() );
		parameters.put("VALOR_TOTAL_PAGAR", slip.getValorTotalPagar());
		
		
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(slip.getListaProdutoEdicaoSlipDTO());
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("/reports/slip.jasper");
		
		String path = null;
		
		try {
		
			path = url.toURI().getPath();
		
		} catch (URISyntaxException e) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Não foi possível gerar relatório Slip");
			
		}
		
		try {
		
			return  JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
		
		} catch (JRException e) {
		
			throw new ValidacaoException(TipoMensagem.ERROR, "Não foi possível gerar relatório Slip");
		
		}
		
	}
	
	/**
	 * Adiciona ao primeiro parâmetro (valorTotal) o segundo parâmetro (valorAdiciona)
	 * e retorna o resultado da soma; 
	 * 
	 * @param valorTotal
	 * @param valorAdicional
	 * 
	 * @return BigDecimal
	 */
	private BigDecimal adicionarValorBigDecimal(BigDecimal valorTotal, BigDecimal valorAdicional) {
		
		if(valorAdicional == null ) {
			return valorTotal;
		}
		
		if(valorTotal == null) {
			valorTotal = BigDecimal.ZERO;
		}
		
		return valorTotal.add(valorAdicional);
		
	}
	
	
}
