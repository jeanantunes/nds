<form id="idRedistribuicaoParciais">

	<jsp:include page="../messagesDialog.jsp">
			<jsp:param value="dialog-redistribuicao" name="messageDialog"/>
	</jsp:include>

<div id="dialog-redistribuicao" title="Parcial" style="display:none">

	<fieldset style="width:500px;">

		<legend>Dados do Período</legend>
		
		<table width="500" border="0" cellpadding="2" cellspacing="1">
			<tr>
			  <td width="30"><strong>Código:</strong></td>
			  <td width="50" id="parcial-codigoProdutoRed" ></td>
			  <td width="30"><strong>Produto:</strong></td>
			  <td width="194" id="parcial-nomeProdutoRed"></td>
			</tr>
			<tr>	
			  <td width="30"><strong>Edição:</strong></td>
			  <td width="50" id="parcial-numEdicaoRed"></td>
			  <td width="20"><strong>Período:</strong></td>
			  <td width="55"id="parcial-numeroPeriodoRed"></td>
			</tr>
			<tr>
			  <td width="40"><strong>Lançamento:</strong></td>
			  <td width="30"id="parcial-dataLancamentoRed"></td>
			  <td width="40"><strong>Recolhimento:</strong></td>
			  <td width="30"id="parcial-dataRecolhimentoRed"></td>
			</tr>
		</table>
		
	</fieldset>
	
	<br />
	<br clear="all" />
	<br />
	
	<fieldset style="width:500px;">

		<legend>Manutenção de Redistribuição</legend>
		
		<table class="parcial-parciaisRedistribuicaoGrid"></table>
		
	</fieldset>
	<br />
	<br clear="all" />
	<br />
	 <span id="btnIncluirRedistribuicao" class="bt_novos">
		<a href="javascript:;" isEdicao="true" onclick="ParciaisController.popupNovaRedistribuicao(true);" rel="tipsy" title="Incluir Nova Redistribuição ">
			<img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" alt="Incluir Redistribuição" />
		</a>
	</span>
	
	
</div>

</form>

<form id="idNovaRedistribuicaoParciais">
	
	<div id="dialog-nova-redistribuicao" title="Parcial" style="display:none">
		
		<fieldset style="width:260px;">

		<legend>Nova Redistribuição</legend>
		
		<table width="200" border="0" cellpadding="2" cellspacing="1">
			<tr>
			  <td width="40"><strong>Lançamento:</strong></td>
			  <td><input  id="parcial-lancamentoNovaRed" type="text" name="parcial-lancamentoNovaRed" style="width:80px;"/></td>
			</tr>
	    </table>

	</fieldset>

	</div>
	
</form>

<form id="idRedistribuicaoExcluir">
	<div id="dialog-excluir-redistribuicao" title="Excluir Lançamento" style="display:none">
	  <p>Confirma a exclusão deste Lançamento?</p>
	</div>
</form>