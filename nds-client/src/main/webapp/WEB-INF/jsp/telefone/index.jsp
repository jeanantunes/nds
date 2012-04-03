<head>
	<script type="text/javascript">
		function popupTelefone() {
			
			$("#manutencaoTelefones").dialog({
				resizable: false,
				height:500,
				width:840,
				modal : true
			});
			
			popularGrid();
		};
		
		function popularGrid() {
			
			$("#telefonesGrid").flexigrid({
				preProcess: processarResultado,
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
					display : 'Ramal / ID',
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
				height : 150,
				disableSelect: true
			});
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

			if ($('#telefonesGrid').css('display') == 'none') {
					
				$('#telefonesGrid').show();
			}
			
			if (data.result){
				return data.result;
			}
			return data;
		}
		
		function getActions(idTelefone) {

			return '<a href="javascript:;" onclick="editarTelefone(' + idTelefone + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Editar telefone">' +
					'<img src="/nds-client/images/ico_editar.gif" border="0px"/>' +
					'</a>' +
					'<a href="javascript:;" onclick="removerTelefone(' + idTelefone + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Excluir telefone">' +
					'<img src="/nds-client/images/ico_excluir.gif" border="0px"/>' +
					'</a>';
		}
		
		function adicionarTelefone(){
			
			var data = "referencia=" + $("#referenciaHidden").val() + "&tipoTelefone=" + $("#tipoTelefone").val() + 
				"&ddd=" + $("#ddd").val() + "&numero=" + $("#numeroTelefone").val() + "&ramal=" + $("#ramal").val() + 
				"&principal=" + ("" + $("#telefonePrincipal").attr("checked") == 'checked');
			
			$.postJSON(
				'/nds-client/cadastro/telefone/adicionarTelefone',
				data,
				function(result) {
					$("#telefonesGrid").flexAddData({
						page: 1, total: 1, rows: result.rows
					});	
					
					limparCampos();
					
					$("#referenciaHidden").val("");
					
					$("#botaoAddEditar").text("Incluir Novo");
				},
				null,
				true
			);
		}
		
		function removerTelefone(referenciaTelefone){
			var data = "referencia=" + referenciaTelefone;
		
			$("#dialog-excluir").dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$(this).dialog("close");
						
						$.postJSON(
							'/nds-client/cadastro/telefone/removerTelefone',
							data,
							function(result) {
								$("#telefonesGrid").flexAddData({
									page: 1, total: 1, rows: result.rows
								});
								
								limparCampos();
								
								$("#referenciaHidden").val("");
								
								$("#botaoAddEditar").text("Incluir Novo");
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
			
			$("#dialog-excluir").show();
		}
		
		function editarTelefone(referenciaTelefone){
			limparCampos();
			
			var data = "referencia=" + referenciaTelefone;
		
			$.postJSON(
				'/nds-client/cadastro/telefone/editarTelefone',
				data,
				function(result) {
					if (result != ''){
						$("#tipoTelefone").val(result.tipoTelefone);
						$("#ddd").val(result.telefone.ddd);
						$("#numeroTelefone").val(result.telefone.numero);
						$("#ramal").val(result.telefone.ramal);
						$("#telefonePrincipal").attr("checked", result.principal);
						
						$("#referenciaHidden").val(referenciaTelefone);
						
						$("#botaoAddEditar").text("Editar");
						
						opcaoTel(result.tipoTelefone, 'trRamalId', 'lblRamalId', 'ramal');
					}
				},
				null,
				true
			);
		}
		
		function limparCampos(){
			$("#tipoTelefone").val("");
			$("#ddd").val("");
			$("#numeroTelefone").val("");
			$("#ramal").val("");
			$("#telefonePrincipal").attr("checked", false);
		}
		
		function opcaoTel(opcao, idDiv, idLbl, idCampo) {
			var div1 = $("#" + idDiv);
			var lbl = $("#" + idLbl);
			var campo = $("#" + idCampo);
			
			switch (opcao) {
				case 'COMERCIAL':
				case 'FAX':
					div1.show();
					lbl.text('Ramal:');
					campo.css('width', 40);
				break;
				case 'RADIO':
					div1.show();
					lbl.text('ID:');
					campo.css('width', 167);
				break;
				default:
					div1.hide();
					campo.val("");
				break;
			}
		}
		
		function carregarTelefones() {
			popularGrid();
			
			$.postJSON(
				'/nds-client/cadastro/telefone/pesquisarTelefones',
				null,
				function(result) {
					$("#telefonesGrid").flexAddData({
						page: result.page, total: result.total, rows: result.rows
					});
					
					limparCampos();
					
					$("#referenciaHidden").val("");
					
					$("#botaoAddEditar").text("Incluir Novo");
				},
				null,
				true
			);
		}
		
		//OS MÉTODOS A BAIXO FORAM CRIADOS APENAS PARA TESTE, JA QUE ESSA TELA SERÁ INCLUDE PARA OUTRAS
		function salvar(){
			var data = 'idCota=' + $("#idCota").val() + '&idFornecedor=' + $("#idFornecedor").val();
			$.postJSON(
				'/nds-client/cadastro/telefone/salvar',
				data,
				function(result){
					if (result.tipoMensagem){
						exibirMensagemDialog(
							result.tipoMensagem, 
							result.listaMensagens
						);
						
						cadastrarTelefone();
					}
				},
				null,
				true
			);
		}
		
		function cadastrarTelefone(){
			limparCampos();
			$("#referenciaHidden").val("");
			$("#botaoAddEditar").text("Incluir Novo");
			
			var data = 'idCota=' + $("#idCota").val() + '&idFornecedor=' + $("#idFornecedor").val();
			$.postJSON(
				'/nds-client/cadastro/telefone/cadastrar',
				data,
				function(result){
					popupTelefone();
					$("#telefonesGrid").flexAddData({
						page: 1, total: 1, rows: result.rows
					});
				},
				null,
				true
			);
		}
	</script>
</head>
	<div id="manutencaoTelefones" title="Telefones">
		
		<div id="dialog-excluir" title="Telefones">
			<p>Confirma esta Exclusão?</p>
		</div>
		<table width="280" cellpadding="2" cellspacing="2" style="text-align:left ">
			<tr>
				<td width="72">Tipo:</td>
				<td width="192">
					<select onchange="opcaoTel(this.value, 'trRamalId', 'lblRamalId', 'ramal');" style="width:174px;" id="tipoTelefone">
						<option value="" selected="selected">Selecione</option>
						<option value="COMERCIAL">Comercial</option>
						<option value="CELULAR">Celular</option>
						<option value="FAX">Fax</option>
						<option value="RESIDENCIAL">Residencial</option>
						<option value="RADIO">Rádio</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>Telefone: </td>
				<td>
					<input type="text" style="width:40px" id="ddd" maxlength="255" />-<input type="text" style="width:110px" id="numeroTelefone" maxlength="255"/>
				</td>
			</tr>
			<tr id="trRamalId">
				<td id="lblRamalId">Ramal: </td>
				<td>
					<input type="text" style="width:40px; float:left;" id="ramal" maxlength="255"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="principal1">Principal:</label>
				</td>
				<td class="complementar">
					<input type="checkbox" name="principal1" id="telefonePrincipal" />
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><span class="bt_add"><a href="javascript:;" onclick="adicionarTelefone();" id="botaoAddEditar">Incluir Novo</a></span></td>
			</tr>
		</table>
		
		<br />
		
		<label><strong>Telefones Cadastrados</strong></label>
		
		<br />
		
		<table id="telefonesGrid"></table>
		
		<br/>
		
		<input type="hidden" id="referenciaHidden"/>
</div>