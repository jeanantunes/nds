<head>
	<script language="javascript" type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/produto.js"></script>

	<script language="javascript" type="text/javascript">
	
		$(function() {
			$('input[id^="dataLancamento"]').datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
		});
		
		function mostrarGridConsulta() {
			var data = null;
			
			$.postJSON("<c:url value='/estoque/diferenca/consultarFaltasSobras' />",
					   data, exibirFaltasSobras);
		}
		
		function exibirFaltasSobras(result) {
			$(".grids").show();
			
			alert(result);
		}
		
	</script>
</head>

<body>
	<div id="effect" style="padding: 0 .7em;"
		class="ui-state-highlight ui-corner-all">
		<p>
			<span style="float: left; margin-right: .3em;"
				class="ui-icon ui-icon-info"></span> <b>Lançamento de Faltas e
				Sobras < evento > com < status >.</b>
		</p>
	</div>

	<fieldset class="classFieldset">
		<legend>Pesquisar Faltas e Sobras</legend>
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="59">Código:</td>
				<td colspan="3">
					<input type="text" name="codigo" id="codigo" style="width: 80px; float: left; margin-right: 5px;" />
					<span class="classPesquisar" title="Pesquisar Produto">
						<a href="javascript:;" onclick="pesquisarPorCodigoProduto();">&nbsp;</a>
					</span>
				</td>
				<td width="60">Produto:</td>
				<td width="220">
					<input type="text" name="produto" id="produto" style="width: 200px;"
					       onkeyup="pesquisarPorNomeProduto();" />
				</td>
				
				<td width="50" align="right">Edição:</td>
				<td width="90">
					<input type="text" style="width:70px;" name="edicao" id="edicao" maxlength="20"
						   onchange="validarNumEdicao();"/>
				</td>
				
				<td width="73">Fornecedor:</td>
				<td width="230" colspan="2">
					<select name="select8" id="select13" style="width: 200px;">
						<c:forEach var="fornecedor" items="${listaFornecedores}">
							<option value="${fornecedor.key}">${fornecedor.value}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>
		<table width="950" border="0" cellspacing="2" cellpadding="2"
			class="filtro">
			<tr>
				<td width="178">Período de Data Lançamento:</td>
				<td width="108">
					<input type="text" name="dataLancamentoDe" id="dataLancamentoDe" style="width: 80px;" />
				</td>
				<td width="33" align="center">Até</td>
				<td width="147">
					<input type="text" name="dataLancamentoAte" id="dataLancamentoAte" style="width: 80px;" />
				</td>
				<td width="134" align="right">Tipo de Diferença:</td>
				<td width="169">
					<select name="select9" id="select14" style="width: 120px;">
						<c:forEach var="tipoDiferenca" items="${listaTiposDiferenca}">
							<option value="${tipoDiferenca.key}">${tipoDiferenca.value}</option>
						</c:forEach>
					</select>
				</td>
				<td width="137"><span class="bt_pesquisar"><a
						href="javascript:;" onclick="mostrarGridConsulta();">Pesquisar</a> </span></td>
			</tr>
		</table>
	</fieldset>
	<div class="linha_separa_fields">&nbsp;</div>

	<fieldset class="classFieldset">
		<legend>Faltas e Sobras Cadastradas</legend>
		<div class="grids" style="display: none;">
			<table class="consultaFaltasSobrasGrid"></table>
			<span class="bt_novos" title="Gerar Arquivo"><a
				href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png"
					hspace="5" border="0" />Arquivo</a> </span> <span class="bt_novos"
				title="Imprimir"><a href="javascript:;"><img
					src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5"
					border="0" />Imprimir</a> </span>
		</div>

	</fieldset>
	<div class="linha_separa_fields">&nbsp;</div>

	<script>
	
		$(".consultaFaltasSobrasGrid").flexigrid({
			//url : '../xml/consulta_faltas_sobras-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Data',
				name : 'data',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Código',
				name : 'codigo',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 170,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Tipo de Diferença',
				name : 'tipoDiferenca',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nota',
				name : 'nota',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Exemplar',
				name : 'exemplar',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Status',
				name : 'status',
				width : 80,
				sortable : true,
				align : 'center'
			} ],
			sortname : "data",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	</script>
</body>