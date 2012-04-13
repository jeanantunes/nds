<script type="text/javascript">
	var addConjuge = false;
	
	function opcaoCivilPf(valor){
		if (valor == "CASADO"){
			$(".divConjuge").show();
			addConjuge = true;
		} else {
			$(".divConjuge").hide();
			addConjuge = false;
		}
	}
	
	function cadastrarFiadorCpf(janela){
		
		fecharModalCadastroFiador = true;
		
		var data = "pessoa.nome=" + $("#nomeFiadorCpf").val() + "&" +
		           "pessoa.email=" + $("#emailFiadorCpf").val() + "&" +
		           "pessoa.cpf=" + $("#cpfFiador").val() + "&" +
		           "pessoa.rg=" + $("#rgFiador").val() + "&" +
		           "pessoa.dataNascimento=" + $("#dataNascimentoFiadorCpf").val() + "&" +
		           "pessoa.orgaoEmissor=" + $("#orgaoEmissorFiadorCpf").val() + "&" +
		           "pessoa.ufOrgaoEmissor=" + $("#selectUfOrgaoEmiCpf").val() + "&" +
		           "pessoa.estadoCivil=" + $("#estadoCivilFiadorCpf").val() + "&" +
		           "pessoa.sexo=" + $("#selectSexoFiador").val() + "&" +
		           "pessoa.nacionalidade=" + $("#nacionalidadeFiadorCpf").val() + "&" +
		           "pessoa.natural=" + $("#naturalFiadorCpf").val();
		
		if (addConjuge){ 
		           data = data + "&pessoa.conjuge.nome=" + $("#nomeConjugeCpf").val() + "&" +
		           "pessoa.conjuge.email=" + $("#emailConjugeCpf").val() + "&" +
		           "pessoa.conjuge.cpf=" + $("#cpfConjuge").val() + "&" +
		           "pessoa.conjuge.rg=" + $("#rgConjuge").val() + "&" +
		           "pessoa.conjuge.dataNascimento=" + $("#dataNascimentoConjugeCpf").val() + "&" +
		           "pessoa.conjuge.orgaoEmissor=" + $("#orgaoEmissorConjugeCpf").val() + "&" +
		           "pessoa.conjuge.ufOrgaoEmissor=" + $("#selectUfOrgaoEmiConjugeCpf").val() + "&" +
		           "pessoa.conjuge.sexo=" + $("#selectSexoConjuge").val() + "&" +
		           "pessoa.conjuge.nacionalidade=" + $("#nacionalidadeConjugeCpf").val() + "&" +
		           "pessoa.conjuge.natural=" + $("#naturalConjugeCpf").val();
		}
		
		$.postJSON("<c:url value='/cadastro/fiador/cadastrarFiadorCpf'/>", data, 
			function(result){
					
				if (result[0].tipoMensagem){
					exibirMensagem(result[0].tipoMensagem, result[0].listaMensagens);
				}
				
				if (result[1] != ""){
					$(".pessoasGrid").flexAddData({
						page: result[1].page, total: result[1].total, rows: result[1].rows
					});
					
					$("#gridFiadoresCadastrados").show();
				} else {
					$("#gridFiadoresCadastrados").hide();
				}
				
				if (result[0].tipoMensagem == "SUCCESS"){
					$(janela).dialog("close");
				}
			},
			null,
			true
		);
	}
	
	function limparDadosCadastraisCPF(){
		$('[name="nomeFiadorCpf"]:eq(0)').val("");
        $('[name="emailFiadorCpf"]:eq(0)').val("");
        $('[name="cpfFiador"]:eq(0)').val("");
        $('[name="rgFiador"]:eq(0)').val("");
        $('[name="dataNascimentoFiadorCpf"]:eq(0)').val("");
        $('[name="orgaoEmissorFiadorCpf"]:eq(0)').val("");
        $('[name="selectUfOrgaoEmiCpf"]:eq(0)').val("");
        $('[name="estadoCivilFiadorCpf"]:eq(0)').val("");
        $('[name="selectSexoFiador"]:eq(0)').val("");
        $('[name="nacionalidadeFiadorCpf"]:eq(0)').val("");
        $('[name="naturalFiadorCpf"]:eq(0)').val("");
        $('[name="nomeConjugeCpf"]:eq(0)').val("");
        $('[name="emailConjugeCpf"]:eq(0)').val("");
        $('[name="cpfConjuge"]:eq(0)').val("");
        $('[name="rgConjuge"]:eq(0)').val("");
        $('[name="dataNascimentoConjugeCpf"]:eq(0)').val("");
        $('[name="orgaoEmissorConjugeCpf"]:eq(0)').val("");
        $('[name="selectUfOrgaoEmiConjugeCpf"]:eq(0)').val("");
        $('[name="selectSexoConjuge"]:eq(0)').val("");
        $('[name="nacionalidadeConjugeCpf"]:eq(0)').val("");
        $('[name="naturalConjugeCpf"]:eq(0)').val("");
	}
	
	$(function(){
		$('[name="cpfFiador"]').mask("999.999.999-99");
		$('[name="cpfConjuge"]').mask("999.999.999-99");
		$('[name="rgFiador"]').mask("99.999.999-9");
		$('[name="rgConjuge"]').mask("99.999.999-9");
		$('[name="dataNascimentoFiadorCpf"]').mask("99/99/9999");
		$('[name="dataNascimentoConjugeCpf"]').mask("99/99/9999");
	});
	
	function buscarPessoaCPF(cpf){
		
		var refAba = $("#tab-1").css("display") == "block" ? 0 : 1;
		
		if (cpf != "___.___.___-__"){
			$.postJSON("<c:url value='/cadastro/fiador/buscarPessoaCPF' />", "cpf=" + cpf, 
				function(result) {
					
					if (result){
						$('[name="nomeFiadorCpf"]:eq(' + refAba + ')').val(result[0]);
						$('[name="emailFiadorCpf"]:eq(' + refAba + ')').val(result[1]);
						$('[name="cpfFiador"]:eq(' + refAba + ')').val(result[2]);
						$('[name="rgFiador"]:eq(' + refAba + ')').val(result[3]);
						$('[name="dataNascimentoFiadorCpf"]:eq(' + refAba + ')').val(result[4]);
						$('[name="orgaoEmissorFiadorCpf"]:eq(' + refAba + ')').val(result[5]);
						$('[name="selectUfOrgaoEmiCpf"]:eq(' + refAba + ')').val(result[6]);
						$('[name="estadoCivilFiadorCpf"]:eq(' + refAba + ')').val(result[7]);
						$('[name="selectSexoFiador"]:eq(' + refAba + ')').val(result[8]);
						$('[name="nacionalidadeFiadorCpf"]:eq(' + refAba + ')').val(result[9]);
						$('[name="naturalFiadorCpf"]:eq(' + refAba + ')').val(result[10]);
						
						if (result[7] == "CASADO"){
							
							opcaoCivilPf(result[7]);
							
							$('[name="nomeConjugeCpf"]:eq(' + refAba + ')').val(result[11]);
							$('[name="emailConjugeCpf"]:eq(' + refAba + ')').val(result[12]);
							$('[name="cpfConjuge"]:eq(' + refAba + ')').val(result[13]);
							$('[name="rgConjuge"]:eq(' + refAba + ')').val(result[14]);
							$('[name="dataNascimentoConjugeCpf"]:eq(' + refAba + ')').val(result[15]);
							$('[name="orgaoEmissorConjugeCpf"]:eq(' + refAba + ')').val(result[16]);
							$('[name="selectUfOrgaoEmiConjugeCpf"]:eq(' + refAba + ')').val(result[17]);
							$('[name="selectSexoConjuge"]:eq(' + refAba + ')').val(result[18]);
							$('[name="nacionalidadeConjugeCpf"]:eq(' + refAba + ')').val(result[19]);
							$('[name="naturalConjugeCpf"]:eq(' + refAba + ')').val(result[20]);
						}
					}
				},
				null,
				true
			);
		}
	}
