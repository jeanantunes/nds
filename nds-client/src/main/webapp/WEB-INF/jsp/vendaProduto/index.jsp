<head>

<script type="text/javascript" src="scripts/endereco.js"></script>
<script type="text/javascript" src="scripts/vendaProduto.js"></script>
<script language="javascript" type="text/javascript">

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

<div class="corpo">  
    <br clear="all"/>
    <br />
   
    <div class="container">    
		<fieldset class="classFieldset">
		  <legend> Pesquisar Produto</legend>
		  <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		      <tr>
		        <td width="46">Código:</td>
		        <td colspan="3"><input type="text" name="textfield5" id="codigo" style="width:80px;" onchange="vendaProdutoController.pesquisarPorCodigoProduto('#codigo', '#produto', false,
										undefined,
										undefined);" /></td>
		        <td width="51">Produto:</td>
		        <td width="164"><input type="text" name="publica" id="produto" style="width:150px;" onkeyup="vendaProdutoController.autoCompletarPorNome('#produto', false);"
								onblur="vendaProdutoController.pesquisarPorNome('#codigo', '#produto', false,
									undefined,
									undefined);" /></td>
		        <td width="45">Edição:</td>
		        <td width="95"><input type="text" name="edicoes" id="edicoes" style="width:80px;"/></td>
		        <td width="67">Fornecedor:</td>
              	<td colspan="2">
					<select id="idFornecedor" name="idFornecedor" style="width:200px;">
					    <option value="-1"  selected="selected">Todos</option>
					    <c:forEach items="${listaFornecedores}" var="fornecedor">
					      		<option value="${fornecedor.key}">${fornecedor.value}</option>	
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
		            <!--<span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup();"><img src="../images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>-->
		            <span class="bt_novos" title="Gerar Arquivo">
		            	<a href="${pageContext.request.contextPath}/lancamento/vendaProduto/exportar?fileType=XLS&tipoExportacao=principal">
		            		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
		            		Arquivo
		            	</a>
		            </span>
					<span class="bt_novos" title="Imprimir">
						<a href="${pageContext.request.contextPath}/lancamento/vendaProduto/exportar?fileType=PDF&tipoExportacao=principal">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
							Imprimir
						</a>
					</span>	        
		      </fieldset>
		      <div class="linha_separa_fields">&nbsp;</div>
	      </div>
    </div>
</div> 
</body>