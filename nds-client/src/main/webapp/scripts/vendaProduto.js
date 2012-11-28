var vendaProdutoController = $.extend(true, {
	
	pesquisaRealizada : false,

	descricaoAtribuida : true,

	intervalo : null,

	init : function() {

		window.addEventListener('blur', function() {
			window.clearInterval(vendaProdutoController.intervalo);
		});

		$("#produto", vendaProdutoController.workspace).autocomplete({source: ""});				

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
				name : 'valorVendaFormatado',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda',
				name : 'percentagemVendaFormatado',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço Capa R$',
				name : 'valorPrecoCapaFormatado',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total R$',
				name : 'valorTotalFormatado',
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
				sortable : true,
				align : 'center'
			}],
			sortname : "edicao",
			sortorder : "asc",
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
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Lançamento',
				name : 'dataLancamento',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Recolhimento',
				name : 'dataRecolhimento',
				width : 110,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 55,
				sortable : true,
				align : 'center'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 55,
				sortable : true,
				align : 'center'
			}, {
				display : 'Vendas',
				name : 'vendaFormatado',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Acumulada',
				name : 'vendaAcumuladaFormatado',
				width : 130,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda',
				name : 'percentualVendaFormatado',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			width : 750,
			height : 200
		});

	},
	
	popup_detalhes : function(numEdicao) {
		var edicao = numEdicao;
		var codigo = $('#codigo', vendaProdutoController.workspace).val();
		$(".detalhesVendaGrid", vendaProdutoController.workspace).flexOptions({
			url: contextPath + "/lancamento/vendaProduto/pesquisarLancamentoEdicao",
			dataType : 'json',
			params: [
			          {name:'filtro.edicao', value:edicao},
				      {name:'filtro.codigo', value:codigo}
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
	
	pesquisarPorCodigoProduto : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
		var codigoProduto = $(idCodigo,this.workspace).val();

		codigoProduto = $.trim(codigoProduto);

		$(idCodigo,this.workspace).val(codigoProduto);

		$(idProduto,this.workspace).val("");

		if (codigoProduto && codigoProduto.length > 0) {

			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
					{codigoProduto:codigoProduto},
					function(result) { vendaProdutoController.pesquisarPorCodigoSuccessCallBack(result, idProduto, successCallBack); },
					function() { vendaProdutoController.pesquisarPorCodigoErrorCallBack(idCodigo, errorCallBack); }, isFromModal);

		} else {

			if (errorCallBack) {
				errorCallBack();
			}
		}
	},

	autoCompletarPorNome : function(idProduto, isFromModal) {
		
		vendaProdutoController.pesquisaRealizada = false;

		var nome = $(idProduto,this.workspace).val();

		if (nome && nome.length > 2) {
			$.postJSON(contextPath + "/produto/autoCompletarPorNomeProduto", {"nomeProduto" : nome},
					function(result) { vendaProdutoController.exibirAutoComplete(result, idProduto); },
					null, isFromModal);
		}
	},
	
	pesquisarPorNome : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
		setTimeout(function() {
			
			clearInterval(vendaProdutoController.intervalo); 
		
		}, 10 * 1000);

		vendaProdutoController.intervalo = setInterval(function() {
			
			if (vendaProdutoController.descricaoAtribuida) {

				if (vendaProdutoController.pesquisaRealizada) {
					
					clearInterval(vendaProdutoController.intervalo);

					return;
				}

				vendaProdutoController.pesquisarPorNomeAposIntervalo(idCodigo, idProduto,
						isFromModal, successCallBack, errorCallBack);
			}

		}, 100);
		
	},

	pesquisarPorNomeAposIntervalo : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
		
		clearInterval(vendaProdutoController.intervalo);

		vendaProdutoController.pesquisaRealizada = true;

		var nomeProduto = $(idProduto,this.workspace).val();

		nomeProduto = $.trim(nomeProduto);

		$(idCodigo,this.workspace).val("");

		if (nomeProduto && nomeProduto.length > 0) {
			$.postJSON(contextPath + "/produto/pesquisarPorNomeProduto", {"nomeProduto" : nomeProduto},
					function(result) { vendaProdutoController.pesquisarPorNomeSuccessCallBack(result, idCodigo, idProduto, successCallBack); },
					function() { vendaProdutoController.pesquisarPorNomeErrorCallBack(idCodigo, idProduto, errorCallBack); }, isFromModal);
		} else {

			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	pesquisarPorCodigoSuccessCallBack : function(result, idProduto, successCallBack, idCodigo, isFromModal) {

		$(idProduto,this.workspace).val(result.nome);

		vendaProdutoController.pesquisaRealizada = true;

		if (successCallBack) {
			successCallBack();
		}
	},

	pesquisarPorCodigoErrorCallBack : function(idCodigo, errorCallBack) {
		$(idCodigo,this.workspace).val("");
		$(idCodigo).focus();

		if (errorCallBack) {
			errorCallBack();
		}
	},

	pesquisarPorNomeSuccessCallBack : function(result, idCodigo, idProduto, successCallBack) {
		if (result != "") {
			$(idCodigo,this.workspace).val(result.codigo);
			$(idProduto,this.workspace).val(result.nome);

			if (successCallBack) {
				successCallBack();
			}
		}
	},

	pesquisarPorNomeErrorCallBack : function(idCodigo, idProduto, errorCallBack) {
		$(idProduto,this.workspace).val("");
		$(idProduto).focus();

		if (errorCallBack) {
			errorCallBack();
		}
	},	

	exibirAutoComplete : function(result, idProduto) {
		
		$(idProduto).autocomplete({
			source : result,
			focus : function(event, ui) {
				vendaProdutoController.descricaoAtribuida = false;
			},
			close : function(event, ui) {
				vendaProdutoController.descricaoAtribuida = true;
			},
			select : function(event, ui) {
				vendaProdutoController.descricaoAtribuida = true;
			},
			minLength: 4,
			delay : 0,
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

			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var linkDetalhe = '<a href="javascript:;" onclick="vendaProdutoController.popup_detalhes('+row.cell.numEdicao+');" style="cursor:pointer">' +
							   	 '<img title="Lançamentos da Edição" src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0px" />' +
							   '</a>';
			
			row.cell.acao = linkDetalhe;
		});
		
		
		
		
		$(".grids", vendaProdutoController.workspace).show();
		
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
		return resultado;
	}
	
}, BaseController);
//@ sourceURL=vendaProduto.js
