package br.com.abril.nds.service;
import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.dto.ChequeCaucaoDTO;
import br.com.abril.nds.dto.CotaGarantiaDTO;
import br.com.abril.nds.dto.FormaCobrancaCaucaoLiquidaDTO;
import br.com.abril.nds.dto.ImovelDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.NotaPromissoriaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.CaucaoLiquida;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.ContaBancariaDeposito;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.GarantiaCotaOutros;
import br.com.abril.nds.model.cadastro.Imovel;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaChequeCaucao;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaFiador;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaImovel;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaNotaPromissoria;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaOutros;
import br.com.abril.nds.model.cadastro.garantia.pagamento.PagamentoCaucaoLiquida;

/**
 * Interface que define os serviços referentes ao cadastro de garantias da cota.
 *
 * @author Discover Technology
 */
public interface CotaGarantiaService {

	/**
	 * Recupera a garantia da cota.
	 *
	 * @param idCota
	 *            Id da cota.
	 * @return
	 */
	public abstract CotaGarantiaDTO<CotaGarantia> getByCota(Long idCota);

	/**
	 * Salva no repositorio de dados a garantia de nota promissoria.
	 *
	 * @param notaPromissoria
	 *            Nota Promissoria
	 * @param idCota
	 *            Id da Cota
	 * @return
	 * @throws ValidacaoException
	 *             Caso ocorra uma violação de relacionamento na entidade.
	 */
	public abstract CotaGarantiaNotaPromissoria salvaNotaPromissoria(
			NotaPromissoria notaPromissoria, Long idCota)
			throws ValidacaoException, InstantiationException,
			IllegalAccessException;

	/**
	 * @return
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obtemTiposGarantiasAceitas()
	 */
	public abstract List<TipoGarantia> obtemTiposGarantiasAceitas();
	
	/**
	 * Obtem Imoveis da Da Cota Garantia
	 * @param idCota
	 * @return List<ImovelDTO>
	 */
	public List<ImovelDTO> obterDadosImoveisDTO(Long idCota);
	
	/**
	 * Obtem Imoveis da Da Cota Garantia
	 * @param idCota
	 * @return List<Imovel>
	 */
	public List<Imovel> obterDadosImoveis(Long idCota);
	
	/**
	 * Obtem ChequeCaucao da Da Cota Garantia
	 * @param idCota
	 * @return ChequeCaucaoDTO
	 */
	public ChequeCaucaoDTO obterDadosChequeCaucaoDTO(Long idCota);
	
	/**
	 * Obtem ChequeCaucao da Da Cota Garantia
	 * @param idCota
	 * @return Cheque
	 */
	public Cheque obterDadosChequeCaucao(Long idCota);
	
	/**
	 * Obtem NotaPromissoria da Da Cota Garantia
	 * @param idCota
	 * @return NotaPromissoria
	 */
	public NotaPromissoria obterDadosNotaPromissoria(Long idCota);

	/**
	 * Salva no repositorio de dados a garantia de imóvel.
	 *
	 * @param listaImovel
	 *            lista de imóveis
	 * @param idCota
	 *            Id da cota
	 * @return
	 * @throws ValidacaoException
	 *             Caso ocorra uma violação de relacionamento na entidade.
	 */
	public abstract CotaGarantiaImovel salvaImovel(List<Imovel> listaImoveis,
			Long idCota) throws ValidacaoException, InstantiationException,
			IllegalAccessException;

	/**
	 * Salva no repositorio de dados a garantia de um cheque caução.
	 *
	 * @param cheque
	 *            Cheque
	 * @param idCota
	 *            Id da Cota
	 * @return cotaGarantiaChequeCaucao salva no respositório.
	 * @throws ValidacaoException
	 *             Caso ocorra uma violação de relacionamento na entidade.
	 */
	public abstract CotaGarantiaChequeCaucao salvaChequeCaucao(Cheque cheque,
			Long idCota) throws ValidacaoException, InstantiationException,
			IllegalAccessException;

	/**
	 * @param nome
	 * @param maxResults
	 * @return
	 * @see br.com.abril.nds.repository.FiadorRepository#buscaFiador(java.lang.String,
	 *      int)
	 */
	public abstract List<ItemDTO<Long, String>> buscaFiador(String nome,
			int maxResults);

	/**
	 * Recupera o fiador por id
	 *
	 * @param idFiador
	 * @param doc
	 *            Documento de identificação do Fiador
	 * @return
	 * @see br.com.abril.nds.repository.FiadorRepository
	 */
	public Fiador getFiador(Long idFiador, String doc);

	/**
	 * Salva no repositorio de dados a garantia de um fiador.
	 *
	 * @param idFiador
	 * @param idCota
	 * @return
	 * @throws ValidacaoException
	 */
	public abstract CotaGarantiaFiador salvaFiador(Long idFiador, Long idCota)
			throws ValidacaoException, InstantiationException,
			IllegalAccessException;	

	/**
	 * @param idCheque
	 * @return
	 * @see br.com.abril.nds.repository.CotaGarantiaRepository#getCheque(long)
	 */
	public abstract byte[] getImageCheque(long idCheque);

	/**
	 * Salva a imagem do cheque
	 *
	 * @param idCheque
	 * @param image
	 */
	public abstract void salvaChequeImage(long idCheque, byte[] image);

