package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;


public interface ChamadaEncalheCotaRepository extends Repository<ChamadaEncalheCota,Long> {
	
	/**
	 * Obtém lista de idProdutoEdicao referentes a chamada encalhe relacionadas a um 
	 * numeroCota e dataRecolhimento.  
	 * 
	 * Se flag indPesquisaCEFutura for true sera pesquisado registro com 
	 * dataRecolhimento >= a data passado por parâmetro, senão, pesquisará 
	 * registros com dataRecolhimento igual data passada por parâmetro.
	 * 
	 * @param numeroCota
	 * @param dataOperacao
	 * @param indPesquisaCEFutura
	 * @param conferido
	 * 
	 * @return List - Long
	 */
	public List<Long> obterListaIdProdutoEdicaoChamaEncalheCota(Integer numeroCota, Date dataOperacao, boolean indPesquisaCEFutura, boolean conferido);
	
	
	/**
	 * Obtém lista de chamada encalhe cota a partir do 
	 * numeroCota e dataRecolhimento.  
	 * 
	 * Se flag indPesquisaCEFutura for true sera pesquisado registro com 
	 * dataRecolhimento >= a data passado por parâmetro, senão, pesquisará 
	 * registros com dataRecolhimento igual data passada por parâmetro.
	 * 
	 * @param numeroCota
	 * @param dataOperacao
	 * @param idProdutoEdicao
	 * @param indPesquisaCEFutura
	 * @param conferido
	 * 
	 * @return Long
	 */
	public Long obterQtdListaChamaEncalheCota(Integer numeroCota, Date dataOperacao, Long idProdutoEdicao, boolean indPesquisaCEFutura, boolean conferido);

	
	
}
