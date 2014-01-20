package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.ParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorEmissaoDocumento;

public interface ParametrosDistribuidorEmissaoDocumentoRepository extends Repository<ParametrosDistribuidorEmissaoDocumento, Long> {

	public void alterarOuCriar(ParametrosDistribuidorEmissaoDocumento parametrosDistribuidorEmissaoDocumento);

	/**
	 * Obtem parâmetro utiliza impressao de acordo com o tipo 
	 * de documento do distribuidor pesquisado.
	 * 
	 * @param tipoParametrosDistribuidorEmissaoDocumento
	 * 
	 * @return boolean
	 */
	public boolean isUtilizaImpressao(TipoParametrosDistribuidorEmissaoDocumento tipoParametrosDistribuidorEmissaoDocumento);
	
	/**
	 * Obtem parâmetro utiliza envio por email de acordo com o tipo 
	 * de documento do distribuidor pesquisado.
	 * 
	 * @param tipoParametrosDistribuidorEmissaoDocumento
	 * 
	 * @return boolean
	 */
	public boolean isUtilizaEnvioEmail(TipoParametrosDistribuidorEmissaoDocumento tipoParametrosDistribuidorEmissaoDocumento);
	
}
