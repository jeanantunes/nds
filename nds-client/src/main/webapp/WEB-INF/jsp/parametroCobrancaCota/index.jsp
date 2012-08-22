<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/parametroCobrancaCota.js"></script>
<script language="javascript" type="text/javascript">

$(function(){
	parametroCobrancaCotaController.init();
});

</script>
<style>

#divRecebeEmail,
#divTrasnsferenciaBancaria,
#divDadosBancarios {display:none;}
#dialog-pesq-fornecedor{display:none;}
.forncedoresSel, .semanal, .mensal{display:none;}
#dialog-unificacao, #dialog-excluir-unificacao{display:none;}
#dialog-excluir-unificacao fieldset{width:430px!important;}
#dialog-unificacao fieldset {width:440px!important;}

</style>


</head>
	
    <!--PESSOA FISICA - FINANCEIRO -->
    
    
    <!--  <div id="tabpf-financeiro" > -->
       
    <input type="hidden" name="_idCota" id="_idCota"/>
    <input type="hidden" name="_numCota" id="_numCota"/>
    <input type="hidden" name="_idFormaCobranca" id="_idFormaCobranca"/>
    <input type="hidden" name="_idParametroCobranca"id="_idParametroCobranca"/>
    
    <input type="hidden" name="tipoFormaCobranca" id="tipoFormaCobranca"/>

    <div name="formFinanceiro" id="formFinanceiro">
    
	    <table width="770" border="0" cellspacing="1" cellpadding="1">
		      
		   <tr>
		   
		     <td width="212">Contrato:</td>
		     <td width="254">
		         <input id="contrato" name="contrato" type="checkbox" style="float:left;" onclick="parametroCobrancaCotaController.exibe_botao_contrato(this.checked);" />
		         <span name="botaoContrato" id="botaoContrato" class="bt_imprimir">
		             <!-- BOTAO PARA IMPRESSÃO DE CONTRATO -->
		             <a href="javascript:;" onclick="parametroCobrancaCotaController.imprimeContrato()">Contrato</a>
		         </span>
		     </td>
		      
		     <td width="168">Fator Vencimento em D+:</td>
		       
		     <td width="123">
			     <select id="fatorVencimento" name="fatorVencimento" size="1" style="width:50px; height:19px;" >
			       <option value="1">1</option>
			       <option value="2">2</option>
			       <option value="3">3</option>
			       <option value="4">4</option>
			       <option value="5">5</option>
			       <option value="6">6</option>
			       <option value="7">7</option>
			       <option value="8">8</option>
			       <option value="9">9</option>
			       <option value="10">10</option>
			       <option value="11">11</option>
			       <option value="12">12</option>
			       <option value="13">13</option>
			       <option value="14">14</option>
			       <option value="15">15</option>
			       <option value="16">16</option>
			       <option value="17">17</option>
			       <option value="18">18</option>
			       <option value="19">19</option>
			       <option value="20">20</option>
			     </select>
		     </td>
		     
		   </tr>
		   
		   <tr>
		   
		    <td>Tipo da Cota:</td>
			 <td>
		       <select name="tipoCota" id="tipoCota" style="width:150px;">
                  <option value="">Selecione</option>
                  <c:forEach varStatus="counter" var="itemTipoCota" items="${listaTiposCota}">
                      <option value="${itemTipoCota.key}">${itemTipoCota.value}</option>
                  </c:forEach>
               </select> 
             </td>  
             
             <td>Comissão %:</td>
		     <td>
		         <input maxlength="15" name="comissao" id="comissao" type="text" style="width:60px;" />
		     </td>
		     
		   </tr>
		   
		   <tr>
		   
		     <td>Sugere Suspensão:</td>
		     <td><input id="sugereSuspensao" name="sugereSuspensao" type="checkbox" value="" />
		     </td>
		     
		     <td>Valor Mínimo R$:</td>
		     <td>
		         <input maxlength="15" name="valorMinimo" id="valorMinimo" type="text" style="width:60px;" />
		     </td>
		     
		   </tr>

		   <tr>
		   
		     <td height="23">Sugere Suspensão quando atingir:</td>
		     <td  colspan="3">
			     <table width="100%" border="0" cellspacing="0" cellpadding="0">
			           
			        <tr>
			           <td width="31%">Qtde de dividas em aberto:</td>
			            
			           <td width="13%">
			               <input maxlength="15" type="text" name="qtdDividasAberto" id="qtdDividasAberto" style="width:60px;" />
			           </td>
			            
			           <td width="8%">ou</td>
			           <td width="6%">R$: </td>
			            
			           <td width="42%">
			               <input maxlength="15" type="text" name="vrDividasAberto" id="vrDividasAberto" style="width:60px;" />
			           </td>
			       </tr>
			       
		         </table>
		      </td>
		      
		   </tr>

		</table>
	</div>  

	<strong>Formas de Pagamento</strong>
	  
	<table class="boletosUnificadosGrid"></table>
	  
	<br clear="all" />
	  
	<span class="bt_novos" title="Nova Unificação">
	    <a href="javascript:;" onclick="parametroCobrancaCotaController.popup_nova_unificacao();">
	        <img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>
	        Nova Forma de Pagamento
	    </a>
	</span>
			
			
    <br clear="all" />

	<form id="form-unificacao">
    <div id="dialog-unificacao" title="Nova Unificação de Boletos">
		
		<jsp:include page="../messagesDialog.jsp">
			<jsp:param value="idModalUnificacao" name="messageDialog"/>
		</jsp:include>
		
		<fieldset>
			<legend>Unificar Boletos</legend>
			
			<div name="formularioFormaCobranca" id="formularioFormaCobranca">		
                           
			    <table width="434" height="25" border="0" cellpadding="1" cellspacing="1">
				    
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
				             
			                 <p><br clear="all" />
				                 <br clear="all" />
				                 <br clear="all" />
				                 <br clear="all" />
			                 </p>
			                 
		                 </td>
		                 
					     <td width="21" align="left" valign="top">&nbsp;</td>
					     <td width="233" align="left" valign="top"style="border:1px solid #ccc;">
	
	
					         <table width="100%" border="0" cellspacing="1" cellpadding="1">
						         <tr>
						             <td width="20"><input type="radio" name="mensal" id="mensal" value="radio" onclick="parametroCobrancaCotaController.mostraMensal();" /></td>
						             <td width="173">Mensal</td>
						             <td width="20"><input type="radio" name="semanal" id="semanal" value="radio" onclick="parametroCobrancaCotaController.mostraSemanal();" /></td>
						             <td width="173">Semanal</td>
						         </tr>
						     </table>
						    
						    
						     <table width="100%" border="0" cellspacing="1" cellpadding="1" class="mensal">
						         <tr>
						             <td width="68">Todo dia:</td>
						             <td width="156"><input maxlength="2" type="text" name="diaDoMes" id="diaDoMes" style="width:60px;"/></td>
						         </tr>
						     </table>
					     
 			        
		                     <table width="100%" border="0" cellspacing="1" cellpadding="1" class="semanal">
							        
					             <tr>
					                 <td>
					                     <input type="checkbox" name="PS" id="PS" />
					                 </td>    
					                 <td>
					                     <label for="PS">Segunda-feira</label>
					                 </td>
					             </tr>
							            
							     <tr>
					                 <td>           
							             <input type="checkbox" name="PT" id="PT" />
							         </td>    
					                 <td>    
							             <label for="PT">Terça-feira</label>
							         </td>
					             </tr>
					             
					             <tr>
					                 <td>            
							             <input type="checkbox" name="PQ" id="PQ" />
							         </td>    
					                 <td>      
							             <label for="PQ">Quarta-feira</label>
							         </td>
					              </tr>    
							                          
							      <tr>
					                 <td>          
							             <input type="checkbox" name="PQu" id="PQu" />
							          </td>    
					                  <td>  
							             <label for="PQu">Quinta-feira</label>
							          </td>
					              </tr>
							                  
							      <tr>
					                 <td>          
							             <input type="checkbox" name="PSex" id="PSex" />
							         </td>    
					                 <td>      
							             <label for="PSex">Sexta-feira</label>
							         </td>
					              </tr>    
							               
							      <tr>
					                 <td>    
							             <input type="checkbox" name="PSab" id="PSab" />
							             </td>    
					                 <td>  
							             <label for="PSab">Sábado</label>
							         </td>
					              </tr>
							                   
							      <tr>
					                  <td>
							             <input type="checkbox" name="PDom" id="PDom" />
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
		
					  
					 <tr>
				        <td valign="top"><strong>Tipo de Pagamento:</strong></td>
					    <td valign="top">&nbsp;</td>
					    <td valign="top">
		
					        <select name="tipoCobranca" id="tipoCobranca" style="width:150px;" onchange="parametroCobrancaCotaController.opcaoPagto(this.value);">
		                        <option value="">Selecione</option>
		                        <c:forEach varStatus="counter" var="itemTipoCobranca" items="${listaTiposCobranca}">
				                    <option value="${itemTipoCobranca.key}">${itemTipoCobranca.value}</option>
				                </c:forEach>
		                    </select> 
		
				        </td>    
				     </tr>
	
				</table>
				
		    </div>
		    

	        <div id="divComboBanco" style="display:none;">   
	            
	            <div name="formularioDadosBanco" id="formularioDadosBanco">	
	            
	                <table width="417" border="0" cellpadding="2" cellspacing="2">
	                    <tr>
				      	  <td colspan="2"><b>Dados do Banco</b></td>
				       	</tr>
				       	  
				      	<tr>
				      	  <td width="57">Nome:</td>
				      	  <td width="346">
				    	    
				    	    <select name="banco" id="banco" style="width:150px;">
				    	        <option value=""></option>
	                            <c:forEach varStatus="counter" var="banco" items="${listaBancos}">
					                <option value="${banco.key}">${banco.value}</option>
					            </c:forEach>
	                        </select>
				    	 
				      	</tr>
	                 </table>
	                 
		         </div>
	        </div>    
	        
        
        
	        <div id="divRecebeEmail" style="display:none;">   
	        
                <div name="formularioDadosBoleto" id="formularioDadosBoleto">
                
			  		<table width="417" border="0" cellpadding="2" cellspacing="2">
			  		
				      	<tr>
				      	  <td align="right">
				      	      <input type="checkbox" id="recebeEmail" name="recebeEmail" /></td>
				      	  <td>Receber por E-mail?</td>
				        </tr>
				        
			        </table>
			        
		        </div>
		        
	        </div>   
       
       
       
	        <div id="divDadosBancarios" style="display:none;">   
	        
				<div name="formularioDadosDeposito" id="formularioDadosDeposito">   
				           
		      	 	<table width="558" border="0" cellspacing="2" cellpadding="2">
						  
						  <tr>
						    <td colspan="4"><strong>Dados Bancários - Cota:</strong></td>
						  </tr>
						  
						  <tr>
						    <td width="88">Num. Banco:</td>
						    <td width="120"><input maxlength="25" type="text" id="numBanco" name="numBanco" style="width:60px;" /></td>
						    <td width="47">Nome:</td>
						    <td width="277"><input maxlength="40" type="text" id="nomeBanco" name="nomeBanco" style="width:150px;" /></td>
						  </tr>
						  
						  <tr>
						    <td>Agência:</td>
						    <td><input maxlength="17" type="text" id="agencia" name="agencia" style="width:60px;" />
						      -
						      <input maxlength="1" type="text" id="agenciaDigito" name="agenciaDigito" style="width:30px;" /></td>
						    <td>Conta:</td>
						    <td>
						        <input maxlength="17" type="text" id="conta" name="conta" style="width:60px;" />
						      -
						        <input maxlength="1" type="text" id="contaDigito" name="contaDigito" style="width:30px;" /></td>
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


			<br clear="all" />
			<span class="bt_add">
			    <a href="javascript:;" onclick="parametroCobrancaCotaController.incluirNovaUnificacao();">
			        Incluir Novo
			    </a>
			</span>


	    </fieldset>
	</div>	
	</form>
			
			
	<form id="form-excluir-unificacao">
	<div id="dialog-excluir-unificacao" title="Unificação de Boletos">
	<fieldset>
		<legend>Exclusão de Unificalção de Boletos</legend>
	    <p>Confirma a exclusão desta Unificação de Boleto</p>
	</fieldset>
	</div>
	</form>
	
	<form id="form-pesq-fornecedor">
	<div id="dialog-pesq-fornecedor" title="Selecionar Fornecedor">
	<fieldset>
		<legend>Selecione um ou mais Fornecedores para unificação dos boletos</legend>
	    <select name="" size="1" multiple="multiple" style="width:440px; height:150px;" >
	      <option>Dinap</option>
	      <option>FC</option>
	      <option>Treelog</option>
	    </select>
	</fieldset>
	</div>
	</form>
				    		    
           
    <!-- /PESSOA FISICA - FINANCEIRO -->  
    
