<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/telefone.js"></script>
	
	<script type="text/javascript">
		
		var ${param.tela} = new Telefone('${param.tela}');
		
	</script>
	
	
</head>
	<div id="${param.tela}manutencaoTelefones" title="Telefones">
		
		<div id="${param.tela}dialog-excluir" title="Telefones" style="display: none;">
			<p>Confirma esta Exclusão?</p>
		</div>
		<table width="280" cellpadding="2" cellspacing="2" style="text-align:left ">
			<tr>
				<td width="72">Tipo:</td>
				<td width="192">
					<select onchange="${param.tela}.opcaoTel(this.value, 'trRamalId', 'lblRamalId', 'ramal');" style="width:174px;" id="${param.tela}tipoTelefone">
						<option value="" selected="selected">Selecione</option>
						<option value="COMERCIAL">Comercial</option>
						<option value="CELULAR">Celular</option>
						<option value="FAX">Fax</option>
						<option value="RESIDENCIAL">Residencial</option>
						<option value="RADIO">Rádio</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>Telefone: </td>
				<td>
					<input type="text" style="width:40px" id="${param.tela}ddd" maxlength="255" />-<input type="text" style="width:110px" id="${param.tela}numeroTelefone" maxlength="255"/>
				</td>
			</tr>
			<tr id="${param.tela}trRamalId">
				<td id="${param.tela}lblRamalId">Ramal: </td>
				<td>
					<input type="text" style="width:40px; float:left;" id="${param.tela}ramal" maxlength="255"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="principal1">Principal:</label>
				</td>
				<td class="complementar">
					<input type="checkbox" name="principal1" id="${param.tela}telefonePrincipal" />
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><span class="bt_add"><a href="javascript:;" onclick="${param.tela}.adicionarTelefone();" id="${param.tela}botaoAddEditar">Incluir Novo</a></span></td>
			</tr>
		</table>
		
		<br />
		
		<label><strong>Telefones Cadastrados</strong></label>
		
		<br />
		
		<table id="${param.tela}telefonesGrid"></table>
		
		<br/>
		
		<input type="hidden" id="${param.tela}referenciaHidden"/>
</div>