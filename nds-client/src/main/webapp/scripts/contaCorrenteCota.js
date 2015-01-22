var contaCorrenteCotaController = $.extend(true, {

	/*
	 * O JAVASCRIPT ABAIXO E O PRIMEIRO A SER EXECUTADO 
	 * QUANDO A PAGINA CARREGA.
	 */
	init : function() {
		$("#cc-cota", contaCorrenteCotaController.workspace).numeric();
		
		$("#cc-nomeCota", contaCorrenteCotaController.workspace).autocomplete({source: ""});
		
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
	 * FAZ A PESQUISA DOS ITENS REFERENTES A CONTA CORRENTE COTA.
	 */
	pesquisarItemContaCorrenteCota : function() {

		var numeroCota = $("#cc-cota", contaCorrenteCotaController.workspace).val();
		
		var nomeCota = $("#cc-nomeCota", contaCorrenteCotaController.workspace).val();
		
		$("#cotaHidden", contaCorrenteCotaController.workspace).val(numeroCota);
		
		$("#nomeCotaHidden", contaCorrenteCotaController.workspace).val(nomeCota);
		
		var parametroPesquisa = [
                 {name:'filtroViewContaCorrenteCotaDTO.numeroCota', value: numeroCota },
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
			
		var numeroCota = $("#cotaHidden", contaCorrenteCotaController.workspace).val();		
		
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
						$("#cotaHidden", contaCorrenteCotaController.workspace).val()+" - "+$("#nomeCotaHidden", contaCorrenteCotaController.workspace).val());
		
				var conteudoSpan = $("#listaInfoEncalhe", contaCorrenteCotaController.workspace).html("");
				
				$.each(data.listaInfoFornecedores, function(index, value) {
					conteudoSpan = $("#listaInfoEncalhe", contaCorrenteCotaController.workspace).html();
					$("#listaInfoEncalhe", contaCorrenteCotaController.workspace).html(conteudoSpan + 
							value.nomeFornecedor+":      " + (formatMoneyValue(value.valorTotal)) + "<br><br>");
			    });
				
				$(".encalheCotaGrid", contaCorrenteCotaController.workspace).show();
				$(".gridsEncalhe", contaCorrenteCotaController.workspace).show();
				$("#labelTotalGeral", contaCorrenteCotaController.workspace).text((formatMoneyValue(result[2])));
			});
		contaCorrenteCotaController.popup_encalhe();
	},

	/**
	 * FAZ A PESQUISA DE CONSIGNADO DA COTA EM UMA DETERMINADA DATA
	 */
	pesquisarConsignadoCota : function(idConsolidado, dataConsolidado){
			
		var numeroCota = $("#cotaHidden", contaCorrenteCotaController.workspace).val();		
		
		var parametroPesquisa = [{name:'filtro.numeroCota', value:numeroCota },
		                         {name:'filtro.idConsolidado', value:idConsolidado },
		                         {name:'filtro.dataConsolidado', value:dataConsolidado},
		                         {name:'sortname', value:'sequencia'}];

		$.postJSON(
			contextPath + '/financeiro/contaCorrenteCota/consultarConsignadoCota',
			parametroPesquisa,
			function(result){
				
				$(".consignadoCotaGrid", contaCorrenteCotaController.workspace).flexAddData({
					page: 1, total: 1, rows: result[1].tableModelConsignado.rows
				});
				
				var data = result[1];
				
				$("#datacotanome_consignado", contaCorrenteCotaController.workspace).html(dataConsolidado+" - Cota: "+
						$("#cotaHidden", contaCorrenteCotaController.workspace).val()+" - "+$("#nomeCotaHidden", contaCorrenteCotaController.workspace).val());
				
				var conteudoSpan = $("#listaInfoConsignado", contaCorrenteCotaController.workspace).html("");
				
				$.each(data.listaInfoFornecedores, function(index, value) {
			 	 	
			      conteudoSpan = $("#listaInfoConsignado", contaCorrenteCotaController.workspace).html();
			 	 	
			      $("#listaInfoConsignado", contaCorrenteCotaController.workspace).html(conteudoSpan + 
			    		  value.nomeFornecedor+":      " + (formatMoneyValue(value.valorTotal)) + "<br><br>");
			    });
				
				$(".consignadoCotaGrid", contaCorrenteCotaController.workspace).show();
				$(".gridsConsignado", contaCorrenteCotaController.workspace).show();
				$("#totalGeralConsignado", contaCorrenteCotaController.workspace).text((formatMoneyValue(result[2])));
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
				
				var dataRaizPostergado =  value.cell.dataRaiz;	
				
				if(!dataRaizPostergado){
					
					dataRaizPostergado = value.cell.dataConsolidado;
				}
				
				var dataRaizPendente =  value.cell.dataPendente;
								
				var hintNumeroAcumulo = ((value.cell.numeroAcumulo && value.cell.numeroAcumulo > 0) ? 
						".\n " + value.cell.numeroAcumulo + "º acúmulo." : ""); 
				
				if(!dataRaizPendente){
					
					dataRaizPendente = value.cell.dataConsolidado;
				}
				
				value.cell.consignado = (value.cell.consignado != null && value.cell.consignado != 0)?'<a href="javascript:;" onclick="contaCorrenteCotaController.pesquisarConsignadoCota('+
										[value.cell.id ? value.cell.id : '\'\'']+',\''+ value.cell.dataConsolidado +'\');"/>'+
										(formatMoneyValue(value.cell.consignado))+'</a>' : '0.0000';
				
				value.cell.encalhe = (value.cell.encalhe != null && value.cell.encalhe != 0)?'<a href="javascript:;" onclick="contaCorrenteCotaController.pesquisarEncalheCota('+
									 [value.cell.id ? value.cell.id : '\'\'']+',\''+ value.cell.dataConsolidado +'\');"/>' + 
									 (formatMoneyValue(value.cell.encalhe * -1)) + '</a>' : '0.0000';
				
				value.cell.valorVendaDia = (value.cell.valorVendaDia != null && value.cell.valorVendaDia != 0)?
	                          '<a href="javascript:;" onclick="contaCorrenteCotaController.popup_valorVendaDia(' +
	                          [value.cell.id ? value.cell.id : '\'\'']+',\''+value.cell.dataConsolidado+'\',\'' + value.cell.debitoCredito * -1 +'\');"/>' + 
	                          (formatMoneyValue(value.cell.valorVendaDia)) : '0.0000'; 
					                          

				value.cell.vendaEncalhe = (value.cell.vendaEncalhe != null && value.cell.vendaEncalhe != 0)?'<a href="javascript:;" onclick="vendaEncalhe.showDialog('+
										  [value.cell.id ? value.cell.id : '\'\'']+',\''+value.cell.dataConsolidado+'\','+ $("#cotaHidden", contaCorrenteCotaController.workspace).val() +
										  ',\''+value.cell.nomeBox+'\');"/>' + (formatMoneyValue(Math.abs(value.cell.vendaEncalhe))) + '</a>' : '0.0000';
				
				if (value.cell.detalharDebitoCredito){
					
					var valor = (value.cell.debitoCredito != null && value.cell.debitoCredito != 0) ? value.cell.debitoCredito * -1 : '0.0000';
					
					value.cell.debitoCredito = '<a href="javascript:;" onclick="contaCorrenteCotaController.popup_debitoCredito('+
							   [value.cell.id ? value.cell.id : '\'\'']+',\''+value.cell.dataConsolidado+'\',\'' + valor +'\');"/>' +
								(formatMoneyValue(valor)) +'</a>';
				} else {
					
					value.cell.debitoCredito = '0.0000';
				}
										  
				value.cell.encargos = (value.cell.encargos != null && value.cell.encargos != 0)?'<a href="javascript:;" onclick="contaCorrenteCotaController.popup_encargos('+
									  [value.cell.id ? value.cell.id : '\'\'']+',\''+value.cell.dataConsolidado +'\');"/>' + 
									  (formatMoneyValue(Math.abs(value.cell.encargos))) +'</a>' : '0.0000';
				
				var hint = value.cell.valorPostergado && value.cell.valorPostergado != 0 ? '<a rel="tipsy" title="Valor Referente à '+ 
						dataRaizPostergado + '">' : "";
				
			    value.cell.valorPostergado = (value.cell.valorPostergado != null && value.cell.valorPostergado != 0)?'<span class="bt_tool">' + hint +
					                          (formatMoneyValue(value.cell.valorPostergado * -1)) + '</a></span>' : '0.0000';

				hint = value.cell.pendente && value.cell.pendente != 0 ? '<a rel="tipsy" title="Valor Referente à '+ dataRaizPendente + hintNumeroAcumulo + '">' : "";
				       
				value.cell.pendente = (value.cell.pendente != null && value.cell.pendente != 0)?'<span class="bt_tool">' + hint +
					                   (formatMoneyValue(Math.abs(value.cell.pendente), 2)) +'</a></span>' : '0.00';
				
				value.cell.total = (value.cell.total != null && value.cell.total != 0)?
						           (formatMoneyValue(value.cell.total * -1, 2)) : '0.00'; 				
				
				value.cell.valorPago = (value.cell.valorPago != null && value.cell.valorPago != 0)?
						               (formatMoneyValue(value.cell.valorPago, 2)) : '0.00'; 
				
						               
				value.cell.saldo = (value.cell.saldo != null && formatMoneyValue(value.cell.saldo, 2) != 0)?
                                   (formatMoneyValue(value.cell.saldo * -1, 2)) : '0.00'; 
               
                if(value.cell.statusDivida && (value.cell.statusDivida == "NEGOCIADA" || value.cell.statusDivida == "POSTERGADA")) {	
                	
                	value.cell.cobrado = '<img src="'+ contextPath +'/images/hammer.png" alt="Dívida Negociada"/>';
                	
                } else if (value.cell.inadimplente) {
				
            	   value.cell.cobrado = '<img src="'+ contextPath +'/images/seta_sobe.gif" alt="Dívida Acumulada"/>';
               
               } else if (value.cell.cobrado){
					
					value.cell.cobrado = '<img src="'+ contextPath +'/images/bt_financeiro.png" alt="Cobrança Gerada" />';
				
				} else {
					
					value.cell.cobrado = '<img src="'+ contextPath +'/images/ico_boletos.gif" alt="Dívida Postergada" />';
					value.cell.saldo = (value.cell.total != null && value.cell.total != 0)?
					                   (formatMoneyValue(value.cell.total, 2)) : '0.00';
				}
			});
			
			$("#cotanomeselecionado", contaCorrenteCotaController.workspace).html($("#cotaHidden", contaCorrenteCotaController.workspace).val()+" "+
					$("#nomeCotaHidden", contaCorrenteCotaController.workspace).val());
			
			$("#msgFieldsetdebitosCreditos", contaCorrenteCotaController.workspace).
				text("Cota: " + $("#cotaHidden", contaCorrenteCotaController.workspace).val()+" "+$("#nomeCotaHidden", contaCorrenteCotaController.workspace).val());
			
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
				
				if(!value.cell.sequencia){
					value.cell.sequencia ="Postergado";
				}
				value.cell.precoCapa = formatMoneyValue(value.cell.precoCapa, 2);
				value.cell.precoComDesconto = formatMoneyValue(value.cell.precoComDesconto);
				value.cell.total = formatMoneyValue(value.cell.total);
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
		
		var numeroCota = $("#cotaHidden", contaCorrenteCotaController.workspace).val();
		var nomeCota = $("#nomeCotaHidden", contaCorrenteCotaController.workspace).val();
		
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
				name : 'cobrado',
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
				display : 'Sequência',
				name : 'sequencia',
				width : 50,
				sortable : false,
				align : 'left'
			}, {
				display : 'Código',
				name : 'codigoProduto',
				width : 50,
				sortable : false,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 130,
				sortable : false,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 70,
				sortable : false,
				align : 'right'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 95,
				sortable : false,
				align : 'right'
			}, {
				display : 'Preço c/ Desc. R$',
				name : 'precoComDesconto',
				width : 70,
				sortable : false,
				align : 'right'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 70,
				sortable : false,
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
				sortable : false,
				align : 'right'
			}],
			sortname : "sequencia",
			sortorder : "asc",
			width : 850,
			height : 200
		});
	},

	montarColunaConsignado : function(){

	$(".consignadoCotaGrid", contaCorrenteCotaController.workspace).flexigrid({
		preProcess : function(data) {
			$.each(data.rows, function(index, value) {
				if(!value.cell.motivoTexto){
					value.cell.motivo = "";
					value.cell.motivoTexto = "";
				}
				
				value.cell.precoCapa = formatMoneyValue(value.cell.precoCapa, 2);
				value.cell.precoComDesconto = formatMoneyValue(value.cell.precoComDesconto);
				value.cell.total = formatMoneyValue(value.cell.total);
			});
			
			return data;
		},
		dataType : 'json',	
		colModel : [{
			display : 'SM',
			name : 'sequencia',
			width : 50,
			sortable : false,
			align : 'left'
		}, {
			display : 'Código',
			name : 'codigoProduto',
			width : 40,
			sortable : false,
			align : 'left'
		}, {
			display : 'Produto',
			name : 'nomeProduto',
			width : 90,
			sortable : false,
			align : 'left'
		}, {
			display : 'Edição',
			name : 'numeroEdicao',
			width : 40,
			sortable : false,
			align : 'center'
		}, {
			display : 'Preço Capa R$',
			name : 'precoCapa',
			width : 80,
			sortable : false,
			align : 'right',
		}, {
			display : 'Preço c/ Desc. R$',
			name : 'precoComDesconto',
			width : 60,
			sortable : false,
			align : 'right',
		}, {
			display : 'Reparte Sugerido',
			name : 'reparteSugerido',
			width : 82,
			sortable : false,
			align : 'center'
		}, {
			display : 'Reparte Final',
			name : 'reparteFinal',
			width : 70,
			sortable : false,
			align : 'center'
		}, {
			display : 'Diferença',
			name : 'diferenca',
			width : 45,
			sortable : false,
			align : 'center'
		}, {
			display : 'Motivo',
			name : 'motivoTexto',
			width : 80,
			sortable : false,
			align : 'left'
		}, {
			display : 'Fornecedor',
			name : 'nomeFornecedor',
			width : 60,
			sortable : false,
			align : 'left'
		}, {
			display : 'Total R$',
			name : 'total',
			width : 50,
			sortable : false,
			align : 'right'
		}],
		sortname : "sequencia",
		sortorder : "asc",
		width : 900,
		height : 200
	});

	},

	popup_consignado : function() {
		
		$( "#dialog-consignado", contaCorrenteCotaController.workspace ).dialog({
			resizable: false,
			height:490,
			width:940,
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
			height:470,
			width:890,
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
					value.cell.valor = formatMoneyValue(value.cell.valor);
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
	},
	
	montarGridValorVendaDia : function(){
		$(".valorVendaDiaGrid", contaCorrenteCotaController.workspace).flexigrid({
			preProcess : function(data){
				
				$.each(data.rows, function(index, value) {
					
					value.cell.dataLancamento = value.cell.data;
					value.cell.observacoes = value.cell.descricao;
					value.cell.valor = formatMoneyValue(value.cell.valor);
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
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Observação',
				name : 'observacoes',
				width : 390,
				sortable : true,
				align : 'left'
			}],
			width : 600,
			height : 190,
			sortname : "dataLancamento",
			sortorder : "asc"
		});
	},
	
	popup_debitoCredito : function(idConsolidado, dataConsolidado, valorTotal){
		
		var dadosPesquisa = [
		   {name:'idConsolidado', value: idConsolidado},
		   {name:'data', value:dataConsolidado},
		   {name:'numeroCota', value: $("#cotaHidden", contaCorrenteCotaController.workspace).val()}
		];
		
		$.postJSON(contextPath + "/financeiro/contaCorrenteCota/consultarDebitoCreditoCota",
			dadosPesquisa,
			function (result){
				
				var texto = $("#msgFieldsetdebitosCreditos", contaCorrenteCotaController.workspace).text();
				$("#msgFieldsetdebitosCreditos", contaCorrenteCotaController.workspace).text(dataConsolidado + " - " + texto);
				$("#valorTotalDebitoCredito", contaCorrenteCotaController.workspace).text(formatMoneyValue(valorTotal));
			
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
	
	popup_valorVendaDia : function(idConsolidado, dataConsolidado, valorTotal){
		
		var dadosPesquisa = [
		   {name:'idConsolidado', value: idConsolidado},
		   {name:'data', value:dataConsolidado},
		   {name:'numeroCota', value: $("#cotaHidden", contaCorrenteCotaController.workspace).val()}
		];
		
		$.postJSON(contextPath + "/financeiro/contaCorrenteCota/consultarValorVendaDia",
			dadosPesquisa,
			function (result){
				
				contaCorrenteCotaController.montarGridValorVendaDia();
				$("#dialog-valor-venda-dia", contaCorrenteCotaController.workspace ).dialog({
					resizable: false,
					height:340,
					width:660,
					modal: true,
					buttons: {
						"Fechar": function() {
							$( this ).dialog( "close" );
							
							$(".valorVendaDiaGrid", contaCorrenteCotaController.workspace).show();
						},
					},
					form: $("#dialog-valor-venda-dia", this.workspace).parents("form"),
				});
				
				$(".valorVendaDiaGrid", contaCorrenteCotaController.workspace).flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});
				
				$(".valorVendaDiaGrid", contaCorrenteCotaController.workspace).flexOptions({
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
			"&numeroCota=" + $("#cotaHidden", contaCorrenteCotaController.workspace).val() + 
			"&sortname=" + $(".debitoCreditoCotaGrid", contaCorrenteCotaController.workspace).flexGetSortName() +
			"&sortorder=" + $(".debitoCreditoCotaGrid", contaCorrenteCotaController.workspace).getSortOrder();
		
		window.open(contextPath + "/financeiro/contaCorrenteCota/exportarDebitoCreditoCota?"+ params);
	},
	
	popup_encargos : function(idConsolidado, dataConsolidado){
		
		var dadosPesquisa = [
		   {name:'idConsolidado', value: idConsolidado},
		   {name:'data', value:dataConsolidado},
		   {name:'numeroCota', value:$("#cotaHidden", contaCorrenteCotaController.workspace).val()}
		];
		
		$.postJSON(contextPath + "/financeiro/contaCorrenteCota/consultarEncargosCota",
			dadosPesquisa,
			function (result){

				var texto = "Cota: " + $("#cotaHidden", contaCorrenteCotaController.workspace).val()
									 + " "
									 + $("#nomeCotaHidden", contaCorrenteCotaController.workspace).val();

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

//@ sourceURL=contaCorrenteCota.js
