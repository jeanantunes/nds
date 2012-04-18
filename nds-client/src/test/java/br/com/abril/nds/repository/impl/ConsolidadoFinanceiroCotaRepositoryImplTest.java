package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsignadoCotaDTO;
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.VendaEncalheDTO;
import br.com.abril.nds.dto.FiltroConsolidadoConsignadoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoVendaCotaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.ConsolidadoFinanceiroService;

public class ConsolidadoFinanceiroCotaRepositoryImplTest extends AbstractRepositoryImplTest {
			
	@Autowired
	private ConsolidadoFinanceiroService consolidadoFinanceiroService;
	
	Cota cotaManoel;
	Date dataAtual = new Date();
	List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = new ArrayList<MovimentoEstoqueCota>();
	EstoqueProdutoCota estoqueProdutoCota = new EstoqueProdutoCota();
	MovimentoEstoqueCota movimento = new MovimentoEstoqueCota();
	
	@Before
	public void setUp() {
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
				save(manoel);
				
		Box box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.REPARTE);
		save(box1);
		
		cotaManoel = Fixture.cota(1235, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
		
		TipoMovimentoEstoque tipoMovimentoEncalhe = Fixture.tipoMovimentoEnvioEncalhe();
		save(tipoMovimentoEncalhe);
		
		TipoMovimentoEstoque tipoMovimentoVenda = Fixture.tipoMovimentoVendaEncalhe();
		save(tipoMovimentoVenda);
		
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		TipoProduto tipoProduto = Fixture.tipoRevista();
		save(tipoProduto);
		
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedor);
		
		Produto produto = Fixture.produtoBravo(tipoProduto);
		Fornecedor fornecedor = Fixture.fornecedorAcme(tipoFornecedor);
		produto.setEditor(abril);
		save(fornecedor);
		produto.addFornecedor(fornecedor);
		save(produto);
		
		ProdutoEdicao produtoEdicao = Fixture.produtoEdicao(234L,12 , 1, new BigDecimal(9), new BigDecimal(8), 
				new BigDecimal(10), produto);
		save(produtoEdicao);
				
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
				
		PessoaJuridica pj = Fixture.juridicaDinap();		
		save(pj);
		
		CFOP cfop = Fixture.cfop5102();		
		save(cfop);
				
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		save(tipoNotaFiscal);
					
		NotaFiscalEntradaFornecedor notaFiscal = Fixture.notaFiscalEntradaFornecedor(cfop, pj, fornecedor, tipoNotaFiscal, usuario, new BigDecimal(145),  new BigDecimal(10),  new BigDecimal(10));
		save(notaFiscal);
		
		RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(notaFiscal, usuario, new Date(), new Date(), StatusConfirmacao.PENDENTE);
		save(recebimentoFisico);
		
		ItemNotaFiscalEntrada itemNotaFiscal= 
				Fixture.itemNotaFiscal(
						produtoEdicao, 
						usuario, 
						notaFiscal, 
						new Date(), 
						new Date(),
						TipoLancamento.LANCAMENTO,
						new BigDecimal(12));
		save(itemNotaFiscal);
		
		ItemRecebimentoFisico itemRecebimentoFisico= Fixture.itemRecebimentoFisico(itemNotaFiscal, recebimentoFisico, new BigDecimal(12));
		save(itemRecebimentoFisico);
		
