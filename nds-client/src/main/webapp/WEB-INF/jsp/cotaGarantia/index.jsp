<script language="text/javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/cotaGarantia.js"></script>
<script language="text/javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/manterCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
<script language="text/javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script language="text/javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/confirmDialog.js"></script>
<script language="text/javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/utils.js"></script>
<script type="text/javascript">
	var tipoCotaGarantia = new TipoCotaGarantia(MANTER_COTA.workspace);
		
</script>

<fieldset style="width:880px!important; margin:5px;">
 	<legend>Tipos de Garantias</legend>	
	<table width="860" cellpadding="2" cellspacing="2"
		style="text-align: left;">
		<tr>
		
			<td width="90">Tipo de Garantia:</td>
			<td width="256"><select id="tipoGarantiaSelect"
				onchange="tipoCotaGarantia.onChange($(this).val());"
				style="width: 250px;">
					<option value="" selected="selected">Selecione...</option>
			</select></td>
			
			<td width="128">Classificação da Cota:</td>
		    <td width="358" colspan="2">
	     	    <input disabled="disabled" name="cotaGarantiaClassificacaoCota" id="cotaGarantiaClassificacaoCota" style="width:300px;border: 0px; background-color: inherit; border-color: inherit;">
	        </td>
			
		</tr>
	</table>
</fieldset>
<div id="cotaGarantiaNotaPromissoriaPanel" style="display: none;">
	<fieldset style="width:880px!important; margin:5px;">
		<legend>Nota Promiss&oacute;ria</legend>

		<table width="755" cellpadding="2" cellspacing="2"
			style="text-align: left;">
			<tr>
				<td width="128">N&#176; Nota Promiss&oacute;ria:</td>
				<td width="219"><span id="cotaGarantiaNotaPromissoriaId"></span></td>
				<td width="78">Vencimento:</td>
				<td width="302"><input type="text"
					name="cotaGarantiaNotaPromissoriaVencimento"
					id="cotaGarantiaNotaPromissoriaVencimento" style="width: 120px;" /></td>

			</tr>
			<tr>
				<td>Valor R&#36;</td>

				<td><input type="text" name="cotaGarantiaNotaPromissoriaValor"
					id="cotaGarantiaNotaPromissoriaValor"
					style="width: 120px; text-align: right;" /></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>por extenso:</td>
				<td colspan="3"><input type="text"
					name="cotaGarantiaNotaPromissoriavalorExtenso"
					id="cotaGarantiaNotaPromissoriavalorExtenso" style="width: 425px;" /></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td colspan="3"><span class="bt_novos"><a
						href="javascript:;"
						id="cotaGarantiaNotaPromissoriaImprimir" rel="tipsy" title="Imprimir Nota Promissória"><img
							src="${pageContext.request.contextPath}/images/ico_impressora.gif"
							hspace="5" border="0" /></a></span></td>
			</tr>
		</table>
	</fieldset>
</div>

<div id="cotaGarantiaFiadorPanel" style="display: none;">

	<fieldset style="width:880px!important; margin:5px;">

		<legend>Fiador</legend>

		<table width="755" cellpadding="2" cellspacing="2"
			style="text-align: left;">
			<tr>
				<td width="121"><strong>Nome / Raz&atilde;o Social:</strong></td>

				<td width="323"><input type="text"
					id="cotaGarantiaFiadorSearchName"  style="width:300px;"/></td>

				<td width="60"><strong>CPF/CNPJ:</strong></td>

				<td width="223"><input type="text"
					id="cotaGarantiaFiadorSearchDoc" style="width: 200px;" /></td>
			</tr>

		</table>

	</fieldset>

	<br clear="all" /> <br />
	<div id="cotaGarantiaFiadorDadosPanel" style="display: none;">
		<fieldset style="width:880px!important; margin:5px;">
			<legend>Dados do Fiador</legend>
			<table width="755" cellpadding="2" cellspacing="2"
				style="text-align: left;">
				<tr>
					<td width="154"><strong>Nome / Raz&atilde;o Social:</strong></td>

					<td width="272"><span id="cotaGarantiaFiadorNome"></span></td>

					<td width="87"><strong>CPF/CNPJ:</strong></td>

					<td width="214"><span id="cotaGarantiaFiadorDoc"></span></td>
				</tr>
				<tr>
					<td><strong>Endere&ccedil;o:</strong></td>
					<td colspan="3"><span id="cotaGarantiaFiadorEndereco"></span></td>
				</tr>
				<tr>
					<td><strong>Telefone:</strong></td>

					<td><span id="cotaGarantiaFiadorTelefone"></span></td>

					<td>&nbsp;</td>

					<td>&nbsp;</td>

				</tr>

			</table>

		</fieldset>
		<br clear="all" /> <br />
		<fieldset class="cotaGarantiaFiadorDadosPanel" style="width:880px!important;">
			<legend>Garantias Cadastradas</legend>
			<table id="cotaGarantiaFiadorGarantiasGrid"></table>
		</fieldset>
	</div>
