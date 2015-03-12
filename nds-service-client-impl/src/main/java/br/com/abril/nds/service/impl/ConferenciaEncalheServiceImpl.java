package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DadosDocumentacaoConfEncalheCotaDTO;
import br.com.abril.nds.dto.DataCEConferivelDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.MovimentoEstoqueDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
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
import br.com.abril.nds.model.estoque.ConferenciaEncalheBackup;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.Semaforo;
import br.com.abril.nds.model.estoque.StatusProcessoEncalhe;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ValoresAplicados;
import br.com.abril.nds.model.financeiro.Cobranca;
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
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.DebitoCreditoCota;
import br.com.abril.nds.model.movimentacao.ProdutoEdicaoSlip;
import br.com.abril.nds.model.movimentacao.Slip;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.CobrancaControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ConferenciaEncalheBackupRepository;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.CotaUnificacaoRepository;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.FechamentoEncalheRepository;
import br.com.abril.nds.repository.FeriadoRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.NotaFiscalEntradaRepository;
import br.com.abril.nds.repository.ParametroEmissaoNotaFiscalRepository;
import br.com.abril.nds.repository.ParametrosDistribuidorEmissaoDocumentoRepository;
import br.com.abril.nds.repository.PeriodoLancamentoParcialRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.repository.SemaforoRepository;
import br.com.abril.nds.repository.SlipRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DebitoCreditoCotaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.GrupoService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.NegociacaoDividaService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
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
	private DocumentoCobrancaService documentoCobrancaService;
	
	@Autowired
	private ParametroEmissaoNotaFiscalRepository parametroEmissaoNotaFiscalRepository;

	@Autowired
	private TipoNotaFiscalRepository tipoNotaFiscalRepository;
	
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
	private NegociacaoDividaService negociacaoDividaService;
	
	@Autowired
	private FechamentoEncalheRepository fechamentoEncalheRepository;
	
	@Autowired
	private ParametrosDistribuidorEmissaoDocumentoRepository parametrosDistribuidorEmissaoDocumentoRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private GrupoService grupoService;

	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private DebitoCreditoCotaService debitoCreditoCotaService;
	
	@Autowired
	private CotaUnificacaoRepository cotaUnificacaoRepository;
	
	@Autowired
	private PeriodoLancamentoParcialRepository periodoLancamentoParcialRepository;
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	@Autowired
	private DistribuicaoFornecedorRepository distribuicaoFornecedorRepository;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private SemaforoRepository semaforoRepository;
	
	@Autowired
	private ConferenciaEncalheBackupRepository conferenciaEncalheBackupRepository;
	
	@Autowired
	private SlipRepository slipRepository;
	
	@Autowired
    private FormaCobrancaService formaCobrancaService;
	
	@Autowired
	FeriadoRepository feriadoRepository;
	
	private final int PRIMEIRO_DIA_RECOLHIMENTO = 1;
	
	private final int QUANTIDADE_DIAS_UTEIS = 5;
	
	/**
	 * Carrega uma mapa tendo como chave os id de fornecedores do sistemas
	 * e valor um objeto que contém listas de datas de encalhe possíveis 
	 * para a cota e dataOperação e me questão (por id de fornecedor que
	 * é chave).
	 * 
	 * @param numeroCota
	 * 
	 * @return Map<Long, DataCEConferivelDTO>
	 */
	@Override
	@Transactional
	public Map<Long, DataCEConferivelDTO> obterDatasChamadaEncalheConferiveis(final Integer numeroCota) {
		
		final Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		final List<Long> listaIdFornecedor = fornecedorRepository.obterIdFornecedores();
		
		final Map<Long, DataCEConferivelDTO> mapaForncedorDatasEncalhe = new HashMap<>();
		
		final boolean indCotaOperacaoDiferenciada = cotaService.isCotaOperacaoDiferenciada(numeroCota, dataOperacao);
		
		for(final Long idFornecedor : listaIdFornecedor) {
			
			final DataCEConferivelDTO dataCEConferivel = new DataCEConferivelDTO();
			
			carregarObjetoDataCEConferivel(dataCEConferivel, numeroCota, idFornecedor, dataOperacao, indCotaOperacaoDiferenciada);
			
			if( !dataCEConferivel.getListaDataConferivelProdutoNaoParcial().isEmpty() ||
				!dataCEConferivel.getListaDataConferivelProdutoParcial().isEmpty() ){
				mapaForncedorDatasEncalhe.put(idFornecedor, dataCEConferivel);
			}
			
			
		}
		
		return mapaForncedorDatasEncalhe;
		
	}
	
	/**
	 * Adiciona a dataCE na listaDataConferivelProdutoNaoParcial (do objeto dataCEConferivel) :
	 * Caso esta dataCE represente 1º, 2º, 3º, 4º ou 5º dia de recolhimento de acordo com a dataOperacao
	 * 
	 * 
	 * Adiciona a dataCE na listaDataConferivelProdutoParcial (do objeto dataCEConferivel) :
	 * Caso esta dataCE represente 1º dia de recolhimento de acordo com a dataOperacao
	 * 
	 * 
	 * @param dataCEConferivel
	 * @param listaDiasRecolheAtrasado
	 * @param dataOperacao
	 * @param dataCE
	 * @param numeroCota
	 * @param listaIdFornecedor
	 * @param isCotaOperacaoDiferenciada TODO
	 */
	private void carregarDatasConferiveis(
			final DataCEConferivelDTO dataCEConferivel,
			final List<Integer> listaDiasRecolheAtrasado,
			final Date dataOperacao, 
			final Date dataCE, 
			final Integer numeroCota, 
			final List<Long> listaIdFornecedor,
			boolean isCotaOperacaoDiferenciada) {
		
		final int dia = distribuidorService.obterDiaDeRecolhimentoDaData(dataOperacao, dataCE, numeroCota, null, listaIdFornecedor, isCotaOperacaoDiferenciada);
		
		if(listaDiasRecolheAtrasado.contains(dia)) {
			
			if(!dataCEConferivel.getListaDataConferivelProdutoNaoParcial().contains(DateUtil.removerTimestamp(dataCE))) {
				dataCEConferivel.getListaDataConferivelProdutoNaoParcial().add(DateUtil.removerTimestamp(dataCE));
			}
			
			if(PRIMEIRO_DIA_RECOLHIMENTO==dia) {
				
				if(!dataCEConferivel.getListaDataConferivelProdutoParcial().contains(DateUtil.removerTimestamp(dataCE))) {
					dataCEConferivel.getListaDataConferivelProdutoParcial().add(DateUtil.removerTimestamp(dataCE));
				}
				
			}
		}
		
	}
	
	private void carregarObjetoDataCEConferivel(
			final DataCEConferivelDTO dataCEConferivel,
			final Integer numeroCota,
			final Long idFornecedor,
			final Date dataOperacao,
			final boolean indCotaOperacaoDiferenciada) {
		
		dataCEConferivel.setListaDataConferivelProdutoParcial(new ArrayList<Date>());
		dataCEConferivel.setListaDataConferivelProdutoNaoParcial(new ArrayList<Date>());
		
		final List<Long> listaIdFornecedor = Arrays.asList(idFornecedor);
		
		final List<Integer> diasSemanaDistribuidorOpera = this.distribuicaoFornecedorRepository.obterCodigosDiaDistribuicaoFornecedor(
				OperacaoDistribuidor.RECOLHIMENTO, idFornecedor);
		
		final List<Integer> listaDiasRecolheAtrasado = distribuidorService.getListaDiaOrdinalAceitaRecolhimento();
		
		carregarDatasConferiveis(dataCEConferivel, listaDiasRecolheAtrasado, dataOperacao, dataOperacao, numeroCota, listaIdFornecedor, indCotaOperacaoDiferenciada);
		
		if(diasSemanaDistribuidorOpera.isEmpty()){
		    throw new ValidacaoException(TipoMensagem.ERROR, "Existe fornecedor com dia de recolhimento não cadastrado.");
		}
		
		final List<Date> datasAnteriores = distribuidorService.obterListaDataOperacional(dataOperacao, QUANTIDADE_DIAS_UTEIS, diasSemanaDistribuidorOpera, false);
		
		if(datasAnteriores!=null && !datasAnteriores.isEmpty()) {
			for(final Date dataAnterior : datasAnteriores) {
				carregarDatasConferiveis(dataCEConferivel, listaDiasRecolheAtrasado, dataOperacao, dataAnterior, numeroCota, listaIdFornecedor, false);
			}
		}

		if(!indCotaOperacaoDiferenciada) {
			return;
		}
		
		final List<Date> datasPosteriores = distribuidorService.obterListaDataOperacional(
					dataOperacao, 
					QUANTIDADE_DIAS_UTEIS, 
					diasSemanaDistribuidorOpera, 
					true);
			
		
		if(datasPosteriores!=null && !datasPosteriores.isEmpty()) {
			
			for(final Date dataPosterior : datasPosteriores) {
				carregarDatasConferiveis(dataCEConferivel, listaDiasRecolheAtrasado, dataOperacao, dataPosterior, numeroCota, listaIdFornecedor, false);
			}
			
		}
		
	}
	
	
	@Override
	@Transactional
	public boolean isCotaEmiteNfe(final Integer numeroCota) {

		
		final Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota);

		if (cota == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
 "Cota não encontrada.");
		}

		final boolean indEmiteNfe = (cota.getParametrosCotaNotaFiscalEletronica() != null && cota
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
	@Override
	@Transactional(readOnly = true)
	public List<Box> obterListaBoxEncalhe(final Long idUsuario) {
	
		final List<Box> listaBoxEncalhe = boxRepository.obterListaBox(TipoBox.ENCALHE);
		
		if (idUsuario == null){
			
			return listaBoxEncalhe;
		}
		
		final Integer codigoBoxPadraoUsuario = this.obterBoxPadraoUsuario(idUsuario);
		
		if (codigoBoxPadraoUsuario == null){
			
			return listaBoxEncalhe;
			
		}
		
		final List<Box> boxes = new ArrayList<Box>();
			
		for (final Box box : listaBoxEncalhe){
			
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
	private Integer obterBoxPadraoUsuario(final Long idUsuario) {
		
		if (idUsuario == null){
			
			return null;
		}
		
		return this.boxRepository.obterCodigoBoxPadraoUsuario(idUsuario);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#validarFechamentoEncalheRealizado()
	 */
	@Override
	@Transactional(readOnly = true)
	public void validarFechamentoEncalheRealizado() throws FechamentoEncalheRealizadoException {
	
		final Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		final boolean indFechamentoEncalhe = fechamentoEncalheRepository.buscaControleFechamentoEncalhe(dataOperacao);
		
		if(indFechamentoEncalhe) {
			throw new FechamentoEncalheRealizadoException(
                    "Não é possível realizar nova conferência para data de operação [ "
                        + DateUtil.formatarDataPTBR(dataOperacao) + "].  \n"
                        + "Fechamento de encalhe já foi realizado. ");
			
		}
		
	}

	/**
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#verificarCotaComConferenciaEncalheFinalizada(java.lang.Integer)
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean verificarCotaComConferenciaEncalheFinalizada(final Integer numeroCota) {
		
		final Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		final ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				controleConferenciaEncalheCotaRepository.obterControleConferenciaEncalheCota(numeroCota, dataOperacao);
		
		if(controleConferenciaEncalheCota != null && StatusOperacao.CONCLUIDO.equals(controleConferenciaEncalheCota.getStatus())) {
			return true;
		}
		
		return false;
	}
	
	    /**
     * Obtém a quantidade de itens existentes no EstoqueProdutoEdicao da Cota de
     * determinado ProdutoEdicao que ainda não foram devolvidos.
     * 
     */
	private BigInteger obterQtdItensEstoqueProdutoEdicaoDaCotaNaoDevolvidos(final Long idCota, final Long idProdutoEdicao) {
		
		return estoqueProdutoCotaRepository.obterTotalEmEstoqueProdutoCota(idCota, idProdutoEdicao);
		
	}
	
	private class DiaSemanaRecolhimento {
		
		public Date data;
		public DiaSemana diaSemana;
		public boolean indOperacaoDiferenciada;
		public boolean indDiaProgramadoRecolhimento;
		public boolean indPrimeiroDiaRecolhimento;
		
	}
	
	/**
     * Valida a existência de chamada de encalhe de acordo com a cota de
     * operação diferenciada e produtoEdicao cuja dataRecolhimento esteja dentro
     * da faixa aceitavel para cota de operação diferenciada.
     * 
     * @param cota
     * @param produtoEdicao
     * 
     * @return ChamadaEncalheCota
     */
	private ChamadaEncalheCota validarChamadaEncalheOperacaoDiferenciada(final Cota cota, final ProdutoEdicao produtoEdicao) {
		
		final Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		final Integer numeroCota = cota.getNumeroCota();
		
		final ChamadaEncalheCota cec = obterChamadaEncalheParaCotaProdutoEdicao(cota, produtoEdicao);
		final ChamadaEncalhe ce = cec.getChamadaEncalhe();
		
		final Date dataRecolhimentoCE = ce.getDataRecolhimento();
		
		final Date primeiroDiaEncalheOperacaoDiferenciada = obterDataPrimeiroDiaEncalheOperacaoDiferenciada(numeroCota, dataRecolhimentoCE);
		
		boolean aceitaRecolhimentoParcialAtraso = distribuidorService.distribuidorAceitaRecolhimentoParcialAtraso();
		
		if(produtoEdicao.isParcial() &&
		   !aceitaRecolhimentoParcialAtraso && 
		   !isPeriodoLancamentoRecolhimentoFinal(produtoEdicao.getId(), cota.getId(), distribuidorService.obterDataOperacaoDistribuidor())) {
			
			if(primeiroDiaEncalheOperacaoDiferenciada.compareTo(dataOperacao)!=0) {

				final ProdutoEdicao pEdicao = produtoEdicaoRepository.buscarPorId(produtoEdicao.getId());
				
				if(pEdicao == null) {
                    throw new ValidacaoException(TipoMensagem.ERROR, "Produto edição não encontrado");
				}
				
				final String nomeProdutoEdicao = pEdicao.getProduto().getNome();
				
				throw new ValidacaoException(
						TipoMensagem.WARNING, 
                        " Não é possível realizar a conferência do produto edição parcial ["
                            + nomeProdutoEdicao
                            + "]. "
                            + " Data de operação excedendo ou fora do primeiro dia de recolhimento de operação diferenciada. ");
				
			}
			
			
		} else {
			
			isDataRecolhimentoValida(dataOperacao, primeiroDiaEncalheOperacaoDiferenciada, produtoEdicao.getId(), true);
			
		}
		
		
		return cec;
		
	}

	@Override
	@Transactional(readOnly=true)
	public Date obterDataPrimeiroDiaEncalheOperacaoDiferenciada(final Integer numeroCota, final Date dataRecolhimentoCE) {
		
		final DiaSemana inicioSemana = distribuidorService.inicioSemanaRecolhimento();
		
		final List<DiaSemanaRecolhimento> diasSemanaRecolhimentoOperacaoDiferenciada = 
				obterListaDiaSemanaRecolhimentoOperacaoDiferenciada(
						numeroCota, 
						dataRecolhimentoCE, 
						inicioSemana.getCodigoDiaSemana());
		
		identificarPrimeiroDiaRecolhimentoOperacaoDiferenciada(diasSemanaRecolhimentoOperacaoDiferenciada);
		
		Date primeiraDataRecolhimento = null;
		
		for(final DiaSemanaRecolhimento dia : diasSemanaRecolhimentoOperacaoDiferenciada) {
			if(dia.indPrimeiroDiaRecolhimento){
				primeiraDataRecolhimento = dia.data;
				break;
			}
		}
		
		return primeiraDataRecolhimento;
	}
	
	/**
	 * Identifica o primeiro qual o primeiro dia de recolhimento na semana 
	 * da cota de operacao diferenciada.
	 * 
	 * @param diasSemanaRecolhimentoOperacaoDiferenciada
	 */
	private void identificarPrimeiroDiaRecolhimentoOperacaoDiferenciada(final List<DiaSemanaRecolhimento> diasSemanaRecolhimentoOperacaoDiferenciada) {
		
        // Se o dia programado de recolhimento for também dia de operação
        // diferenciada então este
        // é o primeiro dia de recolhimento
		for(final DiaSemanaRecolhimento dia : diasSemanaRecolhimentoOperacaoDiferenciada) {
			if(dia.indDiaProgramadoRecolhimento && dia.indOperacaoDiferenciada) {
				dia.indPrimeiroDiaRecolhimento = true;
				return;
			}
		}
		
		int counter = 0;
		int ownerPosition = -1;
		boolean ceFound = false;
		
		Collections.reverse(diasSemanaRecolhimentoOperacaoDiferenciada);
		
		for(final DiaSemanaRecolhimento dia : diasSemanaRecolhimentoOperacaoDiferenciada) {
			
			if(dia.indDiaProgramadoRecolhimento) {
				ceFound = true;
			}
			
			if(dia.indOperacaoDiferenciada) {
				ownerPosition = counter;
			}
			
			if(ceFound && ownerPosition!=-1){
				break;
			}
			
			++counter;
			
		}
		
		diasSemanaRecolhimentoOperacaoDiferenciada.get(ownerPosition).indPrimeiroDiaRecolhimento = true;
		
		Collections.reverse(diasSemanaRecolhimentoOperacaoDiferenciada);
		
	}
	
	    /**
     * Retorna uma lista ordenada de objetos com informações sobre as datas da
     * semana de recolhimento da operação diferenciada sendo esta semana baseada
     * na data de recolhimento informada por parâmetro.
     * 
     * @param numeroCota
     * @param dataRecolhimentoCE
     * @param primeiroDiaDaSemanaDistribuidor
     * 
     * @return List - DiaSemanaRecolhimento
     */
	private List<DiaSemanaRecolhimento> obterListaDiaSemanaRecolhimentoOperacaoDiferenciada(
			final Integer numeroCota, 
			final Date dataRecolhimentoCE, 
			final Integer primeiroDiaDaSemanaDistribuidor) {
		
		final Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		List<DiaSemana> diasSemanaOperacaoDiferenciada = grupoRepository.obterDiasOperacaoDiferenciadaCota(numeroCota, dataOperacao);
		
		final List<DiaSemanaRecolhimento> listaDiaSemanaRecolhimento = new ArrayList<>();

		final Calendar cData = Calendar.getInstance();
		cData.setTime(dataRecolhimentoCE);
		cData.setFirstDayOfWeek(primeiroDiaDaSemanaDistribuidor);
		
		final Integer diaSemanaRecolhimentoProgramado = cData.get(Calendar.DAY_OF_WEEK);
		
		for(final DiaSemana dia : DiaSemana.values()) {
			
			cData.set(Calendar.DAY_OF_WEEK, dia.getCodigoDiaSemana());
			
			final DiaSemanaRecolhimento diaSemanaRecolhimento = new DiaSemanaRecolhimento();
			diaSemanaRecolhimento.diaSemana = dia;
			diaSemanaRecolhimento.data = cData.getTime();
			diaSemanaRecolhimento.indDiaProgramadoRecolhimento = (diaSemanaRecolhimentoProgramado.intValue() == dia.getCodigoDiaSemana());
			diaSemanaRecolhimento.indOperacaoDiferenciada = diasSemanaOperacaoDiferenciada.contains(dia);
			diaSemanaRecolhimento.indPrimeiroDiaRecolhimento = false;
			
			listaDiaSemanaRecolhimento.add(diaSemanaRecolhimento);
			
		}
		
		Collections.sort(listaDiaSemanaRecolhimento, new Comparator<DiaSemanaRecolhimento>(){

			@Override
			public int compare(final DiaSemanaRecolhimento o1,
					final DiaSemanaRecolhimento o2) {
				return o1.data.compareTo(o2.data);
			}
			
		});
		
		
		return listaDiaSemanaRecolhimento;

	}
	
	    /**
     * Retorna a chamada de encalhe do produto edição e cota em questão.
     * 
     * @param cota
     * @param produtoEdicao
     * 
     * @return ChamadaEncalheCota
     */
	private ChamadaEncalheCota obterChamadaEncalheParaCotaProdutoEdicao(final Cota cota, final ProdutoEdicao produtoEdicao) {
		
		final boolean postergado = false;
		final Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		ChamadaEncalheCota chamadaEncalheCota = null;
		
		if(produtoEdicao.isParcial()) {
			
			Date dataEncalhe =
					chamadaEncalheCotaRepository.obterDataChamadaEncalheCotaProximaDataOperacao(cota, 
																		produtoEdicao.getId(), 
																		postergado,
																		dataOperacao);
			
			if(dataEncalhe == null) {
				
				throw new ValidacaoException(
						TipoMensagem.WARNING, 
	                    " Não é possível realizar a conferência do produto edição [" + produtoEdicao.getNomeComercial()
	                        + "] da cota. " + " Este produto edição não possui CE. ");
			}
			
			chamadaEncalheCota = chamadaEncalheCotaRepository.obterChamadaEncalheCotaNaData(cota, produtoEdicao.getId(), postergado, dataEncalhe);
			
		
		} else {
			chamadaEncalheCota =
					chamadaEncalheCotaRepository.obterUltimaChamaEncalheCota(cota, 
																			produtoEdicao.getId(), 
																			postergado,
																			dataOperacao);
		}
		
		
		if(chamadaEncalheCota == null){
			
			throw new ValidacaoException(
					TipoMensagem.WARNING, 
                    " Não é possível realizar a conferência do produto edição [" + produtoEdicao.getNomeComercial()
                        + "] da cota. " + " Este produto edição não possui CE. ");
		
		} 
		
		this.validarLancamentoFechado(produtoEdicao, chamadaEncalheCota);
		
		return chamadaEncalheCota;
	}

	private void validarLancamentoFechado(final ProdutoEdicao produtoEdicao, final ChamadaEncalheCota chamadaEncalheCota) {
        
        Iterator<Lancamento> it = chamadaEncalheCota.getChamadaEncalhe().getLancamentos().iterator();
        
        Lancamento lancamento = it.next();
        
        if (lancamento.getStatus().equals(StatusLancamento.FECHADO)) {
            
            throw new ValidacaoException(
                TipoMensagem.WARNING, 
                "Não é possível realizar a conferência do produto edição [" + produtoEdicao.getNomeComercial()
                    + "] da cota. " + " Este produto já está fechado.");
        }
    }
	
	/**
     * Retorna a chamada de encalhe do produto edição parcial e cota em questão.
     * 
     * @param cota
     * @param produtoEdicao
     * 
     * @return ChamadaEncalheCota
     */
	private ChamadaEncalheCota obterChamadaEncalheParaCotaProdutoEdicaoParcial(final Cota cota, final ProdutoEdicao produtoEdicao) {
		
		final boolean postergado = false;
		final Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		ChamadaEncalheCota chamadaEncalheCota = null;
			
		chamadaEncalheCota = 
				chamadaEncalheCotaRepository.obterChamadaEncalheCotaNaData(cota, produtoEdicao.getId(),postergado,dataOperacao);
		
		if(chamadaEncalheCota == null ) {
			
			throw new ValidacaoException(
					TipoMensagem.WARNING, 
                    " Não é possível realizar a conferência do produto edição [" + produtoEdicao.getNomeComercial()
                        + "] da cota. " + " Este produto edição não possui CE. ");
			
		}
			
		
		return chamadaEncalheCota;
		
	}
	
	
	    /**
     * Valida a existência de chamada de encalhe de acordo com a cota e
     * produtoEdicao cuja dataRecolhimento esteja dentro da faixa aceitavel (de
     * acordo com parâmetro do Distribuidor e dataOperacao atual).
     * 
     * Obs.: A conferência de encalhe de ProdutoEdicao parcial só será possível
     * se houver chamadaEncalhe para o mesmo na dataOperacao atual, senão sera
     * lançada EncalheRecolhimentoParcialException.
     * 
     * Se encontrada, será retornada esta chamadaEncalhe para o produtoEdicao em
     * questão.
     * 
     * @param cota
     * @param produtoEdicao
     * 
     * @return ChamadaEncalhe
     */
	private ChamadaEncalheCota validarChamadaEncalheParaCotaProdutoEdicao(final Cota cota, final ProdutoEdicao produtoEdicao) {
		
		ChamadaEncalheCota chamadaEncalheCota = null;
		
		boolean aceitaRecolhimentoParcialAtraso = distribuidorService.distribuidorAceitaRecolhimentoParcialAtraso();
		
		if(	produtoEdicao.isParcial() && 
			!aceitaRecolhimentoParcialAtraso &&
			!isPeriodoLancamentoRecolhimentoFinal(produtoEdicao.getId(), cota.getId(), distribuidorService.obterDataOperacaoDistribuidor())) {
			chamadaEncalheCota = obterChamadaEncalheParaCotaProdutoEdicaoParcial(cota, produtoEdicao);
		} else {
			chamadaEncalheCota = obterChamadaEncalheParaCotaProdutoEdicao(cota, produtoEdicao);
		}

		
		
		final Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		if(produtoEdicao.isParcial() && !aceitaRecolhimentoParcialAtraso) {
		
			return chamadaEncalheCota;
			
		} else {
		
			isDataRecolhimentoValida(dataOperacao, chamadaEncalheCota.getChamadaEncalhe().getDataRecolhimento(), produtoEdicao.getId(), false);
			
		}	
		
		return chamadaEncalheCota;
		
	}
	
	    /**
     * Verifica se o periodo do lançamento do produto edição em recolhimento é
     * FINAL
     * 
     * @param idProdutoEdicao - identificador do produto edição
     * @param idCota - identificador da cota
     * @param dataRecolhimento - data de recolhimento do produto
     * @return boolean
     */
	private boolean isPeriodoLancamentoRecolhimentoFinal(final Long idProdutoEdicao, final Long idCota, final Date dataRecolhimento){
		
		return periodoLancamentoParcialRepository.isLancamentoConferenciaEncalheCotaPeriodoFinal(idProdutoEdicao, idCota, dataRecolhimento);
	}
	
	    /**
     * Valida se o produto edição (de acordo com sua data de recolhimento
     * encontrada) pode ser recolhido na data de operação informada.
     * 
     * @param dataOperacao
     * @param dataPrimeiroRecolhimento
     * @param idProdutoEdicao
     * @param indOperacaoDiferenciada
     */
	@Override
	@Transactional(readOnly = true)
	public void isDataRecolhimentoValida(final Date dataOperacao, final Date dataPrimeiroRecolhimento, final Long idProdutoEdicao, final boolean indOperacaoDiferenciada) {
		
		final ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		if(produtoEdicao == null){
            throw new ValidacaoException(TipoMensagem.ERROR, "Produto edição não encontrado");
		}
		
		final String nomeProdutoEdicao = produtoEdicao.getProduto().getNome();
	
		if(!indOperacaoDiferenciada) {
			
			final boolean recolhimentoMaiorQueDataOperacao = dataPrimeiroRecolhimento.compareTo(dataOperacao) > 0;

			if(recolhimentoMaiorQueDataOperacao) {
					
				throw new ValidacaoException(
						TipoMensagem.WARNING, 
                        "Não é possível realizar a conferência do produto edição [" + nomeProdutoEdicao + "]. <br> "
                            + "Não é permitida antecipação de produtos pelo distribuidor. ");
						
			}
			
		}
		
		final List<Date> datasRecolhimento = 
				distribuidorService.obterDatasAposFinalizacaoPrazoRecolhimento(dataPrimeiroRecolhimento,
																			   this.obterIdsFornecedorDoProduto(produtoEdicao));
		if(datasRecolhimento == null || datasRecolhimento.isEmpty()){
			throw new ValidacaoException(
					TipoMensagem.WARNING, 
                    " Distribuidor não possui parametrização de dias de recolhimento para o "
                        + "<br> fornecedor do produto edição [" + nomeProdutoEdicao + "].");
		}
		
		for(final Date item : datasRecolhimento){
			
			if(item.compareTo(dataOperacao)==0){
				return;
			}
			
		}
		
		if(indOperacaoDiferenciada) {
			throw new ValidacaoException(
					TipoMensagem.WARNING, 
                    " Não é possível realizar a conferência do produto edição [" + nomeProdutoEdicao + "]. <br>"
                        + " Data de operação excedendo ou fora dos dias de recolhimento de operação diferenciada. ");
		} else {
			throw new ValidacaoException(
					TipoMensagem.WARNING, 
                    " Não é possível realizar a conferência do produto edição [" + nomeProdutoEdicao + "]. <br>"
                        + " Data de operação excedendo ou fora dos dias de recolhimento possíveis. ");
		}

	}
	
	@Override
	@Transactional
	public Long[] obterIdsFornecedorDoProduto(final ProdutoEdicao produtoEdicao){
		
		final Set<Fornecedor> fornecedores = produtoEdicao.getProduto().getFornecedores();
		
		final Long[] idsFornecedor = new Long[fornecedores.size()];
		
		int indice = 0;
		
		for(final Fornecedor item : fornecedores){
			idsFornecedor[indice++] = item.getId();
		}
		
		return idsFornecedor;
	}
	
	
	@Override
    @Transactional(readOnly = true)
    public boolean isParcialNaoFinal(final Long idProdutoEdicao) {
        
        return this.conferenciaEncalheRepository.isParcialNaoFinal(idProdutoEdicao);
    }
	
	    /**
     * Obtém lista de conferenciaEncalhe com os produtosEdicao que fazem parte
     * da chamaEncalhe atual para a cota em questão ou que estejam dentro da
     * semana de recolhimento. Caso uma operação de conferencia de encalhe
     * esteja sendo realizada, serão adicionados apenas produtosEdicao ainda não
     * tenham sido adicionados a lista de conferencia de encalhe existente.
     * 
     * @param idControleConferenciaEncalheCota
     * @param listaConferenciaEncalhe
     */
	private List<ConferenciaEncalheDTO> obterListaConferenciaEncalheContingencia(
			Date dataOperacao,
			final Integer numeroCota,
			final List<Date> datasRecolhimento,
			final List<ConferenciaEncalheDTO> listaConferenciaEncalhe) {
		
		final Set<Long> listaIdProdutoEdicao = new HashSet<Long>();
		
		if(listaConferenciaEncalhe!=null && !listaConferenciaEncalhe.isEmpty()) {
			
			for(final ConferenciaEncalheDTO conferencia : listaConferenciaEncalhe) {
				
				listaIdProdutoEdicao.add(conferencia.getIdProdutoEdicao());
				
			}
			
		}
		
		final boolean indFechado = false;
		final boolean indPostergado = false;
		
		long idInicial = System.currentTimeMillis();
		idInicial = (idInicial - (1000000));
		
		
		final List<ConferenciaEncalheDTO> listaConferenciaEncalheContingencia = 
			conferenciaEncalheRepository.obterListaConferenciaEncalheDTOContingencia(
				numeroCota, 
				datasRecolhimento, 
				indFechado, 
				indPostergado, 
				listaIdProdutoEdicao);
		
		for(final ConferenciaEncalheDTO conferencia : listaConferenciaEncalheContingencia) {
			final long id = (-1 * (idInicial++));
			
			conferencia.setIdConferenciaEncalhe(Long.valueOf(id));
			
			final Integer diaRecolhimento = this.distribuidorService.obterDiaDeRecolhimentoDaData(dataOperacao, 
					conferencia.getDataRecolhimento(),
                    numeroCota,
                    conferencia.getIdProdutoEdicao(), null, null);
			
			conferencia.setDia(diaRecolhimento);
			
		}
		
		return listaConferenciaEncalheContingencia;
	}
	
	@Transactional
	@Override
    public BigDecimal obterValorTotalDesconto(final Integer numeroCota, final Date dataOperacao) {
		
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
    public BigDecimal obterValorTotalReparteSemDesconto(final Integer numeroCota, final Date dataOperacao) {
		
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
    public BigDecimal obterValorTotalReparte(final Integer numeroCota, final List<Date> datas) {
		
		if(datas == null || datas.isEmpty()) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal reparte = chamadaEncalheCotaRepository.obterReparteDaChamaEncalheCota(numeroCota, datas, false, false);
		
		if (reparte == null) {
			
			reparte = BigDecimal.ZERO;
		}
		
		return reparte;
	}

	private String obterDescricaoDiasSemana(final List<DiaSemana> diasSemana) {
		
		final StringBuilder descricao = new StringBuilder();
		
		int counter = 0;
		
		for(final DiaSemana d : diasSemana) {
			descricao.append(d.getDescricaoDiaSemana());
			descricao.append(++counter==diasSemana.size() ? "" : ", ");
		}
		
		return descricao.toString();
		
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public void verificarCotaOperacaoDiferenciada(final Integer numeroCota) {
		
		final Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		List<DiaSemana> diasSemanaOperacaoDiferenciada = grupoRepository.obterDiasOperacaoDiferenciadaCota(numeroCota, dataOperacao);
		
		final DiaSemana diaSemanaDataOperacao = DiaSemana.getByCodigoDiaSemana(DateUtil.obterDiaDaSemana(dataOperacao));
		
		if(diasSemanaOperacaoDiferenciada==null) {
			return;
		}
		
		if(diasSemanaOperacaoDiferenciada.contains(diaSemanaDataOperacao)) {
			return;
		}
		
		throw new ValidacaoException(TipoMensagem.WARNING, 
                " Cota possui operação difenciada, pode ser operada apenas: "
                    + obterDescricaoDiasSemana(diasSemanaOperacaoDiferenciada));
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean isCotaComReparteARecolherNaDataOperacao(final Integer numeroCota, final List<Date> datas) {
		
		final BigDecimal valorTotal = obterValorTotalReparte(numeroCota, datas);
		
		if(BigDecimal.ZERO.compareTo(valorTotal) < 0) {
			return true;
		}
		
		return false;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean hasCotaAusenteFechamentoEncalhe(final Integer numeroCota) {

		final Date dataRecolhimento = distribuidorService.obterDataOperacaoDistribuidor();

		final BigInteger quantidadeCotasAusentes = this.chamadaEncalheCotaRepository.quantidadeCotaAusenteFechamentoEncalhe(numeroCota, dataRecolhimento);

		return quantidadeCotasAusentes.compareTo(BigInteger.ZERO) > 0;
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public InfoConferenciaEncalheCota obterInfoConferenciaEncalheCota(final Integer numeroCota, final boolean indConferenciaContingencia) {
		
		final Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();

		final ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				controleConferenciaEncalheCotaRepository.obterControleConferenciaEncalheCota(numeroCota, dataOperacao);
		
		final InfoConferenciaEncalheCota infoConfereciaEncalheCota = new InfoConferenciaEncalheCota();
		
		if(controleConferenciaEncalheCota != null) {
			infoConfereciaEncalheCota.setEncalhe(BigDecimal.ZERO);
			infoConfereciaEncalheCota.setIdControleConferenciaEncalheCota(controleConferenciaEncalheCota.getId());
			infoConfereciaEncalheCota.setNotaFiscalEntradaCota(controleConferenciaEncalheCota.getNotaFiscalEntradaCotaPricipal());
		} else {
			infoConfereciaEncalheCota.setEncalhe(BigDecimal.ZERO);
		}
		
		final List<Date> datasRecolhimento = this.grupoService.obterDatasRecolhimentoOperacaoDiferenciada(numeroCota, dataOperacao);
		
		carregarListaConferenciaEncalhe(
				infoConfereciaEncalheCota, 
				dataOperacao,
				numeroCota,
				controleConferenciaEncalheCota == null ? null : controleConferenciaEncalheCota.getId(), 
				indConferenciaContingencia, 
				datasRecolhimento);
		
		final Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota);
		
		this.debitoCreditoCotaService.carregarDadosDebitoCreditoDaCota(infoConfereciaEncalheCota, cota, datasRecolhimento);
		
		infoConfereciaEncalheCota.setCota(cota);
		
		infoConfereciaEncalheCota.setReparte(obterValorTotalReparte(numeroCota, datasRecolhimento));
		
		// impl Erik Scaranello
		BigDecimal valorDebitoCreditoFinalizado = new BigDecimal(0);
		for(final DebitoCreditoCota debitoCredito : infoConfereciaEncalheCota.getListaDebitoCreditoCota()) {
			valorDebitoCreditoFinalizado = valorDebitoCreditoFinalizado.add(debitoCredito.getValor());
		}
		
		infoConfereciaEncalheCota.setTotalDebitoCreditoCota(valorDebitoCreditoFinalizado);
		
		infoConfereciaEncalheCota.setValorPagar(BigDecimal.ZERO);
		
		infoConfereciaEncalheCota.setValorVendaDia(BigDecimal.ZERO);
		
		infoConfereciaEncalheCota.setDistribuidorAceitaJuramentado(this.controleConferenciaEncalheCotaRepository.obterAceitaJuramentado(cota.getId()));
		
		infoConfereciaEncalheCota.setIndCotaOperacaoDiferenciada(cotaService.isCotaOperacaoDiferenciada(numeroCota, dataOperacao));
		
		return infoConfereciaEncalheCota;
	}
	
	private void carregarListaConferenciaEncalhe(
			InfoConferenciaEncalheCota info, 
			Date dataOperacao,
			Integer numeroCota,
			Long idControleConfEncalheCota, 
			boolean indConferenciaContingencia, 
			List<Date> datasRecolhimento) {
		
		boolean existeBackupConferenciaEncalhe = conferenciaEncalheBackupRepository.existemConferenciasEncalheBackupNaData(numeroCota, dataOperacao);
		
		if(existeBackupConferenciaEncalhe) {
			
			List<ConferenciaEncalheDTO> conferencias = conferenciaEncalheBackupRepository.obterDadosConferenciasEncalheBackup(numeroCota, dataOperacao);
			
			if(conferencias!=null) {
				info.setListaConferenciaEncalhe(new HashSet<ConferenciaEncalheDTO>(conferencias));
			}
			
			return;
			
		}
		
		List<ConferenciaEncalheDTO> listaConferenciaEncalheDTO = null;
		
		if(idControleConfEncalheCota != null) {
			
			listaConferenciaEncalheDTO = conferenciaEncalheRepository.obterListaConferenciaEncalheDTO(idControleConfEncalheCota);
			
			if(listaConferenciaEncalheDTO != null) {
				info.setListaConferenciaEncalhe(new HashSet<ConferenciaEncalheDTO>(listaConferenciaEncalheDTO));
			}
			
		} 
		
		if(info.getListaConferenciaEncalhe() == null) {
			info.setListaConferenciaEncalhe(new HashSet<ConferenciaEncalheDTO>());
		}
		
		if(indConferenciaContingencia) {
			
			List<ConferenciaEncalheDTO> listaConferenciaEncalheContingencia = null;
			
			if(datasRecolhimento!=null && !datasRecolhimento.isEmpty()) {
				listaConferenciaEncalheContingencia = obterListaConferenciaEncalheContingencia(
						dataOperacao, 
						numeroCota, 
						datasRecolhimento, new ArrayList<ConferenciaEncalheDTO>(info.getListaConferenciaEncalhe()));
			}
			
			if(listaConferenciaEncalheContingencia!=null && !listaConferenciaEncalheContingencia.isEmpty()) {
				
				carregarDiaRecolhimento(numeroCota, dataOperacao, listaConferenciaEncalheContingencia);
				
				info.getListaConferenciaEncalhe().addAll(listaConferenciaEncalheContingencia);
			
			}
			
		}
		
	}
	
	
	/**
	 * Carrega o dia de recolhimento nos itens de conferencia
	 * 
	 * @param numeroCota
	 * @param dataOperacao
	 * @param listaConferenciaEncalheDTO
	 */
	private void carregarDiaRecolhimento(
			Integer numeroCota,
			Date dataOperacao,
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe) {
		
		for(ConferenciaEncalheDTO conf : listaConferenciaEncalhe) {
			
			final Integer diaRecolhimento = this.distribuidorService.obterDiaDeRecolhimentoDaData(
					dataOperacao, 
					conf.getDataRecolhimento(),
	                numeroCota,
	                conf.getIdProdutoEdicao(), 
	                null, null);
			
			conf.setDia(diaRecolhimento);
			
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#obterDetalheConferenciaEncalhe(java.lang.Integer, java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional
	public ConferenciaEncalheDTO obterDetalheConferenciaEncalhe(final Integer numeroCota, final Long idConferenciaEncalhe, final Long idProdutoEdicao) {
		
		final ConferenciaEncalheDTO conferenciaEncalheDTO = new ConferenciaEncalheDTO();
		
		if(idConferenciaEncalhe!=null) {
		
			final ConferenciaEncalhe conferenciaEncalhe = conferenciaEncalheRepository.buscarPorId(idConferenciaEncalhe);
			
			if(conferenciaEncalhe == null) {
				
                throw new IllegalStateException("Conferência de encalhe não encontrada");
				
			}
			
			conferenciaEncalheDTO.setIdConferenciaEncalhe(conferenciaEncalhe.getId());
			
			conferenciaEncalheDTO.setObservacao(conferenciaEncalhe.getObservacao());
			
		}
		
		final Long idCota = cotaRepository.obterIdPorNumeroCota(numeroCota);
		final ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		conferenciaEncalheDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
		
		final BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
        conferenciaEncalheDTO.setPrecoCapa(precoVenda);
		
		final BigDecimal percentualDesconto = descontoService.obterValorDescontoPorCotaProdutoEdicao(null, idCota, produtoEdicao);

		final BigDecimal valorDesconto = MathUtil.calculatePercentageValue(precoVenda, percentualDesconto);

		conferenciaEncalheDTO.setDesconto(valorDesconto);
		
		conferenciaEncalheDTO.setPacotePadrao(produtoEdicao.getPacotePadrao());
		
		conferenciaEncalheDTO.setNomeProduto(produtoEdicao.getProduto().getNome());
		
		conferenciaEncalheDTO.setPossuiBrinde(produtoEdicao.isPossuiBrinde());
		
		conferenciaEncalheDTO.setNomeEditor(produtoEdicao.getProduto().getEditor().getPessoaJuridica().getNome());
		
		conferenciaEncalheDTO.setNomeEditor(produtoEdicao.getProduto().getFornecedor().getJuridica().getNome());
		
		return conferenciaEncalheDTO;
	}
	
	@Override
	@Transactional(readOnly = true)
	public ProdutoEdicaoDTO pesquisarProdutoEdicaoPorId(final Integer numeroCota, final Long idProdutoEdicao) throws EncalheRecolhimentoParcialException {
		
		
		if (numeroCota == null) {
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório.");
		}
		
		if (idProdutoEdicao == null) {
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Id Prdoduto Edição é obrigatório.");
		}
		
		final ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		ProdutoEdicaoDTO produtoEdicaoDTO = null;
		
		if (produtoEdicao != null){
		    
			final Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
			
			produtoEdicaoDTO = new ProdutoEdicaoDTO();
			
		    final Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota);
		    
	    	ChamadaEncalheCota chamadaEncalheCota = null;
	    	
	    	if(cotaService.isCotaOperacaoDiferenciada(cota.getNumeroCota(), dataOperacao)){
	    		chamadaEncalheCota = this.validarChamadaEncalheOperacaoDiferenciada(cota, produtoEdicao);
	    	} else {
				chamadaEncalheCota = this.validarChamadaEncalheParaCotaProdutoEdicao(cota, produtoEdicao);
	    	}
			
			if( chamadaEncalheCota != null) {
				
				final ChamadaEncalhe chamadaEncalhe = chamadaEncalheCota.getChamadaEncalhe();
				produtoEdicaoDTO.setDataRecolhimentoDistribuidor(chamadaEncalhe.getDataRecolhimento());
				produtoEdicaoDTO.setTipoChamadaEncalhe(chamadaEncalhe.getTipoChamadaEncalhe());
				produtoEdicaoDTO.setReparte(chamadaEncalheCota.getQtdePrevista());
			}
			else{
				
				atribuirDataRecolhimentoParaProdutoSemChamadaEncalhe(produtoEdicao, produtoEdicaoDTO);
			}
			
			produtoEdicaoDTO.setId(produtoEdicao.getId());
			produtoEdicaoDTO.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
			produtoEdicaoDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
			final BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
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
			
			GrupoProduto grupoProduto = produtoEdicao.getGrupoProduto()!=null?
					produtoEdicao.getGrupoProduto():
						produtoEdicao.getProduto().getTipoProduto()!=null?
								produtoEdicao.getProduto().getTipoProduto().getGrupoProduto()
								:null;
			
			produtoEdicaoDTO.setContagemPacote( (GrupoProduto.CROMO.equals(grupoProduto) || GrupoProduto.CARDS.equals(grupoProduto))  ? true : false );

		}
		
		return produtoEdicaoDTO;
	}
	
	    /*
     * Obtem a maior data de lançamnto de um produto edição
     */
	private void atribuirDataRecolhimentoParaProdutoSemChamadaEncalhe(final ProdutoEdicao produtoEdicao, final ProdutoEdicaoDTO produtoEdicaoDTO) {
		
		final Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		final Date dataRecolhimentoDistribuidor = lancamentoRepository.obterDataUltimoLancamento(produtoEdicao.getId(), dataOperacao);
		
		if(dataRecolhimentoDistribuidor == null) {
			
			throw new ValidacaoException(
					TipoMensagem.WARNING, 
 " O produto edição [ "
                + produtoEdicao.getNomeComercial() + " ] não possui C.E. ou data de recolhimento prevista, "
                + " portanto não poder adicionado a conferência de encalhe.");
			
		}
		
		produtoEdicaoDTO.setDataRecolhimentoDistribuidor(dataRecolhimentoDistribuidor);
		
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
	private void carregarValoresAplicadosProdutoEdicao( final ProdutoEdicaoDTO produtoEdicaoDTO, 
														final Integer numeroCota, 
														final Long idProdutoEdicao, 
														final Date dataOperacao) {

		final ValoresAplicados valoresAplicados =  movimentoEstoqueCotaRepository.obterValoresAplicadosProdutoEdicao(numeroCota, idProdutoEdicao, dataOperacao);
		
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

	private String obterEditor(final ProdutoEdicao produtoEdicao) {
		
		if(	produtoEdicao == null || 
			produtoEdicao.getProduto() == null ||
			produtoEdicao.getProduto().getEditor() == null ||
			produtoEdicao.getProduto().getEditor().getPessoaJuridica() == null ||
			produtoEdicao.getProduto().getEditor().getPessoaJuridica().getRazaoSocial() == null) {
			
			return "";
			
		}
		
		return produtoEdicao.getProduto().getEditor().getPessoaJuridica().getRazaoSocial();
	}
	
	private String obterNomeFornecedor(final ProdutoEdicao produtoEdicao) {
		
		final Fornecedor fornecedor = produtoEdicao.getProduto().getFornecedor();
		
		if (fornecedor != null) {
		
			return fornecedor.getJuridica().getRazaoSocial();
		}

		return null;
	}
	
	    /**
     * Retorna a dataRecolhimento referencia sendo esta igual a dataOperacao -
     * qtdDiasEncalheAtrasadoAceitavel(parâmetro do distribuidor).
     * 
     * @return dataRecolhimentoReferencia.
     */
	private Date obterDataRecolhimentoReferencia() {
		
		final Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		final int qtdDiasEncalheAtrasadoAceitavel = this.distribuidorService.qtdDiasEncalheAtrasadoAceitavel();
		
		final Date dataRecolhimentoReferencia = DateUtil.subtrairDias(dataOperacao, qtdDiasEncalheAtrasadoAceitavel);
		
		return dataRecolhimentoReferencia;
	}
	
	    /**
     * Associa a Cobrança relativa a uma operação
     * ControleConferenciaEncalheCota.
     */
	private void associarCobrancaConferenciaEncalheCota(final Long idControleConferenciaEncalheCota, final String nossoNumero) {
		
		final CobrancaControleConferenciaEncalheCota cobrancaControleConferenciaEncalheCota = 
				new CobrancaControleConferenciaEncalheCota();
		
		final Cobranca cobranca = cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
		
		final ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				controleConferenciaEncalheCotaRepository.buscarPorId(idControleConferenciaEncalheCota); 
		
		cobrancaControleConferenciaEncalheCota.setCobranca(cobranca);
		
		cobrancaControleConferenciaEncalheCota.setControleConferenciaEncalheCota(controleConferenciaEncalheCota);
		
		cobrancaControleConferenciaEncalheCotaRepository.adicionar(cobrancaControleConferenciaEncalheCota);

		
	}
	
	private void removerAssociacoesCobrancaConferenciaEncalheCota(final Long idControleConferenciaEncalheCota) {
		
		final List<CobrancaControleConferenciaEncalheCota> listaCobrancaControleConferenciaEncalheCota = 
				cobrancaControleConferenciaEncalheCotaRepository.obterCobrancaControleConferenciaEncalheCota(idControleConferenciaEncalheCota);
		
		if(listaCobrancaControleConferenciaEncalheCota!=null && !listaCobrancaControleConferenciaEncalheCota.isEmpty()) {
			
			for(final CobrancaControleConferenciaEncalheCota cobrancaControleConfEncCota :  listaCobrancaControleConferenciaEncalheCota) {
				
				cobrancaControleConferenciaEncalheCotaRepository.remover(cobrancaControleConfEncCota);
				
			}
		}
	}
	
	/**
     * Reseta dados financeiros na finalização da conferencia de encalhe
     * 
     * @param controleConfEncalheCota
     */
	private void resetarDadosFinalizacaoConferencia(final ControleConferenciaEncalheCota controleConfEncalheCota){
		
		if(	controleConfEncalheCota.getId() != null) {
			
			final StatusOperacao statusAtualOperacaoConfEnc = 
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
			                                                                 final List<ConferenciaEncalheDTO> listaConferenciaEncalhe,
			                                                                 final List<Long> listaIdConferenciaEncalheParaExclusao,
			                                                                 final Usuario usuario,
			                                                                 final boolean indConferenciaContingencia){
		
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
	@Override
	@Transactional(timeout = 500)
	public Long salvarDadosConferenciaEncalhe(
			final ControleConferenciaEncalheCota controleConfEncalheCota, 
			final List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			final Usuario usuario, 
			final boolean indConferenciaContingencia) throws EncalheSemPermissaoSalvarException, ConferenciaEncalheFinalizadaException {
		
		resetarDadosFinalizacaoConferencia(controleConfEncalheCota);

		List<Long> listaIdConferenciaEncalheParaExclusao = obterIdConferenciasParaExclusao(controleConfEncalheCota.getId(), listaConferenciaEncalhe);
		
		final ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				inserirDadosConferenciaEncalhe(controleConfEncalheCota, listaConferenciaEncalhe, listaIdConferenciaEncalheParaExclusao, usuario, StatusOperacao.EM_ANDAMENTO, indConferenciaContingencia);
		
		limparBackupAnterior(controleConfEncalheCota.getCota().getNumeroCota(), distribuidorService.obterDataOperacaoDistribuidor());
		
		return controleConferenciaEncalheCota.getId();
		
	}
	
	private void sinalizarInicioProcessoEncalhe(Integer numeroCota, Usuario usuario) {
		
		Semaforo semaforo = semaforoRepository.selectForUpdate(numeroCota);
		
		if(semaforo!=null && StatusProcessoEncalhe.INICIADO.equals(semaforo.getStatusProcessoEncalhe())){
			throw new ValidacaoException(TipoMensagem.WARNING, "A cota " + numeroCota + " ainda esta sendo processada!");
		}
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		if(semaforo == null) {
			semaforo = new Semaforo();
		} 
		
		semaforo.setUsuario(usuario);
		semaforo.setStatusProcessoEncalhe(StatusProcessoEncalhe.INICIADO);
		semaforo.setDataOperacao(dataOperacao);
		semaforo.setErrorLog(null);
		semaforo.setDataInicio(new Date());
		semaforo.setDataFim(null);
		
		if(semaforo.getNumeroCota()==null) {
			semaforo.setNumeroCota(numeroCota);
			semaforoRepository.adicionar(semaforo);
		} else {
			semaforoRepository.alterar(semaforo);
		}
		
	}
	
	@Transactional
	public void validarCotaProcessandoEncalhe(Integer numeroCota) {
		
		Semaforo semaforo = semaforoRepository.buscarPorId(numeroCota);
		
		if(semaforo!=null && StatusProcessoEncalhe.INICIADO.equals(semaforo.getStatusProcessoEncalhe())){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Encalhe da cota " + numeroCota + " ainda esta sendo processado!");
		
		}
		
	}
	
	@Transactional
	public void sinalizarFimProcessoEncalhe(Integer numeroCota) {
		
		Semaforo semaforo = semaforoRepository.buscarPorId(numeroCota);
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		if(semaforo == null) {
			throw new IllegalStateException("Processo da cota [" + numeroCota + "] não possui sinalização de início.");
		} 
		
		semaforo.setStatusProcessoEncalhe(StatusProcessoEncalhe.FINALIZADO);
		semaforo.setDataOperacao(dataOperacao);
		semaforo.setErrorLog(null);
		semaforo.setDataFim(new Date());
		
		semaforoRepository.alterar(semaforo);
		
	}
	
	private String obterDescricaoErro(Exception e) {
		
		String logMessage = "Erro não identificado no processo de encalhe da cota";
		
		if(e!=null && e.getMessage()!=null) {
			
			if(e.getMessage().length()>255) {
				logMessage = e.getMessage().substring(0,255);
			} else {
				logMessage = e.getMessage();
			}
		} 

		return logMessage;
	}
	
	@Transactional
	public void sinalizarErroProcessoEncalhe(Integer numeroCota, Exception e) {
		
		Semaforo semaforo = semaforoRepository.buscarPorId(numeroCota);
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		if(semaforo == null) {
			semaforo = new Semaforo();
		} 
		
		semaforo.setStatusProcessoEncalhe(StatusProcessoEncalhe.INTERROMPIDO);
		semaforo.setDataOperacao(dataOperacao);
		semaforo.setDataFim(new Date());
		
		String logMessage = obterDescricaoErro(e);
		
		semaforo.setErrorLog(logMessage);
		
		if(semaforo.getNumeroCota()==null) {
			semaforo.setNumeroCota(numeroCota);
			semaforoRepository.adicionar(semaforo);
		} else {
			semaforoRepository.alterar(semaforo);
		}
		
	}
	
	@Transactional
	public void criarBackupConferenciaEncalhe(Usuario usuario
			, InfoConferenciaEncalheCota infoConferenciaEncalheCota
			, ControleConferenciaEncalheCota controleConferenciaEncalheCota) {
		
		this.sinalizarInicioProcessoEncalhe(controleConferenciaEncalheCota.getCota().getNumeroCota(), usuario);

		Date dataCriacao = new Date();
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		limparBackupAnterior(controleConferenciaEncalheCota.getCota().getNumeroCota(), dataOperacao);
		
		List<Date> datasRecolhimento = new ArrayList<Date>();
		datasRecolhimento.add(dataOperacao);
		List<ConferenciaEncalheDTO>	listaConferenciaEncalheCompleta = obterListaConferenciaEncalheContingencia(
					dataOperacao, 
					controleConferenciaEncalheCota.getCota().getNumeroCota(), 
					datasRecolhimento, new ArrayList<ConferenciaEncalheDTO>(infoConferenciaEncalheCota.getListaConferenciaEncalhe()));
		
		if(listaConferenciaEncalheCompleta!=null && !listaConferenciaEncalheCompleta.isEmpty()) {
			
			carregarDiaRecolhimento(controleConferenciaEncalheCota.getCota().getNumeroCota(), dataOperacao, listaConferenciaEncalheCompleta);
			
		}
		
		List<Long> produtoEdicoesIdsConferidos = new ArrayList<Long>();
		
		for(ConferenciaEncalheDTO conf : infoConferenciaEncalheCota.getListaConferenciaEncalhe()) {
			produtoEdicoesIdsConferidos.add(conf.getIdProdutoEdicao());
		}
		
		for(ConferenciaEncalheDTO conf : listaConferenciaEncalheCompleta) {
			if(!produtoEdicoesIdsConferidos.contains(conf.getIdProdutoEdicao())) {
				infoConferenciaEncalheCota.getListaConferenciaEncalhe().add(conf);
			}
		}
		
		for(ConferenciaEncalheDTO conf : infoConferenciaEncalheCota.getListaConferenciaEncalhe()) {
			criarNovoRegistroBackupConferenciaEncalhe(conf, controleConferenciaEncalheCota.getCota(), dataCriacao, dataOperacao);
		}
		
	}
	
	private void limparBackupAnterior(Integer numeroCota, Date dataOperacao) {
		
		List<Long> listaIdConferenciaEncalheBackup = conferenciaEncalheBackupRepository.obterIdConferenciasEncalheBackupNaData(numeroCota, dataOperacao);		
		
		if(listaIdConferenciaEncalheBackup==null || listaIdConferenciaEncalheBackup.isEmpty()) {
			return;
		}
		
		for(Long id : listaIdConferenciaEncalheBackup) {
			conferenciaEncalheBackupRepository.removerPorId(id);
		}
		
	}
	

	private void criarNovoRegistroBackupConferenciaEncalhe(
			final ConferenciaEncalheDTO conferenciaEncalheDTO,
			final Cota cota,
			final Date dataCriacao,
			final Date dataOperacao) {
		
		boolean juramentada = (conferenciaEncalheDTO.getJuramentada()) == null ? false : conferenciaEncalheDTO.getJuramentada();
		
		ConferenciaEncalheBackup conferenciaEncalheBackup = new ConferenciaEncalheBackup();

		conferenciaEncalheBackup.setDataOperacao(dataOperacao);
		
		if (conferenciaEncalheDTO.getDataRecolhimento() != null) {
			
			Long idChamadaEncalheCota = 
					chamadaEncalheCotaRepository.obterIdChamadaEncalheCotaNaData(
							cota.getId(), 
							conferenciaEncalheDTO.getIdProdutoEdicao(), conferenciaEncalheDTO.getDataRecolhimento());
			
			ChamadaEncalheCota cec = new ChamadaEncalheCota();
			cec.setId(idChamadaEncalheCota);
			conferenciaEncalheBackup.setChamadaEncalheCota(cec);
			
		}
		
		conferenciaEncalheBackup.setIdConferenciaOriginal(conferenciaEncalheDTO.getIdConferenciaEncalhe());
		
		
		
		conferenciaEncalheBackup.setDiaRecolhimento(conferenciaEncalheDTO.getDia());
		
		conferenciaEncalheBackup.setDataCriacao(dataCriacao);
		
		conferenciaEncalheBackup.setJuramentada(juramentada);
		
		conferenciaEncalheBackup.setObservacao(conferenciaEncalheDTO.getObservacao());
		
		conferenciaEncalheBackup.setCota(cota);

		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(conferenciaEncalheDTO.getIdProdutoEdicao());
		conferenciaEncalheBackup.setProdutoEdicao(produtoEdicao);

		conferenciaEncalheBackup.setQtde(conferenciaEncalheDTO.getQtdExemplar());
		
		conferenciaEncalheBackup.setQtdeInformada(conferenciaEncalheDTO.getQtdInformada());
		
		conferenciaEncalheBackup.setPrecoCapaInformado(conferenciaEncalheDTO.getPrecoCapaInformado());

		conferenciaEncalheBackup.setPrecoCapa(conferenciaEncalheDTO.getPrecoCapa());
		
		conferenciaEncalheBackup.setPrecoComDesconto(conferenciaEncalheDTO.getPrecoComDesconto());
		
		conferenciaEncalheBackupRepository.adicionar(conferenciaEncalheBackup);
		
	}
	
	private List<Long> obterIdConferenciasParaExclusao(Long idControleConfEncalheCota, List<ConferenciaEncalheDTO> listaConferenciaEncalhe) {
		
		List<Long> listaIdProdutoEdicaoConferidos = null;
		
		if(listaConferenciaEncalhe!=null && !listaConferenciaEncalhe.isEmpty()) {
			listaIdProdutoEdicaoConferidos = new ArrayList<>();
			for(ConferenciaEncalheDTO conf :  listaConferenciaEncalhe) {
				listaIdProdutoEdicaoConferidos.add(conf.getIdProdutoEdicao());
			}
		}
		
		return conferenciaEncalheRepository.obterIdConferenciasExcluidas(idControleConfEncalheCota, listaIdProdutoEdicaoConferidos);
		
	}
	
	@Override
	@Transactional(rollbackFor=GerarCobrancaValidacaoException.class, timeout = 900, isolation= Isolation.READ_COMMITTED)
	public DadosDocumentacaoConfEncalheCotaDTO finalizarConferenciaEncalhe(
			final ControleConferenciaEncalheCota controleConfEncalheCota, 
			final List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			final Usuario usuario,
			final boolean indConferenciaContingencia, 
			BigDecimal reparte) throws GerarCobrancaValidacaoException {
		
		final Integer numeroCota = controleConfEncalheCota.getCota().getNumeroCota();
		
		final Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota);
		
		List<Long> listaIdConferenciaEncalheParaExclusao = obterIdConferenciasParaExclusao(controleConfEncalheCota.getId(), listaConferenciaEncalhe);
		
		this.incluirDadosConferenciaEncalheCota(controleConfEncalheCota, 
				                                listaConferenciaEncalhe, 
				                                listaIdConferenciaEncalheParaExclusao, 
				                                usuario, 
				                                indConferenciaContingencia);

		this.abaterNegociacao(listaConferenciaEncalhe, cota.getId(), usuario);

		Set<String> nossoNumeroCollection = new HashSet<String>();
		
		final DadosDocumentacaoConfEncalheCotaDTO documentoConferenciaEncalhe = new DadosDocumentacaoConfEncalheCotaDTO();

		try {
		
			nossoNumeroCollection = gerarCobranca(controleConfEncalheCota);
		} catch(final GerarCobrancaValidacaoException e) {
			
			documentoConferenciaEncalhe.setMsgsGeracaoCobranca(e.getValidacaoVO());			
		}

		final ParametroDistribuicaoCota parametroDistribuicaoCota = cota.getParametroDistribuicao();

		final PoliticaCobranca politicaCobranca = politicaCobrancaService.obterPoliticaCobrancaPrincipal();
		
		final FormaEmissao formaEmissao = politicaCobranca.getFormaEmissao();
		
		final boolean isUtililzaBoletoImpressao = parametrosDistribuidorEmissaoDocumentoRepository.isUtilizaImpressao(TipoParametrosDistribuidorEmissaoDocumento.BOLETO);
		
		final boolean isUtililzaSlipImpressao = parametrosDistribuidorEmissaoDocumentoRepository.isUtilizaImpressao(TipoParametrosDistribuidorEmissaoDocumento.SLIP);
		
		final boolean isUtililzaBoletoSlipImpressao = parametrosDistribuidorEmissaoDocumentoRepository.isUtilizaImpressao(TipoParametrosDistribuidorEmissaoDocumento.BOLETO_SLIP);
		
		final boolean isUtililzaReciboImpressao = parametrosDistribuidorEmissaoDocumentoRepository.isUtilizaImpressao(TipoParametrosDistribuidorEmissaoDocumento.RECIBO);
		
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
			
			final Iterator<String> iterator = nossoNumeroCollection.iterator();
			
			while (iterator.hasNext()){
				
				final String nossoNumero = iterator.next();
				
				if(nossoNumero!=null && !nossoNumero.trim().isEmpty()) {
					
					associarCobrancaConferenciaEncalheCota(controleConfEncalheCota.getId(), nossoNumero);
				}
				
				documentoConferenciaEncalhe.getListaNossoNumero().put(nossoNumero, true);
			}
		}
		
		limparBackupAnterior(numeroCota, distribuidorService.obterDataOperacaoDistribuidor());
		
		//cria registros para possível geração posterior de slip + boleto em massa
		Slip slip = this.slipRepository.obterPorNumeroCotaData(numeroCota, controleConfEncalheCota.getDataOperacao());
		
		//só existe 1 slip por cota por dia
		if (slip != null){
		    
		    this.slipRepository.remover(slip);
		}
		
		slip = this.documentoCobrancaService.gerarSlipDTOCobranca(controleConfEncalheCota.getId(), true);
		
		if (slip != null){
		    
		    for (DebitoCreditoCota d : slip.getListaComposicaoCobranca()){
	            d.setSlip(slip);
	            d.setComposicaoCobranca(true);
	        }
	        
	        for (DebitoCreditoCota d : slip.getListaResumoCobranca()){
	            d.setSlip(slip);
	        }
	        
	        for (ProdutoEdicaoSlip d : slip.getListaProdutoEdicaoSlip()){
	            d.setSlip(slip);
	        }
	        
	        this.slipRepository.adicionar(slip);
		}
		
		return documentoConferenciaEncalhe;
		
	}
	
	private void abaterNegociacao(final List<ConferenciaEncalheDTO> listaConferenciaEncalhe, final Long idCota, final Usuario usuario) {
		
		BigDecimal valorTotalReparteOperacaoConferenciaEncalhe = BigDecimal.ZERO;
		BigDecimal valorTotalEncalheOperacaoConferenciaEncalhe = BigDecimal.ZERO;

		for (final ConferenciaEncalheDTO dto : listaConferenciaEncalhe){
			
			valorTotalReparteOperacaoConferenciaEncalhe = 
					valorTotalReparteOperacaoConferenciaEncalhe.add(dto.getPrecoComDesconto().multiply(new BigDecimal(dto.getQtdReparte())));
			valorTotalEncalheOperacaoConferenciaEncalhe = 
					valorTotalEncalheOperacaoConferenciaEncalhe.add(dto.getPrecoComDesconto().multiply(new BigDecimal(dto.getQtdExemplar())));
		}
		
		this.negociacaoDividaService.abaterNegociacaoPorComissao(
			idCota, valorTotalReparteOperacaoConferenciaEncalhe, valorTotalEncalheOperacaoConferenciaEncalhe, usuario
		);		
	}

	private boolean getDocumentoImpressao(Boolean documentoImpressaoCota,
										  Boolean documentoImpressaoDistribuidor) {
		
		if (documentoImpressaoCota != null) {
			
			return documentoImpressaoCota;
		}
		
		return (documentoImpressaoDistribuidor != null) ? documentoImpressaoDistribuidor : false; 
	}
	
	    /**
     * Gera o movimento financeiro referente a operação de conferência de
     * encalhe e em seguida dispara componentes responsáveis pela geração da
     * cobrança.
     * 
     * @param controleConferenciaEncalheCota
     * 
     * @return Set - String
     * @throws GerarCobrancaValidacaoException
     */
	private Set<String> gerarCobranca(final ControleConferenciaEncalheCota controleConferenciaEncalheCota) throws GerarCobrancaValidacaoException {
		
		
		if(	controleConferenciaEncalheCota.getId() != null) {
			
			final StatusOperacao statusAtualOperacaoConfEnc = 
					controleConferenciaEncalheCotaRepository.obterStatusControleConferenciaEncalheCota(
							controleConferenciaEncalheCota.getId());
			
			if(StatusOperacao.CONCLUIDO.equals(statusAtualOperacaoConfEnc)) {
								
				removerAssociacoesCobrancaConferenciaEncalheCota(controleConferenciaEncalheCota.getId());
			}
		}	
		
		final Set<String> nossoNumeroCollection = new HashSet<String>();
        
        final List<Date> datasRecolhimento = this.grupoService.obterDatasRecolhimentoOperacaoDiferenciada(controleConferenciaEncalheCota.getCota().getNumeroCota(), 
        		                                                                                          controleConferenciaEncalheCota.getDataOperacao());
        
		if (controleConferenciaEncalheCota.getCota().getTipoCota().equals(TipoCota.CONSIGNADO)){
			
			
			//CANCELA DIVIDA EXCLUI CONSOLIDADO E MOVIMENTOS FINANCEIROS DE REPARTE X ENCALHE (RECEBIMENTO_REPARTE E ENVIO_ENCALHE) PARA QUE SEJAM RECRIADOS
			this.gerarCobrancaService.cancelarDividaCobranca(null, 
					                                         controleConferenciaEncalheCota.getCota().getId(), 
					                                         controleConferenciaEncalheCota.getDataOperacao(), 
					                                         true);
			
			if(datasRecolhimento!=null && !datasRecolhimento.isEmpty()) {
				//CRIA MOVIMENTOS FINANCEIROS DE REPARTE X ENCALHE (RECEBIMENTO_REPARTE E ENVIO_ENCALHE)
				this.movimentoFinanceiroCotaService.gerarMovimentoFinanceiroCota(controleConferenciaEncalheCota.getCota(),
						                                                         datasRecolhimento,
																				 controleConferenciaEncalheCota.getUsuario(),
																				 controleConferenciaEncalheCota.getId(),
																				 null);
			}
			
		
			final boolean existeBoletoAntecipado =  this.boletoService.existeBoletoAntecipadoCotaDataRecolhimento(controleConferenciaEncalheCota.getCota().getId(), 
					                                                                                              controleConferenciaEncalheCota.getDataOperacao());
			
            // se a cota for unificadora ou unificada não pode gerar cobrança
            // nesse ponto
			final boolean cotaUnificadora = this.cotaUnificacaoRepository.verificarCotaUnificada(
					controleConferenciaEncalheCota.getCota().getNumeroCota()),
					
					cotaUnificada = this.cotaUnificacaoRepository.verificarCotaUnificadora(
							controleConferenciaEncalheCota.getCota().getNumeroCota());
			
			if (!cotaUnificadora && !cotaUnificada){
			
				
				FormaCobranca fc = formaCobrancaService.obterFormaCobrancaCota(controleConferenciaEncalheCota.getCota().getId()
										, null, controleConferenciaEncalheCota.getDataOperacao());
                
                if (existeBoletoAntecipado 
                		|| (fc != null && fc.getTipoCobranca() != null && TipoCobranca.BOLETO_EM_BRANCO.equals(fc.getTipoCobranca()))) {
					
					gerarCobrancaService.gerarDividaPostergada(controleConferenciaEncalheCota.getCota().getId(), 
													           controleConferenciaEncalheCota.getUsuario().getId());
				}
				else{
				
					gerarCobrancaService.gerarCobranca(controleConferenciaEncalheCota.getCota().getId(), 
													   controleConferenciaEncalheCota.getUsuario().getId(), 
													   nossoNumeroCollection,
													   new HashSet<String>());
				}
			}
	    }
		else if (controleConferenciaEncalheCota.getCota().getTipoCota().equals(TipoCota.A_VISTA)){
			
			final boolean isConferenciaRealizada = this.controleConferenciaEncalheCotaRepository.isConferenciaEncalheCotaFinalizada(controleConferenciaEncalheCota.getCota().getId(), 
					                                                                                                                controleConferenciaEncalheCota.getDataOperacao());
			
			if(isConferenciaRealizada){

                // EXLUI MOVIMENTOS FINANCEIROS COTA PARA CRIÁ-LOS NOVAMENTE
			    this.movimentoFinanceiroCotaService.removerMovimentosFinanceirosCotaConferenciaNaoConsolidados(controleConferenciaEncalheCota.getCota().getNumeroCota(), 
			    		                                                                                       controleConferenciaEncalheCota.getDataOperacao());
			}
			
			if(datasRecolhimento!=null && !datasRecolhimento.isEmpty()) {
				
				final Integer diasUteisPostergar = null;

				//CRIA MOVIMENTOS FINANCEIROS DE REPARTE X ENCALHE PARA A DATA ATUAL (RECEBIMENTO_REPARTE E ENVIO_ENCALHE) PARA COTA A VISTA COM CONSIGNADO PENDENTE
				this.movimentoFinanceiroCotaService.gerarMovimentoFinanceiroCota(controleConferenciaEncalheCota.getCota(),
						                                                         datasRecolhimento,
																				 controleConferenciaEncalheCota.getUsuario(),
																				 controleConferenciaEncalheCota.getId(),
																				 diasUteisPostergar);
				
			}
		}
		
		return nossoNumeroCollection;
	}
	
	/**
     * Faz o cancelamento de dados financeiros relativos a operação de
     * conferência de encalhe em questão.
     * 
     * @param controleConferenciaEncalheCota
     */
	private void resetarDadosFinanceirosConferenciaEncalheCota(final Long idControleConferenciaEncalheCota, final Long idCota) {
		
		final Cota cota = this.cotaRepository.buscarCotaPorID(idCota);
		
		final Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		if (cota.getTipoCota().equals(TipoCota.CONSIGNADO)){
			
			final List<MovimentoFinanceiroCota> movimentosFinanceiroCota = 
					movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroDaOperacaoConferenciaEncalhe(idControleConferenciaEncalheCota);
			
			if(movimentosFinanceiroCota!=null && !movimentosFinanceiroCota.isEmpty()) {

				gerarCobrancaService.cancelarDividaCobranca(null, idCota, dataOperacao, true);
			}
		}
		else if (cota.getTipoCota().equals(TipoCota.A_VISTA)){
			
            // EXLUI MOVIMENTOS FINANCEIROS COTA PARA CRIÁ-LOS NOVAMENTE
			this.movimentoFinanceiroCotaService.removerMovimentosFinanceirosCotaConferenciaNaoConsolidados(cota.getNumeroCota(), dataOperacao);
		}
	}
	
	private void removerItensConferenciaEncallhe(final Cota cota, final List<Long> listaIdConferenciaEncalheParaExclusao) {
		
		if(listaIdConferenciaEncalheParaExclusao!=null && !listaIdConferenciaEncalheParaExclusao.isEmpty()) {
			
			for(final Long idConferenciaEncalheExclusao : listaIdConferenciaEncalheParaExclusao) {
				excluirRegistroConferenciaEncalhe(cota, idConferenciaEncalheExclusao);
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
			final ControleConferenciaEncalheCota controleConfEncalheCota, 
			final List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			final List<Long> listaIdConferenciaEncalheParaExclusao,
			final Usuario usuario,
			final StatusOperacao statusOperacao,
			final boolean indConferenciaContingencia) {
		
	    final Date dataRecolhimentoReferencia = obterDataRecolhimentoReferencia();
	    
		final Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		final Date dataCriacao = new Date();
		final Integer numeroCota = controleConfEncalheCota.getCota().getNumeroCota();
		
		final Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota);
		
		removerItensConferenciaEncallhe(cota, listaIdConferenciaEncalheParaExclusao);
		
		final NotaFiscalEntradaCota notaFiscalEntradaCota = atualizarCabecalhoNotaFiscalEntradaCota(
				controleConfEncalheCota.getId(),
				controleConfEncalheCota.getNotaFiscalEntradaCotaPricipal(), 
				numeroCota, 
				usuario, 
				dataCriacao);
		
		atualizarItensNotaFiscalEntradaCota(
				dataOperacao,
				notaFiscalEntradaCota, 
				listaConferenciaEncalhe);
		
		final List<NotaFiscalEntradaCota> notaFiscalEntradaCotas = new ArrayList<NotaFiscalEntradaCota>();
		notaFiscalEntradaCotas.add(notaFiscalEntradaCota);
		controleConfEncalheCota.setNotaFiscalEntradaCota(notaFiscalEntradaCotas);
		
		
		final ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				obterControleConferenciaEncalheCotaAtualizado(controleConfEncalheCota, statusOperacao, usuario);
		
		final Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque = obterMapaTipoMovimentoEstoque();
		
		final boolean validarExemplaresComZero =false;
		
		for(final ConferenciaEncalheDTO conferenciaEncalheDTO : listaConferenciaEncalhe) {

			validarQtdeEncalheExcedeQtdeReparte(
					conferenciaEncalheDTO,
					controleConferenciaEncalheCota.getCota(), 
					dataOperacao, indConferenciaContingencia, validarExemplaresComZero);
			
			if(conferenciaEncalheDTO.getIdConferenciaEncalhe() != null) {

				atualizarRegistroConferenciaEncalhe(
						cota,
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
						cota,
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
	 * @param cota
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
			final Cota cota,
			final ConferenciaEncalheDTO conferenciaEncalheDTO, 
			final StatusOperacao statusOperacao, 
			final Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque,
			final ControleConferenciaEncalheCota controleConferenciaEncalheCota,
			final Integer numeroCota,
			final Date dataCriacao,
			final Date dataRecolhimentoReferencia,
			final Usuario usuario) {
		
	
		
		Long idMovimentoEstoqueCota = this.movimentoEstoqueCotaRepository.findIdByIdConferenciaEncalhe(conferenciaEncalheDTO.getIdConferenciaEncalhe());
		Long idMovimentoEstoque = null;
	
		MovimentoEstoqueDTO movimentoEstoqueDTO = null;

		if(idMovimentoEstoqueCota!=null) {
		
			atualizarMovimentoEstoqueCota(idMovimentoEstoqueCota, conferenciaEncalheDTO, cota);
		
		} else {
			
			MovimentoEstoqueCota movimentoEstoqueCota = criarNovoRegistroMovimentoEstoqueCota(
					controleConferenciaEncalheCota, 
					conferenciaEncalheDTO, 
					numeroCota, 
					dataRecolhimentoReferencia, 
					dataCriacao, 
					mapaTipoMovimentoEstoque, 
					usuario);
			idMovimentoEstoqueCota = this.movimentoEstoqueCotaRepository.merge(movimentoEstoqueCota).getId();
		}
		
		movimentoEstoqueDTO = this.movimentoEstoqueRepository.findByIdConferenciaEncalhe(conferenciaEncalheDTO.getIdConferenciaEncalhe());
			
		if (movimentoEstoqueDTO != null) {
			idMovimentoEstoque = movimentoEstoqueDTO.getId();
			movimentoEstoqueService.atualizarMovimentoEstoqueDeEncalhe(cota, movimentoEstoqueDTO, conferenciaEncalheDTO.getQtdExemplar(), conferenciaEncalheDTO.getIdProdutoEdicao());
			
		} else {	
		
			ChamadaEncalheCota chamadaEncalheCota = null;
			
			if (conferenciaEncalheDTO.getDataRecolhimento() != null) {
				
				chamadaEncalheCota = obterChamadaEncalheCotaParaConfEncalhe(
					numeroCota, conferenciaEncalheDTO.getDataRecolhimento(), 
						conferenciaEncalheDTO.getIdProdutoEdicao());
			}
			
			idMovimentoEstoque = criarNovoRegistroMovimentoEstoque(
					cota,
					controleConferenciaEncalheCota, 
					conferenciaEncalheDTO, 
					numeroCota, 
					dataRecolhimentoReferencia, 
					dataCriacao, 
					mapaTipoMovimentoEstoque, 
					usuario,
					chamadaEncalheCota).getId();
		}
		
		final boolean juramentada = (conferenciaEncalheDTO.getJuramentada()) == null ? false
				: conferenciaEncalheDTO.getJuramentada();

		this.conferenciaEncalheRepository.update(
				conferenciaEncalheDTO.getIdConferenciaEncalhe(), juramentada,
				conferenciaEncalheDTO.getObservacao(),
				conferenciaEncalheDTO.getQtdInformada(),
				conferenciaEncalheDTO.getPrecoCapaInformado(),
				idMovimentoEstoqueCota, idMovimentoEstoque,
				conferenciaEncalheDTO.getQtdExemplar());
		
	}
	
	
	/**
	 * Cria novo registro de ConferenciaEncalhe e entidades relacionadas.
	 * 
	 * @param cota
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
			final Cota cota,
			final ConferenciaEncalheDTO conferenciaEncalheDTO, 
			final StatusOperacao statusOperacao, 
			final Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque,
			final ControleConferenciaEncalheCota controleConferenciaEncalheCota,
			final Integer numeroCota,
			final Date dataCriacao,
			final Date dataRecolhimentoReferencia,
			final Usuario usuario){
		
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
				 	cota,
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
													final boolean juramentada,
													final Date dataRecolhimentoDistribuidor,
													final Date dataConferenciaEncalhe,
													final Map<GrupoMovimentoEstoque, 
													TipoMovimentoEstoque> mapaTipoMovimentoEstoque,
													final TipoChamadaEncalhe tipoChamadaEncalhe) {
		
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
     * Verifica se a dataRecolhimentoDistribuidor é menor ou igual a
     * dataConferenciaEncalhe.
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
     * Obtém um mapa de tipoMovimentoEstoque utilizados pela funcionalidade de
     * conferência de encalhe.
     * 
     * @return Map<GrupoMovimentoEstoque, TipoMovimentoEstoque>
     */
	private Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> obterMapaTipoMovimentoEstoque() {
		
		final Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque = new HashMap<GrupoMovimentoEstoque, TipoMovimentoEstoque>();
		
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
     * Atualiza os dados da notaFiscalEntradaCota relacionada com uma operação
     * de conferência de encalhe.
     * 
     * @param notaFiscalEntradaCota
     * @param usuario
     * @param dataCriacao
     */
	private NotaFiscalEntradaCota atualizarCabecalhoNotaFiscalEntradaCota(final Long idControleConferenciaEncalheCota, final NotaFiscalEntradaCota notaFiscalEntradaCota, final Integer numeroCota, final Usuario usuario, final Date dataCriacao) {
		
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

			final ControleConferenciaEncalheCota controleConferenciaEncalheCotaFromBD = 			
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

			final StatusEmissaoNotaFiscal statusNF = StatusEmissaoNotaFiscal.EMITIDA;
			
			final ParametroEmissaoNotaFiscal parametroEmissaoNF = parametroEmissaoNotaFiscalRepository.obterParametroEmissaoNotaFiscal(GrupoNotaFiscal.RECEBIMENTO_MERCADORIAS_ENCALHE);
			
			if(parametroEmissaoNF == null) {
                throw new IllegalStateException("Nota Fiscal Saida não parametrizada no sistema");
			}
			
			final TipoNotaFiscal tipoNF = tipoNotaFiscalRepository.obterTipoNotaFiscal(GrupoNotaFiscal.RECEBIMENTO_MERCADORIAS_ENCALHE);

			if(tipoNF == null) {
                throw new IllegalStateException("TipoNotaFiscal não parametrizada");
			}
			
	 		final Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota);
			
			final CFOP cfop = parametroEmissaoNF.getCfopDentroEstado();
			
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
	private void inserirRecebimentoFisico(final NotaFiscalEntradaCota notaFiscal, final Usuario usuario, final Date dataCriacao) {
		
		final RecebimentoFisico recebimentoFisico = new RecebimentoFisico();
		
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
			final Date dataOperacao,
			final NotaFiscalEntradaCota notaFiscalEntradaCota,
			final List<ConferenciaEncalheDTO> listaConferenciaEncalhe) {
		
		if(notaFiscalEntradaCota == null || notaFiscalEntradaCota.getId() == null) {
			return;
		}
		
		final Long idNotaFiscalEntrada = notaFiscalEntradaCota.getId();
		
		final List<ItemNotaFiscalEntrada> itensNotaFiscalEntradaCota = 
				itemNotaFiscalEntradaRepository.buscarItensPorIdNota(idNotaFiscalEntrada);
		
		if(itensNotaFiscalEntradaCota!= null && !itensNotaFiscalEntradaCota.isEmpty()) {
			
			for(final ItemNotaFiscalEntrada itemNFEntrada : itensNotaFiscalEntradaCota) {
				
				final ItemRecebimentoFisico itemRecebimentoFisico = itemNFEntrada.getRecebimentoFisico();
				
				if(itemRecebimentoFisico!=null) {
					itemRecebimentoFisicoRepository.remover(itemRecebimentoFisico);
				}
				
				itemNotaFiscalEntradaRepository.remover(itemNFEntrada);
			}
			
		}
		
		if(listaConferenciaEncalhe != null && !listaConferenciaEncalhe.isEmpty()) {
		
			final RecebimentoFisico recebimentoFisico = recebimentoFisicoRepository.obterRecebimentoFisicoPorNotaFiscal(idNotaFiscalEntrada);
			
			for(final ConferenciaEncalheDTO conferenciaEncalhe : listaConferenciaEncalhe) {

				final ProdutoEdicao produtoEdicao = new ProdutoEdicao();
				produtoEdicao.setId(conferenciaEncalhe.getIdProdutoEdicao());
				
				final ItemNotaFiscalEntrada itemNotaFiscalEntrada = new ItemNotaFiscalEntrada();
				itemNotaFiscalEntrada.setNotaFiscal(notaFiscalEntradaCota);
				itemNotaFiscalEntrada.setQtde(conferenciaEncalhe.getQtdInformada());
				itemNotaFiscalEntrada.setProdutoEdicao(produtoEdicao);
				itemNotaFiscalEntrada.setTipoLancamento(TipoLancamento.LANCAMENTO);
				
				final Date dataUltimoLancamento = lancamentoRepository.obterDataUltimoLancamento(
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
	private void inserirItemRecebimentoFisico( final RecebimentoFisico recebimentoFisico, 
															  final ItemNotaFiscalEntrada itemNotaFiscal,
															  final BigInteger qtdeFisico,
															  final Diferenca diferenca) {
		
		final ItemRecebimentoFisico itemRecebimentoFisico = new ItemRecebimentoFisico();
		
		itemRecebimentoFisico.setDiferenca(diferenca);
		itemRecebimentoFisico.setItemNotaFiscal(itemNotaFiscal);
		itemRecebimentoFisico.setQtdeFisico(qtdeFisico);
		itemRecebimentoFisico.setRecebimentoFisico(recebimentoFisico);
		
		itemRecebimentoFisicoRepository.adicionar(itemRecebimentoFisico);
	}
	
    /**
     * Valida se a quantidade da conferência de encalhe não excede o reparte de
     * um produtoEdicao para determinada cota.
     * 
     * @param conferenciaEncalhe
     * @param cota
     * @param dataOperacao
     * @param indConferenciaContingencia
     */
	@Override
	@Transactional(readOnly=true)
	public boolean validarQtdeEncalheExcedeQtdeReparte(
			final ConferenciaEncalheDTO conferenciaEncalhe,
			final Cota cota, 
			final Date dataOperacao, final boolean indConferenciaContingencia, boolean validarExemplarZero) {
		
		if(validarExemplarZero){
			
			if (!indConferenciaContingencia &&
					(conferenciaEncalhe.getQtdExemplar() == null || conferenciaEncalhe.getQtdExemplar().compareTo(BigInteger.ZERO) <= 0)){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Quantidade de itens conferidos no encalhe deve ser maior que zero.");
				
			}
		}

		final boolean indNovoRegistroConfEncalheCota = conferenciaEncalhe.getIdConferenciaEncalhe() == null || 
				(conferenciaEncalhe.getIdConferenciaEncalhe() < 0);

		final BigInteger qtdItensEstoqueProdutoEdicaoDaCotaNaoDevolvidos = obterQtdItensEstoqueProdutoEdicaoDaCotaNaoDevolvidos(cota.getId(), conferenciaEncalhe.getIdProdutoEdicao());
		
		if(indNovoRegistroConfEncalheCota) {

			final BigInteger qtdeNew = conferenciaEncalhe.getQtdExemplar();
			
			if(qtdeNew.compareTo(qtdItensEstoqueProdutoEdicaoDaCotaNaoDevolvidos) > 0) {
                // throw new ValidacaoException(TipoMensagem.WARNING,
                // "Conferência de encalhe está excedendo quantidade de reparte.");
				return true;
			}
			
		} else {
			
			final BigInteger qtdeOld = conferenciaEncalheRepository.obterQtdeEncalhe(conferenciaEncalhe.getIdConferenciaEncalhe());
			final BigInteger qtdeNew = conferenciaEncalhe.getQtdExemplar();
			
			if(qtdeNew.compareTo( qtdeOld.add(qtdItensEstoqueProdutoEdicaoDaCotaNaoDevolvidos) ) > 0) {
                // throw new ValidacaoException(TipoMensagem.WARNING,
                // "Conferência de encalhe está excedendo quantidade de reparte.");
				return true;
			}
			
		}
		
		return false;
	}
	
	/**
	 * Exclui um registros de ConferenciaEncalhe e movimentos relacionados como 
	 * MovimentoEstoqueCota e MovimentoEstoque.
	 * 
	 * @param idConferenciaEncalhe
	 */
	private void excluirRegistroConferenciaEncalhe(final Cota cota, final Long idConferenciaEncalhe) {
		
		final ConferenciaEncalhe conferenciaEncalhe = conferenciaEncalheRepository.buscarPorId(idConferenciaEncalhe);
		
		final MovimentoEstoqueCota movimentoEstoqueCota = conferenciaEncalhe.getMovimentoEstoqueCota();
		
		final MovimentoEstoque movimentoEstoque = conferenciaEncalhe.getMovimentoEstoque();

		conferenciaEncalheRepository.remover(conferenciaEncalhe);
		
		if(movimentoEstoqueCota != null) {
			
			excluirRegistroMovimentoEstoqueCota(movimentoEstoqueCota);
		}
		
		if(movimentoEstoque != null) {
			
			movimentoEstoqueService.excluirRegistroMovimentoEstoqueDeEncalhe(cota, movimentoEstoque);
		}
		
	}
	
	
	/**
	 * Exclui registro de MovimentoEstoqueCota alterando consequentemente
	 * o EstoqueProdutoCota relativo.
	 * 
	 * @param movimentoEstoqueCota
	 */
	private void excluirRegistroMovimentoEstoqueCota(final MovimentoEstoqueCota movimentoEstoqueCota) {
		
		if(movimentoEstoqueCota.getQtde() != null && (movimentoEstoqueCota.getQtde().compareTo(BigInteger.ZERO) != 0)) {

			final EstoqueProdutoCota estoqueProdutoCota = movimentoEstoqueCota.getEstoqueProdutoCota();
			
			if(estoqueProdutoCota != null) {

				final BigInteger qtdeDevolvidaOriginal = estoqueProdutoCota.getQtdeDevolvida() == null ? BigInteger.ZERO : estoqueProdutoCota.getQtdeDevolvida();
				
				estoqueProdutoCota.setQtdeDevolvida(qtdeDevolvidaOriginal.subtract(movimentoEstoqueCota.getQtde()));

				this.estoqueProdutoCotaRepository.alterar(estoqueProdutoCota);
			}
			
		}
		
		this.movimentoEstoqueCotaRepository.remover(movimentoEstoqueCota);
	}

	
	
	
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#obterTipoContabilizacaoCE()
	 */
	@Override
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
			final ControleConferenciaEncalheCota controleConferenciaEncalheCota,
			final ConferenciaEncalheDTO conferenciaEncalheDTO,
			final Integer numeroCota, 
			final Date dataRecolhimentoReferencia,
			final Date dataCriacao,
			final Map<GrupoMovimentoEstoque, TipoMovimentoEstoque> mapaTipoMovimentoEstoque,
			final Usuario usuario) {
		
		final Long idCota = controleConferenciaEncalheCota.getCota().getId();
		
		final ProdutoEdicao produtoEdicao = 
				this.produtoEdicaoRepository.buscarPorId(conferenciaEncalheDTO.getIdProdutoEdicao());
		
		final TipoMovimentoEstoque tipoMovimentoEstoqueCota = mapaTipoMovimentoEstoque.get(GrupoMovimentoEstoque.ENVIO_ENCALHE);
	
		ValoresAplicados valoresAplicados = 
		        movimentoEstoqueCotaRepository.obterValoresAplicadosProdutoEdicao(
		                numeroCota, produtoEdicao.getId(), distribuidorService.obterDataOperacaoDistribuidor());
		
        if(valoresAplicados == null){
            valoresAplicados = new ValoresAplicados(BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO);
        }else{
            verificarValorAplicadoNulo(valoresAplicados);
        }
		
		MovimentoEstoqueCota movimentoEstoqueCota = 
				movimentoEstoqueService.gerarMovimentoCota(
						null, 
						produtoEdicao.getId(), 
						idCota, 
						usuario.getId(), 
						conferenciaEncalheDTO.getQtdExemplar(), 
						tipoMovimentoEstoqueCota,
						this.distribuidorService.obterDataOperacaoDistribuidor(),
						valoresAplicados);
		
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
	 * @param cota
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
									final Cota cota,
									final ControleConferenciaEncalheCota controleConferenciaEncalheCota,
									final ConferenciaEncalheDTO conferenciaEncalheDTO,
									final Integer numeroCota, 
									final Date dataRecolhimentoReferencia,
									final Date dataCriacao,
									final Map<GrupoMovimentoEstoque, 
									TipoMovimentoEstoque> mapaTipoMovimentoEstoque,
									final Usuario usuario,
									final ChamadaEncalheCota chamadaEncalheCota) {
		
		final boolean juramentada = 
			(conferenciaEncalheDTO.getJuramentada()) == null 
				? false : conferenciaEncalheDTO.getJuramentada();
		
		final ChamadaEncalhe chamadaEncalhe = 
    			(chamadaEncalheCota != null) ?
    				chamadaEncalheCota.getChamadaEncalhe() : null;
				
		final TipoChamadaEncalhe tipoChamadaEncalhe = 
			(chamadaEncalhe != null) ? chamadaEncalhe.getTipoChamadaEncalhe() : null;
			
		final Date dataConferenciaEncalhe = this.distribuidorService.obterDataOperacaoDistribuidor();

		final TipoMovimentoEstoque tipoMovimentoEstoque = 
			obterTipoMovimentoEstoqueDistribuidor(
				juramentada, conferenciaEncalheDTO.getDataRecolhimento(),
				dataConferenciaEncalhe, mapaTipoMovimentoEstoque, tipoChamadaEncalhe);

		final MovimentoEstoque movimentoEstoque = 
			this.movimentoEstoqueService.gerarMovimentoEstoque(
					conferenciaEncalheDTO.getIdProdutoEdicao(), usuario.getId(), 
					conferenciaEncalheDTO.getQtdExemplar(), tipoMovimentoEstoque, true, cota);
		
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
			final ControleConferenciaEncalheCota controleConferenciaEncalheCota,
			final ConferenciaEncalheDTO conferenciaEncalheDTO,
			final Date dataCriacao,
			final Integer numeroCota, 
			final MovimentoEstoqueCota movimentoEstoqueCota,
			final MovimentoEstoque movimentoEstoque,
			final ChamadaEncalheCota chamadaEncalheCota) {
		
		final Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		final Integer diaRecolhimento = this.distribuidorService.obterDiaDeRecolhimentoDaData(
				dataOperacao, 
                conferenciaEncalheDTO.getDataRecolhimento(),
                numeroCota,
                conferenciaEncalheDTO.getIdProdutoEdicao(), 
                null, null);
		
		final boolean juramentada = (conferenciaEncalheDTO.getJuramentada()) == null ? false : conferenciaEncalheDTO.getJuramentada();
		
		final ConferenciaEncalhe conferenciaEncalhe = new ConferenciaEncalhe();

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
		
		conferenciaEncalhe.setDiaRecolhimento(diaRecolhimento);
		
		final ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		
		produtoEdicao.setId(conferenciaEncalheDTO.getIdProdutoEdicao());
		
		conferenciaEncalhe.setProdutoEdicao(produtoEdicao);
		
		conferenciaEncalheRepository.adicionar(conferenciaEncalhe);
	}
	
	/**
     * Obtém a ChamadaEncalheCota de acordo com a cota, idProdutoEdicao e
     * dataRecolhimento informados.
     * 
     * @param numeroCota
     * @param dataRecolhimentoReferencia
     * @param idProdutoEdicao
     * 
     * @return ChamadaEncalheCota
     */
	private ChamadaEncalheCota obterChamadaEncalheCotaParaConfEncalhe(
			final Integer numeroCota,
			final Date dataRecolhimento,
			final Long idProdutoEdicao) {
		
		final boolean postergado = false;
		
		final Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota);
		
		return chamadaEncalheCotaRepository.obterChamadaEncalheCotaNaData(cota, idProdutoEdicao, postergado, dataRecolhimento);
		
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
			final ControleConferenciaEncalheCota ctrlConfEncalheCota, 
			final StatusOperacao statusOperacao, 
			final Usuario usuario) {
		
		final Date dataOperacaoDistribuidor = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		final Cota cota = cotaRepository.obterPorNumeroDaCota(ctrlConfEncalheCota.getCota().getNumeroCota());
			
		final Date dataFinalizacao = new Date();
		
		if(ctrlConfEncalheCota.getId()!=null) { 
			
			final ControleConferenciaEncalheCota controleConferenciaEncalheCotaFromBD = 			
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

            // Método não pode haver concorrência
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
	private void atualizarMovimentoEstoqueCota(final Long idMovimentoEstoqueCota, 
											   final ConferenciaEncalheDTO conferenciaEncalheDTO, final Cota cota) {
		
		
		
		
		final BigInteger newQtdeMovEstoquecota = conferenciaEncalheDTO.getQtdExemplar();
		final BigInteger oldQtdeMovEstoqueCota = this.movimentoEstoqueCotaRepository.loadQtdeById(idMovimentoEstoqueCota);
		
		ValoresAplicados valoresAplicados =  movimentoEstoqueCotaRepository.
				obterValoresAplicadosProdutoEdicao(
						cota.getNumeroCota(), 
						conferenciaEncalheDTO.getIdProdutoEdicao(), 
						distribuidorService.obterDataOperacaoDistribuidor());

		if(valoresAplicados == null){
			valoresAplicados = new ValoresAplicados(BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO);
		}else{
			verificarValorAplicadoNulo(valoresAplicados);
		}

		
		this.movimentoEstoqueCotaRepository.updateById(idMovimentoEstoqueCota, valoresAplicados, newQtdeMovEstoquecota);
		
		
		final ItemDTO<Long, BigInteger> estoqueProdutoCotaItemDTO = 
				this.estoqueProdutoCotaRepository.loadIdAndQtdDevolvidaByIdProdutoEdicaoAndIdCota(conferenciaEncalheDTO.getIdProdutoEdicao(), cota.getId());
		if(estoqueProdutoCotaItemDTO == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Produto ["
							+ conferenciaEncalheDTO.getCodigo() + " - "
							+ conferenciaEncalheDTO.getNomeProduto()
							+ " - " + conferenciaEncalheDTO.getNumeroEdicao()
                + "] não possui registro de estoque da cota.");
			
		}
		
		
		BigInteger qtdDevolvida = (BigInteger) ObjectUtils.defaultIfNull(estoqueProdutoCotaItemDTO.getValue(), BigInteger.ZERO);
				
		qtdDevolvida = 
			qtdDevolvida.subtract(oldQtdeMovEstoqueCota).add(newQtdeMovEstoquecota);
					
	
		
		validarAlteracaoEstoqueProdutoCota(qtdDevolvida, conferenciaEncalheDTO);
		
		this.estoqueProdutoCotaRepository.updateById(estoqueProdutoCotaItemDTO.getKey(), qtdDevolvida);

	}
	
	

	private void validarAlteracaoEstoqueProdutoCota(final BigInteger saldoEstoque,
			final ConferenciaEncalheDTO conferenciaEncalheDTO) {

		if (!this.validarSaldoEstoque(saldoEstoque)) {

			throw new ValidacaoException(
					TipoMensagem.WARNING,
					"Saldo do produto ["
							+ conferenciaEncalheDTO.getCodigo()
							+ " - "
							+ conferenciaEncalheDTO.getNomeProduto()
							+ " - "
							+ conferenciaEncalheDTO.getNumeroEdicao()
							+ "] no estoque da cota, insuficiente para movimentação.");
		}
	}

	private boolean validarSaldoEstoque(final BigInteger saldoEstoque) {

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
	@Override
	@Transactional
	public byte[] gerarDocumentosConferenciaEncalhe(			
			final Long idControleConferenciaEncalheCota,
			final String nossoNumero,
			final br.com.abril.nds.enums.TipoDocumentoConferenciaEncalhe tipoDocumentoConferenciaEncalhe,
			final boolean geraNovoNumeroSlip
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
	public BigDecimal obterValorTotalDebitoCreditoCota(final Integer numeroCota, final Date dataOperacao) {
		
		final TipoMovimentoFinanceiro tipoMovimentoFinanceiroEnvioEncalhe = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
		final TipoMovimentoFinanceiro tipoMovimentoFinanceiroRecebimentoReparte = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE);
		
		final List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados = new ArrayList<TipoMovimentoFinanceiro>();
		
		tiposMovimentoFinanceiroIgnorados.add(tipoMovimentoFinanceiroEnvioEncalhe);
		tiposMovimentoFinanceiroIgnorados.add(tipoMovimentoFinanceiroRecebimentoReparte);
		
		final List<DebitoCreditoCota> listaDebitoCreditoCota = 
				movimentoFinanceiroCotaRepository.
				obterDebitoCreditoSumarizadosParaCotaDataOperacao(
						numeroCota, 
						dataOperacao, 
						tiposMovimentoFinanceiroIgnorados);
		
		BigDecimal totalDebitoCredito = BigDecimal.ZERO;
		
		for(final DebitoCreditoCota debitoCreditoCota : listaDebitoCreditoCota) {
			
			if(debitoCreditoCota.getValor() == null) {
				continue;
			}
			
            if (OperacaoFinaceira.CREDITO.equals(debitoCreditoCota.getTipoLancamento())) {
				
				totalDebitoCredito = totalDebitoCredito.add(debitoCreditoCota.getValor());
				
            } else if (OperacaoFinaceira.DEBITO.equals(debitoCreditoCota.getTipoLancamento())) {
				
				totalDebitoCredito = totalDebitoCredito.subtract(debitoCreditoCota.getValor());
				
			}
			
		}
		
		return totalDebitoCredito;
	}

	    /**
     * Obtém lista de debito crédito relativa a cobrança relacionada com uma
     * operação de encalhe.
     * 
     * @param controleConferenciaEncalheCota
     * 
     * @return List - ComposicaoCobrancaSlipDTO
     */
	@Override
	@Transactional(readOnly=true)
	public List<DebitoCreditoCota> obterDebitoCreditoDeCobrancaPorOperacaoEncalhe(
	        final ControleConferenciaEncalheCota controleConferenciaEncalheCota,
	        final Long idFornecedor){
		
		return this.debitoCreditoCotaService.obterListaDebitoCreditoCotaDTO(
		        controleConferenciaEncalheCota.getCota(), Arrays.asList(controleConferenciaEncalheCota.getDataOperacao()),
		        idFornecedor);
	}
	
	protected String obterSlipReportPath() throws URISyntaxException {
		
		final URL url = Thread.currentThread().getContextClassLoader().getResource("/reports/slip.jasper");
		
		return url.toURI().getPath();
		
	}

	protected String obterSlipSubReportPath() throws URISyntaxException {
	
		final URL subReportDir = Thread.currentThread().getContextClassLoader().getResource("/reports/");
		
		return subReportDir.toURI().getPath();
		
	}
	
	    /**
     * Obtem valor total para geração de crédito na C.E.
     * 
     * @param idControleConferenciaEncalheCota
     * @return BigDecimal
     */
	@Transactional
	@Override
	public BigDecimal obterValorTotalConferenciaEncalhe(final Long idControleConferenciaEncalheCota){
		
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
	@Transactional(readOnly=true)
	public List<ItemAutoComplete> obterListaProdutoEdicaoParaRecolhimentoPorCodigoBarras(final Integer numeroCota, final String codigoBarras) {

		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		List<Date> datasRecolhimentoValidas = lancamentoRepository.obterDatasRecolhimentoValidas();
		
		return this.conferenciaEncalheRepository.obterListaProdutoEdicaoParaRecolhimentoPorCodigoBarras(numeroCota, codigoBarras, dataOperacao, datasRecolhimentoValidas);
	}
	
	@Transactional
	public List<ItemAutoComplete> obterListaProdutoEdicaoParaRecolhimentoPorCodigoSM(
			final Integer numeroCota, 
			final Integer codigoSM,
			final Integer quantidadeRegistros,
			final Map<Long, DataCEConferivelDTO> mapaDataCEConferivelDTO, boolean indCotaOperacaoDif) {
		
		Date dataOperacao = null;
		
		if(!indCotaOperacaoDif) {
			dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		}
        
		boolean indAceitaRecolhimentoParcialAtraso = distribuidorService.distribuidorAceitaRecolhimentoParcialAtraso();
		
		final List<ProdutoEdicao> listaProdutoEdicao = produtoEdicaoRepository.obterProdutoPorCodigoNomeCodigoSM(codigoSM,
                null, numeroCota, quantidadeRegistros, mapaDataCEConferivelDTO, dataOperacao, indAceitaRecolhimentoParcialAtraso);
		
		final List<ItemAutoComplete> listaItem = new ArrayList<ItemAutoComplete>();
		
		if (listaProdutoEdicao != null && !listaProdutoEdicao.isEmpty()){
			
			for (final ProdutoEdicao produtoEdicao : listaProdutoEdicao){
				
				listaItem.add(
						new ItemAutoComplete(codigoSM.toString(),
								codigoSM.toString() + " - " +produtoEdicao.getProduto().getCodigo() + " - " + produtoEdicao.getProduto().getNome() + " - " + produtoEdicao.getNumeroEdicao(), 
								produtoEdicao.getId()));

				
			}
			
			
		}
		
		return listaItem;
		
	}

}
