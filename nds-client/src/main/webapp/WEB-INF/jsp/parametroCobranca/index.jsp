<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}" />
<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/parametroCobranca.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numberformatter-1.2.3.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>

<script language="javascript" type="text/javascript">

	var pesquisaCota = new PesquisaCota(parametroCobrancaController.workspace);

    $(function() {
    	parametroCobrancaController.init();
    	bloquearItensEdicao(parametroCobrancaController.workspace);
    });

</script>

<style type="text/css">
#dialog-excluir, #dialog-novo, #dialog_cota_unificacao, #dialog_nova_cota_unificacao{display:none;}
.linha_fornecedor{display:none;}
</style>

</head>

    <form id="excluir_parametro_form" name="excluir_parametro_form">
	   <div id="dialog-excluir" title="Excluir Parâmetro de Cobrança">
		  <p>Confirma a exclusão deste Parâmetro de Cobrança?</p>
	   </div>
    </form>

	<form id="parametro_form" name="parametro_form">

		<div id="dialog-novo" title="Incluir Forma de Recebimento">

			<jsp:include page="../messagesDialog.jsp">
				<jsp:param value="idModal" name="messageDialog" />
			</jsp:include>

			<fieldset style="width: 845px !important;">

				<legend>Formas de Pagamento</legend>

				<table width="823" border="0" cellspacing="2" cellpadding="2">
                	<tr>
                    	<td width="133">Tipo de Pagamento:</td>
                        <td>
                        	<select name="dTipoCobranca" id="dTipoCobranca" style="width: 200px;"
                                onchange="parametroCobrancaController.limparCamposValores();parametroCobrancaController.opcaoPagto(this.value);parametroCobrancaController.carregarFormasEmissao(this.value,'');">
                               <option value="">Selecione</option>
                               <c:forEach varStatus="counter" var="tipoCobranca"
                                   items="${listaTiposCobranca}">
                                   <option value="${tipoCobranca.key}">${tipoCobranca.value}</option>
                               </c:forEach>
                            </select>
                        </td>
    
                        <td width="185" style="text-align: right;">Acumula D&iacute;vida:</td>
                        <td width="270">
                        	<select name="acumulaDivida" id="acumulaDivida" style="width: 80px;" onchange="parametroCobrancaController.isAcumulaDivida = this.value">
                                    <option value="S">Sim</option>
                                    <option value="N">N&atilde;o</option>
                            </select>
                        </td>
                    </tr>
    
                    <tr>
                    	<td width="133"><label class="tdComboBanco" for="dBanco">Banco:</label></td>
                        <td>
                        	<select class="tdComboBanco" name="dBanco" id="dBanco" style="width: 200px;" onchange="parametroCobrancaController.obterDadosBancarios(this.value);">
                            	<option value="">Selecione</option>
                                <c:forEach varStatus="counter" var="banco"
                                    items="${listaBancos}">
                                    <option value="${banco.key}">${banco.value}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
    
                    <tr>
                    
                        <td style="width: 228px; ">Vencimentos somente em dia &uacute;til:</td>
                        <td>
                        	<select name="vencimentoDiaUtil" id="vencimentoDiaUtil" style="width: 80px;">
                                <option value="S">Sim</option>
                                <option value="N">N&atilde;o</option>
                        	</select>
                      	</td> 
                    </tr>
                    
                        <tr>
                            <td><label class="tdMulta" for="taxaMulta">Multa %:</label></td>
    
                            <td width="209">
                                <table class="tdMulta" width="100%" border="0" cellspacing="0"
                                    cellpadding="0">
                                    <tr>
                                        <td width="31%"><input type="text"
                                            name="taxaMulta" id="taxaMulta" style="width: 50px; text-align:right;" 
                                            readonly="readonly"/></td>
                                        <td width="40%">&nbsp;ou R$:</td>
                                        <td width="29%"><input type="text"
                                            name="valorMulta" id="valorMulta" style="width: 50px; text-align:right;" 
                                            readonly="readonly"/></td>
                                    </tr>
                                </table>
                            </td>
                            
                            <td style="text-align: right;">
                            	Por Fornecedor:
                            </td>
                            <td>
                            	<select name="unificada" id="unificada" style="width: 80px;" 
                                	onchange="parametroCobrancaController.tratarFornecedoresCobrancaUnificada();">
                                    <option value="S">Sim</option>
                                    <option value="N">N&atilde;o</option>
                            	</select>
                            </td>
                        </tr>
    
                        <tr>
                            <td><label class="tdJuros" for="taxaJuros">Juros %:</label></td>
                            <td><input class="tdJuros" type="text"
                                name="taxaJuros" id="taxaJuros" style="width: 50px; text-align:right;" 
                                	  readonly="readonly"/></td>
                                	  
                            <td>
                            	
                            </td>
                            <td>
                            	
                            </td>
                        </tr>
    
                        <tr>
                            <td><span class="formPgto">Forma de Cobran&ccedil;a:</span></td>
                            <td>
                                <table width="100%" border="0" cellspacing="0" cellpadding="0"
                                    class="formPgto">
                                    <tr>
                                        <td width="9%"><input class="formPgto" type="radio"
                                            name="radioFormaCobrancaBoleto"
                                            id="radioFormaCobrancaBoleto.REGISTRADA" value="COM_REGISTRO" /></td>
                                        <td width="34%"><label class="formPgto"
                                            for="radioFormaCobrancaBoleto.REGISTRADA">Registrado</label></td>
                                        <td width="10%"><input class="formPgto" type="radio"
                                            name="radioFormaCobrancaBoleto"
                                            id="radioFormaCobrancaBoleto.NAO_REGISTRADA"
                                            value="SEM_REGISTRO" /></td>
                                        <td width="47%"><label class="formPgto"
                                            for="radioFormaCobrancaBoleto.NAO_REGISTRADA">N&atilde;o
                                                Registrado</label></td>
                                    </tr>
                                </table>
                            </td>
