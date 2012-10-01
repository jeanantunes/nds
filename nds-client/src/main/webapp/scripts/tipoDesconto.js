var tipoDescontoController = $.extend(true, {

	init : function() {
	
		$(".tiposDescGeralGrid", tipoDescontoController.workspace).flexigrid({
			preProcess: executarPreProcessamento,
				dataType : 'json',
				colModel : [ {
					display : '',
					name : 'seq',
					width : 60,
					sortable : true,
					align : 'center'
				},{
					display : 'Desconto %',
					name : 'desconto',
					width : 120,
					sortable : true,
					align : 'center'
				}, {
					display : 'Data Alteração',
					name : 'dtAlteracao',
					width : 150,
					sortable : true,
					align : 'center'
				}, {
					display : 'Usuário',
					name : 'usuario',
					width : 502,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'usuario',
					width : 30,
					sortable : true,
					align : 'center'
				}],
				sortname : "seq",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 'auto'
			});
			
			$(".tiposDescEspecificoGrid", tipoDescontoController.workspace).flexigrid({
				url : '../xml/tipos-desconto-especifico-xml.xml',
				dataType : 'xml',
				colModel : [ {
					display : 'Cota',
					name : 'cota',
					width : 60,
					sortable : true,
					align : 'left'
				},{
					display : 'Nome',
					name : 'nome',
					width : 350,
					sortable : true,
					align : 'left'
				}, {
					display : 'Desconto %',
					name : 'desconto',
					width : 150,
					sortable : true,
					align : 'center'
				}, {
					display : 'Data Alteração',
					name : 'dtAlteracao',
					width : 120,
					sortable : true,
					align : 'center'			}, {
					display : 'Usuário',
					name : 'usuario',
					width : 150,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'usuario',
					width : 30,
					sortable : true,
					align : 'center'
				}],
				sortname : "cota",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 'auto'
			});
			
			$(".tiposDescProdutoGrid", tipoDescontoController.workspace).flexigrid({
				url : '../xml/tipos-desconto-produto-xml.xml',
				dataType : 'xml',
				colModel : [ {
					display : 'Código',
					name : 'codigo',
					width : 70,
					sortable : true,
					align : 'left'
				},{
					display : 'Produto',
					name : 'produto',
					width : 228,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'edicao',
					width : 100,
					sortable : true,
					align : 'center'
				}, {
					display : 'Desconto %',
					name : 'desconto',
					width : 150,
					sortable : true,
					align : 'center'
				}, {
					display : 'Data Alteração',
					name : 'dtAlteracao',
					width : 120,
					sortable : true,
					align : 'center'			
				}, {
					display : 'Usuário',
					name : 'usuario',
					width : 150,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'usuario',
					width : 30,
					sortable : true,
					align : 'center'
				}],
				sortname : "codigo",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 'auto'
			});

			$("#produto", tipoDescontoController.workspace).autocomplete({source: ""});		
			$("#descontoGeral", tipoDescontoController.workspace).mask("99.99");
			$("#descontoEspecifico", tipoDescontoController.workspace).mask("99.99");
			$("#descontoProduto", tipoDescontoController.workspace).mask("99.99");		

	},
	
	buscarNomeProduto : function(){
		if ($("#codigo", tipoDescontoController.workspace).val().length > 0){
			var data = "codigoProduto=" + $("#codigo", tipoDescontoController.workspace).val();
			$.postJSON(contextPath + "/lancamento/furoProduto/buscarNomeProduto", data,
				function(result){
					if (result){
						$("#produto", tipoDescontoController.workspace).val(result);	
						$("#descontoProduto", tipoDescontoController.workspace).focus();
					} else {
						$("#produto", tipoDescontoController.workspace).val("");
						$("#produto", tipoDescontoController.workspace).focus();
					}
				}
			);
		}
	},
	
	pesquisarPorNomeProduto : function(){
		var produto = $("#produto", tipoDescontoController.workspace).val();
		
		if (produto && produto.length > 0){
			$.postJSON(contextPath + "/lancamento/furoProduto/pesquisarPorNomeProduto", "nomeProduto=" + produto, exibirAutoComplete);
		}
	},
	
	exibirAutoComplete : function(result){
		$("#produto", tipoDescontoController.workspace).autocomplete({
			source: result,
			select: function(event, ui){
				tipoDescontoController.completarPesquisa(ui.item.chave);
			}
		});
	},
	
	completarPesquisa : function(chave){
		$("#codigo", tipoDescontoController.workspace).val(chave.codigoProduto);	
	},
	
	
	popup_geral : function() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
			
			tipoDescontoController.limparTelaCadastro();
		
			$( "#dialog-geral", tipoDescontoController.workspace ).dialog({
				resizable: false,
				height:230,
				width:400,
				modal: true,
				buttons: {
					"Confirmar": function() {
						var geral = $("#radioGeral", tipoDescontoController.workspace).is(":checked");
						var especifico = $("#radioEspecifico", tipoDescontoController.workspace).is(":checked");
						var produto = $("#radioProduto", tipoDescontoController.workspace).is(":checked");
						if(geral){
							tipoDescontoController.novoDescontoGeral();							
						}else if(especifico){
							
						}else if(produto){
							
						}	

					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
	},
	
	novoDescontoGeral : function() {

		var descontoGeral = $("#descontoGeral", tipoDescontoController.workspace).val();
		var dataAlteracao = $("#dataAlteracaoGeral", tipoDescontoController.workspace).val();
		var usuario = $("#textfield24", tipoDescontoController.workspace).val();		
		
		$.postJSON(contextPath + "/administracao/tipoDescontoCota/novoDescontoGeral",
				   "desconto="+descontoGeral+
				   "&dataAlteracao="+ dataAlteracao +
				   "&usuario="+ usuario,
				   function(result) {
					   tipoDescontoController.fecharDialogs();
					   var tipoMensagem = result.tipoMensagem;
					   var listaMensagens = result.listaMensagens;
					   if (tipoMensagem && listaMensagens) {
					       exibirMensagem(tipoMensagem, listaMensagens);
				       }
					   tipoDescontoController.pesquisar();
	               },
				   null,
				   true);

		$(".tiposDescGeralGrid", tipoDescontoController.workspace).flexReload();
		
	},

	novoDescontoEspecifico : function() {
		
		var cotaEspecifica = $("#cotaEspecifica", tipoDescontoController.workspace).val();
		var nomeEspecifico = $("#nomeEspecifico", tipoDescontoController.workspace).val();
		var descontoEspecifico = $("#descontoEspecifico", tipoDescontoController.workspace).val();
		var dataAlteracaoEspecifico = $("#dataAlteracaoEspecifico", tipoDescontoController.workspace).val();
		var usuarioEspecifico = $("#usuarioEspecifico", tipoDescontoController.workspace).val()
		
		$.postJSON(contextPath + "/administracao/tipoDescontoCota/novoDescontoEspecifico",
				   "cotaEspecifica="+cotaEspecifica+
				   "&nomeEspecifico="+ nomeEspecifico +
				   "&descontoEspecifico="+ descontoEspecifico +
				   "&dataAlteracaoEspecifico="+ dataAlteracaoEspecifico +
				   "&usuarioEspecifico="+ usuarioEspecifico,
				   function(result) {
					   tipoDescontoController.fecharDialogs();
					   var tipoMensagem = result.tipoMensagem;
					   var listaMensagens = result.listaMensagens;
					   if (tipoMensagem && listaMensagens) {
					       exibirMensagem(tipoMensagem, listaMensagens);
				       }
					   tipoDescontoController.mostrarGridConsulta();
	               },
				   null,
				   true);
		
	},

	fecharDialogs : function() {
		$( "#dialog-geral", tipoDescontoController.workspace ).dialog( "close" );
	}, 
	
	limparTelaCadastro : function() {
		$("#descontoGeral", tipoDescontoController.workspace).val("");				
	},
	
	popup_especifico : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-especifico", tipoDescontoController.workspace ).dialog({
			resizable: false,
			height:300,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	},
	
	popup_produto : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-produto", tipoDescontoController.workspace ).dialog({
			resizable: false,
			height:320,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	},
	
	mostra_geral : function(){
		$( '#tpoGeral',tipoDescontoController.workspace ).show();
		$( '#tpoEspecifico', tipoDescontoController.workspace ).hide();
		$( '#tpoProduto', tipoDescontoController.workspace ).hide();
		$( '.especifico', tipoDescontoController.workspace ).hide();
		$( '.produto', tipoDescontoController.workspace ).hide();
	},

	mostra_especifico : function(){
		$( '#tpoGeral', tipoDescontoController.workspace ).hide();
		$( '#tpoEspecifico', tipoDescontoController.workspace ).show();
		$( '.especifico', tipoDescontoController.workspace ).show();
		$( '#tpoProduto', tipoDescontoController.workspace ).hide();
		$( '.produto', tipoDescontoController.workspace ).hide();
	},
	
	mostra_produto : function(){
		$( '#tpoGeral', tipoDescontoController.workspace ).hide();
		$( '#tpoEspecifico', tipoDescontoController.workspace ).hide();
		$( '.especifico', tipoDescontoController.workspace ).hide();
		$( '#tpoProduto', tipoDescontoController.workspace ).show();
		$( '.produto', tipoDescontoController.workspace ).show();
	},
	
	pesquisar : function() {
		
		var descontoGeral = $("#descontoGeral", tipoDescontoController.workspace).val();
		var dataAlteracao = $("#dataAlteracaoGeral", tipoDescontoController.workspace).val();
		var usuario = $("#textfield24", tipoDescontoController.workspace).val();		
		
		$(".tiposDescGeralGrid", tipoDescontoController.workspace).flexOptions({
			url: contextPath + "/administracao/tipoDescontoCota/pesquisarDescontoGeral",
			params: [],
		    newp: 1,
		});
		
		$(".tiposDescGeralGrid", tipoDescontoController.workspace).flexReload();
	},
	
	executarPreProcessamento : function(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", tipoDescontoController.workspace).hide();

			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var linkAprovar = '<a href="javascript:;" onclick="tipoDescontoController.aprovarMovimento(' + row.cell.id + ');" style="cursor:pointer">' +
					     	  	'<img title="Aprovar" src="' + contextPath + '/images/ico_check.gif" hspace="5" border="0px" />' +
					  		  '</a>';
			
			var linkRejeitar = '<a href="javascript:;" onclick="tipoDescontoController.rejeitarMovimento(' + row.cell.id + ');" style="cursor:pointer">' +
							   	 '<img title="Rejeitar" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
							   '</a>';
			
			row.cell.acao = linkAprovar + linkRejeitar;
		});
		
		$(".grids", tipoDescontoController.workspace).show();
		
		return resultado;
	}

}, BaseController);
//@ sourceURL=tipoDesconto.js