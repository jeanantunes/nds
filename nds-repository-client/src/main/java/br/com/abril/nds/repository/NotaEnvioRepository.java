package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.NotaEnvioProdutoEdicao;
import br.com.abril.nds.model.envio.nota.NotaEnvio;

public interface NotaEnvioRepository extends Repository<NotaEnvio, Long> {

	public boolean verificarExistenciaEmissaoDeNotasPeloEstudo(Long idEstudo);
	
	Date obterMenorDataLancamentoPorNotaEnvio(Long numeroNotaEnvio);
	
	List<NotaEnvioProdutoEdicao> obterNotaEnvioProdutoEdicao(Integer numeroCota, List<Long> idsProdutoEdicao);
	
}
