var produtoEdicaoController = {

		// Pesquisa por código de produto
		pesquisarPorCodigoProduto : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
			var codigoProduto = $(idCodigo,this.workspace).val();

			codigoProduto = $.trim(codigoProduto);

			$(idCodigo,this.workspace).val(codigoProduto);

			$(idProduto,this.workspace).val("");

			if (codigoProduto && codigoProduto.length > 0) {

				$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
						"codigoProduto=" + codigoProduto,
						function(result) { produtoEdicao.pesquisarPorCodigoSuccessCallBack(result, idProduto, successCallBack); },
						function() { produtoEdicao.pesquisarPorCodigoErrorCallBack(idCodigo, errorCallBack); }, isFromModal);

			} else {

				if (errorCallBack) {
					errorCallBack();
				}
			}
		},

		// Pesquisa por código de produto
		pesquisarPorCodigoProdutoAutoCompleteEdicao : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
			var codigoProduto = $(idCodigo,this.workspace).val();

			codigoProduto = $.trim(codigoProduto);

			$(idCodigo,this.workspace).val(codigoProduto);

			$(idProduto,this.workspace).val("");

			if (codigoProduto && codigoProduto.length > 0) {

				$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
						"codigoProduto=" + codigoProduto,
						function(result) { produtoEdicao.pesquisarPorCodigoSuccessCallBack(result, idProduto, successCallBack, idCodigo, isFromModal); },
						null, isFromModal);

			} else {

				if (errorCallBack) {
					errorCallBack();
				}
			}
		},

		pesquisarPorCodigoSuccessCallBack : function(result, idProduto, successCallBack, idCodigo, isFromModal) {

			$(idProduto,this.workspace).val(result.nome);

			produtoEdicao.pesquisaRealizada = true;

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

		// Mostrar auto complete por nome do produto
		autoCompletarPorNomeProduto : function(idProduto, isFromModal) {

			produtoEdicao.pesquisaRealizada = false;

			var nomeProduto = $(idProduto,this.workspace).val();

			if (nomeProduto && nomeProduto.length > 2) {
				$.postJSON(contextPath + "/produto/autoCompletarPorPorNomeProduto", "nomeProduto=" + nomeProduto,
						function(result) { produtoEdicao.exibirAutoComplete(result, idProduto); },
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
					produtoEdicao.descricaoAtribuida = false;
				},
				close : function(event, ui) {
					produtoEdicao.descricaoAtribuida = true;
				},
				select : function(event, ui) {
					produtoEdicao.descricaoAtribuida = true;
				},
				minLength: 4,
				delay : 0,
			});
		},

		// Pesquisar por nome do produto
		pesquisarPorNomeProduto : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {

			setTimeout(function() { clearInterval(produtoEdicao.intervalo); }, 10 * 1000);

			produtoEdicao.intervalo = setInterval(function() {

				if (produtoEdicao.descricaoAtribuida) {

					if (produtoEdicao.pesquisaRealizada) {

						clearInterval(produtoEdicao.intervalo);

						return;
					}

					produtoEdicao.pesquisarPorNomeProdutoAposIntervalo(idCodigo, idProduto,
							isFromModal, successCallBack, errorCallBack);
				}

			}, 100);
		},

		pesquisarPorNomeProdutoAposIntervalo : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {

			clearInterval(produtoEdicao.intervalo);

			produtoEdicao.pesquisaRealizada = true;

			var nomeProduto = $(idProduto,this.workspace).val();

			nomeProduto = $.trim(nomeProduto);

			$(idCodigo,this.workspace).val("");

			if (nomeProduto && nomeProduto.length > 0) {
				$.postJSON(contextPath + "/produto/pesquisarPorNomeProduto", "nomeProduto=" + nomeProduto,
						function(result) { produtoEdicao.pesquisarPorNomeSuccessCallBack(result, idCodigo, idProduto, successCallBack); },
						function() { produtoEdicao.pesquisarPorNomeErrorCallBack(idCodigo, idProduto, errorCallBack); }, isFromModal);
			} else {

				if (errorCallBack) {
					errorCallBack();
				}
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

		init : function(){

			$( "#tabEdicoes" ).tabs();
			$( "#pDateLanctoDe,#pDateLanctoAte" ).datepicker({
				showOn: "button",
				buttonImage: contextPath +"/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true
			});		

			$( "#dateLancto_pop" ).datepicker({
				showOn: "button",
				buttonImage: contextPath +"/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true
			});
			$( "#dtLancto" ).datepicker({
				showOn: "button",
				buttonImage: contextPath+"/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true
			});


			$("#produtoEdicaoController.numeroEdicao").numeric();
			$("#produtoEdicaoController.pacotePadrao").numeric();
			$("#produtoEdicaoController.repartePrevisto").numeric();
			$("#produtoEdicaoController.expectativaVenda").numeric();
			$("#produtoEdicaoController.repartePromocional").numeric();
			$("#produtoEdicaoController.precoPrevisto").numeric();
			$("#produtoEdicaoController.precoVenda").numeric();
			$("#produtoEdicaoController.desconto").numeric();
			$("#produtoEdicaoController.peso").numeric();
			$("#produtoEdicaoController.largura").numeric();
			$("#produtoEdicaoController.comprimento").numeric();
			$("#produtoEdicaoController.espessura").numeric();
			$("#produtoEdicaoController.numeroLancamento").numeric();
			$("#produtoEdicaoController.ped").numeric();


			$("#produtoEdicaoController.dataLancamentoPrevisto").mask("99/99/9999");
			$("#produtoEdicaoController.dataRecolhimentoPrevisto").mask("99/99/9999");
			$("#produtoEdicaoController.dataRecolhimentoReal").mask("99/99/9999");
			$("#produtoEdicaoController.dataLancamento").mask("99/99/9999");

			$('#produtoEdicaoController.possuiBrinde').change(function(){
				if($(this).attr('checked')){
					$('.descBrinde').show();
				}else{
					$('.descBrinde').hide();
				}
			});
			
			
			$(".bonificacoesGrid").flexigrid({
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
					display : 'A&ccedil;ões',
					name : 'acoes',
					width : 60,
					sortable : true,
					align : 'center'
				}],
				width : 620,
				height : 120,
				singleSelect : true
			});

		$(".prodsPesqGrid").flexigrid({
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
			
			
		$(".edicoesGrid").flexigrid({
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
			
			
			$(".produtoEdicaoBaseGrid").flexigrid({
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

			var codigoProduto = $("#produtoEdicaoController.pCodigoProduto",this.workspace).val();
			var nomeProduto = $("#produtoEdicaoController.pNomeProduto",this.workspace).val();
			var dataLancamentoDe = $("#produtoEdicaoController.pDateLanctoDe",this.workspace).val();	
			var precoDe = $("#produtoEdicaoController.pPrecoDe",this.workspace).val();
			var precoAte = $("#produtoEdicaoController.pPrecoAte",this.workspace).val();
			var dataLancamentoAte = $("#produtoEdicaoController.pDateLanctoAte",this.workspace).val();
			var situacaoLancamento = $("#produtoEdicaoController.pSituacaoLancamento",this.workspace).val();
			var codigoDeBarras = $("#produtoEdicaoController.pCodigoDeBarras",this.workspace).val();

			$("#produtoEdicaoController.pBrinde",this.workspace).val(0);
			if (document.getElementById('pBrinde').checked){
				$("#produtoEdicaoController.pBrinde",this.workspace).val(1);
			}
			var brinde = $("#produtoEdicaoController.pBrinde",this.workspace).val();

			$(".edicoesGrid").flexOptions({
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

			$(".edicoesGrid").flexReload();
		},
		executarPreProcessamento : 	function (resultado) {

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

				var linkAprovar = '<a href="javascript:;" onclick="editarEdicao(' + row.cell.id + ');" style="cursor:pointer">' +
				'<img title="Editar" src="' + contextPath + '/images/ico_editar.gif" hspace="5" border="0px" />' +
				'</a>';

				var linkExcluir = '<a href="javascript:;" onclick="removerEdicao(' + row.cell.id + ');" style="cursor:pointer">' +
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

			$(".grids").show();

			//
			var txt = '';
			if (nProduto != null || cProduto != null) {
				txt = ": " + cProduto + " - " + nProduto;
			}
			$("#produtoEdicaoController.labelNomeProduto").html(txt);
			$("#produtoEdicaoController.codigoProduto",this.workspace).val(cProduto);

			return resultado;
		},
		salvaUmaEdicao:function () {

			$("#produtoEdicaoController.formUpload").ajaxSubmit({
				success: function(responseText, statusText, xhr, $form)  { 
					var mensagens = (responseText.mensagens) ? responseText.mensagens : responseText.result;   
					var tipoMensagem = mensagens.tipoMensagem;
					var listaMensagens = mensagens.listaMensagens;
					if (tipoMensagem && listaMensagens) {
						exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
					}

					prepararTela(null);
					carregarDialog(null);
				}, 
				url:  contextPath + '/cadastro/edicao/salvar',
				type: 'POST',
				dataType: 'json',
				data: { codigoProduto : $("#produtoEdicaoController.codigoProduto",this.workspace).val() }
			});

		},
		novaEdicao : function () {
			popup(null);
		},
		editarEdicao:			function (id) {
			popup(id);
		},

		prepararTela : 			function (id) {

			// limpar os campos:
			form_clear('formUpload');
			carregarImagemCapa(id);
			$('#produtoEdicaoController.formUpload').find(':input').each(
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
			$(".prodsPesqGrid").flexOptions({
				url: contextPath + '/cadastro/edicao/ultimasEdicoes.json',
				params: [{name:'codigoProduto', value: $("#produtoEdicaoController.codigoProduto",this.workspace).val() }],
				newp: 1,
			});

			$(".prodsPesqGrid").flexReload();	
		},
		carregarDialog : 			function (id) {

			// Exibir os dados do Produto:
			$.postJSON(
					 contextPath + '/cadastro/edicao/carregarDadosProdutoEdicao.json',
					{ codigoProduto : $("#produtoEdicaoController.codigoProduto",this.workspace).val(), 
						idProdutoEdicao : id},
						function(result) {
							$("#produtoEdicaoController.tabSegmentacao").show();	
							if (result) {
								$("#produtoEdicaoController.idProdutoEdicao",this.workspace).val(result.id);
								$("#produtoEdicaoController.codigoProdutoEdicao",this.workspace).val(result.codigoProduto);
								$("#produtoEdicaoController.nomePublicacao",this.workspace).val(result.nomeProduto);
								$("#produtoEdicaoController.nomeComercialProduto",this.workspace).val(result.nomeComercialProduto);
								$("#produtoEdicaoController.nomeFornecedor",this.workspace).val(result.nomeFornecedor);
								$("#produtoEdicaoController.situacao",this.workspace).val(result.situacao);
								$("#produtoEdicaoController.numeroEdicao",this.workspace).val(result.numeroEdicao);
								$("#produtoEdicaoController.fase",this.workspace).val(result.fase);
								$("#produtoEdicaoController.numeroLancamento",this.workspace).val(result.numeroLancamento);
								$("#produtoEdicaoController.pacotePadrao",this.workspace).val(result.pacotePadrao);
								$("#produtoEdicaoController.tipoLancamento",this.workspace).val(result.tipoLancamento);
								$("#produtoEdicaoController.precoPrevisto",this.workspace).val(result.precoPrevisto);
								$("#produtoEdicaoController.precoVenda",this.workspace).val(result.precoVenda);
								$("#produtoEdicaoController.dataLancamentoPrevisto",this.workspace).val(result.dataLancamentoPrevisto == undefined ? '' : result.dataLancamentoPrevisto.$);
								$("#produtoEdicaoController.dataLancamento",this.workspace).val(result.dataLancamento == undefined ? '' : result.dataLancamento.$);
								$("#produtoEdicaoController.repartePrevisto",this.workspace).val(result.repartePrevisto);
								$("#produtoEdicaoController.expectativaVenda",this.workspace).val(result.expectativaVenda);
								$("#produtoEdicaoController.repartePromocional",this.workspace).val(result.repartePromocional);
								// $("#produtoEdicaoController.categoria",this.workspace).val();
								$("#produtoEdicaoController.codigoDeBarras",this.workspace).val(result.codigoDeBarras);
								$("#produtoEdicaoController.codigoDeBarrasCorporativo",this.workspace).val(result.codigoDeBarrasCorporativo);
								$("#produtoEdicaoController.desconto",this.workspace).val(result.desconto);
								$("#produtoEdicaoController.peso",this.workspace).val(result.peso);
								$("#produtoEdicaoController.largura",this.workspace).val(result.largura);
								$("#produtoEdicaoController.comprimento",this.workspace).val(result.comprimento);
								$("#produtoEdicaoController.espessura",this.workspace).val(result.espessura);
								$("#produtoEdicaoController.chamadaCapa",this.workspace).val(result.chamadaCapa);
								$('#produtoEdicaoController.parcial',this.workspace).val(result.parcial + "");
								$('#produtoEdicaoController.possuiBrinde').attr('checked', result.possuiBrinde).change();
								$('#produtoEdicaoController.boletimInformativo',this.workspace).val(result.boletimInformativo);

								$('#produtoEdicaoController.dataRecolhimentoPrevisto',this.workspace).val(result.dataRecolhimentoPrevisto == undefined ? '' : result.dataRecolhimentoPrevisto.$).attr("readonly", false);
								$('#produtoEdicaoController.semanaRecolhimento',this.workspace).val(result.semanaRecolhimento);
								$('#produtoEdicaoController.dataRecolhimentoReal',this.workspace).val(result.dataRecolhimentoReal == undefined ? '' : result.dataRecolhimentoReal.$);
								$("#produtoEdicaoController.ped",this.workspace).val(result.ped).attr("readonly", false);		
								$("#produtoEdicaoController.descricaoProduto",this.workspace).val(result.descricaoProduto).attr("readonly", false);
								$("#produtoEdicaoController.descricaoBrinde",this.workspace).val(result.descricaoBrinde).attr("readonly", false);

								if (result.origemInterface) {
									$("#produtoEdicaoController.precoVenda").attr("readonly", false);	
								} else {
									$("#produtoEdicaoController.tabSegmentacao").hide();	
									$("#produtoEdicaoController.codigoProdutoEdicao").attr("readonly", false);
									$("#produtoEdicaoController.nomeComercialProduto").attr("readonly", false);
									$("#produtoEdicaoController.numeroEdicao").attr("readonly", (result.numeroEdicao == 1));
									$("#produtoEdicaoController.pacotePadrao").attr("readonly", false);
									$("#produtoEdicaoController.tipoLancamento").attr("disabled", false);
									$("#produtoEdicaoController.precoPrevisto").attr("readonly", false);
									$("#produtoEdicaoController.dataLancamentoPrevisto").attr("readonly", false);
									$("#produtoEdicaoController.repartePrevisto").attr("readonly", false);
									$("#produtoEdicaoController.repartePromocional").attr("readonly", false);
									$("#produtoEdicaoController.codigoDeBarrasCorporativo").attr("readonly", false);
									$('#produtoEdicaoController.parcial').attr("disabled", false);
									$("#produtoEdicaoController.desconto").attr("readonly", false);
									$("#produtoEdicaoController.largura").attr("readonly", false);
									$("#produtoEdicaoController.comprimento").attr("readonly", false);
									$("#produtoEdicaoController.espessura").attr("readonly", false);
									$('#produtoEdicaoController.boletimInformativo').attr("readonly", false);
								}

								$("#produtoEdicaoController.numeroLancamento").attr("readonly", false); 
								$("#produtoEdicaoController.imagemCapa").attr("disabled", false);
								$("#produtoEdicaoController.codigoDeBarras").attr("readonly", false);				
								$("#produtoEdicaoController.chamadaCapa").attr("readonly", false);
								$('#produtoEdicaoController.possuiBrinde').attr("disabled", false);
								$("#produtoEdicaoController.peso").attr("readonly", false);
							}
						},
						function(result) { 
							exibirMensagemDialog(result.tipoMensagem, result.listaMensagens, "");
						},
						true
			);

		},
		popup:			function (id) {

			if ($("#produtoEdicaoController.pCodigoProduto",this.workspace).val() != $("#produtoEdicaoController.codigoProduto",this.workspace).val()){
				$("#produtoEdicaoController.codigoProduto",this.workspace).val($("#produtoEdicaoController.pCodigoProduto",this.workspace).val());
			}

			// if ($(".edicoesGrid > tbody").data() == null ||
			// $(".edicoesGrid > tbody").data() == undefined) {
			if ($("#produtoEdicaoController.codigoProduto",this.workspace).val() == "") {
				exibirMensagem('WARNING', ['Por favor, escolha um produto para adicionar a Edi&ccedil;&atilde;o!'], "");
				return;
			}

			pesquisarEdicoes();
			prepararTela(id);
			carregarDialog(id);

			$( "#dialog-novo" ).dialog({
				resizable: false,
				height:615,
				width:960,
				modal: true,
				buttons: {
					"Confirmar": function() {

						$("#produtoEdicaoController.formUpload").ajaxSubmit({
							beforeSubmit: function(arr, formData, options) {
								// Incluir aqui as validacoes;
							},
							success: function(responseText, statusText, xhr, $form)  { 
								var mensagens = (responseText.mensagens) ? responseText.mensagens : responseText.result;   
								var tipoMensagem = mensagens.tipoMensagem;
								var listaMensagens = mensagens.listaMensagens;
								if (tipoMensagem && listaMensagens) {
									// exibirMensagemDialog(tipoMensagem,
									// listaMensagens,
									// 'dialogMensagemNovo');

									$("#produtoEdicaoController.dialog-novo").dialog( "close" );
									pesquisarEdicoes();
									exibirMensagem(tipoMensagem, listaMensagens);
								}
							}, 
							url:  contextPath + '/cadastro/edicao/salvar',
							type: 'POST',
							dataType: 'json',
							data: { codigoProduto : $("#produtoEdicaoController.codigoProduto",this.workspace).val() }
						});
					},
					"Cancelar": function() {
						$("#produtoEdicaoController.dialog-novo",this.workspace).dialog( "close" );
					}
				}
			});
		},
		carregarImagemCapa:			function (idProdutoEdicao) {

			var imgPath = (idProdutoEdicao == null || idProdutoEdicao == undefined)
			? "" :  contextPath + '/capa/' + idProdutoEdicao + '?' + Math.random(); 
			var img = $("<img />").attr('src', imgPath).attr('width', '144').attr('height', '185').attr('alt', 'Capa');
			$("#produtoEdicaoController.div_imagem_capa",this.workspace).empty();
			$("#produtoEdicaoController.div_imagem_capa",this.workspace).append(img);

			img.load(function() {
				if (!(!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0)) {
					$("#produtoEdicaoController.div_imagem_capa",this.workspace).append(img);
				}
			});

		},
		form_clear:function (formName) {

			$('#produtoEdicaoController.' + formName).find(':input').each(
					function() {
						switch(this.type) {
						case 'hidden': 
						case 'text':
						case 'textarea':
						case 'password':
						case 'select-multiple':
						case 'select-one':
						case 'file':
							$(this,this.workspace).val('');
							break;

						case 'checkbox':
						case 'radio':
							this.checked = false;
							break;
						}
					}
			);
		},
		popup_alterar:			function () {
			// $( "#dialog:ui-dialog" ).dialog( "destroy" );

			$( "#dialog-novo" ).dialog({
				resizable: false,
				height:615,
				width:960,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ,this.workspace).dialog( "close" );
						$("#produtoEdicaoController.effect",this.workspace).hide("highlight", {}, 1000, callback);
						$( "#abaPdv" ).show( );

					},
					"Cancelar": function() {
						$( this ,this.workspace).dialog( "close" );
					}
				}
			});	      
		},
		removerEdicao:				function (id) {

			$( "#dialog-excluir" ,this.workspace).dialog({
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
									$("#produtoEdicaoController.dialog-excluir",this.workspace).dialog("close");

									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;

									if (tipoMensagem && listaMensagens) {

										exibirMensagem(tipoMensagem, listaMensagens);
									}

									carregarImagemCapa(null);
								},
								function(result) {
									$("#produtoEdicaoController.dialog-excluir",this.workspace).dialog("close");

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
		popup_excluir_capa:			function () {
			// $( "#dialog:ui-dialog" ).dialog( "destroy" );

			$( "#dialog-excluir-capa" ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$.postJSON(
								 contextPath + '/capa/removerCapa',
								{idProdutoEdicao : $("#produtoEdicaoController.idProdutoEdicao",this.workspace).val()},
								function(result) {
									$( "#dialog-excluir-capa" ).dialog( "close" );

									var mensagens = (result.mensagens) ? result.mensagens : result;
									var tipoMensagem = mensagens.tipoMensagem;
									var listaMensagens = mensagens.listaMensagens;

									if (tipoMensagem && listaMensagens) {
										exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
										if (tipoMensagem == "SUCCESS") { 
											$("#produtoEdicaoController.div_imagem_capa",this.workspace).empty();
										}
									}
								},
								function(result) {
									$( "#dialog-excluir-capa" ,this.workspace).dialog( "close" );

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
						$( "#dialog-excluir-capa" ,this.workspace).dialog( "close" );
					}
				}
			});
		},
		mostrar_prod:function (){
			$( "#pesqProdutos" ,this.workspace).fadeIn('slow');

		},
		fecha_prod:function (){
			$( "#pesqProdutos",this.workspace ).fadeOut('slow');

		}
		,mostraLinhaProd:

			function (){

			$( ".prodLinhas",this.workspace ).show('slow');
		}

};


