package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;

public class ControleBaixaBancariaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ControleBaixaBancariaRepository controleBaixaBancariaRepository;
	
	@Before
	public void setUp() {
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		
		save(usuarioJoao);
		
		
		ControleBaixaBancaria controleBaixaBancaria =
				Fixture.controleBaixaBancaria(new Date(), StatusControle.CONCLUIDO_SUCESSO, usuarioJoao);
		
		save(controleBaixaBancaria);
	}
	
	@Test
	public void obterProdutoPorCodigo() {
		ControleBaixaBancaria controleBaixaBancaria = 
				controleBaixaBancariaRepository.obterPorData(new Date());
		
		Assert.assertTrue(controleBaixaBancaria != null);
	}
	
}