</div>

<div id="cotaGarantiaChequeCaucaoPanel" style="display: none;">
	<fieldset style="width:880px!important; margin:5px;">
		<legend>Dados Banc&aacute;rios</legend>
		<table width="601" border="0" cellspacing="2" cellpadding="2">
			<tr>
				<td width="138">Num. Banco:</td>
				<td width="122"><input type="text"
					name="cotaGarantiaChequeCaucaoNumeroBanco"
					id="cotaGarantiaChequeCaucaoNumeroBanco" style="width: 60px;" /></td>

				<td width="82">Nome:</td>
				<td width="233"><input type="text"
					name="cotaGarantiaChequeCaucaoNomeBanco"
					id="cotaGarantiaChequeCaucaoNomeBanco" style="width: 200px;" /></td>
			</tr>

			<tr>
				<td>Ag&ecirc;ncia:</td>
				<td><input type="text" name="cotaGarantiaChequeCaucaoAgencia"
					id="cotaGarantiaChequeCaucaoAgencia" style="width: 60px;" />- <input
					type="text" name="cotaGarantiaChequeCaucaoDvAgencia"
					id="cotaGarantiaChequeCaucaoDvAgencia" style="width: 30px;" /></td>

				<td>Conta:</td>
				<td><input type="text" name="cotaGarantiaChequeCaucaoConta"
					id="cotaGarantiaChequeCaucaoConta" style="width: 60px;" />- <input
					type="text" name="cotaGarantiaChequeCaucaoDvConta"
					id="cotaGarantiaChequeCaucaoDvConta" style="width: 30px;" /></td>
			</tr>

			<tr>
				<td>N&ordm; Cheque:</td>
				<td><input type="text"
					name="cotaGarantiaChequeCaucaoNumeroCheque"
					id="cotaGarantiaChequeCaucaoNumeroCheque" style="width: 107px;" />
				</td>
				<td>Valor R$:</td>
				<td><input type="text" name="cotaGarantiaChequeCaucaoValor"
					id="cotaGarantiaChequeCaucaoValor"
					style="width: 108px; text-align: right;" /></td>
			</tr>
			<tr>
				<td>Data do Cheque:</td>
				<td><input type="text" name="cotaGarantiaChequeCaucaoEmissao"
					id="cotaGarantiaChequeCaucaoEmissao" style="width: 107px;" /></td>
				<td>Validade:</td>
				<td><input type="text" name="cotaGarantiaChequeCaucaoValidade"
					id="cotaGarantiaChequeCaucaoValidade"
					style="width: 108px; text-align: right;" /></td>
			</tr>
			<tr>
				<td>Nome Correntista:</td>
				<td colspan="3"><input type="text"
					name="cotaGarantiaChequeCaucaoCorrentista"
					id="cotaGarantiaChequeCaucaoCorrentista" style="width: 345px;" />
				</td>
			</tr>

			<tr>
				<td>Imagem Cheque:</td>
				<td colspan="3">
					<form action="${pageContext.request.contextPath}/cadastro/garantia/uploadCheque" id="cotaGarantiaChequeCaucaoFormUpload" method="post" enctype="multipart/form-data" onsubmit="ChequeCaucao.prototype.uploadFormOnSubmit()">
						<input type="hidden" id="idCheque" name="idCheque" size="58" />
						<input type="file" id="cotaGarantiaChequeCaucaoUpload" name="image" size="58" />
						<input type="submit" value="Enviar"/> 
					</form>
				</td>
			</tr>
		</table>
	</fieldset>
	<br clear="all" /> <br />
	<div id="cotaGarantiaChequeCaucaoImagemPanel" style="display: none;">
		<fieldset style="width:880px!important; margin:5px;">
			<legend>Foto Cheque</legend>
			<br />
			<div align="center" id="cotaGarantiaChequeCaucaoImagem"></div>
		</fieldset>
	</div>
