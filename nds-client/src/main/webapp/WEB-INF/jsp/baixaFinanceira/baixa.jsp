<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
	
	<script type="text/javascript">
	
		$(function() {
			$(".liberaDividaGrid").flexigrid({
				preProcess: getDataFromResult,
				dataType : 'json',
				colModel : [ {
					display : 'C�digo',
					name : 'codigo',
					width : 80,
					sortable : true,
					align : 'left'
				},{
					display : 'Nome',
					name : 'nome',
					width : 395,
					sortable : true,
					align : 'left'
				}, {
					display : 'Data Emiss�o',
					name : 'dtEmissao',
					width : 90,
					sortable : true,
					align : 'center'
				}, {
					display : 'Data Vencimento',
					name : 'dtVencimento',
					width : 90,
					sortable : true,
					align : 'center'
				}, {
					display : 'Valor Divida R$',
					name : 'vlDivida',
					width : 100,
					sortable : true,
					align : 'right'
				}, {
					display : 'Detalhes',
					name : 'detalhe',
					width : 60,
					sortable : true,
					align : 'center',
				}, {
					display : '',
					name : 'sel',
					width : 40,
					sortable : true,
					align : 'center',
				}],
				sortname : "Nome",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180
			});
		});
		
		$(function() {
			$(".dadosDividaGrid").flexigrid({
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
					display : 'Observa��o',
					name : 'observacao',
					width : 320,
					sortable : true,
					align : 'left'
				}],
				width : 620,
				height : 160
			});
	    }); 
	
		$(function() {	
			var options = {
				success: tratarRespostaBaixaAutomatica,
		    };
			
			$('#formBaixaAutomatica').ajaxForm(options);
			
			$("#valorFinanceiro").numeric();
			
		});
		
		function mostrarBaixaAuto() {
			
			limparCamposBaixaAutomatica();
			
			$('#tableBaixaAuto').show();
			$('#tableBaixaManual').hide();
			$('#extratoBaixaManual').hide();
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
	
		function popup_manual() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
			$( "#dialog-manual" ).dialog({
				resizable: false,
				height:'auto',
				width:470,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		};
		
	    function popup() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
			$( "#dialog-novo" ).dialog({
				resizable: false,
				height:280,
				width:770,
				modal: true,
				buttons: {
					"Confirmar": function() {
						popup_excluir();
						$( this ).dialog( "close" );
						
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		};
		
		function popup_alterar() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
			$( "#dialog-novo" ).dialog({
				resizable: false,
				height:350,
				width:650,
				modal: true,
				buttons: {
					"Fechar": function() {
						$( this ).dialog( "close" );
						
						
					},
					
				}
			});	
			      
		};
		
		function popup_excluir() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
			$( "#dialog-excluir" ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").show("highlight", {}, 1000, callback);
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		};
		
		function popup_divida() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
			$( "#dialog-divida" ).dialog({
				resizable: false,
				height:370,
				width:450,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").show("highlight", {}, 1000, callback);
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		};

		
		$(function() {
			$( "#datepickerDe" ).datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true
			});
			$( "#datepickerDe2" ).datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true
			});
			$( "#datepickerAte" ).datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true
			});
			
			$("#numCota").numeric();
		}); 

		function mostrarBaixaManual(){
			$( '#dadosArquivo' ).hide( );
			$( '#tableBaixaManual' ).show( );
			$( '#tableBaixaAuto' ).hide( );
		}
		
		function dividaManual(){
			$( '#extratoBaixaManual' ).show( );
			$( '#dadosArquivo' ).hide( );
		}
		
		function dividaManualNossoNumero(){
			$( '#nossoNumero' ).show( );
			$( '#porCota' ).hide( );
		}
		
		function dividaManualCota(){
			$( '#extratoBaixaManual' ).show( );
			$( '#porCota' ).show( );
			$( '#nossoNumero' ).hide( );	
		}	
		




















		//BAIXA MANUAL POR NOSSO NUMERO
		function buscaManual() {
			
			var nossoNumero = $("#filtroNossoNumero").val();
			var numCota = $("#numCota").val();
			
			if (nossoNumero==''){
				$(".liberaDividaGrid").flexOptions({
					url: "<c:url value='/financeiro/buscaBoletos' />",
					params: [
					         {name:'numCota', value:$("#numCota").val()}
					        ] ,
				});
				$(".liberaDividaGrid").flexReload();
				$(".grids").show();
				
				dividaManualCota();
				
			}
			else{
				var data = "nossoNumero = " + nossoNumero;
				$.postJSON("<c:url value='/financeiro/buscaBoleto' />",nossoNumero, setBoleto);
			}
			
		}
		
		function setBoleto(result) {
			var cobranca = result;
			
			$("#cota").html(cobranca.cota);
			$("#banco").html(cobranca.banco);
			$("#nossoNumero").html(cobranca.nossoNumero);
			$("#dataEmissao").html(cobranca.dataEmissao);
			$("#dataVencimento").html(cobranca.dataVencimento);
			$("#valor").html(cobranca.valor);
			
			$("#dividaTotal").html(cobranca.dividaTotal);
			$("#dataPagamento").html(cobranca.dataPagamento);
			$("#desconto").html(cobranca.desconto);
			$("#juros").html(cobranca.juros);
			$("#valorTotal").html(cobranca.valorTotal);
			
			dividaManualNossoNumero();
		}
		
		function getDataFromResult(resultado) {
			//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
			if (resultado.mensagens) {
				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				$(".grids").hide();
				return resultado.tableModel;
			}

			var dadosPesquisa;
			$.each(resultado, function(index, value) {
				  if(value[0] == "TblModelBoletos") {
					  dadosPesquisa = value[1];
				  }
		    });
			
			return dadosPesquisa;
		}
		
		
		
		
		
		
		
		
		

		
	</script>
	
	<style>

		#resultadoIntegracao{display:none;}
        #tableBaixaManual, #tableBaixaAuto, #extratoBaixaManual, #nossoNumero, #porCota, #dialog-divida{display:none;}

	</style>
	
    </head>

