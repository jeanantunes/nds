package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
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
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;


public class ItemNotaFiscalRecebimentoFisicoRepTest extends AbstractRepositoryImplTest{
	
	ItemRecebimentoFisico itemRecebimento = new ItemRecebimentoFisico();
	Usuario usuario = new Usuario();
	TipoProduto tipoProduto = new TipoProduto();
	TipoFornecedor tipoFornecedorPublicacao = new TipoFornecedor();
	TipoNotaFiscal tipoNotaFiscal = new TipoNotaFiscal();
	ItemNotaFiscalEntrada itemNotaFiscal = new ItemNotaFiscalEntrada();
	NotaFiscalEntradaFornecedor notaFiscal = new NotaFiscalEntradaFornecedor();
	RecebimentoFisico recebimentoFisico = new RecebimentoFisico();
	
	Date dataAtual = new Date();
		
	@Autowired
	private ItemRecebimentoFisicoRepository itemRecebimentoFisicoRepository;
	
	String cnpj = "00.000.000/0001-00";
	String chave = "11111";
	String numeroNota = "2344242";
	String serie = "345353543";
	
	@Before
	public void setup() {
		
				
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		TipoProduto tipoProduto = Fixture.tipoProduto("Revista", GrupoProduto.REVISTA, "99000642");
		save(tipoProduto);
		
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		
		Fornecedor dinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(dinap);
		
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		Produto produto = Fixture.produto("1", "Revista Veja", "Veja", PeriodicidadeProduto.SEMANAL, tipoProduto);
		produto.addFornecedor(dinap);
		produto.setEditor(abril);
		save(produto);
		

		ProdutoEdicao produtoEdicao =
				Fixture.produtoEdicao(1L, 10, 14, new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), produto);		
		save(produtoEdicao);

		CFOP cfop = Fixture.cfop5102();
		save(cfop);

		
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();		
		save(tipoNotaFiscal);
		
		
		PessoaJuridica emitente= Fixture.juridicaDinap(); 
		save(emitente);
		
				
		NotaFiscalEntradaFornecedor notaFiscal = new NotaFiscalEntradaFornecedor();	
		notaFiscal = Fixture.notaFiscalEntradaFornecedor(cfop, emitente, dinap, tipoNotaFiscal, usuario, new BigDecimal(12), new BigDecimal(33), new BigDecimal(11));
		notaFiscal.setTipoNotaFiscal(tipoNotaFiscal);	
		save(notaFiscal);
		
			
		
		itemNotaFiscal = Fixture.itemNotaFiscal(produtoEdicao, usuario, notaFiscal, dataAtual, dataAtual, TipoLancamento.LANCAMENTO, new BigDecimal(23));
		save(itemNotaFiscal);
		
		recebimentoFisico = Fixture.recebimentoFisico(notaFiscal, usuario, dataAtual, dataAtual, StatusConfirmacao.PENDENTE);
		save(recebimentoFisico);
	}
		
	@Test
	public void inserirItemNotaRecebimento() {					
		
		
		ItemRecebimentoFisico itemRecebimento = new ItemRecebimentoFisico();
		itemRecebimento.setItemNotaFiscal(itemNotaFiscal);
		itemRecebimento.setQtdeFisico(new BigDecimal(12));
		
		itemRecebimento.setRecebimentoFisico(recebimentoFisico);
		
		itemRecebimentoFisicoRepository.adicionar(itemRecebimento);		
		
		
	}
	
}
