<div id="dialog-salvar" title="Salvar Conferência" style="display:none;">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idModalConfirmarSalvarConf" name="messageDialog"/>
	</jsp:include>
	
	<fieldset style="width: 415px;">
        <legend>Salvar Conferência</legend>
        <p>Confima a Conferência de Encalhe?</p>
    </fieldset>

</div>

<div id="dialog-outros-valores" title="Outros Valores" style="display:none;">
	<fieldset>
        <legend>Outros Valores</legend>
        <table class="outrosVlrsGrid"></table>
    </fieldset>
</div>

<div id="dialog-detalhe-publicacao" title="Box de Encalhe" style="display:none;">
	<fieldset>
		<legend>Detalhes do Produto</legend>
		<table width="703" border="0" cellspacing="0" cellpadding="2" >
	  		<tr>
			    <td width="117">
			    	<img src="" id="imagemProduto" width="117" height="145" alt="" />
			    </td>
			    <td width="1">&nbsp;</td>
			    <td width="573" valign="top">
			    	<table width="574" border="0" cellspacing="1" cellpadding="2">
	      				<tr>
					        <td width="108" style="border-bottom:1px solid #ccc;"><strong>Nome:</strong></td>
					        <td width="174" style="border-bottom:1px solid #ccc;" id="nomeProdutoDetalhe"></td>
					        <td width="120" style="border-bottom:1px solid #ccc;"><strong>Preço Capa:</strong></td>
					        <td width="151" style="border-bottom:1px solid #ccc;" id="precoCapaDetalhe"></td>
						</tr>
						<tr>
					        <td style="border-bottom:1px solid #ccc;"><strong>Chamada Capa:</strong></td>
					        <td style="border-bottom:1px solid #ccc;" id="chamadaCapa"></td>
					        <td style="border-bottom:1px solid #ccc;"><strong>Preço Desconto:</strong></td>
					        <td style="border-bottom:1px solid #ccc;" id="precoDesconto"></td>
				     	</tr>
				      	<tr>
					        <td style="border-bottom:1px solid #ccc;"><strong>Fornecedor:</strong></td>
					        <td style="border-bottom:1px solid #ccc;" id="fornecedor"></td>
					        <td style="border-bottom:1px solid #ccc;"><strong>Brinde:</strong></td>
					        <td style="border-bottom:1px solid #ccc;" id="brinde"></td>
				      	</tr>
				      	<tr>
					        <td style="border-bottom:1px solid #ccc;"><strong>Editor:</strong></td>
					        <td style="border-bottom:1px solid #ccc;" id="editor"></td>
					        <td style="border-bottom:1px solid #ccc;"><strong>Pacote Padrão:</strong></td>
					        <td style="border-bottom:1px solid #ccc;" id="pacotePadrao"></td>
						</tr>
						<tr>
					        <td style="border-bottom:1px solid #ccc; display:none;" class="obs"><strong>Observação:</strong></td>
					        <td colspan="3" style="border-bottom:1px solid #ccc; display:none;" class="obs" id="observacaoReadOnly"></td>
	        			</tr>
	    			</table>
	    		</td>
	  		</tr>
	  		<tr>
			    <td>&nbsp;</td>
			    <td>&nbsp;</td>
	    		<td style="border-bottom:1px solid #ccc;" class="tit"><strong>Observação:</strong></td>
	  		</tr>
	  		<tr>
			    <td>&nbsp;</td>
			    <td>&nbsp;</td>
			    <td>
			    	<textarea class="tit" name="observacao" id="observacao" cols="90" rows="5"></textarea>
			    </td>
	  		</tr>
	  		<tr>
			    <td>&nbsp;</td>
			    <td>&nbsp;</td>
			    <td>
			    	<span class="bt_novos" id="btObs" title="Incluir Observação">
			    		<a href="javascript:;" onclick="ConferenciaEncalhe.gravaObs();">
			    			<img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" />Incluir Observação
			    		</a>
			    	</span>
			    </td>
	  		</tr>
		</table>
	</fieldset>
</div>

<div id="dialog-logado" title="Box de Encalhe" style="display:none;">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idModalBoxRecolhimento" name="messageDialog"/>
	</jsp:include>
	
	<fieldset style="width: 410px">
		<legend>Box de Encalhe</legend>
	    <label>Confirma Box de Encalhe: </label>
	    <select name="boxes" style="width:150px;" id="boxLogado">
			<c:forEach var="item" items="${boxes}">
		       <option value="${item.id}">${item.nome}</option>
		    </c:forEach>
	    </select>
	</fieldset>
</div>

<div id="dialog-pesquisar" title="Pesquisa de Produtos" style="display:none;">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idModalPesquisarProdutos" name="messageDialog"/>
	</jsp:include>
	
	<fieldset style="width: 510px">
		<legend>Pesquisar Produto</legend>
		<table width="100%" border="0" cellspacing="1" cellpadding="2">
			<tr>
				<td width="44%">Digite o Código / Nome do Produto:</td>
				<td width="56%">
            		<input name="pesq_prod" type="text" id="pesq_prod" style="width:200px; float:left; margin-right:5px;"/>
	                <span class="classPesquisar">
	                	<a href="javascript:;" onclick="mostrar_produtos();">&nbsp;</a>
	                </span>
            	</td>
          	</tr>
        </table>
    </fieldset>
