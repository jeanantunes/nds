<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/depara.js"></script>
<script type="text/javascript">

$(function(){
	deparaController.init();	
});
</script>
<style type="text/css">
	#dialog-depara{display:none;}
	#dialog-depara fieldset{width:570px!important;}
</style>

</head>

<body>
	

	<form action="/cadastro/depara/" id="excluir_depara_form">
	<div id="dialog-excluir" title="Excluir Depara">		
		<p>Confirma a exclus&atilde;o deste Depara?</p>
	</div>
	</form>
	
	<form action="/cadastro/depara/" id="novo_depara_form">
	<input type="hidden" name="depara.id" id="deparaId" style="width:80px;"/>
	<div id="dialog-novo" title="Incluir Novo Depara">  
		<jsp:include page="../messagesDialog.jsp" />  
	    <table width="356" border="0" cellpadding="2" cellspacing="1" class="filtro">
	            
	            <tr>
	              <td>FC:</td>
	              <td><input type="text" name="depara.fc" id="deparaFc" style="width:220px;"/></td>
	            </tr>
	            <tr>
	              <td>Dinap:</td>
	              <td><input type="text" name="depara.dinap" id="deparaDinap" style="width:220px;"/></td>
	            </tr>
	                     
          </table>
    </div>
	</form>
	
	
	<form action="/cadastro/depara/" id="pesquisar_depara_form">
	<div class="areaBts">
		<div class="area">
			<span class="bt_novos">
				<a href="javascript:;" id="btnNovo" rel="tipsy" title="Incluir Novo Depara">
					<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>
				</a>
			</span>
			<div id="fileExport" style="display: none; float:right;">
				<span class="bt_arq">
					<a href="${pageContext.request.contextPath}/cadastro/depara/exportarConsulta?fileType=PDF" rel="tipsy" title="Imprimir">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" border="0" />
					</a>
				</span>
				
				<span class="bt_arq">
					<a href="${pageContext.request.contextPath}/cadastro/depara/exportarConsulta?fileType=XLS"  rel="tipsy" title="Gerar Arquivo">
						<img src="${pageContext.request.contextPath}/images/ico_excel.png" border="0" />
					</a>
				</span>
			</div>
		</div>  		  		
	</div>
  	<div class="linha_separa_fields">&nbsp;</div>
	
	
	
	 <fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
   	    <legend> Pesquisar Depara</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="30">FC:</td>
                <td width="152"><input type="text" name="fc" id="pesquisaFc" style="width:130px;"/></td>
                <td width="77">Dinap:</td>
                <td width="152"><input type="text" name="dinap" id="pesquisaDinap" style="width:130px;"/></td>
              <td width="474"><span class="bt_novos"><a href="javascript:;" id="btnPesquisar"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:none;">
	      <fieldset class="fieldGrid">
	       	  <legend>Depara Cadastrados</legend>
	        	<table class="deparaGrid"></table>	
	      </fieldset>
      </div>
    </form>
</body>