
<div id="dialog-geral" title="Novo Tipo de Desconto Geral" style="display:none;">    

	<jsp:include page="../messagesDialog.jsp">
		
		<jsp:param value="idModalDescontoGeral" name="messageDialog"/>
	
	</jsp:include>


<table width="480" border="0" align="center" cellpadding="2" cellspacing="2">
   
	<tr class="especialidades">
		<td colspan="3" valign="top">
		   <fieldset style="width: 650px !important;">
				<legend>Selecione os Fornecedores</legend>
					<select name="selectFornecedor_option" id="selectFornecedor_option" 
							style="width: 170px;">
						<option></option>
					</select>
			</fieldset>
		</td>
	</tr>
   
   <!--  
    <tr class="especialidades">
    	
    	<td width="202" valign="top">
    		<fieldset>
      			<legend>Selecione os Fornecedores</legend>
      			<select style="height:130px; width:200px;" id="selectFornecedor_option" multiple="multiple" size="10" name="selectFornecedor">
      				<option></option>
			    </select>
    		</fieldset>
    	</td>
    
	    <td width="56" align="center">
			<a id="linkFornecedorEnviarTodos" href="javascript:;"><img height="30" width="39" src="${pageContext.request.contextPath}/images/seta_vai_todos.png" border="0"></a>
			<br><br>
			<a id="linkFornecedorVoltarTodos" href="javascript:;"><img height="30" width="39" src="${pageContext.request.contextPath}/images/seta_volta_todos.png" border="0"></a>
			<br>
		</td>
    
	    <td width="202" valign="top">
		    <fieldset>
		      <legend>Fornecedores selecionados</legend>
		      <select style="height:130px; width:200px;" id="selectFornecedorSelecionado_option" multiple="multiple" size="10" name="selectFornecedorSelecionado">
			  </select>
		      <br>
		    </fieldset>
	    </td>
  </tr>
  
  <tr class="especialidades">
    <td colspan="3" valign="top">
	    <fieldset style="width:485px;">
	    <legend>Informe o Percentual de Desconto</legend>
	    	Desconto: <input type="text" name="descontoGeral" id="descontoGeral" style="width:60px; margin-left:10px; margin-right:10px;"/> %
	    </fieldset>
    </td>
    </tr>
   -->
  
</table> 

<fieldset style="width: 650px !important;">
<legend>Descontos aplicados</legend>
<table width="650" border="0" align="center" cellpadding="2" cellspacing="2">
	<tr class="especialidades">
		<td width="100" align="center">
			<label>&nbsp;</label>
		</td>
		<td width="100" align="center">
			<label> Produtos com Desconto </label>
		</td>
		<td width="100" align="center">
			Margem Distribuidor (fornecedor)
		</td>
		<td width="100" align="center">
			Desconto Cota (intermedi&aacute;rio)
		</td>
		<td width="100" align="center">
			Margem Adicional (Distribuidor)
		</td>
		<td width="100" align="center">
			Desconto Cota (Aplicado)
		</td>
	</tr>
	<tr class="especialidades">
		<td width="100" align="center">
			Dinap
		</td>
		<td width="100" align="center">
			<input type="text" name="produtoComDesconto" id="produtoComDesconto_1" style="text-align: center; width:80px;" value="0,0" disabled="disabled"/>
		</td>
		<td width="100" align="center">
			<input type="text" name="produtoComDesconto" id="produtoComDesconto_1" style="text-align: center; width:80px;" value="0,0" disabled="disabled"/>
		</td>
		<td width="100" align="center">
			<input type="text" name="produtoComDesconto" id="produtoComDesconto_1" style="text-align: center; width:80px;" value="0,0" disabled="disabled"/>
		</td>
		<td width="100" align="center">
			<input type="text" name="produtoComDesconto" id="produtoComDesconto_1" style="text-align: center; width:80px;" value="0,0" disabled="disabled"/>
		</td>
		<td width="100" align="center">
			<input type="text" name="produtoComDesconto" id="produtoComDesconto_1" style="text-align: center; width:80px;" value="0,0"/>
		</td>
	</tr>
</table>
</fieldset>

<!-- 


<table width="480" border="0" align="center" cellpadding="2" cellspacing="2">
    
    <tr class="especialidades">
    	
    	<td width="202" valign="top">
    		<fieldset>
      			<legend>Selecione os Fornecedores</legend>
      			<select style="height:130px; width:200px;" id="selectFornecedor_option" multiple="multiple" size="10" name="selectFornecedor">
      				<option></option>
			    </select>
    		</fieldset>
    	</td>
    
	    <td width="56" align="center">
			<a id="linkFornecedorEnviarTodos" href="javascript:;"><img height="30" width="39" src="${pageContext.request.contextPath}/images/seta_vai_todos.png" border="0"></a>
			<br><br>
			<a id="linkFornecedorVoltarTodos" href="javascript:;"><img height="30" width="39" src="${pageContext.request.contextPath}/images/seta_volta_todos.png" border="0"></a>
			<br>
		</td>
    
	    <td width="202" valign="top">
		    <fieldset>
		      <legend>Fornecedores selecionados</legend>
		      <select style="height:130px; width:200px;" id="selectFornecedorSelecionado_option" multiple="multiple" size="10" name="selectFornecedorSelecionado">
			  </select>
		      <br>
		    </fieldset>
	    </td>
  </tr>
  
  <tr class="especialidades">
    <td colspan="3" valign="top">
    <fieldset style="width:485px;">
    <legend>Informe o Percentual de Desconto</legend>
    	Desconto: <input type="text" name="descontoGeral" id="descontoGeral" style="width:60px; margin-left:10px; margin-right:10px;"/> %
    </fieldset></td>
    </tr>
    
</table> 

 -->        

</div>