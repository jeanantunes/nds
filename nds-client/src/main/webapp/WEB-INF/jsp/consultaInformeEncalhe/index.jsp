<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script
	src="${pageContext.request.contextPath}/scripts/jquery-upload/js/vendor/jquery.ui.widget.js"
	type="text/javascript"></script>
<script
	src="${pageContext.request.contextPath}/scripts/jquery-upload/js/jquery.iframe-transport.js"
	type="text/javascript"></script>
<script
	src="${pageContext.request.contextPath}/scripts/jquery-upload/js/jquery.fileupload.js"
	type="text/javascript"></script>
</head>
<body>
	<div id="dialog-imprimir" title="Imprimir Informe de Encalhe">
		<fieldset>
			<legend>Tipo de Impressão</legend>
			<table width="248" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="20"><input name="input" type="checkbox" value=""
						checked="checked" /></td>
					<td width="102">Frente</td>
					<td width="23"><input name="input2" type="checkbox" value="" /></td>
					<td width="103">Verso</td>
				</tr>
			</table>
		</fieldset>
		<br clear="all" /> <br />
		<fieldset>
			<legend>Escolha as colunas</legend>
			<table width="825" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="23"><input name="input11" type="checkbox" value="" /></td>
					<td width="119">Capa</td>
					<td width="27"><input name="input11" type="checkbox" value="" /></td>
					<td width="183">Código</td>
					<td width="21"><input name="input11" type="checkbox" value="" /></td>
					<td width="105">Produto</td>
					<td width="23"><input name="input11" type="checkbox" value="" /></td>
					<td width="101">Edição</td>
					<td width="23"><input name="input11" type="checkbox" value="" /></td>
					<td width="200">Chamada de Capa</td>
				</tr>
				<tr>
					<td><input name="input11" type="checkbox" value="" /></td>
					<td>Preço de Capa</td>
					<td><input name="input2" type="checkbox" value="" /></td>
					<td>Editor</td>
					<td><input name="input3" type="checkbox" value="" /></td>
					<td>Brinde</td>
					<td><input name="input4" type="checkbox" value="" /></td>
					<td>Data Lançamento</td>
					<td><input name="input5" type="checkbox" value="" /></td>
					<td>Data Recolhimento</td>
				</tr>

				<tr>
					<td><input name="input6" type="checkbox" value="" /></td>

					<td>Código Barras</td>

					<td><input name="input7" type="checkbox" value="" /></td>

					<td>Recolhimento Parcial/Final</td>

					<td>&nbsp;</td>

					<td>&nbsp;</td>

					<td>&nbsp;</td>

					<td>&nbsp;</td>

					<td>&nbsp;</td>

					<td>&nbsp;</td>
				</tr>
			</table>
		</fieldset>
	</div>
	<div id="dialog-detalhes" title="Visualizando Produto">
		<img src="../capas/revista-nautica-11.jpg" width="235" height="314"
			alt="" />
	</div>
	<div id="dialog-img" title="Nova Imagem">
		<p>Este Produto esta sem capa no momento, deseja incluir uma?</p>
		<p>
			<strong>Selecione a imagem desejada:</strong>
		</p>
		<input name="fileField" type="file" id="fileField" size="31" />
	</div>
	<fieldset class="classFieldset">
		<legend> Pesquisar Informe de Recolhimento</legend>
		<table width="950" border="0" cellpadding="2" cellspacing="1"
			class="filtro">
			<tr>
				<td width="68">Fornecedor:</td>
				<td width="210"><select name="select" id="select"
					style="width: 200px;">
						<option selected="selected"></option>
						<option>Todos</option>
						<option>Dinap</option>
						<option>FC</option>
				</select></td>

				<td colspan="3">Semana:</td>

				<td width="89"><input type="text" name="textfield"
					id="textfield" style="width: 70px;" /></td>

				<td colspan="3">Data Recolhimento:</td>

				<td width="272"><input type="text" name="datepickerDe"
					id="datepickerDe" style="width: 70px;" /></td>

				<td width="104"><span class="bt_pesquisar"><a
						href="javascript:;" onclick="mostrar();">Pesquisar</a></span></td>

			</tr>

		</table>



	</fieldset>

	<div class="linha_separa_fields">&nbsp;</div>



	<fieldset class="classFieldset">

		<legend>Informe de Recolhimentos Cadastrados</legend>

		<div class="grids" style="display: none;">



			<table class="consultaInformeEncalheGrid"></table>





			<span class="bt_novos" title="Imprimir"><a href="javascript:;"
				onclick="popup_impressao();"><img
					src="../images/ico_impressora.gif" hspace="5" border="0" alt="" />Imprimir</a></span>
		</div>













	</fieldset>

	<div class="linha_separa_fields">&nbsp;</div>









	<div></div>

</body>
</html>