<div id="tabCadastro">
	<br/>
	<fieldset style="width: 380px !important; float: left; margin-left:10px;">
		<legend>Cadastro Distribuidor</legend>
		<table width="370" border="0" cellpadding="2" cellspacing="1"
			class="filtro">
			<tr>
				<td width="139">Razão Social:</td>
				<td width="220">
					<input type="text" name="razaoSocial"
						   id="razaoSocial" style="width: 220px;" maxlength="255"
						   value="${parametrosDistribuidor.razaoSocial}" />
				</td>
			</tr>
			<tr>
				<td>Nome Fantasia:</td>
				<td>
					<input type="text" name="nomeFantasia"
						   id="nomeFantasia" style="width: 220px;" maxlength="255"
						   value="${parametrosDistribuidor.nomeFantasia}" />
				</td>
			</tr>
			
			<tr>
				<td colspan="2">
					<div id="div_imagem_logotipo"></div>
				</td>
			</tr>
			<tr>
				<td>Logo:</td>
				<td>
					<input type="file" name="logo" id="logo" onchange="parametrosDistribuidorController.salvarLogo();" />
				</td>
			</tr>
			<tr>
				<td>CNPJ:</td>
				<td>
					<input type="text" name="cnpj"
						   id="cnpj" style="width: 220px;" maxlength="255"
						   value="${parametrosDistribuidor.cnpj}" />
				</td>
			</tr>
			<tr>
				<td>Insc. Estadual:</td>
				<td><input type="text" name="inscricaoEstadual"
						   id="inscricaoEstadual" style="width: 220px;" maxlength="14"
						   value="${parametrosDistribuidor.inscricaoEstadual}" />
				</td>
			</tr>
			<tr>
				<td>Insc. Municipal:</td>
				<td>
					<input type="text" name="inscricaoMunicipal"
						   id="inscricaoMunicipal" style="width: 220px;" maxlength="15"
						   value="${parametrosDistribuidor.inscricaoMunicipal}" />
				</td>
			</tr>
			<tr>
				<td>E-mail:</td>
				<td>
					<input type="text" name="email" id="email" maxlength="255"
						   style="width: 220px;" value="${parametrosDistribuidor.email}" />
				</td>
			</tr>
			<tr>
				<td>Cód. Distribuidor Dinap:</td>
				<td>
					<input type="text" name="codigoDistribuidorDinap"
						   id="codigoDistribuidorDinap" style="width: 220px;" maxlength="255"
						   value="${parametrosDistribuidor.codigoDistribuidorDinap}" />
				</td>
			</tr>
			<tr>
				<td>Cód. Distribuidor FC:</td>
				<td>
					<input type="text" name="codigoDistribuidorFC"
						   id="codigoDistribuidorFC" style="width: 220px;" maxlength="255"
						   value="${parametrosDistribuidor.codigoDistribuidorFC}" />
				</td>
			</tr>
		</table>
	</fieldset>

	<fieldset
		style="width: 440px !important; margin-bottom: 5px; float: left;">
		<legend>Endereço</legend>
		<table width="443" cellpadding="2" cellspacing="2"
			style="text-align: left">
			<tr>
				<td width="99">Tipo Endereço:</td>
				<td width="180">
					<select style="width: 150px" id="tipoEndereco" name="tipoEndereco">
						<option value="COMERCIAL">Comercial</option>
						<option value="LOCAL_ENTREGA">Local de Entrega</option>
						<option value="RESIDENCIAL">Residencial</option>
						<option value="COBRANCA">Cobrança</option>
					</select>
				</td>
				<td width="54">&nbsp;</td>
				<td width="82">&nbsp;</td>
			</tr>
			<tr>
				<td>CEP:</td>
				<td width="180">
					<input type="text" style="float: left; margin-right: 5px;" 
						   id="DISTRIBUIDORcep" name="cep"
						   value="${parametrosDistribuidor.endereco.cep}"
						   maxlength="9" />
					<span class="classPesquisar" title="Pesquisar Cep.">
						<a href="javascript:;" onclick="endereco.pesquisarEnderecoPorCep();">&nbsp;</a>
					</span>
				</td>
				<td>UF:</td>
				<td>
					<select style="width: 50px" id="DISTRIBUIDORuf" name="uf"></select>
				</td>
			</tr>
			<tr>
				<td>Cidade:</td>
				<td>
					<input type="text" style="width: 180px" id="cidade" name="cidade"
						   value="${parametrosDistribuidor.endereco.localidade}"
						   onkeyup="endereco.autoCompletarLocalidades();"
						   onblur="endereco.autoCompletarLocalidades(true);"
						   maxlength="60"/>
					<input type="hidden" id="DISTRIBUIDORcodigoCidadeIBGE" name="codigoCidadeIBGE"
						   value="${parametrosDistribuidor.endereco.codigoCidadeIBGE}"
						   />
				</td>
			</tr>
			<tr>
				<td>Bairro:</td>
				<td>
					<input type="text" style="width: 180px" id="bairro" name="bairro"
						   value="${parametrosDistribuidor.endereco.bairro}"
						   onkeyup="endereco.autoCompletarBairros();"
						   onblur="endereco.autoCompletarBairros(true);"
						   maxlength="60"/>
				</td>
				<td>Tipo :</td>
				<td>
					<input type="text" style="width: 50px"
						   id="tipoLogradouro" name="tipoLogradouro"
						   value="${parametrosDistribuidor.endereco.tipoLogradouro}"
						   maxlength="255" />
				</td>
			</tr>
			<tr>
				<td>Logradouro:</td>
				<td>
					<input type="text" style="width: 180px" id="logradouro" name="logradouro"
						   value="${parametrosDistribuidor.endereco.logradouro}"
						   onkeyup="endereco.autoCompletarLogradouros();"
						   onblur="endereco.autoCompletarLogradouros(true);"
						   maxlength="60"/>
				</td>
				<td>Número:</td>
				<td>
					<input type="text" style="width: 50px" id="numero" name="numero"
						   value="${parametrosDistribuidor.endereco.numero}"
						   maxlength="11" />
				</td>
			</tr>
			<tr>
				<td>Complemento:</td>
				<td>
					<input type="text" style="width: 180px" id="complemento" name="complemento"
						   value="${parametrosDistribuidor.endereco.complemento}"
						   maxlength="60" />
				</td>
			</tr>
			
			<tr>
				<td>Telefone: </td>
				<td>
					<input type="text" style="width:40px" id="numeroDDD" maxlength="2" value="${parametrosDistribuidor.numeroDDD}"/>-
					<input type="text" style="width:70px" id="numeroTelefone" maxlength="8" value="${parametrosDistribuidor.numeroTelefone}"/>
				</td>
			</tr>
			
		</table>
	</fieldset>
	
	<br clear="all" />
</div>