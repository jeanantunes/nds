<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/contasAPagar.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
</head>
<body>

<fieldset class="classFieldset">
	<legend> Pesquisar Contas a Pagar</legend>
	<form id="contasAPagarForm">
	<input type="hidden" name="filtro.dataDetalhe" id="contasAPagar_dataDetalhe"/>
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="20" align="right"><input  type="radio" name="radio" id="contasAPagarRadioDistribuidor" value="radio" onchange="contasAPagarController.pesqDistribuidor();" /></td>
	            <td width="69">Distribuidor</td>
	            <td width="20"><input  type="radio" name="radio" id="contasAPagarRadioProduto" value="radio" onchange="contasAPagarController.pesqProduto();" /></td>
	            <td width="47">Produto</td>
	            <td width="195">
	            	
	            	<!-- Exibe check group com lista de distribuidores -->  
	            	
	            	<div class="filtroFornecedor" style="display:none;"><a href="#" id="selFornecedor">Clique e Selecione o Fornecedor</a>
	              		<div class="menu_fornecedor" style="display:none;">
	                	 	<span class="bt_sellAll">
	                	 		<input type="checkbox" id="contasAPagarCheckSelecionarTodos" name="Todos1" onclick="contasAPagarController.checkAll(this);" style="float:left;"/><label for="sel">Selecionar Todos</label>
	                	 	</span>
	                    	<br clear="all" />
	                  		  
		                  	<c:forEach items="${fornecedores}" var="fornecedor">
			                <input id="fornecedor_${fornecedor.id}" value="${fornecedor.id}"  name="filtro.idsFornecedores" onclick="contasAPagarController.verificarCheck();" type="checkbox" class="contasApagarCheck"/>
			                <label for="fornecedor_${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</label>
			                <br clear="all" />
			                </c:forEach> 
	                    
	              		</div>
	            	</div>
	            	
	            	<!-- Exibe popup para consulta de produtos -->  
	            	    
		            <div class="filtroProduto" style="display:none;">                       
		            	<a href="javascript:;" onclick="contasAPagarController.popup_pesq_produto();">Clique e Selecione o Produto</a>              
		            </div>
		            
	            </td>
	            <td width="46">Per&iacute;odo:</td>
	            <td width="102"><input type="text" name="filtro.dataDe" id="contasAPagar_Filtro_De" style="width:80px;"/></td>
	            <td width="28">At&eacute;:</td>
	            <td width="107"><input type="text" name="filtro.dataAte" id="contasAPagar_Filtro_Ate" style="width:80px;"/></td>
	            <td width="67">Semana CE:</td>
	            <td width="71"><input type="text" name="filtro.ce" id="contasAPagar_Filtro_Ce" style="width:50px;" maxlength="2"/></td>
	           
	            <td width="147">

	            <span class="bt_pesquisar filtroBusca" style="display:none;"><a href="javascript:;" onclick="contasAPagarController.pesquisar()"></a></span></td>
	       </tr>
		</table>
	</form>
	
</fieldset>

<div class="linha_separa_fields">&nbsp;</div>
  
