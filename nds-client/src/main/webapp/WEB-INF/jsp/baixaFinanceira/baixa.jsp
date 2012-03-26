<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
	
	<script type="text/javascript">
	
		$(function() {
		    
			var options = {
				success: showResponse,
		    };
			
			$('#formBaixaAutomatica').ajaxForm(options);
			
		});
		
		function showResponse(data) {
			
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
			}
			
			if (responseJson.result) {
				
				alert(responseJson.result.nomeArquivo);
				alert(responseJson.result.quantidadeLidos);				
			}
		}
		
		function replaceAll(string, token, newtoken) {
			while (string.indexOf(token) != -1) {
		 		string = string.replace(token, newtoken);
			}
			return string;
		}	
	
		function popup_excluir() {	
			$("#dialog-excluir").dialog({
				resizable : false,
				height : 170,
				width : 380,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						$(this).dialog("close");
						$("#effect").show("highlight", {}, 1000, callback);
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				}
			});
		};
		
		$(function() {
			
			if (${exibeCamposBaixaAutomatica}) {
				
				$("#radioBaixaAuto").click();	
			}
			
			$("#valorFinanceiro").numeric();
	
		});
	
		function mostraBaixaManual() {
	
			$('#dadosArquivo').hide();
			$('#tableBaixaManual').show();
			$('#tableBaixaAuto').hide();
		}
	
		function mostrarBaixaAuto() {
	
			$('#tableBaixaAuto').show();
			$('#tableBaixaManual').hide();
			$('#extratoBaixaManual').hide();
		}
		
		function dividaManualNossoNumero() {
			$('#nossoNumero').show();
			$('#porCota').hide();
	
		}
		function dividaManualCota() {
			$('#extratoBaixaManual').show();
			$('#porCota').show();
			$('#nossoNumero').hide();
	
		}
		function mostrarArquivo() {
			
			$('#extratoBaixaManual').hide();
			
			$('#formBaixaAutomatica').submit();
		}
		
		
		function buscaBoleto() {
			var data = [{name: 'nossoNumero', value: $("#filtroNossoNumero").val()}];
			$.postJSON("<c:url value='/financeiro/buscaBoleto' />",data, setBoleto);
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
			
			dividaManualCota();
		}
		
		
	</script>
	
	<style>
		
		#tableBaixaManual,#tableBaixaAuto,#extratoBaixaManual,#nossoNumero,#porCota {
			display: none;
		}
	</style>
</head>

