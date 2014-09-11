package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.client.vo.FormaCobrancaDefaultVO;
import br.com.abril.nds.dto.FormaCobrancaFornecedorDTO;
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
	
	List<FormaCobrancaDefaultVO> obterFormaCobrancaDefault();

	List<FormaCobranca> obterFormasCobrancaCota(Cota cota);
	
	int obterQuantidadeFormasCobrancaCota(Cota cota);
	
	void desativarFormaCobranca(long idFormaCobranca);

	List<FormaCobranca> obterPorCota(Long idCota, Long idFormaCobranca);
	
	List<FormaCobranca> obterPorDistribuidor(Long idDistribuidor, Long idFormaCobranca);

	List<FormaCobrancaDefaultVO> obterFormaCobrancaCotaDefault(Integer numeroCota);
	
	/**
	 * Obtem FormaCobranca da Cota
	 * @param idCota
	 * @param idFornecedor
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
	FormaCobranca obterFormaCobranca(Long idCota, Long idFornecedor, Integer diaDoMes, Integer diaDaSemana);
	
	/**
	 * Obtem FormaCobranca do Distribuidor
	 * @param idFornecedor
	 * @param data
	 * @param valor
	 * @param principal
	 * @return FormaCobranca
	 */
	FormaCobranca obterFormaCobranca(Long idFornecedor, Integer diaDoMes, Integer diaDaSemana, boolean principal);
	
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
	
	/**
	 * Obtem as formas de cobranca atva/inativa das cotas
	 * @param ativa
	 * @return
	 */
	boolean obterFormasCobrancaAtivaCotas(Boolean ativa, Long idFormaCobranca);

	void removerFormasCobrancaCota(Integer numeroCota);

	/**
	 * Obtem lista de FormaCobranca ativa da cota ou do distribuidor
	 * Onde a concentração de pagamento é compatível com a data de operação atual
	 * 
	 * @param idFornecedor
	 * @param diaDoMes
	 * @param diaDaSemana
	 * @return List<FormaCobranca>
	 */
	List<FormaCobranca> obterFormasCobrancaPorFornecedor(Long idFornecedor, Integer diaDoMes, Integer diaDaSemana);

    FormaCobranca obterFormaCobrancaCompleto();

	List<FormaCobrancaFornecedorDTO> obterFormasCobrancaDistribuidorFornecedor();

	List<FormaCobrancaFornecedorDTO> obterFormasCobrancaCotaFornecedor();
	
}
