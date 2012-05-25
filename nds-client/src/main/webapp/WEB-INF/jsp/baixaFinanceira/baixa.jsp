<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>

	<script type="text/javascript">
	
	
		$(function() {
			
			$("#filtroNumCota").numeric();
			$("#descricaoCota").autocomplete({source: ""});
			
		}); 
	
	
	    //BAIXA MANUAL--------------------------------------
	    
	    function popup_baixa_dividas() {
			$( "#dialog-baixa-dividas" ).dialog({
				resizable: false,
				height:430,
				width:480,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						popup_confirma_baixa_dividas();
						
						$( this ).dialog( "close" );
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		};

		function popup_detalhes(codigo) {
			
			obterDetalhesDivida(codigo);
			
			$( "#dialog-detalhes-divida" ).dialog({
				resizable: false,
				height:350,
				width:650,
				modal: true,
				buttons: {
					"Fechar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		};

		function popup_confirma_baixa_dividas() {
			$( "#dialog-confirma-baixa" ).dialog({
				resizable: false,
				height:130,
				width:470,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						baixaManualDividas();
						
						$( this ).dialog( "close" );
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		};
		
		function mostrarPopupPagamento() {
			$( "#dialog-confirma-baixa-numero" ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						
						baixaPorNossoNumero();
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		};
	
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
				cont = 1;
				
    	        $("#totalDividasSelecionadas").html($("#totalDividas").html());
			    $("#totalDividasSelecionadasHidden").val($("#totalDividasHidden").val());
            }
			
			else{
				
				var elem = document.getElementById("textoSelTodos");
				elem.innerHTML = "Marcar todos";
				cont = 0;
				
				$("#totalDividasSelecionadas").html("0,00");
				$("#totalDividasSelecionadasHidden").val("0,00");
			}
		}

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

		function getDataFromResultDividas(resultado) {
			
			//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
			if (resultado.mensagens) {
				exibirMensagemDialog(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				$(".grids").hide();
				return resultado;
			}	
			
			var totalDividas=0;
			$.each(resultado.rows, function(index, row) {
				
				valorItem = removeMascaraPriceFormat(row.cell.valor);
				totalDividas = intValue(totalDividas) + intValue(valorItem);
	
				var detalhes = '<a href="javascript:;" onclick="popup_detalhes(' + row.cell.codigo + ');" style="cursor:pointer">' +
						 	   '<img title="Aprovar" src="${pageContext.request.contextPath}/images/ico_detalhes.png" hspace="5" border="0px" />' +
							   '</a>';	
							   
				var checkBox = '<input type="checkbox" name="checkboxGrid" id="checkbox_'+ row.cell.codigo +'" onchange="calculaSelecionados(this.checked,'+ valorItem +');" />';	
				
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

			$("#totalDividasSelecionadas").html("0,00");
			$("#totalDividasSelecionadasHidden").val("0,00");

			$(".grids").show();
			
			return resultado;
		}
		
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
		
        function obterDetalhesDivida(idDivida){
			$(".dadosDividaGrid").flexOptions({
				url: "<c:url value='/financeiro/obterDetalhesDivida' />",
				params: [
				         {name:'idDivida', value: idDivida}
				        ] ,
				        newp: 1
			});
			$(".dadosDividaGrid").flexReload();
			$(".grids").show();
		}
		
		function getDataFromResultDivida(resultado) {
			$(".grids").show();
			return resultado;
		}
		
		function buscaManual() {
			
			var nossoNumero = $("#filtroNossoNumero").val();
			
			if (nossoNumero==''){
				
				/*BAIXA MANUAL DE DÍVIDAS*/
				$(".liberaDividaGrid").flexOptions({
					url: "<c:url value='/financeiro/buscaDividas' />",
					params: [
					         {name:'numCota', value:$("#filtroNumCota").val()}
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
		
        function obterPagamentoDividas() {
			$.postJSON("<c:url value='/financeiro/obterPagamentoDividas' />",
					   obterCobrancasDividasMarcadas(),
					   sucessCallbackPagamentoDivida,
					   null);
		}
		
		function calculaTotalManualDividas() {
        	
			var valorDividas = removeMascaraPriceFormat($("#valorDividasHidden").val());
			var desconto = removeMascaraPriceFormat($("#descontoDividas").val());
			var juros = removeMascaraPriceFormat($("#jurosDividas").val());
			var multa = removeMascaraPriceFormat($("#multaDividas").val());
			
			var total = intValue(valorDividas) + intValue(juros) + intValue(multa) - intValue(desconto);
            
			$("#valorPagoDividas").val(total);
			
			$("#valorPagoDividas").priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
		}
        
		function sucessCallbackPagamentoDivida(resultado) {
			
		    $("#multaDividas").val(resultado.valorMulta);
			$("#jurosDividas").val(resultado.valorJuros);
			$("#descontoDividas").val(resultado.valorDesconto);
			$("#valorPagoDividas").val(resultado.valorPagamento);
			$("#formaRecebimentoDividas").val(resultado.tipoPagamento);
			$("#observacoesDividas").val(resultado.observacoes);
			
			$("#saldoCotaDividasHidden").val(resultado.valorSaldo);
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
			
			$('#saldoCotaDividas').priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#saldoCotaDividasHidden').priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#valorDividasHidden').priceFormat({
				allowNegative: true,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$("#saldoCotaDividas").html($("#saldoCotaDividasHidden").val());
			$("#valorDividas").html($("#valorDividasHidden").val());
			
			popup_baixa_dividas();
		}
		
        function baixaManualDividas() {

        	var valorDividas = $("#valorDividas").html();
        	var multaDividas = $("#multaDividas").val();
        	var jurosDividas = $("#jurosDividas").val();
        	var descontoDividas = $("#descontoDividas").val();
        	var valorPagoDividas = $("#valorPagoDividas").val();
        	var saldoCotaDividas = $("#saldoCotaDividas").html();
        	var formaRecebimentoDividas = $("#formaRecebimentoDividas").val();
        	var observacoesDividas = $("#observacoesDividas").val();

			$.postJSON("<c:url value='/financeiro/baixaManualDividas'/>",
					   "valorDividas="+valorDividas+
					   "&valorMulta="+ multaDividas +
					   "&valorJuros="+ jurosDividas+
					   "&valorDesconto="+ descontoDividas +
					   "&valorPagamento="+ valorPagoDividas+
					   "&valorSaldo="+ saldoCotaDividas+
					   "&tipoPagamento="+ formaRecebimentoDividas+
					   "&observacoes="+ observacoesDividas +
					   "&"+obterCobrancasDividasMarcadas(),
					   null,
					   null,
					   true);
			
		}
		
        function obterNegociacao(dataVencimento) {
			var data = [{name: 'dataVencimento', value: dataVencimento}];
			$.postJSON("<c:url value='/financeiro/obterNegociacao' />",
					   data,
					   popup_negociacao());
		}
		
        function popup_negociacao() {
			$( "#dialog-negociar-dividas" ).dialog({
				resizable: false,
				height:350,
				width:650,
				modal: true,
				buttons: {
					"Fechar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		};

		
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
		

	</script>
	
	<style>

		#resultadoIntegracao{display:none;}
        #tableBaixaManual, #tableBaixaAuto, #extratoBaixaManual, #porNossoNumero, #porCota, #dialog-divida{display:none;}
        #dialog-baixa-dividas,#dialog-detalhes-divida,#dialog-baixa-dividas{display:none;}
        #dialog-confirma-baixa-numero,#dialog-confirma-baixa,#dialog-negociar-dividas{display:none;}
        
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
             		onchange="cota.pesquisarPorNumeroCota('#filtroNumCota', '#descricaoCota');" />
				</td>
				
				<td width="39">Nome:</td>
             	
             	<td width="210">
		        	<input name="descricaoCota" 
		      		 	   id="descricaoCota" 
		      		 	   type="text"
		      		 	   class="nome_jornaleiro" 
		      		 	   maxlength="255"
		      		 	   style="width:130px;"
		      		 	   onkeyup="cota.autoCompletarPorNome('#descricaoCota');" 
		      		 	   onblur="cota.pesquisarPorNomeCota('#filtroNumCota', '#descricaoCota');" />
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
		
		<input type="hidden" id="saldoCotaDividasHidden" />
		<input type="hidden" id="valorDividasHidden" />
		
		<input type="hidden" id="totalDividasHidden" />
	    <input type="hidden" id="totalDividasSelecionadasHidden" />
	 
	
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
		            
		                <td width="20%"><span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
		                <span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
		                </td>
		                <td width="20%">   
		                    <span class="bt_confirmar_novo" title="Pagar Boleto"><a onclick="obterPagamentoDividas();" href="javascript:;"><img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Pagar</a></span>
		                    <span class="bt_confirmar_novo" title="Negociar Dívida"><a onclick="obterNegociacao('01/01/2012');" href="javascript:;"><img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Negociar</a></span>
		                </td>
		                
		                <td width="15%">
		                    <strong>Total Selecionado R$:</strong>
		                </td>
		                <td width="7%" id="totalDividasSelecionadas"></td>
		                
		                <td width="7%">
		                    <strong>Total R$:</strong>
		                </td>
		                <td width="7%" id="totalDividas"></td>
		                
		                <td width="25%">
		                
		                    <span class="checar">
		                        
		                        <label for="textoSelTodos" id="textoSelTodos">
		                            Selecionar Todos
		                        </label>
		                        
		                        <input type="checkbox" id="selTodos" name="selTodos" onclick="selecionarTodos(this.checked);" style="float:left;"/>
		                    </span>

		                </td>
		            </tr>
		        </table>
	        </div>
	        
	        <div id="dialog-confirma-baixa" title="Baixa Bancária">
			    <p>Deseja confirmar Baixa Manual ?</p>
		    </div>
		
			<div id="dialog-baixa-dividas" title="Baixa Bancária">
				<table width="433" border="0" cellpadding="2" cellspacing="2">
				  <tr>
				    <td width="153"><strong>Valor Dívida R$:</strong>
				    
				    </td>
				    <td width="266" id="valorDividas" ></td>
				  </tr>
				  <tr>
				    <td><strong>Multa R$:</strong></td>
				    <td><input  maxlength="16" id="multaDividas" name="multaDividas" onblur="calculaTotalManualDividas();" type="text" style="width:80px; text-align:right;" /></td>
				  </tr>
				  <tr>
				    <td><strong>Juros R$:</strong></td>
				    <td><input maxlength="16" id="jurosDividas" name="jurosDividas" onblur="calculaTotalManualDividas();" type="text" style="width:80px; text-align:right;" /></td>
				  </tr>
				  <tr>
				    <td><strong>Desconto R$:</strong></td>
				    <td><input maxlength="16" id="descontoDividas" name="descontoDividas" onblur="calculaTotalManualDividas();" type="text" style="width:80px; text-align:right;" /></td>
				  </tr>
				  <tr>
				    <td><strong>Valor pago R$:</strong></td>
				    <td><input maxlength="16" id="valorPagoDividas" name="valorPagoDividas" type="text" style="width:80px; text-align:right;" /></td>
				  </tr>
				  <tr>
				    <td>&nbsp;</td>
				    <td style="border-bottom:1px solid #000;">&nbsp;</td>
				  </tr>
				  <tr>
				    <td><strong>Saldo R$:</strong></td>
				    <td id="saldoCotaDividas" ></td>
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
	                    <td id="saldoDividas"></td>
	                </tr>        
                </table>
			</div> 


            <div id="dialog-negociar-dividas" title="Negociar Dívida">
			    <p>EMS 0038 - negociar dividas</p>
		    </div>
			
	    </fieldset>
    
    </form>
    
    <!--  -->
    
	
</body>