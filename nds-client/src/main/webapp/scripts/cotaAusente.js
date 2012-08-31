var cotaAusenteController = $.extend(true, {
	
	numCotaAusente : null,
	mov : null,
	indiceAtual : null,

	init : function() {
		$("#idNovaCota", cotaAusenteController.workspace).numeric();
		$("#idNomeNovaCota", cotaAusenteController.workspace).autocomplete({source: ""});
		
		$("#idCota", cotaAusenteController.workspace).numeric();
		$("#idNomeCota", cotaAusenteController.workspace).autocomplete({source: ""});

		$("#idCota", cotaAusenteController.workspace).mask("?99999999999999999999", {placeholder:""});

		$( "#idData", cotaAusenteController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#idData", cotaAusenteController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#idData", cotaAusenteController.workspace ).datepicker( "option", "dateFormat", "dd/mm/yy" );
		$("#idData", cotaAusenteController.workspace).mask("99/99/9999");

		
		$(".ausentesGrid", cotaAusenteController.workspace).flexigrid($.extend({},{
			colModel : [ {
				display : 'Data',
				name : 'data',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Box',
				name : 'box',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 480,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor NE R$',
				name : 'valorNe',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			}],
			sortname : "data",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		})); 	
		
		$(".grids", cotaAusenteController.workspace).show();		
	
		$( "#tabs-pop", cotaAusenteController.workspace ).tabs();

	},

	cliquePesquisar : function() {
		
		var dataAusencia = $('#idData', cotaAusenteController.workspace).attr('value');
		var numcota = $('#idCota', cotaAusenteController.workspace).attr('value');
		var nomeCota = $('#idNomeCota', cotaAusenteController.workspace).attr('value');
		var box = $('#idBox', cotaAusenteController.workspace).attr('value');
			
		$(".ausentesGrid", cotaAusenteController.workspace).flexOptions({			
			url : contextPath + '/cotaAusente/pesquisarCotasAusentes',
			dataType : 'json',
			preProcess:cotaAusenteController.processaRetornoPesquisa,
			params:[{name:'dataAusencia',value:dataAusencia},
			        {name:'numCota',value:numcota},
			        {name:'nomeCota',value:nomeCota},
			        {name:'box',value:box}]		
		});
		
		$(".ausentesGrid", cotaAusenteController.workspace).flexReload();
	},

	processaRetornoPesquisa : function(result) {
		
		var grid = result[0];
		var mensagens = result[1];
		var status = result[2];
		
		if(mensagens!=null && mensagens.length!=0) {
			exibirMensagem(status,mensagens);
		}
		
		if(!grid.rows) {
			return grid;
		}
		
		$.each(grid.rows, function(index, row) {
			
			row.cell.acao = cotaAusenteController.gerarBotaoExcluir(row.cell.idCotaAusente);		
			
	  	});
		
		return grid;
	},

	gerarBotaoExcluir : function(idCotaAusente) {
		
		if(idCotaAusente) {
			return "<a href=\"javascript:;\" onclick=\"popup_excluir("+idCotaAusente+");\"> "+
			 "<img src=\"${pageContext.request.contextPath}/images/ico_excluir.gif\" title=\"Excluir\" hspace=\"5\" border=\"0\" /></a>";
		} else {
			return  "<img style=\"opacity: 0.5\" src=\"${pageContext.request.contextPath}/images/ico_excluir.gif\" title=\"Excluir\" hspace=\"5\" border=\"0\" />";
		}
	},

	popupNovaCotaAusente : function() {
		
		$( "#dialog-novo", cotaAusenteController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:540,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					var numCota = $("#idNovaCota", cotaAusenteController.workspace).attr("value");
					var nomeCota = $("#idNomeNovaCota", cotaAusenteController.workspace).attr("value");
					
					if($.trim(numCota) == "" || $.trim(nomeCota) == "") {
						exibirMensagemDialog("WARNING",["O campo \"Cota\" &eacute obrigat&oacuterio."]);	
						return;
					}
					
					cotaAusenteController.popupConfirmaAusenciaCota(numCota);
					
					$("#idNovaCota", cotaAusenteController.workspace).attr("value","");
					$("#idNomeNovaCota", cotaAusenteController.workspace).attr("value",""); 
					$( this ).dialog( "close" );
					
				},
				"Cancelar": function() {
					
					$("#idNovaCota", cotaAusenteController.workspace).attr("value","");
					$("#idNomeNovaCota", cotaAusenteController.workspace).attr("value","");
					
					$( this ).dialog( "close" );
				}
			}
		});
	},

	popupConfirmaAusenciaCota : function(numcota) {
		
		numCotaAusente = numcota;
		
			$( "#dialog-confirm", cotaAusenteController.workspace ).dialog({
				resizable: false,
				height:'auto',
				width:350,
				modal: true,
				buttons: {
					"Sim": function() {
						
						$.postJSON(contextPath + "/cotaAusente/enviarParaSuplementar", 
								"numCota="+numcota, 
								cotaAusenteController.retornoEnvioSuplementar);
					
						$( "#dialog-confirm", cotaAusenteController.workspace ).dialog("close");
						
					},
					"Não": function() {
						
						$.postJSON(contextPath + "/cotaAusente/carregarDadosRateio", 
								"numCota="+numcota, 
								popupRateio);
						
						$( this ).dialog( "close" );
					}
				}
			});
	},

	retornoEnvioSuplementar : function(result) {
		
		var mensagens = result[0];
		var status = result[1];
		
		exibirMensagem(status, mensagens);
		
		$(".ausentesGrid", cotaAusenteController.workspace).flexReload();
	},

	retornoRateio : function(result) {
		
		var mensagens = result[0];
		var status = result[1];
		
		if(status == "SUCCESS") {
		
			exibirMensagem(status, mensagens);
			
			$(".ausentesGrid", cotaAusenteController.workspace).flexReload();
			
			$( "#dialog-confirm", cotaAusenteController.workspace ).dialog("close");
		} else {
			exibirMensagemDialog(status, mensagens);
		}
	},

	gerarMovimentos : function(movimentos) {
		
		var tabMovimentos = $("#idMovimentos", cotaAusenteController.workspace);	
		var cabecalho = $("#idCabecalhoMovimentos", cotaAusenteController.workspace);
			
		tabMovimentos.clear();
		
		tabMovimentos.append(cabecalho);
		
		$.each(movimentos, function(index, movimento) {
			var novaLinha = $(document.createElement("TR"));
			
			var codigo = document.createElement("TD");
			var produto = document.createElement("TD");
			var edicao = document.createElement("TD");
			var reparte = document.createElement("TD");
			var botao = document.createElement("TD");
					
			codigo.innerHTML = movimento.codigoProd;
			produto.innerHTML = movimento.nomeProd;
			edicao.innerHTML = movimento.edicaoProd;
			reparte.innerHTML = movimento.qtdeReparte;
			botao.innerHTML = "<a onclick=\"gerarGridRateios("+index+");\" href=\"javascript:;\"><img src=\"${pageContext.request.contextPath}/images/ico_negociar.png\" border=\"0\" /></a>";
			
			novaLinha.append(codigo);
			novaLinha.append(produto);
			novaLinha.append(edicao);
			novaLinha.append(reparte);	
			novaLinha.append(botao);
			
			if(index%2 == 0) {
				novaLinha.attr("style", "background:#F8F8F8;");
			}
			
			tabMovimentos.append(novaLinha);
		});
	},

	gerarGridRateios : function(indice) {
		
		indiceAtual = indice;
		
		$("#idFieldRateios", cotaAusenteController.workspace).attr("style","");

		document.getElementById("idLegendRateios", cotaAusenteController.workspace).innerHTML= "Redistribuição - "+mov[indice].nomeProd;
		
		var tabRateios = $("#idRateios", cotaAusenteController.workspace);	
		var cabecalho = $("#idCabecalhoRateios", cotaAusenteController.workspace);
			
		tabRateios.clear();
		tabRateios.append(cabecalho);
		
		var proxIndice;
		
		if(mov[indice].rateios) {
		
			proxIndice = mov[indice].rateios.length;
			
			$.each(mov[indice].rateios, function(index, rateio) {
				
				gerarLinhaNova(index,rateio.numCota,rateio.nomeCota,rateio.qtde);
			});
			
		}  else {
			mov[indice]["rateios"] = new Array();
			proxIndice = 0;
		}	
		
		gerarLinhaNova(proxIndice,"","","");
		
		var qtdeRateios = mov[indiceAtual].rateios.length;
		document.getElementById('idNum'+ qtdeRateios).focus();
	},
		
	gerarNovoRateio : function(indiceLinhaAlterada) {
		
		var numCota = $("#idNum" + indiceLinhaAlterada, cotaAusenteController.workspace).attr("value");
		var nomeCota = $("#idNom" + indiceLinhaAlterada, cotaAusenteController.workspace).attr("value");
		var qtde = $("#idQtde" + indiceLinhaAlterada, cotaAusenteController.workspace).attr("value");
		
			
		var totalRateado = 0 * 1;
		$.each(mov[indiceAtual].rateios, function(index, rateio) {		
			totalRateado = totalRateado*1 + rateio.qtde*1;
		});
		
		var soma = totalRateado*1 + qtde*1; 
		
		if( soma > mov[indiceAtual].qtdeReparte) {
			exibirMensagemDialog("WARNING",["N&atildeo h&aacute reparte suficiente."]);	
			
			alterarEvento(
					"idQtde"+indiceLinhaAlterada,
					'idQtde'+ indiceLinhaAlterada, 
					"onblur");
			
			$("#idQtde" + indiceLinhaAlterada).attr("value","");
			return;
		}
		
		
		var qtdeRateios = mov[indiceAtual].rateios.length;
		
		if( indiceLinhaAlterada == (qtdeRateios) ) {
			
			mov[indiceAtual].rateios.push({"numCota":numCota, "nomeCota":nomeCota, "qtde":qtde});
					
			gerarLinhaNova( (qtdeRateios + 1) ,"","","");
					
			alterarEvento(
					"idQtde"+indiceLinhaAlterada,
					'idNum'+ (qtdeRateios +1), 
					"onblur");
			
		} else {
			
			mov[indiceAtual].rateios[indiceLinhaAlterada] = {"numCota":numCota, "nomeCota":nomeCota, "qtde":qtde};
			
			alterarEvento(
					"idQtde"+indiceLinhaAlterada,
					'idNum'+ qtdeRateios, 
					"onblur");
			
		}
			
		if($.trim(numCota) == "" || $.trim(nomeCota) == "" || $.trim(qtde) == "" || $.trim(qtde) == "0") {
			
			mov[indiceAtual].rateios.splice(indiceLinhaAlterada,indiceLinhaAlterada + 1);
			
			gerarGridRateios(indiceAtual);
			return;
		}
	},

	alterarEvento : function(idFocoAtual, idNovoFoco, evento) {
		
		var elemAtual = document.getElementById(idFocoAtual);	
		var elemNovoFoco = document.getElementById(idNovoFoco);	
		
		var valorEvento = elemAtual.getAttribute(evento);
		
		elemAtual.setAttribute(evento,null);
			
		elemNovoFoco.focus();
		
		elemAtual.setAttribute(evento,valorEvento);	
	},
		
	gerarLinhaNova : function(indice,num, nome, qtd) {
			
		var tabRateios = $("#idRateios", cotaAusenteController.workspace);	
		
		var novaLinha = $(document.createElement("TR"));
		
		var numCota = $(document.createElement("TD"));
		var nomeCota = $(document.createElement("TD"));
		var qtde = $(document.createElement("TD"));
				
		numCota.append(getInput(
				num,
				"idNum"+indice ,
				"60px",
				null,
				null,
				"pesquisaCotaCotaAusente.pesquisarPorNumeroCota('#idNum"+indice+"', '#idNom"+indice+"',true)"));
		
		nomepesquisaCotaCotaAusente.append(getInput(
				nome,
				"idNom"+indice ,
				"180px",
				null,
				"pesquisaCotaCotaAusente.pesquisarPorNomeCota('#idNum"+indice+"', '#idNom"+indice+"',true)", 
				null,
				"pesquisaCotaCotaAusente.autoCompletarPorNome('#idNom"+indice+"')"));
		
		qtde.append(getInput(
				qtd,
				"idQtde"+indice ,
				"60px",
				"center",
				"cotaAusenteController.gerarNovoRateio("+indice+");"));
		
		novaLinha.append(numCota);
		novaLinha.append(nomeCota);
		novaLinha.append(qtde);
		novaLinha.append("<td><input type=\"hidden\" value=\""+indice+"\"></td>");
		
		tabRateios.append(novaLinha);
		
		$("#idNum"+indice, cotaAusenteController.workspace).numeric();
		$("#idQtde"+indice, cotaAusenteController.workspace).numeric();
		$("#idNom"+indice, cotaAusenteController.workspace).autocomplete({source: ""});
	},

	getInput : function(value,id, width,textAlign,onblur,onchange,onkeyup) {
		
		var input = document.createElement("INPUT");
		input.type="text";
		input.name=id;
		input.id=id;
		input.value=value;
		input.style.setProperty("width",width);
		
		if(textAlign) {
			input.style.setProperty("text-align",textAlign);
		}
		
		if(onblur) {
			input.setAttribute("onblur",onblur);
		}
		
		if(onchange) {
			input.setAttribute("onchange",onchange);
		}
		
		if(onkeyup) {
			input.setAttribute("onkeyup",onkeyup);
		}
			
		return input;
	},
		
	popupRateio : function(movimentos) {
		
		mov = movimentos;
			
		cotaAusenteController.gerarMovimentos(movimentos);
		
		if(movimentos[0])
			cotaAusenteController.gerarGridRateios(0);
		
		$( "#dialog-suplementar", cotaAusenteController.workspace ).dialog({
			resizable: false,
			height:450,
			width:800,
			modal: true,
			buttons: {
				"Suplementar": function() {
					
					$.postJSON(contextPath + "/cotaAusente/enviarParaSuplementar", 
							"numCota=" + numCotaAusente, 
							cotaAusenteController.retornoEnvioSuplementar);
					
					$( this ).dialog( "close" );
					
				},
				"Redistribuir": function() {
					
					var parametros = cotaAusenteController.getParametrosFromMovimentos();
					
					if(!parametros) {
						return;
					}
					
					$.postJSON(contextPath + "/cotaAusente/realizarRateio", 
							parametros,
							cotaAusenteController.retornoRateio);
					
					$( this ).dialog( "close" );
				}
			}
		});
	},

	getParametrosFromMovimentos : function() {
		
		var parametros = [];
		
		$.each(mov, function(index, movimento) {
			
			parametros.push({name:'movimentos['+ index +'].idCota', value: movimento.idCota});
			parametros.push({name:'movimentos['+ index +'].idProdEd', value: movimento.idProdEd});
			parametros.push({name:'movimentos['+ index +'].codigoProd', value: movimento.codigoProd});
			parametros.push({name:'movimentos['+ index +'].edicaoProd', value: movimento.edicaoProd});
			parametros.push({name:'movimentos['+ index +'].nomeProd', value: movimento.nomeProd});
			parametros.push({name:'movimentos['+ index +'].qtdeReparte', value: movimento.qtdeReparte});
			
			if(movimento.rateios) {
							
				$.each(movimento.rateios, function(indexR, rateio) {
					parametros.push({name:'movimentos['+ index +'].rateios['+ indexR +'].numCota', value: rateio.numCota});
					parametros.push({name:'movimentos['+ index +'].rateios['+ indexR +'].nomeCota', value: rateio.nomeCota});
					parametros.push({name:'movimentos['+ index +'].rateios['+ indexR +'].qtde', value: rateio.qtde});			
				});		
			}
	  	});
		
		parametros.push({name:'numCota', value: numCotaAusente});
		
		return parametros;
	},
			
	popup_alterar : function() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
			$( "#dialog-novo", cotaAusenteController.workspace ).dialog({
				resizable: false,
				height:220,
				width:540,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						
						
						$( this ).dialog( "close" );
						$("#effect", cotaAusenteController.workspace).show("highlight", {}, 1000, callback);
						$(".grids", cotaAusenteController.workspace).show();
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
	},	

	retornoExlusaoCotaAusente : function(result) {
		
		var mensagens = result[0];
		var status = result[1];
		
		exibirMensagem(status, mensagens);
		
		$(".ausentesGrid", cotaAusenteController.workspace).flexReload();
	},
		
	popup_excluir : function(idCotaAusente) {
		
			$( "#dialog-excluir", cotaAusenteController.workspace ).dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						$.postJSON(contextPath + "/cotaAusente/cancelarCotaAusente", 
								"idCotaAusente="+idCotaAusente, 
								cotaAusenteController.retornoExlusaoCotaAusente);
						
						$( this ).dialog( "close" );
						$(".grids", cotaAusenteController.workspace).show();
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
	},

	abre_linha_1 : function(){
		$( '.linha_1', cotaAusenteController.workspace ).show();
		textfield5.focus();
	},
	
	abre_linha_2 : function(){
		$( '.linha_2', cotaAusenteController.workspace ).show();
		textfield8.focus();
	},
	
	abre_linha_3 : function(){
		$( '.linha_3', cotaAusenteController.workspace ).show();
		textfield11.focus();
	},
	
	abre_linha_4 : function(){
		$( '.linha_4', cotaAusenteController.workspace ).show();
		textfield11.focus();
	},
	
	abre_linha_4 : function(){
		$( '.linha_5', cotaAusenteController.workspace ).show();
		textfield17.focus();
	},
	
	abre_linha_5 : function(){
		$( '.linha_6', cotaAusenteController.workspace).show();
		textfield20.focus();
	},
	
	abre_linha_21 : function(){
		$( '.linha_21', cotaAusenteController.workspace ).show();
		textfield24.focus();
	},
	
	abre_linha_22 : function(){
		$( '.linha_22', cotaAusenteController.workspace ).show();
		textfield27.focus();
	},

	abre_linha_31 : function(){
		$( '.linha_31', cotaAusenteController.workspace ).show();
		textfield34.focus();
	},
	
	abre_linha_32 : function(){
		$( '.linha_32', cotaAusenteController.workspace ).show();
		textfield37.focus();
	},

	mostra_grid : function(){
		$( '#grid_1', cotaAusenteController.workspace ).show();
	}

}, BaseController);