</div>

<div id="cotaGarantiaImovelPanel" style="display: none;">
	<div id="dialog-excluir-imovel" title="Excluir Im&oacute;vel" style="display:none;" >
		<p>Confirma a exclus&atilde;o desse im&oacute;vel?</p>
	</div>
	<fieldset style="width:880px!important; margin:5px;">
		<legend>Im&oacute;vel</legend>
		<table width="755" border="0" cellpadding="2" cellspacing="2"
			style="text-align: left;">
			<tbody>
				<tr>
					<td width="106">Propriet&aacute;rio:</td>
					<td width="635"><input type="text"
						name="cotaGarantiaImovelProprietario"
						id="cotaGarantiaImovelProprietario" style="width: 250px;"></td>
				</tr>
				<tr>
					<td>Endere&ccedil;o:</td>
					<td><input type="text" name="cotaGarantiaImovelEndereco"
						id="cotaGarantiaImovelEndereco" style="width: 450px;"></td>
				</tr>
				<tr>
					<td>N&uacute;mero Registro:</td>
					<td><input type="text" name="cotaGarantiaImovelNumeroRegistro"
						id="cotaGarantiaImovelNumeroRegistro" style="width: 250px;"></td>
				</tr>
				<tr>
					<td>Valor R$:</td>
					<td><input type="text" name="cotaGarantiaImovelValor"
						id="cotaGarantiaImovelValor"
						style="width: 100px; text-align: right;"></td>
				</tr>
				<tr>
					<td>Observa&ccedil;&atilde;o:</td>
					<td><textarea name="cotaGarantiaImovelObservacao" rows="3"
							id="cotaGarantiaImovelObservacao" style="width: 450px;"></textarea></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>
					
					<span class="bt_novos"> <a href="javascript:;"
							id="cotaGarantiaImovelIncluirNovo" rel="tipsy" title="Incluir Novo">
							<img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0">
							</a>
					</span>
					
					<span class="bt_novos">
			  			<a href="javascript:;" id="cotaGarantiaImovelSalvaEdicao" style="display:none;" rel="tipsy" title="Salvar">
			  			<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"> </a>
			  		</span>
					
					</td>

				</tr>
			</tbody>
		</table>
	</fieldset>
	<br clear="all"> <br>

	<fieldset style="width:880px!important; margin:5px;">
		<legend>Im&oacute;veis Cadastradas</legend>
		<div class="flexigrid" style="width: 740px;">
			<table class="cotaGarantiaImovelGrid"></table>
		</div>
	</fieldset>
</div>

