  
 <form name="formDadosBasicoCnpj" id="formDadosBasicoCnpj">
 
  <table width="823" cellpadding="2" cellspacing="2" style="text-align:left;">
  <tr>
      <td><strong>Cota:</strong></td>
      <td><input name="cotaDTO.numeroCota" id="numeroCota" type="text" style="width:100px" /></td>
      <td><strong>Início de Atividade:</strong></td>
      <td><span id="dataInclusao"style="width:100px"></span></td>
  </tr>
    <tr>
      <td>Razão Social:</td>
      <td><input type="text" style="width:230px " name="cotaDTO.razaoSocial" id="razaoSocial"/></td>
      <td>Nome Fantasia:</td>
      <td><input type="text" style="width:230px " name="cotaDTO.nomeFantasia" id="nomeFantasia" /></td>
    </tr>
    <tr>
      <td>CNPJ:</td>
      <td><input type="text" style="width:150px" name="cotaDTO.numeroCnpj" id="numeroCnpj" onblur="COTA_CNPJ.carregarDadosCNPJ('#numeroCnpj')"/></td>
      <td>Inscrição Estadual:</td>
      <td><input type="text" style="width:230px" name="cotaDTO.inscricaoEstadual" id="inscricaoEstadual"/></td>
    </tr>
    <tr>
        <td width="129">Inscrição Municipal:</td>
        <td width="230"><input type="text" style="width:150px" name="cotaDTO.inscricaoMunicipal" id="inscricaoMunicipal" /></td>
        <td width="136">E-mail:</td>
        <td width="300"><input type="text" style="width:230px" name="cotaDTO.email" id="email" onblur="MANTER_COTA.validarEmail('#email')"/></td>
    </tr>
    <tr>
      <td>E-mail NF-e:</td>
      <td><input type="text" style="width:230px " name="cotaDTO.emailNF" id="emailNF" onblur="MANTER_COTA.validarEmail('#emailNF')" /></td>
      <td>Emite NF-e?</td>
      <td><input type="checkbox" name="cotaDTO.emiteNFE" id="emiteNFE" /></td>
    </tr>
    <tr>
      <td>Status:</td>
      <td><input type="text" style="width:230px " id="status" disabled="disabled" name="cotaDTO.status" /></td>
      
      <td>Classificação:</td>
      <td>
      		<select name="cotaDTO.classificacaoSelecionada" id="classificacaoSelecionada" style="width:300px;">
             
          	</select>
      </td>
    </tr>
</table>

<table width="515" border="0" cellspacing="2" cellpadding="2">
      <tr class="linhas">
        <td width="127" nowrap="nowrap"><strong>Cota Base:</strong></td>
        <td><table width="348" border="0" cellspacing="1" cellpadding="2">
          <tr>
            <td width="51">Período:</td>
            <td width="117">
            	<input type="text" name="cotaDTO.inicioPeriodo" id="periodoCotaDe" style="width:80px;" /></td>
            <td width="26" align="center">Até</td>
            <td width="133">
            	<input type="text" name="cotaDTO.fimPeriodo" id="periodoCotaAte" style="width:80px;" /></td>
          </tr>
        </table></td>
      </tr>
      <tr class="linhas">
        <td valign="top"><strong>Utilizar Histórico:</strong></td>
        <td width="374"><table width="348" border="0" cellspacing="1" cellpadding="2">
          <tr>
            <td width="38">Cota:</td>
            <td width="127">
            	<input type="text" name="cotaDTO.historicoPrimeiraCota" id="historicoPrimeiraCota" style="width:70px; margin-right:10px;" onblur="MANTER_COTA.validarCotaHistoricoBase('#historicoPrimeiraCota,#historicoPrimeiraPorcentagem')" /></td>
            <td width="30" align="center">%</td>
            <td width="132">
            	<input type="text" name="cotaDTO.historicoPrimeiraPorcentagem" id="historicoPrimeiraPorcentagem" maxlength="4" style="width:50px;" /></td>
            </tr>
          <tr>
            <td>Cota:</td>
            <td>
            	<input type="text" name="cotaDTO.historicoSegundaCota" id="historicoSegundaCota" style="width:70px; margin-right:10px;" onblur="MANTER_COTA.validarCotaHistoricoBase('#historicoSegundaCota,')"/></td>
            <td align="center">%</td>
            <td>
            	<input type="text" name="cotaDTO.historicoSegundaPorcentagem" id="historicoSegundaPorcentagem" maxlength="4" style="width:50px;" /></td>
            </tr>
          <tr>
            <td>Cota:</td>
            <td>
            	<input type="text" name="cotaDTO.historicoTerceiraCota" id="historicoTerceiraCota" style="width:70px; margin-right:10px;" onblur="MANTER_COTA.validarCotaHistoricoBase('#historicoTerceiraCota')" /></td>
            <td align="center">%</td>
            <td>
            	<input type="text" name="cotaDTO.historicoTerceiraPorcentagem" id="historicoTerceiraPorcentagem" maxlength="4" style="width:50px;" /></td>
          </tr>
        </table></td>
      </tr>
    </table>
    
  </form>