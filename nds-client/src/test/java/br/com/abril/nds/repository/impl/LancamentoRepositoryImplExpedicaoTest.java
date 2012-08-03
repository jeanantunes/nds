package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO.SortColumn;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class LancamentoRepositoryImplExpedicaoTest extends AbstractRepositoryImplTest{


	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	
	@Before
	public void setUp() {
		Box box300Reparte = Fixture.boxReparte300();
		save(box300Reparte);

		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoRevista);
		
		CFOP cfop = Fixture.cfop5102();
		save(cfop);
		
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		for(Integer i=1000;i<1010; i++) {
			
			PessoaJuridica juridica = Fixture.pessoaJuridica("PessoaJ"+i,
					"0"+ i, "000.000.000.000", "acme@mail.com", "99.999-9");
			save(juridica);
			
			Fornecedor fornecedor = Fixture.fornecedor(juridica, SituacaoCadastro.ATIVO, true, tipoFornecedorPublicacao, null);
			save(fornecedor);
			
			Produto produto = Fixture.produto("00"+i, "descricao"+i, "nome"+i, PeriodicidadeProduto.ANUAL, tipoRevista, 5, 5, BigDecimal.TEN, TributacaoFiscal. TRIBUTADO);
			produto.addFornecedor(fornecedor);
			produto.setEditor(abril);
			save(produto); 
			
			ProdutoEdicao produtoEdicao = Fixture.produtoEdicao("1", i.longValue(), 50, 40, 
					new BigDecimal(30), new BigDecimal(20), new BigDecimal(10), "ABCDEFGHIJKLMNOPQRSTU", 1L, produto, null, false);	
			save(produtoEdicao);
			
			
			TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
			save(tipoNotaFiscal);

			NotaFiscalEntradaFornecedor notaFiscalFornecedor = Fixture
					.notaFiscalEntradaFornecedor(cfop, juridica, fornecedor, tipoNotaFiscal,
							usuario, new BigDecimal(1),new BigDecimal(1),new BigDecimal(1));
			save(notaFiscalFornecedor);
			
			ItemNotaFiscalEntrada itemNotaFiscal= Fixture.itemNotaFiscal(
					produtoEdicao, usuario, notaFiscalFornecedor, 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012),
					Fixture.criarData(23, Calendar.FEBRUARY, 2012),
					TipoLancamento.LANCAMENTO,
					BigInteger.valueOf(i));					
			save(itemNotaFiscal);
			
			RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(
				notaFiscalFornecedor, usuario, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
			save(recebimentoFisico);
			
			
			ItemRecebimentoFisico itemFisico = Fixture.itemRecebimentoFisico(
					itemNotaFiscal, recebimentoFisico, BigInteger.valueOf(i));
			save(itemFisico);
			
			Lancamento lancamento = Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicao,
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					BigInteger.valueOf(100), 
					StatusLancamento.ESTUDO_FECHADO, 
					itemFisico, 1);
			lancamento.setReparte(BigInteger.valueOf(10));
			save(lancamento);
		
			Estudo estudo = new Estudo();
			estudo.setDataLancamento(Fixture.criarData(23, Calendar.FEBRUARY, 2012));
			estudo.setProdutoEdicao(produtoEdicao);
			estudo.setQtdeReparte(BigInteger.valueOf(10));
			save(estudo);
			
			Pessoa pessoa = Fixture.pessoaJuridica("razaoS"+i, "01" + i, "ie"+i, "email"+i, "99.999-9");
			Cota cota = Fixture.cota(i, pessoa, SituacaoCadastro.ATIVO, box300Reparte);
			EstudoCota estudoCota = Fixture.estudoCota(BigInteger.valueOf(3), BigInteger.valueOf(3), 
					estudo, cota);
			save(pessoa,cota,estudoCota);		
			
			Pessoa pessoa2 = Fixture.pessoaJuridica("razaoS2"+i, "02" + i, "ie"+i, "email"+i, "99.999-9");
			Cota cota2 = Fixture.cota(i+3000, pessoa2, SituacaoCadastro.ATIVO, box300Reparte);
			EstudoCota estudoCota2 = Fixture.estudoCota(BigInteger.valueOf(7), BigInteger.valueOf(7), 
					estudo, cota2);
			save( pessoa2,cota2,estudoCota2);		
			
			
			TipoMovimentoEstoque tipoMovimento = Fixture.tipoMovimentoRecebimentoReparte();	

			TipoMovimentoEstoque tipoMovimento2 = Fixture.tipoMovimentoEnvioJornaleiro();
			save(tipoMovimento,tipoMovimento2);
		}
		
	}


	@Test
	public void obterLancamentosNaoExpedidos() {
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(0, 100, "ASC",SortColumn.CODIGO_PRODUTO.getProperty());
		
		List<Lancamento> lancamentos = lancamentoRepository.obterLancamentosNaoExpedidos(
				paginacaoVO, 
				Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
				null, true);
			
		Assert.assertTrue(lancamentos.size()==10);
	}
	
	@Test
	public void obterTotalLancamentosNaoExpedidos() {
				
		Long nLancamentos = lancamentoRepository.obterTotalLancamentosNaoExpedidos(
				Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
				null, true);
					
		Assert.assertTrue(nLancamentos.equals(10L));
	}	

}


