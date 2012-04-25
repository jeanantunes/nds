package br.com.abril.nds.service.impl;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.CotaAusente;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.CotaAusenteService;

public class CotaAusenteServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private CotaAusenteService cotaAusenteCotaService;
	
	CotaAusente cotaAusente;
	
	Usuario user;
	
	
	@Before
	public void setup() {
		Pessoa pessoa = Fixture.pessoaFisica("232.234.12", "w@s.com.br", "Joao");
		save(pessoa);
		
		Box box = Fixture.boxReparte300();
		save(box);
		
		Cota cota = Fixture.cota(23, pessoa, SituacaoCadastro.ATIVO, box);
		save(cota);
		
		cotaAusente = Fixture.cotaAusenteAtivo();
		cotaAusente.setCota(cota);
		save(cotaAusente);
		
		TipoProduto tipoProduto = Fixture.tipoCromo();
		save(tipoProduto);
		
		Produto produto = Fixture.produtoCaras(tipoProduto);
		save(produto);
		
		ProdutoEdicao produtoEdicao = Fixture.produtoEdicao(1L, 0, 2, new BigDecimal(3), new BigDecimal(5), 
				new BigDecimal(3), produto);
		save(produtoEdicao);
		
		TipoMovimentoEstoque tipoMovimentoEstoque = Fixture.tipoMovimentoEnvioJornaleiro();
		save(tipoMovimentoEstoque);
		
		user = Fixture.usuarioJoao();
		save(user);
		
		EstoqueProduto estoqueProduto = Fixture.estoqueProduto(produtoEdicao, new BigDecimal(23));
		save(estoqueProduto);
	
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(produtoEdicao, cota, new BigDecimal(23), new BigDecimal(8));
		save(estoqueProdutoCota);		
		
		
		MovimentoEstoqueCota movimentoEst = Fixture.movimentoEstoqueCota(produtoEdicao, tipoMovimentoEstoque, user, estoqueProdutoCota, new BigDecimal(45), cota, StatusAprovacao.APROVADO, "sem motivo");
		save(movimentoEst);
		
		TipoMovimentoEstoque tm = Fixture.tipoMovimentoReparteCotaAusente();
		save(tm);
		
		TipoMovimentoEstoque tmr = Fixture.tipoMovimentoRestauracaoReparteCotaAusente();
		save(tmr);
		
		
	}
	
	@Test
	public void cotaAusente() {
		cotaAusenteCotaService.obterCotasAusentes(new FiltroCotaAusenteDTO());
	}


}
