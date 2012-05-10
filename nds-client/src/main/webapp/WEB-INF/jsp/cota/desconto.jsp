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
		        <option>051 - TESTE</option>
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
		        <option>001 - Normal</option>
		        <option>002 - Produtos Tributados</option>
		        <option>003 - Vídeo Print de 1/1/96 A</option>
		        <option>004 - Cromos - Normal Exc Ju</option>
		        <option>005 - Importadas - Eletrolibe</option>
		        <option>006 - Promoções</option>
		        <option>007 - Especial Globo</option>
		        <option>008 - Magali Fome Zero</option>
		        <option>009 - Impratadas Mag</option>
		        <option>011 - Importadas MagExpress</option>
		      </select>
		      
		    </fieldset>
	   </td>
  </tr>
</tbody>
</table>