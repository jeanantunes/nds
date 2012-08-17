<script type="text/javascript">

$(function() {
	
});

</script>

<table width="597" cellspacing="2" cellpadding="2" border="0" align="center">
  <tbody>
  	
  	<tr class="especialidades">
    	
    	<td width="278" valign="top">
    		<fieldset style="height:280px; width:250px;">
      			<legend>Selecione os Fornecedores</legend>
      			<select style="height:270px; width:245px;" id="selectFornecedor_option_${param.paramFornecedores}" multiple="multiple" size="10" name="selectFornecedor_${param.paramFornecedores}">
      				<option></option>
			    </select>
    		</fieldset>
    	</td>
    
	    <td width="69" align="center">
			<a id="linkFornecedorEnviarTodos_${param.paramFornecedores}" href="javascript:;"><img height="30" width="39" src="${pageContext.request.contextPath}/images/seta_vai_todos.png" border="0"></a>
			<br><br>
			<a id="linkFornecedorVoltarTodos_${param.paramFornecedores}" href="javascript:;"><img height="30" width="39" src="${pageContext.request.contextPath}/images/seta_volta_todos.png" border="0"></a>
			<br>
		</td>
    
	    <td width="285" valign="top">
		    <fieldset style="height:280px; width:250px;">
		      <legend>Fornecedores selecionados</legend>
		      <select style="height:270px; width:245px;" id="selectFornecedorSelecionado_option_${param.paramFornecedores}" multiple="multiple" size="10" name="selectFornecedorSelecionado_${param.paramFornecedores}">
			  </select>
		      <br>
		    </fieldset>
	    </td>
  </tr>
</tbody></table>