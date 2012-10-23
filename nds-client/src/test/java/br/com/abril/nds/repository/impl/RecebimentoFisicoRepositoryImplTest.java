package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.NotaFiscalEntradaRepository;
import br.com.abril.nds.service.RecebimentoFisicoService;

public class RecebimentoFisicoRepositoryImplTest extends AbstractRepositoryImplTest{
		
	
	@Autowired
	private RecebimentoFisicoService recebimentoFisicoService;
	
	@Autowired
	private NotaFiscalEntradaRepository notaFiscalRepository;
	
	Fornecedor dinap = new Fornecedor();
	PessoaJuridica pj = new PessoaJuridica();
	CFOP cfop = new CFOP();
	TipoNotaFiscal tipoNotaFiscal = new TipoNotaFiscal();
	Fornecedor fornecedor = new Fornecedor();
	Usuario usuario = new Usuario();
	NotaFiscalEntradaFornecedor notaFiscal = new NotaFiscalEntradaFornecedor();
	TipoProduto tipoProduto = new TipoProduto();
	Produto produto = new Produto();
	ProdutoEdicao produtoEdicao = new ProdutoEdicao();
	RecebimentoFisico recebimentoFisico = new RecebimentoFisico();
	ItemNotaFiscalEntrada itemNotaFiscal = new ItemNotaFiscalEntrada();
	ItemRecebimentoFisico itemRecebimentoFisico = new ItemRecebimentoFisico();
	
	Long idNotaFiscal;
	
	@Before
	public void setup() {
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		
		Fornecedor dinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(dinap);
		
		cfop = Fixture.cfop5102();
		save(cfop);
		
		
		tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		save(tipoNotaFiscal);
				
		fornecedor = Fixture.fornecedorAcme(tipoFornecedorPublicacao);
		save(fornecedor);
		
		usuario = Fixture.usuarioJoao();
		save(usuario);
		
		notaFiscal = Fixture.notaFiscalEntradaFornecedor(cfop, fornecedor, tipoNotaFiscal, usuario, new BigDecimal(10),  new BigDecimal(10),  new BigDecimal(10));
		notaFiscal.setOrigem(Origem.MANUAL);
		save(notaFiscal);		
				
		NCM ncmRevistas = Fixture.ncm(99000642l,"REVISTAS","KG");
		save(ncmRevistas);
		
		tipoProduto = Fixture.tipoProduto("Revista", GrupoProduto.REVISTA, ncmRevistas, null, 13L);
		save(tipoProduto);
		
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		produto = Fixture.produto("1", "Revista Veja", "Veja", PeriodicidadeProduto.SEMANAL, tipoProduto, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		produto.addFornecedor(dinap);
		produto.setEditor(abril);
		save(produto);
		
		produtoEdicao =
				Fixture.produtoEdicao(1L, 10, 14, new Long(100), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQ", produto, null, false);
		save(produtoEdicao);
		
		itemNotaFiscal= 
				Fixture.itemNotaFiscal(
						produtoEdicao, 
						usuario, 
						notaFiscal, 
						new Date(), 
						new Date(),
						TipoLancamento.LANCAMENTO,
						BigInteger.valueOf(12));
		save(itemNotaFiscal);
		
		recebimentoFisico = Fixture.recebimentoFisico(notaFiscal, usuario, new Date(), new Date(), StatusConfirmacao.PENDENTE);
		save(recebimentoFisico);
		
		itemRecebimentoFisico= Fixture.itemRecebimentoFisico(itemNotaFiscal, recebimentoFisico, BigInteger.valueOf(12));
		save(itemRecebimentoFisico);
	}
		
	@Test
	public void inserirDadosRecebimentoFisico(){
		
		RecebimentoFisicoDTO recebimentoDTO = new RecebimentoFisicoDTO();
		
		recebimentoDTO.setIdProdutoEdicao(produtoEdicao.getId());
		recebimentoDTO.setQtdFisico(BigInteger.valueOf(50));
		recebimentoDTO.setDataLancamento(new Date(System.currentTimeMillis()));
		recebimentoDTO.setDataRecolhimento(new Date(System.currentTimeMillis()));
		recebimentoDTO.setRepartePrevisto(BigInteger.valueOf(12));
		recebimentoDTO.setTipoLancamento(TipoLancamento.LANCAMENTO);
	
		List<RecebimentoFisicoDTO> listaDTO = new ArrayList<RecebimentoFisicoDTO>();
		
		listaDTO.add(recebimentoDTO);
		
		NotaFiscalEntrada notaFiscalFromBD = notaFiscalRepository.buscarPorId(notaFiscal.getId());
		
		recebimentoFisicoService.inserirDadosRecebimentoFisico(usuario,notaFiscalFromBD, listaDTO, new Date());
		
	}
	
	@Test
	public void cancelamentoDeDadosRecebimentoFisico(){
		recebimentoFisicoService.cancelarNotaFiscal(notaFiscal.getId());
		
	}
		
}
