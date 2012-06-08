package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.ParametrosDistribuidorVO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ParametroSistema;

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
	
	/**
	 * Retorna a lista de Parametros do Sistema a partir do ParametrosDistribuidorVO
	 * @param ParametrosDistribuidorVO
	 * @return List<ParametroSistema>
	 */
	public List<ParametroSistema> getListaParametrosSistema(ParametrosDistribuidorVO parametrosDistribuidor);
	
}
