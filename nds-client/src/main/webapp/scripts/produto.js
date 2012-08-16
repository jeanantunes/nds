//#workspace div.ui-tabs-panel:not(.ui-tabs-hide)
var produtoController = $.extend(true, {
	
	inicializar : function () {
		this.iniciarGrid();
		$( "#tabProduto", this.workspace).tabs();

	},

	aplicarMascaras : function () {
		$("#peb", this.workspace).numeric();
		$("#pacotePadrao", this.workspace).numeric();
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
		return  "codigoProduto=" + $("#codigoProduto", this.workspace).val();
	},
	
	pesquisarFornecedor:function(data){
	
		$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/pesquisarFornecedor",
				   data, this.montarComboFornecedores);
	},

	//Mostrar auto complete por nome do produto
	autoCompletarPorNomeFornecedor : function(idFornecedor, isFromModal) {
		
		produtoController.pesquisaRealizada = false;
		
		var nomeFornecedor = $(idFornecedor).val();
		
		if (nomeFornecedor && nomeFornecedor.length > 2) {
			$.postJSON(contextPath + "/produto/autoCompletarPorNomeFornecedor", "nomeFornecedor=" + nomeFornecedor,
					   function(result) { produtoController.exibirAutoComplete(result, idFornecedor); },
					   null, isFromModal);
		}
	},

	montarComboFornecedores:function(result) {
		var comboFornecedores =  montarComboBox(result, true);
		
		$("#fornecedor", this.workspace).html(comboFornecedores);
	},
	
	validarEdicaoSuccessCallBack : function(){
		
		 var data = [{name:"codigoProduto",value:$("#codigoProduto", this.workspace).val()},
         			 {name:"numeroEdicao",value:$("#edicao", this.workspace).val()},
					];
		
		 $.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/pesquisarDataProgramada",
				   data, function(result) {
			 $("#dataProgramada", this.workspace).val(result);
		 });
	},
	
	validarEdicaoErrorCallBack: function() {
		 $("#dataProgramada", this.workspace).val("");
	},	
	
	carregarPercentualDesconto : function() {
		
		var codigoTipoDesconto = $("#comboTipoDesconto", this.workspace).val();

		if (codigoTipoDesconto == '0') {
			$("#percentualDesconto", this.workspace).val("");
		}
		
		$.postJSON(contextPath + "/produto/carregarPercentualDesconto",
					"codigoTipoDesconto=" + codigoTipoDesconto, 
					function(result) {

						if (result == 0) {
							result = "";
						}

						$("#percentualDesconto", this.workspace).val(result);
				});

	},

	iniciarGrid : function() {
		$(".produtosGrid", this.workspace).flexigrid({
			preProcess: produtoController.executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'C&oacute;digo',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produtoDescricao',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo Produto',
				name : 'tipoProdutoDescricao',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Editor',
				name : 'nomeEditor',
				width : 190,
				sortable : true,
				align : 'left'
			}, {
				display : 'Fornecedor',
				name : 'tipoContratoFornecedor',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'PEB',
				name : 'peb',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Situa&ccedil;&atilde;o',
				name : 'situacao',
				width : 60,
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
			height : 255,
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
		var periodicidade = $("#periodicidade", this.workspace).val();
		var fornecedor = $("#fornecedor", this.workspace).val();
		var editor = $("#edicao", this.workspace).val();
		var codigoTipoProduto = $("#comboTipoProduto", this.workspace).val();
		
		$(".produtosGrid").flexOptions({
			url: contextPath + "/produto/pesquisarProdutos",
			params: [{name:'codigo', value: codigo },
				     {name:'produto', value: produto },
				     {name:'fornecedor', value: fornecedor },
				     {name:'editor', value: editor },
				     {name:'codigoTipoProduto', value : codigoTipoProduto}],
			newp: 1,
		});
		
		$(".produtosGrid").flexReload();
	},
	
	editarProduto : function(id) {

		$("#dialog-novo", this.workspace).dialog({
			resizable: false,
			height:550,
			width:850,
			modal: true,
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
		
		this.carregarNovoProduto(
			function() {
				produtoController.limparModalCadastro();
				produtoController.carregarProdutoEditado(id);		
			}
		);
	},
	
	carregarProdutoEditado : function(id) {

		$.postJSON(contextPath + "/produto/carregarProdutoParaEdicao", 
				   	"id=" + id,
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
						$("#comboTipoDesconto", produtoController.workspace).val(result.tipoDesconto);
						$("#comboTipoProdutoCadastro", produtoController.workspace).val(result.codigoTipoProduto);
						$("#segmentacaoClasseSocial", produtoController.workspace).val(result.classeSocial);
						$("#segmentacaoSexo", produtoController.workspace).val(result.sexo);
						$("#segmentacaoFaixaEtaria", produtoController.workspace).val(result.faixaEtaria);
						$("#segmentacaoFormato", produtoController.workspace).val(result.formatoProduto);
						$("#segmentacaoTipoLancamento", produtoController.workspace).val(result.tipoLancamento);
						$("#segmentacaoTemaPrincipal", produtoController.workspace).val(result.temaPrincipal);
						$("#segmentacaoTemaSecundario", produtoController.workspace).val(result.temaSecundario);

						if (result.formaComercializacao == 'Conta Firme') {
							$("#formaComercializacaoContaFirme").attr('checked', true);
						} else if (result.formaComercializacao == 'Consignado') {
							$("#formaComercializacaoConsignado").attr('checked', true);
						}
						
						if (result.tributacaoFiscal == 'TRIBUTADO') {
							$("#radioTributado", this.workspace).attr('checked', true);
						} else if (result.tributacaoFiscal == 'ISENTO') {
							$("#radioIsento", this.workspace).attr('checked', true);
						} else if (result.tributacaoFiscal == 'OUTROS') {
							$("#radioTributacaoOutros", this.workspace).attr('checked', true);
						}

						produtoController.carregarTipoDescontoProduto(produtoController.carregarPercentualDesconto);							
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
					
					$.postJSON(contextPath + "/produto/removerProduto", 
							   "id=" + id,
							   function(result) {
							   		
							   		$("#dialog-excluir", this.workspace).dialog("close");
							   		
									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										
										exibirMensagem(tipoMensagem, listaMensagens);
									}
											
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
			form: $("#dialog-novo", this.workspace).parents("form")
		});
	},
	
	novoProduto : function () {

		$("#dialog-novo", this.workspace).dialog({
			resizable: false,
			height:550,
			width:850,
			modal: true,
			buttons: {
				"Confirmar": function() {

					produtoController.salvarProduto();
					
			   		$(".produtosGrid", this.workspace).flexReload();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function() {
				produtoController.limparModalCadastro();
				clearMessageDialogTimeout('dialogMensagemNovo', this.workspace);
			}
		});

		this.carregarNovoProduto(this.limparModalCadastro);
	},

	carregarNovoProduto : function(callback) {

		$.postJSON(contextPath + "/produto/carregarDadosProduto",
					null,
					function (result) {

						produtoController.popularCombo(result[0], $("#comboTipoProdutoCadastro", this.workspace));
						produtoController.popularCombo(result[1], $("#comboFornecedoresCadastro", this.workspace));
						produtoController.popularCombo(result[2], $("#comboEditor", this.workspace));
						produtoController.popularCombo(result[3], $("#comboTipoDesconto", this.workspace));

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

		$("#formaComercializacaoContaFirme", this.workspace).attr('checked', false);
		$("#formaComercializacaoConsignado", this.workspace).attr('checked', false);
					
		$("#radioTributado", this.workspace).attr('checked', false);
		$("#radioIsento", this.workspace).attr('checked', false);
		$("#radioTributacaoOutros", this.workspace).attr('checked', false);
		
		$("#percentualDesconto", this.workspace).val("");
		$("#grupoEditorial", this.workspace).val("");
		$("#subGrupoEditorial", this.workspace).val("");
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
        			   {name:"codigoTipoProduto",value:$("#comboTipoProdutoCadastro", produtoController.workspace).val()}];
 
		$.postJSON(contextPath + "/produto/salvarProduto",  
			   	params,
			   	function (result) {

					var tipoMensagem = result.tipoMensagem;
					var listaMensagens = result.listaMensagens;
					
					if (tipoMensagem && listaMensagens) {
						
						this.exibirMensagem(tipoMensagem, listaMensagens);
					} 

					if (tipoMensagem == 'SUCCESS') {
						$("#dialog-novo").dialog( "close" );
					}
					
				},
			  	null,
			   	true,
			   	'dialogMensagemNovo'
		);
	},
	
	executarPreProcessamento : function(resultado) {

		$.each(resultado.rows, function(index, row) {
			
			var linkAprovar = '<a href="javascript:;" onclick="produtoController.editarProduto(' + row.cell.id + ');" style="cursor:pointer">' +
					     	  	'<img title="Editar" src="' + contextPath +'/images/ico_editar.gif" hspace="5" border="0px" />' +
					  		  '</a>';
			
			var linkExcluir = '<a href="javascript:;" onclick="produtoController.removerProduto(' + row.cell.id + ');" style="cursor:pointer">' +
							   	 '<img title="Excluir" src="' + contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" />' +
							   '</a>';
			
			row.cell.acao = linkAprovar + linkExcluir;
		});
			
		$(".grids").show();
		
		return resultado;
	},	
	
	carregarTipoDescontoProduto : function(callback) {
		if (callback) {
			callback();
		}
	},
	
	//Pesquisa por código de produto
	pesquisarPorCodigoProduto : function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		var codigoProduto = $(idCodigo).val();
		
		codigoProduto = $.trim(codigoProduto);
		
		$(idCodigo).val(codigoProduto);
		
		$(idProduto).val("");
		$(idEdicao).val("");
		$(idEdicao).attr("disabled", "disabled");
		
		if (codigoProduto && codigoProduto.length > 0) {
			
			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
					   "codigoProduto=" + codigoProduto,
					   function(result) { produtoController.pesquisarPorCodigoSuccessCallBack(result, idProduto, idEdicao, successCallBack); },
					   function() { produtoController.pesquisarPorCodigoErrorCallBack(idCodigo, errorCallBack); }, isFromModal);
		
		} else {
		
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	//Pesquisa por código de produto
	pesquisarPorCodigoProdutoAutoCompleteEdicao : function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		var codigoProduto = $(idCodigo).val();
		
		codigoProduto = $.trim(codigoProduto);
		
		$(idCodigo).val(codigoProduto);
		
		$(idProduto).val("");
		$(idEdicao).val("");
		
		if (codigoProduto && codigoProduto.length > 0) {
			
			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
					   "codigoProduto=" + codigoProduto,
					   function(result) { produtoController.pesquisarPorCodigoSuccessCallBack(result, idProduto, idEdicao, successCallBack, idCodigo, isFromModal); 
					   		produtoController.autoCompletarEdicaoPorProduto(idCodigo, idEdicao, isFromModal);
						},
					   null, isFromModal);
		
		} else {
		
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},

	pesquisarPorCodigoSuccessCallBack : function(result, idProduto, idEdicao, successCallBack,idCodigo, isFromModal) {
		
		$(idEdicao).removeAttr("disabled");
		$(idProduto).val(result.nome);
		$(idEdicao).focus();
		
		produtoController.pesquisaRealizada = true;
		
		if (successCallBack) {
			successCallBack();
		}
	},
	
	pesquisarPorCodigoErrorCallBack : function(idCodigo, errorCallBack) {
		$(idCodigo).val("");
		$(idCodigo).focus();
		
		if (errorCallBack) {
			errorCallBack();
		}
	},
	
	//Mostrar auto complete por nome do produto
	autoCompletarEdicaoPorProduto : function(idCodigoProduto, idEdicao, isFromModal) {
		
		produtoController.pesquisaRealizada = false;
		
		var codigoProduto = $(idCodigoProduto).val();
		
		$.postJSON(contextPath + "/produto/autoCompletarEdicaoPorProduto", "codigoProduto=" + codigoProduto,
					   function(result) { produtoController.exibirAutoCompleteEdicao(result, idEdicao); },
					   null, isFromModal);
		
	},
	
	//Mostrar auto complete por nome do produto
	autoCompletarPorNomeProduto : function(idProduto, isFromModal) {
		
		produtoController.pesquisaRealizada = false;
		
		var nomeProduto = $(idProduto).val();
		
		if (nomeProduto && nomeProduto.length > 2) {
			$.postJSON(contextPath + "/produto/autoCompletarPorPorNomeProduto", "nomeProduto=" + nomeProduto,
					   function(result) { produtoController.exibirAutoComplete(result, idProduto); },
					   null, isFromModal);
		}
	},
	
	descricaoAtribuida : true,
	
	pesquisaRealizada : false,
	
	intervalo : null,
	
	exibirAutoComplete : function(result, idProduto) {
		
		$(idProduto).autocomplete({
			source : result,
			focus : function(event, ui) {
				produtoController.descricaoAtribuida = false;
			},
			close : function(event, ui) {
				produtoController.descricaoAtribuida = true;
			},
			select : function(event, ui) {
				produtoController.descricaoAtribuida = true;
			},
			minLength: 4,
			delay : 0,
		});
	},
	
	exibirAutoCompleteEdicao : function(result, idEdicao) {
		
		$(idEdicao).autocomplete({
			source : result
		});
	},
	
	//Pesquisar por nome do produto
	pesquisarPorNomeProduto : function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		
		setTimeout(function() { clearInterval(produtoController.intervalo); }, 10 * 1000);
		
		produtoController.intervalo = setInterval(function() {
			
			if (produtoController.descricaoAtribuida) {
				
				if (produtoController.pesquisaRealizada) {
					
					clearInterval(produtoController.intervalo);
					
					return;
				}
				
				produtoController.pesquisarPorNomeProdutoAposIntervalo(idCodigo, idProduto, idEdicao,
															isFromModal, successCallBack, errorCallBack);
			}
			
		}, 100);
	},
	
	pesquisarPorNomeProdutoAposIntervalo : function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		
		clearInterval(produtoController.intervalo);
		
		produtoController.pesquisaRealizada = true;
		
		var nomeProduto = $(idProduto).val();
		
		nomeProduto = $.trim(nomeProduto);
		
		$(idCodigo).val("");
		$(idEdicao).val("");
		$(idEdicao).attr("disabled", "disabled");
		
		if (nomeProduto && nomeProduto.length > 0) {
			$.postJSON(contextPath + "/produto/pesquisarPorNomeProduto", "nomeProduto=" + nomeProduto,
					   function(result) { produtoController.pesquisarPorNomeSuccessCallBack(result, idCodigo, idProduto, idEdicao, successCallBack); },
					   function() { produtoController.pesquisarPorNomeErrorCallBack(idCodigo, idProduto, idEdicao, errorCallBack); }, isFromModal);
		} else {
			
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	pesquisarPorNomeSuccessCallBack : function(result, idCodigo, idProduto, idEdicao, successCallBack) {
		if (result != "") {
			$(idCodigo).val(result.codigo);
			$(idProduto).val(result.nome);
			
			$(idEdicao).removeAttr("disabled");
			$(idEdicao).focus();
			
			if (successCallBack) {
				successCallBack();
			}
		}
	},
	
	pesquisarPorNomeErrorCallBack : function(idCodigo, idProduto, idEdicao, errorCallBack) {
		$(idProduto).val("");
		$(idProduto).focus();
		
		if (errorCallBack) {
			errorCallBack();
		}
	},	

	//Validação do número da edição
	validarNumEdicao : function(idCodigo, idEdicao, isFromModal, successCallBack, errorCallBack) {
		var codigoProduto = $(idCodigo).val();
		var numeroEdicao = $(idEdicao).val();
		
		if (codigoProduto && codigoProduto.length > 0
				&& numeroEdicao && numeroEdicao.length > 0) {
			
			var data = "codigoProduto=" + codigoProduto +
			   		   "&numeroEdicao=" + numeroEdicao;

			$.postJSON(contextPath + "/produto/validarNumeroEdicao",
					data, function(result) { produtoController.validaNumeroEdicaoSucessoCallBack(idCodigo, idEdicao, successCallBack); },
					function() { produtoController.validarNumeroEdicaoErrorCallBack(idEdicao, errorCallBack); }, isFromModal);
		} else {
			
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},

	validaNumeroEdicaoSucessoCallBack : function(idCodigo, idEdicao, successCallBack) {
		if (successCallBack) {
			successCallBack(idCodigo, idEdicao);
		}
	},
	
	validarNumeroEdicaoErrorCallBack : function(idEdicao, errorCallBack) {
		$(idEdicao).val("");
		$(idEdicao).focus();
		
		if (errorCallBack) {
			this.errorCallBack();
		}
	},

}, BaseController);