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

</script>

<div id="dadosCadastraisPJ" style="display:none">
<form id="formDadosEntregadorPJ">
	<input type="hidden" name="entregador.id" id="idEntregadorPJ"/>
    <table width="765" cellpadding="2" cellspacing="2" style="text-align:left;">
    <tr>
      <td><strong>Código:</strong></td>
      <td><input type="text" name="entregador.codigo" id="codigoEntregadorPJ" style="width:100px" /></td>
      <td><strong>Início de Atividade:</strong></td>
      <td><span id="inicioAtividadePJ" name="entregador.inicioAtividade"></span></td>
    </tr>
    <tr>
      <td>Razão Social:</td>
      <td><input type="text" name="pessoaJuridica.razaoSocial" id="razaoSocial" style="width:230px " /></td>
      <td>Nome Fantasia:</td>
      <td><input type="text" name="pessoaJuridica.nomeFantasia" id="nomeFantasia" style="width:230px " /></td>
    </tr>
    <tr>
      <td>CNPJ:</td>
      <td><input type="text" name="pessoaJuridica.cnpj" id="cnpj" style="width:230px" /></td>
      <td>Inscrição Estadual:</td>
      <td><input type="text" name="pessoaJuridica.inscricaoEstadual" id="inscricaoEstadual" style="width:230px" /></td>
    </tr>
    <tr>
        <td width="128">E-mail:</td>
        <td width="234"><input type="text" name="pessoaJuridica.email" id="emailPJ" style="width:230px" /></td>
        <td width="138">&nbsp;</td>
        <td width="237">&nbsp;</td>
    </tr>
    <tr>
      <td>É comissionado?</td>
      <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="10%"><input type="radio" name="comissaoPJ" id="comissionadoPJ" value="radio" onclick="showComissaoPJ(true);" /></td>
    <td width="23%">Sim</td>
    <td width="9%"><input type="radio" name="naoComissaoPJ" id="naoComissionadoPJ" value="radio" onclick="showComissaoPJ();" /></td>
    <td width="58%">Não</td>
  </tr>
</table></td>
      <td><span class="comissionadoPJ">
      Informe o %:
      </span></td>
      <td><span class="comissionadoPJ">
      <input type="text" name="entregador.percentualComissao" id="percentualComissaoPJ" style="width:100px" />
      </span></td>
    </tr>
    <tr>
      <td>Procuração:</td>
      <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="10%"><input type="radio" name="procuracaoPJ" id="procuracaoPJ" value="radio" onclick="showProcuracaoPJ(true);" /></td>
          <td width="23%">Sim</td>
          <td width="9%"><input type="radio" name="naoProcuracaoPJ" id="naoProcuracaoPJ" value="radio" onclick="showProcuracaoPJ();" /></td>
          <td width="58%">Não</td>
        </tr>
      </table></td>
      <td>&nbsp;
      	
      </td>
      <td>&nbsp;</td>
    </tr>
     </table>
</form>
     <br clear="all" />
     <div class="arqProcuracaoPJ" style="display:none;">
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
            <option selected="selected">Selecione...</option>
            <option>Solteiro</option>
            <option>Casado</option>
            <option>Divorciado</option>
            <option>Víuvo</option>
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
               <td><input type="text" style="width:230px " /></td>
               <td>RG:</td>
               <td><input type="text" style="width:150px " /></td>
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
               <td><select name="" style="width:155px;">
            <option selected="selected">Selecione...</option>
            <option>Solteiro</option>
            <option>Casado</option>
            <option>Divorciado</option>
            <option>Víuvo</option>
          </select></td>
             </tr>
             <tr>
               <td>Endereço:</td>
               <td><input type="text" style="width:350px " /></td>
               <td>RG</td>
               <td><input type="text" style="width:150px " /></td>
             </tr>
             <tr>
               <td>Nacionalidade:</td>
               <td><input type="text" style="width:230px " /></td>
               <td>Profissão:</td>
               <td><input type="text" style="width:150px " /></td>
             </tr>
           </table>
           <br clear="all" />

			<span class="bt_novos" title="Imprimir"><a href="javascript:;">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir Procuração</a>
			</span>
            <br clear="all" />

      	</div>
      <br />

        </div>