<fieldset class="classFieldset">
	<legend>Contas a Pagar </legend>
		<div class="gridDistrib" style="display:none;">
          <div class="distrFornecedor">
       	  	<table class="porDistrFornecedorGrid"></table>
            <table width="950" border="0" cellspacing="1" cellpadding="1">
				<tr>
	                <td width="277">
	                	<span class="bt_arquivo"><a href="${pageContext.request.contextPath}/financeiro/contasAPagar/exportPesquisarPorDistribuidor?fileType=XLS">Arquivo</a></span>
						<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/financeiro/contasAPagar/exportPesquisarPorDistribuidor?fileType=PDF">Imprimir</a></span>
					</td>
	                <td width="220" align="right"><strong>Total Bruto R$: <span id="contasAPagar_gridFornecedorTotalBruto"></span></strong></td>
	                <td width="243" align="right"><strong>Total Desconto R$: <span id="contasAPagar_gridFornecedorTotalDesconto"></span></strong></td>
	                <td width="197" align="right"><strong>Saldo a Pagar R$: <span id="contasAPagar_gridFornecedorSaldo"></span></strong></td>
              </tr>
            </table>
          </div>
 
		</div>
        <div class="gridProduto" style="display:none;">
			<div class="porProdutos">
			
				<!-- tabela que renderiza o flexgrid contendo os resultados da busca contas a pagar  -->
	       		<table class="porProdutosGrid"></table>
	            <table width="950" border="0" cellspacing="1" cellpadding="1">
					<tr>
	                	<td width="241">
							<span class="bt_arquivo"><a href="${pageContext.request.contextPath}/financeiro/contasAPagar/exportPesquisarPorProduto?fileType=XLS">Arquivo</a></span>
							<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/financeiro/contasAPagar/exportPesquisarPorProduto?fileType=PDF">Imprimir</a></span>
					
						</td>
		                <td width="226" align="right"><strong>Total Pagto R$: <span id="contasAPagar_gridProdutoTotalPagto"></span></strong></td>
		                <td width="216" align="right"><strong>Total Desconto R$: <span id="contasAPagar_gridProdutoTotalDesconto"></span></strong></td>
		                <td width="254" align="right"><strong>Valor L&iacute;quido a Pagar R$: <span id="contasAPagar_gridProdutoValorLiquido"></span></strong></td>
	              	</tr>
	            </table>
			</div>
         </div>   
           
      </fieldset>
      
     <!-- Popup de busca por produto -->
		            
	<div id="dialog-pesq-produto-contasAPagar" title="Pesquisar Produtos" style="display:none;">
		<fieldset style="width:550px!important;">
			<legend>Pesquisar Produtos</legend>
			<form id="contasAPagarPesquisaProdutoEdicaoForm">
     		<table width="530" border="0" cellspacing="0" cellpadding="0">
       			<tr>
       				<td width="50">C&oacute;digo</td>
         			<td width="80"><input type="text" id="codigo" name="filtro.produto" style="width:60px;" onchange="contasAPagarController.pesquisarProdutoPorCodigo();"/></td>
         			<td width="50">Produto</td>
         			<td width="180"><input type="text" id="produto" style="width:150px;" onkeyup="pesquisaProdutoCAP.autoCompletarPorNomeProduto('#produto', false);" onblur="contasAPagarController.pesquisarProdutoPorNome();"/></td>
					<td width="40">Edi&ccedil;&atilde;o</td>
					<td width="60" align="right"><input type="text" id="edicao" name="filtro.edicao" style="width:60px;"/></td>
					<td width="70">&nbsp;</td>
					<td width="20"><span class="bt_pesquisar filtroBusca"><a href="javascript:;" onclick="contasAPagarController.pesquisarProdutoEdicao()"></a></span></td>
				</tr>
       		</table>
			</form>
   		</fieldset>
		
		<br clear="all"/>

 		<fieldset style="width:550px!important; margin-top:10px;">
    		<legend>Pesquisar Produtos</legend>
    		<table class="contasAPagarListaProdutosGrid"></table>
  		</fieldset>
</div> 
      



<div id="dialog-contasAPagar-tipo" title="Parcial" style="display:none;">
	
	<fieldset>
		<legend>Dados da Parcial</legend>
    	
    	<table width="740" border="0" cellpadding="2" cellspacing="1">
        	<tr>
            	<td width="81"><strong>C&oacute;digo:</strong></td>
              	<td width="94"><span id="contasAPagar_popupTipo_codigo"></span></td>
              	<td width="115"><strong>Produto:</strong></td>
              	<td width="211"><span id="contasAPagar_popupTipo_produto"></span></td>
              	<td width="68"><strong>Edi&ccedil;&atilde;o:</strong></td>
              	<td width="140"><span id="contasAPagar_popupTipo_edicao"></span></td>
            </tr>
            <tr>
              	<td><strong>Fornecedor:</strong></td>
              	<td><span id="contasAPagar_popupTipo_fornecedor"></span></td>
              	<td><strong>Data Lan&ccedil;amento:</strong></td>
              	<td><span id="contasAPagar_popupTipo_dataLcto"></span></td>
              	<td><strong>Data Final:</strong></td>
              	<td><span id="contasAPagar_popupTipo_dataFinal"></span></td>
            </tr>
		</table>
    </fieldset>

	<br />
	<br clear="all" />
	<br />

	<fieldset>
		<legend>Parciais</legend>
	    <table class="contasAPagar_parciaispopGrid"></table>
	</fieldset>

	<br clear="all" />
	<br />

			<span class="bt_arquivo"><a href="${pageContext.request.contextPath}/financeiro/contasAPagar/exportPesquisarParcial?fileType=XLS">Arquivo</a></span>
			<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/financeiro/contasAPagar/exportPesquisarParcial?fileType=PDF">Imprimir</a></span>
