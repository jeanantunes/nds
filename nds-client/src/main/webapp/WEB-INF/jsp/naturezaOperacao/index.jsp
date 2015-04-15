<head>
<title>Cadastro de Tipos de Notas</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/naturezaOperacao.js"></script>
<script type="text/javascript">

$(function(){
	cadastroTipoNotaController.init();
});

</script>

</head>

<body>
  
  <form id="pesquisar_tipo_nota_form">	
  <div class="areaBts">
  	<div class="area">
  		<span class="bt_arq"><a href="${pageContext.request.contextPath}/administracao/naturezaOperacao/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /></a></span>
    	<span class="bt_arq"><a href="${pageContext.request.contextPath}/administracao/naturezaOperacao/exportar?fileType=PDF" rel="tipsy" title="Imprimir"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" /></a></span>
  	</div>
  </div>
 <div class="linha_separa_fields">&nbsp;</div>
   <fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
 		<legend> Pesquisar Tipo de Nota</legend>
 		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
     		<tr>
       			<td width="64">Operação:</td>
         		<td width="215">
         			 <select name="operacao" id="operacaoID" style="width:200px;" class="campoDePesquisa">
					     <option selected="selected" value="">Todos</option>
					     <c:forEach var="operacao" items="${listaAtividades}">
								<option value="${operacao.key}">${operacao.value}</option>
					  	 </c:forEach>
					   </select>
         		</td>
	            <td width="74">Descrição:</td>
	            <td width="200"><input type="text" name="tipoNota" id="tipoNota" style="width:200px;" class="campoDePesquisa" /></td>
	         	<td width="411"><span id="btnPesquisar" class="bt_novos"><a href="javascript:;" class="botaoPesquisar" ><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
     		</tr>
   		</table>
	</fieldset>
	<div class="linha_separa_fields">&nbsp;</div>
	<div class="grids" style="display:none;">
		<fieldset class="fieldGrid">
	 	  	<legend>Tipos de Notas Cadastradas</legend>
	  		<table class="tiposNotasGrid"></table>
		</fieldset>
	</div>
   </form>
</body>
</html>