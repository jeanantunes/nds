<head>
<script>

	function showProcuracaoPJ(show) {

		if (show) {

			$(".arqProcuracaoPJ").show();

		} else {

			$(".arqProcuracaoPJ").hide();
		}
	}

	function showComissaoPJ(show) {

		if (show) {

			$(".comissionadoPJ").show();

		} else {

			$(".comissionadoPJ").hide();
		}
	}

	$(function() {

		$("#cnpj").mask("99.999.999/9999-99");
		$("#rgProcuradorProcuracaoPJ").mask("99.999.999-9");
		$("input[id^='percentualComissaoPJ']").maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		}); 	
	});

	function obterPessoaJuridica(cnpj) {

		if (!cnpj || cnpj == "") {
			return;
		}
		
		$.postJSON(
			'<c:url value="/cadastro/entregador/obterPessoaJuridica" />',
			{'cnpj': cnpj},
			function(result) {

				if (result.id) {

					$("#razaoSocial").val(result.razaoSocial);
					$("#nomeFantasia").val(result.nomeFantasia);
					$("#cnpj").val(result.cnpj).mask("99.999.999/9999-99");
					$("#inscricaoEstadual").val(result.inscricaoEstadual).mask("999.999.999.999");
					$("#emailPJ").val(result.email);	

				} else {

					if ($("#razaoSocial").val() != '') {

						$("#razaoSocial").focus();
					}
				}
			},
			function(result) {

				$("#razaoSocial").val("");
				$("#nomeFantasia").val("");
				$("#cnpj").val("");
				$("#inscricaoEstadual").val("");
				$("#emailPJ").val("");

				exibirMensagemDialog(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens
				);
			},
			true
		);
	}

</script>
</head>
<div id="dadosCadastraisPJ" style="display:none">
<form id="formDadosEntregadorPJ">
	<input type="hidden" name="entregador.id" id="idEntregadorPJ"/>
    <table width="765" cellpadding="2" cellspacing="2" style="text-align:left;">
    <tr>
      <td><strong>Código:</strong></td>
      <td><input type="text" name="entregador.codigo" id="codigoEntregadorPJ" maxlength="20" style="width:100px" /></td>
      <td><strong>Início de Atividade:</strong></td>
      <td><span id="inicioAtividadePJ" name="entregador.inicioAtividade"></span></td>
    </tr>
    <tr>
      <td>Razão Social:</td>
      <td><input type="text" id="razaoSocial" style="width:230px " /></td>
      <td>Nome Fantasia:</td>
      <td><input type="text" id="nomeFantasia" style="width:230px " /></td>
    </tr>
    <tr>
      <td>CNPJ:</td>
      <td><input type="text" id="cnpj" style="width:230px" onblur="obterPessoaJuridica($(this).val())" /></td>
      <td>Inscrição Estadual:</td>
      <td><input type="text" id="inscricaoEstadual" style="width:230px" /></td>
    </tr>
    <tr>
        <td width="128">E-mail:</td>
        <td width="234"><input type="text" id="emailPJ" style="width:230px" /></td>
        <td width="138">&nbsp;</td>
        <td width="237">&nbsp;</td>
    </tr>
    <tr>
      <td>É comissionado?</td>
      <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="10%"><input type="radio" name="comissaoPJ" id="comissionadoPJ" value="radio" onclick="showComissaoPJ(true);" /></td>
    <td width="23%">Sim</td>
    <td width="9%"><input type="radio" name="comissaoPJ" id="naoComissionadoPJ" value="radio" onclick="showComissaoPJ();" /></td>
    <td width="58%">Não</td>
  </tr>
