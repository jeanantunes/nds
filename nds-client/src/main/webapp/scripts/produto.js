var produto = $.extend(true, {
	
	inicializar : function () {
		this.iniciarGrid();
		$( "#tabProduto" ).tabs();

	},

	aplicarMascaras : function () {
		$("#peb", this.workspace).numeric();
		$("#pacotePadrao", this.workspace).numeric();
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
		return  "codigoProduto=" + $("#codigoProduto", this.workspace).val();
	},
	
	pesquisarFornecedor:function(data){
	
		$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/pesquisarFornecedor",
				   data, this.montarComboFornecedores);
	},

	//Mostrar auto complete por nome do produto
	autoCompletarPorNomeFornecedor : function(idFornecedor, isFromModal) {
		
		produto.pesquisaRealizada = false;
		
		var nomeFornecedor = $(idFornecedor, this.workspace).val();
		
		if (nomeFornecedor && nomeFornecedor.length > 2) {
			$.postJSON(contextPath + "/produto/autoCompletarPorNomeFornecedor", "nomeFornecedor=" + nomeFornecedor,
					   function(result) { produto.exibirAutoComplete(result, idFornecedor); },
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
		
		var codigo = $("#codigoProduto", this.workspace).val();
		var produto = $("#produto", this.workspace).val();
		var periodicidade = $("#periodicidade", this.workspace).val();
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

		$("#dialog-novo", this.workspace).dialog({
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
			},
			close : function() {
				$(this).hide();
				$(this).dialog('destroy');
				var form  = $(this).closest('form');
				$(this).appendTo(form);
				//$(this).appendTo('#novo_produto_form');
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
			   
						$("#idProduto", this.workspace).val(result.id);
						$("#codigoProdutoCadastro", this.workspace).val(result.codigo);
						$("#nomeProduto", this.workspace).val(result.nome);
						$("#sloganProduto", this.workspace).val(result.slogan);
						$("#peb", this.workspace).val(result.peb);
						$("#pacotePadrao", this.workspace).val(result.pacotePadrao);
						$("#comboPeriodicidade", this.workspace).val(result.periodicidade);
						$("#grupoEditorial", this.workspace).val(result.grupoEditorial);
						$("#subGrupoEditorial", this.workspace).val(result.subGrupoEditorial);
						$("#comboEditor", this.workspace).val(result.codigoEditor);
						$("#comboFornecedoresCadastro", this.workspace).val(result.codigoFornecedor);
						$("#comboTipoDesconto", this.workspace).val(result.tipoDesconto);
						$("#comboTipoProdutoCadastro", this.workspace).val(result.codigoTipoProduto);
						$("#segmentacaoClasseSocial", this.workspace).val(result.classeSocial);
						$("#segmentacaoSexo", this.workspace).val(result.sexo);
						$("#segmentacaoFaixaEtaria", this.workspace).val(result.faixaEtaria);
						$("#segmentacaoFormato", this.workspace).val(result.formatoProduto);
						$("#segmentacaoTipoLancamento", this.workspace).val(result.tipoLancamento);
						$("#segmentacaoTemaPrincipal", this.workspace).val(result.temaPrincipal);
						$("#segmentacaoTemaSecundario", this.workspace).val(result.temaSecundario);

						if (result.formaComercializacao == 'Conta Firme') {
							$("#formaComercializacaoContaFirme", this.workspace).attr('checked', true);
						} else if (result.formaComercializacao == 'Consignado') {
							$("#formaComercializacaoConsignado", this.workspace).attr('checked', true);
						}
						
						if (result.tributacaoFiscal == 'TRIBUTADO') {
							$("#radioTributado", this.workspace).attr('checked', true);
						} else if (result.tributacaoFiscal == 'ISENTO') {
							$("#radioIsento", this.workspace).attr('checked', true);
						} else if (result.tributacaoFiscal == 'OUTROS') {
							$("#radioTributacaoOutros", this.workspace).attr('checked', true);
						}

						produto.carregarTipoDescontoProduto(produto.carregarPercentualDesconto);							
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
			}
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

					produto.salvarProduto();
					
			   		$(".produtosGrid", this.workspace).flexReload();
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

						produto.popularCombo(result[0], $("#comboTipoProdutoCadastro", this.workspace));
						produto.popularCombo(result[1], $("#comboFornecedoresCadastro", this.workspace));
						produto.popularCombo(result[2], $("#comboEditor", this.workspace));
						produto.popularCombo(result[3], $("#comboTipoDesconto", this.workspace));

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

		 var params = [{name:"produto.id",value:$("#idProduto", this.workspace).val()},
        			   {name:"produto.codigo",value:$("#codigoProdutoCadastro", this.workspace).val()},
        			   {name:"produto.nome",value:$("#nomeProduto", this.workspace).val()},
        			   {name:"produto.peb",value:$("#peb", this.workspace).val()},
        			   {name:"produto.pacotePadrao",value:$("#pacotePadrao", this.workspace).val()},
        			   {name:"produto.slogan",value:$("#sloganProduto", this.workspace).val()},
        			   {name:"produto.periodicidade",value:$("#comboPeriodicidade", this.workspace).val()},
        			   {name:"produto.formaComercializacao",value:this.buscarValueRadio('formaComercializacao')},
        			   {name:"produto.tributacaoFiscal",value:this.buscarValueRadio('radioTributacaoFiscal')},
        			   {name:"produto.grupoEditorial",value:$("#grupoEditorial", this.workspace).val()},
        			   {name:"produto.subGrupoEditorial",value:$("#subGrupoEditorial", this.workspace).val()},	
        			   {name:"produto.segmentacao.classeSocial",value:$("#segmentacaoClasseSocial", this.workspace).val()},
        			   {name:"produto.segmentacao.sexo",value:$("#segmentacaoSexo", this.workspace).val()},
        			   {name:"produto.segmentacao.faixaEtaria",value:$("#segmentacaoFaixaEtaria", this.workspace).val()},
        			   {name:"produto.segmentacao.formatoProduto",value:$("#segmentacaoFormato", this.workspace).val()},
        			   {name:"produto.segmentacao.tipoLancamento",value:$("#segmentacaoTipoLancamento", this.workspace).val()},
        			   {name:"produto.segmentacao.temaPrincipal",value:$("#segmentacaoTemaPrincipal", this.workspace).val()},
        			   {name:"produto.segmentacao.temaSecundario",value:$("#segmentacaoTemaSecundario", this.workspace).val()},
        			   {name:"codigoEditor",value:$("#comboEditor", this.workspace).val()},
        			   {name:"codigoFornecedor",value:$("#comboFornecedoresCadastro", this.workspace).val()},
        			   {name:"codigoTipoDesconto",value:$("#comboTipoDesconto", this.workspace).val()},
        			   {name:"codigoTipoProduto",value:$("#comboTipoProdutoCadastro", this.workspace).val()}];
 
		$.postJSON(contextPath + "/produto/salvarProduto",  
			   	params,
			   	function (result) {

					var tipoMensagem = result.tipoMensagem;
					var listaMensagens = result.listaMensagens;
					
					if (tipoMensagem && listaMensagens) {
						
						this.exibirMensagem(tipoMensagem, listaMensagens);
					} 

					if (tipoMensagem == 'SUCCESS') {
						$("#dialog-novo", this.workspace).dialog( "close" );
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
			
		$(".grids", this.workspace).show();
		
		return resultado;
	},	
	
	carregarTipoDescontoProduto : function(callback) {
		if (callback) {
			callback();
		}
	},
	
	//Pesquisa por código de produto
	pesquisarPorCodigoProduto : function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		var codigoProduto = $(idCodigo, this.workspace).val();
		
		codigoProduto = $.trim(codigoProduto);
		
		$(idCodigo, this.workspace).val(codigoProduto);
		
		$(idProduto, this.workspace).val("");
		$(idEdicao, this.workspace).val("");
		$(idEdicao, this.workspace).attr("disabled", "disabled");
		
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
		var codigoProduto = $(idCodigo, this.workspace).val();
		
		codigoProduto = $.trim(codigoProduto);
		
		$(idCodigo, this.workspace).val(codigoProduto);
		
		$(idProduto, this.workspace).val("");
		$(idEdicao, this.workspace).val("");
		
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
		
		$(idEdicao, this.workspace).removeAttr("disabled");
		$(idProduto, this.workspace).val(result.nome);
		$(idEdicao, this.workspace).focus();
		
		produto.pesquisaRealizada = true;
		
		if (successCallBack) {
			successCallBack();
		}
	},
	
	pesquisarPorCodigoErrorCallBack : function(idCodigo, errorCallBack) {
		$(idCodigo, this.workspace).val("");
		$(idCodigo, this.workspace).focus();
		
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
		
		var nomeProduto = $(idProduto, this.workspace).val();
		
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
		
		$(idProduto, this.workspace).autocomplete({
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
		
		$(idEdicao, this.workspace).autocomplete({
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
		
		var nomeProduto = $(idProduto, this.workspace).val();
		
		nomeProduto = $.trim(nomeProduto);
		
		$(idCodigo, this.workspace).val("");
		$(idEdicao, this.workspace).val("");
		$(idEdicao, this.workspace).attr("disabled", "disabled");
		
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
			$(idCodigo, this.workspace).val(result.codigo);
			$(idProduto, this.workspace).val(result.nome);
			
			$(idEdicao, this.workspace).removeAttr("disabled");
			$(idEdicao, this.workspace).focus();
			
			if (successCallBack) {
				successCallBack();
			}
		}
	},
	
	pesquisarPorNomeErrorCallBack : function(idCodigo, idProduto, idEdicao, errorCallBack) {
		$(idProduto, this.workspace).val("");
		$(idProduto, this.workspace).focus();
		
		if (errorCallBack) {
			errorCallBack();
		}
	},	

	//Validação do número da edição
	validarNumEdicao : function(idCodigo, idEdicao, isFromModal, successCallBack, errorCallBack) {
		var codigoProduto = $(idCodigo, this.workspace).val();
		var numeroEdicao = $(idEdicao, this.workspace).val();
		
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
		$(idEdicao, this.workspace).val("");
		$(idEdicao, this.workspace).focus();
		
		if (errorCallBack) {
			this.errorCallBack();
		}
	},

}, BaseController);