</div>

      
      
<div id="dialog-contasAPagar-consignado" title="Consignados" style="display:none;">
	
	<fieldset style="width:895px!important;">
		<legend>Pesquisar Produtos</legend>
       	<table width="530" border="0" cellspacing="0" cellpadding="0">
         		<tr>
           		<td width="96">C&oacute;digo / Produto:</td>
           		<td width="311"><input type="text" name="produtos2" id="produtos2" style="width:290px;" /></td>
           		<td width="40">Edi&ccedil;&atilde;o:</td>
           		<td width="83" align="right"><input type="text" name="textfield9" id="textfield9" style="width:60px;" /></td>
         		</tr>
       	</table>
    </fieldset>
    
	<fieldset style="width:895px!important; margin-top:10px;">
    	<legend><span id="contasAPagar_legend_popupConsignado"></span></legend>
        <table class="contasAPagar-consignadoGrid"></table>
    
        <span class="bt_arquivo"><a href="${pageContext.request.contextPath}/financeiro/contasAPagar/exportPesquisarDetalheConsignado?fileType=XLS">Arquivo</a></span>
		<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/financeiro/contasAPagar/exportPesquisarDetalheConsignado?fileType=PDF">Imprimir</a></span>
    
       	<table id="contasAPagar_table_popupConsignado" width="180" border="0" cellspacing="2" cellpadding="2" style="float:right; margin-top: 7px;"></table>
        <table width="109" border="0" cellspacing="2" cellpadding="2"  style="float:right; margin-top: 7px;">
			<tr><td><strong>Total R$:</strong></td></tr>
		</table>
	</fieldset>

</div>





<div id="contasAPagar_popupEncalhe" title="Encalhe da Cota" style="display:none;">
	<fieldset style="width:810px!important;">
    	<legend><span id="contasAPagar_legend_popupEncalhe"></span></legend>
        
        <table class="contasAPagar_encalheGrid"></table>
        
        <span class="bt_arquivo"><a href="${pageContext.request.contextPath}/financeiro/contasAPagar/exportPesquisarDetalheEncalhe?fileType=XLS">Arquivo</a></span>
		<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/financeiro/contasAPagar/exportPesquisarDetalheEncalhe?fileType=PDF">Imprimir</a></span>
        
        <table id="contasAPagar_table_popupEncalhe" width="180" border="0" cellspacing="2" cellpadding="2" style="float:right; margin-top: 7px;"></table>
        <table width="109" border="0" cellspacing="2" cellpadding="2"  style="float:right; margin-top: 7px;">
			<tr><td><strong>Total R$:</strong></td></tr>
		</table>
    </fieldset>
</div>



<div id="contasAPagar_popupFaltasSobras" title="Venda de Encalhe" style="display:none;">
	<fieldset style="width:800px!important;">
    	<legend><span id="contasAPagar_legend_popupFaltasSobras"></span></legend>
        
        <table class="contasAPagar_faltasSobrasGrid"></table>
        
        <span class="bt_arquivo"><a href="${pageContext.request.contextPath}/financeiro/contasAPagar/exportPesquisarDetalheFaltasSobras?fileType=XLS">Arquivo</a></span>
		<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/financeiro/contasAPagar/exportPesquisarDetalheFaltasSobras?fileType=PDF">Imprimir</a></span>
		
		<table id="contasAPagar_table_popupFaltasSobras" width="180" border="0" cellspacing="2" cellpadding="2" style="float:right; margin-top: 7px;"></table>
        <table width="109" border="0" cellspacing="2" cellpadding="2"  style="float:right; margin-top: 7px;">
			<tr><td><strong>Total R$:</strong></td></tr>
		</table>
    </fieldset>
</div>


<script type="text/javascript">
	
	var pesquisaProdutoCAP = new PesquisaProduto();

	$(function(){
		contasAPagarController.init();
	});
	
</script>
</body>