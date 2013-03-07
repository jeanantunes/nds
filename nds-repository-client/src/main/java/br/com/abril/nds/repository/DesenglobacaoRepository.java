package br.com.abril.nds.repository;

import br.com.abril.nds.model.distribuicao.Desenglobacao;

public interface DesenglobacaoRepository extends Repository<Desenglobacao, Long>{
	
	void obterDesenglobacaoPorCota();

}
