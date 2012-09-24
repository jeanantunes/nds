package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.RoteirizacaoService;

public class RoteirizacaoServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	private PDV pdvManoel;
	
	private Cota cotaManoel;
	
	private PessoaFisica manoel;
	
	private static Box box;
	
	private static Roteirizacao roteirizacao;
	
	private static Roteiro roteiro1;
	
	private static Roteiro roteiro2;
	
	private static Roteiro roteiro3;
	
	private static Rota rota1;
	
	private static Rota rota2;
	
	private static Rota rota3;
	
	private static Rota rota4;
	
	private static Rota rota5;
	
	private static Rota rota6;
	
	private static Rota rota7;
	
	private static Rota rota8;
	
	private static Rota rota9;
	
	@Before
	public void setub(){
		
		box = Fixture.criarBox(300, "Box 300", TipoBox.LANCAMENTO);
		save(box);
		
		
		manoel = Fixture.pessoaFisica("10732815665",
				"sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO,box);
		save(cotaManoel);
				
		pdvManoel = Fixture.criarPDVPrincipal("PDV MANOEL", cotaManoel);
		save(pdvManoel);


		roteirizacao = Fixture.criarRoteirizacao(box);
		save(roteirizacao);
		
		roteiro1 = Fixture.criarRoteiro("Roteiro 1", roteirizacao, box, TipoRoteiro.NORMAL);
		roteiro1.setBox(box);
		save(roteiro1);
		
		roteiro2 = Fixture.criarRoteiro("Roteiro 2",roteirizacao, box, TipoRoteiro.NORMAL);
		roteiro2.setBox(box);
		save(roteiro2);
		
		roteiro3 = Fixture.criarRoteiro("Roteiro 3", roteirizacao, box, TipoRoteiro.NORMAL);
		roteiro3.setBox(box);
		save(roteiro3);
			
		
		
		rota1 = Fixture.rota("1", "Rota 1", roteiro1, Arrays.asList(pdvManoel));
		save(rota1);
		
		rota2 = Fixture.rota("2", "Rota 2", roteiro1, Arrays.asList(pdvManoel));
		save(rota2);
		
		rota3 = Fixture.rota("3", "Rota 3", roteiro1, Arrays.asList(pdvManoel));
		save(rota3);
		
		
		
		rota4 = Fixture.rota("4", "Rota 4", roteiro2, Arrays.asList(pdvManoel));
		save(rota4);
		
		rota5 = Fixture.rota("5", "Rota 5", roteiro2, Arrays.asList(pdvManoel));
		save(rota5);
		
		rota6 = Fixture.rota("6", "Rota 6", roteiro2, Arrays.asList(pdvManoel));
		save(rota6);

		
		
		rota7 = Fixture.rota("7", "Rota 7", roteiro3, Arrays.asList(pdvManoel));
		save(rota7);
		
		rota8 = Fixture.rota("8", "Rota 8", roteiro3, Arrays.asList(pdvManoel));
		save(rota8);
		
		rota9 = Fixture.rota("9", "Rota 9", roteiro3, Arrays.asList(pdvManoel));
		save(rota9);
		
	}

	@Test
	public void testeObterListaBoxLancamento() {
		
		List<Box> listaBox = this.roteirizacaoService.obterListaBoxLancamento();
        
		Assert.assertEquals(listaBox.size(), 1);

	}
	
	@Test
	public void testeObterListaRoteiroBoxLancamento() {
		
		List<Roteiro> listaRoteiro = this.roteirizacaoService.obterListaRoteiroPorBox(box.getId());
        
		Assert.assertEquals(listaRoteiro.size(), 3);

	}
	
	@Test
	public void testeObterListaRotaBoxLancamento() {
		
		List<Rota> listaRota = this.roteirizacaoService.obterListaRotaPorRoteiro(roteiro1.getId());
        
		Assert.assertEquals(listaRota.size(), 3);
		
        listaRota = this.roteirizacaoService.obterListaRotaPorRoteiro(roteiro2.getId());
        
		Assert.assertEquals(listaRota.size(), 3);
		
		listaRota = this.roteirizacaoService.obterListaRotaPorRoteiro(roteiro3.getId());
        
		Assert.assertEquals(listaRota.size(), 3);

	}
	

}
