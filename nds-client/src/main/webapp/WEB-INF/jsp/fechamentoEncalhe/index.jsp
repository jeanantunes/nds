<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/fechamentoEncalhe.js"></script>
	<script language="javascript" type="text/javascript">
	$(function(){
		fechamentoEncalheController.init();
	});
	</script>

	<style type="text/css">
 		#dialog-encerrarEncalhe fieldset{width:600px!important;}
	</style>
	
</head>

<body>

	<form id="form-confirm">
	<div id="dialog-confirm" title="Encerrar Opera&ccedil;&atilde;o" style="display:none;">
		<p>Confirma o encerramento da opera&ccedil;&atilde;o do dia <span id="dataConfirma"></span>:</p>
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
		
		<fieldset style="width:200px!important;">
	    	<legend>Postergar Encalhe</legend>
			<table border="0" cellspacing="2" cellpadding="0">
	          <tr>
	            <td width="70">Nova Data:</td>
	            <td width="103"><input name="dtPostergada" type="text" id="dtPostergada" style="width:80px;" onchange="fechamentoEncalheController.carregarDataPostergacao();" /></td>
	          </tr>
	        </table>
	    </fieldset>
	</div>
	</form>
	
	<form id="form-encerrarEncalhe">
	<div id="dialog-encerrarEncalhe" title="Opera&ccedil;&atilde;o de Encalhe" style="display:none;">
		
		<jsp:include page="../messagesDialog.jsp">
			<jsp:param value="dialogMensagemEncerrarEncalhe" name="messageDialog"/>
		</jsp:include> 
		
		<fieldset>
			<legend>Cotas Ausentes</legend>
			<form id="formGridCotas" name="formGridCotas" >
				<table class="cotasGrid" id="tabelaGridCotas" ></table>
			</form>
			<span class="bt_novos" title="Gerar Arquivo" >
				<a href="javascript:gerarArquivoCotasAusentes('XLS');">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					Arquivo
				</a>
			</span>
			<span class="bt_novos" title="Imprimir">
				<a href="javascript:gerarArquivoCotasAusentes('PDF');">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
					Imprimir 
				</a>
			</span>
			<span class="bt_sellAll" style="float:right;">
				<input type="checkbox" id="checkTodasCotas" name="checkTodasCotas" onchange="fechamentoEncalheController.checarTodasCotasGrid(this.checked);" style="float:right;margin-right:25px;"/>
				<label for="checkTodasCotas" id="textoCheckAllCotas" ></label>
			</span>
		</fieldset>
	</div>
	</form>

    
    <fieldset class="classFieldset">
    	<legend> Pesquisar Fornecedor</legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="75">Data Encalhe:</td>
				<td width="114"><input name="datepickerDe" type="text" id="datepickerDe" style="width:80px;" value="${dataOperacao}" onchange="fechamentoEncalheController.limpaGridPesquisa()" /></td>
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
					<select name="selectBoxEncalhe" id="selectBoxEncalhe" style="width:100px;" onchange="fechamentoEncalheController.limpaGridPesquisa()">
					<option value="">Selecione...</option>
					<c:forEach var="box" items="${listaBoxes}">
						<option value="${box.id}">${box.nome}</option>
					</c:forEach>
					</select>
				</td>
				<td width="106"><span class="bt_pesquisar"><a href="javascript:;" onclick="fechamentoEncalheController.verificarMensagemConsistenciaDados();">Pesquisar</a></span></td>
			</tr>
		</table>
    </fieldset>

    <div class="linha_separa_fields">&nbsp;</div>
      
    <fieldset class="classFieldset">
       	<legend> Fechamento Encalhe</legend>
        <div class="grids" style="display:none;" id="divFechamentoGrid">
			
			<table class="fechamentoGrid"></table>
			
			<div id="divBotoesPrincipais" style="display:none;">
	            <span class="bt_novos" title="Salvar"><a href="javascript:;" onclick="fechamentoEncalheController.salvar()"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Salvar </a></span>
				<span class="bt_novos" title="Cotas Ausentes"><a href="javascript:;" onclick="fechamentoEncalheController.popup_encerrarEncalhe();"><img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />Cotas Ausentes</a></span>
				<span class="bt_novos" title="Encerrar Opera&ccedil;&atilde;o Encalhe"><a href="javascript:;" onclick="fechamentoEncalheController.salvarNoEncerrementoOperacao();"><img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />Encerrar Opera&ccedil;&atilde;o Encalhe</a></span>
				<span class="bt_sellAll" style="float:right;"><a href="javascript:;" id="sel" onclick="fechamentoEncalheController.replicarTodos();"><img src="${pageContext.request.contextPath}/images/ico_atualizar.gif" border="0" /></a><label for="sel">Replicar Todos</label></span>
			</div>
			
        	<br clear="all" />
        	
			<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;" onclick="fechamentoEncalheController.imprimirArquivo('XLS');"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
			<span class="bt_novos" title="Imprimir"><a href="javascript:;" onclick="fechamentoEncalheController.imprimirArquivo('PDF');"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir </a></span>
        </div>
	</fieldset>
    
    <div class="linha_separa_fields">&nbsp;</div>

</body>
