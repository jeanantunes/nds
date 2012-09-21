package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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

import br.com.abril.nds.client.util.BigDecimalUtil;
import br.com.abril.nds.client.util.BigIntegerUtil;
import br.com.abril.nds.dto.ComposicaoCobrancaSlipDTO;
import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DadosDocumentacaoConfEncalheCotaDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.dto.SlipDTO;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.TipoSlip;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
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
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
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
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;
import br.com.abril.nds.repository.LancamentoParcialRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.NotaFiscalEntradaRepository;
import br.com.abril.nds.repository.ParametroEmissaoNotaFiscalRepository;
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
import br.com.abril.nds.service.exception.EncalheExcedeReparteException;
import br.com.abril.nds.service.exception.EncalheRecolhimentoParcialException;
import br.com.abril.nds.service.exception.EncalheSemPermissaoSalvarException;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.MathUtil;
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
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Date dataOperacao = distribuidor.getDataOperacao();
		
		if(produtoEdicao.isParcial()) {

			
			List<ChamadaEncalheCota> listaChamadaEncalheCota = chamadaEncalheCotaRepository.
					obterListaChamaEncalheCota(cota.getNumeroCota(), dataOperacao, produtoEdicao.getId(), indPesquisaCEFutura, encalheConferido, postergado);

			if(listaChamadaEncalheCota == null || listaChamadaEncalheCota.isEmpty()) {
				
				validarProdutoEdicaoSemChamadaEncalheCota(cota.getId(), produtoEdicao.getId(), dataOperacao);
				
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
				
				validarProdutoEdicaoSemChamadaEncalheCota(cota.getId(), produtoEdicao.getId(), dataOperacao);
				
			}
			
			return listaChamadaEncalheCota.get(0).getChamadaEncalhe();
			
		}
		
	}
	
	/**
	 * Caso não exista chamada encalhe cota para o produtoEdicao e cota em questão, 
	 * serão feitas verificações para detectar se o este produtoEdição foi de fato expedido
	 * para a cota.
	 * 
	 */
	private void validarProdutoEdicaoSemChamadaEncalheCota(Long idCota, Long idProdutoEdicao, Date dataOperacao) {
		
		BigInteger qtdeReparte = obterQtdeReparteParaProdutoEdicao(idCota, idProdutoEdicao, dataOperacao, dataOperacao);
		
		if(qtdeReparte.compareTo(BigInteger.ZERO)<=0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto edição não foi expedido para a cota.");
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#isLancamentoParcialProdutoEdicao(java.lang.String, java.lang.Long)
	 */
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
			Long idDistribuidor, 
			Integer numeroCota,
			Date dataOperacao,
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe) {
		
		Set<Long> listaIdProdutoEdicao = new HashSet<Long>();
		
		if(listaConferenciaEncalhe!=null && !listaConferenciaEncalhe.isEmpty()) {
			
			for(ConferenciaEncalheDTO conferencia : listaConferenciaEncalhe) {
				
				listaIdProdutoEdicao.add(conferencia.getIdProdutoEdicao());
				
			}
			
		}
		
		Date dataInicial = obterDataRecolhimentoReferencia();
		Date dataFinal = dataOperacao;
		boolean indFechado = false;
		boolean indPostergado = false;
		
		int idInicial = (int) System.currentTimeMillis();
		idInicial = (idInicial - (1000000));
		
		
		List<ConferenciaEncalheDTO> listaConferenciaEncalheContingencia = 
			conferenciaEncalheRepository.obterListaConferenciaEncalheDTOContingencia(
				idDistribuidor,
				numeroCota, 
				dataInicial, 
				dataFinal, 
				indFechado, 
				indPostergado, 
				listaIdProdutoEdicao);
		
		for(ConferenciaEncalheDTO conferencia : listaConferenciaEncalheContingencia) {
			int id = (-1 * (idInicial++));
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
	 * @param idDistribuidor
	 * @param numeroCota
	 * @param dataOperacao
	 * 
	 * @return BigDecimal
	 */
	private BigDecimal obterValorTotalReparte(Long idDistribuidor, Integer numeroCota, Date dataOperacao) {
		
		List<Long> listaIdProdutoEdicao = 
				chamadaEncalheCotaRepository.obterListaIdProdutoEdicaoChamaEncalheCota(numeroCota, dataOperacao, true, false, false);
		
		BigDecimal reparte = BigDecimal.ZERO;
		
		if(listaIdProdutoEdicao != null && !listaIdProdutoEdicao.isEmpty()) {
			reparte = estoqueProdutoCotaRepository.obterValorTotalReparteCota(numeroCota, listaIdProdutoEdicao, idDistribuidor);
		} 
		
		return reparte;
		
	}
	
	@Transactional(readOnly = true)
	public InfoConferenciaEncalheCota obterInfoConferenciaEncalheCota(Integer numeroCota, boolean indConferenciaContingencia) {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		boolean aceitaJuramentado = distribuidor.isAceitaJuramentado();
		
		Date dataOperacao = distribuidor.getDataOperacao();
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				controleConferenciaEncalheCotaRepository.obterControleConferenciaEncalheCota(numeroCota, dataOperacao);
		
		InfoConferenciaEncalheCota infoConfereciaEncalheCota = new InfoConferenciaEncalheCota();
		
		List<ConferenciaEncalheDTO> listaConferenciaEncalheDTO = null;
		
		if(controleConferenciaEncalheCota!=null) {
			
			listaConferenciaEncalheDTO = conferenciaEncalheRepository.obterListaConferenciaEncalheDTO(
							controleConferenciaEncalheCota.getId(), 
							distribuidor.getId());
			
			infoConfereciaEncalheCota.setListaConferenciaEncalhe(listaConferenciaEncalheDTO);
			
			infoConfereciaEncalheCota.setEncalhe(null);
			
			infoConfereciaEncalheCota.setIdControleConferenciaEncalheCota(controleConferenciaEncalheCota.getId());
			
			infoConfereciaEncalheCota.setNotaFiscalEntradaCota(controleConferenciaEncalheCota.getNotaFiscalEntradaCota());
			
		} else {
			
			infoConfereciaEncalheCota.setEncalhe(BigDecimal.ZERO);
			
		}
		
		if(indConferenciaContingencia) {
			
			List<ConferenciaEncalheDTO> listaConferenciaEncalheContingencia = 
					obterListaConferenciaEncalheContingencia(
							distribuidor.getId(), 
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
		
		BigDecimal reparte = obterValorTotalReparte(distribuidor.getId(), numeroCota, dataOperacao);
		
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
		
		Cota cota = cotaRepository.obterPorNumerDaCotaAtiva(numeroCota);
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		conferenciaEncalheDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
		
		BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
        conferenciaEncalheDTO.setPrecoCapa(precoVenda);
		
		BigDecimal percentualDesconto = descontoService.obterDescontoPorCotaProdutoEdicao(cota, produtoEdicao);
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
		
		if (produtoEdicao != null){
		    
			produtoEdicaoDTO = new ProdutoEdicaoDTO();
			
		    Cota cota = cotaRepository.obterPorNumerDaCotaAtiva(numeroCota);
		    
			ChamadaEncalhe chamadaEncalhe = this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(cota, produtoEdicao);
			
			if( chamadaEncalhe != null) {
				Integer dia = obterQtdeDiaAposDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
				produtoEdicaoDTO.setDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
				produtoEdicaoDTO.setTipoChamadaEncalhe(chamadaEncalhe.getTipoChamadaEncalhe());
				produtoEdicaoDTO.setDia(dia);
			}
			
			produtoEdicaoDTO.setId(produtoEdicao.getId());
			produtoEdicaoDTO.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
			produtoEdicaoDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
            produtoEdicaoDTO.setPrecoVenda(precoVenda);
			BigDecimal percentualDesconto = descontoService.obterDescontoPorCotaProdutoEdicao(cota, produtoEdicao);
			BigDecimal valorDesconto = MathUtil.calculatePercentageValue(precoVenda, percentualDesconto);
			produtoEdicaoDTO.setDesconto(valorDesconto);
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
		    
			produtoEdicaoDTO = new ProdutoEdicaoDTO();
			
		    Cota cota = cotaRepository.obterPorNumerDaCotaAtiva(numeroCota);
		    
			ChamadaEncalhe chamadaEncalhe = this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(cota, produtoEdicao);
			
			if( chamadaEncalhe != null) {
				Integer dia = obterQtdeDiaAposDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
				produtoEdicaoDTO.setDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
				produtoEdicaoDTO.setTipoChamadaEncalhe(chamadaEncalhe.getTipoChamadaEncalhe());
				produtoEdicaoDTO.setDia(dia);
			}
			
			produtoEdicaoDTO.setId(produtoEdicao.getId());
			produtoEdicaoDTO.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
			produtoEdicaoDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
            produtoEdicaoDTO.setPrecoVenda(precoVenda);
			BigDecimal percentualDesconto = descontoService.obterDescontoPorCotaProdutoEdicao(cota, produtoEdicao);
			BigDecimal valorDesconto = MathUtil.calculatePercentageValue(precoVenda, percentualDesconto);
			produtoEdicaoDTO.setDesconto(valorDesconto);
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
		    
			produtoEdicaoDTO = new ProdutoEdicaoDTO();
			
		    Cota cota = cotaRepository.obterPorNumerDaCotaAtiva(numeroCota);
		    
			ChamadaEncalhe chamadaEncalhe = this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(cota, produtoEdicao);
			
			if( chamadaEncalhe != null) {
				Integer dia = obterQtdeDiaAposDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
				produtoEdicaoDTO.setDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
				produtoEdicaoDTO.setTipoChamadaEncalhe(chamadaEncalhe.getTipoChamadaEncalhe());
				produtoEdicaoDTO.setDia(dia);
			}
			
			produtoEdicaoDTO.setId(produtoEdicao.getId());
			produtoEdicaoDTO.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
			produtoEdicaoDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
            produtoEdicaoDTO.setPrecoVenda(precoVenda);
			BigDecimal percentualDesconto = descontoService.obterDescontoPorCotaProdutoEdicao(cota, produtoEdicao);
			BigDecimal valorDesconto = MathUtil.calculatePercentageValue(precoVenda, percentualDesconto);
			produtoEdicaoDTO.setDesconto(valorDesconto);
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
	public DadosDocumentacaoConfEncalheCotaDTO finalizarConferenciaEncalhe(
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
		
		controleConfEncalheCota = inserirDadosConferenciaEncalhe(controleConfEncalheCota, listaConferenciaEncalhe, listaIdConferenciaEncalheParaExclusao, usuario, StatusOperacao.CONCLUIDO);
		
		Set<String> nossoNumeroCollection = gerarCobranca(controleConfEncalheCota);
		
		String nossoNumero = "";
		
		if(nossoNumeroCollection!=null && !nossoNumeroCollection.isEmpty()) {
			
			nossoNumero = nossoNumeroCollection.iterator().next();
			
		}
		
		DadosDocumentacaoConfEncalheCotaDTO documentoConferenciaEncalhe = new DadosDocumentacaoConfEncalheCotaDTO();
		
		documentoConferenciaEncalhe.setIdControleConferenciaEncalheCota(controleConfEncalheCota.getId());
		documentoConferenciaEncalhe.setNossoNumero(nossoNumero);

		PoliticaCobranca politicaCobranca = politicaCobrancaService.obterPoliticaCobrancaPrincipal();
		FormaEmissao formaEmissao = politicaCobranca.getFormaEmissao();
		
		documentoConferenciaEncalhe.setIndGeraDocumentacaoConferenciaEncalhe(FormaEmissao.INDIVIDUAL_BOX.equals(formaEmissao));
		
		documentoConferenciaEncalhe.setUtilizaSlipBoleto(parametrosDistribuidorService.getParametrosDistribuidor().getBoletoSlipImpressao());
		
		return documentoConferenciaEncalhe;
		
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
		
		if(valorTotalEncalheOperacaoConferenciaEncalhe == null) {
			valorTotalEncalheOperacaoConferenciaEncalhe = BigDecimal.ZERO;
		}
		
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
		
		movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);

		Set<String> nossoNumeroCollection = new HashSet<String>();
		try {
			gerarCobrancaService.gerarCobranca(
					controleConferenciaEncalheCota.getCota().getId(), 
					controleConferenciaEncalheCota.getUsuario().getId(), nossoNumeroCollection);
		} catch (GerarCobrancaValidacaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		
		if(movimentoFinanceiroCota!=null) {
			gerarCobrancaService.cancelarDividaCobranca(movimentoFinanceiroCota.getId());
			movimentoFinanceiroCotaRepository.remover(movimentoFinanceiroCota);
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
	 * 
	 * @return ControleConferenciaEncalheCota
	 * 
	 * @throws EncalheExcedeReparteException
	 */
	private ControleConferenciaEncalheCota inserirDadosConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario,
			StatusOperacao statusOperacao) throws EncalheExcedeReparteException {
		
		if(listaConferenciaEncalhe == null || listaConferenciaEncalhe.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum item conferido, não é possível realizar a conferência de encalhe.");
		}
		
	    Date dataRecolhimentoReferencia = obterDataRecolhimentoReferencia();
	    Distribuidor distribuidor = distribuidorService.obter();
		Date dataOperacao = distribuidor.getDataOperacao();
		Date dataCriacao = new Date();
		Integer numeroCota = controleConfEncalheCota.getCota().getNumeroCota();
		
		NotaFiscalEntradaCota notaFiscalEntradaCota = atualizarCabecalhoNotaFiscalEntradaCota(
				controleConfEncalheCota.getId(),
				controleConfEncalheCota.getNotaFiscalEntradaCota(), 
				numeroCota, 
				usuario, 
				dataCriacao);
		
		atualizarItensNotaFiscalEntradaCota(
				dataOperacao,
				notaFiscalEntradaCota, 
				listaConferenciaEncalhe);
		
		controleConfEncalheCota.setNotaFiscalEntradaCota(notaFiscalEntradaCota);
			
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				obterControleConferenciaEncalheCotaAtualizado(controleConfEncalheCota, statusOperacao, usuario);
		
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
		
		if(listaIdConferenciaEncalheParaExclusao!=null && !listaIdConferenciaEncalheParaExclusao.isEmpty()) {
		
			for(Long idConferenciaEncalheExclusao : listaIdConferenciaEncalheParaExclusao) {
				excluirRegistroConferenciaEncalhe(idConferenciaEncalheExclusao);
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
			
			notaFiscalEntradaCotaFromBD = controleConferenciaEncalheCotaFromBD.getNotaFiscalEntradaCota();
			
			
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
			
			Pessoa pessoa = cota.getPessoa();
			
			if(pessoa instanceof PessoaFisica) {
				throw new IllegalStateException("Cota emitente de nota fiscal deve ser pessoa física");
			}

			
			notaFiscalEntradaCota.setCfop(cfop);
			notaFiscalEntradaCota.setStatusEmissao(statusNF);
			notaFiscalEntradaCota.setTipoNotaFiscal(tipoNF);
			notaFiscalEntradaCota.setDataEmissao(dataCriacao);
			notaFiscalEntradaCota.setDataExpedicao(dataCriacao);
			notaFiscalEntradaCota.setEmitente((PessoaJuridica)pessoa);
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
			Usuario usuario) throws EncalheSemPermissaoSalvarException, ConferenciaEncalheFinalizadaException, EncalheExcedeReparteException {

		validarConferenciaEncalheReaberta(controleConfEncalheCota.getId());
		
		validarPermissaoSalvarConferenciaEncalhe(listaConferenciaEncalhe);
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				inserirDadosConferenciaEncalhe(controleConfEncalheCota, listaConferenciaEncalhe, listaIdConferenciaEncalheParaExclusao, usuario, StatusOperacao.EM_ANDAMENTO);
		
		return controleConferenciaEncalheCota.getId();
		
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
			BigInteger qtdeExemplarEncalhe) throws EncalheExcedeReparteException {
		
		BigInteger qtdeReparte = obterQtdeReparteParaProdutoEdicao(
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
	 * @return BigInteger
	 */
	private BigInteger obterQtdeReparteParaProdutoEdicao(
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
			
			BigInteger qtdeReparte = movimentoEstoqueCotaRepository.obterQtdeMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo(
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

			
			BigInteger qtdeDevolvida = BigInteger.ZERO;
			
			BigInteger qtdeRecebida = BigInteger.ZERO;

			EstoqueProdutoCota estoqueProdutoCota = estoqueProdutoCotaRepository.buscarEstoquePorProdutEdicaoECota(idProdutoEdicao, idCota);
			
			if(estoqueProdutoCota != null) {
				
				qtdeDevolvida 	= (estoqueProdutoCota.getQtdeDevolvida() == null) ? BigInteger.ZERO : estoqueProdutoCota.getQtdeDevolvida();
				qtdeRecebida 	=  (estoqueProdutoCota.getQtdeRecebida() == null) ? BigInteger.ZERO : estoqueProdutoCota.getQtdeRecebida();

			}
			
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
					
					BigInteger qtdeOriginal = estoqueProduto.getQtde() == null ? BigInteger.ZERO : estoqueProduto.getQtde();
					
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
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		return distribuidor.getTipoContabilizacaoCE();
		
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
			
		} else {

			ctrlConfEncalheCota.setUsuario(usuario);
			ctrlConfEncalheCota.setCota(cota);
			ctrlConfEncalheCota.setDataOperacao(distribuidor.getDataOperacao());
			ctrlConfEncalheCota.setStatus(statusOperacao);
			ctrlConfEncalheCota.setControleConferenciaEncalhe(obterControleConferenciaEncalhe(distribuidor.getDataOperacao()));
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
	 * @param mapaTipoMovimentoEstoque
	 */
	private void atualizarMovimentoEstoqueCota(
			MovimentoEstoqueCota movimentoEstoqueCota, 
			ConferenciaEncalheDTO conferenciaEncalheDTO, 
			Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque) {
		
		BigInteger oldQtdeMovEstoqueCota = movimentoEstoqueCota.getQtde();
		
		BigInteger newQtdeMovEstoquecota = conferenciaEncalheDTO.getQtdExemplar();
		
		movimentoEstoqueCota.setQtde(newQtdeMovEstoquecota);
		
		movimentoEstoqueCotaRepository.alterar(movimentoEstoqueCota);
		
		EstoqueProdutoCota estoqueProdutoCota =  movimentoEstoqueCota.getEstoqueProdutoCota();
		
		BigInteger qtdDevolvida = 
				estoqueProdutoCota.getQtdeDevolvida() != null ? 
					estoqueProdutoCota.getQtdeDevolvida() : BigInteger.ZERO;
					
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
		
		BigInteger oldQtdeMovEstoque = movimentoEstoque.getQtde();
		
		BigInteger newQtdeMovEstoque = conferenciaEncalheDTO.getQtdExemplar();
		
		movimentoEstoque.setQtde(newQtdeMovEstoque);
		
		GrupoMovimentoEstoque grupoMovimentoEstoque = ((TipoMovimentoEstoque)movimentoEstoque.getTipoMovimento()).getGrupoMovimentoEstoque();
		
		movimentoEstoqueRepository.alterar(movimentoEstoque);
		
		EstoqueProduto estoqueProduto =  movimentoEstoque.getEstoqueProduto();
		
		if(estoqueProduto != null) {
			
			if(GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO.equals(grupoMovimentoEstoque)) {

				BigInteger qtdeOriginal = estoqueProduto.getQtdeSuplementar() == null ? BigInteger.ZERO : estoqueProduto.getQtdeSuplementar();
				estoqueProduto.setQtdeSuplementar(qtdeOriginal.subtract(oldQtdeMovEstoque));

				estoqueProduto.setQtdeSuplementar(estoqueProduto.getQtdeSuplementar().add(newQtdeMovEstoque));
				estoqueProdutoRepository.alterar(estoqueProduto);

				
			} else {
				
				BigInteger qtdeOriginal = estoqueProduto.getQtde() == null ? BigInteger.ZERO : estoqueProduto.getQtde();
				estoqueProduto.setQtde(qtdeOriginal.subtract(oldQtdeMovEstoque));

				estoqueProduto.setQtde(estoqueProduto.getQtde().add(newQtdeMovEstoque));
				estoqueProdutoRepository.alterar(estoqueProduto);
				
			}
			
		}
		
		estoqueProdutoRepository.alterar(estoqueProduto);
		
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
			DadosDocumentacaoConfEncalheCotaDTO dadosDocumentacaoConfEncalheCotaDTO, 
			TipoDocumentoConferenciaEncalhe tipoDocumentoConferenciaEncalhe) {
			
		String nossoNumero = dadosDocumentacaoConfEncalheCotaDTO.getNossoNumero();
		
		Long idControleConferenciaEncalheCota = dadosDocumentacaoConfEncalheCotaDTO.getIdControleConferenciaEncalheCota();

		switch(tipoDocumentoConferenciaEncalhe) {
		
		case SLIP :
			
			return gerarSlip(idControleConferenciaEncalheCota);
		
		case BOLETO_OU_RECIBO:
			
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
	private BigDecimal obterValorTotalDebitoCreditoCota(Integer numeroCota, Date dataOperacao) {
		
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
	
	private String space(int size){
		String s="";
		for (int i=0;i<size;i++){
			s+=" ";
		}
		return s; 
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
	
	private byte[] gerarSlip(Long idControleConferenciaEncalheCota) {

		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				controleConferenciaEncalheCotaRepository.buscarPorId(idControleConferenciaEncalheCota);
		
		Distribuidor distribuidor = distribuidorService.obter();
		Date dataOperacao = distribuidor.getDataOperacao();
		
		List<ProdutoEdicaoSlipDTO> listaProdutoEdicaoSlip = 
				conferenciaEncalheRepository.obterDadosSlipConferenciaEncalhe(
						idControleConferenciaEncalheCota, 
						distribuidor.getId());
		
		NumberFormat formatter = new DecimalFormat("00000");
		
		Integer numeroCota 		= controleConferenciaEncalheCota.getCota().getNumeroCota();
		Long idCota				= controleConferenciaEncalheCota.getCota().getId();
		String nomeCota 		= controleConferenciaEncalheCota.getCota().getPessoa().getNome();
		Date dataConferencia 	= controleConferenciaEncalheCota.getDataOperacao();
		Integer codigoBox 		= controleConferenciaEncalheCota.getBox().getCodigo();
		Long numeroSlip 		= controleNumeracaoSlipService.obterProximoNumeroSlip(TipoSlip.SLIP_CONFERENCIA_ENCALHE);
		
		BigInteger qtdeTotalProdutos 	= null;
		BigDecimal valorTotalEncalhe 	= null;
		BigDecimal valorTotalPagar 		= null;
		BigDecimal valorDevido = BigDecimal.ZERO;
		
		BigDecimal valorTotalReparte = obterValorTotalReparte(distribuidor.getId(), numeroCota, dataOperacao);
		
		BigInteger qtdeTotalProdutosDia = null;
		BigDecimal valorTotalEncalheDia = null;
		long dAnt=0;
		long d=0;
		boolean exibeSubtotalDia = false;
		
		for(ProdutoEdicaoSlipDTO produtoEdicaoSlip : listaProdutoEdicaoSlip) {
				
 			dAnt=d;
 			
			BigInteger reparte = obterQtdeReparteParaProdutoEdicao(
					idCota, 
					produtoEdicaoSlip.getIdProdutoEdicao(), 
					dataOperacao, 
					produtoEdicaoSlip.getDataRecolhimentoDistribuidor());
			
			produtoEdicaoSlip.setReparte(reparte);
			
			qtdeTotalProdutos = BigIntegerUtil.soma(qtdeTotalProdutos, produtoEdicaoSlip.getEncalhe());
			
			valorTotalEncalhe = BigDecimalUtil.soma(valorTotalEncalhe, produtoEdicaoSlip.getValorTotal());
			
			qtdeTotalProdutosDia = BigIntegerUtil.soma(qtdeTotalProdutosDia, produtoEdicaoSlip.getEncalhe());   
				
			valorTotalEncalheDia = BigDecimalUtil.soma(valorTotalEncalheDia, produtoEdicaoSlip.getValorTotal());
			
			valorDevido = BigDecimalUtil.soma(valorDevido,produtoEdicaoSlip.getPrecoVenda().multiply(new BigDecimal(produtoEdicaoSlip.getReparte().intValue())));
			
			d = this.obterDiasEntreDatas(produtoEdicaoSlip);
 
			String numCE = formatter.format(produtoEdicaoSlip.getIdChamadaEncalhe());
			
			produtoEdicaoSlip.setOrdinalDiaConferenciaEncalhe(d==dAnt?"":this.getDiaMesOrdinal(d)+" DIA - CE NUM "+numCE);
		
		    exibeSubtotalDia = (listaProdutoEdicaoSlip.indexOf(produtoEdicaoSlip)==(listaProdutoEdicaoSlip.size()-1))||
		    		           (d!=this.obterDiasEntreDatas(listaProdutoEdicaoSlip.get(listaProdutoEdicaoSlip.indexOf(produtoEdicaoSlip)+1)));	
			                     
			produtoEdicaoSlip.setQtdeTotalProdutos(!exibeSubtotalDia?"":"Total de Produtos do dia:"+this.space(133 - (CurrencyUtil.formatarValor(qtdeTotalProdutosDia).length()))+CurrencyUtil.formatarValor(qtdeTotalProdutosDia));
 			produtoEdicaoSlip.setValorTotalEncalhe(!exibeSubtotalDia?"":"Total do Encalhe do dia:"+this.space(133 - (CurrencyUtil.formatarValor(valorTotalEncalheDia).length()))+CurrencyUtil.formatarValor(valorTotalEncalheDia));
 			
 			if(exibeSubtotalDia){
 				qtdeTotalProdutosDia = BigInteger.ZERO;   
 				valorTotalEncalheDia = BigDecimal.ZERO;
 			}
	
		}
		
		valorTotalReparte = (valorTotalReparte == null) ? BigDecimal.ZERO : valorTotalReparte;
		
		valorTotalEncalhe = (valorTotalEncalhe == null) ? BigDecimal.ZERO : valorTotalEncalhe;
		
		BigDecimal valorTotalDebitoCredito	= obterValorTotalDebitoCreditoCota(numeroCota, dataOperacao);
		
		valorTotalPagar = (valorTotalReparte.subtract(valorTotalEncalhe));
		
		if(BigDecimal.ZERO.compareTo(valorTotalDebitoCredito)>0) {
			
			valorTotalPagar = valorTotalPagar.add(valorTotalDebitoCredito.abs());
			
		} else {
			
			valorTotalPagar = valorTotalPagar.subtract(valorTotalDebitoCredito.abs());
		}

		
		SlipDTO slip = new SlipDTO();

		//TODO: pode haver produtos na operação de encalhe pertencentes
		// 		a mais de uma chamada de encalhe.
		slip.setCeJornaleiro(null);
		
		
		slip.setNumeroCota(numeroCota);
		slip.setNomeCota(nomeCota);
		slip.setDataConferencia(dataConferencia);           
		slip.setCodigoBox(codigoBox.toString());                   
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
		parameters.put("NUM_SLIP", numeroSlip.toString());
		parameters.put("CODIGO_BOX", slip.getCodigoBox());
		parameters.put("DATA_CONFERENCIA", slip.getDataConferencia());
		parameters.put("CE_JORNALEIRO", slip.getCeJornaleiro());
		parameters.put("VALOR_DEVIDO", slip.getValorDevido());
		parameters.put("VALOR_SLIP", slip.getValorSlip());
		parameters.put("TOTAL_PRODUTOS", slip.getTotalProdutos());
		parameters.put("VALOR_TOTAL_ENCA", slip.getValorTotalEncalhe() );
		parameters.put("VALOR_PAGAMENTO_POSTERGADO", slip.getValorTotalPagar());
		parameters.put("VALOR_PAGAMENTO_PENDENTE", slip.getValorTotalPagar());
		parameters.put("VALOR_MULTA_MORA", slip.getValorTotalPagar());
		parameters.put("VALOR_CREDITO_DIF", slip.getValorTotalPagar());

		
		//OBTEM OS MOVIMENTOS FINANCEIROS(DÉBITOS E CRÉDITOS) DA COTA NA DATA DE OPERAÇÃO
		List<ComposicaoCobrancaSlipDTO> listaComposicaoCobranca = this.conferenciaEncalheRepository.obterComposicaoCobrancaSlip(numeroCota, controleConferenciaEncalheCota.getDataOperacao(), null);
		
		parameters.put("LISTA_COMPOSICAO_COBRANCA",listaComposicaoCobranca);
		
		
        //OBTÉM O NUMERO DE CHAMADA DE ENCALHE RELACIONADO COM CADA MOVIMENTO FINANCEIRO DA COMPOSIÇÃO DE COBRANÇA
		for (ComposicaoCobrancaSlipDTO item:listaComposicaoCobranca){
			ChamadaEncalheCota chamadaEncalhe = this.conferenciaEncalheRepository.obterChamadaEncalheDevolucao(item.getIdMovimentoFinanceiro());
		    if (chamadaEncalhe!=null){
		        item.setDescricao(item.getDescricao()+"-CE num "+formatter.format(chamadaEncalhe.getId()));
		    }
		}
		
		
        //TOTALIZAÇÃO DO SLIP CONSIDERANDO COMPOSIÇÃO DE COBRANÇA
		BigDecimal totalComposicao = BigDecimal.ZERO;
		for(ComposicaoCobrancaSlipDTO item:listaComposicaoCobranca){
			if (item.getOperacaoFinanceira().equals("D")){
				totalComposicao = totalComposicao.add(item.getValor());
			}
			else{
				totalComposicao = totalComposicao.subtract(item.getValor());
			}
		}
		totalComposicao = totalComposicao.add(slip.getValorSlip());
		parameters.put("VALOR_TOTAL_PAGAR", totalComposicao);
		
		
		URL subReportDir = Thread.currentThread().getContextClassLoader().getResource("/reports/");
		try{
		    parameters.put("SUBREPORT_DIR", subReportDir.toURI().getPath());
		}
		catch(Exception e){
			e.printStackTrace();
		}


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
	
}