<div id="cotaGarantiaCaucaoLiquida" style="display: none;">

	
	<div id="dialog-confirma-resgate" title="Confirmar Resgate Caução Líquida" style="display:none;" >
		<fieldset style="width: 500px;">
		
			<legend>Resgate de Cau&ccedil;&atilde;o L&iacute;quida</legend>
			
    		<p>Confirma o Resgate da Cau&ccedil;&atilde;o L&iacute;quida no Valor R$ 
    		<input type="text" id="valorResgate" style="text-align: right; width: 100px;" /> 
    		?</p>
    		
		</fieldset>
	</div>
	
	<fieldset style="width:880px!important; margin:5px;">
	   	<legend>Cau&ccedil;&atilde;o L&iacute;quida</legend>
        <table width="860" cellpadding="2" cellspacing="2" style="text-align:left;">
            <tbody>
            	<tr>
            		<td width="850">

                 <table width="755" border="0" cellspacing="2" cellpadding="2">
                     <tr>
				        <td valign="top" width="140">Forma de Pagamento:</td>
					    <td valign="top" width="232">
		
					        <select name="tipoCobranca" id="tipoCobranca" style="width:150px;" onchange="CaucaoLiquida.prototype.opcaoPagto(this.value);">
		                    </select> 
		
				        </td>
				        
				        <td width="361">
					        <div name="divFormaDeposito" id="divFormaDeposito" style="display: none;">
								Valor R$
								<input type="text" id="valorDeposito" style="text-align: right;" />
							</div>
							
							<div name="divFormaDinheiro" id="divFormaDinheiro" style="display: none;">
								Valor R$
								<input type="text" id="valorDinheiro" style="text-align: right;" />
							</div>
				        </td>
				        
				     </tr>
                 </table>
                 
                 <div name="divFormaBoleto" id="divFormaBoleto" style="display: none;">
    
	                 <table width="50%" border="0" cellspacing="1" cellpadding="1">
				         <tr>
				             <td width="20"><input type="radio" name="diario" id="diario" value="radio" onclick="CaucaoLiquida.prototype.mostraDiario();" /></td>
				             <td width="69">Diário</td>
				             <td width="20"><input type="radio" name="semanal" id="semanal" value="radio" onclick="CaucaoLiquida.prototype.mostraSemanal();" /></td>
				             <td width="70">Semanal</td>
				             <td width="20"><input type="radio" name="quinzenal" id="quinzenal" value="radio" onclick="CaucaoLiquida.prototype.mostraQuinzenal();" /></td>
				             <td width="76">Quinzenal</td>
				             <td width="20"><input type="radio" name="mensal" id="mensal" value="radio" onclick="CaucaoLiquida.prototype.mostraMensal();" /></td>
				             <td width="105">Mensal</td>
				         </tr>
				     </table>
							     
	                 <table width="100%">
	                    
	                     <tr>
	                         <td>
	                            
							     <table width="100%" border="0" cellspacing="1" cellpadding="1" class="diario">
							         <tr>
							             <td width="68"></td>
							             <td width="156"></td>
							         </tr>
							     </table>
							     
							     
							     <table width="60%" border="0" cellspacing="1" cellpadding="1" class="quinzenal">
							         <tr>
							             <td width="68">Todo dia:</td>
							             <td width="320">
							                 <input maxlength="2" type="text" name="primeiroDiaQuinzenal" id="primeiroDiaQuinzenal" style="width:60px;"/>
							                 e 
							                 <input maxlength="2" type="text" name="segundoDiaQuinzenal" id="segundoDiaQuinzenal" style="width:60px;"/>
							             </td>
							         </tr>
							     </table>
							    
							    
							     <table width="60%" border="0" cellspacing="1" cellpadding="1" class="mensal">
							         <tr>
							             <td width="68">Todo dia:</td>
							             <td width="156"><input maxlength="2" type="text" name="diaDoMes" id="diaDoMes" style="width:60px;"/></td>
							         </tr>
							     </table>
						     
	 			        
			                     <table width="96%" border="0" cellspacing="1" cellpadding="1" class="semanal">
	
			                        <tr>
									    <td width="20"><input type="checkbox" name="PS" id="PS" /></td>
									    <td width="95">Segunda-feira  </td>
									    <td width="20"><input type="checkbox" name="PT" id="PT" /></td>
									    <td width="76">Terça-feira  </td>
									    <td width="20"><input type="checkbox" name="PQ" id="PQ" /></td>
									    <td width="82">Quarta-feira  </td>
									    <td width="20"><input type="checkbox" name="PQu" id="PQu" /></td>
									    <td width="79">Quinta-feira  </td>
									    <td width="20"><input type="checkbox" name="PSex" id="PSex" /></td>
									    <td width="76">Sexta-feira  </td>
									    <td width="20"><input type="checkbox" name="PSab" id="PSab" /></td>
									    <td width="55">Sábado  </td>
									    <td width="20"><input type="checkbox" name="PDom" id="PDom" /></td>
									    <td width="73">Domingo  </td>
									</tr>
	
								 </table>
								 
						     </td>
			             </tr>  
	                 </table>   
	                 
	                 
	                 <table width="100%" border="0" cellspacing="1" cellpadding="1">
						  <tr>
						    <td width="9%">Valor R$:</td>
						    <td width="15%"><input name="cota-garantia-valorBoleto" id="cota-garantia-valorBoleto" type="text" maxlength="16" style="width:80px; text-align:right;" onblur="CaucaoLiquida.prototype.calculaValorParcela();" /></td>
						    <td width="13%">Qtde. Parcelas </td>
						    <td width="10%"><input name="qtdParcelaBoleto" id="qtdParcelaBoleto" type="text" maxlength="6" style="width:50px; text-align:center;" onchange="CaucaoLiquida.prototype.calculaValorParcela();" /></td>
						    <td width="15%">Valor Parcela R$:</td>
						    <td width="38%"><input disabled="disabled" name="valorParcelaBoleto" id="valorParcelaBoleto" type="text" maxlength="16" style="width:80px; text-align:right;" /></td>
						  </tr>
					 </table>
	                 
	                 
                 </div>
                 
                 <div name="divFormaDesconto" id="divFormaDesconto" style="display: none;">

                     <table width="100%" border="0" cellspacing="1" cellpadding="1">
				        <tr>
					    	<td colspan="5"><strong>Comissão da Cota:</strong></td>
					    </tr>
					  	<tr>
					    	<td width="6%">Valor R$:</td>
					    	<td width="11%"><input maxlength="16" name="valorDesconto" id="valorDesconto" type="text" style="width:50px; text-align:right;" /></td>
					    	<td width="11%">Desconto Atual:</td>
					    	<td width="7%"><input maxlength="16" name="valorDescontoAtual" id="valorDescontoAtual" type="text" style="width:50px; text-align:right;" /></td>
					    	<td width="65%">%</td>
				    	</tr>
					  	<tr>
						    <td>Utilizar:</td>
						    <td><input maxlength="16" name="utilizarDesconto" id="utilizarDesconto" type="text" style="width:50px; text-align:right;" /></td>
						    <td>Desconto da Cota:</td>
						    <td><input maxlength="16" name="descontoCotaDesconto" id="descontoCotaDesconto" type="text" style="width:50px; text-align:right;" /></td>
						    <td>&nbsp;</td>
					    </tr>
				     </table>

                 </div>
                 </td>
               </tr>
                        
            </tbody>
            
        </table>
	</fieldset>
	
	
	
    <br clear="all">
    
    <br>
    
    
    <table>
	    <tr>
	       <td width="203">
	           <span class="bt_novos">
	        	   <a id="cotaGarantiaCaucaoLiquidaResgatar" href="javascript:;" rel="tipsy" title="Resgatar Valor Cau&ccedil;&atilde;o">
	        		   <img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_negociar.png">
	        	   </a>
	           </span>
	       </td>
	    </tr>
    </table>
            
     
    
    <fieldset style="width:880px!important; margin:5px;">
    	<legend>Cau&ccedil;&atilde;o L&iacute;quida</legend>
    	
    	<div class="flexigrid" style="width: 740px; ">
    		<table id="cotaGarantiaCaucaoLiquidaGrid"></table>
        </div>
	</fieldset>
	
	
	<br clear="all">
    
    <br>
    
    <div id="divComboBancoCedente" style="display: none;" >
    
        <fieldset style="width:880px!important; margin:5px;">

            <legend>Caução Debitada da conta</legend>

			<div name="formularioDadosBancoCedente" id="formularioDadosBancoCedente">

				<table width="755" border="0" cellpadding="2" cellspacing="2">
					<tr>
						<td width="57">Nome:</td>
						<td width="346"><select name="bancoCedente" id="bancoCedente"
							style="width: 150px;">
								<option value=""></option>
								<c:forEach varStatus="counter" var="banco" items="${listaBancosCedente}">
									<option value="${banco.key}">${banco.value}</option>
								</c:forEach>
						</select>
					</tr>
				</table>

			</div>

	    </fieldset>
	    
	</div>
	
	<fieldset style="width:880px!important; margin:5px;">

   	    <legend>Caução Depositada na conta</legend>
    
        <table width="755" border="0" cellspacing="2" cellpadding="2">
        
			  <tr>
			      <td width="16%">Número Banco:</td>
			      <td width="27%"><input maxlength="100" type="text" name="numBancoDeposito" id="numBancoDeposito" style="width:100px;" /></td>
			      <td width="15%">Agência:</td>
			      <td width="42%"><input maxlength="17" type="text" name="agenciaDeposito" id="agenciaDeposito" style="width:100px;" /></td>
			  </tr>
			  
			  <tr>
			      <td>Banco:</td>
			      <td><input maxlength="100" type="text" name="nomeBancoDeposito" id="nomeBancoDeposito" style="width:100px;" /></td>
			      <td>Conta-Corrente:</td>
			      <td><input maxlength="17" type="text" name="contaDeposito" id="contaDeposito" style="width:100px;" /></td>
			  </tr>
			  
			  <tr>
			      <td>Nome Correntista:</td>
			      <td colspan="3"><input maxlength="100" type="text" name="nomeCorrentistaDeposito" id="nomeCorrentistaDeposito" style="width:430px;" /></td>
			  </tr>
			  
		</table>
           
     </fieldset>

