<div id="tabAprovacao" style="font-size: 11px;">
	<br/>
	<fieldset style="width: 470px !important; margin-bottom: 5px; margin-left:10px;">
		<legend>Aprovação</legend>
		<table width="450" border="0" cellspacing="2" cellpadding="2">
			<tr>
				<td width="23">
				    <input onchange="parametrosDistribuidorController.mensagemControleAprovacao(this.checked);"
					       name="parametrosDistribuidor.utilizaControleAprovacao"
					       id="utilizaControleAprovacao" type="checkbox" ${parametrosDistribuidor.utilizaControleAprovacao ? "checked" : ""} 
					       onclick="parametrosDistribuidorController.alternarControleAprovacao();" />
			    </td>
				<td width="190">Utiliza Controle de Aprovação?</td>
				<td width="20">&nbsp;</td>
				<td width="178">&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</table>
		<table width="451" border="0" cellspacing="2" cellpadding="2" id="controlesAprovacao">
			<tr>
				<td colspan="6">Para as Funcionalidades:</td>
			</tr>
			<tr>
				<td width="20"><input
					name="parametrosDistribuidor.paraDebitosCreditos"
					id="paraDebitosCreditos" type="checkbox" ${parametrosDistribuidor.paraDebitosCreditos ? "checked" : ""} /></td>
				<td width="143">Débitos e Créditos</td>
				<td width="20"><input name="parametrosDistribuidor.negociacao"
					id="negociacao" type="checkbox" ${parametrosDistribuidor.negociacao ? "checked" : ""} /></td>
				<td width="133">Negociação</td>
				<td width="20"><input
					name="parametrosDistribuidor.ajusteEstoque" id="ajusteEstoque"
					type="checkbox" ${parametrosDistribuidor.ajusteEstoque ? "checked" : ""} /></td>
				<td width="115">Ajuste de Estoque</td>
			</tr>
			<tr>
				<td><input name="parametrosDistribuidor.postergacaoCobranca"
					type="checkbox" id="postergacaoCobranca" ${parametrosDistribuidor.postergacaoCobranca ? "checked" : ""} /></td>
				<td>Postergação de Cobrança</td>
				<td><input name="parametrosDistribuidor.devolucaoFornecedor"
					type="checkbox" id="devolucaoFornecedor" ${parametrosDistribuidor.devolucaoFornecedor ? "checked" : ""} /></td>
				<td>Devolução Fornecedor</td>
				<td><input name="parametrosDistribuidor.faltasSobras"
					type="checkbox" id="faltasSobras" ${parametrosDistribuidor.faltasSobras ? "checked" : ""} /></td>
				<td>Faltas e Sobras</td>
				<td><input name="parametrosDistribuidor.cobranca"
					type="checkbox" id="cobranca" ${parametrosDistribuidor.consolidadoCota ? "checked" : ""} /></td>
				<td>Cobrança</td>
			</tr>
		</table>
	</fieldset>
	<fieldset style="width: 390px !important; margin-bottom: 5px;">
		<legend>Aviso Prévio para Validade de Garantia</legend>
		<table width="320" border="0" cellspacing="2" cellpadding="2">
			<tr>
				<td width="280">Aviso prévio para vencimento de garantias (dias).</td>
				<td width="40"><input
					name="parametrosDistribuidor.prazoAvisoPrevioValidadeGarantia"
					type="text" id="prazoAvisoPrevioValidadeGarantia"
					value="${parametrosDistribuidor.prazoAvisoPrevioValidadeGarantia}"
					style="float: left; width: 40px;" value="15" /></td>
			</tr>
		</table>
	</fieldset>
</div>