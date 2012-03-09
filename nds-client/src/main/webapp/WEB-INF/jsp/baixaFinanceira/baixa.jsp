<head>
	
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
	<script type="text/javascript">
		function popup_manual() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
			$("#dialog-manual").dialog({
				resizable : false,
				height : 'auto',
				width : 470,
				modal : true,
				buttons : {
					"Confirmar" : function() {
	
						$(this).dialog("close");
	
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				}
			});
		};
		function popup() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
			$("#dialog-novo").dialog({
				resizable : false,
				height : 280,
				width : 770,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						popup_excluir();
						$(this).dialog("close");
	
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				}
			});
		};
	
		function popup_alterar() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
			$("#dialog-novo").dialog({
				resizable : false,
				height : 500,
				width : 650,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						$(this).dialog("close");
						$("#effect").hide("highlight", {}, 1000, callback);
	
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				}
			});
	
		};
	
		function popup_excluir() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
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
			$("input[id^='datepicker']")
					.datepicker(
							{
								showOn : "button",
								buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
								buttonImageOnly : true
							});
	
		});
	
		function mostraManual() {
	
			$('#dadosArquivo').hide();
			$('#baixaManual').show();
			$('#baixaAuto').hide();
		}
	
		function mostraAuto() {
	
			$('#baixaAuto').show();
			$('#baixaManual').hide();
			$('#extratoBaixaManual').hide();
		}
		function dividaManual() {
	
			$('#extratoBaixaManual').show();
			$('#dadosArquivo').hide();
	
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
			$('#dadosArquivo').show();
			$('#extratoBaixaManual').hide();
	
		}
	</script>
	
	<style>
		#dadosArquivo {
			display: none;
		}
		
		#baixaManual,#baixaAuto,#extratoBaixaManual,#nossoNumero,#porCota {
			display: none;
		}
	</style>
</head>

