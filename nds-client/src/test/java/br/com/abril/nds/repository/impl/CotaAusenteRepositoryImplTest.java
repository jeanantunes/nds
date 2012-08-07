package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO.ColunaOrdenacao;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.movimentacao.CotaAusente;
import br.com.abril.nds.vo.PaginacaoVO;

public class CotaAusenteRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private CotaAusenteRepositoryImpl cotaAusenteRepository;
	
	private Cota cotaManoel;
	
	private PessoaFisica manoel;
	
	private Box box1;
	
	private Date data;
	
	
	@Before
	public void setup() {
		
		manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
		save(manoel);
		
		box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
		
		data =Fixture.criarData(31, 03, 2000);
		
		CotaAusente c =  Fixture.cotaAusente(data, true, cotaManoel);
		save(c);
		
		NCM ncmRevistas = Fixture.ncm(1l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto t = Fixture.tipoProduto("teste", GrupoProduto.JORNAL, ncmRevistas, null, 9L);
		save(t);
		
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		Produto produto = Fixture.produto("12","Algum", "Nome-p", PeriodicidadeProduto.MENSAL, t, 5, 5, BigDecimal.TEN, TributacaoFiscal.TRIBUTADO);
		produto.setEditor(abril);
		save(produto);
		
		ProdutoEdicao pe = Fixture.produtoEdicao("1", 12L, 3, 2, new BigDecimal(12), new BigDecimal(120),new BigDecimal(35), "ABCDEFGHIJKLMNOPQ", 1L, produto, null, false);
		save(pe);
		
	
	}
	
	@Test
	public void buscarCotaAusente(){

		FiltroCotaAusenteDTO filtro = new FiltroCotaAusenteDTO(data, null, cotaManoel.getNumeroCota(),
				new PaginacaoVO(1, 15, "ASC"), 
				ColunaOrdenacao.valueOf("data"));
		
		List<CotaAusenteDTO> listaCotaAusenteDTO =cotaAusenteRepository.obterCotasAusentes(filtro);
		
		Assert.assertTrue(listaCotaAusenteDTO.size() == 1);
		
	}
	
	@Test
	public void obterCountCotasAusentes(){

		FiltroCotaAusenteDTO filtro = new FiltroCotaAusenteDTO(data, null, cotaManoel.getNumeroCota(),
				new PaginacaoVO(0, 15, "ASC"), 
				ColunaOrdenacao.valueOf("data"));		
				
		Long count = cotaAusenteRepository.obterCountCotasAusentes(filtro);
	
		Assert.assertTrue(count.equals(1L));
				
	}

}
