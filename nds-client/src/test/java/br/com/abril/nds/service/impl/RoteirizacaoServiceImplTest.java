package br.com.abril.nds.service.impl;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.BoxRoteirizacaoDTO;
import br.com.abril.nds.dto.PdvRoteirizacaoDTO;
import br.com.abril.nds.dto.PdvRoteirizacaoDTO.OrigemEndereco;
import br.com.abril.nds.dto.RotaRoteirizacaoDTO;
import br.com.abril.nds.dto.RoteirizacaoDTO;
import br.com.abril.nds.dto.RoteiroRoteirizacaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
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
import br.com.abril.nds.model.cadastro.pdv.RotaPDV;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.util.TipoMensagem;

public class RoteirizacaoServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;

	private Cota cotaManoel;
	private Cota cotaJose;
	private Cota cotaMaria;
	private Cota cotaMariana;
	private Cota cotaOrlando;
	private Cota cotaLuis;
	private Cota cotaJoao;
	private Cota cotaGuilherme;
	private Cota cotaMurilo;
	private Cota cotaJoana;
	private Cota cotaCarlos;
	private Cota cotaJoaquim;
	private Cota cotaBenedito;
	private Cota cotaClaudio;
	
	private PDV pdvManoel;
	private PDV pdvJose;
	private PDV pdvMaria;
	private PDV pdvMariana;
	private PDV pdvOrlando;
	private PDV pdvLuis;
	private PDV pdvJoao;
	private PDV pdvGuilherme;
	private PDV pdvMurilo;
	private PDV pdvJoana;
	private PDV pdvCarlos;
	private PDV pdvJoaquim;
	private PDV pdvBenedito;
	private PDV pdvClaudio;
	
	private Box box300;
	private Box box400;
	private Box box500;
	
	private Roteirizacao roteirizacao;
	
	private Roteiro roteiro1;
	private Roteiro roteiro2;
	private Roteiro roteiro3;
	
	private Rota rota1Roteiro1;
	private Rota rota2Roteiro1;
	private Rota rota3roteiro1;
	private Rota rota1Roteiro2;
	private Rota rota2Roteiro2;
	private Rota rota3Roteiro2;
	private Rota rota1Roteiro3;
	
	@Before
	public void setup(){
		box300 = Fixture.criarBox(300, "Box 300", TipoBox.LANCAMENTO);
		box400 = Fixture.criarBox(400, "Box 400", TipoBox.LANCAMENTO);
		box500 = Fixture.criarBox(500, "Box 500", TipoBox.LANCAMENTO);
        save(box300, box400, box500);
		
		PessoaFisica manoel = Fixture.pessoaFisica("10732815665", "sys.discover@gmail.com", "Manoel da Silva");
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box300);
		save(manoel, cotaManoel);
		
		PessoaFisica jose = Fixture.pessoaFisica("12345678901", "sys.discover@gmail.com", "Jose da Silva");
	    cotaJose = Fixture.cota(1234, jose, SituacaoCadastro.ATIVO, box300);
	    save(jose, cotaJose);
	      
	    PessoaFisica maria = Fixture.pessoaFisica("12345678902", "sys.discover@gmail.com", "Maria da Silva");
	    cotaMaria = Fixture.cota(12345, maria, SituacaoCadastro.ATIVO, box400);
	    save(maria, cotaMaria);

	    PessoaFisica mariana = Fixture.pessoaFisica("99933355533", "sys.discover@gmail.com", "Mariana");
	    cotaMariana = Fixture.cota(32345, mariana, SituacaoCadastro.ATIVO, box400);
	    save(mariana, cotaMariana);
	    
	    PessoaFisica orlando = Fixture.pessoaFisica("99933355544", "sys.discover@gmail.com", "Orlando");
	    cotaOrlando = Fixture.cota(4444, orlando, SituacaoCadastro.ATIVO, box500);
	    save(orlando, cotaOrlando);

        PessoaFisica luis  = Fixture.pessoaFisica("12345678903",  "sys.discover@gmail.com", "Luis Silva");
        cotaLuis = Fixture.cota(888, luis, SituacaoCadastro.ATIVO, box500);
        save(luis, cotaLuis);

        PessoaFisica joao = Fixture.pessoaFisica("12345678904", "sys.discover@gmail.com", "João da Silva");
        cotaJoao = Fixture.cota(9999, joao, SituacaoCadastro.ATIVO, box500);
        save(joao, cotaJoao);
        
        PessoaFisica guilherme = Fixture.pessoaFisica("99933355511", "sys.discover@gmail.com", "Guilherme de Morais Leandro");
        cotaGuilherme = Fixture.cota(333, guilherme, SituacaoCadastro.ATIVO, box500);
        save(guilherme, cotaGuilherme);
        
        PessoaFisica murilo = Fixture.pessoaFisica("99933355522", "sys.discover@gmail.com", "Murilo");
        cotaMurilo = Fixture.cota(22345, murilo, SituacaoCadastro.ATIVO, box500);
        save(murilo, cotaMurilo);
        
        PessoaFisica joana = Fixture.pessoaFisica("99933355583", "sys.discover@gmail.com", "Joana");
        cotaJoana = Fixture.cota(567, joana, SituacaoCadastro.ATIVO, box500);
        save(joana, cotaJoana);
        
        PessoaFisica carlos = Fixture.pessoaFisica("77733355533", "sys.discover@gmail.com", "Carlos");
        cotaCarlos = Fixture.cota(998, carlos, SituacaoCadastro.ATIVO, box500);
        save(carlos, cotaCarlos);
        
        PessoaFisica joaquim = Fixture.pessoaFisica("5553355533", "sys.discover@gmail.com", "Joaquim");
        cotaJoaquim = Fixture.cota(994, joaquim, SituacaoCadastro.ATIVO, box500);
        save(joaquim, cotaJoaquim);
        
        PessoaFisica benedito = Fixture.pessoaFisica("7893355533", "sys.discover@gmail.com", "Benedito");
        cotaBenedito = Fixture.cota(894, benedito, SituacaoCadastro.ATIVO, box500);
        save(benedito, cotaBenedito);
        
        PessoaFisica claudio = Fixture.pessoaFisica("87989733555", "sys.discover@gmail.com", "Claudio");
        cotaClaudio = Fixture.cota(794, claudio, SituacaoCadastro.ATIVO, box500);
        save(claudio, cotaClaudio);
		
		pdvManoel = Fixture.criarPDVPrincipal("PDV MANOEL", cotaManoel);
		save(pdvManoel);

		pdvJose = Fixture.criarPDVPrincipal("PDV JOSE", cotaJose);
		save(pdvJose);
		
		pdvMaria = Fixture.criarPDVPrincipal("PDV MARIA", cotaMaria);
        save(pdvMaria);
        
        pdvMariana = Fixture.criarPDVPrincipal("PDV MARIANA", cotaMariana);
        save(pdvMariana);
        
        pdvOrlando = Fixture.criarPDVPrincipal("PDV ORLANDO", cotaOrlando);
        save(pdvOrlando);
        
        pdvLuis = Fixture.criarPDVPrincipal("PDV LUIS", cotaLuis);
        save(pdvLuis);
        
        pdvJoao = Fixture.criarPDVPrincipal("PDV JOAO", cotaJoao);
        save(pdvJoao);
        
        pdvGuilherme = Fixture.criarPDVPrincipal("PDV GUILHERME", cotaGuilherme);
        save(pdvGuilherme);
        
        pdvMurilo = Fixture.criarPDVPrincipal("PDV MURILO", cotaMurilo);
        save(pdvMurilo);
        
        pdvJoana = Fixture.criarPDVPrincipal("PDV JOANA", cotaJoana);
        save(pdvJoana);
        
        pdvCarlos = Fixture.criarPDVPrincipal("PDV CARLOS", cotaCarlos);
        save(pdvCarlos);
        
        pdvJoaquim = Fixture.criarPDVPrincipal("PDV CARLOS", cotaJoaquim);
        save(pdvJoaquim);
        
        pdvBenedito = Fixture.criarPDVPrincipal("PDV BENEDITO", cotaBenedito);
        save(pdvBenedito);
        
        pdvClaudio = Fixture.criarPDVPrincipal("PDV CLAUDIO", cotaClaudio);
        save(pdvClaudio);
	
		roteirizacao = Fixture.criarRoteirizacao(box300);
		save(roteirizacao);
		
		roteiro1 = Fixture.criarRoteiro("Roteiro 1", roteirizacao, TipoRoteiro.NORMAL);
		roteiro2 = Fixture.criarRoteiro("Roteiro 2", roteirizacao, TipoRoteiro.NORMAL);
		roteiro3 = Fixture.criarRoteiro("Roteiro 3", roteirizacao, TipoRoteiro.NORMAL);
		save(roteiro1, roteiro2, roteiro3);
			
		rota1Roteiro1 = Fixture.rota("1", "Rota 1 - Roteiro 1", roteiro1);
		rota1Roteiro1.addPDV(pdvManoel, 1);
		rota1Roteiro1.addPDV(pdvJose, 2);
		save(rota1Roteiro1);
		
		rota2Roteiro1 = Fixture.rota("2", "Rota 2", roteiro1);
		rota2Roteiro1.addPDV(pdvMaria, 1);
	    rota2Roteiro1.addPDV(pdvMariana, 2);
		save(rota2Roteiro1);
		
		rota3roteiro1 = Fixture.rota("3", "Rota 3", roteiro1);
  	    rota3roteiro1.addPDV(pdvOrlando, 1);
		save(rota3roteiro1);

		rota1Roteiro2 = Fixture.rota("4", "Rota 4", roteiro2);
	    rota1Roteiro2.addPDV(pdvLuis, 1);
	    rota1Roteiro2.addPDV(pdvJoao, 2);
		save(rota1Roteiro2);
		
		rota2Roteiro2 = Fixture.rota("5", "Rota 5", roteiro2);
	    rota2Roteiro2.addPDV(pdvGuilherme, 1);
		save(rota2Roteiro2);
		
		rota3Roteiro2 = Fixture.rota("6", "Rota 6", roteiro2);
	    rota3Roteiro2.addPDV(pdvMurilo, 1);
		save(rota3Roteiro2);

		rota1Roteiro3 = Fixture.rota("7", "Rota 7", roteiro3);
	    rota1Roteiro3.addPDV(pdvJoana, 1);
		save(rota1Roteiro3);
				
		save(roteirizacao);
		
	}

	@Test
	public void testeObterListaBoxLancamento() {
		
		List<Box> listaBox = this.roteirizacaoService.obterListaBoxLancamento(null);
        
		Assert.assertEquals(3, listaBox.size());

	}
	
	@Test
	public void testeObterListaRoteiroBoxLancamento() {
		
		List<Roteiro> listaRoteiro = this.roteirizacaoService.obterListaRoteiroPorBox(box300.getId(), null);
        
		Assert.assertEquals(listaRoteiro.size(), 3);

	}
	
	@Test
	public void testeObterListaRotaBoxLancamento() {
		
		List<Rota> listaRota = this.roteirizacaoService.obterListaRotaPorRoteiro(roteiro1.getId(), null);
        
		Assert.assertEquals(listaRota.size(), 3);
		
        listaRota = this.roteirizacaoService.obterListaRotaPorRoteiro(roteiro2.getId(), null);
        
		Assert.assertEquals(listaRota.size(), 3);
		
		listaRota = this.roteirizacaoService.obterListaRotaPorRoteiro(roteiro3.getId(), null);
        
		Assert.assertEquals(1, listaRota.size());

	}
	
	@Test
	public void testeObterDadosRoteirizacao(){
		RoteirizacaoDTO roteirizacaoDTO = this.roteirizacaoService.obterRoteirizacaoPorId(roteirizacao.getId());
		
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
		Assert.assertEquals(1,rotas3.size());
		
		
		
		RotaRoteirizacaoDTO rotaDTO = rotas3.get(0);
        Assert.assertEquals("Rota 7", rotaDTO.getNome());
        
		List<PdvRoteirizacaoDTO> pdvs = rotaDTO.getPdvs();
        Assert.assertEquals(1, pdvs.size());
	
	}
	
	@Test
	public void confirmaNovaRoteirizacao() {
	    Box box = Fixture.criarBox(900, "Box 900", TipoBox.LANCAMENTO);
        save(box);
        BoxRoteirizacaoDTO boxDTO = new BoxRoteirizacaoDTO(box.getId(), box.getNome());
        
	    RoteirizacaoDTO roteirizacaoDTO = RoteirizacaoDTO.novaRoteirizacao(Arrays.asList(boxDTO));
	    roteirizacaoDTO.setBox(boxDTO);
	    
	    RoteiroRoteirizacaoDTO roteiroDTO1 = new RoteiroRoteirizacaoDTO(null, 1, "Roteiro 1");
	    RoteiroRoteirizacaoDTO roteiroDTO2 = new RoteiroRoteirizacaoDTO(null, 2, "Roteiro 1");
        roteirizacaoDTO.addRoteiro(roteiroDTO1);
        roteirizacaoDTO.addRoteiro(roteiroDTO2);
        
        RotaRoteirizacaoDTO rotaDTO1 = new RotaRoteirizacaoDTO(null, 1, "Rota 1 - Roteiro 1");
        PdvRoteirizacaoDTO pdvRotaDTO1 = new PdvRoteirizacaoDTO(pdvCarlos.getId(), pdvCarlos
                .getNome(), OrigemEndereco.PDV, null, pdvCarlos.getCota().getNumeroCota(),
                pdvCarlos.getCota().getPessoa().getNome(), 1);
        rotaDTO1.addPdv(pdvRotaDTO1);
        roteiroDTO1.addRota(rotaDTO1);
        
        RotaRoteirizacaoDTO rotaDTO2 = new RotaRoteirizacaoDTO(null, 2, "Rota 2 - Roteiro 1");
        PdvRoteirizacaoDTO pdvRotaDTO2 = new PdvRoteirizacaoDTO(pdvJoaquim.getId(), pdvJoaquim
                .getNome(), OrigemEndereco.PDV, null, pdvJoaquim.getCota().getNumeroCota(),
                pdvJoaquim.getCota().getPessoa().getNome(), 1);
        rotaDTO2.addPdv(pdvRotaDTO2);
        roteiroDTO1.addRota(rotaDTO2);
        
        RotaRoteirizacaoDTO rotaDTO3 = new RotaRoteirizacaoDTO(null, 1, "Rota 1 - Roteiro 2");
        PdvRoteirizacaoDTO pdvRotaDTO3 = new PdvRoteirizacaoDTO(pdvBenedito.getId(), pdvBenedito
                .getNome(), OrigemEndereco.PDV, null, pdvBenedito.getCota().getNumeroCota(),
                pdvBenedito.getCota().getPessoa().getNome(), 1);
        rotaDTO3.addPdv(pdvRotaDTO3);
        roteiroDTO2.addRota(rotaDTO3);
        
        RotaRoteirizacaoDTO rotaDTO4 = new RotaRoteirizacaoDTO(null, 2, "Rota 2 - Roteiro 2");
        PdvRoteirizacaoDTO pdvRotaDTO4 = new PdvRoteirizacaoDTO(pdvClaudio.getId(), pdvClaudio
                .getNome(), OrigemEndereco.PDV, null, pdvClaudio.getCota().getNumeroCota(),
                pdvClaudio.getCota().getPessoa().getNome(), 1);
        rotaDTO4.addPdv(pdvRotaDTO4);
        roteiroDTO2.addRota(rotaDTO4);
        
        Roteirizacao roteirizacao = roteirizacaoService.confirmarRoteirizacao(roteirizacaoDTO);
        flushClear();
        
        roteirizacao = (Roteirizacao) getSession().get(Roteirizacao.class, roteirizacao.getId());        
        
        Assert.assertNotNull(roteirizacao);
        Assert.assertEquals(box.getId(), roteirizacao.getBox().getId());
        List<Roteiro> roteiros = roteirizacao.getRoteiros();
        Assert.assertEquals(2, roteiros.size());
      
        Roteiro roteiro1 = roteiros.get(0);
        Assert.assertNotNull(roteiro1);
        Assert.assertEquals(roteiroDTO1.getNome(), roteiro1.getDescricaoRoteiro());
        Assert.assertEquals(roteiroDTO1.getOrdem(), roteiro1.getOrdem());
        Assert.assertEquals(TipoRoteiro.NORMAL, roteiro1.getTipoRoteiro());
       
        List<Rota> rotasRoteiro1 = roteiro1.getRotas();
        Assert.assertEquals(2, rotasRoteiro1.size());
        
        Rota rota1Roteiro1 = rotasRoteiro1.get(0);
        Assert.assertNotNull(rota1Roteiro1);
        Assert.assertEquals(rotaDTO1.getNome(), rota1Roteiro1.getDescricaoRota());
        Assert.assertEquals(rotaDTO1.getOrdem(), rota1Roteiro1.getOrdem());
        Assert.assertEquals(pdvRotaDTO1.getId(), rota1Roteiro1.getRotaPDVs().get(0).getPdv().getId());
        Assert.assertEquals(pdvRotaDTO1.getOrdem(), rota1Roteiro1.getRotaPDVs().get(0).getOrdem());
        
        Rota rota2Roteiro1 = rotasRoteiro1.get(1);
        Assert.assertNotNull(rota2Roteiro1);
        Assert.assertEquals(rotaDTO2.getNome(), rota2Roteiro1.getDescricaoRota());
        Assert.assertEquals(rotaDTO2.getOrdem(), rota2Roteiro1.getOrdem());
        Assert.assertEquals(pdvRotaDTO2.getId(), rota2Roteiro1.getRotaPDVs().get(0).getPdv().getId());
        Assert.assertEquals(pdvRotaDTO2.getOrdem(), rota2Roteiro1.getRotaPDVs().get(0).getOrdem());
        
        Roteiro roteiro2 = roteiros.get(1);
        Assert.assertNotNull(roteiro2);
        Assert.assertEquals(roteiroDTO2.getNome(), roteiro2.getDescricaoRoteiro());
        Assert.assertEquals(roteiroDTO2.getOrdem(), roteiro2.getOrdem());
        Assert.assertEquals(TipoRoteiro.NORMAL, roteiro2.getTipoRoteiro());
        
        List<Rota> rotasRoteiro2 = roteiro2.getRotas();
        Assert.assertEquals(2, rotasRoteiro2.size());
        
        Rota rota1Roteiro2 = rotasRoteiro2.get(0);
        Assert.assertNotNull(rota1Roteiro2);
        Assert.assertEquals(rotaDTO3.getNome(), rota1Roteiro2.getDescricaoRota());
        Assert.assertEquals(rotaDTO3.getOrdem(), rota1Roteiro2.getOrdem());
        Assert.assertEquals(pdvRotaDTO3.getId(), rota1Roteiro2.getRotaPDVs().get(0).getPdv().getId());
        Assert.assertEquals(pdvRotaDTO3.getOrdem(), rota1Roteiro2.getRotaPDVs().get(0).getOrdem());
        
        Rota rota2Roteiro2 = rotasRoteiro2.get(1);
        Assert.assertNotNull(rota2Roteiro2);
        Assert.assertEquals(rotaDTO4.getNome(), rota2Roteiro2.getDescricaoRota());
        Assert.assertEquals(rotaDTO4.getOrdem(), rota2Roteiro2.getOrdem());
        Assert.assertEquals(pdvRotaDTO4.getId(), rota2Roteiro2.getRotaPDVs().get(0).getPdv().getId());
        Assert.assertEquals(pdvRotaDTO4.getOrdem(), rota2Roteiro2.getRotaPDVs().get(0).getOrdem());
	}
	
	@Test
    public void confirmaNovaRoteirizacaoBoxEspecial() {
        BoxRoteirizacaoDTO boxDTO = new BoxRoteirizacaoDTO(Box.ESPECIAL.getId(), Box.ESPECIAL.getNome());
        
        RoteirizacaoDTO roteirizacaoDTO = RoteirizacaoDTO.novaRoteirizacao(Arrays.asList(boxDTO));
        roteirizacaoDTO.setBox(boxDTO);
        
        RoteiroRoteirizacaoDTO roteiroDTO1 = new RoteiroRoteirizacaoDTO(null, 1, "Roteiro 1");
        roteirizacaoDTO.addRoteiro(roteiroDTO1);
        
        RotaRoteirizacaoDTO rotaDTO1 = new RotaRoteirizacaoDTO(null, 1, "Rota 1 - Roteiro 1");
        PdvRoteirizacaoDTO pdvRotaDTO1 = new PdvRoteirizacaoDTO(pdvCarlos.getId(), pdvCarlos
                .getNome(), OrigemEndereco.PDV, null, pdvCarlos.getCota().getNumeroCota(),
                pdvCarlos.getCota().getPessoa().getNome(), 1);
        rotaDTO1.addPdv(pdvRotaDTO1);
        roteiroDTO1.addRota(rotaDTO1);
        
        RotaRoteirizacaoDTO rotaDTO2 = new RotaRoteirizacaoDTO(null, 2, "Rota 2 - Roteiro 1");
        PdvRoteirizacaoDTO pdvRotaDTO2 = new PdvRoteirizacaoDTO(pdvJoaquim.getId(), pdvJoaquim
                .getNome(), OrigemEndereco.PDV, null, pdvJoaquim.getCota().getNumeroCota(),
                pdvJoaquim.getCota().getPessoa().getNome(), 1);
        rotaDTO2.addPdv(pdvRotaDTO2);
        roteiroDTO1.addRota(rotaDTO2);
        
        Roteirizacao roteirizacao = roteirizacaoService.confirmarRoteirizacao(roteirizacaoDTO);
        flushClear();
        
        roteirizacao = (Roteirizacao) getSession().get(Roteirizacao.class, roteirizacao.getId());        
        
        Assert.assertNotNull(roteirizacao);
        Assert.assertNull(roteirizacao.getBox());
        List<Roteiro> roteiros = roteirizacao.getRoteiros();
        Assert.assertEquals(1, roteiros.size());
      
        Roteiro roteiro1 = roteiros.get(0);
        Assert.assertNotNull(roteiro1);
        Assert.assertEquals(roteiroDTO1.getNome(), roteiro1.getDescricaoRoteiro());
        Assert.assertEquals(roteiroDTO1.getOrdem(), roteiro1.getOrdem());
        Assert.assertEquals(TipoRoteiro.ESPECIAL, roteiro1.getTipoRoteiro());
       
        List<Rota> rotasRoteiro1 = roteiro1.getRotas();
        Assert.assertEquals(2, rotasRoteiro1.size());
        
        Rota rota1Roteiro1 = rotasRoteiro1.get(0);
        Assert.assertNotNull(rota1Roteiro1);
        Assert.assertEquals(rotaDTO1.getNome(), rota1Roteiro1.getDescricaoRota());
        Assert.assertEquals(rotaDTO1.getOrdem(), rota1Roteiro1.getOrdem());
        Assert.assertEquals(pdvRotaDTO1.getId(), rota1Roteiro1.getRotaPDVs().get(0).getPdv().getId());
        Assert.assertEquals(pdvRotaDTO1.getOrdem(), rota1Roteiro1.getRotaPDVs().get(0).getOrdem());
        
        Rota rota2Roteiro1 = rotasRoteiro1.get(1);
        Assert.assertNotNull(rota2Roteiro1);
        Assert.assertEquals(rotaDTO2.getNome(), rota2Roteiro1.getDescricaoRota());
        Assert.assertEquals(rotaDTO2.getOrdem(), rota2Roteiro1.getOrdem());
        Assert.assertEquals(pdvRotaDTO2.getId(), rota2Roteiro1.getRotaPDVs().get(0).getPdv().getId());
        Assert.assertEquals(pdvRotaDTO2.getOrdem(), rota2Roteiro1.getRotaPDVs().get(0).getOrdem());
    }
	
	@Test
    public void confirmaNovaRoteirizacaoBoxJaUtilizadoRoteirizacao() {
	    BoxRoteirizacaoDTO boxDTO = new BoxRoteirizacaoDTO(box300.getId(), box300.getNome());
        
        RoteirizacaoDTO roteirizacaoDTO = RoteirizacaoDTO.novaRoteirizacao(Arrays.asList(boxDTO));
        roteirizacaoDTO.setBox(boxDTO);
        
        RoteiroRoteirizacaoDTO roteiroDTO1 = new RoteiroRoteirizacaoDTO(Long.valueOf(-1), 1, "Roteiro 1");
        roteirizacaoDTO.addRoteiro(roteiroDTO1);
        
        RotaRoteirizacaoDTO rotaDTO1 = new RotaRoteirizacaoDTO(null, 1, "Rota 1 - Roteiro 1");
        PdvRoteirizacaoDTO pdvRotaDTO1 = new PdvRoteirizacaoDTO(pdvCarlos.getId(), pdvCarlos
                .getNome(), OrigemEndereco.PDV, null, pdvCarlos.getCota().getNumeroCota(),
                pdvCarlos.getCota().getPessoa().getNome(), 1);
        rotaDTO1.addPdv(pdvRotaDTO1);
        roteiroDTO1.addRota(rotaDTO1);
        
        RotaRoteirizacaoDTO rotaDTO2 = new RotaRoteirizacaoDTO(null, 2, "Rota 2 - Roteiro 1");
        PdvRoteirizacaoDTO pdvRotaDTO2 = new PdvRoteirizacaoDTO(pdvJoaquim.getId(), pdvJoaquim
                .getNome(), OrigemEndereco.PDV, null, pdvJoaquim.getCota().getNumeroCota(),
                pdvJoaquim.getCota().getPessoa().getNome(), 1);
        rotaDTO2.addPdv(pdvRotaDTO2);
        roteiroDTO1.addRota(rotaDTO2);
        
        try {
            roteirizacaoService.confirmarRoteirizacao(roteirizacaoDTO);
        } catch (ValidacaoException ex) {
            Assert.assertEquals(TipoMensagem.ERROR, ex.getValidacao().getTipoMensagem());
            Assert.assertTrue(ex.getValidacao().getListaMensagens().contains("Box já está associado a uma Roteirização!"));
        }
    }
	
	@Test
    public void confirmaNovaRoteirizacaoPDVUtilizadoRoteirizacaoComBox() {
        BoxRoteirizacaoDTO boxDTO = new BoxRoteirizacaoDTO(box500.getId(), box500.getNome());
        
        RoteirizacaoDTO roteirizacaoDTO = RoteirizacaoDTO.novaRoteirizacao(Arrays.asList(boxDTO));
        roteirizacaoDTO.setBox(boxDTO);
        
        RoteiroRoteirizacaoDTO roteiroDTO1 = new RoteiroRoteirizacaoDTO(Long.valueOf(-1), 1, "Roteiro 1");
        roteirizacaoDTO.addRoteiro(roteiroDTO1);
        
        RotaRoteirizacaoDTO rotaDTO1 = new RotaRoteirizacaoDTO(null, 1, "Rota 1 - Roteiro 1");
        PdvRoteirizacaoDTO pdvRotaDTO1 = new PdvRoteirizacaoDTO(pdvManoel.getId(), pdvManoel
                .getNome(), OrigemEndereco.PDV, null, pdvManoel.getCota().getNumeroCota(),
                pdvManoel.getCota().getPessoa().getNome(), 1);
        rotaDTO1.addPdv(pdvRotaDTO1);
        roteiroDTO1.addRota(rotaDTO1);
       
        try {
            roteirizacaoService.confirmarRoteirizacao(roteirizacaoDTO);
            Assert.fail("Deveria ter falhado com " +  ValidacaoException.class.getName());
        } catch (ValidacaoException ex) {
            Assert.assertEquals(TipoMensagem.ERROR, ex.getValidacao().getTipoMensagem());
            String mensagem = ex.getValidacao().getListaMensagens().get(0);
            Assert.assertEquals(mensagem, "O PDV ["+ pdvManoel.getNome() +"] já pertence a uma roteirização associada a um Box!");
        }
    }
	
	@Test
    public void confirmaNovaRoteirizacaoEspecialPDVUtilizadoRoteirizacaoComBox() {
        RoteirizacaoDTO roteirizacaoDTO = RoteirizacaoDTO.novaRoteirizacao(Arrays.asList(BoxRoteirizacaoDTO.ESPECIAL));
        roteirizacaoDTO.setBox(BoxRoteirizacaoDTO.ESPECIAL);
        
        RoteiroRoteirizacaoDTO roteiroDTO1 = new RoteiroRoteirizacaoDTO(Long.valueOf(-1), 1, "Roteiro 1");
        roteirizacaoDTO.addRoteiro(roteiroDTO1);
        
        RotaRoteirizacaoDTO rotaDTO1 = new RotaRoteirizacaoDTO(null, 1, "Rota 1 - Roteiro 1");
        PdvRoteirizacaoDTO pdvRotaDTO1 = new PdvRoteirizacaoDTO(pdvManoel.getId(), pdvManoel
                .getNome(), OrigemEndereco.PDV, null, pdvManoel.getCota().getNumeroCota(),
                pdvManoel.getCota().getPessoa().getNome(), 1);
        rotaDTO1.addPdv(pdvRotaDTO1);
        roteiroDTO1.addRota(rotaDTO1);

        Roteirizacao roteirizacao = roteirizacaoService.confirmarRoteirizacao(roteirizacaoDTO);
        flushClear();
        
        Roteirizacao confirmado = (Roteirizacao) getSession().get(Roteirizacao.class, roteirizacao.getId()); 
        Assert.assertNull(confirmado.getBox());
        
        List<Roteiro> roteiros = confirmado.getRoteiros();
        Assert.assertEquals(1, roteiros.size());
        Roteiro roteiro = roteiros.get(0);
        Assert.assertEquals(roteiroDTO1.getOrdem(), roteiro.getOrdem());
        Assert.assertEquals(roteiroDTO1.getNome(), roteiro.getDescricaoRoteiro());
        
        List<Rota> rotas = roteiro.getRotas();
        Assert.assertEquals(1, rotas.size());
        Rota rota = rotas.get(0);
        Assert.assertEquals(rotaDTO1.getOrdem(), rota.getOrdem());
        Assert.assertEquals(rotaDTO1.getNome(), rota.getDescricaoRota());
        
        List<RotaPDV> pdvs = rota.getRotaPDVs();
        Assert.assertEquals(1, pdvs.size());
        RotaPDV pdv = pdvs.get(0);
        Assert.assertEquals(pdvRotaDTO1.getOrdem(), pdv.getOrdem());
        Assert.assertEquals(pdvManoel, pdv.getPdv());
    }
	
	@Test
    public void confirmaNovaRoteirizacaoMesmosPDVsRoteirosDiferentes() {
	    BoxRoteirizacaoDTO boxDTO = new BoxRoteirizacaoDTO(box500.getId(), box500.getNome());
        
        RoteirizacaoDTO roteirizacaoDTO = RoteirizacaoDTO.novaRoteirizacao(Arrays.asList(boxDTO));
        roteirizacaoDTO.setBox(boxDTO);
        
        
        RoteiroRoteirizacaoDTO roteiroDTO1 = new RoteiroRoteirizacaoDTO(Long.valueOf(-1), 1, "Roteiro 1");
        roteirizacaoDTO.addRoteiro(roteiroDTO1);
        
        RoteiroRoteirizacaoDTO roteiroDTO2 = new RoteiroRoteirizacaoDTO(Long.valueOf(-2), 2, "Roteiro 2");
        roteirizacaoDTO.addRoteiro(roteiroDTO2);
        
        RotaRoteirizacaoDTO rotaDTO1 = new RotaRoteirizacaoDTO(null, 1, "Rota 1 - Roteiro 1");
        roteiroDTO1.addRota(rotaDTO1);
        
        RotaRoteirizacaoDTO rotaDTO2 = new RotaRoteirizacaoDTO(null, 1, "Rota 1 - Roteiro 2");
        roteiroDTO2.addRota(rotaDTO2);
        
        
        PdvRoteirizacaoDTO pdvRotaDTO = new PdvRoteirizacaoDTO(pdvCarlos.getId(), pdvCarlos
                .getNome(), OrigemEndereco.PDV, null, pdvCarlos.getCota().getNumeroCota(),
                pdvCarlos.getCota().getPessoa().getNome(), 1);
        rotaDTO1.addPdv(pdvRotaDTO);
        
        rotaDTO2.addPdv(pdvRotaDTO);

        Roteirizacao roteirizacao = roteirizacaoService.confirmarRoteirizacao(roteirizacaoDTO);
        flushClear();
        
        Roteirizacao confirmado = (Roteirizacao) getSession().get(Roteirizacao.class, roteirizacao.getId()); 
        Assert.assertEquals(box500.getId(), confirmado.getBox().getId());
        
        List<Roteiro> roteiros = confirmado.getRoteiros();
        Assert.assertEquals(2, roteiros.size());
        Roteiro roteiro1 = roteiros.get(0);
        Assert.assertEquals(roteiroDTO1.getOrdem(), roteiro1.getOrdem());
        Assert.assertEquals(roteiroDTO1.getNome(), roteiro1.getDescricaoRoteiro());
        
        Roteiro roteiro2 = roteiros.get(1);
        Assert.assertEquals(roteiroDTO2.getOrdem(), roteiro2.getOrdem());
        Assert.assertEquals(roteiroDTO2.getNome(), roteiro2.getDescricaoRoteiro());
        
        List<Rota> rotasRoteiro1 = roteiro1.getRotas();
        Assert.assertEquals(1, rotasRoteiro1.size());

        Rota rotaRoteiro1 = rotasRoteiro1.get(0);
        Assert.assertEquals(rotaDTO1.getOrdem(), rotaRoteiro1.getOrdem());
        Assert.assertEquals(rotaDTO1.getNome(), rotaRoteiro1.getDescricaoRota());
        
        List<RotaPDV> pdvsRotaRoteiro1 = rotaRoteiro1.getRotaPDVs();
        Assert.assertEquals(1, pdvsRotaRoteiro1.size());
        RotaPDV pdvRotaRoteiro1 = pdvsRotaRoteiro1.get(0);
        Assert.assertEquals(pdvRotaDTO.getOrdem(), pdvRotaRoteiro1.getOrdem());
        Assert.assertEquals(pdvCarlos, pdvRotaRoteiro1.getPdv());
        
        List<Rota> rotasRoteiro2 = roteiro2.getRotas();
        Assert.assertEquals(1, rotasRoteiro2.size());

        Rota rotaRoteiro2 = rotasRoteiro2.get(0);
        Assert.assertEquals(rotaDTO2.getOrdem(), rotaRoteiro2.getOrdem());
        Assert.assertEquals(rotaDTO2.getNome(), rotaRoteiro2.getDescricaoRota());
        
        List<RotaPDV> pdvsRotaRoteiro2 = rotaRoteiro2.getRotaPDVs();
        Assert.assertEquals(1, pdvsRotaRoteiro2.size());
        RotaPDV pdvRotaRoteiro2 = pdvsRotaRoteiro2.get(0);
        Assert.assertEquals(pdvRotaDTO.getOrdem(), pdvRotaRoteiro2.getOrdem());
        Assert.assertEquals(pdvCarlos, pdvRotaRoteiro2.getPdv());
    }
	
	
	@Test
    public void confirmaNovaRoteirizacaoMesmosPDVsRotasDiferentes() {
        BoxRoteirizacaoDTO boxDTO = new BoxRoteirizacaoDTO(box500.getId(), box500.getNome());
        
        RoteirizacaoDTO roteirizacaoDTO = RoteirizacaoDTO.novaRoteirizacao(Arrays.asList(boxDTO));
        roteirizacaoDTO.setBox(boxDTO);
        
        RoteiroRoteirizacaoDTO roteiroDTO1 = new RoteiroRoteirizacaoDTO(Long.valueOf(-1), 1, "Roteiro 1");
        roteirizacaoDTO.addRoteiro(roteiroDTO1);
        
        RotaRoteirizacaoDTO rotaDTO1 = new RotaRoteirizacaoDTO(Long.valueOf(-1), 1, "Rota 1 - Roteiro 1");
        roteiroDTO1.addRota(rotaDTO1);
        
        RotaRoteirizacaoDTO rotaDTO2 = new RotaRoteirizacaoDTO(Long.valueOf(-2), 2, "Rota 2 - Roteiro 1");
        roteiroDTO1.addRota(rotaDTO2);
        
        
        PdvRoteirizacaoDTO pdvRotaDTO = new PdvRoteirizacaoDTO(pdvCarlos.getId(), pdvCarlos
                .getNome(), OrigemEndereco.PDV, null, pdvCarlos.getCota().getNumeroCota(),
                pdvCarlos.getCota().getPessoa().getNome(), 1);
        rotaDTO1.addPdv(pdvRotaDTO);
        
        rotaDTO2.addPdv(pdvRotaDTO);

        Roteirizacao roteirizacao = roteirizacaoService.confirmarRoteirizacao(roteirizacaoDTO);
        flushClear();
        
        Roteirizacao confirmado = (Roteirizacao) getSession().get(Roteirizacao.class, roteirizacao.getId()); 
        Assert.assertEquals(box500.getId(), confirmado.getBox().getId());
        
        List<Roteiro> roteiros = confirmado.getRoteiros();
        Assert.assertEquals(1, roteiros.size());
       
        Roteiro roteiro1 = roteiros.get(0);
        Assert.assertEquals(roteiroDTO1.getOrdem(), roteiro1.getOrdem());
        Assert.assertEquals(roteiroDTO1.getNome(), roteiro1.getDescricaoRoteiro());
        
        List<Rota> rotasRoteiro1 = roteiro1.getRotas();
        Assert.assertEquals(2, rotasRoteiro1.size());

        Rota rota1Roteiro1 = rotasRoteiro1.get(0);
        Assert.assertEquals(rotaDTO1.getOrdem(), rota1Roteiro1.getOrdem());
        Assert.assertEquals(rotaDTO1.getNome(), rota1Roteiro1.getDescricaoRota());
        
        List<RotaPDV> pdvsRota1Roteiro1 = rota1Roteiro1.getRotaPDVs();
        Assert.assertEquals(1, pdvsRota1Roteiro1.size());
        RotaPDV pdvRotaRoteiro1 = pdvsRota1Roteiro1.get(0);
        Assert.assertEquals(pdvRotaDTO.getOrdem(), pdvRotaRoteiro1.getOrdem());
        Assert.assertEquals(pdvCarlos, pdvRotaRoteiro1.getPdv());
        
        Rota rota2Roteiro1 = rotasRoteiro1.get(1);
        Assert.assertEquals(rotaDTO2.getOrdem(), rota2Roteiro1.getOrdem());
        Assert.assertEquals(rotaDTO2.getNome(), rota2Roteiro1.getDescricaoRota());
        
        List<RotaPDV> pdvsRota2Roteiro1 = rota2Roteiro1.getRotaPDVs();
        Assert.assertEquals(1, pdvsRota2Roteiro1.size());
        RotaPDV pdvRota2Roteiro1 = pdvsRota2Roteiro1.get(0);
        Assert.assertEquals(pdvRotaDTO.getOrdem(), pdvRota2Roteiro1.getOrdem());
        Assert.assertEquals(pdvCarlos, pdvRota2Roteiro1.getPdv());
    }
	
	@Test
    public void confirmaNovaRoteirizacaoOrdemRoteiroDuplicada() {
        BoxRoteirizacaoDTO boxDTO = new BoxRoteirizacaoDTO(box500.getId(), box500.getNome());
        
        RoteirizacaoDTO roteirizacaoDTO = RoteirizacaoDTO.novaRoteirizacao(Arrays.asList(boxDTO));
        roteirizacaoDTO.setBox(boxDTO);
        
        RoteiroRoteirizacaoDTO roteiroDTO1 = new RoteiroRoteirizacaoDTO(Long.valueOf(-1), 1, "Roteiro 1");
        roteirizacaoDTO.addRoteiro(roteiroDTO1);
        
        RoteiroRoteirizacaoDTO roteiroDTO2 = new RoteiroRoteirizacaoDTO(Long.valueOf(-2), 1, "Roteiro 2");
        roteirizacaoDTO.addRoteiro(roteiroDTO2);
        
        RotaRoteirizacaoDTO rotaDTO1 = new RotaRoteirizacaoDTO(Long.valueOf(-1), 1, "Rota 1 - Roteiro 1");
        roteiroDTO1.addRota(rotaDTO1);
        
        RotaRoteirizacaoDTO rotaDTO2 = new RotaRoteirizacaoDTO(Long.valueOf(-2), 1, "Rota 2 - Roteiro 1");
        roteiroDTO2.addRota(rotaDTO2);
        
        PdvRoteirizacaoDTO pdvRotaDTO = new PdvRoteirizacaoDTO(pdvCarlos.getId(), pdvCarlos
                .getNome(), OrigemEndereco.PDV, null, pdvCarlos.getCota().getNumeroCota(),
                pdvCarlos.getCota().getPessoa().getNome(), 1);
        rotaDTO1.addPdv(pdvRotaDTO);
        
        rotaDTO2.addPdv(pdvRotaDTO);
        
        try {
            roteirizacaoService.confirmarRoteirizacao(roteirizacaoDTO);
            Assert.fail("Deveria ter falhado com " +  IllegalArgumentException.class.getName());
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Ordem [1] para o Roteiro já utilizada!", ex.getMessage());
        }
    }
	
	@Test
    public void confirmaNovaRoteirizacaoOrdemRotaDuplicada() {
        BoxRoteirizacaoDTO boxDTO = new BoxRoteirizacaoDTO(box500.getId(), box500.getNome());
        
        RoteirizacaoDTO roteirizacaoDTO = RoteirizacaoDTO.novaRoteirizacao(Arrays.asList(boxDTO));
        roteirizacaoDTO.setBox(boxDTO);
        
        RoteiroRoteirizacaoDTO roteiroDTO1 = new RoteiroRoteirizacaoDTO(Long.valueOf(-1), 1, "Roteiro 1");
        roteirizacaoDTO.addRoteiro(roteiroDTO1);
        
        RotaRoteirizacaoDTO rotaDTO1 = new RotaRoteirizacaoDTO(Long.valueOf(-1), 1, "Rota 1 - Roteiro 1");
        roteiroDTO1.getTodasRotas().add(rotaDTO1);
        
        RotaRoteirizacaoDTO rotaDTO2 = new RotaRoteirizacaoDTO(Long.valueOf(-2), 1, "Rota 2 - Roteiro 1");
        roteiroDTO1.getTodasRotas().add(rotaDTO2);
        
        PdvRoteirizacaoDTO pdvRotaDTO = new PdvRoteirizacaoDTO(pdvCarlos.getId(), pdvCarlos
                .getNome(), OrigemEndereco.PDV, null, pdvCarlos.getCota().getNumeroCota(),
                pdvCarlos.getCota().getPessoa().getNome(), 1);
        rotaDTO1.addPdv(pdvRotaDTO);
        
        rotaDTO2.addPdv(pdvRotaDTO);
        
        try {
            roteirizacaoService.confirmarRoteirizacao(roteirizacaoDTO);
            Assert.fail("Deveria ter falhado com " +  IllegalArgumentException.class.getName());
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Ordem [1] para a Rota já utilizada!", ex.getMessage());
        }
    }
	
	
	@Test
    public void confirmaNovaRoteirizacaoOrdemPDVDuplicada() {
        BoxRoteirizacaoDTO boxDTO = new BoxRoteirizacaoDTO(box500.getId(), box500.getNome());
        
        RoteirizacaoDTO roteirizacaoDTO = RoteirizacaoDTO.novaRoteirizacao(Arrays.asList(boxDTO));
        roteirizacaoDTO.setBox(boxDTO);
        
        RoteiroRoteirizacaoDTO roteiroDTO1 = new RoteiroRoteirizacaoDTO(Long.valueOf(-1), 1, "Roteiro 1");
        roteirizacaoDTO.addRoteiro(roteiroDTO1);
        
        RotaRoteirizacaoDTO rotaDTO1 = new RotaRoteirizacaoDTO(Long.valueOf(-1), 1, "Rota 1 - Roteiro 1");
        roteiroDTO1.addRota(rotaDTO1);
        
        PdvRoteirizacaoDTO pdvRota1DTO = new PdvRoteirizacaoDTO(pdvCarlos.getId(), pdvCarlos
                .getNome(), OrigemEndereco.PDV, null, pdvCarlos.getCota().getNumeroCota(),
                pdvCarlos.getCota().getPessoa().getNome(), 1);
        rotaDTO1.addPdv(pdvRota1DTO);
        
        PdvRoteirizacaoDTO pdvRota2DTO = new PdvRoteirizacaoDTO(pdvJoaquim.getId(), pdvJoaquim
                .getNome(), OrigemEndereco.PDV, null, pdvJoaquim.getCota().getNumeroCota(),
                pdvJoaquim.getCota().getPessoa().getNome(), 1);
        rotaDTO1.addPdv(pdvRota2DTO);

        try {
            roteirizacaoService.confirmarRoteirizacao(roteirizacaoDTO);
            Assert.fail("Deveria ter falhado com " +  IllegalArgumentException.class.getName());
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Ordem [1] para o PDV já utilizada!", ex.getMessage());
        }
    }
	
	@Test
    public void confirmaNovaRoteirizacaoOrdemPDVInvalida() {
        BoxRoteirizacaoDTO boxDTO = new BoxRoteirizacaoDTO(box500.getId(), box500.getNome());
        
        RoteirizacaoDTO roteirizacaoDTO = RoteirizacaoDTO.novaRoteirizacao(Arrays.asList(boxDTO));
        roteirizacaoDTO.setBox(boxDTO);
        
        RoteiroRoteirizacaoDTO roteiroDTO1 = new RoteiroRoteirizacaoDTO(Long.valueOf(-1), 1, "Roteiro 1");
        roteirizacaoDTO.addRoteiro(roteiroDTO1);
        
        RotaRoteirizacaoDTO rotaDTO1 = new RotaRoteirizacaoDTO(Long.valueOf(-1), 1, "Rota 1 - Roteiro 1");
        roteiroDTO1.addRota(rotaDTO1);
        
        PdvRoteirizacaoDTO pdvRota1DTO = new PdvRoteirizacaoDTO(pdvCarlos.getId(), pdvCarlos
                .getNome(), OrigemEndereco.PDV, null, pdvCarlos.getCota().getNumeroCota(),
                pdvCarlos.getCota().getPessoa().getNome(), 1);
        rotaDTO1.addPdv(pdvRota1DTO);
        
        PdvRoteirizacaoDTO pdvRota2DTO = new PdvRoteirizacaoDTO(pdvJoaquim.getId(), pdvJoaquim
                .getNome(), OrigemEndereco.PDV, null, pdvJoaquim.getCota().getNumeroCota(),
                pdvJoaquim.getCota().getPessoa().getNome(), -1);
        rotaDTO1.addPdv(pdvRota2DTO);

        try {
            roteirizacaoService.confirmarRoteirizacao(roteirizacaoDTO);
            Assert.fail("Deveria ter falhado com " +  IllegalArgumentException.class.getName());
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Ordem [-1] para o PDV não é válida!", ex.getMessage());
        }
    }
	
	@Test
    public void confirmaRoteirizacaoExistenteOrdemPDVDuplicada() {
        RoteirizacaoDTO roteirizacaoDTO = RoteirizacaoDTO.toDTO(roteirizacao, Arrays.asList(box300), false);
        RoteiroRoteirizacaoDTO roteiro1DTO = roteirizacaoDTO.getRoteiro(roteiro1.getId());
        RotaRoteirizacaoDTO rota1DTO = roteiro1DTO.getRota(rota1Roteiro1.getId());
        PdvRoteirizacaoDTO pdvDTO = rota1DTO.getPdv(pdvJose.getId());
        pdvDTO.setOrdem(1);
        
        try {
            roteirizacaoService.confirmarRoteirizacao(roteirizacaoDTO);
            Assert.fail("Deveria ter falhado com " +  IllegalArgumentException.class.getName());
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Ordem [1] para o PDV já utilizada!", ex.getMessage());
        }
    
    }
	
	@Test
    public void confirmaRoteirizacaoExistente() {
        RoteirizacaoDTO roteirizacaoDTO = RoteirizacaoDTO.toDTO(roteirizacao, Arrays.asList(box300), false);
        roteirizacaoDTO.removerRoteiro(roteiro3.getId());
        roteirizacaoDTO.addRoteiroExclusao(roteiro3.getId());
        
        RoteiroRoteirizacaoDTO novoRoteiro = new RoteiroRoteirizacaoDTO(Long.valueOf(-1), 5, "Novo Roteiro");
        roteirizacaoDTO.addRoteiro(novoRoteiro);
        
        RotaRoteirizacaoDTO novaRotaNovoRoteiro = new RotaRoteirizacaoDTO(Long.valueOf(-1), 10, "Nova Rota - Novo Roteiro");
        novoRoteiro.addRota(novaRotaNovoRoteiro);
        novaRotaNovoRoteiro.addPdv(new PdvRoteirizacaoDTO(pdvCarlos.getId(), pdvCarlos.getNome(), OrigemEndereco.PDV,
                null, pdvCarlos.getCota().getNumeroCota(), pdvCarlos
                        .getCota().getPessoa().getNome(), 1));
        
        RoteiroRoteirizacaoDTO roteiro1DTO = roteirizacaoDTO.getRoteiro(roteiro1.getId());
        roteiro1DTO.removerRota(rota1Roteiro1.getId());
        roteiro1DTO.adicionarRotaExclusao(rota1Roteiro1.getId());
       
        RotaRoteirizacaoDTO rota2Roteiro1DTO = roteiro1DTO.getRota(rota2Roteiro1.getId());
        rota2Roteiro1DTO.removerPdv(pdvMariana.getId());
        
        RotaRoteirizacaoDTO novaRotaRoteiro1DTO = new RotaRoteirizacaoDTO(Long.valueOf(-1), 10, "Nova Rota");
        roteiro1DTO.addRota(novaRotaRoteiro1DTO);
        novaRotaRoteiro1DTO.addPdv(new PdvRoteirizacaoDTO(pdvJoaquim.getId(), pdvJoaquim.getNome(), OrigemEndereco.PDV,
                null, pdvJoaquim.getCota().getNumeroCota(), pdvJoaquim
                        .getCota().getPessoa().getNome(), 1));
                
        RoteiroRoteirizacaoDTO roteiro2DTO = roteirizacaoDTO.getRoteiro(roteiro2.getId());
        RotaRoteirizacaoDTO  rota1Roteiro2DTO = roteiro2DTO.getRota(rota1Roteiro2.getId());
        PdvRoteirizacaoDTO pdvDTO = rota1Roteiro2DTO.getPdv(pdvJoao.getId());
        pdvDTO.setOrdem(10);
        
        RotaRoteirizacaoDTO rota3Roteiro2DTO = roteiro2DTO.getRota(rota3Roteiro2.getId());
        PdvRoteirizacaoDTO pdvManoel2DTO = new PdvRoteirizacaoDTO(pdvBenedito.getId(), pdvBenedito.getNome(), OrigemEndereco.PDV,
                null, pdvBenedito.getCota().getNumeroCota(), pdvBenedito
                        .getCota().getPessoa().getNome(), 3);
        rota3Roteiro2DTO.addPdv(pdvManoel2DTO);
        
        roteirizacaoService.confirmarRoteirizacao(roteirizacaoDTO);
        flushClear();
        
        Roteirizacao confirmado = (Roteirizacao) getSession().get(Roteirizacao.class, roteirizacao.getId()); 
        List<Roteiro> roteiros = confirmado.getRoteiros();
        Assert.assertEquals(3, roteiros.size());
        
        Roteiro roteiro1 = roteiros.get(0);
        Assert.assertEquals(roteiro1.getDescricaoRoteiro(), roteiro1.getDescricaoRoteiro());
        List<Rota> rotas = roteiro1.getRotas();
        Assert.assertEquals(3, rotas.size());
      
        Rota rota1 = rotas.get(0);
        Assert.assertEquals(rota2Roteiro1.getDescricaoRota(), rota1.getDescricaoRota());
        List<RotaPDV> rotaPDVs = rota1.getRotaPDVs();
        Assert.assertEquals(1, rotaPDVs.size());
  
        RotaPDV rotaPDV1 = rotaPDVs.get(0);
        Assert.assertEquals(Integer.valueOf(1), rotaPDV1.getOrdem());
        Assert.assertEquals(pdvMaria, rotaPDV1.getPdv());
        
        Rota rota2 = rotas.get(1);
        Assert.assertEquals(rota3roteiro1.getDescricaoRota(), rota2.getDescricaoRota());
        
        Rota rota3 = rotas.get(2);
        Assert.assertEquals(novaRotaRoteiro1DTO.getNome(), rota3.getDescricaoRota());
        
        Roteiro roteiro2 = roteiros.get(1);
        Assert.assertEquals(roteiro2.getDescricaoRoteiro(), roteiro2.getDescricaoRoteiro());
        List<Rota> rotasRoteiro2 = roteiro2.getRotas();
        Assert.assertEquals(3, rotasRoteiro2.size());
        
        Rota rota1Roteiro2 = rotasRoteiro2.get(0);
        Assert.assertEquals(rota1Roteiro2.getDescricaoRota(), rota1Roteiro2.getDescricaoRota());
        List<RotaPDV> rota1Roteiro2PDVs = rota1Roteiro2.getRotaPDVs();
        Assert.assertEquals(2, rota1Roteiro2PDVs.size());
        
        RotaPDV rota1Roteiro2PDV1 = rota1Roteiro2PDVs.get(0);
        Assert.assertEquals(Integer.valueOf(1), rota1Roteiro2PDV1.getOrdem());
        Assert.assertEquals(pdvLuis, rota1Roteiro2PDV1.getPdv());
        
        RotaPDV rota1Roteiro2PDV2 = rota1Roteiro2PDVs.get(1);
        Assert.assertEquals(pdvDTO.getOrdem(), rota1Roteiro2PDV2.getOrdem());
        Assert.assertEquals(pdvJoao, rota1Roteiro2PDV2.getPdv());
        
        Rota rota2Roteiro2 = rotasRoteiro2.get(1);
        Assert.assertEquals(rota2Roteiro2.getDescricaoRota(), rota2Roteiro2.getDescricaoRota());
        
        Rota rota3Roteiro2 = rotasRoteiro2.get(2);
        Assert.assertEquals(rota3Roteiro2.getDescricaoRota(), rota3Roteiro2.getDescricaoRota());
        
        List<RotaPDV> rota3Roteiro2PDVs = rota3Roteiro2.getRotaPDVs();
        Assert.assertEquals(2, rota1Roteiro2PDVs.size());
        
        RotaPDV rota3Roteiro2PDV1 = rota3Roteiro2PDVs.get(0);
        Assert.assertEquals(Integer.valueOf(1), rota3Roteiro2PDV1.getOrdem());
        Assert.assertEquals(pdvMurilo, rota3Roteiro2PDV1.getPdv());
        
        RotaPDV rota3Roteiro2PDV2 = rota3Roteiro2PDVs.get(1);
        Assert.assertEquals(pdvManoel2DTO.getOrdem(), rota3Roteiro2PDV2.getOrdem());
        Assert.assertEquals(pdvBenedito, rota3Roteiro2PDV2.getPdv());
        
        Roteiro roteiro3 = roteiros.get(2);
        Assert.assertEquals(novoRoteiro.getNome(), roteiro3.getDescricaoRoteiro());
        List<Rota> rotasRoteiro3 = roteiro3.getRotas();
        Assert.assertEquals(1, rotasRoteiro3.size());
        
        Rota rota1Roteiro3 = rotasRoteiro3.get(0);
        Assert.assertEquals(novaRotaNovoRoteiro.getNome(), rota1Roteiro3.getDescricaoRota());
    }

}
