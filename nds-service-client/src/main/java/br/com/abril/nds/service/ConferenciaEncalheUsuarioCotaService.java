package br.com.abril.nds.service;

import br.com.abril.nds.model.seguranca.ConferenciaEncalheUsuarioCota;

/**
 * @author InfoA2
 */
public interface ConferenciaEncalheUsuarioCotaService {

	public ConferenciaEncalheUsuarioCota obterPorCota(Integer numeroCota);
	
	public ConferenciaEncalheUsuarioCota obterPorLogin(String login);
	
	public ConferenciaEncalheUsuarioCota obterPorSessionId(String sessionId);
	
	public boolean removerPorLogin(Integer numeroCota);
	
	public boolean removerPorLogin(String login);
    
}