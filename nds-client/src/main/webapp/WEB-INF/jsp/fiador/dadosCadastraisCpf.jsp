<script type="text/javascript">
	function opcaoCivilPf(valor){
		if (valor == "CASADO"){
			$(".divConjuge").show();
		} else {
			$(".divConjuge").hide();
		}
	}
	
	function cadastrarFiadorCpf(janela){
		var data = "fiador.nome=" + $("#nomeFiadorCpf").val() + "&" +
		           "fiador.email=" + $("#emailFiadorCpf").val() + "&" +
		           "fiador.cpf=" + $("#cpfFiador").val() + "&" +
		           "fiador.rg=" + $("#rgFiador").val() + "&" +
		           "fiador.dataNascimento=" + $("#dataNascimentoFiadorCpf").val() + "&" +
		           "fiador.orgaoEmissor=" + $("#orgaoEmissorFiadorCpf").val() + "&" +
		           "fiador.ufOrgaoEmissor=" + $("#selectUfOrgaoEmiCpf").val() + "&" +
		           "fiador.estadoCivil=" + $("#estadoCivilFiadorCpf").val() + "&" +
		           "fiador.sexo=" + $("#selectSexoFiador").val() + "&" +
		           "fiador.nacionalidade=" + $("#nacionalidadeFiadorCpf").val() + "&" +
		           "fiador.natural=" + $("#naturalFiadorCpf").val() + "&" +
		           
		           "conjuge.nome=" + $("#nomeConjugeCpf").val() + "&" +
		           "conjuge.email=" + $("#emailConjugeCpf").val() + "&" +
		           "conjuge.cpf=" + $("#cpfConjuge").val() + "&" +
		           "conjuge.rg=" + $("#rgConjuge").val() + "&" +
		           "conjuge.dataNascimento=" + $("#dataNascimentoConjugeCpf").val() + "&" +
		           "conjuge.orgaoEmissor=" + $("#orgaoEmissorConjugeCpf").val() + "&" +
		           "conjuge.ufOrgaoEmissor=" + $("#selectUfOrgaoEmiConjugeCpf").val() + "&" +
		           "conjuge.sexo=" + $("#selectSexoConjuge").val() + "&" +
		           "conjuge.nacionalidade=" + $("#nacionalidadeConjugeCpf").val() + "&" +
		           "conjuge.natural=" + $("#naturalConjugeCpf").val();
		
		$.postJSON("<c:url value='/cadastro/fiador/cadastrarFiadorCpf' />", data, 
				function(){
					$(janela).dialog("close");
					$("#effect").show("highlight", {}, 1000, callback);
					$(".grids").show();
				}
		);
	}
</script>

<table width="754" cellpadding="2" cellspacing="2" style="text-align: left;">
	<tr>
		<td nowrap="nowrap"><strong>Início de Atividade:</strong></td>
		<td>${dataAtual}</td>
		<td>&nbsp;</td>
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr>
		<td width="118">Nome:</td>
		<td width="237"><input type="text" style="width: 230px" id="nomeFiadorCpf" /></td>
		<td width="134">E-mail:</td>
		<td colspan="3"><input type="text" style="width: 230px" id="emailFiadorCpf" /></td>
	</tr>
	<tr>
		<td>CPF:</td>
		<td><input type="text" style="width: 150px" id="cpfFiador" /></td>
		<td>R. G.:</td>
		<td colspan="3"><input type="text" style="width: 150px" id="rgFiador" /></td>
	</tr>
	<tr>
		<td>Data Nascimento:</td>
		<td><input type="text" style="width: 150px" id="dataNascimentoFiadorCpf" /></td>
		<td>Orgão Emissor:</td>
		<td width="59"><input type="text" style="width: 50px" id="orgaoEmissorFiadorCpf" /></td>
		<td width="31">UF:</td>
		<td width="135">
			<select name="select" id="selectUfOrgaoEmiCpf" style="width: 50px">
				<option selected="selected"></option>
				<option>RJ</option>
				<option>SP</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>Estado Civil:</td>
		<td>
			<select name="select4" style="width: 155px;" onchange="opcaoCivilPf(this.value);" id="estadoCivilFiadorCpf">
				<option selected="selected">Selecione...</option>
				<option value="SOLTEIRO">Solteiro</option>
				<option value="CASADO">Casado</option>
				<option value="DIVORCIADO">Divorciado</option>
				<option value="VIUVO">Víuvo</option>
			</select>
		</td>
		<td>Sexo:</td>
		<td colspan="3">
			<select name="select11" id="selectSexoFiador" style="width: 155px">
				<option selected="selected">Selecione...</option>
				<option value="MASCULINO">Masculino</option>
				<option value="FEMININO">Feminino</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>Nacionalidade:</td>
		<td><input type="text" style="width: 150px" id="nacionalidadeFiadorCpf" /></td>
		<td>Natural:</td>
		<td colspan="3"><input type="text" style="width: 150px" id="naturalFiadorCpf" /></td>
	</tr>
	<tr class="trSocioPrincipal" style="display: none;">
		<td>Principal:</td>
		<td><input type="checkbox" name="checkbox" id="checkbox" /></td>
		<td>&nbsp;</td>
		<td colspan="3">&nbsp;</td>
	</tr>
</table>

<div class="divConjuge" style="display: none; margin-left: 5px; margin-top: 5px;">
	<strong>Dados do Conjuge</strong>
	<table width="760" cellpadding="2" cellspacing="2" style="text-align: left;">
		<tr>
			<td width="108">Nome:</td>
			<td width="274"><input type="text" style="width: 230px" id="nomeConjugeCpf" /></td>
			<td width="118">E-mail:</td>
			<td colspan="3"><input type="text" style="width: 230px" id="emailConjugeCpf" /></td>
		</tr>
		<tr>
			<td>CPF:</td>
			<td><input type="text" style="width: 150px" id="cpfConjuge" /></td>
			<td>R. G.:</td>
			<td colspan="3"><input type="text" style="width: 175px" id="rgConjuge" /></td>
		</tr>
		<tr>
			<td>Data Nascimento:</td>
			<td><input type="text" style="width: 150px" id="dataNascimentoConjugeCpf" /></td>
			<td>Orgão Emissor:</td>
			<td width="63"><input type="text" style="width: 50px" id="orgaoEmissorConjugeCpf" /></td>
			<td width="26">UF:</td>
			<td width="136">
				<select name="select6" id="selectUfOrgaoEmiConjugeCpf" style="width: 50px">
					<option selected="selected"></option>
					<option>RJ</option>
					<option>SP</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>Sexo:</td>
			<td>
				<select name="select2" id="selectSexoConjuge" style="width: 150px">
					<option selected="selected">Selecione...</option>
					<option value="MASCULINO">Masculino</option>
					<option value="FEMININO">Feminino</option>
				</select>
			</td>
			<td>Nacionalidade:</td>
			<td colspan="3"><input type="text" style="width: 150px" id="nacionalidadeConjugeCpf" /></td>
		</tr>
		<tr>
			<td>Natural:</td>
			<td><input type="text" style="width: 175px" id="naturalConjugeCpf" /></td>
			<td>&nbsp;</td>
			<td colspan="3">&nbsp;</td>
		</tr>
	</table>
</div>