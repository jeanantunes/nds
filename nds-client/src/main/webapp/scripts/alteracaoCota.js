$(document).ready(function() {
	
	
	showCamposSuspensao($("#idIsSugereSuspensaoModal").attr("checked") == "checked");
	
	$("#idIsSugereSuspensaoModal").click(function() {
		showCamposSuspensao($("#idIsSugereSuspensaoModal").attr("checked") == "checked");
	});
	
	
	
	
});

var alteracaoCotaController = $.extend(true, {
	
	pesquisaCotaAlteracaoCota : null,

	init : function(pesquisaCota) {
		this.pesquisaCotaAlteracaoCota = pesquisaCota;
		
		this.iniciarGrid();
		
		$("#percentualFaturamentoEntregaBranca", this.workspace).mask("99.99");
		$("#taxaFixaEntregaBranca", this.workspace).numeric();
		$("#carenciaInicioEntregaBranca", this.workspace).mask("99/99/9999");
		$("#carenciaFimEntregaBranca", this.workspace).mask("99/99/9999");
		
		$("#percentualFaturamentoEntregador", this.workspace).mask("99.99");
		$("#carenciaInicioEntregador", this.workspace).mask("99/99/9999");
		$("#carenciaFimEntregado", this.workspace).mask("99/99/9999");
		
		
		$("#carenciaInicioEntregaBranca", this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$("#carenciaFimEntregaBranca", this.workspace).datepicker({
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
							sequentialUploads: true,
							dataType : 'json',
							paramName : 'uploadedFileProcuracao',
							replaceFileInput: false,
							submit : function(e, data) {
								data = $("#pesquisarForm", this.workspace).serialize();

							},
							done : function(e, data) {
							//	 $('#uploadedFileProcuracao').destroy();
							},
							progressall : function(e, data) {
								
							},
							send : function(e, data) {

							}
							 
						});
    	
    	
    	$('#uploadedFileTermo').fileupload(
				{
					url :"administracao/alteracaoCota/uploadedFileTermo",
					sequentialUploads: true,
					dataType : 'json',
					paramName : 'uploadedFileTermo',
					replaceFileInput: false,
					submit : function(e, data) {
						data = $("#pesquisarForm", this.workspace).serialize();

					},
					done : function(e, data) {
					//	 $('#uploadedFileProcuracao').destroy();
					},
					progressall : function(e, data) {
						
					},
					send : function(e, data) {

					}
					 
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
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			}],
			sortname : "cota.numeroCota",
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
	
	pesquisar : function() {
		alteracaoCotaController.verificarCheck();
		var params = $("#pesquisarForm", this.workspace).serialize();
		
		$(".alteracaoGrid", this.workspace).flexOptions({
			url: contextPath + "/administracao/alteracaoCota/pesquisarAlteracaoCota.json?"+params,
			newp: 1
		});
	
		
		$(".alteracaoGrid", this.workspace).flexReload();
		$(".grids", this.workspace).show();
				

	},
	
	executarPreProcessamento : function(resultado) {

		$.each(resultado.rows, function(index, row) {
			
			var campoSelect = "<input name='filtroAlteracaoCotaDTO.listaLinhaSelecao["+ index +"]' class='selectLine' type='checkbox' value='"+row.cell.idCota+"' onclick='alteracaoCotaController.verificarCheck();'>";
			
			row.cell.acao = campoSelect;
		});
			
		$(".grids", this.workspace).show();
		
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
		
		$(".selectLine", this.workspace).each(function(index, element) {	
			if(element.checked){
				element.name = 'filtroAlteracaoCotaDTO.listaLinhaSelecao['+ linhasSelecionadas +']';
				linhasSelecionadas++;
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
		
		$.postJSON(contextPath + "/administracao/alteracaoCota/carregarCamposAlteracao",
				$("#pesquisarForm", this.workspace).serialize(),
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
						alteracaoCotaController.selectTipoEntregaDistribuicao()
							
						if($("#idModalIdTipoEntrega").val() == 'ENTREGA_EM_BANCA'){
							//Entrega em Banca
							$("#termoAdesao").attr("checked", filtro.filtroModalDistribuicao.termoAdesao);
							$("#termoAdesaoRecebido").attr("checked", filtro.filtroModalDistribuicao.termoAdesaoRecebido);
							$("#percentualFaturamentoEntregaBranca").val(filtro.filtroModalDistribuicao.percentualFaturamentoEntregaBranca);
							$("#taxaFixaEntregaBranca").val(filtro.filtroModalDistribuicao.taxaFixaEntregaBranca);
							$("#carenciaInicioEntregaBranca").val(filtro.filtroModalDistribuicao.carenciaInicioEntregaBranca);
							$("#carenciaFimEntregaBranca").val(filtro.filtroModalDistribuicao.carenciaFimEntregaBranca);
						}else if($("#idModalIdTipoEntrega").val() == 'ENTREGADOR'){
							$("#procuracao").attr("checked", filtro.filtroModalDistribuicao.procuracao);
							$("#procuracaoRecebida").attr("checked", filtro.filtroModalDistribuicao.procuracaoRecebida);
							$("#percentualFaturamentoEntregador").val(filtro.filtroModalDistribuicao.percentualFaturamentoEntregador);
							$("#carenciaInicioEntregador").val(filtro.filtroModalDistribuicao.carenciaInicioEntregador);
							$("#carenciaFimEntregador").val(filtro.filtroModalDistribuicao.carenciaFimEntregador);
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
					
				},
			  	null,
			   	true
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
		$("#percentualFaturamentoEntregaBranca").val("");
		$("#taxaFixaEntregaBranca").val("");
		$("#carenciaInicioEntregaBranca").val("");
		$("#carenciaFimEntregaBranca").val("");
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
	},
	
	popularComboFornecedor : function(data, combo) {
		opcoes = "";
		$.each(data, function(i,n){
			opcoes+="<option value="+n.id+">"+n.juridica.razaoSocial+"</option>";
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
		
		var  dataForm = $("#pesquisarForm", this.workspace).serializeArray();
		$("#idListaFornecedorAssociado option", this.workspace).each(function (index) {
			 dataForm.push({name: 'filtroAlteracaoCotaDTO.filtroModalFornecedor.listaFornecedoresSelecionados['+index+']', value:$(this, this.workspace).val() } );
		});
		
		$.postJSON(contextPath + "/administracao/alteracaoCota/salvarAlteracao",
				dataForm,  
			   	function () {
					$("#dialog-novo", this.workspace).dialog( "close" );
					alteracaoCotaController.pesquisar();
					
				},
			  	null,
			   	true,
			   	'dialogMensagemNovo'
		);
	},
	
	
	selectTipoEntregaDistribuicao : function() {

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
		document.location.assign(contextPath + "/cadastro/cota/downloadTermoAdesao?termoAdesaoRecebido="+D.get("termoAdesaoRecebido")+"&numeroCota="+D.get("numCota")+"&taxa="+D.get("taxaFixaEntregaBanca")+"&percentual="+D.get("percentualFaturamentoEntregaBanca"));
	},
	
	downloadProcuracao : function() {
		
		document.location.assign(contextPath + "/cadastro/cota/downloadProcuracao?procuracaoRecebida="+D.get("procuracaoRecebida")+"&numeroCota="+D.get("numCota"));
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