</div>

<div id="dialog-dadosNotaFiscal" title="Dados da Nota Fiscal" style="display:none;">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idModalDadosNotaFiscal" name="messageDialog"/>
	</jsp:include>
	
	<fieldset>
    	<legend>Nota Fiscal</legend>
        	<table width="830" border="0" cellspacing="1" cellpadding="1" style="color:#666;">
          		<tr>
		            <td width="133">Núm. Nota Fiscal:</td>
		            <td width="307" id="numeroNotaFiscalExibir"></td>
		            <td width="106">Série:</td>
		            <td width="111" id="serieExibir"></td>
          		</tr>
          		<tr>
		            <td>Data:</td>
		            <td id="dataExibir"></td>
		            <td>Valor Total R$:</td>
		            <td id="valorTotalNotaFiscalExibir"></td>
          		</tr>
          		<tr>
		            <td>Chave de Acesso:</td>
		            <td colspan="3" id="chaveAcessoExibir"></td>
          		</tr>
        	</table>
    </fieldset>
	<br clear="all" />
    <br />
    
	<fieldset>
        <legend>Produtos Nota Fiscal</legend>
        <div style="overflow: auto; height: 250px; width: 828px; border: 1px #EEEEEE solid;">
			<table class="pesqProdutosNotaGrid gridTeste" style="width: 811px;" id="dadosGridConferenciaEncalheFinalizar">
				<tr class="header_table">
					<td style="width: 50px; text-align: left;">Código</td>
					<td style="width: 100px; text-align: left;">Produto</td>
					<td style="width: 50px; text-align: center;">Edição</td>
					<td style="width: 70px; text-align: left;">Dia</td>
					<td style="width: 70px; text-align: left;">Qtde. info</td>
					<td style="width: 101px; text-align: left;">Qtde. Recebida</td>
					<td style="width: 80px; text-align: right;" nowrap="nowrap">Preço Capa R$</td>
					<td style="width: 80px; text-align: right;" nowrap="nowrap">Preço Desconto R$</td>
					<td style="width: 60px; text-align: right;">Total R$</td>
					<td style="width: 30px; text-align: center;">Ação</td>
				</tr>
			</table>
		</div>
        
		<table width="800" border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td style="width: 50px;"></td>
				<td style="width: 100px;"></td>
				<td style="width: 50px;"></td>
				<td style="width: 70px;"><strong>Total:</strong></td>
				<td style="width: 70px; text-align: center;" id="somatorioQtdInformada"></td>
				<td style="width: 101px; text-align: center;" id="somatorioQtdRecebida"></td>
				<td style="width: 80px;"></td>
				<td style="width: 80px;"></td>
				<td style="width: 60px; text-align: right;" id="somatorioTotal"></td>
				<td style="width: 30px;"></td>
		  	</tr>
		</table>
	</fieldset>

</div>

<div id="dialog-notaFiscal" title="Dados da Nota Fiscal" style="display:none;">
	<p><strong>Insira os dados da Nota Fiscal</strong></p>
	<table width="670" border="0" cellspacing="1" cellpadding="1">
		<tr>
			<td width="119">Núm. Nota Fiscal:</td>
		    <td width="321"><input type="text" id="numNotaFiscal" style="width:200px;" /></td>
		    <td width="106">Série:</td>
		    <td width="111"><input type="text" id="serieNotaFiscal" style="width:80px;" /></td>
  		</tr>
  		<tr>
		    <td>Data:</td>
		    <td><input type="text" id="dataNotaFiscal" style="width:80px;" /></td>
		    <td>Valor Total R$:</td>
		    <td>
		    	<input type="text" id="valorNotaFiscal" style="width:80px; text-align:right;" />
		    </td>
		</tr>
		<tr>
			<td>NF-e:</td>
			<td>
				<table width="300" border="0" cellspacing="0" cellpadding="0">
					<tr>
				        <td width="26"><input type="radio" name="radioNFE" id="radioNFEsim" value="S" /></td>
				        <td width="71" valign="bottom">Sim</td>
				        <td width="20"><input type="radio" name="radioNFE" id="radioNFEnao" value="N" /></td>
				        <td width="183" valign="bottom">Não</td>
	      			</tr>
	    		</table>
	    	</td>
	    	<td>&nbsp;</td>
	    	<td>&nbsp;</td>
		</tr>
		<tr>
		    <td>Chave de Acesso:</td>
	    	<td colspan="3">
	    		<input type="text" id="chaveAcessoNFE" style="width:510px;" />
	    	</td>
    	</tr>
	</table>
</div>

<div id="dialog-alert" title="Nota Fiscal">
	<fieldset style="width: 410px;">
		<legend>Nota Fiscal</legend>
	    <p>Existe Nota Fiscal para esta Cota?</p>
	</fieldset>
</div>

<div id="dialog-reabertura" title="Reabertura" style="display: none;">
	<fieldset style="width: 310px;">
		<legend>Nota Fiscal</legend>
	    <p>Já existe conferencia de encalhe para esta cota.<br/>
	    Efetuar reabertura?</p>
	</fieldset>
</div>

<div id="dialog-excluir-conferencia" title="Conferência" style="display: none;">
	<fieldset style="width: 350px;">
		<legend>Excluir Conferência</legend>
	    <p>Confirma a exclusão dessa conferência?</p>
	</fieldset>
</div>