<script type="text/javascript">
	$(function() {
		$(".imoveisGrid").flexigrid({
			preProcess: processarResultadoGarantias,
			dataType : 'json',
			colModel : [ {
				display : 'Descrição',
				name : 'garantia.descricao',
				width : 510,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'garantia.valor',
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
			height : 150,
			sortname : "garantia.descricao",
			sortorder : "asc",
			disableSelect: true
		});
		
		$(".imoveisGrid").flexOptions({url: "<c:url value='/cadastro/fiador/obterGarantiasFiador'/>"});
		
		$("#valorGarantia").numeric();
		$("#valorGarantia").priceFormat({
		    centsSeparator: ',',
		    thousandsSeparator: '.',
		    centsLimit: 4
		});
		
	});
	
	function carregarGarantias(){
		$.postJSON("<c:url value='/cadastro/fiador/obterGarantiasFiador' />", null, 
			function(result) {
				$(".imoveisGrid").flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});	
				
				$("#botaoAddEditarGarantia").text("Incluir Novo");
				
				$("#valorGarantia").val("");
				$("#descricaoGarantia").val("");
			},
			null,
			true
		);
	}
	
	function processarResultadoGarantias(data){
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
			
			data.rows[i].cell[lastIndex] = getActionsGarantia(data.rows[i].id);
		}

		$('.imoveisGrid').show();
		
		if (data.result){
			return data.result;
		}
		return data;
	}
	
	function getActionsGarantia(idGarantia) {

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
		var data = "garantia.valor=" + $("#valorGarantia").floatValue() + "&" +
        		   "garantia.descricao=" + $("#descricaoGarantia").val() + "&" +
        		   "referencia=" + $("#referenciaGarantia").val();
		
		$.postJSON("<c:url value='/cadastro/fiador/adicionarGarantia' />", data, 
			function(result) {
				$(".imoveisGrid").flexAddData({
					page: 1, total: 1, rows: result.rows
				});	
				
				$("#botaoAddEditarGarantia").text("Incluir Novo");
				
				$("#valorGarantia").val("");
				$("#descricaoGarantia").val("");
				$("#referenciaGarantia").val("");
			},
			null,
			true
		);
	}
	
	function editarGarantia(referencia){
		$.postJSON("<c:url value='/cadastro/fiador/editarGarantia' />", "referencia=" + referencia, 
			function(result) {
				
				$("#botaoAddEditarGarantia").text("Editar");
				
				$("#valorGarantia").val(result.valor);
				$("#descricaoGarantia").val(result.descricao);
				$("#referenciaGarantia").val(referencia);
			},
			null,
			true
		);
	}
	
	function removerGarantia(referencia){
		$("#dialog-excluir-garantia").dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$(this).dialog("close");
					
					$.postJSON("<c:url value='/cadastro/fiador/excluirGarantia' />", "referencia=" + referencia, 
						function(result) {
							
							$(".imoveisGrid").flexAddData({
								page: 1, total: 1, rows: result.rows
							});
							
							$("#botaoAddEditarGarantia").text("Incluir Novo");
							
							$("#valorGarantia").val("");
							$("#descricaoGarantia").val("");
							$("#referenciaGarantia").val("");
						},
						null,
						true
					);
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			}
		});
		
		$("#dialog-excluir-garantia").show();
	}
	
	function limparCamposGarantias(){
		$("#valorGarantia").val("");
		$("#descricaoGarantia").val("");
	}
</script>

<div id="dialog-excluir-garantia" title="Garantias" style="display: none;">
	<p>Confirma esta Exclusão?</p>
</div>
<table width="750" cellpadding="2" cellspacing="2"
	style="text-align: left; display: s;" class="fiadorPF">
	<tr>
		<td>Valor R$:</td>
		<td><input type="text" style="width: 100px" id="valorGarantia" maxlength="255"/></td>
	</tr>
	<tr>
		<td>Descrição:</td>
		<td>
			<textarea name="textarea2" rows="4" style="width: 600px" id="descricaoGarantia" maxlength="255"></textarea>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>
			<span class="bt_add">
				<a href="javascript:adicionarEditarGarantia();" id="botaoAddEditarGarantia">Incluir Novo</a>
			</span>
		</td>
	</tr>
</table>
<br />
<label><strong>Garantias Cadastradas</strong></label>
<br />
<table class="imoveisGrid"></table>
<input type="hidden" id="referenciaGarantia"/>