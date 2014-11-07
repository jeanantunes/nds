<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">

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

	<div class="areaBts">
		<div class="area">
			<div class="divPesquisaSimplesGrid" style="display:none;">
				<span class="bt_arq" id="btGerarArquivoCota">
					<a id="linkGerarSimples" href="${pageContext.request.contextPath}/distribuicao/caracteristicaDistribuicao/exportarSimples?fileType=XLS&tipoExportacao=cota" rel="tipsy" title="Gerar Arquivo">
						<img src="images/ico_excel.png" hspace="5" border="0" />
					</a>
				</span>
				<span class="bt_arq" id="btImprimirCota">
					<a id="linkImprimirSimples" href="${pageContext.request.contextPath}/distribuicao/caracteristicaDistribuicao/exportarSimples?fileType=PDF&tipoExportacao=cota" rel="tipsy"  title="Imprimir">
						<img src="images/ico_impressora.gif" hspace="5" border="0" />
					</a>
				</span>
			</div>

			<div class="divPesquisaDetalheGrid"  style="display:none;">
				<span class="bt_arq"  id="btGerarArquivoCaracDist">
					<a id="linkGerarDetalhe" href="${pageContext.request.contextPath}/distribuicao/caracteristicaDistribuicao/exportarDetalhe?fileType=XLS&tipoExportacao=cota" rel="tipsy" title="Gerar Arquivo">
						<img src="images/ico_excel.png" hspace="5" border="0" />
					</a>
				</span>
				<span class="bt_arq"  id="btImprimirCaracDist">
					<a id="linkImprimirDetalhe" href="${pageContext.request.contextPath}/distribuicao/caracteristicaDistribuicao/exportarDetalhe?fileType=PDF&tipoExportacao=cota" rel="tipsy"  title="Imprimir">
						<img src="images/ico_impressora.gif" hspace="5" border="0" />
					</a>
				</span>

			</div>

		</div>
	</div>

	<br clear="all"/>
	<br/>
	<fieldset class="classFieldset">
		<legend>Características de Distribuição</legend>
		<table width="950" border="0" cellpadding="2" cellspacing="5" class="filtro">
			<tr>
				<td width="52">Código:</td>
				<td width="86">
					<input type="text" name="codigoProduto" id="codigoProduto"  style="width:80px;" onkeydown="onlyNumeric(event);" class="inputField" />
				</td>
				<td width="152">Classificação:</td>
				<td width="86">
					<select id="classificacao" class="inputField">
						<option value=""></option>
						<c:forEach var="classificacao" items="${classificacoes}">
							<option value="<c:out value="${classificacao.descricao}"/>">
							<c:out value="${classificacao.descricao}"/>
						</option>
					</c:forEach>
				</select>
			</td>
			<td width="48">Segmento:</td>
			<td width="206">
				<select id="segmento" class="inputField" style="width:100px;">
					<option value=""></option>
					<c:forEach var="segmento" items="${segmentos}">
						<option value="<c:out value="${segmento.descricao}"/>
						">
						<c:out value="${segmento.descricao}"/>
					</option>
				</c:forEach>
			</select>
		</td>
		<td width="48">Brinde:</td>
		<td width="70">
		<input type="checkbox" name="checkBrinde" id="checkIsBrinde" style="margin-right:0px;"/>
		</td>
</tr>
<tr></tr>
<tr>
	<td width="48">Produto:</td>
	<td width="206">
		<input type="text" name="nomeProduto" id="nomeProduto"  style="width:100px;" class="inputField"></input>
</td>
<td width="70">Nome Editor:</td>
<td width="206">
	<input type="text" name="nomeEditor" id="nomeEditor" style="width:100px;" class="inputField"></input>
</td>
<td width="200">Chamada de capa:</td>
<td width="206">
<input type="text" name="chamadaCapa" id="chamadaCapa" style="width:100px;" class="inputField"></input>
<td width="70">Exato</td>
<td width="70">
<input type="checkbox" name="checkPublicacao" id="checkPublicacaoExato" style="margin-right:0px;"/>
</td>
</tr>
<tr>
<td width="200" >Faixa de Preço:</td>
<td colspan="2" >
De:
<input type="text" name="faixaDe" id="faixaDe" onkeyup="return caracteristicaDistribuicaoController.moeda(this);" maxlength="8" style="width:50px;" class="inputField"/>
Até:
<input type="text" name="faixaAte" maxlength="8" id="faixaAte" onkeyup="return caracteristicaDistribuicaoController.moeda(this);"  style="width:50px;" class="inputField"/>
</td>
<td width="106" >
<span class="bt_pesquisar" style="width:35px;">
	<a href="javascript:;" onclick="caracteristicaDistribuicaoController.pesquisar();">Pesquisar</a>
</span>
</td>

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
<div class="divPesquisaSimplesGrid"  style="display:none;">
<fieldset class="classFieldset">
<legend>Pesquisa</legend>
<table id="pesquisaSimplesGrid" class="pesquisaSimplesGrid"></table>

</fieldset>
</div>

<div class="divPesquisaDetalheGrid"  style="display:none;">
<fieldset class="classFieldset">
<legend>Pesquisa</legend>
<table id="pesquisaDetalheGrid" class="pesquisaDetalheGrid"></table>

</fieldset>
</div>

<div id="modal-pesquisa-detalhe"  style="display:none;" title="Detalhes do Produto">
<div id="divPesquisaDetalheModal"  style="display:none;">
<table id="pesquisaDetalheGridModal" class="pesquisaDetalheGridModal"></table>
<span class="bt_novos"  id="btGerarArquivoCaracDistModal">
<a id="linkGerarModal" href="${pageContext.request.contextPath}/distribuicao/caracteristicaDistribuicao/exportarDetalhe?fileType=XLS&tipoExportacao=cota" title="Gerar Arquivo">
	<img src="images/ico_excel.png" hspace="5" border="0" />
	Arquivo
</a>
</span>
<span class="bt_novos"  id="btImprimirCaracDistModal">
<a id="linkImprimirModal" href="${pageContext.request.contextPath}/distribuicao/caracteristicaDistribuicao/exportarDetalhe?fileType=PDF&tipoExportacao=cota" title="Imprimir">
	<img src="images/ico_impressora.gif" hspace="5" border="0" />
	Imprimir
</a>
</span>
</div>
</div>

</div>

</body>

</html>