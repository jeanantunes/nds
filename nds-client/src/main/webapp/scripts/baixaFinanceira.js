var baixaFinanceiraController = $.extend(true, {
	
	dataOperacaoDistribuidor: null,
	tipoBaixa: null,
	acaoPesquisa:null, 
	
	init : function() {
		$("#filtroNumCota", baixaFinanceiraController.workspace).numeric();
		$("#descricaoCota", baixaFinanceiraController.workspace).autocomplete({source: ""});
		$("filtroNossoNumero", baixaFinanceiraController.workspace).numeric();
		
		$("#dataBaixa", baixaFinanceiraController.workspace).datepicker({
			showOn : "button",
			buttonImage : contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat : 'dd/mm/yy'
		});

		$("#dataBaixa", baixaFinanceiraController.workspace).change(function(){
			
			$('#resultadoIntegracao', baixaFinanceiraController.workspace).hide();
			
			$("#valorFinanceiro", baixaFinanceiraController.workspace).val("");
			
		});
		
		
		$("#dtPagamentoManual", baixaFinanceiraController.workspace).datepicker({
			showOn : "button",
			buttonImage : contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat : 'dd/mm/yy'
		});
		
		$("#dtPagamentoManualBoleto", baixaFinanceiraController.workspace).datepicker({
			showOn : "button",
			buttonImage : contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat : 'dd/mm/yy', 
		});
		
		this.initGradeDividas();
		this.initGridDadosDivida();

		$( "#dtPostergada", baixaFinanceiraController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#ecargosPostergacao", baixaFinanceiraController.workspace).numeric();
		
		var options = {
			success: baixaFinanceiraController.tratarRespostaBaixaAutomatica,
	    };
		
		$('#formBaixaAutomatica', baixaFinanceiraController.workspace).ajaxForm(options);
		
		$("#valorFinanceiro", baixaFinanceiraController.workspace).priceFormat({
			centsSeparator: ',',
		    thousandsSeparator: '.',
			centsLimit: 2
		});
		
		baixaFinanceiraController.dataOperacaoDistribuidor = 
			$("#dataBaixa", baixaFinanceiraController.workspace).datepicker("getDate");
		
		$("#radioBaixaManual", baixaFinanceiraController.workspace).focus();
		
		baixaFinanceiraController.iniciarGridPrevisao();
		baixaFinanceiraController.iniciarGriBoletosBaixados();
		baixaFinanceiraController.iniciarGridBaixadosRejeitados();
		baixaFinanceiraController.iniciarGridBaixadosDivergentes();
		baixaFinanceiraController.iniciarGridInadimplentes();
		baixaFinanceiraController.iniciarGridTotalBancario();
		
	},
	
	iniciarGridPrevisao : function() {
		$(".previsaoGrid", baixaFinanceiraController.workspace).flexigrid({
			preProcess: baixaFinanceiraController.getDataFromResult,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 35,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nomeCota',
				width : 145,
				sortable : true,
				align : 'left'
			}, {
				display : 'Banco',
				name : 'nomeBanco',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Conta-Corrente',
				name : 'numeroConta',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nosso Número',
				name : 'nossoNumero',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valorBoleto',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Dt Emissão',
				name : 'dataEmissao',
				width : 75,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dt Vencimento',
				name : 'dataVencimento',
				width : 75,
				sortable : true,
				align : 'center'
			}],
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 750,
			height : 220
		});
	},

	iniciarGriBoletosBaixados : function() {
		$(".boletoBaixadoGrid", baixaFinanceiraController.workspace).flexigrid({
			preProcess: baixaFinanceiraController.getDataFromResult,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 35,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nomeCota',
				width : 135,
				sortable : true,
				align : 'left'
			}, {
				display : 'Banco',
				name : 'nomeBanco',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Conta-Corrente',
				name : 'numeroConta',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nosso Número',
				name : 'nossoNumero',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valorBoleto',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Dt Emissão',
				name : 'dataEmissao',
				width : 75,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dt Vencimento',
				name : 'dataVencimento',
				width : 75,
				sortable : true,
				align : 'center'
			}],
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 750,
			height : 220
		});
	},
	
	iniciarGridBaixadosRejeitados : function() {
		$(".boletoRejeitadoGrid", baixaFinanceiraController.workspace).flexigrid({
			preProcess: baixaFinanceiraController.getDataFromResult,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Motivo',
				name : 'motivoRejeitado',
				width : 420,
				sortable : true,
				align : 'left'
			}, {
				display : 'Banco',
				name : 'nomeBanco',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Conta-Corrente',
				name : 'numeroConta',
				width : 80,
				sortable : true,
				align : 'left'
			},{
				display : 'Valor Boleto R$',
				name : 'valorBoleto',
				width : 120,
				sortable : true,
				align : 'right'
			}],
			sortname : "motivoRejeitado",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 1050,
			height : 220
		});
	},
	
	iniciarGridBaixadosDivergentes : function() {
		$(".boletoDivergenciaGrid", baixaFinanceiraController.workspace).flexigrid({
			preProcess: baixaFinanceiraController.getDataFromResult,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 55,
				sortable : true,
				align : 'left'
			},{
				display : 'Motivo',
				name : 'motivoDivergencia',
				width : 255,
				sortable : true,
				align : 'left'
			}, {
				display : 'Banco',
				name : 'nomeBanco',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Conta-Corrente',
				name : 'numeroConta',
				width : 100,
				sortable : true,
				align : 'left'
			},{
				display : 'Boleto R$',
				name : 'valorBoleto',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Pago R$',
				name : 'valorPago',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Diferença R$',
				name : 'diferencaValor',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Dt Emissão',
				name : 'dataEmissao',
				width : 75,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dt Vencimento',
				name : 'dataVencimento',
				width : 75,
				sortable : true,
				align : 'center'
			}],
			sortname : "motivoDivergencia",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 1050,
			height : 220
		});
	},
	
	iniciarGridInadimplentes : function() {
		$(".inadimplentesGrid", baixaFinanceiraController.workspace).flexigrid({
			preProcess: baixaFinanceiraController.getDataFromResult,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 45,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nomeCota',
				width : 135,
				sortable : true,
				align : 'left'
			}, {
				display : 'Banco',
				name : 'nomeBanco',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Conta-Corrente',
				name : 'numeroConta',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nosso Número',
				name : 'nossoNumero',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valorBoleto',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Dt Emissão',
				name : 'dataEmissao',
				width : 75,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dt Vencimento',
				name : 'dataVencimento',
				width : 75,
				sortable : true,
				align : 'center'
			}],
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 950,
			height : 220
		});
	},
	
	iniciarGridTotalBancario : function() {
		$(".totalGrid", baixaFinanceiraController.workspace).flexigrid({
			preProcess: baixaFinanceiraController.getDataFromResult,
			dataType : 'json',
			colModel : [ {
				display : 'Banco',
				name : 'nomeBanco',
				width : 130,
				sortable : true,
				align : 'left'
			},{
				display : 'C.Corrente',
				name : 'numeroConta',
				width : 145,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valorPago',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Data Vencimento',
				name : 'dataVencimento',
				width : 100,
				sortable : true,
				align : 'center'
			}],
			sortname : "nomeBanco",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 550,
			height : 220
		});
	},
	
	getDataFromResult : function(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
		}
		
		var hasRows = resultado.rows.length > 0;
		
		if (hasRows) {
			
			baixaFinanceiraController.mostrarBotoes();
			
		} else {
			
			baixaFinanceiraController.esconderBotoes();
		}
		
		return resultado;
	},
	
	mostrarBotoes : function() {
		
		$("#botoesExportacao", baixaFinanceiraController.workspace).show();
	},
	
	esconderBotoes : function() {
		
		$("#botoesExportacao", baixaFinanceiraController.workspace).hide();
	},
	
    //------- BAIXA MANUAL --------
    
    //POPUPS
    popup_detalhes : function (codigo, isBoletoAntecipado) {
		
    	baixaFinanceiraController.obterDetalhesDivida(codigo, isBoletoAntecipado);
		
		$( "#dialog-detalhes-divida", baixaFinanceiraController.workspace ).dialog({
			resizable: false,
			height:350,
			width:650,
			modal: true,
			buttons:[ 
			          {
				           id:"bt_fechar",
				           text:"Fechar", 
				           click: function() {
				        	   
				        	   $( this ).dialog( "close" );
				           }
			           }
	        ],
			form: $("#dialog-detalhes-divida", this.workspace).parents("form")
		});
	},
	
    popup_baixa_dividas : function() {
    	baixaFinanceiraController.mostrarBancos($("#formaRecebimentoDividas", baixaFinanceiraController.workspace).val());
		$( "#dialog-baixa-dividas", baixaFinanceiraController.workspace ).dialog({
			resizable: false,
			height:430,
			width:480,
			modal: true,
			buttons:[ 
			          {
				           id:"bt_confirmar",
				           text:"Confirmar", 
				           click: function() {
				        	   
							   baixaFinanceiraController.validarDividasManual();  
				           }
			           },
			           {
				           id:"bt_cancelar",
				           text:"Cancelar", 
				           click: function() {
				        	   $( this ).dialog( "close" );
				           }
			           }
	        ],
			beforeClose: function() {
				clearMessageDialogTimeout();
		    },
		    form: $("#div-baixa-dividas", this.workspace)
		});
	},

	popup_confirma_baixa_dividas : function() {
		$( "#dialog-confirma-baixa", baixaFinanceiraController.workspace ).dialog({
			resizable: false,
			height:130,
			width:470,
			modal: true,
			buttons:[ 
			          {
				           id:"bt_confirmar",
				           text:"Confirmar", 
				           click: function() {
				        	   
				        	   var pagamentoAVista = $('#formaRecebimentoDividas', baixaFinanceiraController.workspace).val()==='DINHEIRO';
				        	   
				        	   if(pagamentoAVista)
				        		   baixaFinanceiraController.baixaManualDividas(false);
				        	   else
				        		   baixaFinanceiraController.popup_confirma_pendente();
								 
							   $( this ).dialog( "close" );
				           }
			           },
			           {
				           id:"bt_cancelar",
				           text:"Cancelar", 
				           click: function() {
				        	   $( this ).dialog( "close" );
				           }
			           }
	        ],
			beforeClose: function() {
				clearMessageDialogTimeout();
		    },
		    form: $("#dialog-confirma-baixa", this.workspace).parents("form")
		});
	},
	
	popup_confirma_pendente : function() {
		$( "#dialog-confirma-pendente", baixaFinanceiraController.workspace ).dialog({
			resizable: false,
			height:170,
			width:550,
			modal: true,
			buttons:[ 
			          {
				           id:"bt_sim",
				           text:"Sim", 
				           click: function() {
				        	   baixaFinanceiraController.baixaManualDividas(true);
								
							   $( this ).dialog( "close" );
				           }
			           },
			           {
				           id:"bt_nao",
				           text:"Não", 
				           click: function() {
				        	   baixaFinanceiraController.baixaManualDividas(false);
								
							   $( this ).dialog( "close" );
				           }
			           }
	        ],
			beforeClose: function() {
				clearMessageDialogTimeout();
		    },
		    form: $("#dialog-confirma-pendente", this.workspace).parents("form")
		});
	},
	
	mostrarPopupPagamento : function() {
		$( "#dialog-confirma-baixa-numero", baixaFinanceiraController.workspace ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons:[ 
			          {
				           id:"bt_confirmar",
				           text:"Confirmar", 
				           click: function() {
				        	   $( this ).dialog( "close" );
								
				        	   baixaFinanceiraController.baixaPorNossoNumero();
				           }
			           },
			           {
				           id:"bt_cancelar",
				           text:"Cancelar", 
				           click: function() {
				        	   $( this ).dialog( "close" );
				           }
			           }
	        ],
	        form: $("#dialog-confirma-baixa-numero", this.workspace).parents("form")
		});
	},

	dividaManualNossoNumero : function() {
		
		$('#porCota', baixaFinanceiraController.workspace).hide();
		$('#extratoBaixaManual', baixaFinanceiraController.workspace).show();
		$('#porNossoNumero', baixaFinanceiraController.workspace).show();
	},
	
	dividaManualCota : function() {
		
		$('#porNossoNumero', baixaFinanceiraController.workspace).hide();
		$('#extratoBaixaManual', baixaFinanceiraController.workspace).show();
		$('#porCota', baixaFinanceiraController.workspace).show();
        $("#totalDividasSelecionadas", baixaFinanceiraController.workspace).html("0,00");
		$("#totalDividasSelecionadasHidden", baixaFinanceiraController.workspace).val("0,00");
		$("#totalDividas", baixaFinanceiraController.workspace).html("0,00");
		$("#totalDividasHidden", baixaFinanceiraController.workspace).val("0,00");
	},
	
	limparCamposBaixaManual : function() {
		
		$('#filtroNumCota', baixaFinanceiraController.workspace).val("");
		$('#descricaoCota', baixaFinanceiraController.workspace).val("");
		$('#filtroNossoNumero', baixaFinanceiraController.workspace).val("");
	},
	
	//GRADE DE DIVIDAS
	initGradeDividas : function() {
		$(".liberaDividaGrid", baixaFinanceiraController.workspace).flexigrid({
			preProcess: baixaFinanceiraController.getDataFromResultDividas,
			onSuccess: function() {bloquearItensEdicao(baixaFinanceiraController.workspace);},
			dataType : 'json',
			colModel : [ {
				display : 'Codigo',
				name : 'codigo',
				width : 10,
				sortable : false,
				align : 'left',
				hide: true
			},{
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nome',
				width : 350,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data Emissão',
				name : 'dataEmissao',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Vencimento',
				name : 'dataVencimento',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor Divida R$',
				name : 'valor',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Nosso Número',
				name : 'nossoNumero',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Detalhes',
				name : 'acao',
				width : 50,
				sortable : false,
				align : 'center',
			}, {
				display : '',
				name : 'check',
				width : 20,
				sortable : false,
				align : 'center',
			}],
			sortname : "dataVencimento",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	},


	//SELECIONAR TODAS AS DIVIDAS
	selecionarTodos : function(checked){
		
		for (var i=0;i<document.formularioListaDividas.elements.length;i++) {
		     var x = document.formularioListaDividas.elements[i];
		     if (x.name == 'checkboxGrid') {
		    	 x.checked = checked;
		     }    
		}
		
		if (checked){
			
			var elem = $("#textoSelTodos", baixaFinanceiraController.workspace);
			elem.innerHTML = "Desmarcar todos";
			
	        $("#totalDividasSelecionadas", baixaFinanceiraController.workspace).html($("#totalDividas", baixaFinanceiraController.workspace).html());
		    $("#totalDividasSelecionadasHidden", baixaFinanceiraController.workspace).val($("#totalDividasHidden", baixaFinanceiraController.workspace).val());
        }
		
		else{
			
			var elem = document.getElementById("textoSelTodos");
			elem.innerHTML = "Marcar todos";
			
			$("#totalDividasSelecionadas", baixaFinanceiraController.workspace).html("0,00");
			$("#totalDividasSelecionadasHidden", baixaFinanceiraController.workspace).val("0,00");
		}
	},
	
	
    //CALCULAR VALOR TOTAL DAS DIVIDAS SELECIONADAS
	calculaSelecionados : function(checked, valor) {
		
		var totalSelecionado = removeMascaraPriceFormat($("#totalDividasSelecionadasHidden", baixaFinanceiraController.workspace).val());
	    
		if(checked){
			totalSelecionado = intValue(totalSelecionado) + intValue(valor);
		}
		else{
			totalSelecionado = intValue(totalSelecionado) - intValue(valor);
		}

		$("#totalDividasSelecionadasHidden", baixaFinanceiraController.workspace).val(totalSelecionado);
		
		$("#totalDividasSelecionadasHidden", baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.',
			centsLimit: 2
		});
		
		$("#totalDividasSelecionadas", baixaFinanceiraController.workspace).html($("#totalDividasSelecionadasHidden", baixaFinanceiraController.workspace).val());
	},
	
	
    //POPULA GRADE DE DIVIDAS, ATUALIZANDO TOTALIZAÇÕES
	getDataFromResultDividas : function(resultado) {	
		
		//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
		if (resultado.mensagens) {
			if(baixaFinanceiraController.acaoPesquisa != null){
				exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
				);
			}else{
				
				$("#filtroNumCota",baixaFinanceiraController.workspace).val("");
				$("#descricaoCota",baixaFinanceiraController.workspace).val("");
				$("#filtroNossoNumero",baixaFinanceiraController.workspace).val("");
				$("#checkCobrancasBaixadas",baixaFinanceiraController.workspace).attr("checked",false);
				$("#extratoBaixaManual",baixaFinanceiraController.workspace).hide();	
			}
			$(".area", baixaFinanceiraController.workspace).hide();
			$(".grids", baixaFinanceiraController.workspace).hide();
			return resultado;
		}
		
		$("#selTodos", baixaFinanceiraController.workspace).attr("checked", false);
		baixaFinanceiraController.selecionarTodos(false);
		
		var totalDividas=0;
		$.each(resultado.rows, function(index, row) {
			
			valorItem = removeMascaraPriceFormat(row.cell.valor);
			totalDividas = intValue(totalDividas) + intValue(valorItem);
			
			var checkBox;
			
			if (!row.cell.codigo){
				
				checkBox = '<a onclick="exibirMensagem(\'WARNING\', [\'Para dívidas com boleto em branco é necessário baixar pelo Nosso Número\']); '+
					' $(\'#filtroNossoNumero\', baixaFinanceiraController.workspace).val('+ row.cell.nossoNumero +');" href="javascript:;" title="Boleto em Branco">'+
					'<img border="0" hspace="5" src="/nds-client/images/bt_help.png"></a>';
				
				row.cell.acao = '';
			} else { 
				
				row.cell.acao = '<a href="javascript:;" onclick="baixaFinanceiraController.popup_detalhes(' + row.cell.codigo + ', ' + row.cell.boletoAntecipado + ');" style="cursor:pointer">' +
			 	   '<img title="Detalhes da Dívida" src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0px" />' +
				   '</a>';
				
				if (row.cell.check){
				    checkBox = '<input isEdicao="true" checked="' + row.cell.check + '" type="checkbox" name="checkboxGrid" id="checkbox_'+ row.cell.codigo +
				    	'" onchange="baixaFinanceiraController.calculaSelecionados(this.checked,'+ valorItem +'); dataHolder.hold(\'baixaManual\', '+ 
				    	row.cell.codigo +', \'checado\', this.checked); " />';
				    
				    baixaFinanceiraController.calculaSelecionados(true, valorItem);
				} else{
				    checkBox = '<input isEdicao="true" title="Selecionar Dívida" type="checkbox" name="checkboxGrid" id="checkbox_'+ row.cell.codigo +
				    	'" onchange="baixaFinanceiraController.calculaSelecionados(this.checked,'+ valorItem +'); dataHolder.hold(\'baixaManual\', '+ 
				    	row.cell.codigo +', \'checado\', this.checked); " />';
				}
			}
			
			row.cell.check = checkBox;
		});

		$("#totalDividasHidden", baixaFinanceiraController.workspace).val(totalDividas);
		$('#totalDividasHidden', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.',
			centsLimit: 2
		});
		$("#totalDividas", baixaFinanceiraController.workspace).html($("#totalDividasHidden", baixaFinanceiraController.workspace).val());
		
		$(".area", baixaFinanceiraController.workspace).show();
		$(".grids", baixaFinanceiraController.workspace).show();
		$("#extratoBaixaManual",baixaFinanceiraController.workspace).show();
		
		baixaFinanceiraController.acaoPesquisa = null;
		
		return resultado;
	},
    
    
	initGridDadosDivida : function() {
	    //GRADE DE DETALHES DA DIVIDA
		$(function() {
			$(".dadosDividaGrid", baixaFinanceiraController.workspace).flexigrid({
				preProcess: baixaFinanceiraController.getDataFromResultDivida,
				dataType : 'json',
				colModel : [ {
					display : 'Data',
					name : 'data',
					width : 140,
					sortable : true,
					align : 'center'
				},{
					display : ' ',
					name : 'tipo',
					width : 110,
					sortable : true,
					align : 'left'
				},{
					display : 'R$',
					name : 'valor',
					width : 60,
					sortable : true,
					align : 'right'
				},  {
					display : 'Observação',
					name : 'observacao',
					width : 250,
					sortable : true,
					align : 'left'
				}],
				width : 620,
				height : 160
			});
		});
	},
    
    //POPULA GRADE DE DETALHES DA DIVIDA E CALCULA SALDO DE DIVIDAS
    obterDetalhesDivida : function(idDividaCobranca, isBoletoAntecipado){
    	
		$(".dadosDividaGrid", baixaFinanceiraController.workspace).flexOptions({
			url: contextPath + "/financeiro/baixa/obterDetalhesDivida",
			params: [
			         {name:'idCobranca', value: idDividaCobranca},
			         {name:'isBoletoAntecipado', value: isBoletoAntecipado}
			        ] ,
			        newp: 1
		});
		$(".dadosDividaGrid", baixaFinanceiraController.workspace).flexReload();
		$(".grids", baixaFinanceiraController.workspace).show();
	},
	
	getDataFromResultDivida : function(resultado) {
		
		var saldoDivida=0;
		$.each(resultado.rows, function(index, row) {
			saldoDivida = saldoDivida + eval(priceToFloat(row.cell.valor));
		});
		
		$("#saldoDividaHidden", baixaFinanceiraController.workspace).val(saldoDivida);

		$("#saldoDivida", baixaFinanceiraController.workspace).html(floatToPrice(saldoDivida));
		
		$(".grids", baixaFinanceiraController.workspace).show();
		
		return resultado;
	},
	
	//EFETUA BUSCA DE DIVIDAS(POR COTA) OU COBRANCA(POR NOSSO NUMERO)
	buscaManual : function(acaoTela) {
		
		baixaFinanceiraController.acaoPesquisa = acaoTela;
		
		dataHolder.clearAction('baixaManual');

		var nossoNumero = $("#filtroNossoNumero", baixaFinanceiraController.workspace).val();
		
		var numCota = $("#filtroNumCota", baixaFinanceiraController.workspace).val();
		
		var botoesDividasNaoPagas = $("#botoesDividasNaoPagas", baixaFinanceiraController.workspace);
		
		var botoesDividasPagas = $("#botoesDividasPagas", baixaFinanceiraController.workspace);
	
		if($("#checkCobrancasBaixadas", baixaFinanceiraController.workspace).is(':checked')){
			
			botoesDividasPagas.show();
			
			botoesDividasNaoPagas.hide();
			
			/*BAIXA MANUAL DE DIVIDAS BAIXADAS*/
			$(".liberaDividaGrid", baixaFinanceiraController.workspace).flexOptions({
				url: contextPath + "/financeiro/baixa/buscaDividasBaixadas",
				params: [
				         {name:'numCota', value: numCota},
				         {name:'nossoNumero', value: nossoNumero}
				        ] ,
				        newp: 1
			});
			
			$(".liberaDividaGrid", baixaFinanceiraController.workspace).flexReload();
			
			$(".grids", baixaFinanceiraController.workspace).show();
			
			baixaFinanceiraController.dividaManualCota();
			
		}else{
			
			botoesDividasPagas.hide();
			botoesDividasNaoPagas.show();
			
			if (nossoNumero==''){
				
				/*BAIXA MANUAL DE DIVIDAS*/
				$(".liberaDividaGrid", baixaFinanceiraController.workspace).flexOptions({
					url: contextPath + "/financeiro/baixa/buscaDividas",
					params: [
					         {name:'numCota', value: numCota}
					        ] ,
					        newp: 1
				});
				
				$(".liberaDividaGrid", baixaFinanceiraController.workspace).flexReload();
				
				$(".grids", baixaFinanceiraController.workspace).show();
				
				baixaFinanceiraController.dividaManualCota();
			}
			
			else{
				
				$("#dtPagamentoManualBoleto", baixaFinanceiraController.workspace).val('');
				
				/*BAIXA INDIVIDUAL DE COBRANÇA(BOLETO)*/
				var data = [
				   {name: 'nossoNumero', value: nossoNumero}
				];
				
				$.postJSON(contextPath + "/financeiro/baixa/buscaBoleto",
						   data,
						   baixaFinanceiraController.sucessCallbackPesquisarBoleto, 
						   baixaFinanceiraController.errorCallbackPesquisarBoleto);
				
			}
		}
	},
	
	atualizarDadosCobrancaManualBoleto : function() {
		
		var nossoNumero = $("#filtroNossoNumero", baixaFinanceiraController.workspace).val();
		
		var dataPagamento = 
			$("#dtPagamentoManualBoleto", baixaFinanceiraController.workspace).val();
		
		var valor = null;
		
		if ($("#isBoletoAntecipado", baixaFinanceiraController.workspace).val()) {

			valor = 
				priceToFloat($("#valorBoletoHidden", baixaFinanceiraController.workspace).val()) - 
				priceToFloat($("#encalhe", baixaFinanceiraController.workspace).val());
		}
		
		/*BAIXA INDIVIDUAL DE COBRANÇA(BOLETO)*/
		var data = [
		   {name: 'nossoNumero', value: nossoNumero},
		   {name: 'dataPagamento', value: dataPagamento},
		   {name: 'valor', value: valor.toString().replace(".",",")}
		];
		
		$.postJSON(contextPath + "/financeiro/baixa/buscaBoleto",
				   data,
				   baixaFinanceiraController.sucessCallbackPesquisarBoleto, 
				   baixaFinanceiraController.errorCallbackPesquisarBoleto);
	},
	
	sucessCallbackPesquisarBoleto : function(resultado) {
		
		$("#baixa-cota", baixaFinanceiraController.workspace).html(resultado.cota);
		$("#baixa-banco", baixaFinanceiraController.workspace).html(resultado.banco);
		$("#baixa-nossoNumero", baixaFinanceiraController.workspace).html(resultado.nossoNumero);
		$("#baixa-dataEmissao", baixaFinanceiraController.workspace).html(resultado.dataEmissao);
		$("#baixa-dataVencimento", baixaFinanceiraController.workspace).html(resultado.dataVencimento);
		
		$("#dividaTotal", baixaFinanceiraController.workspace).html(resultado.dividaTotal);
		$("#dataPagamento", baixaFinanceiraController.workspace).html(resultado.dataPagamento);
		$("#dtPagamentoManualBoleto", baixaFinanceiraController.workspace).val(resultado.dataPagamento);
		
		if (resultado.boletoAntecipado) {
			
			$("#encalhe", baixaFinanceiraController.workspace).val(resultado.desconto);
			$("#infoEncalheBoletoAntecipado", baixaFinanceiraController.workspace).show();
			$("#desconto", baixaFinanceiraController.workspace).val("");
			$("#infoDescontoCobranca", baixaFinanceiraController.workspace).hide();
			
		} else {
			
			$("#desconto", baixaFinanceiraController.workspace).val(resultado.desconto);
			$("#infoDescontoCobranca", baixaFinanceiraController.workspace).show();
			$("#encalhe", baixaFinanceiraController.workspace).val("");
			$("#infoEncalheBoletoAntecipado", baixaFinanceiraController.workspace).hide();			
		}
		
		$("#isBoletoAntecipado", baixaFinanceiraController.workspace).val(resultado.boletoAntecipado);

		$("#juros", baixaFinanceiraController.workspace).val(resultado.juros);
		$("#multa", baixaFinanceiraController.workspace).val(resultado.multa);
		
		$("#valorTotalHidden", baixaFinanceiraController.workspace).val(resultado.valorTotal);
		$("#valorBoletoHidden", baixaFinanceiraController.workspace).val(resultado.valor);
		
		$("#baixa-valorTotal", baixaFinanceiraController.workspace).html($("#valorTotalHidden", baixaFinanceiraController.workspace).val());
		$("#baixa-valorBoleto", baixaFinanceiraController.workspace).html($("#valorBoletoHidden", baixaFinanceiraController.workspace).val());
		
		baixaFinanceiraController.dividaManualNossoNumero();
	},
	
	errorCallbackPesquisarBoleto : function() {
		$('#extratoBaixaManual', baixaFinanceiraController.workspace).hide();
	},
	
	
	//EFETUA BAIXA DE COBRANCA POR NOSSO NUMERO
    baixaPorNossoNumero : function() {
		
    	var isBoletoAntecipado = $("#isBoletoAntecipado", baixaFinanceiraController.workspace).val() === "true";
    	
    	var desconto;
    	
    	if (isBoletoAntecipado) {
    		
    		desconto = $("#encalhe", baixaFinanceiraController.workspace).val();	
    	
    	} else {
    		
    		desconto = $("#desconto", baixaFinanceiraController.workspace).val();
    	}
    	
    	var param ={ nossoNumero : $("#baixa-nossoNumero", baixaFinanceiraController.workspace).html(),
    			dataPagamento :$("#dtPagamentoManualBoleto", baixaFinanceiraController.workspace).val(),
    			valor : $("#valorBoletoHidden", baixaFinanceiraController.workspace).val(),
    			desconto : desconto,
    			juros : $("#juros", baixaFinanceiraController.workspace).val(),
    			multa : $("#multa", baixaFinanceiraController.workspace).val()
    	};
		
		$.postJSON(contextPath + "/financeiro/baixa/baixaManualBoleto",param,
				   function() {baixaFinanceiraController.mostrarBaixa('MANUAL');});
	},
	
	
	//CALCULA TOTAL CONFORME AÇÃO DO USUARIO NA TELA DE BAIXA POR NOSSO NUMERO
	calculaTotalManual : function(recalcularJurosMulta) {
    	
		var isBoletoAntecipado = $("#isBoletoAntecipado", baixaFinanceiraController.workspace).val() === "true";
		
		if (isBoletoAntecipado && recalcularJurosMulta) {
			
			baixaFinanceiraController.atualizarDadosCobrancaManualBoleto();
			
			return;		
		}	
		
		var desconto = removeMascaraPriceFormat($("#desconto", baixaFinanceiraController.workspace).val());			
		var encalhe = removeMascaraPriceFormat($("#encalhe", baixaFinanceiraController.workspace).val());

		var valorBoleto = removeMascaraPriceFormat($("#valorBoletoHidden", baixaFinanceiraController.workspace).val());
		var juros = removeMascaraPriceFormat($("#juros", baixaFinanceiraController.workspace).val());
		var multa = removeMascaraPriceFormat($("#multa", baixaFinanceiraController.workspace).val());
		
		encalhe = encalhe === "" ? 0 : encalhe;
		valorBoleto = eval(valorBoleto) - eval(encalhe);

		var total = intValue(valorBoleto) + intValue(juros) + intValue(multa) - intValue(desconto);
        
		$("#valorTotalHidden", baixaFinanceiraController.workspace).val(total);
		
		$("#valorTotalHidden", baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.',
			centsLimit: 2
		});

		$("#baixa-valorTotal", baixaFinanceiraController.workspace).html($("#valorTotalHidden", baixaFinanceiraController.workspace).val());
	},
	
	
	//OBTEM OS CODIGOS DAS DIVIDAS MARCADAS
	obterCobrancasDividasMarcadas : function(){

		var dividasMarcadas=new Array();
		var table = $("#tabelaDividas tr", baixaFinanceiraController.workspace);
		
		for(var i = 0; i < table.length; i++){   
			if ($("#checkbox_"+table[i].cells[0].textContent, baixaFinanceiraController.workspace).attr("checked") == "checked") {				
				dividasMarcadas.push(table[i].cells[0].textContent);
			}
		} 		
		return dividasMarcadas;
	},
	
	obterDataVencimentoDividaManual : function(){

		var table = $("#tabelaDividas tr", baixaFinanceiraController.workspace);
		
		for(var i = 0; i < table.length; i++){   
			if ($("#checkbox_"+table[i].cells[0].textContent, baixaFinanceiraController.workspace).attr("checked") == "checked") {				
				return (table[i].cells[3].textContent);
			}
		} 		
		return null;
	},
	
	
	//OBTEM DADOS CALCULADOS REFERENTES ÀS DAS DIVIDAS MARCADAS PARA A TELA DE BAIXA POR COTA
    obterPagamentoDividas : function() {
    	
		var dividasMarcadas = baixaFinanceiraController.obterCobrancasDividasMarcadas();
		
		var dataPagamento;
		
		$("#dtPagamentoManual", baixaFinanceiraController.workspace).datepicker("setDate", baixaFinanceiraController.dataOperacaoDistribuidor);
	
	   dataPagamento = $("#dtPagamentoManual", baixaFinanceiraController.workspace).val();
	
		var  param = {dataPagamento :dataPagamento};
		
    	param = serializeArrayToPost('idCobrancas',dividasMarcadas, param);
		
		$.postJSON(contextPath + "/financeiro/baixa/obterPagamentoDividas",
				   param,
				   function (resultado){
					
					baixaFinanceiraController.sucessCallbackPagamentoDivida(resultado);
					
				   },
				   null); 
	},
	
    sucessCallbackPagamentoDivida : function(resultado) {
		
	    $("#multaDividas", baixaFinanceiraController.workspace).val(resultado.valorMulta);
		$("#jurosDividas", baixaFinanceiraController.workspace).val(resultado.valorJuros);
		$("#descontoDividas", baixaFinanceiraController.workspace).val(resultado.valorDesconto);
		$("#valorPagoDividas", baixaFinanceiraController.workspace).val(resultado.valorPagamento);
		$("#formaRecebimentoDividas", baixaFinanceiraController.workspace).val(resultado.tipoPagamento);
		$("#observacoesDividas", baixaFinanceiraController.workspace).val(resultado.observacoes);
		
		$("#valorSaldoDividasHidden", baixaFinanceiraController.workspace).val(resultado.valorSaldo);
		$("#valorDividasHidden", baixaFinanceiraController.workspace).val(resultado.valorDividas);
			
		$('#valorDividas', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
		    centsSeparator: ',',
		    thousandsSeparator: '.',
			centsLimit: 2
		});
		
		$('#multaDividas', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.',
			centsLimit: 4
		});
		
		$('#jurosDividas', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.',
			centsLimit: 4
		});
		
		$('#descontoDividas', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.',
			centsLimit: 4
		});
		
		$('#valorPagoDividas', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.',
			centsLimit: 2
		});
		
		$('#valorSaldoDividas', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.',
			centsLimit: 2
		});
		
		$('#valorSaldoDividasHidden', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.',
			centsLimit: 2
		});
		
		$('#valorDividasHidden', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.',
			centsLimit: 2
		});
		
		$("#valorSaldoDividas", baixaFinanceiraController.workspace).html($("#valorSaldoDividasHidden", baixaFinanceiraController.workspace).val());
		$("#valorDividas", baixaFinanceiraController.workspace).html($("#valorDividasHidden", baixaFinanceiraController.workspace).val());
	
		baixaFinanceiraController.popup_baixa_dividas();
	},
    
    
    //CALCULA TOTAL CONFORME AÇÃO DO USUARIO NA TELA DE BAIXA POR COTA
	calculaTotalManualDividas : function() {
    	
		baixaFinanceiraController.validarCamposBaixa();
		
		var valorDividas = $("#valorDividasHidden", baixaFinanceiraController.workspace).val(); 
		var desconto = $("#descontoDividas", baixaFinanceiraController.workspace).val(); 
		var juros = $("#jurosDividas", baixaFinanceiraController.workspace).val(); 
		var multa = $("#multaDividas", baixaFinanceiraController.workspace).val(); 
		
		var param = {vlDivida : valorDividas , vlMulta : multa , vlJuros :juros, vlDesconto: desconto};
		
		$.postJSON(contextPath + "/financeiro/baixa/calculaTotalManualDividas",param,
		function(result) {
		   
			var valorPago = result;
		   
			$("#valorPagoDividas", baixaFinanceiraController.workspace).val(valorPago);
		
			$("#valorPagoDividas", baixaFinanceiraController.workspace).priceFormat({
				allowNegative: true,
				centsSeparator: ',',
				thousandsSeparator: '.',
				centsLimit: 2
			});
   
		},
		null,
		true);
	},

    
	//CALCULA SALDO CONFORME AÇÃO DO USUARIO NA TELA DE BAIXA POR COTA
	calculaSaldoDividas : function() {
    	
		baixaFinanceiraController.validarCamposBaixa();
		
		var valorDividas = formatMoneyValue($("#valorDividasHidden", baixaFinanceiraController.workspace).val(),2); 
		var valorPago = formatMoneyValue($("#valorPagoDividas", baixaFinanceiraController.workspace).val(),2); 
		var desconto = formatMoneyValue($("#descontoDividas", baixaFinanceiraController.workspace).val()); 
		var juros = formatMoneyValue($("#jurosDividas", baixaFinanceiraController.workspace).val()); 
		var multa = formatMoneyValue($("#multaDividas", baixaFinanceiraController.workspace).val()); 

		var param = {vlDividaStr : valorDividas , vlMultaStr : multa , vlJurosStr :juros, vlDescontoStr: desconto , vlPagoStr: valorPago };
		
		$.postJSON(contextPath + "/financeiro/baixa/calcularSaldoDivida",param,
		function(result) {
		   
		   var valorSaldo = result;
			$("#valorSaldoDividasHidden", baixaFinanceiraController.workspace).val(valorSaldo);
			
			$("#valorSaldoDividasHidden", baixaFinanceiraController.workspace).priceFormat({
				allowNegative: true,
				centsSeparator: ',',
				thousandsSeparator: '.',
				centsLimit: 2
			});
			$("#valorSaldoDividas", baixaFinanceiraController.workspace).html($("#valorSaldoDividasHidden", baixaFinanceiraController.workspace).val());
   
		},
		null,
		true);
	},
    
	validarCamposBaixa : function() {
		
		$("#pagarDividas input[type='text']").each(function(index, component) {
			
			var val = $(this).val(); 
			
			if (!val || val == "") {
				
				if (component.id != "dtPagamentoManual"){
					$(this).val("0,00");
				}	
			}
		});
	},
    
    //EFETUA BAIXA MANUAL DE DIVIDAS SELECIONADAS E CALCULADAS
    baixaManualDividas : function(manterPendente) {
 
    	var  param = {valorDividas : $("#valorDividas", baixaFinanceiraController.workspace).html(),
    			valorMulta : $("#multaDividas", baixaFinanceiraController.workspace).val(),
    			valorJuros : $("#jurosDividas", baixaFinanceiraController.workspace).val(),
    			valorDesconto : $("#descontoDividas", baixaFinanceiraController.workspace).val(),
    			valorPagamento : $("#valorPagoDividas", baixaFinanceiraController.workspace).val(),
    			valorSaldo : $("#valorSaldoDividas", baixaFinanceiraController.workspace).html(),
    			tipoPagamento : $("#formaRecebimentoDividas", baixaFinanceiraController.workspace).val(),
    			observacoes : $("#observacoesDividas", baixaFinanceiraController.workspace).val(),
    			idBanco : $("#bancoDividas", baixaFinanceiraController.workspace).val(),
    			manterPendente:manterPendente,
				dataPagamento : $("#dtPagamentoManual", baixaFinanceiraController.workspace).val()};

    	param = serializeArrayToPost('idCobrancas',baixaFinanceiraController.obterCobrancasDividasMarcadas(), param);
		
    	$.postJSON(contextPath + "/financeiro/baixa/baixaManualDividas",param,
				   function(mensagens) {
					   
			           $("#dialog-baixa-dividas", baixaFinanceiraController.workspace).dialog("close");
					   
					   if (mensagens){
						   var tipoMensagem = mensagens.tipoMensagem;
						   var listaMensagens = mensagens.listaMensagens;
						   if (tipoMensagem && listaMensagens) {
						       exibirMensagem(tipoMensagem, listaMensagens);
					       }
		        	   }
			           
					   baixaFinanceiraController.buscaManual(null);
	               },
	               null,
	               true);
	},
	
	validarDividasManual : function() {
 
    	var  param = {	
						valorMulta : $("#multaDividas", baixaFinanceiraController.workspace).val(),
						valorJuros : $("#jurosDividas", baixaFinanceiraController.workspace).val(),
						valorDesconto : $("#descontoDividas", baixaFinanceiraController.workspace).val(),
						valorSaldo : $("#valorSaldoDividas", baixaFinanceiraController.workspace).html(),
						tipoPagamento : $("#formaRecebimentoDividas", baixaFinanceiraController.workspace).val(),
						idBanco : $("#bancoDividas", baixaFinanceiraController.workspace).val(),
						dataPagamento : $("#dtPagamentoManual", baixaFinanceiraController.workspace).val()};

    	param = serializeArrayToPost('idCobrancas',baixaFinanceiraController.obterCobrancasDividasMarcadas(), param);
		
    	$.postJSON(contextPath + "/financeiro/baixa/validarBaixaManual",param,
				   function(result) {
					   
			           if (result){
						   
						   var tipoMensagem = result.tipoMensagem;
						   var listaMensagens = result.listaMensagens;
						   
						   if (tipoMensagem && listaMensagens) {
						       exibirMensagem(tipoMensagem, listaMensagens);
					       }
						   else{
								
								baixaFinanceiraController.popup_confirma_baixa_dividas();
						   }
		        	   }
	               },
	               null,
	               true);
	},
	
	atualizarDadosCobrancaManual: function (){
		
var dividasMarcadas = baixaFinanceiraController.obterCobrancasDividasMarcadas();
		
		var  param = {dataPagamento : $("#dtPagamentoManual", baixaFinanceiraController.workspace).val()};
		
		var valorDivida = $("#valorDividasHidden", baixaFinanceiraController.workspace).val();
		
    	param = serializeArrayToPost('idCobrancas',dividasMarcadas,param);
		
    	$.postJSON(contextPath + "/financeiro/baixa/obterPagamentoDividas",
				   param,
				   function (resultado){

					baixaFinanceiraController.sucessCallbackPagamentoDivida(resultado);
					
					$("#valorDividasHidden", baixaFinanceiraController.workspace).val(valorDivida);
				
					$("#valorDividas", baixaFinanceiraController.workspace).html(valorDivida);
					
				   },
				   null
		);
	},
	
    //OBTEM VALIDAÇÃO DE PERMISSÃO DE NEGOCIAÇÃO
    obterNegociacao : function() {
    	var param = serializeArrayToPost('idCobrancas',baixaFinanceiraController.obterCobrancasDividasMarcadas());
		$.postJSON(contextPath + "/financeiro/baixa/obterNegociacao",param);
	},

	
	//----------- BAIXA AUTOMATICA ----------------

	integrar : function() {
		
		$('#formBaixaAutomatica', baixaFinanceiraController.workspace).submit();
	},
	
	mostrarGridBoletosPrevisao : function() {
		
		var dataBaixa = $("#dataBaixa", baixaFinanceiraController.workspace).val();
		
		$(".previsaoGrid", baixaFinanceiraController.workspace).flexOptions({
			url: contextPath + "/financeiro/baixa/mostrarGridBoletosPrevisao",
			params: [
		         {name:'data', value: dataBaixa}
		    ],
		});
		
		$(".previsaoGrid", baixaFinanceiraController.workspace).flexReload();
		
		$("#dialog-previsao").dialog({
			resizable: false,
			height:430,
			width:800,
			modal: true,
			buttons: [
			    {
			    	id: "dialogPrevisaoBtnFechar",
			    	text: "Fechar",
			    	click: function() {
						$(this).dialog("close");
			    	}
			    }
			],
			form: $("#dialog-previsao", baixaFinanceiraController.workspace).parents("form")
		});
	},
	
	mostrarGridBoletosBaixados : function() {
		
		var dataBaixa = $("#dataBaixa", baixaFinanceiraController.workspace).val();
		
		$(".boletoBaixadoGrid", baixaFinanceiraController.workspace).flexOptions({
			url: contextPath + "/financeiro/baixa/mostrarGridBoletosBaixados",
			params: [
		         {name:'data', value: dataBaixa}
		    ],
		});
		
		$(".boletoBaixadoGrid", baixaFinanceiraController.workspace).flexReload();
		
		$("#dialog-boletos-baixados", baixaFinanceiraController.workspace).dialog({
			resizable: false,
			height:430,
			width:800,
			modal: true,
			buttons: [
			    {
			    	id: "dialogBoletosBaixadosBtnFechar",
			    	text: "Fechar",
			    	click: function() {
						$(this).dialog("close");
			    	}
			    }
			],
			form: $("#dialog-boletos-baixados", baixaFinanceiraController.workspace).parents("form")
		});
	},
	
	mostrarGridBoletosRejeitados : function() {
		
		var dataBaixa = $("#dataBaixa", baixaFinanceiraController.workspace).val();
		
		$(".boletoRejeitadoGrid", baixaFinanceiraController.workspace).flexOptions({
			url: contextPath + "/financeiro/baixa/mostrarGridBoletosRejeitados",
			params: [
		         {name:'data', value: dataBaixa}
		    ],
		});
		
		$(".boletoRejeitadoGrid", baixaFinanceiraController.workspace).flexReload();
		
		$("#dialog-baixados-rejeitados", baixaFinanceiraController.workspace).dialog({
			resizable: false,
			height:430,
			width:1050,
			modal: true,
			buttons: [
			    {
			    	id: "dialogBaixadosRejeitadosBtnFechar",
			    	text: "Fechar",
			    	click: function() {
						$(this).dialog("close");
			    	}
			    }
			],
			form: $("#dialog-baixados-rejeitados", baixaFinanceiraController.workspace).parents("form")
		});
	},
	
	mostrarGridBoletosBaixadosComDivergencia : function() {
		
		var dataBaixa = $("#dataBaixa", baixaFinanceiraController.workspace).val();
		
		$(".boletoDivergenciaGrid", baixaFinanceiraController.workspace).flexOptions({
			url: contextPath + "/financeiro/baixa/mostrarGridBoletosBaixadosComDivergencia",
			params: [
		         {name:'data', value: dataBaixa}
		    ],
		});
		
		$(".boletoDivergenciaGrid", baixaFinanceiraController.workspace).flexReload();
		
		$("#dialog-baixados-divergentes", baixaFinanceiraController.workspace).dialog({
			resizable: false,
			height:430,
			width:1050,
			modal: true,
			buttons: [
			    {
			    	id: "dialogBaixadosDivergentesBtnFechar",
			    	text: "Fechar",
			    	click: function() {
						$(this).dialog("close");
			    	}
			    }
			],
			form: $("#dialog-baixados-divergentes", baixaFinanceiraController.workspace).parents("form")
		});
	},
	
	mostrarGridBoletosInadimplentes : function() {
		
		var dataBaixa = $("#dataBaixa", baixaFinanceiraController.workspace).val();
		
		$(".inadimplentesGrid", baixaFinanceiraController.workspace).flexOptions({
			url: contextPath + "/financeiro/baixa/mostrarGridBoletosInadimplentes",
			params: [
		         {name:'data', value: dataBaixa}
		    ],
		});
		
		$(".inadimplentesGrid", baixaFinanceiraController.workspace).flexReload();
		
		$("#dialog-inadimplentes", baixaFinanceiraController.workspace).dialog({
			resizable: false,
			height:430,
			width:800,
			modal: true,
			buttons: [
			    {
			    	id: "dialogIsnadimplentesBtnFechar",
			    	text: "Fechar",
			    	click: function() {
						$(this).dialog("close");
			    	}
			    }
			],
			form: $("#dialog-inadimplentes", baixaFinanceiraController.workspace).parents("form")
		});
	},
	
	mostrarGridTotalBancario : function() {
		
		var dataBaixa = $("#dataBaixa", baixaFinanceiraController.workspace).val();
		
		$(".totalGrid", baixaFinanceiraController.workspace).flexOptions({
			url: contextPath + "/financeiro/baixa/mostrarGridTotalBancario",
			params: [
		         {name:'data', value: dataBaixa}
		    ],
		});
		
		$(".totalGrid", baixaFinanceiraController.workspace).flexReload();
		
		$("#dialog-total", baixaFinanceiraController.workspace).dialog({
			resizable: false,
			height:430,
			width:600,
			modal: true,
			buttons: [
			    {
			    	id: "dialogTotalBtnFechar",
			    	text: "Fechar",
			    	click: function() {
						$(this).dialog("close");
			    	}
			    }
			],
			form: $("#dialog-total", baixaFinanceiraController.workspace).parents("form")
		});
	},
	
	replaceAll : function(string, token, newtoken) {
		while (string.indexOf(token) != -1) {
	 		string = string.replace(token, newtoken);
		}
		return string;
	},
	
	tratarRespostaBaixaAutomatica : function(data) {
		
		data = baixaFinanceiraController.replaceAll(data, "<pre>", "");
		data = baixaFinanceiraController.replaceAll(data, "</pre>", "");
		
		data = baixaFinanceiraController.replaceAll(data, "<PRE>", "");
		data = baixaFinanceiraController.replaceAll(data, "</PRE>", "");
		
		var responseJson = jQuery.parseJSON(data);
		
		if (responseJson.mensagens) {

			exibirMensagem(
				responseJson.mensagens.tipoMensagem, 
				responseJson.mensagens.listaMensagens
			);
			
			$('#resultadoIntegracao', baixaFinanceiraController.workspace).hide();
		}
		
		if (responseJson.result) {
			$("#nomeArquivo", baixaFinanceiraController.workspace).html(responseJson.result.nomeArquivo);
			$("#dataCompetencia", baixaFinanceiraController.workspace).html(responseJson.result.dataCompetencia);
			$("#somaPagamentos", baixaFinanceiraController.workspace).html(responseJson.result.somaPagamentos);
			
			baixaFinanceiraController.mostrarDadosResumoBaixaFinanceira(responseJson.result);
			
			baixaFinanceiraController.resetarCamposBaixaAutomatica(true);
			
			$("#tableDadosArquivo", baixaFinanceiraController.workspace).show();

			$('#resultadoIntegracao', baixaFinanceiraController.workspace).show();
			
			$('#tableDados', baixaFinanceiraController.workspace).css({"text-align": ""});
		}
	},
	
	resetarCamposBaixaAutomatica: function(keepDadosAposBaixa) {
		
		baixaFinanceiraController.limparCamposBaixaAutomatica(keepDadosAposBaixa);
		
		if(!keepDadosAposBaixa) {
			$("#dataBaixa", baixaFinanceiraController.workspace).datepicker(
					"setDate", baixaFinanceiraController.dataOperacaoDistribuidor
			);
		}

		$("#btnIntegrar", baixaFinanceiraController.workspace).css("display", "none");
		$("#btnExibirResumos", baixaFinanceiraController.workspace).css("display", "block");
	},
	
	limparCamposBaixaAutomatica : function(keepDadosAposBaixa) {
		$("#uploadedFile", baixaFinanceiraController.workspace).val("");
		
		if(!keepDadosAposBaixa) {
			$("#valorFinanceiro", baixaFinanceiraController.workspace).val("");
		}
	},
	
	//-----------------------------------------------------
	
	
	//------ Baixa CONSOLIDADA ------
	
	integrarCobrancaConsolidada : function() {
		
		 $('#formBaixaConsolidada', baixaFinanceiraController.workspace).ajaxSubmit({
   		   
		      success: function(responseText, statusText, xhr, $form) { 
		    	  
		    	  var mensagens = (responseText.mensagens) ? responseText.mensagens : responseText.result;   
		          var tipoMensagem = mensagens.tipoMensagem;
		          var listaMensagens = mensagens.listaMensagens;

		          if (tipoMensagem && listaMensagens) {
		           
		           if (tipoMensagem != 'SUCCESS') {
		            
		            exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialog-msg-upload');
		           }
		           exibirMensagem(tipoMensagem, listaMensagens); 
		          }
		      }, 
		      type: 'POST',
		      dataType: 'json',
	   });
	},
	
	limparCamposBaixaConsolidada : function() {
		
		$("#dataBaixaConsolidada", baixaFinanceiraController.workspace).datepicker(
				"setDate", baixaFinanceiraController.dataOperacaoDistribuidor
		);
		
		$("#uploadedFileConsolidada", baixaFinanceiraController.workspace).val("");
	}, 
	
	//-----------------------------------------------------
	
	// ------- View Radio Cobranca -------
	
	mostrarBaixa : function(tipoBaixa) {
		
		switch(tipoBaixa) {
	    
			case 'AUTOMATICA':
		    	
		    	baixaFinanceiraController.resetarCamposBaixaAutomatica();
	
				$(".area", baixaFinanceiraController.workspace).hide();
				
				baixaFinanceiraController.hideFiltroManual();
				baixaFinanceiraController.hideFiltroConsolidado();
								
				$('#tableBaixaAuto', baixaFinanceiraController.workspace).show();
				
				baixaFinanceiraController.tipoBaixa = 'AUTOMATICA';

			break;
			
		    case 'MANUAL':
		    	
		    	baixaFinanceiraController.limparCamposBaixaManual();
				
		    	baixaFinanceiraController.hideFiltroAutomatico();
		    	baixaFinanceiraController.hideFiltroConsolidado();
		    	
				$('#extratoBaixaManual', baixaFinanceiraController.workspace).hide();
				$('#tableBaixaManual', baixaFinanceiraController.workspace).show();
				
				baixaFinanceiraController.tipoBaixa = 'MANUAL';
			
	        break;
	        
		    case 'CONSOLIDADA':
		        
		    	baixaFinanceiraController.limparCamposBaixaConsolidada();
				
		    	baixaFinanceiraController.hideFiltroManual();
		    	baixaFinanceiraController.hideFiltroAutomatico();
		    	
		    	$('#tableBaixaConsolidada', baixaFinanceiraController.workspace).show();
		    	
				baixaFinanceiraController.tipoBaixa = 'CONSOLIDADA';
	    	
	        break;
		}
		
	},
	
	/* RESUMO BAIXA CONSOLIDADA - MANTER, POIS AINDA SERÁ FINALIZADO
	analisarBaixaConsolidada : function() {
		
		$("#dialog-baixaConsolidada").dialog({
			resizable : false,
			height : 450,
			width : 550,
			modal : true,
			open:function(){
			},
			buttons : {
				"Confirmar" : function() {
					
				}
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});
	},
	
		$("#banco").html("<fieldset><td> <strong> Nome: </strong> 1 - itau </td> </br><td> <strong> Qtd baixas: </strong> 104 </td> </br><td> 
		<strong> Valor baixas: </strong> 232994,90 </td> </br></fieldset><fieldset><td> <strong> Nome: </strong> 1 - itau </td> </br><td> 
		<strong> Qtd baixas: </strong> 104 </td> </br><td> <strong> Valor baixas: </strong> 232994,90 </td> </br></fieldset><fieldset><td> 
		<strong> Nome: </strong> 1 - itau </td> </br><td> <strong> Qtd baixas: </strong> 104 </td> </br><td> <strong> Valor baixas: 
		</strong> 232994,90 </td> </br></fieldset>");
	
	*/
	
	hideFiltroManual : function() {
		$('#tableBaixaManual', baixaFinanceiraController.workspace).hide();
		$('#extratoBaixaManual', baixaFinanceiraController.workspace).hide();
	},
	
	hideFiltroAutomatico : function() {
		$('#resultadoIntegracao', baixaFinanceiraController.workspace).hide();
		$('#tableBaixaAuto', baixaFinanceiraController.workspace).hide();
	},
	
	hideFiltroConsolidado : function() {
		$('#tableBaixaConsolidada', baixaFinanceiraController.workspace).hide();
	},
	
	//-----------------------------------------------------
	
	finalizarPostergacao : function() {
		
		var param = {dataPostergacao:$("#dtPostergada", baixaFinanceiraController.workspace).val(),
				isIsento:baixaFinanceiraController.buscarValueCheckBox('checkIsIsento')};
		param = serializeArrayToPost('idCobrancas',baixaFinanceiraController.obterCobrancasDividasMarcadas(),param);
		$.postJSON(contextPath + "/financeiro/baixa/finalizarPostergacao",param,
					function (result) {

						$("#dialog-postergar", baixaFinanceiraController.workspace).dialog("close");
						$(".liberaDividaGrid", baixaFinanceiraController.workspace).flexReload();
						
						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						
						if (tipoMensagem && listaMensagens) {
							
							exibirMensagem(tipoMensagem, listaMensagens);
						} 
						
						baixaFinanceiraController.limparModalPostergacao();
					},
					null,
					true
				);	
	},
	
	//OBTEM VALIDAÇÃO DE PERMISSÃO DE POSTERGAÇÃO
    obterPostergacao : function(calcularEncargo) {
    	
    	var param = {
    		dataPostergacao : $("#dtPostergada", baixaFinanceiraController.workspace).val()
    	};
		
    	param = 
    		serializeArrayToPost('idCobrancas', 
    			baixaFinanceiraController.obterCobrancasDividasMarcadas(), param);
    	
		$.postJSON(
			contextPath + "/financeiro/baixa/obterPostergacao", 
			param,
			function (result) {
	
				if (result) {
						
					baixaFinanceiraController.postergarDivida();
					
					if (calcularEncargo === true) {
						
						$("#ecargosPostergacao", baixaFinanceiraController.workspace).val(result);
					}
				}					
			},
			function() {
				$("#dtPostergada", baixaFinanceiraController.workspace).val('');
				$("#ecargosPostergacao", baixaFinanceiraController.workspace).val('');
			},
			true
		);
	},
	
	 alterarIsencao: function(isento) {
		 
		 if(isento) {
			 $("#ecargosPostergacao", baixaFinanceiraController.workspace).hide();
		 } else {
			 $("#ecargosPostergacao", baixaFinanceiraController.workspace).show();
		 }
			
	}, 
				
	postergarDivida : function() {
		
		var isento = $('#checkIsIsento', baixaFinanceiraController.workspace).attr('checked') === 'checked';
		
		baixaFinanceiraController.alterarIsencao(isento);
			
		$("#dialog-postergar", baixaFinanceiraController.workspace).dialog({
			resizable: false,
			height:220,
			width:300,
			modal: true,
			buttons:[ 
			          {
				           id:"bt_confirmar",
				           text:"Confirmar", 
				           click: function() {
				        	   baixaFinanceiraController.finalizarPostergacao();
				           }
			           },
			           {
				           id:"bt_cancelar",
				           text:"Cancelar", 
				           click: function() {
				        	   $( this ).dialog( "close" );
				        	   baixaFinanceiraController.limparModalPostergacao();
				           }
			           }
	        ],
			beforeClose: function() {
				baixaFinanceiraController.limparModalPostergacao();
				clearMessageDialogTimeout('dialogMensagemNovo');
			},
			form: $("#dialog-postergar", this.workspace).parents("form")
		});
	},

	limparModalPostergacao : function() {

		$("#dtPostergada", baixaFinanceiraController.workspace).val("");
		$("#checkIsIsento", baixaFinanceiraController.workspace).attr('checked', false);
		$("#ecargosPostergacao", baixaFinanceiraController.workspace).val("");
	},

	buscarValueCheckBox : function(checkName) {
		return $("#"+checkName, baixaFinanceiraController.workspace).is(":checked");
	},
	
	mostrarBancos : function(value) {
		$("#bancoDividas", baixaFinanceiraController.workspace).prop("selectedIndex",0);
		if(value == 'DEPOSITO' || value == 'TRANSFERENCIA_BANCARIA'){
			$("#bancoDividas", baixaFinanceiraController.workspace).show();
			$("#labelBanco", baixaFinanceiraController.workspace).show();
		}else{
			$("#bancoDividas", baixaFinanceiraController.workspace).hide();
			$("#labelBanco", baixaFinanceiraController.workspace).hide();
		}
	},
	
	habilitarIntegracao: function() {
		
		if ($("#uploadedFile", baixaFinanceiraController.workspace).val() == "") {

			$("#btnIntegrar", baixaFinanceiraController.workspace).hide();
			$("#btnExibirResumos", baixaFinanceiraController.workspace).show();
			
		} else {

			$("#btnIntegrar", baixaFinanceiraController.workspace).show();
			$("#btnExibirResumos", baixaFinanceiraController.workspace).hide();
		}
	},

	obterResumoBaixaFinanceira: function() {
		
		if ((!baixaFinanceiraController.tipoBaixa) || (baixaFinanceiraController.tipoBaixa == 'MANUAL')){
			
			return false;
		}

		var dataSelecionada = $("#dataBaixa", baixaFinanceiraController.workspace).val();

		var data = [{name:'data', value: dataSelecionada}];
		
		$.postJSON(contextPath + "/financeiro/baixa/mostrarResumoBaixaFinanceira",
			data,
			function(result) {

				$("#tableDadosArquivo", baixaFinanceiraController.workspace).hide();

				$("#tableDados", baixaFinanceiraController.workspace).css("text-align", "center");
				
				baixaFinanceiraController.mostrarDadosResumoBaixaFinanceira(result);
			}, 
			function(result) {

				if (result.mensagens) {

					exibirMensagem(
						resultado.mensagens.tipoMensagem, 
						resultado.mensagens.listaMensagens
					);
				}
			}
		);	
	},

	mostrarDadosResumoBaixaFinanceira: function(result) {

		$("#tableDadosResumoBaixa", baixaFinanceiraController.workspace).show();

		$('#resultadoIntegracao', baixaFinanceiraController.workspace  ).show();

		$("#quantidadePrevistos", baixaFinanceiraController.workspace).html(
			result.quantidadePrevisao ? result.quantidadePrevisao : 0
		);
		$("#quantidadeLidos", baixaFinanceiraController.workspace).html(
			result.quantidadeLidos ? result.quantidadeLidos : 0
		);
		$("#quantidadeBaixados", baixaFinanceiraController.workspace).html(
			result.quantidadeBaixados ? result.quantidadeBaixados : 0
		);
		$("#quantidadeRejeitados", baixaFinanceiraController.workspace).html(
			result.quantidadeRejeitados ? result.quantidadeRejeitados : 0
		);
		$("#quantidadeBaixadosComDivergencia", baixaFinanceiraController.workspace).html(
			result.quantidadeBaixadosComDivergencia ? result.quantidadeBaixadosComDivergencia : 0
		);
		$("#quantidadeInadimplentes", baixaFinanceiraController.workspace).html(
			result.quantidadeInadimplentes ? result.quantidadeInadimplentes : 0
		);

		var valorTotalBancario = result.valorTotalBancario ? result.valorTotalBancario : 0.0;
		
		valorTotalBancario = floatToPrice(valorTotalBancario);
		
		$("#tdValorTotal", baixaFinanceiraController.workspace).html(
			'<span id="valorTotalBancario">' + 
				valorTotalBancario + 
			'</span>'
		);

		if (result.possuiDiversasBaixas) {

			$("#valorTotalBancario", baixaFinanceiraController.workspace).wrap(
				'<a href="javascript:;" onclick="baixaFinanceiraController.mostrarGridTotalBancario();" />'
			);
		} 
	},
	
    confirmarBaixa : function() {
    	var param = serializeArrayToPost('idCobrancas',baixaFinanceiraController.obterCobrancasDividasMarcadas());
    	$.postJSON(contextPath + "/financeiro/baixa/confirmarBaixaDividas",param,
				   function() { baixaFinanceiraController.buscaManual(null); }
    	);
	},
	
	cancelarBaixa : function() {
		var param = serializeArrayToPost('idCobrancas',baixaFinanceiraController.obterCobrancasDividasMarcadas());
		$.postJSON(contextPath + "/financeiro/baixa/cancelarBaixaDividas",param,
				   function() { baixaFinanceiraController.buscaManual(null); }
		);
	},
	
    limparValorInformadoBaixaAutomatica : function(elm) {
    	
    	var dif = (elm.defaultValue != elm.value);
    	
    	if (dif == true){ 
		
    	    $("#valorFinanceiro", baixaFinanceiraController.workspace).val("");
    	}
	}
},
BaseController);

//@ sourceURL=baixaFinanceira.js
