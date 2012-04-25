
<script type="text/javascript">
$(function() {
	
	$("select[name='selectMap']").multiSelect("select[name='selectMaterialPromocional']", {trigger: "#linkMapVoltarTodos"});
	
	$("select[name='selectMaterialPromocional']").multiSelect("select[name='selectMap']", {trigger: "#linkMapEnviarTodos"});
	
});

</script>

<fieldset>
	<legend>MAP</legend>
	<table width="605" border="0" align="center" cellpadding="2" cellspacing="2">
	  <tr class="especialidades">
	    <td width="264" valign="top">
		    <fieldset style="width:250px!important;">
		    	<legend>Material Promocional</legend>
			    
			    <select name="selectMaterialPromocional" size="10" multiple="multiple" id="selectMaterialPromocional" style="height:270px; width:245px;">
			      <option>Adesivo</option>
			      <option>Bandeirola</option>
			      <option>Brindes</option>
			      <option>Cartaz Grande</option>
			      <option>Display Acrilico</option>
			      <option>Display Aramado de Chão</option>
			      <option>Faixa de Banca</option>
			      <option>Móbile</option>
			      <option>Poster</option>
			      <option>Reprint Capa</option>
			      <option>Sapateira</option>
			      <option>Totem</option>
		    	</select>
		    
		    </fieldset>
	    </td>
	    
	     <td width="69" align="center">
			<a id="linkMapEnviarTodos" href="javascript:;"><img height="30" width="39" src="./images/seta_vai_todos.png"></a>
			<br><br>
			<a id="linkMapVoltarTodos" href="javascript:;"><img height="30" width="39" src="./images/seta_volta_todos.png"></a>
			<br>
		</td>
	    	
	    <td width="252" valign="top">
	    	<fieldset style="width:250px!important;">
	    		
	    		<legend>Tem espaço para receber o MAP</legend>
	    		
	    		<select name="selectMap" id ="selectMap" size="10" multiple="multiple" id="select4" style="height:270px; width:245px;">
				</select>
				
			</fieldset>
		</td>
	  </tr>
	</table>
</fieldset>

