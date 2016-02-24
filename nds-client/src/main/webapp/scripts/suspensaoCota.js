var suspensaoCotaController = $.extend(true, {

	infoCota : "",

	init : function() {
		$( "#datepickerDe", suspensaoCotaController.workspace ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerDe1", suspensaoCotaController.workspace ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});

		$(".suspensaoGrid", suspensaoCotaController.workspace).flexigrid($.extend({},{
			url : contextPath + '/suspensaoCota/obterCotasSuspensaoJSON',
			dataType : 'json',
			preProcess:suspensaoCotaController.processaRetornoPesquisa,
			onSuccess: function() {bloquearItensEdicao(suspensaoCotaController.workspace);},
			colModel : [  {
				display : 'Cota',
				name : 'numCota',
				width : 40,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nome',
				width : 110,
				sortable : true,
				align : 'left'
			},{
				display : 'Valor Consignado Total R$',
				name : 'vlrConsignado',
				width : 120,
				sortable : false,
				align : 'right'
			}, {
				display : 'Reparte do Dia R$(Capa)',
				name : 'vlrReparte',
				width : 125,
				sortable : false,
				align : 'right'
			}, {
				display : 'Divida Acumulada R$',
				name : 'dividaAcumulada',
				width : 120,
				sortable : true,
				align : 'right'
			}, {
				display : 'Dias em Aberto',
				name : 'diasAberto',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Faturamento R$',
				name : 'faturamento',
				width : 80,
				sortable : false,
				align : 'center'
			
			}, {
				display : '% Dívida',
				name : 'percDivida',
				width : 80,
				sortable : false,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 45,
				sortable : false,
				align : 'center',
			},{
				display : '  ',
				name : 'selecionado',
				width : 20,
				sortable : false,
				align : 'center'
			}],
			sortname : "numCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			showTableToggleBtn : true,
			width : 960,
			height : 260
		})); 	
		
		
	},	
	
	getDetalhes : function(idCota, numeroCota, nome) {
		suspensaoCotaController.infoCota = numeroCota + " - " + nome;
		$.postJSON(contextPath + "/suspensaoCota/getInadinplenciasDaCota", 
				{idCota:idCota,method:'get'}, 
				suspensaoCotaController.popupDetalhes);	
	}, 
	
	popupDetalhes : function(result) {

		suspensaoCotaController.gerarTabelaDetalhes(result, suspensaoCotaController.infoCota);
		
		$( "#dialog-detalhes", suspensaoCotaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:380,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					
				},
			},
			form: $("#dialog-detalhes", suspensaoCotaController.workspace).parents("form")
		});
	},
	
	selecionarTodos : function(element) {
		$.postJSON(contextPath + "/suspensaoCota/selecionarTodos", 
				{selecionado:element.checked}, 
				checkAll(element,'selecao'));	
		
	},
	
	popupConfirmar : function() {
	
		var checks = $('[name="selecao"]', suspensaoCotaController.workspace);
		var algumSelecionado = false;
		
		$.each(checks, function(index,value){
			
			if( $(value).is(':checked') == true)
				algumSelecionado = true;
		});
		
		if(!algumSelecionado) {
			suspensaoCotaController.popupNenhumaSelecionada();
			return;
		}
		
		$( "#dialog-suspender", suspensaoCotaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					$.postJSON(contextPath + "/suspensaoCota/suspenderCotas", 
							"", 
							suspensaoCotaController.popupRelatorio);	
					
					$( this ).dialog( "close" );
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-suspender", suspensaoCotaController.workspace).parents("form")
		});	
		      
	},
	
	popupNenhumaSelecionada : function() {
		
		$( "#dialog-nao-selecionada", suspensaoCotaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:380,
			modal: true,
			buttons: {
				"Fechar": function() {
					
					$( this ).dialog( "close" );
					
				}
			},
			form: $("#dialog-nao-selecionada", suspensaoCotaController.workspace).parents("form")
		});	
	},
	
	popupRelatorio : function(result) {
		
		var cotas = result[0];
		var mensagens = result[1];
		var status = result[2];	
		
		
		if(mensagens!=null && mensagens.length!=0) {
			exibirMensagem(status,mensagens);
		}
		
		if(status=="ERROR" || status=="WARNING") {
			$(".suspensaoGrid", suspensaoCotaController.workspace).flexReload();
			return null;
		}
		
		if(cotas.length != 0) {
			suspensaoCotaController.gerarRelatorio(cotas);
		
		
			$( "#divRelatorio", suspensaoCotaController.workspace ).dialog({
				resizable: false,
				height:'auto',
				width:380,
				modal: true,
				buttons: {
					"Fechar": function() {
						$(".suspensaoGrid", suspensaoCotaController.workspace).flexReload();
						exibirMensagem(status,["Suspensão realizada com sucesso."]);
						$( this ).dialog( "close" );
					}
				},
				form: $("#divRelatorio", suspensaoCotaController.workspace).parents("form")
			});
		} else {
			$(".suspensaoGrid", suspensaoCotaController.workspace).flexReload();
			exibirMensagem(status,["Suspensão realizada com sucesso."]);
		}
	},
	
	adicionarSelecao : function(id, check) {
		
		$.postJSON(contextPath + "/suspensaoCota/selecionarItem", 
				{idCota:id,selecionado:check.checked}, 
				suspensaoCotaController.retornoSemAcao);				
	},
	
	retornoSemAcao : function(data) {
		
	},
	
	popup_excluir : function() {
	
		$( "#dialog-excluir", suspensaoCotaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-excluir", suspensaoCotaController.workspace).parents("form")
		});
	},
	
 
	mostrar : function(){
		$(".grids", suspensaoCotaController.workspace).show();
	},	

	gerarCheckbox : function(id,name,idCota,selecionado) {
		
		var input = document.createElement("INPUT");
		input.id=id;
		input.name=name;
		input.style.cssText = "float:left;" + input.style.cssText;
		input.type="checkbox";
		input.setAttribute("isEdicao",true);
		input.setAttribute("onclick","suspensaoCotaController.adicionarSelecao("+idCota+",this);");
		
		if(selecionado==true) {
			input.checker=true;
		}
		
		return input.outerHTML || new XMLSerializer().serializeToString(input);
	},
	
	gerarAcoes : function(idCota,dividas,numCota, nome) {
		
		var a = document.createElement("A");
		a.href = "javascript:;";
		a.setAttribute("onclick","suspensaoCotaController.getDetalhes("+idCota+",'"+numCota+"','"+nome+"');");
		
		var img =document.createElement("IMG");
		img.src=contextPath + "/images/ico_detalhes.png";
		img.border="0";
		img.hspace="5";
		img.title="Detalhes";		
		a.innerHTML = img.outerHTML || new XMLSerializer().serializeToString(img);;		
		
		return a.outerHTML || new XMLSerializer().serializeToString(a);
	},
	
	gerarTabelaDetalhes : function(dividas, nome) {
		var div = $("#dialog-detalhes", suspensaoCotaController.workspace);
		
		$(div).html("");
		//div.innerHTML="";
		
		var fieldset  = document.createElement("FIELDSET");
		
		fieldset.style.cssText = "width:330px;" + fieldset.style.cssText;
		
		$(div).append(fieldset);
		//div.appendChild(fieldset);
		
		var legend = document.createElement("LEGEND");
		legend.innerHTML = "Cota: ".bold() + nome;
		
		fieldset.appendChild(legend);
		
		var table = document.createElement("TABLE");
		table.id = "tabelaDetalhesId";
		table.width = "330";
		table.border = "0";
		table.cellspacing = "1";
		table.cellpadding = "1";
		
		fieldset.appendChild(table);
		
		var tbody = document.createElement("TBODY");
		
		table.appendChild(tbody);
		
	 	var cabecalho = document.createElement("TR");
	 	cabecalho.className="header_table";
	 	
	 	var tdEmissao = document.createElement("TD");
	 	tdEmissao.width="136";
	 	tdEmissao.align="left";
	 	tdEmissao.innerHTML="Dt Emissao".bold();
	 	cabecalho.appendChild(tdEmissao);
	 	
	 	var tdDia = document.createElement("TD");
	 	tdDia.width="136";
	 	tdDia.align="left";
	 	tdDia.innerHTML="Dia Vencimento".bold();
	 	cabecalho.appendChild(tdDia);
	 	
	 	var tdValor = document.createElement("TD");
	 	tdValor.width="157";
	 	tdValor.align="right";
	 	tdValor.innerHTML="Valor R$".bold();		 	
	 	cabecalho.appendChild(tdValor);
	 	
	 	tbody.appendChild(cabecalho);
		
		 $(dividas).each(function (index, divida) {
			 
			 var linha = document.createElement("TR");
			 
			 var lin = (index%2==0) ? 1:2;
			 
			 linha.className="class_linha_" + lin ;
	 	 
		 	var cel = document.createElement("TD");
		 	cel.align="left";
		 	text = document.createTextNode(divida.emissao);
		 	cel.appendChild(text);			 	
		 	linha.appendChild(cel);

		 	var cel1 = document.createElement("TD");
		 	cel1.align="left";
		 	text = document.createTextNode(divida.vencimento);
		 	cel1.appendChild(text);			 	
		 	linha.appendChild(cel1);
		 	
		 	var cel2 = document.createElement("TD");
		 	cel2.align="right";
		 	text2 = document.createTextNode(divida.valor);
		 	cel2.appendChild(text2);			 	
		 	linha.appendChild(cel2);
		 	
		 	tbody.appendChild(linha);
			 
			
		 });		 		
	},
	
	gerarRelatorio : function(cotas) {
		
		var table = $("#tabelaRelatorio", suspensaoCotaController.workspace);
		table.width = "330";
		table.border = "0";
		table.cellspacing = "1";
		table.cellpadding = "1";
		
		table.innerHTML="";
		
		var tbody = document.createElement("TBODY");
		
		//table.appendChild(tbody);
		$(table).append(tbody);
		
	 	var cabecalho = document.createElement("TR");
	 	cabecalho.className="header_table";
	 	
	 	var tdDia = document.createElement("TD");
	 	tdDia.width="72";
	 	tdDia.align="left";
	 	tdDia.innerHTML="Cota".bold();
	 	cabecalho.appendChild(tdDia);
	 	
	 	var tdValor = document.createElement("TD");
	 	tdValor.width="251";
	 	tdValor.align="left";
	 	tdValor.innerHTML="Nome".bold();		 	
	 	cabecalho.appendChild(tdValor);
	 	
	 	tbody.appendChild(cabecalho);
		
		 $(cotas).each(function (index, cota) {
			 
			 var linha = document.createElement("TR");
			 
			 var lin = (index%2==0) ? 1:2;
			 
			 linha.className="class_linha_" + lin ;
	 	 
		 	var cel = document.createElement("TD");
		 	cel.align="left";
		 	text = document.createTextNode(cota.idCota);
		 	cel.appendChild(text);			 	
		 	linha.appendChild(cel);
		 	
		 	var cel2 = document.createElement("TD");
		 	cel2.align="right";
		 	text2 = document.createTextNode(cota.nome);
		 	cel2.appendChild(text2);			 	
		 	linha.appendChild(cel2);
		 	
		 	tbody.appendChild(linha);
			 
			
		 });		 		
	},
	
	processaRetornoPesquisa : function(data) {
		
		var grid = data[0];
		var mensagens = data[1];
		var status = data[2];
		
		var checkTodos = $("#sel", suspensaoCotaController.workspace).is("checked");
				
		if(mensagens != null && mensagens.length!=0 && checkTodos!=true) {
			exibirMensagem(status,mensagens);
			checkTodos=false;			
		}
				
		if(!grid.rows || status=="error") {
			
			$("#total", suspensaoCotaController.workspace).text("0,00");
			$("#totalSugerida", suspensaoCotaController.workspace).text("0");	

			return grid;
			
		} else {
			$(".corpo", suspensaoCotaController.workspace).show();	
		}
		

		for(var i=0; i<grid.rows.length; i++) {			
			
			var cell = grid.rows[i].cell;
			
			cell.acao = suspensaoCotaController.gerarAcoes(cell.idCota,cell.dividas,cell.numCota,cell.nome);
			cell.selecionado = suspensaoCotaController.gerarCheckbox('idCheck'+i,'selecao', cell.idCota,cell.selecionado);;					
		}
		
		$("#total", suspensaoCotaController.workspace).text(data[4]);
		$("#totalSugerida", suspensaoCotaController.workspace).text(data[3]);	

		
		return grid;
	}
	
}, BaseController);

//@ sourceURL=suspensaoCota.js
