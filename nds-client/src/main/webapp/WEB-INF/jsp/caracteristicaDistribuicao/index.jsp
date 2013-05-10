<%@ page contentType="text/html" pageEncoding="UTF-8" %>  
<html>
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/caracteristicaDistribuicao.js"></script>
<script type="text/javascript">
$(function(){
	caracteristicaDistribuicaoController.init();
});

</script> 
</head>
<body>

<br clear="all"/>
    <br/>
    
    
    <fieldset class="classFieldset">
   	    <legend>Características de Distribuição</legend>
	        <table width="950" border="0" cellpadding="2" cellspacing="5" class="filtro">
	         	
	              <tr>
		            <td width="52">Código:</td>
		            <td width="86"><input type="text" name="codigoProduto" id="codigoProduto"  style="width:80px;" onkeydown="onlyNumeric(event);" class="inputField" /></td>
		          </tr>
		          <tr>
		            <td width="152">Classificação:</td>
		            <td width="86">
			            <select id="classificacao" class="inputField">
			            	<option value=""></option>
			            	<option value=""></option>
			            	<c:forEach var="classificacao" items="${classificacoes}">
			            		<option value="<c:out value="${classificacao.descricao}"/>"><c:out value="${classificacao.descricao}"/></option>
			            	</c:forEach>
			            </select>
		            </td>
		            <td width="48">Segmento:</td>
		            <td width="206">
		            	 <select id="segmento" class="inputField"> 
			            	<option value=""></option>
			            	<c:forEach var="segmento" items="${segmentos}">
			            		<option value="<c:out value="${segmento.descricao}"/>"><c:out value="${segmento.descricao}"/></option>
			            	</c:forEach>
			            </select>
		            </td>
		            <td width="48">Brinde:</td>
		            <td width="206">
		            	<select id="brinde" class="inputField">
			            	<option></option>
			            	<c:forEach var="brinde" items="${brindes}">
			            		<c:if test="${brinde.descricao !=null && brinde.descricao !=''}">
			            			<option value="<c:out value="${brinde.descricao}"/>"><c:out value="${brinde.descricao}"/></option>
			            		</c:if>
			            	</c:forEach>
			            </select>
		            </td>
		          </tr>
		          <tr>
	         	   <td width="48">Produto:</td>
		            <td width="206"><input type="text" name="nomeProduto" id="nomeProduto"  style="width:200px;" class="inputField"></input></td>
		            
	 	            <td align="right"><input type="checkbox" name="checkPublicacao" id="checkPublicacaoExato"/></td><td><label for="checkPublicacaoExato"> Exato</label></td>
		          </tr>
		          <tr>
		           <td width="70">Nome Editor:</td>
		            <td width="206"><input type="text" name="nomeEditor" id="nomeEditor" style="width:200px;" class="inputField"></input></td>
		            <td align="right"><input type="checkbox" name="checkEditor" id="checkEditorExato" /></td><td><label for="checkEditorExato"> Exato</label></td>
		          </tr>
		          <tr>
		          	<td width="148">Chamada de capa:</td>
		             <td width="206"><input type="text" name="chamadaCapa" id="chamadaCapa" style="width:200px;" class="inputField"></input>
		             <td align="right"><input type="checkbox" name="checkChamadaCapa" id="checkChamadaCapaExato" ></td><td><label for="checkChamadaCapaExato"> Exato</label></td>
	 	          </tr>
	 	           <tr>
		             <td width="148" >Faixa de Preço:  </td>
	 	             <td>De: <input type="text" name="faixaDe" id="faixaDe" onkeyup="return caracteristicaDistribuicaoController.moeda(this);" maxlength="8" style="width:50px;" class="inputField"/> Até:<input type="text" name="faixaAte" maxlength="8" id="faixaAte" onkeyup="return caracteristicaDistribuicaoController.moeda(this);"  style="width:50px;" class="inputField"/></td>
	 	             <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="caracteristicaDistribuicaoController.pesquisar();">Pesquisar</a></span></td>
	 	           </tr>
	        </table>
	        
	         <!-- dialog que contem a capa a ser exibida -->
			<div id="dialog-detalhes" title="Visualizando Produto" style="margin-right:0px!important; float:right!important;">
				<img src="images/loading.gif" id="loadingCapa"/>
				<img  width="235" height="314" id="imagemCapaEdicao" style="display:none"/>
			</div>
        
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>


		 <div class="grids" style="display:block;">
	       <div id="divPesquisaSimplesGrid"  style="display:none;">
		      <fieldset class="classFieldset">
		       	  <legend>Pesquisa</legend>
		        	<table id="pesquisaSimplesGrid" class="pesquisaSimplesGrid"></table> 
		             <span class="bt_novos" title="Gerar Arquivo" id="btGerarArquivoCota"><a href="${pageContext.request.contextPath}/distribuicao/caracteristicaDistribuicao/exportar?fileType=XLS&tipoExportacao=cota"><img src="images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
					<span class="bt_novos" title="Imprimir" id="btImprimirCota"><a href="${pageContext.request.contextPath}/distribuicao/caracteristicaDistribuicao/exportar?fileType=PDF&tipoExportacao=cota"><img src="images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
		      </fieldset>
   		   </div>
   		   
   		    <div id="divPesquisaDetalheGrid"  style="display:none;">
		      <fieldset class="classFieldset">
		       	  <legend>Pesquisa</legend>
		        
		        	<table id="pesquisaDetalheGrid" class="pesquisaDetalheGrid"></table> 
		             <span class="bt_novos" title="Gerar Arquivo" id="btGerarArquivoCaracDist"><a id="linkGerarArquivoCaracDist" href="${pageContext.request.contextPath}/distribuicao/caracteristicaDistribuicao/exportar?fileType=XLS&tipoExportacao=cota"><img src="images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
					<span class="bt_novos" title="Imprimir" id="btImprimirCaracDist"><a id="linkImprimirCaracDist" href="${pageContext.request.contextPath}/distribuicao/caracteristicaDistribuicao/exportar?fileType=PDF&tipoExportacao=cota"><img src="images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
		      </fieldset>
   		   </div>
   		   
   		  
    </div>
    
 </body> 
   
</html>
