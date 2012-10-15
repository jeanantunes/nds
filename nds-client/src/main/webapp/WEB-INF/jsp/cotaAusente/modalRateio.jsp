
<div id="dialog-suplementar" title="Redistribuição">

<jsp:include page="../messagesDialog.jsp" />

<table width="555" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="226" valign="top">
    
    <fieldset>
    	<legend>Nota de Envio</legend>
        <table id="idMovimentos" width="350" border="0" cellspacing="2" cellpadding="2">
      <tr id="idCabecalhoMovimentos" align="center">
        <td width="49" bgcolor="#F5F5F5"><strong>Código</strong></td>
        <td width="112" bgcolor="#F5F5F5"><strong>Produto</strong></td>
        <td width="61" bgcolor="#F5F5F5"><strong>Edição</strong></td>
        <td width="62" align="center" bgcolor="#F5F5F5"><strong>Reparte</strong></td>
        <td width="34" bgcolor="#F5F5F5">&nbsp;</td>
      </tr>
      
    </table>
    <span class="bt_sellAll" style="float:right;">
    	<table>
    		<tr>
    			<td> <label for="sel">Selecionar Todos</label> </td> 
    			<td> <input type="checkbox" id="sel" name="Todos" onclick="cotaAusenteController.selecionarTodos(this.checked);" style="float:left; margin-right:10px; "> </td> 
    		</tr>
    	</table>
    	
    </span>
    
    </fieldset>
    
    
    </td>
    <td width="315" valign="top">
    <fieldset id="idFieldRateios" style="display: none">
    <legend id="idLegendRateios">Redistribuição</legend>
    
    <table id="idRateios" border="0" cellspacing="1" cellpadding="1" width:350px;" id="grid_1">
      <tr id="idCabecalhoRateios">
        <td bgcolor="#F5F5F5"><strong>Cota</strong></td>
        <td bgcolor="#F5F5F5"><strong>Nome</strong></td>
        <td align="center" bgcolor="#F5F5F5"><strong>Quantidade</strong></td>
      </tr>
     
    </table>
    
     <span class="bt_sellAll" style="float:right;margin-right:20px; ">
    	
    	<table>
    		<tr>
    			<td> <label for="sel"><strong>Total Redistribuido:</strong></label> </td> 
    			<td> <div id="qtdeTotal"></div> </td> 
    			<td> exes </td> 
    		</tr>
    	</table>
    	
    </span>
    
    </fieldset>
    
     <fieldset id="idFieldRateioMultiproduto" style="display: none">
    <legend id="idLegendRateiosMultiProduto">Redistribuição - Produtos Selecionados</legend>
    
    <table id="idRateioMultiProduto" border="0" cellspacing="1" cellpadding="1" width:350px;" id="grid_1">
      <tr id="idCabecalhoRateiosMultiproduto">
        <td bgcolor="#F5F5F5"><strong>Cota</strong></td>
        <td bgcolor="#F5F5F5"><strong>Nome</strong></td>
      </tr>
     
       <tr id="idCabecalhoRateiosMultiproduto">
        <td><input type="text" name="idNumCotaDestino" id="idNumCotaDestino" style="width: 80px; " class="cotaDestino" onchange="pesquisaCotaCotaAusente.pesquisarPorNumeroCota('#idNumCotaDestino', '#idNomeCotaDestino',true)"></td>
        <td><input type="text" name="idNomeCotaDestino" id="idNomeCotaDestino" style="width: 250px; " onblur="pesquisaCotaCotaAusente.pesquisarPorNomeCota('#idNumCotaDestino', '#idNomeCotaDestino',true);" onkeyup="pesquisaCotaCotaAusente.autoCompletarPorNome('#idNomeCotaDestino')" ></td>
      </tr>
     
    </table>
    
    </fieldset>
    
    
    </td>
  </tr>
</table>    
    
</div>