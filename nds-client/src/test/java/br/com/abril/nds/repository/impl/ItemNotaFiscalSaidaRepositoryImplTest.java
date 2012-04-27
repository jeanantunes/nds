package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ItemDanfe;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalSaida;
import br.com.abril.nds.model.fiscal.NotaFiscalSaidaFornecedor;
import br.com.abril.nds.model.fiscal.StatusEmissaoNfe;
import br.com.abril.nds.model.fiscal.TipoEmissaoNfe;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ItemNotaFiscalSaidaRepository;

public class ItemNotaFiscalSaidaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private ItemNotaFiscalSaidaRepository itemNotaFiscalSaidaRepository;
	
	private static Box box1;
	
	private static NotaFiscalSaidaFornecedor notaFiscalSaidaFornecedor;
	
	@Before
	public void setUp() {
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		CFOP cfop5102 = Fixture.cfop5102();
		save(cfop5102);
		
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		save(tipoNotaFiscal);
		
		box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.REPARTE);
		save(box1);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista();
		save(tipoProdutoRevista);
		
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		
		Fornecedor fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(fornecedorDinap);
		
		Cota cotaJohnyConsultaEncalhe = null;
		
		PessoaFisica johnyCE = Fixture.pessoaFisica(
				"352.855.474-00",
				"johny@discover.com.br", "Johny da Silva");
		save(johnyCE);
		
		cotaJohnyConsultaEncalhe = Fixture.cota(2593, johnyCE, SituacaoCadastro.ATIVO, box1);
		save(cotaJohnyConsultaEncalhe);
		
		ProdutoEdicao produtoEdicaoCE = null;
		ProdutoEdicao produtoEdicaoCE_2 = null;
		ProdutoEdicao produtoEdicaoCE_3 = null;
		
		PeriodicidadeProduto periodicidade = PeriodicidadeProduto.MENSAL;
		
		Produto produtoCE = Fixture.produto("00084", "Produto CE", "ProdutoCE", periodicidade, tipoProdutoRevista);
		Produto produtoCE_2 = Fixture.produto("00085", "Produto CE 2", "ProdutoCE_2", periodicidade, tipoProdutoRevista);
		Produto produtoCE_3 = Fixture.produto("00086", "Produto CE 3", "ProdutoCE_3", periodicidade, tipoProdutoRevista);
		
		produtoCE.addFornecedor(fornecedorDinap);
		produtoCE_2.addFornecedor(fornecedorDinap);
		produtoCE_3.addFornecedor(fornecedorDinap);
		
		save(produtoCE, produtoCE_2, produtoCE_3);

		produtoEdicaoCE = Fixture.produtoEdicao(84L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(15), produtoCE);
		produtoEdicaoCE.setDesconto(BigDecimal.ZERO);

		
		produtoEdicaoCE_2 = Fixture.produtoEdicao(85L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(18), produtoCE_2);
		produtoEdicaoCE.setDesconto(BigDecimal.ONE);

		
		produtoEdicaoCE_3 = Fixture.produtoEdicao(86L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(90), produtoCE_3);
		produtoEdicaoCE.setDesconto(BigDecimal.ONE);

		
		save(produtoEdicaoCE, produtoEdicaoCE_2, produtoEdicaoCE_3);
		
		gerarNotasFiscaisEletronicas(
				cfop5102, 
				cotaJohnyConsultaEncalhe, 
				fornecedorDinap, 
				tipoNotaFiscal,
				usuarioJoao, 
				produtoEdicaoCE, 
				produtoEdicaoCE_2, 
				produtoEdicaoCE_3);
		
	}
	
	private void gerarNotasFiscaisEletronicas(
			CFOP cfop5102,
			Cota cota,
			Fornecedor fornecedorDinap, 
			TipoNotaFiscal tipoNotaFiscal, 
			Usuario usuarioJoao,
			ProdutoEdicao produtoEdicaoCE, 
			ProdutoEdicao produtoEdicaoCE_2,
			ProdutoEdicao produtoEdicaoCE_3) {
		
	
		notaFiscalSaidaFornecedor = 
				Fixture.notaFiscalSaidaFornecedorNFE(
						cfop5102, 
						fornecedorDinap.getJuridica(),
						33300003003L,
						"30300333330",
						"0003303",
						fornecedorDinap,
						StatusEmissaoNfe.NFE_AUTORIZADA,
						TipoEmissaoNfe.CONTINGENCIA_DPEC,
						tipoNotaFiscal,
						usuarioJoao, 
						BigDecimal.TEN, 
						BigDecimal.ZERO, 
						BigDecimal.TEN,
						true);
		
		
		save(notaFiscalSaidaFornecedor);

		ItemNotaFiscalSaida itemNotaFiscalSaida = 
				Fixture.itemNotaFiscalSaida(produtoEdicaoCE, notaFiscalSaidaFornecedor, BigDecimal.TEN);
		
		save(itemNotaFiscalSaida);

		
	}
	
	@Test
	public void testObterListaItemNotaFiscalSaidaDadosDanfe() {
		
		List<ItemDanfe> result =  itemNotaFiscalSaidaRepository.obterListaItemNotaFiscalSaidaDadosDanfe(notaFiscalSaidaFornecedor.getId());
		
	}
	
}
