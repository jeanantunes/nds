<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<input id="permissaoGridColRepartePrevisto" type="hidden" value="${permissaoGridColRepartePrevisto}">
<input id="permissaoGridColDiferenca" type="hidden" value="${permissaoGridColDiferenca}">

<input id="permissaoColValorTotal" type="hidden" value="${permissaoColValorTotal}">
<input id="permissaoColValorTotalDesconto" type="hidden" value="${permissaoColValorTotalDesconto}">

<input id="indConferenciaCega" type="hidden" value="${indConferenciaCega}">

<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numberformatter-1.2.3.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/scriptRecebimentoFisico.js"></script>


<script language="javascript" type="text/javascript">

    var pesquisaProdutoRecebimentoFisico = new PesquisaProduto(recebimentoFisicoController.workspace);
	$(function(){
		recebimentoFisicoController.init();	
		bloquearItensEdicao(recebimentoFisicoController.workspace);
    });
	
</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<style type="text/css">
    fieldset label {width: auto; margin-bottom: 0px!important;}
    .nfes{display:none; float: left; margin-top: 4px;line-height: 24px;}
    
    .gridLinhaDestacada {
	  background:#BEBEBE; 
	  font-weight:bold; 
	  color:#fff;
	}
	
	.gridLinhaDestacada:hover {
	   color:#000;
	}
	
	.gridLinhaDestacada a {
	   color:#fff;
	}
	
	.gridLinhaDestacada a:hover {
	   color:#000;
	}
</style>

</head>