</script>

<table width="754" cellpadding="2" cellspacing="2" style="text-align: left;">
	<tr class="inicioAtividade">
		<td nowrap="nowrap"><strong>Início de Atividade:</strong></td>
		<td>${dataAtual}</td>
		<td>&nbsp;</td>
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr>
		<td width="118">Nome:</td>
		<td width="237"><input type="text" style="width: 230px" id="nomeFiadorCpf" name="nomeFiadorCpf" /></td>
		<td width="134">E-mail:</td>
		<td colspan="3"><input type="text" style="width: 230px" id="emailFiadorCpf" name="emailFiadorCpf" /></td>
	</tr>
	<tr>
		<td>CPF:</td>
		<td>
			<input type="text" style="width: 150px" id="cpfFiador" name="cpfFiador" onblur="buscarPessoaCPF(this.value);"/>
		</td>
		<td>R. G.:</td>
		<td colspan="3"><input type="text" style="width: 150px" id="rgFiador" name="rgFiador" /></td>
	</tr>
	<tr>
		<td>Data Nascimento:</td>
		<td>
			<input type="text" style="width: 150px" id="dataNascimentoFiadorCpf" name="dataNascimentoFiadorCpf" />
		</td>
		<td>Orgão Emissor:</td>
		<td width="59">
			<input type="text" style="width: 50px" id="orgaoEmissorFiadorCpf" name="orgaoEmissorFiadorCpf" />
		</td>
		<td width="31">UF:</td>
		<td width="135">
			<select name="selectUfOrgaoEmiCpf" id="selectUfOrgaoEmiCpf" style="width: 50px">
				<option selected="selected"></option>
				<option>RJ</option>
				<option>SP</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>Estado Civil:</td>
		<td>
			<select name="estadoCivilFiadorCpf" style="width: 155px;" onchange="opcaoCivilPf(this.value);" id="estadoCivilFiadorCpf">
				<option selected="selected">Selecione...</option>
				<option value="SOLTEIRO">Solteiro</option>
				<option value="CASADO">Casado</option>
				<option value="DIVORCIADO">Divorciado</option>
				<option value="VIUVO">Víuvo</option>
			</select>
		</td>
		<td>Sexo:</td>
		<td colspan="3">
			<select name="selectSexoFiador" id="selectSexoFiador" style="width: 155px">
				<option selected="selected">Selecione...</option>
				<option value="MASCULINO">Masculino</option>
				<option value="FEMININO">Feminino</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>Nacionalidade:</td>
		<td>
			<input type="text" style="width: 150px" id="nacionalidadeFiadorCpf" name="nacionalidadeFiadorCpf" />
		</td>
		<td>Natural:</td>
		<td colspan="3">
			<input type="text" style="width: 150px" id="naturalFiadorCpf" name="naturalFiadorCpf" />
		</td>
	</tr>
	<tr class="trSocioPrincipal" style="display: none;">
		<td>Principal:</td>
		<td><input type="checkbox" name="checkboxSocioPrincipal" id="checkboxSocioPrincipal" /></td>
		<td>&nbsp;</td>
		<td colspan="3">&nbsp;</td>
	</tr>
