var ConferenciaEncalheCont = {
	
	modalAberta: false,
	
	idProdutoEdicaoNovoEncalhe: "",
		
	pesquisarCota : function(){
		
		var data = [{name : 'numeroCota', value : $("#numeroCota").val()}];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/verificarReabertura", data,
			function(result){
				
				if (result.listaMensagens && result.listaMensagens[0] == "REABERTURA"){
					
					ConferenciaEncalheCont.modalAberta = true;
					
					$("#dialog-reabertura").dialog({
						resizable : false,
						height : 200,
						width : 360,
						modal : true,
						buttons : {
							"Sim" : function() {
								
								$("#dialog-reabertura").dialog("close");
								ConferenciaEncalheCont.carregarListaConferencia();
								ConferenciaEncalheCont.popup_alert();
							},
							"Não" : function() {
								$("#dialog-reabertura").dialog("close");
							}
						}, close : function(){
							
							ConferenciaEncalheCont.modalAberta = false;
						}
					});
				} else {
					
					ConferenciaEncalheCont.carregarListaConferencia();
					$("#dialog-reabertura").dialog("close");
					ConferenciaEncalheCont.popup_alert();
				}
			}
		);
	},
	
	carregarListaConferencia: function(){
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarListaConferencia', 
				[{name: "numeroCota", value: $("#numeroCota").val()}], 
				function(result){
					
					ConferenciaEncalheCont.preProcessarConsultaConferenciaEncalhe(result);
				}
		);
	},
	
	preProcessarConsultaConferenciaEncalhe : function(result){
		
		if (result.mensagens){
			
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
			return;
		}
		
		var innerTable = '';
		
		var modeloConferenciaEncalhe = result.listaConferenciaEncalhe;
		
		$("._dadosConfEncalhe").remove();
		
		$.each(modeloConferenciaEncalhe, 
			function(index, value) {
				
				var _class;
				
				if (index % 2 == 0){
					_class = "class_linha_1 _dadosConfEncalhe";
				} else {
					_class = "class_linha_2 _dadosConfEncalhe";
				}
				
				innerTable += "<tr class='" + _class + "'>";
				
				innerTable += "<td nowrap='nowrap'>" + value.codigo + "</td>";
				
				innerTable += "<td nowrap='nowrap'>" + value.nomeProduto + "</td>";
				
				innerTable += "<td nowrap='nowrap' style='text-align: center;'>" + value.numeroEdicao + "</td>";
				
				innerTable += "<td style='text-align: center;'>" + "TODO" + "</td>";
				
				innerTable += "<td style='text-align: right;' nowrap='nowrap'>" + parseFloat(value.precoCapa).toFixed(2) + "</td>";
				
				innerTable += "<td style='text-align: right;' nowrap='nowrap'>" + parseFloat(value.desconto).toFixed(2) + "</td>";
				
				innerTable += "<td nowrap='nowrap' style='text-align: center;'>";
				
				var valorExemplares = parseInt(value.qtdExemplar);
				
				var inputExemplares = '<input id="qtdExemplaresGrid_' + index + '" maxlength="255" onchange="ConferenciaEncalhe.recalcularValores('+ index +');" style="width:90px; text-align: center;" value="' + valorExemplares + '"/>' +
					'<input id="idConferenciaEncalheHidden_' + index + '" type="hidden" value="' + value.idConferenciaEncalhe + '"/>';
				
				innerTable += inputExemplares + "</td>";
				
				innerTable += "<td align='right' nowrap='nowrap' id='valorTotalConferencia_" + index + "'>" + parseFloat(value.valorTotal).toFixed(2) + "</td>";
				
				var inputCheckBoxJuramentada = 
					'<input type="checkbox" ' + (value.juramentada == "true" ? 'checked="checked"' : '')
						+ (!value.juramentada ? 'disabled="disabled"' : '')
						+ ' id="checkGroupJuramentada_' + index + '"/>';
				
				innerTable += "<td style='text-align: center;' nowrap='nowrap'>" + inputCheckBoxJuramentada + "</td>";
				
				innerTable += "</tr>";
				
				$(innerTable).appendTo("#dadosGridConferenciaEncalhe");
				
				innerTable = '';
			}
		);
		
		$('input[id*="qtdExemplaresGrid"]').numeric();
		
		$(".outrosVlrsGrid").flexAddData({
			page: result.listaDebitoCredito.page, total: result.listaDebitoCredito.total, rows: result.listaDebitoCredito.rows
		});
		
		$("#totalReparte").text(parseFloat(result.reparte).toFixed(2));
		$("#totalEncalhe").text(parseFloat(result.valorEncalhe).toFixed(2));
		$("#valorVendaDia").text(parseFloat(result.valorVendaDia).toFixed(2));
		$("#totalOutrosValores").text(parseFloat(result.valorDebitoCredito).toFixed(2));
		$("#valorAPagar").text(parseFloat(result.valorPagar).toFixed(2));
		
		$(".dadosFiltro").show();
		$("#nomeCota").text(result.razaoSocial);
		$("#statusCota").text(result.situacao);
	},
	
	pesquisarProdutoPorCodigoNome: function(){
		
		var codigoNomeProduto = $("#lstProdutos").val();
		
		if (codigoNomeProduto && codigoNomeProduto.length > 0){
			$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoPorCodigoNome', 
					"codigoNomeProduto=" + codigoNomeProduto, 
				function(result){
					
					if (result[0]){
						
						$("#lstProdutos").autocomplete({
							source: result,
							select: function(event, ui){
								
								$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/pesquisarProdutoEdicaoPorId",
									[{name: "idProdutoEdicao", value: ui.item.chave.long}],
									function(result2){
										
										if (result2){
											
											ConferenciaEncalheCont.idProdutoEdicaoNovoEncalhe = ui.item.chave.long;
											$("#lstProdutos").val(ui.item.chave.string);
											$("#numEdicaoNovoEncalhe").val(result2.numeroEdicao);
											$("#precoCapaNovoEncalhe").val(parseFloat(result2.precoVenda).toFixed(2));
											$("#descontoNovoEncalhe").val(parseFloat(result2.desconto).toFixed(2));
										}
									}, null, true, "idModalNovoEncalhe"
								);
							}
						});
					}
				}, null, true, "idModalNovoEncalhe"
			);
		}
	},
	
	adicionarEncalhe: function(){
		
		var data = [{name: "idProdutoEdicao", value: ConferenciaEncalheCont.idProdutoEdicaoNovoEncalhe}, 
		            {name: "quantidade", value: $("#exemplaresNovoEncalhe").val()}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/adicionarProdutoConferido', data,
			function(result){
				
				ConferenciaEncalheCont.preProcessarConsultaConferenciaEncalhe(result);
				
				ConferenciaEncalheCont.limparCamposNovoEncalhe();
			}, null, true, "idModalNovoEncalhe"
		);
	},
	
	limparCamposNovoEncalhe: function(){
		
		$("#lstProdutos").val("");
		$("#numEdicaoNovoEncalhe").val("");
		$("#precoCapaNovoEncalhe").val("");
		$("#descontoNovoEncalhe").val("");
		$("#exemplaresNovoEncalhe").val("");
		$("#valorTotalNovoEncalhe").val("");
		$("#checkboxJueramentadaNovoEncalhe").removeAttr("checked");
	},
	
	calcularValorTotalNovoEncalhe: function(){
		
		$("#valorTotalNovoEncalhe").val(parseFloat(($("#precoCapaNovoEncalhe").val() - $("#descontoNovoEncalhe").val()) * $("#exemplaresNovoEncalhe").val()).toFixed(2));
	},
	
	popup_conferencia: function () {
		
		ConferenciaEncalheCont.modalAberta = true;
	
		$("#dialog-conferencia").dialog({
			resizable : false,
			height : 'auto',
			width : 450,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$(this).dialog("close");
					$("#effect").hide("highlight", {}, 1000, callback);
	
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});
	
	},
	
	popup_novo_encalhe: function () {
		
		ConferenciaEncalheCont.modalAberta = true;
		
		$("#dialog-encalhe").dialog({
			resizable : false,
			height : 'auto',
			width : 450,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$(this).dialog("close");
					$("#effect").hide("highlight", {}, 1000, callback);

				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			}
		});

	},
	
	popup_alert: function () {
		
		ConferenciaEncalheCont.modalAberta = true;

		$("#dialog-alert").dialog({
			resizable : false,
			height : 190,
			width : 460,
			modal : true,
			buttons : {
				"Sim" : function() {
					$(this).dialog("close");
					ConferenciaEncalheCont.popup_notaFiscal();

				},
				"Não" : function() {
					$("#vlrCE").focus();
					$(this).dialog("close");
				}
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			}
		});

	},
	
	popup_notaFiscal: function () {
		
		ConferenciaEncalheCont.modalAberta = true;

		$("#dialog-notaFiscal").dialog({
			resizable : false,
			height : 360,
			width : 750,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					var data = [
						{name : "notaFiscal.numero", value : $("#numNotaFiscal").val()},
						{name : "notaFiscal.serie", value : $("#serieNotaFiscal").val()},
						{name : "notaFiscal.dataEmissao", value : $("#dataNotaFiscal").val()},
						{name : "notaFiscal.valorProdutos", value : $("#valorNotaFiscal").val()},
						{name : "notaFiscal.chaveAcesso", value : $("#chaveAcessoNFE").val()}
					];
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/salvarNotaFiscal', data, 
						function(result){
							
							$("#dialog-notaFiscal").dialog("close");
							
							$("#vlrCE").val(parseFloat($("#valorNotaFiscal").val()).toFixed(2));
							
							$("#vlrCE").focus();
						}
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
					$("#vlrCE").focus();
				}
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			}
		});

	},

	popup_outros_valores: function () {
		
		ConferenciaEncalheCont.modalAberta = true;
		
		$('#observacao').focus();
		$("#dialog-outros-valores").dialog({
			resizable : false,
			height : 430,
			width : 460,
			modal : true,
			buttons : {
				"Fechar" : function() {
					$(this).dialog("close");

				}
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			}
		});

	},

	popup_finalizar_conferencia: function () {
		
		ConferenciaEncalheCont.modalAberta = true;

		$("#dialog-finaliza-conferencia").dialog({
			resizable : false,
			height : 190,
			width : 460,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$(this).dialog("close");
					$("#effect").hide("highlight", {}, 1000, callback);
				},
				"Cancelar" : function() {
					$(this).dialog("close");

				}
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			}
		});

	},

	popup_salvarInfos: function () {
		
		ConferenciaEncalheCont.modalAberta = true;
		
		$("#dialog-salvar").dialog({
			resizable : false,
			height : 190,
			width : 460,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$(this).dialog("close");
					$("#effect").hide("highlight", {}, 1000, callback);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			}
		});

	}
};

