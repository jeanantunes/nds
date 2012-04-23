
<fieldset>
<legend>Dados Básicos</legend>
<table width="777" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="572" valign="top">
    <table width="572" border="0" cellspacing="1" cellpadding="1">
      <tr>
        <td>Status:</td>
        <td width="135">
        	<select name="selectStatus" id="selectStatus">
	          <option selected="selected"> </option>
	          <option value="ATIVO">Ativo</option>
	          <option value="SUSPENSO">Suspenso</option>
          	</select>
          </td>
        <td width="80">Data Início:</td>
        	<td width="215">
        		<input type="text" id="dataInicio"  style="width:80px;" disabled="disabled"/>
        	</td>
      </tr>
      <tr>
        <td width="129">Nome PDV:</td>
        <td colspan="3">
        	<input type="text" name="nomePDV" id="nomePDV" style="width:300px;"/>
        </td>
        </tr>
      <tr>
        <td>Contato:</td>
        <td colspan="3">
        	<input type="text" name="contatoPDV" id="contatoPDV" style="width:300px;"/>
        </td>
        </tr>
      <tr>
        <td>Site:</td>
        <td colspan="3">
        	<input type="text" name="sitePDV" id="sitePDV" style="width:300px;"/>
        </td>
        </tr>
      <tr>
        <td>E-mail:</td>
        <td colspan="3">
        	<input type="text" name="emailPDV" id="emailPDV" style="width:300px;"/>
        </td>
        </tr>
      <tr>
        <td>Ponto de Referência:</td>
        <td colspan="3">
        	<input type="text" name="pontoReferenciaPDV" id="pontoReferenciaPDV" style="width:300px;"/>
        </td>
        </tr>
      <tr>
        <td align="right">
        	<input type="checkbox" name=dentroOutroEstabelecimento" id="dentroOutroEstabelecimento"  onclick="PDV.opcaoEstabelecimento('#dentroOutroEstabelecimento');" />
        </td>
        <td colspan="3">Dentro de Outro Estabelecimento?</td>
        </tr>
      <tr>
        <td align="right">&nbsp;</td>
        <td colspan="3">
          <div id="divTipoEstabelecimento" style="display:none;">
            Tipo Estabelecimento:
            <select name="selectTipoEstabelecimento" id="selectTipoEstabelecimento" style="width:180px;">
              <option selected="selected">Selecione</option>
              <option value="1">Hiper / Supermercado</option>
              <option value="2">Shopping</option>
              <option value="3">Galeria</option>
              <option value="4">Posto Serviço</option>
            </select></div>
        </td>
        </tr>
      </table>
      <br />

    </td>
    <td width="191" align="center" valign="top">
    	<img src="../images/bancaJornal.jpg" width="191" height="136" alt="Banca" /><br />
    	<a href="javascript:" onclick="popup_img();"><img src="../images/bt_cadastros.png" alt="Editar Imagem" width="15" height="15" hspace="10" vspace="3" border="0"  /></a><a href="javascript:"><img src="../images/ico_excluir.gif" alt="Excluir Imagem" width="15" height="15" hspace="10" vspace="3" border="0" /></a>
    </td>
  </tr>
</table>
    <table width="777" border="0" cellspacing="1" cellpadding="1">
      <tr>
	    <td width="135">Dias Funcionamento:</td>
	    <td width="252" class="diasFunc">
	    	<select name="selectDiasFuncionamento" id="selectDiasFuncionamento" style="width:230px;">
		      <option selected="selected">Selecione</option>
		      <option>Diário</option>
		      <option>Segunda - Sexta</option>
		      <option>Finais de Semana</option>
		      <option>Feriados</option>
		      <option>24 Horas</option>
		      <option>-----------------------------------------</option>
		      <option>Domingo</option>
		      <option>Segunda-feira</option>
		      <option>Terça-feira</option>
		      <option>Quarta-feira</option>
		      <option>Quinta-feira</option>
		      <option>Sexta-feira</option>
		      <option>Sábado</option>
	      </select>
    	</td>
    	<td width="47">Horário:</td>
    	<td width="179"><input type="text" name="inicioHorario" id="inicioHorario" style="width:60px;"/>
      		As
      	<input type="text" name="fimHorario" id="fimHorario" style="width:60px;"/></td>
    	<td width="148"><span class="bt_add"><a href="javascript:;" onclick="PDV.montartabelaDiasFuncionamento()">Incluir Novo</a></span></td>
  	</tr>
</table>

<table width="777" border="0" cellspacing="1" cellpadding="1">
      <tbody id="listaDiasFuncionais"></tbody>
</table>

<br />
<table width="777" border="0" cellspacing="1" cellpadding="1">
  <tr>
    <td>Tamanho:</td>
    <td>
    	<select name="selectTamanhoPDV" id="selectTamanhoPDV" style="width:200px;">
	      <option>Selecione...</option>
	      <option value="P">P (2m² até 4m² )</option>
	      <option value="M">M (4m² até 8m²)</option>
	      <option value="G">G (8m² até 12m²)</option>
	      <option value="SG">SG  (acima de 12m²)</option>
   		 </select>
   </td>
    <td>Qtde  Funcionários:</td>
    <td>
    	<input type="text" name="qntFuncionarios" id="qntFuncionarios" style="width:60px;"/>
    </td>
  </tr>
  <tr>
    <td>Sistema IPV:</td>
    <td>
    	<input name="sistemaIPV" id="sistemaIPV" type="checkbox" />
    </td>
    <td>% Faturamento:</td>
    <td>
    	<input type="text" name="porcentagemFaturamento" id="porcentagemFaturamento" style="width:60px;"/>
    </td>  
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td width="136">Tipo de Licença:</td>
    <td width="239">
    	<select name="selectTipoLicenca" id="selectTipoLicenca" style="width:232px;">
      		<option selected="selected">Selecione</option>
      		<option value="1" >Poção 1</option>
      		<option value="1">Opção 2</option>
      	</select></td>
    <td width="132">Número da Licença:</td>
    <td width="257">
    	<input type="text" name="numerolicenca" id="numerolicenca" style="width:225px;"/>
    </td>
  </tr>
  <tr>
    <td>Nome  da Licença:</td>
    <td><span class="diasFunc">
      <input type="text" name="nomeLicenca" id="nomeLicenca" style="width:225px;"/>
      </span></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>
<br />
</fieldset>

