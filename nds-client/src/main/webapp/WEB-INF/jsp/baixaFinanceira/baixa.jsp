<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/data.holder.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/baixaFinanceira.js"></script>

	<script type="text/javascript">
		var pesquisaCotaBaixaFinanceira = new PesquisaCota();
		$(function(){
			baixaFinanceiraController.init();
		});
	</script>
	
	<style>

		#resultadoIntegracao{display:none;}
        #tableBaixaManual, #tableBaixaAuto, #extratoBaixaManual, #porNossoNumero, #porCota, #dialog-divida{display:none;}
        #dialog-baixa-dividas,#dialog-detalhes-divida{display:none;}
        #dialog-confirma-baixa-numero,#dialog-confirma-baixa,#dialog-confirma-pendente,#dialog-postergar{display:none;}
        
	</style>
	
    </head>

<body>

	<fieldset class="classFieldset">
		
		<legend> Baixa Financeira</legend>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="115">Tipo de Baixa:</td>
              <td colspan="3"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="7%"><input type="radio" name="baixaFinanceira" id="radioBaixaManual" onclick="baixaFinanceiraController.mostrarBaixaManual();"/></td>
                  <td width="22%">Manual</td>
                  <td width="8%"><input type="radio" name="baixaFinanceira" id="radioBaixaAutomatica" onclick="baixaFinanceiraController.mostrarBaixaAuto();" /></td>
                  <td width="63%">Automatica</td>
                </tr>
              </table></td>
              <td width="112">&nbsp;</td>
              <td width="114">&nbsp;</td>
              <td width="55">&nbsp;</td>
              <td width="102">&nbsp;</td>
              <td width="104">&nbsp;</td>
            </tr>
        </table>
		
		
		<!-- BAIXA AUTOMÁTICA -->
		
		<form action="<c:url value='/financeiro/realizarBaixaAutomatica' />" id="formBaixaAutomatica"
			  method="post" enctype="multipart/form-data" >
		
			<input type="hidden" name="formUploadAjax" value="true" />
		
			<table width="950" border="0" cellpadding="2" cellspacing="1"
				   class="filtro" id="tableBaixaAuto">
				
					<tr>
						<td width="100">Data:</td>
						<td width="200">
							<input type="text" name="dataBaixa" id="dataBaixa" 
								   onchange="baixaFinanceiraController.alterarEstadoInputsBaixaAutomatica();"
								   style="width: 90px; text-align: right;" value="${dataOperacao}" />
						</td>
						<td width="65">Arquivo:</td>
						<td colspan="3">
							<input name="uploadedFile" type="file" id="uploadedFile" size="25" 
								   onchange="baixaFinanceiraController.habilitarIntegracao();" />
						</td>
						
						<td width="200">Valor Financeiro R$:</td>
						<td width="288">
							<input type="text" name="valorFinanceiro"
								   id="valorFinanceiro" style="width: 90px; text-align: right;" />
						</td>
						
						<td width="111">
							<span class="bt_integrar" title="Integrar" id="btnIntegrar" style="display:none">
								<a href="javascript:;" onclick="baixaFinanceiraController.integrar();">Integrar</a>
							</span>
							<span class="bt_pesquisar" title="Exibir Resumo" id="btnExibirResumos">
								<a href="javascript:;" onclick="baixaFinanceiraController.exibirResumo();" id="btnPesquisar"></a>
							</span>
						</td>
					</tr>			
			</table>
		</form>
		<!--  -->
		
		
		<!-- BAIXA MANUAL -->
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro" id="tableBaixaManual">
            <tr>
				<td width="29">Cota:</td>
                
                <td>
              
                <input name="filtroNumCota" 
             	    id="filtroNumCota" 
             		type="text"
             		maxlength="11"
             		style="width:60px; 
             		float:left; margin-right:5px;"
             		onchange="pesquisaCotaBaixaFinanceira.pesquisarPorNumeroCota('#filtroNumCota', '#descricaoCota');" />
				</td>
				
				<td width="39">Nome:</td>
             	
             	<td width="210">
		        	<input name="descricaoCota" 
		      		 	   id="descricaoCota" 
		      		 	   type="text"
		      		 	   class="nome_jornaleiro" 
		      		 	   maxlength="255"
		      		 	   style="width:130px;"
		      		 	   onkeyup="pesquisaCotaBaixaFinanceira.autoCompletarPorNome('#descricaoCota');" 
		      		 	   onblur="pesquisaCotaBaixaFinanceira.pesquisarPorNomeCota('#filtroNumCota', '#descricaoCota');" />
		        </td>
			  
				<td width="97">Nosso Número:</td>
				<td width="333"><input maxlength="100" type="text" name="filtroNossoNumero" id="filtroNossoNumero" style="width: 300px;" /></td>
				<td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="baixaFinanceiraController.buscaManual();">Pesquisar</a></span></td>
			</tr>
        </table>
        
        <!--  -->
        
        
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
	
    
    <!-- BAIXA AUTOMÁTICA -->
    
	<fieldset class="classFieldset" id="resultadoIntegracao">
		<legend> Baixa Financeira Integrada</legend>
		<br />

		<table border="0" align="center" cellpadding="2" cellspacing="2">
			<tr>
				<td valign="top">
					<table width="269" border="0" align="center" cellpadding="2"
						cellspacing="1" style="display: inline; margin-right: 15px;">
						<tr>
							<td colspan="2" align="center" class="header_table">Dados do
								Arquivo</td>
						</tr>
						<tr>
							<td width="121" align="left" class="linha_borda"><strong>Nome
									do Arquivo:</strong></td>
							<td id="nomeArquivo" width="137" align="right" class="linha_borda"></td>
						</tr>
						<tr>
							<td align="left" class="linha_borda"><strong>Data
									Competência:</strong></td>
							<td id="dataCompetencia" align="right" class="linha_borda"></td>
						</tr>
						<tr>
							<td align="left" class="linha_borda"><strong>Valor
									R$:</strong></td>
							<td id="somaPagamentos" align="right" class="linha_borda"></td>
						</tr>
						<tr>
							<td align="left" class="linha_borda">&nbsp;</td>
							<td align="right" class="linha_borda">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="2" align="left"
								style="line-height: 28px; border: 1px solid #0C0;"><img
								src="${pageContext.request.contextPath}/images/bt_check.gif" width="22" height="22"
								alt="Arquivo Integrado com Sucesso" align="left" /> <span><strong>Arquivo
										Integrado com Sucesso!</strong></span></td>
						</tr>
					</table>
				</td>
				<td valign="top"><table width="275" border="0" align="center"
						cellpadding="2" cellspacing="1" style="display: inline;">
						<tr>
							<td colspan="2" align="center" class="header_table"
								class="linha_borda">Baixa Automática</td>
						</tr>
						<tr>
							<td width="162" align="left" class="linha_borda"><strong>Registros
									Lidos:</strong></td>
							<td id="quantidadeLidos" width="102" align="right" class="linha_borda"></td>
						</tr>
						<tr>
							<td align="left" class="linha_borda"><strong>Registros
									Baixados:</strong></td>
							<td id="quantidadeBaixados" align="right" class="linha_borda"></td>
						</tr>
						<tr>
							<td align="left" class="linha_borda"><strong>Registros
									Rejeitados:</strong></td>
							<td id="quantidadeRejeitados" align="right" class="linha_borda"></td>
						</tr>
						<tr>
							<td align="left" class="linha_borda"><strong>Baixados
									com Divergência:</strong></td>
							<td id="quantidadeBaixadosComDivergencia" align="right" class="linha_borda"></td>
						</tr>
					</table></td>
			</tr>
		</table>
		
		<br /> <br />
		
		<div class="linha_separa_fields">&nbsp;</div>
		
		<br clear="all" />

	</fieldset>
		
	<div class="linha_separa_fields">&nbsp;</div>
	
	<!--  -->
	
	
	<!-- BAIXA MANUAL -->
	
	<form name="formularioListaDividas" id="formularioListaDividas">
	
	   
		<input type="hidden" id="valorTotalHidden" />
		<input type="hidden" id="valorBoletoHidden" />
		
		<input type="hidden" id="valorSaldoDividasHidden" />
		<input type="hidden" id="valorDividasHidden" />
		
		<input type="hidden" id="totalDividasHidden" />
	    <input type="hidden" id="totalDividasSelecionadasHidden" />
	    
	    <input type="hidden" id="saldoDividaHidden" />
	 
	
		<fieldset class="classFieldset" id="extratoBaixaManual" >
	      	<legend>Baixa Manual</legend>
	        <br />
	        
	        <div id="dialog-confirma-baixa-numero" title="Baixa Bancária">
			    <p>Deseja confirmar Baixa Manual deste Boleto ?</p>
		    </div>
		
	        <div  id="porNossoNumero">
		      	<table width="342" border="0" align="center" cellpadding="2" cellspacing="1" style="text-align:left;">
		      	  <tr>
		      	    <td colspan="2" class="header_table" align="center">Dados Boleto</td>
		   	      </tr>
		      	  <tr>
		      	    <td class="linha_borda"><strong>Núm.Boleto:</strong></td>
		      	    <td class="linha_borda" id="nossoNumero" />
		   	      </tr>
		      	  <tr>
		      	    <td class="linha_borda"><strong>Cota:</strong></td>
		      	    <td class="linha_borda" id="cota" />
		   	      </tr>
		      	  <tr>
		      	    <td width="81" class="linha_borda"><strong>Banco:</strong></td>
		      	    <td width="250" class="linha_borda" id="banco" />
		   	      </tr>
		      	  <tr>
		      	    <td class="linha_borda"><strong>Emissão:</strong></td>
		      	    <td class="linha_borda" id="dataEmissao" />
		   	      </tr>
		      	  <tr>
		      	    <td class="linha_borda"><strong>Vencimento:</strong></td>
		      	    <td class="linha_borda" id="dataVencimento" />
		   	      </tr>
		      	  <tr>
		      	    <td class="linha_borda"><strong>Valor R$:</strong></td>
		      	    <td class="linha_borda" id="valorBoleto" />
		   	      </tr>
		   	      
		   	      <tr>
		      	    <td class="linha_borda"><strong>Desconto R$:</strong></td>
		      	    <td class="linha_borda">  <input maxlength="22" onblur="baixaFinanceiraController.calculaTotalManual();" id="desconto" type="text" style="width:120px; text-align:right;"/>  </td>
		   	      </tr>
		   	      <tr>
		      	    <td class="linha_borda"><strong>Juros R$:</strong></td>
		      	    <td class="linha_borda">  <input maxlength="22" onblur="baixaFinanceiraController.calculaTotalManual();" id="juros" type="text" style="width:120px; text-align:right;"/>  </td>
		   	      </tr>
		   	      <tr>
		      	    <td class="linha_borda"><strong>Multa R$:</strong></td>
		      	    <td class="linha_borda">  <input maxlength="22" onblur="baixaFinanceiraController.calculaTotalManual();" id="multa" type="text" style="width:120px; text-align:right;"/>  </td>
		   	      </tr>
		   	      
		   	      <tr>
		      	    <td class="linha_borda">&nbsp;</td>
		      	    <td class="linha_borda">&nbsp;</td>
  				  </tr>
      	          <tr>
      	            <td class="linha_borda"><strong>Valor Total R$:</strong></td>
      	            <td class="linha_borda" id="valorTotal" />
                  <tr>
      	          <tr>
      	            <td class="linha_borda">&nbsp;</td>
      	            <td class="linha_borda">&nbsp;</td>
                  </tr>

		      	  <tr>
		      	    <td class="linha_borda">&nbsp;</td>
		      	    <td class="linha_borda"><span class="bt_confirmar_novo" title="Pagar"><a onclick="baixaFinanceiraController.mostrarPopupPagamento();" href="javascript:;"><img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Pagar</a></span></td>
		   	      </tr>
		   	      
		   	    </table>
			</div>
			
	      	<div  id="porCota">
	      	
		       <table class="liberaDividaGrid" id="tabelaDividas"></table>
		    
		       <table width="100%" border="0" cellspacing="2" cellpadding="2">
		            <tr>
		            
		                <td width="20%">
		                    <span class="bt_novos" title="Gerar Arquivo">
								<a href="${pageContext.request.contextPath}/financeiro/exportar?fileType=XLS">
									<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
									Arquivo
								</a>
							</span>
							
							<span class="bt_novos" title="Imprimir">
								<a href="${pageContext.request.contextPath}/financeiro/exportar?fileType=PDF">
									<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
									Imprimir
								</a>
							</span>
		                </td>

		                <td width="30%">   
		                    <span class="bt_confirmar_novo" title="Pagar Dívida"><a onclick="baixaFinanceiraController.obterPagamentoDividas();" href="javascript:;"><img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">À Vista</a></span>
		                    <span class="bt_confirmar_novo" title="Negociar Dívida"><a onclick="baixaFinanceiraController.obterNegociacao();" href="javascript:;"><img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Negociar</a></span>
		                    <span class="bt_confirmar_novo" title="Postergar Dívida"><a onclick="baixaFinanceiraController.obterPostergacao();" href="javascript:;"><img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Postergar</a></span>
		                </td>
		                
		                <td width="14%">
		                    <strong>Total Selecionado R$:</strong>
		                </td>
		                <td width="7%" id="totalDividasSelecionadas"></td>
		                
		                <td width="7%">
		                    <strong>Total R$:</strong>
		                </td>
		                <td width="7%" id="totalDividas"></td>
		                
		                <td width="20%">
		                
		                    <span class="checar">
		                        
		                        <label for="textoSelTodos" id="textoSelTodos">
		                            Marcar Todos
		                        </label>
		                        
		                        <input title="Selecionar todas as Dívidas" type="checkbox" id="selTodos" name="selTodos" onclick="baixaFinanceiraController.selecionarTodos(this.checked);" style="float:left;"/>
		                    </span>

		                </td>
		            </tr>
		        </table>
	        </div>
	        
	        <div id="dialog-confirma-baixa" title="Baixa Bancária">
			    <p>Deseja confirmar Baixa Manual ?</p>
		    </div>
		
			<div id="div-baixa-dividas">
				
				<div id="dialog-baixa-dividas" title="Baixa Bancária">
				
				    
				    <jsp:include page="../messagesDialog.jsp"></jsp:include>
				    
				
					<table width="433" border="0" cellpadding="2" cellspacing="2">
					  <tr>
					    <td width="153"><strong>Valor Dívida R$:</strong>
					    
					    </td>
					    <td width="266" id="valorDividas" ></td>
					  </tr>
					  <tr>
					    <td><strong>Multa R$:</strong></td>
					    <td><input  maxlength="16" id="multaDividas" name="multaDividas" onblur="baixaFinanceiraController.calculaTotalManualDividas();baixaFinanceiraController.calculaSaldoDividas();" type="text" style="width:80px; text-align:right;" /></td>
					  </tr>
					  <tr>
					    <td><strong>Juros R$:</strong></td>
					    <td><input maxlength="16" id="jurosDividas" name="jurosDividas" onblur="baixaFinanceiraController.calculaTotalManualDividas();baixaFinanceiraController.calculaSaldoDividas();" type="text" style="width:80px; text-align:right;" /></td>
					  </tr>
					  <tr>
					    <td><strong>Desconto R$:</strong></td>
					    <td><input maxlength="16" id="descontoDividas" name="descontoDividas" onblur="baixaFinanceiraController.calculaTotalManualDividas();baixaFinanceiraController.calculaSaldoDividas();" type="text" style="width:80px; text-align:right;" /></td>
					  </tr>
					  <tr>
					    <td><strong>Valor pago R$:</strong></td>
					    <td><input maxlength="16" id="valorPagoDividas" name="valorPagoDividas" onblur="baixaFinanceiraController.calculaSaldoDividas();" type="text" style="width:80px; text-align:right;" /></td>
					  </tr>
					  <tr>
					    <td>&nbsp;</td>
					    <td style="border-bottom:1px solid #000;">&nbsp;</td>
					  </tr>
					  <tr>
					    <td><strong>Saldo R$:</strong></td>
					    <td id="valorSaldoDividas" ></td>
					  </tr>
					  
					  
					  <tr>
					    <td><strong>Forma Recebimento:</strong></td>
					    <td>
					        <select name="formaRecebimentoDividas" id="formaRecebimentoDividas" style="width:150px;">
		                        <option value="">Selecione</option>
		                        <c:forEach varStatus="counter" var="itemTipoCobranca" items="${listaTiposCobranca}">
				                    <option value="${itemTipoCobranca.key}">${itemTipoCobranca.value}</option>
				                </c:forEach>
		                    </select> 
					    </td>
					  </tr>
					  
					   <tr>
					    <td><strong>Banco:</strong></td>
					    <td>
					        <select name="idBanco" id="bancoDividas" style="width:150px;">
		                        <option value="">Selecione</option>
		                        <c:forEach items="${bancos}" var="banco">
									<option value="${banco.id}" >${banco.nome}</option>
								</c:forEach>
		                    </select> 
					    </td>
					  </tr>
					  
					  
					  <tr>
					    <td>&nbsp;</td>
					    <td align="right"></td>
					  </tr>
					  <tr>
					    <td><strong>Observação:</strong></td>
					    <td><textarea maxlength="150" name="observacoesDividas" id="observacoesDividas" cols="45" rows="3" style="width:260px;"></textarea></td>
					  </tr>
					</table>
				</div>
           </div>  


			<div id="dialog-detalhes-divida" title="Detalhes da Dívida">
				<table class="dadosDividaGrid"></table>
                <table>
	                <tr>
	                    <td><strong>Saldo R$: </strong></td>
	                    <td id="saldoDivida"></td>
	                </tr>        
                </table>
			</div> 


            <div id="dialog-confirma-pendente" title="Baixa Manual de Dívidas">
			    <p>Deseja manter as dívidas com o status [Pendente] até a confirmação do pagamento ?</p>
		    </div> 

			<div id="dialog-postergar" title="Postergar D&iacute;vida">
				<fieldset style="width:255px!important;">
			    	<legend>Postergar D&iacute;vida</legend>
					<table width="230" border="0" cellspacing="2" cellpadding="0">
			          <tr>
			            <td width="121">Nova Data:</td>
			            <td width="103"><input name="dtPostergada" type="text" id="dtPostergada" style="width:80px;" onchange="baixaFinanceiraController.obterPostergacao();" /></td>
			          </tr>
			          <tr>
			            <td>Encargos R$:</td>
			            <td><input name="ecargosPostergacao" id="ecargosPostergacao" type="text" style="width:80px; text-align:right;" disabled="disabled" /></td>
			          </tr>
			          <tr>
			            <td>Isentos Encargos</td>
			            <td><input type="checkbox" name="checkIsIsento" id="checkIsIsento" /></td>
			          </tr>
			        </table>
			    </fieldset>
			</div>

			
	    </fieldset>
    
    </form>
    
    <!--  -->
    
	
</body>