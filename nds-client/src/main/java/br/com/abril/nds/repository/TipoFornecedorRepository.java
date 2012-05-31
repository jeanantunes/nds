package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ComboTipoFornecedorDTO;
import br.com.abril.nds.model.cadastro.TipoFornecedor;

public interface TipoFornecedorRepository extends Repository<TipoFornecedor, Long> {

	List<ComboTipoFornecedorDTO> obterComboTipoFornecedor();
	
}
