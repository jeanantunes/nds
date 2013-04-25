<%@ page contentType="text/html" pageEncoding="UTF-8" %>  
<script type="text/javascript" src="scripts/caracteristicaDistribuicao.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<script type="text/javascript">
$(function(){
	caracteristicaDistribuicaoController.init();
});

</script> 

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
		            <td align="right"><input type="radio" name="radioPublicacao" id="radioPublicacaoInicio" value="1" checked="checked"/></td><td><label for="radioPublicacaoInicio"> Inicia com </label></td>
	 	            <td align="right"><input type="radio" name="radioPublicacao" id="radioPublicacaoContido" value="2"/></td><td><label for="radioPublicacaoContido"> Contem </label></td>
	 	            <td align="right"><input type="radio" name="radioPublicacao" id="radioPublicacaoExato" value="3"/></td><td><label for="radioPublicacaoExato"> Exato</label></td>
		          </tr>
		          <tr>
		           <td width="70">Nome Editor:</td>
		            <td width="206"><input type="text" name="nomeEditor" id="nomeEditor" style="width:200px;" class="inputField"></input></td>
		            <td align="right"><input type="radio" name="radioEditor" id="radioEditorInicio" value="1" checked="checked"/></td><td><label for="radioEditorInicio"> Inicia com </label></td>
	 	            <td align="right"><input type="radio" name="radioEditor" id="radioEditorContido" value="2"/></td><td><label for="radioEditorContido"> Contem </label></td>
	 	            <td align="right"><input type="radio" name="radioEditor" id="radioEditorExato" value="3"/></td><td><label for="radioEditorExato"> Exato</label></td>
		          </tr>
		          <tr>
		          	<td width="148">Chamada de capa:</td>
		             <td width="206"><input type="text" name="chamadaCapa" id="chamadaCapa" style="width:200px;" class="inputField"></input>
		             <td align="right"><input type="radio" name="radioChamadaCapa" id="radioChamadaCapaInicio" value="1" checked="checked"/></td><td><label for="radioChamadaCapaInicio" > Inicia com </label></td>
	 	            <td align="right"><input type="radio" name="radioChamadaCapa" id="radioChamadaCapaContido" value="2"/></td><td><label for="radioChamadaCapaContido"> Contem </label> </td>
	 	            <td align="right"><input type="radio" name="radioChamadaCapa" id="radioChamadaCapaExato" value="3"/></td><td><label for="radioChamadaCapaExato"> Exato</label></td>
	 	          </tr>
	 	           <tr>
		             <td width="148" >Faixa de Preço:  </td>
	 	             <td>De: <input type="text" name="faixaDe" id="faixaDe" onkeydown="onlyNumeric(event);" maxlength="5" style="width:50px;" class="inputField"/> Até:<input type="text" name="faixaAte" maxlength="5" id="faixaAte" onkeydown="onlyNumeric(event);"  style="width:50px;" class="inputField"/></td>
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
