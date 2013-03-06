package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;


/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.FormaCobranca}  
 * 
 * @author Discover Technology
 *
 */
public interface FormaCobrancaRepository extends Repository<FormaCobranca,Long>{
	
	
	FormaCobranca obterPorTipoEBanco(TipoCobranca tipo, Banco banco);
	
	List<Banco> obterBancosPorTipoDeCobranca(TipoCobranca tipo);

	List<FormaCobranca> obterFormasCobrancaCota(Cota cota);
	
	int obterQuantidadeFormasCobrancaCota(Cota cota);
	
	void desativarFormaCobranca(long idFormaCobranca);

	List<FormaCobranca> obterPorCotaETipoCobranca(Long idCota,TipoCobranca tipoCobranca, Long idFormaCobranca);
	
	List<FormaCobranca> obterPorDistribuidorETipoCobranca(TipoCobranca tipoCobranca, Long idFormaCobranca);

	/**
	 * Obtem FormaCobranca da Cota
	 * @param numeroCota
	 * @param fornecedoresId
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
	FormaCobranca obterFormaCobranca(Integer numeroCota, List<Long> fornecedoresId, Integer diaDoMes, Integer diaDaSemana, BigDecimal valor);
	
	/**
	 * Obtem FormaCobranca do Distribuidor
	 * @param fornecedoresId
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
	FormaCobranca obterFormaCobranca(List<Long> fornecedoresId, Integer diaDoMes, Integer diaDaSemana, BigDecimal valor);
	
	/**
	 * Obtem FormaCobranca principal da Cota
	 * @param idCota
	 * @return FormaCobranca
	 */
	FormaCobranca obterFormaCobranca(Long idCota);
	
	/**
	 * Obtem FormaCobranca principal do Distribuidor
	 * @return FormaCobranca
	 */
	FormaCobranca obterFormaCobranca();
}
