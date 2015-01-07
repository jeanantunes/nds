 function MapaAbastecimento(pathTela, objName, workspace) {
	
	var _workspace = workspace;
	
	var T = this;
	
	this.mapas = [];
	this.produtosSelecionados = [],
	
	this.carregarProdutos = function() {
		
		$("#selectProdutos", _workspace).val("");
		
		$.postJSON(contextPath + "/mapaAbastecimento/getProdutos",
				   {dataLancamento: T.get("dataLancamento")},
				   function(result) {
					
					   var options = "";
					   
					   $.each(result, function(index, row) {
						   options += "<option value='" + row.key.$ + "'>" + row.value.$ + "</option>";
					   });
					   
					   $("#selectProdutos", _workspace).html(options);
					   
					   $("#dialog-pesq-produtos", _workspace).dialog({
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
				   }
		);
	},
	
	this.limparProdutosSelecionados = function() {
		
		T.produtosSelecionados = [];
		
		T.mostrarProdutosSelecionados();
	},
	
	this.getProdutosSelecionados = function() {
		
		$.each( $('#selectProdutos option:selected', this.workspace), function(index, row) {
			
			T.adicionarProduto(row.value, row.text);
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
		
		var linkProdutos = $("#linkProdutos", _workspace);
		
		linkProdutos.attr("style", "opacity: 0.5;");
		
		var funcaoClick = linkProdutos.attr("onclick").replace("return;", "" );
		
		linkProdutos.attr("onclick", "return;" + funcaoClick);
	},
	
	this.desbloquearLinkProdutos = function() {
		
		var linkProdutos = $("#linkProdutos", _workspace);
		
		linkProdutos.attr("style", "");
		linkProdutos.attr("onclick", linkProdutos.attr("onclick").replace("return;", "" ));
	},
	
	this.pesquisar = function() {
				
		var tipoConsulta = $("#tipoConsulta", _workspace).val();
		
		var params = T.getDadosFiltro();

		$("#gridBox", _workspace).hide();
		$("#gridBoxQuebraCota", _workspace).hide();
		$("#gridCota", _workspace).hide();
		$("#gridRota", _workspace).hide();
		$("#gridRotaQuebraCota", _workspace).hide();
		$("#gridProduto", _workspace).hide();
		$("#gridPromocional", _workspace).hide();
		$("#gridProdutoEspecifico", _workspace).hide();
		$("#gridProdutoCota", _workspace).hide();
		$("#gridEntregador", _workspace).hide();
		$("#gridBoxCotaQuebraCota", _workspace).hide();
		
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
				T.processarRetornoPesquisaPorRota,
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
		
		case 'PROMOCIONAL':
			
			T.preencherGrid(
				".mapaAbastecimentoPromocionalGrid", 
				pathTela + "/mapaAbastecimento/pesquisar", 
				T.processarMensagens, 
				"#gridPromocional",
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
			
			$("#nomeEntregador", _workspace).text($("#idEntregador option:selected", _workspace).text());
			
			T.preencherGrid(
				".mapaAbastecimentoEntregadorGrid", 
				pathTela + "/mapaAbastecimento/pesquisar", 
				T.processarMensagens, 
				"#gridEntregador",
				params
			);

			break;
		
		case 'BOX_X_COTA':
			
			var quebraPorCota = true;
			
			T.preencherGrid(
				quebraPorCota ? ".mapaAbastecimentoBoxCotaGridQuebraCota" : ".mapaAbastecimentoGrid", 
				pathTela + "/mapaAbastecimento/pesquisar", 
				T.processaRetornoPesquisa, 
				quebraPorCota ? "#gridBoxCotaQuebraCota" : "#gridBox",
				params
			);
			
			break;
		
		default:
			exibirMensagem("WARNING", ["É necessario selecionar o Tipo de Consulta a ser realizada!"]);
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
		
		$(".areaBts", _workspace).show();	
	},
	
	this.processarMensagens = function(result) {

		if(result.mensagens) {
			exibirMensagem(result.mensagens.tipoMensagem,result.mensagens.listaMensagens);
		} else {
			$(result.rows).each(function(){
				if(this.cell.codigoBox == undefined) {
					this.cell.codigoBox = "Sem box definido";
				}
				if(this.cell.codigoRota == undefined) {
					this.cell.codigoRota = "Sem rota definida";
				}
				
				if(!this.cell.reparte){
					this.cell.reparte = 0;
				}
				
				if (!this.cell.qtdeExms){
					this.cell.qtdeExms = 0;
				}
			});	
		}
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

		$("#codigoCotaHeader", _workspace).html(codigoCota);
		$("#nomeCotaHeader", _workspace).html(nomeCota);
		
		return result;
	},
	
	this.processarRetornoPesquisaPorProdutoEdicao = function(result) {
		
		T.processarMensagens(result);

		var produto = T.produtosSelecionados[0];
		
		if(produto) {
			
			$("#nomeProdutoHeader", _workspace).html(produto.nome);
		}
		
		if (result.rows){
			$.each(result.rows, function(index, item){
				
				item.cell.codigoBox += (" - " + item.cell.nomeBox);
			});
		}
		
		return result;
	},
	
	this.processarRetornoPesquisaPorRota = function(result){
		
		T.processarMensagens(result);
		
		if (result.rows){
			
			$.each(result.rows, function(index, item){
				
				item.cell.codigoBox += (" - " + item.cell.nomeBox);
			});
		}
		
		return result;
	},
	
	this.pesquisarDetalhes = function(indice) {
	
		var mapa = T.mapas[indice];
		
		$('#titleBox', _workspace).html(mapa.box);
		
		var dataLancamento = $('#dataLancamento', _workspace).val();
		
		var data = [];
		
		data.push({name: 'idBox' ,value: mapa.idBox});
		data.push({name: 'data'  ,value: mapa.data?mapa.data:dataLancamento});
		
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
		
		if(cell.box == undefined || cell.box == 'undefined') {
			cell.box = "Sem box definido";
		}
		
		T.mapas.push(cell);
			
		cell.acao =	'<a href="javascript:;" onclick="' + objName +'.carregarDetalhes(\''+ index + '\')">' +
					'<img src="' + pathTela + '/images/ico_detalhes.png" alt="Detalhes do box" border="0" /></a>';
	},
	
	this.atualizarBoxRota = function(isCotaDefined) {		

		if (!T.get('codigoCota')){
			isCotaDefined = false;
		}
		
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
		$('#mapa-abastecimento-box', _workspace).html(comboBox);
		
		var comboRota =  montarComboBox(result[1], false);
		$('#rota', _workspace).html(comboRota);
		
		var comboRoteiro =  montarComboBox(result[2], false);
		$('#roteiro', _workspace).html(comboRoteiro);
		
		if(isCotaDefined) {
			$('#rota', _workspace).enable();
			$('#roteiro', _workspace).enable();
		}
		
		if (result[3]){
			
			result[3].splice(0,0,{"key": {"@class": "string","$": ""},"value": {"@class": "string","$": "Selecione..."}});
			$("#idEntregador", _workspace).html(
					montarComboBox(result[3], false)
			);
		}
	},
	
	this.carregarDetalhes = function(indice) {
		T.pesquisarDetalhes(indice);
		popup_detalhe_box();
	},
	
	this.displayEntregador = function(display) {
		
		if(display===true)
			$('.entregador', _workspace).show();
		else
			$('.entregador', _workspace).hide();
		
	},
	
	this.mudarTipoPesquisa = function(tipo) {
				
		switch (tipo) {
			
		case 'BOX':
			T.atualizarBoxRota();
			T.bloquearCampos('rota','roteiro','codigoProduto','nomeProduto','codigoCota','nomeCota');
			T.desbloquearCampos('box','quebraPorCota');
			T.bloquearLinkProdutos();
			T.displayEntregador(false);
			$('#quebraPorCota').attr("checked", false);
			T.limparProdutosSelecionados();
			break;
		case 'ROTA':
			T.atualizarBoxRota();
			T.bloquearCampos('codigoProduto','nomeProduto','codigoCota','nomeCota');
			T.desbloquearCampos('box','rota','roteiro','quebraPorCota');
			T.displayEntregador(false);
			$('#quebraPorCota').attr("checked", false);
			T.desbloquearLinkProdutos();
			T.limparProdutosSelecionados();
			break;
		case 'COTA':
			T.bloquearCampos('box','rota','roteiro', 'codigoProduto','nomeProduto','quebraPorCota');
			T.desbloquearCampos('codigoCota','nomeCota');
			T.displayEntregador(false);
			T.bloquearLinkProdutos();
			$('#quebraPorCota').attr("checked", false);
			T.limparProdutosSelecionados();
			break;
		case 'PRODUTO_X_COTA' :
			T.bloquearCampos('box','rota','roteiro','quebraPorCota');
			T.desbloquearCampos('codigoCota','nomeCota', 'codigoProduto','nomeProduto');
			$('#quebraPorCota').attr("checked", false);
			T.desbloquearLinkProdutos();
			T.displayEntregador(false);
			break;
		case 'PRODUTO':
			T.bloquearCampos('box','rota','roteiro', 'codigoCota','nomeCota','quebraPorCota');
			$('#quebraPorCota').attr("checked", false);
			T.desbloquearCampos('codigoProduto','nomeProduto');
			T.displayEntregador(false);
			T.desbloquearLinkProdutos();
			break;
		case 'PROMOCIONAL':
			T.bloquearCampos('box','rota','roteiro', 'codigoCota','nomeCota','quebraPorCota');
			T.desbloquearCampos('codigoProduto','nomeProduto');
			T.displayEntregador(false);
			T.desbloquearLinkProdutos();
			break;
		case 'PRODUTO_ESPECIFICO':
			T.bloquearCampos('box','rota','roteiro','codigoCota','nomeCota','quebraPorCota');
			$('#quebraPorCota').attr("checked", false);
			T.desbloquearCampos('codigoProduto','nomeProduto');
			T.desbloquearLinkProdutos();
			break;
		case 'ENTREGADOR':
			T.atualizarBoxRota();
			T.bloquearCampos('quebraPorCota');
			$('#quebraPorCota').attr("checked", false);
			T.desbloquearCampos('box','rota','roteiro', 'codigoProduto','nomeProduto','codigoCota','nomeCota');
			T.displayEntregador(true);
			T.bloquearLinkProdutos();
			T.desbloquearLinkProdutos();
			break;
		case 'BOX_X_COTA':
			T.atualizarBoxRota();
			T.bloquearCampos('rota','roteiro','codigoProduto','nomeProduto','codigoCota','nomeCota');
			$('#quebraPorCota').attr("checked", true);
			T.desbloquearCampos('rota','roteiro');
			T.bloquearLinkProdutos();
			T.displayEntregador(false);
			T.limparProdutosSelecionados();
			break;	
		default:
			T.bloquearCampos('box','rota', 'roteiro', 'codigoProduto','nomeProduto','codigoCota','nomeCota','quebraPorCota');
			break;
		}
	},	
	
	this.bloquearCampos = function() {
		for(var campo in arguments) {

			if($('#' + arguments[campo], _workspace).is("select")) {
				$('#' + arguments[campo], _workspace)
			    .find('option')
			    .remove()
			    .end()
			    .append('<option value="">Selecione</option>')
			    .val('');
			}
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
			data.push({name:'filtro.codigosProduto[' + index + ']',	value: row.id.split('_')[0]});
		});
		
		$.each(T.produtosSelecionados, function(index, row) {
			data.push({name:'filtro.numerosEdicao[' + index + ']',	value: row.id.split('_')[1]});
		});
		
		data.push({name:'filtro.nomeProduto',		value: T.get("nomeProduto")});
		data.push({name:'filtro.codigoCota',		value: T.get("codigoCota")});
		data.push({name:'filtro.nomeCota',			value: T.get("nomeCota")});
		data.push({name:'filtro.quebraPorCota',	value: T.get("quebraPorCota")});
		
		if ($(".entregador", workspace).is(":visible")){
			data.push({name:'filtro.idEntregador',	value: T.get("idEntregador")});
		}
		
		data.push({name:'filtro.excluirProdutoSemReparte', value: T.get("excluirProdutoSemReparte")});
		
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
	

	this.buscarRotaPorRoteiro = function() {
		if(T.get('tipoConsulta') == 'ROTA'){
			$.postJSON(contextPath +'/mapaAbastecimento/buscarRotaPorRoteiro',
					{idRoteiro:T.get("roteiro")},
					function(result) {
						var comboRota =  montarComboBoxCustomJson(result.rotas, true,'Selecione...');
						$('#rota', _workspace).html(comboRota);
					}
				
			);
		}
	};
	this.buscarRoteiroPorBox = function() {
		
		if(T.get('tipoConsulta') == 'ROTA'){
			$.postJSON(contextPath +'/mapaAbastecimento/buscarRoteiroPorBox',
					{idBox:T.get("box")},
					function(result) {
						var comboRota =  montarComboBoxCustomJson(result.rotas, true,'Selecione...');
						$('#rota', _workspace).html(comboRota);
						
						var comboRoteiro =  montarComboBoxCustomJson(result.roteiros, true,'Selecione...');
						$('#roteiro', _workspace).html(comboRoteiro);
					}
				
			);
		}
		
	};
	
	T.bloquearLinkProdutos();
	T.limparProdutosSelecionados();
}


$(function() {	
	
	$(".mapaAbastecimentoGrid", BaseController.workspace).flexigrid($.extend({},{
		colModel: [ {
			display : 'Box',
			name : 'box',
			width : 150,
			sortable : true,
			align : 'left'
		}, {
			display : 'Total de Produtos',
			name : 'totalProduto',
			width : 230,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Reparte',
			name : 'totalReparte',
			width : 230,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Box R$',
			name : 'totalBox',
			width : 225,
			sortable : true,
			align : 'right'
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
	
	$(".mapaAbastecimentoPromocionalGrid", BaseController.workspace).flexigrid($.extend({},{
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
			width : 280,
			sortable : true,
			align : 'left'
		}, {
			display : 'Total Reparte',
			name : 'reparte',
			width : 175,
			sortable : true,
			align : 'center'
		}, {
			display : 'Reparte Promocional',
			name : 'materialPromocional',
			width : 175,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Box R$',
			name : 'totalBox',
			width : 150,
			sortable : true,
			align : 'right'
		}],
		sortname : "nomeProduto",
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
			width : 220,
			sortable : true,
			align : 'left'
		},{
			display : 'Box',
			name : 'box',
			width : 120,
			sortable : true,
			align : 'left'
		}, {
			display : 'Total de Produtos',
			name : 'totalProduto',
			width : 120,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Reparte',
			name : 'totalReparte',
			width : 120,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Box R$',
			name : 'totalBox',
			width : 120,
			sortable : true,
			align : 'right'
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
			width : 150,
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
			width : 180,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Reparte',
			name : 'reparte',
			width : 180,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Box R$',
			name : 'totalBox',
			width : 175,
			sortable : true,
			align : 'right'
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
			width : 180,
			sortable : true,
			align : 'left'
		}, {
			display : 'Box',
			name : 'codigoBox',
			width : 130,
			sortable : true,
			align : 'left'
		},{
			display : 'Rota',
			name : 'codigoRota',
			width : 130,
			sortable : true,
			align : 'left'
		}, {
			display : 'Total de Produtos',
			name : 'totalProduto',
			width : 110,
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
			width : 240,
			sortable : true,
			align : 'left'
		}, {
			display : 'Total Reparte',
			name : 'reparte',
			width : 325,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Box R$',
			name : 'totalBox',
			width : 325,
			sortable : true,
			align : 'right'
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
			width : 140,
			sortable : true,
			align : 'left'
		},{
			display : 'Nome',
			name : 'nomeCota',
			width : 340,
			sortable : true,
			align : 'left'
		}, {
			display : 'Total Reparte',
			name : 'reparte',
			width : 200,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total Box R$',
			name : 'totalBox',
			width : 200,
			sortable : true,
			align : 'right'
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

	$(".mapaAbastecimentoBoxCotaGridQuebraCota", BaseController.workspace).flexigrid($.extend({},{
		dataType : 'json',
		colModel: [{
			display : 'Box',
			name : 'nomeBox',
			width : 130,
			sortable : true,
			align : 'left'
		},{
			display : 'Cota',
			name : 'codigoCota',
			width : 100,
			sortable : true,
			align : 'left'
		},{
			display : 'Nome',
			name : 'nomeCota',
			width : 280,
			sortable : true,
			align : 'left'
		},{
			display : 'Total de Produtos',
			name : 'totalProduto',
			width : 110,
			sortable : true,
			align : 'center'
		},{
			display : 'Total Reparte',
			name : 'reparte',
			width : 100,
			sortable : true,
			align : 'center'
		},{
			display : 'Total R$',
			name : 'totalBox',
			width : 100,
			sortable : true,
			align : 'right'
		},{
			display : 'Ação',
			name : 'acao',
			width : 30,
			sortable : false,
			align : 'center'
		}],
		sortname : "nomeBox",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 255
	})); 
	
});
//@ sourceURL=mapaAbastecimento.js