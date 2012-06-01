function MapaAbastecimento(pathTela, objName) {
	
	var T = this;
	
	this.mapas = [];
	
	this.pesquisar = function() {
		

		$(".mapaAbastecimentoGrid").flexOptions({			
			url : pathTela + "/mapaAbastecimento/pesquisar",
			dataType : 'json',
			preProcess: T.processaRetornoPesquisa,
			params:T.getDadosFiltro()
		});
		
		$(".mapaAbastecimentoGrid").flexReload();
	},
	
	this.pesquisarDetalhes = function() {
		

		$(".mapaAbastecimentoDetalheGrid").flexOptions({			
			url : pathTela + "/mapaAbastecimento/pesquisarDetalhes",
			dataType : 'json',			
			params:T.getDadosFiltro()
		});
		
		$(".mapaAbastecimentoGrid").flexReload();
	},
	
	
	this.processaRetornoPesquisa = function(result) {
		debugger;
		if(result.messages)
			exibirMensagem(result.messages.tipoMensagem,result.messages.listaMensagens);
		
		T.mapas = [];
		
		$.each(result.rows, function(index,row){T.processarLinha(index,row.cell);} );
		
		return result;
	},
	

	this.processarLinha = function(index,cell) {
		
		T.mapas.push(cell);
		
		cell.acao =	'<a href="javascript:;" onclick="' + objName +'.carregarDetalhes(\''+ index + '\')">' +
					'<img src="' + pathTela + '/images/ico_detalhes.png" alt="Detalhes do box" border="0" /></a>';
	},
	
	this.carregarDetalhes = function() {
		popup_detalhe_box();
	},
	
	this.mudarTipoPesquisa = function(tipo) {
		
		switch (tipo) {
			
		case 'BOX':
			T.bloquearCampos('rota','codigoProduto','nomeProduto','edicao','codigoCota','nomeCota','quebraPorCota');
			T.desbloquearCampos('box');
			break;
		case 'ROTA':
			T.bloquearCampos('codigoProduto','nomeProduto','edicao','codigoCota','nomeCota','quebraPorCota');
			T.desbloquearCampos('box','rota');
			break;
		case 'COTA':
			T.bloquearCampos('box','rota','codigoProduto','nomeProduto','edicao','quebraPorCota');
			T.desbloquearCampos('codigoCota','nomeCota');
			break;
		case 'PRODUTO':
			T.bloquearCampos('box','rota','codigoCota','nomeCota');
			T.desbloquearCampos('codigoProduto','nomeProduto','edicao','quebraPorCota');
			break;			
		default:
			T.bloquearCampos('box','rota','codigoProduto','nomeProduto','edicao','codigoCota','nomeCota','quebraPorCota');
			break;
		}
	},	
	
	this.bloquearCampos = function() {
		for(var campo in arguments) {
			$('#' + arguments[campo]).disable();
		}
	},
	
	this.desbloquearCampos = function() {
		for(var campo in arguments) {
			$('#' + arguments[campo]).enable();
		}
	},
	
	this.getDadosFiltro = function() {
		
		var data = [];
		
		data.push({name:'filtro.dataLancamento',	value: T.get("dataLancamento")});
		data.push({name:'filtro.tipoConsulta',		value: T.get("tipoConsulta")});
		data.push({name:'filtro.box',				value: T.get("box")});
		data.push({name:'filtro.rota',				value: T.get("rota")});
		data.push({name:'filtro.codigoProduto',	value: T.get("codigoProduto")});
		data.push({name:'filtro.nomeProduto',		value: T.get("nomeProduto")});
		data.push({name:'filtro.edicao',			value: T.get("edicao")});
		data.push({name:'filtro.codigoCota',		value: T.get("codigoCota")});
		data.push({name:'filtro.nomeCota',			value: T.get("nomeCota")});
		data.push({name:'filtro.quebraPorCota',	value: T.get("quebraPorCota")});		
		
		return data;
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
	
	/**
	 * Execuçãos ao carregar Obj
	 */
	$(function() {
		
		//$("#codigo").numeric();
		
		$("#dataLancamento").mask("99/99/9999");
		
		$( "#dataLancamento" ).datepicker({
			showOn: "button",
			buttonImage: pathTela + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});

	});
}