package br.com.abril.nds.repository;

import br.com.abril.nds.model.seguranca.ConferenciaEncalheCotaUsuario;

public interface ConferenciaEncalheCotaUsuarioRepository extends Repository<ConferenciaEncalheCotaUsuario, Long> {

	public ConferenciaEncalheCotaUsuario obterPorNumeroCota(Integer numeroCota);
	
	public ConferenciaEncalheCotaUsuario obterPorLogin(String login);
	
	public ConferenciaEncalheCotaUsuario obterPorSessionId(String sessionId);
	
	public int removerPorNumeroCota(Integer numeroCota);
	
	public int removerPorLogin(String login);
	
	public int removerPorSessionId(String sessionId);
	
}