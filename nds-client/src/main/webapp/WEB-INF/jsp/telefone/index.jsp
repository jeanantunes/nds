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
		
		function popularGrid() {
			
			$("#telefonesGrid").flexigrid({
				//preProcess: processarResultado,
				url : '<c:url value="/cadastro/telefone/pesquisarTelefones" />',
				dataType : 'json',
				colModel : [  {
					display : 'Tipo Telefone',
					name : 'tipotelefone',
					width : 165,
					sortable : true,
					align : 'left'
				},{
					display : 'DDD',
					name : 'ddd',
					width : 100,
					sortable : true,
					align : 'center'
				}, {
					display : 'Número',
					name : 'numero',
					width : 150,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ramal',
					name : 'ramal',
					width : 100,
					sortable : true,
					align : 'center'
				}, {
					display : 'Principal',
					name : 'principal',
					width : 100,
					sortable : true,
					align : 'center'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : true,
					align : 'center'
				}],
				width : 770,
				height : 150
			});
		}
		
		$(function(){
			popularGrid();
		});
	</script>
</head>

<div class="container">
	<div id="manutencaoTelefones" style="display:none" title="Telefones">
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
				<td>Ramal: </td>
				<td>
					<input type="text" style="width:40px; float:left;" />
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
				<td><span class="bt_add"><a href="javascript:;" onclick="alert('ela me bagunça');">Incluir Novo</a></span></td>
			</tr>
		</table>
		
		<br />
		
		<label><strong>Telefones Cadastrados</strong></label>
		
		<br />
		
		<table id="telefonesGrid"></table>
	</div>
</div>