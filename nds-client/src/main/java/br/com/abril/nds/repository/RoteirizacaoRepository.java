package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.Roteirizacao;

public interface RoteirizacaoRepository extends Repository<Roteirizacao, Long> {
	
	Roteirizacao buscarRoteirizacaoDeCota(Integer numeroCota);
	
	/**
	 * Retorna uma lista de Roteirizacao.
	 * @param sortname - nome do campo para ordenação
	 * @param ordenacao - tipo da ordenção 
	 * @return List<Rota>
	 */
    List<Roteirizacao> buscarRoterizacaoPorRota(Long rotaId);
}

