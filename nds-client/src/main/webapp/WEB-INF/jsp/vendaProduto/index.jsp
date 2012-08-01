<head>

<script language="javascript" type="text/javascript">

	$(function() {		
		$("#produto").autocomplete({source: ""});				
	});

	function popup_detalhes(numEdicao) {
		var edicao = numEdicao;
		var codigo = $('#codigo').val();
		$(".detalhesVendaGrid").flexOptions({
			url: "<c:url value='/lancamento/vendaProduto/pesquisarLancamentoEdicao'/>",
			dataType : 'json',
			params: [
			          {name:'filtro.edicao', value:edicao},
				      {name:'filtro.codigo', value:codigo}
			         ]
		    
		});
		
		$(".detalhesVendaGrid").flexReload();
		
		$( "#dialog-detalhes" ).dialog({
			resizable: false,
			height:400,
			width:800,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
			}
		});
	};
	
	
	function buscarNomeProduto(){
		if ($("#codigo").val().length > 0){
			var data = "codigoProduto=" + $("#codigo").val();
			$.postJSON("<c:url value='/lancamento/furoProduto/buscarNomeProduto'/>", data,
				function(result){
					if (result){
						$("#produto").val(result);	
						$("#edicoes").focus();
					} else {
						$("#produto").val("");
						$("#edicoes").focus();
					}
				}
			);
		}
	}
	
	function pesquisarPorNomeProduto(){
		var produto = $("#produto").val();
		
		if (produto && produto.length > 0){
			$.postJSON("<c:url value='/lancamento/furoProduto/pesquisarPorNomeProduto'/>", "nomeProduto=" + produto, exibirAutoComplete);
		}
	}
	
	function exibirAutoComplete(result){
		$("#produto").autocomplete({
			source: result,
			select: function(event, ui){
				completarPesquisa(ui.item.chave);
			}
		});
	}
	
	function completarPesquisa(chave){
		$("#codigo").val(chave.codigoProduto);
		$("#edicoes").focus();
	}
	
	function cliquePesquisar(){	
		
		$(".parciaisGrid").flexOptions({
			url: "<c:url value='/lancamento/vendaProduto/pesquisarVendaProduto'/>",
			dataType : 'json',
			params: getDados()
		});
		
		$(".parciaisGrid").flexReload();
				
	}
	
	function getDados() {
		
		var data = [];
		
		data.push({name:'filtro.codigo',		value: get("codigo")});
		data.push({name:'filtro.nomeProduto',		value: get("produto")});
		data.push({name:'filtro.edicao',		value: get("edicoes")});
		data.push({name:'filtro.idFornecedor',		value: get("idFornecedor")});		
		data.push({name:'filtro.nomeFornecedor',	value: $('#idFornecedor option:selected').text()});
		
		return data;
	}
	
	function get(campo) {
		
		var elemento = $("#" + campo);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	}
	
	function executarPreProcessamento(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids").hide();

			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var linkDetalhe = '<a href="javascript:;" onclick="popup_detalhes('+row.cell.numEdicao+');" style="cursor:pointer">' +
							   	 '<img title="Lançamentos da Edição" src="${pageContext.request.contextPath}/images/ico_detalhes.png" hspace="5" border="0px" />' +
							   '</a>';
			
			row.cell.acao = linkDetalhe;
		});
		
		
		
		
		$(".grids").show();
		
		return resultado;
	}
	
	function executarPreProcessamentoFilha(resultado) {
		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			$(".grids").hide();
			return resultado;
		}
		$(".grids").show();
		return resultado;
	}
	
	
</script>
<style type="text/css">
#dialog-detalhes fieldset{width:750px!important;}
</style>
</head>

