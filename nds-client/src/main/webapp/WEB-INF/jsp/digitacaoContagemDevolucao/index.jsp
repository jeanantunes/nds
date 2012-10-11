<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/digitacaoContagemDevolucao.js"></script>

<script  type="text/javascript">
$(function(){
	digitacaoContagemDevolucaoController.init("${userProfileOperador}");
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
	</div></form>

	<fieldset class="classFieldset">
		
		  <legend> Pesquisar Fornecedor</legend>
		  
		  <form id="pesquisaContagemDevolucaoForm"
				name="pesquisaContagemDevolucaoForm" 
				method="post">
		  	
		  	<div id="dialog-confirmar" title="Digitação de Contagem para Devolução" 
		  		 style="display: none; width: auto; height: auto; overflow: visible;">
				<p>Confirma a Digitação de Contagem para Devolução?</p>
			</div>
		  	
			  <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
											 
				 <tr>
				     <td width="73">Período de:</td>
				    <td width="121"><input name="dataDe" type="text" id="dataDe" style="width:80px; float:left; margin-right:5px;"/></td>
				    <td width="22">Até:</td>
				    <td width="131"><input name="dataAte" type="text" id="dataAte" style="width:80px; float:left; margin-right:5px;"/></td>
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
				 		<input type="text" name="semanaConferenciaEncalhe" id="semanaConferenciaEncalhe" style="width:80px; float:left; margin-right:5px;"/>
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

			<table width="100%" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="51%">
						
							<span class="bt_novos" title="Gerar Arquivo">
							<a href="${pageContext.request.contextPath}/devolucao/digitacao/contagem/exportar?fileType=XLS">
								<img src="${pageContext.request.contextPath}/images/ico_excel.png"
								hspace="5" border="0" /> Arquivo 
							</a> 
							</span> 
							
							<span id="btnSalvar" class="bt_novos" title="Salvar"> 
							<a href="javascript:digitacaoContagemDevolucaoController.salvar();"> 
							<img border="0" hspace="5" alt="Salvar"
								src="${pageContext.request.contextPath}/images/ico_salvar.gif" />
								Salvar
							</a> 
							</span> 
							
							<span class="bt_novos" title="Incluir Produtos" onclick="digitacaoContagemDevolucaoController.incluirProdutoDialog();">
							<a href="javascript:;">
							<img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0">
									Incluir Edições
							</a>
							</span>
							
							<span class="bt_novos" title="Gerar NF-e de Dev. ao Fornecedor" onclick="digitacaoContagemDevolucaoController.geraNota();">
							<a href="javascript:;">
								<img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0">
								Gerar NF-e
							</a>
							</span>
							
							<span id="btnConfirmar" class="bt_novos" title="Devolução Parcial"> 
							<a href="javascript:digitacaoContagemDevolucaoController.popupConfirmar();"> 
								<img border="0" hspace="5" alt="Confirmar"
								src="${pageContext.request.contextPath}//images/bt_expedicao.png" hspace="5" border="0">
							Devolução Parcial
							</a> 
							</span>
							
							<span class="bt_novos" title="Replicar Quantidades">
							<a href="javascript:digitacaoContagemDevolucaoController.replicarValores();">
							<img src="${pageContext.request.contextPath}/images/ico_integrar.png" hspace="5" border="0">
							Replicar
							</a>
							</span>
							
					</td>
					
					<td width="17%">
						<strong>Total Geral R$:</strong>
					</td>
					<td width="14%" id="totalGeral"></td>

					</tr>
						<td>
							<span class="bt_novos" title="Imprimir CE Devolução">
							<a href="javascript:;">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0">
							CE Devolução
							</a>
							</span>
							
							<span class="bt_novos" title="Imprimir Conferência Cega">
							<a href="${pageContext.request.contextPath}/devolucao/digitacao/contagem/exportarCoferenciaCega?fileType=PDF">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0">
							Conferência Cega
							</a>
							</span>
								
							<span class="bt_novos" title="Imprimir"> 
							<a href="${pageContext.request.contextPath}/devolucao/digitacao/contagem/exportar?fileType=PDF">
								<img src="${pageContext.request.contextPath}/images/ico_impressora.gif"
								hspace="5" border="0" /> 
								Imprimir 
							</a>
							</span>
						</td>
					<td width="18%">
						<span id="bt_sellAll" class="bt_sellAll">
							<label for="sel" style="margin-right:15px;">Selecionar Todos</label> 
							<input type="checkbox" name="Todos" id="sel" onclick="digitacaoContagemDevolucaoController.checkAllReplicarValor(this, 'checkgroup');"
							style="float: left;" /> 
						</span>
					</td>
				</tr>
			</table>

		</div>
	</fieldset>
</body>
