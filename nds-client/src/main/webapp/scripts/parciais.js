function Parciais(pathTela) {
	
	var T = this;
	
	/**
	 * Ação de clique do botão pesquisar
	 */
	this.cliquePesquisar = function() {
		
		$(".parciaisGrid").flexOptions({			
			url : pathTela + "/parciais/pesquisarParciais",
			dataType : 'json',
			preProcess: T.processaRetornoPesquisaParciais,
			params:T.getDados()
		});
		
		$(".parciaisGrid").flexReload();
	},
	
	this.processaRetornoPesquisaParciais = function(result) {
		
		if(result.mensagens) 
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
					
		$.each(result.rows, function(index,row){T.gerarAcao(index,row);} );
				
		return result;
	},
	
	/**
	 * Retorna todos os dados da tela principal no padrão utilizado pelo VRaptor
	 * @return Espelho de FiltroParciaisDTO (br.com.abril.nds.dto) 
	 */
	this.getDados = function() {
	
		var data = [];
		
		data.push({name:'filtro.codigoProduto',		value: T.get("codigoProduto")});
		data.push({name:'filtro.nomeProduto',		value: T.get("nomeProduto")});
		data.push({name:'filtro.edicaoProduto',		value: T.get("edicaoProduto")});
		data.push({name:'filtro.idFornecedor',		value: T.get("idFornecedor")});
		data.push({name:'filtro.dataInicial',		value: T.get("dataInicial")});
		data.push({name:'filtro.dataFinal',			value: T.get("dataFinal")});
		data.push({name:'filtro.status',			value: T.get("status")});
		
		return data;
	},
	
	this.gerarAcaoDetalhes = function(index,row) {
		row.cell.acao = 
			'<a href="javascript:;" onclick="popup_detalhes('+ row.cell.idProdutoEdicao +');"> ' +
			'<img src="'+pathTela+'/images/ico_detalhes.png" border="0" /></a>';
	},
	
	/**
	 * Atribui valor a um campo da tela
	 * Obs: Checkboxs devem ser atribuidos com o valor de true ou false
	 * 
	 * @param campo - Campo a ser alterado
	 * @param value - valor
	 */
	this.set = function(campo,value) {
				
		var elemento = $("#" + campo);
		
		if(elemento.attr('type') == 'checkbox') {
			
			if(value) {
				elemento.attr('checked','checked');
			} else {
				elemento.removeAttr('checked');
			}
						
		} else {
			elemento.val(value);
		}
	},
	
	/**
	 * Obtém valor de elemento da tela
	 * @param campo - de onde o valor será obtido
	 */
	this.get = function(campo) {
		
		var elemento = $("#" + campo);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	};

	
	$(function() {
		$("#dataInicial").mask("99/99/9999");
		$("#dataFinal").mask("99/99/9999");
		
		$("#edicaoProduto").numeric();
	});
}