package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;

public class MovimentoFinanceiroCotaRepositoryImplTest extends AbstractRepositoryImplTest  {
	
	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;	
	
	@Before
	public void setup() {
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56.003.315/0001-47", "333.333.333.333", "distrib_acme@mail.com");
		save(juridicaDistrib);
		
		Distribuidor distribuidor = Fixture.distribuidor(juridicaDistrib, new Date());
		save(distribuidor);
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroCredito =
			Fixture.tipoMovimentoFinanceiroCredito();
		save(tipoMovimentoFinanceiroCredito);
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		Box box = Fixture.criarBox("Box-1", "BX-001", TipoBox.REPARTE);
		save(box);
		
		PessoaFisica manoel = Fixture.pessoaFisica("319.435.088-95",
				"developertestermail@gmail.com", "Manoel da Silva");
		save(manoel);
		
		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box);
		save(cotaManoel);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroCredito, usuarioJoao,
				new BigDecimal(200), null, new Date());
		save(movimentoFinanceiroCota);
	}
	
	@Test
	public void obterMovimentoFinanceiroCotaDataOperacao() {
		
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiro =
			movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCotaDataOperacao(null);
			
		Assert.assertTrue(!listaMovimentoFinanceiro.isEmpty());
	}
	
}
