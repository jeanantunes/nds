<div id="tabContratos">
  <table width="420" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td valign="top">
        <!-- coluna 1 -->
        
        <fieldset style="width: 420px !important; margin-bottom: 5px;">
          <legend>Condições de Contratação:</legend>
          <table width="392" border="0" cellspacing="1" cellpadding="1">
            <tr>
              <td>Utiliza Contrato com as Cotas?</td>
              <td><input name="parametrosDistribuidor.utilizaContratoComCotas" 
                        onclick="habilitaPrazoContrato()"
                        id="utilizaContratoComCotas" 
                        type="checkbox" ${parametrosDistribuidor.utilizaContratoComCotas}/>
              </td>
            </tr>
            <tr>
              <td width="190">Prazo do Contrato (em meses ):</td>
              <td width="195"><input type="text" 
                                     name="parametrosDistribuidor.prazoContrato" 
                                     id="prazoContrato"
                                     style="width: 80px;" 
                                     disabled="disabled" 
                                     value="${parametrosDistribuidor.prazoContrato}" />
              </td>
            </tr>
            <tr>
              <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
              <td colspan="2">Informações complementares do Contrato:</td>
            </tr>
            <tr>
              <td colspan="2"><textarea name="parametrosDistribuidor.informacoesComplementaresContrato" 
                                        rows="4"
                                        id="informacoesComplementaresContrato" 
                                        style="width: 350px;"></textarea>
              </td>
            </tr>
          </table>

        </fieldset>



        <fieldset style="width: 420px !important; margin-bottom: 5px; float: left">
          <legend>Garantia</legend>
          <table width="335" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="143">Utiliza Garantia para PDVs?</td>
              <td width="192"><input type="checkbox" 
                              id="checkUtilizaGarantiaPdv" 
                              onclick="mostraTabelaGarantiasAceitas();" />
              </td>
            </tr>
          </table>
          <table width="335" border="0" cellspacing="1" cellpadding="1" id="tabelaGarantiasAceitas" style="display: none;">
            <tr class="header_table">
              <td>&nbsp;</td>
              <td>Garantia</td>
              <td>Validade (Meses)</td>
            </tr>
            <tr class="class_linha_1">
              <td width="20"><input name="parametrosDistribuidor.utilizaChequeCaucao" id="utilizaChequeCaucao" type="checkbox"/>
              </td>
              <td width="201">Cheque Caução</td>
              <td width="104"><input name="parametrosDistribuidor.validadeChequeCaucao" 
                         type="text" style="float: left; width: 60px;" 
                         value="${parametrosDistribuidor.validadeChequeCaucao}" 
                         id="validadeChequeCaucao" />
              </td>
            </tr>
            <tr class="class_linha_2">
              <td><input name="parametrosDistribuidor.utilizaCaucaoLiquida" id="utilizaCaucaoLiquida" type="checkbox"/>
              </td>
              <td>Caução Líquida</td>
              <td><input name="parametrosDistribuidor.validadeCaucaoLiquida" 
                         type="text" style="float: left; width: 60px;" 
                         value="${parametrosDistribuidor.validadeCaucaoLiquida}" 
                         id="validadeCaucaoLiquida" />
              </td>
            </tr>
            <tr class="class_linha_2">
              <td><input name="parametrosDistribuidor.utilizaFiador" id="utilizaFiador" type="checkbox"/>
              </td>
              <td>Fiador</td>
              <td><input name="parametrosDistribuidor.validadeFiador" 
                         type="text" style="float: left; width: 60px;" 
                         id="validadeFiador" 
                         value="${parametrosDistribuidor.validadeFiador}" />
              </td>
            </tr>
            <tr class="class_linha_1">
              <td><input name="parametrosDistribuidor.utilizaNotaPromissoria" id="utilizaNotaPromissoria" type="checkbox"/>
              </td>
              <td>Nota Promissória</td>
              <td><input name="parametrosDistribuidor.validadeNotaPromissoria" 
                         type="text" style="float: left; width: 60px;" 
                         id="validadeNotaPromissoria" 
                         value="${parametrosDistribuidor.validadeNotaPromissoria}" />
              </td>
            </tr>
            <tr class="class_linha_2">
              <td><input name="parametrosDistribuidor.utilizaImovel" id="utilizaImovel" type="checkbox"/>
              </td>
              <td>Imóvel</td>
              <td><input name="parametrosDistribuidor.validadeImovel" 
                         type="text" style="float: left; width: 60px;" 
                         id="validadeImovel" 
                         value="${parametrosDistribuidor.validadeImovel}" /> 
              </td>
            </tr>
            <tr class="class_linha_1">
              <td><input name="parametrosDistribuidor.utilizaAntecedenciaValidade" id="utilizaAntecedenciaValidade" type="checkbox"/>
              </td>
              <td>Antecedência da Validade</td>
              <td><input name="parametrosDistribuidor.validadeAntecedenciaValidade" 
                         type="text" style="float: left; width: 60px;" 
                         value="${parametrosDistribuidor.validadeAntecedenciaValidade}" 
                         id="validadeAntecedenciaValidade" />
              </td>
            </tr>
            <tr class="class_linha_2">
              <td><input name="parametrosDistribuidor.utilizaOutros" id="utilizaOutros" type="checkbox"/>
              </td>
              <td>Outros</td>
              <td><input name="parametrosDistribuidor.validadeOutros" 
                         type="text" style="float: left; width: 60px;" 
                          value="${parametrosDistribuidor.validadeOutros}" 
                         id="validadeOutros" />
              </td>
            </tr>
          </table>

        </fieldset></td>
      <td style="width: 10px;">&nbsp;</td>
      <td valign="top">
        <!-- coluna 2 -->
        
        <fieldset style="width: 420px !important; margin-bottom: 5px;">
          <legend>Procuração</legend>
          <table width="393" border="0" cellspacing="1" cellpadding="1">
            <tr>
              <td><input name="parametrosDistribuidor.utilizaProcuracaoEntregadores" 
                         id="utilizaProcuracaoEntregadores" 
                         type="checkbox" ${parametrosDistribuidor.utilizaProcuracaoEntregadores}/>
              </td>
              <td width="386" align="left">Utiliza procuração para Entregadores?</td>
            </tr>
            <tr>
              <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
              <td colspan="2">Informações complementares da Procuração:</td>
            </tr>
            <tr>
              <td colspan="2"><textarea name="parametrosDistribuidor.informacoesComplementaresProcuracao" 
                                        rows="4" 
                                        id="informacoesComplementaresProcuracao" 
                                        style="width: 150px;"></textarea>
              </td>
            </tr>
          </table>
        </fieldset>


        <fieldset style="width: 420px !important; margin-bottom: 5px;">
          <legend>Termo de Adesão</legend>
          <table width="393" border="0" cellspacing="1" cellpadding="1">
            <tr>
              <td><input name="parametrosDistribuidor.utilizaTermoAdesaoEntregaBancas" 
                         id="utilizaTermoAdesaoEntregaBancas" 
                         type="checkbox" ${parametrosDistribuidor.utilizaProcuracaoEntregadores} />
              </td>
              <td width="386" align="left">Utiliza Termo de Adesão para Serviço de Entrega em Banca?</td>
            </tr>
            <tr>
              <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
              <td colspan="2">Informações complementares do Termo de Adesão:</td>
            </tr>
            <tr>
              <td colspan="2"><textarea name="parametrosDistribuidor.informacoesComplementaresTermoAdesaoEntregaBancas" 
                                        rows="4" 
                                        id="informacoesComplementaresTermoAdesaoEntregaBancas" 
                                        style="width: 150px;"></textarea>
              </td>
            </tr>
          </table>
        </fieldset></td>
    </tr>
  </table>
</div>
