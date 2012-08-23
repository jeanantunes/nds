<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselects-0.3.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cadastroTipoDesconto.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cadastroTipoDescontoDistribuidor.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cadastroTipoDescontoCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cadastroTipoDescontoProduto.js"></script>

<script language="javascript" type="text/javascript">

var pesquisaCotaTipoDescontoCota = new PesquisaCota(tipoDescontoController.workspace);

var pesquisaProdutoTipoDescontoCota = new PesquisaProduto(tipoDescontoController.workspace);

$(function() {		
	tipoDescontoController.init();
	descontoDistribuidorController.init();
	descontoCotaController.init();
	descontoProdutoController.init(pesquisaCotaTipoDescontoCota);
});

</script>

</head>

<body>
	
	<form action="/administracao/cadastroTipoNota" id="dialog-excluirCota_form">
		<div id="dialog-excluirCota" title="Atenção" style="display:none">
			<p>Confirmar exclusão Desconto ?</p>
		</div>
	</form>
	
	<!-- Modal de inclusão de novo desconto Geral  -->
	<form action="/administracao/cadastroTipoNota" id="dialog-geral_form">
		<jsp:include page="novoDescontoGeral.jsp"/>
	</form>	
	
	<!-- Modal de inclusão de novo desconto Especifico  -->
	<form action="/administracao/cadastroTipoNota" id="dialog-especifico_form">
		<jsp:include page="novoDescontoEspecifico.jsp"/>
	</form>
	
	<!-- Modal de inclusão de novo desconto Produto  -->
	<jsp:include page="novoDescontoProduto.jsp"/>
	
	
	<form action="/administracao/cadastroTipoNota" id="dialog-fornecedores_form">
		<div id="dialog-fornecedores" title="Fornecedores" style="display:none;">
			<fieldset style="width:350px!important;">
	    		<legend>Fornecedores</legend>
	        	<table class="lstFornecedoresGrid"></table>
	    	</fieldset>
		</div>
	</form>

	<form action="/administracao/cadastroTipoNota" id="dialog_consulta_tipo_desconto_form">
 
      <fieldset class="fieldFiltro">
   	    <legend> Pesquisar Tipo de Desconto Cota</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="20"><input type="radio" name="radio" id="radioGeral" value="radio" onclick="tipoDescontoController.mostra_geral();" /></td>
                <td width="47">Geral</td>
                <td width="20"><input type="radio" name="radio" id="radioEspecifico" value="radio" onclick="tipoDescontoController.mostra_especifico();"  /></td>
                <td width="65">Específico</td>
                <td width="20"><input type="radio" name="radio" id="radioProduto" value="radio" onclick="tipoDescontoController.mostra_produto();"  /></td>
                <td width="48">Produto</td>
                <td width="585">
                <div class="especifico" style="display: none">
	                
	                <label style="width:auto!important;">Cota:</label>
	                <input name="numCotaPesquisa" 
			           		   id="numCotaPesquisa" 
			           		   type="text"
			           		   maxlength="11"
			           		   style="width:70px;float:left;"
			           		   onchange="pesquisaCotaTipoDescontoCota.pesquisarPorNumeroCota('#numCotaPesquisa', '#descricaoCotaPesquisa',false,
			           	  											null, 
			           	  											null);" />
			    	<label style="width:auto!important;">Nome:</label>
			        <input  name="descricaoCotaPesquisa" 
					      		 id="descricaoCotaPesquisa" 
					      		 type="text" 
					      		 class="nome_jornaleiro" 
					      		 maxlength="255"
					      		 style="width:200px;float:left;"
					      		 onkeyup="pesquisaCotaTipoDescontoCota.autoCompletarPorNome('#descricaoCotaPesquisa');" 
					      		 onblur="pesquisaCotaTipoDescontoCota.pesquisarPorNomeCota('#numCotaPesquisa', '#descricaoCotaPesquisa',false,
													      			null,
													      			null);" />
				                
                </div>
                
                <div class="produto" style="display: none">
	                <label style="width:auto!important;">Código:</label>
	                <input type="text" name="codigoPesquisa" id="codigoPesquisa" maxlength="255" 
					   	   style="width:80px; float:left;"
					       onblur="pesquisaProdutoTipoDescontoCota.pesquisarPorCodigoProduto('#codigoPesquisa', '#produtoPesquisa', false,
								   undefined,
								   undefined);"/>
	                <label style="width:auto!important;">Produto:</label>
	                <input type="text" name="produtoPesquisa" id="produtoPesquisa" maxlength="255" 
						   style="width:160px; float:left;"
						   onkeyup="pesquisaProdutoTipoDescontoCota.autoCompletarPorNomeProduto('#produtoPesquisa', false);"
						   onblur="pesquisaProdutoTipoDescontoCota.pesquisarPorNomeProduto('#codigoPesquisa', '#produtoPesquisa', false,
								   undefined,
								   undefined);" />
	                
                </div>
                </td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="tipoDescontoController.pesquisar();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
      <div class="grids" style="display:none;">
      
      <fieldset class="fieldGrid" id="tpoGeral" style="display:none;">
       	  <legend>Tipos de Desconto Geral</legend>
        
            <div id="idExportacaoGeral">
	       		
	       		<table class="tiposDescGeralGrid"></table>
	       		
	       		<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;">
	       			<a href="${pageContext.request.contextPath}/financeiro/tipoDescontoCota/exportar?fileType=XLS&tipoDesconto=GERAL">
						<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
						Arquivo
					</a>
	       		</span>
	             <span class="bt_novos" title="Imprimir">
	             	<a href="${pageContext.request.contextPath}/financeiro/tipoDescontoCota/exportar?fileType=PDF&tipoDesconto=GERAL">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
						Imprimir
					</a>
	             </span>
             </div>
           <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="descontoDistribuidorController.popup_geral();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
   
      </fieldset>
      
      
      <fieldset class="fieldGrid" id="tpoEspecifico" style="display:none;">
       	  <legend>Tipos de Desconto Específico</legend>
       
       		<div id="idExportacaoEspecifico">
				
				<table class="tiposDescEspecificoGrid"></table>
				
				<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;">
	       			<a href="${pageContext.request.contextPath}/financeiro/tipoDescontoCota/exportar?fileType=XLS&tipoDesconto=ESPECIFICO">
						<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
						Arquivo
					</a>
	       		</span>
	             <span class="bt_novos" title="Imprimir">
	             	<a href="${pageContext.request.contextPath}/financeiro/tipoDescontoCota/exportar?fileType=PDF&tipoDesconto=ESPECIFICO">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
						Imprimir
					</a>
	             </span>
	        </div>
           <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="descontoCotaController.popup_especifico();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
   
      </fieldset>
      
      
      <fieldset class="fieldGrid" id="tpoProduto" style="display:none;">
       	
       	<legend>Tipos de Desconto Produto</legend>
       		
       	<div id="idExportacaoProduto">	
       		
       		<table class="tiposDescProdutoGrid"></table>
			
			<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;">
       			<a href="${pageContext.request.contextPath}/financeiro/tipoDescontoCota/exportar?fileType=XLS&tipoDesconto=PRODUTO">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					Arquivo
				</a>
       		</span>
             <span class="bt_novos" title="Imprimir">
             	<a href="${pageContext.request.contextPath}/financeiro/tipoDescontoCota/exportar?fileType=PDF&tipoDesconto=PRODUTO">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
					Imprimir
				</a>
             </span>
        </div>
        <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="descontoProdutoController.popup_produto();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
   
      </fieldset>
    </div>
</form>

</body>