var produtoEdicaoController =$.extend(true,  {

	// Pesquisa por código de produto
	pesquisarPorCodigoProduto : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
		var codigoProduto = $(idCodigo,this.workspace).val();

		codigoProduto = $.trim(codigoProduto);

		$(idCodigo,this.workspace).val(codigoProduto);

		$(idProduto,this.workspace).val("");

		if (codigoProduto && codigoProduto.length > 0) {

			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
					{codigoProduto:codigoProduto},
					function(result) { produtoEdicaoController.pesquisarPorCodigoSuccessCallBack(result, idProduto, successCallBack); },
					function() { produtoEdicaoController.pesquisarPorCodigoErrorCallBack(idCodigo, errorCallBack); }, isFromModal);

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
					{codigoProduto:codigoProduto},
					function(result) { produtoEdicaoController.pesquisarPorCodigoSuccessCallBack(result, idProduto, successCallBack, idCodigo, isFromModal); },
					null, isFromModal);

		} else {

			if (errorCallBack) {
				errorCallBack();
			}
		}
	},

	pesquisarPorCodigoSuccessCallBack : function(result, idProduto, successCallBack, idCodigo, isFromModal) {

		$(idProduto,this.workspace).val(result.nome);

		produtoEdicaoController.pesquisaRealizada = true;

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
	autoCompletarPorNome : function(idProduto, isFromModal) {
		
		produtoEdicaoController.pesquisaRealizada = false;

		var nome = $(idProduto,this.workspace).val();

		if (nome && nome.length > 2) {
			$.postJSON(contextPath + "/produto/autoCompletarPorNomeProduto", {"nomeProduto" : nome},
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

	// Pesquisar por nome do produto
	pesquisarPorNome : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
		
		setTimeout(function() {
			
			clearInterval(produtoEdicaoController.intervalo); 
		
		}, 10 * 1000);

		produtoEdicaoController.intervalo = setInterval(function() {
			
			if (produtoEdicaoController.descricaoAtribuida) {

				if (produtoEdicaoController.pesquisaRealizada) {
					
					clearInterval(produtoEdicaoController.intervalo);

					return;
				}

				produtoEdicaoController.pesquisarPorNomeAposIntervalo(idCodigo, idProduto,
						isFromModal, successCallBack, errorCallBack);
			}

		}, 100);

	},

	pesquisarPorNomeAposIntervalo : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
		
		clearInterval(produtoEdicaoController.intervalo);

		produtoEdicaoController.pesquisaRealizada = true;

		var nomeProduto = $(idProduto,this.workspace).val();

		nomeProduto = $.trim(nomeProduto);

		$(idCodigo,this.workspace).val("");

		if (nomeProduto && nomeProduto.length > 0) {
			$.postJSON(contextPath + "/produto/pesquisarPorNomeProduto", {"nomeProduto" : nomeProduto},
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

		window.addEventListener('blur', function() {

			window.clearInterval(produtoEdicaoController.intervalo);
		
		});

		
		$("#produtoEdicaoController-tabEdicoes",this.workspace ).tabs();
		$("#produtoEdicaoController-pDateLanctoDe,#produtoEdicaoController-pDateLanctoAte",this.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath +"/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});		

		$( "#produtoEdicaoController-dateLancto_pop",this.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath +"/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#produtoEdicaoController-dtLancto",this.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath+"/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});


		$("#produtoEdicaoController-numeroEdicao").numeric();
		$("#produtoEdicaoController-pacotePadrao").numeric();
		$("#produtoEdicaoController-repartePrevisto").numeric();
		$("#produtoEdicaoController-expectativaVenda").numeric();
		$("#produtoEdicaoController-repartePromocional").numeric();
		$("#produtoEdicaoController-precoPrevisto").numeric();
		$("#produtoEdicaoController-precoVenda").numeric();
		$("#produtoEdicaoController-desconto").numeric();
		$("#produtoEdicaoController-peso").numeric();
		$("#produtoEdicaoController-largura").numeric();
		$("#produtoEdicaoController-comprimento").numeric();
		$("#produtoEdicaoController-espessura").numeric();
		$("#produtoEdicaoController-numeroLancamento").numeric();
		$("#produtoEdicaoController-ped").numeric();


		$("#produtoEdicaoController-dataLancamentoPrevisto").mask("99/99/9999");
		$("#produtoEdicaoController-dataRecolhimentoPrevisto").mask("99/99/9999");
		$("#produtoEdicaoController-dataRecolhimentoReal").mask("99/99/9999");
		$("#produtoEdicaoController-dataLancamento").mask("99/99/9999");

		$('#produtoEdicaoController-possuiBrinde',this.workspace).change(function(){
			if($(this,this.workspace).attr('checked')){
				$('.descBrinde',this.workspace).show();
			}else{
				$('.descBrinde',this.workspace,this.workspace).hide();
			}
		});
			
			
		$(".bonificacoesGrid",this.workspace).flexigrid({
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

		$(".prodsPesqGrid",this.workspace).flexigrid({
			dataType : 'json',
			preProcess: produtoEdicaoController.executarPreProcessamentoPesquisados,
			colModel : [ {
				display : 'Produto',
				name : 'nomeComercial',
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
			sortname : "numeroEdicao",
			sortorder : "desc",
			width : 200,
			height : 300,
			singleSelect : true
		});
			
			
		$(".edicoesGrid",this.workspace).flexigrid({
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
				name : 'nomeComercial',
				width : 197,
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
				sortable : false,
				align : 'center'
			}],
			sortname : "numeroEdicao",
			sortorder : "desc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 'auto',
			singleSelect : true
		});
			
			
		$(".produtoEdicaoBaseGrid",this.workspace).flexigrid({
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
		
	pesquisarEdicoes : function(codigoProduto, nomeProduto) {

		if (codigoProduto == "" || codigoProduto == undefined) {
			codigoProduto = $("#produtoEdicaoController-pCodigoProduto",this.workspace).val();
		}
		if (nomeProduto == "" || nomeProduto == undefined) {
			nomeProduto = $("#produtoEdicaoController-pNome",this.workspace).val();
		}
		var dataLancamentoDe = $("#produtoEdicaoController-pDateLanctoDe",this.workspace).val();	
		var precoDe = $("#produtoEdicaoController-pPrecoDe",this.workspace).val();
		var precoAte = $("#produtoEdicaoController-pPrecoAte",this.workspace).val();
		var dataLancamentoAte = $("#produtoEdicaoController-pDateLanctoAte",this.workspace).val();
		var situacaoLancamento = $("#produtoEdicaoController-pSituacaoLancamento",this.workspace).val();
		var codigoDeBarras = $("#produtoEdicaoController-pCodigoDeBarras",this.workspace).val();

		$("#produtoEdicaoController-pBrinde",this.workspace).val(0);
		if (document.getElementById('produtoEdicaoController-pBrinde').checked){
			$("#produtoEdicaoController-pBrinde",this.workspace).val(1);
		}
		var brinde = $("#produtoEdicaoController-pBrinde",this.workspace).val();

		$(".edicoesGrid",this.workspace).flexOptions({
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

		$(".edicoesGrid",this.workspace).flexReload();
	},
	
	executarPreProcessamento : 	function (resultado) {

		// Exibe mensagem de erro/alerta, se houver:
		var mensagens = (resultado.mensagens) ? resultado.mensagens : resultado.result;   
		var tipoMensagem = (mensagens && mensagens.tipoMensagem) ? mensagens.tipoMensagem : null; 
		var listaMensagens = (mensagens && mensagens.listaMensagens) ? mensagens.listaMensagens : null;
		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens);				
			$(".grids").hide();
		}else{

			var nProduto = '';
			var cProduto = '';
			$.each(resultado.rows, function(index, row) {

				var linkAprovar = '<a href="javascript:;" onclick="produtoEdicaoController.editarEdicao(' + row.cell.id + ', \'' + row.cell.codigoProduto + '\', \'' + row.cell.nomeProduto + '\');" style="cursor:pointer; margin-right:10px;">' +
				'<img title="Editar" src="' + contextPath + '/images/ico_editar.gif" border="0px" />' +
				'</a>';

				var linkExcluir = '<a href="javascript:;" onclick="produtoEdicaoController.removerEdicao(' + row.cell.id + ');" style="cursor:pointer">' +
				'<img title="Excluir" src="' + contextPath + '/images/ico_excluir.gif" border="0px" />' +
				'</a>';

				row.cell.acao = linkAprovar + linkExcluir;

				//
				if(row.cell.nomeComercial){
					nProduto = row.cell.nomeComercial;
				}else{
					row.cell.nomeComercial = '';
				}
				cProduto = row.cell.codigoProduto;
			});

			$(".grids").show();

			//
			var txt = '';
			if (nProduto != null || cProduto != null) {
				txt = ": " + cProduto + " - " + nProduto;
			}
			$("#produtoEdicaoController-labelNomeProduto",this.workspace).html(txt);
			
			if (cProduto != "") {
			    
			    $("#produtoEdicaoController-codigoProduto",this.workspace).val(cProduto);
            }

			return resultado;
		}
	},

	executarPreProcessamentoPesquisados : 	function (resultado) {

		$.each(resultado.rows, function(index, row) {
			
			if(row.cell.numeroEdicao){
				
				row.cell.numeroEdicao = '<a href="javascript:;" onclick="produtoEdicaoController.editarEdicao(' + row.cell.id + ');" style="cursor:pointer; margin-right:10px;">' +
				                        '<label style = "height:30px; width:40px;" >'  +
										 row.cell.numeroEdicao +
										'</label>'  + 
										'</a>';
			}else{
				
				row.cell.numeroEdicao = '';
			}

			if(row.cell.nomeComercial){
				
				row.cell.nomeComercial = '<a href="javascript:;" onclick="produtoEdicaoController.editarEdicao(' + row.cell.id + ');" style="cursor:pointer; margin-right:10px;">' +
				 						 '<label style = "height:30px; width:115px;" >'  +						  
										  row.cell.nomeComercial +
										 '</label>'  + 
										 '</a>';
			}else{
				
				row.cell.nomeComercial = '';
			}
		});

		$(".grids").show();

		return resultado;
	},
	
	novaEdicao : function () {
		produtoEdicaoController.popup("", "", "");
	},

	editarEdicao:			function (id, codigo, nome) {
		if (id == undefined) {
			id = "";
		}
		if (codigo == undefined) {
			codigo = "";
		}
		if (nome == undefined) {
			nome = "";
		}
		produtoEdicaoController.popup(id, codigo, nome);
	},

	prepararTela : 			function (id, codigo) {

		produtoEdicaoController.form_clear('dialog-novo');
		
		produtoEdicaoController.carregarImagemCapa(id);
		$('produtoEdicaoController-dialog-novo',this.workspace).find(':input').each(
				
				function() {
					switch(this.type) {
					case 'text':
					case 'textarea':
					case 'password':
						$(this,this.workspace).attr("readonly", true);
						break;

					case 'file':
					case 'select-multiple':
					case 'select-one':
					case 'checkbox':
					case 'radio':
						$(this,this.workspace).attr("disabled", true);
						break;
					}
				}
		);

		if (codigo == "") {
			codigo = $("#produtoEdicaoController-codigoProduto",this.workspace).val();
		}

		// Popular a lista de Edições:
		$(".prodsPesqGrid",this.workspace).flexOptions({
			url: contextPath + '/cadastro/edicao/ultimasEdicoes.json',
			params: [{name:'codigoProduto', value:  codigo}],
			newp: 1,
		});

		$(".prodsPesqGrid",this.workspace).flexReload();	
	},
	
	carregarDialog : 			function (id, codigoProduto) {

		if (codigoProduto == "") {
			codigoProduto = $("#produtoEdicaoController-codigoProduto",this.workspace).val();
		}
		
		// Exibir os dados do Produto:
		$.postJSON(
				 contextPath + '/cadastro/edicao/carregarDadosProdutoEdicao.json',
				{ codigoProduto : codigoProduto, 
					idProdutoEdicao : id},
					function(result) {
						
						if (result) {
							$("#produtoEdicaoController-idProdutoEdicao").val(result.id);
							$("#produtoEdicaoController-codigoProdutoEdicao").val(result.codigoProduto);
							$("#produtoEdicaoController-nomePublicacao").val(result.nomeProduto);
							$("#produtoEdicaoController-nomeComercialProduto").val(result.nomeComercialProduto);
							$("#produtoEdicaoController-nomeFornecedor").val(result.nomeFornecedor);
							$("#produtoEdicaoController-situacao").val(result.situacao);
							$("#produtoEdicaoController-numeroEdicao").val(result.numeroEdicao);
							$("#produtoEdicaoController-fase").val(result.fase);
							$("#produtoEdicaoController-numeroLancamento").val(result.numeroLancamento);
							$("#produtoEdicaoController-pacotePadrao").val(result.pacotePadrao);
							$("#produtoEdicaoController-situacao").val(result.situacaoLancamento);
							$("#produtoEdicaoController-tipoLancamento").val(result.tipoLancamento);
							$("#produtoEdicaoController-precoPrevisto").val(result.precoPrevisto);
							$("#produtoEdicaoController-precoVenda").val(result.precoVenda);
							$("#produtoEdicaoController-dataLancamentoPrevisto").val(result.dataLancamentoPrevisto == undefined ? '' : result.dataLancamentoPrevisto.$);
							$("#produtoEdicaoController-dataLancamento").val(result.dataLancamento == undefined ? '' : result.dataLancamento.$);
							$("#produtoEdicaoController-repartePrevisto").val(result.repartePrevisto);
							$("#produtoEdicaoController-expectativaVenda").val(result.expectativaVenda);
							$("#produtoEdicaoController-repartePromocional").val(result.repartePromocional);
							// $("#produtoEdicaoController-categoria",this.workspace).val();
							$("#produtoEdicaoController-codigoDeBarras").val(result.codigoDeBarras);
							$("#produtoEdicaoController-codigoDeBarrasCorporativo").val(result.codigoDeBarrasCorporativo);
							$("#produtoEdicaoController-desconto").val(result.desconto);
							$("#produtoEdicaoController-peso").val(result.peso);
							$("#produtoEdicaoController-largura").val(result.largura);
							$("#produtoEdicaoController-comprimento").val(result.comprimento);
							$("#produtoEdicaoController-espessura").val(result.espessura);
							$("#produtoEdicaoController-chamadaCapa").val(result.chamadaCapa);
							$('#produtoEdicaoController-parcial').val(result.parcial + "");
							$('#produtoEdicaoController-possuiBrinde').attr('checked', result.possuiBrinde).change();
							$('#produtoEdicaoController-boletimInformativo').val(result.boletimInformativo);
							$('#produtoEdicaoController-semanaRecolhimento').val(result.semanaRecolhimento);
							$('#produtoEdicaoController-dataRecolhimentoReal').val(result.dataRecolhimentoReal == undefined ? '' : result.dataRecolhimentoReal.$);								
							$('#produtoEdicaoController-dataRecolhimentoPrevisto').val(result.dataRecolhimentoPrevisto == undefined ? '' : result.dataRecolhimentoPrevisto.$);
							$("#produtoEdicaoController-ped").val(result.ped);		
							$("#produtoEdicaoController-descricaoProduto").val(result.descricaoProduto);
							$("#produtoEdicaoController-descricaoBrinde").val(result.idBrinde);

							var naoEditavel = result.origemInterface;

						    $("#produtoEdicaoController-tabSegmentacao").toggle(naoEditavel);
							
							$('#produtoEdicaoController-dataRecolhimentoPrevisto').attr("readonly", naoEditavel);
							$("#produtoEdicaoController-ped").attr("readonly", naoEditavel);		
							$("#produtoEdicaoController-descricaoProduto").attr("readonly", naoEditavel);
							$("#produtoEdicaoController-codigoProdutoEdicao").attr("readonly", naoEditavel);
							$("#produtoEdicaoController-nomeComercialProduto").attr("readonly", naoEditavel);
							$("#produtoEdicaoController-numeroEdicao").attr("readonly", (result.numeroEdicao == 1));
							$("#produtoEdicaoController-pacotePadrao").attr("readonly", naoEditavel);
							$("#produtoEdicaoController-tipoLancamento").attr("disabled", naoEditavel);
							$("#produtoEdicaoController-precoPrevisto").attr("readonly", naoEditavel);
							$("#produtoEdicaoController-dataLancamentoPrevisto").attr("readonly", naoEditavel);
							$("#produtoEdicaoController-repartePrevisto").attr("readonly", naoEditavel);
							$("#produtoEdicaoController-repartePromocional").attr("readonly", naoEditavel);
							$("#produtoEdicaoController-codigoDeBarrasCorporativo").attr("readonly", naoEditavel);
							$('#produtoEdicaoController-parcial').attr("disabled", naoEditavel);
							$("#produtoEdicaoController-desconto").attr("readonly", naoEditavel);
							$("#produtoEdicaoController-largura").attr("readonly", naoEditavel);
							$("#produtoEdicaoController-comprimento").attr("readonly", naoEditavel);
							$("#produtoEdicaoController-espessura").attr("readonly", naoEditavel);
							$('#produtoEdicaoController-boletimInformativo').attr("readonly", naoEditavel);

							$("#produtoEdicaoController-precoVenda").attr("readonly", false);	
							$("#produtoEdicaoController-numeroLancamento").attr("readonly", false); 
							$("#produtoEdicaoController-imagemCapa").attr("disabled", false);
							$("#produtoEdicaoController-codigoDeBarras").attr("readonly", false);				
							$("#produtoEdicaoController-chamadaCapa").attr("readonly", false);
							$('#produtoEdicaoController-possuiBrinde').attr("disabled", false);
							$("#produtoEdicaoController-descricaoBrinde").attr("readonly", false);
							$("#produtoEdicaoController-peso").attr("readonly", false);
						}
					},
					function(result) { 
						$("#produtoEdicaoController-dialog-novo",this.workspace).dialog( "close" );
						
						var mensagens = (result.mensagens) ? result.mensagens : result;   
						var tipoMensagem = mensagens.tipoMensagem;
						var listaMensagens = mensagens.listaMensagens;
		
						if (tipoMensagem && listaMensagens) {

							exibirMensagem(tipoMensagem, listaMensagens);
						}
					},
					true
		);

	},
	
	popup:			function (id, codigo, nome) {

		$("#produtoEdicaoController-codigoProduto",this.workspace).val($("#produtoEdicaoController-pCodigoProduto",this.workspace).val());
		
		// if ($(".edicoesGrid > tbody").data() == null ||
		// $(".edicoesGrid > tbody").data() == undefined) {
		if ($("#produtoEdicaoController-codigoProduto",this.workspace).val() == "" && (codigo == "")) {
			exibirMensagem('WARNING', ['Por favor, escolha um produto para adicionar a Edi&ccedil;&atilde;o!'], "");
			return;
		}

		if (codigo == "" || codigo == undefined) {
			codigo = $("#produtoEdicaoController-codigoProduto",this.workspace).val();
		}
		
		if (nome == undefined) {
			nome = "";
		}

		$( "#produtoEdicaoController-dialog-novo" ).dialog({
			resizable: false,
			height:540,
			width:960,
			modal: true,
			buttons: {
				"Confirmar": function() {

					produtoEdicaoController.salvarProdutoEdicao(true);
					
					
				},
				"Cancelar": function() {
					$("#produtoEdicaoController-dialog-novo",this.workspace).dialog( "close" );
				}
			},
			form: $("#produtoEdicaoController-dialog-novo", this.workspace).parents("form")
		});

		produtoEdicaoController.prepararTela(id, codigo);
		produtoEdicaoController.carregarDialog(id, codigo);
	},
	
	salvarProdutoEdicao : function(closePopUp) {

		$("#produtoEdicaoController-formUpload").ajaxSubmit({
			beforeSubmit: function(arr, formData, options) {
				// Incluir aqui as validacoes;
			},
			success: function(responseText, statusText, xhr, $form)  { 
				var mensagens = (responseText.mensagens) ? responseText.mensagens : responseText.result;   
				var tipoMensagem = mensagens.tipoMensagem;
				var listaMensagens = mensagens.listaMensagens;

				if (tipoMensagem && listaMensagens) {
					
					if (tipoMensagem != 'SUCCESS') {
						
						exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
					} else if(closePopUp) {
						
						$("#produtoEdicaoController-dialog-novo").dialog( "close" );
						
						produtoEdicaoController.pesquisarEdicoes("", "");
						
						exibirMensagem(tipoMensagem, listaMensagens);	
					} else {
						
						exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
						
						produtoEdicaoController.popup("", "", "");
						
						produtoEdicaoController.prepararTela(null, null);
					}
				}
			}, 
			url:  contextPath + '/cadastro/edicao/salvar',
			type: 'POST',
			dataType: 'json',
			data: { codigoProduto : $("#produtoEdicaoController-codigoProduto",this.workspace).val() }
		});
	},
	
	carregarImagemCapa:			function (idProdutoEdicao) {

		var imgPath = (idProdutoEdicao == null || idProdutoEdicao == undefined)
		? "" :  contextPath + '/capa/' + idProdutoEdicao + '?' + Math.random();
		var img = $("<img />").attr('src', imgPath).attr('width', '144').attr('height', '185').attr('alt', 'Capa');
		$("#produtoEdicaoController-div_imagem_capa",this.workspace).empty();
		$("#produtoEdicaoController-div_imagem_capa",this.workspace).append(img);
		
		img.load(function() {
			if (!(!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0)) {
				$("#produtoEdicaoController-div_imagem_capa",this.workspace).empty();
				$("#produtoEdicaoController-div_imagem_capa",this.workspace).append(img);
			}
		});

	},
	
	form_clear:function (formName) {

		$('#produtoEdicaoController-' + formName).find(':input').each(
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
		// $( "#produtoEdicaoController-dialog:ui-dialog" ).dialog( "destroy" );

		$( "#produtoEdicaoController-dialog-novo" ).dialog({
			resizable: false,
			height:615,
			width:960,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ,this.workspace).dialog( "close" );
					$("#produtoEdicaoController-effect",this.workspace).hide("highlight", {}, 1000, callback);
					$( "#abaPdv" ).show( );

				},
				"Cancelar": function() {
					$( this ,this.workspace).dialog( "close" );
				}
			},
			form: $("#produtoEdicaoController-dialog-novo", this.workspace).parents("form")
		});	      
	},
	
	removerEdicao:function (id) {

		$( "#produtoEdicaoController-dialog-excluir").dialog({
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
								$("#produtoEdicaoController-dialog-excluir").dialog("close");

								var tipoMensagem = result.tipoMensagem;
								var listaMensagens = result.listaMensagens;

								if (tipoMensagem && listaMensagens) {

									exibirMensagem(tipoMensagem, listaMensagens);
								}

								produtoEdicaoController.carregarImagemCapa(null);
								$(".edicoesGrid").flexReload();
							},
							function(result) {
								$("#produtoEdicaoController-dialog-excluir").dialog("close");

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
			},
			form: $("#produtoEdicaoController-dialog-excluir", this.workspace).parents("form")
		});
	},
	
	popup_excluir_capa:			function () {
		// $( "#produtoEdicaoController-dialog:ui-dialog" ).dialog( "destroy" );

		$( "#produtoEdicaoController-dialog-excluir-capa" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$.postJSON(
							 contextPath + '/capa/removerCapa',
							{idProdutoEdicao : $("#produtoEdicaoController-idProdutoEdicao",this.workspace).val()},
							function(result) {
								$( "#produtoEdicaoController-dialog-excluir-capa" ).dialog( "close" );

								var mensagens = (result.mensagens) ? result.mensagens : result;
								var tipoMensagem = mensagens.tipoMensagem;
								var listaMensagens = mensagens.listaMensagens;

								if (tipoMensagem && listaMensagens) {
									exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
									if (tipoMensagem == "SUCCESS") { 
										var img = $("<img />").attr('width', '144').attr('height', '185').attr('alt', 'Capa');
										$("#produtoEdicaoController-div_imagem_capa",this.workspace).empty();
										$("#produtoEdicaoController-div_imagem_capa",this.workspace).append(img);
									}
								}
							},
							function(result) {
								$( "#produtoEdicaoController-dialog-excluir-capa" ,this.workspace).dialog( "close" );

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
					$( "#produtoEdicaoController-dialog-excluir-capa" ,this.workspace).dialog( "close" );
				},
				form: $("#produtoEdicaoController-dialog-excluir-capa", this.workspace).parents("form")
			}
		});
	},
	
	mostrar_prod:function (){
		$( "#produtoEdicaoController-pesqProdutos" ,this.workspace).fadeIn('slow');

	},
	
	fecha_prod:function (){
		$( "#pesqProdutos",this.workspace ).fadeOut('slow');

	},
	
	mostraLinhaProd:function (){

		$( ".prodLinhas",this.workspace ).show('slow');
	}

}, BaseController);

//@ sourceURL=scriptProdutoEdicao.js
