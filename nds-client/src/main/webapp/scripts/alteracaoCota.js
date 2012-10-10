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
				width:850,
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
						$("#isSkipImpresso").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isSkipImpresso);
						$("#isSkipEmail").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isSkipEmail);
						$("#isBoletoImpresso").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isBoletoImpresso);
						$("#isBoletoEmail").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isBoletoEmail);
						$("#isBoletoSkipImpresso").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isBoletoSkipImpresso);
						$("#isBoletoSkipEmail").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isBoletoSkipEmail);
						$("#isReciboImpresso").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isReciboImpresso);
						$("#isReciboEmail").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isReciboEmail);
						$("#isNoteEnvioImpresso").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isNoteEnvioImpresso);
						$("#isNoteEnvioEmail").attr("checked", filtro.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isNoteEnvioEmail);
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
		$("#isSkipImpresso").attr("checked", false);
		$("#isSkipEmail").attr("checked", false);
		$("#isBoletoImpresso").attr("checked", false);
		$("#isBoletoEmail").attr("checked", false);
		$("#isBoletoSkipImpresso").attr("checked", false);
		$("#isBoletoSkipEmail").attr("checked", false);
		$("#isReciboImpresso").attr("checked", false);
		$("#isReciboEmail").attr("checked", false);
		$("#isNoteEnvioImpresso").attr("checked", false);
		$("#isNoteEnvioEmail").attr("checked", false);
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
	
	salvarAlteracao : function() {

		$.postJSON(contextPath + "/administracao/alteracaoCota/salvarAlteracao.json?"+$("#pesquisarForm", this.workspace).serialize(),  
			   	null,
			   	function (result) {

					var tipoMensagem = result.tipoMensagem;
					var listaMensagens = result.listaMensagens;
					
					if (tipoMensagem && listaMensagens) {
						exibirMensagem(tipoMensagem, listaMensagens);
					} 

					if (tipoMensagem == 'SUCCESS') {
						$("#dialog-novo", this.workspace).dialog( "close" );
						alteracaoCotaController.pesquisar();
					}
				},
			  	null,
			   	true,
			   	'dialogMensagemNovo'
		);
	},
	

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
	var entregaBancaPj = document.getElementById("entregaBancaPj"); 
	var entregadorPf = document.getElementById("entregadorPj");
	
	
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