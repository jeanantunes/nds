
<head>

<script type="text/javascript">

$(function() {
	
	$("#resumoExpedicaoGridBox").flexigrid({
		preProcess:executarPreProcessamento,
		dataType : 'json',
		colModel : [ {
			
			display : 'Box',
			name : 'codigoBox',
			width : 60,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nome do Box',
			name : 'descricaoBox',
			width : 430,
			sortable : true,
			align : 'left'
		}, {
			display : 'Qtde Produto',
			name : 'qntProduto',
			width : 90,
			sortable : true,
			align : 'center'
		}, {
			display : 'Reparte',
			name : 'reparte',
			width : 90,
			sortable : true,
			align : 'center'
		},{
			display : 'Diferença',
			name : 'qntDiferenca',
			width : 90,
			sortable : true,
			align : 'center'
		},{
			display : 'Valor Faturado R$',
			name : 'valorFaturado',
			width : 90,
			sortable : true,
			align : 'right'
		}],
		sortname : "codigoBox",
		sortorder : "asc",
		scroll:true,
		width : 960,
		height : 150
	});

});


$(function() {
	
	$("#venda-encalhe-grid").flexigrid({
		
		preProcess:executarPreProcessamento,
		
		dataType : 'json',
		
		colModel : [{
			display : 'Código',
			name : 'codigoProduto',
			width : 60,
			sortable : true,
			align : 'left'
		}, {
			display : 'Produto',
			name : 'descricaoProduto',
			width : 330,
			sortable : true,
			align : 'left'
		}, {
			display : 'Edição',
			name : 'edicaoProduto',
			width : 90,
			sortable : true,
			align : 'center'
		}, {
			display : 'Preço Capa R$',
			name : 'precoCapa',
			width : 90,
			sortable : true,
			align : 'right'
		}, {
			display : 'Reparte',
			name : 'reparte',
			width : 90,
			sortable : true,
			align : 'center'
		},{
			display : 'Diferença',
			name : 'qntDiferenca',
			width : 90,
			sortable : true,
			align : 'center'
		},{
			display : 'Total R$',
			name : 'total',
			width : 90,
			sortable : true,
			align : 'center'
		},{
			display : 'Fornecedor',
			name : 'total',
			width : 90,
			sortable : true,
			align : 'center'
		}],
		
		sortname : "codigoProduto",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 150
	});

});


$(function() {
	$("#resumoExpedicaoGridProduto").flexigrid({
		preProcess:executarPreProcessamento,
		dataType : 'json',
		colModel : [ {
			display : 'Código',
			name : 'codigoProduto',
			width : 60,
			sortable : true,
			align : 'left'
		}, {
			display : 'Produto',
			name : 'descricaoProduto',
			width : 330,
			sortable : true,
			align : 'left'
		}, {
			display : 'Edição',
			name : 'edicaoProduto',
			width : 90,
			sortable : true,
			align : 'center'
		}, {
			display : 'Preço Venda R$',
			name : 'precoCapa',
			width : 90,
			sortable : true,
			align : 'right'
		}, {
			display : 'Reparte',
			name : 'reparte',
			width : 90,
			sortable : true,
			align : 'center'
		},{
			display : 'Diferença',
			name : 'qntDiferenca',
			width : 90,
			sortable : true,
			align : 'center'
		}, {
			display : 'Valor Faturado R$',
			name : 'valorFaturado',
			width : 90,
			sortable : true,
			align : 'right'
		}],
		sortname : "codigoProduto",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 150
	});

});

	/**
	 * Executa o pré processamento detalhes resumo expedicao 
	 */
	function executarPreProcessamentoDetalheResumoExpedicao(resultado) {
		
		if (resultado.mensagens) {
	
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
	
			return resultado.tableModel;
		}
		
		return resultado.tableModel;
	}

	function detalharResumoExpedicao(codigoBox, dataLancamento){ 
		
		$("#venda-encalhe-grid").flexOptions({
			
			url: contextPath + '/expedicao/resumo/pesquisar/detalhe',
			dataType : 'json',
			
			params:[{name:'codigoBox', value: codigoBox},
			        
			        {name:'dataLancamento', value: dataLancamento}]
		
		});
		
		$("#venda-encalhe-grid").flexReload();
		
		$( "#dialog-venda-encalhe" ).dialog({
			
			resizable: false,
			
			width:1000,
			
			modal: true,
			
			buttons: {
				
				"Fechar": function() {
					$( this ).dialog( "close" );
				}
			}
		});		
		
		
		
	}
	

	/**
	 * Executa o pré processamento das informa��es retornadas da requisição de pesquisa.
	 */
	function executarPreProcessamento(resultado) {
		
		if (resultado.mensagens) {
	
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$("#grid").hide();
	
			return resultado.tableModel;
		}
		
		$("#totalReparte").html(resultado.qtdeTotalReparte);
		
		$("#totalValorFaturado").html(resultado.valorTotalFaturado);
		
	
		$("#grid").show();
		
		mudarLegendaFielsSet('idFiledResultResumo','resumo');
		
		$("#dataLancamento").focus();
		
		return resultado.tableModel;
	}

	/**
	 * Renderiza componente de data de lançamento
	 */
	$(function() {
	
		$("#dataLancamento").datepicker({
			showOn : "button",
			buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: 'dd/mm/yy',
			defaultDate: new Date()
		});
		
		$("#dataLancamento").mask("99/99/9999");
		$("#dataLancamento").focus();
	});

	/**
	 * Efetua a pesquisa de resumo de expedições, conforme tipo de pesquisa selecionado.
	 */
	function pesquisar () {
		
		var dataLancamento = $('#dataLancamento').val();
		var tipoPesquisa = $('#tipoPesquisa').val();
		
		var formData = [
		                {name:"dataLancamento",value:dataLancamento},
		                {name:"tipoPesquisa",value:tipoPesquisa}
						];
		
		if (tipoPesquisa === 'PRODUTO'){
			carregarGridProduto(formData);
			$("#gridBox").hide();
		}
		else if (tipoPesquisa === 'BOX'){
			carregarGridbox(formData);
			$("#gridProduto").hide();
		}else{
			var mensagens = new Array('O preenchimento do campo \'Tipo de Consulta\' é obrigatório.') ;
			exibirMensagem('WARNING',mensagens);
		}
		
		$("#dataLancamento").focus();
	}

	/**
	 * Efetua a busca das informações referente a Produto, e monta grid
	 */
	function carregarGridProduto(formData){
	
		$("#resumoExpedicaoGridProduto").flexOptions({
			url: "<c:url value='/expedicao/resumo/pesquisar/produto' />",
			params: formData,newp: 1
		});
		
		$("#resumoExpedicaoGridProduto").flexReload();
		
		$("#gridProduto").show();
	}

	/**
	 * Efetua a busca das informações referente a Box, e monta o grid
	 */
	function carregarGridbox(formData){
	
		$("#resumoExpedicaoGridBox").flexOptions({
			url: "<c:url value='/expedicao/resumo/pesquisar/box' />",
			params: formData,newp: 1
		});
		
		$("#resumoExpedicaoGridBox").flexReload();
		
		$("#gridBox").show();
	}

	/**
	 * Altera o titulo do fieldset conforme tipo de pesquisa selecionado
	 */
	function mudarLegendaFielsSet(id, tipo){
		
		 if(tipo === "pesquisar"){
			 document.getElementById (id).innerHTML = getTituloFieldSetPesquisa();
		 }else{
			 document.getElementById (id).innerHTML = getTituloFieldSet();
		 }	 
	}

	/**
	 * Retorna o tirulo do fieldset conforme tipo de pesquisa selecionado
	 */
	function getTituloFieldSetPesquisa(){
		return ($('#tipoPesquisa').val() === 'BOX')
				? 'Pesquisar Expedição por Box'
						:'Pesquisar Expedição por Produto';
	}
 
	/**
	 * Retorna o tirulo do fieldset conforme tipo de pesquisa selecionado
	 */
	function getTituloFieldSet(){
		return ($('#tipoPesquisa').val() === 'BOX')
				? 'Resumo  Expedição por Box'
						:'Resumo  Expedição por Produto';
	}

  	/**
  	 * Efetua a exporta��o dos dados da pesquisa.
  	 */
	function exportar(fileType) {

		var tipoPesquisa = $("#tipoPesquisa").val();

		if (!tipoPesquisa || !fileType) {

			return;
		}

		window.location = 
			contextPath + "/expedicao/resumo/exportar?tipoConsulta=" + tipoPesquisa + "&fileType=" + fileType;
	}
