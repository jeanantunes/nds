<table width="754" cellpadding="2" cellspacing="2" style="text-align: left;">
	<tr class="fiadorController-inicioAtividade">
		<td nowrap="nowrap"><strong>Início de Atividade:</strong></td>
		<td class="fiadorController-inicioAtividadeNovo" style="display: none;">${dataAtual}</td>
		<td class="fiadorController-inicioAtividadeEdicao" style="display: none;"></td>
		<td>&nbsp;</td>
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr>
		<td width="118">Nome:</td>
		<td width="237"><input type="text" style="width: 230px" id="${param.prefix}nomeFiadorCpf" name="nomeFiadorCpf" maxlength="255"/></td>
		<td width="134">E-mail:</td>
		<td colspan="3"><input type="text" style="width: 230px" id="${param.prefix}emailFiadorCpf" name="emailFiadorCpf" maxlength="255"/></td>
	</tr>
	<tr>
		<td>CPF:</td>
		<td>
			<input type="text" style="width: 150px" id="${param.prefix}cpfFiador" name="cpfFiador" />
		</td>
		<td>R. G.:</td>
		<td colspan="3"><input type="text" style="width: 150px" id="${param.prefix}rgFiador" name="rgFiador" maxlength="15" /></td>
	</tr>
	<tr>
		<td>Data Nascimento:</td>
		<td>
			<input type="text" style="width: 150px" id="${param.prefix}dataNascimentoFiadorCpf" name="dataNascimentoFiadorCpf" />
		</td>
		<td>Orgão Emissor:</td>
		<td width="59">
			<input type="text" style="width: 50px" id="${param.prefix}orgaoEmissorFiadorCpf" class="justLetter" name="orgaoEmissorFiadorCpf" maxlength="255"/>
		</td>
		<td width="31">UF:</td>
		<td width="135">
			<input type="text" name="selectUfOrgaoEmiCpf" id="${param.prefix}selectUfOrgaoEmiCpf" style="width: 50px; text-transform:uppercase;"/>
		</td>
	</tr>
	<tr>
		<td>Estado Civil:</td>
		<td>
			<select name="estadoCivilFiadorCpf" style="width: 155px;" onchange="fiadorController.opcaoCivilPf(this.value,'${param.prefix}');" id="${param.prefix}estadoCivilFiadorCpf">
				<option selected="selected">Selecione...</option>
				<option value="SOLTEIRO">Solteiro</option>
				<option value="CASADO">Casado</option>
				<option value="DIVORCIADO">Divorciado</option>
				<option value="VIUVO">Víuvo</option>
			</select>
		</td>
		<td>Sexo:</td>
		<td colspan="3">
			<select name="selectSexoFiador" id="${param.prefix}selectSexoFiador" style="width: 155px">
				<option selected="selected">Selecione...</option>
				<option value="MASCULINO">Masculino</option>
				<option value="FEMININO">Feminino</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>Nacionalidade:</td>
		<td>
			<input type="text" style="width: 150px" id="${param.prefix}nacionalidadeFiadorCpf" name="nacionalidadeFiadorCpf" maxlength="255"/>
		</td>
		<td>Natural:</td>
		<td colspan="3">
			<input type="text" style="width: 150px" id="${param.prefix}naturalFiadorCpf" name="naturalFiadorCpf" maxlength="255"/>
		</td>
	</tr>
	<tr class="trSocioPrincipal" style="display: none;">
		<td>Principal:</td>
		<td><input type="checkbox" name="checkboxSocioPrincipal" id="${param.prefix}checkboxSocioPrincipal" /></td>
		<td>&nbsp;</td>
		<td colspan="3">&nbsp;</td>
	</tr>
</table>

<div class="${param.prefix}divConjuge" style="display: none; margin-left: 5px; margin-top: 5px;">
	<strong>Dados do Conjuge</strong>
	<table width="760" cellpadding="2" cellspacing="2" style="text-align: left;">
		<tr>
			<td width="108">Nome:</td>
			<td width="274"><input type="text" style="width: 230px" id="${param.prefix}nomeConjugeCpf" name="nomeConjugeCpf" maxlength="255"/></td>
			<td width="118">E-mail:</td>
			<td colspan="3"><input type="text" style="width: 230px" id="${param.prefix}emailConjugeCpf" name="emailConjugeCpf" maxlength="255"/></td>
		</tr>
		<tr>
			<td>CPF:</td>
			<td><input type="text" style="width: 150px" id="${param.prefix}cpfConjuge" name="cpfConjuge" onblur="fiadorController.buscarPessoaCPF(this.value, false);"/></td>
			<td>R. G.:</td>
			<td colspan="3"><input type="text" style="width: 175px" id="${param.prefix}rgConjuge" name="rgConjuge" maxlength="15" /></td>
		</tr>
		<tr>
			<td>Data Nascimento:</td>
			<td><input type="text" style="width: 150px" id="${param.prefix}dataNascimentoConjugeCpf" name="dataNascimentoConjugeCpf" /></td>
			<td>Orgão Emissor:</td>
			<td width="63"><input type="text" style="width: 50px" id="${param.prefix}orgaoEmissorConjugeCpf" name="orgaoEmissorConjugeCpf" maxlength="255" /></td>
			<td width="26">UF:</td>
			<td width="136">
				<input type="text" name="selectUfOrgaoEmiConjugeCpf" id="${param.prefix}selectUfOrgaoEmiConjugeCpf" style="width: 50px; text-transform:uppercase;" />
			</td>
		</tr>
		<tr>
			<td>Sexo:</td>
			<td>
				<select name="selectSexoConjuge" id="${param.prefix}selectSexoConjuge" style="width: 150px">
					<option selected="selected">Selecione...</option>
					<option value="MASCULINO">Masculino</option>
					<option value="FEMININO">Feminino</option>
				</select>
			</td>
			<td>Nacionalidade:</td>
			<td colspan="3"><input type="text" style="width: 150px" id="${param.prefix}nacionalidadeConjugeCpf" name="nacionalidadeConjugeCpf" maxlength="255"/></td>
		</tr>
		<tr>
			<td>Natural:</td>
			<td><input type="text" style="width: 175px" id="${param.prefix}naturalConjugeCpf" name="naturalConjugeCpf" maxlength="255"/></td>
			<td>&nbsp;</td>
			<td colspan="3">&nbsp;</td>
		</tr>
	</table>
</div>