package br.com.abril.nds.service;
import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.client.vo.TipoDescontoVO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.TipoDesconto;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TipoDescontoCota}
 * 
 */
public interface TipoDescontoService {

	void incluirDescontoGeral(TipoDesconto tipoDesconto);
	
	void atualizarDistribuidores(BigDecimal desconto);
	
	List<TipoDescontoVO> obterTipoDescontoGeral();

	Cota obterCota(int numeroCota);
	
	List<TipoDesconto> obterTodosTiposDescontos();
	
	TipoDesconto obterTipoDescontoPorID(Long id);
}
	
