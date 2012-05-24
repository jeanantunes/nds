package br.com.abril.nds.service;

import java.util.List;

import org.hibernate.criterion.MatchMode;


import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.RotaRoteiroOperacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public interface RoteirizacaoService {
	
	List<Roteiro> buscarRoteiro(String sortname, Ordenacao ordenacao);
	
	List<Rota> buscarRota(String sortname, Ordenacao ordenacao);
	
	/**
	 * Busca os RotaRoteiro respeitando as restricoes parametrizadas.
 	 * @param codigoBox Código do Box
	 * @param tipoBox Tipo do Box {@link TipoBox}
	 * @param postoAvancado se restringe apenas a postos avançados
	 * @param orderBy nome do campo para compor a ordenação
	 * @param ordenacao tipo da ordenação
	 * @param initialResult resultado inicial
	 * @param maxResults numero maximo de resultados
	 * @return
	 */
	List<RotaRoteiroOperacao> busca(Long idBox, Long idRoteiro, Long idRota , String orderBy,
			Ordenacao ordenacao, int initialResult, int maxResults);
	
	
	void incluirRoteiro(Roteiro roteiro);
	
	List<Roteiro> buscarRoteiroPorDescricao(String descricao, MatchMode matchMode);
	
	List<Rota> buscarRotaPorRoteiro(Long idRoteiro);
	
	void incluirRota(Rota rota);
	
	void  excluirListaRota(List<Long> rotasId);
}
