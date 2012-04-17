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
	
	$(function() {
		
		$("#dataNascimento").mask("99/99/9999");
		$("#rg").mask("99.999.999-9");
		$("#cpf").mask("999.999.999-99");
	});
	
</script>

<div id="dadosCadastraisPF" style="display:none">
	<form id="formDadosEntregadorPF">
		<input type="hidden" name="entregador.id" id="idEntregadorPF"/>
       <table width="754" cellpadding="2" cellspacing="2" style="text-align:left;">
       <tr>
         <td><strong>Código:</strong></td>
         <td><input type="text" name="entregador.codigo" id="codigoEntregadorPF" style="width:100px" /></td>
         <td><strong>Início de Atividade:</strong></td>
         <td colspan="3"><span id="inicioAtividade" name="entregador.inicioAtividadePF"></span></td>
       </tr>
       <tr>
           <td width="118">Nome:</td>
           <td width="237"><input type="text" name="pessoaFisica.nome" id="nomeEntregador" style="width:230px " /></td>
           <td width="134">Apelido:</td>
           <td colspan="3"><input type="text" name="pessoaFisica.apelido" id="apelido" style="width:230px" /></td>
       </tr>
       <tr>
         <td>CPF:</td>
         <td><input type="text" name="pessoaFisica.cpf" id="cpf" style="width:150px" /></td>
         <td>R. G.:</td>
         <td colspan="3"><input type="text" name="pessoaFisica.rg" id="rg" style="width:150px" /></td>
       </tr>
       <tr>
         <td>Data Nascimento:</td>
         <td><input type="text" name="pessoaFisica.dataNascimento" id="dataNascimento" style="width:150px" /></td>
         <td>Orgão Emissor:</td>
         <td width="59"><input type="text" name="pessoaFisica.orgaoEmissor" id="orgaoEmissor" style="width:50px" /></td>
         <td width="31">UF:</td>
         <td width="135"><select name="pessoaFisica.ufOrgaoEmissor" id="ufOrgaoEmissor" style="width:50px">
           <option selected="selected"> </option>
           <option>RJ</option>
           <option>SP</option>
         </select></td>
       </tr>
       <tr>
         <td>Estado Civil:</td>
         <td><select name="pessoaFisica.estadoCivil" id="estadoCivil" style="width:155px;">
           <option selected="selected"></option>
           <option value="SOLTEIRO">Solteiro</option>
           <option value="CASADO">Casado</option>
           <option value="DIVORCIADO">Divorciado</option>
           <option value="VIUVO">Víuvo</option>
         </select></td>
         <td>Sexo:</td>
         <td colspan="3"><select name="pessoaFisica.sexo" id="sexo" style="width:155px">
             <option selected="selected"></option>
             <option value="MASCULINO">Masculino</option>
             <option value="FEMININO">Feminino</option>
           </select></td>
       </tr>
       <tr>
         <td>Nacionalidade:</td>
         <td><input type="text" style="width:150px" /></td>
         <td>Natural:</td>
         <td colspan="3"><input type="text" name="pessoaFisica.nacionalidade" id="nacionalidade" style="width:150px" /></td>
       </tr>
       <tr>
         <td>E-mail:</td>
         <td><input type="text" name="pessoaFisica.email" id="emailPF" style="width:230px" /></td>
         <td>&nbsp;</td>
         <td colspan="3">&nbsp;</td>
       </tr>
       <tr>
         <td>É comissionado?</td>
         <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
           <tr>
             <td width="10%"><input type="radio" name="comissionado" id="comissionadoPF" onclick="showComissaoPF(true);" /></td>
             <td width="15%">Sim</td>
             <td width="10%"><input type="radio" name="naoComissionado" id="naoComissionadoPF" onclick="showComissaoPF();" /></td>
             <td width="65%">Não</td>
           </tr>
         </table></td>
         <td><span class="comissionadoPf" style="display:none">
     Informe o %:
     </span></td>
         <td colspan="3"><span class="comissionadoPf" style="display:none">
     <input type="text" name="entregador.percentualComissao" id="percentualComissaoPF" style="width:100px" />
     </span></td>
       </tr>
       <tr>
         <td>Procuração:</td>
         <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
           <tr>
             <td width="10%"><input type="radio" name="procuracao" id="procuracaoPF" value="radio" onclick="showProcuracaoPF(true);" /></td>
             <td width="15%">Sim</td>
             <td width="10%"><input type="radio" name="semProcuracao" id="naoProcuracaoPF" value="radio" onclick="showProcuracaoPF();" /></td>
             <td width="64%">Não</td>
           </tr>
         </table></td>
         <td>&nbsp;</td>
         <td colspan="3">&nbsp;</td>
       </tr>
     </table>
