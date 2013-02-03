package br.com.abril.nds.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.MovimentoEstoqueService;

public class MovimentoEstoqueServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	MovimentoEstoqueService movimentoEstoqueService;
	
	private Cota cotaManoel;
	
	@Before
	public void setup() {
		
		
		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
		save(manoel);
		
		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
	}
	
	@Test
	public void obterMovimentoPorTipo(){
		Date data = Fixture.criarData(28, Calendar.FEBRUARY, 2012);
		
		//movimentoEstoqueService.enviarSuplementarCotaAusente(data, cotaManoel.getId());
		
		
	}

}
