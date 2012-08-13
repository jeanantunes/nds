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
              <td width="20"><input name="parametrosDistribuidor.chequeCalcao" id="chequeCalcao" type="checkbox" ${parametrosDistribuidor.chequeCalcao} />
              </td>
              <td width="201">Cheque Caução</td>
              <td width="104"><input name="date1" type="text" style="float: left; width: 60px;" id="date1" />
              </td>
            </tr>
            <tr class="class_linha_2">
              <td><input name="parametrosDistribuidor.caucaoLiquida" id="caucaoLiquida" type="checkbox" ${parametrosDistribuidor.caucaoLiquida}/>
              </td>
              <td>Caução Líquida</td>
              <td><input name="parametrosDistribuidor.chequeCalcaoValor" 
                         type="text" style="float: left; width: 60px;" 
                         value="${parametrosDistribuidor.chequeCalcaoValor}" 
                         id="chequeCalcaoValor" />
              </td>
            </tr>
            <tr class="class_linha_2">
              <td><input name="parametrosDistribuidor.fiador" id="fiador" type="checkbox" ${parametrosDistribuidor.fiador}/>
              </td>
              <td>Fiador</td>
              <td><input name="parametrosDistribuidor.fiadorValor" 
                         type="text" style="float: left; width: 60px;" 
                         id="fiadorValor" 
                         value="${parametrosDistribuidor.fiadorValor}" />
              </td>
            </tr>
            <tr class="class_linha_1">
              <td><input name="parametrosDistribuidor.notaPromissoria" id="notaPromissoria" type="checkbox" ${parametrosDistribuidor.notaPromissoria}/>
              </td>
              <td>Nota Promissória</td>
              <td><input name="parametrosDistribuidor.notaPromissoriaValor" 
                         type="text" style="float: left; width: 60px;" 
                         id="notaPromissoriaValor" 
                         value="${parametrosDistribuidor.notaPromissoriaValor}" />
              </td>
            </tr>
            <tr class="class_linha_2">
              <td><input name="parametrosDistribuidor.imovel" id="imovel" type="checkbox" ${parametrosDistribuidor.imovel}/>
              </td>
              <td>Imóvel</td>
              <td><input name="parametrosDistribuidor.imovelValor" 
                         type="text" style="float: left; width: 60px;" 
                         id="imovelValor" 
                         value="${parametrosDistribuidor.imovelValor}" /> 
              </td>
            </tr>
            <tr class="class_linha_1">
              <td><input name="parametrosDistribuidor.antecedenciaValidade" id="antecedenciaValidade" type="checkbox" ${parametrosDistribuidor.antecedenciaValidade}/>
              </td>
              <td>Antecedência da Validade</td>
              <td><input name="parametrosDistribuidor.antecedenciaValidadeValor" 
                         type="text" style="float: left; width: 60px;" 
                         value="${parametrosDistribuidor.antecedenciaValidadeValor}" 
                         id="antecedenciaValidadeValor" />
              </td>
            </tr>
            <tr class="class_linha_2">
              <td><input name="parametrosDistribuidor.outros" id="outros" type="checkbox"/>
              </td>
              <td>Outros</td>
              <td><input name="parametrosDistribuidor.outrosValor" 
                         type="text" style="float: left; width: 60px;" 
                         value="" 
                         id="outrosValor" />
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