$(function() {

	$(".outrosVlrsGrid").flexigrid({
		dataType : 'json',
		colModel : [ {
			display : 'Data',
			name : 'data',
			width : 100,
			sortable : false,
			align : 'left'
		}, {
			display : 'Tipo de Lançamento',
			name : 'tipoLancto',
			width : 140,
			sortable : false,
			align : 'left'
		}, {
			display : 'Valor R$',
			name : 'valor',
			width : 100,
			sortable : false,
			align : 'right'
		} ],
		width : 400,
		height : 250
	});
	
	$("#numeroCota").numeric();
	$("#numeroCota").focus();
	$("#exemplaresNovoEncalhe").numeric();
	$("#vlrCE").numeric();
	$("#dataNotaFiscal").mask("99/99/9999");
	
	$("#numeroCota").keypress(function(e) {
		
		if (e.keyCode == 13) {
			
			ConferenciaEncalheCont.pesquisarCota();
		}
	});
	
	$("#lstProdutos").keypress(function(e){
		
		ConferenciaEncalheCont.pesquisarProdutoPorCodigoNome();
	});
});

shortcut.add("F2", function() {
	
	if (!ConferenciaEncalheCont.modalAberta){
		
		ConferenciaEncalheCont.limparCamposNovoEncalhe();
		ConferenciaEncalheCont.popup_novo_encalhe();
	}
});

shortcut.add("F6", function() {
	
	if (!ConferenciaEncalheCont.modalAberta){
	
		ConferenciaEncalheCont.popup_finalizar_conferencia();
	}
});

shortcut.add("F8", function() {
	
	if (!ConferenciaEncalheCont.modalAberta){
		
		ConferenciaEncalheCont.popup_outros_valores();
	}
});

shortcut.add("F9", function() {
	
	if (!ConferenciaEncalheCont.modalAberta){
		
		ConferenciaEncalheCont.popup_salvarInfos();
	}
});