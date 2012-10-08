var cotaAusenteController = $.extend(true, {
	
	numCotasAusente : null,
	mov : null,
	indiceAtual : null,
	cotaAtual : '',

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
		
		cotaAusenteController.initGridProdutosEstoqueSuplementar();
		
		$(".grids", cotaAusenteController.workspace).show();		
	
		$( "#tabs-pop", cotaAusenteController.workspace ).tabs();

	},

	
	initGridProdutosEstoqueSuplementar : function() {
		
		$("#flexiGridProdutoEstoqueSuplementar", cotaAusenteController.workspace).flexigrid($.extend({},{
			dataType : 'json',
			colModel : [{
				display : 'Código',
				name : 'codigoProdutoEdicao',
				width : 100,
				sortable : false,
				align : 'left' 
			},{
				display : 'Produto',
				name : 'nomeProdutoEdicao',
				width : 100,
				sortable : false,
				align : 'left' 
			},{
				display : 'Edição',
				name : 'numeroEdicao',
				width : 70,
				sortable : false,
				align : 'left' 
			},{
				display : 'Reparte',
				name : 'reparte',
				width : 70,
				sortable : false,
				align : 'left' 
			},{
				display : 'Qtd. Disponível',
				name : 'quantidadeDisponivel',
				width : 100,
				sortable : false,
				align : 'left' 
			}],
			width : 520,
		}));
	},
	
	cliquePesquisar : function() {
		
		var dataAusencia = $('#idData', cotaAusenteController.workspace).attr('value');
		var numcota = $('#idCota', cotaAusenteController.workspace).attr('value');
		var nomeCota = $('#idNomeCota', cotaAusenteController.workspace).attr('value');
		var box = $('#idBox', cotaAusenteController.workspace).attr('value');
		var idRota = $("#selectRota", cotaAusenteController.workspace).attr('value');
		var idRoteiro = $("#selectRoteiro", cotaAusenteController.workspace).attr('value');
		
		var params = [{name:'dataAusencia',value:dataAusencia},
			          {name:'numCota',value:numcota},
			          {name:'nomeCota',value:nomeCota},
			          {name:'box',value:box},
			          {name:'idRota', value:idRota},
			          {name:'idRoteiro', value:idRoteiro}];
		
		$(".ausentesGrid", cotaAusenteController.workspace).flexOptions({			
			url : contextPath + '/cotaAusente/pesquisarCotasAusentes',
			dataType : 'json',
			preProcess:cotaAusenteController.processaRetornoPesquisa,
			params:params		
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
			return "<a href=\"javascript:;\" onclick=\"cotaAusenteController.popup_excluir("+idCotaAusente+");\"> "+
			 "<img src=\"" + contextPath + "/images/ico_excluir.gif\" title=\"Excluir\" hspace=\"5\" border=\"0\" /></a>";
		} else {
			return  "<img style=\"opacity: 0.5\" src=\"" + contextPath + "/images/ico_excluir.gif\" title=\"Excluir\" hspace=\"5\" border=\"0\" />";
		}
	},

	popupNovaCotaAusente : function(evitarReset) {
		
		if(!evitarReset) {
			$('#idCotas tr').remove();
			cotaAusenteController.gerarLinhaCota('','');
		}
		
		$( "#dialog-novo", cotaAusenteController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:530,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					var cotas = [];
					$('.cotaOrigem').each(function() {if(this.value.length>0)cotas.push(this.value);});
					
					if(cotas.length === 0) {
						exibirMensagemDialog("WARNING",["Selecione uma ou mais cotas."]);	
						return;
					}
					
					cotaAusenteController.popupConfirmaAusenciaCota(cotas);
										
					$( this ).dialog( "close" );
					
				},
				"Cancelar": function() {
										
					$( this ).dialog( "close" );
				}				
			},
			form: $("#dialog-novo", cotaAusenteController.workspace).parents("form")
		
		});
	},

	popupConfirmaAusenciaCota : function(cotas) {
		
		cotaAusenteController.numCotasAusente = cotas;
		
		var parametros = [];
		
		$.each(cotas, function(index, num) {			
			parametros.push({name:'numCotas['+ index +']', value: num});
	  	});
		
		$( "#dialog-confirm", cotaAusenteController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:350,
			modal: true,
			buttons: {
				"Suplementar": function() {
					
					$.postJSON(contextPath + "/cotaAusente/enviarParaSuplementar", 
							parametros, 
							function(result){
								$( "#dialog-confirm", cotaAusenteController.workspace ).dialog("close");
								if(result[1]!='SUCCESS')
									cotaAusenteController.popupNovaCotaAusente(true);
								else
									$('#idCotas tr').remove();
								
								cotaAusenteController.retornoEnvioSuplementar(result);
									
							}, null);									
				},
				"Redistribuir": function() {
					
					$.postJSON(contextPath + "/cotaAusente/carregarDadosRateio", 
							parametros, 
							cotaAusenteController.popupRateio);
					
					$( "#dialog-confirm", cotaAusenteController.workspace ).dialog("close");
				}				
			},
			form: $("#dialog-confirm", cotaAusenteController.workspace ).parents("form")
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
			cotaAusenteController.popupNovaCotaAusente(true);
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
			botao.innerHTML = "<a onclick=\"cotaAusenteController.gerarGridRateios("+index+");\" href=\"javascript:;\"><img src=\"" + contextPath + "/images/ico_negociar.png\" border=\"0\" /></a>";
			
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
				
				cotaAusenteController.gerarLinhaNova(index,rateio.numCota,rateio.nomeCota,rateio.qtde);
			});
			
		}  else {
			mov[indice]["rateios"] = new Array();
			proxIndice = 0;
		}	
		
		cotaAusenteController.gerarLinhaNova(proxIndice,"","","");
		
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
					
			cotaAusenteController.gerarLinhaNova( (qtdeRateios + 1) ,"","","");
					
			cotaAusenteController.alterarEvento(
					"idQtde"+indiceLinhaAlterada,
					'idNum'+ (qtdeRateios +1), 
					"onblur");
			
		} else {
			
			mov[indiceAtual].rateios[indiceLinhaAlterada] = {"numCota":numCota, "nomeCota":nomeCota, "qtde":qtde};
			
			cotaAusenteController.alterarEvento(
					"idQtde"+indiceLinhaAlterada,
					'idNum'+ qtdeRateios, 
					"onblur");
			
		}
			
		if($.trim(numCota) == "" || $.trim(nomeCota) == "" || $.trim(qtde) == "" || $.trim(qtde) == "0") {
			
			mov[indiceAtual].rateios.splice(indiceLinhaAlterada,indiceLinhaAlterada + 1);
			
			cotaAusenteController.gerarGridRateios(indiceAtual);
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
				
		numCota.append(cotaAusenteController.getInput(
				num,
				"idNum"+indice ,
				"60px",
				null,
				null,
				"pesquisaCotaCotaAusente.pesquisarPorNumeroCota('#idNum"+indice+"', '#idNom"+indice+"',true)"));
		
		nomeCota.append(cotaAusenteController.getInput(
				nome,
				"idNom"+indice ,
				"180px",
				null,
				"pesquisaCotaCotaAusente.pesquisarPorNomeCota('#idNum"+indice+"', '#idNom"+indice+"',true)", 
				null,
				"pesquisaCotaCotaAusente.autoCompletarPorNome('#idNom"+indice+"')"));
		
		qtde.append(cotaAusenteController.getInput(
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
	
	deletarInputVazio : function(elemento) {
				
		if(elemento.value.length===0) {
			cotaAusenteController.processarLinhaAlterada();
			return true;
		} else {
			return false; 
		}
	},
	
	processarLinhaAlterada : function() {
				
		var atual = cotaAusenteController.cotaAtual;
		
		var num = $('#idNumCotaOrigem' + atual).val();
		var nome = $('#idNomeCotaOrigem' + atual).val();
		
		var isNew = atual.length === 0;
		
		var nomePreenchido = nome.length != 0;
		
		if( nomePreenchido ) {
			
			$("#idLinhaCota" + atual).remove();
			
			var cotaJaExiste = $( "#idLinhaCota" + num ).length>0;
			
			if( cotaJaExiste ) {				
				exibirMensagemDialog("WARNING",["Cota já foi selecionada."]);
				$('#idNumCotaOrigem' + atual).val('');
				$('#idNomeCotaOrigem' + atual).val('');
			}
			
			cotaAusenteController.gerarLinhaCota(num,nome);
			
			var existeNovo = $( '#idNumCotaOrigem').length > 0;
			
			if ( existeNovo)
				$( '#idNumCotaOrigem').focus();
			else 			
				cotaAusenteController.gerarLinhaCota('','');
						
		} else {
			
			if(!isNew) {
				$("#idLinhaCota" + atual).remove();
				cotaAusenteController.cotaAtual = '';
			}
		}
						
		$( '#idNumCotaOrigem').focus();
	},
	
	setNum : function(elemento) {
		cotaAusenteController.cotaAtual = elemento.getAttribute('num');
	},
		
	gerarLinhaCota : function(num, nome) {		
		
		cotaAusenteController.cotaAtual = '';
		
		var tabRateios = $("#idCotas", cotaAusenteController.workspace);	
		
		tabRateios.append(cotaAusenteController.getNovaLinhaCota(num,nome));
		
	},
	
	getNovaLinhaCota : function(num, nome) {
		
		var novaLinha = $(document.createElement("TR"));
		
		novaLinha.attr('id','idLinhaCota' + num);
		
		var labelNum = $(document.createElement("TD"));
		var labelNome = $(document.createElement("TD"));
				
		var numCota = $(document.createElement("TD"));
		var nomeCota = $(document.createElement("TD"));
		
		labelNum.text('Cota:');
						
		numCota.append(cotaAusenteController.getInput(
				num,
				"idNumCotaOrigem"+num ,
				"80px",
				null,
				null,
				"pesquisaCotaCotaAusente.pesquisarPorNumeroCota('#idNumCotaOrigem"+num+"', '#idNomeCotaOrigem"+num+"',true,cotaAusenteController.processarLinhaAlterada)",
				null,
				"cotaOrigem",
				"cotaAusenteController.setNum(this)",
				"num", num));

		labelNome.text('Nome:');
				
		nomeCota.append(cotaAusenteController.getInput(
				nome,
				"idNomeCotaOrigem"+num ,
				"280px",
				null,
				"if(cotaAusenteController.deletarInputVazio(this))return; pesquisaCotaCotaAusente.pesquisarPorNomeCota('#idNumCotaOrigem"+num+"', '#idNomeCotaOrigem"+num+"',true, cotaAusenteController.processarLinhaAlterada);", 
				null,
				"pesquisaCotaCotaAusente.autoCompletarPorNome('#idNomeCotaOrigem"+num+"')",
				null,
				"cotaAusenteController.setNum(this)",
				"num", num));
				
		
		novaLinha.append(labelNum);
		novaLinha.append(numCota);
		novaLinha.append(labelNome);
		novaLinha.append(nomeCota);
		
		$("#idNumCotaOrigem"+num, cotaAusenteController.workspace).numeric();
		$("#idNomeCotaOrigem"+num, cotaAusenteController.workspace).autocomplete({source: ""});
		
		return novaLinha;
	},

	getInput : function(value,id, width,textAlign,onblur,onchange,onkeyup, classe, onfocusin, attr, attrValue) {
		
		
		var input = document.createElement("INPUT");
		input.type="text";
		input.name=id;
		input.id=id;
		input.value=value;
		input.style.setProperty("width",width);
				
		if(classe)
			input.setAttribute("class",classe);
		
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
		
		if(onfocusin) {
			input.setAttribute("onfocusin",onfocusin);
		}
		
		if(attr) {
			input.setAttribute(attr,attrValue);
		}			
		
		return input;
	},
		
	popupRateio : function(movimentos) {
		
		mov = movimentos;
			
		cotaAusenteController.gerarMovimentos(movimentos);
		
		if(movimentos[0])
			cotaAusenteController.gerarGridRateios(0);
		
		var parametros = [];
		
		$.each(cotaAusenteController.numCotasAusente, function(index, num) {			
			parametros.push({name:'numCotas['+ index +']', value: num});
	  	});
		
		$( "#dialog-suplementar", cotaAusenteController.workspace ).dialog({
			resizable: false,
			height:450,
			width:800,
			modal: true,
			buttons: {
				"Suplementar": function() {
					$.postJSON(contextPath + "/cotaAusente/enviarParaSuplementar", 
							parametros, 
							function(result){
								$( "#dialog-suplementar", cotaAusenteController.workspace ).dialog("close");
								if(result[1]!='SUCCESS')
									cotaAusenteController.popupNovaCotaAusente(true);
								
								cotaAusenteController.retornoEnvioSuplementar(result);
									
							}, null);	
				},
				"Redistribuir": function() {
					
					var parametros = cotaAusenteController.getParametrosFromMovimentos();
					
					if(!parametros) {
						return;
					}
					
					$.postJSON(contextPath + "/cotaAusente/realizarRateio", 
							parametros,
							cotaAusenteController.retornoRateio);
					
					$( "#dialog-suplementar", cotaAusenteController.workspace ).dialog( "close" );
				}
			},form: $( "#dialog-suplementar", cotaAusenteController.workspace ).parents("form")
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
		
		$.each(cotaAusenteController.numCotasAusente , function(index, num) {			
			parametros.push({name:'numCotas['+ index +']', value: num});
	  	});
		
		return parametros;
	},
	
	retornoExlusaoCotaAusente : function(result) {
		
		var mensagens = result[0];
		var status = result[1];
		
		exibirMensagem(status, mensagens);
		
		$(".ausentesGrid", cotaAusenteController.workspace).flexReload();
	},
		
	popup_excluir : function(idCotaAusente) {
		
			cotaAusenteController.exibirProdutoEstoqueDisponivel(idCotaAusente);
		
			$( "#dialog-excluir", cotaAusenteController.workspace ).dialog({
				resizable: false,
				height:'auto',
				width:'auto',
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
				},
				form: $( "#dialog-excluir", cotaAusenteController.workspace ).parents("form")
			});
	},
	
	exibirProdutoEstoqueDisponivel : function(idCotaAusente) {
		var params = [{name:'idCotaAusente',value:idCotaAusente}];
		
		$("#flexiGridProdutoEstoqueSuplementar", cotaAusenteController.workspace).flexOptions({			
			url : contextPath + '/cotaAusente/exibirProdutosSuplementaresDisponiveis',
			params:params,
			newp : 1
		});
		
		$("#flexiGridProdutoEstoqueSuplementar", cotaAusenteController.workspace).flexReload();
	}
	
}, BaseController);
