<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/relatorioTiposProdutos.js"></script>
	
	<script type="text/javascript">
		$(function(){
			relatorioTiposProdutosController.init();
		});
	</script>
</head>

<body>

	<fieldset class="classFieldset">
   	    <legend> Pesquisar Tipos de Produtos</legend>
   	    
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              	<td width="77">Tipo Produto:</td>
              	<td colspan="3">
              		<select name="filtro.tipoProduto" style="width:120px;">
                		<option selected="selected" value="-1"></option>
						<c:forEach items="${listTipoProduto}" var="tipoProduto">
							<option value="${tipoProduto.id}">${tipoProduto.descricao}</option>
						</c:forEach>
              		</select>
              	</td>
              	<td width="71">Lan&ccedil;amento:</td>
              	<td width="87"><input type="text" name="filtro.dataLancamentoDe" id="dateLanctoDe" style="width:60px;"/></td>
              	<td width="22">At&eacute;:</td>
              	<td width="89"><input type="text" name="filtro.dataLancamentoAte" id="dateLanctoAte" style="width:60px;"/></td>
              	<td width="79">Recolhimento:</td>
              	<td width="94"><input type="text" name="filtro.dataRecolhimentoDe" id="dateRecoltoDe" style="width:70px;"/></td>
              	<td width="25">At&eacute;:</td>
              	<td width="113"><input type="text" name="filtro.dataRecolhimentoAte" id="dateRecoltoAte" style="width:70px;"/></td>
              	<td width="107"><span class="bt_pesquisar"><a href="javascript:;" onclick="relatorioTiposProdutosController.pesquisar();"></a></span></td>
            </tr>
        </table>
    </fieldset>

    <div class="linha_separa_fields">&nbsp;</div>

    <div class="grids" style="display:none;">
	    <fieldset class="classFieldset">
    		<legend>Tipos de Produtos Cadastrados</legend>
        	<table class="tiposProdutosGrid"></table>
            <span class="bt_arquivo"><a href="${pageContext.request.contextPath}/lancamento/relatorioTiposProdutos/exportar?fileType=XLS">Arquivo</a></span>
			<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/lancamento/relatorioTiposProdutos/exportar?fileType=PDF">Imprimir</a></span>
        </fieldset>
    </div>
    
    <div class="linha_separa_fields">&nbsp;</div>
	
	
	 
	
</body>
