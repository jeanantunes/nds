<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/data.holder.js"></script>

	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/fechamentoEncalhe.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cotaBloqueada.js"></script>
	
	<script language="javascript" type="text/javascript">
	
		$(function(){
		
			var contextoAplicacao = "${pageContext.request.contextPath}";
			
			fechamentoEncalheController.init();
			
			cotaBloqueadaController.init();
			
			bloquearItensEdicao(fechamentoEncalheController.workspace);
			
			$('#cancelar-sessao', fechamentoEncalheController.workspace).click(function(){
				$.postJSON(contextPath + "/devolucao/fechamentoEncalhe/limparDadosDaSessaoGrid", function() {
					  console.log( "success" );
				});
			});
		});
	
	</script>

	<style type="text/css">
 		#dialog-encerrarEncalhe fieldset{width:600px!important;}
	</style>
	
</head>

<body>
	<input id="permissaoColExemplDevolucao" type="hidden" value="${permissaoColExemplDevolucao}" />
	<input id="permissaoBtnConfirmar" type="hidden" value="${permissaoBtnConfirmar}" />
	<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
	<input id="toggleColunaJuramentado" type="hidden" value="${not aceitaJuramentado}" />
	
	<form id="form-confirm">
	<div id="dialog-confirm" title="Encerrar Opera&ccedil;&atilde;o" style="display:none;">
		<p>Confirma o encerramento da opera&ccedil;&atilde;o do dia <span id="dataConfirma"></span>:</p>
	</div>
	<div id="dialog-confirm-box-nao-salvo" title="Salvar box" style="display:none;">
		<p>
			As informa&ccedil;&otilde;es para esse box n&atilde;o foram salvas. 
			Deseja salvar antes de continuar?
		</p>  
	</div>
	</form>
	
	<form id="form-mensagem-consistencia-dados">
	<div id="dialog-mensagem-consistencia-dados" title="Fechamento Encalhe" style="display:none;">
		<p id="mensagemConsistenciaDados" ></p>
	</div>
	</form>
	
	<form id="form-postergar">
	<div id="dialog-postergar" title="Postergar Encalhe" style="display:none;">
	
		<jsp:include page="../messagesDialog.jsp">
			<jsp:param value="dialogMensagemPostergarCotas" name="messageDialog"/>
		</jsp:include> 

		
		<fieldset style="width:270px!important;">
	    	<legend>Postergar Encalhe</legend>
			<table border="0" cellspacing="2" cellpadding="0">
	          <tr>
	            <td width="60">Nova Data:</td>
	            <td width="120"><input name="dtPostergada" type="text" id="dtPostergada" style="width:80px;" onchange="fechamentoEncalheController.carregarDataPostergacao();" /></td>
	          </tr>
	        </table>
	    </fieldset>
	    
	    
	</div>
	</form>
	
	<form id="form-encerrarEncalhe">
		<jsp:include page="cotasAusentesDialog.jsp" />
	</form>

	<form id="form-confirmar-regerar-cobranca">
		<div id="dialog-confirmar-regerar-cobranca" title="Regerar Cobrança" style="display: none;">
			<fieldset>
				<legend>Regerar cobrança?</legend>
				<p id="msgRegerarCobranca"></p>
			</fieldset>
		</div>
	</form>
	<div class="areaBts">
		<div class="area">
			<div class="divBotoesPrincipais" style="display:none; float:left;">
	            <span class="bt_novos"><a isEdicao="true" href="javascript:;" onclick="fechamentoEncalheController.salvar()" rel="tipsy" title="Salvar"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" /> </a></span>
			</div>
			<span class="bt_novos" style="display:none;" id="bt_cotas_ausentes" >
				<a href="javascript:;" onclick="fechamentoEncalheController.popup_encerrarEncalhe(false);" rel="tipsy" title="Cotas Ausentes">
					<img src="${pageContext.request.contextPath}/images/ico_usuarios1.gif" hspace="5" border="0" />
				</a>
			</span>
			<span id="btAnaliticoEncalhe" class="bt_novos" style="display: none;">
				<a href="javascript:;" onclick="fechamentoEncalheController.analiticoEncalhe();" 
				   rel="tipsy" title="Anal&iacute;tico Encalhe">
					<img src="${pageContext.request.contextPath}/images/bt_lancamento.png" hspace="5" border="0" />
				</a>
			</span>
			<div class="divBotoesPrincipais" style="display:none; float:left;">
				<span class="bt_novos"><a id="btnEncerrarOperacaoEncalhe" isEdicao="true" href="javascript:;" onclick="fechamentoEncalheController.salvarNoEncerrementoOperacao();" rel="tipsy" title="Encerrar Opera&ccedil;&atilde;o Encalhe"><img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" /></a></span>
			</div>
			
			<span class="bt_arq"><a href="javascript:;" onclick="fechamentoEncalheController.imprimirArquivo('XLS');" rel="tipsy" title="Gerar Arquivo"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /></a></span>
			<span class="bt_arq"><a href="javascript:;" onclick="fechamentoEncalheController.imprimirArquivo('PDF');" rel="tipsy" title="Imprimir"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" /> </a></span>
		</div>
	</div>
    <div class="linha_separa_fields">&nbsp;</div>
    <fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
    	<legend> Pesquisar Fornecedor</legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="75">Data Encalhe:</td>
				<td width="114"><input name="fechamentoEncalhe-datepickerDe" type="text" id="fechamentoEncalhe-datepickerDe" style="width:80px;" value="${dataOperacao}" onchange="fechamentoEncalheController.limpaGridPesquisa()" /></td>
				<td width="67">Fornecedor:</td>
				<td width="216">
					<select name="selectFornecedor" id="selectFornecedor" style="width:200px;" onchange="fechamentoEncalheController.limpaGridPesquisa()">
					<option value="">Selecione...</option>
					<c:forEach var="fornecedor" items="${listaFornecedores}">
						<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
					</c:forEach>
					</select>
				</td>
				<td width="97">Box de Encalhe:</td>
				<td width="239">
					<select name="selectBoxEncalhe" id="selectBoxEncalhe" style="width:100px;" onchange="fechamentoEncalheController.modoBox.changeBox(this.value);">
					<option value="">Todos</option>
					<c:forEach var="box" items="${listaBoxes}">
						<option value="${box.id}">${box.nome}</option>
					</c:forEach>
					</select>
				</td>
				<td width="106"><span class="bt_novos"><a href="javascript:;" onclick="fechamentoEncalheController.verificarMensagemConsistenciaDados();"><img id="cancelar-sessao" src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
			</tr>
		</table>
    </fieldset>

    <div class="linha_separa_fields">&nbsp;</div>
      
    <fieldset class="fieldGrid" style="width: 1030px;">
       	<legend> Fechamento Encalhe</legend>
        <div class="grids" style="display:none;" id="divFechamentoGrid">	
			
			<table class="fechamentoGrid"></table>
			
			<span class="bt_sellAll" style="float:right;">
				<label for="sel">Selecionar Todos</label>
				<input isEdicao="true" type="checkbox" id="sel" name="Todos" onclick="fechamentoEncalheController.checkAll(this);" style="float:right;margin-right:65px;"/>
			</span>
			
		</div>
	</fieldset>
	
	<form id="cotas-bloqueadas">
		<jsp:include page="../cota/cotaBloqueada.jsp" />
	</form>
	
</body>
