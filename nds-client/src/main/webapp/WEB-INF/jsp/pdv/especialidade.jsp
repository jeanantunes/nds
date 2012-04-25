<script type="text/javascript">
$(function() {
	
	$("select[name='selectEspecialidades']").selectAdjustWidth("select[name='selectCaracteristicas']");
	
	$("select[name='selectEspecialidades']").multiSelect("select[name='selectCaracteristicas']", {trigger: "#linkVoltarTodos"});
	
	$("select[name='selectCaracteristicas']").multiSelect("select[name='selectEspecialidades']", {trigger: "#linkEnviarTodos"});
	
});

</script>

<fieldset>
  	<legend>Especialidade</legend>
    <table width="605" border="0" align="center" cellpadding="2" cellspacing="2">
	  <tr class="especialidades">
	    <td width="264" valign="top">
	    	<fieldset style="width:250px!important;">
		    	<legend>Caracteristicas</legend>
			   
				<select name="selectCaracteristicas" multiple="true" id="caract_options" size="10" style="height:270px; width:245px;" >
					<option value="1">Decoração</option>
					<option value="1">Fasciculos</option>
					<option value="1">Figurinhas</option>
					<option value="1">Generalista</option>
					<option value="1">Informática</option>
					<option value="1">Moda</option>
					<option value="1">Quadrinhos</option>
					<option value="1">Sexo</option>
				</select>
			
	    	</fieldset>
	    </td>
	    
	    <td width="69" align="center">
			<a id="linkEnviarTodos" href="javascript:;"><img height="30" width="39" src="./images/seta_vai_todos.png"></a>
			<br><br>
			<a id="linkVoltarTodos" href="javascript:;"><img height="30" width="39" src="./images/seta_volta_todos.png"></a>
			<br>
		</td>
	    
	    <td width="252" valign="top">
	    	
	    	<fieldset style="width:250px!important;">
		   		
		   		<legend>Especialidades</legend>
 	
		    	<select name="selectEspecialidades" multiple="true" id="especialidades_options" size="10" style="height:270px; width:245px;">
					  
				</select>
		    	
			</fieldset>
		</td>
	  </tr>
	</table>
</fieldset>

