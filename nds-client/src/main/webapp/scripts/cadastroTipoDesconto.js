var tipoDescontoController = $.extend(true,  {
	
	tipoDescontoSelecionado:"",
	
	pesquisar:function () {
		
		if(tipoDescontoController.tipoDescontoSelecionado == 'GERAL') {
			tipoDescontoController.pesquisarDescontoGeral();						
		} else if(tipoDescontoController.tipoDescontoSelecionado == 'ESPECIFICO') {
			tipoDescontoController.pesquisarDescontoEspecifico();
		} else if(tipoDescontoController.tipoDescontoSelecionado == 'EDITOR') {
			tipoDescontoController.pesquisarDescontoEditor();
		} else if (tipoDescontoController.tipoDescontoSelecionado == 'PRODUTO') {
			tipoDescontoController.pesquisarDescontoProduto();
		} else {
			exibirMensagem("WARNING", ['Informe um tipo de desconto para pesquisa!'], "");
		}	
	},
	
	pesquisarDescontoGeral:function(){
		
		$(".tiposDescGeralGrid",this.workspace).flexOptions({
			url: contextPath +"/financeiro/tipoDescontoCota/pesquisarDescontoGeral",
			params: [],
		    newp: 1,
		});
		
		$(".tiposDescGeralGrid",this.workspace).flexReload();
	},
	
	pesquisarDescontoEspecifico: function() {
		 
		var cotaEspecifica = $("#numCotaPesquisa",this.workspace).val();
		var nomeEspecifico = $("#descricaoCotaPesquisa",this.workspace).val();
		
		$(".tiposDescEspecificoGrid", this.workspace).flexOptions({
			url: contextPath + "/financeiro/tipoDescontoCota/pesquisarDescontoEspecifico",
			params: [
					 {name:'cotaEspecifica', value:cotaEspecifica},
			         {name:'nomeEspecifico', value:nomeEspecifico}
			         ],
		    newp: 1,
		});
		
		$(".tiposDescEspecificoGrid", this.workspace).flexReload();
	},
	
	pesquisarDescontoEditor: function() {
		 
		var codigoEditor = $("#numEditorPesquisa", this.workspace).val();
		var nomeEditor = $("#descricaoEditorPesquisa", this.workspace).val();
		
		$(".tiposDescEditorGrid", this.workspace).flexOptions({
			url: contextPath + "/financeiro/tipoDescontoCota/pesquisarDescontoEditor",
			params: [
					 {name:'codigoEditor', value:codigoEditor},
			         {name:'nomeEditor', value:nomeEditor}
			         ],
		    newp: 1,
		});
		
		$(".tiposDescEditorGrid", this.workspace).flexReload();
	},
	
	pesquisarDescontoProduto:function() {
		var codigo = $("#codigoPesquisa", this.workspace).val();
		var produto = $("#produtoPesquisa", this.workspace).val();
		
		$(".tiposDescProdutoGrid", this.workspace).flexOptions({
			url: contextPath +"/financeiro/tipoDescontoCota/pesquisarDescontoProduto",
			params: [
					 {name:'codigo', value:codigo},
			         {name:'produto', value:produto}
			         ],
		    newp: 1,
		});
		
		$(".tiposDescProdutoGrid", this.workspace).flexReload();
	},
			
	mostra_geral:function() {
		this.preMostra("GERAL");
		$('.especifico', this.workspace).hide();
		$('.editor', this.workspace).hide();
		$('.produto', this.workspace).hide();
		
		tipoDescontoController.exibirExportacao(false);
		$(".grids", this.workspace).show();
	},
	
	mostra_especifico:function() {
		this.preMostra("ESPECIFICO");
		$('.especifico', this.workspace).show();
		$('.produto', this.workspace).hide();
		$('.editor', this.workspace).hide();
		
		tipoDescontoController.exibirExportacao(false);		
		$(".grids", this.workspace).show();
	},
	
	mostra_editor:function() {
		this.preMostra("EDITOR");
		$('.editor', this.workspace).show();
		$('.produto', this.workspace).hide();
		$('.especifico', this.workspace).hide();
		
		tipoDescontoController.exibirExportacao(false);		
		$(".grids", this.workspace).show();
	},
	
	mostra_produto:function() {
		this.preMostra("PRODUTO");
		$('.especifico' ,this.workspace).hide();
		$('.editor', this.workspace).hide();
		$('.produto', this.workspace).show();
		
		tipoDescontoController.exibirExportacao(false);
		$(".grids", this.workspace).show();
	},
	
	preMostra:function(tipo) {
		tipoDescontoController.exibirExportacao(false);
		tipoDescontoController.exibirNovo(false);
		tipoDescontoController.exibirTipoGrid(false);
		tipoDescontoController.tipoDescontoSelecionado = tipo;
		tipoDescontoController.exibirNovo(true);
	},
	
	exibirExportacao:function(isExibir) {
		
			if(isExibir == true) {
				$("#idExportacao" + tipoDescontoController.tipoDescontoSelecionado,this.workspace).show();	
			} else {
				$("#idExportacao" + tipoDescontoController.tipoDescontoSelecionado,this.workspace).hide();
			}
			
	},
	exibirTipoGrid:function(isExibir) {
		
		if(isExibir == true) {
			$("#tpo" + tipoDescontoController.tipoDescontoSelecionado,this.workspace).show();	
		} else {
			$("#tpo" + tipoDescontoController.tipoDescontoSelecionado,this.workspace).hide();
		}
		
	},
	exibirNovo:function(isExibir) {
		
		if(isExibir == true) {
			$("#panelBts" + tipoDescontoController.tipoDescontoSelecionado,this.workspace).show();	
		} else {
			$("#panelBts" + tipoDescontoController.tipoDescontoSelecionado,this.workspace).hide();
		}
		
	},
	
	executarPreProcessamento: function(resultado) {				
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids",this.workspace).hide();
			
			tipoDescontoController.exibirExportacao(false);
			
			if(tipoDescontoController.tipoDescontoSelecionado == "PRODUTO") {
				tipoDescontoController.mostra_produto();
			} else if (tipoDescontoController.tipoDescontoSelecionado == "GERAL") {
				tipoDescontoController.mostra_geral();
			} else if (tipoDescontoController.tipoDescontoSelecionado == "EDITOR") {
				tipoDescontoController.mostra_editor();
			} else {
				tipoDescontoController.mostra_especifico();
			}

			return resultado;
		}

		$.each(resultado.rows, function(index, row) {					

			if(tipoDescontoController.tipoDescontoSelecionado == "PRODUTO") {

				var qtdeCotas = row.cell.qtdeCotas;
				
				if(qtdeCotas) {
					
					row.cell.nomeProduto = '<a href="javascript:;"  onclick="descontoProdutoController.exibirDialogCotasProdutoEdicao(' + row.cell.descontoId + ');" style="cursor:pointer" '
									+ ' title="Desconto aplicado para ' + qtdeCotas +' cota(s)" > ' +
								    row.cell.nomeProduto +
								    '</a>';
				} else {
					row.cell.nomeProduto = '<a title="Desconto aplicado em todas as cotas."> ' + row.cell.nomeProduto + '</a>';
				}	
								
			} else if(tipoDescontoController.tipoDescontoSelecionado == "EDITOR") {
				
				if(row.cell.qtdCotas == 0) {
					row.cell.qtdCotas = '*';
				} else {
					var linkCotas = '<a href="javascript:;" onclick="tipoDescontoController.carregarCotasAssociadasAoDescontoEditor('+ row.cell.descontoId +');">Diversas</a>';
					
					row.cell.qtdCotas = linkCotas; 
				}
				
			} else {
				
				if(row.cell.fornecedor=="Diversos") {
					var linkFornecedores = '<a href="javascript:;" onclick="tipoDescontoController.carregarFornecedoresAssociadosADesconto('+ row.cell.descontoId +');">Diversos</a>';
						
					row.cell.fornecedor = linkFornecedores; 
				}
			}
			
			if(row.cell.predominante != null) row.cell.predominante = (row.cell.predominante ? "Sim" : "Não");
			
			if(row.cell.qtdeProxLcmt != null) row.cell.numeroEdicao = 'por ' + row.cell.qtdeProxLcmt + ' edição(ões)';
			
			if(row.cell.numeroEdicao == null) row.cell.numeroEdicao = '*';
			
			var linkExcluir = '<a isEdicao="true" href="javascript:;" onclick="tipoDescontoController.exibirDialogExclusao('+ row.cell.descontoId +');" style="cursor:pointer">' +
							   	 '<img title="Excluir Desconto" src="'+contextPath+'/images/ico_excluir.gif" hspace="5" border="0px" />' +
							   '</a>';
			
			row.cell.acao = (row.cell.excluivel == false) ? '' : linkExcluir;
		});
		
		$(".grids",this.workspace).show();
		
		tipoDescontoController.exibirExportacao(true);
		tipoDescontoController.exibirTipoGrid(true);
		
		return resultado;
	},

	exibirDialogExclusao:function(idDesconto) {		
		$("#dialog-excluirCota" ,this.workspace).dialog({
			resizable: false,
			height:'auto',
			width:250,
			modal: true,
			buttons: [{
						id:"id_confirmar_exclusao",text:"Confirmar",
						click: function() {
		
							tipoDescontoController.excluirDesconto(idDesconto, tipoDescontoController.tipoDescontoSelecionado);
									
							$( this ).dialog( "close" );
						}
					},{
						id:"id_cancelar_exclusao",text:"Cancelar",
						click: function() {
							$( this ).dialog( "close" );
						}
					}
					],
			form: $("#dialog-excluirCota", this.workspace).parents("form")
		});
	},
	
	excluirDesconto:function(idDesconto, tipoDesconto){		
		$.postJSON(contextPath + "/financeiro/tipoDescontoCota/excluirDesconto",
				{idDesconto:idDesconto,tipoDesconto:tipoDesconto}, 
				function(){
					tipoDescontoController.pesquisar();
				}
		);
	},
	
	fecharDialogs:function() {
		$( "#dialog-geral",this.workspace ).dialog( "close" );
		$( "#dialog-especifico" ,this.workspace).dialog( "close" );
		$( "#dialog-produto",this.workspace ).dialog( "close" );
	},
	
	carregarFornecedores:function(idComboFornecedores){
		
		$.postJSON(contextPath + "/financeiro/tipoDescontoCota/obterFornecedores",
				null, 
				function(result){
					
					if(result){
						var comboClassificacao =  montarComboBox(result, false);
						
						$(idComboFornecedores,this.workspace).html(comboClassificacao);
					}
				},null,true
		);
	},
	
	carregarFornecedoresAssociadosADesconto:function(idDesconto) {
		
		var param = [{name:"idDesconto", value:idDesconto},{name:"tipoDesconto", value:tipoDescontoController.tipoDescontoSelecionado}];
		
		$(".lstFornecedoresGrid",this.workspace).flexOptions({
			url: contextPath +"/financeiro/tipoDescontoCota/obterFornecedoresAssociadosDesconto",
			params: param,
		    newp: 1,
		    sortorder:'asc'
		});
		
		$(".lstFornecedoresGrid",this.workspace).flexReload();
		
		tipoDescontoController.popupListaFornecedores();
	},
	
	carregarCotasAssociadasAoDescontoEditor: function(idDesconto){
		
		var param = [{name:"idDesconto", value:idDesconto}, {name:"tipoDesconto", value:tipoDescontoController.tipoDescontoSelecionado}];
		
		$(".lstCotasEditorGrid",this.workspace).flexOptions({
			url: contextPath +"/financeiro/tipoDescontoCota/obterCotasAssociadasAoDescontoEditor",
			params: param,
		    newp: 1,
		    sortorder:'asc'
		});
		
		$(".lstCotasEditorGrid",this.workspace).flexReload();
		
		tipoDescontoController.popupListaCotas();
	},
	
	popupListaCotas: function() {
		
		$( "#dialog-cotas-editor",this.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons:[ {id:"btn_close_cota",
					   text:"Fechar",
					   click: function() {
							$( this ).dialog( "close" );
						},
					}],
			form: $("#dialog-cotas-editor", this.workspace).parents("form")
		});	      
	},
	
	mostrarGridCota:function() {
		$('.especificaCota', this.workspace).show();
	},

	esconderGridCota:function() {
		
		$('.especificaCota', this.workspace).hide();
		
		descontoProdutoController.resetGridCota();
	},
	
	popupListaFornecedores:function() {
		
		$( "#dialog-fornecedores",this.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons:[ {id:"btn_close_fornecedor",
					   text:"Fechar",
					   click: function() {
							$( this ).dialog( "close" );
						},
					}],
			form: $("#dialog-fornecedores", this.workspace).parents("form")
		});	      
	},
	
	init: function() {
		
		$(".lstFornecedoresGrid",this.workspace).flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Nome',
				name : 'value',
				width : 315,
				sortable : true,
				align : 'left'
			}],
			width : 350,
			height : 155,
			sortname : "value",
			sortorder : "asc",
		});
		
		$(".lstCotasEditorGrid", this.workspace).flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'key',
				width : 45,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'value',
				width : 275,
				sortable : true,
				align : 'left'
			}],
			width : 350,
			height : 155,
			sortname : "value",
			sortorder : "asc",
		});
	}
}, BaseController);

//@ sourceURL=cadastroTipoDesconto.js
