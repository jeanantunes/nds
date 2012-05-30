<head>
<title>Edições Fechadas com Saldo</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/edicoesFechadas.js"></script>
<script type="text/javascript">
	$(function(){
		edicoesFechadasController.init("${pageContext.request.contextPath}");
	});
</script>
<style type="text/css">
#dialog-detalhes fieldset {width:700px!important;}
</style>
</head>

<body>

<div id="dialog-detalhes" title="Saldo de Edições Fechadas com Saldo">
<fieldset>
	<legend>Extrato da Edição</legend>
    <table class="detalheEdicoesFechadasGrid"></table>
    <table width="670" border="0" cellspacing="2" cellpadding="2">
      <tr>
        <td width="445">
        <span class="bt_novos" title="Gerar Arquivo"><a href="${pageContext.request.contextPath}/estoque/extratoEdicao/exportar?fileType=XLS"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
        <span class="bt_novos" title="Imprimir"><a href="${pageContext.request.contextPath}/estoque/extratoEdicao/exportar?fileType=PDF"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
        </td>
        <td width="139" align="right"><strong>Saldo em Estoque:</strong></td>
        <td width="66" align="right"><span id="saldoEstoque" /></td>
      </tr>
    </table>
</fieldset>
</div>
<div class="corpo">
    <div class="container">
    <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Lançamento de Faltas e Sobras < evento > com < status >.</b></p>
	</div>
   <fieldset class="classFieldset">
    <legend>Pesquisar Edições Fechadas com Saldo</legend>
	<table width="950" border="0" cellspacing="2" cellpadding="2" class="filtro">
        <tr>
   	        <td width="58">Período:</td>
   	        <td width="123"><input type="text" name="filtro.dataDe" value="filtro.dataDe" id="dataDe" style="width:80px;" /></td>
   	        <td width="28" align="center">Até</td>
   	        <td width="123"><input type="text" name="filtro.dataAte" value="filtro.dataAte" id="dataAte" style="width:80px;" /></td>
   	        <td width="81">Fornecedor:</td>
   	        <td width="389"><select name="filtro.fornecedor" id="fornecedor" style="width:280px;">
   	          <option selected="selected"></option>
   	          <option value="Todos">Todos</option>
			  <c:forEach items="${fornecedores}" var="fornecedor">
					<option value="${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</option>
			  </c:forEach> 
            </select></td>
   	        <td width="104"><span class="bt_pesquisar"><a href="javascript:;" id="btnPesquisar">Pesquisar</a></span></td>
         </tr>
    </table>
   </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       <fieldset class="classFieldset">
       	  <legend>Edições Fechadas com Saldo</legend>
        <div class="grids" style="display:none;">
		  	<table class="consultaEdicoesFechadasGrid"></table>
            <table width="100%" border="0" cellspacing="2" cellpadding="2">
			  <tr>
			    <td width="70%">
			    <span class="bt_novos" title="Gerar Arquivo"><a href="${pageContext.request.contextPath}/estoque/edicoesFechadas/exportar?fileType=XLS"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
			    <span class="bt_novos" title="Imprimir"><a href="${pageContext.request.contextPath}/estoque/edicoesFechadas/exportar?fileType=PDF"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
			    </td>
			    <td width="5%">&nbsp;</td>
			    <td width="9%" align="right"><strong>Total:</strong></td>
			    <td width="9%" align="right"><span id="totalEdicoesFechadasSaldo" /></td>
			    <td width="7%">&nbsp;</td>
			  </tr>
			</table>
		</div>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
    </div>
</div>
</body>
</html>
