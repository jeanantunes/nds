var ajusteReparteController = $.extend(true, {

	
init : function() {
	
	$('#formaAjusteAjusteSegmento').click(function(){
		ajusteReparteController.filtroPorSegmento();
	});
	
	disabledEnterModalConfirmar.push('dialog-novo-ajusteReparte');
	
	$('#ajuste-reparte-numeroCota', ajusteReparteController.workspace).bind({
		keyup: function(){
			onlyNumeric(event);
		},

		change: function(){
			pesquisaCota.pesquisarPorNumeroCota('#ajuste-reparte-numeroCota', '#ajuste-reparte-nomeCota');
		}
	});
	
	ajusteReparteController.mask();

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
	
	var isAjusteSegmento=false;
	
	var closeDialogPopUpSegmento1=true;
	var closeDialogPopUpSegmento2=true;
	var closeDialogPopUpSegmento3=true;
	
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
			
			var editar = '<a href="javascript:;"  onclick="ajusteReparteController.editarAjuste('+row.cell.idAjusteReparte+');" style="cursor:pointer">' +
							   	 '<img title="Alterar" src="' + contextPath + '/images/ico_editar.gif" hspace="5" border="0px" />' +
							   '</a>';
			var excluir = '<a href="javascript:;" isEdicao="true" onclick="ajusteReparteController.excluirAjuste('+row.cell.idAjusteReparte+');" style="cursor:pointer">' +
		   	 			'<img title="Excluir ajuste" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
		   	 			'</a>';
			
			row.cell.acao = editar + excluir;
			
			if((row.cell.formaAjusteAplicado == "Venda Média") || (row.cell.formaAjusteAplicado == "Encalhe Máximo")){
				row.cell.ajusteAplicado = parseFloat(row.cell.ajusteAplicado).toFixed(0); 
			}
			
			if((row.cell.formaAjusteAplicado == "Segmento") || (row.cell.formaAjusteAplicado == "Histórico")){
				row.cell.ajusteAplicado = parseFloat(row.cell.ajusteAplicado).toFixed(1); 
			}
			
		});
		
		$(".grids", ajusteReparteController.workspace).show();
		
		return resultado;
	},


	//FUNCTIONS

	// FUNCTION NOVO AJUSTE	
	incluirAjuste : function() {
		ajusteReparteController.limparPopUp();
		
		$("#tableSegmentos").hide();
		
		$("#dialog-novo-ajusteReparte").dialog({
			resizable: false,
			height:'auto',
			width:630,
			modal: true,
			buttons: {
				"Confirmar": function() {
					ajusteReparteController.tipoIncluir();
					
				},
				"Cancelar": function() {
					$(this).dialog( "close" );
					ajusteReparteController.limparPopUp();
				}
			}
		});
	},
	
	
	tipoIncluir : function () {
		
		var data = ajusteReparteController.getDados();

		if (ajusteReparteController.isAjusteSegmento == true){
			if(ajusteReparteController.get("segmento1") != ""){
				data.push({name:"ajustes", value: $('#segmento1').val().replace('.',',')});
				data.push({name:"segmentos[0].id", value: ajusteReparteController.get("tipoSegmento1")});
			}
			if(ajusteReparteController.get("segmento2") != ""){
				data.push({name:"ajustes", value: $('#segmento2').val().replace('.',',')});
				data.push({name:"segmentos[1].id", value: ajusteReparteController.get("tipoSegmento2")});
			}
			if(ajusteReparteController.get("segmento3") != ""){
				data.push({name:"ajustes", value: $('#segmento3').val().replace('.',',')});
				data.push({name:"segmentos[2].id", value: ajusteReparteController.get("tipoSegmento3")});
			}
			
			$.postJSON(contextPath + "/distribuicao/ajusteReparte/incluirAjusteSegmento", 
					data,
					function(result) {
							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							
							if (tipoMensagem && listaMensagens) {
								exibirMensagem(tipoMensagem, listaMensagens);
							}
			                 $(".cotasAjusteGrid").flexReload();
			                 $("#dialog-novo-ajusteReparte").dialog("close");
					   },
					   function(result) {
							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							
							if (tipoMensagem && listaMensagens) {
								exibirMensagem(tipoMensagem, listaMensagens);
							}
					   },
					   true
			);
			
		}else{
			var data = ajusteReparteController.getDados();				
			
			$.postJSON(contextPath + "/distribuicao/ajusteReparte/novoAjuste", 
					data,
					function(result) {
							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							
							if (tipoMensagem && listaMensagens) {
								exibirMensagem(tipoMensagem, listaMensagens);
							}
			                 $(".cotasAjusteGrid").flexReload();
			                 $("#dialog-novo-ajusteReparte").dialog("close");
					   },
					   function(result) {
							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							
							if (tipoMensagem && listaMensagens) {
								exibirMensagem(tipoMensagem, listaMensagens);
							}
					   },
					   true
			);
		}
		
	},
	
	mensagemErro : function (mensagem){
		var erros = new Array();
        erros[0] = ""+mensagem;
        exibirMensagemDialog('WARNING',   erros,"");
        return;
	},
	
	//ARRAY DE PARAMETROS	 
	getDados : function() {
		  
		  var data = [];
		  var isValid = true;
		  
		  if(ajusteReparteController.get("ajuste-reparte-numeroCota") != ""){
			  data.push({name:"ajusteDTO.numeroCota",  value: ajusteReparteController.get("ajuste-reparte-numeroCota")});
		  }else{
			  ajusteReparteController.mensagemErro("Numero da cota não pode ser vazio.");
			  isValid = false;
		  }
		  
		  if(ajusteReparteController.get("ajuste-reparte-nomeCota") != ""){
			  data.push({name:"ajusteDTO.nomeCota",  value: ajusteReparteController.get("ajuste-reparte-nomeCota")});
		  }else{
			  ajusteReparteController.mensagemErro("Nome da cota não pode ser vazio.");
			  isValid = false;
		  }
		  
		  if(isValid){
			  
		  data.push({name:"ajusteDTO.formaAjuste",  value: ajusteReparteController.getRadio()});
		  data.push({name:"ajusteDTO.motivoAjuste",  value: ajusteReparteController.get("motivoAjuste")});
		  data.push({name:"ajusteDTO.dataInicioCadastro",  value: ajusteReparteController.get("dataInicioAjusteReparte")});
		  data.push({name:"ajusteDTO.dataFimCadastro",  value: ajusteReparteController.get("dataFimAjusteReparte")});
		  
			  if(ajusteReparteController.getRadio() == "AJUSTE_SEGMENTO"){
				  this.isAjusteSegmento = true;
			  }else{
				  data.push({name:"ajusteDTO.ajusteAplicado", value: ajusteReparteController.getAjusteAplicado()});
				  this.isAjusteSegmento = false;
			  }
		  }
		  return data;
		 },
		 
	 getDadosEditar : function() {
		  
		  var data = [];
		  
		  data.push({name:"ajusteDTO.numeroCota",  value: ajusteReparteController.get("numeroCotaEditar")});
		  data.push({name:"ajusteDTO.nomeCota",  value: ajusteReparteController.get("nomeCotaEditar")});
		  data.push({name:"ajusteDTO.formaAjuste",  value: ajusteReparteController.getRadioEditar()});
		  data.push({name:"ajusteDTO.motivoAjuste",  value: ajusteReparteController.get("motivoAjusteEditar")});
		  data.push({name:"ajusteDTO.dataInicioCadastro",  value: ajusteReparteController.get("dataInicioEditar")});
		  data.push({name:"ajusteDTO.dataFimCadastro",  value: ajusteReparteController.get("dataFimEditar")});
		  
			  if(ajusteReparteController.getRadioEditar() == "AJUSTE_SEGMENTO"){
				  this.isAjusteSegmento = true;
			  }else{
				  this.isAjusteSegmento = false;
			  }
		  return data;
		 },
		 
	
	 //PEGANDO O VALOR DOS CAMPOS	 
		 
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
	
	
	getRadioEditar : function (){
		var valRadio = $('input:radio[name=formaAjusteEditar]:checked').val();
		
		return valRadio;
	},
	
	
	getAjusteAplicado : function (){
		
		var radio = ajusteReparteController.getRadio();
		var valElemento;
		
		if(radio != "ajuste_segmento"){
			valElemento = $("#"+radio+"_input").val().replace('.',',');
		}else{
			valElemento = "Segmento";
		}
		
		return valElemento;
	},
	
	getAjusteAplicadoEditar : function (){
		var valElemento = $("#"+ajusteReparteController.getRadioEditar()+"_editar_input").val().replace('.',',');
		
		return valElemento;
	},
	
	
