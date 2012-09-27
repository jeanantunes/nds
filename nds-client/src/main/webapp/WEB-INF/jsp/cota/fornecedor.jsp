<script type="text/javascript">

$(function() {
	
});

</script>
<input type="hidden" id="telaCotaidFornecedorHidden" value="${param.paramFornecedores}">
<fieldset style="width:880px!important; margin:5px;">
 	<legend>Fornecedores</legend>
	<table cellspacing="2" cellpadding="2" border="0" align="center" style="margin-left:30px;">
	  <tbody>
	  	
	  	<tr class="especialidades">
	    	
	    	<td valign="top">
	    		<fieldset style="width:250px;">
	      			<legend>Selecione os Fornecedores</legend>
	      			<select style="height:270px; width:245px;" id="selectFornecedor_option_${param.paramFornecedores}" multiple="multiple" size="10" name="selectFornecedor_${param.paramFornecedores}">
	      				<option></option>
				    </select>
				    <br clear="all" />
	    		</fieldset>
	    	</td>
	    
		    <td align="center">
				<a id="linkFornecedorEnviarTodos_${param.paramFornecedores}" href="javascript:;"><img src="${pageContext.request.contextPath}/images/seta_vai_todos.png" border="0"></a>
				<br><br>
				<a id="linkFornecedorVoltarTodos_${param.paramFornecedores}" href="javascript:;"><img src="${pageContext.request.contextPath}/images/seta_volta_todos.png" border="0"></a>
				<br>
			</td>
	    
		    <td valign="top">
			    <fieldset style="width:250px;">
			      <legend>Fornecedores selecionados</legend>
			      <select style="height:270px; width:245px;" id="selectFornecedorSelecionado_option_${param.paramFornecedores}" multiple="multiple" size="10" name="selectFornecedorSelecionado_${param.paramFornecedores}">
				  </select>
			      <br clear="all" />
			    </fieldset>
		    </td>
	  	  </tr>
		</tbody>
	</table>
</fieldset>