<form id="formSocioCota">
	<div id="dialog-socio" title="Novo Sócio" style="display:none;">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="dialog-socio" name="messageDialog"/>
	</jsp:include>
	
	<fieldset style="width:710px!important;">
		<legend>Novo Sócio</legend>

			<input type="hidden" name="socioCota.telefone.id" id="idTelefone"/>
			
			<input type="hidden" name="socioCota.endereco.id" id="idEndereco"/>
			
			<input type="hidden" name="socioCota.endereco.codigoBairro" id="codigoBairro"/>
		
			<input type="hidden" name="socioCota.endereco.codigoCidadeIBGE" id="codigoCidadeIBGE"/>
		
			<input type="hidden" id="idLocalidade"/>
			
			<input type="hidden" id="idSocioCota" name="socioCota.id"/>
		
			<table width="700" cellpadding="2" cellspacing="2" style="text-align:left ">
				<tr>
				  <td>Nome:</td>
				  <td><input type="text" name="socioCota.nome" id="nomeSocio" style="width:230px" /></td>
				  <td>Cargo:</td>
				  <td><input type="text" name="socioCota.cargo" id="cargoSocio" style="width:250px" /></td>
	  			</tr>
				<tr>
					<td width="78">Principal:</td>
					<td width="251">
						<input type="checkbox" name="socioCota.principal" id="checkboxSocioPrincipal" />
					</td>
					<td width="95">CEP:</td>
					<td width="261">
						
						<input type="text" name="socioCota.endereco.cep" id="cep" style="float:left; margin-right:5px;" />
						
						<span class="classPesquisar" title="Pesquisar Cep.">
							<a href="javascript:;" id="btnPesquisarEndereco">&nbsp;</a>
						</span>
					</td>
				</tr>
				<tr>
					<td>UF:</td>
				    <td>
				  	<select name="socioCota.endereco.uf" 
				  			id="uf"
				  			style="width:50px" 
				  			id="uf">
				  	</select>
				    </td>
				    <td>Cidade:</td>
					<td>
						<input type="text" name="socioCota.endereco.cidade" style="width:250px"
							   id="cidade" />
					</td>
				  
				</tr>
				
				<tr>
					<td>Bairro:</td>
					<td>
						<input type="text" name="socioCota.endereco.bairro" style="width:230px" id="bairro" />
					</td>
					<td>Complemento:</td>
					<td>
						<input type="text" name="socioCota.endereco.complemento" style="width:250px"
							   id="complemento"  />
					</td>
				</tr>
				<tr>
					<td>Tipo :</td>
					<td>
						<input type="text" name="socioCota.endereco.tipoLogradouro" id="tipoLogradouro" style="width:80px" />
				  	</td>
					<td>Logradouro:</td>
					<td>
						<input type="text" name="socioCota.endereco.logradouro" style="width:250px" id="logradouro"  />
					</td>
				</tr>
				<tr>
					<td>N&uacute;mero:</td>
					<td>
						<input type="text" name="socioCota.endereco.numero" style="width:50px"
							   id="numero" maxlength="9"  />
					</td>
					
				  <td>Tel: </td>
				  <td>
				  <input type="text" name="socioCota.telefone.ddd" id="ddd" maxlength="2" style="width:50px" /> 
				    - 
			      <input type="text" name="socioCota.telefone.numero" id="numeroTelefone" style="width:100px" /></td>
	  			</tr>
			</table>
	    </fieldset>
	</div>
</form>