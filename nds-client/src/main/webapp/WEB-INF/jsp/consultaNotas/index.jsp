<head>
<title>Consulta de Notas</title>
<script language="javascript" type="text/javascript">

		function adicionarAction(data) {

			var i;

			for (i = 0 ; i < data.rows.length; i++) {

				var lastIndex = data.rows[i].cell.length - 1;

				data.rows[i].cell[lastIndex-1] = 
					'<a href="javascript:;" onclick="popup()" style="cursor:pointer" style="border:none">' +
					'<img src="${pageContext.request.contextPath}/images/ico_detalhes.png"/>' +
					'<input type="hidden" name="idNota" value="' + data.rows[i].cell[lastIndex] + '"/>' +
					'</a>';
			}

			return data;
		} 

		function pesquisarNotas() { 

			var formData = $('#formPesquisaNotas').serializeArray();
			
			$("#notasSemFisicoGrid").flexigrid({
				preProcess: adicionarAction,
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
					width : 120,
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
				}, {
					display : "Ação",
					name : 'acao',
					width : 120,
					sortable : true,
					align : 'center'
				}],
				sortname : "numero",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				params: formData,
				showTableToggleBtn : true,
				width : 960,
				height : 180,
				singleSelect: true
			});

			if ($(".grids").css('display') != 'none') {

				formData = $('#formPesquisaNotas').serializeArray();
				
				$("#notasSemFisicoGrid").flexOptions({url : '<c:url value="/consultaNotas/pesquisarNotas" />', params: formData});
				
				$("#notasSemFisicoGrid").flexReload();
			} else {

				$(".grids").show();
			}
		}
		
		function pesquisarDetalhesNota() {
			
			var idNota = $('input[name="idNota"]').val();

			$("#notasSemFisicoDetalheGrid").flexigrid({
				url : '<c:url value="/consultaNotas/pesquisarDetalhesNotaFiscal" />',
				dataType : 'json',
				colModel : [ {
					display : 'Código',
					name : 'codigoItem',
					width : 40,
					sortable : true,
					align : 'left'
				},{
					display : 'Produto',
					name : 'nomeProduto',
					width : 70,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numeroEdicao',
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : 'Exemplares',
					name : 'quantidadeExemplares',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Sobras / Faltas',
					name : 'sobrasFaltas',
					width : 80,
					sortable : true,
					align : 'center'
				}],
				width : 385,
				height : 180,
				params: { idNota: idNota },
				resizable:false
			});

		}

		function popup() {

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

			pesquisarDetalhesNota();
		};

		$(function() {
			$("#datepickerDe").datepicker({
				showOn : "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly : true,
				dateFormat: 'dd/mm/yy',
				defaultDate: new Date()
			});
			$("#datepickerAte").datepicker({
				showOn : "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly : true,
				dateFormat: 'dd/mm/yy',
				defaultDate: new Date()
			});
		});
	</script>

<style type="text/css">
fieldset label 
{
	width: auto;
	margin-bottom: 0px !important;
}

.ui-datepicker 
{
	z-index: 1000 !important;		
}

.ui-datepicker-today a
{
	display:block !important;
}
</style>

</head>

<body>
	<div id="dialog-novo" title="Detalhes da Nota">
	     
	    <table id="notasSemFisicoDetalheGrid" class="notasSemFisicoDetalheGrid"></table>
	
	</div>
	
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
							<select name="filtroConsultaNotaFiscal.idFornecedor" id="selectFornecedores" style="width: 250px;">
								<option selected="selected">Todos</option>
								<c:forEach items="${fornecedores}" var="fornecedor">
									<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
								</c:forEach>
							</select>
						</td>
	
						<td>Período</td>
						<td width="46">de:</td>
						<td width="120">
							<input name="filtroConsultaNotaFiscal.periodo.dataInicial" type="text" id="datepickerDe"
								   style="width: 80px; float: left; margin-right: 5px;" /></td>
						<td align="center">Até</td>
						<td>
							<input name="filtroConsultaNotaFiscal.periodo.dataFinal" type="text" id="datepickerAte"
							 	   style="width: 80px; float: left; margin-right: 5px;" />
						</td>
					</tr>
					<tr>
						<td width="107">Tipo de Nota:</td>
						<td width="293">
						<select name="filtroConsultaNotaFiscal.idTipoNotaFiscal" id="selectTiposNotaFiscal" style="width: 250px;">
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