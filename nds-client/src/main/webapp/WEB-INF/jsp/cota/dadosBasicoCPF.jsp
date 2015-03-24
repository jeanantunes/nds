<div id="dadosCPF" style="display: none;">

 <form name="formDadosBasicoCpf" id="formDadosBasicoCpf">
 <fieldset style="width:880px!important; margin:5px;">
 <legend>Dados Cadastrais</legend>
 <table width="870" cellpadding="2" cellspacing="2" style="text-align:left;">
   <tr>
     <td><strong>Cota:</strong></td>
     <td>
        <input name="cotaDTO.numeroCota" maxlength="16" id="numeroCotaCPF" type="text" style="width:60px" />
     	 
     	 <strong>Tipo:</strong>
     	 
	     <select name="cotaDTO.tipoDistribuicaoCota" id="cotaDTO.tipoDistribuicao" style="width:110px;" onchange="MANTER_COTA.verificaTipoCota(this);">
	        <option value="" selected="selected">Selecione...</option>
	        <option value="ALTERNATIVO">Alternativo</option>
	        <option value="CONVENCIONAL">Convencional</option>
	      </select>
     
     </td>
     
     <td><strong>Início de Atividade:</strong></td>
     <td><span id="dataInclusaoCPF"style="width:100px"></span></td>
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
     <td width="114">Nome:</td> 
     <td width="301"><input type="text" maxlength="250" name="cotaDTO.nomePessoa" id="nomePessoaCPF" style="width:230px " /></td>
     <td width="114">E-mail:</td>
     <td ><input type="text" maxlength="200" style="width:300px" name="cotaDTO.email" id="emailCPF" onblur="MANTER_COTA.validarEmail('#emailCPF')"/></td>
   </tr>
   <tr>
     <td>CPF:</td>
     <td><input type="text" style="width:150px" name="cotaDTO.numeroCPF" id="numeroCPF" onchange="COTA_CPF.carregarDadosCPF('#numeroCPF')"/></td>
     <td>R. G.:</td>
     <td ><input type="text" maxlength="20" style="width:300px" name="cotaDTO.numeroRG" id="numeroRG" /></td>
   </tr>
   <tr>
     <td>Data Nascimento:</td>
     <td><input type="text" style="width:150px" name="cotaDTO.dataNascimento" id="dataNascimento"/></td>
     <td>Orgão Emissor:</td>
     <td width="313"><table width="100%" border="0" cellspacing="0" cellpadding="0">
       <tr>
         <td width="67%"><input type="text" maxlength="4" style="width:100px" name="cotaDTO.orgaoEmissor" id="dados-basico-orgaoEmissor" /></td>
         <td width="13%">UF:</td>
         <td width="20%"><select name="cotaDTO.estadoSelecionado" id="estadoSelecionado" style="width:50px">
           <option selected="selected"> </option>
           <option value="AC">AC</option>
           <option value="AL">AL</option>
           <option value="AM">AM</option>
           <option value="AP">AP</option>
           <option value="BA">BA</option>
           <option value="CE">CE</option>
           <option value="DF">DF</option>
           <option value="ES">ES</option>
           <option value="GO">GO</option>
           <option value="MA">MA</option>
           <option value="MG">MG</option>
           <option value="MS">MS</option>
           <option value="MT">MT</option>
           <option value="PA">PA</option>
           <option value="PB">PB</option>
           <option value="PE">PE</option>
           <option value="PI">PI</option>
           <option value="PR">PR</option>
           <option value="RJ">RJ</option>
           <option value="RN">RN</option>
           <option value="RO">RO</option>
           <option value="RR">RR</option>
           <option value="RS">RS</option>
           <option value="SC">SC</option>
           <option value="SE">SE</option>
           <option value="SP">SP</option>
           <option value="TO">TO</option>
           </select></td>
         </tr>
     </table></td>
     </tr>
   <tr>
     <td>Estado Civil:</td>
     <td><select name="cotaDTO.estadoCivilSelecionado" id="estadoCivilSelecionado" style="width:155px;">
     <option selected="selected">Selecione...</option>
     <option value="SOLTEIRO">Solteiro</option>
     <option value="CASADO">Casado</option>
     <option value="DIVORCIADO">Divorciado</option>
     <option value="VIUVO">Víuvo</option>
   </select></td>
     <td>Sexo:</td>
     <td ><select name="cotaDTO.sexoSelecionado" id="sexoSelecionado" style="width:100px">
       <option selected="selected">Selecione... </option>
       <option value="MASCULINO">Masculino</option>
       <option value="FEMININO">Feminino</option>
     </select></td>
   </tr>
   <tr>
     <td>Nacionalidade:</td>
     <td><input type="text" style="width:150px" maxlength="200" name="cotaDTO.nacionalidade" id="nacionalidade"  /></td>
     <td>Natural:</td>
     <td ><input type="text" style="width:300px" maxlength="200" name="cotaDTO.natural" id="natural"  /></td>
   </tr>
   <tr>
     <td>E-mail NF-e:</td>
      <td><input type="text" style="width:230px " maxlength="200" name="cotaDTO.emailNF" id="emailNFCPF" onblur="MANTER_COTA.validarEmail('#emailNFCPF')" /></td>
      <td>Emite NF-e?</td>
      <td><input type="checkbox" name="cotaDTO.emiteNFE" id="emiteNFECPF" /></td>
   </tr>
   <tr>
     <td>Status:</td>
     <td><input type="text" style="width:230px " id="statusCPF" disabled="disabled" name="cotaDTO.status" /></td>
     <td>Classificação da Cota:</td>
     <td >
     	<div id="selectClassificacaoCPF"></div>        
     </td>
   </tr>
 
