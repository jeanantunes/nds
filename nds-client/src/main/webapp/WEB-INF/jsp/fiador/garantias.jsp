<div id="fiadorController-dialog-excluir-garantia" title="Garantias" style="display: none;">
	<p>Confirma esta Exclusão?</p>
</div>
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
			<span class="bt_add">
				<a href="javascript:fiadorController.adicionarEditarGarantia();" id="fiadorController-garantia-botaoAddEditarGarantia">Incluir Novo</a>
			</span>
		</td>
	</tr>
</table>
<br />
<label><strong>Garantias Cadastradas</strong></label>
<br />
<table class="fiadorController-imoveisGrid"></table>
<input type="hidden" id="fiadorController-referenciaGarantia"/>