<head>
<title>Consulta de Notas</title>

<script language="javascript" type="text/javascript">

	function pesquisarNotas() {
		
	}

	function popup() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$("#dialog-novo").dialog({
			resizable : false,
			height : 370,
			width : 410,
			modal : true,
			buttons : {
				"Fechar" : function() {
					$(this).dialog("close");

				},
			}
		});
	};

	$(function() {
		$("#datepickerDe").datepicker({
			showOn : "button",
			buttonImage: "../../../images/calendar.gif",
			buttonImageOnly : true
		});
		$("#datepickerAte").datepicker({
			showOn : "button",
			buttonImage: "../../../images/calendar.gif",
			buttonImageOnly : true
		});
	});
</script>
<style type="text/css">
fieldset label {
	width: auto;
	margin-bottom: 0px !important;
}
</style>
</head>

<body>

	<div class="container">

		<div id="effect" style="padding: 0 .7em;"
			class="ui-state-highlight ui-corner-all">
			<p>
				<span style="float: left; margin-right: .3em;"
					class="ui-icon ui-icon-info">
				</span> 
				<b>Liberação de Encalhe < evento > com < status >.</b>
			</p>
		</div>

		<fieldset class="classFieldset">
			<legend> Pesquisar Nota </legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1"
				class="filtro">
				<tr>
					<td>Fornecedor:</td>
					<td>
						<select name="selectFornecedores" id="selectFornecedores" style="width: 250px;">
							<option selected="selected">Todos</option>
							<c:forEach items="${fornecedores}" var="fornecedor">
								<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
							</c:forEach>
						</select>
					</td>

					<td>Período</td>
					<td width="46">de:</td>
					<td width="120"><input name="datepickerDe" type="text"
						id="datepickerDe"
						style="width: 80px; float: left; margin-right: 5px;" /></td>
					<td align="center">Até</td>
					<td><input name="datepickerAte" type="text" id="datepickerAte"
						style="width: 80px; float: left; margin-right: 5px;" /></td>
				</tr>
				<tr>
					<td width="107">Tipo de Nota:</td>
					<td width="293">
					<select name="selectTiposNotaFiscal" id="selectTiposNotaFiscal" style="width: 250px;">
						<option selected="selected"></option>
						<c:forEach items="${tiposNotaFiscal}" var="tipoNotaFiscal">
							<option value="${tipoNotaFiscal.id}">${tipoNotaFiscal.descricao}</option>
						</c:forEach>
						
					</select></td>
					<td width="95"><label for="notaRecebida" style="margin:0px">Nota Recebida</label></td>
					<td colspan="2">
					<select name="selectNotaRecebida" id="selectNotaRecebida"
							style="width: 135px;">
							<option>Todos</option>
							<option>Sim</option>
							<option>Não</option>
					</select></td>
					<td width="31">&nbsp;</td>
					<td width="222">
						<span class="bt_pesquisar">
							<a href="javascript:;" onclick="pesquisarNotas();">Pesquisar</a>
						</span>
					</td>
				</tr>
			</table>

		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">
			<legend>Notas Cadastradas</legend>
			<div class="grids" style="display: none;">
				<table class="notasSemFisicoGrid"></table>
				<span class="bt_arquivo"><a href="javascript:;">Arquivo</a></span> <span
					class="bt_imprimir"><a href="javascript:;">Imprimir</a></span>
				</td>
			</div>
		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>
	</div>
	
</body>