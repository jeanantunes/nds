<head>
<title>Cadastro de Tipos de Notas</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cadastroTipoNota.js"></script>
<script type="text/javascript">

$(function(){
	cadastroTipoNotaController.init();
});

</script>

</head>

<body>
	
	<div class="corpo">
    	<div class="container">
      	  <form id="pesquisar_tipo_nota_form">	
          <fieldset class="classFieldset">
   	    		<legend> Pesquisar Tipo de Nota</legend>
        		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            		<tr>
              			<td width="55">Operação:</td>
                		<td width="176">
                			 <select name="operacao" id="operacaoID" style="width:150px;" class="campoDePesquisa">
						      <option selected="selected" value="">Todos</option>
						      <c:forEach var="operacao" items="${listaAtividades}">
										<option value="${operacao.key}">${operacao.value}</option>
							  </c:forEach>
						    </select>
                		</td>
		                <td width="75">Tipo de Nota:</td>
		                <td width="511"><input type="text" name="tipoNota" id="tipoNota" style="width:200px;" class="campoDePesquisa" /></td>
		              	<td width="107"><span id="btnPesquisar" class="bt_pesquisar"><a href="javascript:;" class="botaoPesquisar" >Pesquisar</a></span></td>
            		</tr>
          		</table>
      		</fieldset>
      		<div class="linha_separa_fields">&nbsp;</div>
	      	<fieldset class="classFieldset">
	       	  	<legend>Tipos de Notas Cadastradas</legend>
	        	<div class="grids" style="display:none;">
	        		<table class="tiposNotasGrid"></table>
	            	<div class="linha_separa_fields">&nbsp;</div>
	       			<span class="bt_novos" title="Gerar Arquivo"><a href="${pageContext.request.contextPath}/administracao/cadastroTipoNota/exportar?fileType=XLS"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
	             	<span class="bt_novos" title="Imprimir"><a href="${pageContext.request.contextPath}/administracao/cadastroTipoNota/exportar?fileType=PDF"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
	        	</div>
	      	</fieldset>
          </form>
    	</div>
	</div> 
</body>
</html>
