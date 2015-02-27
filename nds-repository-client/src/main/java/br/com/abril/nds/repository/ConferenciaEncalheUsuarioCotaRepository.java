package br.com.abril.nds.repository;

import br.com.abril.nds.model.seguranca.ConferenciaEncalheUsuarioCota;

public interface ConferenciaEncalheUsuarioCotaRepository extends Repository<ConferenciaEncalheUsuarioCota, Long> {

	public ConferenciaEncalheUsuarioCota obterPorNumeroCota(Integer numeroCota);
	
	public ConferenciaEncalheUsuarioCota obterPorLogin(String login);
	
	public ConferenciaEncalheUsuarioCota obterPorSessionId(String sessionId);
	
	public boolean removerPorNumeroCota(Integer numeroCota);
	
	public boolean removerPorLogin(String login);
	
}