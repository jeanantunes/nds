package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.Tributacao;

/**
 * 
 * @author Diego Fernandes
 *
 */
public interface TributacaoRepository extends Repository<Tributacao, Long> {
	
	/**
	 * usca a Tributação mais proxima a data de vigencia respentando os outros paramentros.
	 * 
	 * @param codigoEmpresa
	 * @param tipoOperacao
	 * @param ufOrigem
	 * @param ufDestino
	 * @param naturezaOperacao
	 * @param codigoNaturezaOperacao
	 * @param codigoNBM
	 * @param dataVigencia
	 * @param cstICMS
	 * @return
	 */
	public abstract Tributacao buscar(String codigoEmpresa, TipoOperacao tipoOperacao, String ufOrigem,
			String ufDestino, int naturezaOperacao, String codigoNaturezaOperacao, String codigoNBM, Date dataVigencia,
			String cstICMS);
	
	
	/**
	 * Recupera a tributação padrão.
	 * 
	 * @param codigoEmpresa
	 * @param tipoOperacao
	 * @param ufOrigem
	 * @param ufDestino
	 * @param naturezaOperacao
	 * @param codigoNaturezaOperacao
	 * @param codigoNBM
	 * @param dataVigencia
	 * @return
	 */
	public abstract Tributacao tributacaoDefault(String codigoEmpresa, TipoOperacao tipoOperacao, String ufOrigem,
			String ufDestino, int naturezaOperacao, String codigoNaturezaOperacao, String codigoNBM, Date dataVigencia);

	/**
	 * Busca a Tributação mais proxima a data de vigencia respentando os outros paramentros.
	 * @param codigoEmpresa
	 * @param tipoOperacao
	 * @param ufs
	 * @param naturezaOperacao
	 * @param codigoNaturezaOperacao
	 * @param codigoNBM
	 * @param dataVigencia
	 * @param cstICMS
	 * @return
	 */
	public abstract Tributacao buscar(String codigoEmpresa, TipoOperacao tipoOperacao, List<String> ufs,
			int naturezaOperacao, String codigoNaturezaOperacao, String codigoNBM, Date dataVigencia, String cstICMS);

}
