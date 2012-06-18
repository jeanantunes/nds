package br.com.abril.nds.service;

import br.com.abril.nds.client.vo.ParametrosDistribuidorVO;
import br.com.abril.nds.model.cadastro.Distribuidor;

/**
 * Interface de serviços do parametrosDistribuidorVO
 * @author InfoA2
 */
public interface ParametrosDistribuidorService {

	/**
	 * Retorna o ParametrosDistribuidorVO utilizando os dados do Distribuidor e ParametrosSistema
	 * @return ParametrosDistribuidorVO
	 */
	public ParametrosDistribuidorVO getParametrosDistribuidor();

	/**
	 * Retorna o Distribuidor a partir do ParametrosDistribuidorVO
	 * @param ParametrosDistribuidorVO
	 * @return Distribuidor
	 */
	public Distribuidor getDistribuidor(ParametrosDistribuidorVO parametrosDistribuidor); 

}
