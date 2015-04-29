<form id="form-salvar">
<div id="dialog-salvar" title="Salvar Confer&ecirc;ncia" style="display:none;">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idModalConfirmarSalvarConf" name="messageDialog"/>
	</jsp:include>
	
	<fieldset style="width: 415px;">
        <legend>Salvar Confer&ecirc;ncia</legend>
        <p>Confima a Confer&ecirc;ncia de Encalhe?</p>
    </fieldset>

</div>
</form>

<iframe src="" id="download-iframe" style="display:none;"></iframe>

<form id="form-outros-valores">
<div id="dialog-outros-valores" title="Outros Valores" style="display:none;">
	<fieldset>
        <legend>Outros Valores</legend>
        <table class="outrosVlrsGrid"></table>
    </fieldset>
</div>
</form>

<form id="form-detalhe-publicacao">
<div id="dialog-detalhe-publicacao" title="Box de Encalhe" style="display:none;">
	<fieldset>
		<legend>Detalhes do Produto</legend>
		<table width="703" border="0" cellspacing="0" cellpadding="2" >
	  		<tr>
			    <td width="117">
			    	<div id="imagemProduto">
			    		<img src="" id="img" width="117" height="145" alt="" />
			    	</div>
			    </td>
			    <td width="1">&nbsp;</td>
			    <td width="573" valign="top">
			    	<table width="574" border="0" cellspacing="1" cellpadding="2">
	      				<tr>
					        <td width="108" style="border-bottom:1px solid #ccc;"><strong>Nome:</strong></td>
					        <td width="174" style="border-bottom:1px solid #ccc;" id="nomeProdutoDetalhe"></td>
					        <td width="120" style="border-bottom:1px solid #ccc;"><strong>Pre&ccedil;o Capa:</strong></td>
					        <td width="151" style="border-bottom:1px solid #ccc;" id="precoCapaDetalhe"></td>
						</tr>
						<tr>
					        <td style="border-bottom:1px solid #ccc;"><strong>Chamada Capa:</strong></td>
					        <td style="border-bottom:1px solid #ccc;" id="chamadaCapa"></td>
					        <td style="border-bottom:1px solid #ccc;"><strong>Pre&ccedil;o Desconto:</strong></td>
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
					        <td style="border-bottom:1px solid #ccc;"><strong>Pacote Padr&atilde;o:</strong></td>
					        <td style="border-bottom:1px solid #ccc;" id="conferencia-pacotePadrao"></td>
						</tr>
						<tr>
					        <td style="border-bottom:1px solid #ccc; display:none;" class="obs"><strong>Observa&ccedil;&atilde;o:</strong></td>
					        <td colspan="3" style="border-bottom:1px solid #ccc; display:none;" class="obs" id="observacaoReadOnly"></td>
	        			</tr>
	    			</table>
	    		</td>
	  		</tr>
	  		<tr>
			    <td>&nbsp;</td>
			    <td>&nbsp;</td>
	    		<td style="border-bottom:1px solid #ccc;" class="tit"><strong>Observa&ccedil;&atilde;o:</strong></td>
	  		</tr>
	  		<tr>
			    <td>&nbsp;</td>
			    <td>&nbsp;</td>
			    <td>
			    	<textarea idEdicao="true" class="tit" name="observacao" id="observacao" cols="80" rows="5"></textarea>
			    </td>
	  		</tr>
	  		<tr>
			    <td>&nbsp;</td>
			    <td>&nbsp;</td>
			    <td>
			    	<span class="bt_novos" id="btObs" title="Incluir Observa&ccedil;&atilde;o">
			    		<a idEdicao="true" href="javascript:;" onclick="ConferenciaEncalhe.gravaObs();">
			    			<img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" />Incluir Observa&ccedil;&atilde;o
			    		</a>
			    	</span>
			    </td>
	  		</tr>
		</table>
	</fieldset>
</div>
</form>

<form id="form-logado">
<div id="dialog-logado" title="Box de Encalhe" style="display:none;">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idModalBoxRecolhimento" name="messageDialog"/>
	</jsp:include>
	
	<fieldset style="width: 410px">
		<legend>Box de Encalhe</legend>
	    <label>Confirma Box de Encalhe: </label>
	    <select name="boxes" style="width:150px;" id="boxLogado">
			<c:forEach var="item" items="${boxes}">
		       <option value="${item.id}" <c:if test="${idBoxLogado == item.id}">selected</c:if>>${item.nome}</option>
		    </c:forEach>
	    </select>
	</fieldset>
