
<div id="dialog-especifico" title="Novo Tipo de Desconto Especifico" style="display:none;"> 

<jsp:include page="../messagesDialog.jsp">
	
	<jsp:param value="idModalDescontoEspecifico" name="messageDialog"/>

</jsp:include>
   
<table width="480" border="0" align="center" cellpadding="2" cellspacing="2">
  
  <tr class="especialidades">
    <td colspan="4" valign="top">
    	<fieldset style="width:500px;">
    		<legend>Selecione a Cota</legend>
    		<label style="width:auto!important;">Cota:</label>
    		<input name="numCotaEspecifico" 
           		   id="numCotaEspecifico" 
           		   type="text"
           		   maxlength="11"
           		   style="width:70px;float:left;"
           		   onchange="pesquisaCotaTipoDescontoCota.pesquisarPorNumeroCota('#numCotaEspecifico', '#descricaoCotaEspecifico',true,
           	  											descontoCotaController.pesquisarCotaSuccessCallBack, 
           	  											descontoCotaController.pesquisarCotaErrorCallBack);" />
           	  											
    		<label style="width:auto!important;">Nome:</label>
    		<input  name="descricaoCotaEspecifico" 
		      		 id="descricaoCotaEspecifico" 
		      		 type="text" 
		      		 class="nome_jornaleiro" 
		      		 maxlength="255"
		      		 style="width:200px;float:left;"
		      		 onkeyup="pesquisaCotaTipoDescontoCota.autoCompletarPorNome('#descricaoCotaEspecifico');" 
		      		 onblur="pesquisaCotaTipoDescontoCota.pesquisarPorNomeCota('#numCotaEspecifico', '#descricaoCotaEspecifico',true,
										      			descontoCotaController.pesquisarCotaSuccessCallBack,
										      			descontoCotaController.pesquisarCotaErrorCallBack);" />

    		
    	</fieldset>
    </td>
  </tr>
  
  <tr class="especialidades">
    	
    	<td width="202" valign="top">
    		<fieldset>
      			<legend>Selecione os Fornecedores</legend>
      			<select style="height:130px; width:200px;" id="selectFornecedor_option_especifico" multiple="multiple" size="10" name="selectFornecedor_especifico">
      				<option></option>
			    </select>
    		</fieldset>
    	</td>
    
	    <td width="56" align="center">
			<a id="linkFornecedorEnviarTodos_especifico" href="javascript:;"><img height="30" width="39" src="${pageContext.request.contextPath}/images/seta_vai_todos.png" border="0"></a>
			<br><br>
			<a id="linkFornecedorVoltarTodos_especifico" href="javascript:;"><img height="30" width="39" src="${pageContext.request.contextPath}/images/seta_volta_todos.png" border="0"></a>
			<br>
		</td>
    
	    <td width="202" valign="top">
		    <fieldset>
		      <legend>Fornecedores selecionados</legend>
		      <select style="height:130px; width:200px;" id="selectFornecedorSelecionado_option_especifico" multiple="multiple" size="10" name="selectFornecedorSelecionado_especifico">
			  </select>
		      <br>
		    </fieldset>
	    </td>
  </tr>
  
  <tr class="especialidades">
    <td colspan="3" valign="top">
    
    <fieldset style="width:500px;">
    	<legend>Informe o Percentual de Desconto</legend>
    	Desconto: <input type="text" name="descontoEspecifico" id="descontoEspecifico" style="width:60px; margin-left:10px; margin-right:10px;"/> %
    </fieldset>
   
   </td>
   
  </tr>
  
</table>       

</div>
