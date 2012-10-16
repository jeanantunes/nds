$(document).ready(function() {
	$("#acionador").click(function() {
		$(".selectLine").attr("checked", this.checked);
	});
	
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
		$("#carenciaFimEntregador", this.workspace).mask("99/99/9999");
		
		
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
			
			var campoSelect = "<input name='filtroAlteracaoCotaDTO.listaLinhaSelecao["+index+"]' class='selectLine' type='checkbox' value='"+row.cell.idCota+"'>";
			
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
						$("#idModalIdTipoEntrega").val(filtro.filtroModalDistribuicao.idTipoEntrega);
	
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
			opcoes+="<option value="+n.id+">"+n.juridica.razaoSocial+"</option>";
		});
		$(combo).clear().append(opcoes);
	},
	
	salvarAlteracao : function() {
		alert($("#pesquisarForm", this.workspace).serialize());
		$.postJSON(contextPath + "/administracao/alteracaoCota/salvarAlteracao",
				$("#pesquisarForm", this.workspace).serialize(),  
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
		document.location.assign("administracao/alteracaoCota/downloadTermoAdesao?termoAdesaoRecebido=true&numeroCota=1234");
	},
	
	downloadProcuracao : function() {
	
		for	(i=0;i<2;i++){
			alteracaoCotaController.newDoc();
		}
	},
	
	limparCamposTipoEntrega : function()
	  {
		$("#percentualFaturamentoEntregaBranca", this.workspace).val("");
		$("#taxaFixaEntregaBranca", this.workspace).val("");
		$("#carenciaInicioEntregaBranca", this.workspace).val("");
		$("#carenciaFimEntregaBranca", this.workspace).val("");
		
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
