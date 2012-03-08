package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.RecebimentoFisicoService;

public class RecebimentoFisicoRepositoryImplTest extends AbstractRepositoryImplTest{
		
	
	@Autowired
	private RecebimentoFisicoService recebimentoFisicoService;
	
	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
	Fornecedor dinap = new Fornecedor();
	PessoaJuridica pj = new PessoaJuridica();
	CFOP cfop = new CFOP();
	TipoNotaFiscal tipoNotaFiscal = new TipoNotaFiscal();
	Fornecedor fornecedor = new Fornecedor();
	Usuario usuario = new Usuario();
	NotaFiscalFornecedor notaFiscal = new NotaFiscalFornecedor();
	TipoProduto tipoProduto = new TipoProduto();
	Produto produto = new Produto();
	ProdutoEdicao produtoEdicao = new ProdutoEdicao();
	
	Long idNotaFiscal;
	
	@Before
	public void setup() {
		Fornecedor dinap = Fixture.fornecedorDinap();
		getSession().save(dinap);
		
		pj = Fixture.juridicaDinap();		
		getSession().save(pj);
		
		cfop = Fixture.cfop5102();
		//slavei o CFOP
		getSession().save(cfop);
		
		
		tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		//Salvei Tipo
		getSession().save(tipoNotaFiscal);
				
		fornecedor = Fixture.fornecedorAcme();
		getSession().save(fornecedor);
		
		usuario = Fixture.usuarioJoao();
		getSession().save(usuario);
		
		notaFiscal = Fixture.notaFiscalFornecedor(cfop, pj, fornecedor, tipoNotaFiscal, usuario, new BigDecimal(10),  new BigDecimal(10),  new BigDecimal(10));
		getSession().save(notaFiscal);		
				
		tipoProduto = Fixture.tipoProduto("Revista", GrupoProduto.REVISTA, "99000642");
		getSession().save(tipoProduto);
		
		produto = Fixture.produto("1", "Revista Veja", "Veja", PeriodicidadeProduto.SEMANAL, tipoProduto);
		produto.addFornecedor(dinap);
		getSession().save(produto);
		
		produtoEdicao =
				Fixture.produtoEdicao(1L, 10, 14, new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), produto);
		produtoEdicao.setId(1L);
		getSession().save(produtoEdicao);	
	}
	
	
	@Test
	@Ignore
	public void inserirItemNotaComDTOComNota() {
		
		
		RecebimentoFisicoDTO recebimentoDTO = new RecebimentoFisicoDTO();
		
		recebimentoDTO.setIdProdutoEdicao(1L);
		recebimentoDTO.setQtdFisico(new BigDecimal(50));
		recebimentoDTO.setDataLancamento(new Date(System.currentTimeMillis()));
		recebimentoDTO.setDataRecolhimento(new Date(System.currentTimeMillis()));
		recebimentoDTO.setRepartePrevisto(new BigDecimal(12));
		recebimentoDTO.setTipoLancamento(TipoLancamento.LANCAMENTO);
	
		List<RecebimentoFisicoDTO> listaDTO = new ArrayList<RecebimentoFisicoDTO>();
		listaDTO.add(recebimentoDTO);
		
		NotaFiscal notaFiscalFromBD = notaFiscalRepository.buscarPorId(notaFiscal.getId());
		
		
		recebimentoFisicoService.inserirDadosRecebimentoFisico(usuario,notaFiscalFromBD, listaDTO, new Date());
		
		
		
	}
	
	@Test	
	public void inserirItemNotaSemIdNota() {
		
		
		RecebimentoFisicoDTO recebimentoDTO = new RecebimentoFisicoDTO();
		
		recebimentoDTO.setIdProdutoEdicao(1L);
		recebimentoDTO.setQtdFisico(new BigDecimal(50));
		recebimentoDTO.setDataLancamento(new Date(System.currentTimeMillis()));
		recebimentoDTO.setDataRecolhimento(new Date(System.currentTimeMillis()));
		recebimentoDTO.setRepartePrevisto(new BigDecimal(12));
		recebimentoDTO.setTipoLancamento(TipoLancamento.LANCAMENTO);
	
		List<RecebimentoFisicoDTO> listaDTO = new ArrayList<RecebimentoFisicoDTO>();
		listaDTO.add(recebimentoDTO);
		
		//NotaFiscal notaFiscalFromBD = notaFiscalRepository.buscarPorId(notaFiscal.getId());
		NotaFiscal notaFiscalF = new NotaFiscalFornecedor();
		notaFiscalF = Fixture.notaFiscalFornecedor(cfop, pj, fornecedor, tipoNotaFiscal, usuario, new BigDecimal(10),  new BigDecimal(10),  new BigDecimal(10));
		
		
		recebimentoFisicoService.inserirDadosRecebimentoFisico(usuario,notaFiscalF, listaDTO, new Date());
		
		
		
	}
		
}
