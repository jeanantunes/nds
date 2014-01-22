package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ClassificacaoNaoRecebidaDTO;
import br.com.abril.nds.dto.CotaQueNaoRecebeClassificacaoDTO;
import br.com.abril.nds.dto.CotaQueRecebeClassificacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroClassificacaoNaoRecebidaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.distribuicao.ClassificacaoNaoRecebida;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;

public interface ClassificacaoNaoRecebidaService {

	List<CotaQueNaoRecebeClassificacaoDTO> obterCotasQueNaoRecebemClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro);

	List<CotaQueRecebeClassificacaoDTO> obterCotasQueRecebemClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro);
	
	void excluirClassificacaoNaoRecebida(Long id);

	void inserirListaClassificacaoNaoRecebida(List<ClassificacaoNaoRecebida> listaClassificacaoNaoRecebida);

	List<ClassificacaoNaoRecebidaDTO> obterClassificacoesNaoRecebidasPelaCota(FiltroClassificacaoNaoRecebidaDTO filtro);

	List<TipoClassificacaoProduto> obterClassificacoesRecebidasPelaCota(FiltroClassificacaoNaoRecebidaDTO filtro);

	List<ClassificacaoNaoRecebidaDTO> obterClassificacoesNaoRecebidasPelaCota(Cota cota); 
}
