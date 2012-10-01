<div id="fiadorController-dialog-excluir-garantia" title="Garantias" style="display: none;">
	<p>Confirma esta Exclusão?</p>
</div>
<fieldset style="margin:5px; width:880px;">
<legend>Cadastrar Garantias</legend>
<table width="750" cellpadding="2" cellspacing="2"
	style="text-align: left; display: s;" class="fiadorPF">
	<tr>
		<td>Valor R$:</td>
		<td><input type="text" style="width: 100px" id="fiadorController-garantia-valorGarantia" maxlength="255"/></td>
	</tr>
	<tr>
		<td>Descrição:</td>
		<td>
			<textarea name="textarea2" rows="4" style="width: 600px" id="fiadorController-garantia-descricaoGarantia" maxlength="255"></textarea>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>
			<span class="bt_novos">
				<a href="javascript:fiadorController.adicionarEditarGarantia();" id="fiadorController-garantia-botaoAddEditarGarantia" rel="tipsy" title="Incluir Nova Garantia"><img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0"/></a>
			</span>
		</td>
	</tr>
</table>
</fieldset>
<fieldset style="margin:5px; width:880px;">
<legend>Garantias Cadastradas</legend>
<table class="fiadorController-imoveisGrid"></table>
</fieldset>
<input type="hidden" id="fiadorController-referenciaGarantia"/>