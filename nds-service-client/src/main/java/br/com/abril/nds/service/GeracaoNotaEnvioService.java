package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaEnvioDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;

public interface GeracaoNotaEnvioService {

	public abstract List<ConsultaNotaEnvioDTO> busca(FiltroConsultaNotaEnvioDTO filtro);
	
	public abstract Integer buscaCotasNotasDeEnvioQtd(FiltroConsultaNotaEnvioDTO filtro);
	
	public abstract List<NotaEnvio> visualizar(FiltroConsultaNotaEnvioDTO filtro);
	
	public abstract List<NotaEnvio> gerarNotasEnvio(FiltroConsultaNotaEnvioDTO filtro);
	
	public abstract void gerarNotaEnvioAtravesNotaFiscal(NotaFiscal notaFiscal);
	
	public abstract ValidacaoException enviarEmail(FiltroConsultaNotaEnvioDTO filtro);

}