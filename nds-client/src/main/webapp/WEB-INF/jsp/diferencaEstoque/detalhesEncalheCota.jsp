<div id="dialogDetalheEncalheCota" title="Detalhes do Encalhe Cotas" style="display:none;">

<jsp:include page="../messagesDialog.jsp">
	<jsp:param value="dialogDetalheEncalheCota" name="messageDialog"/>
</jsp:include>    

<fieldset style="width:730px!important;">
	<legend>Detalhes do Estoque - Cota</legend>
    <table width="680" border="0" cellspacing="0" cellpadding="2">
      <tr>
        <td width="71"><strong>Código:</strong></td>
        <td width="180">
			<span id="codigoDetalheEstoqueCota"></span>
		</td>
        <td width="98"><strong>Produto:</strong></td>
        <td width="130">
        	<span id="nomeProdutoDetalheEstoqueCota"></span>
		</td>
        <td width="97"><strong>Edição:</strong></td>
        <td width="80">
        	<span id="numeroEdicaoDetalheEstoqueCota"></span>
		</td>
      </tr>
      <tr>
        <td><strong>Fornecedor:</strong></td>
        <td>
        	<span id="nomeFornecedorDetalheEstoqueCota"></span>
		</td>
        <td><strong>Tipo Diferença:</strong></td>
        <td>
        	<span id="tipoDiferencaDetalheEstoqueCota"></span>
        </td>
        <td><strong>Qtde. Diferença:</strong></td>
        <td>
        	<span id="quantidadeDiferencaDetalheEstoqueCota"></span>
        </td>
      </tr>
    </table>  
</fieldset>

<div class="linha_separa_fields">&nbsp;</div>
<fieldset style="width:730px!important;">
	<legend>Detalhes do Estoque - Cota</legend>
     <table class="detalhesCotaGrid"></table>
     <table width="100%" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		  	<td width="30%">
		  		<span class="bt_novos" title="Gerar Arquivo">
			  		<a href="${pageContext.request.contextPath}/estoque/diferenca/exportarDetalhesEstoqueCota?fileType=XLS">
			  			<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
			  			Arquivo
			  		</a>
		  		</span>
		    	<span class="bt_novos" title="Imprimir">
		    		<a href="${pageContext.request.contextPath}/estoque/diferenca/exportarDetalhesEstoqueCota?fileType=PDF">
		    			<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
		    			Imprimir
		    		</a>
		    	</span>
		    </td>
		    <td width="12%" align="right">
		    	<strong>Total:</strong>
		    </td>
		    <td width="6%" align="center">
		    	<span id="totalExemplaresDetalheEstoqueCota"></span>
		   	</td>
		    <td width="48%" align="right">
		    	<span id="valorTotalDetalheEstoqueCota"></span>
		    </td>
		    <td width="4%">
		    	&nbsp;
		    </td>
		  </tr>
	</table>

</fieldset>


</div>