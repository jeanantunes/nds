var baixaFinanceiraController = $.extend(true, {

	dataOperacaoDistribuidor: null,

	init : function() {
		$("#filtroNumCota", baixaFinanceiraController.workspace).numeric();
		$("#descricaoCota", baixaFinanceiraController.workspace).autocomplete({source: ""});
		$("#dataBaixa", baixaFinanceiraController.workspace).datepicker({
			showOn : "button",
			buttonImage : contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat : 'dd/mm/yy',
		});
		$("filtroNossoNumero", baixaFinanceiraController.workspace).numeric();
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
		    thousandsSeparator: '.'
		});

		$("#radioBaixaManual", baixaFinanceiraController.workspace).focus();

		baixaFinanceiraController.dataOperacaoDistribuidor = $("#dataBaixa").datepicker( "getDate" );
		
		baixaFinanceiraController.iniciarGridPrevisao();
	},
	
	iniciarGridPrevisao : function() {
		$(".previsaoGrid").flexigrid({
			preProcess: baixaFinanceiraController.getDataFromResult,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 50,
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
				width : 140,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valorBoleto',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Data Vencimento',
				name : 'dataVencimento',
				width : 90,
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
	
	getDataFromResult : function() {
		
	},
	
    //BAIXA MANUAL--------------------------------------
    
    //POPUPS
    popup_detalhes : function (codigo) {
		
    	baixaFinanceiraController.obterDetalhesDivida(codigo);
		
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
				        	   
				        	   baixaFinanceiraController.popup_confirma_baixa_dividas();
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

    mostrarBaixaManual : function() {
		
    	baixaFinanceiraController.limparCamposBaixaManual();
		
		$('#resultadoIntegracao', baixaFinanceiraController.workspace).hide();
		$('#tableBaixaAuto', baixaFinanceiraController.workspace).hide();
		$('#extratoBaixaManual', baixaFinanceiraController.workspace).hide();
		$('#tableBaixaManual', baixaFinanceiraController.workspace).show();
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
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nome',
				width : 440,
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
			sortname : "Data Vencimento",
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
			
	        $("#totalDividasSelecionadas", baixaFinanceiraController.workspace).html($("#totalDividas").html());
		    $("#totalDividasSelecionadasHidden", baixaFinanceiraController.workspace).val($("#totalDividasHidden").val());
        }
		
		else{
			
			var elem = document.getElementById("textoSelTodos");
			elem.innerHTML = "Marcar todos";
			
			$("#totalDividasSelecionadas").html("0,00");
			$("#totalDividasSelecionadasHidden").val("0,00");
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
		    thousandsSeparator: '.'
		});
		
		$("#totalDividasSelecionadas", baixaFinanceiraController.workspace).html($("#totalDividasSelecionadasHidden", baixaFinanceiraController.workspace).val());
	},
	
	
    //POPULA GRADE DE DIVIDAS, ATUALIZANDO TOTALIZAÇÕES
	getDataFromResultDividas : function(resultado) {	
		
		//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			$(".grids", baixaFinanceiraController.workspace).hide();
			return resultado;
		}
		
		$("#selTodos", baixaFinanceiraController.workspace).attr("checked", false);
		baixaFinanceiraController.selecionarTodos(false);
		
		var totalDividas=0;
		$.each(resultado.rows, function(index, row) {
			
			valorItem = removeMascaraPriceFormat(row.cell.valor);
			totalDividas = intValue(totalDividas) + intValue(valorItem);
			
			var detalhes = '<a href="javascript:;" onclick="baixaFinanceiraController.popup_detalhes(' + row.cell.codigo + ');" style="cursor:pointer">' +
					 	   '<img title="Detalhes da Dívida" src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0px" />' +
						   '</a>';	
		
			var checkBox;			   
			if (row.cell.check){			   
			    checkBox = '<input checked="' + row.cell.check + '" type="checkbox" name="checkboxGrid" id="checkbox_'+ row.cell.codigo +'" onchange="baixaFinanceiraController.calculaSelecionados(this.checked,'+ valorItem +'); dataHolder.hold(\'baixaManual\', '+ row.cell.codigo +', \'checado\', this.checked); " />';	
			    baixaFinanceiraController.calculaSelecionados(true, valorItem);
			}
			else{
			    checkBox = '<input title="Selecionar Dívida" type="checkbox" name="checkboxGrid" id="checkbox_'+ row.cell.codigo +'" onchange="baixaFinanceiraController.calculaSelecionados(this.checked,'+ valorItem +'); dataHolder.hold(\'baixaManual\', '+ row.cell.codigo +', \'checado\', this.checked); " />';
			}

			row.cell.acao = detalhes;
		    row.cell.check = checkBox;
		});

		$("#totalDividasHidden", baixaFinanceiraController.workspace).val(totalDividas);
		$('#totalDividasHidden', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
		$("#totalDividas", baixaFinanceiraController.workspace).html($("#totalDividasHidden", baixaFinanceiraController.workspace).val());
		
		$(".grids", baixaFinanceiraController.workspace).show();
		
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
					width : 90,
					sortable : true,
					align : 'center'
				},{
					display : ' ',
					name : 'tipo',
					width : 80,
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
					width : 320,
					sortable : true,
					align : 'left'
				}],
				width : 620,
				height : 160
			});
		});
	},
    
    //POPULA GRADE DE DETALHES DA DIVIDA E CALCULA SALDO DE DIVIDAS
    obterDetalhesDivida : function(idDividaCobranca){
    	
		$(".dadosDividaGrid", baixaFinanceiraController.workspace).flexOptions({
			url: contextPath + "/financeiro/obterDetalhesDivida",
			params: [
			         {name:'idCobranca', value: idDividaCobranca}
			        ] ,
			        newp: 1
		});
		$(".dadosDividaGrid", baixaFinanceiraController.workspace).flexReload();
		$(".grids", baixaFinanceiraController.workspace).show();
	},
	
	getDataFromResultDivida : function(resultado) {
		
		var saldoDivida=0;
		$.each(resultado.rows, function(index, row) {
			saldoDivida = saldoDivida + intValue(removeMascaraPriceFormat(row.cell.valor));
		});
		
		$("#saldoDividaHidden", baixaFinanceiraController.workspace).val(saldoDivida);
		$('#saldoDividaHidden', baixaFinanceiraController.workspace).priceFormat({
		    allowNegative: true,
			centsSeparator: ',',
			thousandsSeparator: '.'
		});
		$("#saldoDivida", baixaFinanceiraController.workspace).html($("#saldoDividaHidden", baixaFinanceiraController.workspace).val());
		
		$(".grids", baixaFinanceiraController.workspace).show();
		
		return resultado;
	},

	
	//EFETUA BUSCA DE DIVIDAS(POR COTA) OU COBRANCA(POR NOSSO NUMERO)
	buscaManual : function() {

		dataHolder.clearAction('baixaManual', baixaFinanceiraController.workspace);

		var nossoNumero = $("#filtroNossoNumero", baixaFinanceiraController.workspace).val();
		var numCota = $("#filtroNumCota", baixaFinanceiraController.workspace).val();
		
		if (nossoNumero==''){
			
			/*BAIXA MANUAL DE DÍVIDAS*/
			$(".liberaDividaGrid", baixaFinanceiraController.workspace).flexOptions({
				url: contextPath + "/financeiro/buscaDividas",
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
			
			/*BAIXA INDIVIDUAL DE COBRANÇA(BOLETO)*/
			var data = [{name: 'nossoNumero', value: nossoNumero}];
			$.postJSON(contextPath + "/financeiro/buscaBoleto",
					   data,
					   baixaFinanceiraController.sucessCallbackPesquisarBoleto, 
					   baixaFinanceiraController.errorCallbackPesquisarBoleto);
			
		}	
	},
	
	sucessCallbackPesquisarBoleto : function(resultado) {
		
		$("#cota", baixaFinanceiraController.workspace).html(resultado.cota);
		$("#banco", baixaFinanceiraController.workspace).html(resultado.banco);
		$("#nossoNumero", baixaFinanceiraController.workspace).html(resultado.nossoNumero);
		$("#dataEmissao", baixaFinanceiraController.workspace).html(resultado.dataEmissao);
		$("#dataVencimento", baixaFinanceiraController.workspace).html(resultado.dataVencimento);
		
		$("#dividaTotal", baixaFinanceiraController.workspace).html(resultado.dividaTotal);
		$("#dataPagamento", baixaFinanceiraController.workspace).html(resultado.dataPagamento);
		
		$("#desconto", baixaFinanceiraController.workspace).val(resultado.desconto);
		$("#juros", baixaFinanceiraController.workspace).val(resultado.juros);
		$("#multa", baixaFinanceiraController.workspace).val(resultado.multa);
		
		$("#valorTotalHidden", baixaFinanceiraController.workspace).val(resultado.valorTotal);
		$("#valorBoletoHidden", baixaFinanceiraController.workspace).val(resultado.valor);
		
		$('#juros', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
		    centsSeparator: ',',
		    thousandsSeparator: '.'
		});
		
		$('#multa', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
		
		$('#desconto', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
		
		$('#valorTotalHidden', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
		
		$('#valorBoletoHidden', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
		
		$("#valorTotal", baixaFinanceiraController.workspace).html($("#valorTotalHidden", baixaFinanceiraController.workspace).val());
		$("#valorBoleto", baixaFinanceiraController.workspace).html($("#valorBoletoHidden", baixaFinanceiraController.workspace).val());
		
		baixaFinanceiraController.dividaManualNossoNumero();
	},
	
	errorCallbackPesquisarBoleto : function() {
		$('#extratoBaixaManual', baixaFinanceiraController.workspace).hide();
	},
	
	
	//EFETUA BAIXA DE COBRANCA POR NOSSO NUMERO
    baixaPorNossoNumero : function() {
		
    	var nossoNumero = $("#nossoNumero", baixaFinanceiraController.workspace).html();
		var dataVencimento = $("#dataVencimento", baixaFinanceiraController.workspace).html();
		var valor = $("#valorBoletoHidden", baixaFinanceiraController.workspace).val();
		var desconto = $("#desconto", baixaFinanceiraController.workspace).val();
		var juros = $("#juros", baixaFinanceiraController.workspace).val();
		var multa = $("#multa", baixaFinanceiraController.workspace).val();
		
		$.postJSON(contextPath + "/financeiro/baixaManualBoleto",
				   "nossoNumero="+nossoNumero+
				   "&valor="+ valor +
				   "&dataVencimento="+ dataVencimento+
				   "&desconto="+ desconto +
				   "&juros="+ juros+
				   "&multa="+ multa,
				   function() {mostrarBaixaManual();});
	},
	
	
	//CALCULA TOTAL CONFORME AÇÃO DO USUARIO NA TELA DE BAIXA POR NOSSO NUMERO
	calculaTotalManual : function() {
    	
		var valorBoleto = removeMascaraPriceFormat($("#valorBoletoHidden", baixaFinanceiraController.workspace).val());
		var desconto = removeMascaraPriceFormat($("#desconto", baixaFinanceiraController.workspace).val());
		var juros = removeMascaraPriceFormat($("#juros", baixaFinanceiraController.workspace).val());
		var multa = removeMascaraPriceFormat($("#multa", baixaFinanceiraController.workspace).val());
		
		var total = intValue(valorBoleto) + intValue(juros) + intValue(multa) - intValue(desconto);
        
		$("#valorTotalHidden", baixaFinanceiraController.workspace).val(total);
		
		$("#valorTotalHidden", baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});

		$("#valorTotal", baixaFinanceiraController.workspace).html($("#valorTotalHidden", baixaFinanceiraController.workspace).val());
	},
	
	
	//OBTEM OS CODIGOS DAS DIVIDAS MARCADAS
	obterCobrancasDividasMarcadas : function(){

		var dividasMarcadas='';
		var table = $("#tabelaDividas tr", baixaFinanceiraController.workspace);
		
		for(i = 0; i < table.length; i++){   
			
			/*if (document.getElementById("checkbox_"+table.rows[i].cells[0].textContent).checked){
			    table.rows[i].cells[0].textContent; 
			    dividasMarcadas+='idCobrancas='+ table.rows[i].cells[0].textContent + '&';
		    }*/
			if ($("#checkbox_"+table[i].cells[0].textContent, baixaFinanceiraController.workspace).attr("checked") == "checked") {
				table[i].cells[0].textContent; 
				dividasMarcadas+='idCobrancas='+ table[i].cells[0].textContent + '&';
			}

		} 
		
		return dividasMarcadas;
	},
	
	
	//OBTEM DADOS CALCULADOS REFERENTES ÀS DAS DIVIDAS MARCADAS PARA A TELA DE BAIXA POR COTA
    obterPagamentoDividas : function() {
		$.postJSON(contextPath + "/financeiro/obterPagamentoDividas",
				   baixaFinanceiraController.obterCobrancasDividasMarcadas(),
				   baixaFinanceiraController.sucessCallbackPagamentoDivida,
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
		    thousandsSeparator: '.'
		});
		
		$('#multaDividas', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
		
		$('#jurosDividas', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
		
		$('#descontoDividas', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
		
		$('#valorPagoDividas', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
		
		$('#valorSaldoDividas', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
		
		$('#valorSaldoDividasHidden', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
		
		$('#valorDividasHidden', baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
		
		$("#valorSaldoDividas", baixaFinanceiraController.workspace).html($("#valorSaldoDividasHidden", baixaFinanceiraController.workspace).val());
		$("#valorDividas", baixaFinanceiraController.workspace).html($("#valorDividasHidden", baixaFinanceiraController.workspace).val());
		
		baixaFinanceiraController.popup_baixa_dividas();
	},
    
    
    //CALCULA TOTAL CONFORME AÇÃO DO USUARIO NA TELA DE BAIXA POR COTA
	calculaTotalManualDividas : function() {
    	
		var valorDividas = removeMascaraPriceFormat($("#valorDividasHidden", baixaFinanceiraController.workspace).val());
		
		var desconto = removeMascaraPriceFormat($("#descontoDividas", baixaFinanceiraController.workspace).val());
		var juros = removeMascaraPriceFormat($("#jurosDividas", baixaFinanceiraController.workspace).val());
		var multa = removeMascaraPriceFormat($("#multaDividas", baixaFinanceiraController.workspace).val());

		var valorPago = intValue(valorDividas) + intValue(juros) + intValue(multa) - intValue(desconto);
			
		$("#valorPagoDividas", baixaFinanceiraController.workspace).val(valorPago);
		
		$("#valorPagoDividas", baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
	},

    
	//CALCULA SALDO CONFORME AÇÃO DO USUARIO NA TELA DE BAIXA POR COTA
	calculaSaldoDividas : function() {
    	
		var valorDividas = removeMascaraPriceFormat($("#valorDividasHidden", baixaFinanceiraController.workspace).val());		
		var valorPago = removeMascaraPriceFormat($("#valorPagoDividas", baixaFinanceiraController.workspace).val());
		
		var desconto = removeMascaraPriceFormat($("#descontoDividas", baixaFinanceiraController.workspace).val());
		var juros = removeMascaraPriceFormat($("#jurosDividas", baixaFinanceiraController.workspace).val());
		var multa = removeMascaraPriceFormat($("#multaDividas", baixaFinanceiraController.workspace).val());

		var valorSaldo = intValue(valorDividas) + intValue(juros) + intValue(multa) - ( intValue(valorPago) + intValue(desconto) );
		
		if (valorSaldo < 0){
			valorSaldo = 0;
		}
		
        $("#valorSaldoDividasHidden", baixaFinanceiraController.workspace).val(valorSaldo);
		
		$("#valorSaldoDividasHidden", baixaFinanceiraController.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
		$("#valorSaldoDividas", baixaFinanceiraController.workspace).html($("#valorSaldoDividasHidden", baixaFinanceiraController.workspace).val());
	},
    
    
    //EFETUA BAIXA MANUAL DE DIVIDAS SELECIONADAS E CALCULADAS
    baixaManualDividas : function(manterPendente) {

    	var valorDividas = $("#valorDividas", baixaFinanceiraController.workspace).html();
    	var multaDividas = $("#multaDividas", baixaFinanceiraController.workspace).val();
    	var jurosDividas = $("#jurosDividas", baixaFinanceiraController.workspace).val();
    	var descontoDividas = $("#descontoDividas", baixaFinanceiraController.workspace).val();
    	var valorPagoDividas = $("#valorPagoDividas", baixaFinanceiraController.workspace).val();
    	var valorSaldoDividas = $("#valorSaldoDividas", baixaFinanceiraController.workspace).html();
    	var formaRecebimentoDividas = $("#formaRecebimentoDividas", baixaFinanceiraController.workspace).val();
    	var observacoesDividas = $("#observacoesDividas", baixaFinanceiraController.workspace).val();

		$.postJSON(contextPath + "/financeiro/baixaManualDividas",
				   "manterPendente="+manterPendente+
				   "&valorDividas="+valorDividas+
				   "&valorMulta="+ multaDividas +
				   "&valorJuros="+ jurosDividas+
				   "&valorDesconto="+ descontoDividas +
				   "&valorPagamento="+ valorPagoDividas+
				   "&valorSaldo="+ valorSaldoDividas+
				   "&tipoPagamento="+ formaRecebimentoDividas+
				   "&observacoes="+ observacoesDividas +
				   "&"+baixaFinanceiraController.obterCobrancasDividasMarcadas(),
				   function(mensagens) {
					   
			           $("#dialog-baixa-dividas", baixaFinanceiraController.workspace).dialog("close");
					   
					   if (mensagens){
						   var tipoMensagem = mensagens.tipoMensagem;
						   var listaMensagens = mensagens.listaMensagens;
						   if (tipoMensagem && listaMensagens) {
						       exibirMensagem(tipoMensagem, listaMensagens);
					       }
		        	   }
			           
					   baixaFinanceiraController.buscaManual();
	               },
	               null,
	               true);
	},
    
    
    //OBTEM VALIDAÇÃO DE PERMISSÃO DE NEGOCIAÇÃO
    obterNegociacao : function() {
		$.postJSON(contextPath + "/financeiro/obterNegociacao",
				baixaFinanceiraController.obterCobrancasDividasMarcadas());
	},

	
	//-----------------------------------------------------
	

	//BAIXA AUTOMÁTICA-------------------------------------

	mostrarBaixaAuto : function() {
		
		baixaFinanceiraController.limparCamposBaixaAutomatica();
		
		$('#tableBaixaManual', baixaFinanceiraController.workspace).hide();
		$('#extratoBaixaManual', baixaFinanceiraController.workspace).hide();
		$('#tableBaixaAuto', baixaFinanceiraController.workspace).show();
	},
	
	integrar : function() {
		
		$('#formBaixaAutomatica', baixaFinanceiraController.workspace).submit();
	},
	
	mostrarGridBoletosPrevisao : function() {
		
		var dataHidden = $("#dataHidden", baixaFinanceiraController.workspace).val();
		
		$(".previsaoGrid", baixaFinanceiraController.workspace).flexOptions({
			url: contextPath + "/financeiro/mostrarGridBoletosPrevisao",
			onSuccess: baixaFinanceiraController.executarAposProcessamento,
			params: [
		         {name:'data', value: dataHidden}
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
			
			$("#quantidadeLidos", baixaFinanceiraController.workspace).html(responseJson.result.quantidadeLidos);
			$("#quantidadeBaixados", baixaFinanceiraController.workspace).html(responseJson.result.quantidadeBaixados);
			$("#quantidadeRejeitados", baixaFinanceiraController.workspace).html(responseJson.result.quantidadeRejeitados);
			$("#quantidadeBaixadosComDivergencia", baixaFinanceiraController.workspace).html(responseJson.result.quantidadeBaixadosComDivergencia);
			
			baixaFinanceiraController.limparCamposBaixaAutomatica();
			
			$('#resultadoIntegracao', baixaFinanceiraController.workspace).show();
		}
	},
	
	limparCamposBaixaAutomatica : function() {

		$("#uploadedFile", baixaFinanceiraController.workspace).replaceWith(
			"<input name='uploadedFile' type='file' id='uploadedFile' size='25' " 
				+ "onchange='baixaFinanceiraController.habilitarIntegracao();' />"
		);
		
		$("#valorFinanceiro", baixaFinanceiraController.workspace).val("");

		baixaFinanceiraController.habilitarBaixaAutomatica(true);

		$("#dataBaixa", baixaFinanceiraController.workspace).datepicker(
			"setDate", baixaFinanceiraController.dataOperacaoDistribuidor
		);

		$("#btnIntegrar", baixaFinanceiraController.workspace).css("display", "none");
		$("#btnExibirResumos", baixaFinanceiraController.workspace).css("display", "block");
	},
	
	//-----------------------------------------------------
	
	finalizarPostergacao : function() {
		
		$.postJSON(contextPath + "/financeiro/finalizarPostergacao",
					"dataPostergacao=" + $("#dtPostergada", baixaFinanceiraController.workspace).val() +
					"&isIsento=" + baixaFinanceiraController.buscarValueCheckBox('checkIsIsento') +
					"&" + baixaFinanceiraController.obterCobrancasDividasMarcadas(),
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
    obterPostergacao : function() {

		$.postJSON(contextPath + "/financeiro/obterPostergacao",
					"dataPostergacao=" + $("#dtPostergada").val() +
					"&" + baixaFinanceiraController.obterCobrancasDividasMarcadas(),
					function (result) {
						if (result) {

							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
						
							if (tipoMensagem && listaMensagens) {
								
								exibirMensagem(tipoMensagem, listaMensagens);
							} 

							if (!tipoMensagem) {
								baixaFinanceiraController.postergarDivida();
								$("#ecargosPostergacao", baixaFinanceiraController.workspace).val(result)
							}	
						}							
					},
					null,
					true
				);
	},
			
	postergarDivida : function() {
	
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
	
	alterarEstadoInputsBaixaAutomatica: function() {
		
		var dataSelecionada = $("#dataBaixa", baixaFinanceiraController.workspace).datepicker( "getDate" );
		
		baixaFinanceiraController.habilitarIntegracao();

		if (dataSelecionada > baixaFinanceiraController.dataOperacaoDistribuidor ||
				dataSelecionada < baixaFinanceiraController.dataOperacaoDistribuidor) {

			baixaFinanceiraController.habilitarBaixaAutomatica(false);
		
		} else {
			
			baixaFinanceiraController.limparCamposBaixaAutomatica();
		}
	},

	habilitarBaixaAutomatica: function(habilitar) {

		$("#uploadedFile", baixaFinanceiraController.workspace).enable(habilitar);
		$("#valorFinanceiro", baixaFinanceiraController.workspace).enable(habilitar);
	},

	habilitarIntegracao: function() {

		var dataSelecionada = $("#dataBaixa", baixaFinanceiraController.workspace).datepicker( "getDate" );

		if (dataSelecionada > baixaFinanceiraController.dataOperacaoDistribuidor ||
				dataSelecionada < baixaFinanceiraController.dataOperacaoDistribuidor) {

			$("#btnIntegrar", baixaFinanceiraController.workspace).css("display", "none");
			$("#btnExibirResumos", baixaFinanceiraController.workspace).css("display", "block");

		} else {

			$("#btnIntegrar", baixaFinanceiraController.workspace).css("display", "block");
			$("#btnExibirResumos", baixaFinanceiraController.workspace).css("display", "none");
		}
	}

	
}, BaseController);

//@ sourceURL=baixaFinanceira.js
