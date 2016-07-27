<div id="tabEmissao">
	<br/>
	<fieldset style="width: 390px !important; margin-left:10px;">
		<legend>Emissão de Documentos</legend>
		<table width="380" border="0" cellspacing="2" cellpadding="2">
			<tr class="header_table">
				<td>Utiliza:</td>
				<td align="center">Impressão</td>
				<td align="center">E-mail</td>
			</tr>
			<tr class="class_linha_1">
				<td width="173">Slip</td>
				<td width="100" align="center"><input
					name="parametrosDistribuidor.slipImpressao" id="slipImpressao"
					type="checkbox" ${parametrosDistribuidor.slipImpressao ? "checked" : ""}/></td>
				<td width="107" align="center"><input
					name="parametrosDistribuidor.slipEmail" id="slipEmail"
					type="checkbox" ${parametrosDistribuidor.slipEmail ? "checked" : ""}/></td>
			</tr>
			<tr class="class_linha_2">
				<td>Boleto / Recibo</td>
				<td align="center"><input
					name="parametrosDistribuidor.boletoImpressao" id="boletoImpressao"
					type="checkbox" ${parametrosDistribuidor.boletoImpressao ? "checked" : ""}/></td>
				<td align="center"><input
					name="parametrosDistribuidor.boletoEmail" id="boletoEmail"
					type="checkbox" ${parametrosDistribuidor.boletoEmail ? "checked" : ""}/></td>
			</tr>
			<tr class="class_linha_1">
				<td>Boleto + Slip</td>
				<td align="center"><input
					name="parametrosDistribuidor.boletoSlipImpressao"
					id="boletoSlipImpressao" type="checkbox" ${parametrosDistribuidor.boletoSlipImpressao ? "checked" : ""}/></td>
				<td align="center"><input
					name="parametrosDistribuidor.boletoSlipEmail" id="boletoSlipEmail"
					type="checkbox" ${parametrosDistribuidor.boletoSlipEmail ? "checked" : ""}/></td>
			</tr>
