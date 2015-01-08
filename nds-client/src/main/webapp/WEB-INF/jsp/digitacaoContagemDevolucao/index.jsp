<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">

<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/digitacaoContagemDevolucao.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/utils.js"></script>

<script  type="text/javascript">
$(function(){
	digitacaoContagemDevolucaoController.init("${userProfileOperador}");
	bloquearItensEdicao(digitacaoContagemDevolucaoController.workspace);
});
</script>

</head>

<body>


	<form id="formEdicoesFechadas">
		<div id="dialogEdicoesFechadas" title="Edi&ccedil;&otilde;es Fechadas com Saldo"
		style="display: none;">
		<fieldset style="width: 900px;">
			<legend>Edi&ccedil;&otilde;es Fechadas com Saldo</legend>

			<table class="consultaEdicoesFechadasGrid"></table>
			<span class="bt_sellAll" style="float: right;"><input
				type="checkbox" id="dialogEdicoesFechadasSelAll" name="Todos" onclick="digitacaoContagemDevolucaoController.edicoesFechadasCheckAll(this);"
				style="float: right; margin-right: 30px;" /> <label for="dialogEdicoesFechadasSelAll">Selecionar
					Todos</label></span>

		</fieldset>
		</div>
	</form>

	<div class="areaBts">
		<div class="area">
		
			<span id="btnSalvar" class="bt_novos" title="Salvar"> 
				<a isEdicao="true" title="Salvar" rel="tipsy" href="javascript:digitacaoContagemDevolucaoController.salvar();"> 
					<img border="0" hspace="5" alt="Salvar" src="${pageContext.request.contextPath}/images/ico_salvar.gif" />
				</a> 
			</span> 
			
			<span id="btnConfirmar" class="bt_novos" title="Devolução Parcial"> 
				<a isEdicao="true" title="Devolução Parcial" rel="tipsy"  href="javascript:digitacaoContagemDevolucaoController.popupConfirmar();"> 
					<img border="0" hspace="5" alt="Confirmar" src="${pageContext.request.contextPath}//images/bt_expedicao.png" hspace="5" border="0"/>
				</a> 
			</span>
			
			<span id="btnConfirmarDevolucaoFinal" class="bt_novos" title="Devolução Final"> 
				<a isEdicao="true" title="Devolução Final" rel="tipsy"  href="javascript:digitacaoContagemDevolucaoController.popupConfirmarDevolucaoFinal();"> 
					<img border="0" hspace="5" alt="Confirmar" src="${pageContext.request.contextPath}//images/ico_check.gif" hspace="5" border="0"/>
				</a> 
			</span>
			
			<span class="bt_novos" title="Imprimir Conferência Cega">
				<a href="${pageContext.request.contextPath}/devolucao/digitacao/contagem/exportarCoferenciaCega?fileType=PDF"  title="Imprimir Conferência Cega" rel="tipsy">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0"/>
				</a>
			</span>

			<span class="bt_arq" title="Gerar Arquivo">
			<a href="${pageContext.request.contextPath}/devolucao/digitacao/contagem/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
				<img src="${pageContext.request.contextPath}/images/ico_excel.png"
				hspace="5" border="0" />
			</a> 
			</span> 

			<span class="bt_arq" title="Imprimir"> 
			<a href="${pageContext.request.contextPath}/devolucao/digitacao/contagem/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif"
				hspace="5" border="0" /> 
			</a>
			</span>
		</div>
	</div>

	<div class="linha_separa_fields">&nbsp;</div>

	<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
		
		  <legend> Pesquisar Fornecedor</legend>
		  
		  <form id="pesquisaContagemDevolucaoForm"
				name="pesquisaContagemDevolucaoForm" 
				method="post">
		  	
		  	<div id="dialog-confirmar" title="Digitação de Contagem para Devolução" 
		  		 style="display: none; width: auto; height: auto; overflow: visible;">
				<p>Confirma a Digitação de Contagem para Devolução?</p>
			</div>
			
			<div id="dialog-confirmar-devolucao-final" title="Digitação de Contagem para Devolução" 
		  		 style="display: none; width: auto; height: auto; overflow: visible;">
				<p>Confirma Devolução Final?</p>
			</div>
			
			  <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
											 
				 <tr>
				     <td width="73">Período de:</td>
				    <td width="121"><input name="digitacao-contagem-dataDe" type="text" id="digitacao-contagem-dataDe" style="width:80px; float:left; margin-right:5px;"/></td>
				    <td width="22">Até:</td>
				    <td width="131"><input name="digitacao-contagem-dataAte" type="text" id="digitacao-contagem-dataAte" style="width:80px; float:left; margin-right:5px;"/></td>
				    <td >Fornecedor:</td>
				    <td width="230">
				    <select name="idFornecedor" id="idFornecedor" style="width:200px;">
				      <option value=""  selected="selected">Todos</option>
				      <c:forEach items="${listaFornecedores}" var="fornecedor">
				      		<option value="${fornecedor.key}">${fornecedor.value}</option>	
				      </c:forEach>
				    </select></td>
				    
				    <td>CE:</td>
				    <td>
				 		<input type="text" name="semanaConferenciaEncalhe" id="semanaConferenciaEncalhe" style="width:80px; float:left; margin-right:5px;" maxlength="6" onkeydown="return onlyNumeric(event);" />
				 		<span>(semana)</span>
				 	</td>
				    <td width="104">
				    	<span class="bt_pesquisar">
				    		<a id="btnPesquisar" href="javascript:;" onclick="digitacaoContagemDevolucaoController.pesquisar();">Pesquisar</a>
				    	</span>
				    </td>
				  </tr>
			  </table>
		</form>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
	
	<fieldset class="classFieldset">
	
		 <legend>Devolução Fornecedor</legend>
		 
		 <div class="grids" id="grids" style="display:none;">
		 	
		 	 <table class="contagemDevolucaoGrid" id="contagemDevolucaoGrid"></table>

			<table border="0" width="100%" cellspacing="2" cellpadding="2">
				<tr>
					<td width="42%"></td>
					
					<td width="17%">
						<strong>Total Geral R$:</strong>
					</td>
					<td width="16%" id="totalGeral"></td>
					
					<td width="18%">
						<span id="bt_sellAll" class="bt_sellAll">
							<label for="sel" style="margin-right:15px;">Selecionar Todos</label> 
							<input isEdicao="true" type="checkbox" name="Todos" id="sel" onclick="digitacaoContagemDevolucaoController.checkAllReplicarValor(this, 'checkgroup');"
							style="float: left;" /> 
						</span>
					</td>

					</tr>
			</table>

		</div>
	</fieldset>
</body>
