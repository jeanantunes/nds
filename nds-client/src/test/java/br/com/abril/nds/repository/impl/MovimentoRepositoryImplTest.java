package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.MovimentoAprovacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroControleAprovacaoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.MovimentoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class MovimentoRepositoryImplTest extends AbstractRepositoryImplTest  {
	
	@Autowired
	private MovimentoRepository movimentoRepository;
	
	private TipoMovimentoEstoque tipoMovimentoFaltaEm;
	
	@Before
	public void setup() {
				
		Calendar calendar = Calendar.getInstance();
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		save(produtoVeja);
		
		ProdutoEdicao produtoEdicaoVeja = Fixture.produtoEdicao("1", 1L, 10, 14,
			new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQRSTU", 1L, produtoVeja, null, false);
		save(produtoEdicaoVeja);
		
		tipoMovimentoFaltaEm = Fixture.tipoMovimentoFaltaEm();
		tipoMovimentoFaltaEm = merge(tipoMovimentoFaltaEm);
		
		EstoqueProduto estoqueProdutoVeja = Fixture.estoqueProduto(produtoEdicaoVeja, BigInteger.ZERO);
		save(estoqueProdutoVeja);
		
		MovimentoEstoque movimentoEstoque = 
			Fixture.movimentoEstoque(
				null, produtoEdicaoVeja, tipoMovimentoFaltaEm, usuarioJoao, estoqueProdutoVeja,
				calendar.getTime(), BigInteger.TEN, StatusAprovacao.PENDENTE, "motivo");
		save(movimentoEstoque);
	}
	
	@Test
	public void obterMovimentosAprovacao() {
		
		FiltroControleAprovacaoDTO filro = getFiltro();

		List<MovimentoAprovacaoDTO> listaControleAprovacaoDTO = 
				this.movimentoRepository.obterMovimentosAprovacao(filro);
		
		Assert.assertNotNull(listaControleAprovacaoDTO);
		
		Assert.assertTrue(!listaControleAprovacaoDTO.isEmpty());
		
		Long qtdeTotalRegistros =
				this.movimentoRepository.obterTotalMovimentosAprovacao(filro);
		
		Assert.assertTrue(qtdeTotalRegistros == 1);
	}
	
	private FiltroControleAprovacaoDTO getFiltro() {

		Calendar calendar = Calendar.getInstance();
		
		FiltroControleAprovacaoDTO filtro = new FiltroControleAprovacaoDTO();
		
		filtro.setIdTipoMovimento(tipoMovimentoFaltaEm.getId());
		
		filtro.setDataMovimento(calendar.getTime());
		
		filtro.setPaginacao(new PaginacaoVO(1, 15, "asc"));

		return filtro;
	}
}
