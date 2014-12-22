
<div id="tabNegociacao">
	<br/>
    	<fieldset style="width:420px!important; margin-bottom:5px; margin-left:10px;">
        	<legend>Negociação de Dividas</legend>
        	
        	 <table width="393" border="0" cellspacing="1" cellpadding="1">
        	 
        	     <tr>
                     <td><label>Sugere Suspensão:</label></td>
	                 <td colspan="3">
	                     <input name="parametrosDistribuidor.sugereSuspensao" id="sugereSuspensaoDistribuidor" onclick="parametrosDistribuidorController.exibe_form_suspencao_distribuidor(this.checked);" type="checkbox" ${parametrosDistribuidor.sugereSuspensao?"checked":""} />
	                 </td>
                 </tr>  
                 
        	 </table>
        	 
        	 <div class="form-suspensao-hidden-class">
        	
                 <table width="393" border="0" cellspacing="1" cellpadding="1">

	                 <tr>
	                   <td colspan="4">
	                   
	                       <label>Sugere suspensão quando atingir</label>
	
						   <!-- Supere Suspensao Quando Atingir Qtde Boletos -->                      
						   <input name="parametrosDistribuidor.sugereSuspensaoQuandoAtingirBoletos" id="sugereSuspensaoQuandoAtingirBoletos" value="${parametrosDistribuidor.sugereSuspensaoQuandoAtingirBoletos}" type="text" style="width:30px; " /> 
	
	
	                       &nbsp;boletos 
	                       ou R$
	
						   <!-- Supere Suspensão quando atingir valor R$ -->
						   <input name="parametrosDistribuidor.sugereSuspensaoQuandoAtingirReais" id="sugereSuspensaoQuandoAtingirReais" value="${parametrosDistribuidor.sugereSuspensaoQuandoAtingirReais}" type="text" style="width:60px; text-align:right;" value="0,00" />
	
	
	                   </td>
	                   
	                 </tr>
	                 
                 </table>
                 
             </div>
             
             <table width="393" border="0" cellspacing="1" cellpadding="1">
                 
				 <tr>
					<td><label>Parar de acumular dívidas?</label></td>
					<td colspan="3">
						<input type="hidden" value="${parametrosDistribuidor.pararAcumuloDividas}" id="pararAcumuloDividasHidden" />
						<select name="pararAcumuloDividas" id="pararAcumuloDividas">
							<option value="true">Sim</option>
							<option value="false">Não</option>
						</select>
					</td>
				 </tr>
				 <tr>
					<td colspan="4">&nbsp;</td>
				 </tr>
					<tr>
                      <td><label>Utiliza desconto da cota para negociação?</label></td>
                      <td width="27">
  
<script type="text/javascript">
function checkDescontoSelecionado() {
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
<input name="parametrosDistribuidor.percentualDesconto" id="tab-negociacao-percentualDesconto" value="${parametrosDistribuidor.percentualDesconto}" type="text" style="width:40px;" />

		
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

			<tr>
				<td>
					<label>Aceita pagamento maior:</label>
				</td>
				<td colspan="3">
					<input name="parametrosDistribuidor.aceitaBaixaPagamentoMaior"
						   id="aceitaBaixaPagamentoMaior" type="checkbox"
						   ${parametrosDistribuidor.aceitaBaixaPagamentoMaior?"checked":""}/>
				</td>
			</tr>
			<tr>
				<td>
					<label>Aceita pagamento menor:</label>
				</td>
				<td colspan="3">
					<input name="parametrosDistribuidor.aceitaBaixaPagamentoMenor"
						   id="aceitaBaixaPagamentoMenor" type="checkbox"
						   ${parametrosDistribuidor.aceitaBaixaPagamentoMenor?"checked":""}/>
				</td>
			</tr>
			<tr>
				<td>
					<label>Aceita pagamento vencido:</label>
				</td>
				<td colspan="3">
					<input name="parametrosDistribuidor.aceitaBaixaPagamentoVencido"
						   id="aceitaBaixaPagamentoVencido" type="checkbox"
						   ${parametrosDistribuidor.aceitaBaixaPagamentoVencido?"checked":""}/>
				</td>
			</tr>
			<!--
			<tr>
				<td>
					<label>Número de dias nova cobrança:</label>
				</td>
				<td colspan="3">
					<input name="parametrosDistribuidor.numeroDiasNovaCobranca"
						   id="numeroDiasNovaCobranca" type="text" style="width: 40px;"
						   value="${parametrosDistribuidor.numeroDiasNovaCobranca}"
						   style="width:40px;" />
				</td>
			</tr>
			-->
			<tr>
				<td>
					<label>Assunto de e-mail de cobrança:</label>
				</td>
			</tr>
			<tr>
				<td colspan="4">
					<input name="parametrosDistribuidor.assuntoEmailCobranca" 
						   id="assuntoEmailCobranca" type="text" style="width: 400px;"
						   style="width:40px;"  value="${parametrosDistribuidor.assuntoEmailCobranca}"/>
				</td>
			</tr>
			<tr>
				<td>
					<label>Mensagem de e-mail de cobrança:</label>
				</td>
			</tr>
			<tr>
				<td colspan="4">
					<textarea name="parametrosDistribuidor.mensagemEmailCobranca"
						   id="mensagemEmailCobranca" type="text" style="width: 40px;"
						   style="width:40px;">${parametrosDistribuidor.mensagemEmailCobranca}</textarea>
				</td>
			</tr>
			
		</table>
 </fieldset>
</div>