package br.com.abril.nds.repository.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.abril.nds.dto.BoxRoteirizacaoDTO;
import br.com.abril.nds.dto.ConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.RotaRoteirizacaoDTO;
import br.com.abril.nds.dto.RoteiroRoteirizacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO.OrdenacaoColunaConsulta;
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
import br.com.abril.nds.repository.RoteirizacaoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class RoteirizacaoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private RoteirizacaoRepository roteirizacaoRepository;
	
	private PDV pdvManoel;
	private Cota cotaManoel;
	private PessoaFisica manoel;
	
	private static Roteirizacao roteirizacao;

	private Box box;
	private Box box1;
	private Box box2;
	private Box box3;
	private Box box4;
	private Box box5;
	private Box box6;

	private Roteiro roteiro;
	private Roteiro roteiro1;
	private Roteiro roteiro2;
	private Roteiro roteiro3;

	private Rota rota;
	private Rota rota1;
	private Rota rota2;
	private Rota rota3;
	
	
	@Before
	public void setup() {
		
		box = Fixture.criarBox(300, "Box 300", TipoBox.LANCAMENTO);
		save(box);
		
		
		manoel = Fixture.pessoaFisica("10732815665",
				"sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO,box);
		save(cotaManoel);
				
		pdvManoel = Fixture.criarPDVPrincipal("PDV MANOEL", cotaManoel,1);
		save(pdvManoel);


		roteirizacao = Fixture.criarRoteirizacao(box);
		save(roteirizacao);
		

		box = Fixture.criarBox(0, "BOX00", TipoBox.LANCAMENTO);
		save(box);

		box1 = Fixture.criarBox(1, "BOX01", TipoBox.LANCAMENTO);
		save(box1);

		box2 = Fixture.criarBox(2, "BOX02", TipoBox.LANCAMENTO);
		save(box2);

		box3 = Fixture.criarBox(3, "BOX03", TipoBox.LANCAMENTO);
		save(box3);

		box4 = Fixture.criarBox(4, "BX-04", TipoBox.LANCAMENTO);
		save(box4);

		box5 = Fixture.criarBox(5, "BX-05", TipoBox.LANCAMENTO);
		save(box5);

		box6 = Fixture.criarBox(6, "BX-06", TipoBox.LANCAMENTO);
		save(box6);
		

		roteiro = Fixture.criarRoteiro("RT00", roteirizacao, box, TipoRoteiro.NORMAL);
		save(roteiro);

		roteiro1 = Fixture.criarRoteiro("RT01", roteirizacao, box, TipoRoteiro.NORMAL);
		save(roteiro1);

		roteiro2 = Fixture.criarRoteiro("R02", roteirizacao, box, TipoRoteiro.NORMAL);
		save(roteiro2);

		roteiro3 = Fixture.criarRoteiro("RT03", roteirizacao, box1, TipoRoteiro.NORMAL);
		save(roteiro3);
		
		
		rota = Fixture.rota("0", "ROTA00", roteiro, Arrays.asList(pdvManoel));
		save(rota);
		
		rota1 = Fixture.rota("1", "ROTA01", roteiro, Arrays.asList(pdvManoel));
		save(rota1);
		
		rota2 = Fixture.rota("2", "ROTA02", roteiro, Arrays.asList(pdvManoel));
		save(rota2);
		
		rota3 = Fixture.rota("3", "ROTA03", roteiro1, Arrays.asList(pdvManoel));
		rota3.setRoteiro(roteiro1);
		save(rota3);

	}

	@Test
	public void obterBoxesPorNome() {

		List<BoxRoteirizacaoDTO> lista = roteirizacaoRepository.obterBoxesPorNome("BOX");

		Assert.assertTrue(lista.get(3) != null);

		Assert.assertEquals(lista.size(), 5);
	}

	@Test
  	public void obterRoteirosPorNomeEBoxes() {
  		
  		List<RoteiroRoteirizacaoDTO> lista = roteirizacaoRepository.obterRoteirosPorNomeEBoxes("RT", 
  				Collections.singletonList(box.getId()));
  		
  		Assert.assertTrue(lista.get(1) != null);
  		
  		Assert.assertEquals(lista.size(), 2);
  	}

	@Test
	public void obterRotasPorNomeERoteiros() {

		List<RotaRoteirizacaoDTO> lista = roteirizacaoRepository.obterRotasPorNomeERoteiros("ROTA",
						Collections.singletonList(roteiro.getId()));

		Assert.assertTrue(lista.get(1) != null);

		Assert.assertEquals(lista.size(), 3);
	}
	
	@Test
	public void obterRoteirizacao() {
	    FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
	    filtro.setNumeroCota(123);
	    filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.ROTA);
	    filtro.setPaginacao(new PaginacaoVO(1, 10, "asc"));
	  
	    List<ConsultaRoteirizacaoDTO> resultado = roteirizacaoRepository.buscarRoteirizacao(filtro); 
	    Assert.assertNotNull(resultado);
	    Assert.assertEquals(4, resultado.size());
	    
	    ConsultaRoteirizacaoDTO resultado1 = resultado.get(0);
	    Assert.assertEquals(String.format("%s - %s", rota.getCodigoRota(), rota.getDescricaoRota()), resultado1.getDescricaoRota());
	    
	    ConsultaRoteirizacaoDTO resultado2 = resultado.get(1);
	    Assert.assertEquals(String.format("%s - %s", rota1.getCodigoRota(), rota1.getDescricaoRota()), resultado2.getDescricaoRota());
	    
	    ConsultaRoteirizacaoDTO resultado3 = resultado.get(2);
	    Assert.assertEquals(String.format("%s - %s", rota2.getCodigoRota(), rota2.getDescricaoRota()), resultado3.getDescricaoRota());
	    
	    ConsultaRoteirizacaoDTO resultado4 = resultado.get(3);
	    Assert.assertEquals(String.format("%s - %s", rota3.getCodigoRota(), rota3.getDescricaoRota()), resultado4.getDescricaoRota());
	}

}