//	FUNCTION PARA LIMPAR POPUP
	limparPopUp : function (){
		$("#ajuste-reparte-numeroCota").val("").enable();
		$("#ajuste-reparte-nomeCota").val("").enable();
		$("AJUSTE_HISTORICO_input").val("");
		$("#segmento1").val("");
		$("#segmento2").val("").show();
		$("#segmento3").val("").show();
		$("#tipoSegmento1").val("");
		$("#tipoSegmento2").val("").show();
		$("#tipoSegmento3").val("").show();
		$("#motivoAjuste").val("");
		$("#dataInicioAjusteReparte").val("");
		$("#dataFimAjusteReparte").val("");
		
		$('#AJUSTE_HISTORICO').attr('checked', false);
		$('#AJUSTE_VENDA_MEDIA').attr('checked', false);
		$('#AJUSTE_ENCALHE_MAX').attr('checked', false);
		$('#formaAjusteAjusteSegmento').attr('checked', false);
		$("#AJUSTE_HISTORICO_input").hide();
		$("#AJUSTE_VENDA_MEDIA_input").hide();
		$("#AJUSTE_ENCALHE_MAX_input").hide();
		
		ajusteReparteController.mask();
	},
	
	limparPopUpEditar : function (){
		$("#numeroCotaEditar").val("").enable();
		$("#nomeCotaEditar").val("").enable();
		$("#segmento1").val("");
		$("#segmento2").val("").show();
		$("#segmento3").val("").show();
		$("#tipoSegmento1").val("");
		$("#tipoSegmento2").val("").show();
		$("#tipoSegmento3").val("").show();
		$("#motivoAjusteEditar").val("");
		$("#dataInicioEditar").val("");
		$("#dataFimEditar").val("");
		
		$("#AJUSTE_HISTORICO_editar_input").val("").hide();
		$("#AJUSTE_VENDA_MEDIA_editar_input").val("").hide();
		$("#AJUSTE_ENCALHE_MAX_editar_input").val("").hide();
	
	},
	
	
	editarAjuste : function(idAjusteReparte) {
		
		$( "#dialog-editar" ).dialog({
			resizable: false,
			height:'auto',
			width:630,
			modal: true,
			open: function (){
				$("#tableSegmentosEditar").hide();
				
				$.postJSON(contextPath + "/distribuicao/ajusteReparte/buscarAjustePorId", 
						{id:idAjusteReparte},
						function(result){
							
							if (result.formaAjuste == "AJUSTE_SEGMENTO"){
								$('#formaAjusteAjusteSegmento_editar').attr('checked', true);
								ajusteReparteController.popularPopUpEditarSegmento(result);
								ajusteReparteController.filtroPorSegmentoEditar();
							}else{
								ajusteReparteController.limparPopUpEditar();
								ajusteReparteController.popularPopUpEditar(result);
							}
				});
			},
			
			buttons: {
				"Confirmar": function() {
					var data = ajusteReparteController.getDadosEditar();

					data.push({name: 'id', value:idAjusteReparte});

					if (ajusteReparteController.isAjusteSegmento == true){
						
						if(ajusteReparteController.get("segmento1") != ""){
							data.push({name:"ajuste", value: $('#segmento1').val().replace('.',',')});
							data.push({name:"segmentos[0].id", value: ajusteReparteController.get("tipoSegmento1")});
						}else{
							 var erros = new Array();
					           erros[0] = "Insira um índice para ajuste";
					           exibirMensagemDialog('WARNING',   erros,"");
					           return;
						}
						
						if(ajusteReparteController.isAjusteSegmento == true){
							var indice = ajusteReparteController.get("segmento1");
							
							if(indice < 0.5 || indice > 1.5){
								var erros = new Array();
					           erros[0] = "Valor inválido para o ajuste!";
					           exibirMensagemDialog('WARNING',   erros,"");
					           return;
							}
						}
						
						$.postJSON(contextPath + "/distribuicao/ajusteReparte/alterarAjusteSegmento", 
								data,
								function(result) {
										var tipoMensagem = result.tipoMensagem;
										var listaMensagens = result.listaMensagens;
										
										if (tipoMensagem && listaMensagens) {
											exibirMensagem(tipoMensagem, listaMensagens);
										}
										$(".cotasAjusteGrid", ajusteReparteController.workspace).flexReload();
								},
									function(result) {
										var tipoMensagem = result.tipoMensagem;
										var listaMensagens = result.listaMensagens;
										
										if (tipoMensagem && listaMensagens) {
											exibirMensagem(tipoMensagem, listaMensagens);
										}
								   },
								   true
						);
						$( this ).dialog( "close" );
					
					}else{
						
						var ajusteAplicado = ajusteReparteController.getAjusteAplicadoEditar(); 
						
						if (ajusteAplicado){
							
						data.push({name:"ajusteDTO.ajusteAplicado", value: ajusteAplicado });
						
						$.postJSON(contextPath + "/distribuicao/ajusteReparte/alterarAjuste", 
								data,
								function(result) {
										var tipoMensagem = result.tipoMensagem;
										var listaMensagens = result.listaMensagens;
										
										if (tipoMensagem && listaMensagens) {
											exibirMensagem(tipoMensagem, listaMensagens);
										}
										$(".cotasAjusteGrid", ajusteReparteController.workspace).flexReload();
								},
								function(result) {
									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										exibirMensagem(tipoMensagem, listaMensagens);
									}
							   },
							   true
						);
						$( this ).dialog( "close" );
						
						}else{
						   var erros = new Array();
				           erros[0] = "Insira um índice para ajuste";
				           exibirMensagemDialog('WARNING',   erros,"");
				           return;
						}
					}
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
					$(".cotasAjusteGrid", ajusteReparteController.workspace).flexReload();
					ajusteReparteController.limparPopUpEditar();
				}
			}
		});
	},
	
