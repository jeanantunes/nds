<head>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/endereco.js"></script>

<script type="text/javascript">

	var ${param.telaEndereco} = new Endereco('${param.telaEndereco}','${param.message}');
	
</script>
    

</head>

<div id="${param.telaEndereco}manutencaoEnderecos">

	<div id="dialog-excluir-end" title="Excluir Endereço">
		<p>Confirma a exclusão desse endereço?</p>
	</div>

	<form name="${param.telaEndereco}formEnderecos" id="${param.telaEndereco}formEnderecos">
		
		<input type="hidden" name="enderecoAssociacao.id" id="${param.telaEndereco}idEndereco"/>
		
		<input type="hidden" name="tela" id="${param.telaEndereco}telaEndereco" value="${param.telaEndereco}"/>
		
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
					   name="enderecoAssociacao.endereco.cep" id="${param.telaEndereco}cep" />

					<span class="classPesquisar" title="Pesquisar Cep.">
						<a href="javascript:;" onclick="${param.telaEndereco}.pesquisarEnderecoPorCep();">&nbsp;</a>
					</span></td>
			</tr>
			<tr>
				<td>Tipo Logradouro:</td>
				<td>
					<input type="text" style="width:230px" 
						   name="enderecoAssociacao.endereco.tipoLogradouro" id="${param.telaEndereco}tipoLogradouro" />
				</td>
				<td>Logradouro:</td>
				<td>
					<input type="text" style="width:250px" 
						   name="enderecoAssociacao.endereco.logradouro" id="${param.telaEndereco}logradouro" />
				</td>
			</tr>
			<tr>			
				<td>Número:</td>
				<td>
					<input type="text" style="width:50px" 
						   name="enderecoAssociacao.endereco.numero" id="${param.telaEndereco}numero" maxlength="9" />
				</td>
				<td>Complemento:</td>
				<td>
					<input type="text" style="width:250px" 
						   name="enderecoAssociacao.endereco.complemento" id="${param.telaEndereco}complemento" />
				</td>
			</tr>
			<tr>
			
				<td>Bairro:</td>
				<td>
					<input type="text"  style="width:230px" 
						   name="enderecoAssociacao.endereco.bairro" id="${param.telaEndereco}bairro" />
				</td>
				<td>Cidade:</td>
				<td>
					<input type="text" style="width:250px" 
						   name="enderecoAssociacao.endereco.cidade" id="${param.telaEndereco}cidade" />
				</td>
				
			</tr>
			<tr>
				<td>UF:</td>
				<td>
					<input type="text" style="width:50px;text-transform:uppercase" 
						   name="enderecoAssociacao.endereco.uf" id="${param.telaEndereco}uf"  />
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
			  	<span class="bt_add" id="${param.telaEndereco}btnIncluirNovoEndereco">
			  		<a href="javascript:;" onclick="${param.telaEndereco}.incluirNovoEndereco();" id="${param.telaEndereco}linkIncluirNovoEndereco">Incluir Novo</a>
			  	</span>
			  </td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
		  </tr>		
		</table>
	</form>
    <br />
    <label>
    	<strong>Endereços Cadastrados</strong>
    </label>
    <br />

    <table class="${param.telaEndereco}enderecosGrid"></table>
    
</div>
