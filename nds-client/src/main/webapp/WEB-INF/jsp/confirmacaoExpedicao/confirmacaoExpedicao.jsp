<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/confirmaExpedicao.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-dateFormat/jquery.dateFormat-1.0.js"></script>

<title>NDS - Treelog</title>

<script type="text/javascript">
	
	$(function(){
		confirmaExpedicaoController.init();
		bloquearItensEdicao(confirmaExpedicaoController.workspace);
	});
	
</script>

</head>

<body>
		
	<div class="areaBts">
		<div class="area">
			<span class="bt_novos">
				<a isEdicao="true" href="javascript:confirmaExpedicaoController.popupConfirmar();" rel="tipsy"  title="Confirmar">
					<img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />
				</a> 
			</span> 
		</div>
	</div>
    </br>
    </br>
    
	<form action="" method="get" id="form1" name="form1">
		
		<div id="dialog-confirmar" title="Matriz de Expedição" style="display: none">
			<p>Confirmar Matriz de Expedição?</p>
		</div>

		<div class="corpo"></div>

		<div class="container">

			<div id="idMensagem" class="ui-state-highlight ui-corner-all"
				style="display: none;">
				<p>
					<span style="float: left; margin-right: .3em;"
						class="ui-icon ui-icon-info"> </span> <b id="idTextoMensagem"></b>
				</p>
			</div>

			<fieldset class="classFieldset fieldFiltroItensNaoBloqueados">
				<legend>Pesquisar Expedi&ccedil;&atilde;o</legend>
				<table width="950" border="0" cellpadding="2" cellspacing="1"
					class="filtro">
					<tr>
						<td width="111">Data Lan&ccedil;amento:</td>
						<td width="100">
						
<!-- DATA LANCAMENTO --> 
							<input id="idDataLancamento" type="text" name="dataLancamento" style="width: 70px;" value="${dataLancamento}"/>

						</td>
						<td colspan="3">Fornecedor:</td>
						<td width="358">
						
<!-- FORNECEDOR -->			
							<select id="idFornecedor" name="fornecedor" id="select" style="width: 350px;">
								
								<option value="">Todos</option>
								
								<c:forEach items="${fornecedores}" var="fornecedor">				
									<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
								</c:forEach>
								
							</select>
						
						</td>
						
						<td width="75" align="right" valign="bottom"><label
							for="estudo">Estudo:</label>
						</td>
						<td width="94">
<!-- ESTUDO --> 			
							<input name="" id="idEstudo" type="checkbox" value="" />
						
						</td>
						<td width="111">
							<span class="bt_pesquisar">
<!-- PESQUISAR -->								
								<a id="idBotaoPesquisar" href="javascript:;" onclick="confirmaExpedicaoController.cliquePesquisar();">Pesquisar</a>
							
							 </span>
						</td>
					</tr>
				</table>

			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>
			<div class="grids" style="display: none;">
				<fieldset class="classFieldset">
					<legend>Expedi&ccedil;&otilde;es Cadastradas</legend>
					
						<table class="confirmaExpedicaoGrid"></table>
						
						<span class="bt_sellAll" style="float: right;">
						<label for="sel">Selecionar Todos</label>					
	<!-- SELECIONAR TODOS -->	
							<input isEdicao="true" type="checkbox" name="Todos" id="selecionarTodosID"onclick="confirmaExpedicaoController.selecionarTodos(this);" style="float: left;" /> </span>
	
				</fieldset>
			</div>
			<div class="linha_separa_fields">&nbsp;</div>




		</div>
		</div>
	</form>


</body>