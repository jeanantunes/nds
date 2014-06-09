package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ClassificacaoNaoRecebidaDTO;
import br.com.abril.nds.dto.CotaQueNaoRecebeClassificacaoDTO;
import br.com.abril.nds.dto.CotaQueRecebeClassificacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroClassificacaoNaoRecebidaDTO;
import br.com.abril.nds.model.distribuicao.ClassificacaoNaoRecebida;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;

public interface ClassificacaoNaoRecebidaRepository extends Repository<ClassificacaoNaoRecebida, Long> {

	List<CotaQueNaoRecebeClassificacaoDTO> obterCotasQueNaoRecebemClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro);
	
	List<CotaQueRecebeClassificacaoDTO> obterCotasQueRecebemClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro);

	List<ClassificacaoNaoRecebidaDTO> obterClassificacoesNaoRecebidasPelaCota(FiltroClassificacaoNaoRecebidaDTO filtro);

	List<TipoClassificacaoProduto> obterClassificacoesRecebidasPelaCota(FiltroClassificacaoNaoRecebidaDTO filtro);
	
}
