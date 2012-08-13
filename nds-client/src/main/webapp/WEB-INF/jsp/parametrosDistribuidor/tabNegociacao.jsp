
 <div id="tabNegociacao">
	<fieldset style="width:400px!important; margin-bottom:5px;">
        <legend>Negociação de Dividas</legend>
        <table width="393" border="0" cellspacing="1" cellpadding="1">
            <tr>
              <td colspan="2"><label>Sugere suspensão quando atingir</label><input name="parametrosDistribuidor.sugereSuspensaoQuandoAtingirBoletos" id="sugereSuspensaoQuandoAtingirBoletos" value="${parametrosDistribuidor.sugereSuspensaoQuandoAtingirBoletos}" type="text" style="width:30px; " /> 
              &nbsp;boletos 
              ou R$
                <input name="parametrosDistribuidor.sugereSuspensaoQuandoAtingirReais" id="sugereSuspensaoQuandoAtingirReais" value="${parametrosDistribuidor.sugereSuspensaoQuandoAtingirReais}" type="text" style="width:60px; text-align:right;" />
              </td>
            </tr>
            <tr>
              <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
              <td width="259"><label>Parcelamento de Divida:</label></td>
              <td width="127"><input name="parametrosDistribuidor.parcelamentoDividas" id="parcelamentoDividas" type="checkbox" ${parametrosDistribuidor.parcelamentoDividas}/></td>
            </tr>
            <tr>
              <td><label>Negociação em até x parcelas:</label></td>
              <td><input name="parametrosDistribuidor.negociacaoAteParcelas" id="negociacaoAteParcelas" type="text" style="width:40px;" value="${parametrosDistribuidor.negociacaoAteParcelas}" /></td>
            </tr>
            <tr>
              <td><label>Rmover:</label></td>
              <td><input name="parametrosDistribuidor.permitePagamentoDividasDivergentes" id="permitePagamentoDividasDivergentes" type="checkbox" ${parametrosDistribuidor.permitePagamentoDividasDivergentes} /></td>
            </tr>
        </table>
    </fieldset>
</div>