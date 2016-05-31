package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DadosDocumentacaoConfEncalheCotaDTO;
import br.com.abril.nds.dto.DataCEConferivelDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.enums.TipoDocumentoConferenciaEncalhe;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.DebitoCreditoCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.exception.ConferenciaEncalheFinalizadaException;
import br.com.abril.nds.service.exception.EncalheRecolhimentoParcialException;
import br.com.abril.nds.service.exception.EncalheSemPermissaoSalvarException;
import br.com.abril.nds.service.exception.FechamentoEncalheRealizadoException;
import br.com.abril.nds.util.ItemAutoComplete;

public interface ConferenciaEncalheService {

	public void sinalizarFimProcessoEncalhe(Integer numeroCota);

	public void sinalizarErroProcessoEncalhe(Integer numeroCota, Exception e);
	
	public void validarCotaProcessandoEncalhe(Integer numeroCota);

	public void criarBackupConferenciaEncalhe(Usuario usuario, InfoConferenciaEncalheCota infoConferenciaEncalheCota, ControleConferenciaEncalheCota controleConferenciaEncalheCota);
	
	/**
	 * Obtém mapa tendo como chave idFornecedor e valor 
	 * o objeto DataCEConferivel (objeto que possui 2 lista
	 * de data ce, uma para produto parcial e outra para produto nao parcial).
	 * 
	 * @param numeroCota
	 * @return DataCEConferivelDTO
	 */
	public Map<Long, DataCEConferivelDTO> obterDatasChamadaEncalheConferiveis(Integer numeroCota);
	
	/**
	 * Retorna uma lista de box de recolhimento.
	 * 
	 * @return List - Box
	 */
	public List<Box> obterListaBoxEncalhe(Long idUsuario);

	/**
	 * Obtem flag indicando se a cota em questão emite Nfe.
	 * 
	 * @param numeroCota
	 * 
	 * @return boolean
	 */
	public boolean isCotaExigeNfe(Integer numeroCota);
	
	/**
	 * Obtém lista de debito crédito relativa a cobrança 
	 * relacionada com uma operação de encalhe.
	 * 
	 * @param controleConferenciaEncalheCota
	 * @param idFornecedor
	 * 
	 * @return List - ComposicaoCobrancaSlipDTO
	 */
	public List<DebitoCreditoCota> obterDebitoCreditoDeCobrancaPorOperacaoEncalhe(
	        ControleConferenciaEncalheCota controleConferenciaEncalheCota, Long idFornecedor);
	
	
	/**
	 * Obtém o primeiro dia de recolhimento para da data de
	 * CE programada da cota de operação diferenciada em questão.
	 * 
	 * @param numeroCota
	 * @param dataRecolhimentoCE
	 * 
	 * @return Date
	 */
	public Date obterDataPrimeiroDiaEncalheOperacaoDiferenciada(Integer numeroCota, Date dataRecolhimentoCE);
	
	/**
	 * Verifica se a cota informada possui operação diferenciada. 
	 * 
	 * Caso possua, é verificado se o dia da semana da data de operação se encontra nos dias
	 * de operação diferenciada configurados para a cota em questão. 
	 * 
	 * @param numeroCota
	 */
	public void verificarCotaOperacaoDiferenciada(Integer numeroCota);

	
	/**
	 * 
	 * @param conferenciaEncalhe
	 * @param cota
	 * @param dataOperacao
	 * @param indConferenciaContingencia
	 * @param validarExemplarZero TODO
	 */
	public boolean validarQtdeEncalheExcedeQtdeReparte(
			ConferenciaEncalheDTO conferenciaEncalhe,
			Cota cota, 
			Date dataOperacao, 
			boolean indConferenciaContingencia, boolean validarExemplarZero);
	
	
	/**
	 * Verifica se a cota em questão possui uma conferencia de encalhe
	 * ja finalizada na data de operação atual.
	 * 
	 * @param numeroCota
	 * 
	 * @return boolean
	 */
	public boolean verificarCotaComConferenciaEncalheFinalizada(Integer numeroCota);
	
	
	/**
	 * Valida a existencia de um fechamento de encalhe para a data em questão, caso positivo
	 * sera lançada uma exceção {@link FechamentoEncalheRealizadoException}
	 * 
	 * @throws FechamentoEncalheRealizadoException
	 */
	public void validarFechamentoEncalheRealizado() throws FechamentoEncalheRealizadoException;
	

	/**
	 * Verifica se a cota cota possui reparte a recolher na data em questão.
	 * 
	 * @param datas
	 * 
	 * @param numeroCota
	 * 
	 * @return boolean
	 */
	public boolean isCotaComReparteARecolherNaDataOperacao(Integer numeroCota, List<Date> datas);
	
	
	/**
	 * Obtém o TipoContabilizacaoCE.
	 * 
	 * @return TipoContabilizacaoCE
	 */
	public TipoContabilizacaoCE obterTipoContabilizacaoCE();

	
	/**
	 * Obtém os dados sumarizados de encalhe da cota, e se esta estiver
	 * com sua conferencia sendo reaberta retorna tambem a lista do que ja foi
	 * conferido.
	 * 
	 * @param numeroCota
	 * @param indConferenciaContingencia
	 * 
	 * @return InfoConferenciaEncalheCota
	 */
	public InfoConferenciaEncalheCota obterInfoConferenciaEncalheCota(Integer numeroCota, boolean indConferenciaContingencia);
	
