package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.service.EntregadorService;
import br.com.abril.nds.service.FiadorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.PessoaService;
import br.com.abril.nds.service.TransportadorService;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/pessoa")
public class PessoaController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private FiadorService fiadorService;
	
	@Autowired
	private EntregadorService entregadorService;
	
	@Autowired
	private TransportadorService transportadorService;
	
	@Autowired
	private FornecedorService fornecedorService;

	@Path("/")
	public void index() {}

	@Post
	public void autoCompletarPorNome(String nomePessoa) {
		
		nomePessoa = PessoaUtil.removerSufixoDeTipo(nomePessoa);
		
		List<Pessoa> listaPessoa = pessoaService.obterPessoasPorNome(nomePessoa);
		
		processarDadosAutoComplete(listaPessoa,false);
	}
	
	@Post
	public void autoCompletarPorNomeFiador(String nomeFiador){
		
		nomeFiador = PessoaUtil.removerSufixoDeTipo(nomeFiador);
		
		List<Pessoa> listaPessoa = fiadorService.obterFiadorPorNome(nomeFiador);
		
		processarDadosAutoComplete(listaPessoa,false);
	}
	
	@Post
	public void autoCompletarPorNomeEntregador(String nomeEntregador){
		
		nomeEntregador = PessoaUtil.removerSufixoDeTipo(nomeEntregador);
		
		List<Pessoa> listaPessoa = entregadorService.obterEntregadorPorNome(nomeEntregador);
		
		processarDadosAutoComplete(listaPessoa,false);
	}
	
	@Post
	public void autoCompletarPorApelidoEntregador(String apelidoEntregador){
		
		apelidoEntregador = PessoaUtil.removerSufixoDeTipo(apelidoEntregador);
		
		List<Pessoa> listaPessoa = entregadorService.obterEntregadorPorApelido(apelidoEntregador);
		
		processarDadosAutoComplete(listaPessoa,true);
	}
	
	@Post
	public void autoCompletarPorNomeTransportador(String nomeTransportador){
		
		nomeTransportador = PessoaUtil.removerSufixoDeTipo(nomeTransportador);
		
		List<Pessoa> listaPessoa = transportadorService.obterTransportadorPorNome(nomeTransportador);
		
		processarDadosAutoComplete(listaPessoa,false);
	}
	
	@Post
	public void autoCompletarPorNomeFantasiaTransportador(String nomeFantasia){
		
		nomeFantasia = PessoaUtil.removerSufixoDeTipo(nomeFantasia);
		
		List<Pessoa> listaPessoa = transportadorService.obterTransportadorPorNomeFantasia(nomeFantasia);
		
		processarDadosAutoComplete(listaPessoa,true);
	}
	
	@Post
	public void autoCompletarPorNomeFornecedor(String nomeFornecedor){
		
		nomeFornecedor = PessoaUtil.removerSufixoDeTipo(nomeFornecedor);
		
		List<Pessoa> listaPessoa = fornecedorService.obterFornecedorPorNome(nomeFornecedor);
		
		processarDadosAutoComplete(listaPessoa,false);
	}
	
	@Post
	public void autoCompletarPorNomeFantasiaFornecedor(String nomeFantasia){
		
		nomeFantasia = PessoaUtil.removerSufixoDeTipo(nomeFantasia);
		
		List<Pessoa> listaPessoa = fornecedorService.obterFornecedorPorNomeFantasia(nomeFantasia);
		
		processarDadosAutoComplete(listaPessoa,true);
	}
	

	private void processarDadosAutoComplete(List<Pessoa> listaPessoa, boolean isApelido) {
		
		List<ItemAutoComplete> listaCotasAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (listaPessoa != null && !listaPessoa.isEmpty()) {
			
			for (Pessoa pessoa : listaPessoa) {
				
				String nomeExibicao = (!isApelido)
						? PessoaUtil.obterNomeExibicaoPeloTipo(pessoa)
								:PessoaUtil.obterApelidoExibicaoPeloTipo(pessoa);
					
				listaCotasAutoComplete.add(new ItemAutoComplete(nomeExibicao, null, pessoa.getId()));
			}
		}
		
		this.result.use(Results.json()).from(listaCotasAutoComplete, "result").include("value", "chave").serialize();
	}
}