</table>

<br clear="all" />

<table width="870" border="0" cellspacing="1" cellpadding="1">
   <tr>
      <td width="424" valign="top" class="antigosProp">
      <strong>Antigos Proprietários</strong><br />
      <table class="antigosProprietariosGridCPF"></table>
      <span class="bt_novos" title="Alterar Titularidade" id="btnAlterarTitularidadeCPF">
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
            	<input type="text" name="cotaDTO.inicioPeriodo" id="periodoCotaDeCPF" style="width:80px;" /></td>
            <td width="26" align="center">Até</td>
            <td width="133">
            	<input type="text" name="cotaDTO.fimPeriodo" id="periodoCotaAteCPF" style="width:80px;" /></td>
          </tr>
        </table></td>
      </tr> 

       <tr class="linhas">
        <td valign="top"><strong>Cota Base:</strong></td>
        <td width="374"><table width="348" border="0" cellspacing="1" cellpadding="2">
          <tr>
            <td width="38">Cota:</td>
            <td width="127">
            	<input type="text" name="cotaDTO.historicoPrimeiraCota" maxlength="16" id="historicoPrimeiraCotaCPF" style="width:50px; margin-right:10px;" onblur="MANTER_COTA.validarCotaHistoricoBase('#historicoPrimeiraCotaCPF,#historicoPrimeiraPorcentagemCPF')" /></td>
            <td width="30" align="center">Nome:</td>
            <td width="132">
            	<input type="text" name="cotaDTO.historicoPrimeiraPorcentagem" id="historicoPrimeiraPorcentagemCPF" maxlength="4" style="width:190px;"/></td>
            </tr>
          <tr>
            <td>Cota:</td>
            <td>
            	<input type="text" name="cotaDTO.historicoSegundaCota" maxlength="16" id="historicoSegundaCotaCPF" style="width:50px; margin-right:10px;" onblur="MANTER_COTA.validarCotaHistoricoBase('#historicoSegundaCotaCPF,#historicoSegundaPorcentagemCPF')"/></td>
            <td align="center">Nome:</td>
            <td>
            	<input type="text" name="cotaDTO.historicoSegundaPorcentagem" id="historicoSegundaPorcentagemCPF" maxlength="4" style="width:190px;"/></td>
            </tr>
          <tr>
            <td>Cota:</td>
            <td>
            	<input type="text" name="cotaDTO.historicoTerceiraCota" maxlength="16" id="historicoTerceiraCotaCPF" style="width:50px; margin-right:10px;" onblur="MANTER_COTA.validarCotaHistoricoBase('#historicoTerceiraCotaCPF,#historicoTerceiraPorcentagemCPF')" /></td>
            <td align="center">Nome:</td>
            <td>
            	<input type="text" name="cotaDTO.historicoTerceiraPorcentagem" id="historicoTerceiraPorcentagemCPF" maxlength="4" style="width:190px;"/></td>
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