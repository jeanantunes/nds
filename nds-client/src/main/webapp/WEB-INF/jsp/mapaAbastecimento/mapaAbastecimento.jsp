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
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td>Data Lançamento:</td>
              <td>
              
<!-- Data Lançamento -->              
<input value="${data}" id="dataLancamento" type="text"  style="width:100px;"/></td>

              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>Tipo Consulta:</td>
              <td>

<!-- Tipo Consulta -->
<select id="tipoConsulta" onchange="MA.mudarTipoPesquisa(this.value)" style="width:120px;">
      <option value="" selected="selected">Selecione...</option>
      <option value="BOX">Box</option>
      <option value="ROTA">Rota</option>
      <option value="COTA">Cota</option>
      <option value="PRODUTO">Produto</option>
</select>
			
			</td>
              <td>Box:</td>
              <td>
              
<!-- Box -->
<select id="box" disabled="disabled" name="select" style="width:120px;">
       <option selected="selected" value="">Selecione...</option>
	        
</select>
				</td>
              <td>Rota:</td>
              <td>
              
<!-- Rota -->              
<select id="rota" disabled="disabled" name="select2" style="width:120px;">
	 <option selected="selected" value="">Selecione...</option>
	 
</select>
			
			</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td width="106">Código:</td>
              <td>
              
<!-- Código Produto -->              
<input id="codigoProduto" disabled="disabled" type="text" style="width:80px; float:left; margin-right:5px;"
		 onchange="pesquisaProdutoMapaAbastecimento.pesquisarPorCodigoProdutoAutoCompleteEdicao('#codigoProduto', '#nomeProduto', null , false);" />
			  
			  </td>
              <td width="73">Produto:</td>              
              <td width="236">
         
<!-- Nome Produto -->              
<input id= "nomeProduto" disabled="disabled" type="text" class="nome_jornaleiro" style="width:220px;"
		 onkeyup="pesquisaProdutoMapaAbastecimento.autoCompletarPorNomeProduto('#nomeProduto', false);"
		 onblur="pesquisaProdutoMapaAbastecimento.pesquisarPorNomeProduto('#codigoProduto', '#nomeProduto', null, false);"/>

			  </td>
			  <td width="45">Edição:</td>
              <td width="173">
              
<!-- EdiçãoProduto -->              
<input id="edicao" disabled="disabled" type="text" style="width:112px; float:left; margin-right:5px;"/></td>

              <td width="104">&nbsp;</td>
            </tr>
            <tr>
              <td>Cota:</td>
              <td width="177">
              
<!-- Código Cota -->              
<input id="codigoCota" disabled="disabled" type="text" style="width:80px; float:left; margin-right:5px;"
			onchange="pesquisaCotaMapaAbastecimento.pesquisarPorNumeroCota('#codigoCota', '#nomeCota',false,function(){MA.atualizarBoxRota(true)});"/>

			  </td>
              <td>Nome:</td>
              <td>
              
<!-- Nome Cota -->              
<input id="nomeCota" disabled="disabled" type="text" class="nome_jornaleiro" style="width:220px;"
		onkeyup="pesquisaCotaMapaAbastecimento.autoCompletarPorNome('#nomeCota');" 
		 	   onblur="pesquisaCotaMapaAbastecimento.pesquisarPorNomeCota('#codigoCota', '#nomeCota');"/>

			 </td>

              <td align="right">
              
<!-- Quebra por Cota -->              
<input id="quebraPorCota" disabled="disabled" type="checkbox" name="checkbox"/></td>

              <td>Quebra por Cota</td>
              <td><span class="bt_pesquisar">
              
<!-- Pesquisar -->
<a id="btnPesquisar" href="javascript:;" onclick="MA.pesquisar();">Pesquisar</a></span></td>

            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Mapas de Abastecimentos</legend>
        <div class="grids" style="display:none;">
       	  <table class="mapaAbastecimentoGrid"></table>
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
<script>


$(function() {	
	
	$(".mapaAbastecimentoGrid", BaseController.workspace).flexigrid($.extend({},{
		colModel : [ {
				display : 'Box',
				name : 'box',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Total de Produtos',
				name : 'totalProduto',
				width : 250,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total Reparte',
				name : 'totalReparte',
				width : 250,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total Box R$',
				name : 'totalBox',
				width : 250,
				sortable : true,
				align : 'right'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : false,
				align : 'center'
			}],
			sortname : "box",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
	})); 
	
	$(".grids", BaseController.workspace).show();	
	
	$(".mapaAbastecimentoDetalheGrid", BaseController.workspace).flexigrid($.extend({},{
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
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total R$',
				name : 'total',
				width : 100,
				sortable : true,
				align : 'right'
			}],
			sortname : "nomeProduto",
			sortorder : "asc",
			width : 650,
			height : 255
	})); 	
});

</script>
</body>

</body>