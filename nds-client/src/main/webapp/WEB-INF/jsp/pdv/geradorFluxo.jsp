
<script type="text/javascript">
$(function() {

	$("select[name='selectFluxoSecundario']").multiSelect("select[name='selecTipoGeradorFluxo']", {trigger: "#linkFluxoVoltarTodos"});
	
	$("select[name='selecTipoGeradorFluxo']").multiSelect("select[name='selectFluxoSecundario']", {trigger: "#linkFluxoEnviarTodos"});
	
});

</script>

<fieldset>
	<legend>Gerador de Fluxo</legend>
    <table width="620" border="0" align="center" cellpadding="2" cellspacing="2">
	  <tr class="geradorFluxo">
	    <td width="252" valign="top">
		    <fieldset style="width:250px!important;">
		    <legend>Tipo Gerador Fluxo</legend>
		    
		    <select name="selecTipoGeradorFluxo" size="10" multiple="multiple" id="selecTipoGeradorFluxo" style="height:267px; width:245px;">
		      <option>Academia de Ginastica</option>
		      <option>Aeroporto</option>
		      <option>Agência de Turismo</option>
		      <option>Banco</option>
		      <option>Bazares</option>
		      <option>Biblioteca</option>
		      <option>Estática</option>
		    </select>
		    
		    </fieldset>
	    </td>
	    <td width="68" align="center" valign="top">
		    <table width="53%" border="0" cellspacing="0" cellpadding="0">
		      <tr>
		        <td height="122" valign="top">
		        	<br />
		        	<img src="../images/seta_vai_todos.png" width="39" height="30" />
		        </td>
		      </tr>
		      <tr>
		        <td>
					<a id="linkFluxoEnviarTodos" href="javascript:;"><img height="30" width="39" src="./images/seta_vai_todos.png"></a>
					<br><br>
					<a id="linkFluxoVoltarTodos" href="javascript:;"><img height="30" width="39" src="./images/seta_volta_todos.png"></a>
					<br>
				</td>
		      </tr>
	    	</table>
	    </td>
    	<td width="280" valign="top">
	    	<fieldset style="width:250px!important;">
	    		<legend>Gerador Fluxo Principal</legend>
	    		<input name="" type="text" style="width:240px; " />
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