// Function para popular o popUp de editar
	
	popularPopUpEditar : function(result){
		
		$("#numeroCotaEditar").val(result.numeroCota).disable();
		$("#nomeCotaEditar").val(result.nomeCota).disable();
		$("#motivoAjusteEditar").val(result.motivoAjuste);
		$("#dataInicioEditar").val(result.dataInicio);
		$("#dataFimEditar").val(result.dataFim);
		
		$('#'+result.formaAjuste+'_editar').attr('checked', true);
		
		$('#AJUSTE_ENCALHE_MAX_editar_input').hide();
		$('#AJUSTE_VENDA_MEDIA_editar_input').hide();
		$('#AJUSTE_HISTORICO_editar_input').hide();
		
		
		if((result.formaAjuste == "AJUSTE_ENCALHE_MAX")){
			$('#'+result.formaAjuste+'_editar_input').show().val(parseFloat(result.ajusteAplicado).toFixed(0));
		}else{
			$('#'+result.formaAjuste+'_editar_input').show().val(parseFloat(result.ajusteAplicado).toFixed(1));
		}
		
		if(result.formaAjuste == "AJUSTE_VENDA_MEDIA"){
			ajusteReparteController.carregarVendaMedia("AJUSTE_VENDA_MEDIA_editar_input");
		}
		
		$("#tipoSegmento1").show();
		$("#tipoSegmento2").hide();
		$("#tipoSegmento3").hide();
		$("#segmento1").show();
		$("#segmento2").hide();
		$("#segmento3").hide();
	},
	
	
	popularPopUpEditarSegmento : function(result){
		$("#numeroCotaEditar").val(result.numeroCota).disable();
		$("#nomeCotaEditar").val(result.nomeCota).disable();
		$("#motivoAjusteEditar").val(result.motivoAjuste);
		$("#dataInicioEditar").val(result.dataInicio);
		$("#dataFimEditar").val(result.dataFim);
		
		$("#tipoSegmento1").val(result.idSegmento);
		$("#segmento1").val(parseFloat(result.ajusteAplicado).toFixed(1));
		
		$("#exibirSegmento1Editar").val(result.ajusteAplicado).disable();
	
		$("#segmento2").hide();
		$("#tipoSegmento2").hide();
		$("#segmento3").hide();
		$("#tipoSegmento3").hide();
	},
	
