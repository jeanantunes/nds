package br.com.abril.nds.repository;

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
	
	FormaCobranca obterFormaCobrancaPrincipalCota(Long idCota);

	List<FormaCobranca> obterFormasCobrancaCota(Cota cota);
	
	int obterQuantidadeFormasCobrancaCota(Cota cota);
	
	void desativarFormaCobranca(long idFormaCobranca);

	List<FormaCobranca> obterPorCotaETipoCobranca(Long idCota,TipoCobranca tipoCobranca, Long idFormaCobranca);
	
	List<FormaCobranca> obterPorDistribuidorETipoCobranca(Long idDistribuidor,TipoCobranca tipoCobranca, Long idFormaCobranca);

	FormaCobranca obterFormaCobrancaPrincipal();
	
}
