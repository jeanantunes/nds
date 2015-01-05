<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/parametroCobrancaCota.js"></script>
<script language="javascript" type="text/javascript">

$(function(){
	parametroCobrancaCotaController.init();
});

</script>
<style>
#divRecebeEmail,#divTrasnsferenciaBancaria,#divDadosBancarios {
	display: none;
}

#dialog-pesq-fornecedor {
	display: none;
}

.forncedoresSel,.semanal,.mensal {
	display: none;
}

#dialog-unificacao,#dialog-excluir-unificacao {
	display: none;
}

#dialog-excluir-unificacao fieldset {
	width: 430px !important;
}

.form-contrato-hidden-class {
	visibility: hidden;
}

.parametroCobrancaFileField, .form-suspensao-hidden-class2 {
	display: none;
}


#dialog-unificacao fieldset {
	width: 440px !important;
}
#fornecedoresCota label{
	margin:0px!important;
}
</style>


</head>

<!--PESSOA FISICA - FINANCEIRO -->


<!--  <div id="tabpf-financeiro" > -->

<input type="hidden" name="_idCota" id="_idCota" />
<input type="hidden" name="_numCota" id="_numCota" />
<input type="hidden" name="_idFormaCobranca" id="_idFormaCobranca" />
<input type="hidden" name="_idParametroCobranca"
	id="_idParametroCobranca" />

<input type="hidden" name="tipoFormaCobranca" id="tipoFormaCobranca" />


<div id="dialog-confirm-formaCobrancaDistribuidor" title="Atenção" style="display: none;">
	<fieldset>
		<legend>Forma Cobran&ccedil;a da Cota</legend>
		<p>
			Confirma a altera&ccedil;&atilde;o na forma de cobran&ccedil;a da cota?
		</p>
	</fieldset>
</div>

