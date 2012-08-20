<head>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>scripts/produto.js'></script>
<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/scriptRecebimentoFisico.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<style type="text/css">
    fieldset label {width: auto; margin-bottom: 0px!important;}
    .nfes{display:none;}
</style>

</head>

<body>

	<div id="dialog-novo-item" style="display: none;" title="Recebimento Físico">

		<jsp:include page="../messagesDialog.jsp" />
	
		<table width="341" border="0" cellspacing="2" cellpadding="2">
			<tr>
				<td>Código:</td>
				<td width="202">
					<input 
					type="text"
					id="codigo"
					maxlength="255"
					style="width: 80px; float: left; margin-right: 5px;"
					onchange="recebimentoFisicoController.jsDadosProduto.pesquisarProdutoPorCodigo();"/>
					
				</td>
			</tr>
			<tr>
				<td>Produto:</td>
				<td width="202">
					<input 
						maxlength="255"
						type="text" 
						id="produto"
						
					       	   onkeyup="produto.autoCompletarPorNomeProduto('#produto', false);"
					       	   onblur="recebimentoFisicoController.jsDadosProduto.pesquisarProdutoPorNome();"/>
				</td>
			</tr>
			<tr>
				<td>Edição:</td>
				<td><input 
					type="text" 
					id="edicao" maxlength="20"
					style="width: 80px;" 
					onchange="recebimentoFisicoController.jsDadosProduto.validarNumeroEdicao();"/>
				</td>
			</tr>
			<tr>
				<td>Data Lançamento:</td>
				<td><input 	type="text" 
							name="datepickerLancto"
							id="datepickerLancto" 
							style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>Data Recolhimento:</td>
				<td><input 	type="text" 
							name="datepickerRecolhimento"
							id="datepickerRecolhimento" 
							style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>Preço R$:</td>
				<td><input 	
							disabled="disabled"
							type="text" 
							id="precoCapa"
							style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>Peso:</td>
				<td><input 	disabled="disabled"
							type="text" 
							id="peso"
							style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>Pacote Padrão:</td>
				<td><input 	disabled="disabled"
							type="text" 
							id="pacotePadrao"
							style="width: 200px;" />
				</td>
			</tr>
			<tr>
				<td>Reparte Previsto:</td>
				<td><input 	type="text" 
							id="repartePrevisto"
							style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>
					Lançamento: 
				</td>				
				<td>
					<select name="tipoLancamento"
						id="tipoLancamento" style="width: 250px;">
							<option value=""></option>
							<c:forEach var="tipoLancamento" items="${listaTipoLancamento}">
								<option value="${tipoLancamento}">${tipoLancamento}</option>
							</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
			    <td>&nbsp;</td>			
			    
			    <td>
			    
			    	<span 	class="bt_incluir_novo" title="Incluir Nova Linha"><a href="javascript:;" 
			    			onclick="recebimentoFisicoController.incluirNovoItemNota();">
			    
			    	<img src="${pageContext.request.contextPath}/images/ico_add_novo.gif" 
			    		alt="Incluir Novo" 
			    		width="16" 
			    		height="16" 
			    		border="0" 
			    		hspace="5" />Incluir Novo</a></span></td>			
			 </tr>			
		</table>

	</div>


	<div class="corpo">

		<br clear="all" /> <br />

		<div class="container">

			<div id="effect" style="padding: 0 .7em;"
				class="ui-state-highlight ui-corner-all">
				<p>
					<span style="float: left; margin-right: .3em;"
						class="ui-icon ui-icon-info"></span> <b>Recebimento Físico <
						evento > com < status >.</b>
				</p>
			</div>

			<form id="pesquisar_recebimento_fisico_form">

				<fieldset class="classFieldset">
				
					<legend> Pesquisar Recebimento Físico</legend>
					
					<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	
						<tr>
	
							<td width="86">Fornecedor:</td>
							
							<td width="254"><select id="fornecedor" name="fornecedor"
								onblur="recebimentoFisicoController.exibirCnpjDoFornecedor()" style="width: 250px;">
									<option value="-1">Todos</option>
									<c:forEach var="fornecedor" items="${listafornecedores}">
										<option value="${fornecedor.juridica.cnpj}">${fornecedor.juridica.razaoSocial}</option>
									</c:forEach>
							</select></td>
							
							<td width="43" align="right">CNPJ:</td>
							<td width="136"><input id="cnpj"
								onblur="recebimentoFisicoController.pesquisarPorCnpjFornecedor();" disabled="disabled" name="cnpj"
								style="width: 130px;" />
							</td>
													
							<td width="76">Nota Fiscal:</td>
							<td width="123"><input type="text" id=notaFiscal
								style="width: 100px;" />
							</td>
							<td width="33">Série:</td>
							<td width="43"><input id="serie" type="text"
								style="width: 30px;" />
							</td>
							<td width="110"><span class="bt_pesquisar"
								title="Pesquisar Recebimento"> <a href="javascript:;"
									onclick="recebimentoFisicoController.verificarNotaFiscalExistente();">Pesquisar</a> </span>
							</td>
	
						</tr>
						<tr>
							<td colspan="4" height="26">
							
								<label for="eNF">É uma NF-e?</label>
								
								<input type="checkbox" name="checkbox8" id="eNF" onchange="recebimentoFisicoController.mostrar_nfes();" style="float: left; margin-right: 10px;" /> 
								
								<span id="nfes" class="nfes"> 
								
								Chave de Acesso: 
								
								<input type="text" name="chaveAcesso" id="chaveAcesso"
									style="width: 120px; margin-left: 10px;" />
							</span>
							
							</td>
							
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td></span></td>
						</tr>
					</table>
					
				</fieldset>
		
			</form>		
			
			<div class="linha_separa_fields">&nbsp;</div>

			<fieldset class="classFieldset">
			
				<legend>Recebimentos Físico Cadastrados</legend>
				
				<div class="grids" style="display: none;">
				
					<div class="gridWrapper">
					
						<table class="itemNotaGrid"></table>
					
					</div>
					
					<div id="botaoNovoProdutoOpaco">
							<span class="bt_incluir_novo" id="bt_novo_produtoOpaco" title="Incluir Nova Linha"> 
								<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"> 
									<img src="${pageContext.request.contextPath}/images/ico_add_novo.gif" border="0" hspace="5" />
									Novo Produto 
								</a> 
							</span>
					</div>

					<div id="botaoAdicionarOpaco">
							<span class="bt_incluir_novo" id="bt_adicionarOpaco" title="Adicionar Nota Fiscal"> 
								<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"> 
									<img src="${pageContext.request.contextPath}/images/ico_add_novo.gif" border="0" hspace="5" />
									Adicionar Nota Fiscal 
								</a> 
							</span>
					</div>

					<div id="botaoNovoProduto">
							<span class="bt_incluir_novo" id="bt_novo_produto" title="Incluir Nova Linha"> 
								<a href="javascript:;" onclick="recebimentoFisicoController.popup_novo_item();"> 
									<img src="${pageContext.request.contextPath}/images/ico_add_novo.gif" border="0" hspace="5" />
									Novo Produto 
								</a> 
							</span>
					</div>	
					
					<div id="botaoAdicionar">
							<span class="bt_incluir_novo" id="bt_adicionar" title="Adicionar Nota Fiscal"> 
								<a href="javascript:;" onclick="recebimentoFisicoController.popup_adicionar();">  
									<img src="${pageContext.request.contextPath}/images/ico_add_novo.gif" border="0" hspace="5" />
									Adicionar Nota Fiscal 
								</a> 
							</span>
					</div>
					
					<div id="botoesNormais">	
														
						<span class="bt_novos" title="Salvar"> 
							<a href="javascript:;" onclick="recebimentoFisicoController.salvarDadosItensDaNotaFiscal()">
								<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" width="19" height="17" alt="Salvar" hspace="5" border="0" />
								Salvar 
							</a> 
						</span>
						
						<span class="bt_novos" title="Cancelar"> 
							<a href="javascript:;" onclick="recebimentoFisicoController.cancelarNotaRecebimentoFisico()">
								<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" width="19" height="17" alt="Salvar" hspace="5" border="0" />
								Cancelar 
							</a> 
						</span>
						
						<span class="bt_confirmar_novo" title="Confirmar Recebimento Físico">
							<a href="javascript:;" onclick="recebimentoFisicoController.confirmarRecebimentoFisico()">
								<img src="${pageContext.request.contextPath}/images/ico_check.gif" width="16" height="16" alt="Confirmar" border="0" hspace="5"/>
								Confirmar
							</a>
						</span>
					</div>	
					
					<div id="botoesOpacos">
						
						<span class="bt_novos" title="Salvar"> 
							<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"> 
								<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" width="19" height="17" alt="Salvar" hspace="5" border="0" />
								Salvar 
							</a> 
						</span>
						
						<span class="bt_novos" title="Cancelar"> 
							<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"> 
								<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" width="19" height="17" alt="Salvar" hspace="5" border="0" />
								Cancelar 
							</a> 
						</span>
						
						<span class="bt_confirmar_novo" title="Confirmar Recebimento Físico">
							<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"> 
								<img src="${pageContext.request.contextPath}/images/ico_check.gif" width="16" height="16" alt="Confirmar" border="0" hspace="5"/>
								Confirmar
							</a>
						</span>						
						 
					</div>
					
					<span class="bt_sellAll" style="float:right; margin-right:40px;">
						<label for="chBoxReplicaValorRepartePrevistoAll">Selecionar Todos</label>
						<input type="checkbox" name="Todos" id="chBoxReplicaValorRepartePrevistoAll" onclick="recebimentoFisicoController.replicarTodosValoresRepartePrevisto(this);" style="float:right;"/>
					</span>
					
				</div>

			</fieldset>

			<div class="linha_separa_fields">&nbsp;</div>

		</div>

	</div>
	
	
	

	<!-- NOVO POPUP DE CADASTRO DE NOTA -->
	
	<div id="dialog-adicionar" title="Recebimento Físico" style="display: none;" >
	
	    <jsp:include page="../messagesDialog.jsp" />
		
		<fieldset style="width:910px!important;">
		  <legend>Dados da Nota</legend>
		  <table width="885" cellpadding="2" cellspacing="2" style="text-align:left;">
		   
		    <tr style="width: 25%">

		        <td width="89">Fornecedor:</td>
		        <td width="168">
			        <select name="select" id="novoFornecedor" name="novoFornecedor" onchange="recebimentoFisicoController.pesquisarCnpjFornecedor();" style="width:160px ">
				        <option selected="selected" value="">Selecione...</option>
						<c:forEach var="fornecedor" items="${listafornecedores}">
						    <option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
						</c:forEach>
			        </select>
		        </td>
		        
		        <td width="95">CNPJ:</td>
		        <td width="132">
		            <input maxlength="200" type="text" style="width:100px " id="novoCnpj" name="novoCnpj" onchange="recebimentoFisicoController.pesquisarFornecedorCnpj();" />
		        </td>
		        
		        <td width="102">Nota Fiscal:</td>
		        <td width="115">
		            <input maxlength="18" type="text" style="width:100px " id="novoNumeroNota" name="novoNumeroNota"  />
		        </td>
		        
		        <td width="34">Série:</td>
		        <td width="97">
		            <input maxlength="200" type="text" style="width:50px " id="novoSerieNota" name="novoSerieNota" />
		        </td>
		      
		    </tr>

		    
		    <tr style="width: 25%">
		      
		      <td>NF-e:</td>
		      <td>
		          <input type="checkbox" id="novoNfe" name="novoNfe" />
		      </td>

		      <td>Chave de Acesso:</td>
		      <td colspan="5">
		          <input maxlength="200" type="text" style="width:470px" id="novoChaveAcesso" name="novoChaveAcesso" />
		      </td>

		    </tr>
		    
		    
		    <tr style="width: 25%">
		      
		      <td>Data Emissão:</td>
		      <td>
		          <input type="text" id="novoDataEmissao" name="novoDataEmissao" style="width:100px " />
		      </td>
		      
		      <td>Data Entrada:</td>
		      <td>
		          <input type="text" id="novoDataEntrada" name="novoDataEntrada" style="width:100px " />
		      </td>
		      
		      <td>Valor Total R$:</td>
		      <td>
		          <input maxlength="17" type="text" style="width:100px; text-align:right; " id="novoValorTotal" name="novoValorTotal"/>
		      </td>
		      
		      <td align="right">
		          <input type="checkbox" id="novoReplicarQtde" name="novoReplicarQtde" />
		      </td>
		      <td>Replicar Qtde:</td>
		      
		    </tr>
		    
		    </table>
		</fieldset>
		    
		
		<fieldset style="width:910px!important; margin-top:10px;">
		    <form name="formularioItensNota" id="formularioItensNota">
			    <legend>Itens da Nota</legend>
			    <table id="tabelaItens" class="novoItemNotaGrid"></table>
			</form>    
		</fieldset> 
		
		<span class="bt_incluir_novo" title="Incluir Novo">
		    <a href="javascript:;" onclick="recebimentoFisicoController.incluiNovoItem();">
		        <img src= "${pageContext.request.contextPath}/images/ico_add.gif" alt="Incluir Novo" width="16" height="16" border="0" hspace="5" />
		        Incluir Novo
		    </a>
		</span>
		
		<span class="bt_sellAll" style="float:right; margin-right:40px; margin-top:8px">
		    <label for="textoSelTodos" id="textoSelTodos">
                Marcar Todos
            </label>
		    <input type="checkbox"  id="selTodos" name="selTodos" onclick="recebimentoFisicoController.selecionarTodos(this.checked);" style="float:right; margin-top:-5px"/>
		</span>
		
		<span style="float:right; margin-right:35px; margin-top:8px">
		    <strong style="margin-right:10px;">
		        Valor Total R$
		    </strong> 
		    <label id="labelValorTotal">0,00</label>
		</span>
	 
	</div>


</body>
