var ConferenciaEncalheCont = {
	
	pesquisarCota : function(){
		
		var data = [{name : 'numeroCota', value : $("#numeroCota").val()}];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/verificarReabertura", data,
			function(result){
				
				if (result.listaMensagens && result.listaMensagens[0] == "REABERTURA"){
					
					modalAberta = true;
					
					$("#dialog-reabertura").dialog({
						resizable : false,
						height : 200,
						width : 360,
						modal : true,
						buttons : {
							"Sim" : function() {
								
								$("#dialog-reabertura").dialog("close");
								//ConferenciaEncalhe.carregarListaConferencia(data);
								//ConferenciaEncalhe.popup_alert();
							},
							"Não" : function() {
								$("#dialog-reabertura").dialog("close");
							}
						}, close : function(){
							
							modalAberta = false;
						}
					});
				} else {
					
					//ConferenciaEncalhe.carregarListaConferencia(data);
					//ConferenciaEncalhe.popup_alert();
				}
			}
		);
	},
		
	popup_conferencia: function () {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
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
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

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
			}
		});

	},
	
	pesquisar_cota: function() {
		$('#pesq_cota').keypress(function(e) {
			if (e.keyCode == 13) {
				$('.dadosFiltro').fadeIn('fast');
			}
		});
	},
	
	pesqMostraCota: function () {
		$('#pesq_cota').keypress(function(e) {
			if (e.keyCode == 13) {
				popup_alert();
				$('.dadosFiltro').fadeIn('fast');
				$("#vlrCE").focus();
				$(".grids").show();

			}
		});
	},
	
	popup_alert: function () {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$("#dialog-alert").dialog({
			resizable : false,
			height : 190,
			width : 460,
			modal : true,
			buttons : {
				"Sim" : function() {
					$(this).dialog("close");
					popup_notaFiscal();
					$("#vlrCE").focus();

				},
				"Não" : function() {
					$(this).dialog("close");
				}
			}
		});

	},
	
	popup_notaFiscal: function () {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$("#dialog-notaFiscal").dialog({
			resizable : false,
			height : 360,
			width : 750,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$(this).dialog("close");

				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});

	},

	popup_outros_valores: function () {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
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
			}
		});

	},

	popup_finalizar_conferencia: function () {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

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
			}
		});

	},

	popup_salvarInfos: function () {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
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

			}
		});

	}
};

$(function() {
	
	$(".conferenciaEncalheContGrid").flexigrid({
		dataType : 'json',
		colModel : [ {
			display : 'Código',
			name : 'codigo',
			width : 70,
			sortable : false,
			align : 'left'
		}, {
			display : 'Produto',
			name : 'produto',
			width : 150,
			sortable : false,
			align : 'left'
		}, {
			display : 'Edição',
			name : 'edicao',
			width : 80,
			sortable : false,
			align : 'center'
		}, {
			display : 'Recolhimento',
			name : 'recolhimento',
			width : 80,
			sortable : false,
			align : 'center'
		}, {
			display : 'Preço Capa R$',
			name : 'precoCapa',
			width : 80,
			sortable : false,
			align : 'right'
		}, {
			display : 'Desconto R$',
			name : 'precoDesconto',
			width : 100,
			sortable : false,
			align : 'right'
		}, {
			display : 'Exemplares',
			name : 'exemplares',
			width : 100,
			sortable : false,
			align : 'center'
		}, {
			display : 'Total R$',
			name : 'total',
			width : 90,
			sortable : false,
			align : 'right'
		}, {
			display : 'Juramentada',
			name : 'juramentada',
			width : 80,
			sortable : false,
			align : 'center'
		} ],
		width : 960,
		height : 180
	});

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
	
	$("#numeroCota").keypress(function(e) {
		
		if (e.keyCode == 13) {
			
			ConferenciaEncalheCont.pesquisarCota();
		}
	});
});

shortcut.add("F2", function() {
	popup_novo_encalhe();
});

shortcut.add("F6", function() {
	popup_finalizar_conferencia();
});

shortcut.add("F8", function() {
	popup_outros_valores();
});

shortcut.add("F9", function() {
	popup_salvarInfos();
});