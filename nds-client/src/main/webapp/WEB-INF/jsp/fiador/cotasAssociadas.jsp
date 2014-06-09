<fieldset style="margin:5px; width:880px;">
<legend>Associar Cotas</legend>
<table width="280" cellpadding="2" cellspacing="2" style="text-align: left;">
	<div class="fiadorController-cotasAssociadas-dialog-excluir" id="fiadorController-cotasAssociadas-dialog-excluir" title="Cotas Associadas" style="display: none;">
		<p>Confirma esta Exclusão?</p>
	</div>
	<tr>
		<td width="46">Cota:</td>
		
		<td width="218">
		
		<input type="text" style="width: 70px; float: left; margin-right: 5px;" 
			
			name="fiadorController-cotasAssociadas-numeroCota"
			
			id="fiadorController-cotasAssociadas-numeroCota" 
			
			maxlength="11" 
			
			onchange="pesquisaCotaCadastroFiador.pesquisarPorNumeroCota(
			'#fiadorController-cotasAssociadas-numeroCota',
			'#fiadorController-cotasAssociadas-nomeCota',
			true, 
			null,
			null);"/>
			
		
		</td>
	</tr>
	<tr>
	
		<td>Nome:</td>
		
		<td>
		
            <input  name="fiadorController-cotasAssociadas-nomeCota" 
			      		 id="fiadorController-cotasAssociadas-nomeCota" 
			      		 type="text" 
			      		 class="nome_jornaleiro" 
			      		 maxlength="255"
			      		 style="width:200px;"
			      		 onkeyup="pesquisaCotaCadastroFiador.autoCompletarPorNome('#fiadorController-cotasAssociadas-nomeCota');" 
			      		 
			      		 onblur="pesquisaCotaCadastroFiador.pesquisarPorNomeCota(
			      				 '#fiadorController-cotasAssociadas-numeroCota', 
			      				 '#fiadorController-cotasAssociadas-nomeCota',
			      				 true,
			      				 null,
			      				 null);"/>
		
		</td>
		
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><span class="bt_novos"><a id="fiadorController-cotasAssociadas-adicionarCotaAssociacao" rel="tipsy" title="Associar Nova Cota" href="javascript:fiadorController.adicionarAssociacaoCota();"><img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0"/></a></span></td>
	</tr>
</table>
</fieldset>

<fieldset style="margin:5px; width:880px;">
<legend>Cotas Associadas</legend>
<table class="fiadorController-cotasAssociadasGrid"></table>
</fieldset>