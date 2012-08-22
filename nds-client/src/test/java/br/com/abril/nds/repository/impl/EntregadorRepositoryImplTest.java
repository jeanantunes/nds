package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEntregadorDTO;
import br.com.abril.nds.dto.filtro.FiltroEntregadorDTO.OrdenacaoColunaEntregador;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoEntregador;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneEntregador;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.repository.EntregadorRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class EntregadorRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private EntregadorRepository entregadorRepository;
	private PessoaFisica maria;
	private PessoaJuridica acme;
	private PessoaJuridica fc;
	private PessoaFisica jose;
	
	private Entregador entregadorAcme;
	private Entregador entregadorFC;
	private Entregador entregadorJose;
	private Entregador entregadorMaria;
	
	@Before
	public void setup() {
		acme = Fixture.juridicaAcme();

		

		entregadorAcme = Fixture.criarEntregador(
				234L, true, new Date(), 
				BigDecimal.TEN, acme, false, null);
		
		save(acme, entregadorAcme);
		
		fc = Fixture.juridicaFC();
		
		entregadorFC = Fixture.criarEntregador(
				123L, false, new Date(), 
				null, fc, false, null);
		save(fc, entregadorFC);

		Endereco endereco = Fixture.criarEndereco(TipoEndereco.COBRANCA, "13131313", "Rua Marechal deodoro", "50", "Centro", "Mococa", "SP",1);
		
		EnderecoEntregador enderecoEntregador = Fixture.enderecoEntregador(entregadorFC, endereco, false, TipoEndereco.COMERCIAL);
		
		Telefone telefone = Fixture.telefone("19", "36560000", null);
		
		TelefoneEntregador telefoneEntregador = Fixture.telefoneEntregador(entregadorFC, false, telefone, TipoTelefone.COMERCIAL);

		save(endereco, enderecoEntregador, telefone, telefoneEntregador);

		jose = Fixture.pessoaFisica("3001258790", "jose@algumacoisa.com", "José");
		
		jose.setApelido("Zezinho");
		
		entregadorJose = Fixture.criarEntregador(
				345L, false, new Date(), 
				null, jose, false, null);
		save(jose, entregadorJose);
		
		endereco = Fixture.criarEndereco(TipoEndereco.COBRANCA, "8766650", "Avenida Brasil", "10", "Centro", "Ribeirão Preto", "SP",2);
		
		enderecoEntregador = Fixture.enderecoEntregador(entregadorJose, endereco, false, TipoEndereco.COBRANCA);
		
		telefone = Fixture.telefone("19", "36112887", null);
		
		telefoneEntregador = Fixture.telefoneEntregador(entregadorJose, false, telefone, TipoTelefone.CELULAR);

		save(endereco, enderecoEntregador, telefone, telefoneEntregador);

		maria = Fixture.pessoaFisica("4001258790", "maria@outracoisa.com", "Maria");
		
		maria.setApelido("Mariazinha");
		
		save(maria);
		
		entregadorMaria = Fixture.criarEntregador(
				456L, false, new Date(), 
				null, maria, false, null);

		endereco = Fixture.criarEndereco(TipoEndereco.COBRANCA, "8766650", "Itaquera", "10", "Centro", "São Paulo", "SP",5);
		
		enderecoEntregador = Fixture.enderecoEntregador(entregadorMaria, endereco, false, TipoEndereco.RESIDENCIAL);

		telefone = Fixture.telefone("11", "31053333", null);
		
		telefoneEntregador = Fixture.telefoneEntregador(entregadorMaria, false, telefone, TipoTelefone.CELULAR);

		save(entregadorMaria, endereco, enderecoEntregador, telefone, telefoneEntregador);
	}
	
	@Test
	public void obterEntregadoresSemFiltroSucesso() {

		FiltroEntregadorDTO filtroEntregador = new FiltroEntregadorDTO();

		filtroEntregador.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.CODIGO);

		List<Entregador> entregadores = 
				this.entregadorRepository.obterEntregadoresPorFiltro(filtroEntregador);
		
		Assert.assertNotNull(entregadores);
		
		int listSizeExpected = 4;
		int listSizeActual = entregadores.size();
		
		Assert.assertEquals(listSizeExpected, listSizeActual);
	}

	@Test
	public void obterEntregadoresComPaginacaoSemFiltroAscOrderSucesso() {

		FiltroEntregadorDTO filtroEntregador = new FiltroEntregadorDTO();

		PaginacaoVO paginacao = new PaginacaoVO();
		
		paginacao.setOrdenacao(Ordenacao.ASC);

		paginacao.setPaginaAtual(1);
		
		paginacao.setQtdResultadosPorPagina(3);

		//ORDER BY NOME/RAZAO SOCIAL
		filtroEntregador.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.NOME_RAZAO_SOCIAL);
		
		filtroEntregador.setPaginacao(paginacao);

		List<Entregador> entregadores = 
				this.entregadorRepository.obterEntregadoresPorFiltro(filtroEntregador);
		
		Assert.assertNotNull(entregadores);
		
		int listSizeExpected = paginacao.getQtdResultadosPorPagina();
		int listSizeActual = entregadores.size();
		
		Assert.assertEquals(listSizeExpected, listSizeActual);
		
		Assert.assertEquals(entregadores.get(0).getPessoa().getId(), acme.getId());
		Assert.assertEquals(entregadores.get(1).getPessoa().getId(), fc.getId());
		Assert.assertEquals(entregadores.get(2).getPessoa().getId(), jose.getId());

		//ORDER BY CODIGO
		filtroEntregador.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.CODIGO);
		
		filtroEntregador.setPaginacao(paginacao);

		entregadores = 
				this.entregadorRepository.obterEntregadoresPorFiltro(filtroEntregador);
		
		Assert.assertNotNull(entregadores);
		
		listSizeExpected = paginacao.getQtdResultadosPorPagina();
		listSizeActual = entregadores.size();
		
		Assert.assertEquals(listSizeExpected, listSizeActual);
		
		Assert.assertEquals(entregadores.get(0).getPessoa().getId(), fc.getId());
		Assert.assertEquals(entregadores.get(1).getPessoa().getId(), acme.getId());
		Assert.assertEquals(entregadores.get(2).getPessoa().getId(), jose.getId());

		//ORDER BY APELIDO/NOME FANTASIA
		filtroEntregador.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.APELIDO_NOME_FANTASIA);
		
		filtroEntregador.setPaginacao(paginacao);

		entregadores = 
				this.entregadorRepository.obterEntregadoresPorFiltro(filtroEntregador);
		
		Assert.assertNotNull(entregadores);
		
		listSizeExpected = paginacao.getQtdResultadosPorPagina();
		listSizeActual = entregadores.size();
		
		Assert.assertEquals(listSizeExpected, listSizeActual);
		
		Assert.assertEquals(entregadores.get(0).getPessoa().getId(), acme.getId());
		Assert.assertEquals(entregadores.get(1).getPessoa().getId(), fc.getId());
		Assert.assertEquals(entregadores.get(2).getPessoa().getId(), maria.getId());
		
		//ORDER BY CPF/CNPJ
		filtroEntregador.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.CPF_CNPJ);
		
		filtroEntregador.setPaginacao(paginacao);

		entregadores = 
				this.entregadorRepository.obterEntregadoresPorFiltro(filtroEntregador);

		Assert.assertNotNull(entregadores);

		listSizeExpected = paginacao.getQtdResultadosPorPagina();
		listSizeActual = entregadores.size();
		
		Assert.assertEquals(listSizeExpected, listSizeActual);
		
		Assert.assertEquals(entregadores.get(0).getPessoa().getId(), fc.getId());
		Assert.assertEquals(entregadores.get(1).getPessoa().getId(), acme.getId());
		Assert.assertEquals(entregadores.get(2).getPessoa().getId(), jose.getId());

		//ORDER BY EMAIL
		filtroEntregador.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.EMAIL);
		
		filtroEntregador.setPaginacao(paginacao);
	
		entregadores = 
				this.entregadorRepository.obterEntregadoresPorFiltro(filtroEntregador);
	
		Assert.assertNotNull(entregadores);
	
		listSizeExpected = paginacao.getQtdResultadosPorPagina();
		listSizeActual = entregadores.size();
		
		Assert.assertEquals(listSizeExpected, listSizeActual);
		
		Assert.assertEquals(entregadores.get(0).getPessoa().getId(), acme.getId());
		Assert.assertEquals(entregadores.get(1).getPessoa().getId(), fc.getId());
		Assert.assertEquals(entregadores.get(2).getPessoa().getId(), jose.getId());
		
