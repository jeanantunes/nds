package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CotaGarantiaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.NotaPromissoriaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.CaucaoLiquida;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.ContaDepositoCaucaoLiquida;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.GarantiaCotaOutros;
import br.com.abril.nds.model.cadastro.Imovel;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.TipoGarantia;
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
	public abstract CotaGarantiaDTO getByCota(Long idCota);

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
	 * @see br.com.abril.nds.repository.FiadorRepository#buscarPorId(Long)
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
	 * Salva no repositorio de dados a garantia Caução Líquida.
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
	
	CotaGarantiaCaucaoLiquida salvarCaucaoLiquida(List<CaucaoLiquida> listaCaucaoLiquida, Long idCota, PagamentoCaucaoLiquida pagamento, ContaDepositoCaucaoLiquida conta) throws ValidacaoException, InstantiationException, IllegalAccessException ;
	
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
}
