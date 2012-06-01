package br.com.abril.nds.repository;

import java.util.List;

import org.hibernate.criterion.MatchMode;

import br.com.abril.nds.dto.RotaRoteiroDTO;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;


public interface RotaRepository extends Repository<Rota, Long> {

	List<RotaRoteiroDTO> buscarRotasRoteiroAssociacao(String sortname, String sortorder);
	
	/**
	 * Retorna uma lista de rotas.
	 * @param sortname - nome do campo para ordenação
	 * @param ordenacao - tipo da ordenção 
	 * @return List<Rota>
	 */
	List<Rota> buscarRota(String sortname, Ordenacao ordenacao);
	
	/**
	 * Retorna uma lista de rotas referente um roteiro.
	 * @param roteiroId - Id do roteiro
	 * @param sortname - nome do campo para ordenação
	 * @param ordenacao - tipo da ordenção 
	 * @return List<Rota>
	 */
	List<Rota> buscarRotaPorRoteiro(Long roteiroId, String sortname, Ordenacao ordenacao );
	 
	/**
	 * atualiza a ordenação das rotas.
	 * 
	 */
	void atualizaOrdenacao(Rota rota );
	
	/**
	 * Retorna uma lista de rotas referente um roteiro e nome da rota.
	 * @param roteiroId - Id do roteiro
	 * @param rotaNome - nome da rota
	 * @param matchMode - matchMode 
	 * @return List<Rota>
	 */
	List<Rota> buscarRotaPorNome(Long roteiroId, String rotaNome , MatchMode matchMode);
	
	List<Roteirizacao> buscarRoterizacaoPorRotaRoteiro(Long rotaId, Long roteiroId);
   /**
	* Retorna uma lista de rotas associadas a um determinado box
	 * @param idBox - identificador do box
	 * @return List<Rota>
	 */
	List<Rota> buscarRotaDeBox(Long idBox);
	
    List<Rota> buscarRotaDeRoteiro(String descRoteiro);

}