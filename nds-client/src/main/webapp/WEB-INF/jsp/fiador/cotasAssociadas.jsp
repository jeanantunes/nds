<script type="text/javascript">
	$(function() {
		$(".cotasCadastradasGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 620,
				sortable : true,
				align : 'left'
			}, {
				display : '',
				name : 'sel',
				width : 30,
				sortable : true,
				align : 'center'
			} ],
			width : 770,
			height : 150
		});
	});
</script>
<table width="280" cellpadding="2" cellspacing="2"
	style="text-align: left;">
	<tr>
		<td width="46">Cota:</td>
		<td width="218"><input type="text"
			style="width: 80px; float: left; margin-right: 5px;" /><span
			class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
	</tr>
	<tr>
		<td>Nome:</td>
		<td><input type="text" style="width: 200px" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><span class="bt_add"><a href="javascript:;">Incluir
					Novo</a></span></td>
	</tr>
</table>
<br />
<label><strong>Cotas Cadastradas</strong></label>
<br />
<table class="cotasCadastradasGrid"></table>