<div name="formFinanceiro" id="formFinanceiro">
<fieldset style="width:880px!important; margin:5px;">
 	<legend>Financeiro</legend>

	<table width="100%" border="0" cellspacing="1" cellpadding="1">
		
		<tbody>
		
			<tr>
				<td width="195" style="width: 274px; ">Fator Vencimento de Cobrança em D+:</td>
				<td width="157" style="width: 147px; ">
				<select id="fatorVencimento" name="fatorVencimento" size="1" style="width: 50px; height: 19px;">
					    <option>0</option>
						<option>1</option>
						<option>2</option>
						<option>3</option>
						<option>4</option>
						<option>5</option>
						<option>6</option>
						<option>7</option>
						<option>8</option>
						<option>9</option>
						<option>10</option>
						<option>11</option>
						<option>12</option>
						<option>13</option>
						<option>14</option>
						<option>15</option>
						<option>16</option>
						<option>17</option>
						<option>18</option>
						<option>19</option>
						<option>20</option>
				</select>
				</td>
				
				<td width="10" style="width: 118px;">&nbsp;</td>
				<td width="60" style="width: 50px; ">Contrato:</td>
				<td width="20" style="width: 16px; "><input id="contrato" name="contrato"
					type="checkbox" style="float: left;"
					onclick="parametroCobrancaCotaController.exibe_form_contrato(this.checked);" />
				</td>
				<td width="377">
					<div class="form-contrato-hidden-class">
						<span name="botaoContrato" id="botaoContrato" class="bt_imprimir">
							<a href="javascript:;"
							onclick="parametroCobrancaCotaController.imprimeContrato()" target="_blank">Contrato</a>
						</span>
					</div>
				</td>
				
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td colspan="2" rowspan="4" valign="top">

					<div class="form-contrato-hidden-class">
						<table width="100%" border="0" cellspacing="1" cellpadding="1"
							class="periodos" style="">
							<tbody>
								<tr>
									<td width="15%">Dt. Início:</td>
									<td width="27%">
									<input type="text" name="dateInicio" class="dataInputMask"
										id="parametroCobrancaDateInicio" style="width: 70px;" 
										onblur="parametroCobrancaCotaController.calcularDataTermino()"></td>
									<td width="20%">Dt. Término:</td>
									<td width="38%"><input type="text" name="dateTermino" class="dataInputMask"
										id="parametroCobrancaDateTermino" style="width: 70px;"></td>
								</tr>
								<tr>
									<td>Recebido?</td>
									<td>
										
											<input type="checkbox" name="checkbox" id="parametroCobrancaIsRecebidoCheckBox" 
													onclick="parametroCobrancaCotaController.exibe_form_upload(this.checked)">
										
									</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td> <span class="parametroCobrancaFileField">Procurar:</span></td>
									<td colspan="3">
										<form id="parametroCobrancaFormUpload" 
											  action="<c:url value='/cota/parametroCobrancaCota/uploadContratoAnexo' />"
										 	  method="post" 
										 	  enctype="multipart/form-data">
										 	  
										 	  <input type="hidden" name="formUploadAjax" value="true" />
										 	  
											  <input id="parametroCobrancaFormUploadFile" name="uploadedFile" type="file" 
													onchange="parametroCobrancaCotaController.uploadContratoAnexo();" 
													class="parametroCobrancaFileField" size="30">
											  
											  <input id="parametroCobrancaNumeroCota" type="hidden" name="numeroCota" />
										</form>	
									</td>
								</tr>
								<tr>
									<td></td>
									<td id="parametroCobrancaArquivo"></td>
								</tr>
							</tbody>
						</table>
					</div>
				</td>
			</tr>
			<tr>
				<td>Valor Mínimo de Cobrança R$:</td>
				<td><input maxlength="15" name="valorMinimo" id="valorMinimo"
					type="text" style="width: 60px;" /></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>

				<td id="tituloDevolveEncalhe">Devolve Encalhe?</td>
				<td id="selectDevolveEncalhe"><select name="devolveEncalhe" id="devolveEncalhe">
						<option value="0" selected="selected">Sim</option>
						<option value="1">Não</option>
				</select></td>
				
				<td></td>
				<td></td>
				
			</tr>
			<!-- tr>
				<td>Unifica Cobrança?</td>
				<td><select name="unificaCobranca" id="unificaCobranca">
						<option value="0">Sim</option>
						<option value="1">Não</option>
				</select></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr -->
			
			<tr>
			    <td>Fornecedor Padrão:</td>
				<td>
					<select id="fornecedorPadrao" style="width: 220px;"></select>
				</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			
		</tbody>
	</table>
	
	<table width="770" border="0" cellspacing="1" cellpadding="1">
		<tbody>
		    <tr>
			    <td width="100" style="width: 221px; ">Utiliza Politica de Suspensão do Distribuidor:</td>
				<td width="24"><input id="sugereSuspensaoDistribuidor"
					name="sugereSuspensaoDistribuidor" type="checkbox" value="" onclick="parametroCobrancaCotaController.exibe_form_suspencao_distribuidor(this.checked);" /></td>
			    <td colspan="2" style="width: 326px; "></td>
			</tr>	
		</tbody>
	</table>		
	
	<div class="form-suspensao-hidden1-class">
	
		<table width="770" border="0" cellspacing="1" cellpadding="1">
		
			<tbody>	
			
					<tr>
						<td width="126" style="width: 114px; ">Sugere Suspensão:</td>

						<td width="24"><input id="sugereSuspensaoCota"
							name="sugereSuspensaoCota" type="checkbox" value="" onclick="parametroCobrancaCotaController.exibe_form_suspencao(this.checked);" /></td>

						<td colspan="3">
							<div class="form-suspensao-hidden2-class">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="31%" style="width: 171px; ">Qtde de dividas em aberto:</td>
										<td width="13%"><input maxlength="15" type="text"
											name="qtdDividasAbertoCota" id="qtdDividasAbertoCota"
											style="width: 60px;" /></td>
										<td width="8%">ou</td>
										<td width="6%">R$:</td>
										<td width="42%"><input maxlength="15" type="text"
											name="vrDividasAbertoCota" id="vrDividasAbertoCota"
											style="width: 60px;" /></td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
		
			</tbody>
			
		</table>
		
	</div>
	
	</fieldset>
	
</div>
<fieldset style="width:880px!important; margin:5px;">
 	<legend>Formas de Pagamento</legend>
	<table class="boletosUnificadosGrid"></table>

<br clear="all" />

<span id="FINANCEIRObtnNovaFormaPagamento" class="bt_novos"> <a
	href="javascript:;"
	isEdicao="true"
	onclick="parametroCobrancaCotaController.popup_nova_unificacao();" rel="tipsy" title="Nova Forma de Pagamento">
		<img src="${pageContext.request.contextPath}/images/ico_salvar.gif"
		hspace="5" border="0" /> 
</a>
</span>
</fieldset>

<br clear="all" />