<!--                             <td width="10">Envio por E-mail:</td> -->
<!--                             <td colspan="2"><select name="envioEmail" id="envioEmail" -->
<!--                                 style="width: 80px;"> -->
<!--                                     <option value="S">Sim</option> -->
<!--                                     <option value="N">N&atilde;o</option> -->
<!--                             </select></td> -->
								<td>&nbsp;</td>
								<td>&nbsp;</td>
                        </tr>
    					
    					<tr>
    						<td>Principal</td>
    						<td align="left">
                            	<input type="checkbox" name="principal" id="principal" onclick="parametroCobrancaController.exibirAcumuloDivida(this.checked)"/>
                            </td>
                            <td style="text-align: right;">Impress&atilde;o:</td>
                            <td>
                                <div id="formasEmissao" />
                            </td>
    					</tr>
    					
                        <tr>
                            <td valign="top">Instru&ccedil;&otilde;es:</td>
                            <td colspan="3"><textarea name="instrucoes" rows="4"
                                    maxlength="100" id="instrucoes" style="width: 605px; height: 46px"
                                    readonly="true"></textarea></td>
                        </tr>
                        
                        <tr>
                            <td valign="top">Fator de vencimento:</td>
                            <td colspan="3">
                            	<select id="comboFatorVencimento">
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
                        </tr>
                        
                        <tr height="40">
                            <td valign="middle">Fornecedor Padrão:</td>
                            <td>
                            	<select id="comboFornecedorPadrao">
                            		<option>Selecione...</option>
                                	<c:forEach step="1" items="${listaFornecedores}" varStatus="status">
                                    	<option value="${listaFornecedores[status.index].key}">${listaFornecedores[status.index].value}</option>
									</c:forEach>
								</select>
							</td>
                        </tr>
    
                        <tr>
                            <td style="vertical-align: text-top;">Fornecedores:</td>
                          <td valign="top" rowspan="2">
                                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                    <c:set var="qauntidadeFornecedores"
                                        value="${fn:length(listaFornecedores)}" />
                                    <c:forEach step="1" items="${listaFornecedores}"
                                        varStatus="status">
                                        <tr>
                                            <td><input type="checkbox"
                                                name="checkGroupFornecedores"
                                                id="ParamCob-fornecedor_<c:out value="${listaFornecedores[status.index].key}" />"
                                                value='<c:out value="${listaFornecedores[status.index].key}" />' /></td>
                                            <td><c:out value="${listaFornecedores[status.index].value}" /></td>
                                            
                                        </tr>
                                    </c:forEach>
                                </table>
                            </td>
                            <td style="vertical-align: text-top;">Concentra&ccedil;&atilde;o de Pagamentos:</td>
                            <td valign="top"><table width="100%" border="0" cellspacing="0"
                                    cellpadding="0">
                                    <tr>
                                        <td width="6%"><input type="radio"
                                            name="concentracaoPagamento" id="radio_diaria" value="diaria"
                                            onclick='parametroCobrancaController.mudaConcentracaoPagamento("diaria");' /></td>
                                        <td width="14%"><label for="diario">Di&aacute;ria</label></td>
                                        <td width="7%"><input type="radio"
                                            name="concentracaoPagamento" id="radio_semanal"
                                            value="semanal"
                                            onclick="parametroCobrancaController.mudaConcentracaoPagamento('semanal');" /></td>
                                        <td width="19%"><label for="semanal">Semanal</label></td>
                                        <td width="7%"><input type="radio"
                                            name="concentracaoPagamento" id="radio_quinzenal"
                                            value="quinzenal"
                                            onclick="parametroCobrancaController.mudaConcentracaoPagamento('quinzenal');" /></td>
                                        <td width="21%"><label for="quinzenal">Quinzenal</label></td>
                                        <td width="7%"><input type="radio"
                                            name="concentracaoPagamento" id="radio_mensal" value="mensal"
                                            onclick="parametroCobrancaController.mudaConcentracaoPagamento('mensal');" /></td>
                                        <td width="19%"><label for="mensal">Mensal</label></td>
                                    </tr>
                          </table></td>
                        </tr>
                        <tr>
                            <td valign="top">&nbsp;</td>
                            <td valign="top">&nbsp;</td>
                            <td valign="top">
                            
                                <table width="100%" border="0" cellspacing="1" cellpadding="1"
                                    class="quinzenal">
                                    <tr>
                                        <td width="72">Todo dia:</td>
                                        <td width="83"><input type="text" name="diasDoMes[]"
                                            id="diaDoMes1" style="width: 60px; text-align:center;" /></td>
                                        <td width="23">e:</td>
                                        <td width="111"><input type="text" name="diasDoMes[]"
                                            id="diaDoMes2" style="width: 60px; text-align:center;" /></td>
                                    </tr>
                                </table>
                                
                                <table width="100%" border="0" cellspacing="1" cellpadding="1"
                                    class="mensal">
                                    <tr>
                                        <td width="64">Todo dia:</td>
                                        <td width="199"><input type="text" name="diasDoMes[]"
                                            id="diaDoMes" style="width: 60px; text-align:center;" /></td>
                                    </tr>
                                </table>
                                
                                <table width="100%" border="0" cellspacing="0" cellpadding="1"
                                    class="semanal">
                                    <tr>
                                        <td width="20"><input type="checkbox" name="dPS" id="dPS" /></td>
                                        <td width="91"><label for="dPS">Segunda-feira</label></td>
                                        <td width="20"><input type="checkbox" name="dPT" id="dPT" /></td>
                                        <td width="131"><label for="dPT">Ter&ccedil;a-feira</label></td>
                                    </tr>
                                    <tr>
                                        <td><input type="checkbox" name="dPQ" id="dPQ" /></td>
                                        <td><label for="dPQ">Quarta-feira</label></td>
                                        <td><input type="checkbox" name="dPQu" id="dPQu" /></td>
                                        <td><label for="dPQu">Quinta-feira</label></td>
                                    </tr>
                                    <tr>
                                        <td><input type="checkbox" name="dPSex" id="dPSex" /></td>
                                        <td><label for="dPSex">Sexta-feira</label></td>
                                        <td><input type="checkbox" name="dPSab" id="dPSab" /></td>
                                        <td><label for="dPSab">S&aacute;bado</label></td>
                                    </tr>
                                    <tr>
                                        <td><input type="checkbox" name="dPDom" id="dPDom" /></td>
                                        <td><label for="dPDom">Domingo</label></td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                    </tr>
                                </table>
                                
                            </td>
                        </tr>
    
                    </table>

			</fieldset>

		</div>

	</form>
	
	<form id="cota_unificacao_form" name="cota_unificacao_form">
	   <div id="dialog_cota_unificacao" title="Cotas Centralizadas">
		  <table class="cotasCentralizadas"></table>
	   </div>
    </form>
	
    <form id="nova_cota_unificacao_form" name="nova_cota_unificacao_form">
	   <div id="dialog_nova_cota_unificacao" title="Unifica&ccedil;&atilde;o de Cotas">
		  <fieldset style="width: 98%;">
		  	<legend>Cota Centralizadora</legend>
		  	<table style="width: 100%;">
		  		<tr>
		  			<td>Cota:</td>
		  			<td>Nome:</td>
		  		</tr>
		  		<tr>
		  			<td style="width: 10%;">
		  				<input type="text" class="numCota" id="parametro-cobranca-numeroCota" style="width: 40px;"
		  					onchange="parametroCobrancaController.buscarCotaPorNumero('')"/>
		  			</td>
		  			<td>
		  				<input type="text" id="nomeCota_" style="width: 495px;" 
		  					onkeyup="parametroCobrancaController.onkeyupCampoNome('');" 
		  					onblur="parametroCobrancaController.onblurCampoNome('');"/>
		  			</td>
		  		</tr>
		  	</table>
		  </fieldset>
		  
		  <fieldset style="width: 98%;">
			<legend>Cotas Centralizadas</legend>
				<div style="overflow: auto; height: 190px;">
					<table id="cotasCentralizadas">
				  		<tr>
				  			<td>Cota</td>
				  			<td>Nome</td>
				  		</tr>
				  	</table>
				</div>
			  </fieldset>
		  </div>
    </form>


		<jsp:include page="../messagesDialog.jsp">
			<jsp:param value="idModalFiltro" name="messageDialog" />
		</jsp:include>
		<div class="areaBts">
			<div class="area">
			<span class="bt_novos" id="bt_novo" title="Novo">
			<a isEdicao="true" href="javascript:;" onclick="parametroCobrancaController.popup();">
				<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_salvar.gif">
			</a>
			</span>
			
			<span class="bt_novos" id="bt_novo" title="Unificar Cotas">
				<a href="javascript:;" onclick="parametroCobrancaController.mostrarUnificacaoCotas();">
					<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_estudo_complementar.gif">
				</a>
			</span>
			
			</div>
		</div>
		<div class="linha_separa_fields">&nbsp;</div>
		<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
			<legend>Pesquisar Parâmetros de Cobran&ccedil;a</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1"
				class="filtro">
				<tr>

					<td width="107">Forma Pagamento:</td>
					<td colspan="3"><select name="filtroTipoCobranca"
						id="filtroTipoCobranca" style="width: 170px;">
							<option></option>
							<c:forEach varStatus="counter" var="filtroTipoCobranca"
								items="${listaTiposCobranca}">
								<option value="${filtroTipoCobranca.key}">${filtroTipoCobranca.value}</option>
							</c:forEach>
					</select></td>

					<td width="43">Banco:</td>
					<td width="177"><select name="filtroBanco" id="filtroBanco"
						style="width: 200px;">
							<option></option>
							<c:forEach varStatus="counter" var="filtroBanco"
								items="${listaBancos}">
								<option value="${filtroBanco.key}">${filtroBanco.value}</option>
							</c:forEach>
					</select></td>

					<td width="413"><span class="bt_novos"> 
						<a href="javascript:;" onclick="parametroCobrancaController.mostrarGridConsulta();">
							<img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" />
						</a>
						</span>
					</td>
				</tr>
			</table>
		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>
		<div class="grids" style="display: none;">
		<fieldset class="fieldGrid">
			<legend>Par&aacute;metros de Cobran&ccedil;as Cadastrados</legend>
				<table class="parametrosGrid"></table>
		</fieldset>
		</div>
