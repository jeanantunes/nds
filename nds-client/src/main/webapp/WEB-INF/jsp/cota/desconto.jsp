<script type="text/javascript">
$(function() {
	
	$("select[name='selectDesconto']").multiSelect("select[name='selectTipoDesconto']", {trigger: "#linkDescontoVoltarTodos"});
	
	$("select[name='selectTipoDesconto']").multiSelect("select[name='selectDesconto']", {trigger: "#linkDescontoEnviarTodos"});
	
});

</script>

<table width="597" cellspacing="2" cellpadding="2" border="0" align="center">
  <tbody>
  	<tr class="especialidades">
    	<td width="278" valign="top">
    	<fieldset>
	      	<legend>Tipos de Desconto</legend>
		      <select style="height:270px; width:245px;" id="selectTipoDesconto" multiple="multiple" size="10" name="selectTipoDesconto">
		       
		      </select>
    	</fieldset>
    	</td>
    	
     	<td width="69" align="center">
			<a id="linkDescontoEnviarTodos" href="javascript:;"><img height="30" width="39" src="./images/seta_vai_todos.png"></a>
			<br><br>
			<a id="linkDescontoVoltarTodos" href="javascript:;"><img height="30" width="39" src="./images/seta_volta_todos.png"></a>
			<br>
		</td>
		
		<td>
		    <fieldset>
		      <legend>Desconto Cota</legend>
		      
		      <select style="height:270px; width:245px;" id="selectDesconto" multiple="multiple" size="10" name="selectDesconto">
		       
		      </select>
		      
		    </fieldset>
	   </td>
  </tr>
</tbody>
</table>