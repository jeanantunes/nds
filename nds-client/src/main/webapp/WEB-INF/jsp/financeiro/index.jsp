<head>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>

<script type="text/javascript">


	$(function() {	
		var idCota = 1; //Receber aqui o ID da Cota que esta sendo alterada ou editada
		editarFinanceiro(idCota);
	});
	
	
	function buscaComboBancos(tipoCobranca){

	}


	function opcaoPagto(op){
		
		$('#divBoleto').hide();
		$('#divComboBanco').hide();
		$('#divDeposito').hide();
		
		if ((op=='BOLETO')||(op=='BOLETO_EM_BRANCO')){
			$('#divComboBanco').show();
			$('#divBoleto').show();
	    }
		else if ((op=='CHEQUE')||(op=='TRANSFERENCIA_BANCARIA')){
			$('#divComboBanco').show();
		}    
		else if (op=='DEPOSITO'){
			$('#divDeposito').show();
		}    
	};
	
	
	function editarFinanceiro(idCota){
		var data = [{name: 'idCota', value: idCota}];
		$.postJSON("<c:url value='/cadastro/financeiro/editarFinanceiro' />",
				   data,
				   sucessCallbackFinanceiro, 
				   null,
				   true);
	}
	
	
	function sucessCallbackFinanceiro(resultado) {
		
		//hidden
		$("#_idCota").val(resultado.idCota);
		$("#_numCota").val(resultado.numCota);
		
		$("#tipoCobranca").val(resultado.tipoCobranca);
		$("#banco").val(resultado.idBanco);
		$("#recebeEmail").val(resultado.recebeEmail);
		$("#numBanco").val(resultado.numBanco);
		$("#nomeBanco").val(resultado.nomeBanco);
		$("#agencia").val(resultado.agencia);
		$("#agenciaDigito").val(resultado.agenciaDigito);
		$("#conta").val(resultado.conta);
	    $("#contaDigito").val(resultado.contaDigito);
		$("#fatorVencimento").val(resultado.fatorVencimento);
	
		$("#sugereSuspensao").val(resultado.sugereSuspensao);
		document.formFinanceiro.sugereSuspensao.checked = resultado.sugereSuspensao;
		
		$("#contrato").val(resultado.contrato);
		document.formFinanceiro.contrato.checked = resultado.contrato;
		
		
		$("#PS").val(resultado.segunda);
		document.formFinanceiro.PS.checked = resultado.segunda;
		
		$("#PT").val(resultado.terca);
		document.formFinanceiro.PT.checked = resultado.terca;
		
		$("#PQ").val(resultado.quarta);
		document.formFinanceiro.PQ.checked = resultado.quarta;
		
		$("#PQu").val(resultado.quinta);
		document.formFinanceiro.PQu.checked = resultado.quinta;
		
		$("#PSex").val(resultado.sexta);
		document.formFinanceiro.PSex.checked = resultado.sexta;
		
		$("#PSab").val(resultado.sabado);
		document.formFinanceiro.PSab.checked = resultado.sabado;
		
		$("#PDom").val(resultado.domingo);
		document.formFinanceiro.PDom.checked = resultado.domingo;
		
		$("#valorMinimo").val(resultado.valorMinimo);
		$("#comissao").val(resultado.comissao);
		$("#qtdDividasAberto").val(resultado.qtdDividasAberto);
		$("#vrDividasAberto").val(resultado.vrDividasAberto);
	}
	
	
	
	
	
	function postarFinanceiro() {
		
		//hidden
		var idCota = $("#_idCota").val();
		var numCota = $("#_numCota").val();
		
		var tipoCobranca        = $("#tipoCobranca").val();
		var idBanco             = $("#banco").val();
		var recebeEmail         = $("#recebeEmail").val();
		var numBanco            = $("#numBanco").val();
		var nomeBanco           = $("#nomeBanco").val();
		var agencia             = $("#agencia").val();
		var agenciaDigito       = $("#agenciaDigito").val();
		var conta               = $("#conta").val();
		var contaDigito         = $("#contaDigito").val();
		var fatorVencimento     = $("#fatorVencimento").val();
		
		$("#sugereSuspensao").val(0);
		if (document.formFinanceiro.sugereSuspensao.checked){
			$("#sugereSuspensao").val(1);
		}
		var sugereSuspensao = $("#sugereSuspensao").val();
		
		$("#contrato").val(0);
		if (document.formFinanceiro.contrato.checked){
			$("#contrato").val(1);
		}
		var contrato = $("#contrato").val();
		
		$("#PS").val(0);
		if (document.formFinanceiro.PS.checked){
			$("#PS").val(1);
		}
		var segunda = $("#PS").val();
		
		$("#PT").val(0);
		if (document.formFinanceiro.PT.checked){
			$("#PT").val(1);
		}
		var terca = $("#PT").val();
		
		$("#PQ").val(0);
		if (document.formFinanceiro.PQ.checked){
			$("#PQ").val(1);
		}
		var quarta = $("#PQ").val();
		
		$("#PQu").val(0);
		if (document.formFinanceiro.PQu.checked){
			$("#PQu").val(1);
		}
		var quinta = $("#PQu").val();
		
		$("#PSex").val(0);
		if (document.formFinanceiro.PSex.checked){
			$("#PSex").val(1);
		}
		var sexta  = $("#PSex").val();
		
		$("#PSab").val(0);
		if (document.formFinanceiro.PSab.checked){
			$("#PSab").val(1);
		}
		var sabado = $("#PSab").val();
		
		$("#PDom").val(0);
		if (document.formFinanceiro.PDom.checked){
			$("#PDom").val(1);
		}
		var domingo  = $("#PDom").val();
		 
		var valorMinimo = $("#valorMinimo").val();
		var comissao = $("#comissao").val();
		var qtdDividasAberto = $("#qtdDividasAberto").val();
		var vrDividasAberto = $("#vrDividasAberto").val();
		
		$.postJSON("<c:url value='/cadastro/financeiro/postarFinanceiroSessao'/>",
				   "cotaCobranca.idCota="+idCota+ 
				   "&cotaCobranca.numCota="+numCota+ 
				   "&cotaCobranca.tipoCobranca="+tipoCobranca+ 
				   "&cotaCobranca.idBanco="+idBanco+            
				   "&cotaCobranca.recebeEmail="+recebeEmail+        
				   "&cotaCobranca.numBanco="+numBanco+        
				   "&cotaCobranca.nomeBanco="+nomeBanco+          
				   "&cotaCobranca.agencia="+agencia+            
				   "&cotaCobranca.agenciaDigito="+agenciaDigito+     
				   "&cotaCobranca.conta="+conta+              
				   "&cotaCobranca.contaDigito="+contaDigito+        
				   "&cotaCobranca.fatorVencimento="+fatorVencimento+    
				   "&cotaCobranca.sugereSuspensao="+sugereSuspensao+    
				   "&cotaCobranca.contrato="+contrato+   
				   "&cotaCobranca.domingo="+domingo+    
				   "&cotaCobranca.segunda="+segunda+            
				   "&cotaCobranca.terca="+terca+            
				   "&cotaCobranca.quarta="+quarta+            
				   "&cotaCobranca.quinta="+quinta+            
				   "&cotaCobranca.sexta="+sexta+            
				   "&cotaCobranca.sabado="+sabado+           
				   "&cotaCobranca.valorMinimo="+valorMinimo+        
				   "&cotaCobranca.comissao="+comissao+          
				   "&cotaCobranca.qtdDividasAberto="+qtdDividasAberto+   
				   "&cotaCobranca.vrDividasAberto="+vrDividasAberto);
	}


	
	
	
	function popup_pesq_fornecedor() {
		
		$( "#dialog-pesq-fornecedor" ).dialog({
			resizable: false,
			height:300,
			width:490,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$( ".forncedoresSel" ).css('display','table-cell');
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	function removeFornecedor(){
		$( ".forncedoresSel" ).fadeOut('fast');
	}

</script>


<style>

#divBoleto,
#divTrasnsferenciaBancaria,
#divDeposito {display:none;}
#dialog-pesq-fornecedor{display:none;}
.forncedoresSel{display:none;}

</style>


</head>

   <!--PESSOA FISICA - FINANCEIRO -->
    
    <form name="formFinanceiro" id="formFinanceiro"> 
    
	    <!--  <div id="tabpf-financeiro" > -->
	       
	       <input type="hidden" id="_idCota"/>
	       <input type="hidden" id="_numCota"/>
	    
		   <table width="671" border="0" cellspacing="2" cellpadding="2">
		      
		      <tr>
		        <td width="171">Tipo de Pagamento:</td>
		        <td width="486">

			        <select name="tipoCobranca" id="tipoCobranca" style="width:150px;" onchange="opcaoPagto(this.value);buscaComboBancos(this.value);">
                        <option value="">Selecione</option>
                        <c:forEach varStatus="counter" var="itemTipoCobranca" items="${listaTiposCobranca}">
		                    <option value="${itemTipoCobranca.key}">${itemTipoCobranca.value}</option>
		                </c:forEach>
                    </select> 

		        </td>
		        
		        
		        
		        
		        <!-- Provisório -->
	            <td>
				    <span class="bt_posta_sessao" title="Teste Posta Sessao">
				        <a href="javascript:;" onclick="postarFinanceiro();">
				        </a>
				    </span>
		        </td>
		        
		        
		        
		        
		      </tr>
		      
		      
		      <tr>
		      
		        <td colspan="2" nowrap="nowrap">
		        
		        
		        
		            <div id="divComboBanco" style="display:none;">   
		            
		                <table width="417" border="0" cellpadding="2" cellspacing="2">
		                    <tr>
					      	  <td colspan="2"><b>Dados do Banco</b></td>
					       	</tr>
					       	  
					      	<tr>
					      	  <td width="57">Nome:</td>
					      	  <td width="346">
					    	    
					    	    <select name="banco" id="banco" style="width:150px;">
		                            <c:forEach varStatus="counter" var="banco" items="${listaBancos}">
						                <option value="${banco.key}">${banco.value}</option>
						            </c:forEach>
		                        </select>
					    	 
					      	</tr>
		                 </table>
				        
			        </div>    
			        
		        
		        
			        <div id="divBoleto" style="display:none;">   
	
				  		<table width="417" border="0" cellpadding="2" cellspacing="2">
				  		
					      	<tr>
					      	  <td align="right">
					      	      <input type="checkbox" id="recebeEmail" name="recebeEmail" /></td>
					      	  <td>Receber por E-mail?</td>
					        </tr>
					        
				        </table>
				        
			        </div>   
			        
	
	
					<div id="divDeposito" style="display:none;">   
			            
			      	 	<table width="558" border="0" cellspacing="2" cellpadding="2">
							  
							  <tr>
							    <td colspan="4"><strong>Dados Bancários - Cota:</strong></td>
							  </tr>
							  
							  <tr>
							    <td width="88">Num. Banco:</td>
							    <td width="120"><input type="text" id="numBanco" name="numBanco" style="width:60px;" /></td>
							    <td width="47">Nome:</td>
							    <td width="277"><input type="text" id="nomeBanco" name="nomeBanco" style="width:150px;" /></td>
							  </tr>
							  
							  <tr>
							    <td>Agência:</td>
							    <td><input type="text" id="agencia" name="agencia" style="width:60px;" />
							      -
							      <input type="text" id="agenciaDigito" name="agenciaDigito" style="width:30px;" /></td>
							    <td>Conta:</td>
							    <td>
							        <input type="text" id="conta" name="conta" style="width:60px;" />
							      -
							        <input type="text" id="contaDigito" name="contaDigito" style="width:30px;" /></td>
							  </tr>
							  
							  <tr>
							    <td>&nbsp;</td>
							    <td>&nbsp;</td>
							    <td>&nbsp;</td>
							    <td>&nbsp;</td>
							  </tr>
							  
						 </table>
						 
			        </div>
			        
			        
			        
		
		        </td>
		        
		      </tr>
		      
		      
		      <tr>
		       
		        <td>Fator Vencimento em D+:</td>
		       
		        <td>
			        <select id="fatorVencimento" name="fatorVencimento" size="1" multiple="multiple" style="width:50px; height:19px;" >
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
		        
		      </tr>
		      
		      <tr>
		        <td>Sugere Suspensão:</td>
		        <td><input id="sugereSuspensao" name="sugereSuspensao" type="checkbox" value="" /></td>
		      </tr>
		      
		      <tr>
		        <td>Contrato:</td>
		        <td>
		            <input id="contrato" name="contrato" type="checkbox" style="float:left;" onclick="imprimir_contrato();" /> 
		            <span class="bt_imprimir"><a href="javascript:;">Contrato</a></span>
		        </td>
		      </tr>
		      
		      <tr>
		        <td>Concentração Pagamento:</td>
		        <td>
		            <input type="checkbox" name="PS" id="PS" />
		            <label for="PS">S</label>
		            <input type="checkbox" name="PT" id="PT" />
		            <label for="PT">T</label>
		            <input type="checkbox" name="PQ" id="PQ" />
		            <label for="PQ">Q</label>
		            <input type="checkbox" name="PQu" id="PQu" />
		            <label for="PQu">Q</label>
		            <input type="checkbox" name="PSex" id="PSex" />
		            <label for="PSex">S</label>
		            <input type="checkbox" name="PSab" id="PSab" />
		            <label for="PSab">S</label>
		            <input type="checkbox" name="PDom" id="PDom" />
		            <label for="PDom">D</label>
		        </td>
		      </tr>
		      
		      <tr>
		        <td>Valor Mínimo R$:</td>
		        <td>
		            <input name="valorMinimo" id="valorMinimo" type="text" style="width:60px;" />
		        </td>
		      </tr>
		      
		      <tr>
		        <td>Comissão %:</td>
		        <td>
		            <input name="comissao" id="comissao" type="text" style="width:60px;" />
		        </td>
		      </tr>
		      
		      <tr>
		        <td height="23">Sugere Suspensão quando:</td>
		        <td>
			        <table width="100%" border="0" cellspacing="0" cellpadding="0">
			           
			           <tr>
			              <td width="36%">Qtde de dividas em aberto:</td>
			            
			              <td width="15%">
			                  <input type="text" name="qtdDividasAberto" id="qtdDividasAberto" style="width:60px;" />
			              </td>
			            
			              <td width="6%">ou</td>
			              <td width="7%">R$: </td>
			            
			              <td width="36%">
			                  <input type="text" name="vrDividasAberto" id="vrDividasAberto" style="width:60px;" />
			              </td>
			          </tr>
			       
			         </table>
		         </td>
		      </tr>
		      
		      
		      <tr>
		          <td height="23">Unifica boleto?</td>
		          <td>
		              <table width="100%" border="0" cellspacing="0" cellpadding="0">
				          <tr>
					          <td>
					              <select name="select3" id="select2" onchange="popup_pesq_fornecedor();">
					                  <option selected="selected"> </option>
					                  <option>Sim</option>
					                  <option>Não</option>
					              </select>
					          </td>
						  </tr>
						      
					      <tr>
					          <td height="23">&nbsp;</td>
					          <td valign="middle">
					              
					              <div style="float:left; font-weight:bold; line-height:37px; margin-right:5px;">Unificar os boletos: </div>
					              
					              <div class="forncedoresSel">
					                  <label>Dinap</label> 
				                      <a href="javascript:;" onclick="removeFornecedor();">
				                          <img src="../images/ico_excluir.gif" width="15" height="15" alt="Excluir" border="0" />
				                      </a>
					              </div>
					        
					              <div class="forncedoresSel">
					                  <label>FC </label>
					                  <a href="javascript:;" onclick="removeFornecedor();">
					                      <img src="../images/ico_excluir.gif" width="15" height="15" alt="Excluir" border="0" />
					                  </a>
					              </div>
					           </td>
					      </tr>
					      
		              </table>
		          </td>
		      </tr>
		      
		  
		  </table>
	      <br clear="all" />
	    <!-- </div> -->
	    
	    
	    
	    
	    
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
    