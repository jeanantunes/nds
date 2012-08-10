//#workspace div.ui-tabs-panel:not(.ui-tabs-hide)
var produto = $.extend(true, {
	
	inicializar : function () {
		this.iniciarGrid();
		$( "#tabProduto" ).tabs();

	},

	aplicarMascaras : function () {
		$("#peb").numeric();
		$("#pacotePadrao").numeric();
	},

	buscarValueRadio:function(radioName) {

		var valueRadio = new Array();

		$("input[type=radio][name='"+radioName+"']:checked").each(function() {
			valueRadio.push($(this).val());
		});

		return valueRadio;
	},
	
	buscarValueCheckBox:function(checkName) {
		return $("#"+checkName).is(":checked");
	},
		
	pesquisarProdutosSuccessCallBack:function() {
		
		produto.pesquisarFornecedor(produto.getCodigoProdutoPesquisa());
	},
	
	pesquisarProdutosErrorCallBack: function() {
			
		produto.pesquisarFornecedor(produto.getCodigoProdutoPesquisa());
	},

	getCodigoProdutoPesquisa: function () {
		return  "codigoProduto=" + $("#codigoProduto").val();
	},
	
	pesquisarFornecedor:function(data){
	
		$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/pesquisarFornecedor",
				   data, this.montarComboFornecedores);
	},

	//Mostrar auto complete por nome do produto
	autoCompletarPorNomeFornecedor : function(idFornecedor, isFromModal) {
		
		produto.pesquisaRealizada = false;
		
		var nomeFornecedor = $(idFornecedor).val();
		
		if (nomeFornecedor && nomeFornecedor.length > 2) {
			$.postJSON(contextPath + "/produto/autoCompletarPorNomeFornecedor", "nomeFornecedor=" + nomeFornecedor,
					   function(result) { produto.exibirAutoComplete(result, idFornecedor); },
					   null, isFromModal);
		}
	},

	montarComboFornecedores:function(result) {
		var comboFornecedores =  montarComboBox(result, true);
		
		$("#fornecedor").html(comboFornecedores);
	},
	
	validarEdicaoSuccessCallBack : function(){
		
		 var data = [{name:"codigoProduto",value:$("#codigoProduto").val()},
         			 {name:"numeroEdicao",value:$("#edicao").val()},
					];
		
		 $.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/pesquisarDataProgramada",
				   data, function(result) {
			 $("#dataProgramada").val(result);
		 });
	},
	
	validarEdicaoErrorCallBack: function() {
		 $("#dataProgramada").val("");
	},	
	
	carregarPercentualDesconto : function() {
		
		var codigoTipoDesconto = $("#comboTipoDesconto").val();

		if (codigoTipoDesconto == '0') {
			$("#percentualDesconto").val("");
		}
		
		$.postJSON(contextPath + "/produto/carregarPercentualDesconto",
					"codigoTipoDesconto=" + codigoTipoDesconto, 
					function(result) {

						if (result == 0) {
							result = "";
						}

						$("#percentualDesconto").val(result);
				});

	},

	iniciarGrid : function() {
		$(".produtosGrid").flexigrid({
			preProcess: produto.executarPreProcessamento,
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
		
		var codigo = $("#codigoProduto").val();
		var produto = $("#produto").val();
		var periodicidade = $("#periodicidade").val();
		var fornecedor = $("#fornecedor").val();
		var editor = $("#edicao").val();
		var codigoTipoProduto = $("#comboTipoProduto").val();
		
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

		$("#dialog-novo").dialog({
			resizable: false,
			height:550,
			width:850,
			modal: true,
			buttons: {
				"Confirmar": function() {

					produto.salvarProduto();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function() {
				produto.limparModalCadastro();
				clearMessageDialogTimeout('dialogMensagemNovo');
			}
		});
		
		this.carregarNovoProduto(
			function() {
				produto.limparModalCadastro();
				produto.carregarProdutoEditado(id);		
			}
		);
	},
	
	carregarProdutoEditado : function(id) {

		$.postJSON(contextPath + "/produto/carregarProdutoParaEdicao", 
				   	"id=" + id,
				   	function(result) {
			   
						$("#idProduto").val(result.id);
						$("#codigoProdutoCadastro").val(result.codigo);
						$("#nomeProduto").val(result.nome);
						$("#sloganProduto").val(result.slogan);
						$("#peb").val(result.peb);
						$("#pacotePadrao").val(result.pacotePadrao);
						$("#comboPeriodicidade").val(result.periodicidade);
						$("#grupoEditorial").val(result.grupoEditorial);
						$("#subGrupoEditorial").val(result.subGrupoEditorial);
						$("#comboEditor").val(result.codigoEditor);
						$("#comboFornecedoresCadastro").val(result.codigoFornecedor);
						$("#comboTipoDesconto").val(result.tipoDesconto);
						$("#comboTipoProdutoCadastro").val(result.codigoTipoProduto);
						$("#segmentacaoClasseSocial").val(result.classeSocial);
						$("#segmentacaoSexo").val(result.sexo);
						$("#segmentacaoFaixaEtaria").val(result.faixaEtaria);
						$("#segmentacaoFormato").val(result.formatoProduto);
						$("#segmentacaoTipoLancamento").val(result.tipoLancamento);
						$("#segmentacaoTemaPrincipal").val(result.temaPrincipal);
						$("#segmentacaoTemaSecundario").val(result.temaSecundario);

						if (result.formaComercializacao == 'Conta Firme') {
							$("#formaComercializacaoContaFirme").attr('checked', true);
						} else if (result.formaComercializacao == 'Consignado') {
							$("#formaComercializacaoConsignado").attr('checked', true);
						}
						
						if (result.tributacaoFiscal == 'TRIBUTADO') {
							$("#radioTributado").attr('checked', true);
						} else if (result.tributacaoFiscal == 'ISENTO') {
							$("#radioIsento").attr('checked', true);
						} else if (result.tributacaoFiscal == 'OUTROS') {
							$("#radioTributacaoOutros").attr('checked', true);
						}

						produto.carregarTipoDescontoProduto(produto.carregarPercentualDesconto);							
					},
					null,
					true
				);
	},
	
	removerProduto : function(id) {
		
		$("#dialog-excluir").dialog( {
			resizable : false,
			height : 'auto',
			width : 450,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$.postJSON(contextPath + "/produto/removerProduto", 
							   "id=" + id,
							   function(result) {
							   		
							   		$("#dialog-excluir").dialog("close");
							   		
									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										
										exibirMensagem(tipoMensagem, listaMensagens);
									}
											
									$(".produtosGrid").flexReload();
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
			}
		});
	},
	
	novoProduto : function () {

		$("#dialog-novo").dialog({
			resizable: false,
			height:550,
			width:850,
			modal: true,
			buttons: {
				"Confirmar": function() {

					produto.salvarProduto();
					
			   		$(".produtosGrid").flexReload();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function() {
				produto.limparModalCadastro();
				clearMessageDialogTimeout('dialogMensagemNovo');
			}
		});

		this.carregarNovoProduto(this.limparModalCadastro);
	},

	carregarNovoProduto : function(callback) {

		$.postJSON(contextPath + "/produto/carregarDadosProduto",
					null,
					function (result) {

						produto.popularCombo(result[0], $("#comboTipoProdutoCadastro"));
						produto.popularCombo(result[1], $("#comboFornecedoresCadastro"));
						produto.popularCombo(result[2], $("#comboEditor"));
						produto.popularCombo(result[3], $("#comboTipoDesconto"));

						if (callback) {
							callback();
						}
					},
				  	null,
				   	true
			);
	},

	limparModalCadastro : function() {

		$("#idProduto").val("");
		$("#codigoProdutoCadastro").val("");
		$("#nomeProduto").val("");
		$("#sloganProduto").val("");
		$("#peb").val("");
		$("#pacotePadrao").val("");
		$("#comboPeriodicidade").val("");

		$("#formaComercializacaoContaFirme").attr('checked', false);
		$("#formaComercializacaoConsignado").attr('checked', false);
					
		$("#radioTributado").attr('checked', false);
		$("#radioIsento").attr('checked', false);
		$("#radioTributacaoOutros").attr('checked', false);
		
		$("#percentualDesconto").val("");
		$("#grupoEditorial").val("");
		$("#subGrupoEditorial").val("");
	},
	
	salvarProduto : function() {

		 var params = [{name:"produto.id",value:$("#idProduto").val()},
        			   {name:"produto.codigo",value:$("#codigoProdutoCadastro").val()},
        			   {name:"produto.nome",value:$("#nomeProduto").val()},
        			   {name:"produto.peb",value:$("#peb").val()},
        			   {name:"produto.pacotePadrao",value:$("#pacotePadrao").val()},
        			   {name:"produto.slogan",value:$("#sloganProduto").val()},
        			   {name:"produto.periodicidade",value:$("#comboPeriodicidade").val()},
        			   {name:"produto.formaComercializacao",value:this.buscarValueRadio('formaComercializacao')},
        			   {name:"produto.tributacaoFiscal",value:this.buscarValueRadio('radioTributacaoFiscal')},
        			   {name:"produto.grupoEditorial",value:$("#grupoEditorial").val()},
        			   {name:"produto.subGrupoEditorial",value:$("#subGrupoEditorial").val()},	
        			   {name:"produto.segmentacao.classeSocial",value:$("#segmentacaoClasseSocial").val()},
        			   {name:"produto.segmentacao.sexo",value:$("#segmentacaoSexo").val()},
        			   {name:"produto.segmentacao.faixaEtaria",value:$("#segmentacaoFaixaEtaria").val()},
        			   {name:"produto.segmentacao.formatoProduto",value:$("#segmentacaoFormato").val()},
        			   {name:"produto.segmentacao.tipoLancamento",value:$("#segmentacaoTipoLancamento").val()},
        			   {name:"produto.segmentacao.temaPrincipal",value:$("#segmentacaoTemaPrincipal").val()},
        			   {name:"produto.segmentacao.temaSecundario",value:$("#segmentacaoTemaSecundario").val()},
        			   {name:"codigoEditor",value:$("#comboEditor").val()},
        			   {name:"codigoFornecedor",value:$("#comboFornecedoresCadastro").val()},
        			   {name:"codigoTipoDesconto",value:$("#comboTipoDesconto").val()},
        			   {name:"codigoTipoProduto",value:$("#comboTipoProdutoCadastro").val()}];
 
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
			
			var linkAprovar = '<a href="javascript:;" onclick="produto.editarProduto(' + row.cell.id + ');" style="cursor:pointer">' +
					     	  	'<img title="Editar" src="' + contextPath +'/images/ico_editar.gif" hspace="5" border="0px" />' +
					  		  '</a>';
			
			var linkExcluir = '<a href="javascript:;" onclick="produto.removerProduto(' + row.cell.id + ');" style="cursor:pointer">' +
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
					   function(result) { produto.pesquisarPorCodigoSuccessCallBack(result, idProduto, idEdicao, successCallBack); },
					   function() { produto.pesquisarPorCodigoErrorCallBack(idCodigo, errorCallBack); }, isFromModal);
		
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
					   function(result) { produto.pesquisarPorCodigoSuccessCallBack(result, idProduto, idEdicao, successCallBack, idCodigo, isFromModal); 
					   		produto.autoCompletarEdicaoPorProduto(idCodigo, idEdicao, isFromModal);
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
		
		produto.pesquisaRealizada = true;
		
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
		
		produto.pesquisaRealizada = false;
		
		var codigoProduto = $(idCodigoProduto).val();
		
		$.postJSON(contextPath + "/produto/autoCompletarEdicaoPorProduto", "codigoProduto=" + codigoProduto,
					   function(result) { produto.exibirAutoCompleteEdicao(result, idEdicao); },
					   null, isFromModal);
		
	},
	
	//Mostrar auto complete por nome do produto
	autoCompletarPorNomeProduto : function(idProduto, isFromModal) {
		
		produto.pesquisaRealizada = false;
		
		var nomeProduto = $(idProduto).val();
		
		if (nomeProduto && nomeProduto.length > 2) {
			$.postJSON(contextPath + "/produto/autoCompletarPorPorNomeProduto", "nomeProduto=" + nomeProduto,
					   function(result) { produto.exibirAutoComplete(result, idProduto); },
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
				produto.descricaoAtribuida = false;
			},
			close : function(event, ui) {
				produto.descricaoAtribuida = true;
			},
			select : function(event, ui) {
				produto.descricaoAtribuida = true;
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
		
		setTimeout(function() { clearInterval(produto.intervalo); }, 10 * 1000);
		
		produto.intervalo = setInterval(function() {
			
			if (produto.descricaoAtribuida) {
				
				if (produto.pesquisaRealizada) {
					
					clearInterval(produto.intervalo);
					
					return;
				}
				
				produto.pesquisarPorNomeProdutoAposIntervalo(idCodigo, idProduto, idEdicao,
															isFromModal, successCallBack, errorCallBack);
			}
			
		}, 100);
	},
	
	pesquisarPorNomeProdutoAposIntervalo : function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		
		clearInterval(produto.intervalo);
		
		produto.pesquisaRealizada = true;
		
		var nomeProduto = $(idProduto).val();
		
		nomeProduto = $.trim(nomeProduto);
		
		$(idCodigo).val("");
		$(idEdicao).val("");
		$(idEdicao).attr("disabled", "disabled");
		
		if (nomeProduto && nomeProduto.length > 0) {
			$.postJSON(contextPath + "/produto/pesquisarPorNomeProduto", "nomeProduto=" + nomeProduto,
					   function(result) { produto.pesquisarPorNomeSuccessCallBack(result, idCodigo, idProduto, idEdicao, successCallBack); },
					   function() { produto.pesquisarPorNomeErrorCallBack(idCodigo, idProduto, idEdicao, errorCallBack); }, isFromModal);
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
					data, function(result) { produto.validaNumeroEdicaoSucessoCallBack(idCodigo, idEdicao, successCallBack); },
					function() { produto.validarNumeroEdicaoErrorCallBack(idEdicao, errorCallBack); }, isFromModal);
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