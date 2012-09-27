package br.com.abril.nds.service.impl;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.BoxRoteirizacaoDTO;
import br.com.abril.nds.dto.PdvRoteirizacaoDTO;
import br.com.abril.nds.dto.RotaRoteirizacaoDTO;
import br.com.abril.nds.dto.RoteirizacaoDTO;
import br.com.abril.nds.dto.RoteiroRoteirizacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.RoteirizacaoService;

public class RoteirizacaoServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	private PDV pdvManoel1;
	
	private PDV pdvManoel2;
	
	private Cota cotaManoel;
	
	private Endereco endereco;
	
	private EnderecoPDV enderecoPdv;
	
	private EnderecoCota enderecoCota;
	
	private PessoaFisica manoel;
	
	private static Box box1;
	
	private static Box box2;
	
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
		
		//BOX
		box1 = Fixture.criarBox(300, "Box 300", TipoBox.LANCAMENTO);
		save(box1);
		
		box2 = Fixture.criarBox(400, "Box 400", TipoBox.LANCAMENTO);
		save(box2);
		
		
		//COTA
		manoel = Fixture.pessoaFisica("10732815665",
				"sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO,box1);
		save(cotaManoel);
		
			
		//PDV
		pdvManoel1 = Fixture.criarPDVPrincipal("PDV MANOEL 1", cotaManoel, 1);
		save(pdvManoel1);

		pdvManoel2 = Fixture.criarPDVPrincipal("PDV MANOEL 2", cotaManoel, 0);
		save(pdvManoel2);
		
		
		//ENDEREÇO
		endereco = Fixture.criarEndereco(TipoEndereco.RESIDENCIAL, "13720-000", "Rua Dos Cerca Lorenço", "10", "Bairro", "Mococa", "SP",1);
		save(endereco);
		
		
		//ENDEREÇO COTA
		enderecoCota = Fixture.enderecoCota(cotaManoel, endereco, true, TipoEndereco.LOCAL_ENTREGA);
		
		
		//ENDEREÇO PDV
		enderecoPdv = Fixture.criarEnderecoPDV(endereco, pdvManoel1, true, TipoEndereco.LOCAL_ENTREGA);
		save(enderecoPdv);
		

		//ROTEIRIZACAO
		roteirizacao = Fixture.criarRoteirizacao(box1);
		save(roteirizacao);
		
		
		roteiro1 = Fixture.criarRoteiro("Roteiro 1", roteirizacao, box1, TipoRoteiro.NORMAL);
		roteiro1.setBox(box1);
		save(roteiro1);
		
		roteiro2 = Fixture.criarRoteiro("Roteiro 2",roteirizacao, box1, TipoRoteiro.NORMAL);
		roteiro2.setBox(box1);
		save(roteiro2);
		
		roteiro3 = Fixture.criarRoteiro("Roteiro 3", roteirizacao, box1, TipoRoteiro.NORMAL);
		roteiro3.setBox(box1);
		save(roteiro3);
			
		
		rota1 = Fixture.rota("1", "Rota 1", roteiro1, Arrays.asList(pdvManoel1,pdvManoel2));
		save(rota1);
		
		rota2 = Fixture.rota("2", "Rota 2", roteiro1, Arrays.asList(pdvManoel1,pdvManoel2));
		save(rota2);
		
		rota3 = Fixture.rota("3", "Rota 3", roteiro1, Arrays.asList(pdvManoel2));
		save(rota3);
		
		
		rota4 = Fixture.rota("4", "Rota 4", roteiro2, Arrays.asList(pdvManoel1));
		save(rota4);
		
		rota5 = Fixture.rota("5", "Rota 5", roteiro2, Arrays.asList(pdvManoel1));
		save(rota5);
		
		rota6 = Fixture.rota("6", "Rota 6", roteiro2, Arrays.asList(pdvManoel1));
		save(rota6);

		
		rota7 = Fixture.rota("7", "Rota 7", roteiro3, Arrays.asList(pdvManoel2));
		save(rota7);
		
		rota8 = Fixture.rota("8", "Rota 8", roteiro3, Arrays.asList(pdvManoel2));
		save(rota8);
		
		rota9 = Fixture.rota("9", "Rota 9", roteiro3, Arrays.asList(pdvManoel2));
		save(rota9);
		
	}

	@Test
	public void testeObterListaBoxLancamento() {
		
		List<Box> listaBox = this.roteirizacaoService.obterListaBoxLancamento(null);
        
		Assert.assertEquals(listaBox.size(), 2);

	}
	
	@Test
	public void testeObterListaRoteiroBoxLancamento() {
		
		List<Roteiro> listaRoteiro = this.roteirizacaoService.obterListaRoteiroPorBox(box1.getId(), null);
        
		Assert.assertEquals(listaRoteiro.size(), 3);

	}
	
	@Test
	public void testeObterListaRotaBoxLancamento() {
		
		List<Rota> listaRota = this.roteirizacaoService.obterListaRotaPorRoteiro(roteiro1.getId(), null);
        
		Assert.assertEquals(listaRota.size(), 3);
		
        listaRota = this.roteirizacaoService.obterListaRotaPorRoteiro(roteiro2.getId(), null);
        
		Assert.assertEquals(listaRota.size(), 3);
		
		listaRota = this.roteirizacaoService.obterListaRotaPorRoteiro(roteiro3.getId(), null);
        
		Assert.assertEquals(listaRota.size(), 3);

	}
	
	@Test
	public void testeObterDadosRoteirizacao(){
		
		FiltroConsultaRoteirizacaoDTO parametros = new FiltroConsultaRoteirizacaoDTO();
		parametros.setIdBox(box1.getId());
		parametros.setNumeroCota(cotaManoel.getNumeroCota());
		parametros.setIdRoteiro(roteiro1.getId());
        parametros.setIdRota(rota1.getId());
		
		RoteirizacaoDTO roteirizacaoDTO = this.roteirizacaoService.obterDadosRoteirizacao(parametros);
		
		BoxRoteirizacaoDTO box = roteirizacaoDTO.getBox();
		
		Assert.assertEquals("Box 300",box.getNome());
		
		
		
		List<RoteiroRoteirizacaoDTO> roteiros = roteirizacaoDTO.getRoteiros();
		Assert.assertEquals(3,roteiros.size());
		
		
		
		RoteiroRoteirizacaoDTO roteiroDTO1 = roteiros.get(0);
		Assert.assertEquals(roteiroDTO1.getNome(), "Roteiro 1");
		
		List<RotaRoteirizacaoDTO> rotas1 = roteiroDTO1.getRotas();
		Assert.assertEquals(3,rotas1.size());
		
		
		
		RoteiroRoteirizacaoDTO roteiroDTO2 = roteiros.get(1);
		Assert.assertEquals(roteiroDTO2.getNome(), "Roteiro 2");
		
		List<RotaRoteirizacaoDTO> rotas2 = roteiroDTO2.getRotas();
		Assert.assertEquals(3,rotas2.size());
		
		
		
		RoteiroRoteirizacaoDTO roteiroDTO3 = roteiros.get(2);
		Assert.assertEquals(roteiroDTO3.getNome(), "Roteiro 3");
		
		List<RotaRoteirizacaoDTO> rotas3 = roteiroDTO3.getRotas();
		Assert.assertEquals(3,rotas3.size());
		
		
		
		RotaRoteirizacaoDTO rotaDTO = rotas3.get(0);
        Assert.assertEquals("Rota 7", rotaDTO.getNome());
        
		List<PdvRoteirizacaoDTO> pdvs = rotaDTO.getPdvs();
        Assert.assertEquals(1, pdvs.size());
	
	}
	
	@Test
	public void testeObterPdvsDisponiveis(){
		
		List<PdvRoteirizacaoDTO> listaPdvDTO = this.roteirizacaoService.obterPdvsDisponiveis();
		
		Assert.assertTrue(listaPdvDTO!=null);
		
		Assert.assertTrue(listaPdvDTO.size()>0);
		
		Assert.assertEquals(listaPdvDTO.size(), 2);
	}
	

}
