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
import br.com.abril.nds.model.cadastro.pdv.RotaPDV;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.util.TipoMensagem;

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
		pdvManoel1 = Fixture.criarPDVPrincipal("PDV MANOEL 1", cotaManoel);
		save(pdvManoel1);

		pdvManoel2 = Fixture.criarPDVPrincipal("PDV MANOEL 2", cotaManoel);
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
		
		
		roteiro1 = Fixture.criarRoteiro("Roteiro 1", roteirizacao, TipoRoteiro.NORMAL);
		save(roteiro1);
		
		roteiro2 = Fixture.criarRoteiro("Roteiro 2",roteirizacao, TipoRoteiro.NORMAL);
		save(roteiro2);
		
		roteiro3 = Fixture.criarRoteiro("Roteiro 3", roteirizacao, TipoRoteiro.NORMAL);
		save(roteiro3);
			
		
		rota1 = Fixture.rota("1", "Rota 1", roteiro1);
		rota1.addPDV(pdvManoel1, 1);
		rota1.addPDV(pdvManoel2, 2);
		save(rota1);
		
		rota2 = Fixture.rota("2", "Rota 2", roteiro1);
		rota2.addPDV(pdvManoel1, 1);
	    rota2.addPDV(pdvManoel2, 2);
		save(rota2);
		
		rota3 = Fixture.rota("3", "Rota 3", roteiro1);
  	    rota3.addPDV(pdvManoel1, 1);
		save(rota3);

		rota4 = Fixture.rota("4", "Rota 4", roteiro2);
	    rota4.addPDV(pdvManoel1, 1);
	    rota4.addPDV(pdvManoel2, 2);
		save(rota4);
		
		rota5 = Fixture.rota("5", "Rota 5", roteiro2);
	    rota5.addPDV(pdvManoel1, 1);
		save(rota5);
		
		rota6 = Fixture.rota("6", "Rota 6", roteiro2);
	    rota6.addPDV(pdvManoel1, 1);
		save(rota6);

		rota7 = Fixture.rota("7", "Rota 7", roteiro3);
	    rota7.addPDV(pdvManoel2, 1);
		save(rota7);
		
		rota8 = Fixture.rota("8", "Rota 8", roteiro3);
	    rota8.addPDV(pdvManoel2, 1);
		save(rota8);
		
		rota9 = Fixture.rota("9", "Rota 9", roteiro3);
	    rota9.addPDV(pdvManoel1, 1);
		save(rota9);
		
		save(roteirizacao);
		
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
		Assert.assertEquals(3,rotas3.size());
		
		
		
		RotaRoteirizacaoDTO rotaDTO = rotas3.get(0);
        Assert.assertEquals("Rota 7", rotaDTO.getNome());
        
		List<PdvRoteirizacaoDTO> pdvs = rotaDTO.getPdvs();
        Assert.assertEquals(1, pdvs.size());
	
	}
	
	@Test
	public void testeObterPdvsDisponiveis(){
		
		List<PdvRoteirizacaoDTO> listaPdvDTO = this.roteirizacaoService.obterPdvsDisponiveis(null,null,null,null,null);
		
		Assert.assertTrue(listaPdvDTO!=null);
		
		Assert.assertTrue(listaPdvDTO.size()>0);
		
		Assert.assertEquals(listaPdvDTO.size(), 1);
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
        PdvRoteirizacaoDTO pdvRotaDTO1 = new PdvRoteirizacaoDTO(pdvManoel1.getId(), pdvManoel1
                .getNome(), OrigemEndereco.PDV, null, pdvManoel1.getCota().getNumeroCota(),
                pdvManoel1.getCota().getPessoa().getNome(), 1);
        rotaDTO1.addPdv(pdvRotaDTO1);
        roteiroDTO1.addRota(rotaDTO1);
        
        RotaRoteirizacaoDTO rotaDTO2 = new RotaRoteirizacaoDTO(null, 2, "Rota 2 - Roteiro 1");
        PdvRoteirizacaoDTO pdvRotaDTO2 = new PdvRoteirizacaoDTO(pdvManoel2.getId(), pdvManoel2
                .getNome(), OrigemEndereco.PDV, null, pdvManoel2.getCota().getNumeroCota(),
                pdvManoel2.getCota().getPessoa().getNome(), 1);
        rotaDTO2.addPdv(pdvRotaDTO2);
        roteiroDTO1.addRota(rotaDTO2);
        
        RotaRoteirizacaoDTO rotaDTO3 = new RotaRoteirizacaoDTO(null, 1, "Rota 1 - Roteiro 2");
        PdvRoteirizacaoDTO pdvRotaDTO3 = new PdvRoteirizacaoDTO(pdvManoel1.getId(), pdvManoel1
                .getNome(), OrigemEndereco.PDV, null, pdvManoel1.getCota().getNumeroCota(),
                pdvManoel1.getCota().getPessoa().getNome(), 1);
        rotaDTO3.addPdv(pdvRotaDTO3);
        roteiroDTO2.addRota(rotaDTO3);
        
        RotaRoteirizacaoDTO rotaDTO4 = new RotaRoteirizacaoDTO(null, 2, "Rota 2 - Roteiro 2");
        PdvRoteirizacaoDTO pdvRotaDTO4 = new PdvRoteirizacaoDTO(pdvManoel2.getId(), pdvManoel2
                .getNome(), OrigemEndereco.PDV, null, pdvManoel2.getCota().getNumeroCota(),
                pdvManoel2.getCota().getPessoa().getNome(), 1);
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
        PdvRoteirizacaoDTO pdvRotaDTO1 = new PdvRoteirizacaoDTO(pdvManoel1.getId(), pdvManoel1
                .getNome(), OrigemEndereco.PDV, null, pdvManoel1.getCota().getNumeroCota(),
                pdvManoel1.getCota().getPessoa().getNome(), 1);
        rotaDTO1.addPdv(pdvRotaDTO1);
        roteiroDTO1.addRota(rotaDTO1);
        
        RotaRoteirizacaoDTO rotaDTO2 = new RotaRoteirizacaoDTO(null, 2, "Rota 2 - Roteiro 1");
        PdvRoteirizacaoDTO pdvRotaDTO2 = new PdvRoteirizacaoDTO(pdvManoel2.getId(), pdvManoel2
                .getNome(), OrigemEndereco.PDV, null, pdvManoel2.getCota().getNumeroCota(),
                pdvManoel2.getCota().getPessoa().getNome(), 1);
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
	    BoxRoteirizacaoDTO boxDTO = new BoxRoteirizacaoDTO(box1.getId(), box1.getNome());
        
        RoteirizacaoDTO roteirizacaoDTO = RoteirizacaoDTO.novaRoteirizacao(Arrays.asList(boxDTO));
        roteirizacaoDTO.setBox(boxDTO);
        
        RoteiroRoteirizacaoDTO roteiroDTO1 = new RoteiroRoteirizacaoDTO(null, 1, "Roteiro 1");
        roteirizacaoDTO.addRoteiro(roteiroDTO1);
        
        RotaRoteirizacaoDTO rotaDTO1 = new RotaRoteirizacaoDTO(null, 1, "Rota 1 - Roteiro 1");
        PdvRoteirizacaoDTO pdvRotaDTO1 = new PdvRoteirizacaoDTO(pdvManoel1.getId(), pdvManoel1
                .getNome(), OrigemEndereco.PDV, null, pdvManoel1.getCota().getNumeroCota(),
                pdvManoel1.getCota().getPessoa().getNome(), 1);
        rotaDTO1.addPdv(pdvRotaDTO1);
        roteiroDTO1.addRota(rotaDTO1);
        
        RotaRoteirizacaoDTO rotaDTO2 = new RotaRoteirizacaoDTO(null, 2, "Rota 2 - Roteiro 1");
        PdvRoteirizacaoDTO pdvRotaDTO2 = new PdvRoteirizacaoDTO(pdvManoel2.getId(), pdvManoel2
                .getNome(), OrigemEndereco.PDV, null, pdvManoel2.getCota().getNumeroCota(),
                pdvManoel2.getCota().getPessoa().getNome(), 1);
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
    public void confirmaRoteirizacaoExistente() {
        RoteirizacaoDTO roteirizacaoDTO = RoteirizacaoDTO.toDTO(RoteirizacaoServiceImplTest.roteirizacao, Arrays.asList(box1));
        roteirizacaoDTO.addRoteiroExclusao(RoteirizacaoServiceImplTest.roteiro3.getId());
        
        RoteiroRoteirizacaoDTO novoRoteiro = new RoteiroRoteirizacaoDTO(Long.valueOf(-1), 5, "Novo Roteiro");
        roteirizacaoDTO.addRoteiro(novoRoteiro);
        
        RoteiroRoteirizacaoDTO roteiro1DTO = roteirizacaoDTO.getRoteiro(RoteirizacaoServiceImplTest.roteiro1.getId());
        roteiro1DTO.removerRota(RoteirizacaoServiceImplTest.rota1.getId());
        roteiro1DTO.adicionarRotaExclusao(RoteirizacaoServiceImplTest.rota1.getId());
       
        RotaRoteirizacaoDTO rota2DTO = roteiro1DTO.getRota(rota2.getId());
        //TODO: Refatorar
        rota2DTO.getPdvs().remove(1);
        rota2DTO.getPdvsExclusao().add(pdvManoel2.getId());
        
        RotaRoteirizacaoDTO novaRota = new RotaRoteirizacaoDTO(Long.valueOf(-1), 10, "Nova Rota");
        roteiro1DTO.addRota(novaRota);
        
        RoteiroRoteirizacaoDTO roteiro2DTO = roteirizacaoDTO.getRoteiro(RoteirizacaoServiceImplTest.roteiro2.getId());
        RotaRoteirizacaoDTO  rota4DTO = roteiro2DTO.getRota(rota4.getId());
        PdvRoteirizacaoDTO pdvDTO = rota4DTO.getPdv(pdvManoel2.getId());
        pdvDTO.setOrdem(10);
        
        RotaRoteirizacaoDTO rota6DTO = roteiro2DTO.getRota(rota6.getId());
        PdvRoteirizacaoDTO pdvManoel2DTO = new PdvRoteirizacaoDTO(
                pdvManoel2.getId(), pdvManoel2.getNome(), OrigemEndereco.PDV,
                null, pdvManoel2.getCota().getNumeroCota(), pdvManoel2
                        .getCota().getPessoa().getNome(), 3);
        rota6DTO.addPdv(pdvManoel2DTO);
        
        
        roteirizacaoService.confirmarRoteirizacao(roteirizacaoDTO);
        flushClear();
        
        Roteirizacao confirmado = (Roteirizacao) getSession().get(Roteirizacao.class, RoteirizacaoServiceImplTest.roteirizacao.getId()); 
        List<Roteiro> roteiros = confirmado.getRoteiros();
        Assert.assertEquals(3, roteiros.size());
        
        Roteiro roteiro1 = roteiros.get(0);
        Assert.assertEquals(RoteirizacaoServiceImplTest.roteiro1.getDescricaoRoteiro(), roteiro1.getDescricaoRoteiro());
        List<Rota> rotas = roteiro1.getRotas();
        Assert.assertEquals(3, rotas.size());
      
        Rota rota1 = rotas.get(0);
        Assert.assertEquals(RoteirizacaoServiceImplTest.rota2.getDescricaoRota(), rota1.getDescricaoRota());
        List<RotaPDV> rotaPDVs = rota1.getRotaPDVs();
        Assert.assertEquals(1, rotaPDVs.size());
  
        RotaPDV rotaPDV1 = rotaPDVs.get(0);
        Assert.assertEquals(Integer.valueOf(1), rotaPDV1.getOrdem());
        Assert.assertEquals(pdvManoel1, rotaPDV1.getPdv());
        
        Rota rota2 = rotas.get(1);
        Assert.assertEquals(RoteirizacaoServiceImplTest.rota3.getDescricaoRota(), rota2.getDescricaoRota());
        
        Rota rota3 = rotas.get(2);
        Assert.assertEquals(novaRota.getNome(), rota3.getDescricaoRota());
        
        Roteiro roteiro2 = roteiros.get(1);
        Assert.assertEquals(RoteirizacaoServiceImplTest.roteiro2.getDescricaoRoteiro(), roteiro2.getDescricaoRoteiro());
        List<Rota> rotasRoteiro2 = roteiro2.getRotas();
        Assert.assertEquals(3, rotasRoteiro2.size());
        
        Rota rota1Roteiro2 = rotasRoteiro2.get(0);
        Assert.assertEquals(rota4.getDescricaoRota(), rota1Roteiro2.getDescricaoRota());
        List<RotaPDV> rota1Roteiro2PDVs = rota1Roteiro2.getRotaPDVs();
        Assert.assertEquals(2, rota1Roteiro2PDVs.size());
        
        RotaPDV rota1Roteiro2PDV1 = rota1Roteiro2PDVs.get(0);
        Assert.assertEquals(Integer.valueOf(1), rota1Roteiro2PDV1.getOrdem());
        Assert.assertEquals(pdvManoel1, rota1Roteiro2PDV1.getPdv());
        
        RotaPDV rota1Roteiro2PDV2 = rota1Roteiro2PDVs.get(1);
        Assert.assertEquals(pdvDTO.getOrdem(), rota1Roteiro2PDV2.getOrdem());
        Assert.assertEquals(pdvManoel2, rota1Roteiro2PDV2.getPdv());
        
        Rota rota2Roteiro2 = rotasRoteiro2.get(1);
        Assert.assertEquals(rota5.getDescricaoRota(), rota2Roteiro2.getDescricaoRota());
        
        Rota rota3Roteiro2 = rotasRoteiro2.get(2);
        Assert.assertEquals(rota6.getDescricaoRota(), rota3Roteiro2.getDescricaoRota());
        
        List<RotaPDV> rota3Roteiro2PDVs = rota3Roteiro2.getRotaPDVs();
        Assert.assertEquals(2, rota1Roteiro2PDVs.size());
        
        RotaPDV rota3Roteiro2PDV1 = rota3Roteiro2PDVs.get(0);
        Assert.assertEquals(Integer.valueOf(1), rota3Roteiro2PDV1.getOrdem());
        Assert.assertEquals(pdvManoel1, rota3Roteiro2PDV1.getPdv());
        
        RotaPDV rota3Roteiro2PDV2 = rota3Roteiro2PDVs.get(1);
        Assert.assertEquals(pdvManoel2DTO.getOrdem(), rota3Roteiro2PDV2.getOrdem());
        Assert.assertEquals(pdvManoel2, rota3Roteiro2PDV2.getPdv());
        
        
        Roteiro roteiro3 = roteiros.get(2);
        Assert.assertEquals(novoRoteiro.getNome(), roteiro3.getDescricaoRoteiro());
    }



}
