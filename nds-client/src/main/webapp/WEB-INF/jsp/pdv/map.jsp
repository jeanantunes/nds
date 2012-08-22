
<fieldset>
	<legend>MAP</legend>
	<table width="605" border="0" align="center" cellpadding="2" cellspacing="2">
	  <tr class="especialidades">
	    <td width="264" valign="top">
		    <fieldset style="width:250px!important;">
		    	<legend>Material Promocional</legend>
			    
			    <select name="selectMaterialPromocional" size="10" multiple="multiple" id="selectMaterialPromocional" style="height:270px; width:245px;">
			      <c:forEach items="${listaMaterialPromocionalPDV}" var="item">
					      <option value="${item.key}">${item.value}</option>	          
					  </c:forEach>
		    	</select>
		    
		    </fieldset>
	    </td>
	    
	     <td width="69" align="center">
			<a id="linkMapEnviarTodos" href="javascript:;"><img height="30" width="39" border="0" src="${pageContext.request.contextPath}/images/seta_vai_todos.png"></a>
			<br><br>
			<a id="linkMapVoltarTodos" href="javascript:;"><img height="30" width="39" border="0" src="${pageContext.request.contextPath}/images/seta_volta_todos.png"></a>
			<br>
		</td>
	    	
	    <td width="252" valign="top">
	    	<fieldset style="width:250px!important;">
	    		
	    		<legend>Tem espaço para receber o MAP</legend>
	    		
	    		<select name="selectMap" id ="selectMap" size="10" multiple="multiple" style="height:270px; width:245px;">
				</select>
				
			</fieldset>
		</td>
	  </tr>
	</table>
	
	<table width="302" cellspacing="1" cellpadding="1" border="0">
	  <tbody>
	  	<tr>
	    	<td width="23"><input type="checkbox" onchange="PDV.mostra_expositor('#expositor');" id="expositor" name="expositor"></td>
	    	<td width="272">Expositor</td>
	  	</tr>
	  	<tr>
	    	<td>&nbsp;</td>
	    	<td style="display:none;" class="tipoExpositor">Tipo:
	    	<input type="text" style="width:150px;" id="tipoExpositor" name="tipoExpositor"></td>
	  	</tr>
	  </tbody>
	</table>
	
</fieldset>

