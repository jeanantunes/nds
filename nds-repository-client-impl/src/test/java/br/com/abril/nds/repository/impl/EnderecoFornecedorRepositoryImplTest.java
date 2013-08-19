package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;

public class EnderecoFornecedorRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private EnderecoFornecedorRepositoryImpl enderecoFornecedorRepositoryImpl;
	
	@Test
	public void testarObterEnderecosFornecedor() {
		
		List<EnderecoAssociacaoDTO> enderecosFornecedor;
		
		Long idFornecedor = 1L;
		
		enderecosFornecedor = enderecoFornecedorRepositoryImpl.obterEnderecosFornecedor(idFornecedor);
		
		Assert.assertNotNull(enderecosFornecedor);
		
	}

}
