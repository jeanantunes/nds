<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/data.holder.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/baixaFinanceira.js"></script>

	<script type="text/javascript">
		var pesquisaCotaBaixaFinanceira = new PesquisaCota(baixaFinanceiraController.workspace);
		$(function(){
			baixaFinanceiraController.init();

			bloquearItensEdicao(baixaFinanceiraController.workspace);
		});
	</script>
	
	<style>

		#resultadoIntegracao{display:none;}
        #tableBaixaManual, #tableBaixaAuto, #extratoBaixaManual, #porNossoNumero, #porCota, #dialog-divida{display:none;}
        #dialog-baixa-dividas,#dialog-detalhes-divida, #bancoDividas, #labelBanco{display:none;}
        #dialog-confirma-baixa-numero,#dialog-confirma-baixa,#dialog-confirma-pendente,#dialog-postergar{display:none;}
        
	</style>
	
    </head>

<body>

	<jsp:include page="dialogsResumoBaixaAutomatica.jsp"></jsp:include>

	<div class="areaBts">
	
		<div class="area" style="display:none">
		
            <div id="botoesDividasPagas">
                <span class="bt_novos">
	             	<a onclick="baixaFinanceiraController.confirmarBaixa();"
	             	   href="javascript:;" rel="tipsy" title="Confirmar">
	             	   	<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">
	             	</a>
                </span>
             	<span class="bt_novos">
	             	<a onclick="baixaFinanceiraController.cancelarBaixa();"
	             	    href="javascript:;" rel="tipsy" title="Cancelar">
	             	   	<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_bloquear.gif">
	             	</a>
            	</span>
            </div>
			
			<div id="botoesDividasNaoPagas">
				<span class="bt_novos">
					<a isEdicao="true" onclick="baixaFinanceiraController.obterPagamentoDividas();" href="javascript:;" rel="tipsy" title="À Vista">
						<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">
					</a>
				</span>
				
				<span class="bt_novos">
					<a isEdicao="true" onclick="baixaFinanceiraController.obterPostergacao();" href="javascript:;" rel="tipsy" title="Postergar">
						<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_reprogramar.gif">
					</a>
				</span>
			</div>
			
			<span class="bt_arq">
				<a href="${pageContext.request.contextPath}/financeiro/baixa/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
			</span>
			
			<span class="bt_arq">
				<a href="${pageContext.request.contextPath}/financeiro/baixa/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
			</span>
		</div>
	</div>

	<div class="linha_separa_fields">&nbsp;</div>

	<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
		
		<legend>Baixa Financeira</legend>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="115">Tipo de Baixa:</td>
              <td colspan="3"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="8%"><input type="radio" name="baixaFinanceira" id="radioBaixaAutomatica" onclick="baixaFinanceiraController.mostrarBaixaAuto();" /></td>
                  <td width="63%">Automatica</td>
                  <td width="7%"><input type="radio" name="baixaFinanceira" id="radioBaixaManual" onclick="baixaFinanceiraController.mostrarBaixaManual();"/></td>
                  <td width="22%">Manual</td>
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
		
		<form action="<c:url value='/financeiro/baixa/realizarBaixaAutomatica' />" id="formBaixaAutomatica"
			  method="post" enctype="multipart/form-data" >
		
			<input type="hidden" name="formUploadAjax" value="true" />
			<input type="hidden" id="dataHidden" value="10/10/2012" />
		
			<table width="950" border="0" cellpadding="2" cellspacing="1"
				   class="filtro" id="tableBaixaAuto">
				
					<tr>
						<td width="140">Data Pagamento:</td>
						<td width="200">
							<input type="text" name="data" id="dataBaixa"
								   style="width: 90px; text-align: right;" value="${dataOperacao}" onchange="baixaFinanceiraController.limparValorInformadoBaixaAutomatica(this);" />
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
								<a href="javascript:;" onclick="baixaFinanceiraController.obterResumoBaixaFinanceira();" id="btnPesquisar"></a>
							</span>
						</td>
					</tr>			
			</table>
		</form>
		
		<!-- BAIXA MANUAL -->
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro" id="tableBaixaManual">
            <tr>
				<td width="20">Cota:</td>
                
                <td>
	                <input name="filtroNumCota" 
	             	       id="filtroNumCota" 
	             		   type="text"
	             		   maxlength="11"
	             		   style="width:60px; 
	             		   float:left; 
	             		   margin-right:5px;"
	             		   onchange="pesquisaCotaBaixaFinanceira.pesquisarPorNumeroCota('#filtroNumCota', '#descricaoCota');" />
				</td>
				
				<td width="30">Nome:</td>
             	
             	<td width="150">
		        	<input name="descricaoCota" 
		      		 	   id="descricaoCota" 
		      		 	   type="text"
		      		 	   class="nome_jornaleiro" 
		      		 	   maxlength="255"
		      		 	   style="width:130px;"
		      		 	   onkeyup="pesquisaCotaBaixaFinanceira.autoCompletarPorNome('#descricaoCota');" 
		      		 	   onblur="pesquisaCotaBaixaFinanceira.pesquisarPorNomeCota('#filtroNumCota', '#descricaoCota');" />
		        </td>
			  
				<td width="100">
				    Nosso Número:
				</td>
				
				<td width="200">
				    <input maxlength="100" type="text" name="filtroNossoNumero" id="filtroNossoNumero" style="width: 200px;" />
				</td>
				
				<td width="280">
				    Exibir apenas Cobran&ccedil;as pagas com pendencia
				</td>
				
				<td width="30">
				    <input type="checkbox" id="checkCobrancasBaixadas"/>
				</td>
				
				<td width="30">
				    <span class="bt_pesquisar">
				        <a href="javascript:;" onclick="baixaFinanceiraController.buscaManual('PESQUISA');"></a>
				    </span>
				</td>
				
			</tr>
        </table>
        
        <!--  -->
        
        
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
	
    
    <!-- BAIXA AUTOMÁTICA -->
    
	<fieldset class="classFieldset" id="resultadoIntegracao">
		<legend> Baixa Financeira Integrada</legend>
		<br />

		<jsp:include page="resumoBaixaAutomatica.jsp"></jsp:include>
		
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
		
	        <div  id="porNossoNumero" align="center">
		      	<table width="342" border="0" align="center" cellpadding="2" cellspacing="1" style="text-align:left;">
		      	  <tr>
		      	    <td colspan="2" class="header_table" align="center">Dados Boleto</td>
		   	      </tr>
		      	  <tr>
		      	    <td class="linha_borda"><strong>Núm.Boleto:</strong></td>
		      	    <td class="linha_borda" id="baixa-nossoNumero" />
		   	      </tr>
		      	  <tr>
		      	    <td class="linha_borda"><strong>Cota:</strong></td>
		      	    <td class="linha_borda" id="baixa-cota" />
		   	      </tr>
		      	  <tr>
		      	    <td width="81" class="linha_borda"><strong>Banco:</strong></td>
		      	    <td width="250" class="linha_borda" id="baixa-banco" />
		   	      </tr>
		      	  <tr>
		      	    <td class="linha_borda"><strong>Emissão:</strong></td>
		      	    <td class="linha_borda" id="baixa-dataEmissao" />
		   	      </tr>
		      	  <tr>
		      	    <td class="linha_borda"><strong>Vencimento:</strong></td>
		      	    <td class="linha_borda" id="baixa-dataVencimento" />
		   	      </tr>
		      	  <tr>
		      	    <td class="linha_borda"><strong>Valor R$:</strong></td>
		      	    <td class="linha_borda" id="baixa-valorBoleto" />
		   	      </tr>
		   	      <tr>
	   	      		<td class="dataPagamentoManualBoleto" width="100">Data Pagamento:</td>
		            <td class="dataPagamentoManualBoleto">
						<input onchange="baixaFinanceiraController.atualizarDadosCobrancaManualBoleto();" name="dtPagamentoManualBoleto" type="text" id="dtPagamentoManualBoleto" style="width:120px; text-align:right;" />
					</td>
				  </tr>
				  
				  <tr style="display:none" id="infoEncalheBoletoAntecipado">
		      	    <td class="linha_borda"><strong>Encalhe R$:</strong></td>
		      	    <td class="linha_borda">  
		      	    <input type="hidden" id="isBoletoAntecipado" />
		      	    <input maxlength="22" onchange="baixaFinanceiraController.calculaTotalManual(true);" id="encalhe" type="text" style="width:120px; text-align:right;"/>  </td>
		   	      </tr>
		   	      
		   	      <tr>
		      	    <td class="linha_borda"><strong>Multa R$:</strong></td>
		      	    <td class="linha_borda">  <input maxlength="22" onblur="baixaFinanceiraController.calculaTotalManual();" id="multa" type="text" style="width:120px; text-align:right;"/>  </td>
		   	      </tr>
		   	      
		   	      <tr>
		      	    <td class="linha_borda"><strong>Juros R$:</strong></td>
		      	    <td class="linha_borda">  <input maxlength="22" onblur="baixaFinanceiraController.calculaTotalManual();" id="juros" type="text" style="width:120px; text-align:right;"/>  </td>
		   	      </tr>
		   	      
		   	      
		   	      <tr id="infoDescontoCobranca">
		      	    <td class="linha_borda"><strong>Desconto R$:</strong></td>
		      	    <td class="linha_borda">  <input maxlength="22" onblur="baixaFinanceiraController.calculaTotalManual();" id="desconto" type="text" style="width:120px; text-align:right;"/>  </td>
		   	      </tr>
		   	      
		   	      <tr>
		      	    <td class="linha_borda">&nbsp;</td>
		      	    <td class="linha_borda">&nbsp;</td>
  				  </tr>
      	          <tr>
      	            <td class="linha_borda"><strong>Valor Total R$:</strong></td>
      	            <td class="linha_borda" id="baixa-valorTotal" />
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
		                	
		                	
	
		                </td>
						
						<td width="30%">   

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
		                        
		                        <input isEdicao="true" title="Selecionar todas as Dívidas" type="checkbox" id="selTodos" name="selTodos" onclick="baixaFinanceiraController.selecionarTodos(this.checked);" style="float:left;"/>
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
				    
				
					<table width="433" border="0" cellpadding="2" cellspacing="2" id="pagarDividas">
					  <tr>
					    <td width="90"><strong>Valor Dívida R$:</strong>
					    
					    </td>
					    <td width="100" id="valorDividas" ></td>
					    
					    <td class="dataPagamentoManual" width="100"><strong>Data Pagamento:</strong></td>
			            <td class="dataPagamentoManual">
							<input onchange="baixaFinanceiraController.atualizarDadosCobrancaManual();" name="dtPagamentoManual" type="text" id="dtPagamentoManual" style="width:80px; text-align:right;" />
						</td>
					    
					  </tr>
					  <tr>
					    <td><strong>Multa R$:</strong></td>
					    <td colspan="3"><input  maxlength="16" id="multaDividas" name="multaDividas" onblur="baixaFinanceiraController.calculaTotalManualDividas();baixaFinanceiraController.calculaSaldoDividas();" type="text" style="width:80px; text-align:right;" /></td>
					  </tr>
					  <tr>
					    <td><strong>Juros R$:</strong></td>
					    <td colspan="3"><input maxlength="16" id="jurosDividas" name="jurosDividas" onblur="baixaFinanceiraController.calculaTotalManualDividas();baixaFinanceiraController.calculaSaldoDividas();" type="text" style="width:80px; text-align:right;" /></td>
					  </tr>
					  <tr>
					    <td><strong>Desconto R$:</strong></td>
					    <td colspan="3"><input maxlength="16" id="descontoDividas" name="descontoDividas" onblur="baixaFinanceiraController.calculaTotalManualDividas();baixaFinanceiraController.calculaSaldoDividas();" type="text" style="width:80px; text-align:right;" /></td>
					  </tr>
					  <tr>
					    <td><strong>Valor pago R$:</strong></td>
					    <td colspan="3"><input maxlength="16" id="valorPagoDividas" name="valorPagoDividas" onblur="baixaFinanceiraController.calculaSaldoDividas();" type="text" style="width:80px; text-align:right;" /></td>
					  </tr>
					  <tr>
					    <td>&nbsp;</td>
					    <td colspan="3" style="border-bottom:1px solid #000;">&nbsp;</td>
					  </tr>
					  <tr>
					    <td><strong>Saldo R$:</strong></td>
					    <td colspan="3" id="valorSaldoDividas" ></td>
					  </tr>
					  
					  
					  <tr>
					    <td><strong>Forma Recebimento:</strong></td>
					    <td colspan="3">
					        <select name="formaRecebimentoDividas" id="formaRecebimentoDividas"  onchange="baixaFinanceiraController.mostrarBancos(this.value);" style="width:150px;">
		                        <option value="">Selecione</option>
		                        <c:forEach varStatus="counter" var="itemTipoCobranca" items="${listaTiposCobranca}">
				                    <option value="${itemTipoCobranca.key}">${itemTipoCobranca.value}</option>
				                </c:forEach>
		                    </select> 
					    </td>
					  </tr>
					  
					  <tr>
					    <td><strong><span id="labelBanco">Banco:</span></strong></td>
					    <td colspan="3">
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
					    <td colspan="3"><textarea maxlength="150" name="observacoesDividas" id="observacoesDividas" cols="45" rows="3" style="width:260px;"></textarea></td>
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
			            <td width="103"><input name="dtPostergada" type="text" id="dtPostergada" style="width:80px;" onchange="baixaFinanceiraController.obterPostergacao(true);" /></td>
			          </tr>
			          <tr>
			            <td>Encargos R$:</td>
			            <td><input name="ecargosPostergacao" id="ecargosPostergacao" type="text" style="width:80px; text-align:right;" disabled="disabled" /></td>
			          </tr>
			          <tr>
			            <td>Isentos Encargos</td>
			            <td><input onchange="baixaFinanceiraController.alterarIsencao(this.checked === true)" type="checkbox" name="checkIsIsento" id="checkIsIsento" /></td>
			          </tr>
			        </table>
			    </fieldset>
			</div>

			
	    </fieldset>
    
    </form>
    
    <!--  -->
</body>