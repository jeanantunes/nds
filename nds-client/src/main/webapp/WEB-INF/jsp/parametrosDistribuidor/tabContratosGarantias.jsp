<div id="tabContratos">
  <fieldset style="width: 420px !important; margin-bottom: 5px;">
    <legend>Condições de Contratação:</legend>
    <table width="392" border="0" cellspacing="1" cellpadding="1">
      <tr>
        <td>Utiliza Contrato com as Cotas?</td>
        <td><input name="parametrosDistribuidor.utilizaContratoComCotas" onclick="habilitaPrazoContrato()"
          id="utilizaContratoComCotas" type="checkbox" ${parametrosDistribuidor.utilizaContratoComCotas}/>
        </td>
      </tr>
      <tr>
        <td width="190">Prazo do Contrato (em meses ):</td>
        <td width="195"><input type="text" name="parametrosDistribuidor.prazoContrato" id="prazoContrato"
          style="width: 80px;" disabled="disabled" value="${parametrosDistribuidor.prazoContrato}" />
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
            id="informacoesComplementaresContrato" style="width: 350px;"></textarea>
        </td>
      </tr>
    </table>
  </fieldset>
  <fieldset style="width: 420px !important; margin-bottom: 5px;">
    <legend>Procuração</legend>
    <table width="393" border="0" cellspacing="1" cellpadding="1">
      <tr>
        <td><input name="parametrosDistribuidor.utilizaProcuracaoEntregadores" id="utilizaProcuracaoEntregadores"
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
        <td colspan="2"><textarea name="parametrosDistribuidor.informacoesComplementaresProcuracao" rows="4"
            id="informacoesComplementaresProcuracao" style="width: 150px;"></textarea>
        </td>
      </tr>
    </table>
  </fieldset>
  <br clear="all" />
  <fieldset style="width: 870px !important; margin-bottom: 5px;">
    <legend>Garantia</legend>
    <table width="300" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="170">Utiliza Garantia para PDVs?</td>
        <td width="25"><input name="parametrosDistribuidor.utilizaGarantiaPdv" id="utilizaGarantiaPdv" type="checkbox" ${parametrosDistribuidor.utilizaGarantiaPdv}/>
        </td>
        <td width="35">&nbsp;</td>
        <td width="20">&nbsp;</td>
        <td width="50">&nbsp;</td>
      </tr>
    </table>
    <table width="780" border="0" cellspacing="1" cellpadding="1">
      <tr>
        <td width="20"><input name="parametrosDistribuidor.chequeCalcao" id="chequeCalcao" type="checkbox" ${parametrosDistribuidor.chequeCalcao}/>
        </td>
        <td width="201">Cheque Caução</td>
        <td width="104"><input name="parametrosDistribuidor.chequeCalcaoValor" type="text" style="float: left; width: 60px;"
          value="${parametrosDistribuidor.chequeCalcaoValor}" id="chequeCalcaoValor" />
        </td>
        <td width="20"><input name="parametrosDistribuidor.fiador" id="fiador" type="checkbox" ${parametrosDistribuidor.fiador}/>
        </td>
        <td width="105">Fiador</td>
        <td width="78"><input name="parametrosDistribuidor.fiadorValor" type="text" style="float: left; width: 60px;"
          id="fiadorValor" value="${parametrosDistribuidor.fiadorValor}" />
        </td>
        <td width="20"><input name="parametrosDistribuidor.imovel" id="imovel" type="checkbox" ${parametrosDistribuidor.imovel}/>
        </td>
        <td width="144">Imóvel</td>
        <td width="60"><input name="parametrosDistribuidor.imovelValor" type="text" style="float: left; width: 60px;"
          id="imovelValor" value="${parametrosDistribuidor.imovelValor}" />
        </td>
      </tr>
      <tr>
        <td><input name="parametrosDistribuidor.caucaoLiquida" id="caucaoLiquida" type="checkbox" ${parametrosDistribuidor.caucaoLiquida}/>
        </td>
        <td>Caução Líquida</td>
        <td><input name="parametrosDistribuidor.caucaoLiquidaValor" type="text" style="float: left; width: 60px;"
          id="caucaoLiquidaValor" value="${parametrosDistribuidor.caucaoLiquidaValor}" />
        </td>
        <td><input name="parametrosDistribuidor.notaPromissoria" id="notaPromissoria" type="checkbox" ${parametrosDistribuidor.notaPromissoria}/>
        </td>
        <td>Nota Promissória</td>
        <td><input name="parametrosDistribuidor.notaPromissoriaValor" type="text" style="float: left; width: 60px;"
          id="notaPromissoriaValor" value="${parametrosDistribuidor.notaPromissoriaValor}" />
        </td>
        <td><input name="parametrosDistribuidor.antecedenciaValidade" id="antecedenciaValidade" type="checkbox" ${parametrosDistribuidor.antecedenciaValidade}/>
        </td>
        <td>Antecedência da Validade</td>
        <td><input name="parametrosDistribuidor.antecedenciaValidadeValor" type="text" style="float: left; width: 60px;"
          value="${parametrosDistribuidor.antecedenciaValidadeValor}" id="antecedenciaValidadeValor" />
        </td>
      </tr>
      <tr>
        <td><input name="parametrosDistribuidor.indicadorReajusteCaucaoLiquida" id="indicadorReajusteCaucaoLiquida"
          type="checkbox" ${parametrosDistribuidor.indicadorReajusteCaucaoLiquida}/>
        </td>
        <td>Indicador reajuste de caução líquida</td>
        <td><input name="parametrosDistribuidor.indicadorReajusteCaucaoLiquidaValor" id="indicadorReajusteCaucaoLiquidaValor"
          type="text" style="width: 60px;" value="${parametrosDistribuidor.indicadorReajusteCaucaoLiquidaValor}" />
        </td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
    </table>

  </fieldset>
</div>