<!-- 			<tr class="class_linha_2"> -->
<!-- 				<td>Recibo</td> -->
<!-- 				<td align="center"><input -->
<!-- 					name="parametrosDistribuidor.reciboImpressao" id="reciboImpressao" -->
<%-- 					type="checkbox" ${parametrosDistribuidor.reciboImpressao ? "checked" : ""}/></td> --%>
<!-- 				<td align="center"><input -->
<!-- 					name="parametrosDistribuidor.reciboEmail" id="reciboEmail" -->
<%-- 					type="checkbox" ${parametrosDistribuidor.reciboEmail ? "checked" : ""}/></td> --%>
<!-- 			</tr> -->
			<tr class="class_linha_1">
				<td>Nota de Envio</td>
				<td align="center"><input
					name="parametrosDistribuidor.notaEnvioImpressao"
					id="notaEnvioImpressao" type="checkbox" ${parametrosDistribuidor.notaEnvioImpressao ? "checked" : ""}/></td>
				<td align="center"><input
					name="parametrosDistribuidor.notaEnvioEmail" id="notaEnvioEmail"
					type="checkbox" ${parametrosDistribuidor.notaEnvioEmail ? "checked" : ""}/></td>
			</tr>
			<tr class="class_linha_2">
				<td>Chamada de Encalhe</td>
				<td align="center"><input
					name="parametrosDistribuidor.chamadaEncalheImpressao"
					id="chamadaEncalheImpressao" type="checkbox" ${parametrosDistribuidor.chamadaEncalheImpressao ? "checked" : ""}/></td>
				<td align="center"><input
					name="parametrosDistribuidor.chamadaEncalheEmail"
					id="chamadaEncalheEmail" type="checkbox" ${parametrosDistribuidor.chamadaEncalheEmail ? "checked" : ""}/></td>
			</tr>
		</table>
	</fieldset>
	<fieldset style="width: 410px !important; margin-bottom: 5px;">
		<legend>Modelo NE / NECA / Danfe</legend>
		<input type="hidden" id="_parametrosDistribuidorimpressaoNECADANFE" value="${parametrosDistribuidor.impressaoNECADANFE}"/>
		<table width="325" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="20"><input type="radio" name="impressaoNECADANFE"
					id="impressaoNECADANFEMODELO1" value="MODELO_1" /></td>
				<td width="79"><a
					href="${pageContext.request.contextPath}/modelos/ce_modelo_3.htm"
					target="_blank">Modelo 1</a></td>
				<td width="20"><input type="radio" name="impressaoNECADANFE"
					id="impressaoNECADANFEMODELO2" value="MODELO_2" /></td>
				<td width="75"><a
					href="${pageContext.request.contextPath}/modelos/nota_envio.html"
					target="_blank">Modelo 2</a></td>
				<td width="20"><input type="radio" name="impressaoNECADANFE"
					id="impressaoNECADANFE" value="DANFE" /></td>
				<td width="111">Danfe</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</table>
	</fieldset>
	<fieldset style="width: 410px !important; margin-bottom: 5px;">
		<legend>Modelo Impressão CE</legend>
		<input type="hidden" id="_parametrosDistribuidorimpressaoCE" value="${parametrosDistribuidor.impressaoCE}" />
		<table width="325" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="26"><input type="radio" name="impressaoCE"
					id="impressaoCEModelo1" value="MODELO_1" /></td>
				<td width="93"><a
					href="${pageContext.request.contextPath}/modelos/ce_modelo_1.htm"
					target="_blank">Modelo 1</a></td>
				<td width="20"><input type="radio" name="impressaoCE"
					id="impressaoCEModelo2" value="MODELO_2" /></td>
				<td width="287"><a
					href="${pageContext.request.contextPath}/modelos/ce_modelo_2.html"
					target="_blank">Modelo 2</a></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</table>
	</fieldset>
	<fieldset style="width: 410px !important; margin-bottom: 5px;">
		<legend>Modelo Interface LED</legend>
		<input type="hidden" id="_parametrosDistribuidorimpressaoInterfaceLED" value="${parametrosDistribuidor.impressaoInterfaceLED}" />
		<table width="325" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="20"><input type="radio" name="interfaceLED"
					id="interfaceLEDMODELO1" value="MODELO_1" /></td>
				<td width="79">Modelo 1</td>
				<td width="20"><input type="radio" name="interfaceLED"
					id="interfaceLEDMODELO2" value="MODELO_2" /></td>
				<td width="75">Modelo 2</td>
				<td width="20"><input type="radio" name="interfaceLED"
					id="interfaceLEDMODELO3" value="MODELO_3" /></td>
				<td width="85">Modelo 3</td>
				<td width="20"><input type="radio" name="interfaceLED"
					id="interfaceLEDMODELO4" value="MODELO_4" /></td>
				<td width="85">Modelo 4</td>
			</tr>
			<tr>
				<td colspan="4">Nome Arquivo Modelo 1:</td>
				<td colspan="3" style="padding-left: 10px;">
					<input type="text" id="nomeArquivoInterfaceLED1" maxlength="100" style="width: 120px;"
						   value="${parametrosDistribuidor.nomeArquivoInterfaceLED1}">
				</td>
			</tr>
			<tr>
				<td colspan="4">Nome Arquivo Modelo 2:</td>
				<td colspan="3" style="padding-left: 10px;">
					<input type="text" id="nomeArquivoInterfaceLED2" maxlength="100" style="width: 120px;"
						   value="${parametrosDistribuidor.nomeArquivoInterfaceLED2}">
				</td>
			</tr>
			<tr>
				<td colspan="4">Nome Arquivo Modelo 3:</td>
				<td colspan="3" style="padding-left: 10px;">
					<input type="text" id="nomeArquivoInterfaceLED3" maxlength="100" style="width: 120px;"
						   value="${parametrosDistribuidor.nomeArquivoInterfaceLED3}">
				</td>
			</tr>
			<tr>
				<td colspan="4">Nome Arquivo Modelo 4:</td>
				<td colspan="3" style="padding-left: 10px;">
					<input type="text" id="nomeArquivoInterfaceLED4" maxlength="100" style="width: 120px;"
						   value="${parametrosDistribuidor.nomeArquivoInterfaceLED4}">
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</table>
	</fieldset>
	<br clear="all" />
</div>