package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Calendar;
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
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalSaida;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.NotaFiscalSaidaFornecedor;
import br.com.abril.nds.model.fiscal.StatusEmissaoNfe;
import br.com.abril.nds.model.fiscal.TipoEmissaoNfe;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;

public class ItemNotaFiscalEntradaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private ItemNotaFiscalEntradaRepository itemNotaFiscalEntradaRepository;
	
	private static Box box1;
	
	private static NotaFiscalEntradaCota notaFiscalEntradaCota;
	
	@Before
	public void setUp() {
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		CFOP cfop5102 = Fixture.cfop5102();
		save(cfop5102);
		
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		save(tipoNotaFiscal);
		
		box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.LANCAMENTO);
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
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(15), "ABCDEFGHIJKLMNOPQRSTU", 1L, produtoCE);
		produtoEdicaoCE.setDesconto(BigDecimal.ZERO);

		
		produtoEdicaoCE_2 = Fixture.produtoEdicao(85L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(18), "ABCDEFGHIJKLMNOPQRST", 2L, produtoCE_2);
		produtoEdicaoCE.setDesconto(BigDecimal.ONE);

		
		produtoEdicaoCE_3 = Fixture.produtoEdicao(86L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(90), "ABCDEFGHIJKLMNOPQRS", 3L, produtoCE_3);
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

		
		
		///// ENTRADA FORNECEDOR
		
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

		
		NotaFiscalEntradaFornecedor notaFiscalEntradaFornecedorNFE = 
				Fixture.notaFiscalEntradaFornecedorNFE(
						cfop5102, 
						fornecedorDinap.getJuridica(),
						11011110L,
						"11111000",
						"11101011101",
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
						
		
		save(notaFiscalEntradaFornecedorNFE);

		ItemNotaFiscalEntrada itemNotaFiscalEntradaNFE = 
				Fixture.itemNotaFiscalEntradaNFE(
						produtoEdicaoCE, 
						usuarioJoao, 
						notaFiscalEntradaFornecedorNFE, 
						Fixture.criarData(22, Calendar.FEBRUARY,2012),     
						Fixture.criarData(22, Calendar.FEBRUARY,2012),     
						TipoLancamento.LANCAMENTO,                         
						new BigDecimal(50), 
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
				
		save(itemNotaFiscalEntradaNFE);
		
		
		///// ENTRADA COTA
		
		notaFiscalEntradaCota = 
				
		Fixture.notaFiscalEntradaCotaNFE(
						cfop5102, 
						fornecedorDinap.getJuridica(),
						2222200002L,
						"220202022220",
						"2000022",
						cota,
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
		
		
		save(notaFiscalEntradaCota);

		ItemNotaFiscalEntrada itemNotaFiscalEntradaNFE_2 = 
				Fixture.itemNotaFiscalEntradaNFE(
						produtoEdicaoCE, 
						usuarioJoao, 
						notaFiscalEntradaCota, 
						Fixture.criarData(22, Calendar.FEBRUARY,2012),     
						Fixture.criarData(22, Calendar.FEBRUARY,2012),     
						TipoLancamento.LANCAMENTO,                         
						new BigDecimal(50), 
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
				
		save(itemNotaFiscalEntradaNFE_2);
		

		
	}
	
	@Test
	public void testObterListaItemNotaFiscalEntradaDadosDanfe() {
		
		@SuppressWarnings("unused")
		List<ItemDanfe> result = itemNotaFiscalEntradaRepository.obterListaItemNotaFiscalEntradaDadosDanfe(notaFiscalEntradaCota.getId());
		
		Assert.assertEquals(1, result.size());

		
	}
	
}
