<div id="dialog-encerrarEncalhe" title="Opera&ccedil;&atilde;o de Encalhe" style="display:none;">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="dialog-encerrarEncalhe" name="messageDialog"/>
	</jsp:include> 
	
	<fieldset>
		<legend>Cotas Ausentes</legend>
		<table class="cotasGrid" id="tabelaGridCotas" ></table>
		<span class="bt_novos" title="Gerar Arquivo" >
			<a href="javascript:;" onclick="fechamentoEncalheController.gerarArquivoCotasAusentes('XLS');">
				<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				Arquivo
			</a>
		</span>
		<span class="bt_novos" title="Imprimir">
			<a href="javascript:;" onclick="fechamentoEncalheController.gerarArquivoCotasAusentes('PDF');">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				Imprimir 
			</a>
		</span>
		<span class="bt_sellAll" style="float:right;">
			<input isEdicao="true" type="checkbox" id="checkTodasCotas" name="checkTodasCotas" onchange="fechamentoEncalheController.checarTodasCotasGrid(this.checked, true);" style="float:right;margin-right:25px;"/>
			<label for="checkTodasCotas" id="textoCheckAllCotas" ></label>
		</span>
	</fieldset>
</div>