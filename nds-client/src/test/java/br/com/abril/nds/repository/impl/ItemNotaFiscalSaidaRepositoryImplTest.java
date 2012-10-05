package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import junit.framework.Assert;

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
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalSaida;
import br.com.abril.nds.model.fiscal.NCM;
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
		
		box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
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
		
		Produto produtoCE = Fixture.produto("00084", "Produto CE", "ProdutoCE", periodicidade, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		Produto produtoCE_2 = Fixture.produto("00085", "Produto CE 2", "ProdutoCE_2", periodicidade, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		Produto produtoCE_3 = Fixture.produto("00086", "Produto CE 3", "ProdutoCE_3", periodicidade, tipoProdutoRevista, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		
		produtoCE.addFornecedor(fornecedorDinap);
		produtoCE_2.addFornecedor(fornecedorDinap);
		produtoCE_3.addFornecedor(fornecedorDinap);
		
		save(produtoCE, produtoCE_2, produtoCE_3);

		produtoEdicaoCE = Fixture.produtoEdicao("1", 84L, 10, 7,
				new Long(100), BigDecimal.TEN, new BigDecimal(15), "ABCDEFGHIJKLMNOPQ", 1L, produtoCE, null, false);

		
		produtoEdicaoCE_2 = Fixture.produtoEdicao("1", 85L, 10, 7,
				new Long(100), BigDecimal.TEN, new BigDecimal(18), "ABCDEFGHIJKLMNOPZ", 2L, produtoCE_2, null, false);

		
		produtoEdicaoCE_3 = Fixture.produtoEdicao("1", 86L, 10, 7,
				new Long(100), BigDecimal.TEN, new BigDecimal(90), "ABCDEFGHIJKLMNOPT", 3L, produtoCE_3, null, false);

		
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
		
		String naturezaOperacao = "34345";
		String formaPagamento = "34535";
		String horaSaida = "345435";
		String ambiente = "45243";
		String protocolo = "";
		String versao = "";
		String emissorInscricaoEstadualSubstituto = "";
		String emissorInscricaoMunicipal = "";
		BigDecimal valorBaseICMS = BigDecimal.TEN;
		BigDecimal valorICMS = BigDecimal.TEN;
		BigDecimal valorBaseICMSSubstituto = BigDecimal.TEN;
		BigDecimal valorICMSSubstituto = BigDecimal.TEN;
		BigDecimal valorProdutos = BigDecimal.TEN;
		BigDecimal valorFrete = BigDecimal.TEN;
		BigDecimal valorSeguro = BigDecimal.TEN;
		BigDecimal valorOutro = BigDecimal.TEN;
		BigDecimal valorIPI = BigDecimal.TEN;
		BigDecimal valorNF = BigDecimal.TEN;
		Integer frete = 0;
		String transportadoraCNPJ = "";
		String transportadoraNome = "";
		String transportadoraInscricaoEstadual = "";
		String transportadoraEndereco = "";
		String transportadoraMunicipio = "";
		String transportadoraUF = "";
		String transportadoraQuantidade = "";
		String transportadoraEspecie = "";
		String transportadoraMarca = "";
		String transportadoraNumeracao = "";
		BigDecimal transportadoraPesoBruto = BigDecimal.TEN;
		BigDecimal transportadoraPesoLiquido = BigDecimal.TEN;
		String transportadoraANTT = "";
		String transportadoraPlacaVeiculo = "";
		String transportadoraPlacaVeiculoUF = "";
		BigDecimal ISSQNTotal = BigDecimal.TEN;
		BigDecimal ISSQNBase = BigDecimal.TEN;
		BigDecimal ISSQNValor = BigDecimal.TEN;
		String informacoesComplementares = "";
		String numeroFatura = "";
		BigDecimal valorFatura = BigDecimal.TEN;
	
		notaFiscalSaidaFornecedor = 
				Fixture.notaFiscalSaidaFornecedorNFE(
						cfop5102, 
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
						true,
						naturezaOperacao,                    
						formaPagamento,                      
						horaSaida,                           
						ambiente,                            
						protocolo,                           
						versao,                              
						emissorInscricaoEstadualSubstituto,  
						emissorInscricaoMunicipal,           
						valorBaseICMS,                       
						valorICMS,                           
						valorBaseICMSSubstituto,            
						valorICMSSubstituto,                 
						valorProdutos,                       
						valorFrete,                          
						valorSeguro,                         
						valorOutro,                          
						valorIPI,                            
						valorNF,                             
						frete,                               
						transportadoraCNPJ,                  
						transportadoraNome,                  
						transportadoraInscricaoEstadual,     
						transportadoraEndereco,              
						transportadoraMunicipio,             
						transportadoraUF,                    
						transportadoraQuantidade,           
						transportadoraEspecie,               
						transportadoraMarca,                 
						transportadoraNumeracao,             
						transportadoraPesoBruto,             
						transportadoraPesoLiquido,           
						transportadoraANTT,                  
						transportadoraPlacaVeiculo,         
						transportadoraPlacaVeiculoUF,        
						ISSQNTotal,                          
						ISSQNBase,                           
						ISSQNValor,                          
						informacoesComplementares,           
						numeroFatura,                        
						valorFatura);
		
		
		save(notaFiscalSaidaFornecedor);
		
		String 		NCMProduto               = "3424";
		String 		CFOPProduto              = "24234";
		Long 		unidadeProduto           = 0L; 
		String 		CSTProduto               = "2344";
		String 		CSOSNProduto             = "1233";
		BigDecimal 	baseCalculoProduto       = BigDecimal.ZERO; 
		BigDecimal 	aliquotaICMSProduto      = BigDecimal.ZERO; 
		BigDecimal 	valorICMSProduto         = BigDecimal.ZERO; 
		BigDecimal 	aliquotaIPIProduto       = BigDecimal.ZERO; 
		BigDecimal 	valorIPIProduto          = BigDecimal.ZERO; 

		
		ItemNotaFiscalSaida itemNotaFiscalSaida = 
				Fixture.itemNotaFiscalSaidaNFE(
						produtoEdicaoCE, 
						notaFiscalSaidaFornecedor, 
						BigInteger.TEN,
						NCMProduto, 
						CFOPProduto, 
						unidadeProduto, 
						CSTProduto, 
						CSOSNProduto, 
						baseCalculoProduto, 
						aliquotaICMSProduto, 
						valorICMSProduto, 
						aliquotaIPIProduto, 
						valorIPIProduto);
		
		save(itemNotaFiscalSaida);

		
	}
	
	@Test
	public void testObterListaItemNotaFiscalSaidaDadosDanfe() {
		
		List<ItemDanfe> result =  itemNotaFiscalSaidaRepository.obterListaItemNotaFiscalSaidaDadosDanfe(notaFiscalSaidaFornecedor.getId());
		
		Assert.assertEquals(1, result.size());
		
	}
	
}
