<script type="text/javascript">
	$(function() {
		$(".imoveisGrid").flexigrid({
			preProcess: processarResultado,
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
				sortable : false,
				align : 'center'
			} ],
			width : 770,
			height : 150
		});
	});
	
	function pesquisarGarantias(){
		$.postJSON("<c:url value='/cadastro/fiador/obterGarantiasFiador' />", null, 
				function(result) {
					$(".imoveisGrid").flexAddData({
						page: 1, total: 1, rows: result.rows
					});	
					
					$("#botaoAddEditar").text("Incluir Novo");
					
					$("#valorGarantia").val("");
					$("#descricaoGarantia").val("");
				},
				null,
				true
		);
	}
	
	function processarResultado(data){
		if (data.mensagens) {

			exibirMensagemDialog(
				data.mensagens.tipoMensagem, 
				data.mensagens.listaMensagens
			);
			
			return;
		}
		
		if (data.result){
			data.rows = data.result.rows;
		}
		
		var i;

		for (i = 0 ; i < data.rows.length; i++) {

			var lastIndex = data.rows[i].cell.length;
			
			data.rows[i].cell[lastIndex - 1] =					
				data.rows[i].cell[lastIndex - 1] == "true" 
						? '<img src="/nds-client/images/ico_check.gif" border="0px"/>'
						: '&nbsp;';

			data.rows[i].cell[lastIndex] = getActions(data.rows[i].id);
		}

		$('.imoveisGrid').show();
		
		if (data.result){
			return data.result;
		}
		return data;
	}
	
	function getActions(idGarantia) {

		return '<a href="javascript:;" onclick="editarGarantia(' + idGarantia + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Editar Garantia">' +
				'<img src="/nds-client/images/ico_editar.gif" border="0px"/>' +
				'</a>' +
				'<a href="javascript:;" onclick="removerGarantia(' + idGarantia + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Excluir Garantia">' +
				'<img src="/nds-client/images/ico_excluir.gif" border="0px"/>' +
				'</a>';
	}
	
	function adicionarEditarGarantia(){
		var data = "garantia.valor=" + $("#valorGarantia").val() + "&" +
        		   "garantia.descricao=" + $("#descricaoGarantia").val();
		
		$.postJSON("<c:url value='/cadastro/fiador/adicionarGarantia' />", data, 
				function(result) {
					$(".imoveisGrid").flexAddData({
						page: 1, total: 1, rows: result.rows
					});	
					
					$("#botaoAddEditar").text("Incluir Novo");
					
					$("#valorGarantia").val("");
					$("#descricaoGarantia").val("");
				},
				null,
				true
		);
	}
	
	function editarGarantia(idGarantia){
		
	}
	
	function removerGarantia(idGarantia){
		
	}
</script>
<table width="750" cellpadding="2" cellspacing="2"
	style="text-align: left; display: s;" class="fiadorPF">
	<tr>
		<td>Valor R$:</td>
		<td><input type="text" style="width: 100px" id="valorGarantia" /></td>
	</tr>
	<tr>
		<td>Descrição:</td>
		<td>
			<textarea name="textarea2" rows="4" style="width: 600px" id="descricaoGarantia"></textarea>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>
			<span class="bt_add" id="botaoAddEditar"><a href="javascript:adicionarEditarGarantia();">Incluir Novo</a></span>
		</td>
	</tr>
</table>
<br />
<label><strong>Garantias Cadastradas</strong></label>
<br />
<table class="imoveisGrid"></table>