//		//ORDER BY TELEFONE
//		filtroEntregador.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.TELEFONE);
//		
//		filtroEntregador.setPaginacao(paginacao);
//	
//		entregadores = 
//				this.entregadorRepository.obterEntregadoresPorFiltro(filtroEntregador);
//	
//		Assert.assertNotNull(entregadores);
//	
//		listSizeExpected = paginacao.getQtdResultadosPorPagina();
//		listSizeActual = entregadores.size();
//		
//		Assert.assertEquals(listSizeExpected, listSizeActual);
//		
//		Assert.assertEquals(entregadores.get(0).getPessoa().getId(), maria.getId());
//		Assert.assertEquals(entregadores.get(1).getPessoa().getId(), jose.getId());
//		Assert.assertEquals(entregadores.get(2).getPessoa().getId(), fc.getId());
	}
	
	@Test
	public void obterEntregadoresComPaginacaoSemFiltroDescOrderSucesso() {

		FiltroEntregadorDTO filtroEntregador = new FiltroEntregadorDTO();

		PaginacaoVO paginacao = new PaginacaoVO();

		paginacao.setOrdenacao(Ordenacao.DESC);

		paginacao.setPaginaAtual(1);

		paginacao.setQtdResultadosPorPagina(3);
		
		filtroEntregador.setOrdenacaoColunaEntregador(OrdenacaoColunaEntregador.NOME_RAZAO_SOCIAL);
		
		filtroEntregador.setPaginacao(paginacao);

		List<Entregador> entregadores = 
				this.entregadorRepository.obterEntregadoresPorFiltro(filtroEntregador);
		
		Assert.assertNotNull(entregadores);
		
		int listSizeExpected = paginacao.getQtdResultadosPorPagina();
		int listSizeActual = entregadores.size();
		
		Assert.assertEquals(listSizeExpected, listSizeActual);
		
		Assert.assertEquals(entregadores.get(0).getPessoa().getId(), maria.getId());
		Assert.assertEquals(entregadores.get(1).getPessoa().getId(), jose.getId());
		Assert.assertEquals(entregadores.get(2).getPessoa().getId(), fc.getId());
	}
	
	@Test
	@Ignore(value = "William, verificar este teste, falha qdo executado conjuntantamente com a suite de testes")
	public void obterEnderecosEntregadorFCSucesso() {
		
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = 
				this.entregadorRepository.obterEnderecosPorIdEntregador(fc.getId());
		
		Assert.assertNotNull(listaEnderecoAssociacao);
		
		Assert.assertFalse(listaEnderecoAssociacao.isEmpty());
		
		int expectedSizeList = 1;
		int actualSizeList = listaEnderecoAssociacao.size();
		
		Assert.assertEquals(expectedSizeList, actualSizeList);
		
		EnderecoAssociacaoDTO enderecoAssociacao = listaEnderecoAssociacao.get(0);
		
		boolean condition = enderecoAssociacao.getTipoEndereco() == TipoEndereco.COMERCIAL;
		
		Assert.assertTrue(condition);
	}
}
