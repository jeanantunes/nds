<head>
<title>Consulta de Notas</title>
<script language="javascript" type="text/javascript">

		function pesquisarNotas() { 

			var formData = $('#formPesquisaNotas').serializeArray();
			
			$("#notasSemFisicoGrid").flexigrid({
				url : '<c:url value="/consultaNotas/pesquisarNotas" />',
				dataType : 'json',
				colModel : [ {
					display : 'Número',
					name : 'numero',
					width : 120,
					sortable : true,
					align : 'center'
				}, {
					display : 'Data Emissao',
					name : 'dataEmissao',
					width : 370,
					sortable : true,
					align : 'left'
				}, {
					display : 'Data Expedicao',
					name : 'dataExpedicao',
					width : 120,
					sortable : true,
					align : 'center'
				}, {
					display : 'Tipo Nota Fiscal',
					name : 'descricao',
					width : 120,
					sortable : true,
					align : 'center'
				}, {
					display : 'Fornecedor',
					name : 'razaoSocial',
					width : 120,
					sortable : true,
					align : 'center'
				}, {
					display : 'Nota Recebida',
					name : 'statusNotaFiscal',
					width : 120,
					sortable : true,
					align : 'center'
				}],
				sortname : "codigo",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				params: formData,
				showTableToggleBtn : true,
				width : 960,
				height : 180
			});
			
			$(".grids").show();
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
				buttonImageOnly : true,
				dateFormat: 'dd/mm/yy',
				defaultDate: new Date()
			});
			$("#datepickerAte").datepicker({
				showOn : "button",
				buttonImage: "../../../images/calendar.gif",
				buttonImageOnly : true,
				dateFormat: 'dd/mm/yy',
				defaultDate: new Date()
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

		<form name="formPesquisaNotas" id="formPesquisaNotas">

			<fieldset class="classFieldset">
				<legend> Pesquisar Nota </legend>
				<table width="950" border="0" cellpadding="2" cellspacing="1"
					class="filtro">
					<tr>
						<td>Fornecedor:</td>
						<td>
							<select name="selectFornecedores" id="selectFornecedores" style="width: 250px;">
								<option selected="selected" value="-1">Todos</option>
								<c:forEach items="${fornecedores}" var="fornecedor">
									<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
								</c:forEach>
							</select>
						</td>
	
						<td>Período</td>
						<td width="46">de:</td>
						<td width="120">
							<input name="dataInicio" type="text" id="datepickerDe"
								   style="width: 80px; float: left; margin-right: 5px;" /></td>
						<td align="center">Até</td>
						<td>
							<input name="dataFim" type="text" id="datepickerAte"
							 	   style="width: 80px; float: left; margin-right: 5px;" />
						</td>
					</tr>
					<tr>
						<td width="107">Tipo de Nota:</td>
						<td width="293">
						<select name="idTipoNotaFiscal" id="selectTiposNotaFiscal" style="width: 250px;">
							<option selected="selected"></option>
							<c:forEach items="${tiposNotaFiscal}" var="tipoNotaFiscal">
								<option value="${tipoNotaFiscal.id}">${tipoNotaFiscal.descricao}</option>
							</c:forEach>
							
						</select></td>
						<td width="95"><label for="notaRecebida" style="margin:0px">Nota Recebida</label></td>
						<td colspan="2">
						<select name="isNotaRecebida" id="selectNotaRecebida"
								style="width: 135px;">
								<option value="-1">Todos</option>
								<option value="1">Sim</option>
								<option value="0">Não</option>
						</select></td>
						<td width="31">&nbsp;</td>
						<td width="222">
							<span class="bt_pesquisar">
								<a href="javascript:;" onclick="pesquisarNotas()">Pesquisar</a>
							</span>
						</td>
					</tr>
				</table>
			</fieldset>
		</form>
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">
			<legend>Notas Cadastradas</legend>
			<div class="grids" style="display: none;">
				<table class="notasSemFisicoGrid" id="notasSemFisicoGrid"></table>
				<span class="bt_arquivo"><a href="javascript:;">Arquivo</a></span> <span
					class="bt_imprimir"><a href="javascript:;">Imprimir</a>
				</span>
			</div>
		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>
	</div>
	
</body>