</div>
</form>

<form id="form-pesquisar" >
<div id="dialog-pesquisar" title="Pesquisa de Produtos" style="display:none;">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idModalPesquisarProdutos" name="messageDialog"/>
	</jsp:include>
	
	<fieldset style="width: 510px">
		<legend>Pesquisar Produto</legend>
		<table width="100%" border="0" cellspacing="1" cellpadding="2">
			<tr>
				<td width="44%">Digite o C&oacute;digo / Nome do Produto:</td>
				<td width="56%">
            		<input name="pesq_prod" type="text" 
            			   id="pesq_prod" style="width:200px; float:left; margin-right:5px;"
            			   onkeypress="return disableEnterKey(event);"/>
            	</td>
          	</tr>
        </table>
    </fieldset>
</div>
</form>

<form id="form-dadosNotaFiscal">
<div id="dialog-dadosNotaFiscal" title="Dados da Nota Fiscal" style="display:none;">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idModalDadosNotaFiscal" name="messageDialog"/>
	</jsp:include>
	
	<fieldset>
    	<legend>Nota Fiscal</legend>
        	<table width="830" border="0" cellspacing="1" cellpadding="1" style="color:#666;">
          		<tr>
		            <td width="133">N&uacute;. Nota Fiscal:</td>
		            <td width="307" id="numeroNotaFiscalExibir"></td>
		            <td width="106">S&eacute;rie:</td>
		            <td width="111" id="serieExibir"></td>
          		</tr>
          		<tr>
		            <td>Data:</td>
		            <td id="conferencia-dataExibir"></td>
		            <td>Valor Total R$:</td>
		            <td id="conferencia-valorTotalNotaFiscalExibir"></td>
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
			<table class="pesqProdutosNotaGrid gridTeste" style="width: 811px;" id="conferencia-dadosGridConferenciaEncalheFinalizar">
				<tr class="header_table">
					<td style="width: 50px; text-align: left;">C&oacute;digo</td>
					<td style="width: 100px; text-align: left;">Produto</td>
					<td style="width: 50px; text-align: center;">Edi&ccedil;&atilde;o</td>
					<td style="width: 70px; text-align: left;">Dia</td>
					<td style="width: 70px; text-align: left;">Qtde. info</td>
					<td style="width: 101px; text-align: left;">Qtde. Recebida</td>
					<td style="width: 80px; text-align: right;" nowrap="nowrap">Pre&ccedil;o Capa R$</td>
					<td style="width: 80px; text-align: right;" nowrap="nowrap">Pre&ccedil;o Desconto R$</td>
					<td style="width: 60px; text-align: right;">Total R$</td>
					<td style="width: 30px; text-align: center;">A&ccedil;&atilde;o</td>
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
</form>

<form id="form-notaFiscal">
<div id="dialog-notaFiscal" title="Dados da Nota Fiscal" style="display:none;">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="dialog-notaFiscal" name="messageDialog"/>
	</jsp:include>
	
	<p><strong>Insira os dados da Nota Fiscal</strong></p>
	<table width="670" border="0" cellspacing="1" cellpadding="1">
		<tr>
			<td width="119">N&uacute;. Nota Fiscal:</td>
		    <td width="321"><input isEdicao="true" type="text" id="numNotaFiscal" style="width:200px;" /></td>
		    <td width="106">S&eacute;rie:</td>
		    <td width="111"><input isEdicao="true" type="text" id="serieNotaFiscal" style="width:80px;" /></td>
  		</tr>
  		<tr>
		    <td>Data:</td>
		    <td><input isEdicao="true" type="text" id="dataNotaFiscal" style="width:80px;" /></td>
		    <td>Valor Total R$:</td>
		    <td>
		    	<input isEdicao="true" type="text" id="conferencia-valorNotaFiscal" style="width:80px; text-align:right;" />
		    </td>
		</tr>
		<tr>
			<td>NF-e:</td>
			<td>
				<table width="300" border="0" cellspacing="0" cellpadding="0">
					<tr>
				    
				        <td width="26"><input isEdicao="true" type="radio" checked="checked" name="radioNFE" onchange="ConferenciaEncalhe.mostrarChaveAcesso()" id="radioNFEsim" value="S"/></td>
				        <td width="71" valign="bottom">Sim</td>
				        <td width="20"><input isEdicao="true" type="radio"  onchange="ConferenciaEncalhe.mostrarChaveAcesso()" name="radioNFE" id="radioNFEnao" value="N" /></td>
				        <td width="183" valign="bottom">N&atilde;o</td>
	      			
	      			</tr>
	    		</table>
	    	</td>
	    	<td>&nbsp;</td>
	    	<td>&nbsp;</td>
		</tr>
		
		<tr>
			<td colspan="4">
				<div id="divForChaveAcessoNFE">
					<table>
					    <td>
					    	Chave de Acesso:
					    </td>
				    	<td colspan="3">
				    		<input isEdicao="true" type="text" id="chaveAcessoNFE" style="width:510px;" />
				    	</td>
					</table>
				</div>
			</td>
    	</tr>
	</table>
