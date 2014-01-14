<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>

<style>
.linkDisabled {
	cursor: default;
	opacity: 0.4;
}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/balanceamento.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/balanceamentoLancamento.js"></script>


<script type="text/javascript">


var pathTela = "${pageContext.request.contextPath}";

var balanceamento = new Balanceamento(pathTela, "balanceamento");

var balanceamentoLancamento = new BalanceamentoLancamento(pathTela, "balanceamentoLancamento", balanceamento, BaseController.workspace);

balanceamentoLancamento.inicializar();

</script>

<style>

.ui-datepicker-today a { display:block !important; }
.dialog-detalhe-produto { display:none; }
.dialog-confirm-balanceamento { display:none; }

.gridLinhaDestacada {
  background:#888; 
  font-weight:bold; 
  color:#fff;
}

.gridLinhaDestacada:hover {
   color:#000;
}

.gridLinhaDestacada a {
   color:inherit;
}

.gridLinhaDestacada a:hover {
   color:inherit;
}

</style>

</head>

<body>

<form id="form-confirm">
<div id="dialog-confirm" title="Balanceamento da Matriz de Lançamento">
			
			<jsp:include page="../messagesDialog.jsp" />
			
			<p>Existem lançamentos não confirmados. Ao prosseguir com essa ação você perderá os dados. Deseja prosseguir?</p>
			   
</div>
</form>

<form id="form-pagincao-confirmada">
<div id="dialog-pagincao-confirmada" title="Balanceamento da Matriz de Lançamento" style="display:none">
			
			<p>As seleções de lançamentos não serão salvas, deseja continuar?</p>
			   
</div>
</form>

<form id="formVoltarConfiguracaoInicial">
<div id="dialogVoltarConfiguracaoInicial" title="Balanceamento da Matriz de Lançamento" style="display:none">
			
			<p>Ao voltar a configuração inicial, você perdará os dados salvos. Deseja prosseguir?</p>
			   
</div>
</form>

	
		<form id="formReprogramarBalanceamento">
		<div id="dialogReprogramarBalanceamento" title="Reprogramar Lançamentos">
		    
		    <jsp:include page="../messagesDialog.jsp" />

		    <p>
			    <strong>Nova Data:</strong>
			    <input name="novaDataLancamento" type="text"
			    	   style="width:80px;" id="novaDataLancamento" />
		    </p>
		</div>
		</form>

<form id="form-alerta-lancamentos-produtos-cancelados">
<div id="dialog-alerta-lancamentos-produtos-cancelados" title="Produtos com lançamentos cancelados" style="display:none">
	<p>Os seguintes produtos tiveram seus lançamentos cancelados</p>
	<div id="flexiGridLancamentosProdutosCancelados" />			   
