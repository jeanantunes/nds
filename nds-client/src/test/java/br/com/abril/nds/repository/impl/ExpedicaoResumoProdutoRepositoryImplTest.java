package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ExpedicaoRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class ExpedicaoResumoProdutoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ExpedicaoRepository expedicaoRepository;
	
	private Date dataLancamento = Fixture.criarData(23, Calendar.FEBRUARY, 2012);
	
	@Before
	public void setup() {
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoRevista);
		
		CFOP cfop = Fixture.cfop5102();
		save(cfop);
		
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		TipoMovimentoEstoque tipoMovimentoSobraDe  = Fixture.tipoMovimentoSobraDe();
		save(tipoMovimentoSobraDe);
		
		TipoMovimentoEstoque tipoMovimentoFaltDe  = Fixture.tipoMovimentoFaltaDe();
		save(tipoMovimentoFaltDe);
		
		TipoMovimentoEstoque tipoMovimentoFaltEM  = Fixture.tipoMovimentoFaltaEm();
		save(tipoMovimentoFaltEM);
		
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		
		for(Integer i=1000;i<1010; i++) {
			
			PessoaJuridica juridica = Fixture.pessoaJuridica("PessoaJ"+i,
					"0"+ i, "000000000000", "acme@mail.com", "99.999-9");
			save(juridica);
			
			Fornecedor fornecedor = Fixture.fornecedor(juridica, SituacaoCadastro.ATIVO, true, tipoFornecedorPublicacao, null);
			save(fornecedor);
			
			Produto produto = Fixture.produto("00"+i, "descricao"+i, "nome"+i, PeriodicidadeProduto.ANUAL, tipoRevista, 5, 5, new Long(10000), TributacaoFiscal.TRIBUTADO);
			produto.addFornecedor(fornecedor);
			produto.setEditor(abril);
			save(produto); 
			
			ProdutoEdicao produtoEdicao = Fixture.produtoEdicao("1", i.longValue(), 50, 40, 
					new Long(30000), new BigDecimal(20), new BigDecimal(10), "ABCDEFGHIJKLMNOPQ", 1L, produto, null, false);	
			save(produtoEdicao);
			
			
			TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
			save(tipoNotaFiscal);

			
			List<ItemRecebimentoFisico> listaRecebimentos = new ArrayList<ItemRecebimentoFisico>() ;
			
			EstoqueProduto estoque  =  Fixture.estoqueProduto(produtoEdicao, BigInteger.ZERO);
			save(estoque);
			
			for(int x= 1; x< 3 ;x++){
				
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
						itemNotaFiscal, recebimentoFisico, BigInteger.valueOf(i+x));
				save(itemFisico);
				
				
				MovimentoEstoque movimentoEstoque  = 
					Fixture.movimentoEstoque(itemFisico, produtoEdicao, tipoMovimentoFaltDe, usuario,
						estoque, new Date(), BigInteger.valueOf(1),
						StatusAprovacao.APROVADO, "Aprovado");
				
				save(movimentoEstoque);
				
				Diferenca diferenca = Fixture.diferenca(BigInteger.valueOf(10), usuario, produtoEdicao, TipoDiferenca.SOBRA_DE, StatusConfirmacao.CONFIRMADO, itemFisico, movimentoEstoque, true);
				save(diferenca);
				
				itemFisico.setDiferenca(diferenca);
				update(itemFisico);
				
				listaRecebimentos.add(itemFisico);
			}
			
			Expedicao expedicao = Fixture.expedicao(usuario,Fixture.criarData(1, 3, 2010));
			save(expedicao);
			
			Lancamento lancamento = Fixture.lancamentos(TipoLancamento.LANCAMENTO, produtoEdicao,
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					BigInteger.valueOf(100), 
					StatusLancamento.EXPEDIDO, 
					listaRecebimentos, 1);
			lancamento.setReparte(BigInteger.valueOf(10));
			lancamento.setExpedicao(expedicao);
			save(lancamento);
		
			Estudo estudo = new Estudo();
			estudo.setDataLancamento(Fixture.criarData(23, Calendar.FEBRUARY, 2012));
			estudo.setProdutoEdicao(produtoEdicao);
			estudo.setQtdeReparte(BigInteger.valueOf(i));
			estudo.setStatus(StatusLancamento.ESTUDO_FECHADO);
			estudo.setDataCadastro(new Date());
			save(estudo);
		}
		
	}
	
	@Test 
	public void consultarResumoExpedicaoPorProdutos(){
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		filtro.setDataLancamento(dataLancamento);
		filtro.setPaginacao(getPaginacaoVO(1, 10, Ordenacao.DESC));
		
		List<ExpedicaoDTO> lista = expedicaoRepository.obterResumoExpedicaoPorProduto(filtro);
		
		Assert.assertNotNull(lista);
		
		Assert.assertTrue(!lista.isEmpty());
	
	}
		
	@Test
	public void consultarQuantidadeResumoExpedicaoPorProdutos(){
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		filtro.setDataLancamento(dataLancamento);
		filtro.setPaginacao(getPaginacaoVO(1, 10, Ordenacao.DESC));
		
		Long quantidade =  expedicaoRepository.obterQuantidadeResumoExpedicaoPorProduto(filtro);
		
		Assert.assertNotNull(quantidade);
		
		Assert.assertTrue(quantidade != 0);
	}

	/**
	 * Retorna um objeto com  valores de paginação.
	 * @param paginaAtual
	 * @param resultadoPorPagina
	 * @param ordenacao
	 * @return PaginacaoVO
	 */
	private PaginacaoVO getPaginacaoVO(int paginaAtual, int resultadoPorPagina, Ordenacao ordenacao){
		
		PaginacaoVO paginacao = new PaginacaoVO();
		
		paginacao.setOrdenacao(ordenacao);
		paginacao.setPaginaAtual(paginaAtual);
		paginacao.setQtdResultadosPorPagina(resultadoPorPagina);
		
		return paginacao;
	}
	
}