</div>
</form>

<form id="form-alert">
<div id="dialog-alert" title="Nota Fiscal">
	<fieldset style="width: 410px;">
		<legend>Nota Fiscal</legend>
	    <p>Existe Nota Fiscal para esta Cota?</p>
	</fieldset>
</div>
</form>

<form id="form-reabertura">
<div id="dialog-reabertura" title="Reabertura" style="display: none;">
	<fieldset style="width: 310px;">
		<legend>Nota Fiscal</legend>
	    <p>J&aacute; existe conferencia de encalhe para esta cota.<br/>
	    Efetuar reabertura?</p>
	</fieldset>
</div>
</form>

<form id="form-excluir-conferencia">
<div id="dialog-excluir-conferencia" title="Confer&ecirc;ncia" style="display: none;">
	<fieldset style="width: 350px;">
		<legend>Excluir Confer&ecirc;ncia</legend>
	    <p>Confirma a exclus&atilde;o dessa confer&ecirc;ncia?</p>
	</fieldset>
</div>
</form>
<form id="form-confirmar-regerar-cobranca">
	<div id="dialog-confirmar-regerar-cobranca" title="Regerar Cobran&ccedil;a" style="display: none;">
		<fieldset>
			<legend>Regerar cobran&ccedil;a?</legend>
			<p id="msgRegerarCobranca"></p>
		</fieldset>
	</div>
</form>
<form id="form-confirmar">
	<div id="dialog-confirmar" title="Confirma">
		<fieldset>
			<legend>Confirma</legend>
			<p id="msgConfirmar"></p>
		</fieldset>
	</div>
</form>

<form id="form-conferencia-nao-salva">
<div id="dialog-conferencia-nao-salva" title="Confer&Ecirc;ncia de Encalhe" style="display: none;">
	<fieldset style="width: 400px;">
		<legend>Confer&ecirc;ncia de Encalhe da Cota</legend>
	    <p>Confer&ecirc;ncia de encalhe da cota atual ainda n&atilde;o foi salva.</br> 
	    Deseja realmente fechar esta aba?</p>
	</fieldset>
</div>
</form>

<form id="form-conferencia-nao-salva-troca-de-cota">
<div id="dialog-conferencia-nao-salva-troca-de-cota" title="Confer&Ecirc;ncia de Encalhe" style="display: none;">
	<fieldset style="width: 400px;">
		<legend>Confer&ecirc;ncia de Encalhe da Cota</legend>
	    <p>Confer&ecirc;ncia de encalhe da cota atual ainda n&atilde;o foi salva.</br> 
	    Deseja iniciar a confer&ecirc;ncia de outra cota mesmo assim?</p>
	</fieldset>
</div>
</form>

<form id="form-autenticar-supervisor">
<div id="dialog-autenticar-supervisor" title="Permiss&atilde;o de Supervisor Necess&aacute;ria" style="display:none">
	<fieldset style="width: 360px;">
		<legend id="msgSupervisor"></legend>
		<div align="center">
			<table>
				<tr>
					<td>Usu&aacute;rio:</td>
					<td>
						<input type="password" name="failAutoFill" style="display:none" />
						<input type="text" id="inputUsuarioSup"/>
					</td>
				</tr>
				<tr>
					<td>Senha:</td>
					<td><input type="password" id="inputSenha" /></td>
				</tr>
			</table>
		</div>
	</fieldset>
</div>
</form>