<!-- 

	VIEW REFERENTE A EMS0081
	
 -->

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>meuConteudo</title>

<script type="text/javascript">

function pesquisarExtratoEdicao(){
	
	$(".extratoEdicaoGrid").flexigrid({
		url : '<c:url value="/estoque/extratoEdicao/toJSon"/>',
		dataType : 'json',
		colModel : [ {
			display : 'Data',
			name : 'data',
			width : 120,
			sortable : true,
			align : 'center'
		}, {
			display : 'Movimento',
			name : 'movimento',
			width : 370,
			sortable : true,
			align : 'left'
		}, {
			display : 'Entrada',
			name : 'entrada',
			width : 120,
			sortable : true,
			align : 'center'
		}, {
			display : 'Saída',
			name : 'saida',
			width : 120,
			sortable : true,
			align : 'center'
		}, {
			display : 'Parcial',
			name : 'parcial',
			width : 120,
			sortable : true,
			align : 'center'
		}],
		sortname : "codigo",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 180
	});
	
	$(".grids").show();
	
}




	
</script>

</head>

<body>

	<div class="container">

		<div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all">
			<p>
				<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span> 
				<b> 
					Chamadão < evento > com < status >. 
				</b>
			</p>
		</div>

		<fieldset class="classFieldset">
		
			<legend> Extrato de Edição </legend>
			
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				
				<tr>
					<td width="94">Código:</td>
					<td width="118"><input type="text" name="cota" id="cota"
						style="width: 70px; float: left; margin-right: 5px;" /><span
						class="classPesquisar"><a href="javascript:;">&nbsp;</a>
					</span>
					</td>
					<td width="76">Produto:</td>
					<td width="162">&nbsp;</td>
					<td width="75">&nbsp;</td>
					<td width="256">&nbsp;</td>
					<td width="133">&nbsp;</td>
				</tr>
				
				<tr>
					<td>Preço da Capa:</td>
					<td><input type="text" name="datepickerDe3" id="datepickerDe3"
						style="width: 70px; float: left; margin-right: 5px;"
						disabled="disabled" />
					</td>
					<td>Edição:</td>
					<td><input type="text" name="datepickerDe2" id="datepickerDe2"
						style="width: 70px; float: left; margin-right: 5px;" />
					</td>
					<td>Fornecedor:</td>
					<td><select name="select" id="select" style="width: 220px;">
							<option>Todos</option>
							<option>Dinap</option>
							<option>FC</option>
					</select>
					</td>
					<td><span class="bt_pesquisar"><a href="javascript:;"
							onclick="pesquisarExtratoEdicao();">Pesquisar</a>
					</span>
					</td>
				</tr>
				
			</table>

		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">
		
			<legend>Extrato de Edição</legend>
		
			<div class="grids" style="display: none;">
			
				<table class="extratoEdicaoGrid"></table>
				
				<table width="950" border="0" cellspacing="2" cellpadding="2">
				
					<tr>
						<td width="746"><span class="bt_arquivo"><a
								href="javascript:;">Arquivo</a>
						</span> <span class="bt_imprimir"><a href="javascript:;">Imprimir</a>
						</span>
						</td>
						<td width="58"><strong>Saldo:</strong>
						</td>
						<td width="126">999.999</td>
					</tr>
					
				</table>
				
			</div>


		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

	</div>

</body>

</html>