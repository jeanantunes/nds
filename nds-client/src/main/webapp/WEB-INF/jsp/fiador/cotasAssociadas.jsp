<script type="text/javascript">
	$(function() {
		$(".cotasAssociadasGrid").flexigrid({
			preProcess: processarResultadoCotasAssociadas,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 620,
				sortable : true,
				align : 'left'
			}, {
				display : '',
				name : 'sel',
				width : 30,
				sortable : false,
				align : 'center'
			} ],
			width : 770,
			height : 150,
			sortname : "numeroCota",
			sortorder : "asc",
			disableSelect: true
		});
		
		$("#numeroCota").numeric();
		
		$(".cotasAssociadasGrid").flexOptions({url: "<c:url value='/cadastro/fiador/obterAssociacoesCotaFiador'/>"});
	});
	
	function processarResultadoCotasAssociadas(data){
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

			data.rows[i].cell[lastIndex] = getActionCotaCadastrada(data.rows[i].id);
		}

		$('.cotasAssociadasGrid').show();
		
		if (data.result){
			return data.result;
		}
		return data;
	}
	
	function getActionCotaCadastrada(referencia){
		return '<a href="javascript:;" onclick="removerAssociacaoCota(' + referencia + ')" ' +
		' style="cursor:pointer;border:0px;margin:5px" title="Excluir Associação">' +
		'<img src="/nds-client/images/ico_excluir.gif" border="0px"/>' +
		'</a>';
	}
	
	function carregarCotasAssociadas(){
		$.postJSON("<c:url value='/cadastro/fiador/obterAssociacoesCotaFiador' />", null, 
			function(result) {
				$(".cotasAssociadasGrid").flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});
				
				$("#numeroCota").val("");
				$("#nomeCota").val("");
			},
			null,
			true
		);
	}
	
	function adicionarAssociacaoCota(){
		var data = "numeroCota=" + $("#numeroCota").val() + "&nomeCota=" + $("#nomeCota").val();
		
		$.postJSON("<c:url value='/cadastro/fiador/adicionarAssociacaoCota' />", data, 
			function(result) {
				$(".cotasAssociadasGrid").flexAddData({
					page: 1, total: 1, rows: result.rows
				});
				
				$("#numeroCota").val("");
				$("#nomeCota").val("");
			},
			null,
			true
		);
	}
	
	function removerAssociacaoCota(referencia){
		
		$(".dialog-excluir").dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$(this).dialog("close");
					
					$.postJSON("<c:url value='/cadastro/fiador/removerAssociacaoCota' />", "referencia=" + referencia, 
						function(result) {
							$(".cotasAssociadasGrid").flexAddData({
								page: 1, total: 1, rows: result.rows
							});
							
							$("#numeroCota").val("");
							$("#nomeCota").val("");
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
		
		$(".dialog-excluir").show();
	}
	
	function limparCamposCotasAssociadas(){
		$("#numeroCota").val("");
		$("#nomeCota").val("");
	}
	
	function buscarNomeCota(){
		
		var numeroCota = $("#numeroCota").val();
		
		if (numeroCota.length > 0){
		
			$.postJSON("<c:url value='/cadastro/fiador/pesquisarNomeCotaPorNumeroCota' />", "numeroCota=" + numeroCota, 
				function(result) {
					if (result != ""){
						
						$("#nomeCota").val(result);
						$('[name="habilitarAdicionarCotaAssociacao"]').attr("href", "javascript:adicionarAssociacaoCota();");
					} else {
						
						$("#nomeCota").val("");
						$('[name="habilitarAdicionarCotaAssociacao"]').removeAttr("href");
					}
				},
				null,
				true
			);
		} else {
			$("#nomeCota").val("");
		}
	}
</script>
<table width="280" cellpadding="2" cellspacing="2" style="text-align: left;">
	<div class="dialog-excluir" id="dialog-excluir" title="Cotas Associadas">
		<p>Confirma esta Exclusão?</p>
	</div>
	<tr>
		<td width="46">Cota:</td>
		<td width="218">
			<input type="text" style="width: 80px; float: left; margin-right: 5px;" id="numeroCota" maxlength="11" onblur="buscarNomeCota();"/>
		</td>
	</tr>
	<tr>
		<td>Nome:</td>
		<td><input type="text" style="width: 200px" id="nomeCota" readonly="readonly"/></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><span class="bt_add"><a name="habilitarAdicionarCotaAssociacao">Incluir Novo</a></span></td>
	</tr>
</table>
<br />
<label><strong>Cotas Cadastradas</strong></label>
<br />
<table class="cotasAssociadasGrid"></table>