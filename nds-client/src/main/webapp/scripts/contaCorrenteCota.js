var contaCorrenteCotaController = $.extend(true, {

	/*
	 * O JAVASCRIPT ABAIXO E O PRIMEIRO A SER EXECUTADO 
	 * QUANDO A PAGINA CARREGA.
	 */
	init : function() {
		$("#cota", contaCorrenteCotaController.workspace).numeric();
		
		$("#nomeCota", contaCorrenteCotaController.workspace).autocomplete({source: ""});
		
		$( "#periodoContaDe", contaCorrenteCotaController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#periodoContaAte", contaCorrenteCotaController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		contaCorrenteCotaController.carregarItemContaCorrenteCotaGrid();
		contaCorrenteCotaController.montarColunaConsignado();
		contaCorrenteCotaController.montarColunaEncalheCota();
		
		vendaEncalhe.url = contextPath + '/financeiro/contaCorrenteCota/obterMovimentoVendaEncalhe';
		vendaEncalhe.initGrid(".vendaEncalheGrid", contaCorrenteCotaController.workspace);
		vendaEncalhe.dialogId = "#dialog-venda-encalhe";
		
		vendaEncalhe.urlExport = contextPath + '/financeiro/contaCorrenteCota/exportarVendaEncalhe';
		
	},
	
	/**
	 * VERIFICA A EXISTENCIA DE UMA NOTAFISCAL
	 * COM OS PARÂMETROS DE PESQUISA
	 */
	verificarContaCorrenteCotaExistente : function() {

		var cota = $("#cota", contaCorrenteCotaController.workspace).val();
		
		var dadosPesquisa = {numeroCota:cota};
		
		$.postJSON(contextPath + "/financeiro/contaCorrenteCota/verificarContaCorrenteCotaExistente", 
					   dadosPesquisa,
					   contaCorrenteCotaController.confirmaContaCorrenteCotaEncontrada);

	},

	/**
	 * FAZ A PESQUISA DOS ITENS REFERENTES A CONTA CORRENTE COTA.
	 */
	pesquisarItemContaCorrenteCota : function() {

		var parametroPesquisa = [
                 {name:'filtroViewContaCorrenteCotaDTO.numeroCota', value: $("#cota", contaCorrenteCotaController.workspace).val() },
                 {name:'filtroViewContaCorrenteCotaDTO.inicioPeriodo', value:$("#periodoContaDe", contaCorrenteCotaController.workspace).val() },
                 {name:'filtroViewContaCorrenteCotaDTO.fimPeriodo', value:$("#periodoContaAte", contaCorrenteCotaController.workspace).val() }
		];

		$(".itemContaCorrenteCotaGrid", contaCorrenteCotaController.workspace).flexOptions({
			
			url : contextPath + '/financeiro/contaCorrenteCota/consultarContaCorrenteCota', params: parametroPesquisa
					
		});

		$(".itemContaCorrenteCotaGrid", contaCorrenteCotaController.workspace).flexReload();
		
		$("#bt_email", contaCorrenteCotaController.workspace).show();

		
	},

	/**
	 * FAZ A PESQUISA DE ENCALHE DA COTA EM UMA DETERMINADA DATA
	 */
	pesquisarEncalheCota : function(idConsolidado, dataConsolidado){
			
		var numeroCota = $("#cota", contaCorrenteCotaController.workspace).val();		
		
		var parametroPesquisa = [{name:'filtro.numeroCota', value:numeroCota },
		                         {name:'filtro.dataConsolidado', value:dataConsolidado },
		                         {name:'filtro.idConsolidado', value:idConsolidado}];
		
		$.postJSON(
			contextPath + '/financeiro/contaCorrenteCota/consultarEncalheCota', 
			parametroPesquisa,
			function(result){
				
				$(".encalheCotaGrid", contaCorrenteCotaController.workspace).flexAddData({
					page: 1, total: 1, rows: result[1].tableModelEncalhe.rows
				});
				
				var data = result[1];
				
				$("#datacotanome", contaCorrenteCotaController.workspace).html(dataConsolidado+" - Cota: "+
						$("#cota", contaCorrenteCotaController.workspace).val()+" - "+$("#nomeCota", contaCorrenteCotaController.workspace).val());
		
				var conteudoSpan = $("#listaInfoEncalhe", contaCorrenteCotaController.workspace).html("");
				
				$.each(data.listaInfoFornecedores, function(index, value) {
					conteudoSpan = $("#listaInfoEncalhe", contaCorrenteCotaController.workspace).html();
					$("#listaInfoEncalhe", contaCorrenteCotaController.workspace).html(conteudoSpan + 
							value.nomeFornecedor+":      " + floatToPrice(formatMoneyValue(value.valorTotal)) + "<br><br>");
			    });
				
				$(".encalheCotaGrid", contaCorrenteCotaController.workspace).show();
				$(".gridsEncalhe", contaCorrenteCotaController.workspace).show();
				$("#labelTotalGeral", contaCorrenteCotaController.workspace).text(floatToPrice(formatMoneyValue(result[2])));
			});
		contaCorrenteCotaController.popup_encalhe();
	},

	/**
	 * FAZ A PESQUISA DE CONSIGNADO DA COTA EM UMA DETERMINADA DATA
	 */
	pesquisarConsignadoCota : function(idConsolidado, dataConsolidado){
			
		var numeroCota = $("#cota", contaCorrenteCotaController.workspace).val();		
		
		var parametroPesquisa = [{name:'filtro.numeroCota', value:numeroCota },
		                         {name:'filtro.idConsolidado', value:idConsolidado },
		                         {name:'filtro.dataConsolidado', value:dataConsolidado}];
		
		$.postJSON(
			contextPath + '/financeiro/contaCorrenteCota/consultarConsignadoCota',
			parametroPesquisa,
			function(result){
				
				$(".consignadoCotaGrid", contaCorrenteCotaController.workspace).flexAddData({
					page: 1, total: 1, rows: result[1].tableModelConsignado.rows
				});
				
				var data = result[1];
				
				$("#datacotanome_consignado", contaCorrenteCotaController.workspace).html(dataConsolidado+" - Cota: "+
						$("#cota", contaCorrenteCotaController.workspace).val()+" - "+$("#nomeCota", contaCorrenteCotaController.workspace).val());
				
				var conteudoSpan = $("#listaInfoConsignado", contaCorrenteCotaController.workspace).html("");
				
				$.each(data.listaInfoFornecedores, function(index, value) {
			 	 	
			      conteudoSpan = $("#listaInfoConsignado", contaCorrenteCotaController.workspace).html();
			 	 	
			      $("#listaInfoConsignado", contaCorrenteCotaController.workspace).html(conteudoSpan + 
			    		  value.nomeFornecedor+":      " + floatToPrice(formatMoneyValue(value.valorTotal)) + "<br><br>");
			    });
				
				$(".consignadoCotaGrid", contaCorrenteCotaController.workspace).show();
				$(".gridsConsignado", contaCorrenteCotaController.workspace).show();
			});
		contaCorrenteCotaController.popup_consignado();
	},

	/**
	 * PREPARA OS DADOS A SEREM APRESENTADOS NA GRID.
	 */
	getDataFromResult : function(data) {
		
		if(typeof data.mensagens == "object") {
			
			$(".grids", contaCorrenteCotaController.workspace).hide();
		
			exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
			
		}else{			
			$.each(data.rows, function(index, value) {			
				var dataRaizPostergado =  value.cell.dataRaizConsolidado;			
				if(!dataRaizPostergado){
					dataRaizPostergado = value.cell.dataConsolidado;
				}
				var dataRaizPendente =  value.cell.dataRaiz;
				
				if(!dataRaizPendente){
					dataRaizPendente = value.cell.dataConsolidado;
				}
				
				value.cell.consignado = '<a href="javascript:;" onclick="contaCorrenteCotaController.pesquisarConsignadoCota('+
					[value.cell.id ? value.cell.id : '\'\'']+',\''+ value.cell.dataConsolidado +'\');"/>'+
					(value.cell.consignado != null ? floatToPrice(formatMoneyValue(value.cell.consignado)) : '0,00')+'</a>';
				
				value.cell.encalhe = '<a href="javascript:;" onclick="contaCorrenteCotaController.pesquisarEncalheCota('+
					[value.cell.id ? value.cell.id : '\'\'']+',\''+ value.cell.dataConsolidado +'\');"/>' + 
					floatToPrice(formatMoneyValue(value.cell.encalhe)) + '</a>';
				
				value.cell.valorVendaDia = floatToPrice(formatMoneyValue(value.cell.valorVendaDia));
				
				value.cell.vendaEncalhe = '<a href="javascript:;" onclick="vendaEncalhe.showDialog('+
					[value.cell.id ? value.cell.id : '\'\'']+',\''+value.cell.dataConsolidado+'\','+ $("#cota", contaCorrenteCotaController.workspace).val() +
					',\''+value.cell.nomeBox+'\');"/>' + floatToPrice(formatMoneyValue(value.cell.vendaEncalhe)) + '</a>';
				
				value.cell.debitoCredito = '<a href="javascript:;" onclick="contaCorrenteCotaController.popup_debitoCredito('+
					[value.cell.id ? value.cell.id : '\'\'']+',\''+value.cell.dataConsolidado+'\',\'' + value.cell.debitoCredito +'\');"/>' +
					floatToPrice(formatMoneyValue(value.cell.debitoCredito)) +'</a>';
				
				value.cell.encargos = '<a href="javascript:;" onclick="contaCorrenteCotaController.popup_encargos('+
					[value.cell.id ? value.cell.id : '\'\'']+',\''+value.cell.dataConsolidado +'\');"/>' + floatToPrice(formatMoneyValue(value.cell.encargos)) +'</a>';
					
				value.cell.valorPostergado = '<span class="bt_tool"><a rel="tipsy" title="Valor Referente à '+dataRaizPostergado+'">' +
					(value.cell.valorPostergado != null ? floatToPrice(formatMoneyValue(value.cell.valorPostergado)) : '0,00') + '</a></span>';
				
				value.cell.pendente = '<span class="bt_tool"><a rel="tipsy" title="Valor Referente à '+dataRaizPendente+'">' +
					floatToPrice(formatMoneyValue(value.cell.pendente ? value.cell.pendente : '0,00')) +'</a></span>';
				
				value.cell.total = floatToPrice(formatMoneyValue(value.cell.total));
				value.cell.valorPago = floatToPrice(formatMoneyValue(value.cell.valorPago));
				value.cell.saldo = floatToPrice(formatMoneyValue(value.cell.saldo));
				
				if (value.cell.tipo == 'CONSOLIDADO'){
					
					value.cell.tipo = '<img src="'+ contextPath +'/images/ico_check.gif"/>';
				} else {
					
					value.cell.tipo = '<img src="'+ contextPath +'/images/ico_excluir.gif"/>';
					value.cell.saldo = floatToPrice(formatMoneyValue(value.cell.total));
				}
			});
			
		
			$("#cotanomeselecionado", contaCorrenteCotaController.workspace).html($("#cota", contaCorrenteCotaController.workspace).val()+" "+
					$("#nomeCota", contaCorrenteCotaController.workspace).val());
			
			$("#msgFieldsetdebitosCreditos", contaCorrenteCotaController.workspace).
				text("Cota: " + $("#cota", contaCorrenteCotaController.workspace).val()+" "+$("#nomeCota", contaCorrenteCotaController.workspace).val());
			
			$("#msgFieldsetEncargos", contaCorrenteCotaController.workspace).
				text("Cota: " + $("#cota", contaCorrenteCotaController.workspace).val()+" "+$("#nomeCota", contaCorrenteCotaController.workspace).val());
			
			$(".grids", contaCorrenteCotaController.workspace).show();
			
			return data;
		}

	},
	
	preProcessEncalheCota : function (data){
		
		if(typeof data.mensagens == "object") {
			
			$(".grids", contaCorrenteCotaController.workspace).hide();
		
			exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
			
		}else{			
			$.each(data.rows, function(index, value) {
				
				value.cell.precoCapa = floatToPrice(formatMoneyValue(value.cell.precoCapa));
				value.cell.precoComDesconto = floatToPrice(formatMoneyValue(value.cell.precoComDesconto));
				value.cell.total = floatToPrice(formatMoneyValue(value.cell.total));
			});
			
			return data;
		}
	},
	
	/**
	 * ENVIA EMAIL
	 */
	enviarEmail : function(){
		
		var params = $('#form-email', contaCorrenteCotaController.workspace).serialize();

		$.postJSON(contextPath + '/financeiro/contaCorrenteCota/enviarEmail',
				params,
				function(result) {
					
				},
				function() {
					
				}
			
		);
		
	},
	
	/**
	 * POSSIBILITA EDITAR EMAIL
	 */
	editarEmail : function(){
		var editar = $("#emailCotaEmail");
		
		
		if(editar.is("[readonly]")){
			editar.removeAttr("readonly");
			
		}else{
			editar.attr("readonly", true);
		}
		
		
	},
	
	/**
	 * CARREGA DADOS DA COTA PARA O ENVIO DO EMAIL
	 */
	carregarDadosEmail : function(){
		
		var numeroCota = $("#cota", contaCorrenteCotaController.workspace).val();
		var nomeCota = $("#nomeCota", contaCorrenteCotaController.workspace).val();
		
		var parametroPesquisa = [{name:'numeroCota', value:numeroCota}];
		
		$("#numeroCotaEmail", contaCorrenteCotaController.workspace).val(numeroCota);
		$("#nomeCotaEmail", contaCorrenteCotaController.workspace).val(nomeCota);
	
		
		$.postJSON(contextPath + '/financeiro/contaCorrenteCota/pesquisarEmailCota',
				parametroPesquisa,
				function(result) {
					$("#emailCotaEmail").val(result);
				},
				function() {
					
				}
			
		);
		
	},


	/**
	 * ESTRUTURA DE COLUNAS DA GRID DE RESULTADO.
	 */
	carregarItemContaCorrenteCotaGrid : function() {
		
		$(".itemContaCorrenteCotaGrid", contaCorrenteCotaController.workspace).flexigrid({
			preProcess: contaCorrenteCotaController.getDataFromResult,
			dataType : 'json',
			colModel :  [ {
				display : 'Data',
				name : 'dataConsolidado',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Consignado',
				name : 'consignado',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Valor Venda Dia', 
				name : 'valorVendaDia',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Vlr. Postergado R$', 
				name : 'valorPostergado',
				width : 105,
				sortable : true,
				align : 'right'
			}, {
				display : 'Venda Encalhe',
				name : 'vendaEncalhe',
				width : 80,
				sortable : true,
				align : 'right',
			}, {
				display : 'Déb/Cred.',
				name : 'debitoCredito',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Encargos',
				name : 'encargos',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Pendente',
				name : 'pendente',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total R$',
				name : 'total',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Vlr.Pago R$',
				name : 'valorPago',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Saldo R$',
				name : 'saldo',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Cobrado',
				name : 'tipo',
				width : 65,
				sortable : true,
				align : 'center'
			}],
			sortname : "dataConsolidado",
			sortorder : "desc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 1050,
			height : 255,
			onSuccess : function () {
				$('.itemContaCorrenteCotaGrid tr td', contaCorrenteCotaController.workspace).each( function(){ 
					$('a', this).tipsy({gravity: 'sw'});
				});  
				return true;			
			}  

		});
	},


	montarColunaEncalheCota : function(){
		
		$(".encalheCotaGrid", contaCorrenteCotaController.workspace).flexigrid({		
			dataType : 'json',
			preProcess: contaCorrenteCotaController.preProcessEncalheCota,
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 130,
				sortable : true,
				align : 'right'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 95,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço c/ Desc. R$',
				name : 'precoComDesconto',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 70,
				sortable : true,
				align : 'right',
			}, {
				display : 'Fornecedor',
				name : 'nomeFornecedor',
				width : 100,
				sortable : false,
				align : 'right',		
			}, {
				display : 'Total R$',
				name : 'total',
				width : 80,
				sortable : true,
				align : 'right'
			}],
			sortname : "codigoProduto",
			sortorder : "asc",
			width : 800,
			height : 200
		});
	},

	montarColunaConsignado : function(){

	$(".consignadoCotaGrid", contaCorrenteCotaController.workspace).flexigrid({
		preProcess : function(data) {
			$.each(data.rows, function(index, value) {
				if(!value.cell.motivo){
					value.cell.motivo = "";
				}
				
				value.cell.precoCapa = floatToPrice(formatMoneyValue(value.cell.precoCapa));
				value.cell.precoComDesconto = floatToPrice(formatMoneyValue(value.cell.precoComDesconto));
				value.cell.total = floatToPrice(formatMoneyValue(value.cell.total));
			});
			
			return data;
		},
		dataType : 'json',	
		colModel : [ {
			display : 'Código',
			name : 'codigoProduto',
			width : 40,
			sortable : true,
			align : 'left'
		}, {
			display : 'Produto',
			name : 'nomeProduto',
			width : 90,
			sortable : true,
			align : 'left'
		}, {
			display : 'Edição',
			name : 'numeroEdicao',
			width : 40,
			sortable : true,
			align : 'center'
		}, {
			display : 'Preço Capa R$',
			name : 'precoCapa',
			width : 80,
			sortable : true,
			align : 'right',
		}, {
			display : 'Preço c/ Desc. R$',
			name : 'precoComDesconto',
			width : 60,
			sortable : true,
			align : 'right',
		}, {
			display : 'Reparte Sugerido',
			name : 'reparteSugerido',
			width : 82,
			sortable : true,
			align : 'center'
		}, {
			display : 'Reparte Final',
			name : 'reparteFinal',
			width : 70,
			sortable : true,
			align : 'center'
		}, {
			display : 'Diferença',
			name : 'diferenca',
			width : 45,
			sortable : true,
			align : 'center'
		}, {
			display : 'Motivo',
			name : 'motivo',
			width : 80,
			sortable : true,
			align : 'left'
		}, {
			display : 'Fornecedor',
			name : 'nomeFornecedor',
			width : 60,
			sortable : true,
			align : 'left'
		}, {
			display : 'Total R$',
			name : 'total',
			width : 50,
			sortable : true,
			align : 'right'
		}],
		sortname : "codigo",
		sortorder : "asc",
		width : 860,
		height : 200
	});

	},

	popup_consignado : function() {
		
		$( "#dialog-consignado", contaCorrenteCotaController.workspace ).dialog({
			resizable: false,
			height:490,
			width:900,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".gridsConsignado", contaCorrenteCotaController.workspace).show();
					
				},
				
			},
			form: $("#dialog-consignado", this.workspace).parents("form")
		});
	},
		
	popup_encalhe : function() {
		
		$( "#dialog-encalhe", contaCorrenteCotaController.workspace ).dialog({
			resizable: false,
			height:460,
			width:860,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".gridsEncalhe", contaCorrenteCotaController.workspace).show();
					
				}
			},
			form: $("#dialog-encalhe", this.workspace).parents("form")
		});
	},
			
	popup_contaCorrente : function() {
		
		$( "#dialog-conta", contaCorrenteCotaController.workspace ).dialog({
			resizable: false,
			height:340,
			width:660,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".grids", contaCorrenteCotaController.workspace).show();
					
				}
			},
			form: $("#dialog-conta", this.workspace).parents("form")
		});
	},
		
	popup_encargos : function() {
		
		$( "#dialog-encargos", contaCorrenteCotaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:450,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".grids", contaCorrenteCotaController.workspace).show();
					
				}
			},
			form: $("#dialog-encargos", this.workspace).parents("form")
		});
	},
	
	popup_email : function() {
		
		contaCorrenteCotaController.carregarDadosEmail();
		$("#dialog-email", contaCorrenteCotaController.workspace ).dialog({
			resizable: false,
			height:400,
			width:490,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					contaCorrenteCotaController.enviarEmail();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-email", this.workspace).parents("form")
		});	
		$("#copiaParaCotaEmail").val("");
		$("#mensagemCotaEmail").val("");
		$("#emailCotaEmail").attr("readonly", true);
	},
	
	montarGridDebitoCredio : function(){
		$(".debitoCreditoCotaGrid", contaCorrenteCotaController.workspace).flexigrid({
			preProcess : function(data){
				
				$.each(data.rows, function(index, value) {
					
					value.cell.dataCriacao = value.cell.dataLancamento;
					value.cell.observacoes = value.cell.observacoes ? value.cell.observacoes : '';
					value.cell.valor = floatToPrice(formatMoneyValue(value.cell.valor));
				});
				
				return data;
			},
			dataType : 'json',	
			colModel : [ {
				display : 'Data',
				name : 'dataLancamento',
				width : 80,
				sortable : true,
				align : 'left'
			},{
				display : 'Tipo Movimento',
				name : 'tipoMovimento',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Observação',
				name : 'observacoes',
				width : 290,
				sortable : true,
				align : 'left'
			}],
			width : 600,
			height : 120,
			sortname : "dataLancamento",
			sortorder : "asc"
		});
		
		$(".debitoCreditoCotaGrid", contaCorrenteCotaController.workspace).flexOptions({
			url : contextPath + '/financeiro/contaCorrenteCota/consultarDebitoCreditoCota'
		});
	},
	
	popup_debitoCredito : function(idConsolidado, dataConsolidado, valorTotal){
		
		var dadosPesquisa = [
		   {name:'idConsolidado', value: idConsolidado},
		   {name:'data', value:dataConsolidado},
		   {name:'numeroCota', value: $("#cota", contaCorrenteCotaController.workspace).val()}
		];
		
		$.postJSON(contextPath + "/financeiro/contaCorrenteCota/consultarDebitoCreditoCota",
			dadosPesquisa,
			function (result){
				
				var texto = $("#msgFieldsetdebitosCreditos", contaCorrenteCotaController.workspace).text();
				$("#msgFieldsetdebitosCreditos", contaCorrenteCotaController.workspace).text(dataConsolidado + " - " + texto);
				$("#valorTotalDebitoCredito", contaCorrenteCotaController.workspace).text(floatToPrice(formatMoneyValue(valorTotal)));
			
				contaCorrenteCotaController.montarGridDebitoCredio();
				$("#dialog-debitos-creditos", contaCorrenteCotaController.workspace ).dialog({
					resizable: false,
					height:340,
					width:660,
					modal: true,
					buttons: {
						"Fechar": function() {
							$( this ).dialog( "close" );
							
							$(".debitoCreditoGrid", contaCorrenteCotaController.workspace).show();
						},
					},
					form: $("#dialog-debitos-creditos", this.workspace).parents("form"),
					close: function(event, ui) {
						$("#msgFieldsetdebitosCreditos", contaCorrenteCotaController.workspace).text(texto);
					}
				});
				
				$(".debitoCreditoCotaGrid", contaCorrenteCotaController.workspace).flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});
				
				contaCorrenteCotaController.idConsolidadoDebitoCredito = idConsolidado;
				contaCorrenteCotaController.dataDebitoCredito = dataConsolidado;
				
				$(".debitoCreditoCotaGrid", contaCorrenteCotaController.workspace).flexOptions({
					params : dadosPesquisa
				});
			}
		);
	},
	
	idConsolidadoDebitoCredito : null,
	dataDebitoCredito : null,
	
	exportarDebitoCredito : function(fyleType){
		
		var params = "fileType=" + fyleType + 
			"&idConsolidado=" + contaCorrenteCotaController.idConsolidadoDebitoCredito + 
			"&data=" + contaCorrenteCotaController.dataDebitoCredito + 
			"&sortname=" + $(".debitoCreditoCotaGrid", contaCorrenteCotaController.workspace).flexGetSortName() +
			"&sortorder=" + $(".debitoCreditoCotaGrid", contaCorrenteCotaController.workspace).getSortOrder();
		
		window.open(contextPath + "/financeiro/contaCorrenteCota/exportarDebitoCreditoCota?"+ params);
	},
	
	popup_encargos : function(idConsolidado, dataConsolidado){
		
		var dadosPesquisa = [
		   {name:'idConsolidado', value: idConsolidado},
		   {name:'data', value:dataConsolidado},
		   {name:'numeroCota', value:$("#cota", contaCorrenteCotaController.workspace).val()}
		];
		
		$.postJSON(contextPath + "/financeiro/contaCorrenteCota/consultarEncargosCota",
			dadosPesquisa,
			function (result){
				
				var texto = $("#msgFieldsetEncargos", contaCorrenteCotaController.workspace).text();
				$("#msgFieldsetEncargos", contaCorrenteCotaController.workspace).text(dataConsolidado + " - " + texto);
				
				$("#txtEncargosJuros", contaCorrenteCotaController.workspace).text(result[0]);
				$("#txtEncargosMulta", contaCorrenteCotaController.workspace).text(result[1]);
				
				$("#dialog-encargos", contaCorrenteCotaController.workspace ).dialog({
					resizable: false,
					height:'auto',
					width:450,
					modal: true,
					buttons: {
						"Fechar": function() {
							$( this ).dialog( "close" );
						},
					},
					form: $("#dialog-encargos", this.workspace).parents("form")
				});
			}
		);
	},
	
	irParaNegociacao : function(){
		
		$('#workspace').tabs('addTab', "Negociação de Divida",
				contextPath + "/financeiro/negociacaoDivida" + "?random=" + Math.random());
	}
}, BaseController);

//@ sourceURL=contaCorrente.js