<body>
	
	<form>
		<div id="dialog-descontos-divergente" title="Descontos Divergentes" style="display: none;">
			<p></p>
		</div>
	</form>
			
	<form action="/recebimentoFisico" id="form_novo_item">

		<div id="dialog-valor-nota-divergente" title="Valor Total Nota Fiscal Divergente" style="display: none;">
			<p>Valor total da [Nota] n&atilde;o confere com o valor total dos [Itens], Deseja prosseguir?</p>
		</div>
		
		<div id="dialog-verificacao-quantidades" title="Verificação de Digitação" style="display: none;">
			<p>Há itens que não possuem quantidade digitada ou estão com quantidade zerada. Deseja prosseguir mesmo assim?</p>
		</div>
		
		<div id="dialog-verificacao-exclusao" title="Verificação de Exclusão" style="display: none;">
			<p>Deseja exlcluir esta nota?</p>
		</div>

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

						onchange="pesquisaProdutoRecebimentoFisico.pesquisarPorCodigoProduto('#codigo', '#produto', '#edicao', true, function(){});"
						/>

					</td>
				</tr>
				<tr>
					<td>Produto:</td>
					<td width="202">
						<input 
							maxlength="255"
							type="text" 
							id="produto"

						       	   onkeyup="pesquisaProdutoRecebimentoFisico.autoCompletarPorNomeProduto('#produto', false);"
						       	   onblur="pesquisaProdutoRecebimentoFisico.pesquisarPorNomeProduto('#codigo', '#produto', '#edicao', true, function(){});"/>
					</td>
				</tr>
				<tr>
					<td>Edição:</td>
					<td><input 
						type="text" 
						id="edicao" maxlength="20"
						style="width: 80px;" 
						onchange="pesquisaProdutoRecebimentoFisico.validarNumEdicao('#codigo', '#edicao', true, recebimentoFisicoController.exibirDetalhesProdutoEdicao);"/>
					</td>
				</tr>
				<tr>
					<td>Data LanÃ§amento:</td>
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
								id="recebimento-fisico-precoCapa"
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
								id="recebimento-fisico-pacotePadrao"
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
				    		/>Incluir Novo</a></span></td>			
				 </tr>			
			</table>

		</div>

	</form>
	<div class="areaBts">
		<div class="area">
			<div id="botaoNovoProdutoOpaco" style="float:left;">
							<span class="bt_novos" id="bt_novo_produtoOpaco"> 
								<a isEdicao="true" href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)" rel="tipsy" title="Incluir Novo Produto"> 
									<img src="${pageContext.request.contextPath}/images/ico_estudo_complementar.gif" border="0"  />
								</a> 
							</span>
					</div>

					<div id="botaoNovoProduto" style="float:left;">
							<span class="bt_novos" id="bt_novo_produto"> 
								<a isEdicao="true" href="javascript:;" onclick="recebimentoFisicoController.popup_novo_item();" rel="tipsy" title="Incluir Novo Produto"> 
									<img src="${pageContext.request.contextPath}/images/ico_estudo_complementar.gif" border="0"  />
								</a> 
							</span>
					</div>	

					<div id="botaoAdicionar" style="float:left;">
							<span class="bt_novos" id="bt_adicionar"> 
								<a isEdicao="true" href="javascript:;" onclick="recebimentoFisicoController.popup_adicionar();" rel="tipsy" title="Adicionar Nota Fiscal">  
									<img src="${pageContext.request.contextPath}/images/ico_expedicao_box.gif" border="0"  />
								</a> 
							</span>
					</div>

					<div id="botoesNormais" style="float:left; width:160px;">	

						<span class="bt_novos"> 
							<a isEdicao="true" href="javascript:;" onclick="recebimentoFisicoController.salvarDadosItensDaNotaFiscal()" rel="tipsy" title="Salvar">
								<img src="${pageContext.request.contextPath}/images/ico_salvar.gif"  border="0" /> 
							</a> 
						</span>

						<span class="bt_novos"> 
							<a isEdicao="true" href="javascript:;" onclick="recebimentoFisicoController.cancelarNotaRecebimentoFisico()" rel="tipsy" title="Cancelar">
								<img src="${pageContext.request.contextPath}/images/ico_bloquear.gif"   border="0" />
							</a> 
						</span>

						<c:if test="${permissaoBotaoConfirmacao eq true}">
							<span class="bt_novos">
								<a isEdicao="true" href="javascript:;" onclick="recebimentoFisicoController.validarDescontosRecebimentoFisico()" rel="tipsy" title="Confirmar Recebimento Físico">
									<img src="${pageContext.request.contextPath}/images/ico_check.gif" border="0" />
								</a>
							</span>
							<span class="bt_novos"> 
							<a isEdicao="true" href="javascript:;" onclick="recebimentoFisicoController.exibirConfirmacaoExclusaoNota()" rel="tipsy" title="Excluir">
								<img src="${pageContext.request.contextPath}/images/ico_excluir.gif"   border="0" />
							</a> 
						</span>
						</c:if>
						
					</div>	

					<div id="botoesOpacos">

						<span class="bt_novos"> 
							<a isEdicao="true" href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)" rel="tipsy" title="Salvar"> 
								<img src="${pageContext.request.contextPath}/images/ico_salvar.gif"  border="0" />
							</a> 
						</span>

						<span class="bt_novos"> 
							<a isEdicao="true" href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)" rel="tipsy" title="Cancelar"> 
								<img src="${pageContext.request.contextPath}/images/ico_bloquear.gif" border="0" /> 
							</a> 
						</span>

						<c:if test="${permissaoBotaoConfirmacao eq true}">
							<span class="bt_novos">
								<a isEdicao="true" href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)" rel="tipsy" title="Confirmar Recebimento Físico"> 
									<img src="${pageContext.request.contextPath}/images/ico_check.gif" border="0"/>
								</a>
							</span>
							<span class="bt_novos"> 
							<a isEdicao="true" href="javascript:;" onclick="recebimentoFisicoController.exibirConfirmacaoExclusaoNota()" rel="tipsy" title="Excluir">
								<img src="${pageContext.request.contextPath}/images/ico_excluir.gif"   border="0" />
							</a> 
						</span>			
						</c:if>		

					</div>
		</div>
	</div>
	<div class="linha_separa_fields">&nbsp;</div>

			<form id="pesquisa_recebimento_fisico_form">

				<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">

					<legend> Pesquisar Recebimento Físico</legend>

					<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">

						<tr>

							<td width="86">Fornecedor:</td>

							<td width="254">
							
								<select id="fornecedor" name="fornecedor" onchange="recebimentoFisicoController.exibirCnpjDoFornecedor()" style="width: 250px;">
									
									<option value="-1" selected="selected">Todos</option>
									
									<c:forEach var="fornecedor" items="${listafornecedores}">
										<option value="${fornecedor.juridica.cnpj}">${fornecedor.juridica.razaoSocial}</option>
									</c:forEach>
									
								</select>
								
							</td>

							<td width="43" align="right">CNPJ:</td>
							<td width="136">
								<input id="cnpj" type="text" name="cnpj"
								style="width: 130px;" disabled="disabled" />
							</td>

							<td width="85">Nota Fiscal / NE:</td>
							<td width="123"><input type="text" id=notaFiscal
								style="width: 100px;" maxlength="9" />
							</td>
							<td width="33">Série:</td>
							<td width="43"><input id="serie" type="text"
								style="width: 30px;" maxlength="4"/>
							</td>
							<td width="110"><span class="bt_novos">
								<a href="javascript:;"
									onclick="recebimentoFisicoController.verificarNotaFiscalExistente();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a> </span>
							</td>

						</tr>
						<tr>
							<td colspan="7" height="26">

								<label for="eNF">É uma NF-e?</label>

								<input type="checkbox" name="checkbox8" id="eNF" onchange="recebimentoFisicoController.mostrar_nfes();" style="float: left;  margin-left: 13px; margin-right: 10px;
    margin-top: 9px;" /> 

								<span id="nfes" class="nfes"> 

								Chave de Acesso: 

								<input type="text" name="chaveAcesso" id="chaveAcesso" maxlength="44" style="width: 310px; margin-left: 10px;" />
								
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
			<div class="break_conteudo" style="position: relative !important; left: -10%;">
				<fieldset class="fieldGrid" style="width: 1200px;">
	
					<legend>Recebimentos F&iacute;sico Cadastrados</legend>
	
					<div class="grids" style="display: none;">
	
						<div class="gridWrapper">
	
							<table class="itemNotaGrid"></table>
	
						</div>
						
						<c:if test="${permissaoGridColRepartePrevisto eq true}">
							<span class="bt_sellAll" style="float:right; margin-right:40px;">
								<label for="chBoxReplicaValorRepartePrevistoAll">Selecionar Todos</label>
								<input isEdicao="true" type="checkbox" name="Todos" id="chBoxReplicaValorRepartePrevistoAll" 
									onclick="recebimentoFisicoController.replicarTodosValoresRepartePrevisto(this);" style="float:right;"/>
							</span>
						</c:if>
						
						<span style="float:right; margin-right:40px;" id="spanTotalComDescontoLbl">
							<label>Total com desconto: </label>
							<label id="totalComDescontoLbl"></label>
						</span>
						
						<span style="float:right; margin-right:40px;" id="spanTotalSemDescontoLbl">
							<label>Total: </label>
							<label id="totalSemDescontoLbl"></label>
						</span>
						
						<span style="float:right; margin-right:40px;">
							<label>Total de Exemplares: </label>
							<label id="totalExemplares"></label>
						</span>
						
						<span style="float:right; margin-right:40px;">
							<label>Total de Produtos: </label>
							<label id="qtdeProdutos"></label>
						</span>
						
					</div>
	
				</fieldset>
			</div>




	<!-- NOVO POPUP DE CADASTRO DE NOTA -->

	<div id="div-wrapper-dialog-adicionar">

		<div id="dialog-adicionar" title="Recebimento Físico" style="display: none;" >

		    <jsp:include page="../messagesDialog.jsp" />

			<fieldset style="width:1150px!important;">
			  <legend>Dados da Nota</legend>
			  <table width="1150" cellpadding="2" cellspacing="2" style="text-align:left;">

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
			        <td width="150">
			            <input maxlength="200" type="text" style="width:132px " id="novoCnpj" disabled="true" name="novoCnpj" onchange="recebimentoFisicoController.pesquisarFornecedorCnpj();" />
			        </td>

			        <td width="102">Nota Fiscal:</td>
			        <td width="115">
			            <input maxlength="18" type="text" style="width:100px " id="novoNumeroNota" name="novoNumeroNota"  />
			        </td>

			        <td width="34">Série:</td>
			        <td width="97">
			            <input maxlength="4" type="text" style="width:50px " id="novoSerieNota" name="novoSerieNota" />
			        </td>

			    </tr>


			    <tr style="width: 25%">

			       <td>Nota Envio:</td>
				  
				  <td colspan="1">
					<input id="novoNumeroNotaEnvio" type="text" style="width: 100px;"/>
				  </td>

			      
			      
				  <td colspan="1">
				  
						<label for="novoNfe">NF-e:</label>
						
						<input  type="checkbox" name="checkbox8" id="novoNfe" 
								onchange="recebimentoFisicoController.mostrar_chave_acesso_nova();" 
								style="float: left;  margin-left: 13px; margin-right: 10px;
    							margin-top: 9px;" /> 
						
				  </td>
				  
					<td colspan="5">
						<span id="nfesNovo" class="nfesNovo">
							Chave de Acesso:
							<input type="text" maxlength="44" style="width: 365px; margin-left: 65px;" id="novoChaveAcesso" name="novoChaveAcesso" />
						</span>
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
			    </tr>

			    </table>
			</fieldset>


			<fieldset style="width:1250px!important; margin-top:10px;">
			    <form name="formularioItensNota" id="formularioItensNota">
				    <legend>Itens da Nota</legend>
				    <table id="tabelaItens" class="novoItemNotaGrid"></table>
				</form>    
			</fieldset> 

			<span class="bt_incluir_novo" title="Incluir Novo">
			    <a href="javascript:;" onclick="recebimentoFisicoController.incluiNovoItem();">
			        <img src= "${pageContext.request.contextPath}/images/ico_add.gif" alt="Incluir Novo" width="16" height="16" border="0" />
			        Incluir Novo
			    </a>
			</span>

			<span class="bt_sellAll" style="float:right; margin-right:40px; margin-top:8px">
			    <label for="textoSelTodos" id="textoSelTodos">
	                Selecionar Todos
	            </label>
			    <input type="checkbox"  id="selTodos" name="selTodos" onclick="recebimentoFisicoController.selecionarTodos(this.checked);" style="float:right; margin-left:10px"/>
			</span>
			
			<span style="float:right; margin-right:35px; margin-top:8px">
			    <strong style="margin-right:10px;">
			        Total com desconto:
			    </strong> 
			    <label id="labelValorTotalDesconto">0,00</label>
			</span>

			<span style="float:right; margin-right:35px; margin-top:8px">
			    <strong style="margin-right:10px;">
			        Total:
			    </strong> 
			    <label id="labelValorTotal">0,00</label>
			</span>

		</div>

	</div>

</body>