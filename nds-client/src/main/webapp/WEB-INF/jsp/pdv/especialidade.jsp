<script type="text/javascript">
$(function() {
	
	
	
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
					 <c:forEach items="${listaEspecialidadePDV}" var="item">
					      <option value="${item.key}">${item.value}</option>	          
					  </c:forEach>
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

