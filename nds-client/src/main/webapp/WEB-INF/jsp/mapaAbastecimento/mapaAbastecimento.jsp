<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/mapaAbastecimento.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	<script language="javascript" type="text/javascript">
		
		var pesquisaCotaMapaAbastecimento = new PesquisaCota();
		
		var pesquisaProdutoMapaAbastecimento = new PesquisaProduto();
	
		var MA = new MapaAbastecimento('${pageContext.request.contextPath}', 'MA', BaseController.workspace);
	</script>
	
		
	<script language="javascript" type="text/javascript">
					
		function popup_detalhe_box() {
			
				$( "#dialog-detalhesAbastecimento", BaseController.workspace ).dialog({
					resizable: false,
					height:410,
					width:710,
					modal: true,
					buttons: {
						"Fechar": function() {
							$( this ).dialog( "close" );
						},
					},
					form: $("#dialog-detalhesAbastecimento", this.workspace).parents("form")
				});
			};	
		
	</script>

</head>

<body>
	
<form id="idEmissaoCEfornecedor">
	<div id="dialog-pesq-produtos" title="Selecionar Produtos" style="display: none;">
		<fieldset>
			<legend>Selecione um ou mais Produtos</legend>
			<select id="selectProdutos" name="selectProdutos"
					size="1" multiple="multiple" style="width:440px; height:150px;" >
					
			    <c:forEach items="${listaProdutos}" var="produto">
			    	<option value="${produto.key}_${produto.value}">${produto.value}</option>
		      	</c:forEach>
	      	</select>
		</fieldset>
	</div>
</form>
	
<form id="form-detalhesAbastecimento">
<div id="dialog-detalhesAbastecimento" title="Produtos do Box" style="display:none;">
	<fieldset>
    	<legend><span id="titleBox" ></span></legend>
		<table class="mapaAbastecimentoDetalheGrid"></table>

    </fieldset>
</div>
</form>

<div class="corpo">
  
    <div class="container">
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Mapa de Abastecimento</legend>
        <table width="980" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td>Data Lançamento:</td>
              <td>
			  
			  
<!-- Data Lançamento -->              
<input value="${data}" id="dataLancamento" type="text"  style="width:100px;"/>

			</td>
              <td>Tipo Consulta:</td>
              <td>
			  
<!-- Tipo Consulta -->
<select id="tipoConsulta" onchange="MA.mudarTipoPesquisa(this.value)" style="width:120px;">
      <option value="" selected="selected">Selecione...</option>
      <option value="BOX">Box</option>
      <option value="ROTA">Rota</option>
      <option value="PRODUTO_ESPECIFICO">Produto Específico</option>
      <option value="COTA">Cota</option>
      <option value="PRODUTO_X_COTA">Produto X Cota</option>
      <option value="PRODUTO">Produto</option>
      <option value="ENTREGADOR">Entregador</option>
</select>
			  
			  </td>
              <td>&nbsp;</td>
              <td colspan="3">&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>Box:</td>
              <td>
			  
<!-- Box -->
<select id="box" disabled="disabled" name="select" style="width:120px;">
       <option selected="selected" value="">Selecione...</option>
	        
</select>
			
			  
			  
			  </td>
              <td>Roteiro:</td>
              <td>
			  
			  
			  <select name="select4" id="select4" style="width:140px;">
              </select>
			  
			  
			  </td>
              <td>Rota:</td>
              <td colspan="3">
			  
<!-- Rota -->              
<select id="rota" disabled="disabled" name="select2" style="width:120px;">
	 <option selected="selected" value="">Selecione...</option>
	 
</select>
			  
			  </td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td width="103">Código:</td>
              <td>
			  
			  
<!-- Código Produto -->              
<input id="codigoProduto" disabled="disabled" type="text" style="width:80px; float:left; margin-right:5px;"
	   onchange="MA.pesquisarPorCodigoProduto('#codigoProduto', '#nomeProduto', null , false);" />
	
			  </td>
              <td width="81">Produto:</td>              
              <td width="155">
              	<a id="linkProdutos" href="javascript:;" onclick="MA.carregarProdutos();">Selecione Produto:</a>
			  </td>
              <td width="43">Edição:</td>
              <td colspan="3">
			  
