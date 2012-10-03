package br.com.abril.nds.repository;

import java.util.List;

import org.hibernate.criterion.MatchMode;

import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public interface RoteiroRepository extends Repository<Roteiro, Long> {
	
	List<Roteiro> buscarRoteiro(String sortname, Ordenacao ordenacao);
	
	List<Roteiro> buscarRoteiroPorDescricao(String descricao,  MatchMode matchMode);
	
	void atualizaOrdenacao(Roteiro roteiro);
	
	List<Roteiro> buscarRoteiroDeBox(Long idBox);
	
	Integer buscarMaiorOrdemRoteiro();
	
    List<Roteiro> buscarRoteiroEspecial();

    /**
     * Busca os roteiros associados a um box com a descricao recebida
     * @param idBox identificador do Box
     * @param descricaoRoteiro descricao do roteiro para consulta
     * @return lista de roteiros que satisfacam os criterios da consulta
     */
    List<Roteiro> buscarRoteiroDeBox(Long idBox, String descricaoRoteiro);

	List<Roteiro> obterRoteirosPorCota(Integer numeroCota);
	
}
