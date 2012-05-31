<head>
<script>

	function showProcuracaoPF(show) {

		if (show) {

			$(".arqProcuracaoPf").show();

		} else {

			$(".arqProcuracaoPf").hide();
		}
	}

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
         <td><span class="comissionadoPf" style="display:none">
     Informe o %:
     </span></td>
         <td colspan="3"><span class="comissionadoPf" style="display:none">
     <input type="text" name="entregador.percentualComissao" maxlength="19" 
     		id="percentualComissaoPF" style="width:100px" />
     </span></td>
       </tr>
       <tr>
         <td>Procuração:</td>
         <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
           <tr>
             <td width="10%"><input type="radio" name="procuracaoPF" id="procuracaoPF" value="radio" onclick="showProcuracaoPF(true);" /></td>
             <td width="15%">Sim</td>
             <td width="10%"><input type="radio" name="procuracaoPF" id="naoProcuracaoPF" value="radio" onclick="showProcuracaoPF();" /></td>
             <td width="64%">Não</td>
           </tr>
         </table></td>
         <td>&nbsp;</td>
         <td colspan="3">&nbsp;</td>
       </tr>
     </table>
    <br />
     
     <div class="arqProcuracaoPf" style="display:none;">
        	<b>Dados da Procuração</b>
            <br />
           <table width="765" cellpadding="2" cellspacing="2">
             <tr>
               <td width="113">Nome Jornaleiro:</td>
               <td width="370"><input type="text" style="width:230px " readonly="readonly" id="nomeJornaleiroProcuracaoPF" /></td>
               <td width="86">Box:</td>
               <td width="168"><input type="text" style="width:80px " readonly="readonly" id="boxProcuracaoPF" /></td>
             </tr>
             <tr>
               <td>Cota:</td>
               <td><input type="text" name="procuracaoEntregador.cota.numeroCota" 
               			  id="numeroCotaProcuracaoPF" onblur="obterCota($(this).val(), true)" 
               			  style="width:80px; float:left; margin-right:5px;" maxlength="11"/>
               <span class="classPesquisar"><a href="javascript:;" onclick="obterCota($(this).val())">&nbsp;</a></span></td>
               <td>&nbsp;</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td>Nacionalidade:</td>
               <td><input type="text" style="width:230px " readonly="readonly" id="nacionalidadeProcuracaoPF" /></td>
               <td>Estado Civil:</td>
               <td><select name="" style="width:155px;" disabled="disabled" id="estadoCivilProcuracaoPF">
            <option selected="selected"></option>
            <option value="SOLTEIRO">Solteiro</option>
            <option value="CASADO">Casado</option>
            <option value="DIVORCIADO">Divorciado</option>
            <option value="VIUVO">Víuvo</option>
          </select></td>
             </tr>
             <tr>
               <td>Endereço PDV:</td>
               <td><input type="text" style="width:350px "  readonly="readonly" id="enderecoPDVPrincipalProcuracaoPF" /></td>
               <td>Cep:</td>
               <td><input type="text" name="textfield2" readonly="readonly" id="cepProcuracaoPF" style="width:120px; float:left; margin-right:5px;"/></td>
             </tr>
             <tr>
               <td>Bairro:</td>
               <td><input type="text" style="width:230px " readonly="readonly" id="bairroProcuracaoPF" /></td>
               <td>Cidade:</td>
               <td><input type="text" style="width:150px " readonly="readonly" id="cidadeProcuracaoPF" /></td>
             </tr>
             <tr>
               <td>N° Permissão:</td>
               <td><input type="text" style="width:230px " id="numeroPermissaoPF"  /></td>
               <td>RG:</td>
               <td><input type="text" style="width:150px " readonly="readonly" id="rgProcuracaoPF" /></td>
             </tr>
             <tr>
               <td>CPF/CNPJ:</td>
               <td><input type="text" style="width:230px " readonly="readonly" id="cpfProcuracaoPF" /></td>
               <td>&nbsp;</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td>&nbsp;</td>
               <td>&nbsp;</td>
               <td>&nbsp;</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td>Procurador:</td>
               <td><input type="text" style="width:350px " maxlength="255" id="nomeProcuradorProcuracaoPF" /></td>
               <td>Estado Civil:</td>
               <td><select name="" style="width:155px;" id="estadoCivilProcuradorProcuracaoPF">
            <option selected="selected"></option>
            <option value="SOLTEIRO">Solteiro</option>
            <option value="CASADO">Casado</option>
            <option value="DIVORCIADO">Divorciado</option>
            <option value="VIUVO">Víuvo</option>
          </select></td>
             </tr>
             <tr>
               <td>Endereço:</td>
               <td><input type="text" style="width:350px " maxlength="255" id="enderecoProcuradorProcuracaoPF" /></td>
               <td>RG</td>
               <td><input type="text" style="width:150px " id="rgProcuradorProcuracaoPF"/></td>
             </tr>
             <tr>
               <td>Nacionalidade:</td>
               <td><input type="text" style="width:230px " maxlength="255" id="nacionalidadeProcuradorProcuracaoPF"/></td>
               <td>Profissão:</td>
               <td><input type="text" style="width:150px " maxlength="255" id="profissaoProcuradorProcuracaoPF" /></td>
             </tr>
           </table>
           <br clear="all" />

			<span class="bt_novos" title="Imprimir"><a href="javascript:;" onclick="imprimirProcuracao(true)">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir Procuração</a>
			</span>
            <br clear="all" />

      	</div>
        </form>
	<br />

 </div>