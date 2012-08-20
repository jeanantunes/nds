
<fieldset>
	<legend>Gerador de Fluxo</legend>
    <table width="620" border="0" align="center" cellpadding="2" cellspacing="2">
	  <tr class="geradorFluxo">
	    <td width="252" valign="top">
		    <fieldset style="width:250px!important;">
		    <legend>Tipo Gerador Fluxo</legend>
		    
		    <select name="selecTipoGeradorFluxo" size="10" multiple="multiple" id="selecTipoGeradorFluxo" style="height:267px; width:245px;">
		      <c:forEach items="${listaTipoGeradorFluxoPDV}" var="item">
					      <option value="${item.key}">${item.value}</option>	          
					  </c:forEach>
		    </select>
		    
		    </fieldset>
	    </td>
	    <td width="68" align="center" valign="top">
		    <table width="53%" border="0" cellspacing="0" cellpadding="0">
		      <tr>
		        <td height="122" valign="top">
		        	<br />
		        	<a id="linkFluxoPrincipal" onclick="PDV.enviarFluxoPrincipal();" href="javascript:;"><img border="0" src="${pageContext.request.contextPath}/images/seta_vai_todos.png" width="39" height="30" /></a>
		        </td>
		      </tr>
		      <tr>
		        <td>
					<a id="linkFluxoEnviarTodos" href="javascript:;"><img height="30" width="39" border="0" src="${pageContext.request.contextPath}/images/seta_vai_todos.png"></a>
					<br><br>
					<a id="linkFluxoVoltarTodos" href="javascript:;"><img height="30" width="39" border="0" src="${pageContext.request.contextPath}/images/seta_volta_todos.png"></a>
					<br>
				</td>
		      </tr>
	    	</table>
	    </td>
    	<td width="280" valign="top">
	    	<fieldset style="width:250px!important;">
	    		<legend>Gerador Fluxo Principal</legend>
	    		<input name="txtGeradorFluxoPrincipal" id="txtGeradorFluxoPrincipal" type="text" style="width:240px;" disabled="disabled" />
	    		<input type="hidden"name="hiddenGeradorFluxoPrincipal" id="hiddenGeradorFluxoPrincipal" />
	    	</fieldset>
	    
    		<fieldset style="margin-top:5px; width:250px!important;">
		    	
		    	<legend>Gerador Fluxo Secundário</legend>
			    
			    <select name="selectFluxoSecundario" size="10" multiple="multiple" id="selectFluxoSecundario" style="height:200px; width:245px;">
		    	</select>
		    	
		    </fieldset>
		   
    	</td>
	  </tr>
	 </table>

</fieldset>

