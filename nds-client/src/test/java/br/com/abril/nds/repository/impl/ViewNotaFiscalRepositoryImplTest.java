package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO.OrdenacaoColuna;
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
import br.com.abril.nds.repository.ViewNotaFiscalRepository;
import br.com.abril.nds.vo.PaginacaoVO;

/*
 * TODO: A massa de dados utilizada para este teste unitário
 * está sendo verificada para satisfazer os testes
 */
@Ignore
public class ViewNotaFiscalRepositoryImplTest extends AbstractRepositoryImplTest {

	private static Box box1;
	
	@Before
	public void setUp() {
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		CFOP cfop5102 = Fixture.cfop5102();
		save(cfop5102);
		
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		save(tipoNotaFiscal);
		
		box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.LANCAMENTO, false);
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
		
		Produto produtoCE = Fixture.produto("00084", "Produto CE", "ProdutoCE", periodicidade, tipoProdutoRevista, 5, 5, BigDecimal.TEN);
		Produto produtoCE_2 = Fixture.produto("00085", "Produto CE 2", "ProdutoCE_2", periodicidade, tipoProdutoRevista, 5, 5, BigDecimal.TEN);
		Produto produtoCE_3 = Fixture.produto("00086", "Produto CE 3", "ProdutoCE_3", periodicidade, tipoProdutoRevista, 5, 5, BigDecimal.TEN);
		
		produtoCE.addFornecedor(fornecedorDinap);
		produtoCE_2.addFornecedor(fornecedorDinap);
		produtoCE_3.addFornecedor(fornecedorDinap);
		
		save(produtoCE, produtoCE_2, produtoCE_3);

		produtoEdicaoCE = Fixture.produtoEdicao("1", 84L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(15), "ABCDEFGHIJKLMNOPQRSTU", 1L, produtoCE, null, false);
		produtoEdicaoCE.setDesconto(BigDecimal.ZERO);

		
		produtoEdicaoCE_2 = Fixture.produtoEdicao("1", 85L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(18), "ABCDEFGHIJKLMNOPQRST", 2L, produtoCE_2, null, false);
		produtoEdicaoCE.setDesconto(BigDecimal.ONE);

		
		produtoEdicaoCE_3 = Fixture.produtoEdicao("1", 86L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(90), "ABCDEFGHIJKLMNOPQRS", 3L, produtoCE_3, null, false);
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

		
		///// ENTRADA FORNECEDOR
		
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
				Fixture.itemNotaFiscal(
						produtoEdicaoCE, 
						usuarioJoao,
						notaFiscalEntradaFornecedorNFE, 
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						TipoLancamento.LANCAMENTO,
						new BigDecimal(50));
		
		save(itemNotaFiscalEntradaNFE);
		
		
		///// ENTRADA COTA
		
