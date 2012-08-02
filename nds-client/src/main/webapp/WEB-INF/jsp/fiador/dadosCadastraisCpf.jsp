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
					$("#cpfFiador").removeAttr("disabled");
					$("#cpfConjuge").removeAttr("disabled");
				}
			},
			function (){
				fecharModalCadastroFiador = false;
			},
			true
		);
	}
	
	function limparDadosCadastraisCPF(indiceAba){
		
		$('[name="nomeFiadorCpf"]:eq('+ indiceAba +')').val("");
        $('[name="emailFiadorCpf"]:eq('+ indiceAba +')').val("");
        $('[name="cpfFiador"]:eq('+ indiceAba +')').val("");
        $('[name="rgFiador"]:eq('+ indiceAba +')').val("");
        $('[name="dataNascimentoFiadorCpf"]:eq('+ indiceAba +')').val("");
        $('[name="orgaoEmissorFiadorCpf"]:eq('+ indiceAba +')').val("");
        $('[name="selectUfOrgaoEmiCpf"]:eq('+ indiceAba +')').val("");
        $('[name="estadoCivilFiadorCpf"]:eq('+ indiceAba +')').val("");
        $('[name="selectSexoFiador"]:eq('+ indiceAba +')').val("");
        $('[name="nacionalidadeFiadorCpf"]:eq('+ indiceAba +')').val("");
        $('[name="naturalFiadorCpf"]:eq('+ indiceAba +')').val("");
        $('[name="nomeConjugeCpf"]:eq('+ indiceAba +')').val("");
        $('[name="emailConjugeCpf"]:eq('+ indiceAba +')').val("");
        $('[name="cpfConjuge"]:eq('+ indiceAba +')').val("");
        $('[name="rgConjuge"]:eq('+ indiceAba +')').val("");
        $('[name="dataNascimentoConjugeCpf"]:eq('+ indiceAba +')').val("");
        $('[name="orgaoEmissorConjugeCpf"]:eq('+ indiceAba +')').val("");
        $('[name="selectUfOrgaoEmiConjugeCpf"]:eq('+ indiceAba +')').val("");
        $('[name="selectSexoConjuge"]:eq('+ indiceAba +')').val("");
        $('[name="nacionalidadeConjugeCpf"]:eq('+ indiceAba +')').val("");
        $('[name="naturalConjugeCpf"]:eq('+ indiceAba +')').val("");
        $('[name="checkboxSocioPrincipal"]:eq('+ indiceAba +')').uncheck();
        enableFields(indiceAba);
	}
	
	function enableFields(indiceAba) {
		$('[name="nomeFiadorCpf"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="emailFiadorCpf"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="cpfFiador"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="rgFiador"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="dataNascimentoFiadorCpf"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="orgaoEmissorFiadorCpf"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="selectUfOrgaoEmiCpf"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="estadoCivilFiadorCpf"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="selectSexoFiador"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="nacionalidadeFiadorCpf"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="naturalFiadorCpf"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="nomeConjugeCpf"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="emailConjugeCpf"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="cpfConjuge"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="rgConjuge"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="dataNascimentoConjugeCpf"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="orgaoEmissorConjugeCpf"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="selectUfOrgaoEmiConjugeCpf"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="selectSexoConjuge"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="nacionalidadeConjugeCpf"]:eq('+ indiceAba +')').attr("disabled", false);
        $('[name="naturalConjugeCpf"]:eq('+ indiceAba +')').attr("disabled", false);
	}
	
	$(function(){
		$('[name="cpfFiador"]').mask("999.999.999-99");
		$('[name="cpfConjuge"]').mask("999.999.999-99");
		$('[name="dataNascimentoFiadorCpf"]').mask("99/99/9999");
		$('[name="dataNascimentoConjugeCpf"]').mask("99/99/9999");
		$('[name="selectUfOrgaoEmiCpf"]').mask("aa");
		$('[name="selectUfOrgaoEmiConjugeCpf"]').mask("aa");
	});
	
	function buscarPessoaCPF(cpf, fiador){
		
		var refAba = $("#tab-1").css("display") == "block" ? 0 : 1;
		
		if (cpf != "___.___.___-__" && cpf != ""){
			
			var data = "cpf=" + cpf + "&isFiador=" + fiador + "&cpfConjuge=" + $('[name="cpfFiador"]:eq(' + refAba + ')').val() +
				"&socio=" + (refAba == 0 ? "false" : "true");
			
			$.postJSON("<c:url value='/cadastro/fiador/buscarPessoaCPF' />", data, 
				function(result) {
				
					if (result[0] != undefined){
						
						if (fiador){
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
						} else {
							
							$('[name="nomeConjugeCpf"]:eq(' + refAba + ')').val(result[0]);
							$('[name="emailConjugeCpf"]:eq(' + refAba + ')').val(result[1]);
							$('[name="cpfConjuge"]:eq(' + refAba + ')').val(result[2]);
							$('[name="rgConjuge"]:eq(' + refAba + ')').val(result[3]);
							$('[name="dataNascimentoConjugeCpf"]:eq(' + refAba + ')').val(result[4]);
							$('[name="orgaoEmissorConjugeCpf"]:eq(' + refAba + ')').val(result[5]);
							$('[name="selectUfOrgaoEmiConjugeCpf"]:eq(' + refAba + ')').val(result[6]);
							$('[name="selectSexoConjuge"]:eq(' + refAba + ')').val(result[8]);
							$('[name="nacionalidadeConjugeCpf"]:eq(' + refAba + ')').val(result[9]);
							$('[name="naturalConjugeCpf"]:eq(' + refAba + ')').val(result[10]);
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
		<td class="inicioAtividadeNovo" style="display: none;">${dataAtual}</td>
		<td class="inicioAtividadeEdicao" style="display: none;"></td>
		<td>&nbsp;</td>
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr>
		<td width="118">Nome:</td>
		<td width="237"><input type="text" style="width: 230px" id="nomeFiadorCpf" name="nomeFiadorCpf" maxlength="255"/></td>
		<td width="134">E-mail:</td>
		<td colspan="3"><input type="text" style="width: 230px" id="emailFiadorCpf" name="emailFiadorCpf" maxlength="255"/></td>
	</tr>
	<tr>
		<td>CPF:</td>
		<td>
			<input type="text" style="width: 150px" id="cpfFiador" name="cpfFiador" onblur="buscarPessoaCPF(this.value, true);"/>
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
			<input type="text" style="width: 50px" id="orgaoEmissorFiadorCpf" name="orgaoEmissorFiadorCpf" maxlength="255"/>
		</td>
		<td width="31">UF:</td>
		<td width="135">
			<input type="text" name="selectUfOrgaoEmiCpf" id="selectUfOrgaoEmiCpf" style="width: 50px; text-transform:uppercase;"/>
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
			<input type="text" style="width: 150px" id="nacionalidadeFiadorCpf" name="nacionalidadeFiadorCpf" maxlength="255"/>
		</td>
		<td>Natural:</td>
		<td colspan="3">
			<input type="text" style="width: 150px" id="naturalFiadorCpf" name="naturalFiadorCpf" maxlength="255"/>
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
			<td width="274"><input type="text" style="width: 230px" id="nomeConjugeCpf" name="nomeConjugeCpf" maxlength="255"/></td>
			<td width="118">E-mail:</td>
			<td colspan="3"><input type="text" style="width: 230px" id="emailConjugeCpf" name="emailConjugeCpf" maxlength="255"/></td>
		</tr>
		<tr>
			<td>CPF:</td>
			<td><input type="text" style="width: 150px" id="cpfConjuge" name="cpfConjuge" onblur="buscarPessoaCPF(this.value, false);"/></td>
			<td>R. G.:</td>
			<td colspan="3"><input type="text" style="width: 175px" id="rgConjuge" name="rgConjuge" /></td>
		</tr>
		<tr>
			<td>Data Nascimento:</td>
			<td><input type="text" style="width: 150px" id="dataNascimentoConjugeCpf" name="dataNascimentoConjugeCpf" /></td>
			<td>Orgão Emissor:</td>
			<td width="63"><input type="text" style="width: 50px" id="orgaoEmissorConjugeCpf" name="orgaoEmissorConjugeCpf" maxlength="255" /></td>
			<td width="26">UF:</td>
			<td width="136">
				<input type="text" name="selectUfOrgaoEmiConjugeCpf" id="selectUfOrgaoEmiConjugeCpf" style="width: 50px; text-transform:uppercase;" />
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
			<td colspan="3"><input type="text" style="width: 150px" id="nacionalidadeConjugeCpf" name="nacionalidadeConjugeCpf" maxlength="255"/></td>
		</tr>
		<tr>
			<td>Natural:</td>
			<td><input type="text" style="width: 175px" id="naturalConjugeCpf" name="naturalConjugeCpf" maxlength="255"/></td>
			<td>&nbsp;</td>
			<td colspan="3">&nbsp;</td>
		</tr>
	</table>
</div>