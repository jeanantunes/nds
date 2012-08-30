<head>
<script type="text/javascript" src="scripts/fornecedor.js"></script>
<script language="javascript" type="text/javascript">
$(function(){
	fornecedorController.init();
});
</script>
<style>
	.fornecedorController-diasFunc label, .fornecedorController-finceiro label{ vertical-align:super;}
	.fornecedorController-validade, #fornecedorController-dialog-excluir{display:none;}
</style>
</head>

<body>

	<form id="form-dialog-excluir">
	<div id="fornecedorController-dialog-excluir" title="Excluir Fornecedor">
		<p>Confirma a exclusão deste Fornecedor?</p>
	</div>
	</form>
	
    
	<div id="fornecedorController-effect" style="padding: 0 .7em;"
		 class="ui-state-highlight ui-corner-all">
		<p>
			<span style="float: left; margin-right: .3em;"
				class="ui-icon ui-icon-info">
			</span> 
		</p>
	</div>
	<div class="areaBts">
		<div class="area">
			<span class="bt_novos" title="Novo">
           	<a href="javascript:;" onclick="fornecedorController.novoFornecedor();" rel="tipsy" title="Incluir Novo Fornecedor">
           		<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>
           	</a>
        </span>
		</div>
	</div>
	<div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="fieldFiltro">
   	    <legend> Pesquisar Fornecedor</legend>
   	    <form id="fornecedorController-formularioPesquisaFornecedores">
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="81">Razão Social:</td>
              <td colspan="3">
              	<input type="text" name="filtroConsultaFornecedor.razaoSocial" id="fornecedorController-filtroConsultaFornecedorRazaoSocial" style="width:180px;"/>
              </td>
                <td width="41">CNPJ:</td>
                <td width="138"><input type="text" name="filtroConsultaFornecedor.cnpj" id="fornecedorController-filtroConsultaFornecedorCnpj" style="width:130px;"/></td>
                <td width="93">Nome Fantasia:</td>
                <td width="155"><input type="text" name="filtroConsultaFornecedor.nomeFantasia" id="fornecedorController-filtroConsultaFornecedorNomeFantasia" style="width:150px;"/></td>
              <td width="216"><span class="bt_novos"><a href="javascript:;" onclick="fornecedorController.pesquisarFornecedores();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
            </tr>
          </table>
		</form>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:block;">
	      <fieldset class="fieldGrid">
	       	  <legend>Fornecedores Cadastrados</legend>
	        	<div class="fornecedorController-grids" style="display:none;">
	        		<table id="fornecedorController-fornecedoresGrid"></table>
	        	</div>
	        
	       </fieldset>
       </div>
      
	<jsp:include page="novoFornecedor.jsp" />
	
</body>