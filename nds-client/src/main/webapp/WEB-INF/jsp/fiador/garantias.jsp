<script type="text/javascript">
	$(function() {
		$(".imoveisGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Descrição',
				name : 'descricao',
				width : 510,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 130,
				sortable : true,
				align : 'right'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			} ],
			width : 770,
			height : 150
		});
	});
</script>
<table width="750" cellpadding="2" cellspacing="2"
	style="text-align: left; display: s;" class="fiadorPF">
	<tr>
		<td>Valor R$:</td>
		<td><input type="text" style="width: 100px" /></td>
	</tr>
	<tr>
		<td>Descrição:</td>
		<td><textarea name="textarea2" rows="4" style="width: 600px">
</textarea></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><span class="bt_add"><a href="javascript:;">Incluir
					Novo</a></span></td>
	</tr>
</table>
<br />
<label><strong>Garantias Cadastradas</strong></label>
<br />
<table class="imoveisGrid"></table>