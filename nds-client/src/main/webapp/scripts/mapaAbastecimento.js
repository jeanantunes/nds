function MapaAbastecimento(pathTela, objName, workspace) {
	
	var _workspace = workspace;
	
	var T = this;
	
	this.mapas = [];
	this.produtosSelecionados = [],
	
	//Pesquisa por código de produto
	this.pesquisarPorCodigoProduto = function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		
		var codigoProduto = $(idCodigo, _workspace).val();
		
		codigoProduto = $.trim(codigoProduto);
		
		$(idCodigo, _workspace).val(codigoProduto);
		
		if (codigoProduto && codigoProduto.length > 0) {
			
			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
					   "codigoProduto=" + codigoProduto,
					   function(result) {
							
							T.adicionarProduto(result.codigo, result.descricao);
				
							T.mostrarProdutosSelecionados();
							
							$(idCodigo, _workspace).val("");
					   },
					   function() {
						   
						   $(idCodigo, _workspace).val("");
					   }
			);
		}
	},
	
	this.carregarProdutos = function() {
		
		$("#selectProdutos", _workspace).val("");
		
		$("#dialog-pesq-produtos").dialog({
			resizable: false,
			height:300,
			width:500,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					T.getProdutosSelecionados();
					
					T.mostrarProdutosSelecionados();

					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-pesq-produtos", _workspace).parents("form")
		});
	},
	
	this.limparProdutosSelecionados = function() {
		
		T.produtosSelecionados = [];
		
		T.mostrarProdutosSelecionados();
	},
	
	this.getProdutosSelecionados = function() {
		
		$.each( $('#selectProdutos', this.workspace).val(), function(index, row) {
			
			T.adicionarProduto(row.split('_')[0], row.split('_')[1]);
		});
	},
	
	this.adicionarProduto = function(id, nome) {
		
		if (!T.existeProdutoSelecionado(nome)) {
		
			T.produtosSelecionados.push({id: id, nome: nome});
		}
	},
	
	this.existeProdutoSelecionado = function(nome) {
		
		var existeProduto = false;
		
		$.each(T.produtosSelecionados, function(index, produto) {
			
			if (produto.nome == nome) {
				
				existeProduto = true;
			}
		});
		
		return existeProduto; 
	},
	
	this.mostrarProdutosSelecionados = function() {
		
		var cellProdutos = '';
				
		$.each(T.produtosSelecionados, function(index, produto) {
			
			cellProdutos += '<div class="produtosSel">' +
						    	'<label>'+ produto.nome +'</label>' +
								'<a href="javascript:;" onclick="' + objName + '.removerProduto(' + index + ');">' +
									'<img src="'+ contextPath +'/images/ico_excluir.gif" width="15" height="15" alt="Excluir" border="0" />' +
								'</a>' +
						    '</div>';
		});
		
		$('#produtosSelecionados', this.workspace).html(cellProdutos);
		
	},
	
	this.removerProduto = function(index) {
		T.produtosSelecionados.splice(index, 1);
		T.mostrarProdutosSelecionados();
	},
	
	this.bloquearLinkProdutos = function() {
		
		var linkProdutos = $("#linkProdutos");
		
		linkProdutos.attr("style", "opacity: 0.5;");
		
		var funcaoClick = linkProdutos.attr("onclick").replace("return;", "" );
		
		linkProdutos.attr("onclick", "return;" + funcaoClick);
	},
	
	this.desbloquearLinkProdutos = function() {
		
		var linkProdutos = $("#linkProdutos");
		
		linkProdutos.attr("style", "");
		linkProdutos.attr("onclick", linkProdutos.attr("onclick").replace("return;", "" ));
	},
	
	this.pesquisar = function() {
				
		var tipoConsulta = $("#tipoConsulta").val();
		
		var params = T.getDadosFiltro();

		$("#gridBox", _workspace).hide();
		$("#gridBoxQuebraCota", _workspace).hide();
		$("#gridCota", _workspace).hide();
		$("#gridRota", _workspace).hide();
		$("#gridRotaQuebraCota", _workspace).hide();
		$("#gridProduto", _workspace).hide();
		$("#gridProdutoEspecifico", _workspace).hide();
		$("#gridProdutoCota", _workspace).hide();

		switch (tipoConsulta) {

		case 'BOX':

			var quebraPorCota = T.get('quebraPorCota');
			
			var flexiGrid = quebraPorCota ? ".mapaAbastecimentoGridQuebraCota" : ".mapaAbastecimentoGrid";
			var grid = quebraPorCota ? "#gridBoxQuebraCota" : "#gridBox";

			T.preencherGrid(
				flexiGrid, 
				pathTela + "/mapaAbastecimento/pesquisar", 
				T.processaRetornoPesquisa, 
				grid,
				params
			);

			break;
		case 'COTA':
			
			T.preencherGrid(
				".mapaAbastecimentoCotaEspGrid", 
				pathTela + "/mapaAbastecimento/pesquisar", 
				T.processarRetornoPesquisaPorCota, 
				"#gridCota",
				params
			);
			
			break;
		case 'ROTA':

			var quebraPorCota = T.get('quebraPorCota');
			
			var flexiGrid = quebraPorCota ? ".mapaAbastecimentoRotaGridQuebraCota" : ".mapaAbastecimentoRotaGrid";
			var grid = quebraPorCota ? "#gridRotaQuebraCota" : "#gridRota";

			T.preencherGrid(
				flexiGrid, 
				pathTela + "/mapaAbastecimento/pesquisar", 
				T.processarMensagens, 
				grid,
				params
			);

			break;
		case 'PRODUTO':
			
			T.preencherGrid(
				".mapaAbastecimentoProdutoGrid", 
				pathTela + "/mapaAbastecimento/pesquisar", 
				T.processarMensagens, 
				"#gridProduto",
				params
			);

			break;
		case 'PRODUTO_ESPECIFICO':

			T.preencherGrid(
				".mapaAbastecimentoProdEspGrid", 
				pathTela + "/mapaAbastecimento/pesquisar", 
				T.processarRetornoPesquisaPorProdutoEdicao, 
				"#gridProdutoEspecifico",
				params
			);

			break;
		case 'PRODUTO_X_COTA':
			
			T.preencherGrid(
				".mapaAbastecimentoProdCotaGrid", 
				pathTela + "/mapaAbastecimento/pesquisar", 
				T.processarMensagens, 
				"#gridProdutoCota",
				params
			);

			break;
		
		case 'ENTREGADOR':
			
			T.preencherGrid(
				".mapaAbastecimentoEntregadorGrid", 
				pathTela + "/mapaAbastecimento/pesquisar", 
				T.processarMensagens, 
				"#gridEntregador",
				params
			);

			break;
			
		default:
			break;
		}
	},

	this.preencherGrid = function(tableClass, url, preProcess, grid, params) {
		
		$(tableClass, _workspace).flexOptions({			
			url : url,
			dataType : 'json',
			preProcess: preProcess,
			params:params
		});
		$(tableClass, _workspace).flexReload();
		$(grid, _workspace).show();
	},
	
	this.processarMensagens = function(result) {

		if(result.mensagens)
			exibirMensagem(result.mensagens.tipoMensagem,result.mensagens.listaMensagens);
		
		return result;
	},
	
	this.processaRetornoPesquisa = function(result) {

		T.processarMensagens(result);
		
		T.mapas = [];
		
		$.each(result.rows, function(index,row){T.processarLinha(index,row.cell);} );
		
		return result;
	},
	
	this.processarRetornoPesquisaPorCota = function(result) {
		
		T.processarMensagens(result);

		var codigoCota = T.get('codigoCota');
		var nomeCota = T.get('nomeCota');

		$("#codigoCotaHeader").html(codigoCota);
		$("#nomeCotaHeader").html(nomeCota);
		
		return result;
	},
	
	this.processarRetornoPesquisaPorProdutoEdicao = function(result) {
		
		T.processarMensagens(result);

		var produto = T.produtosSelecionados[0];
		
		var codigoProduto = produto.id;
		var nomeProduto = produto.nome;
		var edicaoProduto = T.get('edicao');

		$("#codigoProdutoHeader").html(codigoProduto);
		$("#nomeProdutoHeader").html(nomeProduto);
		$("#edicaoProdutoHeader").html(edicaoProduto);
		
		return result;
	},
	
	this.pesquisarDetalhes = function(indice) {
	
		var mapa = T.mapas[indice];
		
		$('#titleBox', _workspace).html(mapa.box);
		
		var data = [];
		
		data.push({name: 'idBox' ,value: mapa.idBox});
		data.push({name: 'data'  ,value: mapa.data});
		
		$(".mapaAbastecimentoDetalheGrid", _workspace).flexOptions({			
			url : pathTela + "/mapaAbastecimento/pesquisarDetalhes",
			dataType : 'json',			
			preProcess: T.processaRetornoPesquisaDetalhes,
			params: data
		
		});		
		
		$(".mapaAbastecimentoDetalheGrid", _workspace).flexReload();
	},
	
	this.processaRetornoPesquisaDetalhes = function(result) {
		
		if(result.mensagens)
			exibirMensagem(result.mensagens.tipoMensagem,result.mensagens.listaMensagens);
		
		
		return result;
	},
	
	
	this.processarLinha = function(index,cell) {
		
		T.mapas.push(cell);
		
		cell.acao =	'<a href="javascript:;" onclick="' + objName +'.carregarDetalhes(\''+ index + '\')">' +
					'<img src="' + pathTela + '/images/ico_detalhes.png" alt="Detalhes do box" border="0" /></a>';
	},
	
	this.atualizarBoxRota = function(isCotaDefined) {		

		var data = [];
		
		if(isCotaDefined)
			data.push({name: 'numeroCota' ,value: T.get('codigoCota')});
		
		$.postJSON(pathTela + "/mapaAbastecimento/buscarBoxRotaPorCota",
				data,
				function(result){T.preencherBoxRota(result,isCotaDefined);});	
	},
	
	this.preencherBoxRota = function(result,isCotaDefined) {

		if(!isCotaDefined)
			result[0].splice(0,0,{"key": {"@class": "string","$": ""},"value": {"@class": "string","$": "Selecione..."}});
		
		result[1].splice(0,0,{"key": {"@class": "string","$": ""},"value": {"@class": "string","$": "Selecione..."}});
		
		result[2].splice(0,0,{"key": {"@class": "string","$": ""},"value": {"@class": "string","$": "Selecione..."}});
		
		
		var comboBox =  montarComboBox(result[0], false);
		$('#box', _workspace).html(comboBox);
		
		var comboRota =  montarComboBox(result[1], false);
		$('#rota', _workspace).html(comboRota);
		
		var comboRoteiro =  montarComboBox(result[2], false);
		$('#roteiro', _workspace).html(comboRoteiro);
		
		if(isCotaDefined) {
			$('#rota', _workspace).enable();
			$('#roteiro', _workspace).enable();
		}
	},
	
	this.carregarDetalhes = function(indice) {
		T.pesquisarDetalhes(indice);
		popup_detalhe_box();
	},
	
	this.displayEntregador = function(display) {
		
		if(display===true)
			$('.entregador').show();
		else
			$('.entregador').hide();
		
	},
	
	this.mudarTipoPesquisa = function(tipo) {
		
		switch (tipo) {
			
		case 'BOX':
			T.atualizarBoxRota();
			T.bloquearCampos('rota','roteiro','codigoProduto','nomeProduto','edicao','codigoCota','nomeCota');
			T.desbloquearCampos('box','quebraPorCota');
			T.bloquearLinkProdutos();
			T.displayEntregador(false);
			T.limparProdutosSelecionados();
			break;
		case 'ROTA':
			T.atualizarBoxRota();
			T.bloquearCampos('codigoProduto','nomeProduto','edicao','codigoCota','nomeCota');
			T.desbloquearCampos('box','rota','roteiro','quebraPorCota');
			T.displayEntregador(false);
			T.bloquearLinkProdutos();
			T.limparProdutosSelecionados();
			break;
		case 'COTA':
			T.bloquearCampos('box','rota','roteiro', 'codigoProduto','nomeProduto','edicao');
			T.desbloquearCampos('codigoCota','nomeCota','quebraPorCota');
			T.displayEntregador(false);
			T.bloquearLinkProdutos();
			T.limparProdutosSelecionados();
			break;
		case 'PRODUTO':
			T.bloquearCampos('box','rota','roteiro', 'codigoCota','nomeCota','quebraPorCota');
			T.desbloquearCampos('codigoProduto','nomeProduto','edicao');
			T.displayEntregador(false);
			T.desbloquearLinkProdutos();
			break;
		case 'PRODUTO_ESPECIFICO':
			T.bloquearCampos('box','rota','codigoCota','nomeCota','quebraPorCota');
			T.desbloquearCampos('codigoProduto','nomeProduto','edicao');
			T.desbloquearLinkProdutos();
			break;
		case 'ENTREGADOR':
			T.atualizarBoxRota();
			T.bloquearCampos('quebraPorCota');
			T.desbloquearCampos('box','rota','roteiro', 'codigoProduto','nomeProduto','edicao','codigoCota','nomeCota');
			T.displayEntregador(true);
			T.bloquearLinkProdutos();
			T.desbloquearLinkProdutos();
			break;			
		default:
			T.bloquearCampos('box','rota', 'roteiro', 'codigoProduto','nomeProduto','edicao','codigoCota','nomeCota','quebraPorCota');
			break;
		}
	},	
	
	this.bloquearCampos = function() {
		for(var campo in arguments) {

			$('#' + arguments[campo], _workspace).val('');
			$('#' + arguments[campo], _workspace).disable();
		}
	},
	
	this.desbloquearCampos = function() {
		for(var campo in arguments) {
			$('#' + arguments[campo], _workspace).enable();
		}
	},
	
	this.getDadosFiltro = function() {
		
		var data = [];
		
		data.push({name:'filtro.dataLancamento',	value: T.get("dataLancamento")});
		data.push({name:'filtro.tipoConsulta',		value: T.get("tipoConsulta")});
		data.push({name:'filtro.box',				value: T.get("box")});
		data.push({name:'filtro.rota',				value: T.get("rota")});
		data.push({name:'filtro.roteiro',				value: T.get("roteiro")});
		$.each(T.produtosSelecionados, function(index, row) {
			data.push({name:'filtro.codigosProduto[' + index + ']',	value: row.id});
		});
		
		data.push({name:'filtro.nomeProduto',		value: T.get("nomeProduto")});
		data.push({name:'filtro.edicaoProduto',			value: T.get("edicao")});
		data.push({name:'filtro.codigoCota',		value: T.get("codigoCota")});
		data.push({name:'filtro.nomeCota',			value: T.get("nomeCota")});
		data.push({name:'filtro.quebraPorCota',	value: T.get("quebraPorCota")});
		data.push({name:'filtro.idEntregador',	value: T.get("idEntregador")});
		
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
				
		var elemento = $("#" + campo, _workspace);
		
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
		
		var elemento = $("#" + campo, _workspace);
		
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
				
		$("#dataLancamento", _workspace).mask("99/99/9999");
		
		$( "#dataLancamento", _workspace ).datepicker({
			showOn: "button",
			buttonImage: pathTela + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});

	});
	
	T.bloquearLinkProdutos();
	T.limparProdutosSelecionados();
}


