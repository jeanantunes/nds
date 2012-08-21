package br.com.abril.nds.service;

import java.io.InputStream;

import br.com.abril.nds.client.vo.ParametrosDistribuidorVO;

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
	 * Salva a distribuidor com todos os parâmetros informados
	 * 
	 * @param ParametrosDistribuidorVO
	 * @param imgLogotipo
	 * @param imgContentType
	 */
	public void salvarDistribuidor(ParametrosDistribuidorVO parametrosDistribuidor,
								   InputStream imgLogotipo,
								   String imgContentType);
	
	/**
	 * Retorna o logotipo do distribuidor, caso exista.
	 * 
	 * @return InputStream do logotipo
	 */
	public InputStream getLogotipoDistribuidor();

}
