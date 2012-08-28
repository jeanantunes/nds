
<script type="text/javascript">

$(function() {

	$("#vendaEncalhesGrid").flexigrid({
		preProcess:VENDA_PRODUTO.executarPreProcessamentoGridVenda,
		onSuccess: VENDA_PRODUTO.formatarCampos,
		dataType : 'json',
		colModel :[ {
			display : 'Código de Barras',
			name : 'codigoBarras',
			width : 170,
			sortable : true,
			align : 'left'
		}, { 
			display : 'Código',
			name : 'codigoProduto',
			width : 70,
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
			display : 'Preço c/ Desc. R$',
			name : 'precoDesconto',
			width : 80,
			sortable : true,
			align : 'center'
		}, {
			display : 'Qtde Disp.',
			name : 'qntDisponivel',
			width : 70,
			sortable : true,
			align : 'center'
		}, {
			display : 'Qtde Solic.',
			name : 'qntSolicitada',
			width : 70,
			sortable : true,
			align : 'center'
		}, {
			display : 'Total R$',
			name : 'total',
			width : 70,
			sortable : true,
			align : 'center'
		}, {
			display : 'Forma de Venda',
			name : 'formaVenda',
			width : 130,
			sortable : true,
			align : 'left'
		}],
		width : 1000,
		height : 220
	});
});
</script>


<div id="dialog-venda-encalhe" style="display: none;" title="Venda de Encalhe">
<jsp:include page="../messagesDialog.jsp" />     
<table width="720" border="0" cellpadding="2" cellspacing="1" class="filtro">
  <tr>
    <td width="33">Data:</td>
    <td width="140"><span class="dadosFiltro" id="span_data_venda" style="display: inline;"></span></td>
    <td width="39">Cota:</td>
    <td width="109">
    	<input name="numCotaVenda" 
               id="numCotaVenda" 
               type="text"
               maxlength="11"
               style="width:70px; 
               float:left; margin-right:5px;"
               onchange="pesquisaCotaVendaEncalhe.pesquisarPorNumeroCota('#numCotaVenda', '#descricaoCotaVenda',true,
              	  									 VENDA_PRODUTO.pesquisarCotaVendaSuccessCallBack, 
              	  									 VENDA_PRODUTO.pesquisarCotaVendaErrorCallBack);"/>
				              	  									 	
    </td>
    <td width="350">Nome:
    		<input type="hidden" name="descricaoCotaVenda" id="descricaoCotaVenda"/>
			<span class="dadosFiltro" id="span_nome_cota_venda" style="display: inline;"></span>  
    </td>
    <td width="283">&nbsp;</td>
    <td width="35">Box:</td>
    <td width="80"><span class="dadosFiltro" id="span_nome_box_venda" style="display: inline; width:80px"></span></td>
  </tr>
</table>
<br />

<table id="vendaEncalhesGrid" class="vendaEncalhesGrid"></table>

<br />

<table width="837" border="0" cellspacing="2" cellpadding="2">
  <tr>
  	<td width="160" align="left"><strong>Data Vencimento:</strong></td>
  	<td width="150" align="left">
  		<div style="display: none;" id="div_data_inclusao"><input type="text" style="width:80px;" name="dataVencimento" id="dataVencimento" /></div>
  		<div style="display: none;" id="div_data_edicao"><input disabled="disabled" type="text" style="width:80px;" name="dataVencimentoEdicao" id="dataVencimentoEdicao" /></div>
  	</td>
    <td width="500" align="right"><strong>Total R$</strong></td>
    <td width="110" align="center"><span style="display: inline; width:80px"id="span_total_disponivel_venda">0</span></td>
    <td width="124" align="center"><span style="display: inline; width:80px" id="span_total_solicitado_venda">0</span></td>
    <td width="106" align="center"><span  style="display: inline; width:80px"  id="span_total_geral_venda">0,00</span></td>
  </tr>
</table>

</div>