<body>
	<div id="dialog-detalhes" title="Detalhes do Produto">
     <fieldset>
     	<legend>Produto: 4455  - Veja - Edição 001 - Tipo de Lançamento: Parcial</legend>
        
        <table class="detalhesVendaGrid"></table>
         <span class="bt_novos" title="Gerar Arquivo">
         	<a href="${pageContext.request.contextPath}/lancamento/vendaProduto/exportar?fileType=XLS&tipoExportacao=popup">
         		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
         		Arquivo
         	</a>
         </span>

		<span class="bt_novos" title="Imprimir">
			<a href="${pageContext.request.contextPath}/lancamento/vendaProduto/exportar?fileType=PDF&tipoExportacao=popup">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				Imprimir
			</a>
		</span>
     </fieldset>
   
    </div>

<div class="corpo">  
    <br clear="all"/>
    <br />
   
    <div class="container">    
	     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
			<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
			<b>Parcial < evento > com < status >.</b></p>
		</div>		
		<fieldset class="classFieldset">
		  <legend> Pesquisar Produto</legend>
		  <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		      <tr>
		        <td width="46">Código:</td>
		        <td colspan="3"><input type="text" name="textfield5" id="codigo" style="width:80px;" onblur="buscarNomeProduto();"/></td>
		        <td width="51">Produto:</td>
		        <td width="164"><input type="text" name="publica" id="produto" onkeyup="pesquisarPorNomeProduto();" style="width:150px;"/></td>
		        <td width="45">Edição:</td>
		        <td width="95"><input type="text" name="edicoes" id="edicoes" style="width:80px;"/></td>
		        <td width="67">Fornecedor:</td>
              	<td colspan="2">
					<select id="idFornecedor" name="idFornecedor" style="width:200px;">
					    <option value="-1"  selected="selected">Todos</option>
					    <c:forEach items="${listaFornecedores}" var="fornecedor">
					      		<option value="${fornecedor.key}">${fornecedor.value}</option>	
					    </c:forEach>
					</select>
       			</td>		        
		        <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="cliquePesquisar();">Pesquisar</a></span></td>
		      </tr>
			</table>		
		</fieldset>
	      <div class="linha_separa_fields">&nbsp;</div>
		      <fieldset class="classFieldset">
		       	  <legend>Produto: 4455 - Veja - Tipo de Lançamento: Normal</legend>
		        	<table class="parciaisGrid"></table>
		            <!--<span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup();"><img src="../images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>-->
		            <span class="bt_novos" title="Gerar Arquivo">
		            	<a href="${pageContext.request.contextPath}/lancamento/vendaProduto/exportar?fileType=XLS&tipoExportacao=principal">
		            		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
		            		Arquivo
		            	</a>
		            </span>
					<span class="bt_novos" title="Imprimir">
						<a href="${pageContext.request.contextPath}/lancamento/vendaProduto/exportar?fileType=PDF&tipoExportacao=principal">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
							Imprimir
						</a>
					</span>	        
		      </fieldset>
	      </div>
	      <div class="linha_separa_fields">&nbsp;</div>
    </div>
</div> 
<script>
	$(".parciaisGrid").flexigrid({
			preProcess: executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Edição',
				name : 'numEdicao',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Dt. Lcto',
				name : 'dataLancamento',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dt. Rcto',
				name : 'dataRecolhimento',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda',
				name : 'valorVendaFormatado',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda',
				name : 'percentagemVendaFormatado',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço Capa R$',
				name : 'valorPrecoCapaFormatado',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total R$',
				name : 'valorTotalFormatado',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Chamada Capa',
				name : 'chamadaCapa',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "edicao",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		
		
		$(".detalhesVendaGrid").flexigrid({
			preProcess: executarPreProcessamentoFilha,
			dataType : 'json',
			colModel : [ {
				display : 'Período',
				name : 'periodo',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Lançamento',
				name : 'dataLancamento',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Recolhimento',
				name : 'dataRecolhimento',
				width : 110,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 55,
				sortable : true,
				align : 'center'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 55,
				sortable : true,
				align : 'center'
			}, {
				display : 'Vendas',
				name : 'vendaFormatado',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Acumulada',
				name : 'vendaAcumuladaFormatado',
				width : 130,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda',
				name : 'percentualVendaFormatado',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			width : 750,
			height : 200
		});
</script>
</body>