		NotaFiscalEntradaCota notaFiscalEntradaCotaNFE = 
				Fixture.notaFiscalEntradaCotaNFE(
						cfop5102, 
						fornecedorDinap.getJuridica(),
						222220000L,
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
		
		
		save(notaFiscalEntradaCotaNFE);

		ItemNotaFiscalEntrada itemNotaFiscalEntradaNFE_2 = 
				Fixture.itemNotaFiscal(
						produtoEdicaoCE, 
						usuarioJoao,
						notaFiscalEntradaCotaNFE, 
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						TipoLancamento.LANCAMENTO,
						new BigDecimal(50));
		
		save(itemNotaFiscalEntradaNFE_2);
		
		///// SAIDA FORNECEDOR
	
		NotaFiscalSaidaFornecedor notaFiscalSaidaFornecedorNFE = 
				Fixture.notaFiscalSaidaFornecedorNFE(
						cfop5102, 
						fornecedorDinap.getJuridica(),
						333000030L,
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
		
		
		save(notaFiscalSaidaFornecedorNFE);

		ItemNotaFiscalSaida itemNotaFiscalSaida = 
				Fixture.itemNotaFiscalSaida(produtoEdicaoCE, notaFiscalSaidaFornecedorNFE, BigDecimal.TEN);
		
		save(itemNotaFiscalSaida);

		
	}
	
	
	@Autowired
	private ViewNotaFiscalRepository viewNotaFiscalRepository;
	
	@Test
	public void testObterQtdeRegistroNotaFiscal() {
		
		FiltroMonitorNfeDTO filtro = obterFiltroMonitorNfeDTO();
		
		Integer qtde = viewNotaFiscalRepository.obterQtdeRegistroNotaFiscal(filtro);
		
		Assert.assertEquals(3, qtde.intValue());
		
	}
	
	
	@Test
	public void testObterQtdeRegistroNotaFiscal_Cota() {
		
		FiltroMonitorNfeDTO filtro = obterFiltroMonitorNfeDTO();
		
		filtro.setBox(box1.getCodigo());
		
		Integer qtde = viewNotaFiscalRepository.obterQtdeRegistroNotaFiscal(filtro);
		
		Assert.assertEquals(1, qtde.intValue());
		
	}
	
	@Test
	public void pesquisarNotaFiscal_Todas() {
		FiltroMonitorNfeDTO filtro = obterFiltroMonitorNfeDTO();
		List<NfeDTO> lista = viewNotaFiscalRepository.pesquisarNotaFiscal(filtro);
		Assert.assertNotNull(lista);
		Assert.assertEquals(3, lista.size());

		filtro.setOrdenacaoColuna(OrdenacaoColuna.CNPJ_DESTINATARIO);
		viewNotaFiscalRepository.pesquisarNotaFiscal(filtro);

		filtro.setOrdenacaoColuna(OrdenacaoColuna.CNPJ_REMETENTE);
		viewNotaFiscalRepository.pesquisarNotaFiscal(filtro);

		filtro.setOrdenacaoColuna(OrdenacaoColuna.EMISSAO);
		viewNotaFiscalRepository.pesquisarNotaFiscal(filtro);

		filtro.setOrdenacaoColuna(OrdenacaoColuna.MOVIMENTO_INTEGRACAO);
		viewNotaFiscalRepository.pesquisarNotaFiscal(filtro);

		filtro.setOrdenacaoColuna(OrdenacaoColuna.NOTA);
		viewNotaFiscalRepository.pesquisarNotaFiscal(filtro);

		filtro.setOrdenacaoColuna(OrdenacaoColuna.SERIE);
		viewNotaFiscalRepository.pesquisarNotaFiscal(filtro);
		
		filtro.setOrdenacaoColuna(OrdenacaoColuna.STATUS_NFE);
		viewNotaFiscalRepository.pesquisarNotaFiscal(filtro);

		filtro.setOrdenacaoColuna(OrdenacaoColuna.TIPO_EMISSAO);
		viewNotaFiscalRepository.pesquisarNotaFiscal(filtro);

		filtro.setOrdenacaoColuna(OrdenacaoColuna.TIPO_NFE);
		viewNotaFiscalRepository.pesquisarNotaFiscal(filtro);
		
	}
	
	
	@Test
	public void pesquisarNotaFiscal_EntradaCota() {
		
		FiltroMonitorNfeDTO filtro = obterFiltroMonitorNfeDTO();
		filtro.setBox(box1.getCodigo());
		List<NfeDTO> lista = viewNotaFiscalRepository.pesquisarNotaFiscal(filtro);
		Assert.assertNotNull(lista);
		Assert.assertEquals(1, lista.size());
		NfeDTO notaNFE = lista.get(0);
		Assert.assertEquals("352.855.474-00", notaNFE.getCpfRemetente());
		
	}
	
	
	private FiltroMonitorNfeDTO obterFiltroMonitorNfeDTO() {
		
		FiltroMonitorNfeDTO filtro = new FiltroMonitorNfeDTO();
		
		PaginacaoVO paginacao = new PaginacaoVO();

		paginacao.setOrdenacao(PaginacaoVO.Ordenacao.ASC);
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(500);

		filtro.setPaginacao(paginacao);
	
		filtro.setOrdenacaoColuna(OrdenacaoColuna.EMISSAO);
		
		return filtro;
		
	}
	
}
