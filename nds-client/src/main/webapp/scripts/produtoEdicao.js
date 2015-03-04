var produtoEdicaoController =$.extend(true,  {
	
	linhasDestacadas : [],
	
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
	
	descricaoAtribuida : true,

	pesquisaRealizada : false,

	intervalo : null,

	

	init : function(){
		
		
		$(document).ready(function(){
			
			focusSelectRefField($("#produtoEdicaoController-pCodigoProduto", produtoEdicaoController.workspace));
			
			
			$(document.body).keydown(function(e) {
				
				if(keyEventEnterAux(e)){
					produtoEdicaoController.pesquisarEdicoes();
				}
				
				return true;
			});
			
		});
		
		
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
		$("#produtoEdicaoController-codigoDeBarras",this.workspace).numeric();
		
		

		$("#produtoEdicaoController-pPrecoDe", this.workspace).priceFormat({
			centsSeparator: ',',
		    thousandsSeparator: '.',
		    centsLimit:2
		});
		
		$("#produtoEdicaoController-pPrecoAte", this.workspace).priceFormat({
			centsSeparator: ',',
		    thousandsSeparator: '.',
		    centsLimit:2
		});
		
		$('#produtoEdicaoController-precoPrevisto', this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$('#produtoEdicaoController-precoVenda', this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$('#produtoEdicaoController-pPrecoDe', this.workspace).maskMoney({
					 thousands:'.', 
					 decimal:',', 
					 precision:2
				});
				
		$('#produtoEdicaoController-pPrecoAte', this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});

		$("#produtoEdicaoController-desconto", this.workspace).mask("999,99"); 			
		
		$("#produtoEdicaoController-peso").numeric();
		$("#produtoEdicaoController-largura").numeric();
		$("#produtoEdicaoController-comprimento").numeric();
		$("#produtoEdicaoController-espessura").numeric();
		$("#produtoEdicaoController-numeroLancamento").numeric();
		$("#produtoEdicaoController-peb").numeric();

		$("#produtoEdicaoController-dataLancamentoPrevisto").mask("99/99/9999");
		$("#produtoEdicaoController-dataRecolhimentoPrevisto").mask("99/99/9999");
		$("#produtoEdicaoController-dataRecolhimentoReal").mask("99/99/9999");
		$("#produtoEdicaoController-dataLancamento").mask("99/99/9999");

		$('#produtoEdicaoController-possuiBrinde',this.workspace).change(function(){
			
			if($(this,this.workspace).attr('checked')){
				
				$('.descBrinde',this.workspace).show();
				
				produtoEdicaoController.carregarComboBrindes();
			}else{
				
				$('.descBrinde',this.workspace).hide();
			}
		});
		
		$("#produtoEdicaoController-pNome").autocomplete({
			source:function(param ,callback) {
				$.postJSON(contextPath + "/produto/autoCompletarPorNomeProduto", { 'nomeProduto': param.term }, callback);
			},
			select : function(event, ui) {
				produtoEdicaoController.descricaoAtribuida = true;
				
				$('#produtoEdicaoController-pCodigoProduto',produtoEdicaoController.workspace).val(ui.item.chave.codigo);
				
			},
			minLength: 2,
			delay : 0,
		}).keyup(function(){
			this.value = this.value.toUpperCase();
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
				width : 145,
				sortable : true,
				align : 'left',
			}, {
				display : 'Parcial',
				name : 'parcial',
				width : 40,
				sortable : true,
				align : 'center'
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
		
		$(".produtoEdicaoPeriodosLancamentosGrid", produtoEdicaoController.workspace).flexigrid({
			preProcess: produtoEdicaoController.executarPreProcessamentoLancamentosPeriodo,
			onSuccess: produtoEdicaoController.onSuccessLancamentosPeriodo,
			dataType : 'json',
			colModel : [{
				display : 'Periodo',
				name : 'numeroPeriodo',
				width : 55,
				sortable : false,
				align : 'center'
			},{
				display : 'Nº Lancto',
				name : 'numeroLancamento',
				width : 65,
				sortable : false,
				align : 'center'
			},{ 
				display : 'Lancto Previsto',
				name : 'dataLancamentoPrevista',
				width : 100,
				sortable : false,
				align : 'center'
			},{ 
				display : 'Lancto Real',
				name : 'dataLancamentoDistribuidor',
				width : 80,
				sortable : false,
				align : 'center'
			},{
				display : 'Recolhimento Previsto',
				name : 'dataRecolhimentoPrevista',
				width : 120,
				sortable : false,
				align : 'center'
			},{
				display : 'Recolhimento Real',
				name : 'dataRecolhimentoDistribuidor',
				width : 100,
				sortable : false,
				align : 'center'
			},{
				display : 'Status',
				name : 'status',
				width : 70,
				sortable : false,
				align : 'center'
			},{
				display : 'Reparte',
				name : 'reparte',
				width : 55,
				sortable : false,
				align : 'right'
			}, {
				display : 'Promocional',
				name : 'repartePromocional',
				width : 55,
				sortable : false,
				align : 'right'
			}, {
				display : 'A&ccedil;&atilde;o',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			} ],
			usepager : false,
			useRp : false,
			showTableToggleBtn : false,
			width : 745,
			height : 250
		});
		
		
	},
		
	pesquisarEdicoes : function(codigoProduto, nomeProduto) {
		
		if (codigoProduto == "" || codigoProduto == undefined) {
			codigoProduto = $("#produtoEdicaoController-pCodigoProduto",this.workspace).val();
		}
		
		$("#produtoEdicaoController-codigoProduto",this.workspace).val(codigoProduto);
		
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
			params: [{name:'filtro.codigo', value: codigoProduto },
			         {name:'filtro.nome', value: nomeProduto },
			         {name:'dataLancamentoDe', value: dataLancamentoDe },
			         {name:'dataLancamentoAte', value: dataLancamentoAte },
			         {name:'precoDe', value: precoDe },
			         {name:'precoAte', value: precoAte },
			         {name:'situacaoLancamento', value: situacaoLancamento },
			         {name:'codigoDeBarras', value: codigoDeBarras },
			         {name:'brinde', value : brinde }],
			         newp: 1
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
			$(".grids",produtoEdicaoController.workspace).hide();
		} else {

			var nProduto = '';
			var cProduto = '';
			$.each(resultado.rows, function(index, row) {

				var linkAprovar = '<a href="javascript:;"  onclick="produtoEdicaoController.editarEdicao(' + row.cell.id + ', \'' + row.cell.codigoProduto + '\', \'' + row.cell.statusSituacao + '\');" style="cursor:pointer; margin-right:10px;">' +
				'<img title="Editar" src="' + contextPath + '/images/ico_editar.gif" border="0px" />' +
				'</a>';

				var linkExcluir = '<a href="javascript:;" isEdicao="true" onclick="produtoEdicaoController.removerEdicao(' + row.cell.id + ');" style="cursor:pointer">' +
				'<img title="Excluir" src="' + contextPath + '/images/ico_excluir.gif" border="0px" />' +
				'</a>';

				row.cell.acao = linkAprovar + linkExcluir;

				//
				if(row.cell.nomeComercial){
					nProduto = row.cell.nomeComercial;
				}else{
					row.cell.nomeComercial = '';
				}
				
				//
				if(row.cell.statusLancamento == ''){
					row.cell.statusLancamento = '-';
				}
				
				//
				if(row.cell.statusSituacao == ''){
					row.cell.statusSituacao = '-';
				}
				
				if(row.cell.parcial){
					row.cell.parcial = 'Sim';
				} else {
					row.cell.parcial = 'Não';
				}
				
				cProduto = row.cell.codigoProduto;
			});

			$(".grids",produtoEdicaoController.workspace).show();

			//
			var txt = '';
			
			if($("#produtoEdicaoController-pCodigoProduto", this.workspace).val() == "" && 
					$("#produtoEdicaoController-pNome", this.workspace).val() == "") {
				
				$("#produtoEdicaoController-labelNomeProduto",this.workspace).html("");
			
			} else {
				
				if (nProduto != null || cProduto != null) {
					txt = ": " + cProduto + " - " + nProduto;
				}
				$("#produtoEdicaoController-labelNomeProduto",this.workspace).html(txt);
				
				if (cProduto != "") {
				    $("#produtoEdicaoController-codigoProduto",this.workspace).val(cProduto);
	            }
			}	
		
			return resultado;
		}
	},

	executarPreProcessamentoPesquisados : 	function (resultado) {

		$.each(resultado.rows, function(index, row) {
			
			var isRedistribuicao = produtoEdicaoController.isRedistribuicao(row.cell.modoTela);
			
			if(row.cell.numeroEdicao){
				
				if (isRedistribuicao) {
					
					row.cell.numeroEdicao = '<label style = "height:30px; width:40px;" >'  +
												row.cell.numeroEdicao +
											'</label>';
					
				} else {
					
					row.cell.numeroEdicao = '<a href="javascript:;" onclick="produtoEdicaoController.editarEdicao(' + row.cell.id + ');" style="cursor:pointer; margin-right:10px;">' +
												'<label style = "height:30px; width:40px;" >'  +
													row.cell.numeroEdicao +
												'</label>'  + 
											'</a>';
				}
				
				
			}else{
				
				row.cell.numeroEdicao = '';
			}

			if(row.cell.nomeComercial){
				
				if (isRedistribuicao) {

					row.cell.nomeComercial = '<label style = "height:30px; width:115px;" >'  +						  
											 	row.cell.nomeComercial +
											 '</label>';

					
				} else {
					
					row.cell.nomeComercial = '<a href="javascript:;" onclick="produtoEdicaoController.editarEdicao(' + row.cell.id + ');" style="cursor:pointer; margin-right:10px;">' +
												 '<label style = "height:30px; width:115px;" >'  +						  
												  	row.cell.nomeComercial +
												 '</label>'  + 
											 '</a>';
				}
				 
			}else{
				
				row.cell.nomeComercial = '';
			}
		});

		$(".grids",produtoEdicaoController.workspace).show();

		return resultado;
	},
	
	novaEdicao : function () {
		produtoEdicaoController.popup(null, null, false);
	},
	
	editarEdicao:function (id, codigo, situacaoProdutoEdicao) {
		
		if (id == undefined) {
			id = "";
		}
		if (codigo == undefined) {
			codigo = "";
		}
		
		this.edicao = {id:id,codigo:codigo,situacaoProdutoEdicao:situacaoProdutoEdicao};

		produtoEdicaoController.popup(id, codigo, false, situacaoProdutoEdicao);
	},

	prepararTela : function (id, codigo, modoTela) {

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
		
		//Produtos Pesquisados
		if (codigo == "") {
			codigo = $("#produtoEdicaoController-codigoProduto",this.workspace).val();
		}
		var nomeProduto = $("#produtoEdicaoController-pNome",this.workspace).val();
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

		$(".prodsPesqGrid",this.workspace).flexOptions({
			url: contextPath + "/cadastro/edicao/pesquisarEdicoes.json",
			params: [{name:'filtro.codigo', value: codigo },
			         {name:'filtro.nome', value: nomeProduto },
			         {name:'dataLancamentoDe', value: dataLancamentoDe },
			         {name:'dataLancamentoAte', value: dataLancamentoAte },
			         {name:'precoDe', value: precoDe },
			         {name:'precoAte', value: precoAte },
			         {name:'situacaoLancamento', value: situacaoLancamento },
			         {name:'codigoDeBarras', value: codigoDeBarras },
			         {name:'brinde', value : brinde },
			         {name:'modoTela', value : modoTela }],
			         newp: 1, 
			         rp: 99999
		});

		$(".prodsPesqGrid",this.workspace).flexReload();	
	},
	
	carregarDialog : function (id, codigoProduto, redistribuicao, situacaoProdutoEdicao) {
		
		if (codigoProduto == "") {
			codigoProduto = $("#produtoEdicaoController-codigoProduto",this.workspace).val();
		}
		
		// Exibir os dados do Produto:
		$.postJSON(
				 contextPath + '/cadastro/edicao/carregarDadosProdutoEdicao.json',
				{ 'filtro.codigo': codigoProduto, idProdutoEdicao : id, situacaoProdutoEdicao : situacaoProdutoEdicao, redistribuicao : redistribuicao},

					function(result) {
						
						//Default:
						$("a[name='linkRedistribuicao']", this.workspace).show();
						$("a[name='linkIncluirNovo']", this.workspace).show();
						$("#idLinkMostrarPeriodos", this.workspace).show();
						$("#linkExclusaoCapa", this.workspace).show();
						$("#produtoEdicaoController-imagemCapa").enable();
						
						var title;
						
						var modoTela = result.modoTela;
						
						if (redistribuicao) {
							
							title = "Incluir Nova Redistribuição";
							
							$("a[name='linkRedistribuicao']", this.workspace).hide();
							$("a[name='linkIncluirNovo']", this.workspace).hide();
							$("#idLinkMostrarPeriodos", this.workspace).hide();
							$("#linkExclusaoCapa", this.workspace).hide();
							$("#produtoEdicaoController-imagemCapa").disable();
							
						} else if (produtoEdicaoController.isEdicao(modoTela)) {
							
							title = "Editar Edição";
							
						} else {
							
							title = "Incluir Nova Edição";
							
							$("a[name='linkRedistribuicao']", this.workspace).hide();
						}
						
						$( "#produtoEdicaoController-dialog-novo" ).dialog({
							resizable: false,
							height:540,
							width:960,
							modal: true,
							title: title,
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
						
						produtoEdicaoController.iniciaTab();
						
						produtoEdicaoController.prepararTela(id, codigoProduto, modoTela);
						
						if (result) {
							
							$("#produtoEdicaoController-origemInterface", this.workspace).val(result.origemInterface);
							$("#produtoEdicaoController-modoTela", this.workspace).val(modoTela);
							$("#produtoEdicaoController-idProdutoEdicao").val(result.id);
							$("#produtoEdicaoController-codigoProdutoEdicao").val(result.codigoProduto);
							$("#produtoEdicaoController-nomePublicacao").val(result.nomeProduto);
							$("#produtoEdicaoController-comboClassificacao").val(result.classificacao);
							$("#produtoEdicaoController-nomeComercialProduto").val(result.nomeComercialProduto);
							$("#produtoEdicaoController-nomeFornecedor").val(result.nomeFornecedor);
							$("#produtoEdicaoController-situacao").val(result.statusSituacao);
							$("#produtoEdicaoController-numeroEdicao").val(result.numeroEdicao);
							$("#produtoEdicaoController-fase").val(result.fase);
							$("#produtoEdicaoController-numeroLancamento").val(result.numeroLancamento);
							$("#produtoEdicaoController-pacotePadrao").val(result.pacotePadrao);
							$("#produtoEdicaoController-tipoLancamento").val(result.tipoLancamento);	
							$("#produtoEdicaoController-precoPrevisto").val(result.precoPrevisto).formatNumber({format:'#.00', locale:'br'});;
							$("#produtoEdicaoController-precoVenda").val(result.precoVenda).formatNumber({format:'#.00', locale:'br'});;
							
							$("#produtoEdicaoController-dataLancamentoPrevisto").val(result.dataLancamentoPrevisto == undefined ? '' : result.dataLancamentoPrevisto.$);
							$("#produtoEdicaoController-dataLancamento").val(result.dataLancamento == undefined ? '' : result.dataLancamento.$);
							$("#produtoEdicaoController-repartePrevisto").val(result.repartePrevisto);
							$("#produtoEdicaoController-expectativaVenda").val(result.expectativaVenda);
							$("#produtoEdicaoController-repartePromocional").val(result.repartePromocional);
						    $("#produtoEdicaoController-categoria").val(result.grupoProduto);
							$("#produtoEdicaoController-codigoDeBarras").val(result.codigoDeBarras);
							$("#produtoEdicaoController-codigoDeBarrasCorporativo").val(result.codigoDeBarrasCorporativo);
							$("#produtoEdicaoController-desconto", this.workspace).val($.formatNumber(result.desconto, {format:"###,##000.00", locale:"br"}));
							$("#produtoEdicaoController-descricaoDesconto").val(result.descricaoDesconto);
							$("#produtoEdicaoController-peso").val(result.peso);
							$("#produtoEdicaoController-largura").val(result.largura);
							$("#produtoEdicaoController-comprimento").val(result.comprimento);
							$("#produtoEdicaoController-espessura").val(result.espessura);
							$("#produtoEdicaoController-chamadaCapa").val(result.chamadaCapa);
							$('#produtoEdicaoController-parcial').val(result.parcial + "");
							$('#produtoEdicaoController-boletimInformativo').val(result.boletimInformativo);
//							$('#produtoEdicaoController-semanaRecolhimento').val(result.semanaRecolhimento);
							if (result.dataRecolhimentoReal || result.dataRecolhimentoPrevisto) {
								ano = (result.dataRecolhimentoReal || result.dataRecolhimentoPrevisto).$.split("/")[2];
								anoSemanaRecolhimento = ano + result.semanaRecolhimento;
								$('#produtoEdicaoController-semanaRecolhimento').val(anoSemanaRecolhimento);
							}
							$('#produtoEdicaoController-dataRecolhimentoReal').val(result.dataRecolhimentoReal == undefined ? '' : result.dataRecolhimentoReal.$);
							$('#produtoEdicaoController-dataRecolhimentoDistribuidor').val(result.dataRecolhimentoDistribuidor == undefined ? '' : result.dataRecolhimentoDistribuidor.$);
							$('#produtoEdicaoController-dataRecolhimentoPrevisto').val(result.dataRecolhimentoPrevisto == undefined ? '' : result.dataRecolhimentoPrevisto.$);
							$("#produtoEdicaoController-peb").val(result.peb);		
							$("#produtoEdicaoController-descricaoProduto").val(result.caracteristicaProduto);
							
							if (result.possuiBrinde){
							
							    produtoEdicaoController.carregarComboBrindes(result.idBrinde);
							}
							
							$('#produtoEdicaoController-possuiBrinde').attr('checked', result.possuiBrinde).change();

							//Segmentação
							$("#produtoEdicaoController-tipoSegmento").val(result.tipoSegmentoProdutoId);

							//Desativar Segmentação
							$("#produtoEdicaoController-tipoSegmento").attr("disabled", true);
							
							if(!result.dataLancamento || result.dataLancamento.$ == '01/01/3000'){
								 $("#produtoEdicaoController-dataLancamento").val("");
								 $("#produtoEdicaoController-dataLancamento").removeAttr("disabled");
								 $("#produtoEdicaoController-istrac29").val(true);
								 $("#produtoEdicaoController-lancamentoExcluido", this.workspace).val(true);
							} else {
								
								$("#produtoEdicaoController-lancamentoExcluido", this.workspace).val(false);
							}
							
							
							if (result.origemInterface) {
								
								if(result.classificacao && result.classificacao.length > 0 ){

									$("#produtoEdicaoController-comboClassificacao option").not(":selected").attr("disabled", true);
									
								}
								else{
									
									$("#produtoEdicaoController-comboClassificacao option").not(":selected").removeAttr("disabled");
								}
								
							}else{
								
								$("#produtoEdicaoController-comboClassificacao option").not(":selected").removeAttr("disabled");
							}
							

							if (redistribuicao) {
								
								produtoEdicaoController.definirBackgroundInputs(
									false, result.numeroEdicao, true);
								
							} else {
								
								produtoEdicaoController.definirBackgroundInputs(
									result.origemInterface, result.numeroEdicao, false);	
							}
							
							$("#produtoEdicaoController-tipoLancamento option").not(":selected").attr("disabled", true);
							
							if(result.id){
								
								produtoEdicaoController.carregarLancamentosPeriodo(result.id);
							}
							
							$("#produtoEdicaoController-idFornecedor").val(result.idFornecedor);
							
							$("#produtoEdicaoController-formaComercializacao", this.workspace).val(result.formaComercializacao);
							
							var isProdutoContaFirme = (result.formaComercializacao === "CONTA_FIRME");
							
							if(isProdutoContaFirme){
								$("#fildSet-data-recolhimento", this.workspace).hide();
							}
							else{
								$("#fildSet-data-recolhimento", this.workspace).show();
							}
						}
					},
					null,
					true
		);

	},
	
	definirBackgroundInputs : function(bloquearOrigemInterface, numeroEdicao, bloquearRedistribuicao) {		
		
		var bloquear = (bloquearOrigemInterface || bloquearRedistribuicao);
		
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-dataLancamentoPrevisto", bloquearOrigemInterface);
//		produtoEdicaoController.bloquearCampo("produtoEdicaoController-repartePrevisto", bloquearOrigemInterface);
//		produtoEdicaoController.bloquearCampo("produtoEdicaoController-repartePromocional", bloquearOrigemInterface);
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-dataLancamento", !bloquearOrigemInterface);
		
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-numeroEdicao", bloquear);
		
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-dataRecolhimentoPrevisto", bloquear);
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-dataRecolhimentoReal", bloquear);
//		produtoEdicaoController.bloquearCampo("produtoEdicaoController-peb", bloquear);
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-descricaoProduto", bloquear);
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-codigoProdutoEdicao", bloquear);
//		produtoEdicaoController.bloquearCampo("produtoEdicaoController-nomeComercialProduto", bloquear);
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-precoPrevisto", bloquear);
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-codigoDeBarrasCorporativo", bloquear);
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-desconto", bloquear);
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-largura", bloquear);
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-comprimento", bloquear);
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-espessura", bloquear);
//		produtoEdicaoController.bloquearCampo("produtoEdicaoController-boletimInformativo", bloquear);
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-descricaoDesconto", bloquear);

		produtoEdicaoController.bloquearCampo("produtoEdicaoController-pacotePadrao", bloquearRedistribuicao);
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-precoVenda", bloquearRedistribuicao);
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-codigoDeBarras", bloquearRedistribuicao);
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-chamadaCapa", bloquearRedistribuicao);
		produtoEdicaoController.bloquearCampo("produtoEdicaoController-peso", bloquearRedistribuicao);
		
//		$("#produtoEdicaoController-parcial option").not(":selected").attr("disabled", bloquear);
		$("#produtoEdicaoController-categoria option").not(":selected").attr("disabled", bloquear);
		
		//Segmentação
	    $("#produtoEdicaoController-tipoSegmento option").not(":selected").attr("disabled", bloquear);
	},
	
	bloquearCampo : function(campo, bloquear) {
		
		var color = bloquear ? "buttonface" : "white";
		
		$("#" + campo).css("background-color", color);
		$("#" + campo).attr("readonly", bloquear);
	},
	
	carregarLancamentosPeriodo : function (produtoEdicaoId) {

		$(".produtoEdicaoPeriodosLancamentosGrid", produtoEdicaoController.workspace).flexOptions({
			url: contextPath + "/cadastro/edicao/carregarLancamentosPeriodo",
			params: [{name: "produtoEdicaoId", value: produtoEdicaoId}]
		});
		
		$(".produtoEdicaoPeriodosLancamentosGrid", produtoEdicaoController.workspace).flexReload();
		
	},
	
	executarPreProcessamentoLancamentosPeriodo : function (result) {
		
		linhasDestacadas = [];
		
		if (result.rows && result.rows.length > 1) {

			var bloquearCamposReparte = false;
			$.each(result.rows, function(k, v){
				if(v.cell.numeroLancamento == $('#produtoEdicaoController-numeroLancamento').val()) {
					bloquearCamposReparte = true;
				}
			});
			
			/*
			if(bloquearCamposReparte) {
				$("#produtoEdicaoController-repartePrevisto", produtoEdicaoController.workspace).attr("disabled", "disabled");
				$("#produtoEdicaoController-repartePromocional", produtoEdicaoController.workspace).attr("disabled", "disabled");
			} else {
				$("#produtoEdicaoController-repartePrevisto", produtoEdicaoController.workspace).removeAttr("disabled");
				$("#produtoEdicaoController-repartePromocional", produtoEdicaoController.workspace).removeAttr("disabled");
			}
			*/
		}
		
		$.each(result.rows, function(index, row) {
			
			if(row.cell.numeroLancamento == undefined) {
				row.cell.numeroLancamento = "";
			}
			
			if(row.cell.numeroPeriodo == undefined) {
				row.cell.numeroPeriodo = " - ";
			}
			
			var reparte = row.cell.reparte;
			var repartePromocional = row.cell.repartePromocional;

			if(reparte == undefined) {
				reparte = 0;
			}

			if(repartePromocional == undefined) {
				repartePromocional = 0;
			}
			
			row.cell.reparte = "<input type='text' value='" + reparte  + "'"
							 + " name='reparteLancamento' "
							 + " style='width: 45px' "
							 + "' onchange='produtoEdicaoController.atualizarLancamento(" 
							 + "{ idLancamento: " + row.cell.idLancamento + ", reparte: $(this).val() }"
							 + ")' />";
			
			row.cell.repartePromocional = "<input type='text' value='" + repartePromocional  + "'"
									    + " name='reparteLancamento' "
									    + " style='width: 45px' "
									    + "' onchange='produtoEdicaoController.atualizarLancamento(" 
									    + "{ idLancamento: " + row.cell.idLancamento + ", repartePromocional: $(this).val() }"
									    + ")' />";
			
			if (row.cell.destacarLinha) {
				linhasDestacadas.push(index + 1);
			}
			
			var acao ='</a> <a href="javascript:;" isEdicao="true" onclick="produtoEdicaoController.removerLancamento(' + row.cell.idLancamento + ');""><img src="' + contextPath + '/images/ico_excluir.gif" border="0" /></a>';

			row.cell.acao = acao;	
		});
		
		return result;
	},
	
	atualizarLancamento: function(lancamento) {
		
		if (!lancamento) {
			return;
		}

		$.postJSON(
			contextPath + '/cadastro/edicao/atualizarReparteLancamento',
			lancamento
		);
	},
	
	onSuccessLancamentosPeriodo : function() {
		
		$(linhasDestacadas).each(function(i, item){
			 id = '#row' + item;			    	
			 $(id, this.workspace).removeClass("erow").addClass("gridLinhaDestacada");
			 $(id, this.workspace).children("td").removeClass("sorted");
		});
		
		$("input[name='reparteLancamento']").numeric();
	},
	
	iniciaTab: function(){
		
		$("#produtoEdicaoController-tabIdentificacao",this.workspace).click();
	},
	
	popup:function (id, codigo, redistribuicao, situacaoProdutoEdicao) {

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
		
		produtoEdicaoController.carregarDialog(id, codigo, redistribuicao, situacaoProdutoEdicao);
	},
	
	popupRedistribuicao : function () {
		
		var id = $("#produtoEdicaoController-idProdutoEdicao").val();
		
		var codigo = $("#produtoEdicaoController-codigoProdutoEdicao").val();
		
		var situacaoProdutoEdicao = $("#produtoEdicaoController-situacao", produtoEdicaoController.workspace).val();
		
		produtoEdicaoController.popup(id, codigo, true, situacaoProdutoEdicao);
	},
	
	mostrarPeriodosLancamento : function() {

		$( "#dialog-produto-edicao-periodos-lancamentos" ).dialog({
			resizable: true,
			width:765,
			height:380,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( "#dialog-produto-edicao-periodos-lancamentos" ).dialog("close");
				}
			},
			form: $("#produtoEdicaoController-dialog-novo", this.workspace).parents("form")
		});
		
	},
	
	salvarProdutoEdicao : function(closePopUp) {

		previsto = $('#produtoEdicaoController-precoPrevisto', this.workspace).val();
		real = $('#produtoEdicaoController-precoVenda', this.workspace).val();
		
		if(previsto != real) {
			$("#produtoEdicaoController-dialog-precos-real-previsto-divergentes").dialog({
				resizable: false,
				height:140,
				modal: true,
				buttons: {
					"Sim": function() {

						produtoEdicaoController.submeterProdutoEdicao(closePopUp);

						$( this ).dialog( "close" );
					},
					"Não": function() {
						setTimeout(function() {
							$("#produtoEdicaoController-precoVenda", this.workspace).focus(); }, 10);
						
						setTimeout(function() {
							$("#produtoEdicaoController-precoVenda", this.workspace).select(); }, 10);

						$( this ).dialog( "close" );
					}
				}
			});
		} else {
		
			produtoEdicaoController.submeterProdutoEdicao(closePopUp);
			
		}
	},
	
	submeterProdutoEdicao : function(closePopUp) {
		
		var camposValidaveisBloqueados = $('#produtoEdicaoController-formUpload').find(':input:disabled:.produtoEdicaoCampoValidavel');
		
		$(camposValidaveisBloqueados).removeAttr('disabled');
		
		$("#produtoEdicaoController-formUpload").ajaxSubmit({
			beforeSubmit: function(arr, formData, options) {
				// Incluir aqui as validacoes;
			},
			
			error: function() {
				
				$(camposValidaveisBloqueados).attr('disabled','disabled');
				
			},
			
			success: function(responseText, statusText, xhr, $form)  {
				
				$(camposValidaveisBloqueados).attr('disabled','disabled');
				
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
						
						produtoEdicaoController.popup("", "", false);
						
						var codigoProduto = $("#produtoEdicaoController-codigoProduto",this.workspace).val();
						
						produtoEdicaoController.prepararTela(null, codigoProduto);
					}
				}
			}, 
			url:  contextPath + '/cadastro/edicao/salvar',
			type: 'POST',
			dataType: 'json',
			data: { codigoProduto : $("#produtoEdicaoController-codigoProduto",this.workspace).val()}
		});
	},
	
	carregarImagemCapa:			function (idProdutoEdicao) {
		var self = this;
		$("#produtoEdicaoController-div_imagem_capa",this.workspace).empty();
		$('#linkExclusaoCapa', this.workspace).unbind('click');
		
		if((idProdutoEdicao == null || idProdutoEdicao == undefined)){
			this.semImagemCapa();
			return;
		}
		var imgPath = contextPath + '/capa/'+ idProdutoEdicao + '?' + Math.random();
		
		var img = $("<img />").attr('src', imgPath).attr('width', '144').attr('height', '185').attr('alt', 'Capa');
		
		img.load(function() {
			$("#produtoEdicaoController-div_imagem_capa",self.workspace).append(img);
			$('#linkExclusaoCapa', this.workspace).show();
			$('#linkExclusaoCapa', this.workspace).bind('click',function() {
				produtoEdicaoController.popup_excluir_capa();
			});
			
		}).error(function() {
			self.semImagemCapa();
			
		});

	},
	semImagemCapa :function(){
		var imgSemCapa = $("<img />").attr('src', contextPath + "/capa/getNoImage").attr('width', '144').attr('height', '185').attr('alt', 'Capa');
        
		$("#produtoEdicaoController-div_imagem_capa",this.workspace).append(imgSemCapa);
		$('#linkExclusaoCapa', this.workspace).hide();
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
		$("#produtoEdicaoController-comboClassificacao").val('');
	},
	
	carregarCapaTemporaria : function() {
		var self = this;
		$("#linkExclusaoCapa", produtoEdicaoController.workspace).unbind();
		$("#produtoEdicaoController-formUpload").ajaxSubmit({ 
			
			success: function(responseText, statusText, xhr, $form)  { 
				var mensagens = (responseText.mensagens) ? responseText.mensagens : responseText.result;   
				var tipoMensagem = mensagens.tipoMensagem;
				var listaMensagens = mensagens.listaMensagens;
				
				if (tipoMensagem && listaMensagens) {
					exibirMensagem(tipoMensagem, listaMensagens);	
				}
				if (tipoMensagem == "WARNING" || tipoMensagem == "ERROR") {
					$("#produtoEdicaoController-imagemCapa").val("");
					return;
				}
				
				
				var img = $("<img />").attr('src', contextPath + responseText.result).attr('width', '144').attr('height', '185').attr('alt', 'Capa');
				$("#produtoEdicaoController-div_imagem_capa",self.workspace).empty();
				$("#produtoEdicaoController-div_imagem_capa",self.workspace).append(img);
				$("#linkExclusaoCapa", self.workspace).show();
				
				$("#linkExclusaoCapa", self.workspace).bind('click',function() {
					self.carregarImagemCapa(null);
					$("#produtoEdicaoController-imagemCapa").val('');
				});
				
			},
			
			url:  contextPath + '/capa/carregarCapaTemp',
			type: 'POST',
			dataType: 'json',
			data : $("#produtoEdicaoController-imagemCapa").val()
		});
		
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
										produtoEdicaoController.carregarImagemCapa(null);
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
				}
			},
			form: $("#produtoEdicaoController-dialog-excluir-capa", this.workspace).parents("form")
			
		});
	},
	
	removerEdicao: function (id) {

		$.postJSON(
				 contextPath + '/cadastro/edicao/validarRemocaoEdicao.json',
					{idProdutoEdicao : id},
					function(result) {

						$("#produtoEdicaoController-dialog-excluir").dialog("close");
						
						
						if(result.map.length > 0) {
							
							var tipoMensagem = 'WARNING';
							var listaMensagens = [];
																			
							$.each(result.map, function(index, value){
								listaMensagens.push(value[1]);
							});
														
							if (tipoMensagem && listaMensagens.length>0) {

								exibirMensagem(tipoMensagem, listaMensagens);
							}
							
							return;
						}
						
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
											null,
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
					null,
					true
			);
		
	},
	
	mostrar_prod:function (){
		$( "#produtoEdicaoController-pesqProdutos" ,this.workspace).fadeIn('slow');

	},
	
	fecha_prod:function (){
		$( "#pesqProdutos",this.workspace ).fadeOut('slow');

	},
	
	mostraLinhaProd:function (){

		$( ".prodLinhas",this.workspace ).show('slow');
	},
	
	novaRedistribuicao : function() {
	
		$( "#dialog-redistribuicao-lancamento" ).dialog({
			resizable: true,
			width:720,
			height:380,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( "#dialog-redistribuicao-lancamento" ).dialog("close");
				}
			},
			form: $("#produtoEdicaoController-dialog-novo", this.workspace).parents("form")
		});
	},
	
	isRedistribuicao : function(modoTela) {
		
		return modoTela == 'REDISTRIBUICAO';
	},
	
	isEdicao : function(modoTela) {
		
		return modoTela == 'EDICAO';
	},

