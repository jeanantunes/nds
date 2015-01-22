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

<form id="form-detalhes">
<div id="dialog-detalhes" title="Saldo de Edições Fechadas com Saldo">
<fieldset>
	<legend>Extrato da Edição</legend>
    <table class="detalheEdicoesFechadasGrid"></table>
    <table width="670" border="0" cellspacing="2" cellpadding="2">
      <tr>
        <td width="445">
        <span class="bt_novos"><a href="${pageContext.request.contextPath}/estoque/extratoEdicao/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /></a></span>
        <span class="bt_novos"><a href="${pageContext.request.contextPath}/estoque/extratoEdicao/exportar?fileType=PDF" rel="tipsy" title="Imprimir"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" /></a></span>
        </td>
        <td width="139" align="right"><strong>Saldo em Estoque:</strong></td>
        <td width="66" align="right"><span id="saldoEstoque" /></td>
      </tr>
    </table>
</fieldset>
</div>
</form>
	<div class="areaBts">
		<div class="area">
			<span class="bt_arq"><a href="${pageContext.request.contextPath}/estoque/edicoesFechadas/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /></a></span>
			<span class="bt_arq"><a href="${pageContext.request.contextPath}/estoque/edicoesFechadas/exportar?fileType=PDF" rel="tipsy" title="Imprimir"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" /></a></span>
		</div>
	</div>
	<div class="linha_separa_fields">&nbsp;</div>
   <fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
    <legend>Pesquisar Edições Fechadas com Saldo</legend>
	<table width="950" border="0" cellspacing="2" cellpadding="2" class="filtro">
        <tr>
   	        <td width="58">Período:</td>
   	        <td width="123">
   	        	<input type="text" name="filtro.dataDe" value="filtro.dataDe" id="edicoes-fechadas-dataDe" style="width:80px;" />
   	        </td>
   	        <td width="28" align="center">Até</td>
   	        <td width="123">
   	        	<input type="text" name="filtro.dataAte" value="filtro.dataAte" id="edicoes-fechadas-dataAte" style="width:80px;" />
   	        </td>
   	        <td width="76">Fornecedor:</td>
   	        <td width="211">
   	        	<select name="filtro.fornecedor" id="fornecedor" style="width:200px;">
   	          		<option selected="selected" value="Todos">Todos</option>
			  		<c:forEach items="${fornecedores}" var="fornecedor">
						<option value="${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</option>
			  		</c:forEach> 
            	</select>
            </td>
   	        <td width="287"><span class="bt_novos"><a href="javascript:;" id="btnPesquisar"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
         </tr>
    </table>
   </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       <div class="grids" style="display:none;">
	       <fieldset class="fieldGrid">
	       	  <legend>Edições Fechadas com Saldo</legend>
			  	<table class="consultaEdicoesFechadasGrid"></table>
	            <table width="100%" border="0" cellspacing="2" cellpadding="2">
				  <tr>
				    <td width="70%">
				    &nbsp;
				    </td>
				    <td width="5%">&nbsp;</td>
				    <td width="9%" align="right"><strong>Total:</strong></td>
				    <td width="9%" align="right"><span id="totalEdicoesFechadasSaldo" /></td>
				    <td width="7%">&nbsp;</td>
				  </tr>
				</table>
	      </fieldset>
      </div>
      
</body>
</html>