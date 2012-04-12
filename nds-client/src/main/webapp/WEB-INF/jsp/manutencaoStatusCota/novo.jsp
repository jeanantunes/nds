<div id="dialog-novo" title="Alteração de Status">

	<div class="effectDialog ui-state-highlight ui-corner-all" 
		 style="display: none; position: absolute; z-index: 2000; width: 580px;">
		 
		<p>
			<span style="float: left;" class="ui-icon ui-icon-info"></span>
			<b class="effectDialogText"></b>
		</p>
	</div>

	<table width="535" border="0" cellpadding="2" cellspacing="1" class="filtro">
		<tr>
			<td width="64">Cota:</td>
			<td id="numeroCotaNovo" width="173"></td>
			<td width="46">Box:</td>
			<td id="boxNovo" width="231"></td>
		</tr>
		<tr>
			<td>Nome:</td>
			<td id="novoNomeCota"></td>
			<td>Status:</td>
			<td>
				<select name="novoStatusCota" id="novoStatusCota" style="width: 230px;">
					<option selected="selected">Selecione...</option>
					<c:forEach var="statusCota" items="${listaSituacoesStatusCota}">
						<option value="${statusCota.key}">${statusCota.value}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td>Período:</td>
			<td>
				<input name="novaDataInicialStatusCota" 
					   type="text" 
					   id="novaDataInicialStatusCota" 
					   style="width: 80px; float: left; margin-right: 5px;" />
			</td>
			<td>Até:</td>
			<td>
				<input name="novaDataFinalStatusCota" 
					   type="text" 
					   id="novaDataFinalStatusCota" 
					   style="width: 80px; float: left; margin-right: 5px;" />
			</td>
		</tr>
		<tr>
			<td>Motivo:</td>
			<td colspan="3">
				<select name="novoMotivo" id="novoMotivo" style="width: 150px;">
					<option selected="selected">Selecione...</option>
					<c:forEach var="motivoStatusCota" items="${listaMotivosStatusCota}">
						<option value="${motivoStatusCota.key}">${motivoStatusCota.value}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td>Descrição:</td>
			<td colspan="3">
				<textarea maxlength="255" name="novaDescricao" rows="4" id="novaDescricao" style="width: 450px;"></textarea>
			</td>
		</tr>
	</table>
</div>