</div>


<div id="cotaGarantiaOutros" style="display: none;">
	
	<div id="dialog-excluir-outros" title="Excluir Garantia" style="display:none;" >
	
		<p>Confirma a exclus&atilde;o dessa garantia?</p>
	
	</div>
	
	<fieldset style="width:880px!important; margin:5px;">

	<legend>Informe</legend>
	
        <table style="text-align: left;" border="0" cellSpacing="2" cellPadding="2" width="755">
    
         <tbody><tr>
           
           <td width="106">Descrição:</td>
           
           <td width="635"><textarea style="width: 450px;" id="descricaoCotaGarantiaOutros" rows="3" name="descricaoCotaGarantiaOutros"></textarea></td>
         
         </tr>
    
         <tr>
         
           <td>Valor R$:</td>
         
           <td><input style="width: 100px; text-align: right;" id="valorCotaGarantiaOutros" name="valorCotaGarantiaOutros" type="text"></td>
         
         </tr>
    
         <tr>
         
           <td>Validade:</td>
         
           <td><input style="width: 100px; text-align: right;" id="validadeCotaGarantiaOutros" name="validadeCotaGarantiaOutros" type="text"></td>
         
         </tr>
    
         <tr>
         
           <td>&nbsp;</td>
         
           <td>
               
           		<span class="bt_novos"> 
           			<a href="javascript:;" id="cotaGarantiaOutrosIncluirNovo" rel="tipsy" title="Incluir Novo">
							<img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0"></a>
				</span>
				
				<span class="bt_novos">
				
		  			<a href="javascript:;" id="cotaGarantiaOutrosSalvaEdicao" style="display:none;" rel="tipsy" title="Salvar">
		  			<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"> </a>
		  			
		  		</span>
               
           	</td>
         
         </tr>
    
    	 </tbody></table>

	</fieldset>
	
    <br clear="all">
    
    <br>
    
    <fieldset>
    
    	<legend>Outras Garantias Cadastradas</legend>
    	
    	<div class="flexigrid" style="width: 740px; ">
    	
    		<table id="cotaGarantiaOutrosGrid"></table>
        
        </div>
        
	</fieldset>

</div>
<br clear="all" />