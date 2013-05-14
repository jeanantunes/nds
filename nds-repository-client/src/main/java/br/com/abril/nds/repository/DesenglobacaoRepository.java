package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.distribuicao.Desenglobacao;

public interface DesenglobacaoRepository extends Repository<Desenglobacao, Long> {

    	List<Desenglobacao> obterDesenglobacaoPorCota(Integer numeroCota);
	
	void inserirCotasDesenglobadas(final List<Desenglobacao> cotasDesenglobadas);
	
	Float verificaPorcentagemCota(Long idCota);
	
	public List<Desenglobacao> obterDesenglobacaoPorCotaDesenglobada(Integer numeroCota);

	boolean removerPorCotaDesenglobada(Long idCota);
}
