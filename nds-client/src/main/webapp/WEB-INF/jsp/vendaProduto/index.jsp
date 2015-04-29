<head>

<script type="text/javascript" src="scripts/endereco.js"></script>
<script type="text/javascript" src="scripts/vendaProduto.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/autoCompleteCampos.js"></script>

<script language="javascript" type="text/javascript">

var pesquisaProduto = new PesquisaProduto();
var pesquisaCota = new PesquisaCota();


$(function(){
	vendaProdutoController.init();
});

</script>
<style type="text/css">
#dialog-detalhes fieldset{width:750px!important;}
</style>
</head>

<body>
	<form id="form-detalhes">
	<div id="dialog-detalhes" title="Detalhes do Produto">
     <fieldset>
        <table class="detalhesVendaGrid"></table>
         <span class="bt_novos" title="Gerar Arquivo">
         	<a href="${pageContext.request.contextPath}/lancamento/vendaProduto/exportar?fileType=XLS&tipoExportacao=popup">
         		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
         		Arquivo
         	</a>
         </span>

		<span class="bt_novos" title="Imprimir">
			<a href="${pageContext.request.contextPath}/lancamento/vendaProduto/exportar?fileType=PDF&tipoExportacao=popup">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				Imprimir
			</a>
		</span>
     </fieldset>
   
    </div>
    </form>
    
    <div class="areaBts">
		<div class="area">
   	
   			<span class="bt_arq" >
  				<a href="${pageContext.request.contextPath}/lancamento/vendaProduto/exportar?fileType=XLS&tipoExportacao=principal" rel="tipsy" title="Gerar Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
    		</span>

			<span class="bt_arq" >
				<a href="${pageContext.request.contextPath}/lancamento/vendaProduto/exportar?fileType=PDF&tipoExportacao=principal" rel="tipsy" title="Imprimir">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0"/>
				</a>
			</span>
   	
		</div>
   	</div>

<div class="corpo">  
    <br clear="all"/>
    <br />
   
    <div class="container">    
		<fieldset class="classFieldset fieldFiltroItensNaoBloqueados">
		  <legend> Pesquisar Produto</legend>
		  <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		      <tr>
		      	<td width="44">Código:</td>
              	<td colspan="3">
		        	<input type="text" name="codigo" id="codigo" style="width:80px;" />
				</td>
				
		        <td width="49">Produto:</td>
              	<td width="159">
		        	<input type="text" name="produto" id="produto" style="width:150px;" />
				</td>
		        <td width="75">Edição:</td>
              	<td width="90">
		        	<input type="text" name="edicoes" id="edicoes" style="width:80px;"/>
		        </td>
		        <td width="73">Fornecedor:</td>
              	<td width="230">
					<select id="idFornecedor" name="idFornecedor" style="width:200px;">
					    <option value="-1"  selected="selected">Todos</option>
					    <c:forEach items="${listaFornecedores}" var="fornecedor">
					      		<option value="${fornecedor.key}">${fornecedor.value}</option>	
					    </c:forEach>
					</select>
       			</td>
       			<td width="104">&nbsp;</td>
            	</tr>
            <tr>
              <td>Cota:</td>
              <td colspan="3">
           		<input type="text" name="numeroCota" id="numeroCota" style="width:80px;"
           			   onchange="pesquisaCota.pesquisarPorNumeroCota('#numeroCota', '#nomeCota');"/>
              </td>
              <td>Nome:</td>
              <td>
           		<input type="text" name="nomeCota" id="nomeCota" style="width:150px;"/>
              </td>
              <td>Classificação:</td>
              <td colspan="3">
           		<select name="select2" id="venda-produto-selectClassificacao" style="width:200px;">
               		<option selected="selected">Selecione...</option>
               		<c:forEach items="${listaClassificacao}" var="classificacao">
								<option value="${classificacao.key}">${classificacao.value}</option>
				  	</c:forEach>
           		</select>
              </td>
       					        
		        <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="vendaProdutoController.cliquePesquisar();">Pesquisar</a></span></td>
		    </tr>
			</table>		
		</fieldset>
		
		  <div class="grids" style="display:none;">
		      <div class="linha_separa_fields">&nbsp;</div>
		      <fieldset class="classFieldset">
		        	<table class="parciaisGrid"></table>
		      </fieldset>
		      <div class="linha_separa_fields">&nbsp;</div>
	      </div>
    </div>
</div> 
</body>