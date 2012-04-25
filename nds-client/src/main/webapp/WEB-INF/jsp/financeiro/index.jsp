<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script language="javascript" type="text/javascript">


	
	function carregaFinanceiro(){
		var idCota = $("#_idCotaRef").val();
		populaFinanceiro(idCota);
		exibe_botao_contrato();
		mostrarGridConsulta();
	}

	
	
	
	
	
	
	
	
	

    $(function() {	
		$(".boletosUnificadosGrid").flexigrid({
			preProcess: getDataFromResult,
			dataType : 'json',
			colModel : [  {
				display : 'Fornecedores',
				name : 'fornecedores',
				width : 120,
				sortable : true,
				align : 'left'
			},{
				display : 'Concentração de Pagamento',
				name : 'concentracaoPagto',
				width : 170,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo de Pagamento',
				name : 'tipoPagto',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Detalhes - Tipo de Pagamento',
				name : 'detalhesTipoPagto',
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 50,
				sortable : false,
				align : 'center'
			}],
			width : 770,
			height : 150
		});
    });	
    
    function mostrarGridConsulta() {
    	
		/*PASSAGEM DE PARAMETROS*/
		$(".boletosUnificadosGrid").flexOptions({
			/*METODO QUE RECEBERA OS PARAMETROS*/
			url: "<c:url value='/cadastro/financeiro/consultaFormasCobranca' />",
			params: [
			         {name:'idCota', value:$("#_idCotaRef").val()},
			        ] ,
			        newp: 1
		});
		
		/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
		$(".boletosUnificadosGrid").flexReload();
		
		$(".grids").show();
	}	
	
	function getDataFromResult(resultado) {
		
		//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			$(".grids").hide();
			return resultado.tableModel;
		}
		
		var dadosPesquisa;
		$.each(resultado, function(index, value) {
			  if(value[0] == "TblModelFormasCobranca") {
				  dadosPesquisa = value[1];
			  }
	    });
		
		
        /*
		$.each(dadosPesquisa.rows, 
				function(index, row) {

					 var linkEditar = '<a href="javascript:;" onclick="editarFormaCobranca(' + row.cell[0] + ');" style="cursor:pointer">' +
                                      '<img src="${pageContext.request.contextPath}/images/ico_editar.gif" hspace="5" border="0px" title="Altera forma cobrança" />' +
	                                  '</a>';			
				
			         var linkExcluir =    '<a href="javascript:;" onclick="excluirFormaCobranca(' + row.cell[0] + ');" style="cursor:pointer">' +
			                              '<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" title="Exclui forma cobrança" />' +
						                  '</a>';		 					 
									
				     row.cell[9] = linkEditar + linkExcluir;

		         }
		);
		*/
		
		
		return dadosPesquisa;
	}
    
    
    
    
    
    

	
	
	
	
	

	
	function limparDivDeposito() {
		$("#numBanco").val("");
		$("#nomeBanco").val("");
		$("#agencia").val("");
		$("#agenciaDigito").val("");
		$("#conta").val("");
	    $("#contaDigito").val("");
	}
	
	
	
    function limparDivComboBanco() {
    	$("#banco").val("");
	}
	
    
    
    function limparDivBoleto() {
    	$("#recebeEmail").val("");
    }
    
    

	function opcaoPagto(op){
		
		if ((op=='BOLETO')||(op=='BOLETO_EM_BRANCO')){
			$('#divComboBanco').show();
			$('#divBoleto').show();
			limparDivDeposito();
	    }
		else if ((op=='CHEQUE')||(op=='TRANSFERENCIA_BANCARIA')){
			$('#divComboBanco').show();
			limparDivBoleto();
			limparDivDeposito();
		}    
		else if (op=='DEPOSITO'){
			$('#divDeposito').show();
			limparDivBoleto();
			limparDivComboBanco();
		}    
		else{
			limparDivBoleto();
			limparDivDeposito();
			limparDivComboBanco();
			$('#divBoleto').hide();
			$('#divComboBanco').hide();
			$('#divDeposito').hide();
		}
		
	};
	
	
	
	function populaFinanceiro(idCota){
		var data = [{name: 'idCota', value: idCota}];
		$.postJSON("<c:url value='/cadastro/financeiro/populaFinanceiro' />",
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
		$("#numBanco").val(resultado.numBanco);
		$("#nomeBanco").val(resultado.nomeBanco);
		$("#agencia").val(resultado.agencia);
		$("#agenciaDigito").val(resultado.agenciaDigito);
		$("#conta").val(resultado.conta);
	    $("#contaDigito").val(resultado.contaDigito);
		$("#fatorVencimento").val(resultado.fatorVencimento);
		
		$("#recebeEmail").val(resultado.recebeEmail);
		document.formFinanceiro.recebeEmail.checked = resultado.recebeEmail;
	
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
		var numBanco            = $("#numBanco").val();
		var nomeBanco           = $("#nomeBanco").val();
		var agencia             = $("#agencia").val();
		var agenciaDigito       = $("#agenciaDigito").val();
		var conta               = $("#conta").val();
		var contaDigito         = $("#contaDigito").val();
		var fatorVencimento     = $("#fatorVencimento").val();
		
		$("#recebeEmail").val(0);
		if (document.formFinanceiro.recebeEmail.checked){
			$("#recebeEmail").val(1);
		}
		var recebeEmail = $("#recebeEmail").val();
		
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


	
	function exibe_botao_contrato(){
		if (document.formFinanceiro.contrato.checked){
			$('#botaoContrato').show();
		}
		else{
			$('#botaoContrato').hide();
		}
	}
	

	
	
	
	
	
	
	function popup_nova_unificacao() {
		
		$( "#dialog-nova-unificacao" ).dialog({
			resizable: false,
			height:550,
			width:500,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
    };

    
    function popup_excluir_unificacao() {
    	
		$( "#dialog-excluir-unificacao" ).dialog({
			resizable: false,
			height:170,
			width:490,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	
	
</script>


<style>

#divBoleto,
#divTrasnsferenciaBancaria,
#divDeposito {display:none;}
#dialog-pesq-fornecedor{display:none;}
.forncedoresSel{display:none;}
#dialog-nova-unificacao, #dialog-excluir-unificacao{display:none;}
#dialog-excluir-unificacao fieldset{width:430px!important;}
#dialog-nova-unificacao fieldset {width:440px!important;}

</style>


</head>

   <!--PESSOA FISICA - FINANCEIRO -->
    
    <form name="formFinanceiro" id="formFinanceiro"> 
    
	    <!--  <div id="tabpf-financeiro" > -->
	       
        <input type="hidden" id="_idCota"/>
        <input type="hidden" id="_numCota"/>
    
	    <table width="671" border="0" cellspacing="2" cellpadding="2">
		      
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
		         <input id="contrato" name="contrato" type="checkbox" style="float:left;" onclick="exibe_botao_contrato();" />
		         <span name="botaoContrato" id="botaoContrato" class="bt_imprimir">
		             <!-- BOTAO PARA IMPRESSÃO DE CONTRATO - FUNÇÃO PROVISÓRIA ADICIONADA PARA POSTAR DADOS NA SESSAO -->
		             <a href="javascript:;" onclick="">Contrato</a>
		         </span>
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
  
		</table>
		  

		<strong>Formas de Pagamento</strong>
		  
		<table class="boletosUnificadosGrid"></table>
		  
		<br clear="all" />
		  
		<span class="bt_novos" title="Nova Unificação">
		    <a href="javascript:;" onclick="popup_nova_unificacao();">
		        <img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>
		        Nova Forma de Pagamento
		    </a>
		</span>
				
				
	    <br clear="all" />


	    
	    
	    
	    <div id="dialog-nova-unificacao" title="Nova Unificação de Boletos">
			<fieldset>
				<legend>Unificar Boletos</legend>
			    
			    <table width="434" height="25" border="0" cellpadding="1" cellspacing="1">
				    
				     <tr class="header_table">
				         <td align="left">Fornecedores</td>
				         <td align="left">&nbsp;</td>
				         <td align="left">Concentração de Pagamentos</td>
				     </tr>
				     
			         <tr>
			             <td width="170" align="left" valign="top" style="border:1px solid #ccc;"><table width="168" border="0" cellspacing="1" cellpadding="1">
			             
			             <tr>
			                 <td width="23"><input type="checkbox" name="checkbox12" id="checkbox6" /></td>
			                 <td width="138">Abril</td>
			             </tr>
			             
			             <tr>
			                 <td><input type="checkbox" name="checkbox11" id="checkbox5" /></td>
			                 <td>Treelog</td>
			             </tr>
			             
			             <tr>
			                 <td><input type="checkbox" name="checkbox10" id="checkbox4" /></td>
			                 <td>Dinap</td>
			             </tr>
			             
				         <tr>
				             <td><input type="checkbox" name="checkbox8" id="checkbox3" /></td>
				             <td>FC</td>
				         </tr>
				         
			             </table>
			             
		                 <p><br clear="all" />
			                 <br clear="all" />
			                 <br clear="all" />
			                 <br clear="all" />
		                 </p></td>
		                 
					     <td width="21" align="left" valign="top">&nbsp;</td>
					     <td width="233" align="left" valign="top"style="border:1px solid #ccc;">
					     
		                     <table width="100%" border="0" cellspacing="1" cellpadding="1">
							        
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
		
					        <select name="tipoCobranca" id="tipoCobranca" style="width:150px;" onchange="opcaoPagto(this.value);">
		                        <option value="">Selecione</option>
		                        <c:forEach varStatus="counter" var="itemTipoCobranca" items="${listaTiposCobranca}">
				                    <option value="${itemTipoCobranca.key}">${itemTipoCobranca.value}</option>
				                </c:forEach>
		                    </select> 
		
				        </td>    
				     </tr>
	
				</table>
			    
			    

		        <div id="divComboBanco" style="display:none;">   
		            
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
	
	
				<br clear="all" />
				<span class="bt_add">
				    <a href="javascript:;" onclick="postarFinanceiro();">
				        Incluir Novo
				    </a>
				</span>


		    </fieldset>
		</div>
				
				
		<div id="dialog-excluir-unificacao" title="Unificação de Boletos">
		<fieldset>
			<legend>Exclusão de Unificação de Boletos</legend>
		    <p>Confirma a exclusão desta Unificação de Boleto</p>
		</fieldset>
		</div>
		
		
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
    