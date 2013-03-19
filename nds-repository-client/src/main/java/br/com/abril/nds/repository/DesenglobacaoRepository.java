package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.distribuicao.Desenglobacao;

public interface DesenglobacaoRepository extends Repository<Desenglobacao, Long>{

    	List<Desenglobacao> obterDesenglobacaoPorCota(Long cotaId);
	
	void inserirCotasDesenglobadas(final List<Desenglobacao> cotasDesenglobadas);
	
	Float verificaPorcentagemCota(Long cotaId);
}