<body>
	<div id="dialog-excluir" title="Baixa Bancária">
		<p>Confirma a Baixa deste Valor?</p>
	</div>

	<div id="dialog-manual" title="Baixa de Boletos Manual">

		<table width="436" border="0" cellpadding="2" cellspacing="1">
			<tr>
				<td width="430" align="center"><table width="430" border="0"
						cellpadding="2" cellspacing="1" style="text-align: left;">
						<tr>
							<td><strong>Cota:</strong></td>
							<td colspan="3">9999 - José da Silva Pereira</td>
						</tr>
						<tr>
							<td width="85"><strong>Banco:</strong></td>
							<td width="142">Santander</td>
							<td width="71"><strong>Emissão:</strong></td>
							<td width="111">12/11/2011</td>
						</tr>
						<tr>
							<td><strong>Vencimento:</strong></td>
							<td>12/12/2011</td>
							<td><strong>Valor R$:</strong></td>
							<td>999.999,99</td>
						</tr>
					</table> <br />
					<table width="430" border="0" cellpadding="2" cellspacing="1"
						style="text-align: left;">
						<tr>
							<td width="120"><strong>Data Pagto</strong></td>
							<td width="15" align="center"><strong>&nbsp;&nbsp;</strong></td>
							<td width="155"><strong>Valor Desconto R$</strong></td>
							<td width="11" align="center"><strong>&nbsp;&nbsp;</strong></td>
							<td width="126"><strong>Valor Juros R$</strong></td>
						</tr>
						<tr>
							<td><input type="text" name="textfield12" id="datepickerDe"
								style="width: 80px;" /></td>
							<td align="center">&nbsp;</td>
							<td><input type="text" name="textfield13" id="textfield13"
								style="width: 80px; text-align: right;" /></td>
							<td align="center">&nbsp;</td>
							<td><input type="text" name="textfield14" id="textfield14"
								style="width: 80px; text-align: right;" /></td>
						</tr>
					</table> <br />
					<table width="430" border="0" cellpadding="2" cellspacing="1"
						style="text-align: left;">
						<tr>
							<td><strong>Motivo:</strong></td>
						</tr>
						<tr>
							<td width="424"><textarea name="textfield6" rows="3"
									id="textfield7" style="width: 410px;"></textarea></td>
						</tr>
					</table></td>
			</tr>
		</table>


	</div>



	<div id="dialog-novo" title="Baixa Bancária">

		<table width="740" border="0" cellpadding="2" cellspacing="1">
			<tr>
				<td width="114" align="left"><strong>Nome do Arquivo:</strong></td>
				<td width="191" align="left">CNR00145.DAT</td>
				<td width="114" align="left"><strong>Data Competência:</strong></td>
				<td width="92" align="left">01/11/2011</td>
				<td width="71" align="left"><strong>Valor R$:</strong></td>
				<td width="127" align="left">999.999,99</td>
			</tr>
		</table>
		<table width="740" border="0" cellpadding="2" cellspacing="1">
			<tr class="header_table">
				<td width="350" align="center"><strong>Leitura e
						Conferência de Arquivo</strong></td>
				<td width="24" align="center"><strong>&nbsp;&nbsp;</strong></td>
				<td width="350" align="center"><strong>Baixa
						Automática</strong></td>
			</tr>
			<tr>
				<td align="center" valign="top" bgcolor="#FFFFFF"
					style="border: 1px solid #CCC;"><table width="100%" border="0"
						cellspacing="1" cellpadding="1">
						<tr>
							<td width="44%" align="left"><strong>Qtde.
									Registros:</strong></td>
							<td width="56%" align="left">99999</td>
						</tr>
						<tr>
							<td align="left"><strong>Data do Processamento:</strong></td>
							<td align="left">99/99/9999</td>
						</tr>
						<tr>
							<td align="left"><strong> Registros Verificados:</strong></td>
							<td align="left">999.999</td>
						</tr>
					</table></td>
				<td align="center" bgcolor="#FFFFFF">&nbsp;</td>
				<td align="center" valign="top" bgcolor="#FFFFFF"
					style="border: 1px solid #CCC;"><table width="100%" border="0"
						cellspacing="1" cellpadding="1">
						<tr>
							<td width="52%" align="left"><strong>Registros
									Lidos:</strong></td>
							<td width="48%" align="left">999.999</td>
						</tr>
						<tr>
							<td align="left"><strong>Registros Baixados:</strong></td>
							<td align="left">999.999</td>
						</tr>
						<tr>
							<td align="left"><strong>Registros Rejeitados:</strong></td>
							<td align="left">999.999</td>
						</tr>
					</table></td>
			</tr>
		</table>
	</div>

	<fieldset class="classFieldset">
		<legend> Baixa Financeira</legend>
		<table width="950" border="0" cellpadding="2" cellspacing="1"
			class="filtro">
			<tr>
				<td width="115">Tipo de Baixa:</td>
				<td colspan="3"><table width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="7%"><input type="radio" name="baixa" id="radio"
								value="radio" onclick="mostraManual();" /></td>
							<td width="22%">Manual</td>
							<td width="8%"><input type="radio" name="baixa" id="radio2"
								value="radio" onclick="mostraAuto();" /></td>
							<td width="63%">Automática</td>
						</tr>
					</table></td>
				<td width="112">&nbsp;</td>
				<td width="114">&nbsp;</td>
				<td width="55">&nbsp;</td>
				<td width="102">&nbsp;</td>
				<td width="104">&nbsp;</td>
			</tr>
		</table>
		<table width="950" border="0" cellpadding="2" cellspacing="1"
			class="filtro" id="baixaAuto">
			<tr>
				<td width="100">Nome Arquivo:</td>
				<td colspan="3"><input name="fileField" type="file"
					id="fileField" size="25" /></td>
				<td width="117">Data Competência:</td>
				<td width="109"><input name="datepickerAte" type="text"
					id="datepickerAte" style="width: 80px;" /></td>
				<td width="129">Valor Financeiro R$:</td>
				<td width="104"><input type="text" name="textfield"
					id="textfield" style="width: 90px; text-align: right;" /></td>
				<td width="111"><span class="bt_integrar"><a
						href="javascript:;" onclick="mostrarArquivo();">Integrar</a></span></td>
			</tr>
		</table>
		<table width="950" border="0" cellpadding="2" cellspacing="1"
			class="filtro" id="baixaManual">
			<tr>
				<td width="31">Cota:</td>
				<td colspan="3"><input type="text"
					style="width: 60px; float: left; margin-right: 5px;" /> <span
					class="classPesquisar"><a href="javascript:;"
						onclick="dividaManualCota();">&nbsp;</a></span></td>
				<td width="39">Nome:</td>
				<td width="210"><input type="text"
					style="width: 200px; float: left; margin-right: 5px;" /></td>
				<td width="97">Nosso Número:</td>
				<td width="333"><input type="text" style="width: 300px;" /></td>
				<td width="104"><span class="bt_pesquisar"><a
						href="javascript:;" onclick="dividaManualNossoNumero();">Pesquisar</a></span></td>
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
				<td class="linha_borda"><strong>Núm. Boleto:</strong></td>
				<td class="linha_borda">987675433456675</td>
			</tr>
			<tr>
				<td class="linha_borda"><strong>Cota:</strong></td>
				<td class="linha_borda">9999 - José da Silva Pereira</td>
			</tr>
			<tr>
				<td width="81" class="linha_borda"><strong>Banco:</strong></td>
				<td width="250" class="linha_borda">Santander</td>
			</tr>
			<tr>
				<td class="linha_borda"><strong>Emissão:</strong></td>
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
							<td colspan="3" class="linha_borda">9999 - José da Silva
								Pereira</td>
						</tr>
						<tr>
							<td width="85" class="linha_borda"><strong>Banco:</strong></td>
							<td width="142" class="linha_borda">Santander</td>
							<td width="71" class="linha_borda"><strong>Emissão:</strong></td>
							<td width="91" class="linha_borda">12/11/2011</td>
						</tr>
						<tr>
							<td class="linha_borda"><strong>Vencimento:</strong></td>
							<td class="linha_borda">12/12/2011</td>
							<td class="linha_borda"><strong>Valor R$:</strong></td>
							<td class="linha_borda">999.999,99</td>
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
							<td width="110" class="linha_borda">12.000,00</td>
						</tr>
						<tr>
							<td class="linha_borda"><strong>Data Pagamento:</strong></td>
							<td class="linha_borda">12/12/2011</td>
						</tr>
						<tr>
							<td class="linha_borda"><strong>Desconto R$:</strong></td>
							<td class="linha_borda">0,00</td>
						</tr>
						<tr>
							<td class="linha_borda"><strong>Juros R$:</strong></td>
							<td class="linha_borda">0,00</td>
						</tr>
						<tr>
							<td class="linha_borda"><strong>Valor Total R$:</strong></td>
							<td class="linha_borda">11.000,00</td>
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
							<td width="137" align="right" class="linha_borda">CNR00145.DAT</td>
						</tr>
						<tr>
							<td align="left" class="linha_borda"><strong>Data
									Competência:</strong></td>
							<td align="right" class="linha_borda">01/11/2011</td>
						</tr>
						<tr>
							<td align="left" class="linha_borda"><strong>Valor
									R$:</strong></td>
							<td align="right" class="linha_borda">999.999,99</td>
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
							<td width="102" align="right" class="linha_borda">999.999</td>
						</tr>
						<tr>
							<td align="left" class="linha_borda"><strong>Registros
									Baixados:</strong></td>
							<td align="right" class="linha_borda">999.999</td>
						</tr>
						<tr>
							<td align="left" class="linha_borda"><strong>Registros
									Rejeitados:</strong></td>
							<td align="right" class="linha_borda">999.999</td>
						</tr>
						<tr>
							<td align="left" class="linha_borda"><strong>Baixados
									com Divergência:</strong></td>
							<td align="right" class="linha_borda">999.999</td>
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