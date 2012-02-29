<head>
<title>Consulta de Notas</title>
<script language="javascript" type="text/javascript">
	
		var reloadFlex = false;

		function processarResultadoConsultaNF(data) {

			var i;

			for (i = 0 ; i < data.rows.length; i++) {

				var lastIndex = data.rows[i].cell.length - 1;

				data.rows[i].cell[lastIndex-1] = 
					'<a href="javascript:;" onclick="popup(' + data.rows[i].cell[lastIndex] + ')" style="cursor:pointer" style="border:none">' +
					'<img src="${pageContext.request.contextPath}/images/ico_detalhes.png"/>' +
					'</a>';
			}

			if (data.mensagens) {

				exibirMensagem(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens
				);
				
				$(".grids").hide();
			}

			return data;
		} 

		function pesquisarNotas() { 

			var formData = $('#formPesquisaNotas').serializeArray();

			$("#notasSemFisicoGrid").flexigrid({
				preProcess: processarResultadoConsultaNF,
				url : '<c:url value="/estoque/consultaNotas/pesquisarNotas" />',
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

			if (!reloadFlex) {

				reloadFlex = true;

				$(".grids").show();

			} else {

				formData = $('#formPesquisaNotas').serializeArray();
				
				$("#notasSemFisicoGrid").flexOptions({url : '<c:url value="/estoque/consultaNotas/pesquisarNotas" />', params: formData});
				
				$("#notasSemFisicoGrid").flexReload();
			}
		}
		
		function pesquisarDetalhesNota(idNota) {

			$("#notasSemFisicoDetalheGrid").flexigrid({
				url : '<c:url value="/estoque/consultaNotas/pesquisarDetalhesNotaFiscal" />',
				preProcess: montarGridComRodape,
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
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numeroEdicao',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Preço Capa R$',
					name : 'precoCapa',
					width : 80,
					sortable : true,
					align : 'right'
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
				}, {
					display : 'Total R$',
					name : 'total',
					width : 60,
					sortable : true,
					align : 'right'
				}],
				width : 600,
				height : 200,
				params: [{ name: 'idNota', value: idNota }],
				resizable:false
			});
			
			if (!reloadFlex) {

				reloadFlex = true;

				$("#dialog-novo").show();
			}
			
			$("#notasSemFisicoDetalheGrid").flexOptions({url : '<c:url value="/estoque/consultaNotas/pesquisarDetalhesNotaFiscal" />', 
				params: [{ name: 'idNota', value: idNota }]});

			$("#notasSemFisicoDetalheGrid").flexReload();
		}
		
		function montarGridComRodape(data) {

			var jsonData = jQuery.toJSON(data);

			var result = jQuery.evalJSON(jsonData);

			$("#totalExemplares").html(result.totalExemplares);
			$("#totalSumarizado").html("R$ " + result.totalSumarizado);

			return result.tableModel;
		}
		
		function popup(idNota) {

			$("#dialog-novo").dialog({
				resizable: false,
				height:370,
				width:630,
				modal : true,
				buttons : {
					"Fechar" : function() {
						$(this).dialog("close");
					},
				}
			});

			pesquisarDetalhesNota(idNota);
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
		<br />

		<table width="569" border="0" cellspacing="2" cellpadding="2">
	      <tr style="font-size:11px;">
	        <td width="275" align="right"><strong>Total:</strong></td>
	        <td width="106" align="right">
	        	<span id="totalExemplares"></span>
	        </td>
	        <td width="168" align="right"> 
	        	<span id="totalSumarizado"></span>
	        </td>
	      </tr>
	    </table>
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
								<option selected="selected" value="-1">Todos</option>
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
							<option selected="selected" value="-1"></option>
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