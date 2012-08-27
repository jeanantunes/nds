
<div id="tabNegociacao">
	<br/>
    	<fieldset style="width:400px!important; margin-bottom:5px; margin-left:10px;">
        	<legend>Negociação de Dividas</legend>
             <table width="393" border="0" cellspacing="1" cellpadding="1">
                 <tr>
                   <td colspan="4">                          <label>Sugere suspensão quando atingir</label>

<!-- Supere Suspensao Quando Atingir Qtde Boletos -->                      
<input name="parametrosDistribuidor.sugereSuspensaoQuandoAtingirBoletos" id="sugereSuspensaoQuandoAtingirBoletos" value="${parametrosDistribuidor.sugereSuspensaoQuandoAtingirBoletos}" type="text" style="width:30px; " /> 


                     &nbsp;boletos 
                     ou R$

<!-- Supere Suspensão quando atingir valor R$ -->
<input name="parametrosDistribuidor.sugereSuspensaoQuandoAtingirReais" id="sugereSuspensaoQuandoAtingirReais" value="${parametrosDistribuidor.sugereSuspensaoQuandoAtingirReais}" type="text" style="width:60px; text-align:right;" value="0,00" />


                      </td>
                    </tr>
                    <tr>
                      <td colspan="4">&nbsp;</td>
                    </tr>
                    <tr>
                      <td colspan="4">&nbsp;</td>
                    </tr>
                    <tr>
                      <td><label>Utiliza desconto da cota para negociação?</label></td>
                      <td width="27">
  
<script type="text/javascript">
function checkDescontoSelecionado() {
	//debugger;
	if( $('#utilizaDesconto').is(":checked") )
		$('.percentualDesconto').show();
	else
		$('.percentualDesconto').hide();
}
</script>

<!-- Utiliza desconto na negociação -->
<input name="parametrosDistribuidor.utilizaDesconto" id="utilizaDesconto" type="checkbox" ${parametrosDistribuidor.utilizaDesconto?"checked":""} onclick="checkDescontoSelecionado()" /></td>


                   <td width="50"><span class="percentualDesconto" style="display:${parametrosDistribuidor.utilizaDesconto?"block":"none"}">

<!-- % Desconto utilizado na negociação -->                      
<input name="parametrosDistribuidor.percentualDesconto" id="percentualDesconto" value="${parametrosDistribuidor.percentualDesconto}" type="text" style="width:40px;" />

		
		</span></td>
                   <td width="36"><span class="percentualDesconto" style="display:${parametrosDistribuidor.utilizaDesconto?"block":"none"}">%</span></td>
                 </tr>
                 <tr>
                   <td width="267"><label>Parcelamento de Divida:</label></td>
                   <td colspan="3">
     
<!-- Parcelamento de Divida -->              
<input  name="parametrosDistribuidor.parcelamentoDividas" id="parcelamentoDividas" type="checkbox" ${parametrosDistribuidor.parcelamentoDividas?"checked":""} /></td>


                 </tr>
                 <tr>
                   <td><label>Negociação em até x parcelas:</label></td>
                   <td colspan="3">
                   
<!-- Negociacao de Parcelas -->
<input name="parametrosDistribuidor.negociacaoAteParcelas" id="negociacaoAteParcelas" type="text" style="width:40px;" value="${parametrosDistribuidor.negociacaoAteParcelas}"  style="width:40px;" />
		
		
		</td>
      </tr>
  </table>
 </fieldset>
</div>