 <input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	
	<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
			
	<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

	<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/diferencaEstoque.js"></script>

	<script type="text/javascript">
	
		$(function(){
			diferencaEstoqueController.init();
		});
		
		var pesquisaCotaFaltasSobras = new PesquisaCota(diferencaEstoqueController.workspace);
		var pesquisaProdutoConsultaFaltasSobras = new PesquisaProduto(diferencaEstoqueController.workspace);
		
	</script>
	
	<style type="text/css">
	#dialog-detalhe-1 label{width:135px; float:left; font-weight:bold;}
	#dialog-detalhe-1 div{float:left; margin-top:5px;}
	
	#dialog-detalhe-2 .linha_separa_fields{width:600px;}
	</style>

	
</head>

<body>
    <div class="areaBts">
		<div class="area">
			<span class="bt_arq">
				<a href="${pageContext.request.contextPath}/estoque/diferenca/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
			</span>
			
			<span class="bt_arq">
				<a href="${pageContext.request.contextPath}/estoque/diferenca/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />					
				</a>
			</span>
		</div>
	</div>
	
	<br clear="all"/>
	
	<div class="container">
	    
	<br />

	<fieldset class="classFieldset">
		
		<legend>Pesquisar Faltas e Sobras</legend>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
  		<tbody>
  			<tr>
    			<td width="59" title="tooltip teste">Código:</td>
    			<td colspan="3">
    			
    				<input type="text" name="diferenca-estoque-codigo" id="diferenca-estoque-codigo"
						   style="width: 80px; float: left; margin-right: 5px;" maxlength="255"
						   onchange="pesquisaProdutoConsultaFaltasSobras.pesquisarPorCodigoProduto('#diferenca-estoque-codigo', '#diferenca-estoque-produto', '#diferenca-estoque-edicao', false,
								   									   diferencaEstoqueController.pesquisarProdutosSuccessCallBack,
								   									   diferencaEstoqueController.pesquisarProdutosErrorCallBack);" />
    			</td>
    			<td width="48">Produto:</td>
    			<td width="282">
    			<input type="text" name="diferenca-estoque-produto" id="diferenca-estoque-produto" style="width: 200px;" maxlength="255"
					       onkeyup="pesquisaProdutoConsultaFaltasSobras.autoCompletarPorNomeProduto('#diferenca-estoque-produto', false);"
					       onblur="pesquisaProdutoConsultaFaltasSobras.pesquisarPorNomeProduto('#diferenca-estoque-codigo', '#diferenca-estoque-produto', '#diferenca-estoque-edicao', false,
					    	   										  diferencaEstoqueController.pesquisarProdutosSuccessCallBack,
					    	   										  diferencaEstoqueController.pesquisarProdutosErrorCallBack);"/>
			</td>
    			<td width="103">Fornecedor:</td>
   			 	<td width="312" colspan="2">
   			 		<select name="diferenca-estoque-fornecedor" id="diferenca-estoque-fornecedor" style="width: 200px;">
						<option selected="selected" value="">Todos</option>
						<c:forEach var="fornecedor" items="${listaFornecedores}">
							<option value="${fornecedor.key}">${fornecedor.value}</option>
						</c:forEach>
					</select>
    			</td>
			</tr>
  			<tr>
    			<td>Cota:</td>
    			<td colspan="3">
    				<input type="text"  name="diferenca-estoque-numeroCota" id="diferenca-estoque-numeroCota"  style="width:80px; float:left;margin-right:5px;"
    					onchange="pesquisaCotaFaltasSobras.pesquisarPorNumeroCota('#diferenca-estoque-numeroCota', '#diferenca-estoque-descricaoCota');">
      			</td>
    			<td>Nome:</td>
    			<td>
    				<input type="text"  name="diferenca-estoque-descricaoCota" id="diferenca-estoque-descricaoCota" style="width:272px;"
    				 		onkeyup="pesquisaCotaFaltasSobras.autoCompletarPorNome('#diferenca-estoque-descricaoCota');" 
		      		 	    onblur="pesquisaCotaFaltasSobras.pesquisarPorNomeCota('#diferenca-estoque-numeroCota', '#diferenca-estoque-descricaoCota');" >
    			</td>
    			<td>Tipo de Diferença:</td>
    			<td colspan="2">
    					<select name="tipoDiferenca" id="tipoDiferenca" style="width: 120px;">
						<option selected="selected" value="">Todos</option>
						<c:forEach var="tipoDiferenca" items="${listaTiposDiferenca}">
							<option value="${tipoDiferenca.key}">${tipoDiferenca.value}</option>
						</c:forEach>
					</select>
    			</td>
			</tr>
        </tbody>
	</table>
		
	<table width="950" border="0" cellspacing="2" cellpadding="2" class="filtro">
   	      <tbody><tr>
   	        <td width="178">Período de Data Lançamento:</td>
   	        <td width="108">
   	        	<input type="text" name="diferenca-estoque-dataInicial" id="diferenca-estoque-dataInicial" style="width: 80px;" value="${dataAtual}" />
   	       	</td>
   	        <td width="33" align="center">Até</td>
   	        <td width="147">
				<input type="text" name="diferenca-estoque-dataFinal" id="diferenca-estoque-dataFinal" style="width: 80px;" value="${dataAtual}" />
   	        </td>
   	        <td width="134" align="right">&nbsp;</td>
   	        <td width="202">&nbsp;</td>
   	        <td width="104">
   	        	<span class="bt_pesquisar" title="Pesquisar">
   	        		<a href="javascript:;" onclick="diferencaEstoqueController.pesquisar();">Pesquisar</a>
   	        	</span>
   	        	   	        	
   	        </td>
          </tr>
        </tbody>
     </table>	
	
	</fieldset>
	<div class="linha_separa_fields">&nbsp;</div>

	<fieldset class="classFieldset">
		<legend>Faltas e Sobras Cadastradas</legend>
		<div class="grids" style="display: none;">
			<table class="consultaFaltasSobrasGrid"></table>
	
		</div>

	</fieldset>
	<div class="linha_separa_fields">&nbsp;</div>
	
	<form id="formDetalheEncalheCota">
		<jsp:include page="detalhesEncalheCota.jsp"></jsp:include>
	</form>

	<form id="formDetalheConsultaFaltasSobras">
		<div id="dialog-detalhe-1" title="Detalhes Estoque" style="display:none;">
			<fieldset style="width:300px!important;">
				<legend>Detalhes do Estoque</legend>
			    
			    <label>Código:</label>
			    	<div id="detalheCodigo"></div>
			    <br clear="all" />
			
			    <label>Produto:</label>
			    	<div id="detalheNome"></div>
			    <br clear="all" />
			    
			    <label>Edição:</label>
			    	<div id="detalheEdicao"></div>
			    <br clear="all" />
			    
			    <label>Fornecedor:</label>
			    	<div id="detalheFornecedor"></div>
			    <br clear="all" />
			    
			    <label>Tipo de Diferença:</label>
			    	<div id="detalheTipo"></div>
			    <br clear="all" />
			    
			    <label>Qtde. Diferença:</label>
			    	<div id="detalheQtde"></div>
			    <br clear="all" />
			    
			    <label>Destinado ao Estoque:</label>
			  		<div id="detalheEstoque"></div>
			    <br clear="all" />
			        
			</fieldset>		
		</div>
	</form>
	</div>
</body>