<form id="form-unificacao">
	<div id="dialog-unificacao" title="Nova Unificação de Boletos">

		<jsp:include page="../messagesDialog.jsp">
			<jsp:param value="idModalUnificacao" name="messageDialog" />
		</jsp:include>

		<fieldset>
		
			<legend>Unificar Boletos</legend>

			<div name="formularioFormaCobranca" id="formularioFormaCobranca">		
                           
			    <table width="434" height="25" border="0" cellpadding="1" cellspacing="1">
				    
				     <tr style="height: 40px;">
				        <td valign="top"><strong>Tipo de Pagamento:</strong></td>
					    <td valign="top">&nbsp;</td>
					    <td valign="top">
		
					        <select name="tipoCobrancaParametroCobrancaCota" id="tipoCobrancaParametroCobrancaCota" class="formaCobrancaValueChangeListener" style="width:150px;" onchange="parametroCobrancaCotaController.obterParametrosDistribuidor(this.value);">
		                        <option value="">Selecione</option>
		                        <c:forEach varStatus="counter" var="itemTipoCobranca" items="${listaTiposCobranca}">
				                    <option value="${itemTipoCobranca.key}">${itemTipoCobranca.value}</option>
				                </c:forEach>
		                    </select> 
		
				        </td>    
				     </tr>
				    
				    
				     <tr class="header_table">
				         <td align="left">Fornecedores</td>
				         <td align="left">&nbsp;</td>
				         <td align="left">Concentração de Pagamentos</td>
				     </tr>
				     
			         <tr>
			             <td width="170" align="left" valign="top" style="border:1px solid #ccc;">

			                 <table width="168" border="0" cellspacing="1" cellpadding="1">

                                <div id="fornecedoresCota"/>
 
				             </table>

		                 </td>
		                 
					     <td width="21" align="left" valign="top">&nbsp;</td>
					     <td width="233" align="left" valign="top"style="border:1px solid #ccc;">
	
	
					         <table width="100%" border="0" cellspacing="1" cellpadding="1">
						         <tr>
						             <td width="20"><input type="radio" name="diario" id="diario" value="radio" onclick="parametroCobrancaCotaController.mostraDiario();" /></td>
						             <td width="173">Diário</td>
						             <td width="20"><input type="radio" name="semanal" id="semanal" value="radio" onclick="parametroCobrancaCotaController.mostraSemanal();" /></td>
						             <td width="173">Semanal</td>
						             <td width="20"><input type="radio" name="quinzenal" id="quinzenal" value="radio" onclick="parametroCobrancaCotaController.mostraQuinzenal();" /></td>
						             <td width="173">Quinzenal</td>
						             <td width="20"><input type="radio" name="mensal" id="mensal" value="radio" onclick="parametroCobrancaCotaController.mostraMensal();" /></td>
						             <td width="173">Mensal</td>
						         </tr>
						     </table>
						     
						     
						     <table width="100%" border="0" cellspacing="1" cellpadding="1" class="diario">
						         <tr>
						             <td width="68"></td>
						             <td width="156"></td>
						         </tr>
						     </table>
						     
						     
						     <table width="100%" border="0" cellspacing="1" cellpadding="1" class="quinzenal">
						         <tr>
						             <td width="68">Todo dia:</td>
						             <td width="320">
						                 <input maxlength="2" type="text" name="primeiroDiaQuinzenalParametroCobrancaCota" id="primeiroDiaQuinzenalParametroCobrancaCota" style="width:60px;"/>
						                 e 
						                 <input maxlength="2" type="text" name="segundoDiaQuinzenalParametroCobrancaCota" id="segundoDiaQuinzenalParametroCobrancaCota" style="width:60px;"/>
						             </td>
						         </tr>
						     </table>
						    
						    
						     <table width="100%" border="0" cellspacing="1" cellpadding="1" class="mensal">
						         <tr>
						             <td width="68">Todo dia:</td>
						             <td width="156"><input maxlength="2" type="text" name="diaDoMesCota" id="diaDoMesCota" style="width:60px;"/></td>
						         </tr>
						     </table>
					     
 			        
		                     <table width="100%" border="0" cellspacing="1" cellpadding="1" class="semanal">
							        
					             <tr>
					                 <td width="12%">
					                     <input type="checkbox" name="PCC-PS" id="PCC-PS" class="formaCobrancaValueChangeListener" />
					                 </td>    
					                 <td width="88%">
					                     <label for="PS">Segunda-feira</label>
					                 </td>
					             </tr>
							            
							     <tr>
					                 <td>           
							             <input type="checkbox" name="PCC-PT" id="PCC-PT" class="formaCobrancaValueChangeListener" />
							         </td>    
					                 <td>    
							             <label for="PT">Terça-feira</label>
							         </td>
					             </tr>
					             
					             <tr>
					                 <td>            
							             <input type="checkbox" name="PCC-PQ" id="PCC-PQ" class="formaCobrancaValueChangeListener" />
							         </td>    
					                 <td>      
							             <label for="PQ">Quarta-feira</label>
							         </td>
					              </tr>    
							                          
							      <tr>
					                 <td>          
							             <input type="checkbox" name="PCC-PQu" id="PCC-PQu" class="formaCobrancaValueChangeListener" />
							          </td>    
					                  <td>  
							             <label for="PQu">Quinta-feira</label>
							          </td>
					              </tr>
							                  
							      <tr>
					                 <td>          
							             <input type="checkbox" name="PCC-PSex" id="PCC-PSex" class="formaCobrancaValueChangeListener" />
							         </td>    
					                 <td>      
							             <label for="PSex">Sexta-feira</label>
							         </td>
					              </tr>    
							               
							      <tr>
					                 <td>    
							             <input type="checkbox" name="PCC-PSab" id="PCC-PSab" class="formaCobrancaValueChangeListener" />
							             </td>    
					                 <td>  
							             <label for="PSab">Sábado</label>
							         </td>
					              </tr>
							                   
							      <tr>
					                  <td>
							             <input type="checkbox" name="PCC-PDom" id="PCC-PDom" class="formaCobrancaValueChangeListener" />
							             </td>    
					                 <td>  
							             <label for="PDom">Domingo</label>
							         </td>
					              </tr>
							
							 </table>
							 
						 	
							 
					     </td>
	   
		             </tr>  
	
		         
					 <tr>
					    <td valign="top">&nbsp;</td>
					    <td valign="top">&nbsp;</td>
					    <td valign="top">&nbsp;</td>
					 </tr>
		
					  
	
				</table>

			</div>



			<div id="divComboBanco" style="display: none;">

				<div name="formularioDadosBanco" id="formularioDadosBanco">

					<table width="417" border="0" cellpadding="2" cellspacing="2">
						<tr>
							<td colspan="2"><b>Dados do Banco</b></td>
						</tr>

						<tr>
							<td width="57">Nome:</td>
							<td width="346">
								<select name="parametro-cobranca-banco" id="parametro-cobranca-banco" style="width: 150px;" class="formaCobrancaValueChangeListener">
									<option value=""></option>
									<c:forEach varStatus="counter" var="banco"
										items="${listaBancos}">
										<option value="${banco.key}">${banco.value}</option>
									</c:forEach>
							</select>
						</tr>
					</table>

				</div>
			</div>



