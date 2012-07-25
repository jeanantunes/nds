package br.com.abril.nds.service;
import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.client.vo.TipoDescontoCotaVO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoDescontoDistribuidor;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TipoDescontoDistribuidor}
 * 
 */
public interface TipoDescontoDistribuidorService {

	void incluirDescontoDistribuidor(TipoDescontoDistribuidor tipoDescontoDistribuidor);
	
	void atualizarDistribuidores(BigDecimal desconto);
	
	int obterUltimoSequencial();
	
	void excluirDesconto(TipoDescontoDistribuidor tipoDescontoDistribuidor );
	
	List<Distribuidor> obterDistribuidores();
	
	List<TipoDescontoCotaVO> obterTipoDescontoDistribuidor();
	
}
	