//	CARREGAR COTAS QUE ESTÃO EM AJUSTE
	
	carregarCotasEmAjuste : function() {
		
		$(".cotasAjusteGrid", this.workspace).flexOptions({
			url: contextPath + "/distribuicao/ajusteReparte/buscarCotaAjuste",
			dataType : 'json'
		});
		$(".cotasAjusteGrid", this.workspace).flexReload();		
		
	},
	
	popularSegmentos : function(result){
		switch (result) {
		case 0:
			ajusteReparteController.filtroPorSegmentoQtd_0();
			break;
		case 1:
			ajusteReparteController.filtroPorSegmentoQtd_1();
			break;
		case 2:
			ajusteReparteController.filtroPorSegmentoQtd_2();
			break;
		case 3:
			ajusteReparteController.filtroPorSegmentoQtd_3();
				exibirMensagemDialog("WARNING",["A cota atingiu o limite de ajustes, já recebeu 3 ajustes por Segmento"]);
		break;
		}
	},
	
	mostrarSegmentos : function() {
		var numeroCota = $("#ajuste-reparte-numeroCota").val();
		
		$.postJSON(contextPath + "/distribuicao/ajusteReparte/qtdAjustesSegmento", 
				{nmCota:numeroCota},
				function(result){
					ajusteReparteController.popularSegmentos(result);						
		});
		
		$( "#dialog-segmentos" ).dialog({
			resizable: false,
			height:'auto',
			width:450,
			modal: true,
			close: function(){
				$('#formaAjusteAjusteSegmento').attr('checked', true);
			},
			buttons: {
				"Confirmar": function() {
					ajusteReparteController.validarPopUpDeSegmentos();
					
				},
				"Cancelar": function() {
					ajusteReparteController.validarPopUpDeSegmentos();
				}
			}
		});
	},
	
	validarPopUpDeSegmentos : function(){
		
		this.closeDialogPopUpSegmento1 = true;
		this.closeDialogPopUpSegmento2 = true;
		this.closeDialogPopUpSegmento3 = true;
		
		if((ajusteReparteController.get("tipoSegmento1") != "Selecione...")){
			ajusteReparteController.formatarAjusteAplicadoSegmento1();
		}else{
			ajusteReparteController.validarFechamentoPopUpAjuste(ajusteReparteController.get("tipoSegmento1"), ajusteReparteController.get("segmento1"), 1);
		}
		
		if((ajusteReparteController.get("tipoSegmento2") != "Selecione...")){
			ajusteReparteController.formatarAjusteAplicadoSegmento2();
		}else{
			ajusteReparteController.validarFechamentoPopUpAjuste(ajusteReparteController.get("tipoSegmento2"), ajusteReparteController.get("segmento2"), 2);
		}
		
		if((ajusteReparteController.get("tipoSegmento3") != "Selecione...")){
			ajusteReparteController.formatarAjusteAplicadoSegmento3();
		}else{
			ajusteReparteController.validarFechamentoPopUpAjuste(ajusteReparteController.get("tipoSegmento3"), ajusteReparteController.get("segmento3"), 3);
		}
		
			
		if((this.closeDialogPopUpSegmento1 == true) && (this.closeDialogPopUpSegmento2 == true) && (this.closeDialogPopUpSegmento3 == true)){
			
			ajusteReparteController.validarEMostrarAjustesSegmento();
			$("#dialog-segmentos").dialog("close");
			
			if((ajusteReparteController.get("segmento1") != "") || (ajusteReparteController.get("segmento2") != "") || (ajusteReparteController.get("segmento3") != "")){
				ajusteReparteController.popularIndicesAjusteSegmento();
			}
		}
	},
	
	popularIndicesAjusteSegmento : function (){
		
		$("#tableSegmentos").show();
		$("#tableSegmentosEditar").show();
		
		var segmt1 = $("#segmento1").val(),
			segmt2 = $("#segmento2").val(),
			segmt3 = $("#segmento3").val();
			
			$("#exibirSegmento1").val(segmt1).disable();
			$("#exibirSegmento2").val(segmt2).disable();
			$("#exibirSegmento3").val(segmt3).disable();
			
			//Editar
			$("#exibirSegmento1Editar").val(segmt1).disable();
			$("#exibirSegmento2Editar").val(segmt2).disable();
			$("#exibirSegmento3Editar").val(segmt3).disable();
			
			
	},
	
	validarEMostrarAjustesSegmento : function validarEMostrarAjustesSegmento(){
		
		var seg01 = $("#tipoSegmento1 :selected").text(),
			seg02 = $("#tipoSegmento2 :selected").text(),
			seg03 = $("#tipoSegmento3 :selected").text();
		
		
		if(seg01 !== "Selecione..."){
			$("#tr_exibirSegmento1").show();
			$("#colSegmento1").text(seg01+":");
			$("#colSegmento1Editar").text(seg01+":");
			
		}else{
			$("#colSegmento1").text("Segmento1: ");
		}
		
		if(seg02 !== "Selecione..."){
			$("#tr_exibirSegmento2").show();
			$("#colSegmento2").text(seg02+":");
			
		}else{
			$("#colSegmento2").text("Segmento2: ");
		}
		
		if(seg03 !== "Selecione..."){
			$("#tr_exibirSegmento3").show();
			$("#colSegmento3").text(seg03+":");
		}else{
			$("#colSegmento3").text("Segmento3: ");
		}
		
	},
	
	
	limparExibicaoSegmento : function (idExibir, idtpSegmento, idSegmento, idTr){
		
		$("#"+idExibir.id).val("");
		$("#"+idtpSegmento.id).val("");
		$("#"+idSegmento.id).val("");
		
		//dialogEditar
		$("#"+idExibir.id+"Editar").val("");
		
		$("#"+idTr).hide();
		
	},
	
	validarTipoSegmento1 : function(){
		
		tipoSegmento = ajusteReparteController.get("tipoSegmento1");
		
		var tpSeg = new Array();
		
		tpSeg[0] = ajusteReparteController.get("tipoSegmento2");
		tpSeg[1] = ajusteReparteController.get("tipoSegmento3");
		
		for (var i=0;i<tpSeg.length;i++)
		{ 
			if (tipoSegmento == tpSeg[i] && (tpSeg[i] != "Selecione...")){
		        
		           var erros = new Array();
		           erros[0] = "Este tipo de segmento já foi selecionado anteriormente.";
		           exibirMensagemDialog('WARNING',   erros,"");

		        	   this.closeDialogPopUpSegmento1 = false;

		           return;
		    
			}
		}
	},
	
	validarTipoSegmento2 : function(){
	
		tipoSegmento = ajusteReparteController.get("tipoSegmento2");
		
		var tpSeg = new Array();
		
		tpSeg[0] = ajusteReparteController.get("tipoSegmento1");
		tpSeg[1] = ajusteReparteController.get("tipoSegmento3");
		
		for (var i=0;i<tpSeg.length;i++)
		{ 
			if (tipoSegmento == tpSeg[i] && (tpSeg[i] != "Selecione...")){
		        
		           var erros = new Array();
		           erros[0] = "Este tipo de segmento já foi selecionado anteriormente.";
		           exibirMensagemDialog('WARNING',   erros,"");

		        	   this.closeDialogPopUpSegmento1 = false;

		           return;
		    
			}
		}
	},
	
	validarTipoSegmento3 : function(){
		
		tipoSegmento = ajusteReparteController.get("tipoSegmento3");
		
		var tpSeg = new Array();
		
		tpSeg[0] = ajusteReparteController.get("tipoSegmento1");
		tpSeg[1] = ajusteReparteController.get("tipoSegmento2");
		
		for (var i=0;i<tpSeg.length;i++)
		{ 
			if (tipoSegmento == tpSeg[i] && (tpSeg[i] != "Selecione...")){
		        
		           var erros = new Array();
		           erros[0] = "Este tipo de segmento já foi selecionado anteriormente.";
		           exibirMensagemDialog('WARNING',   erros,"");

		        	   this.closeDialogPopUpSegmento1 = false;

		           return;
		    
			}
		}
	},
	
	mostrarSegmentosEditar : function() {
		$('#tipoSegmento1').show();
		$('#segmento1').show();
		
		$( "#dialog-segmentos" ).dialog({
			resizable: false,
			height:'auto',
			width:450,
			modal: true,
			close: function(){
				$('#formaAjusteAjusteSegmento_editar').attr('checked', true);
			},
			buttons: {
				"Confirmar": function() {
					ajusteReparteController.validarPopUpDeSegmentos();
				},
				"Cancelar": function() {
					ajusteReparteController.validarPopUpDeSegmentos();
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
	
	carregarVendaMedia : function(vdMedia){
		$.postJSON(contextPath + "/distribuicao/ajusteReparte/carregarVendaMedia", null,
				function(result) {
						$("#"+vdMedia).val(result).disable();
				   },
				   null,
				   true
		);
	},
	
	filtroPorSegmento : function(){

		$("#AJUSTE_HISTORICO_input").hide();
		$("#AJUSTE_VENDA_MEDIA_input").hide();
		$("#AJUSTE_ENCALHE_MAX_input").hide();
		
		ajusteReparteController.mostrarSegmentos();
		 
		
	},
	
	filtroPorSegmentoEditar : function(){
		$("#AJUSTE_HISTORICO_editar_input").hide();
		$("#AJUSTE_VENDA_MEDIA_editar_input").hide();
		$("#AJUSTE_ENCALHE_MAX_editar_input").hide();
		
		 ajusteReparteController.mostrarSegmentosEditar();
	},
	
	
	filtroPorHistorico : function(){

		$("#AJUSTE_HISTORICO_input").val("").show();
		$("#AJUSTE_VENDA_MEDIA_input").hide();
		$("#AJUSTE_ENCALHE_MAX_input").hide();
		$("#tableSegmentos").hide();
	},
	
	filtroPorHistoricoEditar : function(){

		$("#AJUSTE_HISTORICO_editar_input").val("").show();
		$("#AJUSTE_VENDA_MEDIA_editar_input").hide();
		$("#AJUSTE_ENCALHE_MAX_editar_input").hide();
		
		$("#tableSegmentosEditar").hide();
	},
	
	
	filtroPorVenda : function(){
		
		$("#AJUSTE_VENDA_MEDIA_input").val('');
		$("#AJUSTE_VENDA_MEDIA_input").show();
		$("#AJUSTE_HISTORICO_input").hide();
		$("#AJUSTE_ENCALHE_MAX_input").hide();
		
		$("#tableSegmentos").hide();
		
		ajusteReparteController.carregarVendaMedia("AJUSTE_VENDA_MEDIA_input");
		
	},
	
	filtroPorVendaEditar : function(){

		$("#AJUSTE_VENDA_MEDIA_editar_input").val('');
		$("#AJUSTE_VENDA_MEDIA_editar_input").show();
		$("#AJUSTE_HISTORICO_editar_input").hide();
		$("#AJUSTE_ENCALHE_MAX_editar_input").hide();
		
		$("#tableSegmentosEditar").hide();
		
		ajusteReparteController.carregarVendaMedia("AJUSTE_VENDA_MEDIA_editar_input");
	},
	
	filtroPorEncalhe : function(){
		
		$("#AJUSTE_ENCALHE_MAX_input").val('');
		$("#AJUSTE_ENCALHE_MAX_input").show();
		$("#AJUSTE_HISTORICO_input").hide();
		$("#AJUSTE_VENDA_MEDIA_input").hide();
		
		$("#tableSegmentos").hide();
	},
	
	filtroPorEncalheEditar : function(){
		
		$("#AJUSTE_ENCALHE_MAX_editar_input").val('');
		$("#AJUSTE_ENCALHE_MAX_editar_input").show();
		$("#AJUSTE_HISTORICO_editar_input").hide();
		$("#AJUSTE_VENDA_MEDIA_editar_input").hide();
		
		$("#tableSegmentosEditar").hide();
	},
	
	filtroPorSegmentoQtd_0 : function(){
		$("#tipoSegmento1").show();
		$("#segmento1").show();
		$("#tipoSegmento2").show();
		$("#segmento2").show();
		$("#segmento3").show();
		$("#tipoSegmento3").show();
		
		$("#tr_exibirSegmento1").show();
		$("#tr_exibirSegmento2").show();
		$("#tr_exibirSegmento3").show();
		
	},
	
	filtroPorSegmentoQtd_1 : function(){
		$("#tipoSegmento1").hide();
		$("#segmento1").hide();
		$("#tipoSegmento2").show();
		$("#segmento2").show();
		$("#segmento3").show();
		$("#tipoSegmento3").show();
		
		$("#tr_exibirSegmento1").hide();
		$("#tr_exibirSegmento2").show();
		$("#tr_exibirSegmento3").show();
		
	},
	
	filtroPorSegmentoQtd_2 : function(){
		$("#tipoSegmento1").hide();
		$("#segmento1").hide();
		$("#tipoSegmento2").hide();
		$("#segmento2").hide();
		$("#segmento3").show();
		$("#tipoSegmento3").show();
		
		$("#tr_exibirSegmento1").hide();
		$("#tr_exibirSegmento2").hide();
		$("#tr_exibirSegmento3").show();
	},
	
	filtroPorSegmentoQtd_3 : function(){
		$("#tipoSegmento1").hide();
		$("#segmento1").hide();
		$("#tipoSegmento2").hide();
		$("#segmento2").hide();
		$("#segmento3").hide();
		$("#tipoSegmento3").hide();
		
		$("#tr_exibirSegmento1").hide();
		$("#tr_exibirSegmento2").hide();
		$("#tr_exibirSegmento3").hide();
		
	},
	
	formatarAjusteAplicadoHistorico : function (){
		var indiceAjuste = $("#AJUSTE_HISTORICO_input").val();
		
		if (indiceAjuste == ""){
			indiceAjuste = $("#AJUSTE_HISTORICO_editar_input").val();
		}
		
	    if(indiceAjuste < 0.5 || indiceAjuste > 1.5){        
	           var erros = new Array();
	           erros[0] = "O Índice deve estar entre 0.5 e 1.5.";
	           exibirMensagemDialog('WARNING',   erros,"");                
	           $("#AJUSTE_HISTORICO_input").val("");
	           $("#AJUSTE_HISTORICO_editar_input").val("");
	           return;
	    }
	},
	
	formatarAjusteAplicadoEncalhe : function (){
		var indiceAjuste = $("#AJUSTE_ENCALHE_MAX_input").val();
		
		if (indiceAjuste == ""){
			indiceAjuste = $("#AJUSTE_ENCALHE_MAX_editar_input").val();
		}
		
	    if(indiceAjuste < 1 || indiceAjuste > 50){        
	           var erros = new Array();
	           erros[0] = "O Índice deve estar entre 01 e 50.";
	           exibirMensagemDialog('WARNING',   erros,"");                
	           $("#AJUSTE_ENCALHE_MAX_input").val("");
	           $("#AJUSTE_ENCALHE_MAX_editar_input").val("");
	           return;
	    }
	},

	formatarAjusteAplicadoEncalheEditar : function (){
			indiceAjuste = $("#AJUSTE_ENCALHE_MAX_editar_input").val();
		
	    if(indiceAjuste < 1 || indiceAjuste > 50){        
	           var erros = new Array();
	           erros[0] = "O Índice deve estar entre 01 e 50.";
	           exibirMensagemDialog('WARNING',   erros,"");                
	           $("#AJUSTE_ENCALHE_MAX_input").val("");
	           $("#AJUSTE_ENCALHE_MAX_editar_input").val("");
	           return;
	    }
	},
	
	formatarAjusteAplicadoVendaMedia : function (){
		var indiceAjuste = $("#AJUSTE_VENDA_MEDIA_input").val();
		
		if (indiceAjuste == ""){
			indiceAjuste = $("AJUSTE_VENDA_MEDIA_editar_input").val();
		}
		
	    if(indiceAjuste < 1 || indiceAjuste > 10){        
	           var erros = new Array();
	           erros[0] = "O Índice deve estar entre 01 e 10.";
	           exibirMensagemDialog('WARNING',   erros,"");                
	           $("#AJUSTE_VENDA_MEDIA_input").val("");
	           $("#AJUSTE_VENDA_MEDIA_editar_input").val("");
	           return;
	    }
	},
	
	validarFechamentoPopUpAjuste : function (tpSegmento, indiceAjuste, id){
		
		if(indiceAjuste != ""){
			if(tpSegmento == "Selecione..."){        
		           var erros = new Array();
		           erros[0] = "Informe um tipo de segmento para o índice de ajuste inserido.";
		           exibirMensagemDialog('WARNING',   erros,"");

		           if(id == 1){
		        	   this.closeDialogPopUpSegmento1 = false;
		           }
		           if(id == 2){
		        	   this.closeDialogPopUpSegmento2 = false;
		           }
		           if(id == 3){
		        	   this.closeDialogPopUpSegmento3 = false;
		           }
		           return;
		    }
		}
	},
	
		formatarAjusteAplicadoSegmento1 : function (){
			var indiceAjuste1 = $("#segmento1").val();
		    
			if(indiceAjuste1 < 0.5 || indiceAjuste1 > 1.5){        
		           var erros = new Array();
		           erros[0] = "O Índice deve estar entre 0.5 e 1.5.";
		           exibirMensagemDialog('WARNING',   erros,"");

		           this.closeDialogPopUpSegmento1 = false;
		           
		           return;
		    }
		},
		
		formatarAjusteAplicadoSegmento2 : function (){
			var indiceAjuste2 = $("#segmento2").val();
		    
			if(indiceAjuste2 < 0.5 || indiceAjuste2 > 1.5){        
		           var erros = new Array();
		           erros[0] = "O Índice deve estar entre 0.5 e 1.5.";
		           exibirMensagemDialog('WARNING',   erros,"");
		           
		           this.closeDialogPopUpSegmento2 = false;
		           
		           return;
		    }
		},
		
		formatarAjusteAplicadoSegmento3 : function (){
			var indiceAjuste3 = $("#segmento3").val();
		    
			if(indiceAjuste3 < 0.5 || indiceAjuste3 > 1.5){        
		           var erros = new Array();
		           erros[0] = "O Índice deve estar entre 0.5 e 1.5.";
		           exibirMensagemDialog('WARNING',   erros,"");
		           
		           this.closeDialogPopUpSegmento3 = false;
		           
		           return;
		    }
		},
		
		mask : function (){

			$("#dataInicioAjusteReparte").datepicker({
				showOn: "button",
				buttonImage: contextPath + "/images/calendar.gif",
				buttonImageOnly: true
			});
			
			$("#dataFimAjusteReparte").datepicker({
				showOn: "button",
				buttonImage: contextPath + "/images/calendar.gif",
				buttonImageOnly: true
			});
			
			$("#dataInicioEditar").datepicker({
				showOn: "button",
				buttonImage: contextPath + "/images/calendar.gif",
				buttonImageOnly: true
			});
			
			$("#dataFimEditar").datepicker({
				showOn: "button",
				buttonImage: contextPath + "/images/calendar.gif",
				buttonImageOnly: true
			});

			$("#AJUSTE_HISTORICO_input").mask("9.9"); 
			$("#AJUSTE_HISTORICO_editar_input").mask("9.9"); 
			$("#AJUSTE_ENCALHE_MAX_input").mask("99");
			$("#AJUSTE_ENCALHE_MAX_input").val("50");
			$("#AJUSTE_ENCALHE_MAX_editar_input").mask("99");
			$("#AJUSTE_ENCALHE_MAX_editar_input").val("50");
			
			$("#AJUSTE_VENDA_MEDIA_input").mask("99");
			$("#AJUSTE_VENDA_MEDIA_input").val("01");
			$("#AJUSTE_VENDA_MEDIA_editar_input").mask("99");
			$("#AJUSTE_VENDA_MEDIA_editar_input").val("01");
			
			
			$("#segmento1").mask("9.9");
			$("#segmento2").mask("9.9");
			$("#segmento3").mask("9.9");
		} 
	
}, BaseController);
//@ sourceURL=ajusteReparte.js
