package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ComboTipoFornecedorDTO;
import br.com.abril.nds.repository.TipoFornecedorRepository;

public class TipoFornecedorRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private TipoFornecedorRepository tipoFornecedorRepository;

	@Test
	public void obterComboTipoFornecedor() {

		List<ComboTipoFornecedorDTO> lista = tipoFornecedorRepository
				.obterComboTipoFornecedor();

		Assert.assertNotNull(lista);
	}

}