</script>

</head>

<body>
	
<fieldset class="classFieldset">
  
  <legend id="idFiledResumo"> Pesquisar Resumo  Expedição por Box </legend>
  
  	 <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	  <tr>
	    <td width="116">Data Lançamento:</td>
	    <td width="145">
	    	<input type="text" name="dataLancamento" id="dataLancamento" style="width:80px;"/>
	    </td>
	    <td width="91">Tipo Consulta:</td>
	    <td width="180">
	        <select name="tipoPesquisa" id="tipoPesquisa" style="width: 200px;" onchange="mudarLegendaFielsSet('idFiledResumo','pesquisar')">
				<option selected="selected"></option>
				<c:forEach var="tipoResumo" items="${listaTipoResumo}">
					<option value="${tipoResumo.key}">${tipoResumo.value}</option>
				</c:forEach>
			</select>
	     </td>
	    <td width="375">
	    	<span class="bt_pesquisar">
	    		<a href="javascript:;" onclick="pesquisar();">Pesquisar</a>
	    	</span>
	    </td>
	  </tr>
  	</table>

    
</fieldset>

<div class="linha_separa_fields">&nbsp;</div>


<div id="grid" style="display:none;">

	<fieldset class="classFieldset">
	    
	    <legend id="idFiledResultResumo">Resumo  Expedição por Box</legend>
	
		    <div id="gridProduto" style="display:none;">
		    	<table id="resumoExpedicaoGridProduto" class="resumoExpedicaoGridProduto"></table>
		    </div> 
		    
		    <div id="gridBox" style="display:none;">
		    	<table id="resumoExpedicaoGridBox" class="resumoExpedicaoGridBox"></table>
		    </div>
			
			<table width="950" border="0" cellspacing="1" cellpadding="1">
				  <tr>
				  	<td width="658">
					    <span class="bt_novos" title="Gerar Arquivo">
					    	<a href="javascript:;" onclick="exportar('XLS');">
					    		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					    		Arquivo
					    	</a>
					    </span>
					    <span class="bt_novos" title="Imprimir">
					    	<a href="javascript:;" onclick="exportar('PDF');">
					    		<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
					    		Imprimir
					    	</a>
					    </span>
				    </td>
				    <td width="96"><strong>Total:</strong></td>
				    <td width="134" id="totalReparte"></td>
				    <td width="94"></td>
				    <td width="89" id="totalValorFaturado"></td>
				  </tr>
			</table>
		
	</fieldset>
 
 </div>
 
 
 
<div id="dialog-venda-encalhe" style="display:none;">

	<fieldset class="classFieldset">
	    
		<legend id="idFiledResultResumo">Resumo  Expedição por Box</legend>
	    
		<table id="venda-encalhe-grid"></table>
		
	</fieldset>

</div>
 
 
</body>
