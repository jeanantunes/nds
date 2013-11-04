package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.model.envio.nota.NotaEnvio;

public interface NotaEnvioRepository extends Repository<NotaEnvio, Long> {

	public boolean verificarExistenciaEmissaoDeNotasPeloEstudo(Long idEstudo);
	
	Date obterMenorDataLancamentoPorNotaEnvio(Long numeroNotaEnvio);
	
}
