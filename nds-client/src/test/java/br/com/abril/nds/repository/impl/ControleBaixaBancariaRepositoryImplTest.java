package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;

public class ControleBaixaBancariaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ControleBaixaBancariaRepository controleBaixaBancariaRepository;
	
	private Banco bancoHSBC;
	
	@Before
	public void setUp() {
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		
		save(usuarioJoao);
		
		bancoHSBC =
			Fixture.banco(10L, true, 1, "1010", 123456L, "1", "1", "Instrucoes HSBC.",
						  "HSBC", "BANCO HSBC S/A", "399", BigDecimal.ONE, BigDecimal.ZERO);
		
		save(bancoHSBC);
		
		ControleBaixaBancaria controleBaixaBancaria =
			Fixture.controleBaixaBancaria(
				new Date(), StatusControle.CONCLUIDO_SUCESSO, usuarioJoao, bancoHSBC);
		
		save(controleBaixaBancaria);
	}
	
	@Test
	public void obterControleBaixaBancaria() {
		
		ControleBaixaBancaria controleBaixaBancaria = 
			this.controleBaixaBancariaRepository.obterControleBaixaBancaria(new Date(), bancoHSBC);
		
		Assert.assertTrue(controleBaixaBancaria != null);
	}
	
	@Test
	public void obterListaControleBaixaBancaria() {
		
		List<ControleBaixaBancaria> listaControleBaixaBancaria = 
			this.controleBaixaBancariaRepository.obterListaControleBaixaBancaria(
				new Date(), StatusControle.CONCLUIDO_SUCESSO);
		
		Assert.assertTrue(listaControleBaixaBancaria != null && !listaControleBaixaBancaria.isEmpty());
	}
	
}
