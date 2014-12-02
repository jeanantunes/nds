<form id="form-venda-encalhe">
<div id="dialog-venda-encalhe-cota" style="display: none;" title="Venda de Encalhe / Suplementar">
<jsp:include page="../messagesDialog.jsp" />     
<table width="720" border="0" cellpadding="2" cellspacing="1" class="filtro">
  <tr>
    <td width="33">Data:</td>
    <td width="140"><span class="dadosFiltro" id="span_data_venda" style="display: inline;"></span></td>
    <td width="39">Cota:</td>
    <td width="109">
    	<input name="vend-suplementar-numCotaVenda" 
               id="vend-suplementar-numCotaVenda" 
               type="text"
               maxlength="11"
               style="width:70px; 
               float:left; margin-right:5px;"
               onchange="pesquisaCotaVendaEncalhe.pesquisarPorNumeroCota('#vend-suplementar-numCotaVenda', '#vend-suplementar-descricaoCotaVenda',true,
              	  									 VENDA_PRODUTO.pesquisarCotaVendaSuccessCallBack, 
              	  									 VENDA_PRODUTO.pesquisarCotaVendaErrorCallBack);"/>
				              	  									 	
    </td>
    <td width="350">Nome:
    		<input type="hidden" name="vend-suplementar-descricaoCotaVenda" id="vend-suplementar-descricaoCotaVenda"/>
			<span class="dadosFiltro" id="span_nome_cota_venda" style="display: inline;"></span>  
    </td>
    <td width="10">&nbsp;</td>
    <td width="35">Box:</td>
    <td width="120"><span class="dadosFiltro" id="span_nome_box_venda" style="display: inline; width:80px"></span></td>
  </tr>
</table>
<br />

<table id="vendaEncalhesGridCota" class="vendaEncalhesGridCota"></table>

<br />

<table width="796" border="0" cellspacing="2" cellpadding="2">
  <tr>
  	<td width="100"><strong>Data Vencimento:</strong></td>
  	<td>
  		<div style="display: none;" id="div_data_inclusao">
			<input type="text" style="width:80px;" name="vend-suplementar-dataVencimento" id="vend-suplementar-dataVencimento" />
		</div>
		<div style="display: none;" id="div_data_edicao">
			<input disabled="disabled" type="text" style="width:80px;" name="vend-suplementar-dataVencimentoEdicao" id="vend-suplementar-dataVencimentoEdicao" />
		</div>
  	</td>
	<td vAlign="bottom" width="254" align="right"><strong>Venda Encalhe:</strong></td>
    <td vAlign="bottom" width="71" align="right" id="span_qntSolicitada_encalhe_venda">0</td>
    <td vAlign="bottom" width="65" align="right" id="span_total_encalhe_venda">0,00</td>
    <td width="119" align="right">&nbsp;</td>
  </tr>
  
  <tr>
    <td height="18">&nbsp;</td>
    <td>&nbsp;</td>
    <td vAlign="top" align="right"><strong>Venda Suplementar:</strong></td>
    <td vAlign="top" align="right" id="span_qntSolicitada_suplementar_venda">0</td>
    <td vAlign="top" align="right" id="span_total_suplementar_venda">0,00</td>
    <td align="right">&nbsp;</td>
  </tr>
  
  <tr>
	
	<td>&nbsp;</td>
    <td>&nbsp;</td>
	<td vAlign="top" align="right"><strong>Total R$</strong></td>
    <td vAlign="top" align="right" id="span_total_solicitado_venda">0</td>
    <td vAlign="top" align="right" id="span_total_geral_venda">0,00</td>
	<td align="right">&nbsp;</td>
  </tr>
  
</table>
</div>
</form>