<!-- EdiçãoProduto -->              
<input id="edicao" disabled="disabled" type="text" style="width:112px; float:left; margin-right:5px;"/>

			  
			  </td>
              <td width="104">&nbsp;</td>
            </tr>
            <tr>
              <td colspan="9">
				<div id="produtosSelecionados" class="produto" style="float:left; width:980px;"></div>
              </td>
            </tr>
            <tr>
              <td>Cota:</td>
              <td width="181">
			  
<!-- Código Cota -->              
<input id="codigoCota" disabled="disabled" type="text" style="width:80px; float:left; margin-right:5px;"
			onchange="pesquisaCotaMapaAbastecimento.pesquisarPorNumeroCota('#codigoCota', '#nomeCota',false,function(){MA.atualizarBoxRota(true)});"/>

			  
			  <span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
              <td>Nome:</td>
              <td>
			  
			  
<!-- Nome Cota -->              
<input id="nomeCota" disabled="disabled" type="text" class="nome_jornaleiro" style="width:150px;"
		onkeyup="pesquisaCotaMapaAbastecimento.autoCompletarPorNome('#nomeCota');" 
		 	   onblur="pesquisaCotaMapaAbastecimento.pesquisarPorNomeCota('#codigoCota', '#nomeCota');"/>

				</td>
              <td align="right">
			  
<!-- Quebra por Cota -->
<input id="quebraPorCota" disabled="disabled" type="checkbox" name="checkbox"/>

				</td>
              <td width="101">Quebra por Cota</td>
              <td width="20">
			  
<!-- Excluir Produto Sem Reparte -->              
<input id="excluirProdutoSemReparte" type="checkbox" name="checkbox"/>

			</td>
              <td width="156">Excluir Produtos s/Reparte</td>
              <td><span class="bt_pesquisar">
			  
<!-- Pesquisar -->
<a id="btnPesquisar" href="javascript:;" onclick="MA.pesquisar();">Pesquisar</a>
			  
			  </span></td>
            </tr>
          </table>

      </fieldset>     
     
      
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Mapas de Abastecimentos</legend>
        <div class="grids" style="display:none;">
			<div id="gridBox" style="display:none;">
				<table class="mapaAbastecimentoGrid"></table>
			</div>
			<div id="gridRota" style="display:none;">
				<table class="mapaAbastecimentoRotaGrid"></table>				
			</div>
			<div id="gridBoxQuebraCota" style="display:none;">
				<table class="mapaAbastecimentoGridQuebraCota"></table>
			</div>
			<div id="gridRotaQuebraCota" style="display:none;">
				<table class="mapaAbastecimentoRotaGridQuebraCota"></table>				
			</div>
			<div id="gridCota" style="display:none;">
				<span>
					<strong>Cota:</strong>  <span id="codigoCotaHeader">  </span> - 
					<strong>Nome:</strong> <span id="nomeCotaHeader">  </span>
				</span>
				<table class="mapaAbastecimentoCotaEspGrid"></table>
			</div>
			<div id="gridProduto" style="display:none;">
				<table class="mapaAbastecimentoProdutoGrid"></table>
			</div>
			<div id="gridProdutoEspecifico" style="display:none;">
				<span>
					<strong>Código:</strong>  <span id="codigoProdutoHeader">  </span> - 
					<strong>Produto:</strong> <span id="nomeProdutoHeader">  </span> - 
					<strong>Edição:</strong>  <span id="edicaoProdutoHeader">  </span>
				</span>
				<table class="mapaAbastecimentoProdEspGrid"></table>

		   </div>
		   <div id="gridProdutoCota" style="display:none;">
				<table class="mapaAbastecimentoProdCotaGrid"></table>
			</div>
          <br />
   		  <span class="bt_novos" id="map_1" title="Imprimir">

<!-- IMPRIMIR MAPA -->   		  
<a href="${pageContext.request.contextPath}/mapaAbastecimento/imprimirMapaAbastecimento" target="blank">

		
		<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir Mapa</a>
		
		</span>          
       
        </div>
        
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 
</body>

