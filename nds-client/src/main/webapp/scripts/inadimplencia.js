var inadimplenciaController = $.extend(true, {

	nomeCota : "",
	numeroCota : "",

	init : function () {		
		$("#idNumCota", inadimplenciaController.workspace).numeric();
		$("#idNomeCota", inadimplenciaController.workspace).autocomplete({source: ""});

		$( "#idDataDe", inadimplenciaController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#idDataAte", inadimplenciaController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#idDataDe", inadimplenciaController.workspace ).datepicker( "option", "dateFormat", "dd/mm/yy" );
		$("#idDataDe", inadimplenciaController.workspace).mask("99/99/9999");
		
		$( "#idDataAte",inadimplenciaController.workspace ).datepicker( "option", "dateFormat", "dd/mm/yy" );
		$("#idDataAte", inadimplenciaController.workspace).mask("99/99/9999");
	
		$("#selDivida", inadimplenciaController.workspace).click(function() {
			$(".menu_dividas", inadimplenciaController.workspace).show().fadeIn("fast");
		});

		$(".menu_dividas", inadimplenciaController.workspace).mouseleave(function() {
			$(".menu_dividas", inadimplenciaController.workspace).hide();
		});

		$("#idNumCota", inadimplenciaController.workspace).mask("?99999999999999999999", {placeholder:""});
	
		$(".inadimplenciaGrid", inadimplenciaController.workspace).flexigrid($.extend({},{
			colModel : [ {
					display : 'Cota',
					name : 'numCota',
					width : 50,
					sortable : true,
					align : 'left'
				},{
					display : 'Nome',
					name : 'nome',
					width : 130,
					sortable : true,
					align : 'left'
				}, {
					display : 'Status',
					name : 'status',
					width : 70,
					sortable : true,
					align : 'left'
				}, {
					display : 'Consignado at&eacute Data',
					name : 'consignado',
					width : 110,
					sortable : true,
					align : 'center'
				}, {
					display : 'Data Vencimento',
					name : 'dataVencimento',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Data Pagamento',
					name : 'dataPagamento',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Situa&ccedil&atildeo da D&iacutevida',
					name : 'situacao',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Divida Acumulada',
					name : 'dividaAcumulada',
					width : 85,
					sortable : true,
					align : 'right'
				}, {
					display : 'Dias em Atraso',
					name : 'diasAtraso',
					width : 75,
					sortable : false,
					align : 'center'
				}, {
					display : 'Detalhes',
					name : 'detalhe',
					width : 40,
					align : 'center',
				}],
				sortname : "dataVencimento",
				sortorder : "desc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 'auto'
			}));
	
		$(".grids", inadimplenciaController.workspace).show();	
	},

	cliquePesquisar : function() {
		
		var periodoDe = $('#idDataDe', inadimplenciaController.workspace).attr('value');
		var periodoAte = $('#idDataAte', inadimplenciaController.workspace).attr('value');
		var numCota = $('#idNumCota', inadimplenciaController.workspace).attr('value');
		var nomeCota = $('#idNomeCota', inadimplenciaController.workspace).attr('value');
		var statusCota = $('#idStatusCota', inadimplenciaController.workspace).attr('value');

		var params= [{name:'periodoDe',value : periodoDe},
			        {name:'periodoAte',value : periodoAte},
			        {name:'numCota',value : numCota},
			        {name:'nomeCota',value : nomeCota},
			        {name:'statusCota',value : statusCota}];
		
		$("input[name='checkgroup_menu_divida']").each(function(index, value) {
			if (this.checked) {
				params.push({name: 'statusDivida['+index+']', value:value.id});
			}
		});

		$(".inadimplenciaGrid", inadimplenciaController.worlspace).flexOptions({			
			url : contextPath + '/inadimplencia/pesquisar',
			dataType : 'json',
			preProcess:inadimplenciaController.processaRetornoPesquisa,
			params: params
		});
		
		$(".inadimplenciaGrid", inadimplenciaController.workspace).flexReload();
	},

	selecionarTodos : function(elementoCheck) {
		
		//var selects =  document.getElementsByName("checkgroup_menu_divida");
		var selects = $("[name='checkgroup_menu_divida']");
		
		$.each(selects, function(index, row) {
			row.checked=elementoCheck.checked;
		});
		
	},

	processaRetornoPesquisa : function(result) {
		
		var grid = result[0];
		var mensagens = result[1];
		var status = result[2];
		var total = result[3];
		var qtde = result[4];
		
		if(mensagens!=null && mensagens.length!=0) {
			exibirMensagem(status,mensagens);
		}
		
		if(!grid.rows) {
			document.getElementById("idTotal").innerHTML  = "0,00";
			document.getElementById("idQtde").innerHTML  = 0;	
			return grid;
		}
		
		$.each(grid.rows, function(index, row) {
			
			var negociada = row.cell.situacao == "Negociada";
			var boletoAntecipado = row.cell.situacao == "Boleto em branco";
			var comissao = (row.cell.comissaoSaldoDivida && row.cell.comissaoSaldoDivida > 0) && negociada; 
			
			if (!boletoAntecipado){
			
				var descricaoNegociacao = row.cell.descricaoTipoCobranca;
				
			    row.cell.detalhe = inadimplenciaController.gerarBotaoDetalhes(row.cell.idDivida, row.cell.numCota, row.cell.nome, comissao, descricaoNegociacao);
			}
			else{
				
				row.cell.detalhe = "<img src=\"" + contextPath + "/images/bt_financeiro.png\" border=\"0\" hspace=\"5\" title=\"Boleto em Branco\" />";
			}
	  	});
		
		$("#idQtde", inadimplenciaController.workspace).html(qtde);
		$("#idTotal", inadimplenciaController.workspace).html(total);
		
		return grid;
	},

	gerarBotaoDetalhes : function(idDivida, numCota, nome, comissao, descricaoNegociacao) {
		
		if(comissao) {
			return "<a href=\"javascript:;\" onclick=\"inadimplenciaController.getDetalhesComissaoCota(" + idDivida + ", " + numCota + ", '" + nome + "');\"><img src=\"" + contextPath + "/images/ico_detalhes.png\" border=\"0\" hspace=\"5\" title=\"Detalhes\" /></a>";
		}
		
		return "<a href=\"javascript:;\" onclick=\"inadimplenciaController.getDetalhes(" + idDivida + ", " + numCota + ", '" + nome + "', '" + descricaoNegociacao + "');\"><img src=\"" + contextPath + "/images/ico_detalhes.png\" border=\"0\" hspace=\"5\" title=\"Detalhes\" /></a>";
	},

	getDetalhes : function(idDivida, numCota, nome, descricaoNegociacao) {
		
		$("#dialog-detalhes", inadimplenciaController.workspace).attr('title', 'Detalhe da Dívida - ' + descricaoNegociacao);
		
		nomeCota = nome;
		numeroCota = numCota;
		
		$.postJSON(contextPath + "/inadimplencia/getDetalhesDivida", 
				{idDivida:idDivida,method:'get'}, 
				inadimplenciaController.popupDetalhes);	
	},
	
	getDetalhesComissaoCota : function(idDivida, numCota, nome, titulo) {
		nomeCota = nome;
		numeroCota = numCota;
		
		$.postJSON(contextPath + "/inadimplencia/getDividaComissao", 
				{idDivida:idDivida,method:'get'}, 
				inadimplenciaController.popupDetalhesComissaoCota);	
	},

	popupDetalhes : function(result) {
		
			inadimplenciaController.gerarTabelaDetalhes(result, numeroCota, nomeCota);
		
			$( "#dialog-detalhes", inadimplenciaController.workspace ).dialog({
				resizable: false,
				height:'auto',
				width:380,
				modal: true,
				buttons: {
					"Fechar": function() {
						$( this ).dialog( "close" );
						
						
					},
				},
				form: $("#dialog-detalhes", this.workspace).parents("form")
			});
	},
	
	popupDetalhesComissaoCota : function(result) {
		
		inadimplenciaController.gerarTabelaDetalhesComissaoCota(result, numeroCota, nomeCota);
	
		$( "#dialog-detalhes-comissao", inadimplenciaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:480,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					
				},
			},
			form: $("#dialog-detalhes-comissao", this.workspace).parents("form")
		});
	},
		
	gerarTabelaDetalhes : function(dividas, numeroCota, nome) {
		
		//var div = document.getElementById("dialog-detalhes");
		var div = $("#dialog-detalhes", inadimplenciaController.workspace);
		
		//div.innerHTML="";
		$(div).html("");
		
		var fieldset  = document.createElement("FIELDSET");
		
		fieldset.style.cssText = "width:330px;" + fieldset.style.cssText;

		$(div).append(fieldset);
		//div.appendChild(fieldset);
		
		var legend = document.createElement("LEGEND");
		legend.innerHTML = "Cota: ".bold() + numeroCota + " - " + nome;
		
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
		 	text = document.createTextNode(divida.vencimento);
		 	cel.appendChild(text);			 	
		 	linha.appendChild(cel);
		 	
		 	var cel2 = document.createElement("TD");
		 	cel2.align="right";
		 	text2 = document.createTextNode(divida.valor);
		 	cel2.appendChild(text2);			 	
		 	linha.appendChild(cel2);
		 	
		 	tbody.appendChild(linha);
			 
		 });		 		
	},
	
	gerarTabelaDetalhesComissaoCota : function(dividaComissao, numeroCota, nome) {
		
		var div = $("#dialog-detalhes-comissao", inadimplenciaController.workspace);

		$(div).html("");
		
		var fieldset  = document.createElement("FIELDSET");
		
		fieldset.style.cssText = "width:450px;" + fieldset.style.cssText;

		$(div).append(fieldset);
		
		var legend = document.createElement("LEGEND");
		legend.innerHTML = "Comissão Cota: ".bold() + numeroCota + " - " + nome;
		
		fieldset.appendChild(legend);
		
		var table = document.createElement("TABLE");
		table.id = "tabelaDetalhesComissaoId";
		table.width = "430";
		table.border = "0";
		table.cellspacing = "1";
		table.cellpadding = "1";
		
		fieldset.appendChild(table);
		
		var tbody = document.createElement("TBODY");
		
		table.appendChild(tbody);
		
	 	var cabecalho = document.createElement("TR");
	 	var linha = document.createElement("TR");

	 	cabecalho.className="header_table";
	 	
	 	var tdPercentual = document.createElement("TD");
	 	tdPercentual.width="125";
	 	tdPercentual.align="left";
	 	tdPercentual.innerHTML="Percentual Utilizado".bold();
	 	cabecalho.appendChild(tdPercentual);
	 	
	 	var celPercentual = document.createElement("TD");
	 	celPercentual.align="right";
	 	var celPercentualText = document.createTextNode(dividaComissao.porcentagem + "%");
	 	celPercentual.appendChild(celPercentualText);			 	
	 	linha.appendChild(celPercentual);

	 	var tdSaldoDivida = document.createElement("TD");
	 	tdSaldoDivida.width="100";
	 	tdSaldoDivida.align="right";
	 	tdSaldoDivida.innerHTML="Valor da Dívida R$".bold();		 	
	 	cabecalho.appendChild(tdSaldoDivida);
	 	
	 	var celSaldoDivida = document.createElement("TD");
	 	celSaldoDivida.align="right";
	 	var celSaldoDividaText = document.createTextNode(dividaComissao.valorDivida);
	 	celSaldoDivida.appendChild(celSaldoDividaText);			 	
	 	linha.appendChild(celSaldoDivida);
	 	
	 	var tdValorPago = document.createElement("TD");
	 	tdValorPago.width="100";
	 	tdValorPago.align="right";
	 	tdValorPago.innerHTML="Valor Pago R$".bold();		 	
	 	cabecalho.appendChild(tdValorPago);
	 	
	 	var celValorPago = document.createElement("TD");
	 	celValorPago.align="right";
	 	var celValorPagoText = document.createTextNode(dividaComissao.valorPago);
	 	celValorPago.appendChild(celValorPagoText);			 	
	 	linha.appendChild(celValorPago);

	 	var tdSaldoResidual = document.createElement("TD");
	 	tdSaldoResidual.width="100";
	 	tdSaldoResidual.align="right";
	 	tdSaldoResidual.innerHTML="Saldo Residual R$".bold();		 	
	 	cabecalho.appendChild(tdSaldoResidual);
	 	
	 	var celValorResidual = document.createElement("TD");
	 	celValorResidual.align="right";
	 	var celValorResidualText = document.createTextNode(dividaComissao.valorResidual);
	 	celValorResidual.appendChild(celValorResidualText);			 	
	 	linha.appendChild(celValorResidual);
	 	
	 	tbody.appendChild(cabecalho);
	 	tbody.appendChild(linha);
				 		
	}
	
}, BaseController);

//@ sourceURL=inadimplencia.js