</form>
    <br />
     <div class="arqProcuracaoPf" style="display:none;">
    
       	<b>Dados da Procuração</b>
           <br />
          <table width="765" cellpadding="2" cellspacing="2">
            <tr>
              <td width="113">Nome Jornaleiro:</td>
              <td width="370"><input type="text" style="width:230px " /></td>
              <td width="86">Box:</td>
              <td width="168"><input type="text" style="width:80px " /></td>
            </tr>
            <tr>
              <td>Cota:</td>
              <td><input type="text" name="textfield2" id="textfield2" style="width:80px; float:left; margin-right:5px;"/><span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>Nacionalidade:</td>
              <td><input type="text" style="width:230px " /></td>
              <td>Estado Civil:</td>
              <td><select name="" style="width:155px;">
           <option selected="selected"></option>
           <option value="SOLTEIRO">Solteiro</option>
           <option value="CASADO">Casado</option>
           <option value="DIVORCIADO">Divorciado</option>
           <option value="VIUVO">Víuvo</option>
         </select></td>
            </tr>
            <tr>
              <td>Endereço PDV:</td>
              <td><input type="text" style="width:350px " /></td>
              <td>Cep:</td>
              <td><input type="text" name="textfield2" id="textfield2" style="width:120px; float:left; margin-right:5px;"/><span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
            </tr>
            <tr>
              <td>Bairro:</td>
              <td><input type="text" style="width:230px " /></td>
              <td>Cidade:</td>
              <td><input type="text" style="width:150px " /></td>
            </tr>
            <tr>
              <td>N° Permissão:</td>
              <td><input type="text" name="procuracaoEntregador.numeroPermissao" style="width:230px " /></td>
              <td>RG:</td>
              <td><input type="text" name="procuracaoEntregador.rg" style="width:150px " /></td>
            </tr>
            <tr>
              <td>CPF:</td>
              <td><input type="text" style="width:230px " /></td>
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
              <td><input type="text" style="width:350px " /></td>
              <td>Estado Civil:</td>
              <td><select name="procuracaoEntregador.estadoCivil" style="width:155px;">
           <option selected="selected">Selecione...</option>
           <option>Solteiro</option>
           <option>Casado</option>
           <option>Divorciado</option>
           <option>Víuvo</option>
         </select></td>
            </tr>
            <tr>
              <td>Endereço:</td>
              <td><input type="text" name="procuracaoEntregador.endereco" style="width:350px " /></td>
              <td>RG</td>
              <td><input type="text" name="procuracaoEntregador.rg" style="width:150px " /></td>
            </tr>
            <tr>
              <td>Nacionalidade:</td>
              <td><input type="text" name="procuracaoEntregador.nacionalidade" style="width:230px " /></td>
              <td>Profissão:</td>
              <td><input type="text" name="procuracaoEntregador.profissao" style="width:150px " /></td>
            </tr>
          </table>
          <br />

          <span class="bt_novos" title="Imprimir"><a href="javascript:;">
          	<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir Procuração</a>
          </span>
          <br clear="all" />
	</div>
	
	
	<br />

 </div>