var produtoEdicaoController = $.extend(true, {

		init : function() {
			this.initGrids();
			$( "#tabEdicoes", this.workspace).tabs();
			$( "#pDataLancamento", this.workspace).datepicker({
				showOn: "button",
				buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true
			});
			$( "#dateLancto_pop", this.workspace).datepicker({
				showOn: "button",
				buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true
			});
			$( "#dtLancto", this.workspace).datepicker({
				showOn: "button",
				buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true
			});
			$( "#pesqProdutos", this.workspace ).fadeIn('slow');
			$( "#pesqProdutos", this.workspace ).fadeOut('slow');
			$( ".prodLinhas", this.workspace ).show('slow');
			$("#numeroEdicao", this.workspace).numeric();
			$("#pacotePadrao", this.workspace).numeric();
			$("#repartePrevisto", this.workspace).numeric();
			$("#reparteDistribuido", this.workspace).numeric();
			$("#repartePromocional", this.workspace).numeric();
			$("#precoPrevisto", this.workspace).numeric();
			$("#precoVenda", this.workspace).numeric();
			$("#desconto", this.workspace).numeric();
			$("#peso", this.workspace).numeric();
			$("#largura", this.workspace).numeric();
			$("#comprimento", this.workspace).numeric();
			$("#espessura", this.workspace).numeric();
			$("#numeroLancamento", this.workspace).numeric();
			$("#dataLancamentoPrevisto", this.workspace).mask("99/99/9999");
			$("#dataLancamento", this.workspace).mask("99/99/9999");

		},
		
		initGrids : function () {
			$(".bonificacoesGrid", this.workspace).flexigrid({
				//url : '../xml/produtos_bonificacoes-xml.xml',
				dataType : 'xml',
				colModel : [ {
					display : 'Faixa',
					name : 'faixa',
					width : 320,
					sortable : true,
					align : 'left'
				}, {
					display : 'Quantidade Adicional',
					name : 'qtdeAdicional',
					width : 160,
					sortable : true,
					align : 'center'
				}, {
					display : 'A&ccedil;�es',
					name : 'acoes',
					width : 60,
					sortable : true,
					align : 'center'
				}],
				width : 620,
				height : 120,
				singleSelect : true
			});

		$(".prodsPesqGrid", this.workspace).flexigrid({
				dataType : 'json',
				colModel : [ {
					display : 'Produto',
					name : 'nomeProduto',
					width : 115,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edi&ccedil;&atilde;o',
					name : 'numeroEdicao',
					width : 40,
					sortable : true,
					align : 'left'
				}],
				width : 200,
				height : 350,
				singleSelect : true
			});
			
			
		$(".edicoesGrid", this.workspace).flexigrid({
			preProcess: produtoEdicaoController.executarPreProcessamento,
			dataType : 'json',
				colModel : [ {
					display : 'C&oacute;digo',
					name : 'codigoProduto',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome Comercial',
					name : 'nomeProduto',
					width : 180,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edi&ccedil;&atilde;o',
					name : 'numeroEdicao',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Fornecedor',
					name : 'nomeFornecedor',
					width : 200,
					sortable : true,
					align : 'left',
				}, {
					display : 'Tipo de Lan&ccedil;amento',
					name : 'statusLancamento',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Situa&ccedil;&atilde;o',
					name : 'statusSituacao',
					width : 115,
					sortable : true,
					align : 'left'
				}, {
					display : 'Brinde',
					name : 'temBrinde',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'A&ccedil;&atilde;o',
					name : 'acao',
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
				height : 255,
				singleSelect : true
			});
			
			
			$(".produtoEdicaoBaseGrid", this.workspace).flexigrid({
				preProcess: produtoEdicaoController.executarPreProcessamento,
				dataType : 'json',
				colModel : [ {
					display : 'C&oacute;digo',
					name : 'codigo',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'produto',
					width : 280,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto &Uacute;nico',
					name : 'produtoUnico',
					width : 100,
					sortable : true,
					align : 'center'
				}, {
					display : 'Edi&ccedil;&atilde;o Base',
					name : 'edicaoBase',
					width : 100,
					sortable : true,
					align : 'center',
				}],
				width : 640,
				height : 120,
				singleSelect : true
			});
		},
		
		pesquisarEdicoes : function() {

			var codigoProduto = $("#pCodigoProduto", this.workspace).val();
			var nomeProduto = $("#pNomeProduto", this.workspace).val();
			var dataLancamento = $("#pDataLancamento", this.workspace).val();
			var situacaoLancamento = $("#pSituacaoLancamento", this.workspace).val();
			var codigoDeBarras = $("#pCodigoDeBarras", this.workspace).val();
			
			$("#pBrinde", this.workspace).val(0);
			if (document.getElementById('pBrinde', this.workspace).checked){
				$("#pBrinde", this.workspace).val(1);
			}
			var brinde = $("#pBrinde", this.workspace).val();
			
			$(".edicoesGrid", this.workspace).flexOptions({
				url: contextPath + '/cadastro/edicao/pesquisarEdicoes.json',
				params: [{name:'codigoProduto', value: codigoProduto },
					     {name:'nomeProduto', value: nomeProduto },
					     {name:'dataLancamento', value: dataLancamento },
					     {name:'situacaoLancamento', value: situacaoLancamento },
					     {name:'codigoDeBarras', value: codigoDeBarras },
					     {name:'brinde', value : brinde }],
				newp: 1,
			});
			
			$(".edicoesGrid", this.workspace).flexReload();
		},

		executarPreProcessamento : function(resultado) {

			// Exibe mensagem de erro/alerta, se houver:
			var mensagens = (resultado.mensagens) ? resultado.mensagens : resultado.result;   
			var tipoMensagem = (mensagens && mensagens.tipoMensagem) ? mensagens.tipoMensagem : null; 
			var listaMensagens = (mensagens && mensagens.listaMensagens) ? mensagens.listaMensagens : null;
			if (tipoMensagem && listaMensagens) {
				exibirMensagem(tipoMensagem, listaMensagens);
				return;
			}
			
			var nProduto = '';
			var cProduto = '';
			$.each(resultado.rows, function(index, row) {
				
				var linkAprovar = '<a href="javascript:;" onclick="produtoEdicaoController.editarEdicao(' + row.cell.id + ');" style="cursor:pointer">' +
						     	  	'<img title="Editar" src="' + contextPath + '/images/ico_editar.gif" hspace="5" border="0px" />' +
						  		  '</a>';
				
				var linkExcluir = '<a href="javascript:;" onclick="produtoEdicaoController.removerEdicao(' + row.cell.id + ');" style="cursor:pointer">' +
								   	 '<img title="Excluir" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
								   '</a>';
				
				row.cell.acao = linkAprovar + linkExcluir;

				//
				nProduto = row.cell.nomeProduto;
				cProduto = row.cell.codigoProduto;
			});
				
			$(".grids").show();

			//
			var txt = '';
			if (nProduto != null || cProduto != null) {
				txt = ": " + cProduto + " - " + nProduto;
			}
			$("#labelNomeProduto", this.workspace).html(txt);
			$("#codigoProduto", this.workspace).val(cProduto);
			
			return resultado;
		},

		salvaUmaEdicao : function() {
			
			$("#formUpload", this.workspace).ajaxSubmit({
				success: function(responseText, statusText, xhr, $form)  { 
					var mensagens = (responseText.mensagens) ? responseText.mensagens : responseText.result;   
					var tipoMensagem = mensagens.tipoMensagem;
					var listaMensagens = mensagens.listaMensagens;
					if (tipoMensagem && listaMensagens) {
						exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
					}
					
					produtoEdicaoController.prepararTela(null);
					produtoEdicaoController.carregarDialog(null);
				}, 
				url: contextPath + '/cadastro/edicao/salvar',
				type: 'POST',
				dataType: 'json',
				data: { codigoProduto : $("#codigoProduto", this.workspace).val() }
			});

		},

		novaEdicao : function() {
			produtoEdicaoController.popup(null);
		},

		editarEdicao : function(id) {
			produtoEdicaoController.popup(id);
		},

		prepararTela : function(id) {
			
			// limpar os campos:
			produtoEdicaoController.form_clear('formUpload');
			produtoEdicaoController.carregarImagemCapa(id);
			$('#formUpload', this.workspace).find(':input').each(
				function() {
					switch(this.type) {
					case 'text':
					case 'textarea':
					case 'password':
						$(this).attr("readonly", true);
						break;
					
					case 'file':
					case 'select-multiple':
					case 'select-one':
					case 'checkbox':
					case 'radio':
						$(this).attr("disabled", true);
						break;
					}
				}
			);
			
			
			// Popular a lista de Edi��es:
			$(".prodsPesqGrid", this.workspace).flexOptions({
				url: contextPath + '/cadastro/edicao/ultimasEdicoes.json',
				params: [{name:'codigoProduto', value: $("#codigoProduto", this.workspace).val() }],
				newp: 1,
			});

			$(".prodsPesqGrid", this.workspace).flexReload();	
		},

		carregarDialog : function(id) {
			
			// Exibir os dados do Produto:
			$.postJSON(
				contextPath + '/cadastro/edicao/carregarDadosProdutoEdicao.json',
				{ codigoProduto : $("#codigoProduto", this.workspace).val(), 
				  idProdutoEdicao : id},
				function(result) {
					if (result) {
						$("#idProdutoEdicao", this.workspace).val(result.id);
						$("#codigoProdutoEdicao", this.workspace).val(result.codigoProduto);
						$("#nomePublicacao", this.workspace).val(result.nomeProduto);
						$("#nomeComercialProduto", this.workspace).val(result.nomeComercialProduto);
						$("#nomeFornecedor", this.workspace).val(result.nomeFornecedor);
						$("#situacao", this.workspace).val(result.situacao);
						$("#numeroEdicao", this.workspace).val(result.numeroEdicao);
						$("#fase", this.workspace).val(result.fase);
						$("#numeroLancamento", this.workspace).val(result.numeroLancamento);
						$("#pacotePadrao", this.workspace).val(result.pacotePadrao);
						$("#tipoLancamento", this.workspace).val(result.tipoLancamento);
						$("#precoPrevisto", this.workspace).val(result.precoPrevisto);
						$("#precoVenda", this.workspace).val(result.precoVenda);
						$("#dataLancamentoPrevisto", this.workspace).val(result.dataLancamentoPrevisto == undefined ? '' : result.dataLancamentoPrevisto.$);
						$("#dataLancamento", this.workspace).val(result.dataLancamento == undefined ? '' : result.dataLancamento.$);
						$("#repartePrevisto", this.workspace).val(result.repartePrevisto)
						$("#reparteDistribuido", this.workspace).val(result.reparteDistribuido);
						$("#repartePromocional", this.workspace).val(result.repartePromocional);
						//$("#categoria").val();
						$("#codigoDeBarras", this.workspace).val(result.codigoDeBarras);
						$("#codigoDeBarrasCorporativo", this.workspace).val(result.codigoDeBarrasCorporativo);
						$("#desconto", this.workspace).val(result.desconto);
						$("#peso", this.workspace).val(result.peso);
						$("#largura", this.workspace).val(result.largura);
						$("#comprimento", this.workspace).val(result.comprimento);
						$("#espessura", this.workspace).val(result.espessura);
						$("#chamadaCapa", this.workspace).val(result.chamadaCapa);
						$('#parcial', this.workspace).val(result.parcial + "");
						$('#possuiBrinde', this.workspace).attr('checked', result.possuiBrinde);
						$('#boletimInformativo', this.workspace).val(result.boletimInformativo);

						if (result.origemInterface) {
							$("#precoVenda", this.workspace).attr("readonly", false);				
						} else {
							$("#codigoProdutoEdicao", this.workspace).attr("readonly", false);
							$("#nomeComercialProduto", this.workspace).attr("readonly", false);
							$("#numeroEdicao", this.workspace).attr("readonly", (result.numeroEdicao == 1));
							$("#pacotePadrao", this.workspace).attr("readonly", false);
							$("#tipoLancamento", this.workspace).attr("disabled", false);
							$("#precoPrevisto", this.workspace).attr("readonly", false);
							$("#dataLancamentoPrevisto", this.workspace).attr("readonly", false);
							$("#repartePrevisto", this.workspace).attr("readonly", false);
							$("#repartePromocional", this.workspace).attr("readonly", false);
							$("#codigoDeBarrasCorporativo", this.workspace).attr("readonly", false);
							$('#parcial', this.workspace).attr("disabled", false);
							$("#desconto", this.workspace).attr("readonly", false);
							$("#largura", this.workspace).attr("readonly", false);
							$("#comprimento", this.workspace).attr("readonly", false);
							$("#espessura", this.workspace).attr("readonly", false);
							$('#boletimInformativo', this.workspace).attr("readonly", false);
						}

						$("#numeroLancamento", this.workspace).attr("readonly", false); 
						$("#imagemCapa", this.workspace).attr("disabled", false);
						$("#codigoDeBarras", this.workspace).attr("readonly", false);				
						$("#chamadaCapa", this.workspace).attr("readonly", false);
						$('#possuiBrinde', this.workspace).attr("disabled", false);
						$("#peso", this.workspace).attr("readonly", false);
					}
				},
				function(result) { 
					exibirMensagemDialog(result.tipoMensagem, result.listaMensagens, "");
					},
				true
			);

		},

		popup : function(id) {
			
			if ($(".edicoesGrid > tbody").data() == null || $(".edicoesGrid > tbody").data() == undefined) {
				exibirMensagem('WARNING', ['Por favor, escolha um produto para adicionar a Edi&ccedil;&atilde;o!'], "");
				return;
			}
			
			produtoEdicaoController.prepararTela(id);
			produtoEdicaoController.carregarDialog(id);

			$( "#dialog-novo", this.workspace ).dialog({
				resizable: false,
				height:615,
				width:960,
				modal: true,
				buttons: {
					"Confirmar": function() {

						$("#formUpload", this.workspace).ajaxSubmit({
							beforeSubmit: function(arr, formData, options) {
								// Incluir aqui as validacoes;
							},
							success: function(responseText, statusText, xhr, $form)  { 
								var mensagens = (responseText.mensagens) ? responseText.mensagens : responseText.result;   
								var tipoMensagem = mensagens.tipoMensagem;
								var listaMensagens = mensagens.listaMensagens;
								if (tipoMensagem && listaMensagens) {
									//exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');

									$("#dialog-novo", this.workspace).dialog( "close" );
									produtoEdicaoController.pesquisarEdicoes();
									exibirMensagem(tipoMensagem, listaMensagens);
								}
							}, 
							url: contextPath + '/cadastro/edicao/salvar',
							type: 'POST',
							dataType: 'json',
							data: { codigoProduto : $("#codigoProduto", this.workspace).val() }
						});
					},
					"Cancelar": function() {
						$("#dialog-novo", this.workspace).dialog( "close" );
					}
				}
			});
		},

		carregarImagemCapa : function(idProdutoEdicao) {

			var imgPath = (idProdutoEdicao == null || idProdutoEdicao == undefined)
				? "" : contextPath + '/capa/' + idProdutoEdicao; 
			var img = $("<img />").attr('src', imgPath).attr('width', '144').attr('height', '185').attr('alt', 'Capa');
			$("#div_imagem_capa", this.workspace).empty();
			$("#div_imagem_capa", this.workspace).append(img);
			
			img.load(function() {
				if (!(!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0)) {
					$("#div_imagem_capa", this.workspace).append(img);
				}
			});

		},

		form_clear : function(formName) {

			$('#' + formName, this.workspace).find(':input').each(
				function() {
					switch(this.type) {
						case 'hidden': 
						case 'text':
						case 'textarea':
						case 'password':
						case 'select-multiple':
						case 'select-one':
						case 'file':
							$(this).val('');
							break;

						case 'checkbox':
						case 'radio':
							this.checked = false;
							break;
					}
				}
			);
		},
			
		popup_alterar : function() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
			$( "#dialog-novo" , this.workspace).dialog({
				resizable: false,
				height:615,
				width:950,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").hide("highlight", {}, 1000, callback);
						$( "#abaPdv" ).show( );
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});	      
		},
			
		removerEdicao : function(id) {

			$( "#dialog-excluir" , this.workspace).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {

						$.postJSON(
							contextPath + '/cadastro/edicao/removerEdicao.json',
							{idProdutoEdicao : id},
							function(result) {
						   		$("#dialog-excluir", this.workspace).dialog("close");
						   		
								var tipoMensagem = result.tipoMensagem;
								var listaMensagens = result.listaMensagens;
								
								if (tipoMensagem && listaMensagens) {
									
									exibirMensagem(tipoMensagem, listaMensagens);
								}

								carregarImagemCapa(null);
							},
							function(result) {
						   		$("#dialog-excluir", this.workspace).dialog("close");
								
								var tipoMensagem = result.tipoMensagem;
								var listaMensagens = result.listaMensagens;
								
								if (tipoMensagem && listaMensagens) {
									
									exibirMensagem(tipoMensagem, listaMensagens);
								}
							},
							true
						);
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				beforeClose: function() {
					clearMessageDialogTimeout();
				}
			});
		},
			
		popup_excluir_capa : function() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );

			$( "#dialog-excluir-capa" , this.workspace).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$.postJSON(
							contextPath + '/capa/removerCapa',
							{idProdutoEdicao : $("#idProdutoEdicao").val()},
							function(result) {
								$( "#dialog-excluir-capa", this.workspace ).dialog( "close" );
								
								var mensagens = (result.mensagens) ? result.mensagens : result.result;
								var tipoMensagem = mensagens.tipoMensagem;
								var listaMensagens = mensagens.listaMensagens;
								
								if (tipoMensagem && listaMensagens) {
									exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
								}
							},
							function(result) {
								$( "#dialog-excluir-capa", this.workspace ).dialog( "close" );

								var mensagens = (result.mensagens) ? result.mensagens : result.result;
								var tipoMensagem = mensagens.tipoMensagem;
								var listaMensagens = mensagens.listaMensagens;
								
								if (tipoMensagem && listaMensagens) {
									exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
								}
							},
							true
						);
					},
					"Cancelar": function() {
						$( "#dialog-excluir-capa", this.workspace).dialog( "close" );
					}
				}
			});
		},		
		
	//Pesquisa por código de produto
	pesquisarPorCodigoProduto : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
		var codigoProduto = $(idCodigo).val();
		
		codigoProduto = $.trim(codigoProduto);
		
		$(idCodigo).val(codigoProduto);
		
		$(idProduto).val("");
		
		if (codigoProduto && codigoProduto.length > 0) {
			
			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
					   "codigoProduto=" + codigoProduto,
					   function(result) { produtoEdicaoController.pesquisarPorCodigoSuccessCallBack(result, idProduto, successCallBack); },
					   function() { produtoEdicaoController.pesquisarPorCodigoErrorCallBack(idCodigo, errorCallBack); }, isFromModal);
		
		} else {
		
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	//Pesquisa por código de produto
	pesquisarPorCodigoProdutoAutoCompleteEdicao : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
		var codigoProduto = $(idCodigo).val();
		
		codigoProduto = $.trim(codigoProduto);
		
		$(idCodigo).val(codigoProduto);
		
		$(idProduto).val("");
		
		if (codigoProduto && codigoProduto.length > 0) {
			
			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
					   "codigoProduto=" + codigoProduto,
					   function(result) { produtoEdicaoController.pesquisarPorCodigoSuccessCallBack(result, idProduto, successCallBack, idCodigo, isFromModal); },
					   null, isFromModal);
		
		} else {
		
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},

	pesquisarPorCodigoSuccessCallBack : function(result, idProduto, successCallBack, idCodigo, isFromModal) {
		
		$(idProduto).val(result.nome);
		
		produtoEdicaoController.pesquisaRealizada = true;
		
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
	autoCompletarPorNomeProduto : function(idProduto, isFromModal) {
		
		produtoEdicaoController.pesquisaRealizada = false;
		
		var nomeProduto = $(idProduto).val();
		
		if (nomeProduto && nomeProduto.length > 2) {
			$.postJSON(contextPath + "/produto/autoCompletarPorPorNomeProduto", "nomeProduto=" + nomeProduto,
					   function(result) { produtoEdicaoController.exibirAutoComplete(result, idProduto); },
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
				produtoEdicaoController.descricaoAtribuida = false;
			},
			close : function(event, ui) {
				produtoEdicaoController.descricaoAtribuida = true;
			},
			select : function(event, ui) {
				produtoEdicaoController.descricaoAtribuida = true;
			},
			minLength: 4,
			delay : 0,
		});
	},
	
	//Pesquisar por nome do produto
	pesquisarPorNomeProduto : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
		
		setTimeout(function() { clearInterval(produtoEdicaoController.intervalo); }, 10 * 1000);
		
		produtoEdicaoController.intervalo = setInterval(function() {
			
			if (produtoEdicaoController.descricaoAtribuida) {
				
				if (produtoEdicaoController.pesquisaRealizada) {
					
					clearInterval(produtoEdicaoController.intervalo);
					
					return;
				}
				
				produtoEdicaoController.pesquisarPorNomeProdutoAposIntervalo(idCodigo, idProduto,
															isFromModal, successCallBack, errorCallBack);
			}
			
		}, 100);
	},
	
	pesquisarPorNomeProdutoAposIntervalo : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
		
		clearInterval(produtoEdicaoController.intervalo);
		
		produtoEdicaoController.pesquisaRealizada = true;
		
		var nomeProduto = $(idProduto).val();
		
		nomeProduto = $.trim(nomeProduto);
		
		$(idCodigo).val("");
		
		if (nomeProduto && nomeProduto.length > 0) {
			$.postJSON(contextPath + "/produto/pesquisarPorNomeProduto", "nomeProduto=" + nomeProduto,
					   function(result) { produtoEdicaoController.pesquisarPorNomeSuccessCallBack(result, idCodigo, idProduto, successCallBack); },
					   function() { produtoEdicaoController.pesquisarPorNomeErrorCallBack(idCodigo, idProduto, errorCallBack); }, isFromModal);
		} else {
			
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	pesquisarPorNomeSuccessCallBack : function(result, idCodigo, idProduto, successCallBack) {
		if (result != "") {
			$(idCodigo).val(result.codigo);
			$(idProduto).val(result.nome);
			
			if (successCallBack) {
				successCallBack();
			}
		}
	},
	
	pesquisarPorNomeErrorCallBack : function(idCodigo, idProduto, errorCallBack) {
		$(idProduto).val("");
		$(idProduto).focus();
		
		if (errorCallBack) {
			errorCallBack();
		}
	},	

}, BaseController);