<!-- 			<div id="divRecebeEmail" style="display: none;"> -->

<!-- 				<div name="formularioDadosBoleto" id="formularioDadosBoleto"> -->

<!-- 					<table width="417" border="0" cellpadding="2" cellspacing="2"> -->

<!-- 						<tr> -->
<!-- 							<td align="right"><input type="checkbox" id="recebeEmail" -->
<!-- 								name="recebeEmail" /></td> -->
<!-- 							<td>Receber Cobrança/Recibo por E-mail?</td> -->
<!-- 						</tr> -->

<!-- 					</table> -->

<!-- 				</div> -->

<!-- 			</div> -->

			<div id="divDadosBancarios" style="display: none;">

				<div name="formularioDadosDeposito" id="formularioDadosDeposito">

					<table width="558" border="0" cellspacing="2" cellpadding="2">

						<tr>
							<td colspan="4"><strong>Dados Bancários - Cota:</strong></td>
						</tr>

						<tr>
							<td width="88">Num. Banco:</td>
							<td width="120"><input maxlength="25" type="text"
								id="numBanco" name="numBanco" style="width: 60px;" /></td>
							<td width="47">Nome:</td>
							<td width="277"><input maxlength="40" type="text"
								id="nomeBanco" name="nomeBanco" style="width: 150px;" /></td>
						</tr>

						<tr>
							<td>Agência:</td>
							<td><input maxlength="17" type="text" id="agencia"
								name="agencia" style="width: 60px;" /> - <input maxlength="1"
								type="text" id="agenciaDigito" name="agenciaDigito"
								style="width: 30px;" /></td>
							<td>Conta:</td>
							<td><input maxlength="17" type="text" id="conta"
								name="conta" style="width: 60px;" /> - <input maxlength="1"
								type="text" id="contaDigito" name="contaDigito"
								style="width: 30px;" /></td>
						</tr>

						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>

					</table>

				</div>

			</div>


			<br clear="all" /> <span id="popupNovaFormaPagamentoIncluirNova" class="bt_add"> <a
				href="javascript:;"
				isEdicao="true"
				onclick="parametroCobrancaCotaController.incluirNovaUnificacao();">
					Incluir Novo </a>
			</span>

		</fieldset>
	</div>
</form>

<form id="form-excluir-unificacao">
	<div id="dialog-excluir-unificacao" title="Forma de Pagamento">
		<fieldset>
			<legend>Exclus&atilde;o Forma de Pagamento da Cota </legend>
			<p>Confirma a exclusão da Forma de Pagamento da Cota?</p>
		</fieldset>
	</div>
</form>

<form id="form-pesq-fornecedor">
	<div id="dialog-pesq-fornecedor" title="Selecionar Fornecedor">
		<fieldset>
			<legend>Selecione um ou mais Fornecedores para unificação
				dos boletos</legend>
			<select name="" size="1" multiple="multiple"
				style="width: 440px; height: 150px;">
				<option>Dinap</option>
				<option>FC</option>
				<option>Treelog</option>
			</select>
		</fieldset>
	</div>
</form>


<!-- /PESSOA FISICA - FINANCEIRO -->
