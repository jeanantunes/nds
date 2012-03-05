package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ItemNotaFiscalRepository;
import br.com.abril.nds.service.RecebimentoFisicoService;

public class ItemNotaFiscalRecebimentoFisicoRepTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private ItemNotaFiscalRepository itemNotaRepository;
	
	@Autowired
	private RecebimentoFisicoService recebimentoFisicoService;
	
	String cnpj = "00.000.000/0001-00";
	String chave = "11111";
	String numeroNota = "2344242";
	String serie = "345353543";
	
	@Before
	public void setup() {
		
		
		PessoaJuridica pj = Fixture.juridicaFC();
		//salvei a pessoa Juridica
		getSession().save(pj);
		
		CFOP cfop =new CFOP();
		cfop.setCodigo("1");
		cfop.setDescricao("cfop desc");
		cfop.setId(1L);
		//slavei o CFOP
		getSession().save(cfop);
		
		NotaFiscalFornecedor notaFiscal = new NotaFiscalFornecedor();
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		//Salvei Tipo
		getSession().save(tipoNotaFiscal);
		notaFiscal = Fixture.notaFiscalFornecedor(cfop, pj, Fixture.fornecedorFC(), tipoNotaFiscal, Fixture.usuarioJoao());
	
	
		
		notaFiscal.setTipoNotaFiscal(tipoNotaFiscal);
		notaFiscal.setId(5L);
		//Salvei a Nota Fiscal
		getSession().save(notaFiscal);		
		
		
		Usuario usuario = Fixture.usuarioJoao();
		usuario.setId(4L);
		//salvei o usuario
		getSession().save(usuario);
		
							
		TipoProduto tipoProduto = Fixture.tipoProduto("Revista", GrupoProduto.REVISTA, "99000642");
		getSession().save(tipoProduto);
		
		Produto produto = Fixture.produto("1", "Revista Veja", "Veja", PeriodicidadeProduto.SEMANAL, tipoProduto);
		getSession().save(produto);
		
		ProdutoEdicao produtoEdicao =
				Fixture.produtoEdicao(1L, 10, 14, new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), produto);
		produtoEdicao.setId(1L);
		getSession().save(produtoEdicao);	
	
		
		
		
	}
	
	
	
}
