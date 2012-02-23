<!-- 

	VIEW REFERENTE A EMS0081
	
 -->

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>meuConteudo</title>

<script type="text/javascript">

	function fazerTratamento(data) {
		
		alert(data);
		
		return data;
	}

	function pesquisarExtratoEdicao() {

		$(".extratoEdicaoGrid")
				.flexigrid(
						{
							url : '<c:url value="/estoque/extratoEdicao/pesquisaExtratoEdicao"/>',
							dataType : 'json',
							preProcess: fazerTratamento,
							colModel : [ 
							    {
								display : 'ID',
								name : 'id',
								width : 120,
								sortable : true,
								align : 'center'
							}, {
								display : 'Data',
								name : 'dataInclusao',
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
							} ],
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

		<div id="effect" style="padding: 0 .7em;"
			class="ui-state-highlight ui-corner-all">
			<p>
				<span style="float: left; margin-right: .3em;"
					class="ui-icon ui-icon-info"></span> <b>Chamadão < evento > com
					< status >.</b>
			</p>
		</div>

		<fieldset class="classFieldset">
			<legend> Extrato de Edição </legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1"
				class="filtro">
				<tr>
					<td width="96">Código:</td>
					<td width="123"><input type="text" name="cota" id="cota"
						style="width: 70px; float: left; margin-right: 5px;" /><span
						class="classPesquisar"><a href="javascript:;">&nbsp;</a>
					</span>
					</td>
					<td width="85">Produto:</td>
					<td width="236"><input type="text" name="lstProduto"
						id="lstProduto"
						style="width: 220px; float: left; margin-right: 5px;" />
					</td>
					<td width="60">Edição:</td>
					<td width="186"><select name="select" id="select"
						style="width: 100px;">
					</select>
					</td>
					<td width="128">&nbsp;</td>
				</tr>
				<tr>
					<td>Preço da Capa:</td>
					<td><input type="text" name="datepickerDe3" id="datepickerDe3"
						style="width: 70px; float: left; margin-right: 5px;"
						disabled="disabled" />
					</td>
					<td>Fornecedor:</td>
					<td><input type="text" name="textfield" id="textfield"
						style="width: 220px;" disabled="disabled" />
					</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
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
						<td width="689"><span class="bt_novos" title="Gerar Arquivo"><a
								href="javascript:;"><img src="../images/ico_excel.png"
									hspace="5" border="0" />Arquivo</a>
						</span> <span class="bt_novos" title="Imprimir"><a
								href="javascript:;"><img src="../images/ico_impressora.gif"
									alt="Imprimir" hspace="5" border="0" />Imprimir</a>
						</span>
						</td>
						<td width="115"><strong>Saldo em Estoque:</strong>
						</td>
						<td width="126">0</td>
					</tr>
				</table>
			</div>


		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

	</div>

</body>

</html>