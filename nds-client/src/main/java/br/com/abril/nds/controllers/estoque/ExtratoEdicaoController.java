package br.com.abril.nds.controllers.estoque;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.fiscal.OrigemNota;
import br.com.abril.nds.model.fiscal.StatusNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.movimentacao.MovimentoEstoque;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.movimentacao.TipoMovimentoEstoque;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModelKeyValue;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/estoque/extratoEdicao")
public class ExtratoEdicaoController {
	
	private Result result;
	
	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	public ExtratoEdicaoController(Result result) {
		this.result = result;
	}
	
	public void index(){
		
	}
	
	
	private List<MovimentoEstoque> getFromBDTeste() {
	
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
		List<MovimentoEstoque> listaModeloGenerico = new LinkedList<MovimentoEstoque>();
		long contador = 0;

		while(contador++<10) {
			
			Usuario usuario = new Usuario();
			usuario.setId(contador);
			usuario.setNome("nome_"+contador);
			
			Diferenca d = new Diferenca();
			d.setId(contador);
			d.setResponsavel(usuario);
			
			MovimentoEstoque m = new MovimentoEstoque();
			m.setId(contador);
			m.setDataInclusao(new Date());
			m.setDiferenca(d);
			listaModeloGenerico.add(m);
		}
		
		return listaModeloGenerico;
		
	}
	
	
	private void massaDadosParaTeste() {
		
		CFOP cfop = new CFOP();
		cfop.setCodigo("010101");
		cfop.setDescricao("default");
		cfop.setId(1L);
		
		TipoMovimento tipoMovimento = new TipoMovimento();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("ssddgggg");
		tipoMovimento.setId(1L);
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setTipoMovimentoEstoque(TipoMovimentoEstoque.FALTA_EM);
		
		
		ItemNotaFiscal itemNotaFiscal = new ItemNotaFiscal();
		itemNotaFiscal.setId(1L);
		itemNotaFiscal.setOrigemNota(OrigemNota.MANUAL);
		//itemNotaFiscal.setProdutoEdicao(produtoEdicao);
		itemNotaFiscal.setQuantidade(new BigDecimal(1.0));
		//itemNotaFiscal.setUsuario(usuario);
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();

		pessoaJuridica.setId(1L);
		pessoaJuridica.setCnpj("654644464");
		pessoaJuridica.setEmail("aaa@yahoo.com");
		pessoaJuridica.setInscricaoEstadual("3333333");
		pessoaJuridica.setNomeFantasia("NOME FANTASIA");
		pessoaJuridica.setRazaoSocial("RAZAO SOCIAL");

		TipoNotaFiscal tipoNotaFiscal = new TipoNotaFiscal();
		tipoNotaFiscal.setDescricao("TIPO NOTA");
		tipoNotaFiscal.setId(1L);
		tipoNotaFiscal.setTipoOperacao(TipoOperacao.ENTRADA);
		
		NotaFiscalFornecedor notaFiscalFornecedor = new NotaFiscalFornecedor();
		
		notaFiscalFornecedor.setId(1L);
		notaFiscalFornecedor.setCfop(cfop);
		notaFiscalFornecedor.setChaveAcesso("11111");
		notaFiscalFornecedor.setDataEmissao(new Date());
		notaFiscalFornecedor.setDataExpedicao(new Date());
		notaFiscalFornecedor.setJuridica(pessoaJuridica);
		notaFiscalFornecedor.setNumero("2344242");
		notaFiscalFornecedor.setOrigemNota(OrigemNota.INTERFACE);
		notaFiscalFornecedor.setSerie("345353543");
		notaFiscalFornecedor.setStatusNotaFiscal(StatusNotaFiscal.PENDENTE);
		notaFiscalFornecedor.setTipoNotaFiscal(tipoNotaFiscal);
		//notaFiscalFornecedor.setUsuario(usuario);
		
		RecebimentoFisico recebimentoFisico = new RecebimentoFisico();
		recebimentoFisico.setData(new Date());
		recebimentoFisico.setDataConfirmacao(new Date());
		recebimentoFisico.setId(1L);
		recebimentoFisico.setNotaFiscal(notaFiscalFornecedor);
		recebimentoFisico.setStatusConfirmacao(StatusConfirmacao.CONFIRMADO);
		//recebimentoFisico.setUsuario(usuario);
		
		ItemRecebimentoFisico itemRecebimentoFisico = new ItemRecebimentoFisico();
		itemRecebimentoFisico.setId(1L);
		itemRecebimentoFisico.setItemNotaFiscal(itemNotaFiscal);
		itemRecebimentoFisico.setQtdeFisico(new BigDecimal(1.0));
		itemRecebimentoFisico.setRecebimentoFisico(recebimentoFisico);
		
		MovimentoEstoque movimentoEstoque = new MovimentoEstoque();

		movimentoEstoque.setId(1L);
		
		movimentoEstoque.setDataInclusao(new Date());
		movimentoEstoque.setDiferenca(null);
		movimentoEstoque.setItemRecebimentoFisico(itemRecebimentoFisico);
		//movimentoEstoque.setProdutoEdicao(produtoEdicao);
		movimentoEstoque.setQtde(new BigDecimal(1.0));
		movimentoEstoque.setTipoMovimento(tipoMovimento);
		//movimentoEstoque.setUsuario(usuario);
		
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void pesquisaExtratoEdicao() throws Exception {
		
		List<CellModelKeyValue<MovimentoEstoque>> listaModeloGenerico = new LinkedList<CellModelKeyValue<MovimentoEstoque>>();
		
		
		for(MovimentoEstoque movimento :  getFromBDTeste()) {
			listaModeloGenerico.add(new CellModelKeyValue<MovimentoEstoque>(0, movimento));
		}
		
		TableModelKeyValue<CellModelKeyValue<MovimentoEstoque>> tm = new TableModelKeyValue<CellModelKeyValue<MovimentoEstoque>>();
		
		tm.setPage(1);
		
		tm.setTotal(listaModeloGenerico.size());
		
		tm.setRows(listaModeloGenerico);
		
		result.use(Results.json()).withoutRoot().from(tm).include("rows").exclude("diferenca").serialize();
		
	}
	
	
}
