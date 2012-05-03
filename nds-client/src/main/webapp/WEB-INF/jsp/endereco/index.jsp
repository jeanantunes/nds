<head>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/endereco.js"></script>

<script type="text/javascript">

	var ${param.telaEndereco} = new Endereco('${param.telaEndereco}');
	
</script>
    

</head>

<div id="manutencaoEnderecos">

	<div id="dialog-excluir-end" title="Excluir Endere�o">
		<p>Confirma a exclusão desse endereço?</p>
	</div>

	<form name="formEnderecos" id="formEnderecos">
		
		<input type="hidden" name="enderecoAssociacao.id" id="idEndereco"/>
		
		<input type="hidden" name="tela" id="telaEndereco" value="${param.telaEndereco}"/>
		
		<table width="754" cellpadding="2" cellspacing="2" style="text-align:left ">
			<tr>
				
				<td width="99">Tipo Endereço:</td>
				<td width="310">
					<select  style="width:157px" 
							 name="enderecoAssociacao.tipoEndereco" 
							 id="tipoEndereco">
						<option value="COMERCIAL">Comercial</option>
						<option value="LOCAL_ENTREGA">Local de Entrega</option>
						<option value="RESIDENCIAL">Residencial</option>
						<option value="COBRANCA">Cobrança</option>
					</select>
				</td>
				<td width="76">CEP:</td>
				<td width="241">
				<input type="text" style="float:left; margin-right:5px;" 
					   name="enderecoAssociacao.endereco.cep" id="cep" />

					<span class="classPesquisar" title="Pesquisar Cep.">
						<a href="javascript:;" onclick="${param.telaEndereco}.pesquisarEnderecoPorCep();">&nbsp;</a>
					</span></td>
			</tr>
			<tr>
				<td>Tipo Logradouro:</td>
				<td>
					<input type="text" style="width:230px" 
						   name="enderecoAssociacao.endereco.tipoLogradouro" id="tipoLogradouro" />
				</td>
				<td>Logradouro:</td>
				<td>
					<input type="text" style="width:250px" 
						   name="enderecoAssociacao.endereco.logradouro" id="logradouro" />
				</td>
			</tr>
			<tr>			
				<td>Número:</td>
				<td>
					<input type="text" style="width:50px" 
						   name="enderecoAssociacao.endereco.numero" id="numero" maxlength="9" />
				</td>
				<td>Complemento:</td>
				<td>
					<input type="text" style="width:250px" 
						   name="enderecoAssociacao.endereco.complemento" id="complemento" />
				</td>
			</tr>
			<tr>
			
				<td>Bairro:</td>
				<td>
					<input type="text"  style="width:230px" 
						   name="enderecoAssociacao.endereco.bairro" id="bairro" />
				</td>
				<td>Cidade:</td>
				<td>
					<input type="text" style="width:250px" 
						   name="enderecoAssociacao.endereco.cidade" id="cidade" />
				</td>
				
			</tr>
			<tr>
				<td>UF:</td>
				<td>
					<input type="text" style="width:50px;text-transform:uppercase" 
						   name="enderecoAssociacao.endereco.uf" id="uf"  />
				</td>
			    <td>Principal:</td>
			   	<td>
				  	<input type="checkbox" id="principal" 
				  		   name="enderecoAssociacao.enderecoPrincipal"/>
			  	</td>
			    <td>&nbsp;</td>
			    <td>&nbsp;</td>
		  </tr>
			<tr>
			  <td>&nbsp;</td>
			  <td>
			  	<span class="bt_add" id="btnIncluirNovoEndereco">
			  		<a href="javascript:;" onclick="${param.telaEndereco}.incluirNovoEndereco();" id="linkIncluirNovoEndereco">Incluir Novo</a>
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

    <table class="enderecosGrid"></table>
    
</div>
