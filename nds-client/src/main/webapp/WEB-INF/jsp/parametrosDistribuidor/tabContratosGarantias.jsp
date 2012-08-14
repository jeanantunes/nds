<div id="tabContratos">
  <table width="420" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td valign="top">
        <fieldset style="width: 420px !important; margin-bottom: 5px;">
          <legend>Condições de Contratação:</legend>
          <table width="392" border="0" cellspacing="1" cellpadding="1">
            <tr>
              <td>Utiliza Contrato com as Cotas?</td>
              <td>
                <c:if test="${parametrosDistribuidor.utilizaContratoComCotas}">
                  <input name="parametrosDistribuidor.utilizaContratoComCotas" onclick="javascript:habilitaPrazoContrato();"
                    id="utilizaContratoComCotas" checked="checked" type="checkbox" />
                </c:if> 
                <c:if test="${not parametrosDistribuidor.utilizaContratoComCotas}">
                  <input name="parametrosDistribuidor.utilizaContratoComCotas" onclick="javascript:habilitaPrazoContrato();"
                    id="utilizaContratoComCotas" type="checkbox" />
                </c:if>
              </td>
            </tr>
            <tr>
              <td width="190">Prazo do Contrato (em meses ):</td>
              <td width="195">
               <input type="text" name="parametrosDistribuidor.prazoContrato" 
                           id="prazoContrato"
                           style="width: 80px;"
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
              <td colspan="2"><textarea name="parametrosDistribuidor.informacoesComplementaresContrato" rows="4"
                  id="informacoesComplementaresContrato" style="width: 350px;"></textarea></td>
            </tr>
          </table>
        </fieldset>
        <fieldset style="width: 420px !important; margin-bottom: 5px; float: left">
          <legend>Garantia</legend>
          <table width="335" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="143">Utiliza Garantia para PDVs?</td>
              <td width="192"><input type="checkbox" id="checkUtilizaGarantiaPdv"
                onclick="javascript:mostraTabelaGarantiasAceitas();" /></td>
            </tr>
          </table>
          <table width="335" border="0" cellspacing="1" cellpadding="1" id="tabelaGarantiasAceitas" style="display: none;">
            <tr class="header_table">
              <td>&nbsp;</td>
              <td>Garantia</td>
              <td>Validade (Meses)</td>
            </tr>
            <tr class="class_linha_1">
              <td width="20">
                <c:if test="${parametrosDistribuidor.utilizaChequeCaucao}">
                  <input name="parametrosDistribuidor.utilizaChequeCaucao" id="utilizaChequeCaucao" type="checkbox"
                    checked="checked" />
                </c:if> 
                <c:if test="${not parametrosDistribuidor.utilizaChequeCaucao}">
                  <input name="parametrosDistribuidor.utilizaChequeCaucao" id="utilizaChequeCaucao" type="checkbox" />
                </c:if>
              </td>
              <td width="201">Cheque Caução</td>
              <td width="104"><input name="parametrosDistribuidor.validadeChequeCaucao" type="text"
                style="float: left; width: 60px;" value="${parametrosDistribuidor.validadeChequeCaucao}" 
                id="validadeChequeCaucao" disabled="${parametrosDistribuidor.utilizaChequeCaucao ? '' : 'disabled'}" />
              </td>
            </tr>
            <tr class="class_linha_2">
              <td>
                <c:if test="${parametrosDistribuidor.utilizaCaucaoLiquida}">
                  <input name="parametrosDistribuidor.utilizaCaucaoLiquida" id="utilizaCaucaoLiquida" checked="checked"
                    type="checkbox" />
                </c:if>
                <c:if test="${not parametrosDistribuidor.utilizaCaucaoLiquida}">
                  <input name="parametrosDistribuidor.utilizaCaucaoLiquida" id="utilizaCaucaoLiquida" type="checkbox" />
                </c:if>
              </td>
              <td>Caução Líquida</td>
              <td><input name="parametrosDistribuidor.validadeCaucaoLiquida" type="text" style="float: left; width: 60px;"
                value="${parametrosDistribuidor.validadeCaucaoLiquida}" id="validadeCaucaoLiquida"
                disabled="${parametrosDistribuidor.utilizaCaucaoLiquida ? '' : 'disabled'}" /></td>
            </tr>
            <tr class="class_linha_2">
              <td>
                <c:if test="${parametrosDistribuidor.utilizaFiador}">
                  <input name="parametrosDistribuidor.utilizaFiador" id="utilizaFiador" checked="checked" type="checkbox" />
                </c:if> <c:if test="${not parametrosDistribuidor.utilizaFiador}">
                  <input name="parametrosDistribuidor.utilizaFiador" id="utilizaFiador" type="checkbox" />
                </c:if>
              </td>
              <td>Fiador</td>
              <td><input name="parametrosDistribuidor.validadeFiador" type="text" style="float: left; width: 60px;"
                id="validadeFiador" value="${parametrosDistribuidor.validadeFiador}"
                disabled="${parametrosDistribuidor.utilizaFiador ? '' : 'disabled'}" /></td>
            </tr>
            <tr class="class_linha_1">
              <td>
                <c:if test="${parametrosDistribuidor.utilizaNotaPromissoria}">
                  <input name="parametrosDistribuidor.utilizaNotaPromissoria" id="utilizaNotaPromissoria" checked="checked"
                    type="checkbox" />
                </c:if>
                <c:if test="${not parametrosDistribuidor.utilizaNotaPromissoria}">
                  <input name="parametrosDistribuidor.utilizaNotaPromissoria" id="utilizaNotaPromissoria" type="checkbox" />
                </c:if>
              </td>
              <td>Nota Promissória</td>
              <td><input name="parametrosDistribuidor.validadeNotaPromissoria" type="text" style="float: left; width: 60px;"
                id="validadeNotaPromissoria" value="${parametrosDistribuidor.validadeNotaPromissoria}"
                disabled="${parametrosDistribuidor.utilizaNotaPromissoria ? '' : 'disabled'}" /></td>
            </tr>
            <tr class="class_linha_2">
              <td>
                <c:if test="${parametrosDistribuidor.utilizaImovel}">
                  <input name="parametrosDistribuidor.utilizaImovel" id="utilizaImovel" checked="checked" type="checkbox" />
                </c:if>
                 <c:if test="${not parametrosDistribuidor.utilizaImovel}">
                  <input name="parametrosDistribuidor.utilizaImovel" id="utilizaImovel" type="checkbox" />
                </c:if>
               </td>
              <td>Imóvel</td>
              <td><input name="parametrosDistribuidor.validadeImovel" type="text" style="float: left; width: 60px;"
                          id="validadeImovel" value="${parametrosDistribuidor.validadeImovel}"
                          disabled="${parametrosDistribuidor.utilizaImovel ? '' : 'disabled'}"/></td>
            </tr>
            <tr class="class_linha_1">
              <td>
                <c:if test="${parametrosDistribuidor.utilizaAntecedenciaValidade}">
                  <input name="parametrosDistribuidor.utilizaAntecedenciaValidade" id="utilizaAntecedenciaValidade"
                    checked="checked" type="checkbox" />
                </c:if> 
                <c:if test="${not parametrosDistribuidor.utilizaAntecedenciaValidade}">
                  <input name="parametrosDistribuidor.utilizaAntecedenciaValidade" id="utilizaAntecedenciaValidade"
                    type="checkbox" />
                </c:if>
              </td>
              <td>Antecedência da Validade</td>
              <td><input name="parametrosDistribuidor.validadeAntecedenciaValidade" type="text"
                style="float: left; width: 60px;" value="${parametrosDistribuidor.validadeAntecedenciaValidade}"
                disabled="${parametrosDistribuidor.utilizaAntecedenciaValidade ? '' : 'disabled'}"
                id="validadeAntecedenciaValidade" /></td>
            </tr>
            <tr class="class_linha_2">
              <td>
                <c:if test="${parametrosDistribuidor.utilizaOutros}">
                  <input name="parametrosDistribuidor.utilizaOutros" id="utilizaOutros" checked="checked" type="checkbox" />
                </c:if> 
                <c:if test="${not parametrosDistribuidor.utilizaOutros}">
                  <input name="parametrosDistribuidor.utilizaOutros" id="utilizaOutros" type="checkbox" />
                </c:if>
              </td>
              <td>Outros</td>
              <td><input name="parametrosDistribuidor.validadeOutros" type="text" style="float: left; width: 60px;"
                value="${parametrosDistribuidor.validadeOutros}" 
                disabled="${parametrosDistribuidor.utilizaOutros ? '' : 'disabled'}"
                id="validadeOutros" /></td>
            </tr>
          </table>
        </fieldset>
      </td>
      <td style="width: 10px;">&nbsp;</td>
      <td valign="top">
        <fieldset style="width: 420px !important; margin-bottom: 5px;">
          <legend>Procuração</legend>
          <table width="393" border="0" cellspacing="1" cellpadding="1">
            <tr>
              <td>
                <c:if test="${parametrosDistribuidor.utilizaProcuracaoEntregadores}">
                  <input name="parametrosDistribuidor.utilizaProcuracaoEntregadores" id="utilizaProcuracaoEntregadores"
                  type="checkbox"  checked="checked"/>
                </c:if>
                <c:if test="${not parametrosDistribuidor.utilizaProcuracaoEntregadores}">
                  <input name="parametrosDistribuidor.utilizaProcuracaoEntregadores" id="utilizaProcuracaoEntregadores"
                  type="checkbox"/>
                </c:if>
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
              <td colspan="2"><textarea name="parametrosDistribuidor.informacoesComplementaresProcuracao" rows="4"
                  id="informacoesComplementaresProcuracao" style="width: 150px;"></textarea></td>
            </tr>
          </table>
        </fieldset>
        <fieldset style="width: 420px !important; margin-bottom: 5px;">
          <legend>Termo de Adesão</legend>
          <table width="393" border="0" cellspacing="1" cellpadding="1">
            <tr>
              <td>
                <c:if test="${parametrosDistribuidor.utilizaTermoAdesaoEntregaBancas}">
                    <input name="parametrosDistribuidor.utilizaTermoAdesaoEntregaBancas" id="utilizaTermoAdesaoEntregaBancas"
                    type="checkbox" checked="checked" />
                </c:if>
                <c:if test="${not parametrosDistribuidor.utilizaTermoAdesaoEntregaBancas}">
                    <input name="parametrosDistribuidor.utilizaTermoAdesaoEntregaBancas" id="utilizaTermoAdesaoEntregaBancas"
                    type="checkbox"/>
                </c:if>
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
                  rows="4" id="informacoesComplementaresTermoAdesaoEntregaBancas" style="width: 150px;"></textarea></td>
            </tr>
          </table>
        </fieldset>
      </td>
    </tr>
  </table>
</div>