	/**
	 * Gera documentação referente a conferência de encalhe.
	 * 
	 * @param idControleConferenciaEncalheCota
	 * @param nossoNumero
	 * @param tipoDocumentoConferenciaEncalhe
	 * @param geraNovoNumeroSlip
	 * 
	 * @return byte
	 */
	
	public byte[] gerarDocumentosConferenciaEncalhe(			
			Long idControleConferenciaEncalheCota,
			String nossoNumero,
			TipoDocumentoConferenciaEncalhe tipoDocumentoConferenciaEncalhe,
			boolean geraNovoNumeroSlip);

	
	/**
	 * Obtém dados do produtoEdicao através do id do mesmo se houver chamada de encalhe.
	 * 
	 * @param numeroCota
	 * @param id
	 * 
	 * @return ProdutoEdicaoDTO
	 * 
	 * @throws EncalheRecolhimentoParcialException
	 */
	ProdutoEdicaoDTO pesquisarProdutoEdicaoPorId(Integer numeroCota, Long id) throws EncalheRecolhimentoParcialException;
	
	/**
	 * Obtém detalhes do item de conferencia de encalhe.
	 * 
	 * @param numeroCota
	 * @param idConferenciaEncalhe
	 * @param idProdutoEdicao
	 * 
	 * @return ConferenciaEncalheDTO
	 */
	ConferenciaEncalheDTO obterDetalheConferenciaEncalhe(Integer numeroCota, Long idConferenciaEncalhe, Long idProdutoEdicao);
	
	/**
	 * Salvas os dados de uma operação de conferência de encalhe.
	 * 
	 * @param controleConfEncalheCota
	 * @param listaConferenciaEncalhe
	 * @param usuario
	 * @param indConferenciaContingencia
	 * 
	 * @throws EncalheSemPermissaoSalvarException
	 * @throws ConferenciaEncalheFinalizadaException
	 * 
	 * @return Long
	 */
	public Long salvarDadosConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Usuario usuario,
			boolean indConferenciaContingencia) throws EncalheSemPermissaoSalvarException, ConferenciaEncalheFinalizadaException;
	
	/**
	 * Finaliza uma conferência de encalhe gerando os movimentos financeiros 
	 * relativos a mesma, faz chamada também ao rotinas relativas a cobrança.
	 * 
	 * Retorna objeto com dados da conferencia finalizada para a geração de
	 * documentos relativos a mesma.
	 * 
	 * @param controleConfEncalheCota
	 * @param listaConferenciaEncalhe
	 * @param usuario
	 * @param indConferenciaContingencia
	 * @param reparte
	 * 
	 * @return DadosDocumentacaoConfEncalheCotaDTO
	 */
	public DadosDocumentacaoConfEncalheCotaDTO finalizarConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Usuario usuario,
			boolean indConferenciaContingencia,
			BigDecimal reparte) throws GerarCobrancaValidacaoException;
	
	/**
	 * Obtem valor total para geração de crédito na C.E.
	 * @param idControleConferenciaEncalheCota
	 * @return BigDecimal
	 */
	BigDecimal obterValorTotalConferenciaEncalhe(Long idControleConferenciaEncalheCota);

	public Long[] obterIdsFornecedorDoProduto(ProdutoEdicao produtoEdicao);
	
	List<ItemAutoComplete> obterListaProdutoEdicaoParaRecolhimentoPorCodigoBarras(Integer numeroCota, String codigoBarras);
	
	public List<ItemAutoComplete> obterListaProdutoEdicaoParaRecolhimentoPorCodigoSM(
			final Integer numeroCota, 
			final Integer codigoSM,
			final Integer quantidadeRegistros,
			final Map<Long, DataCEConferivelDTO> mapaDataCEConferivelDTO, boolean indCotaOperacaoDif);
	
	boolean hasCotaAusenteFechamentoEncalhe(Integer numeroCota);
	
	boolean isParcialNaoFinal(final Long idProdutoEdicao);
	
	void isDataRecolhimentoValida(Date dataOperacao, Date dataRecolhimento, Long idProdutoEdicao, boolean indOperacaoDiferenciada);

	BigDecimal obterValorTotalDesconto(Integer numeroCota, Date dataOperacao);

	BigDecimal obterValorTotalReparteSemDesconto(Integer numeroCota,Date dataOperacao);

	BigDecimal obterValorTotalReparte(Integer numeroCota, List<Date> datas);
	
	Cota validarCotaParaInicioConferenciaEncalhe(final Integer numeroCota);

	public void excluirNotasFiscaisPorReabertura(final InfoConferenciaEncalheCota infoConfereciaEncalheCota);

	void verificarControleConferenciaEncalheCotaDuplicaca(
			ControleConferenciaEncalheCota ccec);

	List<ItemAutoComplete> obterListaProdutoEdicaoParaRecolhimentoPorCodigoBarras_cotaVarejo(final String codigoBarras);

	List<ItemAutoComplete> obterProdutoPorCodigoOuNomeCotaVarejo(final String codigoOuNome);
	
}