package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO;
import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO.SortColumn;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
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
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.vo.PaginacaoVO;

public class LancamentoRepositoryImplExpedicaoTest extends AbstractRepositoryImplTest{


	@Autowired
	private LancamentoRepositoryImpl lancamentoRepository;
	
	@Before
	public void setUp() {
		TipoProduto tipoRevista = Fixture.tipoRevista();
		save(tipoRevista);
		
		CFOP cfop = Fixture.cfop5102();
		save(cfop);
		
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		for(Integer i=1000;i<1050; i++) {
			
			PessoaJuridica juridica = Fixture.pessoaJuridica("PessoaJ"+i,
					"00.000.000/0001-00", "000.000.000.000", "acme@mail.com");
			save(juridica);
			
			Fornecedor fornecedor = Fixture.fornecedor(juridica, SituacaoCadastro.ATIVO, true);
			save(fornecedor);
			
			Produto produto = Fixture.produto("00"+i, "descricao"+i, "nome"+i, PeriodicidadeProduto.ANUAL, tipoRevista);
			produto.addFornecedor(fornecedor);
			save(produto); 
			
			ProdutoEdicao produtoEdicao = Fixture.produtoEdicao(i.longValue(), 50, 40, 
					new BigDecimal(30), new BigDecimal(20), new BigDecimal(10), produto);	
			save(produtoEdicao);
			
			
			TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
			save(tipoNotaFiscal);

			NotaFiscalFornecedor notaFiscalFornecedor = Fixture
					.notaFiscalFornecedor(cfop, juridica, fornecedor, tipoNotaFiscal,
							usuario, new BigDecimal(1),new BigDecimal(1),new BigDecimal(1));
			save(notaFiscalFornecedor);
			
			ItemNotaFiscal itemNotaFiscal= Fixture.itemNotaFiscal(
					produtoEdicao, usuario, notaFiscalFornecedor, 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					new BigDecimal(i));					
			save(itemNotaFiscal);
			
			RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(
				notaFiscalFornecedor, usuario, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
			save(recebimentoFisico);
			
			
			ItemRecebimentoFisico itemFisico = Fixture.itemRecebimentoFisico(
					itemNotaFiscal, recebimentoFisico, new BigDecimal(i));
			save(itemFisico);
			
			Lancamento lancamento = Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicao,
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					new BigDecimal(100), 
					StatusLancamento.RECEBIDO, 
					itemFisico);
			lancamento.setReparte(new BigDecimal(10));
			save(lancamento);
		
			Estudo estudo = new Estudo();
			estudo.setDataLancamento(Fixture.criarData(23, Calendar.FEBRUARY, 2012));
			estudo.setProdutoEdicao(produtoEdicao);
			estudo.setQtdeReparte(new BigDecimal(i));
			save(estudo);
		}
	}


	@Test
	@Ignore
	public void obterLancamentosNaoExpedidos() {
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(0, 100, "ASC",SortColumn.CODIGO_PRODUTO.getProperty());
		
		List<LancamentoNaoExpedidoDTO> lancamentos = lancamentoRepository.obterLancamentosNaoExpedidos(
				paginacaoVO, 
				Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
				null, true);
			
		Assert.assertTrue(lancamentos.size()==50);
	}
	
	@Test
	public void obterTotalLancamentosNaoExpedidos() {
				
		Long nLancamentos = lancamentoRepository.obterTotalLancamentosNaoExpedidos(
				Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
				null, true);
					
		Assert.assertTrue(nLancamentos.equals(50L));
	}
		
}


