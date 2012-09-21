package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.RoteirizacaoService;

public class RoteirizacaoServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	private static Box box;
	
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

		
		roteiro1 = Fixture.criarRoteiro("Roteiro 1", box, TipoRoteiro.NORMAL);
		roteiro1.setBox(box);
		save(roteiro1);
		
		roteiro2 = Fixture.criarRoteiro("Roteiro 2", box, TipoRoteiro.NORMAL);
		roteiro2.setBox(box);
		save(roteiro2);
		
		roteiro3 = Fixture.criarRoteiro("Roteiro 3", box, TipoRoteiro.NORMAL);
		roteiro3.setBox(box);
		save(roteiro3);
		
		Set<Roteiro> roteiros = new HashSet<Roteiro>();
		roteiros.add(roteiro1);
		roteiros.add(roteiro2);
		roteiros.add(roteiro3);
		box.setRoteiros(roteiros);
		save(box);
		
		
		rota1 = Fixture.rota("1", "Rota 1");
		rota1.setRoteiro(roteiro1);
		save(rota1);
		
		rota2 = Fixture.rota("2", "Rota 2");
		rota2.setRoteiro(roteiro1);
		save(rota2);
		
		rota3 = Fixture.rota("3", "Rota 3");
		rota3.setRoteiro(roteiro1);
		save(rota3);
		
		List<Rota> rotas = new ArrayList<Rota>();
		rotas.add(rota1);
		rotas.add(rota2);
		rotas.add(rota3);
		roteiro1.setRotas(rotas);
		
		
		rota4 = Fixture.rota("4", "Rota 4");
		rota4.setRoteiro(roteiro2);
		save(rota4);
		
		rota5 = Fixture.rota("5", "Rota 5");
		rota5.setRoteiro(roteiro2);
		save(rota5);
		
		rota6 = Fixture.rota("6", "Rota 6");
		rota6.setRoteiro(roteiro2);
		save(rota6);
		
		rotas = new ArrayList<Rota>();
		rotas.add(rota4);
		rotas.add(rota5);
		rotas.add(rota6);
		roteiro2.setRotas(rotas);
		
		
		rota7 = Fixture.rota("7", "Rota 7");
		rota7.setRoteiro(roteiro3);
		save(rota7);
		
		rota8 = Fixture.rota("8", "Rota 8");
		rota8.setRoteiro(roteiro3);
		save(rota8);
		
		rota9 = Fixture.rota("9", "Rota 9");
		rota9.setRoteiro(roteiro3);
		save(rota9);
		
		rotas = new ArrayList<Rota>();
		rotas.add(rota7);
		rotas.add(rota8);
		rotas.add(rota9);
		roteiro3.setRotas(rotas);
		
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
