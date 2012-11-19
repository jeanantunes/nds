package br.com.abril.nds.repository.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;

public class TipoMovimentoFinanceiroRepositoryImplTest extends AbstractRepositoryImplTest  {
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;	
	
	@Before
	public void setup() {
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroCredito =
			Fixture.tipoMovimentoFinanceiroCredito();
		
		save(tipoMovimentoFinanceiroCredito);
	}
	
	@Test
	public void buscarTipoMovimentoFinanceiro() {
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository
			.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.CREDITO);
			
		Assert.assertNotNull(tipoMovimentoFinanceiro);
	}
	
	@Test
	public void buscarPorDescricao(){
		
		String descricao = "descricaoteste";
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository.buscarPorDescricao(descricao);
	}
	
}
