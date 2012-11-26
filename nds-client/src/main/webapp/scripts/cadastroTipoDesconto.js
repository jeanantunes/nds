var tipoDescontoController = $.extend(true,  {
	
	tipoDescontoSelecionado:"",
	
	pesquisar:function () {
		
		if(tipoDescontoController.tipoDescontoSelecionado == 'GERAL'){
			tipoDescontoController.pesquisarDescontoGeral();						
		}else if(tipoDescontoController.tipoDescontoSelecionado == 'ESPECIFICO'){
			tipoDescontoController.pesquisarDescontoEspecifico();
		}else if (tipoDescontoController.tipoDescontoSelecionado == 'PRODUTO') {
			tipoDescontoController.pesquisarDescontoProduto();
		}else{
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
	
	pesquisarDescontoEspecifico:function(){
		 
		var cotaEspecifica = $("#numCotaPesquisa",this.workspace).val();
		var nomeEspecifico = $("#descricaoCotaPesquisa",this.workspace).val();
		
		$(".tiposDescEspecificoGrid",this.workspace).flexOptions({
			url: contextPath + "/financeiro/tipoDescontoCota/pesquisarDescontoEspecifico",
			params: [
					 {name:'cotaEspecifica', value:cotaEspecifica},
			         {name:'nomeEspecifico', value:nomeEspecifico}
			         ],
		    newp: 1,
		});
		
		$(".tiposDescEspecificoGrid",this.workspace).flexReload();
	},
	
	pesquisarDescontoProduto:function(){
		var codigo = $("#codigoPesquisa",this.workspace).val();
		var produto = $("#produtoPesquisa",this.workspace).val();
		
		$(".tiposDescProdutoGrid",this.workspace).flexOptions({
			url: contextPath +"/financeiro/tipoDescontoCota/pesquisarDescontoProduto",
			params: [
					 {name:'codigo', value:codigo},
			         {name:'produto', value:produto}
			         ],
		    newp: 1,
		});
		
		$(".tiposDescProdutoGrid",this.workspace).flexReload();
	},
			
	mostra_geral:function(){
		
		tipoDescontoController.tipoDescontoSelecionado = "GERAL";
		
		$( '#tpoGeral' ,this.workspace).show();
		$( '#tpoEspecifico' ,this.workspace).hide();
		$( '#tpoProduto' ,this.workspace).hide();
		$( '.especifico' ,this.workspace).hide();
		$( '.produto' ,this.workspace).hide();
		
		tipoDescontoController.exibirExportacao(false);
		
		$(".grids",this.workspace).show();
	},
	
	mostra_especifico:function(){
		
		tipoDescontoController.tipoDescontoSelecionado = "ESPECIFICO";
		
		$( '#tpoGeral' ,this.workspace).hide();
		$( '#tpoEspecifico' ,this.workspace).show();
		$( '.especifico' ,this.workspace).show();
		$( '#tpoProduto' ,this.workspace).hide();
		$( '.produto' ,this.workspace).hide();
		
		tipoDescontoController.exibirExportacao(false);
		
		$(".grids",this.workspace).show();
	},
	
	mostra_produto:function(){
		
		tipoDescontoController.tipoDescontoSelecionado = "PRODUTO";
		
		$( '#tpoGeral' ,this.workspace).hide();
		$( '#tpoEspecifico' ,this.workspace).hide();
		$( '.especifico' ,this.workspace).hide();
		$( '#tpoProduto' ,this.workspace).show();
		$( '.produto' ,this.workspace).show();
		
		tipoDescontoController.exibirExportacao(false);
		
		$(".grids",this.workspace).show();
	},
	
	exibirExportacao:function(isExibir){
		
		if(tipoDescontoController.tipoDescontoSelecionado =="PRODUTO"){
			if(isExibir == true){
				$("#idExportacaoProduto",this.workspace).show();	
			}else{
				$("#idExportacaoProduto",this.workspace).hide();
			}
		}
		
		if(tipoDescontoController.tipoDescontoSelecionado =="GERAL"){
			if(isExibir == true){
				$("#idExportacaoGeral",this.workspace).show();	
			}else{
				$("#idExportacaoGeral",this.workspace).hide();
			}
		}
		
		if(tipoDescontoController.tipoDescontoSelecionado =="ESPECIFICO"){
			if(isExibir == true){
				$("#idExportacaoEspecifico",this.workspace).show();	
			}else{
				$("#idExportacaoEspecifico",this.workspace).hide();
			}
		}
	},
	
	executarPreProcessamento:function(resultado) {				
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids",this.workspace).hide();
			
			tipoDescontoController.exibirExportacao(false);
			
			if(tipoDescontoController.tipoDescontoSelecionado == "PRODUTO"){
				tipoDescontoController.mostra_produto();
			}else if (tipoDescontoController.tipoDescontoSelecionado == "GERAL"){
				tipoDescontoController.mostra_geral();
			}else{
				tipoDescontoController.mostra_especifico();
			}

			return resultado;
		}

		$.each(resultado.rows, function(index, row) {					

			if(tipoDescontoController.tipoDescontoSelecionado == "PRODUTO"){

				var linkCotas = '<a href="javascript:;" onclick="descontoProdutoController.exibirDialogCotasProdutoEdicao(' + row.cell.idTipoDesconto + ');" style="cursor:pointer">' +
							    row.cell.nomeProduto +
							    '</a>';
							    
				row.cell.nomeProduto = linkCotas;
				
			} else{
				
				if(row.cell.fornecedor=="Diversos"){
					var linkFornecedores = '<a href="javascript:;" onclick="tipoDescontoController.carregarFornecedoresAssociadosADesconto('+ row.cell.idTipoDesconto +');">Diversos</a>';
						
					row.cell.fornecedor = linkFornecedores; 
				}
			}
			
			if(row.cell.numeroEdicao == null) row.cell.numeroEdicao = '*';
			
			var linkExcluir = '<a href="javascript:;" onclick="tipoDescontoController.exibirDialogExclusao(' + row.cell.idTipoDesconto + ');" style="cursor:pointer">' +
							   	 '<img title="Excluir Desconto" src="'+contextPath+'/images/ico_excluir.gif" hspace="5" border="0px" />' +
							   '</a>';
			
			row.cell.acao = linkExcluir;
		});
		
		$(".grids",this.workspace).show();
		
		tipoDescontoController.exibirExportacao(true);
		
		return resultado;
	},

	exibirDialogExclusao:function(idDesconto){		
		$("#dialog-excluirCota" ,this.workspace).dialog({
			resizable: false,
			height:'auto',
			width:250,
			modal: true,
			buttons: [{
						id:"id_confirmar_exclusao",text:"Confirmar",
						click: function() {
		
							tipoDescontoController.excluirDesconto(idDesconto,tipoDescontoController.tipoDescontoSelecionado);
									
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
	
	carregarFornecedoresAssociadosADesconto:function(idDesconto){
		
		var param = [{name:"idDesconto",value:idDesconto},{name:"tipoDesconto",value:tipoDescontoController.tipoDescontoSelecionado}];
		
		$(".lstFornecedoresGrid",this.workspace).flexOptions({
			url: contextPath +"/financeiro/tipoDescontoCota/obterFornecedoresAssociadosDesconto",
			params: param,
		    newp: 1,
		    sortorder:'asc'
		});
		
		$(".lstFornecedoresGrid",this.workspace).flexReload();
		
		tipoDescontoController.popup_lista_fornecedores();
	},
	
	popup_lista_fornecedores:function() {
		
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
	
	init:function(){
		$("#produto",this.workspace).autocomplete({source: ""});		
		
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
	}
}, BaseController);
