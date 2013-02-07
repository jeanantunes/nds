package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CotaQueNaoRecebeClassificacaoDTO;
import br.com.abril.nds.dto.CotaQueRecebeClassificacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroClassificacaoNaoRecebidaDTO;

public interface ClassificacaoNaoRecebidaService {

	List<CotaQueRecebeClassificacaoDTO> obterCotasQueRecebemClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro);

	List<CotaQueNaoRecebeClassificacaoDTO> obterCotasQueNaoRecebemClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro);
	
	void excluirClassificacaoNaoRecebida(Long id);
	
}