		Lancamento lancamento = Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicao, dataAtual, dataAtual, dataAtual, dataAtual, new BigDecimal(30), StatusLancamento.CONFIRMADO, itemRecebimentoFisico);
		save(lancamento);	
		
		Estudo estudo = Fixture.estudo(BigDecimal.TEN, dataAtual, produtoEdicao);
		save(estudo);
		
		EstudoCota estudoCota = Fixture.estudoCota(new BigDecimal(30), new BigDecimal(30), estudo, cotaManoel);
		save(estudoCota);
		
		Expedicao expedicao = Fixture.expedicao(usuario, dataAtual);
		save(expedicao);
		
		estoqueProdutoCota = Fixture.estoqueProdutoCota(produtoEdicao,new BigDecimal(30), cotaManoel, listaMovimentoEstoqueCota);		
		save(estoqueProdutoCota);
		
		MovimentoEstoqueCota movimentoEstoqueCota = Fixture.movimentoEstoqueCota(produtoEdicao, tipoMovimento, usuario, estoqueProdutoCota, new BigDecimal(23), cotaManoel, StatusAprovacao.APROVADO, "MOTIVO A");
		save(movimentoEstoqueCota);
		
		movimento = Fixture.movimentoEstoqueCota(produtoEdicao, tipoMovimentoEncalhe, usuario, estoqueProdutoCota, new BigDecimal(23), cotaManoel, StatusAprovacao.APROVADO, "motivo");
		estoqueProdutoCota.getMovimentos().add(movimento);
		save(movimento);
		MovimentoEstoqueCota  movimentoVenda = Fixture.movimentoEstoqueCota(produtoEdicao, tipoMovimentoVenda, usuario, estoqueProdutoCota, new BigDecimal(23), cotaManoel, StatusAprovacao.APROVADO, "motivo");
		save(movimentoVenda);
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = Fixture.tipoMovimentoFinanceiroEnvioEncalhe();
		save(tipoMovimentoFinanceiro);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota= Fixture.movimentoFinanceiroCota(cotaManoel, tipoMovimentoFinanceiro, usuario, 
				new BigDecimal(230), listaMovimentoEstoqueCota, StatusAprovacao.APROVADO, dataAtual, true);
		save(movimentoFinanceiroCota);
		
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroCompra = Fixture.tipoMovimentoFinanceiroCompraEncalhe();
		save(tipoMovimentoFinanceiroCompra);
		
		List<MovimentoEstoqueCota>  listMovimentoEstoqueCotaVenda= new ArrayList<MovimentoEstoqueCota>();
		listMovimentoEstoqueCotaVenda.add(movimentoVenda);
		
		MovimentoFinanceiroCota movimentoFinanceiroCompra= Fixture.movimentoFinanceiroCota(cotaManoel, tipoMovimentoFinanceiroCompra, usuario, 
				new BigDecimal(230),listMovimentoEstoqueCotaVenda , StatusAprovacao.APROVADO, dataAtual, true);
		save(movimentoFinanceiroCompra);
		
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = new ArrayList<MovimentoFinanceiroCota>();
		listaMovimentoFinanceiroCota.add(movimentoFinanceiroCota);
		listaMovimentoFinanceiroCota.add(movimentoFinanceiroCompra);
		
		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = Fixture.consolidadoFinanceiroCota(listaMovimentoFinanceiroCota, cotaManoel, dataAtual, new BigDecimal(230));
		save(consolidadoFinanceiroCota);
		
	}
	
	//@Before
	public void setUp2() {
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		PessoaFisica manoel = Fixture.pessoaFisica("122.456.789-22",
				"manoel@mail.com", "Manoel da Silva");
				save(manoel);
				
		Box box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.REPARTE);
		save(box1);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
		
		TipoMovimentoEstoque tipoMovimento = Fixture.tipoMovimentoEnvioJornaleiro();
		save(tipoMovimento);
		
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		TipoProduto tipoProduto = Fixture.tipoRevista();
		save(tipoProduto);
		
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedor);
		
		Produto produto = Fixture.produtoBravo(tipoProduto);
		Fornecedor fornecedor = Fixture.fornecedorAcme(tipoFornecedor);
		produto.setEditor(abril);
		save(fornecedor);
		produto.addFornecedor(fornecedor);
		save(produto);
		
		ProdutoEdicao produtoEdicao = Fixture.produtoEdicao(234L,12 , 1, new BigDecimal(9), new BigDecimal(8), 
				new BigDecimal(10), produto);
		save(produtoEdicao);
				
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
				
		PessoaJuridica pj = Fixture.juridicaDinap();		
		save(pj);
		
		CFOP cfop = Fixture.cfop5102();		
		save(cfop);
				
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		save(tipoNotaFiscal);
					
		NotaFiscalEntradaFornecedor notaFiscal = Fixture.notaFiscalEntradaFornecedor(cfop, pj, fornecedor, tipoNotaFiscal, usuario, new BigDecimal(145),  new BigDecimal(10),  new BigDecimal(10));
		save(notaFiscal);
		
		RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(notaFiscal, usuario, new Date(), new Date(), StatusConfirmacao.PENDENTE);
		save(recebimentoFisico);
		
		ItemNotaFiscalEntrada itemNotaFiscal= 
				Fixture.itemNotaFiscal(
						produtoEdicao, 
						usuario, 
						notaFiscal, 
						new Date(), 
						new Date(),
						TipoLancamento.LANCAMENTO,
						new BigDecimal(12));
		save(itemNotaFiscal);
		
		ItemRecebimentoFisico itemRecebimentoFisico= Fixture.itemRecebimentoFisico(itemNotaFiscal, recebimentoFisico, new BigDecimal(12));
		save(itemRecebimentoFisico);
		
		Lancamento lancamento = Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicao, dataAtual, dataAtual, dataAtual, dataAtual, new BigDecimal(30), StatusLancamento.CONFIRMADO, itemRecebimentoFisico);
		save(lancamento);	
		
		Estudo estudo = Fixture.estudo(BigDecimal.TEN, dataAtual, produtoEdicao);
		save(estudo);
		
		EstudoCota estudoCota = Fixture.estudoCota(new BigDecimal(20), new BigDecimal(10), estudo, cotaManoel);
		save(estudoCota);
		
		Expedicao expedicao = Fixture.expedicao(usuario, dataAtual);
		save(expedicao);
		
		//RateioDiferenca rateioDiferenca = Fixture.rateioDiferenca( , cota, diferenca, estudoCota)
		
		estoqueProdutoCota = Fixture.estoqueProdutoCota(produtoEdicao,new BigDecimal(30), cotaManoel, listaMovimentoEstoqueCota);		
		save(estoqueProdutoCota);
		
		MovimentoEstoqueCota movimentoEstoqueCota = Fixture.movimentoEstoqueCota(produtoEdicao, tipoMovimento, usuario, estoqueProdutoCota, new BigDecimal(23), cotaManoel, StatusAprovacao.APROVADO, "MOTIVO A");
		save(movimentoEstoqueCota);
		
		movimento = Fixture.movimentoEstoqueCota(produtoEdicao, tipoMovimento, usuario, estoqueProdutoCota, new BigDecimal(23), cotaManoel, StatusAprovacao.APROVADO, "motivo");
		estoqueProdutoCota.getMovimentos().add(movimento);
		save(movimento);
		
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = Fixture.tipoMovimentoFinanceiroEnvioEncalhe();
		save(tipoMovimentoFinanceiro);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota= Fixture.movimentoFinanceiroCota(cotaManoel, tipoMovimentoFinanceiro, usuario, 
				new BigDecimal(230), listaMovimentoEstoqueCota, StatusAprovacao.APROVADO, dataAtual, true);
		save(movimentoFinanceiroCota);
		
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = new ArrayList<MovimentoFinanceiroCota>();
		listaMovimentoFinanceiroCota.add(movimentoFinanceiroCota);
		
		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = Fixture.consolidadoFinanceiroCota(listaMovimentoFinanceiroCota, cotaManoel, dataAtual, new BigDecimal(230));
		save(consolidadoFinanceiroCota);
		
	}
		
	@Test
	public void obterEncalhedaCota(){
		
		FiltroConsolidadoEncalheCotaDTO filtro = new FiltroConsolidadoEncalheCotaDTO();		
		
		filtro.setDataConsolidado(dataAtual);
		filtro.setNumeroCota(cotaManoel.getNumeroCota());
		
		List<EncalheCotaDTO> lista = consolidadoFinanceiroService.obterMovimentoEstoqueCotaEncalhe(filtro);
		
		Assert.assertEquals(1, lista.size());
		
	}
	
	
	@Test
	public void obterVendaEncalhe(){
		
		FiltroConsolidadoVendaCotaDTO filtro = new FiltroConsolidadoVendaCotaDTO();		
		
		filtro.setIdConsolidado(10l);
		
		filtro.setOrdenacaoColuna(FiltroConsolidadoVendaCotaDTO.OrdenacaoColuna.NUMERO_EDICAO);
		
		List<VendaEncalheDTO> lista = consolidadoFinanceiroService.obterMovimentoVendaEncalhe(filtro);
		Assert.assertEquals(1, lista.size());
		
	}
	
		/*@Test
	public void obterConsignadoCota(){
		try{
		FiltroConsolidadoConsignadoCotaDTO filtro = new FiltroConsolidadoConsignadoCotaDTO();	
		
		filtro.setDataConsolidado(dataAtual);
		filtro.setNumeroCota(cotaManoel.getNumeroCota());
		
		List<ConsignadoCotaDTO> lista = consolidadoFinanceiroService.obterMovimentoEstoqueCotaConsignado(filtro);
		
		Assert.assertEquals(1, lista.size());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}*/

}
