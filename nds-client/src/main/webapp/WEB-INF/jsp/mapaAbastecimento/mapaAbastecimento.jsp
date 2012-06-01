<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/mapaAbastecimento.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
	<script language="javascript" type="text/javascript">
	
		var MA = new MapaAbastecimento('${pageContext.request.contextPath}', 'MA');
	</script>
	
		
	<script language="javascript" type="text/javascript">
					
		function popup_detalhe_box() {
			
				$( "#dialog-detalhesAbastecimento" ).dialog({
					resizable: false,
					height:400,
					width:700,
					modal: true,
					buttons: {
						"Fechar": function() {
							$( this ).dialog( "close" );
						},
					}
				});
			};	
		
	</script>

</head>

<body>
	
	

<div id="dialog-detalhesAbastecimento" title="Produtos do Box" style="display:none;">
	<fieldset>
    	<legend>Box 1</legend>
		<table class="mapaAbastecimentoDetalheGrid"></table>

    </fieldset>
</div>

<div class="corpo">
  
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Ausente < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Mapa de Abastecimento</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td>Data Lançamento:</td>
              <td>
              
<!-- Data Lançamento -->              
<input id="dataLancamento" type="text"  style="width:100px;"/></td>

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
<select id="box" name="select" style="width:120px;" onchange="opcaoImpressao(this.value);">
       <option value="2">Selecione...</option>
       <option value="1">Box 1</option>
       <option value="1">Box 2</option>
       <option value="1">Box 3</option>
       <option value="1">Box 4</option>
</select>
				</td>
              <td>Rota:</td>
              <td>
              
<!-- Rota -->              
<select id="rota" name="select2" style="width:120px;">
</select>
			
			</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td width="106">Código:</td>
              <td>
              
<!-- Código Produto -->              
<input id="codigoProduto" type="text" style="width:80px; float:left; margin-right:5px;"/>

                <span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
              <td width="73">Produto:</td>              
              <td width="236">
         
<!-- Nome Produto -->              
<input id= "nomeProduto" type="text" class="nome_jornaleiro" style="width:220px;"/></td>

              <td width="45">Edição:</td>
              <td width="173">
              
<!-- EdiçãoProduto -->              
<input id="edicao" type="text" style="width:112px; float:left; margin-right:5px;"/></td>

              <td width="104">&nbsp;</td>
            </tr>
            <tr>
              <td>Cota:</td>
              <td width="177">
              
<!-- Código Cota -->              
<input id="codigoCota" type="text" style="width:80px; float:left; margin-right:5px;"/>

			  <span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
              <td>Nome:</td>
              <td>
              
<!-- Nome Cota -->              
<input id="nomeCota" type="text" class="nome_jornaleiro" style="width:220px;"/></td>

              <td align="right">
              
<!-- Quebra por Cota -->              
<input id="quebraPorCota" type="checkbox" name="checkbox"/></td>

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
   		  <span class="bt_novos" id="map_1" title="Imprimir"><a href="${pageContext.request.contextPath}/mapa_abastecimento_1.htm" target="_blank"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir Mapa</a></span>
          
          <span class="bt_novos" id="map_2" title="Imprimir" style="display:none;"><a href="${pageContext.request.contextPath}/mapa_abastecimento_2.html" target="_blank"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir Mapa</a></span>
        </div>
        
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 
<script>


$(function() {	
	
	$(".mapaAbastecimentoGrid").flexigrid($.extend({},{
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
	
	$(".grids").show();	
	
	$(".mapaAbastecimentoDetalheGrid").flexigrid($.extend({},{
		colModel : [ {	
				display : 'Código',
				name : 'codigo',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
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
				name : 'vlrTotal',
				width : 100,
				sortable : true,
				align : 'right'
			}],
			width : 650,
			height : 255
	})); 	
});

</script>
</body>

</body>