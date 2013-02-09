var ajusteReparteController = $.extend(true, {

	
	init : function() {
		
	$("#dataInicio").datepicker({
		showOn: "button",
		buttonImage: contextPath + "/images/calendar.gif",
		buttonImageOnly: true
	});
	
	$("#dataFim").datepicker({
		showOn: "button",
		buttonImage: contextPath + "/images/calendar.gif",
		buttonImageOnly: true
	});
		
	$(".lstSegmentosGrid").flexigrid({		
		preProcess : ajusteReparteController.ghomma,
		dataType : 'json',
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
		preProcess : ajusteReparteController.executarPreProcessCotasAjuste,
		dataType : 'json',
		colModel : [ {
			display : 'Cota',
			name : 'numeroCota',
			width : 40,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nome',
			name : 'nomeCota',
			width : 100,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nome do PDV',
			name : 'nomePDV',
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
			name : 'dataFim',
			width : 69,
			sortable : true,
			align : 'center'
		}, {
			display : 'Motivo',
			name : 'motivoAjuste',
			width : 90,
			sortable : true,
			align : 'left'
		}, {
			display : 'Usuário',
			name : 'nomeUsuario',
			width : 55,
			sortable : true,
			align : 'left'
		}, {
			display : 'Data',
			name : 'dataAlteracao',
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
	ajusteReparteController.carregarCotasEmAjuste();
	},
	
	
	// PREPROCESS
	
	// Preprocess do faixaGrid
	executarPreProcessCotasAjuste : function(resultado){
		
		if (resultado.mensagens) {
			exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
			);
			
			$(".grids", ajusteReparteController.workspace).hide();
			
			return resultado;
		}
		
		$(".grids", ajusteReparteController.workspace).show();
		
		return resultado;
	},
	

	//FUNCTIONS
	// FUNCTION NOVO AJUSTE
	
	incluirAjuste : function() {

		$("#dialog-novo").dialog({
			resizable: false,
			height:'auto',
			width:630,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$(this).dialog( "close" );
					
					var data = ajusteReparteController.getDados();				
					
					$.postJSON(contextPath + "/distribuicao/ajusteReparte/novoAjuste", data);
				},
				"Cancelar": function() {
					$(this).dialog( "close" );
				}
			}
		});
	},
	
	
	
//	ARRAY DE PARAMETROS	 
		 
	getDados : function() {
		  
		  var data = [];
		  
		  data.push({name:"ajusteDTO.numeroCota",  value: ajusteReparteController.get("numeroCota")});
//		  data.push({name:"ajusteDTO.nomeCota",  value: ajusteReparteController.get("nomeCota")});
//		  data.push({name:"ajusteDTO.formaAjuste",  value: ajusteReparteController.get("formaAjuste")});
		  data.push({name:"ajusteDTO.formaAjuste",  value: ajusteReparteController.getRadio()});
		  data.push({name:"ajustedto.ajusteAplicado",  value: ajusteReparteController.get("ajusteAplicado")});
		  data.push({name:"ajusteDTO.motivoAjuste",  value: ajusteReparteController.get("motivoAjuste")});
		  data.push({name:"ajusteDTO.dataInicioCadastro",  value: ajusteReparteController.get("dataInicio")});
		  data.push({name:"ajusteDTO.dataFimCadastro",  value: ajusteReparteController.get("dataFim")});
		  
		  return data;
		 },
	
//	PEGANDO O VALOR DOS CAMPOS	 
		 
	 get : function(campo) {
			  
		  var elemento = $("#" + campo);
		  
		  if(elemento.attr('type') == 'checkbox') {
		   return (elemento.attr('checked') == 'checked') ;
		  } else {
		   return elemento.val();
		  }
			  
		 },
		 
//		 Pegar Valor Radio
		 
	getRadio : function (){
		var valRadio = $('input:radio[name=formaAjuste]:checked').val();
		return valRadio;
	},
	
	editarAjuste : function() {
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
	},
	
	
//	CARREGAR COTAS QUE ESTÃO EM AJUSTE
	
	carregarCotasEmAjuste : function() {
		
		$(".cotasAjusteGrid", this.workspace).flexOptions({
			url: contextPath + "/distribuicao/ajusteReparte/buscarCotaAjuste",
			dataType : 'json'
		});
		$(".cotasAjusteGrid", this.workspace).flexReload();		
		
	},
	
	
	mostrarSegmentos : function() {
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
	},
	
	excluirAjuste : function() {
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
	},
	
	carregarMotivo : function() {
		$(".segmentosGrid", this.workspace).flexOptions({
			url: contextPath + "/distribuicao/ajusteReparte/carregarComboMotivoStatusCota",
			dataType : 'json',
		});
	},
	
	porCota : function(){
		$('.porCota').show();
		$('.porSegmento').hide();
	},
	
	porSegmento : function(){
		$('.porCota').hide();
		$('.porSegmento').show();
	},
	
	filtroPorCota : function(){
		$('.filtroPorCota').show();
		$('.filtroPorSegmento').hide();
		$('.porSegmento').hide();
	},
	
	filtroPorSegmento : function(){
		$('.filtroPorCota').hide();
		$('.filtroPorSegmento').show();
		$('.porCota').hide();
	},
	
	filtroPorHistorico : function(){
		$('.filtroPorCota').hide();
		$('.porHistorico').show();
		$('.porCota').hide();
	},
	
	/*
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
		*/
	
}, BaseController);
//@ sourceURL=ajusteReparte.js

