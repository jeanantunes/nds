var vendaProdutoController = $.extend(true, {
	
	pesquisaRealizada : false,

	descricaoAtribuida : true,

	intervalo : null,

	init : function() {
		
		var workSpace = vendaProdutoController.workspace;

		var autoComp = new AutoCompleteCampos(workSpace);
		
		$('#codigo', workSpace).bind({
			keyup: function(){
				autoComp.autoCompletarPorCodigo ("/produto/autoCompletarPorCodProduto",'#codigo', '#produto', 'codigoProduto', false, 4);
			},
			blur: function(){
				autoComp.pesquisarPorCodigo ("/produto/pesquisarPorCodigoProduto",'#codigo', '#produto', 'codigoProduto');
			}
		});
		
		$("#produto", vendaProdutoController.workspace).autocomplete({
			source:function(param ,callback) {
				$.postJSON(contextPath + "/produto/autoCompletarPorNomeProduto", { 'nomeProduto': param.term }, callback);
			},
			select : function(event, ui) {
				$('#codigo', vendaProdutoController.workspace).val(ui.item.chave.codigo);
				
			},
			minLength: 2,
			delay : 0,
		}).keyup(function(){
			this.value = this.value.toUpperCase();
		});
		
		$(".area", workSpace).hide();
		
		window.addEventListener('blur', function() {
			window.clearInterval(vendaProdutoController.intervalo);
		});

		$(".parciaisGrid", vendaProdutoController.workspace).flexigrid({
			preProcess: vendaProdutoController.executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Edição',
				name : 'numEdicao',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Período',
				name : 'periodoFormatado',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Estudo',
				name : 'numeroEstudo',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dt. Lcto',
				name : 'dataLancamento',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dt. Rcto',
				name : 'dataRecolhimento',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda',
				name : 'venda',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda',
				name : 'percentualVenda',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total R$',
				name : 'total',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Chamada Capa',
				name : 'chamadaCapa',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : false,
				align : 'center'
			}],
			sortname : "dataLancamento",
			sortorder : "desc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		$(".detalhesVendaGrid", vendaProdutoController.workspace).flexigrid({
			preProcess: vendaProdutoController.executarPreProcessamentoFilha,
			dataType : 'json',
			colModel : [ {
				display : 'Período',
				name : 'periodo',
				width : 40,
				sortable : false,
				align : 'center'
			}, {
				display : 'Data Lançamento',
				name : 'dataLancamento',
				width : 100,
				sortable : false,
				align : 'center'
			}, {
				display : 'Data Recolhimento',
				name : 'dataRecolhimento',
				width : 110,
				sortable : false,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 55,
				sortable : false,
				align : 'center'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 55,
				sortable : false,
				align : 'center'
			}, {
				display : 'Vendas',
				name : 'venda',
				width : 70,
				sortable : false,
				align : 'center'
			}, {
				display : 'Venda Acumulada',
				name : 'vendaAcumulada',
				width : 130,
				sortable : false,
				align : 'center'
			}, {
				display : '% Venda',
				name : 'percentualVenda',
				width : 60,
				sortable : false,
				align : 'center'
			}],
			width : 750,
			height : 200
		});

		$(document).ready(function(){
			
			focusSelectRefField($("#codigo", vendaProdutoController.workspace));
			
			$(document.body).keydown(function(e) {
				
				if(keyEventEnterAux(e)){
					vendaProdutoController.cliquePesquisar();
				}
				
				return true;
			});
		});
	},
	
	popup_detalhes : function(codigoProduto, numEdicao) {
		
		$(".detalhesVendaGrid", vendaProdutoController.workspace).flexOptions({
			url: contextPath + "/lancamento/vendaProduto/pesquisarLancamentoEdicao",
			dataType : 'json',
			params: [
			          {name:'filtro.edicao', value:numEdicao},
				      {name:'filtro.codigo', value:codigoProduto}
			         ]
		    
		});
		
		$(".detalhesVendaGrid", vendaProdutoController.workspace).flexReload();
		
		$( "#dialog-detalhes", vendaProdutoController.workspace ).dialog({
			resizable: false,
			height:400,
			width:800,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
			},
			form: $("#dialog-detalhes", this.workspace).parents("form")
		});
	},
	
	
	completarPesquisa : function(chave){
		$("#codigo", vendaProdutoController.workspace).val(chave.codigoProduto);
		$("#edicoes", vendaProdutoController.workspace).focus();
	},
	
	cliquePesquisar : function(){	
		
		$(".parciaisGrid", vendaProdutoController.workspace).flexOptions({
			url: contextPath + "/lancamento/vendaProduto/pesquisarVendaProduto",
			dataType : 'json',
			params: vendaProdutoController.getDados()
		});
		
		$(".parciaisGrid", vendaProdutoController.workspace).flexReload();
				
	},
	
	getDados : function() {
		
		var data = [];
		
		data.push({name:'filtro.codigo',		value: vendaProdutoController.get("codigo")});
		data.push({name:'filtro.nomeProduto',		value: vendaProdutoController.get("produto")});
		data.push({name:'filtro.edicao',		value: vendaProdutoController.get("edicoes")});
		data.push({name:'filtro.idFornecedor',		value: vendaProdutoController.get("idFornecedor")});		
		data.push({name:'filtro.nomeFornecedor',	value: $('#idFornecedor option:selected', vendaProdutoController.workspace).text()});
		data.push({name:'filtro.numeroCota',		value: vendaProdutoController.get("numeroCota")});
		data.push({name:'filtro.nomeCota',		value: vendaProdutoController.get("nomeCota")});
		data.push({name:'filtro.idClassificacaoProduto',		value: vendaProdutoController.get("venda-produto-selectClassificacao")});
		
		return data;
	},
	
	get : function(campo) {
		
		var elemento = $("#" + campo, vendaProdutoController.workspace);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	},
	
	executarPreProcessamento : function(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", vendaProdutoController.workspace).hide();
			$(".area", vendaProdutoController.workspace).hide();
			
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var linkDetalhe;
			
			if (row.cell.parcial) {
				
				linkDetalhe = '<a href="javascript:;" onclick="vendaProdutoController.popup_detalhes(' + row.cell.codigoProduto + ',' + row.cell.numEdicao + ');" style="cursor:pointer">' +
							  	'<img title="Lançamentos da Edição" src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0px" />' +
							  '</a>';
			} else {
				
				linkDetalhe = '<a href="javascript:;" style="cursor:pointer; opacity: 0.5;">' +
		   	 					'<img title="Lançamentos da Edição" src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0px" />' +
		   	 				  '</a>';
			}
			
			row.cell.acao = linkDetalhe;
			
			row.cell.percentualVenda = row.cell.percentualVendaFormatado;
			row.cell.precoCapa = row.cell.valorPrecoCapaFormatado;
			row.cell.total = row.cell.valorTotalFormatado;
			
			if(row.cell.periodoFormatado == 0){
				row.cell.periodoFormatado = '-';
			}
			
			if(row.cell.numeroEstudo == undefined){
				row.cell.numeroEstudo = '';
			}
			
		});
		
		$(".grids", vendaProdutoController.workspace).show();
		$(".area", vendaProdutoController.workspace).show();
		
		return resultado;
	},
	
	executarPreProcessamentoFilha : function(resultado) {
		
		if (resultado.mensagens) {
			$("#dialog-detalhes", vendaProdutoController.workspace).dialog("close");
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			row.cell.percentualVenda = row.cell.percentualVendaFormatado;
			
		});
		
		return resultado;
	}
	
}, BaseController);
//@ sourceURL=vendaProduto.js