package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;

/**
 * Interface que define os parametros de cobranca da cota
 * {@link br.com.abril.nds.model.cadastro.ParametroCobrancaCota}  
 * 
 * @author Discover Technology
 *
 */
public interface ParametroCobrancaCotaRepository extends Repository<ParametroCobrancaCota,Long>{
	
	List<BigDecimal> comboValoresMinimos();

}