$(function() {	
	
	$(".mapaAbastecimentoGrid", BaseController.workspace).flexigrid($.extend({},{
		colModel: [ {
			display : 'Box',
			name : 'box',
			width : 100,
			sortable : true,
			align : 'left'
		}, {
			display : 'Total de Produtos',
			name : 'totalProduto',
			width : 200,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Reparte',
			name : 'totalReparte',
			width : 200,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Box R$',
			name : 'totalBox',
			width : 200,
			sortable : true,
			align : 'right'
		}, {
			display : 'Material Promocional',
			name : 'materialPromocional',
			width : 135,
			sortable : true,
			align : 'center'
		}, {
			display : 'Ação',
			name : 'acao',
			width : 30,
			sortable : true,
			align : 'center'
		}],		
		sortname : "box",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 255
	})); 
	
	$(".mapaAbastecimentoGridQuebraCota", BaseController.workspace).flexigrid($.extend({},{
		colModel: [ {
			display : 'Cota',
			name : 'codigoCota',
			width : 100,
			sortable : true,
			align : 'left'
		},{
			display : 'Nome',
			name : 'nomeCota',
			width : 200,
			sortable : true,
			align : 'left'
		},{
			display : 'Box',
			name : 'box',
			width : 100,
			sortable : true,
			align : 'left'
		}, {
			display : 'Total de Produtos',
			name : 'totalProduto',
			width : 100,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Reparte',
			name : 'totalReparte',
			width : 100,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Box R$',
			name : 'totalBox',
			width : 100,
			sortable : true,
			align : 'right'
		},  {
			display : 'Material Promocional',
			name : 'materialPromocional',
			width : 100,
			sortable : true,
			align : 'center'
		},{
			display : 'Ação',
			name : 'acao',
			width : 30,
			sortable : false,
			align : 'center'
		}],
		
		sortname : "box",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 255
	})); 
	
	$(".grids", BaseController.workspace).show();	
	
	$(".mapaAbastecimentoDetalheGrid", BaseController.workspace).flexigrid($.extend({},{
		colModel : [ {	
				display : 'Código',
				name : 'codigoProduto',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total R$',
				name : 'total',
				width : 100,
				sortable : true,
				align : 'right'
			}],
			sortname : "nomeProduto",
			sortorder : "asc",
			width : 650,
			height : 255
	})); 	
	
	$(".mapaAbastecimentoCotaEspGrid", BaseController.workspace).flexigrid($.extend({},{
		dataType : 'json',
		colModel : [ {
			display : 'Código',
			name : 'codigoProduto',
			width : 100,
			sortable : true,
			align : 'left'
		},{
			display : 'Produto',
			name : 'nomeProduto',
			width : 290,
			sortable : true,
			align : 'left'
		}, {
			display : 'Total Reparte',
			name : 'reparte',
			width : 250,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Box R$',
			name : 'totalBox',
			width : 250,
			sortable : true,
			align : 'right'
		}],
		sortname : "codigoProduto",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 255
	}));
	
	$(".mapaAbastecimentoRotaGrid", BaseController.workspace).flexigrid($.extend({},{
		dataType : 'json',
		colModel: [ {
			display : 'Box',
			name : 'codigoBox',
			width : 100,
			sortable : true,
			align : 'left'
		},{
			display : 'Rota',
			name : 'codigoRota',
			width : 180,
			sortable : true,
			align : 'left'
		}, {
			display : 'Total de Produtos',
			name : 'totalProduto',
			width : 150,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Reparte',
			name : 'reparte',
			width : 150,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Box R$',
			name : 'totalBox',
			width : 150,
			sortable : true,
			align : 'right'
		}, {
			display : 'Material Promocional',
			name : 'materialPromocional',
			width : 135,
			sortable : true,
			align : 'center'
		}],
		sortname : "codigoBox",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 255
	}));
	
	$(".mapaAbastecimentoRotaGridQuebraCota", BaseController.workspace).flexigrid($.extend({},{
		dataType : 'json',
		colModel: [{
			display : 'Cota',
			name : 'codigoCota',
			width : 100,
			sortable : true,
			align : 'left'
		},{
			display : 'Nome',
			name : 'nomeCota',
			width : 150,
			sortable : true,
			align : 'left'
		}, {
			display : 'Box',
			name : 'codigoBox',
			width : 100,
			sortable : true,
			align : 'left'
		},{
			display : 'Rota',
			name : 'codigoRota',
			width : 100,
			sortable : true,
			align : 'left'
		}, {
			display : 'Total de Produtos',
			name : 'totalProduto',
			width : 100,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Reparte',
			name : 'reparte',
			width : 100,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Box R$',
			name : 'totalBox',
			width : 100,
			sortable : true,
			align : 'right'
		}, {
			display : 'Material Promocional',
			name : 'materialPromocional',
			width : 100,
			sortable : true,
			align : 'center'
		}],
		sortname : "codigoBox",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 255
	}));
	
	$(".mapaAbastecimentoProdutoGrid", BaseController.workspace).flexigrid($.extend({},{
		dataType : 'json',
		colModel : [ {
			display : 'Código',
			name : 'codigoProduto',
			width : 100,
			sortable : true,
			align : 'left'
		},{
			display : 'Produto',
			name : 'nomeProduto',
			width : 290,
			sortable : true,
			align : 'left'
		}, {
			display : 'Total Reparte',
			name : 'reparte',
			width : 250,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Box R$',
			name : 'totalBox',
			width : 250,
			sortable : true,
			align : 'right'
		}],
		sortname : "codigoProduto",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 255
	}));
	
	$(".mapaAbastecimentoProdEspGrid", BaseController.workspace).flexigrid($.extend({},{
		dataType : 'json',
		colModel : [ {
			display : 'Box',
			name : 'codigoBox',
			width : 140,
			sortable : true,
			align : 'left'
		}, {
			display : 'Total Reparte',
			name : 'reparte',
			width : 250,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Box R$',
			name : 'totalBox',
			width : 250,
			sortable : true,
			align : 'right'
		}, {
			display : 'Material Promocional',
			name : 'materialPromocional',
			width : 250,
			sortable : true,
			align : 'center'
		}],
		sortname : "codigoBox",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 255
	}));
	
	$(".mapaAbastecimentoProdCotaGrid", BaseController.workspace).flexigrid($.extend({},{
		dataType : 'json',
		colModel : [ {
			display : 'Cota',
			name : 'codigoCota',
			width : 100,
			sortable : true,
			align : 'left'
		},{
			display : 'Nome',
			name : 'nomeCota',
			width : 300,
			sortable : true,
			align : 'left'
		}, {
			display : 'Total Reparte',
			name : 'reparte',
			width : 160,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Box R$',
			name : 'totalBox',
			width : 160,
			sortable : true,
			align : 'right'
		}, {
			display : 'Material Promocional',
			name : 'materialPromocional',
			width : 160,
			sortable : true,
			align : 'center'
		}],
		sortname : "codigoCota",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 255
	}));
	
	$(".mapaAbastecimentoEntregadorGrid").flexigrid({
		dataType : 'json',
		colModel : [ {
			display : 'Código',
			name : 'codigoProduto',
			width : 70,
			sortable : true,
			align : 'left'
		},{
			display : 'Produto',
			name : 'nomeProduto',
			width : 130,
			sortable : true,
			align : 'left'
		},{
			display : 'Edição',
			name : 'numeroEdicao',
			width : 70,
			sortable : true,
			align : 'left'
		},{
			display : 'Cód. Barras',
			name : 'codigoBarra',
			width : 125,
			sortable : true,
			align : 'left'
		},{
			display : 'Pct. Padrão',
			name : 'pacotePadrao',
			width : 60,
			sortable : true,
			align : 'center'
		}, {
			display : 'Reparte',
			name : 'reparte',
			width : 60,
			sortable : true,
			align : 'center'
		}, {
			display : 'Preço Capa R$',
			name : 'precoCapa',
			width : 80,
			sortable : true,
			align : 'right'
		}, {
			display : 'Cota',
			name : 'codigoCota',
			width : 40,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nome',
			name : 'nomeCota',
			width : 120,
			sortable : true,
			align : 'left'
		}, {
			display : 'Qtde Exml',
			name : 'qtdeExms',
			width : 60,
			sortable : true,
			align : 'center'
		}],
		sortname : "codigoProduto",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 255
	});

});
//@ sourceURL=mapaAbastecimento.js