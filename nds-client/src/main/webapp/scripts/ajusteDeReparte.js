var ajusteReparteController = $.extend(true, {

	
	init : function() {
	$(".lstSegmentosGrid").flexigrid({
		url : '../xml/segmentosLst-xml.xml',
		dataType : 'xml',
		colModel : [ {
			display : 'Segmento',
			name : 'segmento',
			width : 285,
			sortable : true,
			align : 'left'
		}, {
			display : '',
			name : 'valor',
			width : 70,
			sortable : true,
			align : 'center'
		}],
		width : 400,
		height : 200
	});
	
	
$(".cotasAjusteGrid").flexigrid({
		url : '../xml/cotasAjusteGrid-xml.xml',
		dataType : 'xml',
		colModel : [ {
			display : 'Cota',
			name : 'cota',
			width : 40,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nome',
			name : 'Nome',
			width : 100,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nome do PDV',
			name : 'pdv',
			width : 100,
			sortable : true,
			align : 'left'
		}, {
			display : 'Status',
			name : 'status',
			width : 40,
			sortable : true,
			align : 'left'
		}, {
			display : 'Forma Ajuste',
			name : 'formaAjuste',
			width : 70,
			sortable : true,
			align : 'left'
		}, {
			display : 'Ajuste<br/>Aplicado',
			name : 'ajusteAplicado',
			width : 40,
			sortable : true,
			align : 'right'
		}, {
			display : 'Dt. Inicio',
			name : 'dataInicio',
			width : 55,
			sortable : true,
			align : 'center'
		}, {
			display : 'Dt. Final',
			name : 'dataFinal',
			width : 69,
			sortable : true,
			align : 'center'
		}, {
			display : 'Motivo',
			name : 'motivo',
			width : 90,
			sortable : true,
			align : 'left'
		}, {
			display : 'Usuário',
			name : 'usuario',
			width : 55,
			sortable : true,
			align : 'left'
		}, {
			display : 'Data',
			name : 'data',
			width : 50,
			sortable : true,
			align : 'center'
		}, {
			display : 'Hora',
			name : 'hora',
			width : 30,
			sortable : true,
			align : 'center'
		},  {
			display : 'Ação',
			name : 'acao',
			width : 42,
			sortable : true,
			align : 'center'
		}],
		sortname : "cota",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 290
	});
	},

	
	incluirAjuste : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:'auto',
			width:630,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};		
	//MODIFICA TODOS FUNCTIONS
	function editarAjuste() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:'auto',
			width:630,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	function mostrarSegmentos() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$( "#dialog-segmentos" ).dialog({
			resizable: false,
			height:'auto',
			width:450,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );	
					$('.ajusteSeg').show();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	function excluirAjuste() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};

	function porCota(){
		$('.porCota').show();
		$('.porSegmento').hide();
	}
	function porSegmento(){
		$('.porCota').hide();
		$('.porSegmento').show();
	}
	function filtroPorCota(){
		$('.filtroPorCota').show();
		$('.filtroPorSegmento').hide();
		$('.porSegmento').hide();
	}
	function filtroPorSegmento(){
		$('.filtroPorCota').hide();
		$('.filtroPorSegmento').show();
		$('.porCota').hide();
	}
	$(function() {		
			$( "#periodo1" ).datepicker({
				showOn: "button",
				buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true
			});
			$( "#periodo2" ).datepicker({
				showOn: "button",
				buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true
			});
		});
	
}, BaseController);
//@ sourceURL=regiao.js

