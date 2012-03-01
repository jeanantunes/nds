package br.com.abril.nds.repository.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;

public class DistribuicaoFornecedorRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private DistribuicaoFornecedorRepository distribuicaoFornecedorRepository;
	
	@Test
	public void testObterDiasSemanaDistribuicao(){
		try {
			this.distribuicaoFornecedorRepository.obterDiasSemanaDistribuicao("1", 1L);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