</table>

<div class="divConjuge" style="display: none; margin-left: 5px; margin-top: 5px;">
	<strong>Dados do Conjuge</strong>
	<table width="760" cellpadding="2" cellspacing="2" style="text-align: left;">
		<tr>
			<td width="108">Nome:</td>
			<td width="274"><input type="text" style="width: 230px" id="nomeConjugeCpf" name="nomeConjugeCpf" /></td>
			<td width="118">E-mail:</td>
			<td colspan="3"><input type="text" style="width: 230px" id="emailConjugeCpf" name="emailConjugeCpf" /></td>
		</tr>
		<tr>
			<td>CPF:</td>
			<td><input type="text" style="width: 150px" id="cpfConjuge" name="cpfConjuge"/></td>
			<td>R. G.:</td>
			<td colspan="3"><input type="text" style="width: 175px" id="rgConjuge" name="rgConjuge" /></td>
		</tr>
		<tr>
			<td>Data Nascimento:</td>
			<td><input type="text" style="width: 150px" id="dataNascimentoConjugeCpf" name="dataNascimentoConjugeCpf" /></td>
			<td>Orgão Emissor:</td>
			<td width="63"><input type="text" style="width: 50px" id="orgaoEmissorConjugeCpf" id="name" /></td>
			<td width="26">UF:</td>
			<td width="136">
				<select name="selectUfOrgaoEmiConjugeCpf" id="selectUfOrgaoEmiConjugeCpf" style="width: 50px">
					<option selected="selected"></option>
					<option>RJ</option>
					<option>SP</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>Sexo:</td>
			<td>
				<select name="selectSexoConjuge" id="selectSexoConjuge" style="width: 150px">
					<option selected="selected">Selecione...</option>
					<option value="MASCULINO">Masculino</option>
					<option value="FEMININO">Feminino</option>
				</select>
			</td>
			<td>Nacionalidade:</td>
			<td colspan="3"><input type="text" style="width: 150px" id="nacionalidadeConjugeCpf" name="nacionalidadeConjugeCpf" /></td>
		</tr>
		<tr>
			<td>Natural:</td>
			<td><input type="text" style="width: 175px" id="naturalConjugeCpf" name="naturalConjugeCpf" /></td>
			<td>&nbsp;</td>
			<td colspan="3">&nbsp;</td>
		</tr>
	</table>
</div>