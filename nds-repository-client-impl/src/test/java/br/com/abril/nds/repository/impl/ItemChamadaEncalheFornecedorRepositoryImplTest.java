package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.ItemChamadaEncalheFornecedorRepository;

public class ItemChamadaEncalheFornecedorRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ItemChamadaEncalheFornecedorRepository itemChamadaEncalheFornecedorRepository;
	
	@Test
	public void teste_obter_total_do_desconto_itens_chamada_encalhe_fornecedor() {
		
		BigDecimal valorTotal = itemChamadaEncalheFornecedorRepository.obterTotalDoDescontoItensChamadaEncalheFornecedor(1L);
		
	}

}
