<head>
<script  type="text/javascript">

$(function() {
	
$("#contagemDevolucaoGrid").flexigrid({
		
		dataType : 'json',
		preProcess:executarPreProcessamento,
		colModel : [ {
			display : 'Código',
			name : 'codigoProduto',
			width : 80,
			sortable : true,
			align : 'left'
		}, {
			display : 'Produto',
			name : 'nomeProduto',
			width : 150,
			sortable : true,
			align : 'left'
		}, {
			display : 'Edição',
			name : 'numeroEdicao',
			width : 90,
			sortable : true,
			align : 'center'
		}, {
			display : 'Preço Capa R$',
			name : 'precoVenda',
			width : 90,
			sortable : true,
			align : 'right'
		}, {
			display : 'Exemplar Devolução',
			name : 'qtdDevolucao',
			width : 140,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total R$',
			name : 'valorTotal',
			width : 90,
			sortable : true,
			align : 'right'
		}, {
			display : 'Exemplar Nota',
			name : 'qtdNota',
			width : 120,
			sortable : true,
			align : 'center'
		}, {
			display : 'Diferença',
			name : 'qntDiferenca',
			width : 80,
			sortable : true,
			align : 'center'
		},{
			display : 'Replicar Qtde',
			name : 'replicarQtde',
			width : 80,
			sortable : true,
			align : 'center'
		}],
		sortname : "Nome",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 180
	});
});
	
$(function() {
	$('input[id^="data"]').datepicker({
		showOn: "button",
		buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
		buttonImageOnly: true,
		dateFormat: "dd/mm/yy"
	});
	
	$('input[id^="data"]').mask("99/99/9999");
	$("#edicao").numeric();
});


function pesquisar(){
		
	var formData = $('#pesquisaContagemDevolucaoForm').serializeArray();
	
	$("#contagemDevolucaoGrid").flexOptions({
		url: "<c:url value='/devolucao/digitacao/contagem/pesquisar' />",
		params: formData
	});
	
	$("#contagemDevolucaoGrid").flexReload();
	
	$("#grids").show();
}

function executarPreProcessamento(resultado) {
	
	if (resultado.mensagens) {

		exibirMensagem(
			resultado.mensagens.tipoMensagem, 
			resultado.mensagens.listaMensagens
		);
		
		$("#grids").hide();

		return resultado.tableModel;
	}
	
	//$("#totalReparte").html(resultado.qtdeTotalReparte);
	
	//$("#totalValorFaturado").html(resultado.valorTotalFaturado);

	$("#grids").show();
		
	return resultado.tableModel;
}


</script>

</head>

<body>
	
	<fieldset class="classFieldset">
		
		  <legend> Pesquisar Fornecedor</legend>
		  
		  <form id="pesquisaContagemDevolucaoForm"
				name="pesquisaContagemDevolucaoForm" 
				method="post">
		  
			  <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				 <tr>
				     <td width="73">Período de:</td>
				    <td width="121"><input name="dataDe" type="text" id="dataDe" style="width:80px; float:left; margin-right:5px;"/></td>
				    <td width="22">Até:</td>
				    <td width="131"><input name="dataAte" type="text" id="dataAte" style="width:80px; float:left; margin-right:5px;"/></td>
				    <td colspan="77">Fornecedor:</td>
				    <td width="287">
				    <select name="idFornecedor" id="idFornecedor" style="width:250px;">
				      <option selected="selected">Todos</option>
				      <c:forEach items="${listaFornecedores}" var="fornecedor">
				      		<option value="${fornecedor.key}">${fornecedor.value}</option>	
				      </c:forEach>
				    </select></td>
				    <td width="203"><span class="bt_pesquisar"><a href="javascript:;" onclick="pesquisar();">Pesquisar</a></span></td>
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
					    	<a href="javascript:;">
					    		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo
					    	</a>
					    </span>
					
						<span class="bt_novos" title="Imprimir">
							<a href="javascript:;">
								<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir
							</a>
						</span>
					
						<span class="bt_novos" title="Salvar">
							<a href="javascript:;">
								<img border="0" hspace="5" alt="Salvar" src="${pageContext.request.contextPath}/images/ico_salvar.gif" />Salvar
							</a>
						</span>
					
						<span class="bt_confirmar_novo" title="Confirmar">
							<a onclick="popup_confirm();" href="javascript:;">
								<img border="0" hspace="5" alt="Confirmar" src="${pageContext.request.contextPath}/images/ico_check.gif">Confirmar
							</a>
						</span>
					</td>
				    <td width="17%"><strong>Total Geral R$:</strong></td>
				    <td width="14%">999.999,00</td>
				    <td width="18%">
				    	<span class="bt_sellAll">
				    		<label for="sel">Selecionar Todos</label>
				    		<input type="checkbox" name="Todos" id="sel" onclick="checkAll();" style="float:left;"/>
				    	</span>
				    </td>
				  </tr>
			</table>		 
		 
		 </div>
	</fieldset>
</body>
		
          
