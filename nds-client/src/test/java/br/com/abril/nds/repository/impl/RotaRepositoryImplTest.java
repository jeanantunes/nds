package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
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
import br.com.abril.nds.repository.RotaRepository;

public class RotaRepositoryImplTest extends AbstractRepositoryImplTest  {

	@Autowired
	private RotaRepository rotaRepository;
	
	private Box box1;
	private PDV pdvManoel;
	private Cota cotaManoel;
	private PessoaFisica manoel;
	private Rota rota;
	private Rota rota10;
	
	@Before
	public void setUp() {
		
		manoel = Fixture.pessoaFisica("10732815665",
				"sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO,box1);
		save(cotaManoel);
				
		pdvManoel = Fixture.criarPDVPrincipal("PDV MANOEL", cotaManoel);
		save(pdvManoel);
				
		box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		Roteiro roteiro = Fixture.criarRoteiro("Pinheiros",box1,TipoRoteiro.NORMAL);
		save(roteiro);

		rota = Fixture.rota("005", "Rota 005");
		rota.setRoteiro(roteiro);
		save(rota);
		
		Roteirizacao roteirizacao = Fixture.criarRoteirizacao(pdvManoel, rota,1);
		save(roteirizacao);
		
		roteiro = Fixture.criarRoteiro("Interlagos", box1,TipoRoteiro.NORMAL);
		save(roteiro);
		
		Roteiro roteiroTCD = Fixture.criarRoteiro("TCD",box1,TipoRoteiro.NORMAL);
		save(roteiroTCD);
		
		rota10 = Fixture.rota("001", "Rota 001");
		rota10.setRoteiro(roteiroTCD);
		save(rota10);
		
		Roteirizacao roteirizacao2 = Fixture.criarRoteirizacao(pdvManoel, rota10,2);
		save(roteirizacao2);
	}
	
	@Test
	public void obterRotasPorCota() {
		
		List<Rota> rotas = rotaRepository.obterRotasPorCota(cotaManoel.getNumeroCota());
		
		Assert.assertTrue(rotas.get(0).getId().equals(rota10.getId()));
		Assert.assertTrue(rotas.get(1).getId().equals(rota.getId()));
	}
}
