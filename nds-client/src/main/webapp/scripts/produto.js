//#workspace div.ui-tabs-panel:not(.ui-tabs-hide)

var produtoController = $.extend(true, {
	
	pesquisaProduto:null,
	
	inicializar : function (pesquisaProduto) {
		
		produtoController.aplicarMascaras();
		
		produtoController.pesquisaProduto = pesquisaProduto;
		
		this.iniciarGrid();
		$( "#tabProduto", this.workspace).tabs();
		
		$(".bt_arq", this.workspace).hide();

		$(document).ready(function(){
			
			focusSelectRefField($("#codigoProduto"));
			
			$(document.body).keydown(function(e) {
				
				if(keyEventEnterAux(e)){
					produtoController.pesquisar();
				}
				
				return true;
			});
		});
	},

	aplicarMascaras : function () {
		$("#peb", this.workspace).numeric();
		$("#pacotePadrao", this.workspace).numeric(
		    {
		    	decimal : false,
		    	negative : false
		    }
		);
		
		$("#percentualDesconto", produtoController.workspace).mask("999,99"); 
	},

	buscarValueRadio:function(radioName) {

		var valueRadio = new Array();

		$("input[type=radio][name='"+radioName+"']:checked", this.workspace).each(function() {
			valueRadio.push($(this).val());
		});

		return valueRadio;
	},
	
	buscarValueCheckBox:function(checkName) {
		return $("#"+checkName).is(":checked", this.workspace);
	},
		
	pesquisarProdutosSuccessCallBack:function() {
		
		produtoController.pesquisarFornecedor(produtoController.getCodigoProdutoPesquisa());

	},
	
	pesquisarProdutosErrorCallBack: function() {
			
		produtoController.pesquisarFornecedor(produtoController.getCodigoProdutoPesquisa());
	},

	getCodigoProdutoPesquisa: function () {
		return  {codigoProduto:$("#codigoProduto", this.workspace).val()};
	},
	
	pesquisarFornecedor:function(data){
	
		$.postJSON(contextPath + "/produto/pesquisarFornecedorProduto",
				   data, this.montarComboFornecedores);
	},

	//Mostrar auto complete por nome do produto
	autoCompletarPorNomeFornecedor : function(idFornecedor, isFromModal) {
		
		produtoController.pesquisaProduto.pesquisaRealizada = false;
		
		var nomeFornecedor = $(idFornecedor, this.workspace).val();
		
		if (nomeFornecedor && nomeFornecedor.length > 2) {
			$.postJSON(contextPath + "/produto/autoCompletarPorNomeFornecedor", {nomeFornecedor:nomeFornecedor},
					   function(result) { produtoController.pesquisaProduto.exibirAutoComplete(result, idFornecedor); },
					   null, isFromModal);
		}
	},

	montarComboFornecedores:function(result) {
		var comboFornecedores =  montarComboBox(result, true);
		
		$("#fornecedor", this.workspace).html(comboFornecedores);
	},	
	
	carregarPercentualDesconto : function() {
		
		var codigoTipoDesconto = $("#comboTipoDesconto", this.workspace).val();

		if (codigoTipoDesconto == '0') {
			$("#percentualDesconto", this.workspace).val("");
		}
		
		$.postJSON(contextPath + "/produto/carregarPercentualDesconto",
					{codigoTipoDesconto:codigoTipoDesconto}, 
					function(result) {					

						$("#percentualDesconto", this.workspace).val($.formatNumber(result, {format:"###,##000.00", locale:"br"}));
				});

	},

	iniciarGrid : function() {
		$(".produtosGrid", this.workspace).flexigrid({
			preProcess: produtoController.executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'C&oacute;digo',
				name : 'codigo',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produtoDescricao',
				width : 170,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo Produto',
				name : 'tipoProdutoDescricao',
				width : 93,
				sortable : true,
				align : 'left'
			}, {
				display : 'Editor',
				name : 'nomeEditor',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Fornecedor',
				name : 'tipoContratoFornecedor',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'PEB',
				name : 'peb',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Pcte. Padr&atilde;o',
				name : 'pacotePadrao',
				width : 65,
				sortable : true,
				align : 'center'
			}, {
				display : 'Desconto %',
				name : 'percentualDesconto',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Periodicidade',
				name : 'periodicidade',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 'auto',
			singleSelect : true
		});
	},			

	popularCombo : function(data, combo) {
		opcoes = "";
		selecionado = "-1";
		$.each(data, function(i,n){
			if (n["checked"]) {
				opcoes+="<option value="+n["value"]+" selected='selected'>"+n["label"]+"</option>";
				selecionado = n["value"];
			} else {
				opcoes+="<option value="+n["value"]+">"+n["label"]+"</option>";
			}
		});
		$(combo).clear().append(opcoes);
		$(combo).val(selecionado);
	},
	
	pesquisar : function() {
		
		var codigo = $("#codigoProduto", this.workspace).val();
		var produto = $("#produto", this.workspace).val();
		var fornecedor = $("#fornecedor", this.workspace).val();
		var editor = $("#edicao", this.workspace).val();
		var codigoTipoProduto = $("#comboTipoProduto", this.workspace).val();
		
		$(".produtosGrid", this.workspace).flexOptions({
			url: contextPath + "/produto/pesquisarProdutos",
			params: [{name:'codigo', value: codigo },
				     {name:'produto', value: produto },
				     {name:'fornecedor', value: fornecedor },
				     {name:'editor', value: editor },
				     {name:'codigoTipoProduto', value : codigoTipoProduto}],
			newp: 1,
		});
		
		$(".produtosGrid", this.workspace).flexReload();
	},
	
	editarProduto : function(id) {
		
		this.carregarNovoProduto(function() {
				produtoController.limparModalCadastro();
				produtoController.carregarProdutoEditado(id);		
				

				$("#dialog-novo", this.workspace).dialog({
					resizable: false,
					height:550,
					width:850,
					modal: true,
					title:"Edição de Produto",
					buttons: {
						"Confirmar": function() {

							produtoController.salvarProduto();
						},
						"Cancelar": function() {
							$( this ).dialog( "close" );
						}
					},
					beforeClose: function() {
						produtoController.limparModalCadastro();
						clearMessageDialogTimeout('dialogMensagemNovo');
					},
					form: $("#dialog-novo", this.workspace).parents("form")
				});
				
				$("#codigoProdutoCadastro", this.workspace).disable();		
		});
		
		
	},
	
	habilitarDesabilitarCamposInterface : function(habilitar) {
		
		$(".habilitarCampoInterface", produtoController.workspace).attr('disabled',!habilitar);
		
		$(".habilitarCampoInterfaceSegmentacao", produtoController.workspace).attr('disabled',!habilitar);
	},
	
	carregarProdutoEditado : function(id) {

		$.postJSON(contextPath + "/produto/carregarProdutoParaEdicao", 
				   	{id:id},
				   	function(result) {
			   
						$("#idProduto", produtoController.workspace).val(result.id);
						$("#codigoProdutoCadastro", produtoController.workspace).val(result.codigo);
						$("#nomeProduto", produtoController.workspace).val(result.nome);
						$("#sloganProduto", produtoController.workspace).val(result.slogan);
						$("#peb", produtoController.workspace).val(result.peb);
						$("#pacotePadrao", produtoController.workspace).val(result.pacotePadrao);
						$("#comboPeriodicidade", produtoController.workspace).val(result.periodicidade);
						$("#grupoEditorial", produtoController.workspace).val(result.grupoEditorial);
						$("#subGrupoEditorial", produtoController.workspace).val(result.subGrupoEditorial);
						$("#comboEditor", produtoController.workspace).val(result.codigoEditor);
						$("#comboFornecedoresCadastro", produtoController.workspace).val(result.codigoFornecedor);

						produtoController.habilitarDesabilitarCamposInterface(!(result.origem == "INTERFACE"));
						
						$("#comboTipoDesconto", produtoController.workspace).val(result.tipoDesconto);
						$("#comboTipoProdutoCadastro", produtoController.workspace).val(result.codigoTipoProduto);
						$("#segmentacaoClasseSocial", produtoController.workspace).val(result.classeSocial);
						$("#segmentacaoSexo", produtoController.workspace).val(result.sexo);
						$("#segmentacaoFaixaEtaria", produtoController.workspace).val(result.faixaEtaria);
						$("#segmentacaoFormato", produtoController.workspace).val(result.formatoProduto);
						$("#segmentacaoTipoLancamento", produtoController.workspace).val(result.tipoLancamento);
						$("#segmentacaoTemaPrincipal", produtoController.workspace).val(result.temaPrincipal);
						$("#segmentacaoTemaSecundario", produtoController.workspace).val(result.temaSecundario);
						
						$("#percentualDesconto", produtoController.workspace).val($.formatNumber(result.desconto, {format:"###,##000.00", locale:"br"}));

						if (result.formaComercializacao == 'CONTA_FIRME') {
							$("#formaComercializacaoContaFirme", this.workspace).attr('checked', true);
						} else if (result.formaComercializacao == 'CONSIGNADO') {
							$("#formaComercializacaoConsignado", this.workspace).attr('checked', true);
						}
						
						if (result.tributacaoFiscal == 'TRIBUTADO') {
							$("#radioTributado", this.workspace).attr('checked', true);
						} else if (result.tributacaoFiscal == 'ISENTO') {
							$("#radioIsento", this.workspace).attr('checked', true);
						} else if (result.tributacaoFiscal == 'OUTROS') {
							$("#radioTributacaoOutros", this.workspace).attr('checked', true);
						}
						
						if (!(result.origem == "INTERFACE")){
							
							produtoController.carregarComboDesconto("MANUAL",result.tipoDesconto);
							$("#comboTipoDesconto", produtoController.workspace).hide();
							$("#tipoDescontoManual", produtoController.workspace).show();
							$("#tipoDescontoManual", produtoController.workspace).val(result.descricaoDescontoManual);
							$("#percentualDesconto", produtoController.workspace).removeAttr('disabled','disabled');
						}
						else{
							
							produtoController.carregarComboDesconto("INTERFACE",result.tipoDesconto);
							$("#percentualDesconto", produtoController.workspace).attr('disabled','disabled');
							$("#comboTipoDesconto", produtoController.workspace).show();
							$("#tipoDescontoManual", produtoController.workspace).hide();
						}	
					},
					null,
					true
				);
	},
	
	carregarComboDesconto : function(origemProduto, tipoDesconto){
		
		$.postJSON(contextPath + "/produto/carregarDadosDesconto",
					{origemProduto:origemProduto},
					function (result) {
						produtoController.popularCombo(result, $("#comboTipoDesconto", this.workspace));
						$("#comboTipoDesconto", this.workspace).val(tipoDesconto);
					},
				  	null,
				   	true
			);
	},
	
	removerProduto : function(id) {

		$("#dialog-excluir", this.workspace).dialog( {
			resizable : false,
			height : 'auto',
			width : 450,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$("#dialog-excluir", this.workspace).dialog("close");
					
					$.postJSON(contextPath + "/produto/removerProduto", 
							   {id:id},
							   function(result) {
							   		
									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										
										exibirMensagem(tipoMensagem, listaMensagens);
									}

									$(".filtro", this.workspace).each(function() {  
										$("input[type='text'], select", this.workspace).val(""); 
									});
									
									$(".produtosGrid", this.workspace).flexReload();
							   },
							   null,
							   true
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout('dialogMensagemNovo');
			},
			form: $("#dialog-excluir", this.workspace).parents("form")
		});
	},
	
	novoProduto : function () {
		
		this.carregarNovoProduto(function(){
			produtoController.limparModalCadastro();
			produtoController.prepararNovoProduto();
			});
	},
	
	prepararNovoProduto : function() {
		
		$("#dialog-novo", this.workspace).dialog({
			resizable: false,
			height:550,
			width:850,
			modal: true,
			title:"Novo Produto",
			buttons: {
				"Confirmar": function() {

					produtoController.salvarProduto();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function() {
				produtoController.limparModalCadastro();
				clearMessageDialogTimeout('dialogMensagemNovo', this.workspace);
			},
			form: $("#dialog-novo", this.workspace).parents("form")
		});

		produtoController.habilitarDesabilitarCamposInterface(true);
		
		$("#codigoProdutoCadastro", this.workspace).enable();
		$("#comboTipoDesconto", produtoController.workspace).hide();
		$("#tipoDescontoManual", produtoController.workspace).show();
		$("#percentualDesconto", produtoController.workspace).removeAttr('disabled');
		
	},

	carregarNovoProduto : function(callback) {

		$.postJSON(contextPath + "/produto/carregarDadosProduto",
					null,
					function (result) {

						produtoController.popularCombo(result[0], $("#comboTipoProdutoCadastro", this.workspace));
						produtoController.popularCombo(result[1], $("#comboFornecedoresCadastro", this.workspace));
						produtoController.popularCombo(result[2], $("#comboEditor", this.workspace));
					
						if (callback) {
							callback();
						}
					},
				  	null,
				   	true
			);
	},

	limparModalCadastro : function() {

		$("#idProduto", this.workspace).val("");
		$("#codigoProdutoCadastro", this.workspace).val("");
		$("#nomeProduto", this.workspace).val("");
		$("#sloganProduto", this.workspace).val("");
		$("#peb", this.workspace).val("");
		$("#pacotePadrao", this.workspace).val("");
		$("#comboPeriodicidade", this.workspace).val("");
		$("#tipoDescontoManual", this.workspace).val("");

		$("#formaComercializacaoContaFirme", this.workspace).attr('checked', false);
		$("#formaComercializacaoConsignado", this.workspace).attr('checked', false);
					
		$("#radioTributado", this.workspace).attr('checked', false);
		$("#radioIsento", this.workspace).attr('checked', false);
		$("#radioTributacaoOutros", this.workspace).attr('checked', false);
		
		$("#percentualDesconto", this.workspace).val("");
		$("#grupoEditorial", this.workspace).val("");
		$("#subGrupoEditorial", this.workspace).val("");
		
		$("#segmentacaoClasseSocial", this.workspace).val("");
		$("#segmentacaoSexo", this.workspace).val("");
		$("#segmentacaoFaixaEtaria", this.workspace).val("");
		$("#segmentacaoFormato", this.workspace).val("");
		$("#segmentacaoTipoLancamento", this.workspace).val("");
		$("#segmentacaoTemaPrincipal", this.workspace).val("");
		$("#segmentacaoTemaSecundario", this.workspace).val("");
	},
	
	salvarProduto : function() {

		 var params = [{name:"produto.id",value:$("#idProduto", produtoController.workspace).val()},
        			   {name:"produto.codigo",value:$("#codigoProdutoCadastro", produtoController.workspace).val()},
        			   {name:"produto.nome",value:$("#nomeProduto", produtoController.workspace).val()},
        			   {name:"produto.peb",value:$("#peb", produtoController.workspace).val()},
        			   {name:"produto.pacotePadrao",value:$("#pacotePadrao", produtoController.workspace).val()},
        			   {name:"produto.slogan",value:$("#sloganProduto", produtoController.workspace).val()},
        			   {name:"produto.periodicidade",value:$("#comboPeriodicidade", produtoController.workspace).val()},
        			   {name:"produto.formaComercializacao",value:this.buscarValueRadio('formaComercializacao', produtoController.workspace)},
        			   {name:"produto.tributacaoFiscal",value:this.buscarValueRadio('radioTributacaoFiscal', produtoController.workspace)},
        			   {name:"produto.grupoEditorial",value:$("#grupoEditorial", produtoController.workspace).val()},
        			   {name:"produto.subGrupoEditorial",value:$("#subGrupoEditorial", produtoController.workspace).val()},	
        			   {name:"produto.segmentacao.classeSocial",value:$("#segmentacaoClasseSocial", produtoController.workspace).val()},
        			   {name:"produto.segmentacao.sexo",value:$("#segmentacaoSexo", produtoController.workspace).val()},
        			   {name:"produto.segmentacao.faixaEtaria",value:$("#segmentacaoFaixaEtaria", produtoController.workspace).val()},
        			   {name:"produto.segmentacao.formatoProduto",value:$("#segmentacaoFormato", produtoController.workspace).val()},
        			   {name:"produto.segmentacao.tipoLancamento",value:$("#segmentacaoTipoLancamento", produtoController.workspace).val()},
        			   {name:"produto.segmentacao.temaPrincipal",value:$("#segmentacaoTemaPrincipal", produtoController.workspace).val()},
        			   {name:"produto.segmentacao.temaSecundario",value:$("#segmentacaoTemaSecundario", produtoController.workspace).val()},
        			   {name:"codigoEditor",value:$("#comboEditor", produtoController.workspace).val()},
        			   {name:"codigoFornecedor",value:$("#comboFornecedoresCadastro", produtoController.workspace).val()},
        			   {name:"codigoTipoDesconto",value:$("#comboTipoDesconto", produtoController.workspace).val()},
        			   {name:"codigoTipoProduto",value:$("#comboTipoProdutoCadastro", produtoController.workspace).val()},
        			   {name:"produto.desconto",value:floatValue( $("#percentualDesconto", produtoController.workspace).val())},
        			   {name:"produto.descricaoDesconto",value:$("#tipoDescontoManual", produtoController.workspace).val()}];
 
		$.postJSON(contextPath + "/produto/salvarProduto",  
			   	params,
			   	function (result) {

					var tipoMensagem = result.tipoMensagem;
					
					var listaMensagens = result.listaMensagens;
					
					if (tipoMensagem && listaMensagens) {
						
						exibirMensagem(tipoMensagem, listaMensagens);
					} 

					if (tipoMensagem == 'SUCCESS') {

						$("#dialog-novo", this.workspace).dialog( "close" );

						$(".produtosGrid", this.workspace).flexReload();
					}
					
				},
			  	null,
			   	true,
			   	'dialogMensagemNovo'
		);
	},
	
	executarPreProcessamento : function(resultado) {
		
		if (resultado.mensagens) {

			$(".bt_arq", this.workspace).hide();
			$(".grids", this.workspace).hide();
			
			exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
			);
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			if(!row.cell.tipoContratoFornecedor){
				row.cell.tipoContratoFornecedor = '';
			}
			
			var linkAprovar = '<a href="javascript:;" onclick="produtoController.editarProduto(' + row.cell.id + ');" style="cursor:pointer">' +
					     	  	'<img title="Editar" src="' + contextPath +'/images/ico_editar.gif" style="margin-right:10px" border="0px" />' +
					  		  '</a>';
			
			var linkExcluir = '<a href="javascript:;" onclick="produtoController.removerProduto(' + row.cell.id + ');" style="cursor:pointer">' +
							   	 '<img title="Excluir" src="' + contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" />' +
							   '</a>';
			
			row.cell.acao = linkAprovar + linkExcluir;
		});
			
		$(".grids", this.workspace).show();
		
		$(".bt_arq", this.workspace).show();
		
		return resultado;
	},	
	
	carregarTipoDescontoProduto : function(callback) {
		if (callback) {
			callback();
		}
	},

}, BaseController);

//@ sourceURL=produto.js
