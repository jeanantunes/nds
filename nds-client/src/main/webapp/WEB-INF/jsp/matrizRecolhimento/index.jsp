	<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">

	<head>

		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/balanceamento.js"></script>

		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/balanceamentoRecolhimento.js"></script>
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/data.holder.js"></script>

		<script type="text/javascript">

			var pathTela = "${pageContext.request.contextPath}";

			var balanceamento = new Balanceamento(pathTela, "balanceamento");

			$(function() {
				balanceamentoRecolhimentoController.inicializar(balanceamento);
				bloquearItensEdicao(balanceamentoRecolhimentoController.workspace);
			});

		</script>

	</head>

	<body>

		<input id="utilizaSedeAtendida" type="hidden" value="false">
		
		<input id="bloquearBotoes" type="hidden" value="false">

		<div class="areaBts resumoPeriodo" style="display: none;">
		<div class="area">

			<span class="bt_novos" >
				<a isEdicao="true" id="linkConfiguracaoInicial" title="Voltar Configuração Inicial" href="javascript:;">
					<img src="${pageContext.request.contextPath}/images/bt_devolucao.png" border="0" hspace="5" />
				</a>
			</span>



			<span class="bt_novos">
				<a id="linkEditor_disabled" style="background-color: gray;opacity: 0.5;">
					  <img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_add_novo.gif" title="Balancear Editor" >
				<a/>
			</span>

			<span class="bt_novos" >
				<a id="linkValor" href="javascript:;" title="Volume">
					<img style="width: 16px;height: 16px;" border="0" hspace="5" src="${pageContext.request.contextPath}/images/bt_financeiro.png">
				</a>
			</span>
			<span class="bt_novos" >
				<a isEdicao="true" id="linkSalvar" href="javascript:;" title="Salvar">
					<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_salvar.gif">
				</a>
			</span>

			<span class="bt_novos" >
				<a id="linkMatrizFornecedor" title="Matriz Fornecedor" href="javascript:;" onclick="balanceamentoRecolhimentoController.exibirMatrizFornecedor();">
					<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_detalhes.png">
				</a>
			</span>

			<span class="bt_novos" >
				<a isEdicao="true" id="linkBloquearDia" href="javascript:;"  title="Bloquear o Dia">
					<img  height="15" width="15" border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_bloqueado.gif">
				</a>
			</span>
			
			<span class="bt_novos hidden_buttons" style="display:none;">
				<a isEdicao="true" id="linkReprogramar" href="javascript:;" title="Reprogramar" >
					<img src="${pageContext.request.contextPath}/images/ico_reprogramar.gif" hspace="5" border="0" />
				</a>
			</span>
			
			<span class="bt_arq" >
  					<a href="${pageContext.request.contextPath}/devolucao/balanceamentoMatriz/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
    		</span>

			<span class="bt_arq" >
				<a href="${pageContext.request.contextPath}/devolucao/balanceamentoMatriz/exportar?fileType=PDF" 	 title="Imprimir">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0"/>
				</a>
			</span>

           <span class="bt_novos hidden_buttons" style="display:none;">
				<a isEdicao="true" id="linkConfirmar" href="javascript:;" title="Confirmar Balanceamento" >
					<img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />
				</a>
			</span>
			

			<span class="bt_novos" >
				<a isEdicao="true" id="linkReabrirMatriz" href="javascript:;" title="Reabrir Matriz">
					<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_reopen.gif">
				</a>
			</span>

		</div>
		</div>
		<div class="linha_separa_fields">&nbsp;</div>

		<form id="form-confirm" >
		<div id="dialog-confirm" title="Balanceamento da Matriz de Recolhimento">

			<jsp:include page="../messagesDialog.jsp" />

			<p>Ao prosseguir com essa ação você perderá seus dados não salvos ou confirmados. Deseja prosseguir?</p>

		</div>
		</form>

		<form id="form-confirm-config-inicial">
		<div id="dialog-confirm-config-inicial" title="Balanceamento da Matriz de Recolhimento" style="display:none;">

			<jsp:include page="../messagesDialog.jsp" />

			<p>Ao voltar a configuração inicial, você perdará os dados salvos. Deseja prosseguir?</p>

		</div>
		</form>
		
		<form id="form-bloqueio-matriz">
		<div id="dialog-bloqueio-matriz" title="Balanceamento da Matriz de Recolhimento" style="display:none;">

			<p>Deseja bloquear para outros usuários a Matriz de Recolhimento para edição?</p>

		</div>
		</form>

		<form id="form-alertAceite">

			<div id="alertAceite" title="Balanceamento da Matriz de Recolhimento" style="display:none;">

				<jsp:include page="../messagesDialog.jsp" />

				<p id="mensagemValidacaoReprogramacao" />
			</div>

		</form>

		<form id="form-ProdutosNaoBalanceadosAposConfirmacaoMatriz">
			<div id="dialogProdutosDeOutraSemana" title="Balanceamento da Matriz de Recolhimento" style="display:none;">
				<jsp:include page="../messagesDialog.jsp" />
				<p>Os seguintes produtos estão com a semana de recolhimento diferente da semana de recolhimento do fornecedor:</p>
				<br>
				<p><span id="descDialogProdutosDeOutraSemana"></span></p>
			</div>
		</form>



		<form id="form-confirm-balanceamento">
		<div id="dialog-confirm-balanceamento" title="Balanceamento" style="display:none;">

		    <jsp:include page="../messagesDialog.jsp">
				<jsp:param value="dialog-confirmar" name="messageDialog"/>
			</jsp:include>

		    <fieldset style="width:250px!important;">
		    	<legend>Confirmar Balanceamento</legend>

		        <table width="240" border="0" cellspacing="1" cellpadding="1" id="tableConfirmaBalanceamentoRecolhimento">
		        </table>

		    </fieldset>
		</div>
		</form>

		<form id="form-reabrir-matriz">
		<div id="dialog-reabrir-matriz" title="Balanceamento" style="display:none;">

		    <jsp:include page="../messagesDialog.jsp">
				<jsp:param value="dialog-reabertura" name="messageDialog"/>
			</jsp:include>

		    <fieldset style="width:250px!important;">
		    	<legend>Reabrir Matrizes Confirmadas</legend>

		        <table width="240" border="0" cellspacing="1" cellpadding="1" id="tableReaberturaMatrizConfirmada">
		        </table>

		    </fieldset>
		</div>
		</form>
		
		<form id="form-cadeado-matriz">
		<div id="dialog-cadeado-matriz" title="Balanceamento" style="display:none;">

		    <jsp:include page="../messagesDialog.jsp">
				<jsp:param value="dialog-cadeado" name="messageDialog"/>
			</jsp:include>

		    <fieldset style="width:250px!important;">
		    	<legend>Bloquear Matriz</legend>

		        <table width="240" border="0" cellspacing="1" cellpadding="1" id="tableCadeadoMatrizConfirmada">
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

		<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados" style="margin-top: 0px;">

			<legend>Pesquisar Balanceamento da Matriz de Recolhimento </legend>

			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="76">Fornecedor:</td>
					<td colspan="3">
						<a href="javascript:void(0)" id="selFornecedor" onclick="return false;">Clique e Selecione o Fornecedor</a>
						<div class="menu_fornecedor" style="display:none;">
		                	<span class="bt_sellAll">
								<input type="checkbox" checked="checked" id="checkBoxSelecionarTodosFornecedores" name="checkBoxSelecionarTodosFornecedores" onclick="checkAll(this, 'checkGroupFornecedores');" style="float:left;"/>
								<label for="checkBoxSelecionarTodosFornecedores">Selecionar Todos</label>
							</span>
		                    <br clear="all" />
		                    <c:forEach items="${fornecedores}" var="fornecedor">
		                    	<input id="fornecedor_${fornecedor.id}" value="${fornecedor.id}" name="checkGroupFornecedores" onclick="verifyCheck($('#checkBoxSelecionarTodosFornecedores'));" type="checkbox" checked="checked"/>
		                      	<label for="fornecedor_${fornecedor.id}">${fornecedor.juridica.razaoSocial}</label>
		                     	<br clear="all" />
		                	</c:forEach> 
		            	</div>
					</td>
					<td width="53">Semana :</td>
					<td width="107">
						<input type="text" 
							   name="numeroSemana" 
							   id="numeroSemana" value="${numeroSemana}${ano}" style="width: 50px;" maxlength="6"
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
							<a href="javascript:;" onclick="balanceamentoRecolhimentoController.verificarBloqueioMatrizRecolhimento();"></a>
						</span>
					</td>
				</tr>
			</table>
		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

		<!--  Resumo do Período -->

		<fieldset class="classFieldset resumoPeriodo" id="resumoPeriodo" style="display: none;">

			<legend>Resumo do Período</legend>

			<div style="width: 950px; overflow-x: auto;">
				<table id="tableResumoPeriodoBalanceamento" name="tableResumoPeriodoBalanceamento" width="100%" border="0" cellspacing="2" cellpadding="2">
				</table>
			</div>

			<!-- Botões de Ação -->

			<table width="950" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="115">

					</td>
					<td width="117">

					</td>
					<td width="296">
						<!-- Removidos Botões Confirmar/Editor/Valor/Salvar -->
					</td>

					<td width="207">

					</td>

					<td width="215">

					</td>
				</tr>
			</table>
		</fieldset>

		<!-- Balanceamento -->

		<fieldset id="fieldsetGrids" class="classFieldset" style="width: 1073px!important;">

			<legend>Balanceamento da Matriz de Recolhimento </legend>

			<div class="grids" style="display: none;">
				<span class="bt_novos" id="bt_fechar" title="Fechar" style="float: right;">
					<a id="linkFechar" href="javascript:;" onclick="balanceamentoRecolhimentoController.fecharGridBalanceamento();">
						<img src="${pageContext.request.contextPath}/images/ico_excluir.gif"
							 hspace="5" border="0" />Fechar
					</a>
				</span>


				<br clear="all" style="margin-top: 20px;" />

				<input type="hidden" id="dataBalanceamentoHidden" />

				<!-- GRID -->
				<table class="balanceamentoGrid"></table>

				<table width="950" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td width="152"></td>
						<td width="46">&nbsp;</td>
						<td width="443">&nbsp;</td>
						<td width="150">
							<span class="bt_sellAll">
								<label for="sel">Selecionar Todos</label>
								<input isEdicao="true" type="checkbox" name="checkAllReprogramar" id="checkAllReprogramar" onclick="balanceamentoRecolhimentoController.selecionarTodos(this);" style="float: left;" />
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