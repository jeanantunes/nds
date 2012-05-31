package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ComboTipoFornecedorDTO;
import br.com.abril.nds.model.cadastro.TipoFornecedor;

public interface TipoFornecedorService {

	List<ComboTipoFornecedorDTO> obterComboTipoFornecedor();
	
	TipoFornecedor obterTipoFornecedorPorId(Long id);
}
