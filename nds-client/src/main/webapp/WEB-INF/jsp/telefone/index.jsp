<head>
	<script type="text/javascript">
		function popup() {
			
			$("#manutencaoTelefones").dialog({
				resizable: false,
				height:500,
				width:840,
				modal : true
			});
		};
	</script>
</head>

<div class="container">
	<div id="manutencaoTelefones" style="display:none" title="Cadastro Telefones">
		<table width="280" cellpadding="2" cellspacing="2" style="text-align:left ">
			<tr>
				<td width="72">Tipo:</td>
				<td width="192">
					<select onchange="opcaoTel(this.value);" style="width:174px;">
						<option value="" selected="selected">Selecione</option>
						<option value="1">Comercial</option>
						<option value="">Celular</option>
						<option value="1">Fax</option>
						<option value="">Residencial</option>
						<option value="2">Nextel</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>Telefone: </td>
				<td>
					<input type="text" style="width:40px" />-<input type="text" style="width:110px" />
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div id="divOpcao1" style="display:none;">
						<div style="width:80px; float:left;">Ramal:</div>
						<input type="text" style="width:40px; float:left;" />
					</div>
					
					<div id="divOpcao2" style="display:none;">
						<div style="width:80px; float:left;;">ID:</div>
						<input type="text" style="width:40px; float:left;" />
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<label for="principal1">Principal:</label>
				</td>
				<td class="complementar">
					<input type="checkbox" name="principal1" id="principal1" />
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><span class="bt_add"><a href="javascript:;" onclick="popup();">Incluir Novo</a></span></td>
			</tr>
		</table>
		
		<br />
		
		<label><strong>Telefones Cadastrados</strong></label>
		
		<br />
		
		<table class="telefonesGrid"></table>
	</div>
</div>