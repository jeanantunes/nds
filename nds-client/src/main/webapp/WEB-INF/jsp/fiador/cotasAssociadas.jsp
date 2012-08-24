<table width="280" cellpadding="2" cellspacing="2" style="text-align: left;">
	<div class="fiadorController-cotasAssociadas-dialog-excluir" id="fiadorController-cotasAssociadas-dialog-excluir" title="Cotas Associadas" style="display: none;">
		<p>Confirma esta Exclusão?</p>
	</div>
	<tr>
		<td width="46">Cota:</td>
		<td width="218">
			<input type="text" style="width: 80px; float: left; margin-right: 5px;" id="fiadorController-cotasAssociadas-numeroCota" maxlength="11" onblur="fiadorController.buscarNomeCota();"/>
		</td>
	</tr>
	<tr>
		<td>Nome:</td>
		<td><input type="text" style="width: 200px" id="fiadorController-cotasAssociadas-nomeCota" readonly="readonly"/></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><span class="bt_add"><a id="fiadorController-cotasAssociadas-adicionarCotaAssociacao">Incluir Novo</a></span></td>
	</tr>
</table>
<br />
<label><strong>Cotas Cadastradas</strong></label>
<br />
<table class="fiadorController-cotasAssociadasGrid"></table>