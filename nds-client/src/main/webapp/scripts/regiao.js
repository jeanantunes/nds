function incluirNovo() {
	//$( "#dialog:ui-dialog" ).dialog( "destroy" );

	$("#dialog-novo").dialog({
		resizable : false,
		height : 400,
		width : 650,
		modal : true,
		buttons : {
			"Confirmar" : function() {
				$(this).dialog("close");

				$("#effect").show("highlight", {}, 1000, callback);
			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});
};

function addCotas() {
	//$( "#dialog:ui-dialog" ).dialog( "destroy" );

	$("#dialog-cotas").dialog({
		resizable : false,
		height : 550,
		width : 650,
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
};

function addNovaRegiao() {
	//$( "#dialog:ui-dialog" ).dialog( "destroy" );

	$("#dialog-addRegiao").dialog({
		resizable : false,
		height : 'auto',
		width : 350,
		modal : true,
		buttons : {
			"Confirmar" : function() {
				$(this).dialog("close");
				mostrar();
				$("#effect").show("highlight", {}, 1000, callback);
			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});
};

function regiaoAutomatica() {
	//$( "#dialog:ui-dialog" ).dialog( "destroy" );

	$("#dialog-regiaoAutomatica").dialog({
		resizable : false,
		height : 400,
		width : 400,
		modal : true,
		buttons : {
			"Confirmar" : function() {
				$(this).dialog("close");
				$("#effect").show("highlight", {}, 1000, callback);
			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});
};

function excluirRegiao() {
	//$( "#dialog:ui-dialog" ).dialog( "destroy" );

	$("#dialog-excluir").dialog({
		resizable : false,
		height : 170,
		width : 380,
		modal : true,
		buttons : {
			"Confirmar" : function() {
				$(this).dialog("close");
				$("#effect").show("highlight", {}, 1000, callback);
			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});
};

function add_lote() {
	$("#dialog-lote").dialog({
		resizable : false,
		height : 320,
		width : 190,
		modal : true,
		buttons : {
			"Confirmar" : function() {
				$(this).dialog("close");
				$("#effect").show("highlight", {}, 1000, callback);
			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});

};

function add_produtos() {
	$("#dialog-addNMaiores").dialog({
		resizable : false,
		height : 520,
		width : 645,
		modal : true,
		buttons : {
			"Confirmar" : function() {
				$(this).dialog("close");

				$("#effect").show("highlight", {}, 1000, callback);
			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});

};

function add_cotas() {
	$("#dialog-pesqCotas").dialog({
		resizable : false,
		height : 520,
		width : 645,
		modal : true,
		buttons : {
			"Confirmar" : function() {
				$(this).dialog("close");
				$("#effect").show("highlight", {}, 1000, callback);
			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});

};

function filtroPorCep() {
	$('.porCep').show();
	$('.porSegmento').hide();

	$('.gridfaixaCep').hide();
	$('.gridNMaiores').hide();
	$('.gridsegmentos').hide();
}

function filtroPorNMaiores() {
	$('.porCep').hide();
	$('.porSegmento').hide();

	$('.gridfaixaCep').hide();
	$('.gridNMaiores').show();
	$('.gridsegmentos').hide();
}

function filtroPorSegmento() {
	$('.porCep').hide();
	$('.porSegmento').show();

	$('.gridfaixaCep').hide();
	$('.gridNMaiores').hide();
	$('.gridsegmentos').hide();
}

function mostrarPorCep() {
	$('.gridfaixaCep').show();
	$('.gridNMaiores').hide();
	$('.gridsegmentos').hide();
}

function mostrarPorNMaiores() {
	$('.gridfaixaCep').hide();
	$('.gridNMaiores').show();
	$('.gridsegmentos').hide();
}

function mostrarPorSegmento() {
	$('.gridfaixaCep').hide();
	$('.gridNMaiores').hide();
	$('.gridsegmentos').show();
}

function popup_detalhes() {
	//$( "#dialog:ui-dialog" ).dialog( "destroy" );

	$("#dialog-detalhes").dialog({
		resizable : false,
		height : 'auto',
		width : 'auto',
		modal : false,
	});
};

function popup_detalhes_close() {
	$("#dialog-detalhes").dialog("close");

}

function add_cotas_grid() {
	$('.cotasRegiaoGrid #row5').show();
}

function remove_cotas_grid() {
	$('.cotasRegiaoGrid #row5').hide();
}