</div>
</form>
		
		   	<jsp:include page="../messagesDialog.jsp">
				<jsp:param value="dialog-novo" name="messageDialog"/>
			</jsp:include>
		     
			  <div class="areaBts">
			  		<div class="area">
			  			<span class="bt_novos" style="display: none;">
		        			<!-- Voltar Configuração Inicial -->
		        			<a id="linkVoltarConfiguracaoInicial" href="javascript:;" onclick="balanceamentoLancamento.abrirAlertaVoltarConfiguracaoInicial();" rel="tipsy" title="Clique para Voltar Configuração Inicial"><img src="<c:url value='images/bt_devolucao.png'/>" title="Voltar Configuração Inicial" border="0" hspace="5" /></a>
		        		</span>
			  			
			  			<span class="bt_novos" style="display: none;">
			  				<!-- Reprogramar -->
			  				<a id="linkReprogramar" href="javascript:;" onclick="balanceamentoLancamento.reprogramarSelecionados();" rel="tipsy" title="Clique para Reprogramar"><img src="<c:url value='images/ico_reprogramar.gif'/>"  hspace="5" border="0" /></a>                    
		                </span>
		                
		                <span class="bt_novos" style="border-width: 2px; border-color: #00CD00; display: none;">
		                    <!-- CONFIRMAR -->	
		                    <a id="linkConfirmar" href="javascript:;" onclick="balanceamentoLancamento.obterConfirmacaoBalanceamento();" rel="tipsy" title="Confirmar Balanceamento">
		                        <img src="<c:url value='images/ico_check.gif'/>"  hspace="5" border="0" />
		                    </a>
		                </span>
		                
		                <span class="bt_novos" style="display: none;">
							<a isEdicao="true" id="linkSalvar" href="javascript:;" onclick="balanceamentoLancamento.salvar();" title="Salvar">
								<img src="<c:url value='images/ico_salvar.gif'/>"  hspace="5" border="0" />
							</a>
						</span>
						
						<span class="bt_novos" style="display: none;">
							<a id="linkMatrizFornecedor" title="Matriz Fornecedor" href="javascript:;" onclick="balanceamentoLancamento.carregarGrid(null, true);">
								<img src="<c:url value='images/ico_detalhes.png'/>"  hspace="5" border="0" />
							</a>
						</span>
			  			
			  			 <span class="bt_arq">
							<!-- ARQUIVO -->
							<a id="linkArquivo" href="${pageContext.request.contextPath}/matrizLancamento/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
							    <img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
						    </a>
						</span>
		            
					
						<span class="bt_arq">
							<!-- IMPRIMIR -->	
							<a id="linkImprimir" href="${pageContext.request.contextPath}/matrizLancamento/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
							    <img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
						    </a>
						</span>
						
						<span class="bt_novos" style="display: none;">
				            <a id="linkReabrirMatriz" href="javascript:;" onclick="balanceamentoLancamento.obterDatasConfirmadasParaReabertura();" title="Reabrir Matriz" >
					             <img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_reopen.gif">
				           </a> 
			            </span>
			  		</div>
			  </div>
			  <div class="linha_separa_fields">&nbsp;</div>
		      <fieldset class="fieldFiltro">
		   	    <legend>Pesquisar Balanceamento da Matriz de Lançamento
		        </legend>
		   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		   	      <tr>
		   	        <td width="68">Fornecedor:&nbsp;</td>
		   	        <td width="228">
		            <a href="#" id="selFornecedor" onclick="return false;">Clique e Selecione o Fornecedor</a>
		              <div class="menu_fornecedor" style="display:none;">
		                	<span class="bt_sellAll">

							<input type="checkbox" id="selTodos1" checked="checked" name="selTodos1" onclick="checkAll(this, 'checkgroup_menu');" style="float:left;"/>

							<label for="selTodos1">Selecionar Todos</label></span>
		                    <br clear="all" />
		                    <c:forEach items="${fornecedores}" var="fornecedor">
		                      <input id="fornecedor_${fornecedor.id}" value="${fornecedor.id}"  name="checkgroup_menu" onclick="verifyCheck($('#selTodos1'));" type="checkbox" checked="checked"/>
		                      <label for="fornecedor_${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</label>
		                      <br clear="all" />
		                   </c:forEach> 
		              </div>
		            
		            </td>
		   	        <td colspan="3">Data de Lançamento Matriz/Distribuidor:</td>
		   	        <td width="109"><input class="campoDePesquisa" type="text" name="datepickerDe" id="datepickerDe" style="width:80px;" value="${data}" /></td>
		   	        <td width="47" align="center">&nbsp;</td>
		   	        <td width="112">&nbsp;</td>
		   	        <td width="104"><span class="bt_novos" title="Pesquisar">   
						<!-- Pesquisar -->
						<a id="linkPesquisarMatrizLancamento" href="javascript:;" onclick="balanceamentoLancamento.verificarBalanceamentosAlterados(balanceamentoLancamento.pesquisar);"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span>
					</td>
		          </tr>
		        </table>
		      </fieldset>
		      <div class="linha_separa_fields">&nbsp;</div>
		      <fieldset class="fieldGrid">
		       	  <legend>Balanceamento da Matriz de Lançamento Cadastrados</legend>
		        <div class="grids" style="display:none;">
		       	   <table id="lancamentosProgramadosGrid" class="lancamentosProgramadosGrid"></table>
		         	  
		         	  <div style="margin-top:15px; margin-left:30px; float:left;"><strong>Valor Total R$: <span id="valorTotal"></span></strong></div>
						<table width="175" border="0" align="right" cellpadding="0" cellspacing="0">
						    <tr>
						        <td width="110" align="right"><label for="selTodos">Selecionar Todos</label></td>
						        <td width="65" align="left" valign="top"><span class="bt_sellAll"><input type="checkbox" id="selTodos" name="Todos" onclick="balanceamentoLancamento.checkUncheckLancamentos()"/></span></td>
						    </tr>
						</table>
		        </div>
		      </fieldset>
		      <div class="linha_separa_fields">&nbsp;</div>      
		      <fieldset class="classFieldset" id="resumoPeriodo"; style="display:none;" >
		      	<legend>Resumo do Período</legend>
		        <div style="width: 950px; overflow-x: auto;">
		        <table width="100%" border="0" cellspacing="2" cellpadding="2" id="tableResumoPeriodo">
		        </table>
		        </div>
		      </fieldset>
		
		<form id="form-detalhe-produto">
        <div id="dialog-detalhe-produto" title="Detalhes do Produto" style="display:none;">
		    <jsp:include page="../produtoEdicao/detalheProduto.jsp" />
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
	

</body>
