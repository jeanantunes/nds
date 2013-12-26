package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DadosDocumentacaoConfEncalheCotaDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.enums.TipoDocumentoConferenciaEncalhe;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.exception.ConferenciaEncalheFinalizadaException;
import br.com.abril.nds.service.exception.EncalheRecolhimentoParcialException;
import br.com.abril.nds.service.exception.EncalheSemPermissaoSalvarException;
import br.com.abril.nds.service.exception.FechamentoEncalheRealizadoException;
import br.com.abril.nds.util.ItemAutoComplete;

public interface ConferenciaEncalheService {
	
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
	public boolean isCotaEmiteNfe(Integer numeroCota);
	
	/**
	 * Obtém lista de debito crédito relativa a cobrança 
	 * relacionada com uma operação de encalhe.
	 * 
	 * @param controleConferenciaEncalheCota
	 * 
	 * @return List - ComposicaoCobrancaSlipDTO
	 */
	public List<DebitoCreditoCotaDTO> obterDebitoCreditoDeCobrancaPorOperacaoEncalhe(ControleConferenciaEncalheCota controleConferenciaEncalheCota);
	
	
	/**
	 * 
	 * @param conferenciaEncalhe
	 * @param cota
	 * @param dataOperacao
	 * @param indConferenciaContingencia
	 */
	public void validarQtdeEncalheExcedeQtdeReparte(
			ConferenciaEncalheDTO conferenciaEncalhe,
			Cota cota, 
			Date dataOperacao, 
			boolean indConferenciaContingencia);
	
	
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
	 * Valida se o produto edicao em questão é relalivo a lancamento parcial.
	 * 
	 * @param codigo
	 * @param numeroEdicao
	 * 
	 * @return boolean
	 */
	public boolean isLancamentoParcialProdutoEdicao(String codigo, Long numeroEdicao);

	/**
	 * Verifica se a cota cota possui reparte a recolher na data em questão.
	 * 
	 * @param numeroCota
	 * 
	 * @return boolean
	 */
	public boolean isCotaComReparteARecolherNaDataOperacao(Integer numeroCota);
	
	
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
	 * Obtém dados do produtoEdicao através do código de barras do mesmo se houver chamada de encalhe.
	 * 
	 * @param numeroCota
	 * @param codigoDeBarras
	 * 
	 * @return List<ProdutoEdicaoDTO>
	 * 
	 * @throws EncalheRecolhimentoParcialException
	 */
	List<ProdutoEdicaoDTO> pesquisarProdutoEdicaoPorCodigoDeBarras(Integer numeroCota, String codigoDeBarras) throws EncalheRecolhimentoParcialException;
	
	/**
	 * Obtém dados do produtoEdicao através do código SM do mesmo se houver chamada de encalhe.
	 * 
	 * @param numeroCota
	 * @param sm
	 * 
	 * @return ProdutoEdicaoDTO
	 * 
	 * @throws EncalheRecolhimentoParcialException
	 */
	ProdutoEdicaoDTO pesquisarProdutoEdicaoPorSM(Integer numeroCota, Integer sm) throws EncalheRecolhimentoParcialException;
	
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
	 * @param listaIdConferenciaEncalheParaExclusao
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
			Set<Long> listaIdConferenciaEncalheParaExclusao,
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
	 * @param listaIdConferenciaEncalheParaExclusao
	 * @param usuario
	 * @param indConferenciaContingencia
	 */
	public DadosDocumentacaoConfEncalheCotaDTO finalizarConferenciaEncalhe(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario,
			boolean indConferenciaContingencia) throws GerarCobrancaValidacaoException;
	
	/**
	 * Obtem valor total para geração de crédito na C.E.
	 * @param idControleConferenciaEncalheCota
	 * @return BigDecimal
	 */
	BigDecimal obterValorTotalConferenciaEncalhe(Long idControleConferenciaEncalheCota);

	public Long[] obterIdsFornecedorDoProduto(ProdutoEdicao produtoEdicao);
	
	List<ItemAutoComplete> obterListaProdutoEdicaoParaRecolhimentoPorCodigoBarras(Integer numeroCota, String codigoBarras);
	
	boolean hasCotaAusenteFechamentoEncalhe(Integer numeroCota);
	
	boolean isLancamentoParcial(Long idProdutoEdicao);
	
	void isDataRecolhimentoValida(Date dataOperacao, Date dataRecolhimento, Long idProdutoEdicao);

	BigDecimal obterValorTotalDesconto(Integer numeroCota, Date dataOperacao);

	BigDecimal obterValorTotalReparteSemDesconto(Integer numeroCota,Date dataOperacao);

	BigDecimal obterValorTotalReparte(Integer numeroCota, Date dataOperacao);
	
}