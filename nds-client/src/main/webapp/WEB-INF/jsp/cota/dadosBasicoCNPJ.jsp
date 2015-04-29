 
 <div id="dadosCNPJ" style="display: none;">
  
 <form name="formDadosBasicoCnpj" id="formDadosBasicoCnpj">
 <fieldset style="width:880px!important; margin:5px;">
 <legend>Dados Cadastrais</legend>
  <table width="870" cellpadding="2" cellspacing="2" style="text-align:left;">
    <tr>
      <td><strong>Cota:</strong></td>
      <td><input name="cotaDTO.numeroCota" id="numeroCotaCNPJ" maxlength="16" type="text" style="width:100px" />
      	     <select name="cotaDTO.tipoDistribuicaoCota" id="cotaDTO.tipoDistribuicaoCNPJ" style="width:110px;" onchange="MANTER_COTA.verificaTipoCota(this);">
	        <option value="" selected="selected">Selecione...</option>
	        <option value="ALTERNATIVO">Alternativo</option>
	        <option value="CONVENCIONAL">Convencional</option>
	      </select>
      
      </td>
      <td><strong>Início de Atividade:</strong></td>
      <td><span id="dataInclusao"style="width:100px"></span></td>
    </tr>
  
    <tr>
      <td>Forma de Pagamento:</td>
	  <td><select name="cotaDTO.tipoCotaFinanceiro" id="tipoCotaFinanceiro" style="width: 150px;" onchange="parametroCobrancaCotaController.exibeDevolveEncalhe(this.value);"
	  onfocus="this.oldValue = this.value;">
	          <option value="">Selecione</option>
			  <c:forEach varStatus="counter" var="itemTipoCota"
				  items="${listaTiposCota}">
				  <option value="${itemTipoCota.key}">${itemTipoCota.value}</option>
			  </c:forEach>
	  </select></td>
	  <td>Utiliza IPV?</td>
      <td><input type="checkbox" name="cotaDTO.utilizaIPV" id="utilizaIPV" /></td>
    </tr>
  
    <tr>
      <td>Razão Social:</td>
      <td><input type="text" style="width:230px " maxlength="250" name="cotaDTO.razaoSocial" id="razaoSocial"/></td>
      <td>Nome Fantasia:</td>
      <td><input type="text" style="width:230px" maxlength="250" name="cotaDTO.nomeFantasia" id="nomeFantasia" /></td>
    </tr>
    <tr>
      <td>CNPJ:</td>
      <td><input type="text" style="width:150px" name="cotaDTO.numeroCnpj" id="numeroCnpj" onblur="COTA_CNPJ.carregarDadosCNPJ('#numeroCnpj')"/></td>
      <td>Inscrição Estadual:</td>
      <td><input type="text" style="width:230px" maxlength="20" name="cotaDTO.inscricaoEstadual" id="inscricaoEstadual"/></td>
    </tr>
    <tr>
        <td>Inscrição Municipal:</td>
        <td width="312"><input type="text" maxlength="15" style="width:150px" name="cotaDTO.inscricaoMunicipal" id="inscricaoMunicipal" /></td>
        <td width="126">E-mail:</td>
        <td width="300"><input type="text" maxlength="200" style="width:230px" name="cotaDTO.email" id="email" onblur="MANTER_COTA.validarEmail('#email')"/></td>
    </tr>
    <tr>
      <td>E-mail NF-e:</td>
      <td><input type="text" style="width:230px" maxlength="200" name="cotaDTO.emailNF" id="emailNF" onblur="MANTER_COTA.validarEmail('#emailNF')" /></td>
      <td>Emite NF-e?</td>
      <td><input type="checkbox" name="cotaDTO.emiteNFE" id="emiteNFE" /></td>
    </tr>
    <tr>
      <td>Status:</td>
      <td><input type="text" style="width:230px " id="status" disabled="disabled" name="cotaDTO.status" /></td>
      
      <td>Classificação da Cota:</td>
      <td>
      		<div id="manter-selectClassificacao"></div>
      </td>
    </tr>
    
</table>

<table width="870" border="0" cellspacing="1" cellpadding="1">
 <tr>
      <td width="424" valign="top" class="antigosProp">
        <strong>Antigos Proprietários</strong><br />
        <table class="antigosProprietariosGridCNPJ"></table>
        <span class="bt_novos" title="Alterar Titularidade" id="btnAlterarTitularidadeCNPJ">
          <a href="javascript:;" style="opacity: 0.6" isEdicao="true">
            <img src="${pageContext.request.contextPath}/images/ico_add_novo.gif" hspace="5" border="0" />Alterar Titularidade
          </a>
        </span>
      </td> 
      
      <td width="439" valign="top"><table width="439" border="0" cellspacing="2" cellpadding="2">
      
        <!-- 
      <tr class="linhas">
        <td width="127" nowrap="nowrap"><strong>Cota Base:</strong></td>
        <td id="percentualCotaBase">
        </td>
      </tr>
        -->
      
      <tr class="linhas">
      <td width="127" nowrap="nowrap"><strong></strong></td>
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
        <td valign="top"><strong>Cota Base:</strong></td>
        <td width="374"><table width="348" border="0" cellspacing="1" cellpadding="2">
          <tr>
            <td width="38">Cota:</td>
            <td width="127">
            	<input type="text" name="cotaDTO.historicoPrimeiraCota" id="historicoPrimeiraCota" maxlength="16" style="width:50px; margin-right:10px;" onblur="MANTER_COTA.validarCotaHistoricoBase('#historicoPrimeiraCota,#historicoPrimeiraPorcentagem')" /></td>
            <td width="30" align="center">Nome:</td>
            <td width="132">
            	<input type="text" name="cotaDTO.historicoPrimeiraPorcentagem" id="historicoPrimeiraPorcentagem" maxlength="4" style="width:190px;"/></td>
            </tr>
          <tr>
            <td>Cota:</td>
            <td>
            	<input type="text" name="cotaDTO.historicoSegundaCota" id="historicoSegundaCota" maxlength="16" style="width:50px; margin-right:10px;" onblur="MANTER_COTA.validarCotaHistoricoBase('#historicoSegundaCota,#historicoSegundaPorcentagem')"/></td>
            <td align="center">Nome:</td>
            <td>
            	<input type="text" name="cotaDTO.historicoSegundaPorcentagem" id="historicoSegundaPorcentagem" maxlength="4" style="width:190px;"/></td>
            </tr>
          <tr>
            <td>Cota:</td>
            <td>
            	<input type="text" name="cotaDTO.historicoTerceiraCota" id="historicoTerceiraCota" maxlength="16" style="width:50px; margin-right:10px;" onblur="MANTER_COTA.validarCotaHistoricoBase('#historicoTerceiraCota,#historicoTerceiraPorcentagem')" /></td>
            <td align="center">Nome:</td>
            <td>
            	<input type="text" name="cotaDTO.historicoTerceiraPorcentagem" id="historicoTerceiraPorcentagem" maxlength="4" style="width:190px;"/></td>
          </tr>
        </table></td>
      </tr> 
    </table>
   </td>
  </tr> 
</table>   
</fieldset>
  </form>
  
  </div>