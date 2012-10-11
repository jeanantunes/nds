package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.model.financeiro.BaixaCobranca;
import br.com.abril.nds.model.financeiro.BaixaManual;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.financeiro.BaixaCobranca}  
 * 
 * @author Discover Technology
 */
public interface BaixaCobrancaRepository extends Repository<BaixaCobranca,Long> {

	/**
	 * Busca a última baixa automática realizada no dia
	 * @param dataOperacao
	 * @return BaixaAutomatica
	 */
	public Date buscarUltimaBaixaAutomaticaDia(Date dataOperacao);

	/**
	 * Busca o dia da última baixa automática realizada
	 * @return Date
	 */
	public Date buscarDiaUltimaBaixaAutomatica();
	
	
	public List<CobrancaVO> buscarCobrancasBaixadas(Integer numCota, String nossoNumero);
	
	public BaixaCobranca obterUltimaBaixaCobranca(Long idCobranca);
	
	List<BaixaManual> obterBaixasManual(List<Long> idsCobranca);
	
}
