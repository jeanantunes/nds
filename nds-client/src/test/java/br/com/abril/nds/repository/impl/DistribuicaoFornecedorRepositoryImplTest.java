package br.com.abril.nds.repository.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DistribuicaoFornecedorRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private DistribuicaoFornecedorRepositoryImpl distribuicaoFornecedorRepository;
	
	@Test
	public void testObterDiasSemanaDistribuicao(){
		distribuicaoFornecedorRepository.obterDiasSemanaDistribuicao("1", 1L);
	}
}