// FUNCTION - ADD EM LOTE
	
	edicaoLote : function() {

		
		$("#dialog-lote").dialog({
			resizable : false,
			height : 250,
			width : 350,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$(this).dialog("close");
					
					produtoEdicaoController.executarSubmitAddLote();
					
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			},
			form: $("#dialog-lote", this.workspace).parents("form")
		});

	},
	
	executarSubmitAddLote : function (){
		 var fileName = $("#xls").val();
	      
	       var ext = fileName.substr(fileName.lastIndexOf(".")+1).toLowerCase();
	       if(ext!="xls" & ext!="xlsx"){
	    	   exibirMensagem("WARNING", ["Somente arquivos com extensão .XLS ou .XLSX são permitidos."]);
	    	   $(this).val('');
	    	   return;
	       }else{
	    	
	    	   $("#arquivoUpLoadEdicao").ajaxSubmit({
					beforeSubmit: function(arr, formData, options) {
					},
					success: function(responseText, statusText, xhr, $form)  { 
						var mensagens = (responseText.mensagens) ? responseText.mensagens : responseText.result;   
						var tipoMensagem = mensagens.tipoMensagem;
						var listaMensagens = mensagens.listaMensagens;

						if (tipoMensagem && listaMensagens) {
							
							if (tipoMensagem != 'SUCCESS') {
								
								exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
							
							}else{
								exibirMensagem(tipoMensagem, listaMensagens);	
							}

							$("#dialog-lote").dialog( "close" );
							
						}
					}, 
					url:  contextPath + '/cadastro/edicao/addLote',
					type: 'POST',
					dataType: 'json'
				});
	       }
	},
	removerLancamento : function(idLancamento){
		$( "#produtoEdicaoController-dialog-excluir-lancamento").dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					var self =  this;
					$(self).dialog("close");
					$.postJSON(
							 contextPath + '/cadastro/edicao/removerLancamento.json',
							{idLancamento : idLancamento},
							function(result) {
								

								var tipoMensagem = result.tipoMensagem;
								var listaMensagens = result.listaMensagens;

								if (tipoMensagem && listaMensagens) {

									exibirMensagem(tipoMensagem, listaMensagens);
								}
									produtoEdicaoController.popup(produtoEdicaoController.edicao.id, produtoEdicaoController.edicao.codigo, false, produtoEdicaoController.edicao.situacaoProdutoEdicao);
									produtoEdicaoController.pesquisarEdicoes();
							},
							null,
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
			form: $("#produtoEdicaoController-dialog-excluir-lancamento", this.workspace).parents("form")
		});
	},
	
	carregarComboBrindes : function (idBrinde){
		
		$('#produtoEdicaoController-selectBrinde > option', this.workspace).remove();
		
	    $('#produtoEdicaoController-selectBrinde', this.workspace).append('<option value="" >Selecione...</option>');
        
        $.postJSON(contextPath + '/cadastro/edicao/obterBrindes.json', null,
            function(result) {

				var tipoMensagem = result.tipoMensagem;
				
				var listaMensagens = result.listaMensagens;
		          
		        if (tipoMensagem && listaMensagens) {
		        	
		        	exibirMensagem(tipoMensagem, listaMensagens);
		        	
		        	return;
		        } 
		        
		        var sel='';
		        
                $.each(result, function(index, row){
                		 
                	sel=(idBrinde == row.key.$)?' selected ':' ';
                	
                    $('#produtoEdicaoController-selectBrinde', this.workspace).append('<option '+sel+' value="'+row.key.$+'">'+row.value.$+'</option>');
                });
            },
            null,
            true
        );
    }
	
}, BaseController);

//@ sourceURL=scriptProdutoEdicao.js
