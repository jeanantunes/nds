var ajusteReparteController = $.extend(true, {

init : function() {
		
	$("#dataInicio").datepicker({
		showOn: "button",
		minDate      : new Date(),
		buttonImage: contextPath + "/images/calendar.gif",
		buttonImageOnly: true
	});
	
	$("#dataFim").datepicker({
		showOn: "button",
		minDate      : new Date(),
		buttonImage: contextPath + "/images/calendar.gif",
		buttonImageOnly: true
	});
	
	$("#AJUSTE_HISTORICO_input").mask("9.9"); 
	$("#AJUSTE_ENCALHE_MAX_input").mask("99.9");
	$("#AJUSTE_ENCALHE_MAX_input").val("50.0");
/*
	$(".lstSegmentosGrid").flexigrid({		
		preProcess : ajusteReparteController.executarPreProcessSegmento,
		dataType : 'json',
		colModel : [ {
			display : 'Segmento',
			name : 'descricao',
			width : 285,
			sortable : true,
			align : 'left'
		}, 
		{
			display : '',
			name : 'valor',
			width : 70,
			sortable : true,
			align : 'center'
		}],
		width : 400,
		height : 200
	});
	*/
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
			name : 'statusCota',
			width : 40,
			sortable : true,
			align : 'left'
		}, {
			display : 'Forma Ajuste',
			name : 'formaAjusteAplicado',
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
			name : 'motivoAjusteAplicado',
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
			
			return resultado;
		}

		$.each(resultado.rows, function(index, row) {
			
			var editar = '<a href="javascript:;" onclick="ajusteReparteController.editarAjuste('+row.cell.idAjusteReparte+');" style="cursor:pointer">' +
							   	 '<img title="Lançamentos da Edição" src="' + contextPath + '/images/ico_editar.gif" hspace="5" border="0px" />' +
							   '</a>';
			var excluir = '<a href="javascript:;" onclick="ajusteReparteController.excluirAjuste('+row.cell.idAjusteReparte+');" style="cursor:pointer">' +
		   	 			'<img title="Lançamentos da Edição" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
		   	 			'</a>';
			
			row.cell.acao = editar + excluir;
		});
		
		$(".grids", ajusteReparteController.workspace).show();
		
		return resultado;
	},

	/*
	// Preprocess do Segmento
	executarPreProcessSegmento : function(resultado){
		
		if (resultado.mensagens) {
			exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
			);
			
			return resultado;
		}
		
		$(".grids", ajusteReparteController.workspace).show();
		
		return resultado;
	},

	*/

	//FUNCTIONS
	
	// FUNCTION NOVO AJUSTE	
	incluirAjuste : function() {
		ajusteReparteController.limparPopUp();
		
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
					
					$(".cotasAjusteGrid").flexReload();
					
					ajusteReparteController.limparPopUp();
				},
				"Cancelar": function() {
					$(this).dialog( "close" );
					$(".cotasAjusteGrid").flexReload();
					ajusteReparteController.limparPopUp();
				}
			}
		});
	},
	
	
	
//	ARRAY DE PARAMETROS	 
		 
	getDados : function() {
		  
		  var data = [];
		  
		  data.push({name:"ajusteDTO.numeroCota",  value: ajusteReparteController.get("numeroCota")});
		  data.push({name:"ajusteDTO.nomeCota",  value: ajusteReparteController.get("nomeCota")});
		  data.push({name:"ajusteDTO.formaAjuste",  value: ajusteReparteController.getRadio()});
		  data.push({name:"ajusteDTO.ajusteAplicado", value: ajusteReparteController.getAjusteAplicado()});
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
		 
//	Pegar Valor Radio
		 
	getRadio : function (){
		var valRadio = $('input:radio[name=formaAjuste]:checked').val();
		return valRadio;
	},
	
	getAjusteAplicado : function (){
		var valElemento = $("#"+ajusteReparteController.getRadio()+"_input").val();
		return valElemento;
	},
	
	
//	FUNCTION PARA LIMPAR POPUP
	limparPopUp : function (){
		$("#numeroCota").val("").enable();
		$("#nomeCota").val("").enable();
		$("#formaAjuste").val("");
		$("#ajusteAplicado").val("");
		$("#motivoAjuste").val("");
		$("#dataInicio").val("");
		$("#dataFim").val("");
		$("#"+ajusteReparteController.getRadio()+"_input").val("");
	},
	
	
	formatarAjusteAplicadoHistorico : function (){
	var indiceAjuste = $("#AJUSTE_HISTORICO_input").val();
    if(indiceAjuste < 0.5 || indiceAjuste > 1.5){        
           var erros = new Array();
           erros[0] = "O Índice deve estar entre 0.5 e 1.5.";
           exibirMensagemDialog('WARNING',   erros,"");                
           $("#AJUSTE_HISTORICO_input").val("");
           return;
    }
},

	formatarAjusteAplicadoEncalhe : function (){
	var indiceAjuste = $("#AJUSTE_ENCALHE_MAX_input").val();
    if(indiceAjuste < 1 || indiceAjuste > 50){        
           var erros = new Array();
           erros[0] = "O Índice deve estar entre 1.0 e 50.";
           exibirMensagemDialog('WARNING',   erros,"");                
           $("#AJUSTE_ENCALHE_MAX_input").val("");
           return;
    }
},
	
	
	editarAjuste : function(idAjusteReparte) {
		ajusteReparteController.limparPopUp();
		$.postJSON(contextPath + "/distribuicao/ajusteReparte/buscarAjustePorId", 
				{id:idAjusteReparte},
				function(result){
					ajusteReparteController.popularPopUpEditar(result);						
		});

		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:'auto',
			width:630,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
					var data = ajusteReparteController.getDados();
					data.push({name: 'id', value:idAjusteReparte});
					
					$.postJSON(contextPath + "/distribuicao/ajusteReparte/alterarAjuste", data);
					
					$(".cotasAjusteGrid", ajusteReparteController.workspace).flexReload();
					
					closest('.cotasAjusteGrid');
					ajusteReparteController.limparPopUp();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
					$(".cotasAjusteGrid", ajusteReparteController.workspace).flexReload();
					ajusteReparteController.limparPopUp();
				}
			}
		});
	},

// Function para popular o popUp de editar
	
	popularPopUpEditar : function(result){
		$("#numeroCota").val(result.numeroCota).disable();
		$("#nomeCota").val(result.nomeCota).disable();
//		$("#formaAjuste").val(ajusteReparteController.getRadio());
		$("#motivoAjuste").val(result.motivoAjuste);
		$("#dataInicio").val(result.dataInicio);
		$("#dataFim").val(result.dataFim);
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
		
//		$(".lstSegmentosGrid", this.workspace).flexOptions({
//			url: contextPath + "/distribuicao/ajusteReparte/carregarSegmento",
//			dataType : 'json'
//		});
//		$(".lstSegmentosGrid", this.workspace).flexReload();
		
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
	
	excluirAjuste : function(idAjusteReparte) {

		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					$.postJSON(contextPath + "/distribuicao/ajusteReparte/excluirAjuste", 
							{id:idAjusteReparte},
							function(result) {
									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										exibirMensagem(tipoMensagem, listaMensagens);
									}
					                 
					                 $(".cotasAjusteGrid").flexReload();
							   },
							   null,
							   true
					);
					$("#dialog-excluir").dialog("close");
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

