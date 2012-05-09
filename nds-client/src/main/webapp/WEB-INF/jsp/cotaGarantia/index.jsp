<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/cotaGarantia.js"></script>

<script type="text/javascript">
var tipoCotaGarantia;
$(function(){
		tipoCotaGarantia = new TipoCotaGarantia();
});
</script>
<table width="755" cellpadding="2" cellspacing="2"
	style="text-align: left;">

	<tr>

		<td width="108">Tipo de Garantia:</td>

		<td width="631">
			<select id="tipoGarantiaSelect" onchange="tipoCotaGarantia.changeController($(this).val());" style="width: 250px;">
				<option value="" selected="selected">Selecione...</option>
		</select></td>

	</tr>

</table>


<div id="cotaGarantiaNotaPromissoriaPanel" style="display: none;">
	<fieldset>
		<legend>Nota Promiss&oacute;ria</legend>

		<table width="755" cellpadding="2" cellspacing="2"
			style="text-align: left;">
			<tr>
				<td width="128">N&#176; Nota Promiss&oacute;ria:</td>

				<td width="219"><span id="cotaGarantiaNotaPromissoriaId">123123</span></td>

				<td width="78">Vencimento:</td>

				<td width="302"><input type="text"
					name="cotaGarantiaNotaPromissoriaVencimento"
					id="cotaGarantiaNotaPromissoriaVencimento" style="width: 120px;" /></td>

			</tr>

			<tr>

				<td>Valor R&#36;</td>

				<td><input type="text" name="cotaGarantiaNotaPromissoriaValor"
					id="cotaGarantiaNotaPromissoriaValor"
					style="width: 120px; text-align: right;" /></td>

				<td>&nbsp;</td>

				<td>&nbsp;</td>

			</tr>

			<tr>

				<td>por extenso:</td>

				<td colspan="3"><input type="text"
					name="cotaGarantiaNotaPromissoriavalorExtenso"
					id="cotaGarantiaNotaPromissoriavalorExtenso" style="width: 425px;" /></td>

			</tr>

			<tr>

				<td>&nbsp;</td>
				<td colspan="3"><span class="bt_novos" title="Imprimir"><a
						href="javascript:void(0);" id="cotaGarantiaNotaPromissoriaImprimir" target="_blank"><img
							src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5"
							border="0" />Imprimir</a></span></td>

			</tr>

		</table>

	</fieldset>

</div>
<br clear="all" />