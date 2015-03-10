package br.com.abril.nds.service;

import br.com.abril.nds.model.seguranca.ConferenciaEncalheCotaUsuario;

/**
 * @author InfoA2
 */
public interface ConferenciaEncalheUsuarioCotaService {

	public ConferenciaEncalheCotaUsuario obterPorCota(Integer numeroCota);
	
	public ConferenciaEncalheCotaUsuario obterPorLogin(String login);
	
	public ConferenciaEncalheCotaUsuario obterPorSessionId(String sessionId);
	
	public boolean removerPorCota(Integer numeroCota);
	
	public boolean removerPorLogin(String login);
    
}