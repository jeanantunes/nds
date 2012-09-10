<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/digitacaoContagemDevolucao.js"></script>

<script  type="text/javascript">
$(function(){
	ContagemDevolucao.init("${userProfileOperador}");
});
</script>

</head>

<body>
	
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
				    <td colspan="77">Fornecedor:</td>
				    <td width="287">
				    <select name="idFornecedor" id="idFornecedor" style="width:250px;">
				      <option value="-1"  selected="selected">Todos</option>
				      <c:forEach items="${listaFornecedores}" var="fornecedor">
				      		<option value="${fornecedor.key}">${fornecedor.value}</option>	
				      </c:forEach>
				    </select></td>
				    <td width="203">
				    	<span class="bt_pesquisar">
				    		<a id="btnPesquisar" href="javascript:;" onclick="ContagemDevolucao.pesquisar();">Pesquisar</a>
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
								hspace="5" border="0" /> Arquivo </a> 
							</span> 
								
							
							<span class="bt_novos" title="Imprimir"> 
							
							<a href="${pageContext.request.contextPath}/devolucao/digitacao/contagem/exportar?fileType=PDF">
								<img src="${pageContext.request.contextPath}/images/ico_impressora.gif"
								hspace="5" border="0" /> 
								Imprimir 
							</a>
							 
							</span> 
							
							<span id="btnSalvar" class="bt_novos" title="Salvar"> 
						
							<a href="javascript:ContagemDevolucao.salvar();"> 
							<img border="0" hspace="5" alt="Salvar"
								src="${pageContext.request.contextPath}/images/ico_salvar.gif" />
								Salvar
							</a> 
							</span> 
							
							<span id="btnConfirmar" class="bt_confirmar_novo" title="Confirmar"> 
							<a href="javascript:ContagemDevolucao.popupConfirmar();"> 
								<img border="0" hspace="5" alt="Confirmar"
								src="${pageContext.request.contextPath}/images/ico_check.gif">
							Confirmar
							</a> 
							</span>
							
					</td>
					
					<td width="17%">
						<strong>Total Geral R$:</strong>
					</td>
					
					<td width="14%" id="totalGeral"></td>
					
					<td width="18%">
						<span id="bt_sellAll" class="bt_sellAll">
							<label for="sel">Selecionar Todos</label> 
							<input type="checkbox" name="Todos" id="sel" onclick="ContagemDevolucao.checkAllReplicarValor(this, 'checkgroup');"
							style="float: left;" /> 
						</span>
					</td>
				</tr>
			</table>

		</div>
	</fieldset>
</body>
