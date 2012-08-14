var produtoEdicaoController = $.extend(true, {

		init : function() {
			this.initGrids();
			$( "#tabEdicoes", produtoEdicaoController.workspace).tabs();
			$( "#pDateLanctoDe,#pDateLanctoAte", produtoEdicaoController.workspace ).datepicker({
				showOn: "button",
				buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true
			});		
			
			$( "#dateLancto_pop", produtoEdicaoController.workspace ).datepicker({
				showOn: "button",
				buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true
			});
			$( "#dtLancto", produtoEdicaoController.workspace ).datepicker({
				showOn: "button",
				buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true
			});
			$( "#pesqProdutos", produtoEdicaoController.workspace ).fadeIn('slow');
			$( "#pesqProdutos", produtoEdicaoController.workspace ).fadeOut('slow');
			$( ".prodLinhas", produtoEdicaoController.workspace ).show('slow');
			
			$("#numeroEdicao", produtoEdicaoController.workspace).numeric();
			$("#pacotePadrao", produtoEdicaoController.workspace).numeric();
			$("#repartePrevisto", produtoEdicaoController.workspace).numeric();
			$("#expectativaVenda", produtoEdicaoController.workspace).numeric();
			$("#repartePromocional", produtoEdicaoController.workspace).numeric();
			$("#precoPrevisto", produtoEdicaoController.workspace).numeric();
			$("#precoVenda", produtoEdicaoController.workspace).numeric();
			$("#desconto", produtoEdicaoController.workspace).numeric();
			$("#peso", produtoEdicaoController.workspace).numeric();
			$("#largura", produtoEdicaoController.workspace).numeric();
			$("#comprimento", produtoEdicaoController.workspace).numeric();
			$("#espessura", produtoEdicaoController.workspace).numeric();
			$("#numeroLancamento", produtoEdicaoController.workspace).numeric();
			$("#ped", produtoEdicaoController.workspace).numeric();
			

			$("#dataLancamentoPrevisto", produtoEdicaoController.workspace).mask("99/99/9999");
			$("#dataRecolhimentoPrevisto", produtoEdicaoController.workspace).mask("99/99/9999");
			$("#dataRecolhimentoReal", produtoEdicaoController.workspace).mask("99/99/9999");
			$("#dataLancamento", produtoEdicaoController.workspace).mask("99/99/9999");
			
			$('#possuiBrinde', produtoEdicaoController.workspace).change(function(){
				if($(this).attr('checked')){
					$('.descBrinde', produtoEdicaoController.workspace).show();
				}else{
					$('.descBrinde', produtoEdicaoController.workspace).hide();
				}
			});

		},
		
		initGrids : function () {
			$(".bonificacoesGrid", produtoEdicaoController.workspace).flexigrid({
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

		$(".prodsPesqGrid", produtoEdicaoController.workspace).flexigrid({
				dataType : 'json',
				preProcess: produtoEdicaoController.executarPreProcessamento,
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
			
			
		$(".edicoesGrid", produtoEdicaoController.workspace).flexigrid({
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
			
			$(".produtoEdicaoBaseGrid", produtoEdicaoController.workspace).flexigrid({
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

			var codigoProduto = $("#pCodigoProduto", produtoEdicaoController.workspace).val();
			var nomeProduto = $("#pNomeProduto", produtoEdicaoController.workspace).val();
			var dataLancamentoDe = $("#pDateLanctoDe", produtoEdicaoController.workspace).val();	
			var precoDe = $("#pPrecoDe", produtoEdicaoController.workspace).val();
			var precoAte = $("#pPrecoAte", produtoEdicaoController.workspace).val();
			var dataLancamentoAte = $("#pDateLanctoAte", produtoEdicaoController.workspace).val();
			var situacaoLancamento = $("#pSituacaoLancamento", produtoEdicaoController.workspace).val();
			var codigoDeBarras = $("#pCodigoDeBarras", produtoEdicaoController.workspace).val();
			
			$("#pBrinde", produtoEdicaoController.workspace).val(0);
			if (document.getElementById('pBrinde', produtoEdicaoController.workspace).checked){
				$("#pBrinde", produtoEdicaoController.workspace).val(1);
			}
			var brinde = $("#pBrinde", produtoEdicaoController.workspace).val();
			
			$(".edicoesGrid", produtoEdicaoController.workspace).flexOptions({
				url: contextPath + "/cadastro/edicao/pesquisarEdicoes.json",
				params: [{name:'codigoProduto', value: codigoProduto },
					     {name:'nomeProduto', value: nomeProduto },
					     {name:'dataLancamentoDe', value: dataLancamentoDe },
					     {name:'dataLancamentoAte', value: dataLancamentoAte },
					     {name:'precoDe', value: precoDe },
					     {name:'precoAte', value: precoAte },
					     {name:'situacaoLancamento', value: situacaoLancamento },
					     {name:'codigoDeBarras', value: codigoDeBarras },
					     {name:'brinde', value : brinde }],
				newp: 1,
			});
			
			$(".edicoesGrid", produtoEdicaoController.workspace).flexReload();
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
						     	  	'<img title="Editar" src="'+ contextPath +'/images/ico_editar.gif" hspace="5" border="0px" />' +
						  		  '</a>';
				
				var linkExcluir = '<a href="javascript:;" onclick="produtoEdicaoController.removerEdicao(' + row.cell.id + ');" style="cursor:pointer">' +
								   	 '<img title="Excluir" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
								   '</a>';
				
				row.cell.acao = linkAprovar + linkExcluir;

				//
				if(row.cell.nomeProduto){
					nProduto = row.cell.nomeProduto;
				}else{
					row.cell.nomeProduto = '';
				}
				cProduto = row.cell.codigoProduto;
			});
				
			$(".grids", produtoEdicaoController.workspace).show();

			//
			var txt = '';
			if (nProduto != null || cProduto != null) {
				txt = ": " + cProduto + " - " + nProduto;
			}
			$("#labelNomeProduto", produtoEdicaoController.workspace).html(txt);
			$("#codigoProduto", produtoEdicaoController.workspace).val(cProduto);
			
			return resultado;

		},

		salvaUmaEdicao : function() {
			
			$("#formUpload", produtoEdicaoController.workspace).ajaxSubmit({
				success: function(responseText, statusText, xhr, $form)  { 
					var mensagens = (responseText.mensagens) ? responseText.mensagens : responseText.result;   
					var tipoMensagem = mensagens.tipoMensagem;
					var listaMensagens = mensagens.listaMensagens;
					if (tipoMensagem && listaMensagens) {
						produtoEdicaoController.exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
					}
					
					produtoEdicaoController.prepararTela(null);
					produtoEdicaoController.carregarDialog(null);
				}, 
				url: contextPath + "/cadastro/edicao/salvar",
				type: 'POST',
				dataType: 'json',
				data: { codigoProduto : $("#codigoProduto", produtoEdicaoController.workspace).val() }
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
			$('#formUpload', produtoEdicaoController.workspace).find(':input').each(
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
			
			
			// Popular a lista de Edições:
			$(".prodsPesqGrid", produtoEdicaoController.workspace).flexOptions({
				url: contextPath + "/cadastro/edicao/ultimasEdicoes.json",
				params: [{name:'codigoProduto', value: $("#codigoProduto", produtoEdicaoController.workspace).val() }],
				newp: 1,
			});

			$(".prodsPesqGrid", produtoEdicaoController.workspace).flexReload();	
	
		},

		carregarDialog : function(id) {
			// Exibir os dados do Produto:
			$.postJSON(
				contextPath + "/cadastro/edicao/carregarDadosProdutoEdicao.json",
				{ codigoProduto : $("#codigoProduto", produtoEdicaoController.workspace).val(), 
				  idProdutoEdicao : id},
				function(result) {
					$("#tabSegmentacao", produtoEdicaoController.workspace).show();	
					if (result) {
						$("#idProdutoEdicao", produtoEdicaoController.workspace).val(result.id);
						$("#codigoProdutoEdicao", produtoEdicaoController.workspace).val(result.codigoProduto);
						$("#nomePublicacao", produtoEdicaoController.workspace).val(result.nomeProduto);
						$("#nomeComercialProduto", produtoEdicaoController.workspace).val(result.nomeComercialProduto);
						$("#nomeFornecedor", produtoEdicaoController.workspace).val(result.nomeFornecedor);
						$("#situacao", produtoEdicaoController.workspace).val(result.situacao);
						$("#numeroEdicao", produtoEdicaoController.workspace).val(result.numeroEdicao);
						$("#fase", produtoEdicaoController.workspace).val(result.fase);
						$("#numeroLancamento", produtoEdicaoController.workspace).val(result.numeroLancamento);
						$("#pacotePadrao", produtoEdicaoController.workspace).val(result.pacotePadrao);
						$("#tipoLancamento", produtoEdicaoController.workspace).val(result.tipoLancamento);
						$("#precoPrevisto", produtoEdicaoController.workspace).val(result.precoPrevisto);
						$("#precoVenda", produtoEdicaoController.workspace).val(result.precoVenda);
						$("#dataLancamentoPrevisto", produtoEdicaoController.workspace).val(result.dataLancamentoPrevisto == undefined ? '' : result.dataLancamentoPrevisto.$);
						$("#dataLancamento", produtoEdicaoController.workspace).val(result.dataLancamento == undefined ? '' : result.dataLancamento.$);
						$("#repartePrevisto", produtoEdicaoController.workspace).val(result.repartePrevisto)
						$("#expectativaVenda", produtoEdicaoController.workspace).val(result.expectativaVenda);
						$("#repartePromocional", produtoEdicaoController.workspace).val(result.repartePromocional);
						//$("#categoria").val();
						$("#codigoDeBarras", produtoEdicaoController.workspace).val(result.codigoDeBarras);
						$("#codigoDeBarrasCorporativo", produtoEdicaoController.workspace).val(result.codigoDeBarrasCorporativo);
						$("#desconto", produtoEdicaoController.workspace).val(result.desconto);
						$("#peso", produtoEdicaoController.workspace).val(result.peso);
						$("#largura", produtoEdicaoController.workspace).val(result.largura);
						$("#comprimento", produtoEdicaoController.workspace).val(result.comprimento);
						$("#espessura", produtoEdicaoController.workspace).val(result.espessura);
						$("#chamadaCapa", produtoEdicaoController.workspace).val(result.chamadaCapa);
						$('#parcial', produtoEdicaoController.workspace).val(result.parcial + "");
						$('#possuiBrinde', produtoEdicaoController.workspace).attr('checked', result.possuiBrinde).change();
						$('#boletimInformativo', produtoEdicaoController.workspace).val(result.boletimInformativo);
						
						$('#dataRecolhimentoPrevisto', produtoEdicaoController.workspace).val(result.dataRecolhimentoPrevisto == undefined ? '' : result.dataRecolhimentoPrevisto.$).attr("readonly", false);
						$('#semanaRecolhimento', produtoEdicaoController.workspace).val(result.semanaRecolhimento);
						$('#dataRecolhimentoReal', produtoEdicaoController.workspace).val(result.dataRecolhimentoReal == undefined ? '' : result.dataRecolhimentoReal.$);
						$("#ped", produtoEdicaoController.workspace).val(result.ped).attr("readonly", false);		
						$("#descricaoProduto", produtoEdicaoController.workspace).val(result.descricaoProduto).attr("readonly", false);
						$("#descricaoBrinde", produtoEdicaoController.workspace).val(result.descricaoBrinde).attr("readonly", false);
						
						if (result.origemInterface) {
							$("#precoVenda", produtoEdicaoController.workspace).attr("readonly", false);	
						} else {
							$("#tabSegmentacao", produtoEdicaoController.workspace).hide();	
							$("#codigoProdutoEdicao", produtoEdicaoController.workspace).attr("readonly", false);
							$("#nomeComercialProduto", produtoEdicaoController.workspace).attr("readonly", false);
							$("#numeroEdicao", produtoEdicaoController.workspace).attr("readonly", (result.numeroEdicao == 1));
							$("#pacotePadrao", produtoEdicaoController.workspace).attr("readonly", false);
							$("#tipoLancamento", produtoEdicaoController.workspace).attr("disabled", false);
							$("#precoPrevisto", produtoEdicaoController.workspace).attr("readonly", false);
							$("#dataLancamentoPrevisto", produtoEdicaoController.workspace).attr("readonly", false);
							$("#repartePrevisto", produtoEdicaoController.workspace).attr("readonly", false);
							$("#repartePromocional", produtoEdicaoController.workspace).attr("readonly", false);
							$("#codigoDeBarrasCorporativo", produtoEdicaoController.workspace).attr("readonly", false);
							$('#parcial', produtoEdicaoController.workspace).attr("disabled", false);
							$("#desconto", produtoEdicaoController.workspace).attr("readonly", false);
							$("#largura", produtoEdicaoController.workspace).attr("readonly", false);
							$("#comprimento", produtoEdicaoController.workspace).attr("readonly", false);
							$("#espessura", produtoEdicaoController.workspace).attr("readonly", false);
							$('#boletimInformativo', produtoEdicaoController.workspace).attr("readonly", false);
						}

						$("#numeroLancamento", produtoEdicaoController.workspace).attr("readonly", false); 
						$("#imagemCapa", produtoEdicaoController.workspace).attr("disabled", false);
						$("#codigoDeBarras", produtoEdicaoController.workspace).attr("readonly", false);				
						$("#chamadaCapa", produtoEdicaoController.workspace).attr("readonly", false);
						$('#possuiBrinde', produtoEdicaoController.workspace).attr("disabled", false);
						$("#peso", produtoEdicaoController.workspace).attr("readonly", false);
					}
				},
				function(result) { 
					exibirMensagemDialog(result.tipoMensagem, result.listaMensagens, "");
					},
				true
			);			

		},

		popup : function(id) {
			if ($("#pCodigoProduto", produtoEdicaoController.workspace).val() != $("#codigoProduto", produtoEdicaoController.workspace).val()){
				$("#codigoProduto", produtoEdicaoController.workspace).val($("#pCodigoProduto", produtoEdicaoController.workspace).val());
			}
			
			//if ($(".edicoesGrid > tbody").data() == null || $(".edicoesGrid > tbody").data() == undefined) {
			if ($("#codigoProduto", produtoEdicaoController.workspace).val() == "") {
				exibirMensagem('WARNING', ['Por favor, escolha um produto para adicionar a Edi&ccedil;&atilde;o!'], "");
				return;
			}
			
			pesquisarEdicoes();
			prepararTela(id);
			carregarDialog(id);

			$( "#dialog-novo", produtoEdicaoController.workspace ).dialog({
				resizable: false,
				height:615,
				width:960,
				modal: true,
				buttons: {
					"Confirmar": function() {

						$("#formUpload", produtoEdicaoController.workspace).ajaxSubmit({
							beforeSubmit: function(arr, formData, options) {
								// Incluir aqui as validacoes;
							},
							success: function(responseText, statusText, xhr, $form)  { 
								var mensagens = (responseText.mensagens) ? responseText.mensagens : responseText.result;   
								var tipoMensagem = mensagens.tipoMensagem;
								var listaMensagens = mensagens.listaMensagens;
								if (tipoMensagem && listaMensagens) {
									//exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');

									$("#dialog-novo", produtoEdicaoController.workspace).dialog( "close" );
									produtoEdicaoController.pesquisarEdicoes();
									produtoEdicaoController.exibirMensagem(tipoMensagem, listaMensagens);
								}
							}, 
							url: contextPath + "/cadastro/edicao/salvar",
							type: 'POST',
							dataType: 'json',
							data: { codigoProduto : $("#codigoProduto", produtoEdicaoController.workspace).val() }
						});
					},
					"Cancelar": function() {
						$("#dialog-novo", produtoEdicaoController.workspace).dialog( "close" );
					}
				}
			});			
		},

		carregarImagemCapa : function(idProdutoEdicao) {

			var imgPath = (idProdutoEdicao == null || idProdutoEdicao == undefined)
				? "" : contextPath + "/capa/" + idProdutoEdicao + '?' + Math.random(); 
			var img = $("<img />").attr('src', imgPath).attr('width', '144').attr('height', '185').attr('alt', 'Capa');
			$("#div_imagem_capa").empty();
			$("#div_imagem_capa").append(img);
			
			img.load(function() {
				if (!(!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0)) {
					$("#div_imagem_capa").append(img);
				}
			});

		},

		form_clear : function(formName) {

			$('#' + formName, produtoEdicaoController.workspace).find(':input').each(
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
			$( "#dialog-novo", produtoEdicaoController.workspace).dialog({
				resizable: false,
				height:615,
				width:960,
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
			$( "#dialog-excluir", produtoEdicaoController.workspace).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {

						$.postJSON(
							contextPath + "/cadastro/edicao/removerEdicao.json",
							{idProdutoEdicao : id},
							function(result) {
						   		$("#dialog-excluir", produtoEdicaoController.workspace).dialog("close");
						   		
								var tipoMensagem = result.tipoMensagem;
								var listaMensagens = result.listaMensagens;
								
								if (tipoMensagem && listaMensagens) {
									
									produtoEdicaoController.exibirMensagem(tipoMensagem, listaMensagens);
								}

								produtoEdicaoController.carregarImagemCapa(null);
							},
							function(result) {
						   		$("#dialog-excluir", produtoEdicaoController.workspace).dialog("close");
								
								var tipoMensagem = result.tipoMensagem;
								var listaMensagens = result.listaMensagens;
								
								if (tipoMensagem && listaMensagens) {
									
									produtoEdicaoController.exibirMensagem(tipoMensagem, listaMensagens);
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
					produtoEdicaoController.clearMessageDialogTimeout();
				}
			});
		},

		popup_excluir_capa : function() {
			$( "#dialog-excluir-capa", produtoEdicaoController.workspace ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$.postJSON(
							contextPath + "/capa/removerCapa",
							{idProdutoEdicao : $("#idProdutoEdicao", produtoEdicaoController.workspace).val()},
							function(result) {
								$( "#dialog-excluir-capa", produtoEdicaoController.workspace ).dialog( "close" );
								
								var mensagens = (result.mensagens) ? result.mensagens : result;
								var tipoMensagem = mensagens.tipoMensagem;
								var listaMensagens = mensagens.listaMensagens;
								
								if (tipoMensagem && listaMensagens) {
									exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
									if (tipoMensagem == "SUCCESS") { 
										$("#div_imagem_capa", produtoEdicaoController.workspace).empty();
									}
								}
							},
							function(result) {
								$( "#dialog-excluir-capa", produtoEdicaoController.workspace ).dialog( "close" );

								var mensagens = (result.mensagens) ? result.mensagens : result.result;
								var tipoMensagem = mensagens.tipoMensagem;
								var listaMensagens = mensagens.listaMensagens;
								
								if (tipoMensagem && listaMensagens) {
									produtoEdicaoController.exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
								}
							},
							true
						);
					},
					"Cancelar": function() {
						$( "#dialog-excluir-capa", produtoEdicaoController.workspace ).dialog( "close" );
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
					
					produtoEdicaoController.clearInterval(produtoEdicaoController.intervalo);
					
					return;
				}
				
				produtoEdicaoController.pesquisarPorNomeProdutoAposIntervalo(idCodigo, idProduto,
															isFromModal, successCallBack, errorCallBack);
			}
			
		}, 100);
	},
	
	pesquisarPorNomeProdutoAposIntervalo : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
		
		produtoEdicaoController.clearInterval(produtoEdicaoController.intervalo);
		
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
			$(idCodigo, produtoEdicaoController.workspace).val(result.codigo);
			$(idProduto, produtoEdicaoController.workspace).val(result.nome);
			
			if (successCallBack) {
				successCallBack();
			}
		}
	},
	
	pesquisarPorNomeErrorCallBack : function(idCodigo, idProduto, errorCallBack) {
		$(idProduto, produtoEdicaoController.workspace).val("");
		$(idProduto, produtoEdicaoController.workspace).focus();
		
		if (errorCallBack) {
			errorCallBack();
		}
	},	

}, BaseController);