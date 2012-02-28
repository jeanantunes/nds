package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.seguranca.Usuario;


public class RecebimentoFisicoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private RecebimentoFisicoRepositoryImpl recebimentoFisicoRepository;

	@Before
	public void setUp() {
		
		TipoProduto tp = Fixture.tipoProduto("Revista", GrupoProduto.LIVRO, "cmd");
	
		Produto p = Fixture.produto("3", "Revista Monica", "Monica", PeriodicidadeProduto.ANUAL,tp );
		
		ProdutoEdicao pe = Fixture.produtoEdicao(233L,3, 4,  new BigDecimal(0.1), BigDecimal.TEN,BigDecimal.TEN , p);
		
		Usuario user = Fixture.usuarioJoao();
		
		CFOP cfop = Fixture.cfop5102();
		
		PessoaJuridica pj = Fixture.pessoaJuridica("11.111.111", "11.111.111", "ie-", "t@t.com.br");
		
		Fornecedor f = Fixture.fornecedor(pj, SituacaoCadastro.ATIVO,true);
		
		TipoNotaFiscal tnf = Fixture.tipoNotaFiscalRecebimento();
		
		NotaFiscalFornecedor nf = Fixture.notaFiscalFornecedor(cfop, pj, f, tnf, user);
		nf.setNumero("2");
		nf.setSerie("3");
		
		RecebimentoFisico rf = Fixture.recebimentoFisico(nf, user,  Fixture.criarData(3, 3, 1983),  Fixture.criarData(3, 3, 1983), StatusConfirmacao.PENDENTE);
		
		ItemNotaFiscal inf = Fixture.itemNotaFiscal(pe, user, nf, Fixture.criarData(3, 3, 1983));
		
		ItemRecebimentoFisico irf = Fixture.itemRecebimentoFisico(inf, rf, BigDecimal.TEN);
		
		getSession().save(tp);
		getSession().save(p);
		getSession().save(pe);
		getSession().save(user);
		getSession().save(cfop);
		getSession().save(pj);
		getSession().save(f);
		getSession().save(tnf);
		getSession().save(nf);
		getSession().save(rf);
		getSession().save(inf);
		getSession().save(irf);	
		
		System.out.println(pj.getCnpj());
		
	}
		
	@Test
	public void obterItemNotaPorCnpjNota() {
		
		//List<RecebimentoFisicoDTO> listDTO = recebimentoFisicoRepository.obterItemNotaPorCnpjNota("11.111.111", "2", "3");
				
		//Assert.assertEquals(1, listDTO.size());
		
	}
		
}
