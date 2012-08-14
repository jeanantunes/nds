<head>
<script>
	function showComissaoPF(show) {

		if (show) {

			$(".comissionadoPf").show();

		} else {

			$(".comissionadoPf").hide();
		}
	}

	function obterPessoaFisica(cpf) {

		if (!cpf || cpf == "") {
			return;
		}
		
		$.postJSON(
			'<c:url value="/cadastro/entregador/obterPessoaFisica" />',
			{'cpf': cpf},
			function(result) {

				if (result.id) {

					$("#nomeEntregador").val(result.nome);
					$("#apelido").val(result.apelido);
					$("#cpf").val(result.cpf).mask("999.999.999-99");
					$("#rg").val(result.rg).mask("99.999.999-9");
					$("#dataNascimento").val(result.dataNascimento).mask("99/99/9999");
					$("#orgaoEmissor").val(result.orgaoEmissor);
					$("#ufOrgaoEmissor").val(result.ufOrgaoEmissor);
					$("#estadoCivil").val(result.estadoCivil);
					$("#sexo").val(result.sexo);
					$("#nacionalidade").val(result.nacionalidade);
					$("#natural").val(result.natural);
					$("#emailPF").val(result.email);

				}
			},
			function(result) {

				$("#nomeEntregador").val("");
				$("#apelido").val("");
				$("#cpf").val("");
				$("#rg").val("");
				$("#dataNascimento").val("");
				$("#orgaoEmissor").val("");
				$("#ufOrgaoEmissor").val("");
				$("#estadoCivil").val("");
				$("#sexo").val("");
				$("#nacionalidade").val("");
				$("#natural").val("");
				$("#emailPF").val("");

				exibirMensagemDialog(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens
				);
			},
			true
		);
	}
	
	$(function() {

		$("#dataNascimento").mask("99/99/9999");
		$("#cpf").mask("999.999.999-99");
		$("#rgProcuradorProcuracaoPF").mask("99.999.999-9");
		$("input[id^='percentualComissaoPF']").maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		}); 	
	});
	
</script>
</head>
<div id="dadosCadastraisPF" style="display:none">
	<form id="formDadosEntregadorPF">
		<input type="hidden" name="entregador.id" id="idEntregadorPF"/>
       <table width="754" cellpadding="2" cellspacing="2" style="text-align:left;">
       <tr>
         <td><strong>Código:</strong></td>
         <td><input type="text" name="entregador.codigo" maxlength="20" id="codigoEntregadorPF" style="width:100px" /></td>
         <td><strong>Início de Atividade:</strong></td>
         <td colspan="3"><span id="inicioAtividadePF"></span></td>
       </tr>
       <tr>
           <td width="118">Nome:</td>
           <td width="237"><input type="text"  id="nomeEntregador" style="width:230px " /></td>
           <td width="134">Apelido:</td>
           <td colspan="3"><input type="text"  id="apelido" style="width:230px" /></td>
       </tr>
       <tr>
         <td>CPF:</td>
         <td><input type="text" name="cpf" id="cpf" style="width:150px" onblur="obterPessoaFisica($(this).val())" /></td>
         <td>R. G.:</td>
         <td colspan="3"><input type="text" id="rg" style="width:150px" /></td>
       </tr>
       <tr>
         <td>Data Nascimento:</td>
         <td><input type="text" id="dataNascimento" style="width:150px" /></td>
         <td>Orgão Emissor:</td>
         <td width="59"><input type="text" id="orgaoEmissor" style="width:50px" /></td>
         <td width="31">UF:</td>
         <td width="135"><select id="ufOrgaoEmissor" style="width:50px">
           <option selected="selected"> </option>
           <option>RJ</option>
           <option>SP</option>
         </select></td>
       </tr>
       <tr>
         <td>Estado Civil:</td>
         <td><select id="estadoCivil" style="width:155px;">
           <option selected="selected"></option>
           <option value="SOLTEIRO">Solteiro</option>
           <option value="CASADO">Casado</option>
           <option value="DIVORCIADO">Divorciado</option>
           <option value="VIUVO">Víuvo</option>
         </select></td>
         <td>Sexo:</td>
         <td colspan="3"><select id="sexo" style="width:155px">
             <option selected="selected"></option>
             <option value="MASCULINO">Masculino</option>
             <option value="FEMININO">Feminino</option>
           </select></td>
       </tr>
       <tr>
         <td>Nacionalidade:</td>
         <td><input type="text" style="width:150px" id="nacionalidade" /></td>
         <td>Natural:</td>
         <td colspan="3"><input type="text" id="natural" style="width:150px" /></td>
       </tr>
       <tr>
         <td>E-mail:</td>
         <td><input type="text" id="emailPF" style="width:230px" /></td>
         <td>&nbsp;</td>
         <td colspan="3">&nbsp;</td>
       </tr>
       <tr>
         <td>É comissionado?</td>
         <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
           <tr>
             <td width="10%"><input type="radio" name="comissionadoPF" id="comissionadoPF" onclick="showComissaoPF(true);" /></td>
             <td width="15%">Sim</td>
             <td width="10%"><input type="radio" name="comissionadoPF" id="naoComissionadoPF" onclick="showComissaoPF();" /></td>
             <td width="65%">Não</td>
           </tr>
         </table></td>
         <td><span class="comissionadoPf" style="display:none; font-size: 9px;">
     Informe o % de Comissão s/ Faturamento (Preço Capa):
     </span></td>
         <td colspan="3"><span class="comissionadoPf" style="display:none">
     <input type="text" name="entregador.percentualComissao" maxlength="19" 
     		id="percentualComissaoPF" style="width:100px" />
     </span></td>
       </tr>
     </table>
        </form>
 </div>