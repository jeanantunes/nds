<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produto.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script type="text/javascript">
	
	var pesquisaProdutoCadastroProduto = new PesquisaProduto(produtoController.workspace);
	
	produtoController.inicializar(pesquisaProdutoCadastroProduto);
	
	</script>

	<style>
		label { 
			vertical-align:super; 
		}
		
		#dialog-novo label { 
			width:370px; margin-bottom:10px; float:left; font-weight:bold; line-height:26px; 
		}
		
		.ui-tabs .ui-tabs-panel {
		   padding: 6px!important;
		}
	</style>

</head>

<body>

	<form action="/produto" id="excluir_form">
	<div id="dialog-excluir" title="Excluir Produto">
		<p>Confirma a exclus&atilde;o deste Produto?</p>
	</div>
	</form>


	<form action="/produto" id="incluir_form">
	<div id="dialog-novo" title="Incluir Novo Produto">
     
		<input id="idProduto" type="hidden" />
		<jsp:include page="../messagesDialog.jsp">
			<jsp:param value="dialogMensagemNovo" name="messageDialog"/>
		</jsp:include> 
     	
     	<fieldset style="margin-bottom: 10px;">
     		<legend>Dados Basicos</legend>
			<table width="800" border="0" cellspacing="1" cellpadding="1">
				<tr>
					<td width="135"><strong>C&oacute;digo:</strong></td>
					<td width="260"><input type="text" name="codigoProdutoCadastro" id="codigoProdutoCadastro" style="width:130px;" maxlength="30" /></td>
					<td width="142"><strong>Produto:</strong></td>
					<td width="250"><input type="text" name="nomeProduto" id="nomeProduto" style="width:250px;" maxlength="60" /></td>
				</tr>
				<tr>
					<td><strong>Fornecedor:</strong></td>
					<td>
						<select name="comboFornecedoresCadastro" id="comboFornecedoresCadastro" style="width:200px;" >
						</select>
					</td>	
					<td><strong>Editor:</strong></td>
					<td>
						<select name="comboEditor" id="comboEditor" style="width:210px;" >
						</select>
					</td>
				</tr>
				<tr>
					<td><strong> Slogan do Produto:</strong></td>
					<td colspan="3"><input type="text" name="sloganProduto" id="sloganProduto" maxlength="50" style="width:657px;" /></td>
				</tr>
				<tr>
					<td><strong>Tipo de Produto:</strong></td>
					<td>
						<select name="comboTipoProdutoCadastro" id="comboTipoProdutoCadastro" style="width:210px;" >
						</select>
					</td>
					<td><strong>Forma Comercializa&ccedil;&atilde;o:</strong></td>
					<td>
						<table width="229" border="0" cellspacing="1" cellpadding="1">
							<tr>
								<td width="21"><input type="radio" name="formaComercializacao" id="formaComercializacaoConsignado" value="CONSIGNADO" /></td>
								<td width="86">Consignado</td>
								<td width="21"><input type="radio" name="formaComercializacao" id="formaComercializacaoContaFirme" value="CONTA_FIRME" /></td>
								<td width="88">Conta Firme</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td><strong>PEB:</strong></td>
					<td><input type="text" name="peb" id="peb" style="width:80px;" maxlength="9" /></td>
					<td><strong>Pacote Padr&atilde;o:</strong></td>
					<td><input type="text" name="pacotePadrao" id="pacotePadrao" style="width:80px;" maxlength="9" /></td>
				</tr>
				<tr>
					<td><strong>Tipo de Desconto:</strong></td>
					<td>
						<select name="comboTipoDesconto" id="comboTipoDesconto" style="width:210px;" onchange="produtoController.carregarPercentualDesconto();" >
						</select>
					</td>
					<td><strong>% Desconto:</strong></td>
					<td><input type="text" name="percentualDesconto" id="percentualDesconto" style="width:80px;" readonly="readonly" disabled="disabled" /></td>
				</tr>
			</table>
		</fieldset>
		
		
		<table width="800" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<table width="400" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td>	
								<fieldset style="width:380px!important; margin-bottom:10px; height: 42px;">
									<legend>Outros</legend>
										
									<table>
										<tr>									
											<td width="105">Periodicidade: </td>
											<td width="209">
												<select name="comboPeriodicidade" id="comboPeriodicidade" style="width:150px;" >
													<option value="" selected="selected"></option>
													<option value="SEMANAL" >Semanal</option>
													<option value="QUINZENAL" >Quinzenal</option>
													<option value="MENSAL" >Mensal</option>
													<option value="TRIMESTRAL" >Trimestral</option>
													<option value="SEMESTRAL" >Semestral</option>
													<option value="ANUAL" >Anual</option>
												</select>
											</td>
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>
						
						<tr>
							<td valign="top">
			   
								<fieldset style="width:380px!important; float:left; margin-bottom:10px; margin-right:0px!important;">
									<legend>Editorial</legend>
									<table width="380" border="0" cellspacing="1" cellpadding="1">
										<tr>
											<td width="100">Grupo Editorial:</td>
											<td ><input type="text" name="grupoEditorial" id="grupoEditorial" maxlength="25" value="" style="width:200px;" ></td>
										</tr>
										<tr>
											<td width="120" >SubGrupo Editorial:</td>
											<td><input type="text" name="subGrupoEditorial" id="subGrupoEditorial" maxlength="25" value="" style="width:200px;" ></td>
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>
						
						<tr>
							<td>
								<fieldset style="width:381px!important; float:left; margin-bottom:10px; margin-right:0px;">
									<legend>Tributa&ccedil;&atilde;o Fiscal</legend>
									<table width="229" border="0" cellpadding="1" cellspacing="1">
										<tr>
											<td width="20"><input type="radio" name="radioTributacaoFiscal" id="radioTributado" value="TRIBUTADO" /></td>
											<td width="59">Tributado</td>
											<td width="20"><input type="radio" name="radioTributacaoFiscal" id="radioIsento" value="ISENTO" /></td>
											<td width="37">Isento</td>
											<td width="20"><input type="radio" name="radioTributacaoFiscal" id="radioTributacaoOutros" value="OUTROS" /></td>
											<td width="81"> Outros</td>
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>
					</table>
				</td>
				
				<td style="vertical-align: top;" >
					<fieldset style="width:385px!important; margin:0 auto!important 10px auto!important; height: 205px;">
						<legend>P&uacute;blico-Alvo</legend>
						<table width="380" border="0" cellspacing="1" cellpadding="1">
							<tr>
								<td width="380" valign="top">
									<table width="380" border="0" cellspacing="1" cellpadding="1">
									
									
										<tr>
											<td width="160"><strong>Classe Social:</strong></td>
											<td width="320">
												<select name="segmentacaoClasseSocial" id="segmentacaoClasseSocial" style="width:150px;">
							                        <option value="">Selecione</option>
							                        <c:forEach varStatus="counter" var="itemClasseSocial" items="${listaClasseSocial}">
									                    <option value="${itemClasseSocial.key}">${itemClasseSocial.value}</option>
									                </c:forEach>
							                    </select> 
											</td>
										</tr>
			
										
										<tr>
											<td><strong>Sexo:</strong></td>
											<td>
												<select name="segmentacaoSexo" id="segmentacaoSexo" style="width:150px;">
							                        <option value="">Selecione</option>
							                        <c:forEach varStatus="counter" var="itemSexo" items="${listaSexo}">
									                    <option value="${itemSexo.key}">${itemSexo.value}</option>
									                </c:forEach>
							                    </select> 
											</td>
										</tr>
										
										
										<tr>
											<td><strong>Faixa-Et&aacute;ria:</strong></td>
											<td>
												<select name="segmentacaoFaixaEtaria" id="segmentacaoFaixaEtaria" style="width:150px;">
							                        <option value="">Selecione</option>
							                        <c:forEach varStatus="counter" var="itemFaixaEtaria" items="${listaFaixaEtaria}">
									                    <option value="${itemFaixaEtaria.key}">${itemFaixaEtaria.value}</option>
									                </c:forEach>
							                    </select> 
											</td>
										</tr>
										
										
										<tr>
											<td width="137"><strong>Formato:</strong></td>
											<td width="200">
												<select name="segmentacaoFormato" id="segmentacaoFormato" style="width:150px;">
							                        <option value="">Selecione</option>
							                        <c:forEach varStatus="counter" var="itemFormato" items="${listaFormatoProduto}">
									                    <option value="${itemFormato.key}">${itemFormato.value}</option>
									                </c:forEach>
							                    </select> 
											</td>
										</tr>
										
										
										<tr>
											<td><strong>Tipo de Lan&ccedil;amento:</strong></td>
											<td>
												<select name="segmentacaoTipoLancamento" id="segmentacaoTipoLancamento" style="width:150px;">
							                        <option value="">Selecione</option>
							                        <c:forEach varStatus="counter" var="itemTipoLancamento" items="${listaTipoLancamento}">
									                    <option value="${itemTipoLancamento.key}">${itemTipoLancamento.value}</option>
									                </c:forEach>
							                    </select> 
											</td>
										</tr>
										
										
										<tr>
											<td><strong>Tema Principal:</strong></td>
											<td>
												<select name="segmentacaoTemaPrincipal" id="segmentacaoTemaPrincipal" style="width:150px;">
							                        <option value="">Selecione</option>
							                        <c:forEach varStatus="counter" var="itemTema" items="${listaTemaProduto}">
									                    <option value="${itemTema.key}">${itemTema.value}</option>
									                </c:forEach>
							                    </select> 
											</td>
										</tr>
										
										
										<tr>
											<td><strong>Tema Secundario:</strong></td>
											<td>
												<select name="segmentacaoTemaSecundario" id="segmentacaoTemaSecundario" style="width:150px;">
							                        <option value="">Selecione</option>
							                        <c:forEach varStatus="counter" var="itemTema" items="${listaTemaProduto}">
									                    <option value="${itemTema.key}">${itemTema.value}</option>
									                </c:forEach>
							                    </select> 
											</td>
										</tr>
										
										
									</table>
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
		</table>
		
	</div> 
	</form>

	<form action="/produto" id="pesquisar_form">
	<div class="areaBts">
		<div class="area">
			<span class="bt_novos" title="Novo">
				<a href="javascript:;" onclick="produtoController.novoProduto();" rel="tipsy" title="Novo Produto">
					<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />
				</a>
			</span>
		</div>
	</div>
	<div class="linha_separa_fields">&nbsp;</div>
	<fieldset class="fieldFiltro">
		<legend> Pesquisar Produtos</legend>
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="43">C&oacute;digo:</td>
				<td width="123" >
					
			    	<input type="text" name="codigoProduto" id="codigoProduto"
						   style="width: 80px; float: left; margin-right: 5px;" maxlength="255"
						   onchange="pesquisaProdutoCadastroProduto.pesquisarPorCodigoProduto('#codigoProduto', '#produto', '#edicao', false,
								   									   produtoController.pesquisarProdutosSuccessCallBack,
								   									   produtoController.pesquisarProdutosErrorCallBack);" />
				</td>
				
				<td width="55">Produto:</td>
				<td width="237">
					<input type="text" name="produto" id="produto" style="width: 222px;" maxlength="255"
					       onkeyup="pesquisaProdutoCadastroProduto.autoCompletarPorNomeProduto('#produto', false);"
					       onblur="pesquisaProdutoCadastroProduto.pesquisarPorNomeProduto('#codigoProduto', '#produto', '#edicao', false,
														    	   produtoController.pesquisarProdutosSuccessCallBack,
														    	   produtoController.pesquisarProdutosErrorCallBack);"/>
				</td>
				<td width="99">Fornecedor:</td>
				<td width="251">
					<input type="text" id="fornecedor" name="fornecedor" style="width:200px;"
					       onkeyup="produtoController.autoCompletarPorNomeFornecedor('#fornecedor', false);" />
				
				</td>
				<td width="106">&nbsp;</td>
			</tr>
			<tr>
				<td>Editor:</td>
				<td colspan="3" >
					<input type="text" style="width:410px;" name="edicao" id="edicao" maxlength="20" disabled="disabled"/>
				</td>
				<td>Tipo de Produto:</td>
				<td>
					<select id="comboTipoProduto" style="width:207px;">
						<option value="0"></option>
						<c:forEach items="${listaTipoProduto}" var="tipoProduto" >
							<option value="${tipoProduto.id}">${tipoProduto.descricao}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<span class="bt_novos">
						<a href="javascript:;" onclick="produtoController.pesquisar();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
					</span>
				</td>
			</tr>
		</table>
	</fieldset>
	</form>

	<div class="linha_separa_fields">&nbsp;</div>
	<div class="grids" style="display:none;">
		<fieldset class="fieldGrid">
			<legend>Produtos Cadastrados</legend>
					<table class="produtosGrid"></table>
		</fieldset>
	</div>
</body>