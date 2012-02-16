package br.com.abril.nds.controllers.lancamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.DTO.FuroProdutoDTO;
import br.com.abril.nds.controllers.ItemAutoComplete;
import br.com.abril.nds.controllers.Util;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/lancamento/furoProduto")
public class FuroProdutoController {

	private Result result;
	
	public FuroProdutoController(Result result){
		this.result = result;
	}
	
	@Get
	@Path("/")
	public void index(){
		
	}
	
	@Post
	public void pesquisar(String codigo, String produto, String edicao, String dataLancamento){
		
		if (this.validarDadosEntrada(codigo, edicao, dataLancamento)){
		
			FuroProdutoDTO furoProdutoDTO = 
					new FuroProdutoDTO(
							1L,
							"produto", 
							(long)(Math.random() * 100), 
							(long)(Math.random() * 100), 
							new Date(),
							"capas/Auto_1.jpg");
			
			result.use(Results.json()).from(furoProdutoDTO, "result").serialize();
			
			result.forwardTo(FuroProdutoController.class).index();
		}
	}
	
	@Post
	public void pesquisarPorNomeProduto(String produto){
		
		List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
		listaProdutos.add(new ItemAutoComplete("Produto 1", new FuroProdutoDTO(1L, null, null, null, null, null)));
		listaProdutos.add(new ItemAutoComplete("Produto 2", new FuroProdutoDTO(8L, null, null, null, null, null)));
		
		//listaProdutos.add(new ItemAutoComplete("Produto 1 - 1", 1));
		//listaProdutos.add(new ItemAutoComplete("Produto 2 - 8", 1));
		
		result.use(Results.json()).from(listaProdutos, "result").include("value", "chave").serialize();
		
		result.forwardTo(FuroProdutoController.class).index();
	}
	
	private boolean validarDadosEntrada(String codigo, String edicao, String dataLancamento) {
		
		boolean valido = true;
		List<String> listaMensagemValidacao = new ArrayList<String>();
		
		if (codigo == null || codigo.isEmpty()){
			valido = false;
			listaMensagemValidacao.add("Código é obrigatório.");
		} else if (!Util.isValidNumber(codigo)){
			listaMensagemValidacao.add("Valor inválido informado para Código.");
		}
		
		if (edicao == null || edicao.isEmpty()){
			valido = false;
			listaMensagemValidacao.add("Edição é obrigatório.");
		} else if (!Util.isValidNumber(edicao)){
			listaMensagemValidacao.add("Valor inválido informado para Edição.");
		}
		
		if (dataLancamento == null || dataLancamento.isEmpty()){
			valido = false;
			listaMensagemValidacao.add("Data Lançamento é obrigatório.");
		} else if (!Util.isValidDate(dataLancamento, null)){
			listaMensagemValidacao.add("Valor inválido informado para Data Lançamento.");
		}
		
		if (!listaMensagemValidacao.isEmpty()){
			result.use(Results.json()).from(listaMensagemValidacao, "mensagens").serialize();
		}
		
		return valido;
	}

	@Post
	public void confirmarFuro(Long codigo, Long edicao, Date dataLancamento, Date novaData){
		System.out.println("Código: " + codigo);
		System.out.println("Edicao: " + edicao);
		System.out.println("dataLancamento: " + dataLancamento);
		System.out.println("novaData: " + novaData);
		
		result.forwardTo(FuroProdutoController.class).index();
	}
}
