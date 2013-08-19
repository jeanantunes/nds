package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaEnvioDTO;
import br.com.abril.nds.model.envio.nota.NotaEnvio;

public interface GeracaoNotaEnvioService {

	public abstract List<ConsultaNotaEnvioDTO> busca(FiltroConsultaNotaEnvioDTO filtro);
	
	public abstract Integer buscaCotasNotasDeEnvioQtd(FiltroConsultaNotaEnvioDTO filtro);
	
	public abstract List<NotaEnvio> visualizar(FiltroConsultaNotaEnvioDTO filtro);
	
	public abstract List<NotaEnvio> gerarNotasEnvio(FiltroConsultaNotaEnvioDTO filtro, List<Long> idCotasSuspensasAusentes);

}
