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
		
		var dadosPesquisa = 
			"numeroCota=" 			+ cota;			
		
		//limparCampos();
		
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
	pesquisarEncalheCota : function(lineId){
			
		var numeroCota = $("#cota", contaCorrenteCotaController.workspace).val();		
		
		var parametroPesquisa = [{name:'filtroConsolidadoEncalheDTO.numeroCota', value:numeroCota },
		                         {name:'filtroConsolidadoEncalheDTO.lineId', value:lineId }];
		
		$.postJSON(
				contextPath + '/financeiro/contaCorrenteCota/consultarEncalheCota', 
				parametroPesquisa,
				function(result){
					
					$(".encalheCotaGrid", contaCorrenteCotaController.workspace).flexToggleCol(7,result[0]);			
					
					$(".encalheCotaGrid", contaCorrenteCotaController.workspace).flexAddData({
						page: 1, total: 1, rows: result[1].tableModelEncalhe.rows
					});
					
					
					/////////////////////////////////////
					
					var data = result[1];
					
					$("#datacotanome", contaCorrenteCotaController.workspace).html(data.dataEscolhida+" Cota: "+$("#cota", contaCorrenteCotaController.workspace).val()+" - "+$("#nomeCota", contaCorrenteCotaController.workspace).val());
			
					var conteudoSpan = $("#listaInfoEncalhe", contaCorrenteCotaController.workspace).html("");
					
					$.each(data.listaInfoFornecedores, function(index, value) {
				 	 	
				      conteudoSpan = $("#listaInfoEncalhe", contaCorrenteCotaController.workspace).html();
				 	 	
				
				      $("#listaInfoEncalhe", contaCorrenteCotaController.workspace).html(conteudoSpan + value.nomeFornecedor+":      "+value.valorTotal+"<br><br>");
				    });
					
					
					////////////////////////////////////
					
					$(".encalheCotaGrid", contaCorrenteCotaController.workspace).show();
					
								
					$(".gridsEncalhe", contaCorrenteCotaController.workspace).show();
					
				});	
				
		contaCorrenteCotaController.popup_encalhe();
		
	},

	/**
	 * FAZ A PESQUISA DE CONSIGNADO DA COTA EM UMA DETERMINADA DATA
	 */
	pesquisarConsignadoCota : function(lineId){
			
		var numeroCota = $("#cota", contaCorrenteCotaController.workspace).val();		
		
		var parametroPesquisa = [{name:'filtroConsolidadoConsignadoCotaDTO.numeroCota', value:numeroCota },
		                         {name:'filtroConsolidadoConsignadoCotaDTO.lineId', value:lineId }];
		
		$.postJSON(
				contextPath + '/financeiro/contaCorrenteCota/consultarConsignadoCota', 
				parametroPesquisa,
				function(result){
					
					
					$(".consignadoCotaGrid", contaCorrenteCotaController.workspace).flexToggleCol(10,result[0]);
								
					$(".consignadoCotaGrid", contaCorrenteCotaController.workspace).flexAddData({
						page: 1, total: 1, rows: result[1].tableModelConsignado.rows
					});
					
					
					/////////////////////////////////////
					
					var data = result[1];
					
					$("#datacotanome_consignado", contaCorrenteCotaController.workspace).html(data.dataEscolhida+" Cota: "+$("#cota", contaCorrenteCotaController.workspace).val()+" - "+$("#nomeCota", contaCorrenteCotaController.workspace).val());
			
					var conteudoSpan = $("#listaInfoConsignado", contaCorrenteCotaController.workspace).html("");
					
					$.each(data.listaInfoFornecedores, function(index, value) {
				 	 	
				      conteudoSpan = $("#listaInfoConsignado", contaCorrenteCotaController.workspace).html();
				 	 	
				      $("#listaInfoConsignado", contaCorrenteCotaController.workspace).html(conteudoSpan + value.nomeFornecedor+":      "+value.valorTotal+"<br><br>");
				    });
					
					
					////////////////////////////////////
					
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
				var dataRaizPendente =  value.cell.dataRaizPendente;
				
				if(!dataRaizPendente){
					dataRaizPendente = value.cell.dataConsolidado;
				}			
									
				value.cell.consignado = '<a href="javascript:;" onclick="contaCorrenteCotaController.pesquisarConsignadoCota('+[value.cell.id]+');"/>'+value.cell.consignado+'</a>';
				value.cell.encalhe = '<a href="javascript:;" onclick="contaCorrenteCotaController.pesquisarEncalheCota('+[value.cell.id]+');"/>'+value.cell.encalhe+'</a>';
				value.cell.vendaEncalhe = '<a href="javascript:;" onclick="vendaEncalhe.showDialog('+value.cell.id+',\''+value.cell.dataConsolidado+'\')"/>'+value.cell.vendaEncalhe+'</a>';
				value.cell.debitoCredito = '<a href="javascript:;"/>'+value.cell.debitoCredito+'</a>';
				value.cell.encargos = '<a href="javascript:;"/>'+value.cell.encargos+'</a>';
					
				value.cell.valorPostergado = '<span class="bt_tool"><a rel="tipsy" title="Valor Referente à '+dataRaizPostergado+'" href="javascript:;">' +value.cell.valorPostergado +'</a></span>';
				value.cell.pendente = '<span class="bt_tool"><a rel="tipsy" title="Valor Referente à '+dataRaizPendente+'" href="javascript:;">' +value.cell.pendente +'</a></span>';
						
			});
			
		
			$("#cotanome", contaCorrenteCotaController.workspace).html($("#cota", contaCorrenteCotaController.workspace).val()+" "+$("#nomeCota", contaCorrenteCotaController.workspace).val());
			$(".grids", contaCorrenteCotaController.workspace).show();
			
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
				name : 'encalhe',
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
			}],
			sortname : "dataConsolidado",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
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
		width : 820,
		height : 200
	});

	},

	popup_consignado : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-consignado", contaCorrenteCotaController.workspace ).dialog({
			resizable: false,
			height:490,
			width:860,
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
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
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

	popup_encalhe_2 : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-encalhe_2", contaCorrenteCotaController.workspace ).dialog({
			resizable: false,
			height:460,
			width:860,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".grids", contaCorrenteCotaController.workspace).show();
					
				}
			},
			form: $("#dialog-encalhe_2", this.workspace).parents("form")
		});
	},
			
	popup_contaCorrente : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
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
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
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
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
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
	}
	
}, BaseController);
