function Parciais(pathTela) {
	
	var T = this;
	
	/**
	 * Ação de clique do botão pesquisar
	 */
	this.cliquePesquisar = function() {
		
		if(T.get('codigoProduto').length!=0 && T.get('edicaoProduto').length!=0) {
			$('#painelLancamentos').hide();
			$('#painelPeriodos').show();			
			T.pesquisarLancamentosParciais();
		} else {
			$('#painelPeriodos').hide();
			$('#painelLancamentos').show();
			T.pesquisarPeriodosParciais();
		}		
	},
	
	this.pesquisarLancamentosParciais = function() {
		
		$(".parciaisGrid").flexOptions({			
			url : pathTela + "/parciais/pesquisarParciais",
			dataType : 'json',
			preProcess: T.processaRetornoPesquisaParciais,
			params:T.getDados()
		});
		
		$(".parciaisGrid").flexReload();
	},
	
	this.pesquisarPeriodosParciais = function() {
		
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
		
		if(result.rows.length==0) {
			$("#exportacao").hide();
		} else {
			$("#exportacao").show();
		}
		
		$.each(result.rows, function(index,row){T.gerarAcaoDetalhes(index,row);} );
				
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
		
		data.push({name:'filtro.nomeFornecedor',	value: $('#idFornecedor option:selected').text()});
		
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