	/**
	 * Recupera os dados para impressao de nota promissoria.
	 * <ul>
	 * <li>Data de vencimento por extenso: conforme preenchido na nota.</li>
	 * <li>Nome Beneficiário / Credor: Nome do Distribuidor</li>
	 * <li>Quantia: Valor na nota promissória, conforme preenchido.</li>
	 * <li>Praça de Pagamento: Cidade em que o distribuidor reside para
	 * Pagamento da Nota Promissória.</li>
	 * <li>Emitente: Nome do Devedor, conforme cadastro da cota em que estiver
	 * emitindo a nota.</li>
	 * <li>CPF do Emitente: CPF do Devedor, conforme cadastro da cota em que
	 * estiver emitindo a nota.</li>
	 * <li>Endereço do Emitente: Endereço principal do Devedor, conforme
	 * cadastro da cota em que estiver emitindo a nota.</li>
	 * <li>Data de Emissão: Data do dia que o cadastro está sendo realizado
	 * (default do sistema).</li>
	 * </ul>
	 *
	 *
	 * @param idCota
	 * @return
	 */
	public abstract NotaPromissoriaDTO getDadosImpressaoNotaPromissoria(
			long idCota);

	/**
	 * Obtem endereço principal do fiador
	 * @param idFiador
	 * @return Endereco
	 */
	public abstract Endereco buscaEnderecoFiadorPrincipal(Long idFiador);
	
	 /**
	 * Salva no repositorio de dados a garantia outros.
	 * 
	 * @param listaOutros
	 * @param idCota
	 * 
	 * @return CotaGarantiaOutros
	 */
	public abstract CotaGarantiaOutros salvaOutros(List<GarantiaCotaOutros> listaOutros,
			Long idCota) throws ValidacaoException, InstantiationException, IllegalAccessException;
     
	/** Salva no repositorio de dados a garantia Caução Líquida.
	 *
	 * @param listaCaucaoLiquida
	 *            Caução Liquida
	 * @param idCota
	 *            Id da Cota
	 * @return CotaGarantiaCaucaoLiquia salva no repositório.
	 * @throws ValidacaoException
	 *             Caso ocorra uma violação de relacionamento na entidade.
	 */
	public abstract CotaGarantiaCaucaoLiquida salvarCaucaoLiquida(
			List<CaucaoLiquida> listaCaucaoLiquida, Long idCota)
			throws ValidacaoException, InstantiationException, IllegalAccessException;
	
    CotaGarantiaCaucaoLiquida salvarCaucaoLiquida(List<CaucaoLiquida> listaCaucaoLiquida, Long idCota, PagamentoCaucaoLiquida pagamento, ContaBancariaDeposito conta) throws ValidacaoException, InstantiationException, IllegalAccessException ;

	/**
	 * Salva Calção Liquida com forma de cobrança Boleto
	 * @param listaCaucaoLiquida
	 * @param idCota
	 * @param formaCobrancaDTO
	 * @return CotaGarantiaCaucaoLiquida
	 * @throws ValidacaoException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	CotaGarantiaCaucaoLiquida salvarCaucaoLiquida(List<CaucaoLiquida> listaCaucaoLiquida, Long idCota, FormaCobrancaCaucaoLiquidaDTO formaCobrancaDTO) throws ValidacaoException, InstantiationException, IllegalAccessException;

	/**
	 * Método responsável por obter os dados da forma de cobranca
	 * @param idFormaCobranca: ID da forma de cobranca
	 * @return Data Transfer Object com os dados da forma de cobranca
	 */
	FormaCobrancaCaucaoLiquidaDTO obterDadosCaucaoLiquida(Long idCota);

	/**
     * Recupera as informações de garantia da histórico de titularidade da cota
     * 
     * @param idCota
     *            identificador da cota
     * @param idHistorico
     *            identificador do histórico
     * @return DTO com as informações da garantia do histórico de titularidade
     *         da cota
     */
	CotaGarantiaDTO<?> obterGarantiaHistoricoTitularidadeCota(Long idCota, Long idHistorico);
	
    /**
     * Recupera as informações da caução líquida do histórico de titularidade da
     * cota
     * 
     * @param idCota
     *            identificador da cota
     * @param idHistorico
     *            identificador do histórico
     * @return DTO com as informações da caução líquida do histórico de
     *         titularidade da cota
     */
    FormaCobrancaCaucaoLiquidaDTO obterCaucaoLiquidaHistoricoTitularidadeCota(Long idCota, Long idHistorico);

    /**
     * Retorna a imagem da garantia cheque caução do histórico de titularidade
     * da cota
     * 
     * @param idCota
     *            identificador da cota
     * @param idHistorico
     *            identificador do histórico de titularidade
     * @return imagem do cheque caução do histórico de titularidade da cota
     */
	byte[] getImagemChequeCaucaoHistoricoTitularidade(Long idCota, Long idHistorico);

	Long getQtdCotaGarantiaByCota(Long idCota);
	
	BigDecimal obterValorComissaoCaucaoLiquida(Long idCota);

	/**
	 * Método responsável por obter lista de CaucaoLiquida da cota
	 * @param idCota: ID da cota
	 * @return List<CaucaoLiquida>
	 */
	List<CaucaoLiquida> obterCaucaoLiquidasCota(Long idCota);

	/**
	 * Método responsável por obter lista de {@link GarantiaCotaOutros} da cota
	 * @param idCota: ID da cota
	 * @return List<GarantiaCotaOutros>
	 */
	List<GarantiaCotaOutros> obterDadosGarantiaOutrosDTO(Long idCota);

	List<String> validarDadosCotaPreImpressao(Long idCota);

	boolean existeCaucaoLiquidasCota(Long idCota);

}