<body>
	<div id="dialog-excluir" title="Baixa Bancária">
		<p>Confirma a Baixa deste Valor?</p>
	</div>

	<fieldset class="classFieldset">
		
		<legend> Baixa Financeira</legend>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="115">Tipo de Baixa:</td>
				<td colspan="3">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="7%">
								<input type="radio" name="radioBaixa" id="radioBaixaManual"
									   value="radio" onclick="mostraBaixaManual();" />
							</td>
							<td width="22%">Manual</td>
							
							<td width="8%">
								<input type="radio" name="radioBaixa" id="radioBaixaAuto"
									   value="radio" onclick="mostrarBaixaAuto();" />
							</td>
							<td width="63%">Automática</td>
						</tr>
					</table>
				</td>
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
								<a href="javascript:;" onclick="mostrarArquivo();">Integrar</a>
							</span>
						</td>
					</tr>			
			</table>
		</form>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1"
			   class="filtro" id="tableBaixaManual">
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
				<td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="buscaBoleto();">Pesquisar</a></span></td>
			</tr>
		</table>
	</fieldset>
	
	
	<div class="linha_separa_fields">&nbsp;</div>
	<fieldset class="classFieldset" id="extratoBaixaManual">
		<legend>Baixa Manual</legend>
		<br />

		<table width="342" border="0" align="center" cellpadding="2"
			cellspacing="1" style="text-align: left;" id="nossoNumero">
			<tr>
				<td colspan="2" class="header_table" align="center">Dados
					Boleto</td>
			</tr>
			<tr>
				<td class="linha_borda"><strong>Num. Boleto:</strong></td>
				<td class="linha_borda"></td>
			</tr>
			<tr>
				<td class="linha_borda"><strong>Cota:</strong></td>
				<td class="linha_borda"></td>
			</tr>
			<tr>
				<td width="81" class="linha_borda"><strong>Banco:</strong></td>
				<td width="250" class="linha_borda"></td>
			</tr>
			<tr>
				<td class="linha_borda"><strong>Emissão:</strong></td>
				<td class="linha_borda"></td>
			</tr>
			<tr>
				<td class="linha_borda"><strong>Vencimento:</strong></td>
				<td class="linha_borda"></td>
			</tr>
			<tr>
				<td class="linha_borda"><strong>Valor R$:</strong></td>
				<td class="linha_borda"></td>
			</tr>
			<tr>
				<td class="linha_borda">&nbsp;</td>
				<td class="linha_borda"><span class="bt_confirmar_novo"
					title="Confirmar"><a onclick="popup_excluir();"
						href="javascript:;"><img border="0" hspace="5"
							src="${pageContext.request.contextPath}/images/ico_check.gif">Confirmar</a></span></td>
			</tr>
		</table>
		
		
		<table width="687" border="0" align="center" cellpadding="2"
			cellspacing="2" id="porCota">
			<tr>
				<td width="410" valign="top"><table width="410" border="0"
						cellpadding="2" cellspacing="1">
						<tr>
							<td colspan="4" align="center" class="header_table">Dados do
								Boleto</td>
						</tr>
						<tr>
							<td class="linha_borda"><strong>Cota:</strong></td>
							<td colspan="3" class="linha_borda" id="cota"><c:out value="cota"/></td>
						</tr>
						<tr>
							<td width="85" class="linha_borda"><strong>Banco:</strong></td>
							<td width="142" class="linha_borda" id="banco"><c:out value="banco"/></td>
							<td width="71" class="linha_borda"><strong>Emissão:</strong></td>
							<td width="91" class="linha_borda" id="dataEmissao"><c:out value="dataEmissao"/></td>
						</tr>
						<tr>
							<td class="linha_borda"><strong>Vencimento:</strong></td>
							<td class="linha_borda" id="dataVencimento"><c:out value="dataVencimento"/></td>
							<td class="linha_borda"><strong>Valor R$:</strong></td>
							<td class="linha_borda" id="valor"><c:out value="valor"/></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
					</table>
					<table width="410" border="0" cellpadding="2" cellspacing="1">
						<tr>
							<td width="187" align="center" class="header_table">Valor
								Desconto R$</td>
							<td width="8" align="center" class="linha_borda">&nbsp;&nbsp;</td>
							<td width="199" align="center" class="header_table">Valor
								Juros R$</td>
						</tr>
						<tr>
							<td align="center" class="linha_borda"><input type="text"
								name="textfield2" id="textfield2"
								style="width: 80px; text-align: right;" /></td>
							<td align="center" class="linha_borda">&nbsp;</td>
							<td align="center" class="linha_borda"><input type="text"
								name="textfield2" id="textfield3"
								style="width: 80px; text-align: right;" /></td>
						</tr>
					</table> <br clear="all" /> <strong>Motivo:</strong> <br clear="all" /> <textarea
						name="" cols="" rows="3" style="width: 400px;"></textarea></td>
				<td width="410" valign="top">&nbsp;&nbsp;</td>
				<td width="249" valign="top"><table width="249" border="0"
						cellspacing="2" cellpadding="1">
						<tr>
							<td colspan="2" align="center" class="header_table">Dados da
								Dívida</td>
						</tr>
						<tr>
							<td width="129" class="linha_borda"><strong>Dívida
									Total R$:</strong></td>
							<td width="110" class="linha_borda" id="dividaTotal"><c:out value="dividaTotal"/></td>
						</tr>
						<tr>
							<td class="linha_borda"><strong>Data Pagamento:</strong></td>
							<td class="linha_borda" id="dataPagamento"><c:out value="dataPagamento"/></td>
						</tr>
						<tr>
							<td class="linha_borda"><strong>Desconto R$:</strong></td>
							<td class="linha_borda" id="desconto"><c:out value="desconto"/></td>
						</tr>
						<tr>
							<td class="linha_borda"><strong>Juros R$:</strong></td>
							<td class="linha_borda" id="juros"><c:out value="juros"/></td>
						</tr>
						<tr>
							<td class="linha_borda"><strong>Valor Total R$:</strong></td>
							<td class="linha_borda" id="valorTotal"><c:out value="valorTotal"/></td>
						</tr>
					</table></td>
			</tr>
			
			<tr>
				<td align="center" valign="top"><span class="bt_confirmar_novo"
					title="Confirmar"><a onclick="popup_excluir();"
						href="javascript:;"><img border="0" hspace="5"
							src="${pageContext.request.contextPath}/images/ico_check.gif">Confirmar</a></span></td>
				<td valign="top">&nbsp;</td>
				<td valign="top">&nbsp;</td>
			</tr>
			
		</table>
		
		
	</fieldset>


	<c:if test="${resumoBaixaAutomaticaBoleto != null}">
		<fieldset class="classFieldset" id="dadosArquivo">
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
								<td width="137" align="right" class="linha_borda">
									${resumoBaixaAutomaticaBoleto.nomeArquivo}
								</td>
							</tr>
							<tr>
								<td align="left" class="linha_borda"><strong>Data
										Competência:</strong></td>
								<td align="right" class="linha_borda">
									${resumoBaixaAutomaticaBoleto.dataCompetencia}
								</td>
							</tr>
							<tr>
								<td align="left" class="linha_borda"><strong>Valor
										R$:</strong></td>
								<td align="right" class="linha_borda">
									${resumoBaixaAutomaticaBoleto.somaPagamentos}
								</td>
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
								<td id="quantidadeLidos" width="102" align="right" class="linha_borda">
									${resumoBaixaAutomaticaBoleto.quantidadeLidos}
								</td>
							</tr>
							<tr>
								<td align="left" class="linha_borda"><strong>Registros
										Baixados:</strong></td>
								<td align="right" class="linha_borda">
									${resumoBaixaAutomaticaBoleto.quantidadeBaixados}
								</td>
							</tr>
							<tr>
								<td align="left" class="linha_borda"><strong>Registros
										Rejeitados:</strong></td>
								<td align="right" class="linha_borda">
									${resumoBaixaAutomaticaBoleto.quantidadeRejeitados}
								</td>
							</tr>
							<tr>
								<td align="left" class="linha_borda"><strong>Baixados
										com Divergência:</strong></td>
								<td align="right" class="linha_borda">
									${resumoBaixaAutomaticaBoleto.quantidadeBaixadosComDivergencia}
								</td>
							</tr>
						</table></td>
				</tr>
			</table>
			<br /> <br />
			<div class="linha_separa_fields">&nbsp;</div>
			<br clear="all" />
	
		</fieldset>
	</c:if>
	<div class="linha_separa_fields">&nbsp;</div>
	
</body>