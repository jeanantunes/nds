package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.RecebimentoFisicoService;

@Ignore
public class ItemNotaFiscalRecebimentoFisicoRepTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private ItemNotaFiscalRepositoryImpl itemNotaRepository;
	
	@Autowired
	private RecebimentoFisicoService recebimentoFisicoService;
	
	String cnpj = "00.000.000/0001-00";
	String chave = "11111";
	String numeroNota = "2344242";
	String serie = "345353543";
	
	@Before
	public void setup() {
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		
		Fornecedor dinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(dinap);
		
		PessoaJuridica pj = Fixture.juridicaFC();
		//salvei a pessoa Juridica
		save(pj);
		
		CFOP cfop =new CFOP();
		cfop.setCodigo("1");
		cfop.setDescricao("cfop desc");
		cfop.setId(1L);
		//slavei o CFOP
		save(cfop);
		
		NotaFiscalFornecedor notaFiscal = new NotaFiscalFornecedor();
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		//Salvei Tipo
		save(tipoNotaFiscal);
		//notaFiscal = Fixture.notaFiscalFornecedor(cfop, pj, Fixture.fornecedorFC(), tipoNotaFiscal, Fixture.usuarioJoao());
	
	
		
		notaFiscal.setTipoNotaFiscal(tipoNotaFiscal);
		//Salvei a Nota Fiscal
		save(notaFiscal);		
		
		
		Usuario usuario = Fixture.usuarioJoao();
		usuario.setId(4L);
		//salvei o usuario
		save(usuario);
		
							
		TipoProduto tipoProduto = Fixture.tipoProduto("Revista", GrupoProduto.REVISTA, "99000642");
		save(tipoProduto);
		
		Produto produto = Fixture.produto("1", "Revista Veja", "Veja", PeriodicidadeProduto.SEMANAL, tipoProduto);
		produto.addFornecedor(dinap);
		save(produto);
		
		ProdutoEdicao produtoEdicao =
				Fixture.produtoEdicao(1L, 10, 14, new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), produto);
		produtoEdicao.setId(1L);
		save(produtoEdicao);	
	}
		
	@Test
	public void inserirItemNotaComDTO() {
	
		
		/*//teste para analisar um item de Nota sem ItemRecebimento e sem RecebimentoFisico
		ItemNotaRecebimentoFisicoDTO dto = new ItemNotaRecebimentoFisicoDTO(
				Fixture.criarData(12, 12, 1983), 
				Fixture.criarData(12, 03, 1983),  
				new BigDecimal(0.1),
				TipoLancamento.LANCAMENTO,
				1L,null,null,1L,1L);
		*/
		
		NotaFiscal notaFiscal= new NotaFiscalFornecedor();
		//List<ItemNotaRecebimentoFisicoDTO> listaItensNota = new ArrayList<ItemNotaRecebimentoFisicoDTO>();
		
		
		//recebimentoFisicoService.inserirDadosRecebimentoFisico(notaFiscal, listaItensNota);
		System.out.println("");
		
		
	}
	
}