</table></td>
      <td><span class="comissionadoPJ">
      Informe o %:
      </span></td>
      <td><span class="comissionadoPJ">
      <input type="text" name="entregador.percentualComissao" maxlength="19" id="percentualComissaoPJ" style="width:100px" />
      </span></td>
    </tr>
    <tr>
      <td>Procuração:</td>
      <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="10%"><input type="radio" name="procuracaoPJ" id="procuracaoPJ" value="radio" onclick="showProcuracaoPJ(true);" /></td>
          <td width="23%">Sim</td>
          <td width="9%"><input type="radio" name="procuracaoPJ" id="naoProcuracaoPJ" value="radio" onclick="showProcuracaoPJ();" /></td>
          <td width="58%">Não</td>
        </tr>
      </table></td>
      <td>&nbsp;

      </td>
      <td>&nbsp;</td>
    </tr>
     </table>

     <br clear="all" />
      <div class="arqProcuracaoPJ" style="display:none;">
        	<b>Dados da Procuração</b>
            <br />
           <table width="765" cellpadding="2" cellspacing="2">
             <tr>
               <td width="113">Nome Jornaleiro:</td>
               <td width="370"><input type="text" style="width:230px " readonly="readonly" id="nomeJornaleiroProcuracaoPJ" /></td>
               <td width="86">Box:</td>
               <td width="168"><input type="text" style="width:80px " readonly="readonly" id="boxProcuracaoPJ" /></td>
             </tr>
             <tr>
               <td>Cota:</td>
               <td><input type="text" name="procuracaoEntregador.cota.numeroCota" 
               			  id="numeroCotaProcuracaoPJ" 
               			  style="width:80px; float:left; margin-right:5px;" maxlength="11"/>
               <span class="classPesquisar"><a href="javascript:;" onclick="obterCota($('#numeroCotaProcuracaoPJ').val()">&nbsp;</a></span></td>
               <td>&nbsp;</td>
               <td>&nbsp;</td>
             </tr>
             <tr>
               <td>Nacionalidade:</td>
               <td><input type="text" style="width:230px " readonly="readonly" id="nacionalidadeProcuracaoPJ" /></td>
               <td>Estado Civil:</td>
               <td><select name="" style="width:155px;" disabled="disabled" id="estadoCivilProcuracaoPJ">
            <option selected="selected"></option>
            <option value="SOLTEIRO">Solteiro</option>
            <option value="CASADO">Casado</option>
            <option value="DIVORCIADO">Divorciado</option>
            <option value="VIUVO">Víuvo</option>
          </select></td>
             </tr>
             <tr>
               <td>Endereço PDV:</td>
               <td><input type="text" style="width:350px "  readonly="readonly" id="enderecoPDVPrincipalProcuracaoPJ" /></td>
               <td>Cep:</td>
               <td><input type="text" name="textfield2" readonly="readonly" id="cepProcuracaoPJ" style="width:120px; float:left; margin-right:5px;"/></td>
             </tr>
             <tr>
               <td>Bairro:</td>
               <td><input type="text" style="width:230px " readonly="readonly" id="bairroProcuracaoPJ" /></td>
               <td>Cidade:</td>
               <td><input type="text" style="width:150px " readonly="readonly" id="cidadeProcuracaoPJ" /></td>
             </tr>
             <tr>
               <td>N° Permissão:</td>
               <td><input type="text" style="width:230px " id="numeroPermissaoPJ"  /></td>
               <td>RG:</td>
               <td><input type="text" style="width:150px " readonly="readonly" id="rgProcuracaoPJ" /></td>
             </tr>
             <tr>
               <td>CPF/CNPJ:</td>
               <td><input type="text" style="width:230px " readonly="readonly" id="cpfProcuracaoPJ" /></td>
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
               <td><input type="text" style="width:350px " name="procuracaoEntregador.procurador" 
               			  id="nomeProcuradorProcuracaoPJ" maxlength="255" /></td>
               <td>Estado Civil:</td>
               <td><select name="" style="width:155px;" name="procuracaoEntregador.estadoCivil" id="estadoCivilProcuradorProcuracaoPJ">
            <option selected="selected"></option>
            <option value="SOLTEIRO">Solteiro</option>
            <option value="CASADO">Casado</option>
            <option value="DIVORCIADO">Divorciado</option>
            <option value="VIUVO">Víuvo</option>
          </select></td>
             </tr>
             <tr>
               <td>Endereço:</td>
               <td><input type="text" style="width:350px " maxlength="255" id="enderecoProcuradorProcuracaoPJ" /></td>
               <td>RG</td>
               <td><input type="text" style="width:150px " id="rgProcuradorProcuracaoPJ"/></td>
             </tr>
             <tr>
               <td>Nacionalidade:</td>
               <td><input type="text" style="width:230px " maxlength="255" id="nacionalidadeProcuradorProcuracaoPJ"/></td>
               <td>Profissão:</td>
               <td><input type="text" style="width:150px " maxlength="255" id="profissaoProcuradorProcuracaoPJ" /></td>
             </tr>
           </table>
           <br clear="all" />

			<span class="bt_novos" title="Imprimir"><a href="javascript:;" onclick="imprimirProcuracao()">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir Procuração</a>
			</span>
            <br clear="all" />

      	</div>
      <br />
</form>
        </div>