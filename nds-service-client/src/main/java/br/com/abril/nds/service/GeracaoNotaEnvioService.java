package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaEnvioDTO;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.util.Intervalo;

public interface GeracaoNotaEnvioService {

	public abstract List<ConsultaNotaEnvioDTO> busca(FiltroConsultaNotaEnvioDTO filtro);
	
	public abstract Integer buscaCotasNotasDeEnvioQtd(FiltroConsultaNotaEnvioDTO filtro);

	public abstract NotaEnvio gerar(Long idCota, Long idRota, String chaveAcesso,
			Integer codigoNaturezaOperacao, String descricaoNaturezaOperacao, Date dataEmissao, Intervalo<Date> periodo, List<Long> listaIdFornecedores);

	public abstract NotaEnvio visualizar(Integer numeroCota, Long idRota, String chaveAcesso,
			Integer codigoNaturezaOperacao, String descricaoNaturezaOperacao, Date dataEmissao, Intervalo<Date> periodo, List<Long> listaIdFornecedores);
	
	public abstract List<NotaEnvio> gerarNotasEnvio(FiltroConsultaNotaEnvioDTO filtro, List<Long> idCotasSuspensasAusentes);

}
