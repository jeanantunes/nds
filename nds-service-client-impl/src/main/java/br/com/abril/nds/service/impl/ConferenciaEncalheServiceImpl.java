package br.com.abril.nds.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ComposicaoCobrancaSlipDTO;
import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DadosDocumentacaoConfEncalheCotaDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.dto.SlipDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.TipoSlip;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoArquivo;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
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
import br.com.abril.nds.model.estoque.StatusEstoqueFinanceiro;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ValoresAplicados;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
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
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.CobrancaControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheRepository;
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
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.ControleNumeracaoSlipService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.service.exception.ChamadaEncalheCotaInexistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheExistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheFinalizadaException;
import br.com.abril.nds.service.exception.EncalheRecolhimentoParcialException;
import br.com.abril.nds.service.exception.EncalheSemPermissaoSalvarException;
import br.com.abril.nds.service.exception.FechamentoEncalheRealizadoException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.BigDecimalUtil;
import br.com.abril.nds.util.BigIntegerUtil;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.JasperUtil;
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
	private ControleConferenciaEncalheRepository controleConferenciaEncalheRepository;
	
	@Autowired
	private ParametroEmissaoNotaFiscalRepository parametroEmissaoNotaFiscalRepository;

	@Autowired
	private TipoNotaFiscalRepository tipoNotaFiscalRepository;
	
	
	@Autowired
	private RecebimentoFisicoRepository recebimentoFisicoRepository;
	
	@Autowired
	private ItemRecebimentoFisicoRepository itemRecebimentoFisicoRepository;
	
	@Autowired
	private ControleNumeracaoSlipService controleNumeracaoSlipService;
	
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
	private ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;
	
	private SlipDTO slipDTO = new SlipDTO();
	private Map<String, Object> parametersSlip = new HashMap<String, Object>();
	
	public SlipDTO getSlipDTO(){
		return slipDTO;
	}
	
	public void setSlipDTO(SlipDTO dto){
		this.slipDTO = dto;
	}
	
	public Map<String, Object> getParametersSlip(){
		return parametersSlip;		
	}
	
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
	

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#verificarChamadaEncalheCota(java.lang.Integer)
	 */
	@Transactional(readOnly = true)
	public void verificarChamadaEncalheCota(Integer numeroCota) throws ConferenciaEncalheExistenteException, ChamadaEncalheCotaInexistenteException {
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				controleConferenciaEncalheCotaRepository.obterControleConferenciaEncalheCota(numeroCota, dataOperacao);
		
		if(controleConferenciaEncalheCota != null && StatusOperacao.CONCLUIDO.equals(controleConferenciaEncalheCota.getStatus())) {
			throw new ConferenciaEncalheExistenteException();
		}
		
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
	private ChamadaEncalhe validarExistenciaChamadaEncalheParaCotaProdutoEdicao(Cota cota, ProdutoEdicao produtoEdicao) {

		boolean encalheConferido = false;
		boolean indPesquisaCEFutura = false;
		boolean postergado = false;
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		if(produtoEdicao.isParcial()) {

			
			List<ChamadaEncalheCota> listaChamadaEncalheCota = chamadaEncalheCotaRepository.
					obterListaChamaEncalheCota(cota.getNumeroCota(), dataOperacao, produtoEdicao.getId(), indPesquisaCEFutura, encalheConferido, postergado);

			if(listaChamadaEncalheCota == null || listaChamadaEncalheCota.isEmpty()) {
				
				validarProdutoEdicaoSemChamadaEncalheCota(cota.getId(), produtoEdicao.getId());
				
				return null;
				
			}
			
			return listaChamadaEncalheCota.get(0).getChamadaEncalhe();
			
		} else {

			encalheConferido = false;
			indPesquisaCEFutura = true;
			postergado = false;
			
			Date dataRecolhimentoReferencia = obterDataRecolhimentoReferencia();
			
			List<ChamadaEncalheCota> listaChamadaEncalheCota = chamadaEncalheCotaRepository.
					obterListaChamaEncalheCota(cota.getNumeroCota(), dataRecolhimentoReferencia, produtoEdicao.getId(), indPesquisaCEFutura, encalheConferido, postergado);
			
			if(listaChamadaEncalheCota == null || listaChamadaEncalheCota.isEmpty()) {
				
				validarProdutoEdicaoSemChamadaEncalheCota(cota.getId(), produtoEdicao.getId());
				
				return null;
			}
			
			return listaChamadaEncalheCota.get(0).getChamadaEncalhe();
			
		}
		
	}
	
	/**
	 * Caso não exista chamada encalhe dentro do período de tempo que se permite o 
	 * recolhimento da cota para o produtoEdicao e cota em questão, 
	 * serão feitas verificações para detectar:
	 * 
	 * 1 - Se nunca existiu uma chamada de encalhe para o produto e cota em questão
	 * sera permitido seu recolhimento. 
	 * 
	 * 2 - Se o produtoEdição foi de fato expedido
	 * para a cota e ainda constam itens e a serem devolvidos sera permitido o seu recolhimento.
	 */
	private void validarProdutoEdicaoSemChamadaEncalheCota(Long idCota, Long idProdutoEdicao) {

		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		if(produtoEdicao == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto edição não encontrado");
		}
		
		String nomeProdutoEdicao = produtoEdicao.getProduto().getNome();
		
		List<ChamadaEncalheCota> listaChamadaEncalheCota = chamadaEncalheCotaRepository.obterListaChamadaEncalheCota(idCota, idProdutoEdicao);
		
		if(listaChamadaEncalheCota != null && !listaChamadaEncalheCota.isEmpty()){
				
			throw new ValidacaoException(
					TipoMensagem.WARNING, 
					"Já houve chamada de encalhe para o produto edição [" + nomeProdutoEdicao  + "] da cota. " +
					" não é possível realizar a conferência de encalhe para o mesmo. "   );
			
		}
		
		BigInteger qtdItensEstoqueProdutoEdicaoDaCotaNaoDevolvidos = obterQtdItensEstoqueProdutoEdicaoDaCotaNaoDevolvidos(idCota, idProdutoEdicao);
		
		if(qtdItensEstoqueProdutoEdicaoDaCotaNaoDevolvidos == null || qtdItensEstoqueProdutoEdicaoDaCotaNaoDevolvidos.compareTo(BigInteger.ZERO) <= 0 ) {
			
			throw new ValidacaoException(
					TipoMensagem.WARNING, 
					"Não há itens do produto edição [" + nomeProdutoEdicao  + "] a serem devolvidos para a cota."   );
		
		}
		
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
		}
		
		return listaConferenciaEncalheContingencia;
		
		
	}
	
	/**
	 * Obtém o total de reparte para uma cota. 
	 * 
	 * Este valor é calculado obtendo-se os produtos com chamada de encalhe
	 * para a cota em questão e em seguida seus respectivos valores de reparte.
	 * 
	 * @param numeroCota
	 * @param dataOperacao
	 * 
	 * @return BigDecimal
	 */
	@Transactional(readOnly = true)
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
	public InfoConferenciaEncalheCota obterInfoConferenciaEncalheCota(Integer numeroCota, boolean indConferenciaContingencia) {
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				controleConferenciaEncalheCotaRepository.obterControleConferenciaEncalheCota(numeroCota, dataOperacao);
		
		InfoConferenciaEncalheCota infoConfereciaEncalheCota = new InfoConferenciaEncalheCota();
		
		List<ConferenciaEncalheDTO> listaConferenciaEncalheDTO = null;
		
		if(controleConferenciaEncalheCota!=null) {
			listaConferenciaEncalheDTO = conferenciaEncalheRepository.obterListaConferenciaEncalheDTO(
							controleConferenciaEncalheCota.getId());
			infoConfereciaEncalheCota.setListaConferenciaEncalhe(listaConferenciaEncalheDTO);
			infoConfereciaEncalheCota.setEncalhe(null);
			infoConfereciaEncalheCota.setIdControleConferenciaEncalheCota(controleConferenciaEncalheCota.getId());
			infoConfereciaEncalheCota.setNotaFiscalEntradaCota(controleConferenciaEncalheCota.getNotaFiscalEntradaCotaPricipal());
		} else {
			infoConfereciaEncalheCota.setEncalhe(BigDecimal.ZERO);
		}
		
		if(indConferenciaContingencia) {
			
			List<ConferenciaEncalheDTO> listaConferenciaEncalheContingencia = 
					obterListaConferenciaEncalheContingencia(
							numeroCota, 
							dataOperacao,
							infoConfereciaEncalheCota.getListaConferenciaEncalhe());
			
			if(listaConferenciaEncalheDTO!=null && !listaConferenciaEncalheDTO.isEmpty()) {
				listaConferenciaEncalheDTO.addAll(listaConferenciaEncalheContingencia);
				infoConfereciaEncalheCota.setListaConferenciaEncalhe(listaConferenciaEncalheDTO);
			} else {
				infoConfereciaEncalheCota.setListaConferenciaEncalhe(listaConferenciaEncalheContingencia);
			}
			
			
		}

		Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);
		
		boolean indConferenciaEncalheReaberta = 
				(controleConferenciaEncalheCota != null && StatusOperacao.CONCLUIDO.equals(controleConferenciaEncalheCota.getStatus()));
		
		carregarDadosDebitoCreditoDaCota(infoConfereciaEncalheCota, cota, dataOperacao, indConferenciaEncalheReaberta);
		
		infoConfereciaEncalheCota.setCota(cota);
		infoConfereciaEncalheCota.setReparte(obterValorTotalReparte(numeroCota, dataOperacao));
		
		infoConfereciaEncalheCota.setTotalDebitoCreditoCota(BigDecimal.ZERO);
		infoConfereciaEncalheCota.setValorPagar(BigDecimal.ZERO);
		infoConfereciaEncalheCota.setValorVendaDia(BigDecimal.ZERO);
		
		infoConfereciaEncalheCota.setDistribuidorAceitaJuramentado(this.distribuidorService.aceitaJuramentado());
		
		return infoConfereciaEncalheCota;
		
	}
	
	/**
	 * Obtem Outros Valores
	 * @param infoConfereciaEncalheCota
	 * @param cota
	 * @param dataOperacao
	 * @param indConferenciaEncalheReaberta
	 */
	private void carregarDadosDebitoCreditoDaCota(InfoConferenciaEncalheCota infoConfereciaEncalheCota, 
												  Cota cota,
												  Date dataOperacao,
												  boolean indConferenciaEncalheReaberta) {
		
		
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCompleta = new ArrayList<DebitoCreditoCotaDTO>();
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroEnvioEncalhe = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroRecebimentoReparte = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE);

		List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados = new ArrayList<TipoMovimentoFinanceiro>();
		
		tiposMovimentoFinanceiroIgnorados.add(tipoMovimentoFinanceiroEnvioEncalhe);
		
		tiposMovimentoFinanceiroIgnorados.add(tipoMovimentoFinanceiroRecebimentoReparte);
		

		//DEBITOS E CREDITOS DA COTA NA DATA DE OPERACAO
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCota = 
				movimentoFinanceiroCotaRepository.obterDebitoCreditoCotaDataOperacao(cota.getNumeroCota(), 
																					 dataOperacao, 
																					 tiposMovimentoFinanceiroIgnorados);

		if(listaDebitoCreditoCota!=null && !listaDebitoCreditoCota.isEmpty()) {
			
			for (DebitoCreditoCotaDTO dccDto : listaDebitoCreditoCota){
				
				dccDto.setObservacoes("Débitos e Créditos");
				
				listaDebitoCreditoCompleta.add(dccDto);
			}
		}

		
		//NEGOCIACOES AVULSAS DA COTA
		List<DebitoCreditoCotaDTO> listaDebitoNegociacaoNaoAvulsaMaisEncargos = 
				movimentoFinanceiroCotaRepository.obterValorFinanceiroNaoConsolidadoDeNegociacaoNaoAvulsaMaisEncargos(cota.getNumeroCota());
		
		if(listaDebitoNegociacaoNaoAvulsaMaisEncargos != null && !listaDebitoNegociacaoNaoAvulsaMaisEncargos.isEmpty()) {
			
			for(DebitoCreditoCotaDTO negociacao : listaDebitoNegociacaoNaoAvulsaMaisEncargos) {
				
				negociacao.setTipoLancamento(OperacaoFinaceira.DEBITO);
				
				negociacao.setObservacoes("Negociação Avulsa e Encargos.");
				
				listaDebitoCreditoCompleta.add(negociacao);
			}
		}

		
		//DÉBIDO OU CRÉDITO DO CONSOLIDADO
		DebitoCreditoCotaDTO cobranca = obterOutroDebitoCreditoDeConsolidados(cota.getId(), dataOperacao);
		
		if(cobranca!=null) {

			listaDebitoCreditoCompleta.add(cobranca);
		}
				
		
		infoConfereciaEncalheCota.setListaDebitoCreditoCota(listaDebitoCreditoCompleta);		
	}
	
	/**
	 * Obtém débito ou crédito do consolidado da cota
	 * @param idCota
	 * @param dataOperacao
	 * @return DebitoCreditoCotaDTO
	 */
	private DebitoCreditoCotaDTO obterOutroDebitoCreditoDeConsolidados(Long idCota, Date dataOperacao) {

		ConsolidadoFinanceiroCota consolidado = this.obterConsolidadoCota(idCota, dataOperacao);
		
		if (consolidado==null){
			
			return null;
		}
		
		BigDecimal outrosValores = this.obterValorOutroDebitoCreditoDeConsolidados(consolidado);
		
		DebitoCreditoCotaDTO cobranca = new DebitoCreditoCotaDTO();
		
		cobranca.setTipoLancamento(OperacaoFinaceira.DEBITO);
		cobranca.setObservacoes("Outros valores");
		cobranca.setDataVencimento(consolidado.getDataConsolidado());
		cobranca.setDataLancamento(DateUtil.parseDataPTBR(DateUtil.formatarDataPTBR(consolidado.getDataConsolidado())));
		cobranca.setValor(outrosValores);
		
		return cobranca;
 	}
	
	/**
	 * Obtem Consolidado da Cota na Data
	 * @param idCota
	 * @param dataOperacao
	 * @return ConsolidadoFinanceiroCota
	 */
	private ConsolidadoFinanceiroCota obterConsolidadoCota(Long idCota, Date dataOperacao){
		
		ConsolidadoFinanceiroCota consolidado = this.consolidadoFinanceiroRepository.buscarPorCotaEData(idCota, dataOperacao);
		
		return consolidado;
	}
	
	/**
	 * Obtem Valor do Consolidado da Cota na Data
	 * @param consolidado
	 * @return BigDecimal
	 */
	private BigDecimal obterValorOutroDebitoCreditoDeConsolidados(ConsolidadoFinanceiroCota consolidado){
		
		BigDecimal outrosValores = BigDecimal.ZERO;

		if (consolidado != null){

			BigDecimal valorConsolidEncalhe = consolidado.getEncalhe() != null ? consolidado.getEncalhe().abs() : BigDecimal.ZERO;
			BigDecimal valorConsolidReparte = consolidado.getConsignado() != null ? consolidado.getConsignado().abs() : BigDecimal.ZERO;
			outrosValores = consolidado.getTotal().abs().subtract(valorConsolidReparte.subtract(valorConsolidEncalhe));
		}

		return outrosValores;	
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
		
		Cota cota = cotaRepository.obterPorNumerDaCotaAtiva(numeroCota);
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		conferenciaEncalheDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
		
		BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
        conferenciaEncalheDTO.setPrecoCapa(precoVenda);
		
		BigDecimal percentualDesconto = descontoService.obterValorDescontoPorCotaProdutoEdicao(null, cota, produtoEdicao);

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
	public ProdutoEdicaoDTO pesquisarProdutoEdicaoPorId(Integer numeroCota, Long idProdutoEdicao) throws ChamadaEncalheCotaInexistenteException, EncalheRecolhimentoParcialException {
		
		
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
		    
			ChamadaEncalhe chamadaEncalhe = this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(cota, produtoEdicao);
			
			if( chamadaEncalhe != null) {
				Integer dia = obterQtdeDiaAposDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
				produtoEdicaoDTO.setDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
				produtoEdicaoDTO.setTipoChamadaEncalhe(chamadaEncalhe.getTipoChamadaEncalhe());
				produtoEdicaoDTO.setDia(dia);
			}
			else{
				
				atribuirDataRecolhimentoParaProdutoSemChamadaEncalhe(idProdutoEdicao, produtoEdicaoDTO);
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
			
			Integer sequenciaMatriz = produtoEdicaoRepository.obterCodigoMatrizPorProdutoEdicao(produtoEdicao.getId(), produtoEdicaoDTO.getDataRecolhimentoDistribuidor());
			
			
			produtoEdicaoDTO.setSequenciaMatriz(sequenciaMatriz);
			
		}
		
		return produtoEdicaoDTO;
	}
	
	/*
	 * Obtem a maior data de lançamnto de um produto edição
	 */
	private void atribuirDataRecolhimentoParaProdutoSemChamadaEncalhe(Long idProdutoEdicao, ProdutoEdicaoDTO produtoEdicaoDTO) {
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		Date dataRecolhimentoDistribuidor = lancamentoRepository.obterDataUltimoLancamento(idProdutoEdicao, dataOperacao);
		 
		produtoEdicaoDTO.setDataRecolhimentoDistribuidor(dataRecolhimentoDistribuidor);
		
		Integer dia = obterQtdeDiaAposDataRecolhimentoDistribuidor(dataRecolhimentoDistribuidor);
		
		produtoEdicaoDTO.setDia(dia);
	}
	
	
	@Transactional(readOnly = true)
	public ProdutoEdicaoDTO pesquisarProdutoEdicaoPorSM(Integer numeroCota, Integer sm) throws ChamadaEncalheCotaInexistenteException, EncalheRecolhimentoParcialException {
		
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
		    
			ChamadaEncalhe chamadaEncalhe = this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(cota, produtoEdicao);
			
			if( chamadaEncalhe != null) {
				Integer dia = obterQtdeDiaAposDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
				produtoEdicaoDTO.setDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
				produtoEdicaoDTO.setTipoChamadaEncalhe(chamadaEncalhe.getTipoChamadaEncalhe());
				produtoEdicaoDTO.setDia(dia);
			}
			else{
				
				atribuirDataRecolhimentoParaProdutoSemChamadaEncalhe(produtoEdicao.getId(), produtoEdicaoDTO);
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
	public List<ProdutoEdicaoDTO> pesquisarProdutoEdicaoPorCodigoDeBarras(Integer numeroCota, String codigoDeBarras) throws ChamadaEncalheCotaInexistenteException, EncalheRecolhimentoParcialException {
		
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
		    
				ChamadaEncalhe chamadaEncalhe = this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(cota, produtoEdicao);
				
				if( chamadaEncalhe != null) {
					Integer dia = obterQtdeDiaAposDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
					produtoEdicaoDTO.setDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
					produtoEdicaoDTO.setTipoChamadaEncalhe(chamadaEncalhe.getTipoChamadaEncalhe());
					produtoEdicaoDTO.setDia(dia);
				}
				else{
					
					atribuirDataRecolhimentoParaProdutoSemChamadaEncalhe(produtoEdicao.getId(), produtoEdicaoDTO);
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
				
				Integer sequenciaMatriz = produtoEdicaoRepository.obterCodigoMatrizPorProdutoEdicao(produtoEdicao.getId(), produtoEdicaoDTO.getDataRecolhimentoDistribuidor());
				
				produtoEdicaoDTO.setSequenciaMatriz(sequenciaMatriz);
				
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
	
	@Transactional(rollbackFor=GerarCobrancaValidacaoException.class)
	public DadosDocumentacaoConfEncalheCotaDTO finalizarConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario,
			boolean indConferenciaContingencia) throws GerarCobrancaValidacaoException {
		
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
		
		controleConfEncalheCota = 
				inserirDadosConferenciaEncalhe(controleConfEncalheCota, listaConferenciaEncalhe, 
						listaIdConferenciaEncalheParaExclusao, usuario, StatusOperacao.CONCLUIDO, indConferenciaContingencia);
		
		BigDecimal valorTotalEncalheOperacaoConferenciaEncalhe = BigDecimal.ZERO;
				//conferenciaEncalheRepository.obterValorTotalEncalheOperacaoConferenciaEncalhe(controleConfEncalheCota.getId());
		
		for (ConferenciaEncalheDTO dto : listaConferenciaEncalhe){
			
			valorTotalEncalheOperacaoConferenciaEncalhe = valorTotalEncalheOperacaoConferenciaEncalhe.add(dto.getValorTotal());
		}
		
		this.abaterNegociacaoPorComissao(controleConfEncalheCota.getCota().getId(), valorTotalEncalheOperacaoConferenciaEncalhe);
		
		Map<String, Boolean> nossoNumeroCollection = new LinkedHashMap<String, Boolean>();
		
		DadosDocumentacaoConfEncalheCotaDTO documentoConferenciaEncalhe = new DadosDocumentacaoConfEncalheCotaDTO();
		
		try {
			
			nossoNumeroCollection = gerarCobranca(controleConfEncalheCota);
		
		} catch(GerarCobrancaValidacaoException e) {
			
			documentoConferenciaEncalhe.setMsgsGeracaoCobranca(e.getValidacaoVO());
			
		}
		
		
		PoliticaCobranca politicaCobranca = politicaCobrancaService.obterPoliticaCobrancaPrincipal();
		
		FormaEmissao formaEmissao = politicaCobranca.getFormaEmissao();
		
		boolean isUtililzaSlipImpressao = parametrosDistribuidorEmissaoDocumentoRepository.isUtilizaImpressao(TipoParametrosDistribuidorEmissaoDocumento.SLIP);
		
		boolean isUtililzaBoletoImpressao = parametrosDistribuidorEmissaoDocumentoRepository.isUtilizaImpressao(TipoParametrosDistribuidorEmissaoDocumento.BOLETO);
		
		boolean isUtililzaBoletoSlipImpressao = parametrosDistribuidorEmissaoDocumentoRepository.isUtilizaImpressao(TipoParametrosDistribuidorEmissaoDocumento.BOLETO_SLIP);
		
		documentoConferenciaEncalhe.setIdControleConferenciaEncalheCota(controleConfEncalheCota.getId());
		documentoConferenciaEncalhe.setIndGeraDocumentacaoConferenciaEncalhe(FormaEmissao.INDIVIDUAL_BOX.equals(formaEmissao));
		documentoConferenciaEncalhe.setUtilizaBoleto(isUtililzaBoletoImpressao);
		documentoConferenciaEncalhe.setUtilizaSlip(isUtililzaSlipImpressao);
		documentoConferenciaEncalhe.setUtilizaBoletoSlip(isUtililzaBoletoSlipImpressao);
		
		documentoConferenciaEncalhe.setListaNossoNumero(new LinkedHashMap<String, Boolean>());
		
		if(nossoNumeroCollection!=null && !nossoNumeroCollection.isEmpty()) {
			
			for (String nossoNumero : nossoNumeroCollection.keySet()){
				
				if(nossoNumero!=null && !nossoNumero.trim().isEmpty()) {
					associarCobrancaConferenciaEncalheCota(controleConfEncalheCota.getId(), nossoNumero);
				}
				
				documentoConferenciaEncalhe.getListaNossoNumero().put(nossoNumero,
						nossoNumeroCollection.get(nossoNumero));
				
			}
		}
		
		return documentoConferenciaEncalhe;
	}
	
	//caso haja negociação por comissão da cota será abatida aqui
	private void abaterNegociacaoPorComissao(Long idCota,
			BigDecimal valorTotalEncalheOperacaoConferenciaEncalhe) {
		
		//verifica se existe valor para abater das negociações
		if (valorTotalEncalheOperacaoConferenciaEncalhe != null &&
				valorTotalEncalheOperacaoConferenciaEncalhe.compareTo(BigDecimal.ZERO) > 0){
			
			//busca negociações por comissão ainda não quitadas
			List<Negociacao> negociacoes = 
					this.negociacaoDividaRepository.obterNegociacaoPorComissaoCota(idCota);
			
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
				
				this.negociacaoDividaRepository.alterar(negociacao);
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

		this.movimentoFinanceiroCotaService.gerarMovimentoFinanceiroCota(
				controleConferenciaEncalheCota.getCota(),
				controleConferenciaEncalheCota.getDataOperacao(),
				controleConferenciaEncalheCota.getUsuario(),
				controleConferenciaEncalheCota.getId(), 
				FormaComercializacao.CONSIGNADO);

		Map<String, Boolean> nossoNumeroCollection = new HashMap<String, Boolean>();
		
		gerarCobrancaService.gerarCobranca(
				controleConferenciaEncalheCota.getCota().getId(), 
				controleConferenciaEncalheCota.getUsuario().getId(), 
				nossoNumeroCollection);
		
		return nossoNumeroCollection;
	}

	
	/**
	 * Faz o cancelamento de dados financeiros relativos a 
	 * operação de conferência de encalhe em questão.
	 * 
	 * @param controleConferenciaEncalheCota
	 */
	private void resetarDadosFinanceirosConferenciaEncalheCota(Long idControleConferenciaEncalheCota, Long idCota) {
		
		List<MovimentoFinanceiroCota> movimentosFinanceiroCota = 
				movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroDaOperacaoConferenciaEncalhe(idControleConferenciaEncalheCota);
		
		if(movimentosFinanceiroCota!=null && !movimentosFinanceiroCota.isEmpty()) {
			
			for (MovimentoFinanceiroCota movimentoFinanceiroCota : movimentosFinanceiroCota) {

				gerarCobrancaService.cancelarDividaCobranca(movimentoFinanceiroCota.getId(), null);
				
				if (movimentoFinanceiroCota.getMovimentos() != null){
					
					for (MovimentoEstoqueCota mec : movimentoFinanceiroCota.getMovimentos()){
						
						mec.setMovimentoFinanceiroCota(null);
						
						mec.setStatusEstoqueFinanceiro(StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);
						
						this.movimentoEstoqueCotaRepository.merge(mec);
					}
				}

				movimentoFinanceiroCota.setConsolidadoFinanceiroCota(null);
				
				this.movimentoFinanceiroCotaRepository.remover(movimentoFinanceiroCota);
			}
		}

		List<TipoMovimentoFinanceiro> listaPostergados = Arrays.asList(
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
						GrupoMovimentoFinaceiro.POSTERGADO_CREDITO),
						
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
						GrupoMovimentoFinaceiro.POSTERGADO_DEBITO)
		);
			
		this.movimentoFinanceiroCotaService.removerPostergadosDia(
				idCota, listaPostergados, 
				this.distribuidorService.obterDataOperacaoDistribuidor());
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
					controleConferenciaEncalheCota.getCota().getNumeroCota(), 
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
			
		if(movimentoEstoque!=null) {
		
			atualizarMovimentoEstoque(movimentoEstoque, conferenciaEncalheDTO);
			
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
					usuario);
		
		criarNovoRegistroConferenciaEncalhe(
				controleConferenciaEncalheCota, 
				conferenciaEncalheDTO,
				dataCriacao,
				numeroCota, 
				movimentoEstoqueCota,
				movimentoEstoque);
		
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
			
			TipoNotaFiscal tipoNF = tipoNotaFiscalRepository.obterTipoNotaFiscal(GrupoNotaFiscal.RECEBIMENTO_MERCADORIAS_ENCALHE);

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
		
		if(StatusOperacao.CONCLUIDO.equals(controleConferenciaEncalheCota.getStatus())){
			
			this.gerarCobrancaService.cancelarDividaCobranca(null, controleConferenciaEncalheCota.getCota().getId());
			
		}
	}
	
	/**
	 * Valida se a quantidade da conferência de encalhe não excede o reparte 
	 * de um produtoEdicao para determinada cota.
	 * 
	 * @param conferenciaEncalhe
	 * @param numeroCota
	 * @param dataOperacao
	 * @param indConferenciaContingencia
	 */
	@Transactional
	public void validarQtdeEncalheExcedeQtdeReparte(
			ConferenciaEncalheDTO conferenciaEncalhe,
			Integer numeroCota, 
			Date dataOperacao, boolean indConferenciaContingencia) {

		if (!indConferenciaContingencia &&
				(conferenciaEncalhe.getQtdExemplar() == null || conferenciaEncalhe.getQtdExemplar().compareTo(BigInteger.ZERO) <= 0)){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Quantidade de itens conferidos deve encalhe de ser maior que zero.");
			
		}
		
		Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);
		
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
		
		boolean juramentada = (conferenciaEncalheDTO.isJuramentada()) == null ? false : conferenciaEncalheDTO.isJuramentada();
		
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
		
		boolean juramentada = (conferenciaEncalheDTO.isJuramentada()) == null ? false : conferenciaEncalheDTO.isJuramentada();
		
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
			Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque,
			Usuario usuario) {
		
		boolean juramentada = (conferenciaEncalheDTO.isJuramentada()) == null ? false : conferenciaEncalheDTO.isJuramentada();
		
		TipoMovimentoEstoque tipoMovimentoEstoque = 
				obterTipoMovimentoEstoqueDistribuidor(
						juramentada, 
						conferenciaEncalheDTO.getDataRecolhimento(),
						dataCriacao, 
						mapaTipoMovimentoEstoque);

		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(conferenciaEncalheDTO.getIdProdutoEdicao());
		
		MovimentoEstoque movimentoEstoque = movimentoEstoqueService.gerarMovimentoEstoque(
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
	 * @param movimentoEstoqueCota
	 * @param movimentoEstoque
	 */
	private void criarNovoRegistroConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConferenciaEncalheCota,
			ConferenciaEncalheDTO conferenciaEncalheDTO,
			Date dataCriacao,
			Integer numeroCota, 
			MovimentoEstoqueCota movimentoEstoqueCota,
			MovimentoEstoque movimentoEstoque) {
		
		boolean juramentada = (conferenciaEncalheDTO.isJuramentada()) == null ? false : conferenciaEncalheDTO.isJuramentada();
		
		ConferenciaEncalhe conferenciaEncalhe = new ConferenciaEncalhe();

		if(conferenciaEncalheDTO.getDataRecolhimento() != null) {

			ChamadaEncalheCota chamadaEncalheCota = 
					obterChamadaEncalheCotaParaConfEncalhe(
							numeroCota, 
							conferenciaEncalheDTO.getDataRecolhimento(), 
							conferenciaEncalheDTO.getIdProdutoEdicao());

			conferenciaEncalhe.setChamadaEncalheCota(chamadaEncalheCota);
			
		}
		
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
		
		boolean encalheConferido = false;
		boolean indPesquisaCEFutura = true;
		boolean postergado = false;
		
		List<ChamadaEncalheCota> listaChamadaEncalheCota = 
				chamadaEncalheCotaRepository.obterListaChamaEncalheCota(numeroCota, dataRecolhimentoReferencia, idProdutoEdicao, indPesquisaCEFutura, encalheConferido, postergado);

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
			ctrlConfEncalheCota.setControleConferenciaEncalhe(obterControleConferenciaEncalhe(dataOperacaoDistribuidor));
			ctrlConfEncalheCota.setDataFim(dataFinalizacao);
			
			controleConferenciaEncalheCotaRepository.adicionar(ctrlConfEncalheCota);
			
			return ctrlConfEncalheCota;
		}
		
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
			br.com.abril.nds.enums.TipoDocumentoConferenciaEncalhe tipoDocumentoConferenciaEncalhe			
			) {

		switch(tipoDocumentoConferenciaEncalhe) {
		
		case SLIP_PDF :
			
			return gerarSlip(idControleConferenciaEncalheCota, true, TipoArquivo.PDF);
		
		case BOLETO_SLIP:
			
			return documentoCobrancaService.gerarDocumentoCobranca(nossoNumero);
		
		default:
			
			return null;
		}	
	}
	
	public enum TipoDocumentoConferenciaEncalhe {
		
		SLIP, BOLETO_OU_RECIBO;
		
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
	
	private String getDiaMesOrdinal(Long x){
		String ord="";
		String aux="";
		aux = Long.toString(x);
		
		int i=1;
		
		if (aux.length()>1){
			aux = aux.substring(i-1, i);
			switch (Integer.parseInt(aux)){ 
			    case 1:
			    	ord+="DÉCIMO";
			        break;
			    case 2:
			    	ord+="VIGÉSIMO";
			        break;
			    case 3:
			    	ord+="TRIGÉSIMO";
			        break;
			    default:
			    	ord+="";
			}
			i++;
		}
		
		aux = Long.toString(x);
		aux = aux.substring(i-1,i);
		switch (Integer.parseInt(aux)){ 
		    case 1:
		    	ord+=" PRIMEIRO";
		    	break;
		    case 2:
		    	ord+=" SEGUNDO";
		    	break;
		    case 3:
		    	ord+=" TERCEIRO";
		    	break;
		    case 4:
		    	ord+=" QUARTO";
		    	break;
		    case 5:
		    	ord+=" QUINTO";
		    	break;
		    case 6:
		    	ord+="SEXTO";
		    	break;
		    case 7:
		    	ord+=" SÉTIMO";
		    	break;
		    case 8:
		    	ord+=" OITAVO";
		        break;
		    case 9:
		    	ord+=" NONO";
		        break;
		    default:
		    	ord+="";
	    }

		return ord;
	}
	
	private long obterDiasEntreDatas(ProdutoEdicaoSlipDTO produtoEdicaoSlip){
		long d = 0;
		if (produtoEdicaoSlip.getDataOperacao()!=null && produtoEdicaoSlip.getDataRecolhimento()!=null){
			d = DateUtil.obterDiferencaDias(produtoEdicaoSlip.getDataOperacao(), produtoEdicaoSlip.getDataRecolhimento());
		}
		return d;
	}
	
	@Transactional
	public byte[] gerarSlipMatricial(Long idControleConferenciaEncalheCota, boolean incluirNumeroSlip) {
		setParamsSlip(idControleConferenciaEncalheCota, incluirNumeroSlip);
		
		return gerarSlipTxtMatricial();
	}
	
	public byte[] gerarSlip(Long idControleConferenciaEncalheCota, boolean incluirNumeroSlip, TipoArquivo tpArquivo) {

		setParamsSlip(idControleConferenciaEncalheCota, incluirNumeroSlip);
		
		switch (tpArquivo) {
		case PDF:
			return gerarSlipPDF();
		default:
			return null;
		}
		
	}

	@Transactional
	public void setParamsSlip(Long idControleConferenciaEncalheCota, boolean incluirNumeroSlip) {
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				controleConferenciaEncalheCotaRepository.buscarPorId(idControleConferenciaEncalheCota);
		
		if(incluirNumeroSlip || controleConferenciaEncalheCota.getNumeroSlip() == null) {
			controleConferenciaEncalheCota.setNumeroSlip(controleNumeracaoSlipService.obterProximoNumeroSlip(TipoSlip.SLIP_CONFERENCIA_ENCALHE));
			controleConferenciaEncalheCotaRepository.alterar(controleConferenciaEncalheCota);
		}
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		List<ProdutoEdicaoSlipDTO> listaProdutoEdicaoSlip = 
				conferenciaEncalheRepository.obterDadosSlipConferenciaEncalhe(
						idControleConferenciaEncalheCota);
		
		NumberFormat formatter = new DecimalFormat("00000");
		
		Integer numeroCota 		= controleConferenciaEncalheCota.getCota().getNumeroCota();
		Long idCota				= controleConferenciaEncalheCota.getCota().getId();
		String nomeCota 		= controleConferenciaEncalheCota.getCota().getPessoa().getNome();
		Date dataConferencia 	= controleConferenciaEncalheCota.getDataFim();
		Integer codigoBox 		= controleConferenciaEncalheCota.getBox().getCodigo();
		Long numeroSlip 		= controleConferenciaEncalheCota.getNumeroSlip();
		
		BigInteger qtdeTotalProdutos 	= null;
		BigDecimal valorTotalEncalhe 	= null;
		BigDecimal valorTotalPagar 		= null;
		BigDecimal valorDevido = BigDecimal.ZERO;
		
		BigDecimal valorTotalReparte = obterValorTotalReparte(numeroCota, dataOperacao);
		
		BigInteger qtdeTotalProdutosDia = null;
		BigDecimal valorTotalEncalheDia = null;
		long diaAnt=0;
		long dia=0;
		boolean exibeSubtotalDia = false;
		
		for(ProdutoEdicaoSlipDTO produtoEdicaoSlip : listaProdutoEdicaoSlip) {
				
 			diaAnt=dia;
 			
			BigInteger reparte = 
					this.conferenciaEncalheRepository.obterReparteConferencia(
							idCota,
							controleConferenciaEncalheCota.getId(),
							produtoEdicaoSlip.getIdProdutoEdicao());
			
			produtoEdicaoSlip.setReparte(reparte);
			
			qtdeTotalProdutos = BigIntegerUtil.soma(qtdeTotalProdutos, produtoEdicaoSlip.getEncalhe());
			
			valorTotalEncalhe = BigDecimalUtil.soma(valorTotalEncalhe, produtoEdicaoSlip.getValorTotal());
			
			qtdeTotalProdutosDia = BigIntegerUtil.soma(qtdeTotalProdutosDia, produtoEdicaoSlip.getEncalhe());   
				
			valorTotalEncalheDia = BigDecimalUtil.soma(valorTotalEncalheDia, produtoEdicaoSlip.getValorTotal());
			
			if(produtoEdicaoSlip.getReparte() == null)
				produtoEdicaoSlip.setReparte(BigInteger.ZERO);
			
			BigDecimal precoVenda = (produtoEdicaoSlip.getPrecoVenda() == null) ? BigDecimal.ZERO : produtoEdicaoSlip.getPrecoVenda();
			
			BigDecimal qtdeReparte = (produtoEdicaoSlip.getReparte() == null) ? BigDecimal.ZERO : new BigDecimal(produtoEdicaoSlip.getReparte().intValue());
			
			valorDevido = BigDecimalUtil.soma(valorDevido,precoVenda.multiply(qtdeReparte));
			
			dia = this.obterDiasEntreDatas(produtoEdicaoSlip);
 
			String numCE = formatter.format(produtoEdicaoSlip.getIdChamadaEncalhe());
			
			produtoEdicaoSlip.setOrdinalDiaConferenciaEncalhe(dia==diaAnt?"":this.getDiaMesOrdinal(dia)+" DIA - CE NUM "+numCE);
		
		    exibeSubtotalDia = (listaProdutoEdicaoSlip.indexOf(produtoEdicaoSlip)==(listaProdutoEdicaoSlip.size()-1))||
		    		           (dia!=this.obterDiasEntreDatas(listaProdutoEdicaoSlip.get(listaProdutoEdicaoSlip.indexOf(produtoEdicaoSlip)+1)));	
			                     
			produtoEdicaoSlip.setQtdeTotalProdutos(!exibeSubtotalDia?"": String.valueOf(qtdeTotalProdutosDia.intValue()));
 			
			produtoEdicaoSlip.setValorTotalEncalhe(!exibeSubtotalDia?"":CurrencyUtil.formatarValor(valorTotalEncalheDia));
 			
 			if(exibeSubtotalDia){
 				qtdeTotalProdutosDia = BigInteger.ZERO;   
 				valorTotalEncalheDia = BigDecimal.ZERO;
 			}
	
		}
		
		valorTotalReparte = (valorTotalReparte == null) ? BigDecimal.ZERO : valorTotalReparte;
		
		valorTotalEncalhe = (valorTotalEncalhe == null) ? BigDecimal.ZERO : valorTotalEncalhe;
		
		qtdeTotalProdutos = (qtdeTotalProdutos == null) ? BigInteger.ZERO : qtdeTotalProdutos;
		
		BigDecimal valorTotalDebitoCredito	= obterValorTotalDebitoCreditoCota(numeroCota, dataOperacao);
		
		valorTotalPagar = (valorTotalReparte.subtract(valorTotalEncalhe));

		if(BigDecimal.ZERO.compareTo(valorTotalDebitoCredito)>0) {
			
			valorTotalPagar = valorTotalPagar.add(valorTotalDebitoCredito.abs());
			
		} else {
			
			valorTotalPagar = valorTotalPagar.subtract(valorTotalDebitoCredito.abs());
		}
		
		
		//Considera Outros Valores no Valor total à Pagar
        ConsolidadoFinanceiroCota consolidado = this.obterConsolidadoCota(idCota, dataOperacao);
		
		BigDecimal outrosValoresConsolidados = this.obterValorOutroDebitoCreditoDeConsolidados(consolidado);
		
        if(BigDecimal.ZERO.compareTo(outrosValoresConsolidados)>0) {
			
			valorTotalPagar = valorTotalPagar.add(outrosValoresConsolidados.abs());
			
		} else {
			
			valorTotalPagar = valorTotalPagar.subtract(outrosValoresConsolidados.abs());
		}
		
		
		slipDTO.setNumeroCota(numeroCota);
		slipDTO.setNomeCota(nomeCota);
		slipDTO.setDataConferencia(dataConferencia);           
		slipDTO.setCodigoBox(codigoBox.toString());                   
		slipDTO.setTotalProdutoDia(qtdeTotalProdutos);  
		slipDTO.setTotalProdutos(qtdeTotalProdutos);    
		slipDTO.setValorEncalheDia(valorTotalEncalhe);    
		slipDTO.setValorTotalEncalhe(valorTotalEncalhe); 
		slipDTO.setValorDevido(valorDevido);        
		slipDTO.setValorSlip(valorDevido.subtract(valorTotalEncalhe));           
		slipDTO.setValorTotalPagar(valorTotalPagar); 
		slipDTO.setNumSlip(null);                 
		slipDTO.setListaProdutoEdicaoSlipDTO(listaProdutoEdicaoSlip);
		
		parametersSlip.put("NUMERO_COTA", slipDTO.getNumeroCota());
		parametersSlip.put("NOME_COTA", slipDTO.getNomeCota());
		parametersSlip.put("NUM_SLIP", numeroSlip.toString());
		parametersSlip.put("CODIGO_BOX", slipDTO.getCodigoBox());
		parametersSlip.put("DATA_CONFERENCIA", slipDTO.getDataConferencia());
		parametersSlip.put("CE_JORNALEIRO", slipDTO.getCeJornaleiro());
		parametersSlip.put("TOTAL_PRODUTOS", slipDTO.getTotalProdutos());
		parametersSlip.put("VALOR_TOTAL_ENCA", slipDTO.getValorTotalEncalhe() );
		parametersSlip.put("VALOR_PAGAMENTO_POSTERGADO", slipDTO.getValorTotalPagar());
		parametersSlip.put("VALOR_PAGAMENTO_PENDENTE", slipDTO.getValorTotalPagar());
		parametersSlip.put("VALOR_MULTA_MORA", slipDTO.getValorTotalPagar());
		parametersSlip.put("VALOR_CREDITO_DIF", slipDTO.getValorTotalPagar());
		slipDTO.setCeJornaleiro(null);
		

		try {
			parametersSlip.put("LOGOTIPO", JasperUtil.getImagemRelatorio(getLogoDistribuidor()));
		} catch(Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao carregar logotipo do distribuidor no documento de cobrança");
		}
		
		
		//OBTEM OS MOVIMENTOS FINANCEIROS(DÉBITOS E CRÉDITOS) DA COTA NA DATA DE OPERAÇÃO
		List<ComposicaoCobrancaSlipDTO> listaComposicaoCobranca = this.conferenciaEncalheRepository.obterComposicaoCobrancaSlip(numeroCota, controleConferenciaEncalheCota.getDataOperacao(), null);
		
		parametersSlip.put("LISTA_COMPOSICAO_COBRANCA",listaComposicaoCobranca);
		
		BigDecimal totalComposicao = BigDecimal.ZERO;
		for(ComposicaoCobrancaSlipDTO item:listaComposicaoCobranca){
			
	        //OBTÉM O NUMERO DE CHAMADA DE ENCALHE RELACIONADO COM CADA MOVIMENTO FINANCEIRO DA COMPOSIÇÃO DE COBRANÇA
			ChamadaEncalheCota chamadaEncalhe = this.conferenciaEncalheRepository.obterChamadaEncalheDevolucao(item.getIdMovimentoFinanceiro());
		    if (chamadaEncalhe!=null){
		        item.setDescricao(item.getDescricao()+"-CE num "+formatter.format(chamadaEncalhe.getId()));
		    }
			
	        //TOTALIZAÇÃO DO SLIP CONSIDERANDO COMPOSIÇÃO DE COBRANÇA
		    if (item.getOperacaoFinanceira().equals("D")){
				totalComposicao = totalComposicao.add(item.getValor());
			}
			else{
				totalComposicao = totalComposicao.subtract(item.getValor());
			}
		}
		
		totalComposicao = totalComposicao.add(slipDTO.getValorSlip());

		BigDecimal devido = listaComposicaoCobranca.isEmpty() ? valorTotalReparte : slipDTO.getValorDevido();
		BigDecimal totalSlip = listaComposicaoCobranca.isEmpty() ? devido.subtract(slipDTO.getValorTotalEncalhe()): slipDTO.getValorSlip();
		BigDecimal totalPagar = listaComposicaoCobranca.isEmpty() ? valorTotalPagar : totalComposicao;

		parametersSlip.put("VALOR_DEVIDO", devido);
		parametersSlip.put("VALOR_SLIP", totalSlip);
		parametersSlip.put("VALOR_TOTAL_PAGAR", totalPagar);
		parametersSlip.put("RAZAO_SOCIAL_DISTRIBUIDOR", this.distribuidorService.obterRazaoSocialDistribuidor());
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
	
	protected InputStream getLogoDistribuidor(){
		
		InputStream inputStream = parametrosDistribuidorService.getLogotipoDistribuidor();
		
		if(inputStream == null){
		  
			return new ByteArrayInputStream(new byte[0]);
		}
		
		return inputStream;
	}

	public byte[] gerarSlipTxtJasper(SlipDTO slip, Map<String, Object> parameters){
		try{
			
		    parameters.put("SUBREPORT_DIR", obterSlipSubReportPath());
		}
		catch(Exception e){
			e.printStackTrace();
		}

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(slip.getListaProdutoEdicaoSlipDTO());
		
		String path = null;
		
		try {
			path = obterSlipReportPath();
			
			//Retorna um byte array de um TXT
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			JRTextExporter exporter = new JRTextExporter();  
			exporter.setParameter( JRExporterParameter.JASPER_PRINT, JasperFillManager.fillReport(path, parameters, jrDataSource) );  
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);  
			exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(4));  
			exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(21.25));
			exporter.exportReport();

			return out.toByteArray();
		} catch (JRException e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Não foi possível gerar relatório Slip");
		}catch (URISyntaxException e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Não foi possível gerar relatário Slip");
		}
	}
	
	public byte[] gerarSlipTxtMatricial(){
		
		StringBuffer sb = new StringBuffer();
		/*ImpressaoMatricialUtil e = new ImpressaoMatricialUtil(sb);
		
		e.darEspaco(1);
		e.adicionar("TREELOG S/A LOGISTICA E DISTRIBUICAO");
		e.quebrarLinhaEscape();
		e.darEspaco(3);
		e.adicionar("SLIP DE RECOLHIMENTO DE ENCALHE");
		e.quebrarLinhaEscape();
		e.quebrarLinhaEscape();
		e.adicionar("Cota: "+slipDTO.getNumeroCota()+" - "+slipDTO.getNomeCota());
		e.quebrarLinhaEscape();
		e.adicionar("Data: "+new SimpleDateFormat("dd/MM/yyyy").format(slipDTO.getDataConferencia()));
		e.quebrarLinhaEscape();
		e.adicionar("Hora: "+new SimpleDateFormat("HH:mm:ss").format(slipDTO.getDataConferencia()));
		e.quebrarLinhaEscape();
		e.adicionar("BOX:  "+slipDTO.getCodigoBox());e.darEspaco(2);e.adicionar("Num. Slip: "+(slipDTO.getNumSlip() == null ? 0 : slipDTO.getNumSlip()));
		e.quebrarLinhaEscape();
		e.adicionar("----------------------------------------");
		e.quebrarLinhaEscape();
		e.adicionar("DESCRICAO", 9);e.darEspaco(1);
		e.adicionar("EDICAO", 6);e.darEspaco(1);
		e.adicionar("REP", 3);e.darEspaco(1);
		e.adicionar("ENC", 3);e.darEspaco(1);
		e.adicionar("PRECO", 7);e.darEspaco(1);
		e.adicionar("TOTAL", 8);
		e.quebrarLinhaEscape();
		
		Iterator<ProdutoEdicaoSlipDTO> iterator = slipDTO.getListaProdutoEdicaoSlipDTO().iterator();
		while(iterator.hasNext()){
			ProdutoEdicaoSlipDTO itemLista = iterator.next();
			
			//Comentado a definir com Cesar como ficará OrdinalDiaConferenciaEncalhe
//			e.adicionar(itemLista.getOrdinalDiaConferenciaEncalhe());
//			e.quebrarLinhaEscape();
			
			e.adicionar(itemLista.getNomeProduto() == null ? "": itemLista.getNomeProduto(), 9);e.darEspaco(1);
			e.adicionar(itemLista.getNumeroEdicao() == null ? "" : itemLista.getNumeroEdicao().toString(), 6);e.darEspaco(1);
			e.adicionar(itemLista.getReparte() == null ? "" : itemLista.getReparte().toString(), 3);e.darEspaco(1);
			e.adicionar(itemLista.getEncalhe() == null ? "" : itemLista.getEncalhe().toString(), 3);e.darEspaco(1);
			e.adicionar(itemLista.getPrecoVenda() == null ? "0,00" : itemLista.getPrecoVenda().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString(), 7);e.darEspaco(1);
			e.adicionar(itemLista.getValorTotal() == null ? "0,00" : itemLista.getValorTotal().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString(), 8);
			e.quebrarLinhaEscape();
			
			//Ultima linha
			if(!iterator.hasNext()){
				e.quebrarLinhaEscape();
				
				String dataRecolhimentoStr = "";
				if(itemLista.getDataRecolhimento()!=null ){
					dataRecolhimentoStr = new SimpleDateFormat("dd/MM/yyyy").format(itemLista.getDataRecolhimento());
				}

				String qtdeTotalProdutos =  itemLista.getQtdeTotalProdutos() == null ? "0" : itemLista.getQtdeTotalProdutos();
				e.adicionarCompleteEspaco("Total de Produtos do dia "+ dataRecolhimentoStr+":", qtdeTotalProdutos);
				
				e.quebrarLinhaEscape();

				String valorTotalEncalhe = itemLista.getValorTotalEncalhe() == null ? "0" : itemLista.getValorTotalEncalhe();
				e.adicionarCompleteEspaco("Total do Encalhe  do dia "+ dataRecolhimentoStr +":", valorTotalEncalhe);
				e.quebrarLinhaEscape();
				e.quebrarLinhaEscape();
			}
		}
		e.adicionar("SUB-TOTAL-------------------------------");
		e.quebrarLinhaEscape();
		
		String totalProdutos = slipDTO.getTotalProdutos() == null ? "0" : slipDTO.getTotalProdutos().toString();
		e.adicionarCompleteEspaco("Total de produtos:", totalProdutos);
		e.quebrarLinhaEscape();
		
		String valorTotalEncalhe = slipDTO.getValorTotalEncalhe() == null ? "0,00" : slipDTO.getValorTotalEncalhe().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
		e.adicionarCompleteEspaco("Valor total de encalhe: ( A )", valorTotalEncalhe);
		e.quebrarLinhaEscape();
		e.quebrarLinhaEscape();
		e.adicionar("COMPOSICAO COBRANCA---------------------");
		e.quebrarLinhaEscape();
		
		String valorSlip = slipDTO.getValorSlip() == null ? "0,00" : slipDTO.getValorSlip().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
		e.adicionarCompleteEspaco("Valor SLIP do dia: ( B - A ) D", valorSlip);
		e.quebrarLinhaEscape();
		e.quebrarLinhaEscape();
		
		String valorTotalPagar = slipDTO.getValorTotalPagar() == null ? "0,00" : slipDTO.getValorTotalPagar().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
		e.adicionarCompleteTraco("VALOR TOTAL A PAGAR", valorTotalPagar);
		
		e.quebrarLinhaEscape(9);//Espaços fim da impressao
*/		
		String saida = sb.toString();
		
		return saida.getBytes();
	}

	private byte[] gerarSlipPDF() {
		
		URL subReportDir = Thread.currentThread().getContextClassLoader().getResource("/reports/");
		try{
		    parametersSlip.put("SUBREPORT_DIR", subReportDir.toURI().getPath());
		}
		catch(Exception e){
			e.printStackTrace();
		}

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(slipDTO.getListaProdutoEdicaoSlipDTO());

		URL url = Thread.currentThread().getContextClassLoader().getResource("/reports/slip_pdf.jasper");

		String path = null;

		try {
			
			path = url.toURI().getPath();
			return  JasperRunManager.runReportToPdf(path, parametersSlip, jrDataSource);
		} catch (JRException e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Não foi possível gerar relatório Slip");
		}catch (URISyntaxException e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Não foi possível gerar relatório Slip");
		}
	}
	
	public static void main (String...strings){
		
		float x = (float) 12.00000000000000;
		
		System.out.println(new BigDecimal(x));
		
		
	}
	
}
