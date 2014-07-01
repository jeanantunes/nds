<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/relatorioTiposProdutos.js"></script>
	
	<script type="text/javascript">
		$(function(){
			relatorioTiposProdutosController.init();
		});
	</script>
</head>

<body>
	
	<div class="areaBts">
		<div class="area">
			 
			 <div class="grids" style="display: none">
			 
				 <span class="bt_arq">
	          		<a href="${pageContext.request.contextPath}/lancamento/relatorioTiposProdutos/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
	          			<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_excel.png">
	           		</a>
	           	</span>
	
				<span class="bt_arq">
					<a href="${pageContext.request.contextPath}/lancamento/relatorioTiposProdutos/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
						<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_impressora.gif">
					</a>
				</span>
			</div>
		</div>
	</div>
	
	 <div class="linha_separa_fields">&nbsp;</div>
	
	<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
   	    <legend> Pesquisar Tipos de Produtos</legend>
   	    <form id="relatorioTiposProdutosForm">
	        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	            <tr>
	              	<td>Tipo Produto:</td>
	              	<td>
	              		<select id="tipoProduto" name="filtro.tipoProduto" style="width:120px;">
	                		<option selected="selected" value="-1"></option>
							<c:forEach items="${listTipoProduto}" var="tipoClassificacao">
								<option value="${tipoClassificacao.id}">${tipoClassificacao.descricao}</option>
							</c:forEach>
	              		</select>
	              	</td>
	              	<td>Lan&ccedil;amento:</td>
	              	<td><input type="text" name="filtro.dataLancamentoDe" id="dateLanctoDe" style="width:70px;"/></td>
	              	<td>At&eacute;:</td>
	              	<td><input type="text" name="filtro.dataLancamentoAte" id="dateLanctoAte" style="width:70px;"/></td>
	              	<td>Recolhimento:</td>
	              	<td><input type="text" name="filtro.dataRecolhimentoDe" id="dateRecoltoDe" style="width:70px;"/></td>
	              	<td>At&eacute;:</td>
	              	<td><input type="text" name="filtro.dataRecolhimentoAte" id="dateRecoltoAte" style="width:70px;"/></td>
	              	<td><span class="bt_pesquisar"><a href="javascript:;" onclick="relatorioTiposProdutosController.pesquisar();"></a></span></td>
	            </tr>
	        </table>
   	    </form>
    </fieldset>

    <div class="linha_separa_fields">&nbsp;</div>

    <div class="grids" style="display:none;">
	    <fieldset class="classFieldset">
    		<legend>Tipos de Produtos Cadastrados</legend>
        	<table class="tiposProdutosGrid"></table>
        </fieldset>
    </div>
    
    <div class="linha_separa_fields">&nbsp;</div>

</body>
