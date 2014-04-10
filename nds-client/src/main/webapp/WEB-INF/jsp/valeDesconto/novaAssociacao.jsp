<head>
	<style>
		#dialog-nova-associacao td {
			font-weight: bolder;
		}
	</style>
</head>

<form id="formNovaAssociacao" name="formNovaAssociacao">
	<div id="dialog-nova-associacao" style="display:none">
		
		<table id="tabelaAssociacaoEdicoes">
		
			<tr>
				<td> Código: </td>
				<td colspan="2"> <input type="text" maxlength="20" id="codigoProdutoAssociacao" /> 
				</td>
			</tr>
			
			<tr>
				<td> Publicação: </td>
				<td colspan="2"> <input type="text" maxlength="20" style="width:220px" id="nomeProdutoAssociacao" /> </td>
			</tr>
			
			<tr>
				<td> 1º Edição: </td>
				<td style="width:120px"> <input name="edicao" type="text" maxlength="20" numeroEdicao="1" style="width:120px"/> </td>
				<td> <span id="spanAlert1"></span> </td>
			</tr>
			
			<tr>
				<td> 2º Edição: </td>
				<td style="width:120px"> <input name="edicao" type="text" maxlength="20" numeroEdicao="2" style="width:120px" /> </td>
				<td> <span id="spanAlert2"></span> </td>
			</tr>
			
			<tr>
				<td> 3º Edição: </td>
				<td style="width:120px"> 
					<input name="edicao" type="text" maxlength="20" style="width:120px" isUltimaEdicao="true" numeroEdicao="3" /> 
				</td>
				<td> <span id="spanAlert3"></span> </td>
			</tr>
			
		</table>
		
		<div class="linha_separa_fields">&nbsp;</div>
		
		<span class="bt_add">
			<a href="javascript:;" onclick="valeDescontoController.associarEdicoes();;">Incluir Novo</a>
		</span>
		
	</div>
</form>