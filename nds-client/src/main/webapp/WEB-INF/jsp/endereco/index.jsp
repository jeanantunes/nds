<head>
<script type="text/javascript" src="scripts/endereco.js"></script>
<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>


<script type="text/javascript">

	var ${param.telaEndereco} = new Endereco('${param.telaEndereco}','${param.message}');

</script>

</head>

<div id="${param.telaEndereco}manutencaoEnderecos">

	<div id="dialog-excluir-end" title="Excluir Endereço">
		<p>Confirma a exclusão desse endereço?</p>
	</div>
	
	<div name="${param.telaEndereco}formEnderecos" id="${param.telaEndereco}formEnderecos">
		 <fieldset style="width:880px!important; margin:5px;">
 			<legend>Endereço</legend>
			<input type="hidden" name="enderecoAssociacao.id" id="${param.telaEndereco}idEndereco"/>			
			
			<input type="hidden" name="enderecoAssociacao.endereco.id" id="${param.telaEndereco}enderecoid"/>
			
			<input type="hidden" name="tela" id="${param.telaEndereco}telaEndereco" value="${param.telaEndereco}"/>
			
			<input type="hidden" name="enderecoAssociacao.endereco.codigoBairro" id="${param.telaEndereco}codigoBairro"/>
			
			<input type="hidden" name="enderecoAssociacao.endereco.codigoCidadeIBGE" id="${param.telaEndereco}codigoCidadeIBGE"/>
			
			<input type="hidden" id="${param.telaEndereco}idLocalidade"/>
			
			<input type="hidden" name="enderecoAssociacao.enderecoPessoa" id="${param.telaEndereco}enderecoPessoa"/>
	
			<table width="754" cellpadding="2" cellspacing="2" style="text-align:left ">
				<tr>
					
					<td width="99">Tipo Endereço:</td>
					<td width="310">
						<select  style="width:157px" 
								 name="enderecoAssociacao.tipoEndereco" 
								 id="${param.telaEndereco}tipoEndereco">
							<option value="COMERCIAL">Comercial</option>
							<option value="LOCAL_ENTREGA">Local de Entrega</option>
							<option value="RESIDENCIAL">Residencial</option>
							<option value="COBRANCA">Cobrança</option>
						</select>
					</td>
					<td width="76">CEP:</td>
					<td width="241">
					<input type="text" style="float:left; margin-right:5px;" 
						   name="enderecoAssociacao.endereco.cep" onkeyup="${param.telaEndereco}.autoCompletarCep();" id="${param.telaEndereco}cep" />
	
						<div id="${param.telaEndereco}wrapperBtnPesquisaCepHabilitado">
							<span class="classPesquisar" title="Pesquisar Cep.">
								<a href="javascript:;" onclick="${param.telaEndereco}.pesquisarEnderecoPorCep();">&nbsp;</a>
							</span>
				  		</div>
	
						
						<div id="${param.telaEndereco}wrapperBtnPesquisaCepDesabilitado" style="display: none">
							<span class="classPesquisar" style="opacity:0.4" title="Pesquisar Cep.">
								<a href="#" onclick="javascript:;">&nbsp;</a>
							</span>
				  		</div>
						
						
						</td>
				</tr>
				<tr>
					<td>UF:</td>
					<td>
						<select style="width:157px;" 
								id="${param.telaEndereco}uf"
								name="enderecoAssociacao.endereco.uf">
						</select>
					</td>
					<td>Cidade:</td>
					<td>
						<input type="text" style="width:250px" 
							   id="${param.telaEndereco}cidade" 
							   onkeyup="${param.telaEndereco}.autoCompletarLocalidades();"
							   onblur="${param.telaEndereco}.autoCompletarLocalidades(true);"
							   name="enderecoAssociacao.endereco.cidade" />
					</td>
				</tr>
				<tr>
				
					<td>Bairro:</td>
					<td>
						<input type="text"  style="width:230px"
							   onkeyup="${param.telaEndereco}.autoCompletarBairros();"
							   onblur="${param.telaEndereco}.autoCompletarBairros(true);"
							   name="enderecoAssociacao.endereco.bairro" id="${param.telaEndereco}bairro" />
					</td>
					
					<td>Tipo Logradouro:</td>
					<td>
						<input type="text" style="width:250px" 
							   name="enderecoAssociacao.endereco.tipoLogradouro" id="${param.telaEndereco}tipoLogradouro" />
					</td>
					
				</tr>
				<tr>
					
					<td>Logradouro:</td>
					<td>
						<input type="text" style="width:230px"
							   onkeyup="${param.telaEndereco}.autoCompletarLogradouros();"
							   onblur="${param.telaEndereco}.autoCompletarLogradouros(true);" 
							   name="enderecoAssociacao.endereco.logradouro" id="${param.telaEndereco}logradouro" />
					</td>
					<td>Complemento:</td>
					<td>
						<input type="text" style="width:250px" 
							   name="enderecoAssociacao.endereco.complemento" id="${param.telaEndereco}complemento" />
					</td>
				    
			  </tr>
			  
				<tr>			
					<td>Número:</td>
					<td>
						<input type="text" style="width:50px" 
							   name="enderecoAssociacao.endereco.numero" id="${param.telaEndereco}numero" maxlength="9" />
					</td>
					<td>Principal:</td>
				   	<td>
					  	<input type="checkbox" id="${param.telaEndereco}principal" 
					  		   name="enderecoAssociacao.enderecoPrincipal"/>
				  	</td>
				    <td>&nbsp;</td>
				    <td>&nbsp;</td>
				</tr>
				<tr>
				  <td>&nbsp;</td>
				  <td>
				  
				    <div id="${param.telaEndereco}wrapperBtnIncluirNovoEnderecoHabilitado">
					  	<span class="bt_novos" id="${param.telaEndereco}btnIncluirNovoEndereco">
					  		<a href="javascript:;" onclick="${param.telaEndereco}.incluirNovoEndereco();" id="${param.telaEndereco}linkIncluirNovoEndereco" rel="tipsy" title="Incluir Novo Endereço"><img src="${pageContext.request.contextPath}/images/ico_add.gif" border="0" /></a>
					  	</span>
				  	</div>
	
				    <div id="${param.telaEndereco}wrapperBtnIncluirNovoEnderecoDesabilitado" style="display: none">
					  	<span class="bt_add" style="opacity:0.4">
					  		<a href="#" onclick="javascript:;" rel="tipsy" title="Incluir Novo Endereço"><img src="${pageContext.request.contextPath}/images/ico_add.gif" border="0" /></a>
					  	</span>
				  	</div>
	
				  	
				  </td>
				  <td>&nbsp;</td>
				  <td>&nbsp;</td>
			  </tr>		
			</table>
		</fieldset>
	</div>
    <br />
     <fieldset style="width:880px!important; margin:5px;">
 		<legend>Endereços Cadastrados</legend>
		<table class="${param.telaEndereco}enderecosGrid"></table>
    </fieldset>
</div>
