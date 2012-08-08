<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>

<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselects-0.3.js"></script>

<script language="javascript" type="text/javascript">
	
$(function() {		
	$("#produto").autocomplete({source: ""});		
	$("#descontoGeral").mask("99.99");
	$("#descontoEspecifico").mask("99.99");
	$("#span_total_geral_venda").formatCurrency({region: 'pt-BR', decimalSymbol: ',', symbol: ''});
});

var TIPO_DESCONTO = {
	
	tipoDescontoSelecionado:"",
	
	pesquisar:function () {
		
		if(TIPO_DESCONTO.tipoDescontoSelecionado == 'GERAL'){
			TIPO_DESCONTO.pesquisarDescontoGeral();						
		}else if(TIPO_DESCONTO.tipoDescontoSelecionado == 'ESPECIFICO'){
			TIPO_DESCONTO.pesquisarDescontoEspecifico();
		}else{
			TIPO_DESCONTO.pesquisarDescontoProduto();
		}	
	},
	
	pesquisarDescontoGeral:function(){
		
		$(".tiposDescGeralGrid").flexOptions({
			url: "<c:url value='/administracao/tipoDescontoCota/pesquisarDescontoGeral'/>",
			params: [],
		    newp: 1,
		});
		
		$(".tiposDescGeralGrid").flexReload();
	},
	
	pesquisarDescontoEspecifico:function(){
		 
		var cotaEspecifica = $("#cotaDaPesquisa").val();
		var nomeEspecifico = $("#nomeDaCotaDaPesquisa").val();
		
		$(".tiposDescEspecificoGrid").flexOptions({
			url: "<c:url value='/administracao/tipoDescontoCota/pesquisarDescontoEspecifico'/>",
			params: [
					 {name:'cotaEspecifica', value:cotaEspecifica},
			         {name:'nomeEspecifico', value:nomeEspecifico}
			         ],
		    newp: 1,
		});
		
		$(".tiposDescEspecificoGrid").flexReload();
	},
	
	pesquisarDescontoProduto:function(){
		var codigo = $("#codigoPesquisa").val();
		var produto = $("#produtoPesquisa").val();
		
		$(".tiposDescProdutoGrid").flexOptions({
			url: "<c:url value='/administracao/tipoDescontoCota/pesquisarDescontoProduto'/>",
			params: [
					 {name:'codigo', value:codigo},
			         {name:'produto', value:produto}
			         ],
		    newp: 1,
		});
		
		$(".tiposDescProdutoGrid").flexReload();
	},
			
	mostra_geral:function(){
		
		TIPO_DESCONTO.tipoDescontoSelecionado = "GERAL";
		
		$( '#tpoGeral' ).show();
		$( '#tpoEspecifico' ).hide();
		$( '#tpoProduto' ).hide();
		$( '.especifico' ).hide();
		$( '.produto' ).hide();
		
		TIPO_DESCONTO.exibirExportacao(false);
		
		$(".grids").show();
	},
	
	mostra_especifico:function(){
		
		TIPO_DESCONTO.tipoDescontoSelecionado = "ESPECIFICO";
		
		$( '#tpoGeral' ).hide();
		$( '#tpoEspecifico' ).show();
		$( '.especifico' ).show();
		$( '#tpoProduto' ).hide();
		$( '.produto' ).hide();
		
		TIPO_DESCONTO.exibirExportacao(false);
		
		$(".grids").show();
	},
	
	mostra_produto:function(){
		
		TIPO_DESCONTO.tipoDescontoSelecionado = "PRODUTO";
		
		$( '#tpoGeral' ).hide();
		$( '#tpoEspecifico' ).hide();
		$( '.especifico' ).hide();
		$( '#tpoProduto' ).show();
		$( '.produto' ).show();
		
		TIPO_DESCONTO.exibirExportacao(false);
		
		$(".grids").show();
	},
	
	exibirExportacao:function(isExibir){
		
		if(TIPO_DESCONTO.tipoDescontoSelecionado =="PROPDUTO"){
			if(isExibir == true){
				$("#idExportacaoProduto").show();	
			}else{
				$("#idExportacaoProduto").hide();
			}
		}
		
		if(TIPO_DESCONTO.tipoDescontoSelecionado =="GERAL"){
			if(isExibir == true){
				$("#idExportacaoGeral").show();	
			}else{
				$("#idExportacaoGeral").hide();
			}
		}
		
		if(TIPO_DESCONTO.tipoDescontoSelecionado =="ESPECIFICO"){
			if(isExibir == true){
				$("#idExportacaoEspecifico").show();	
			}else{
				$("#idExportacaoEspecifico").hide();
			}
		}
	},
	
	executarPreProcessamento:function(resultado) {				
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids").hide();
			
			TIPO_DESCONTO.exibirExportacao(false);
			
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {					

			var linkExcluir = '<a href="javascript:;" onclick="TIPO_DESCONTO.exibirDialogExclusao(' + row.cell.id + ');" style="cursor:pointer">' +
							   	 '<img title="Excluir Desconto" src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" />' +
							   '</a>';
			
			row.cell.acao = linkExcluir;
		});
		
		$(".grids").show();
		
		TIPO_DESCONTO.exibirExportacao(true);
		
		return resultado;
	},

	exibirDialogExclusao:function(idDesconto){		
		$("#dialog-excluirCota" ).dialog({
			resizable: false,
			height:'auto',
			width:250,
			modal: true,
			buttons: {
				"Confirmar": function() {
		
					TIPO_DESCONTO.excluirDesconto(idDesconto,tipoDescontoSelecionado);
									
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	excluirDesconto:function(idDesconto, tipoDesconto){		
		$.postJSON(contextPath + "/administracao/tipoDescontoCota/excluirDesconto",
				"idDesconto="+idDesconto+"&tipoDesconto="+tipoDesconto, 
				function(){
					TIPO_DESCONTO.pesquisar();
				}
		);
	},
	
	fecharDialogs:function() {
		$( "#dialog-geral" ).dialog( "close" );
		$( "#dialog-especifico" ).dialog( "close" );
		$( "#dialog-produto" ).dialog( "close" );
	},
	
	carregarFornecedores:function(idComboFornecedores){
		
		$.postJSON(contextPath + "/administracao/tipoDescontoCota/obterFornecedores",
				null, 
				function(result){
					
					if(result){
						var comboClassificacao =  montarComboBox(result, false);
						
						$(idComboFornecedores).html(comboClassificacao);
					}
				},null,true
		);
	}		
};

</script>

</head>

<body>
	<div id="dialog-excluirCota" title="Atenção" style="display:none">
		<p>Confirmar exclusão Desconto ?</p>
	</div>

	<!-- Modal de inclusão de novo desconto Geral  -->
	<jsp:include page="novoDescontoGeral.jsp"/>
	
	<!-- Modal de inclusão de novo desconto Especifico  -->
	<jsp:include page="novoDescontoEspecifico.jsp"/>

	<!-- Modal de inclusão de novo desconto Produto  -->
	<jsp:include page="novoDescontoProduto.jsp"/>
	

   <div class="container">
    
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Tipo de Desconto Cota</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="20"><input type="radio" name="radio" id="radioGeral" value="radio" onclick="TIPO_DESCONTO.mostra_geral();" /></td>
                <td width="47">Geral</td>
                <td width="20"><input type="radio" name="radio" id="radioEspecifico" value="radio" onclick="TIPO_DESCONTO.mostra_especifico();"  /></td>
                <td width="65">Específico</td>
                <td width="20"><input type="radio" name="radio" id="radioProduto" value="radio" onclick="TIPO_DESCONTO.mostra_produto();"  /></td>
                <td width="48">Produto</td>
                <td width="585">
                <div class="especifico" style="display: none">
	                <label style="width:auto!important;">Cota:</label>
	                <input name="cotaDaPesquisa" id="cotaDaPesquisa" type="text" style="width:80px; float:left;" onchange="cota.pesquisarPorNumeroCota('#cotaDaPesquisa', '#nomeDaCotaDaPesquisa');" />
	                <label style="width:auto!important;">Nome:</label>
	                <input name="nomeDaCotaDaPesquisa" id="nomeDaCotaDaPesquisa" type="text" style="width:160px; float:left;" />
                </div>
                
                <div class="produto" style="display: none">
	                <label style="width:auto!important;">Código:</label>
	                <input type="text" name="codigoPesquisa" id="codigoPesquisa" maxlength="255" 
					   	   style="width:80px; float:left;"
					       onblur="produtoEdicao.pesquisarPorCodigoProduto('#codigoPesquisa', '#produtoPesquisa', false,
								   undefined,
								   undefined);"/>
	                <label style="width:auto!important;">Produto:</label>
	                <input type="text" name="produtoPesquisa" id="produtoPesquisa" maxlength="255" 
						   style="width:160px; float:left;"
						   onkeyup="produtoEdicao.autoCompletarPorNomeProduto('#produtoPesquisa', false);"
						   onblur="produtoEdicao.pesquisarPorNomeProduto('#codigoPesquisa', '#produtoPesquisa', false,
								   undefined,
								   undefined);" />
	                
                </div>
                </td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="TIPO_DESCONTO.pesquisar();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
      <div class="grids" style="display:none;">
      
      <fieldset class="classFieldset" id="tpoGeral" style="display:none;">
       	  <legend>Tipos de Desconto Geral</legend>
        
            <div id="idExportacaoGeral">
	       		
	       		<table class="tiposDescGeralGrid"></table>
	       		
	       		<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;">
	       			<a href="${pageContext.request.contextPath}/administracao/tipoDescontoCota/exportar?fileType=XLS&tipoDesconto=GERAL">
						<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
						Arquivo
					</a>
	       		</span>
	             <span class="bt_novos" title="Imprimir">
	             	<a href="${pageContext.request.contextPath}/administracao/tipoDescontoCota/exportar?fileType=PDF&tipoDesconto=GERAL">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
						Imprimir
					</a>
	             </span>
             </div>
           <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="DESCONTO_GERAL.popup_geral();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
   
      </fieldset>
      
      
      <fieldset class="classFieldset" id="tpoEspecifico" style="display:none;">
       	  <legend>Tipos de Desconto Específico</legend>
       
       		<div id="idExportacaoEspecifico">
				
				<table class="tiposDescEspecificoGrid"></table>
				
				<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;">
	       			<a href="${pageContext.request.contextPath}/administracao/tipoDescontoCota/exportar?fileType=XLS&tipoDesconto=ESPECIFICO">
						<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
						Arquivo
					</a>
	       		</span>
	             <span class="bt_novos" title="Imprimir">
	             	<a href="${pageContext.request.contextPath}/administracao/tipoDescontoCota/exportar?fileType=PDF&tipoDesconto=ESPECIFICO">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
						Imprimir
					</a>
	             </span>
	        </div>
           <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="DESCONTO_ESPECIFICO.popup_especifico();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
   
      </fieldset>
      
      
      <fieldset class="classFieldset" id="tpoProduto" style="display:none;">
       	
       	<legend>Tipos de Desconto Produto</legend>
       		
       	<div id="idExportacaoProduto">	
       		
       		<table class="tiposDescProdutoGrid"></table>
			
			<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;">
       			<a href="${pageContext.request.contextPath}/administracao/tipoDescontoCota/exportar?fileType=XLS&tipoDesconto=PRODUTO">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					Arquivo
				</a>
       		</span>
             <span class="bt_novos" title="Imprimir">
             	<a href="${pageContext.request.contextPath}/administracao/tipoDescontoCota/exportar?fileType=PDF&tipoDesconto=PRODUTO">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
					Imprimir
				</a>
             </span>
        </div>
        <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="DESCONTO_PRODUTO.popup_produto();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
   
      </fieldset>
    </div>
</div>
<script>
	$(".tiposDescGeralGrid").flexigrid({
		preProcess: TIPO_DESCONTO.executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : '',
				name : 'sequencial',
				width : 60,
				sortable : true,
				align : 'center'
			},{
				display : 'Desconto %',
				name : 'desconto',
				width : 150,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fornecedores',
				name : 'forncedores',
				width : 320,
				sortable : true,
				align : 'left'
			}, {

				display : 'Data Alteração',
				name : 'dtAlteracao',
				width : 150,
				sortable : true,
				align : 'center'
			}, {
				display : 'Usuário',
				name : 'usuario',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "sequencial",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		
		$(".tiposDescEspecificoGrid").flexigrid({
			preProcess: TIPO_DESCONTO.executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nomeCota',
				width : 350,
				sortable : true,
				align : 'left'
			}, {
				display : 'Desconto %',
				name : 'desconto',
				width : 150,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Alteração',
				name : 'dataAlteracao',
				width : 120,
				sortable : true,
				align : 'center'
			}, {
				display : 'Usuário',
				name : 'nomeUsuario',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		$(".tiposDescProdutoGrid").flexigrid({
			preProcess: TIPO_DESCONTO.executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 70,
				sortable : true,
				align : 'left'
			},{
				display : 'Produto',
				name : 'produto',
				width : 228,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Desconto %',
				name : 'desconto',
				width : 150,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Alteração',
				name : 'dtAlteracao',
				width : 120,
				sortable : true,
				align : 'center'			
			}, {
				display : 'Usuário',
				name : 'usuario',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		
</script>
</body>