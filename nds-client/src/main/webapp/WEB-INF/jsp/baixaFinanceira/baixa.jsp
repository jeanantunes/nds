<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/data.holder.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>

	<script type="text/javascript">
	
		var pesquisaCotaBaixaFinanceira = new PesquisaCota();
	
		$(function() {
			$("#filtroNumCota").numeric();
			$("#descricaoCota").autocomplete({source: ""});
			$("filtroNossoNumero").numeric();
		}); 
	
		
		
	
	    //BAIXA MANUAL--------------------------------------
	    
	    
	    
	    //POPUPS
	    function popup_detalhes(codigo) {
			
			obterDetalhesDivida(codigo);
			
			$( "#dialog-detalhes-divida" ).dialog({
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
		        ]
			});
		};
		
	    function popup_baixa_dividas() {
			$( "#dialog-baixa-dividas" ).dialog({
				resizable: false,
				height:430,
				width:480,
				modal: true,
				buttons:[ 
				          {
					           id:"bt_confirmar",
					           text:"Confirmar", 
					           click: function() {
					        	   
					        	   popup_confirma_baixa_dividas();
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
			    }
			});
		};

		function popup_confirma_baixa_dividas() {
			$( "#dialog-confirma-baixa" ).dialog({
				resizable: false,
				height:130,
				width:470,
				modal: true,
				buttons:[ 
				          {
					           id:"bt_confirmar",
					           text:"Confirmar", 
					           click: function() {
					        	   popup_confirma_pendente();
									
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
			    }
			});
		};
		
		function popup_confirma_pendente() {
			$( "#dialog-confirma-pendente" ).dialog({
				resizable: false,
				height:170,
				width:550,
				modal: true,
				buttons:[ 
				          {
					           id:"bt_sim",
					           text:"Sim", 
					           click: function() {
					        	   baixaManualDividas(true);
									
								   $( this ).dialog( "close" );
					           }
				           },
				           {
					           id:"bt_nao",
					           text:"Não", 
					           click: function() {
					        	   baixaManualDividas(false);
									
								   $( this ).dialog( "close" );
					           }
				           }
		        ],
				beforeClose: function() {
					clearMessageDialogTimeout();
			    }
			});
		};
		
		function mostrarPopupPagamento() {
			$( "#dialog-confirma-baixa-numero" ).dialog({
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
									
								   baixaPorNossoNumero();
					           }
				           },
				           {
					           id:"bt_cancelar",
					           text:"Cancelar", 
					           click: function() {
					        	   $( this ).dialog( "close" );
					           }
				           }
		        ]
			});
		};
	
        function mostrarBaixaManual() {
			
			limparCamposBaixaManual();
			
			$('#resultadoIntegracao').hide();
			$('#tableBaixaAuto').hide();
			$('#extratoBaixaManual').hide();
			$('#tableBaixaManual').show();
		}
		
		function dividaManualNossoNumero() {
			
			$('#porCota').hide();
			$('#extratoBaixaManual').show();
			$('#porNossoNumero').show();
		}
		
		function dividaManualCota() {
			
			$('#porNossoNumero').hide();
			$('#extratoBaixaManual').show();
			$('#porCota').show();
		}
		
		function limparCamposBaixaManual() {
			
			$('#filtroNumCota').val("");
			$('#descricaoCota').val("");
			$('#filtroNossoNumero').val("");
		}
		
		
		
		
		//GRADE DE DIVIDAS
		$(function() {
			$(".liberaDividaGrid").flexigrid({
				preProcess: getDataFromResultDividas,
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
		});
	


		
		//SELECIONAR TODAS AS DIVIDAS
		function selecionarTodos(checked){
			
			for (var i=0;i<document.formularioListaDividas.elements.length;i++) {
			     var x = document.formularioListaDividas.elements[i];
			     if (x.name == 'checkboxGrid') {
			    	 x.checked = checked;
			     }    
			}
			
			if (checked){
				
				var elem = document.getElementById("textoSelTodos");
				elem.innerHTML = "Desmarcar todos";
				
    	        $("#totalDividasSelecionadas").html($("#totalDividas").html());
			    $("#totalDividasSelecionadasHidden").val($("#totalDividasHidden").val());
            }
			
			else{
				
				var elem = document.getElementById("textoSelTodos");
				elem.innerHTML = "Marcar todos";
				
				$("#totalDividasSelecionadas").html("0,00");
				$("#totalDividasSelecionadasHidden").val("0,00");
			}
		}
		

		
		
        //CALCULAR VALOR TOTAL DAS DIVIDAS SELECIONADAS
		function calculaSelecionados(checked, valor) {
			
			var totalSelecionado = removeMascaraPriceFormat($("#totalDividasSelecionadasHidden").val());
		    
			if(checked){
				totalSelecionado = intValue(totalSelecionado) + intValue(valor);
			}
			else{
				totalSelecionado = intValue(totalSelecionado) - intValue(valor);
			}

			$("#totalDividasSelecionadasHidden").val(totalSelecionado);
			
			$("#totalDividasSelecionadasHidden").priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$("#totalDividasSelecionadas").html($("#totalDividasSelecionadasHidden").val());
		}
		
		
		
		
        //POPULA GRADE DE DIVIDAS, ATUALIZANDO TOTALIZAÇÕES
		function getDataFromResultDividas(resultado) {	
			
			//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
			if (resultado.mensagens) {
				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				$(".grids").hide();
				return resultado;
			}
			
			document.getElementById("selTodos").checked = false;
			selecionarTodos(false);
			
			var totalDividas=0;
			$.each(resultado.rows, function(index, row) {
				
				valorItem = removeMascaraPriceFormat(row.cell.valor);
				totalDividas = intValue(totalDividas) + intValue(valorItem);
				
				var detalhes = '<a href="javascript:;" onclick="popup_detalhes(' + row.cell.codigo + ');" style="cursor:pointer">' +
						 	   '<img title="Detalhes da Dívida" src="${pageContext.request.contextPath}/images/ico_detalhes.png" hspace="5" border="0px" />' +
							   '</a>';	
			
				var checkBox;			   
				if (row.cell.check){			   
				    checkBox = '<input checked="' + row.cell.check + '" type="checkbox" name="checkboxGrid" id="checkbox_'+ row.cell.codigo +'" onchange="calculaSelecionados(this.checked,'+ valorItem +'); dataHolder.hold(\'baixaManual\', '+ row.cell.codigo +', \'checado\', this.checked); " />';	
				    calculaSelecionados(true, valorItem);
				}
				else{
				    checkBox = '<input title="Selecionar Dívida" type="checkbox" name="checkboxGrid" id="checkbox_'+ row.cell.codigo +'" onchange="calculaSelecionados(this.checked,'+ valorItem +'); dataHolder.hold(\'baixaManual\', '+ row.cell.codigo +', \'checado\', this.checked); " />';
				}

				row.cell.acao = detalhes;
			    row.cell.check = checkBox;
			});

			$("#totalDividasHidden").val(totalDividas);
			$('#totalDividasHidden').priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			$("#totalDividas").html($("#totalDividasHidden").val());
			
			$(".grids").show();
			
			return resultado;
		}
		
        
        
        
        //GRADE DE DETALHES DA DIVIDA
		$(function() {
			$(".dadosDividaGrid").flexigrid({
				preProcess: getDataFromResultDivida,
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
		
        
        
        
        //POPULA GRADE DE DETALHES DA DIVIDA E CALCULA SALDO DE DIVIDAS
        function obterDetalhesDivida(idDividaCobranca){
        	
			$(".dadosDividaGrid").flexOptions({
				url: "<c:url value='/financeiro/obterDetalhesDivida' />",
				params: [
				         {name:'idCobranca', value: idDividaCobranca}
				        ] ,
				        newp: 1
			});
			$(".dadosDividaGrid").flexReload();
			$(".grids").show();
		}
		
		function getDataFromResultDivida(resultado) {
			
			var saldoDivida=0;
			$.each(resultado.rows, function(index, row) {
				saldoDivida = saldoDivida + intValue(removeMascaraPriceFormat(row.cell.valor));
			});
			
			$("#saldoDividaHidden").val(saldoDivida);
			$('#saldoDividaHidden').priceFormat({
			    allowNegative: true,
				centsSeparator: ',',
				thousandsSeparator: '.'
			});
			$("#saldoDivida").html($("#saldoDividaHidden").val());
			
			$(".grids").show();
			
			return resultado;
		}


		
		
		//EFETUA BUSCA DE DIVIDAS(POR COTA) OU COBRANCA(POR NOSSO NUMERO)
		function buscaManual() {

			dataHolder.clearAction('baixaManual');

			var nossoNumero = $("#filtroNossoNumero").val();
			var numCota = $("#filtroNumCota").val();
			
			if (nossoNumero==''){
				
				/*BAIXA MANUAL DE DÍVIDAS*/
				$(".liberaDividaGrid").flexOptions({
					url: "<c:url value='/financeiro/buscaDividas' />",
					params: [
					         {name:'numCota', value: numCota}
					        ] ,
					        newp: 1
				});
				
				$(".liberaDividaGrid").flexReload();
				
				$(".grids").show();
				
				dividaManualCota();
			}
			
			else{
				
				/*BAIXA INDIVIDUAL DE COBRANÇA(BOLETO)*/
				var data = [{name: 'nossoNumero', value: nossoNumero}];
				$.postJSON("<c:url value='/financeiro/buscaBoleto' />",
						   data,
						   sucessCallbackPesquisarBoleto, 
						   errorCallbackPesquisarBoleto);
				
			}	
		}
		
		function sucessCallbackPesquisarBoleto(resultado) {
			
			$("#cota").html(resultado.cota);
			$("#banco").html(resultado.banco);
			$("#nossoNumero").html(resultado.nossoNumero);
			$("#dataEmissao").html(resultado.dataEmissao);
			$("#dataVencimento").html(resultado.dataVencimento);
			
			$("#dividaTotal").html(resultado.dividaTotal);
			$("#dataPagamento").html(resultado.dataPagamento);
			
			$("#desconto").val(resultado.desconto);
			$("#juros").val(resultado.juros);
			$("#multa").val(resultado.multa);
			
			$("#valorTotalHidden").val(resultado.valorTotal);
			$("#valorBoletoHidden").val(resultado.valor);
			
			$('#juros').priceFormat({
				allowNegative: true,
			    centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#multa').priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#desconto').priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#valorTotalHidden').priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#valorBoletoHidden').priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$("#valorTotal").html($("#valorTotalHidden").val());
			$("#valorBoleto").html($("#valorBoletoHidden").val());
			
			dividaManualNossoNumero();
		}
		
		function errorCallbackPesquisarBoleto() {
			$('#extratoBaixaManual').hide();
		}
		
		
		
		
		//EFETUA BAIXA DE COBRANCA POR NOSSO NUMERO
        function baixaPorNossoNumero() {
			
        	var nossoNumero = $("#nossoNumero").html();
			var dataVencimento = $("#dataVencimento").html();
			var valor = $("#valorBoletoHidden").val();
			var desconto = $("#desconto").val();
			var juros = $("#juros").val();
			var multa = $("#multa").val();
			
			$.postJSON("<c:url value='/financeiro/baixaManualBoleto'/>",
					   "nossoNumero="+nossoNumero+
					   "&valor="+ valor +
					   "&dataVencimento="+ dataVencimento+
					   "&desconto="+ desconto +
					   "&juros="+ juros+
					   "&multa="+ multa,
					   function() {mostrarBaixaManual();});
		}

		
		
		
		//CALCULA TOTAL CONFORME AÇÃO DO USUARIO NA TELA DE BAIXA POR NOSSO NUMERO
		function calculaTotalManual() {
        	
			var valorBoleto = removeMascaraPriceFormat($("#valorBoletoHidden").val());
			var desconto = removeMascaraPriceFormat($("#desconto").val());
			var juros = removeMascaraPriceFormat($("#juros").val());
			var multa = removeMascaraPriceFormat($("#multa").val());
			
			var total = intValue(valorBoleto) + intValue(juros) + intValue(multa) - intValue(desconto);
            
			$("#valorTotalHidden").val(total);
			
			$("#valorTotalHidden").priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});

			$("#valorTotal").html($("#valorTotalHidden").val());
		}
		

		
		
		//OBTEM OS CODIGOS DAS DIVIDAS MARCADAS
		function obterCobrancasDividasMarcadas(){

			var dividasMarcadas='';
			var table = document.getElementById("tabelaDividas");
			
			for(i = 0; i < table.rows.length; i++){   
				
				if (document.getElementById("checkbox_"+table.rows[i].cells[0].textContent).checked){
				    table.rows[i].cells[0].textContent; 
				    dividasMarcadas+='idCobrancas='+ table.rows[i].cells[0].textContent + '&';
			    }

			} 
			
			return dividasMarcadas;
		}
		
		
		
		
		//OBTEM DADOS CALCULADOS REFERENTES ÀS DAS DIVIDAS MARCADAS PARA A TELA DE BAIXA POR COTA
        function obterPagamentoDividas() {
			$.postJSON("<c:url value='/financeiro/obterPagamentoDividas' />",
					   obterCobrancasDividasMarcadas(),
					   sucessCallbackPagamentoDivida,
					   null);
		}
		
        function sucessCallbackPagamentoDivida(resultado) {
			
		    $("#multaDividas").val(resultado.valorMulta);
			$("#jurosDividas").val(resultado.valorJuros);
			$("#descontoDividas").val(resultado.valorDesconto);
			$("#valorPagoDividas").val(resultado.valorPagamento);
			$("#formaRecebimentoDividas").val(resultado.tipoPagamento);
			$("#observacoesDividas").val(resultado.observacoes);
			
			$("#valorSaldoDividasHidden").val(resultado.valorSaldo);
			$("#valorDividasHidden").val(resultado.valorDividas);
			
			$('#valorDividas').priceFormat({
				allowNegative: true,
			    centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#multaDividas').priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#jurosDividas').priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#descontoDividas').priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#valorPagoDividas').priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#valorSaldoDividas').priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#valorSaldoDividasHidden').priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#valorDividasHidden').priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$("#valorSaldoDividas").html($("#valorSaldoDividasHidden").val());
			$("#valorDividas").html($("#valorDividasHidden").val());
			
			popup_baixa_dividas();
		}
        
        
        
        
        //CALCULA TOTAL CONFORME AÇÃO DO USUARIO NA TELA DE BAIXA POR COTA
		function calculaTotalManualDividas() {
        	
			var valorDividas = removeMascaraPriceFormat($("#valorDividasHidden").val());
			
			var desconto = removeMascaraPriceFormat($("#descontoDividas").val());
			var juros = removeMascaraPriceFormat($("#jurosDividas").val());
			var multa = removeMascaraPriceFormat($("#multaDividas").val());

			var valorPago = intValue(valorDividas) + intValue(juros) + intValue(multa) - intValue(desconto);
				
			$("#valorPagoDividas").val(valorPago);
			
			$("#valorPagoDividas").priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
		}

        
        
        
		//CALCULA SALDO CONFORME AÇÃO DO USUARIO NA TELA DE BAIXA POR COTA
		function calculaSaldoDividas() {
        	
			var valorDividas = removeMascaraPriceFormat($("#valorDividasHidden").val());		
			var valorPago = removeMascaraPriceFormat($("#valorPagoDividas").val());
			
			var desconto = removeMascaraPriceFormat($("#descontoDividas").val());
			var juros = removeMascaraPriceFormat($("#jurosDividas").val());
			var multa = removeMascaraPriceFormat($("#multaDividas").val());

			var valorSaldo = intValue(valorDividas) + intValue(juros) + intValue(multa) - ( intValue(valorPago) + intValue(desconto) );
			
			if (valorSaldo < 0){
				valorSaldo = 0;
			}
			
            $("#valorSaldoDividasHidden").val(valorSaldo);
			
			$("#valorSaldoDividasHidden").priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			$("#valorSaldoDividas").html($("#valorSaldoDividasHidden").val());
		}
		
        
        
        
        //EFETUA BAIXA MANUAL DE DIVIDAS SELECIONADAS E CALCULADAS
        function baixaManualDividas(manterPendente) {

        	var valorDividas = $("#valorDividas").html();
        	var multaDividas = $("#multaDividas").val();
        	var jurosDividas = $("#jurosDividas").val();
        	var descontoDividas = $("#descontoDividas").val();
        	var valorPagoDividas = $("#valorPagoDividas").val();
        	var valorSaldoDividas = $("#valorSaldoDividas").html();
        	var formaRecebimentoDividas = $("#formaRecebimentoDividas").val();
        	var observacoesDividas = $("#observacoesDividas").val();

			$.postJSON("<c:url value='/financeiro/baixaManualDividas'/>",
					   "manterPendente="+manterPendente+
					   "&valorDividas="+valorDividas+
					   "&valorMulta="+ multaDividas +
					   "&valorJuros="+ jurosDividas+
					   "&valorDesconto="+ descontoDividas +
					   "&valorPagamento="+ valorPagoDividas+
					   "&valorSaldo="+ valorSaldoDividas+
					   "&tipoPagamento="+ formaRecebimentoDividas+
					   "&observacoes="+ observacoesDividas +
					   "&"+obterCobrancasDividasMarcadas(),
					   function(mensagens) {
						   
				           $("#dialog-baixa-dividas").dialog("close");
						   
						   if (mensagens){
							   var tipoMensagem = mensagens.tipoMensagem;
							   var listaMensagens = mensagens.listaMensagens;
							   if (tipoMensagem && listaMensagens) {
							       exibirMensagem(tipoMensagem, listaMensagens);
						       }
			        	   }
				           
						   buscaManual();
		               },
		               null,
		               true);
		}
		
        
        
        
        //OBTEM VALIDAÇÃO DE PERMISSÃO DE NEGOCIAÇÃO
        function obterNegociacao() {
			$.postJSON("<c:url value='/financeiro/obterNegociacao' />",
					   obterCobrancasDividasMarcadas());
		}
        
        
        
        
        
		
		
		

		
		//-----------------------------------------------------
		

		//BAIXA AUTOMÁTICA-------------------------------------

		$(function() {	
			var options = {
				success: tratarRespostaBaixaAutomatica,
		    };
			
			$('#formBaixaAutomatica').ajaxForm(options);
			
			$("#valorFinanceiro").priceFormat({
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$("#radioBaixaManual").focus();
		});
		
		function mostrarBaixaAuto() {
			
			limparCamposBaixaAutomatica();
			
			$('#tableBaixaManual').hide();
			$('#extratoBaixaManual').hide();
			$('#tableBaixaAuto').show();
		}
		
		function integrar() {
			
			$('#formBaixaAutomatica').submit();
		}
		
		function tratarRespostaBaixaAutomatica(data) {
			
			data = replaceAll(data, "<pre>", "");
			data = replaceAll(data, "</pre>", "");
			
			data = replaceAll(data, "<PRE>", "");
			data = replaceAll(data, "</PRE>", "");
			
			var responseJson = jQuery.parseJSON(data);
			
			if (responseJson.mensagens) {

				exibirMensagem(
					responseJson.mensagens.tipoMensagem, 
					responseJson.mensagens.listaMensagens
				);
				
				$('#resultadoIntegracao').hide();
			}
			
			if (responseJson.result) {
				$("#nomeArquivo").html(responseJson.result.nomeArquivo);
				$("#dataCompetencia").html(responseJson.result.dataCompetencia);
				$("#somaPagamentos").html(responseJson.result.somaPagamentos);
				
				$("#quantidadeLidos").html(responseJson.result.quantidadeLidos);
				$("#quantidadeBaixados").html(responseJson.result.quantidadeBaixados);
				$("#quantidadeRejeitados").html(responseJson.result.quantidadeRejeitados);
				$("#quantidadeBaixadosComDivergencia").html(responseJson.result.quantidadeBaixadosComDivergencia);
				
				limparCamposBaixaAutomatica();
				
				$('#resultadoIntegracao').show();
			}
		}
		
		function limparCamposBaixaAutomatica() {
			
			$("#uploadedFile").replaceWith(
				"<input name='uploadedFile' type='file' id='uploadedFile' size='25' />");
			
			$("#valorFinanceiro").val("");
		}
		
		//-----------------------------------------------------
		
		function finalizarPostergacao() {
			
			$.postJSON("<c:url value='/financeiro/finalizarPostergacao' />",
						"dataPostergacao=" + $("#dtPostergada").val() +
						"&isIsento=" + buscarValueCheckBox('checkIsIsento') +
						"&" + obterCobrancasDividasMarcadas(),
						function (result) {

							$("#dialog-postergar").dialog("close");
							$(".liberaDividaGrid").flexReload();
							
							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							
							if (tipoMensagem && listaMensagens) {
								
								exibirMensagem(tipoMensagem, listaMensagens);
							} 
							
							limparModalPostergacao();
						},
						null,
						true
					);	
		}
		
		//OBTEM VALIDAÇÃO DE PERMISSÃO DE POSTERGAÇÃO
        function obterPostergacao() {

			$.postJSON("<c:url value='/financeiro/obterPostergacao' />",
						"dataPostergacao=" + $("#dtPostergada").val() +
						"&" + obterCobrancasDividasMarcadas(),
						function (result) {
							if (result) {

								var tipoMensagem = result.tipoMensagem;
								var listaMensagens = result.listaMensagens;
							
								if (tipoMensagem && listaMensagens) {
									
									exibirMensagem(tipoMensagem, listaMensagens);
								} 

								if (!tipoMensagem) {
									postergarDivida();
									$("#ecargosPostergacao").val(result)
								}	
							}							
						},
						null,
						true
					);
		}
				
		$(function() {
			$( "#dtPostergada" ).datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true
			});

			$("#ecargosPostergacao").numeric();
		});
		
		function postergarDivida() {
		
			$("#dialog-postergar").dialog({
				resizable: false,
				height:220,
				width:300,
				modal: true,
				buttons:[ 
				          {
					           id:"bt_confirmar",
					           text:"Confirmar", 
					           click: function() {
					        	   finalizarPostergacao();
					           }
				           },
				           {
					           id:"bt_cancelar",
					           text:"Cancelar", 
					           click: function() {
					        	   $( this ).dialog( "close" );
								   limparModalPostergacao();
					           }
				           }
		        ],
				beforeClose: function() {
					limparModalPostergacao();
					clearMessageDialogTimeout('dialogMensagemNovo');
				}
			});
		}

		function limparModalPostergacao() {

			$("#dtPostergada").val("");
			$("#checkIsIsento").attr('checked', false);
			$("#ecargosPostergacao").val("");
		}

		function buscarValueCheckBox(checkName) {
			return $("#"+checkName).is(":checked");
		}
		;

	</script>
	
	<style>

		#resultadoIntegracao{display:none;}
        #tableBaixaManual, #tableBaixaAuto, #extratoBaixaManual, #porNossoNumero, #porCota, #dialog-divida{display:none;}
        #dialog-baixa-dividas,#dialog-detalhes-divida{display:none;}
        #dialog-confirma-baixa-numero,#dialog-confirma-baixa,#dialog-confirma-pendente,#dialog-postergar{display:none;}
        
	</style>
	
    </head>

<body>

	<fieldset class="classFieldset">
		
		<legend> Baixa Financeira</legend>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="115">Tipo de Baixa:</td>
              <td colspan="3"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="7%"><input type="radio" name="baixaFinanceira" id="radioBaixaManual" onclick="mostrarBaixaManual();"/></td>
                  <td width="22%">Manual</td>
                  <td width="8%"><input type="radio" name="baixaFinanceira" id="radioBaixaAutomatica" onclick="mostrarBaixaAuto();" /></td>
                  <td width="63%">Automatica</td>
                </tr>
              </table></td>
              <td width="112">&nbsp;</td>
              <td width="114">&nbsp;</td>
              <td width="55">&nbsp;</td>
              <td width="102">&nbsp;</td>
              <td width="104">&nbsp;</td>
            </tr>
        </table>
		
		
		<!-- BAIXA AUTOMÁTICA -->
		
		<form action="<c:url value='/financeiro/realizarBaixaAutomatica' />" id="formBaixaAutomatica"
			  method="post" enctype="multipart/form-data" >
		
			<input type="hidden" name="formUploadAjax" value="true" />
		
			<table width="950" border="0" cellpadding="2" cellspacing="1"
				   class="filtro" id="tableBaixaAuto">
				
					<tr>
						<td width="65">Arquivo:</td>
						<td colspan="3">
							<input name="uploadedFile" type="file" id="uploadedFile" size="25" />
						</td>
						
						<td width="133">Valor Financeiro R$:</td>
						<td width="288">
							<input type="text" name="valorFinanceiro"
								   id="valorFinanceiro" style="width: 90px; text-align: right;" />
						</td>
						
						<td width="111">
							<span class="bt_integrar" title="Integrar">
								<a href="javascript:;" onclick="integrar();">Integrar</a>
							</span>
						</td>
					</tr>			
			</table>
		</form>
		<!--  -->
		
		
		<!-- BAIXA MANUAL -->
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro" id="tableBaixaManual">
            <tr>
				<td width="29">Cota:</td>
                
                <td>
              
                <input name="filtroNumCota" 
             	    id="filtroNumCota" 
             		type="text"
             		maxlength="11"
             		style="width:60px; 
             		float:left; margin-right:5px;"
             		onchange="pesquisaCotaBaixaFinanceira.pesquisarPorNumeroCota('#filtroNumCota', '#descricaoCota');" />
				</td>
				
				<td width="39">Nome:</td>
             	
             	<td width="210">
		        	<input name="descricaoCota" 
		      		 	   id="descricaoCota" 
		      		 	   type="text"
		      		 	   class="nome_jornaleiro" 
		      		 	   maxlength="255"
		      		 	   style="width:130px;"
		      		 	   onkeyup="pesquisaCotaBaixaFinanceira.autoCompletarPorNome('#descricaoCota');" 
		      		 	   onblur="pesquisaCotaBaixaFinanceira.pesquisarPorNomeCota('#filtroNumCota', '#descricaoCota');" />
		        </td>
			  
				<td width="97">Nosso Número:</td>
				<td width="333"><input maxlength="100" type="text" name="filtroNossoNumero" id="filtroNossoNumero" style="width: 300px;" /></td>
				<td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="buscaManual();">Pesquisar</a></span></td>
			</tr>
        </table>
        
        <!--  -->
        
        
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
	
    
    <!-- BAIXA AUTOMÁTICA -->
    
	<fieldset class="classFieldset" id="resultadoIntegracao">
		<legend> Baixa Financeira Integrada</legend>
		<br />

		<table border="0" align="center" cellpadding="2" cellspacing="2">
			<tr>
				<td valign="top">
					<table width="269" border="0" align="center" cellpadding="2"
						cellspacing="1" style="display: inline; margin-right: 15px;">
						<tr>
							<td colspan="2" align="center" class="header_table">Dados do
								Arquivo</td>
						</tr>
						<tr>
							<td width="121" align="left" class="linha_borda"><strong>Nome
									do Arquivo:</strong></td>
							<td id="nomeArquivo" width="137" align="right" class="linha_borda"></td>
						</tr>
						<tr>
							<td align="left" class="linha_borda"><strong>Data
									Competência:</strong></td>
							<td id="dataCompetencia" align="right" class="linha_borda"></td>
						</tr>
						<tr>
							<td align="left" class="linha_borda"><strong>Valor
									R$:</strong></td>
							<td id="somaPagamentos" align="right" class="linha_borda"></td>
						</tr>
						<tr>
							<td align="left" class="linha_borda">&nbsp;</td>
							<td align="right" class="linha_borda">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="2" align="left"
								style="line-height: 28px; border: 1px solid #0C0;"><img
								src="${pageContext.request.contextPath}/images/bt_check.gif" width="22" height="22"
								alt="Arquivo Integrado com Sucesso" align="left" /> <span><strong>Arquivo
										Integrado com Sucesso!</strong></span></td>
						</tr>
					</table>
				</td>
				<td valign="top"><table width="275" border="0" align="center"
						cellpadding="2" cellspacing="1" style="display: inline;">
						<tr>
							<td colspan="2" align="center" class="header_table"
								class="linha_borda">Baixa Automática</td>
						</tr>
						<tr>
							<td width="162" align="left" class="linha_borda"><strong>Registros
									Lidos:</strong></td>
							<td id="quantidadeLidos" width="102" align="right" class="linha_borda"></td>
						</tr>
						<tr>
							<td align="left" class="linha_borda"><strong>Registros
									Baixados:</strong></td>
							<td id="quantidadeBaixados" align="right" class="linha_borda"></td>
						</tr>
						<tr>
							<td align="left" class="linha_borda"><strong>Registros
									Rejeitados:</strong></td>
							<td id="quantidadeRejeitados" align="right" class="linha_borda"></td>
						</tr>
						<tr>
							<td align="left" class="linha_borda"><strong>Baixados
									com Divergência:</strong></td>
							<td id="quantidadeBaixadosComDivergencia" align="right" class="linha_borda"></td>
						</tr>
					</table></td>
			</tr>
		</table>
		
		<br /> <br />
		
		<div class="linha_separa_fields">&nbsp;</div>
		
		<br clear="all" />

	</fieldset>
		
	<div class="linha_separa_fields">&nbsp;</div>
	
	<!--  -->
	
	
	<!-- BAIXA MANUAL -->
	
	<form name="formularioListaDividas" id="formularioListaDividas">
	
	   
		<input type="hidden" id="valorTotalHidden" />
		<input type="hidden" id="valorBoletoHidden" />
		
		<input type="hidden" id="valorSaldoDividasHidden" />
		<input type="hidden" id="valorDividasHidden" />
		
		<input type="hidden" id="totalDividasHidden" />
	    <input type="hidden" id="totalDividasSelecionadasHidden" />
	    
	    <input type="hidden" id="saldoDividaHidden" />
	 
	
		<fieldset class="classFieldset" id="extratoBaixaManual" >
	      	<legend>Baixa Manual</legend>
	        <br />
	        
	        <div id="dialog-confirma-baixa-numero" title="Baixa Bancária">
			    <p>Deseja confirmar Baixa Manual deste Boleto ?</p>
		    </div>
		
	        <div  id="porNossoNumero">
		      	<table width="342" border="0" align="center" cellpadding="2" cellspacing="1" style="text-align:left;">
		      	  <tr>
		      	    <td colspan="2" class="header_table" align="center">Dados Boleto</td>
		   	      </tr>
		      	  <tr>
		      	    <td class="linha_borda"><strong>Núm.Boleto:</strong></td>
		      	    <td class="linha_borda" id="nossoNumero" />
		   	      </tr>
		      	  <tr>
		      	    <td class="linha_borda"><strong>Cota:</strong></td>
		      	    <td class="linha_borda" id="cota" />
		   	      </tr>
		      	  <tr>
		      	    <td width="81" class="linha_borda"><strong>Banco:</strong></td>
		      	    <td width="250" class="linha_borda" id="banco" />
		   	      </tr>
		      	  <tr>
		      	    <td class="linha_borda"><strong>Emissão:</strong></td>
		      	    <td class="linha_borda" id="dataEmissao" />
		   	      </tr>
		      	  <tr>
		      	    <td class="linha_borda"><strong>Vencimento:</strong></td>
		      	    <td class="linha_borda" id="dataVencimento" />
		   	      </tr>
		      	  <tr>
		      	    <td class="linha_borda"><strong>Valor R$:</strong></td>
		      	    <td class="linha_borda" id="valorBoleto" />
		   	      </tr>
		   	      
		   	      <tr>
		      	    <td class="linha_borda"><strong>Desconto R$:</strong></td>
		      	    <td class="linha_borda">  <input maxlength="22" onblur="calculaTotalManual();" id="desconto" type="text" style="width:120px; text-align:right;"/>  </td>
		   	      </tr>
		   	      <tr>
		      	    <td class="linha_borda"><strong>Juros R$:</strong></td>
		      	    <td class="linha_borda">  <input maxlength="22" onblur="calculaTotalManual();" id="juros" type="text" style="width:120px; text-align:right;"/>  </td>
		   	      </tr>
		   	      <tr>
		      	    <td class="linha_borda"><strong>Multa R$:</strong></td>
		      	    <td class="linha_borda">  <input maxlength="22" onblur="calculaTotalManual();" id="multa" type="text" style="width:120px; text-align:right;"/>  </td>
		   	      </tr>
		   	      
		   	      <tr>
		      	    <td class="linha_borda">&nbsp;</td>
		      	    <td class="linha_borda">&nbsp;</td>
  				  </tr>
      	          <tr>
      	            <td class="linha_borda"><strong>Valor Total R$:</strong></td>
      	            <td class="linha_borda" id="valorTotal" />
                  <tr>
      	          <tr>
      	            <td class="linha_borda">&nbsp;</td>
      	            <td class="linha_borda">&nbsp;</td>
                  </tr>

		      	  <tr>
		      	    <td class="linha_borda">&nbsp;</td>
		      	    <td class="linha_borda"><span class="bt_confirmar_novo" title="Pagar"><a onclick="mostrarPopupPagamento();" href="javascript:;"><img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Pagar</a></span></td>
		   	      </tr>
		   	      
		   	    </table>
			</div>
			
	      	<div  id="porCota">
	      	
		       <table class="liberaDividaGrid" id="tabelaDividas"></table>
		    
		       <table width="100%" border="0" cellspacing="2" cellpadding="2">
		            <tr>
		            
		                <td width="20%">
		                    <span class="bt_novos" title="Gerar Arquivo">
								<a href="${pageContext.request.contextPath}/financeiro/exportar?fileType=XLS">
									<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
									Arquivo
								</a>
							</span>
							
							<span class="bt_novos" title="Imprimir">
								<a href="${pageContext.request.contextPath}/financeiro/exportar?fileType=PDF">
									<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
									Imprimir
								</a>
							</span>
		                </td>

		                <td width="30%">   
		                    <span class="bt_confirmar_novo" title="Pagar Dívida"><a onclick="obterPagamentoDividas();" href="javascript:;"><img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">À Vista</a></span>
		                    <span class="bt_confirmar_novo" title="Negociar Dívida"><a onclick="obterNegociacao();" href="javascript:;"><img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Negociar</a></span>
		                    <span class="bt_confirmar_novo" title="Postergar Dívida"><a onclick="obterPostergacao();" href="javascript:;"><img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Postergar</a></span>
		                </td>
		                
		                <td width="14%">
		                    <strong>Total Selecionado R$:</strong>
		                </td>
		                <td width="7%" id="totalDividasSelecionadas"></td>
		                
		                <td width="7%">
		                    <strong>Total R$:</strong>
		                </td>
		                <td width="7%" id="totalDividas"></td>
		                
		                <td width="20%">
		                
		                    <span class="checar">
		                        
		                        <label for="textoSelTodos" id="textoSelTodos">
		                            Marcar Todos
		                        </label>
		                        
		                        <input title="Selecionar todas as Dívidas" type="checkbox" id="selTodos" name="selTodos" onclick="selecionarTodos(this.checked);" style="float:left;"/>
		                    </span>

		                </td>
		            </tr>
		        </table>
	        </div>
	        
	        <div id="dialog-confirma-baixa" title="Baixa Bancária">
			    <p>Deseja confirmar Baixa Manual ?</p>
		    </div>
		
			<div id="dialog-baixa-dividas" title="Baixa Bancária">
			
			    
			    <jsp:include page="../messagesDialog.jsp"></jsp:include>
			    
			
				<table width="433" border="0" cellpadding="2" cellspacing="2">
				  <tr>
				    <td width="153"><strong>Valor Dívida R$:</strong>
				    
				    </td>
				    <td width="266" id="valorDividas" ></td>
				  </tr>
				  <tr>
				    <td><strong>Multa R$:</strong></td>
				    <td><input  maxlength="16" id="multaDividas" name="multaDividas" onblur="calculaTotalManualDividas();calculaSaldoDividas();" type="text" style="width:80px; text-align:right;" /></td>
				  </tr>
				  <tr>
				    <td><strong>Juros R$:</strong></td>
				    <td><input maxlength="16" id="jurosDividas" name="jurosDividas" onblur="calculaTotalManualDividas();calculaSaldoDividas();" type="text" style="width:80px; text-align:right;" /></td>
				  </tr>
				  <tr>
				    <td><strong>Desconto R$:</strong></td>
				    <td><input maxlength="16" id="descontoDividas" name="descontoDividas" onblur="calculaTotalManualDividas();calculaSaldoDividas();" type="text" style="width:80px; text-align:right;" /></td>
				  </tr>
				  <tr>
				    <td><strong>Valor pago R$:</strong></td>
				    <td><input maxlength="16" id="valorPagoDividas" name="valorPagoDividas" onblur="calculaSaldoDividas();" type="text" style="width:80px; text-align:right;" /></td>
				  </tr>
				  <tr>
				    <td>&nbsp;</td>
				    <td style="border-bottom:1px solid #000;">&nbsp;</td>
				  </tr>
				  <tr>
				    <td><strong>Saldo R$:</strong></td>
				    <td id="valorSaldoDividas" ></td>
				  </tr>
				  
				  
				  <tr>
				    <td><strong>Forma Recebimento:</strong></td>
				    <td>
				        <select name="formaRecebimentoDividas" id="formaRecebimentoDividas" style="width:150px;">
	                        <option value="">Selecione</option>
	                        <c:forEach varStatus="counter" var="itemTipoCobranca" items="${listaTiposCobranca}">
			                    <option value="${itemTipoCobranca.key}">${itemTipoCobranca.value}</option>
			                </c:forEach>
	                    </select> 
				    </td>
				  </tr>
				  
				  
				  <tr>
				    <td>&nbsp;</td>
				    <td align="right"></td>
				  </tr>
				  <tr>
				    <td><strong>Observação:</strong></td>
				    <td><textarea maxlength="150" name="observacoesDividas" id="observacoesDividas" cols="45" rows="3" style="width:260px;"></textarea></td>
				  </tr>
				</table>
			</div>



			<div id="dialog-detalhes-divida" title="Detalhes da Dívida">
				<table class="dadosDividaGrid"></table>
                <table>
	                <tr>
	                    <td><strong>Saldo R$: </strong></td>
	                    <td id="saldoDivida"></td>
	                </tr>        
                </table>
			</div> 


            <div id="dialog-confirma-pendente" title="Baixa Manual de Dívidas">
			    <p>Deseja manter as dívidas com o status [Pendente] até a confirmação do pagamento ?</p>
		    </div> 

			<div id="dialog-postergar" title="Postergar D&iacute;vida">
				<fieldset style="width:255px!important;">
			    	<legend>Postergar D&iacute;vida</legend>
					<table width="230" border="0" cellspacing="2" cellpadding="0">
			          <tr>
			            <td width="121">Nova Data:</td>
			            <td width="103"><input name="dtPostergada" type="text" id="dtPostergada" style="width:80px;" onchange="obterPostergacao();" /></td>
			          </tr>
			          <tr>
			            <td>Encargos R$:</td>
			            <td><input name="ecargosPostergacao" id="ecargosPostergacao" type="text" style="width:80px; text-align:right;" disabled="disabled" /></td>
			          </tr>
			          <tr>
			            <td>Isentos Encargos</td>
			            <td><input type="checkbox" name="checkIsIsento" id="checkIsIsento" /></td>
			          </tr>
			        </table>
			    </fieldset>
			</div>

			
	    </fieldset>
    
    </form>
    
    <!--  -->
    
	
</body>