<body>

	<div id="dialog-excluir" title="Baixa Bancária">
		<p>Confirma a Baixa deste Valor?</p>
	</div>	
	
	<div id="dialog-divida" title="Baixa Banc�ria">
		<table width="414" border="0" cellpadding="2" cellspacing="2">
	  <tr>
	    <td width="114"><strong>Valor D�vida R$:</strong>
	    
	    </td>
	    <td width="114" align="right"><strong>1.000,00</strong></td>
	    <td width="166" align="right">&nbsp;</td>
	  </tr>
	  <tr>
	    <td>Juros:</td>
	    <td align="right"><input type="text" style="width:80px; text-align:right;" /></td>
	    <td align="right">&nbsp;</td>
	  </tr>
	  <tr>
	    <td>Desconto:</td>
	    <td align="right"><input type="text" style="width:80px; text-align:right;" /></td>
	    <td align="right">&nbsp;</td>
	  </tr>
	  <tr>
	    <td>Valor pago R$:</td>
	    <td align="right"><input type="text" style="width:80px; text-align:right;" /></td>
	    <td align="right">&nbsp;</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td align="right">------------------</td>
	    <td align="right">&nbsp;</td>
	  </tr>
	  <tr>
	    <td>Saldo R$:</td>
	    <td align="right"><strong>600,00</strong></td>
	    <td align="right">&nbsp;</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td align="right"></td>
	    <td align="right"></td>
	  </tr>
	  <tr>
	    <td><strong>Observa��o:</strong></td>
	    <td colspan="2"><textarea name="textarea" id="textarea" cols="45" rows="3" style="width:280px;"></textarea></td>
	  </tr>
	</table>
	</div>
	
	<div id="dialog-manual" title="Baixa de Boletos Manual">
	  <table width="436" border="0" cellpadding="2" cellspacing="1">
	  <tr>
	    <td width="430" align="center"><table width="430" border="0" cellpadding="2" cellspacing="1" style="text-align:left;">
	      <tr>
	        <td><strong>Cota:</strong></td>
	        <td colspan="3">9999 - Jos� da Silva Pereira</td>
	      </tr>
	      <tr>
	        <td width="85"><strong>Banco:</strong></td>
	        <td width="142">Santander</td>
	        <td width="71"><strong>Emiss�o:</strong></td>
	        <td width="111">12/11/2011</td>
	      </tr>
	      <tr>
	        <td><strong>Vencimento:</strong></td>
	        <td>12/12/2011</td>
	        <td><strong>Valor R$:</strong></td>
	        <td>999.999,99</td>
	      </tr>
	    </table>
	      <br />
	      <table width="430" border="0" cellpadding="2" cellspacing="1" style="text-align:left;">
	        <tr>
	          <td width="120"><strong>Data Pagto</strong></td>
	          <td width="15" align="center"><strong>&nbsp;&nbsp;</strong></td>
	          <td width="155"><strong>Valor Desconto R$</strong></td>
	          <td width="11" align="center"><strong>&nbsp;&nbsp;</strong></td>
	          <td width="126"><strong>Valor Juros R$</strong></td>
	        </tr>
	        <tr>
	          <td><input type="text" name="textfield12" id="datepickerDe" style="width:80px;"/></td>
	          <td align="center">&nbsp;</td>
	          <td><input type="text" name="textfield13" id="textfield13" style="width:80px; text-align:right;"/></td>
	          <td align="center">&nbsp;</td>
	          <td><input type="text" name="textfield14" id="textfield14" style="width:80px; text-align:right;"/></td>
	        </tr>
	      </table>
	      <br />
	      <table width="430" border="0" cellpadding="2" cellspacing="1" style="text-align:left;">
	        <tr>
	          <td><strong>Motivo:</strong></td>
	        </tr>
	        <tr>
	          <td width="424"><textarea name="textfield6" rows="3" id="textfield7" style="width:410px;"></textarea></td>
	        </tr>
	    </table>      
	    </td>
	  </tr>
	</table>
	</div>
	
	<div id="dialog-novo" title="Detalhes da D�vida">
		<table class="dadosDividaGrid"></table>
	    <br />
	    <strong>Saldo R$: -600,00</strong>
	    <br /> 
	</div>	

	<fieldset class="classFieldset">
		
		<legend> Baixa Financeira</legend>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="115">Tipo de Baixa:</td>
              <td colspan="3"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="7%"><input type="radio" name="baixaFinanceira" id="radio" value="radio"  onclick="mostrarBaixaManual();"/></td>
                  <td width="22%">Manual</td>
                  <td width="8%"><input type="radio" name="baixaFinanceira" id="radio2" value="radio" onclick="mostrarBaixaAuto();" /></td>
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
		
		
		
		
		<form action="<c:url value='/financeiro/baixa' />" id="formBaixaAutomatica"
			  method="post" enctype="multipart/form-data" >
		
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
							<span class="bt_integrar">
								<a href="javascript:;" onclick="integrar();">Integrar</a>
							</span>
						</td>
					</tr>			
			</table>
		</form>
		
		
		
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro" id="tableBaixaManual">
            <tr>
				<td width="29">Cota:</td>
                <td width="260">
              
                <input name="numCota" 
             	    id="numCota" 
             		type="number"
             		maxlength="11"
             		style="width:80px; 
             		float:left; margin-right:5px;"
             		onchange="cota.limparCamposPesquisa('#descricaoCota')" />
             	  
                 <span class="classPesquisar" title="Pesquisar Cota">
             	     <a href="javascript:;" onclick="cota.pesquisarPorNumeroCota('#numCota', '#descricaoCota');">&nbsp;</a>
             	 </span>
		
		         <input name="descricaoCota" 
		      		 id="descricaoCota" 
		      		 type="text" 
		      		 class="nome_jornaleiro" 
		      		 maxlength="255"
		      		 style="width:130px;"
		      		 onkeyup="cota.autoCompletarPorNome('#descricaoCota');" 
		      		 onchange="cota.pesquisarPorNomeCota('#numCota', '#descricaoCota');" />
		         </td>
			  
				 <td width="97">Nosso Número:</td>
				 <td width="333"><input type="text" name="filtroNossoNumero" id="filtroNossoNumero" style="width: 300px;" /></td>
				 <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="buscaManual();">Pesquisar</a></span></td>
			</tr>
         </table>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
	
	<fieldset class="classFieldset" id="extratoBaixaManual" >
      	<legend>Baixa Manual</legend>
        <br />

      	<table width="342" border="0" align="center" cellpadding="2" cellspacing="1" style="text-align:left;" id="nossoNumero">
      	  <tr>
      	    <td colspan="2" class="header_table" align="center">Dados Boleto</td>
   	      </tr>
      	  <tr>
      	    <td class="linha_borda"><strong>N�m. Boleto:</strong></td>
      	    <td class="linha_borda">987675433456675</td>
   	      </tr>
      	  <tr>
      	    <td class="linha_borda"><strong>Cota:</strong></td>
      	    <td class="linha_borda">9999 - Jos� da Silva Pereira</td>
   	      </tr>
      	  <tr>
      	    <td width="81" class="linha_borda"><strong>Banco:</strong></td>
      	    <td width="250" class="linha_borda">Santander</td>
   	      </tr>
      	  <tr>
      	    <td class="linha_borda"><strong>Emiss�o:</strong></td>
      	    <td class="linha_borda">12/11/2011</td>
   	      </tr>
      	  <tr>
      	    <td class="linha_borda"><strong>Vencimento:</strong></td>
      	    <td class="linha_borda">12/12/2011</td>
   	      </tr>
      	  <tr>
      	    <td class="linha_borda"><strong>Valor R$:</strong></td>
      	    <td class="linha_borda">1.000,00</td>
   	      </tr>
      	  <tr>
      	    <td class="linha_borda">&nbsp;</td>
      	    <td class="linha_borda"><span class="bt_confirmar_novo" title="Confirmar"><a onclick="popup_excluir();" href="javascript:;"><img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Confirmar</a></span></td>
   	      </tr>
   	    </table>
		
      	<div  id="porCota">
      	
         <table class="liberaDividaGrid"></table>
         
         <table width="100%" border="0" cellspacing="2" cellpadding="2">
              <tr>
                <td width="24%"><span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

                 <span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
                 </td>
                 <td width="47%">   
                    <span class="bt_confirmar_novo" title="Pagar Boleto"><a onclick="popup_divida();" href="javascript:;"><img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Pagar</a></span>
                    
                    <span class="bt_confirmar_novo" title="Negociar D�vida"><a href="javascript:;"><img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Negociar</a></span>


                </td>
                <td width="7%"><strong>Total R$:</strong></td>
                <td width="7%">10.567,00</td>
                <td width="15%"><span class="bt_sellAll"><label for="sel">Selecionar Todos</label><input type="checkbox" id="sel" name="Todos" onclick="checkAll();" style="float:left;"/></span></td>
              </tr>
            </table>
        </div>
		
    </fieldset>

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
	
</body>