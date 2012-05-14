package br.com.abril.nds.service;
import java.util.List;

import br.com.abril.nds.client.vo.ParametroCobrancaVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaParametrosCobrancaDTO;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Banco}
 * 
 * @author Discover Technology
 */
public interface ParametroCobrancaService {

	List<ParametroCobrancaVO> obterParametrosCobranca(FiltroConsultaParametrosCobrancaDTO filtro);

	int obterQuantidadeParametrosCobranca(FiltroConsultaParametrosCobrancaDTO filtro);
	
}
