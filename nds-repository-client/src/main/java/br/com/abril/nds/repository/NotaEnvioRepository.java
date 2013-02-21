package br.com.abril.nds.repository;

import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.planejamento.Estudo;

public interface NotaEnvioRepository extends Repository<NotaEnvio, Long> {

	public boolean verificarExistenciaEmissaoDeNotasPeloEstudo(Long idEstudo);
}
