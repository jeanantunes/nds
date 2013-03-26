
var alteracaoCotaController = $.extend(true, {
	
	pesquisaCotaAlteracaoCota : null,

	carregarBairros : function(cidade) {
		
		$("#idBairro option", alteracaoCotaController.workspace).remove();
		
		$.postJSON(contextPath + '/administracao/alteracaoCota/buscarBairroPorCidade.json', {
			'cidade' : cidade
		}, function(data) {
			$("#idBairro", alteracaoCotaController.workspace).append($("<option/>", {value: "-1", text: ""}));
			$(data).each(function() {
				$("#idBairro", alteracaoCotaController.workspace).append($("<option/>", {value: $(this)[0].toString(),
															 			   text: $(this)[0].toString() 
															 			  }));
			});
		});

	},
	
	carregarValorMinimo : function() {
		
		$("#idVrMinimo option", alteracaoCotaController.workspace).remove();
		
		$.postJSON(contextPath + '/administracao/alteracaoCota/buscarValorMinimo',null, 
				function(data) {
					$("#idVrMinimo", alteracaoCotaController.workspace).append($("<option/>", {value: "-1", text: ""}));
					$(data).each(function() {
						$("#idVrMinimo", alteracaoCotaController.workspace).append($("<option/>", {value: $(this)[0].toString(),
																	 			   text: $(this)[0].toString() 
																	 			  }));
					});
				});

	},

	
	init : function(pesquisaCota) {
		this.pesquisaCotaAlteracaoCota = pesquisaCota;
		
		this.iniciarGrid();
		
		this.formatarCampos();
		
		$("#idQtdDividaEmAbertoModal", this.workspace).numeric();
		$("#percentualFaturamentoEntregaBanca", this.workspace).mask("99.99");
		$("#taxaFixaEntregaBanca", this.workspace).numeric();
		$("#carenciaInicioEntregaBanca", this.workspace).mask("99/99/9999");
		$("#carenciaFimEntregaBanca", this.workspace).mask("99/99/9999");
		
		$("#percentualFaturamentoEntregador", this.workspace).mask("99.99");
		$("#carenciaInicioEntregador", this.workspace).mask("99/99/9999");
		$("#carenciaFimEntregador", this.workspace).mask("99/99/9999");
		
		
		$("#carenciaInicioEntregaBanca", this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$("#carenciaFimEntregaBanca", this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		
		$("#carenciaInicioEntregador", this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$("#carenciaFimEntregador", this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		
	
    	$('#uploadedFileProcuracao').fileupload(
						{
							url :"administracao/alteracaoCota/uploadProcuracao",
							sequentialUploads: false,
							dataType : 'json',
							paramName : 'uploadedFileProcuracao',
							replaceFileInput: false,
							submit : function(e, data) {
								data = $("#pesquisarForm", this.workspace).serialize();
							},
							success : function(e, data) {
								$("#nomeArquivoProcuracao").html(e.result);
							}
						});
    	
    	
    	$('#uploadedFileTermo').fileupload(
			{
				url :"administracao/alteracaoCota/uploadTermoAdesao",
				sequentialUploads: false,
				dataType : 'json',
				paramName : 'uploadedFileTermo',
				replaceFileInput: false,
				submit : function(e, data) {
					data = $("#pesquisarForm", this.workspace).serialize();
	
				},
				success : function(e, data) {
					$("#nomeArquivoTermoAdesao").html(e.result);
			}
					 
		});
    	
    	$(document).ready(function() {
    		
    		
    		showCamposSuspensao($("#idIsSugereSuspensaoModal").attr("checked") == "checked");
    		
    		$("#idIsSugereSuspensaoModal").click(function() {
    			showCamposSuspensao($("#idIsSugereSuspensaoModal").attr("checked") == "checked");
    		});
    		
			focusSelectRefField($("#numeroCota", this.workspace));
			
			$(document.body).keydown(function(e) {
				
				if(keyEventEnterAux(e)){
					alteracaoCotaController.pesquisar();
				}
				
				return true;
			});
    	});			
	},
	
	formatarCampos: function() {
	
		$("#idVrMinimoModal", this.workspace).priceFormat({
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
		$("#idVrDividaEmAbertoModal", this.workspace).priceFormat({
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
		$("#taxaFixaEntregaBanca", this.workspace).priceFormat({
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
		$("#taxaFixaEntregador", this.workspace).priceFormat({
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
	},
	
	iniciarGrid : function() {
		
	
		$(".alteracaoGrid", this.workspace).flexigrid({			
			dataType : 'json',
			preProcess: alteracaoCotaController.executarPreProcessamento,
			colModel : [ {
				display : 'C&oacute;digo',
				name : 'numeroCota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome/Raz&atilde;o Social',
				name : 'nomeRazaoSocial',
				width : 170,
				sortable : true,
				align : 'left'
			}, {
				display : 'Fornecedor',
				name : 'nomeFornecedor',
				width : 93,
				sortable : true,
				align : 'left'
			}, {
				display : 'Desconto',
				name : 'tipoDesconto',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Vencimento',
				name : 'vencimento',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor M&iacute;nimo R$',
				name : 'valorMinimo',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Tipo Entrega',
				name : 'tipoEntrega',
				width : 65,
				sortable : true,
				align : 'center'
			}, {
				display : 'Box',
				name : 'box',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'A&ccedil;&atilde;o',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			}],
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960
		});
		$(".alteracaoGrid", this.workspace).flexOptions({
			url: contextPath + "/administracao/alteracaoCota/pesquisarAlteracaoCota.json",
			params: [],
			newp: 1
		});
	},
	
	pesquisar : function() {
		alteracaoCotaController.verificarCheck();
		$("#totalCotasSelecionadas", this.workspace).html(0);
		$("#alteracaoCotaCheckAll", this.workspace).attr("checked",false);
		
		var params = $("#pesquisarForm :input[value][value!=''][value!='-1']", this.workspace).serializeArray();
		
				
		$(".alteracaoGrid", this.workspace).flexOptions({
			params: params,
			newp: 1
		});
	
		
		$(".alteracaoGrid", this.workspace).flexReload();
		$(".grids", this.workspace).show();
				

	},
	
	executarPreProcessamento : function(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			return resultado;
		}

		$.each(resultado.rows, function(index, row) {
			
			var campoSelect = "<input name='filtroAlteracaoCotaDTO.listaLinhaSelecao["+ index +"]' class='selectLine' type='checkbox' value='"+row.cell.idCota+"' onclick='alteracaoCotaController.verificarCheck();'>";
			
			row.cell.acao = campoSelect;
			
			row.cell.vencimento = row.cell.vencimento == null || row.cell.vencimento == -1 ? '' : row.cell.vencimento;
			row.cell.nomeFornecedor = (row.cell.nomeFornecedor)?row.cell.nomeFornecedor:"";
			row.cell.tipoDesconto = (row.cell.tipoDesconto)?row.cell.tipoDesconto:"";
			row.cell.valorMinimo = (row.cell.valorMinimo)?row.cell.valorMinimo:"";
			row.cell.tipoEntrega = (row.cell.tipoEntrega)?row.cell.tipoEntrega:"";
			row.cell.box = (row.cell.box)?row.cell.box:"";
		});
		
		return resultado;
	},	
	
	callBackSuccess:function () {
		
		pesquisaCotaAlteracaoCota.pesquisarPorNumeroCota($("#numeroCota", "#nomeCota",alteracaoCotaController.workspace).val(), false, function(result) {

			if (!result) {

				return;
			}

		});
	},
	
	callBackErro:function(){
		
	
	},
	
	carregarAlteracao : function() {
		var linhasSelecionadas = 0;
		
		this.listListaLinhaSelecao = new Array();
		
		var listListaLinhaSelecao = this.listListaLinhaSelecao;
		
		$(".selectLine", this.workspace).each(function(index, element) {	
			if(element.checked){
				element.name = 'filtroAlteracaoCotaDTO.listaLinhaSelecao['+ linhasSelecionadas +']';
				linhasSelecionadas++;
				listListaLinhaSelecao.push({name:element.name, value:element.value});
			}
			
		});
		
		if(linhasSelecionadas > 0){
			$("#dialog-novo", this.workspace).dialog({
				resizable: false,
				height:550,
				width:900,
				modal: true,
				buttons: {
					"Confirmar": function() {
						alteracaoCotaController.salvarAlteracao();
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				
				form: $("#dialog-novo", this.workspace).parents("form")
			});
			this.carregarCamposAlteracao();
		}else{
			exibirMensagem("WARNING", ["Selecione ao menos uma cota."]);
		}
	},
	
	carregarCamposAlteracao : function() {
		
		alteracaoCotaController.limparCamposAbas();
		
		$('#tabs',this.workspace ).tabs('select', '#tabs-1');
		
		var params = $("#gridForm", this.workspace).serializeArray();
		
		

		/*$.postJSON(contextPath + "/administracao/alteracaoCota/carregarCamposAlteracao",
				null,
				function(result) {
					
				},
				null,
				true
		);*/
		
		$.postJSON(contextPath + "/administracao/alteracaoCota/carregarCamposAlteracao",
				params,
				function (result) {
					var filtro = result["filtroAlteracaoCotaDTO"];
					alteracaoCotaController.popularComboFornecedor(filtro.filtroModalFornecedor.listFornecedores, $("#idListFornecedores", this.workspace));
					alteracaoCotaController.popularComboFornecedor(filtro.filtroModalFornecedor.listaFornecedorAssociado, $("#idListaFornecedorAssociado", this.workspace));

					if(filtro.listaLinhaSelecao.length == 1){
						//Set vals Aba Financeiro
						$("#idVencimentoModal").val(filtro.filtroModalFinanceiro.idVencimento);
						$("#idVrMinimoModal").val(filtro.filtroModalFinanceiro.vrMinimo);
						$("#idIsSugereSuspensaoModal").attr("checked", filtro.filtroModalFinanceiro.isSugereSuspensao);
						showCamposSuspensao($("#idIsSugereSuspensaoModal").attr("checked") == "checked");
						$("#idQtdDividaEmAbertoModal").val(filtro.filtroModalFinanceiro.qtdDividaEmAberto);
						$("#idVrDividaEmAbertoModal").val(filtro.filtroModalFinanceiro.vrDividaEmAberto);
						
						//Set vals Aba Distribuicao
						$("#idModalNmAssitPromoComercial").val(filtro.filtroModalDistribuicao.nmAssitPromoComercial);
						$("#idModalNmGerenteComercial").val(filtro.filtroModalDistribuicao.nmGerenteComercial);
						$("#idModalObservacao").val(filtro.filtroModalDistribuicao.observacao);
						$("#idModalIsRepartePontoVenda").attr("checked", filtro.filtroModalDistribuicao.isRepartePontoVenda);
						$("#idModalIsSolicitacaoNumAtrasoInternet").attr("checked", filtro.filtroModalDistribuicao.isSolicitacaoNumAtrasoInternet);
						$("#idModalIsRecebeRecolheProdutosParciais").attr("checked", filtro.filtroModalDistribuicao.isRecebeRecolheProdutosParciais);
						
						//Tipo Entrega
						$("#idModalIdTipoEntrega").val(filtro.filtroModalDistribuicao.descricaoTipoEntrega);
						alteracaoCotaController.selectTipoEntregaDistribuicao();
							
						if($("#idModalIdTipoEntrega").val() == 'ENTREGA_EM_BANCA'){
							//Entrega em Banca
							$("#termoAdesao").attr("checked", filtro.filtroModalDistribuicao.termoAdesao);
							$("#termoAdesaoRecebido").attr("checked", filtro.filtroModalDistribuicao.termoAdesaoRecebido);
							$("#percentualFaturamentoEntregaBanca").val(filtro.filtroModalDistribuicao.percentualFaturamentoEntregaBanca);
							$("#taxaFixaEntregaBanca").val(filtro.filtroModalDistribuicao.taxaFixaEntregaBanca);
							
							if(filtro.filtroModalDistribuicao.carenciaInicioEntregaBanca){
								$("#carenciaInicioEntregaBanca").val(filtro.filtroModalDistribuicao.carenciaInicioEntregaBanca.$);
							}
							
							if(filtro.filtroModalDistribuicao.carenciaFimEntregaBanca){
								$("#carenciaFimEntregaBanca").val(filtro.filtroModalDistribuicao.carenciaFimEntregaBanca.$);
							}
						
							alteracaoCotaController.mostrarEsconderDivUtilizaArquivoTermo();
							alteracaoCotaController.mostrarEsconderDivArquivoUpLoadTermo();
						}else if($("#idModalIdTipoEntrega").val() == 'ENTREGADOR'){
							$("#procuracao").attr("checked", filtro.filtroModalDistribuicao.procuracao);
							$("#procuracaoRecebida").attr("checked", filtro.filtroModalDistribuicao.procuracaoRecebida);
							$("#percentualFaturamentoEntregador").val(filtro.filtroModalDistribuicao.percentualFaturamentoEntregador);
							
							if(filtro.filtroModalDistribuicao.carenciaInicioEntregador){
								$("#carenciaInicioEntregador").val(filtro.filtroModalDistribuicao.carenciaInicioEntregador.$);
							}
							
							if(filtro.filtroModalDistribuicao.carenciaFimEntregador){
								$("#carenciaFimEntregador").val(filtro.filtroModalDistribuicao.carenciaFimEntregador.$);
							}
							
							alteracaoCotaController.mostrarEsconderDivUtilizaArquivoProcuracao();
							alteracaoCotaController.mostrarEsconderDivArquivoUpLoadProcuracao();
						}
						
						
						
						
						//Checks Emissao Documento
						$("#isSlipImpresso").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isSlipImpresso);
						$("#isSlipEmail").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isSlipEmail);
						$("#isBoletoImpresso").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isBoletoImpresso);
						$("#isBoletoEmail").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isBoletoEmail);
						$("#isBoletoSlipImpresso").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isBoletoSlipImpresso);
						$("#isBoletoSlipEmail").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isBoletoSlipEmail);
						$("#isReciboImpresso").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isReciboImpresso);
						$("#isReciboEmail").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isReciboEmail);
						$("#isNotaEnvioImpresso").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isNotaEnvioImpresso);
						$("#isNotaEnvioEmail").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isNotaEnvioEmail);
						$("#isChamdaEncalheImpresso").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isChamdaEncalheImpresso);
						$("#isChamdaEncalheEmail").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isChamdaEncalheEmail);
						
					
						
					}else{
						alteracaoCotaController.limparCamposAbas();
					}
					
					alteracaoCotaController.formatarCampos();
				},
			  	null
		);
	},
	
	limparCamposAbas : function(){
		
		//Set vals Aba Financeiro
		$("#idVencimentoModal").val("");
		$("#idVrMinimoModal").val("");
		$("#idIsSugereSuspensaoModal").attr("checked", false);
		$("#idQtdDividaEmAbertoModal").val("");
		$("#idVrDividaEmAbertoModal").val("");
		
		//Set vals Aba Distribuicao
		$("#idModalNmAssitPromoComercial").val("");
		$("#idModalNmGerenteComercial").val("");
		$("#idModalObservacao").val("");
		$("#idModalIsRepartePontoVenda").attr("checked", false);
		$("#idModalIsSolicitacaoNumAtrasoInternet").attr("checked", false);
		$("#idModalIsRecebeRecolheProdutosParciais").attr("checked", false);
		$("#idModalIdTipoEntrega").val("");
			//Entrega em Banca
		$("#entregaBancaPj").hide();
		$("#termoAdesao").attr("checked", false);
		$("#termoAdesaoRecebido").attr("checked", false);
		$("#uploadedFileTermo").val("");
		$("#percentualFaturamentoEntregaBanca").val("");
		$("#taxaFixaEntregaBanca").val("");
		$("#carenciaInicioEntregaBanca").val("");
		$("#carenciaFimEntregaBanca").val("");
			//Entregador
		$("#entregadorPj").hide();
		$("#procuracao").attr("checked", false);
		$("#procuracaoRecebida").attr("checked", false);
		$("#uploadedFileProcuracao").val("");
		$("#percentualFaturamentoEntregador").val("");
		$("#carenciaInicioEntregador").val("");
		$("#carenciaFimEntregador").val("");
		

		//Checks Emissao Documento
		$("#isSlipImpresso").attr("checked", false);
		$("#isSlipEmail").attr("checked", false);
		$("#isBoletoImpresso").attr("checked", false);
		$("#isBoletoEmail").attr("checked", false);
		$("#isBoletoSlipImpresso").attr("checked", false);
		$("#isBoletoSlipEmail").attr("checked", false);
		$("#isReciboImpresso").attr("checked", false);
		$("#isReciboEmail").attr("checked", false);
		$("#isNotaEnvioImpresso").attr("checked", false);
		$("#isNotaEnvioEmail").attr("checked", false);
		$("#isChamdaEncalheImpresso").attr("checked", false);
		$("#isChamdaEncalheEmail").attr("checked", false);
		alteracaoCotaController.limparCamposTipoEntrega();
		
	},
	
	popularComboFornecedor : function(data, combo) {
		opcoes = "";
		$.each(data, function(i,n){
			opcoes+="<option value="+n.key.$+">"+n.value.$+"</option>";
		});
		$(combo).clear().append(opcoes);
	},
	
	verificarCheck : function(){
		var todosChecados = true;
		var selecionados = 0;
		var totalCotas = $("#totalCotasSelecionadas", this.workspace);
		
		$(".selectLine", this.workspace).each(function(index, element) {	
			if(element.checked){
				selecionados++;
			}else{
				todosChecados = false;
			}
			
		});
		totalCotas.html(selecionados);
		$("#alteracaoCotaCheckAll", this.workspace).get(0).checked = todosChecados;
	},
	
	checkAll : function(check){
		
		$(".selectLine", this.workspace).each(function(index, element) {
			element.checked = check.checked;
		});
		alteracaoCotaController.verificarCheck();
	},
	
	salvarAlteracao : function() {
		
		var  dataForm = $("#alteracaoForm :input[value][value!=''][value!='-1']", this.workspace).serializeArray();
		if(dataForm[0] && dataForm[0].value){
			dataForm[0].value = floatValue(dataForm[0].value);
		}
		
		
		$("#idListaFornecedorAssociado option", this.workspace).each(function (index) {
			 dataForm.push({name: 'filtroAlteracaoCotaDTO.filtroModalFornecedor.listaFornecedoresSelecionados['+index+']', 
				 			value:$(this, this.workspace).val() } );
		});
		
		dataForm =  dataForm.concat(this.listListaLinhaSelecao);
		
		
		$.postJSON(contextPath + "/administracao/alteracaoCota/salvarAlteracao",
				dataForm,  
			   	function () {
					$("#dialog-novo", this.workspace).dialog( "close" );
					alteracaoCotaController.carregarValorMinimo();
					alteracaoCotaController.pesquisar();
					
				},
			  	null,
			   	true,
			   	'dialogMensagemNovo'
		);
	},
	
	
	selectTipoEntregaDistribuicao : function() {
		alteracaoCotaController.limparCamposTipoEntrega();
		var tipoEntrega = $('#idModalIdTipoEntrega', this.workspace).val();
		if ( tipoEntrega == 'COTA_RETIRA' ) {
			$('#entregaBancaPj', this.workspace).hide();
			$('#entregadorPj', this.workspace).hide();
			
		} else if (tipoEntrega == 'ENTREGA_EM_BANCA'  ){
			$('#entregaBancaPj', this.workspace).show();
			$('#entregadorPj', this.workspace).hide();
			
		} else if (tipoEntrega == 'ENTREGADOR'  ){
			$('#entregadorPj', this.workspace).show();
			$('#entregaBancaPj', this.workspace).hide();
			
		}
		
	},
	
	uploadArquivo : function(file, formId) {
		$('#' + formId).submit();
		
	},
	
   tratarRetornoUpload : function(data) {
		
		data = replaceAll(data, "<pre>", "");
		data = replaceAll(data, "</pre>", "");
		
		data = replaceAll(data, "<PRE>", "");
		data = replaceAll(data, "</PRE>", "");
		
		var responseJson = jQuery.parseJSON(data);
		
		if (responseJson.mensagens) {

			exibirMensagemDialog(
				responseJson.mensagens.tipoMensagem, 
				responseJson.mensagens.listaMensagens, "dialog-cota"
			);
		}

	},
	
	downloadTermoAdesao : function() {
		var taxaFixaEntregaBanca =  $("#taxaFixaEntregaBanca", this.workspace).val();
		var percentualFaturamentoEntregaBanca = $("#percentualFaturamentoEntregaBanca", this.workspace).val();
		
	
		$.postJSON(contextPath + "/administracao/alteracaoCota/validarValoresParaDownload",
				[{name : "taxa", value : taxaFixaEntregaBanca} ,{
				 name : "percentual", value : percentualFaturamentoEntregaBanca } ],
				function() {
					document.location.assign(contextPath + "/administracao/alteracaoCota/downloadTermoAdesao?termoAdesaoRecebido="+ $('#termoAdesaoRecebido', this.workspace).is(':checked')+"&numeroCota="+alteracaoCotaController.buscaIdPrimeiraCotaSelecionada()+"&taxa="+$("#taxaFixaEntregaBanca").val()+"&percentual="+$("#percentualFaturamentoEntregaBanca").val());
				},
				null,
				true,
				"dialog-cota");
	},
	
	downloadProcuracao : function() {
		
		document.location.assign(contextPath + "/administracao/alteracaoCota/downloadProcuracao?procuracaoRecebida="+$('#procuracaoRecebida', this.workspace).is(':checked')+"&numeroCota="+alteracaoCotaController.buscaIdPrimeiraCotaSelecionada());
	},
	
	limparCamposTipoEntrega : function()
	  {
		$("#percentualFaturamentoEntregaBanca", this.workspace).val("");
		$("#taxaFixaEntregaBanca", this.workspace).val("");
		$("#carenciaInicioEntregaBanca", this.workspace).val("");
		$("#carenciaFimEntregaBanca", this.workspace).val("");
		
		$("#percentualFaturamentoEntregador", this.workspace).val("");
		$("#carenciaInicioEntregador", this.workspace).val("");
		$("#carenciaFimEntregador", this.workspace).val("");
		
		$("#procuracao", this.workspace ).attr("checked", false);
		$("#procuracaoRecebida", this.workspace).attr("checked", false);
		$("#termoAdesaoRecebido", this.workspace).attr("checked", false);
		$("#termoAdesao", this.workspace).attr("checked", false);
		$("#uploadTermo", this.workspace).hide();
		$("#termoArquivoRecebido", this.workspace).hide();
		$("#termoRecebidoDownload", this.workspace).hide();
		$("#uploadedFileTermoDiv", this.workspace).hide();
		$('#uploadedFileTermo').val('');
		$('#uploadedFileProcuracao').val('');
		$('#nomeArquivoTermoAdesao').val('');
		$('#nomeArquivoProcuracao').val('');
		
		
		
		
		$("#uploadProcuracao", this.workspace).hide();
		$("#procuracaoArquivoRecebido", this.workspace).hide();
		$("#procuracaoRecebidoDownload", this.workspace).hide();
		$("#uploadedFileProcuracaoDiv", this.workspace).hide();
		
	  },
	  mostrarEsconderDivUtilizaArquivoTermo :function(){
		  
	      if ( $('#termoAdesao', this.workspace).is(':checked') ) {
			  $("#uploadTermo", this.workspace).show();
			  $("#termoArquivoRecebido", this.workspace).show();
			  $("#termoRecebidoDownload", this.workspace).show();
	      } else {
			  $("#uploadTermo", this.workspace).hide();
			  $("#termoArquivoRecebido", this.workspace).hide();
			  $("#termoRecebidoDownload", this.workspace).hide();
			  
	      }
	  },
	  
	  
	  mostrarEsconderDivArquivoUpLoadTermo :function(){
		  if ( $('#termoAdesaoRecebido', this.workspace).is(':checked') ) {
			  $("#uploadedFileTermoDiv", this.workspace).show();
	      } else {
			  $("#uploadedFileTermoDiv", this.workspace).hide();
	      }
	  },
	  
	  mostrarEsconderDivUtilizaArquivoProcuracao :function(){
		  
	      if ( $('#procuracao', this.workspace).is(':checked') ) {
			  $("#uploadProcuracao", this.workspace).show();
			  $("#procuracaoArquivoRecebido", this.workspace).show();
			  $("#procuracaoRecebidoDownload", this.workspace).show();
	      } else {
			  $("#uploadProcuracao", this.workspace).hide();
			  $("#procuracaoArquivoRecebido", this.workspace).hide();
			  $("#procuracaoRecebidoDownload", this.workspace).hide();
			  
			  
	      }
	  },
	  
	  
	  mostrarEsconderDivArquivoUpLoadProcuracao :function(){
		  if ( $('#procuracaoRecebida', this.workspace).is(':checked') ) {
			  $("#uploadedFileProcuracaoDiv", this.workspace).show();
	      } else {
			  $("#uploadedFileProcuracaoDiv", this.workspace).hide();
	      }
	  },
	  
	  buscaIdPrimeiraCotaSelecionada : function(){
		
		  id = null;
	   $(".selectLine", this.workspace).each(function(index, element) {	
			if(element.checked){
				id = element.value;
			}
			
		});
	  
	   return id;
	  }

	

}, BaseController);


function popup() {
	
	$( "#dialog-novo" ).dialog({
		resizable: false,
		height:560,
		width:880,
		modal: true,
		buttons: {
			"Confirmar": function() {
				$( this ).dialog( "close" );
				$("#effect").show("highlight", {}, 1000, callback);
				$(".grids").show();
				
			},
			"Cancelar": function() {
				$( this ).dialog( "close" );
			}
		}
	});
	
};

function popup_confirmar() {

	$( "#dialog-confirm" ).dialog({
		resizable: false,
		height:'auto',
		width:300,
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
};


$(function() {
	$("#tabs").tabs();
});

function mostraSemanal() {
	$(".semanal").show();
	$(".quinzenal").hide();
	$(".mensal").hide();
};

function mostraMensal() {
	$(".semanal").hide();
	$(".quinzenal").hide();
	$(".mensal").show();
};
function mostraDiario() {
	$(".semanal").hide();
	$(".quinzenal").hide();
	$(".mensal").hide();
};
function mostraQuinzenal() {
	$(".semanal").hide();
	$(".quinzenal").show();
	$(".mensal").hide();
}


function associarFornecedor(){

	var options = $("#idListFornecedores option:selected", this.workspace);
	var op = "";
	$.each(options, function(i,n){
		op+="<option value="+n.value+">"+n.text+"</option>";
	});
	
	$("#idListaFornecedorAssociado").append(op);
	options.remove();
}

function desAssociarFornecedor(){
	var options = $("#idListaFornecedorAssociado option:selected", this.workspace);
	var op = "";
	
	$.each(options, function(i,n){
		op+="<option value="+n.value+">"+n.text+"</option>";
	});
	
	$("#idListFornecedores").append(op);
	options.remove();
}

function showCamposSuspensao(show){
	if(show)
		$('.suspensao').show();
	else
		$('.suspensao').hide();
}

function tipoEntregaPj(opcao){
	var entregaBancaPj = $("entregaBancaPj"); 
	var entregadorPf = $("entregadorPj");
	
	
	switch (opcao) {   
		case '1':   
			entregaBancaPj.style.display = "";   
			entregadorPj.style.display = "none";     
		break;   
		case '2':   
			entregaBancaPj.style.display = "none";   
			entregadorPj.style.display = "";      
		break; 
		default:   
			entregaBancaPj.style.display = "none";   
			entregadorPj.style.display = "none";   
		break;   
	}   
	
}

function mostraTermoPf(){
	$('.termoPf').show();
	}

//@ sourceURL=alteracaoCota.js
