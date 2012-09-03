	<head>
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/balanceamento.js"></script>
	
		<script type="text/javascript">

			var pathTela = "${pageContext.request.contextPath}";

			var balanceamento = new Balanceamento(pathTela, "balanceamento");
			
			$(function() {
				balanceamentoRecolhimentoController.inicializar(balanceamento);
			});
		</script>
	</head>
	
	<body>
		
		<form id="form-confirm">
		<div id="dialog-confirm" title="Balanceamento da Matriz de Recolhimento">
			
			<jsp:include page="../messagesDialog.jsp" />
			
			<p>Ao prosseguir com essa ação você perderá seus dados não salvos ou confirmados. Deseja prosseguir?</p>
			   
		</div>
		</form>
		
		<form id="form-confirm-balanceamento">
		<div id="dialog-confirm-balanceamento" title="Balanceamento" style="display:none;">
		    
		    <jsp:include page="../messagesDialog.jsp">
				<jsp:param value="dialog-confirmar" name="messageDialog"/>
			</jsp:include>
			
		    <fieldset style="width:250px!important;">
		    	<legend>Confirmar Balanceamento</legend>

		        <table width="240" border="0" cellspacing="1" cellpadding="1" id="tableConfirmaBalanceamento">
		        </table>

		    </fieldset>
		</div>
		</form>
		
		<form id="formReprogramarBalanceamento">
		<div id="dialogReprogramarBalanceamento" title="Reprogramar Recolhimentos">
		    
		    <jsp:include page="../messagesDialog.jsp" />
		    
		    <p>
			    <strong>Nova Data:</strong>
			    <input name="novaDataRecolhimento" type="text"
			    	   style="width:80px;" id="novaDataRecolhimento" />
		    </p>
		</div>
		</form>
		
		<!-- Filtro de Pesquisa -->
		
		<fieldset class="classFieldset">
		
			<legend>Pesquisar Balanceamento da Matriz de Recolhimento </legend>
			
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="76">Fornecedor:</td>
					<td colspan="3">
						<a href="#" id="selFornecedor" onclick="return false;">Clique e Selecione o Fornecedor</a>
						<div class="menu_fornecedor" style="display:none;">
		                	<span class="bt_sellAll">
								<input type="checkbox" id="checkBoxSelecionarTodosFornecedores" name="checkBoxSelecionarTodosFornecedores" onclick="balanceamentoRecolhimentoController.checkAll(this, 'checkGroupFornecedores');" style="float:left;"/>
								<label for="checkBoxSelecionarTodosFornecedores">Selecionar Todos</label>
							</span>
		                    <br clear="all" />
		                    <c:forEach items="${fornecedores}" var="fornecedor">
		                    	<input id="fornecedor_${fornecedor.id}" value="${fornecedor.id}" name="checkGroupFornecedores" onclick="balanceamentoRecolhimentoController.verifyCheck($('#checkBoxSelecionarTodosFornecedores'));" type="checkbox"/>
		                      	<label for="fornecedor_${fornecedor.id}">${fornecedor.juridica.razaoSocial}</label>
		                     	<br clear="all" />
		                	</c:forEach> 
		            	</div>
					</td>
					<td width="53">Semana:</td>
					<td width="107">
						<input type="text" 
							   name="numeroSemana" 
							   id="numeroSemana" value="${numeroSemana}" style="width: 50px;"
							   onchange="balanceamentoRecolhimentoController.carregarDataSemana();" />
					</td>
					<td width="33">Data:</td>
					<td width="145">
						<input type="text" 
							   name="dataPesquisa" 
							   id="dataPesquisa" 
							   style="width: 80px; float: left; margin-right: 5px;" maxlength="10"
							   value="${dataAtual}"
							   onchange="balanceamentoRecolhimentoController.carregarDiaSemana();" />
					</td>
					<td width="164">
						<span class="bt_pesquisar" title="Pesquisar">
							<a href="javascript:;" onclick="balanceamentoRecolhimentoController.verificarBalanceamentosAlterados(pesquisar);">Pesquisar</a>
						</span>
					</td>
				</tr>
			</table>
		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>
		
		<!--  Resumo do Período -->
		
		<fieldset class="classFieldset" id="resumoPeriodo" style="display: none;">
		
			<legend>Resumo do Período</legend>
			
			<div style="width: 950px; overflow-x: auto;">
				<table id="tableResumoPeriodoBalanceamento" name="tableResumoPeriodoBalanceamento" width="100%" border="0" cellspacing="2" cellpadding="2">
				</table>
			</div>
			
			<!-- Botões de Ação -->
			
			<table width="950" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="115">
						<span class="bt_confirmar_novo" title="Confirmar balanceamento">
							<a id="linkConfirmar" href="javascript:;">
								<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Confirmar
							</a>
						</span>
					</td>
					<td width="117">
						<strong>Balanceamento por:</strong>
					</td>
					<td width="296">
						<span class="bt_confirmar_novo" title="Balancear Editor">
							<a id="linkEditor" href="javascript:;">
								<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Editor
							</a>
						</span>
						<span class="bt_confirmar_novo" title="Balancear Volume / Valor">
							<a id="linkValor" href="javascript:;">
								<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Valor
							</a>
						</span>
						<span class="bt_novos" title="Salvar">
							<a id="linkSalvar" href="javascript:;">
								<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_salvar.gif">Salvar
							</a>
						</span>
					</td>
					
					<td width="207">
						<span class="bt_novos" title="Matriz Fornecedor" style="float: right;">
							<a id="linkMatrizFornecedor" href="javascript:;" onclick="balanceamentoRecolhimentoController.exibirMatrizFornecedor();">
								<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_detalhes.png">Matriz Fornecedor
							</a>
						</span>
					</td>
					
					<td width="215">
						<span class="bt_configura_inicial" title="Voltar Configuração Inicial">
							<a id="linkConfiguracaoInicial" href="javascript:;">
								<img src="${pageContext.request.contextPath}/images/bt_devolucao.png" border="0" hspace="5" />
								Voltar Configuração Inicial
							</a>
						</span>
					</td>
				</tr>
			</table>
		</fieldset>
		
		<!-- Balanceamento -->
		
		<fieldset id="fieldsetGrids" class="classFieldset">
		
			<legend>Balanceamento da Matriz de Recolhimento </legend>
			
			<div class="grids" style="display: none;">
	
				<span class="bt_novos" id="bt_fechar" title="Fechar" style="float: right;">
					<a id="linkFechar" href="javascript:;" onclick="balanceamentoRecolhimentoController.fecharGridBalanceamento();">
						<img src="${pageContext.request.contextPath}/images/ico_excluir.gif"
							 hspace="5" border="0" />Fechar
					</a>
				</span>
	
				<br clear="all" />
				
				<input type="hidden" id="dataBalanceamentoHidden" />
				
				<!-- GRID -->
				<table class="balanceamentoGrid"></table>
				
				<table width="950" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td width="152">
							<span class="bt_novos" title="Reprogramar">
								<a id="linkReprogramar" href="javascript:;">
									<img src="${pageContext.request.contextPath}/images/ico_reprogramar.gif" hspace="5" border="0" />Reprogramar
								</a>
							</span>
						</td>
						<td width="46">&nbsp;</td>
						<td width="443">&nbsp;</td>
						<td width="150">
							<span class="bt_sellAll">
								<label for="sel">Selecionar Todos</label>
								<input type="checkbox" name="checkAllReprogramar" id="checkAllReprogramar" onclick="balanceamentoRecolhimentoController.selecionarTodos(this);" style="float: left;" />
							</span>
						</td>
					</tr>
				</table>
			</div>
		</fieldset>
		
		<form id="form-detalhe-produto">
		<div id="dialog-detalhe-produto" title="Detalhes do Produto" style="display:none;">
    		<jsp:include page="../produtoEdicao/detalheProduto.jsp" />
		</div>
		</form>
		
		<div class="linha_separa_fields">&nbsp;</div>
		
	</body>