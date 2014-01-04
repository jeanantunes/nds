package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DadosDocumentacaoConfEncalheCotaDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroDistribuicaoCota;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoArquivo;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.estoque.CobrancaControleConferenciaEncalheCota;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.EstoqueProdutoCotaJuramentado;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ValoresAplicados;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Negociacao;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.fiscal.ParametroEmissaoNotaFiscal;
import br.com.abril.nds.model.fiscal.StatusEmissaoNotaFiscal;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.CobrancaControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaJuramentadoRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.FechamentoEncalheRepository;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.NegociacaoDividaRepository;
import br.com.abril.nds.repository.NotaFiscalEntradaRepository;
import br.com.abril.nds.repository.ParametroEmissaoNotaFiscalRepository;
import br.com.abril.nds.repository.ParametrosDistribuidorEmissaoDocumentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.NaturezaOperacaoRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DebitoCreditoCotaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.service.exception.ConferenciaEncalheFinalizadaException;
import br.com.abril.nds.service.exception.EncalheRecolhimentoParcialException;
import br.com.abril.nds.service.exception.EncalheSemPermissaoSalvarException;
import br.com.abril.nds.service.exception.FechamentoEncalheRealizadoException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.MathUtil;

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
	private EstoqueProdutoCotaJuramentadoRepository estoqueProdutoCotaJuramentadoRepository;
	
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
	private CotaService cotaService;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Autowired
	private NotaFiscalEntradaRepository notaFiscalEntradaRepository;
	
	@Autowired
	private ItemNotaFiscalEntradaRepository itemNotaFiscalEntradaRepository;
	
	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRepository;
	
	@Autowired
	private DocumentoCobrancaService documentoCobrancaService;
	
	@Autowired
	private ParametroEmissaoNotaFiscalRepository parametroEmissaoNotaFiscalRepository;

	@Autowired
	private NaturezaOperacaoRepository tipoNotaFiscalRepository;
	
	@Autowired
	private RecebimentoFisicoRepository recebimentoFisicoRepository;
	
	@Autowired
	private ItemRecebimentoFisicoRepository itemRecebimentoFisicoRepository;
	
	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private ParametrosDistribuidorService parametrosDistribuidorService;
	
	@Autowired
	private CobrancaControleConferenciaEncalheCotaRepository cobrancaControleConferenciaEncalheCotaRepository;
	
	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Autowired
	private NegociacaoDividaRepository negociacaoDividaRepository;
	
	@Autowired
	private FechamentoEncalheRepository fechamentoEncalheRepository;
	
	@Autowired
	private ParametrosDistribuidorEmissaoDocumentoRepository parametrosDistribuidorEmissaoDocumentoRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private DebitoCreditoCotaService debitoCreditoCotaService;
	
	@Transactional
	public boolean isCotaEmiteNfe(Integer numeroCota) {

		
		Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);

		if (cota == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Cota não encontrada.");
		}

		boolean indEmiteNfe = (cota.getParametrosCotaNotaFiscalEletronica() != null && cota
				.getParametrosCotaNotaFiscalEletronica

				().getEmiteNotaFiscalEletronica() != null) ? cota
				.getParametrosCotaNotaFiscalEletronica

				().getEmiteNotaFiscalEletronica() : false;

		return indEmiteNfe;

	}	
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#obterListaBoxEncalhe()
	 */
	@Transactional(readOnly = true)
	public List<Box> obterListaBoxEncalhe(Long idUsuario) {
	
		List<Box> listaBoxEncalhe = boxRepository.obterListaBox(TipoBox.ENCALHE);
		
		if (idUsuario == null){
			
			return listaBoxEncalhe;
		}
		
		Integer codigoBoxPadraoUsuario = this.obterBoxPadraoUsuario(idUsuario);
		
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
	private Integer obterBoxPadraoUsuario(Long idUsuario) {
		
		if (idUsuario == null){
			
			return null;
		}
		
		return this.boxRepository.obterCodigoBoxPadraoUsuario(idUsuario);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#validarFechamentoEncalheRealizado()
	 */
	@Transactional(readOnly = true)
	public void validarFechamentoEncalheRealizado() throws FechamentoEncalheRealizadoException {
	
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		boolean indFechamentoEncalhe = fechamentoEncalheRepository.buscaControleFechamentoEncalhe(dataOperacao);
		
		if(indFechamentoEncalhe) {
			throw new FechamentoEncalheRealizadoException(
					"Não é possível realizar nova conferência para data de operação [ " + DateUtil.formatarDataPTBR(dataOperacao) + "].  \n" +
					"Fechamento de encalhe já foi realizado. ");
			
		}
		
	}

	/**
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#verificarCotaComConferenciaEncalheFinalizada(java.lang.Integer)
	 */
	@Transactional(readOnly = true)
	public boolean verificarCotaComConferenciaEncalheFinalizada(Integer numeroCota) {
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				controleConferenciaEncalheCotaRepository.obterControleConferenciaEncalheCota(numeroCota, dataOperacao);
		
		if(controleConferenciaEncalheCota != null && StatusOperacao.CONCLUIDO.equals(controleConferenciaEncalheCota.getStatus())) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Obtém a quantidade de itens existentes no EstoqueProdutoEdicao
	 * da Cota de determinado ProdutoEdicao que ainda não foram devolvidos.
	 * 
	 */
	private BigInteger obterQtdItensEstoqueProdutoEdicaoDaCotaNaoDevolvidos(Long idCota, Long idProdutoEdicao) {
		
		return estoqueProdutoCotaRepository.obterTotalEmEstoqueProdutoCota(idCota, idProdutoEdicao);
		
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
	 * @param cota
	 * @param produtoEdicao
	 *  
	 * @return ChamadaEncalhe
	 */
	private ChamadaEncalheCota validarExistenciaChamadaEncalheParaCotaProdutoEdicao(Cota cota, ProdutoEdicao produtoEdicao) {

		boolean postergado = false;
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		ChamadaEncalheCota chamadaEncalheCota = null;
		
		if(produtoEdicao.isParcial()) {

			chamadaEncalheCota = 
					chamadaEncalheCotaRepository.obterChamadaEncalheCotaNaData(cota, produtoEdicao.getId(),postergado,dataOperacao);
			
			if(chamadaEncalheCota == null ) {
				
				throw new ValidacaoException(
						TipoMensagem.WARNING, 
						" Não é possível realizar a conferência do produto edição [" + produtoEdicao.getNomeComercial()  + "] da cota. " +
						" Este produto edição não possui CE. "   );
				
			}
			
		} else {

			chamadaEncalheCota =
					chamadaEncalheCotaRepository.obterUltimaChamaEncalheCota(cota, 
																			produtoEdicao.getId(), 
																			postergado,
																			dataOperacao);
			if(chamadaEncalheCota == null){
				
				throw new ValidacaoException(
						TipoMensagem.WARNING, 
						" Não é possível realizar a conferência do produto edição [" + produtoEdicao.getNomeComercial()  + "] da cota. " +
						" Este produto edição não possui CE. "   );				
			
			} else {
				
				isDataRecolhimentoValida(dataOperacao, chamadaEncalheCota.getChamadaEncalhe().getDataRecolhimento(), produtoEdicao.getId());
			
			}
			
			
			
		}
		
		return chamadaEncalheCota;
	}
	
	/**
	 * Valida se o produto edição (de acordo com sua data de recolhimento encontrada) pode ser 
	 * recolhido na data de operação informada.
	 *    
	 * @param dataOperacao
	 * @param dataRecolhimento
	 * @param idProdutoEdicao
	 */
	@Transactional(readOnly = true)
	public void isDataRecolhimentoValida(Date dataOperacao, Date dataRecolhimento, Long idProdutoEdicao) {
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		if(produtoEdicao == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto edição não encontrado");
		}
		
		String nomeProdutoEdicao = produtoEdicao.getProduto().getNome();
	
		boolean recolhimentoMaiorQueDataOperacao = dataRecolhimento.compareTo(dataOperacao) > 0;

		if(recolhimentoMaiorQueDataOperacao) {
				
			throw new ValidacaoException(
					TipoMensagem.WARNING, 
					"Não é possível realizar a conferência do produto edição [" + nomeProdutoEdicao  + "]. <br> " +
					"Não é permitida antecipação de produtos pelo distribuidor. "   );
					
		}
		
		List<Date> datasRecolhimento = 
				distribuidorService.obterDatasAposFinalizacaoPrazoRecolhimento(dataRecolhimento,
																			   this.obterIdsFornecedorDoProduto(produtoEdicao));
		if(datasRecolhimento == null || datasRecolhimento.isEmpty()){
			throw new ValidacaoException(
					TipoMensagem.WARNING, 
					" Distribuidor não possui parametrização de dias de recolhimento para o " +
					"<br> fornecedor do produto edição [" + nomeProdutoEdicao  + "]."   );		
		}
		
		for(Date item : datasRecolhimento){
			
			if(item.compareTo(dataOperacao)==0){
				return;
			}
			
		}
		
		throw new ValidacaoException(
				TipoMensagem.WARNING, 
				" Não é possível realiza a conferência do produto edição [" + nomeProdutoEdicao  + "]. <br>" +
				" Data de operação excedendo ou fora dos dias de recolhimento possíveis. "   );		

	}
	
	@Transactional
	public Long[] obterIdsFornecedorDoProduto(ProdutoEdicao produtoEdicao){
		
		Set<Fornecedor> fornecedores = produtoEdicao.getProduto().getFornecedores();
		
		Long[] idsFornecedor = new Long[fornecedores.size()];
		
		int indice = 0;
		
		for(Fornecedor item : fornecedores){
			idsFornecedor[indice++] = item.getId();
		}
		
		return idsFornecedor;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#isLancamentoParcialProdutoEdicao(java.lang.String, java.lang.Long)
	 */
	@Transactional(readOnly = true)
	public boolean isLancamentoParcialProdutoEdicao(String codigo, Long numeroEdicao) {
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigo, numeroEdicao);
		
		return produtoEdicao.isParcial();
		
	}
	
	@Transactional(readOnly = true)
	public boolean isLancamentoParcial(Long idProdutoEdicao) {
		
		return this.conferenciaEncalheRepository.isLancamentoParcial(idProdutoEdicao);
	}
	
	/**
	 * Obtém lista de conferenciaEncalhe com os produtosEdicao que fazem parte da chamaEncalhe atual para 
	 * a cota em questão ou que estejam dentro da semana de recolhimento. Caso uma operação de conferencia de 
	 * encalhe esteja sendo realizada, serão adicionados apenas produtosEdicao ainda não tenham sido adicionados
	 * a lista de conferencia de encalhe existente.
	 * 
	 * @param idControleConferenciaEncalheCota
	 * @param listaConferenciaEncalhe
	 */
	private List<ConferenciaEncalheDTO> obterListaConferenciaEncalheContingencia(
			Integer numeroCota,
			Date dataOperacao,
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe) {
		
		Set<Long> listaIdProdutoEdicao = new HashSet<Long>();
		
		if(listaConferenciaEncalhe!=null && !listaConferenciaEncalhe.isEmpty()) {
			
			for(ConferenciaEncalheDTO conferencia : listaConferenciaEncalhe) {
				
				listaIdProdutoEdicao.add(conferencia.getIdProdutoEdicao());
				
			}
			
		}
		
		Date dataRecolhimento = distribuidorService.obterDataOperacaoDistribuidor();
		
		boolean indFechado = false;
		boolean indPostergado = false;
		
		long idInicial = System.currentTimeMillis();
		idInicial = (idInicial - (1000000));
		
		
		List<ConferenciaEncalheDTO> listaConferenciaEncalheContingencia = 
			conferenciaEncalheRepository.obterListaConferenciaEncalheDTOContingencia(
				numeroCota, 
				dataRecolhimento, 
				indFechado, 
				indPostergado, 
				listaIdProdutoEdicao);
		
		for(ConferenciaEncalheDTO conferencia : listaConferenciaEncalheContingencia) {
			long id = (-1 * (idInicial++));
			conferencia.setIdConferenciaEncalhe(new Long(id));
			conferencia.setDia(obterQtdeDiaAposDataRecolhimentoDistribuidor(conferencia.getDataRecolhimento()));
		}
		
		return listaConferenciaEncalheContingencia;
	}
	
	@Transactional
	@Override
    public BigDecimal obterValorTotalDesconto(Integer numeroCota, Date dataOperacao) {
		
		BigDecimal reparte =
			chamadaEncalheCotaRepository.obterTotalDescontoDaChamaEncalheCota(
				numeroCota, dataOperacao, false, false);
		
		if (reparte == null) {
			
			reparte = BigDecimal.ZERO;
		}
		
		return reparte;
	}
	
	@Transactional
	@Override
    public BigDecimal obterValorTotalReparteSemDesconto(Integer numeroCota, Date dataOperacao) {
		
		BigDecimal reparte =
			chamadaEncalheCotaRepository.obterTotalDaChamaEncalheCotaSemDesconto(
				numeroCota, dataOperacao, false, false);
		
		if (reparte == null) {
			
			reparte = BigDecimal.ZERO;
		}
		
		return reparte;
	}
	
	@Transactional
	@Override
    public BigDecimal obterValorTotalReparte(Integer numeroCota, Date dataOperacao) {
		
		BigDecimal reparte =
			chamadaEncalheCotaRepository.obterReparteDaChamaEncalheCota(
				numeroCota, dataOperacao, false, false);
		
		if (reparte == null) {
			
			reparte = BigDecimal.ZERO;
		}
		
		return reparte;
	}
	
	@Transactional(readOnly = true)
	public boolean isCotaComReparteARecolherNaDataOperacao(Integer numeroCota) {
		
		BigDecimal valorTotal = obterValorTotalReparte(numeroCota, distribuidorService.obterDataOperacaoDistribuidor());
		
		if(BigDecimal.ZERO.compareTo(valorTotal) < 0) {
			return true;
		}
		
		return false;
		
	}

	@Transactional(readOnly = true)
	public boolean hasCotaAusenteFechamentoEncalhe(Integer numeroCota) {

		Date dataRecolhimento = distribuidorService.obterDataOperacaoDistribuidor();

		BigInteger quantidadeCotasAusentes = this.chamadaEncalheCotaRepository.quantidadeCotaAusenteFechamentoEncalhe(numeroCota, dataRecolhimento);

		return quantidadeCotasAusentes.compareTo(BigInteger.ZERO) > 0;
	}
	
	@Transactional(readOnly = true)
	public InfoConferenciaEncalheCota obterInfoConferenciaEncalheCota(Integer numeroCota, boolean indConferenciaContingencia) {
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				controleConferenciaEncalheCotaRepository.obterControleConferenciaEncalheCota(numeroCota, dataOperacao);
		
		InfoConferenciaEncalheCota infoConfereciaEncalheCota = new InfoConferenciaEncalheCota();
		
		List<ConferenciaEncalheDTO> listaConferenciaEncalheDTO = null;
		
		if(controleConferenciaEncalheCota != null) {
			
			listaConferenciaEncalheDTO = conferenciaEncalheRepository.obterListaConferenciaEncalheDTO(controleConferenciaEncalheCota.getId());
			
			for(ConferenciaEncalheDTO item : listaConferenciaEncalheDTO){
				Integer diaSemanaRecolhimento = 
						distribuidorService.obterDiaDeRecolhimentoDaData(item.getDataConferencia(),item.getDataRecolhimento() ,item.getIdProdutoEdicao());
				item.setDia(diaSemanaRecolhimento);
			}
			
			infoConfereciaEncalheCota.setListaConferenciaEncalhe(listaConferenciaEncalheDTO);
			
			infoConfereciaEncalheCota.setEncalhe(null);
			
			infoConfereciaEncalheCota.setIdControleConferenciaEncalheCota(controleConferenciaEncalheCota.getId());
			
			infoConfereciaEncalheCota.setNotaFiscalEntradaCota(controleConferenciaEncalheCota.getNotaFiscalEntradaCotaPricipal());
		} else {
			
			infoConfereciaEncalheCota.setEncalhe(BigDecimal.ZERO);
		}
		
		if(indConferenciaContingencia) {
			
			List<ConferenciaEncalheDTO> listaConferenciaEncalheContingencia = 
					obterListaConferenciaEncalheContingencia(numeroCota, dataOperacao, infoConfereciaEncalheCota.getListaConferenciaEncalhe());
			
			if(listaConferenciaEncalheDTO!=null && !listaConferenciaEncalheDTO.isEmpty()) {
				
				listaConferenciaEncalheDTO.addAll(listaConferenciaEncalheContingencia);
				
				infoConfereciaEncalheCota.setListaConferenciaEncalhe(listaConferenciaEncalheDTO);
			} else {
				
				infoConfereciaEncalheCota.setListaConferenciaEncalhe(listaConferenciaEncalheContingencia);
			}
		}
				
		Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);
				
		this.debitoCreditoCotaService.carregarDadosDebitoCreditoDaCota(infoConfereciaEncalheCota, cota, dataOperacao);
		
		infoConfereciaEncalheCota.setCota(cota);
		
		infoConfereciaEncalheCota.setReparte(obterValorTotalReparte(numeroCota, dataOperacao));
		
		// impl Erik Scaranello
		BigDecimal valorDebitoCreditoFinalizado = new BigDecimal(0);
		for(DebitoCreditoCotaDTO debitoCredito : infoConfereciaEncalheCota.getListaDebitoCreditoCota())
		{
			valorDebitoCreditoFinalizado = valorDebitoCreditoFinalizado.add(debitoCredito.getValor());
		}
		
		infoConfereciaEncalheCota.setTotalDebitoCreditoCota(valorDebitoCreditoFinalizado);
		
		infoConfereciaEncalheCota.setValorPagar(BigDecimal.ZERO);
		
		infoConfereciaEncalheCota.setValorVendaDia(BigDecimal.ZERO);
		
		infoConfereciaEncalheCota.setDistribuidorAceitaJuramentado(this.controleConferenciaEncalheCotaRepository.obterAceitaJuramentado(cota.getId()));
		
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
		
		Long idCota = cotaRepository.obterIdPorNumeroCota(numeroCota);
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		conferenciaEncalheDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
		
		BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
        conferenciaEncalheDTO.setPrecoCapa(precoVenda);
		
		BigDecimal percentualDesconto = descontoService.obterValorDescontoPorCotaProdutoEdicao(null, idCota, produtoEdicao);

		BigDecimal valorDesconto = MathUtil.calculatePercentageValue(precoVenda, percentualDesconto);

		conferenciaEncalheDTO.setDesconto(valorDesconto);
		
		conferenciaEncalheDTO.setPacotePadrao(produtoEdicao.getPacotePadrao());
		
		conferenciaEncalheDTO.setNomeProduto(produtoEdicao.getProduto().getNome());
		
		conferenciaEncalheDTO.setPossuiBrinde(produtoEdicao.isPossuiBrinde());
		
		conferenciaEncalheDTO.setNomeEditor(produtoEdicao.getProduto().getEditor().getPessoaJuridica().getNome());
		
		conferenciaEncalheDTO.setNomeEditor(produtoEdicao.getProduto().getFornecedor().getJuridica().getNome());
		
		return conferenciaEncalheDTO;
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
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		if(dataOperacao.compareTo(dataRecolhimentoDistribuidor) < 0 ) {
			return null;
		}
		
		Long qtde = DateUtil.obterDiferencaDias(dataRecolhimentoDistribuidor, dataOperacao);
		
		Integer posicaoDia = (qtde.intValue() + 1);
		
		return posicaoDia;
	}
	
	@Transactional(readOnly = true)
	public ProdutoEdicaoDTO pesquisarProdutoEdicaoPorId(Integer numeroCota, Long idProdutoEdicao) throws EncalheRecolhimentoParcialException {
		
		
		if (numeroCota == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório.");
		}
		
		if (idProdutoEdicao == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id Prdoduto Edição é obrigatório.");
		}
		
		ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		ProdutoEdicaoDTO produtoEdicaoDTO = null;
		
		if (produtoEdicao != null){
		    
			Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
			
			produtoEdicaoDTO = new ProdutoEdicaoDTO();
			
		    Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);
		    
			ChamadaEncalheCota chamadaEncalheCota = this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(cota, produtoEdicao);
			
			if( chamadaEncalheCota != null) {
				
				ChamadaEncalhe chamadaEncalhe = chamadaEncalheCota.getChamadaEncalhe();
				
				Integer dia = obterQtdeDiaAposDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
				produtoEdicaoDTO.setDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
				produtoEdicaoDTO.setTipoChamadaEncalhe(chamadaEncalhe.getTipoChamadaEncalhe());
				produtoEdicaoDTO.setDia(dia);
				produtoEdicaoDTO.setReparte(chamadaEncalheCota.getQtdePrevista());
			}
			else{
				
				atribuirDataRecolhimentoParaProdutoSemChamadaEncalhe(produtoEdicao, produtoEdicaoDTO);
			}
			
			produtoEdicaoDTO.setId(produtoEdicao.getId());
			produtoEdicaoDTO.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
			produtoEdicaoDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
            produtoEdicaoDTO.setPrecoVenda(precoVenda);
			
			carregarValoresAplicadosProdutoEdicao(produtoEdicaoDTO, numeroCota, idProdutoEdicao, dataOperacao);
			
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
			
			produtoEdicaoDTO.setNomeFornecedor(this.obterNomeFornecedor(produtoEdicao));
			produtoEdicaoDTO.setEditor(this.obterEditor(produtoEdicao));
			produtoEdicaoDTO.setChamadaCapa(produtoEdicao.getChamadaCapa());
			
			produtoEdicaoDTO.setSequenciaMatriz(
				produtoEdicaoRepository.obterCodigoMatrizPorProdutoEdicao(
					produtoEdicao.getId(), produtoEdicaoDTO.getDataRecolhimentoDistribuidor(),
						numeroCota));
		}
		
		return produtoEdicaoDTO;
	}
	
	/*
	 * Obtem a maior data de lançamnto de um produto edição
	 */
	private void atribuirDataRecolhimentoParaProdutoSemChamadaEncalhe(ProdutoEdicao produtoEdicao, ProdutoEdicaoDTO produtoEdicaoDTO) {
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		Date dataRecolhimentoDistribuidor = lancamentoRepository.obterDataUltimoLancamento(produtoEdicao.getId(), dataOperacao);
		
		if(dataRecolhimentoDistribuidor == null) {
			
			throw new ValidacaoException(
					TipoMensagem.WARNING, 
					" O produto edição [ " + produtoEdicao.getNomeComercial() + " ] não possui C.E. ou data de recolhimento prevista, " +
					" portanto não poder adicionado a conferência de encalhe."   );
			
		}
		
		produtoEdicaoDTO.setDataRecolhimentoDistribuidor(dataRecolhimentoDistribuidor);
		
		Integer dia = obterQtdeDiaAposDataRecolhimentoDistribuidor(dataRecolhimentoDistribuidor);
		
		produtoEdicaoDTO.setDia(dia);
	}
	
	@Transactional(readOnly = true)
	public ProdutoEdicaoDTO pesquisarProdutoEdicaoPorSM(Integer numeroCota, Integer sm) throws EncalheRecolhimentoParcialException {
		
		if (numeroCota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório.");
		}
		
		if (sm == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "SM é obrigatório.");
		}
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.obterProdutoEdicaoPorSequenciaMatriz(sm, dataOperacao);
		
		ProdutoEdicaoDTO produtoEdicaoDTO = null;
		
		if (produtoEdicao != null){
		    
			produtoEdicaoDTO = new ProdutoEdicaoDTO();
			
		    Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);
		    
			ChamadaEncalheCota chamadaEncalheCota = this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(cota, produtoEdicao);
			
			if( chamadaEncalheCota != null) {
				
				ChamadaEncalhe chamadaEncalhe = chamadaEncalheCota.getChamadaEncalhe();
				
				Integer dia = obterQtdeDiaAposDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
				produtoEdicaoDTO.setDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
				produtoEdicaoDTO.setTipoChamadaEncalhe(chamadaEncalhe.getTipoChamadaEncalhe());
				produtoEdicaoDTO.setDia(dia);
				produtoEdicaoDTO.setReparte(chamadaEncalheCota.getQtdePrevista());
			}
			else{
				
				atribuirDataRecolhimentoParaProdutoSemChamadaEncalhe(produtoEdicao, produtoEdicaoDTO);
			}
			
			produtoEdicaoDTO.setId(produtoEdicao.getId());
			produtoEdicaoDTO.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
			produtoEdicaoDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
            produtoEdicaoDTO.setPrecoVenda(precoVenda);
			
            carregarValoresAplicadosProdutoEdicao(produtoEdicaoDTO, numeroCota, produtoEdicao.getId(), dataOperacao);
			
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
			
			produtoEdicaoDTO.setNomeFornecedor(this.obterNomeFornecedor(produtoEdicao));
			produtoEdicaoDTO.setEditor(this.obterEditor(produtoEdicao));
			produtoEdicaoDTO.setChamadaCapa(produtoEdicao.getChamadaCapa());
			
			produtoEdicaoDTO.setSequenciaMatriz(sm);
			
		}
		
		return produtoEdicaoDTO;
	}	
	
	/**
	 * Carrega no objeto produtoEdicaoDTO os valores relativos
	 * ao desconto aplicado no produtoEdicao.
	 * 
	 * @param produtoEdicaoDTO
	 * @param numeroCota
	 * @param idProdutoEdicao
	 * @param dataOperacao
	 */
	private void carregarValoresAplicadosProdutoEdicao( ProdutoEdicaoDTO produtoEdicaoDTO, 
														Integer numeroCota, 
														Long idProdutoEdicao, 
														Date dataOperacao) {

		ValoresAplicados valoresAplicados =  movimentoEstoqueCotaRepository.obterValoresAplicadosProdutoEdicao(numeroCota, idProdutoEdicao, dataOperacao);
		
		BigDecimal precoComDesconto = produtoEdicaoDTO.getPrecoVenda();
		BigDecimal precoVenda 		= produtoEdicaoDTO.getPrecoVenda();
		
		if(valoresAplicados != null) {
		
			precoComDesconto = valoresAplicados.getPrecoComDesconto() != null ? valoresAplicados.getPrecoComDesconto() : produtoEdicaoDTO.getPrecoVenda();
			precoVenda = valoresAplicados.getPrecoVenda() != null ? valoresAplicados.getPrecoVenda() : produtoEdicaoDTO.getPrecoVenda();
		
		} 
		
		if(precoComDesconto == null) {
			precoComDesconto = BigDecimal.ZERO;
		}
		
		if(precoVenda == null) {
			precoVenda = BigDecimal.ZERO;
		}
		
		produtoEdicaoDTO.setDesconto(precoVenda.subtract(precoComDesconto));
		produtoEdicaoDTO.setPrecoComDesconto(precoComDesconto);
		produtoEdicaoDTO.setPrecoVenda(precoVenda);
	}
	
	@Transactional(readOnly = true)
	public List<ProdutoEdicaoDTO> pesquisarProdutoEdicaoPorCodigoDeBarras(Integer numeroCota, String codigoDeBarras) throws EncalheRecolhimentoParcialException {
		
		if (numeroCota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório.");
		}
		
		if (codigoDeBarras == null || codigoDeBarras.trim().isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Código de Barras é obrigatório.");
		}
		
		List<ProdutoEdicao> produtosEdicao = this.produtoEdicaoRepository.obterProdutoEdicaoPorCodigoBarra(codigoDeBarras);
		
		List<ProdutoEdicaoDTO> produtosEdicaoDTO = null;
		
		if (produtosEdicao != null && !produtosEdicao.isEmpty()) {

			produtosEdicaoDTO = new ArrayList<>();
			
			ProdutoEdicaoDTO produtoEdicaoDTO = new ProdutoEdicaoDTO();
			
		    Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);
		    
		    for (ProdutoEdicao produtoEdicao : produtosEdicao) {
		    
				ChamadaEncalheCota chamadaEncalheCota = this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(cota, produtoEdicao);
				
				if( chamadaEncalheCota != null) {
					
					ChamadaEncalhe chamadaEncalhe = chamadaEncalheCota.getChamadaEncalhe();
					
					Integer dia = obterQtdeDiaAposDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
					produtoEdicaoDTO.setDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
					produtoEdicaoDTO.setTipoChamadaEncalhe(chamadaEncalhe.getTipoChamadaEncalhe());
					produtoEdicaoDTO.setDia(dia);
					produtoEdicaoDTO.setReparte(chamadaEncalheCota.getQtdePrevista());
				}
				else{
					
					atribuirDataRecolhimentoParaProdutoSemChamadaEncalhe(produtoEdicao, produtoEdicaoDTO);
				}
				
				produtoEdicaoDTO.setId(produtoEdicao.getId());
				produtoEdicaoDTO.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
				produtoEdicaoDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
				BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
	            produtoEdicaoDTO.setPrecoVenda(precoVenda);
	
	            Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
	
				carregarValoresAplicadosProdutoEdicao(produtoEdicaoDTO, numeroCota, produtoEdicao.getId(), dataOperacao);
				
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
				
				produtoEdicaoDTO.setNomeFornecedor(this.obterNomeFornecedor(produtoEdicao));
				produtoEdicaoDTO.setEditor(this.obterEditor(produtoEdicao));
				produtoEdicaoDTO.setChamadaCapa(produtoEdicao.getChamadaCapa());
				
				produtoEdicaoDTO.setSequenciaMatriz(
					produtoEdicaoRepository.obterCodigoMatrizPorProdutoEdicao(
						produtoEdicao.getId(), produtoEdicaoDTO.getDataRecolhimentoDistribuidor(),
							numeroCota));
				
				produtosEdicaoDTO.add(produtoEdicaoDTO);
		    }
		}
		
		return produtosEdicaoDTO;
	}

	private String obterEditor(ProdutoEdicao produtoEdicao) {
		
		if(	produtoEdicao == null || 
			produtoEdicao.getProduto() == null ||
			produtoEdicao.getProduto().getEditor() == null ||
			produtoEdicao.getProduto().getEditor().getPessoaJuridica() == null ||
			produtoEdicao.getProduto().getEditor().getPessoaJuridica().getRazaoSocial() == null) {
			
			return "";
			
		}
		
		return produtoEdicao.getProduto().getEditor().getPessoaJuridica().getRazaoSocial();
	}
	
	private String obterNomeFornecedor(ProdutoEdicao produtoEdicao) {
		
		Fornecedor fornecedor = produtoEdicao.getProduto().getFornecedor();
		
		if (fornecedor != null) {
		
			return fornecedor.getJuridica().getRazaoSocial();
		}

		return null;
	}
	
	/**
	 * Retorna a dataRecolhimento referencia sendo esta igual a 
	 * dataOperacao - qtdDiasEncalheAtrasadoAceitavel(parâmetro do distribuidor). 
	 * 
	 * @return dataRecolhimentoReferencia.
	 */
	private Date obterDataRecolhimentoReferencia() {
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		int qtdDiasEncalheAtrasadoAceitavel = this.distribuidorService.qtdDiasEncalheAtrasadoAceitavel();
		
		Date dataRecolhimentoReferencia = DateUtil.subtrairDias(dataOperacao, qtdDiasEncalheAtrasadoAceitavel);
		
		return dataRecolhimentoReferencia;
	}
	
	/**
	 * Associa a Cobrança relativa a uma operação 
	 * ControleConferenciaEncalheCota.
	 */
	private void associarCobrancaConferenciaEncalheCota(Long idControleConferenciaEncalheCota, String nossoNumero) {
		
		CobrancaControleConferenciaEncalheCota cobrancaControleConferenciaEncalheCota = 
				new CobrancaControleConferenciaEncalheCota();
		
		Cobranca cobranca = cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				controleConferenciaEncalheCotaRepository.buscarPorId(idControleConferenciaEncalheCota); 
		
		cobrancaControleConferenciaEncalheCota.setCobranca(cobranca);
		
		cobrancaControleConferenciaEncalheCota.setControleConferenciaEncalheCota(controleConferenciaEncalheCota);
		
		cobrancaControleConferenciaEncalheCotaRepository.adicionar(cobrancaControleConferenciaEncalheCota);
		
	}
	
	private void removerAssociacoesCobrancaConferenciaEncalheCota(Long idControleConferenciaEncalheCota) {
		
		List<CobrancaControleConferenciaEncalheCota> listaCobrancaControleConferenciaEncalheCota = 
				cobrancaControleConferenciaEncalheCotaRepository.obterCobrancaControleConferenciaEncalheCota(idControleConferenciaEncalheCota);
		
		if(listaCobrancaControleConferenciaEncalheCota!=null && !listaCobrancaControleConferenciaEncalheCota.isEmpty()) {
			
			for(CobrancaControleConferenciaEncalheCota cobrancaControleConfEncCota :  listaCobrancaControleConferenciaEncalheCota) {
				
				cobrancaControleConferenciaEncalheCotaRepository.alterar(cobrancaControleConfEncCota);
				
			}
		}
	}
	
	/**
	 * Reseta dados financeiros na finalização da conferencia de encalhe
	 * @param controleConfEncalheCota
	 */
	private void resetarDadosFinalizacaoConferencia(ControleConferenciaEncalheCota controleConfEncalheCota){
		
		if(	controleConfEncalheCota.getId() != null) {
			
			StatusOperacao statusAtualOperacaoConfEnc = 
					controleConferenciaEncalheCotaRepository.obterStatusControleConferenciaEncalheCota(
					controleConfEncalheCota.getId());
			
			if(StatusOperacao.CONCLUIDO.equals(statusAtualOperacaoConfEnc)) {
				
				removerAssociacoesCobrancaConferenciaEncalheCota(controleConfEncalheCota.getId());
				
				resetarDadosFinanceirosConferenciaEncalheCota( 
						controleConfEncalheCota.getId(), 
						controleConfEncalheCota.getCota().getId());
			}
		} 			
	}
	
	/**
	 * Inclui dados da conferencia de encalhe da cota
	 * @param controleConfEncalheCota
	 * @param listaConferenciaEncalhe
	 * @param listaIdConferenciaEncalheParaExclusao
	 * @param usuario
	 * @param indConferenciaContingencia
	 * @return ControleConferenciaEncalheCota
	 */
	private ControleConferenciaEncalheCota incluirDadosConferenciaEncalheCota(ControleConferenciaEncalheCota controleConfEncalheCota,
			                                                                 List<ConferenciaEncalheDTO> listaConferenciaEncalhe,
			                                                                 Set<Long> listaIdConferenciaEncalheParaExclusao,
			                                                                 Usuario usuario,
			                                                                 boolean indConferenciaContingencia){
		
		controleConfEncalheCota = inserirDadosConferenciaEncalhe(controleConfEncalheCota, 
				                                                 listaConferenciaEncalhe, 
						                                         listaIdConferenciaEncalheParaExclusao, 
						                                         usuario, 
						                                         StatusOperacao.CONCLUIDO, 
						                                         indConferenciaContingencia);
		
		return controleConfEncalheCota;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#salvarDadosConferenciaEncalhe(br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota, java.util.List, java.util.Set, br.com.abril.nds.model.seguranca.Usuario)
	 */
	@Transactional
	public Long salvarDadosConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario, 
			boolean indConferenciaContingencia) throws EncalheSemPermissaoSalvarException, ConferenciaEncalheFinalizadaException {

		desfazerCobrancaConferenciaEncalheReaberta(controleConfEncalheCota.getId());
		
		//validarPermissaoSalvarConferenciaEncalhe(listaConferenciaEncalhe);
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				inserirDadosConferenciaEncalhe(controleConfEncalheCota, listaConferenciaEncalhe, listaIdConferenciaEncalheParaExclusao, usuario, StatusOperacao.EM_ANDAMENTO, indConferenciaContingencia);
		
		return controleConferenciaEncalheCota.getId();
		
	}
	
	@Transactional(rollbackFor=GerarCobrancaValidacaoException.class)
	public DadosDocumentacaoConfEncalheCotaDTO finalizarConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario,
			boolean indConferenciaContingencia, 
			BigDecimal reparte) throws GerarCobrancaValidacaoException {
		
		this.resetarDadosFinalizacaoConferencia(controleConfEncalheCota);
		
		this.incluirDadosConferenciaEncalheCota(controleConfEncalheCota, 
				                                listaConferenciaEncalhe, 
				                                listaIdConferenciaEncalheParaExclusao, 
				                                usuario, 
				                                indConferenciaContingencia);

		Cota cota = controleConfEncalheCota.getCota();       

		BigDecimal valorTotalEncalheOperacaoConferenciaEncalhe = BigDecimal.ZERO;
				//conferenciaEncalheRepository.obterValorTotalEncalheOperacaoConferenciaEncalhe(controleConfEncalheCota.getId());
		
		for (ConferenciaEncalheDTO dto : listaConferenciaEncalhe){
			
			valorTotalEncalheOperacaoConferenciaEncalhe = valorTotalEncalheOperacaoConferenciaEncalhe.add(dto.getValorTotal());
		}
		
		reparte = reparte == null ? BigDecimal.ZERO : reparte;
		
		valorTotalEncalheOperacaoConferenciaEncalhe = reparte.subtract(valorTotalEncalheOperacaoConferenciaEncalhe);
		
		this.abaterNegociacaoPorComissao(cota.getId(), valorTotalEncalheOperacaoConferenciaEncalhe, usuario);
		
		Map<String, Boolean> nossoNumeroCollection = new LinkedHashMap<String, Boolean>();
		
		DadosDocumentacaoConfEncalheCotaDTO documentoConferenciaEncalhe = new DadosDocumentacaoConfEncalheCotaDTO();
		
		try {
			
			nossoNumeroCollection = gerarCobranca(controleConfEncalheCota);
		
		} catch(GerarCobrancaValidacaoException e) {
			
			documentoConferenciaEncalhe.setMsgsGeracaoCobranca(e.getValidacaoVO());			
		}
		
		ParametroDistribuicaoCota parametroDistribuicaoCota = cota.getParametroDistribuicao();
		
		PoliticaCobranca politicaCobranca = politicaCobrancaService.obterPoliticaCobrancaPrincipal();
		
		FormaEmissao formaEmissao = politicaCobranca.getFormaEmissao();
		
		boolean isUtililzaBoletoImpressao = parametrosDistribuidorEmissaoDocumentoRepository.isUtilizaImpressao(TipoParametrosDistribuidorEmissaoDocumento.BOLETO);
		
		boolean isUtililzaSlipImpressao = parametrosDistribuidorEmissaoDocumentoRepository.isUtilizaImpressao(TipoParametrosDistribuidorEmissaoDocumento.SLIP);
		
		boolean isUtililzaBoletoSlipImpressao = parametrosDistribuidorEmissaoDocumentoRepository.isUtilizaImpressao(TipoParametrosDistribuidorEmissaoDocumento.BOLETO_SLIP);
		
		boolean isUtililzaReciboImpressao = parametrosDistribuidorEmissaoDocumentoRepository.isUtilizaImpressao(TipoParametrosDistribuidorEmissaoDocumento.RECIBO);
		
		documentoConferenciaEncalhe.setIdControleConferenciaEncalheCota(controleConfEncalheCota.getId());
		documentoConferenciaEncalhe.setIndGeraDocumentacaoConferenciaEncalhe(FormaEmissao.INDIVIDUAL_BOX.equals(formaEmissao));
		
		documentoConferenciaEncalhe.setUtilizaBoleto(
			this.getDocumentoImpressao(parametroDistribuicaoCota.getBoletoImpresso(), isUtililzaBoletoImpressao));
		
		documentoConferenciaEncalhe.setUtilizaSlip(
			this.getDocumentoImpressao(parametroDistribuicaoCota.getSlipImpresso(), isUtililzaSlipImpressao));
		
		documentoConferenciaEncalhe.setUtilizaBoletoSlip(
			this.getDocumentoImpressao(parametroDistribuicaoCota.getBoletoSlipImpresso(), isUtililzaBoletoSlipImpressao));
		
		documentoConferenciaEncalhe.setUtilizaRecibo(
			this.getDocumentoImpressao(parametroDistribuicaoCota.getReciboImpresso(), isUtililzaReciboImpressao));
		
		documentoConferenciaEncalhe.setListaNossoNumero(new LinkedHashMap<String, Boolean>());
		
		if(nossoNumeroCollection!=null && !nossoNumeroCollection.isEmpty()) {
			
			for (String nossoNumero : nossoNumeroCollection.keySet()){
				
				if(nossoNumero!=null && !nossoNumero.trim().isEmpty()) {
					
					associarCobrancaConferenciaEncalheCota(controleConfEncalheCota.getId(), nossoNumero);
				}
				
				documentoConferenciaEncalhe.getListaNossoNumero().put(nossoNumero, nossoNumeroCollection.get(nossoNumero));
			}
		}
		
		return documentoConferenciaEncalhe;
	}
	
	private boolean getDocumentoImpressao(Boolean documentoImpressaoCota,
										  Boolean documentoImpressaoDistribuidor) {
		
		if (documentoImpressaoCota != null) {
			
			return documentoImpressaoCota;
		}
		
		return (documentoImpressaoDistribuidor != null) ? documentoImpressaoDistribuidor : false; 
	}
	
	//caso haja negociação por comissão da cota será abatida aqui
	private void abaterNegociacaoPorComissao(Long idCota,
			BigDecimal valorTotalEncalheOperacaoConferenciaEncalhe,
			Usuario usuario) {
		
		//verifica se existe valor para abater das negociações
		if (valorTotalEncalheOperacaoConferenciaEncalhe != null &&
				valorTotalEncalheOperacaoConferenciaEncalhe.compareTo(BigDecimal.ZERO) > 0){
			
			//busca negociações por comissão ainda não quitadas
			List<Negociacao> negociacoes = 
					this.negociacaoDividaRepository.obterNegociacaoPorComissaoCota(idCota);
			
			if (negociacoes != null && !negociacoes.isEmpty()){
			
				Cota cota = this.cotaRepository.buscarPorId(idCota);
				
				TipoMovimentoFinanceiro tipoMovimentoFinanceiro = 
						this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
							GrupoMovimentoFinaceiro.NEGOCIACAO_COMISSAO);
				
				Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
				
				BigDecimal valorCem = new BigDecimal(100);
				
				for (Negociacao negociacao : negociacoes){
					
					//caso todo o valor da conferencia tenha sido usado para quitação das negociações
					if  (valorTotalEncalheOperacaoConferenciaEncalhe.compareTo(BigDecimal.ZERO) <= 0){
						
						valorTotalEncalheOperacaoConferenciaEncalhe = BigDecimal.ZERO;
						break;
					}
					
					BigDecimal comissao = negociacao.getComissaoParaSaldoDivida();
					
					BigDecimal valorDescontar = valorTotalEncalheOperacaoConferenciaEncalhe.multiply(comissao).divide(valorCem);
					
					valorTotalEncalheOperacaoConferenciaEncalhe = 
							valorTotalEncalheOperacaoConferenciaEncalhe.subtract(valorDescontar);
					
					BigDecimal valorRestanteNegociacao = negociacao.getValorDividaPagaComissao().subtract(valorDescontar);
					
					//se o valor resultante não quita a negociação
					if (valorRestanteNegociacao.compareTo(BigDecimal.ZERO) > 0){
						
						negociacao.setValorDividaPagaComissao(valorRestanteNegociacao);
					} else {
						
						negociacao.setValorDividaPagaComissao(BigDecimal.ZERO);
						
						//gera crédito para cota caso a comissão gere sobra na quitação
						valorTotalEncalheOperacaoConferenciaEncalhe = 
								valorTotalEncalheOperacaoConferenciaEncalhe.add(valorRestanteNegociacao.negate());
					}
					
					MovimentoFinanceiroCotaDTO movDTO = new MovimentoFinanceiroCotaDTO();
					movDTO.setAprovacaoAutomatica(true);
					movDTO.setCota(cota);
					movDTO.setDataAprovacao(dataOperacao);
					movDTO.setDataCriacao(dataOperacao);
					movDTO.setDataOperacao(dataOperacao);
					movDTO.setDataVencimento(movDTO.getDataAprovacao());
					movDTO.setObservacao("Oriundo de negociação por comissão");
					movDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
					movDTO.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiro);
					movDTO.setUsuario(usuario);
					
					if (valorRestanteNegociacao.compareTo(BigDecimal.ZERO) > 0){
						
						movDTO.setValor(valorDescontar);
					} else {
						
						movDTO.setValor(valorRestanteNegociacao);
					}
					
					MovimentoFinanceiroCota m = 
						this.movimentoFinanceiroCotaService.gerarMovimentoFinanceiroCota(movDTO, null);
					
					this.movimentoFinanceiroCotaRepository.adicionar(m);
					
					if (negociacao.getMovimentosFinanceiroCota() == null){
						
						negociacao.setMovimentosFinanceiroCota(new ArrayList<MovimentoFinanceiroCota>());
					}
					
					negociacao.getMovimentosFinanceiroCota().add(m);
					
					this.negociacaoDividaRepository.alterar(negociacao);
				}
			}
		}
	}

	/**
	 * Gera o movimento financeiro referente a operação de conferência de encalhe e
	 * em seguida dispara componentes responsáveis pela geração da cobrança.
	 * 
	 * @param controleConferenciaEncalheCota
	 * 
	 * @return Set - String
	 * @throws GerarCobrancaValidacaoException 
	 */
	private Map<String, Boolean> gerarCobranca(ControleConferenciaEncalheCota controleConferenciaEncalheCota) throws GerarCobrancaValidacaoException {
		
		Map<String, Boolean> nossoNumeroCollection = new HashMap<String, Boolean>();
		
		//COTA COM TIPO ALTERADO NA DATA DE OPERAÇÃO AINDA É TRATADA COMO CONSIGNADA ATÉ FECHAMENTO DO DIA
        boolean isAlteracaoTipoCotaNaDataAtual = this.cotaService.isCotaAlteradaNaData(controleConferenciaEncalheCota.getCota(), 
        		                                                                       controleConferenciaEncalheCota.getDataOperacao());
		
		if (controleConferenciaEncalheCota.getCota().getTipoCota().equals(TipoCota.CONSIGNADO) || isAlteracaoTipoCotaNaDataAtual){
			
			//CANCELA DIVIDA EXCLUI CONSOLIDADO E MOVIMENTOS FINANCEIROS DE REPARTE X ENCALHE (RECEBIMENTO_REPARTE E ENVIO_ENCALHE) PARA QUE SEJAM RECRIADOS
			this.gerarCobrancaService.cancelarDividaCobranca(null, 
					                                         controleConferenciaEncalheCota.getCota().getId(), 
					                                         controleConferenciaEncalheCota.getDataOperacao(), 
					                                         true);

			//CRIA MOVIMENTOS FINANCEIROS DE REPARTE X ENCALHE (RECEBIMENTO_REPARTE E ENVIO_ENCALHE)
			this.movimentoFinanceiroCotaService.gerarMovimentoFinanceiroCota(controleConferenciaEncalheCota.getCota(),
																			 controleConferenciaEncalheCota.getDataOperacao(),
																			 controleConferenciaEncalheCota.getUsuario(),
																			 controleConferenciaEncalheCota.getId());
		
			boolean existeBoletoAntecipado =  this.boletoService.existeBoletoAntecipadoCotaDataRecolhimento(controleConferenciaEncalheCota.getCota().getId(), 
					                                                                                        controleConferenciaEncalheCota.getDataOperacao());
			
			if (existeBoletoAntecipado){
				
				gerarCobrancaService.gerarDividaPostergada(controleConferenciaEncalheCota.getCota().getId(), 
												           controleConferenciaEncalheCota.getUsuario().getId());
			}
			else{
			
				gerarCobrancaService.gerarCobranca(controleConferenciaEncalheCota.getCota().getId(), 
												   controleConferenciaEncalheCota.getUsuario().getId(), 
												   nossoNumeroCollection);
			}
	    }
		else if (controleConferenciaEncalheCota.getCota().getTipoCota().equals(TipoCota.A_VISTA)){
			
			boolean isConferenciaRealizada = this.controleConferenciaEncalheCotaRepository.isConferenciaEncalheCotaFinalizada(controleConferenciaEncalheCota.getCota().getId(), 
					                                                                                                          controleConferenciaEncalheCota.getDataOperacao());
			
			if(isConferenciaRealizada){

			    //EXLUI MOVIMENTOS FINANCEIROS COTA PARA CRIÁ-LOS NOVAMENTE
			    this.movimentoFinanceiroCotaService.removerMovimentosFinanceirosCotaConferenciaNaoConsolidados(controleConferenciaEncalheCota.getCota().getNumeroCota(), 
			    		                                                                                       controleConferenciaEncalheCota.getDataOperacao());
			}
			
			//CRIA MOVIMENTOS FINANCEIROS DE REPARTE X ENCALHE (RECEBIMENTO_REPARTE E ENVIO_ENCALHE) PARA COTA A VISTA COM CONSIGNADO PENDENTE
			this.movimentoFinanceiroCotaService.gerarMovimentoFinanceiroCota(controleConferenciaEncalheCota.getCota(),
																			 controleConferenciaEncalheCota.getDataOperacao(),
																			 controleConferenciaEncalheCota.getUsuario(),
																			 controleConferenciaEncalheCota.getId());
		}
		
		return nossoNumeroCollection;
	}
	
	/**
	 * Faz o cancelamento de dados financeiros relativos a 
	 * operação de conferência de encalhe em questão.
	 * 
	 * @param controleConferenciaEncalheCota
	 */
	private void resetarDadosFinanceirosConferenciaEncalheCota(Long idControleConferenciaEncalheCota, Long idCota) {
		
		Cota cota = this.cotaRepository.buscarCotaPorID(idCota);
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		//COTA COM TIPO ALTERADO NA DATA DE OPERAÇÃO AINDA É TRATADA COMO CONSIGNADA ATÉ FECHAMENTO DO DIA
        boolean isAlteracaoTipoCotaNaDataAtual = this.cotaService.isCotaAlteradaNaData(cota,dataOperacao);
		
		if (cota.getTipoCota().equals(TipoCota.CONSIGNADO) || isAlteracaoTipoCotaNaDataAtual){
		
			List<MovimentoFinanceiroCota> movimentosFinanceiroCota = 
					movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroDaOperacaoConferenciaEncalhe(idControleConferenciaEncalheCota);
			
			if(movimentosFinanceiroCota!=null && !movimentosFinanceiroCota.isEmpty()) {

				gerarCobrancaService.cancelarDividaCobranca(null, idCota, dataOperacao, true);
			}
		}
		else if (cota.getTipoCota().equals(TipoCota.A_VISTA)){
			
			//EXLUI MOVIMENTOS FINANCEIROS COTA PARA CRIÁ-LOS NOVAMENTE
			this.movimentoFinanceiroCotaService.removerMovimentosFinanceirosCotaConferenciaNaoConsolidados(cota.getNumeroCota(), dataOperacao);
		}
	}
	
	private void removerItensConferenciaEncallhe(Set<Long> listaIdConferenciaEncalheParaExclusao) {
		
		if(listaIdConferenciaEncalheParaExclusao!=null && !listaIdConferenciaEncalheParaExclusao.isEmpty()) {
			
			for(Long idConferenciaEncalheExclusao : listaIdConferenciaEncalheParaExclusao) {
				excluirRegistroConferenciaEncalhe(idConferenciaEncalheExclusao);
			}
			
		}
		
	}
	
	/**
	 * Insere os dados da conferência de encalhe.
	 * 
	 * @param controleConfEncalheCota
	 * @param listaConferenciaEncalhe
	 * @param listaIdConferenciaEncalheParaExclusao
	 * @param usuario
	 * @param statusOperacao
	 * @param indConferenciaContingencia
	 * 
	 * @return ControleConferenciaEncalheCota
	 */
	private ControleConferenciaEncalheCota inserirDadosConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario,
			StatusOperacao statusOperacao,
			boolean indConferenciaContingencia) {
		
	    Date dataRecolhimentoReferencia = obterDataRecolhimentoReferencia();
	    
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		Date dataCriacao = new Date();
		Integer numeroCota = controleConfEncalheCota.getCota().getNumeroCota();
		
		removerItensConferenciaEncallhe(listaIdConferenciaEncalheParaExclusao);
		
		NotaFiscalEntradaCota notaFiscalEntradaCota = atualizarCabecalhoNotaFiscalEntradaCota(
				controleConfEncalheCota.getId(),
				controleConfEncalheCota.getNotaFiscalEntradaCotaPricipal(), 
				numeroCota, 
				usuario, 
				dataCriacao);
		
		atualizarItensNotaFiscalEntradaCota(
				dataOperacao,
				notaFiscalEntradaCota, 
				listaConferenciaEncalhe);
		
		List<NotaFiscalEntradaCota> notaFiscalEntradaCotas = new ArrayList<NotaFiscalEntradaCota>();
		notaFiscalEntradaCotas.add(notaFiscalEntradaCota);
		controleConfEncalheCota.setNotaFiscalEntradaCota(notaFiscalEntradaCotas);
		
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				obterControleConferenciaEncalheCotaAtualizado(controleConfEncalheCota, statusOperacao, usuario);
		
		Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque = 
				obterMapaTipoMovimentoEstoque();

		for(ConferenciaEncalheDTO conferenciaEncalheDTO : listaConferenciaEncalhe) {
			

			validarQtdeEncalheExcedeQtdeReparte(
					conferenciaEncalheDTO,
					controleConferenciaEncalheCota.getCota(), 
					dataOperacao, indConferenciaContingencia);
				
			
			
			if(conferenciaEncalheDTO.getIdConferenciaEncalhe()!=null) {

				atualizarRegistroConferenciaEncalhe(
						conferenciaEncalheDTO, 
						statusOperacao, 
						mapaTipoMovimentoEstoque,
						controleConferenciaEncalheCota,
						numeroCota,
						dataCriacao,
						dataRecolhimentoReferencia,
						usuario);
				
			} else {
				
				criarNovoRegistroConferenciaEncalhe(
						conferenciaEncalheDTO, 
						statusOperacao, 
						mapaTipoMovimentoEstoque,
						controleConferenciaEncalheCota,
						numeroCota,
						dataCriacao,
						dataRecolhimentoReferencia,
						usuario);
				
			}
			
		}

		return controleConferenciaEncalheCota;
	}
	
	/**
	 * Atualiza registro de ConferenciaEncalhe e entidades relacionadas.
	 * 
	 * @param conferenciaEncalheDTO
	 * @param statusOperacao
	 * @param mapaTipoMovimentoEstoque
	 * @param controleConferenciaEncalheCota
	 * @param numeroCota
	 * @param dataCriacao
	 * @param dataRecolhimentoReferencia
	 * @param usuario
	 */
	private void atualizarRegistroConferenciaEncalhe(
			ConferenciaEncalheDTO conferenciaEncalheDTO, 
			StatusOperacao statusOperacao, 
			Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque,
			ControleConferenciaEncalheCota controleConferenciaEncalheCota,
			Integer numeroCota,
			Date dataCriacao,
			Date dataRecolhimentoReferencia,
			Usuario usuario) {
		
		ConferenciaEncalhe conferenciaEncalheFromDB = conferenciaEncalheRepository.buscarPorId(conferenciaEncalheDTO.getIdConferenciaEncalhe());
		
		MovimentoEstoqueCota movimentoEstoqueCota = null;
		
		MovimentoEstoque movimentoEstoque = null;

		movimentoEstoqueCota = conferenciaEncalheFromDB.getMovimentoEstoqueCota();

		movimentoEstoque = conferenciaEncalheFromDB.getMovimentoEstoque();
			
		if(movimentoEstoqueCota!=null) {
		
			atualizarMovimentoEstoqueCota(movimentoEstoqueCota, conferenciaEncalheDTO);
		
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
			
		if (movimentoEstoque != null) {
		
			atualizarMovimentoEstoque(movimentoEstoque, conferenciaEncalheDTO);
			
		} else {	
		
			ChamadaEncalheCota chamadaEncalheCota = null;
			
			if (conferenciaEncalheDTO.getDataRecolhimento() != null) {
				
				chamadaEncalheCota = obterChamadaEncalheCotaParaConfEncalhe(
					numeroCota, conferenciaEncalheDTO.getDataRecolhimento(), 
						conferenciaEncalheDTO.getIdProdutoEdicao());
			}
			
			movimentoEstoque = criarNovoRegistroMovimentoEstoque(
					controleConferenciaEncalheCota, 
					conferenciaEncalheDTO, 
					numeroCota, 
					dataRecolhimentoReferencia, 
					dataCriacao, 
					mapaTipoMovimentoEstoque, 
					usuario,
					chamadaEncalheCota);
		}
		
		atualizarRegistroConferenciaEncalhe(
				conferenciaEncalheDTO, 
				conferenciaEncalheFromDB,
				movimentoEstoqueCota,
				movimentoEstoque);
		
	}
	
	/**
	 * Cria novo registro de ConferenciaEncalhe e entidades relacionadas.
	 * 
	 * @param conferenciaEncalheDTO
	 * @param statusOperacao
	 * @param mapaTipoMovimentoEstoque
	 * @param controleConferenciaEncalheCota
	 * @param numeroCota
	 * @param dataCriacao
	 * @param dataRecolhimentoReferencia
	 * @param usuario
	 */
	private void criarNovoRegistroConferenciaEncalhe(
			ConferenciaEncalheDTO conferenciaEncalheDTO, 
			StatusOperacao statusOperacao, 
			Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque,
			ControleConferenciaEncalheCota controleConferenciaEncalheCota,
			Integer numeroCota,
			Date dataCriacao,
			Date dataRecolhimentoReferencia,
			Usuario usuario){
		
		ChamadaEncalheCota chamadaEncalheCota = null;
				
		if (conferenciaEncalheDTO.getDataRecolhimento() != null) {
			
			chamadaEncalheCota = obterChamadaEncalheCotaParaConfEncalhe(
				numeroCota, conferenciaEncalheDTO.getDataRecolhimento(), 
					conferenciaEncalheDTO.getIdProdutoEdicao());
		}
		
		MovimentoEstoqueCota movimentoEstoqueCota = null;
		
		MovimentoEstoque movimentoEstoque = null;
			
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
					usuario,
					chamadaEncalheCota);
		
		criarNovoRegistroConferenciaEncalhe(
				controleConferenciaEncalheCota, 
				conferenciaEncalheDTO,
				dataCriacao,
				numeroCota, 
				movimentoEstoqueCota,
				movimentoEstoque,
				chamadaEncalheCota);
		
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
													Map<GrupoMovimentoEstoque, 
													TipoMovimentoEstoque> mapaTipoMovimentoEstoque,
													TipoChamadaEncalhe tipoChamadaEncalhe) {
		
		if (juramentada) {
			
			return mapaTipoMovimentoEstoque.get(
				GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE_JURAMENTADO);
		}
		
		if (isDataRecolhimentoDistribuidorMenorIgualDataConferenciaEncalhe(
				dataRecolhimentoDistribuidor, dataConferenciaEncalhe)
				&& TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO.equals(tipoChamadaEncalhe)) {
			
			return mapaTipoMovimentoEstoque.get(
				GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE);
			
		} else {
			
			return mapaTipoMovimentoEstoque.get(
				GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO);
			
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
		
		if (dataRecolhimentoDistribuidor == null){
			
			return false;
		}
		
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
	 * @param notaFiscalEntradaCota
	 * @param usuario
	 * @param dataCriacao
	 */
	private NotaFiscalEntradaCota atualizarCabecalhoNotaFiscalEntradaCota(Long idControleConferenciaEncalheCota, NotaFiscalEntradaCota notaFiscalEntradaCota, Integer numeroCota, Usuario usuario, Date dataCriacao) {
		
		if(notaFiscalEntradaCota == null) {
			return null;
		}
		
		if ( notaFiscalEntradaCota.getNumero() 		== null || 
			 notaFiscalEntradaCota.getSerie() 		== null || 
			 notaFiscalEntradaCota.getSerie().isEmpty()     ||
			 notaFiscalEntradaCota.getDataEmissao() == null	||
			 notaFiscalEntradaCota.getValorProdutos() == null) {
			
			return null;
			
		}			
		
		NotaFiscalEntradaCota notaFiscalEntradaCotaFromBD = null;
		
		if(idControleConferenciaEncalheCota!=null) {

			ControleConferenciaEncalheCota controleConferenciaEncalheCotaFromBD = 			
					controleConferenciaEncalheCotaRepository.buscarPorId(idControleConferenciaEncalheCota);
			
			notaFiscalEntradaCotaFromBD = controleConferenciaEncalheCotaFromBD.getNotaFiscalEntradaCotaPricipal();
			
			
		}
		
		if(notaFiscalEntradaCotaFromBD!=null) {
			
			notaFiscalEntradaCotaFromBD.setNumero(notaFiscalEntradaCota.getNumero());
			notaFiscalEntradaCotaFromBD.setSerie(notaFiscalEntradaCota.getSerie());
			notaFiscalEntradaCotaFromBD.setChaveAcesso(notaFiscalEntradaCota.getChaveAcesso());
			
			notaFiscalEntradaCotaFromBD.setDataEmissao(notaFiscalEntradaCota.getDataEmissao());
			notaFiscalEntradaCotaFromBD.setDataExpedicao(notaFiscalEntradaCota.getDataEmissao());
			
			notaFiscalEntradaCotaFromBD.setValorProdutos(notaFiscalEntradaCota.getValorProdutos());
			notaFiscalEntradaCotaFromBD.setValorDesconto(notaFiscalEntradaCota.getValorProdutos());
			notaFiscalEntradaCotaFromBD.setValorLiquido(notaFiscalEntradaCota.getValorProdutos());
			notaFiscalEntradaCotaFromBD.setValorBruto(notaFiscalEntradaCota.getValorProdutos());
			
			
			notaFiscalEntradaRepository.alterar(notaFiscalEntradaCotaFromBD);
			
		} else {

			StatusEmissaoNotaFiscal statusNF = StatusEmissaoNotaFiscal.EMITIDA;
			
			ParametroEmissaoNotaFiscal parametroEmissaoNF = parametroEmissaoNotaFiscalRepository.obterParametroEmissaoNotaFiscal(GrupoNotaFiscal.RECEBIMENTO_MERCADORIAS_ENCALHE);
			
			if(parametroEmissaoNF == null) {
				throw new IllegalStateException("Nota Fiscal Saida não parametrizada no sistema");
			}
			
			NaturezaOperacao tipoNF = tipoNotaFiscalRepository.obterTipoNotaFiscal(GrupoNotaFiscal.RECEBIMENTO_MERCADORIAS_ENCALHE);

			if(tipoNF == null) {
				throw new IllegalStateException("TipoNotaFiscal não parametrizada");
			}
			
	 		Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);
			
			CFOP cfop = parametroEmissaoNF.getCfopDentroEstado();
			
			notaFiscalEntradaCota.setCfop(cfop);
			notaFiscalEntradaCota.setStatusEmissao(statusNF);
			notaFiscalEntradaCota.setTipoNotaFiscal(tipoNF);
			notaFiscalEntradaCota.setDataEmissao(dataCriacao);
			notaFiscalEntradaCota.setDataExpedicao(dataCriacao);
			notaFiscalEntradaCota.setCota(cota);
			notaFiscalEntradaCota.setValorDesconto(BigDecimal.ZERO);
			notaFiscalEntradaCota.setValorLiquido(notaFiscalEntradaCota.getValorProdutos());
			notaFiscalEntradaCota.setValorBruto(notaFiscalEntradaCota.getValorProdutos());
			notaFiscalEntradaCota.setOrigem(Origem.MANUAL);
			notaFiscalEntradaCota.setStatusNotaFiscal(StatusNotaFiscalEntrada.RECEBIDA);
			
			notaFiscalEntradaRepository.adicionar(notaFiscalEntradaCota);
			
			inserirRecebimentoFisico(notaFiscalEntradaCota, usuario, dataCriacao);
			
		}
		
		return notaFiscalEntradaCota;
	}
	
	/**
	 * Insere registro de recebimento fisico
	 * 
	 * @param notaFiscal
	 * @param usuario
	 * @param dataCriacao
	 */
	private void inserirRecebimentoFisico(NotaFiscalEntradaCota notaFiscal, Usuario usuario, Date dataCriacao) {
		
		RecebimentoFisico recebimentoFisico = new RecebimentoFisico();
		
		recebimentoFisico.setConferente(usuario);
		recebimentoFisico.setDataConfirmacao(dataCriacao);
		recebimentoFisico.setDataRecebimento(dataCriacao);
		recebimentoFisico.setNotaFiscal(notaFiscal);
		recebimentoFisico.setRecebedor(usuario);
		recebimentoFisico.setStatusConfirmacao(StatusConfirmacao.CONFIRMADO);
		
		recebimentoFisicoRepository.adicionar(recebimentoFisico);
		
	}
	
	/**
	 * Atualiza os itens relativos a uma notaFiscalEntradaCota.
	 */
	private void atualizarItensNotaFiscalEntradaCota(
			Date dataOperacao,
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
				
				ItemRecebimentoFisico itemRecebimentoFisico = itemNFEntrada.getRecebimentoFisico();
				
				if(itemRecebimentoFisico!=null) {
					itemRecebimentoFisicoRepository.remover(itemRecebimentoFisico);
				}
				
				itemNotaFiscalEntradaRepository.remover(itemNFEntrada);
			}
			
		}
		
		if(listaConferenciaEncalhe != null && !listaConferenciaEncalhe.isEmpty()) {
		
			RecebimentoFisico recebimentoFisico = recebimentoFisicoRepository.obterRecebimentoFisicoPorNotaFiscal(idNotaFiscalEntrada);
			
			for(ConferenciaEncalheDTO conferenciaEncalhe : listaConferenciaEncalhe) {

				ProdutoEdicao produtoEdicao = new ProdutoEdicao();
				produtoEdicao.setId(conferenciaEncalhe.getIdProdutoEdicao());
				
				ItemNotaFiscalEntrada itemNotaFiscalEntrada = new ItemNotaFiscalEntrada();
				itemNotaFiscalEntrada.setNotaFiscal(notaFiscalEntradaCota);
				itemNotaFiscalEntrada.setQtde(conferenciaEncalhe.getQtdInformada());
				itemNotaFiscalEntrada.setProdutoEdicao(produtoEdicao);
				itemNotaFiscalEntrada.setTipoLancamento(TipoLancamento.LANCAMENTO);
				
				Date dataUltimoLancamento = lancamentoRepository.obterDataUltimoLancamento(
						conferenciaEncalhe.getIdProdutoEdicao(), 
						dataOperacao);
				
				itemNotaFiscalEntrada.setDataLancamento(dataUltimoLancamento);
				
				itemNotaFiscalEntrada.setDataRecolhimento(conferenciaEncalhe.getDataRecolhimento());
				
				itemNotaFiscalEntradaRepository.adicionar(itemNotaFiscalEntrada);
				
				if(recebimentoFisico!=null) {
					inserirItemRecebimentoFisico(recebimentoFisico, itemNotaFiscalEntrada, conferenciaEncalhe.getQtdInformada(), null);
				}
				
				
			}
			
		}
		
	}
	
	/**
	 * Insere registro de itemRecebimentoFisico
	 * 
	 * @param recebimentoFisico
	 * @param itemNotaFiscal
	 * @param qtdeFisico
	 * @param diferenca
	 */
	private void inserirItemRecebimentoFisico( RecebimentoFisico recebimentoFisico, 
															  ItemNotaFiscalEntrada itemNotaFiscal,
															  BigInteger qtdeFisico,
															  Diferenca diferenca) {
		
		ItemRecebimentoFisico itemRecebimentoFisico = new ItemRecebimentoFisico();
		
		itemRecebimentoFisico.setDiferenca(diferenca);
		itemRecebimentoFisico.setItemNotaFiscal(itemNotaFiscal);
		itemRecebimentoFisico.setQtdeFisico(qtdeFisico);
		itemRecebimentoFisico.setRecebimentoFisico(recebimentoFisico);
		
		itemRecebimentoFisicoRepository.adicionar(itemRecebimentoFisico);
	}
	
	/**
	 * Se uma conferência de encalhe ja foi finalizada e depois reaberta, a mesma
	 * terá que cancelar tudo o que for referente a cobrança da mesma
	 * 
	 * @param idControleConferenciaEncalheCota
	 * @param conferenciaReaberta
	 * @throws ConferenciaEncalheFinalizadaException
	 */
	private void desfazerCobrancaConferenciaEncalheReaberta(Long idControleConferenciaEncalheCota) {
		
		if(idControleConferenciaEncalheCota == null) {
			return;
		}
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				controleConferenciaEncalheCotaRepository.buscarPorId(idControleConferenciaEncalheCota);
		
        Cota cota = controleConferenciaEncalheCota.getCota();
        
        Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
        
        //COTA COM TIPO ALTERADO NA DATA DE OPERAÇÃO AINDA É TRATADA COMO CONSIGNADA ATÉ FECHAMENTO DO DIA
        boolean isAlteracaoTipoCotaNaDataAtual = this.cotaService.isCotaAlteradaNaData(cota, dataOperacao);
		
		if (cota.getTipoCota().equals(TipoCota.CONSIGNADO) || (isAlteracaoTipoCotaNaDataAtual)){
		
			if(StatusOperacao.CONCLUIDO.equals(controleConferenciaEncalheCota.getStatus())){

				this.gerarCobrancaService.cancelarDividaCobranca(null, 
						                                         cota.getId(), 
						                                         dataOperacao, 
						                                         true);
				
			}
		}	
		else if (cota.getTipoCota().equals(TipoCota.A_VISTA)){

			//EXLUI MOVIMENTOS FINANCEIROS COTA PARA CRIÁ-LOS NOVAMENTE
			this.movimentoFinanceiroCotaService.removerMovimentosFinanceirosCotaConferenciaNaoConsolidados(cota.getNumeroCota(), dataOperacao);	
		}
	}
	
	/**
	 * Valida se a quantidade da conferência de encalhe não excede o reparte 
	 * de um produtoEdicao para determinada cota.
	 * 
	 * @param conferenciaEncalhe
	 * @param cota
	 * @param dataOperacao
	 * @param indConferenciaContingencia
	 */
	@Transactional(readOnly=true)
	public void validarQtdeEncalheExcedeQtdeReparte(
			ConferenciaEncalheDTO conferenciaEncalhe,
			Cota cota, 
			Date dataOperacao, boolean indConferenciaContingencia) {

		if (!indConferenciaContingencia &&
				(conferenciaEncalhe.getQtdExemplar() == null || conferenciaEncalhe.getQtdExemplar().compareTo(BigInteger.ZERO) <= 0)){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Quantidade de itens conferidos no encalhe deve ser maior que zero.");
			
		}
		
		if(dataOperacao == null) {
			dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		}

		boolean indNovoRegistroConfEncalheCota = conferenciaEncalhe.getIdConferenciaEncalhe() == null || 
				(conferenciaEncalhe.getIdConferenciaEncalhe() < 0);

		BigInteger qtdItensEstoqueProdutoEdicaoDaCotaNaoDevolvidos = obterQtdItensEstoqueProdutoEdicaoDaCotaNaoDevolvidos(cota.getId(), conferenciaEncalhe.getIdProdutoEdicao());
		
		if(indNovoRegistroConfEncalheCota) {

			BigInteger qtdeNew = conferenciaEncalhe.getQtdExemplar();
			
			if(qtdeNew.compareTo(qtdItensEstoqueProdutoEdicaoDaCotaNaoDevolvidos) > 0) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Conferência de encalhe está excedendo quantidade de reparte.");
			}
			
		} else {
			
			BigInteger qtdeOld = conferenciaEncalheRepository.obterQtdeEncalhe(conferenciaEncalhe.getIdConferenciaEncalhe());
			BigInteger qtdeNew = conferenciaEncalhe.getQtdExemplar();
			
			if(qtdeNew.compareTo( qtdeOld.add(qtdItensEstoqueProdutoEdicaoDaCotaNaoDevolvidos) ) > 0) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Conferência de encalhe está excedendo quantidade de reparte.");
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

		MovimentoEstoque movimentoEstoque = conferenciaEncalhe.getMovimentoEstoque();

		conferenciaEncalheRepository.remover(conferenciaEncalhe);
		
		if(movimentoEstoqueCota!=null){
			excluirRegistroMovimentoEstoqueCota(movimentoEstoqueCota);
		}
		
		if(movimentoEstoque!=null){
			excluirRegistroMovimentoEstoque(movimentoEstoque);
		}
		
	}
	
	/**
	 * Exclui registro de MovimentoEstoqueCota alterando consequentemente
	 * o EstoqueProdutoCota relativo.
	 * 
	 * @param movimentoEstoqueCota
	 */
	private void excluirRegistroMovimentoEstoqueCota(MovimentoEstoqueCota movimentoEstoqueCota) {
		
		if(movimentoEstoqueCota.getQtde() != null && (movimentoEstoqueCota.getQtde().compareTo(BigInteger.ZERO) != 0)) {

			EstoqueProdutoCota estoqueProdutoCota = movimentoEstoqueCota.getEstoqueProdutoCota();
			
			if(estoqueProdutoCota != null) {

				BigInteger qtdeDevolvidaOriginal = estoqueProdutoCota.getQtdeDevolvida() == null ? BigInteger.ZERO : estoqueProdutoCota.getQtdeDevolvida();
				
				estoqueProdutoCota.setQtdeDevolvida(qtdeDevolvidaOriginal.subtract(movimentoEstoqueCota.getQtde()));

				this.estoqueProdutoCotaRepository.alterar(estoqueProdutoCota);
			}
			
		}
		
		this.movimentoEstoqueCotaRepository.remover(movimentoEstoqueCota);
	}

	/**
	 * Exclui um registro de MovimentoEstoque alterando consequentemente
	 * o EstoqueProduto relativo.
	 * 
	 * @param movimentoEstoque
	 */
	private void excluirRegistroMovimentoEstoque(MovimentoEstoque movimentoEstoque) {
		
		if(movimentoEstoque.getQtde() != null && (movimentoEstoque.getQtde().compareTo(BigInteger.ZERO) != 0)) {

			GrupoMovimentoEstoque grupoMovimentoEstoqueExcluido = ((TipoMovimentoEstoque)movimentoEstoque.getTipoMovimento()).getGrupoMovimentoEstoque();
			
			EstoqueProduto estoqueProduto = movimentoEstoque.getEstoqueProduto();
			
			if(estoqueProduto != null) {
				
				if(GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO.equals(grupoMovimentoEstoqueExcluido)) {

					BigInteger qtdeOriginal = estoqueProduto.getQtdeSuplementar() == null ? BigInteger.ZERO : estoqueProduto.getQtdeSuplementar();
					
					estoqueProduto.setQtdeSuplementar(qtdeOriginal.subtract(movimentoEstoque.getQtde()));

					estoqueProdutoRepository.alterar(estoqueProduto);

					
				} else {
					
					BigInteger qtdeOriginal = estoqueProduto.getQtdeDevolucaoEncalhe() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoEncalhe();
					
					estoqueProduto.setQtdeDevolucaoEncalhe(qtdeOriginal.subtract(movimentoEstoque.getQtde()));

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
		
		boolean juramentada = (conferenciaEncalheDTO.getJuramentada()) == null ? false : conferenciaEncalheDTO.getJuramentada();
		
		conferenciaEncalheFromDB.setObservacao(conferenciaEncalheDTO.getObservacao());
		conferenciaEncalheFromDB.setJuramentada(juramentada);
		conferenciaEncalheFromDB.setQtdeInformada(conferenciaEncalheDTO.getQtdInformada());
		conferenciaEncalheFromDB.setPrecoCapaInformado(conferenciaEncalheDTO.getPrecoCapaInformado());
		conferenciaEncalheFromDB.setMovimentoEstoqueCota(movimentoEstoqueCota);
		conferenciaEncalheFromDB.setMovimentoEstoque(movimentoEstoque);
		conferenciaEncalheFromDB.setQtde(conferenciaEncalheDTO.getQtdExemplar());
		
		conferenciaEncalheRepository.alterar(conferenciaEncalheFromDB);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#obterTipoContabilizacaoCE()
	 */
	@Transactional
	public TipoContabilizacaoCE obterTipoContabilizacaoCE() {
		
		return this.distribuidorService.tipoContabilizacaoCE();
		
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
		
		ProdutoEdicao produtoEdicao = 
				this.produtoEdicaoRepository.buscarPorId(conferenciaEncalheDTO.getIdProdutoEdicao());
		
		TipoMovimentoEstoque tipoMovimentoEstoqueCota = mapaTipoMovimentoEstoque.get(GrupoMovimentoEstoque.ENVIO_ENCALHE);
		
		boolean juramentada = (conferenciaEncalheDTO.getJuramentada()) == null ? false : conferenciaEncalheDTO.getJuramentada();
		
		tipoMovimentoEstoqueCota.setIncideJuramentado(juramentada);
		
		MovimentoEstoqueCota movimentoEstoqueCota = 
				movimentoEstoqueService.gerarMovimentoCota(
						null, 
						produtoEdicao.getId(), 
						idCota, 
						usuario.getId(), 
						conferenciaEncalheDTO.getQtdExemplar(), 
						tipoMovimentoEstoqueCota,
						this.distribuidorService.obterDataOperacaoDistribuidor());
		
		ValoresAplicados valoresAplicados =  movimentoEstoqueCotaRepository.obterValoresAplicadosProdutoEdicao(numeroCota, produtoEdicao.getId(), distribuidorService.obterDataOperacaoDistribuidor());
		if(valoresAplicados == null){
			valoresAplicados = new ValoresAplicados(BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO);
		}else{
			verificarValorAplicadoNulo(valoresAplicados);
		}
		
		movimentoEstoqueCota.setValoresAplicados(valoresAplicados);
		
		return movimentoEstoqueCota;
	}
	
	private void verificarValorAplicadoNulo(ValoresAplicados valoresAplicados){
		
		if (valoresAplicados == null) {
			valoresAplicados = new ValoresAplicados();
		}
		
		if(valoresAplicados.getPrecoComDesconto() == null) {
			valoresAplicados.setPrecoComDesconto(BigDecimal.ZERO);
		}
		
		if(valoresAplicados.getPrecoVenda() == null) {
			valoresAplicados.setPrecoVenda(BigDecimal.ZERO);
		}

		if(valoresAplicados.getValorDesconto() == null) {
			valoresAplicados.setValorDesconto(BigDecimal.ZERO);
		}
		
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
									Map<GrupoMovimentoEstoque, 
									TipoMovimentoEstoque> mapaTipoMovimentoEstoque,
									Usuario usuario,
									ChamadaEncalheCota chamadaEncalheCota) {
		
		boolean juramentada = 
			(conferenciaEncalheDTO.getJuramentada()) == null 
				? false : conferenciaEncalheDTO.getJuramentada();
		
		ChamadaEncalhe chamadaEncalhe = 
			(chamadaEncalheCota != null) ?
				chamadaEncalheCota.getChamadaEncalhe() : null;
				
		TipoChamadaEncalhe tipoChamadaEncalhe = 
			(chamadaEncalhe != null) ? chamadaEncalhe.getTipoChamadaEncalhe() : null;
			
		Date dataConferenciaEncalhe = this.distribuidorService.obterDataOperacaoDistribuidor();

		TipoMovimentoEstoque tipoMovimentoEstoque = 
			obterTipoMovimentoEstoqueDistribuidor(
				juramentada, conferenciaEncalheDTO.getDataRecolhimento(),
				dataConferenciaEncalhe, mapaTipoMovimentoEstoque, tipoChamadaEncalhe);

		MovimentoEstoque movimentoEstoque = 
			this.movimentoEstoqueService.gerarMovimentoEstoque(
					conferenciaEncalheDTO.getIdProdutoEdicao(), usuario.getId(), 
					conferenciaEncalheDTO.getQtdExemplar(), tipoMovimentoEstoque);
		
		return movimentoEstoque;
	}
	
	/**
	 * Cria um novo registro de ConferenciaEncalhe.
	 * 
	 * @param controleConferenciaEncalheCota
	 * @param conferenciaEncalheDTO
	 * @param dataCriacao
	 * @param numeroCota
	 * @param movimentoEstoqueCota
	 * @param movimentoEstoque
	 */
	private void criarNovoRegistroConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConferenciaEncalheCota,
			ConferenciaEncalheDTO conferenciaEncalheDTO,
			Date dataCriacao,
			Integer numeroCota, 
			MovimentoEstoqueCota movimentoEstoqueCota,
			MovimentoEstoque movimentoEstoque,
			ChamadaEncalheCota chamadaEncalheCota) {
		
		boolean juramentada = (conferenciaEncalheDTO.getJuramentada()) == null ? false : conferenciaEncalheDTO.getJuramentada();
		
		ConferenciaEncalhe conferenciaEncalhe = new ConferenciaEncalhe();

		conferenciaEncalhe.setChamadaEncalheCota(chamadaEncalheCota);
		
		conferenciaEncalhe.setControleConferenciaEncalheCota(controleConferenciaEncalheCota);
		
		conferenciaEncalhe.setMovimentoEstoqueCota(movimentoEstoqueCota);
		
		conferenciaEncalhe.setMovimentoEstoque(movimentoEstoque);
		
		conferenciaEncalhe.setJuramentada(juramentada);
		
		conferenciaEncalhe.setObservacao(conferenciaEncalheDTO.getObservacao());
		
		conferenciaEncalhe.setQtdeInformada(conferenciaEncalheDTO.getQtdInformada());
		
		conferenciaEncalhe.setPrecoCapaInformado(conferenciaEncalheDTO.getPrecoCapaInformado());
		
		conferenciaEncalhe.setQtde(conferenciaEncalheDTO.getQtdExemplar());
		
		conferenciaEncalhe.setData(dataCriacao);
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		
		produtoEdicao.setId(conferenciaEncalheDTO.getIdProdutoEdicao());
		
		conferenciaEncalhe.setProdutoEdicao(produtoEdicao);
		
		conferenciaEncalheRepository.adicionar(conferenciaEncalhe);
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
		
		boolean postergado = false;
		
		Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);
		
		ChamadaEncalheCota chamadaEncalheCota = 
				chamadaEncalheCotaRepository.obterChamadaEncalheCotaNaData(cota, idProdutoEdicao, postergado, dataRecolhimentoReferencia);
		
		return chamadaEncalheCota;
		
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
		
		Date dataOperacaoDistribuidor = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		Cota cota = cotaRepository.obterPorNumerDaCota(ctrlConfEncalheCota.getCota().getNumeroCota());
			
		Date dataFinalizacao = new Date();
		
		if(ctrlConfEncalheCota.getId()!=null) { 
			
			ControleConferenciaEncalheCota controleConferenciaEncalheCotaFromBD = 			
					controleConferenciaEncalheCotaRepository.buscarPorId(ctrlConfEncalheCota.getId());
			
			controleConferenciaEncalheCotaFromBD.setStatus(statusOperacao);
			controleConferenciaEncalheCotaFromBD.setUsuario(usuario);
			
			controleConferenciaEncalheCotaFromBD.setDataFim(dataFinalizacao);
			
			controleConferenciaEncalheCotaFromBD.setConferenciasEncalhe(null);
			
			return controleConferenciaEncalheCotaRepository.merge(controleConferenciaEncalheCotaFromBD);
			
		} else {

			ctrlConfEncalheCota.setUsuario(usuario);
			ctrlConfEncalheCota.setCota(cota);
			ctrlConfEncalheCota.setDataOperacao(dataOperacaoDistribuidor);
			ctrlConfEncalheCota.setStatus(statusOperacao);

			//Método não pode haver concorrência
			synchronized (this) {
				ctrlConfEncalheCota.setControleConferenciaEncalhe(parametrosDistribuidorService.obterControleConferenciaEncalhe(dataOperacaoDistribuidor));
			}
			
			ctrlConfEncalheCota.setDataFim(dataFinalizacao);
			
			controleConferenciaEncalheCotaRepository.adicionar(ctrlConfEncalheCota);
			
			return ctrlConfEncalheCota;
		}
	}
	
	/**
	 * Atualiza o registro de MovimentoEstoqueCota assim como o 
	 * EstoqueProdutoCota relativo ao mesmo.
	 * 
	 * @param movimentoEstoqueCota
	 * @param conferenciaEncalheDTO
	 */
	private void atualizarMovimentoEstoqueCota(MovimentoEstoqueCota movimentoEstoqueCota, 
											   ConferenciaEncalheDTO conferenciaEncalheDTO) {
		
		
		
		ValoresAplicados valoresAplicados =  movimentoEstoqueCotaRepository.
				obterValoresAplicadosProdutoEdicao(
						movimentoEstoqueCota.getCota().getNumeroCota(), 
						movimentoEstoqueCota.getProdutoEdicao().getId(), 
						distribuidorService.obterDataOperacaoDistribuidor());

		if(valoresAplicados == null){
			valoresAplicados = new ValoresAplicados(BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO);
		}else{
			verificarValorAplicadoNulo(valoresAplicados);
		}
		
		movimentoEstoqueCota.setValoresAplicados(valoresAplicados);
		
		BigInteger oldQtdeMovEstoqueCota = movimentoEstoqueCota.getQtde();
		
		BigInteger newQtdeMovEstoquecota = conferenciaEncalheDTO.getQtdExemplar();
		
		movimentoEstoqueCota.setQtde(newQtdeMovEstoquecota);
		
		this.movimentoEstoqueCotaRepository.alterar(movimentoEstoqueCota);

		GrupoMovimentoEstoque grupoMovimentoEstoque = 
			((TipoMovimentoEstoque) movimentoEstoqueCota.getTipoMovimento())
				.getGrupoMovimentoEstoque();
		
		if (GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE_JURAMENTADO.equals(grupoMovimentoEstoque)) {
			
			EstoqueProdutoCotaJuramentado estoqueProdutoCotaJuramentado =
				movimentoEstoqueCota.getEstoqueProdutoCotaJuramentado();
			
			if(estoqueProdutoCotaJuramentado == null) {
				
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Produto ["
								+ movimentoEstoqueCota.getProdutoEdicao().getProduto().getCodigo() + " - "
								+ movimentoEstoqueCota.getProdutoEdicao().getProduto().getNomeComercial()
								+ " - " + movimentoEstoqueCota.getProdutoEdicao().getNumeroEdicao()
								+ "] não possui registro de estoque da cota.");
				
			}
			
			BigInteger qtdDevolvida = 
				estoqueProdutoCotaJuramentado.getQtde() != null ? 
					estoqueProdutoCotaJuramentado.getQtde() : BigInteger.ZERO;
					
			qtdDevolvida = 
				qtdDevolvida.subtract(oldQtdeMovEstoqueCota).add(newQtdeMovEstoquecota);
						
			estoqueProdutoCotaJuramentado.setQtde(qtdDevolvida);
			
			validarAlteracaoEstoqueProdutoCota(estoqueProdutoCotaJuramentado.getQtde(), movimentoEstoqueCota.getProdutoEdicao());
			
			this.estoqueProdutoCotaJuramentadoRepository.alterar(estoqueProdutoCotaJuramentado);
			
		} else {
			
			EstoqueProdutoCota estoqueProdutoCota =  
				movimentoEstoqueCota.getEstoqueProdutoCota();

			
			if(estoqueProdutoCota == null) {
				
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Produto ["
								+ movimentoEstoqueCota.getProdutoEdicao().getProduto().getCodigo() + " - "
								+ movimentoEstoqueCota.getProdutoEdicao().getProduto().getNomeComercial()
								+ " - " + movimentoEstoqueCota.getProdutoEdicao().getNumeroEdicao()
								+ "] não possui registro de estoque da cota.");
				
			}
			
			BigInteger qtdDevolvida = 
				estoqueProdutoCota.getQtdeDevolvida() != null ? 
					estoqueProdutoCota.getQtdeDevolvida() : BigInteger.ZERO;
					
			qtdDevolvida = 
				qtdDevolvida.subtract(oldQtdeMovEstoqueCota).add(newQtdeMovEstoquecota);
						
			estoqueProdutoCota.setQtdeDevolvida(qtdDevolvida);
			
			validarAlteracaoEstoqueProdutoCota(estoqueProdutoCota.getQtdeDevolvida(), movimentoEstoqueCota.getProdutoEdicao());
			
			this.estoqueProdutoCotaRepository.alterar(estoqueProdutoCota);
		}
	}
	
	/**
	 * Atualiza registro de MovimentoEstoque bem como o registro 
	 * de EstoqueProduto relacionado.
	 * 
	 * @param movimentoEstoque
	 * @param conferenciaEncalheDTO
	 */
	private void atualizarMovimentoEstoque(MovimentoEstoque movimentoEstoque, 
										   ConferenciaEncalheDTO conferenciaEncalheDTO) {
		
		BigInteger oldQtdeMovEstoque = movimentoEstoque.getQtde();
		
		BigInteger newQtdeMovEstoque = conferenciaEncalheDTO.getQtdExemplar();
		
		movimentoEstoque.setQtde(newQtdeMovEstoque);
		
		GrupoMovimentoEstoque grupoMovimentoEstoque = ((TipoMovimentoEstoque) movimentoEstoque.getTipoMovimento()).getGrupoMovimentoEstoque();
		
		this.movimentoEstoqueRepository.alterar(movimentoEstoque);
		
		EstoqueProduto estoqueProduto =  movimentoEstoque.getEstoqueProduto();
		
		if (estoqueProduto != null) {
			
			if (GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO.equals(grupoMovimentoEstoque)) {

				BigInteger qtdeOriginal = estoqueProduto.getQtdeSuplementar() == null ? BigInteger.ZERO : estoqueProduto.getQtdeSuplementar();
				
				estoqueProduto.setQtdeSuplementar(qtdeOriginal.subtract(oldQtdeMovEstoque));

				estoqueProduto.setQtdeSuplementar(estoqueProduto.getQtdeSuplementar().add(newQtdeMovEstoque));
				
				validarAlteracaoEstoqueProdutoDistribuidor(
						estoqueProduto.getQtdeSuplementar(), TipoEstoque.SUPLEMENTAR, estoqueProduto.getProdutoEdicao());
				
				this.estoqueProdutoRepository.alterar(estoqueProduto);

			} else {
				
				BigInteger qtdeEncalhe = estoqueProduto.getQtdeDevolucaoEncalhe() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoEncalhe();
				
				estoqueProduto.setQtdeDevolucaoEncalhe(qtdeEncalhe.subtract(oldQtdeMovEstoque));

				estoqueProduto.setQtdeDevolucaoEncalhe(estoqueProduto.getQtdeDevolucaoEncalhe().add(newQtdeMovEstoque));
				
				validarAlteracaoEstoqueProdutoDistribuidor(
						estoqueProduto.getQtdeDevolucaoEncalhe(), TipoEstoque.DEVOLUCAO_ENCALHE, estoqueProduto.getProdutoEdicao());
				
				this.estoqueProdutoRepository.alterar(estoqueProduto);			
			}
			
		} else {
			
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Produto ["
							+ movimentoEstoque.getProdutoEdicao().getProduto().getCodigo() + " - "
							+ movimentoEstoque.getProdutoEdicao().getProduto().getNomeComercial()
							+ " - " + movimentoEstoque.getProdutoEdicao().getNumeroEdicao()
							+ "] não possui registro de estoque do distribuidor.");
			
		}
		
	}

	private void validarAlteracaoEstoqueProdutoDistribuidor(
			BigInteger saldoEstoque, TipoEstoque tipoEstoque,
			ProdutoEdicao produtoEdicao) {

		if (!this.validarSaldoEstoque(saldoEstoque)) {

			throw new ValidacaoException(TipoMensagem.WARNING,
					"Saldo do produto ["
							+ produtoEdicao.getProduto().getCodigo() + " - "
							+ produtoEdicao.getProduto().getNomeComercial()
							+ " - " + produtoEdicao.getNumeroEdicao()
							+ "] no estoque \"" + tipoEstoque.getDescricao()
							+ "\", insuficiente para movimentação.");
		}
	}

	private void validarAlteracaoEstoqueProdutoCota(BigInteger saldoEstoque,
			ProdutoEdicao produtoEdicao) {

		if (!this.validarSaldoEstoque(saldoEstoque)) {

			throw new ValidacaoException(
					TipoMensagem.WARNING,
					"Saldo do produto ["
							+ produtoEdicao.getProduto().getCodigo()
							+ " - "
							+ produtoEdicao.getProduto().getNomeComercial()
							+ " - "
							+ produtoEdicao.getNumeroEdicao()
							+ "] no estoque da cota, insuficiente para movimentação.");
		}
	}

	private boolean validarSaldoEstoque(BigInteger saldoEstoque) {

		return (saldoEstoque != null && saldoEstoque.compareTo(BigInteger.ZERO) >= 0);
	}
	
	/**
	 * Apos finalizar conferencia de encalhe sera verificado        
	 * quais documentos serao gerados e se os mesmos serao impressos
	 * ou enviados por email.
	 *                                        
	 * @param dadosDocumentacaoConfEncalheCotaDTO
	 * @param documentoConferenciaEncalhe
	 * 
	 * @return byte[]
	 */
	@Transactional
	public byte[] gerarDocumentosConferenciaEncalhe(			
			Long idControleConferenciaEncalheCota,
			String nossoNumero,
			br.com.abril.nds.enums.TipoDocumentoConferenciaEncalhe tipoDocumentoConferenciaEncalhe,
			boolean geraNovoNumeroSlip
			) {
		
		Cobranca cobranca = null;
		
		TipoCobranca tipoCobranca = null;
		
		if (nossoNumero != null) {
			
			cobranca = this.cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
			
			if (cobranca != null) {
			
				tipoCobranca = cobranca.getTipoCobranca();
			}
		}
		
		switch(tipoDocumentoConferenciaEncalhe) {
		
			case SLIP_PDF :
				
				return this.documentoCobrancaService.gerarSlipCobranca(idControleConferenciaEncalheCota, geraNovoNumeroSlip, TipoArquivo.PDF);
			
			case BOLETO:
				
				if (tipoCobranca != null && tipoCobranca.equals(TipoCobranca.BOLETO)) {
				
					return documentoCobrancaService.gerarDocumentoCobranca(nossoNumero);
					
				} else {
					
					return null;
				}
				
			case RECIBO:
				
				if (tipoCobranca != null && !tipoCobranca.equals(TipoCobranca.BOLETO)) {
					
					return documentoCobrancaService.gerarDocumentoCobranca(nossoNumero);
					
				} else {
					
					return null;
				}
				
			case SLIP_TXT:
				
				return this.documentoCobrancaService.gerarSlipCobrancaMatricial(idControleConferenciaEncalheCota, geraNovoNumeroSlip);
				
			default:
				
				return null;
				
		}	
	}
	
	public enum TipoDocumentoConferenciaEncalhe {
		
		SLIP, 
		BOLETO_OU_RECIBO;
	}	
	
	/**
	 * Obtém o valor total de débito ou credito de uma cota na dataOperacao.	
	 * 
	 * @param numeroCota
	 * @param dataOperacao
	 * 
	 * @return BigDecimal
	 */
	@Transactional(readOnly = true)
	public BigDecimal obterValorTotalDebitoCreditoCota(Integer numeroCota, Date dataOperacao) {
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroEnvioEncalhe = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroRecebimentoReparte = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE);
		
		List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados = new ArrayList<TipoMovimentoFinanceiro>();
		
		tiposMovimentoFinanceiroIgnorados.add(tipoMovimentoFinanceiroEnvioEncalhe);
		tiposMovimentoFinanceiroIgnorados.add(tipoMovimentoFinanceiroRecebimentoReparte);
		
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCota = 
				movimentoFinanceiroCotaRepository.
				obterDebitoCreditoSumarizadosParaCotaDataOperacao(
						numeroCota, 
						dataOperacao, 
						tiposMovimentoFinanceiroIgnorados);
		
		BigDecimal totalDebitoCredito = BigDecimal.ZERO;
		
		for(DebitoCreditoCotaDTO debitoCreditoCota : listaDebitoCreditoCota) {
			
			if(debitoCreditoCota.getValor() == null) {
				continue;
			}
			
			if(OperacaoFinaceira.CREDITO.name().equals(debitoCreditoCota.getTipoLancamento())) {
				
				totalDebitoCredito = totalDebitoCredito.add(debitoCreditoCota.getValor());
				
			} else if(OperacaoFinaceira.DEBITO.name().equals(debitoCreditoCota.getTipoLancamento())) {
				
				totalDebitoCredito = totalDebitoCredito.subtract(debitoCreditoCota.getValor());
				
			}
			
		}
		
		return totalDebitoCredito;
	}

	/**
	 * Obtém lista de debito crédito relativa a cobrança 
	 * relacionada com uma operação de encalhe.
	 * 
	 * @param controleConferenciaEncalheCota
	 * 
	 * @return List - ComposicaoCobrancaSlipDTO
	 */
	@Transactional(readOnly=true)
	public List<DebitoCreditoCotaDTO> obterDebitoCreditoDeCobrancaPorOperacaoEncalhe(ControleConferenciaEncalheCota controleConferenciaEncalheCota){
		
		return this.debitoCreditoCotaService.obterListaDebitoCreditoCotaDTO(controleConferenciaEncalheCota.getCota(), controleConferenciaEncalheCota.getDataOperacao());
	}
	
	protected String obterSlipReportPath() throws URISyntaxException {
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("/reports/slip.jasper");
		
		return url.toURI().getPath();
		
	}

	protected String obterSlipSubReportPath() throws URISyntaxException {
	
		URL subReportDir = Thread.currentThread().getContextClassLoader().getResource("/reports/");
		
		return subReportDir.toURI().getPath();
		
	}
	
	/**
	 * Obtem valor total para geração de crédito na C.E.
	 * @param idControleConferenciaEncalheCota
	 * @return BigDecimal
	 */
	@Transactional
	@Override
	public BigDecimal obterValorTotalConferenciaEncalhe(Long idControleConferenciaEncalheCota){
		
		 BigDecimal valorTotalEncalheOperacaoConferenciaEncalhe = 
					conferenciaEncalheRepository.obterValorTotalEncalheOperacaoConferenciaEncalhe(idControleConferenciaEncalheCota);
			
		 if(valorTotalEncalheOperacaoConferenciaEncalhe == null) {
		     valorTotalEncalheOperacaoConferenciaEncalhe = BigDecimal.ZERO;
		 }
		
		 return valorTotalEncalheOperacaoConferenciaEncalhe;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public List<ItemAutoComplete> obterListaProdutoEdicaoParaRecolhimentoPorCodigoBarras(Integer numeroCota, String codigoBarras) {

		return this.conferenciaEncalheRepository.obterListaProdutoEdicaoParaRecolhimentoPorCodigoBarras(numeroCota, codigoBarras);
	}
}
