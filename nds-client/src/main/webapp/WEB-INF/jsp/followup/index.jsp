<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/followUpSistema.js"></script>
</head>

<body>

    <br clear="all"/>
    <br />
    	<div id="divGeral" >
      <fieldset class="classFieldset">
   	    <legend> Follow Up do Sistema </legend>
        <div id="tab-followup">
        
            <ul>
                <li><a href="#tabNegocia">Negociação</a></li>
                <li><a href="#tabChamadao">Chamadão</a></li>
                <li><a href="#tabDistribuicao">Distribuição</a></li>
                <li><a href="#tabAlteracao">Alteração de Status Cota</a></li>
                <li><a href="#tabAtualizacao">Atualização Cadastral</a></li>
                <li><a href="#tabPendencia">Pend&ecirc;ncias NF-e Encalhe</a></li>
                <li><a href="#tabCadastroParcial">Cadastro Parcial</a></li>
                
                
            </ul>
            <div id="tabNegocia" style="height:auto">
               <fieldset style="width:880px!important;">
               	  <legend>Negociação</legend>
                    <table class="negociacaoGrid"></table>
                    <div id="botoesArquivoNegociacao">
                     <span class="bt_novos" title="Gerar Arquivo">                      
						<a href="${pageContext.request.contextPath}/followup/exportar?fileType=XLS&tipoExportacao=negociacao">
         					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
         				Arquivo
						</a>
                  </span>
                  <span class="bt_novos" title="Imprimir">                      
                      <a href="${pageContext.request.contextPath}/followup/exportar?fileType=PDF&tipoExportacao=negociacao">
         					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
         				Imprimir
						</a>
                  </span>  
                  </div>            
               </fieldset>
            </div>
            <div id="tabChamadao">
                <fieldset style="width:880px!important;">
               	<legend>Chamadão</legend>
                <table class="chamadaoGrid"></table>
                 <div id="botoesArquivoChamadao">
                  <span class="bt_novos" title="Gerar Arquivo">                      
						<a href="${pageContext.request.contextPath}/followup/exportar?fileType=XLS&tipoExportacao=chamadao">
         					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
         				Arquivo
						</a>
                  </span>
                  <span class="bt_novos" title="Imprimir">                      
                      <a href="${pageContext.request.contextPath}/followup/exportar?fileType=PDF&tipoExportacao=chamadao">
         					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
         				Imprimir
						</a>
                  </span>  
                  </div>              
               </fieldset>
            </div>
            <div id="tabDistribuicao">
               <fieldset style="width:880px!important;">
               	<legend>Distribuição</legend>
                <table class="distribuicaoGrid"></table>
                <div id="botoesArquivoDistribuicao">
                  <span class="bt_novos" title="Gerar Arquivo">                      
						<a href="${pageContext.request.contextPath}/followup/exportar?fileType=XLS&tipoExportacao=distribuicao">
         					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
         				Arquivo
						</a>
                  </span>
                  <span class="bt_novos" title="Imprimir">                      
                      <a href="${pageContext.request.contextPath}/followup/exportar?fileType=PDF&tipoExportacao=distribuicao">
         					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
         				Imprimir
						</a>
                  </span> 
                  </div>   
               </fieldset>
            </div>
            <div id="tabAlteracao">
                <fieldset style="width:880px!important;">
               	<legend>Alteração de Status Cota</legend>
                <table class="alteracaoStatusGrid"></table>
                <div id="botoesArquivoAlteracaoStatusCota">
                 <span class="bt_novos" title="Gerar Arquivo">                      
						<a href="${pageContext.request.contextPath}/followup/exportar?fileType=XLS&tipoExportacao=alteracao">
         					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
         				Arquivo
						</a>
                  </span>
                  <span class="bt_novos" title="Imprimir">                      
                      <a href="${pageContext.request.contextPath}/followup/exportar?fileType=PDF&tipoExportacao=alteracao">
         					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
         				Imprimir
						</a>
                  </span> 
                  </div>
               </fieldset>
            </div>
            <div id="tabAtualizacao">
               <fieldset style="width:880px!important;">
               	<legend>Atualização Cadastral</legend>
                <table class="atualizacaoCadastralGrid"></table>
                <div id="botoesArquivoAtualizacaoCadastral">
                  <span class="bt_novos" title="Gerar Arquivo">                      
						<a href="${pageContext.request.contextPath}/followup/exportar?fileType=XLS&tipoExportacao=atualizacao">
         					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
         				Arquivo
						</a>
                  </span>
                  <span class="bt_novos" title="Imprimir">                      
                      <a href="${pageContext.request.contextPath}/followup/exportar?fileType=PDF&tipoExportacao=atualizacao">
         					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
         				Imprimir
						</a>
                  </span>
                  </div>
               </fieldset>
            </div>            
            <div id="tabPendencia">
               <fieldset style="width:880px!important;">
               	<legend>Pend&ecirc;ncias NF-e Encalhe</legend>
                <table class="pendenciasGrid"></table>
                <div id="botoesArquivoPendencia">
                  <span class="bt_novos" title="Gerar Arquivo">                      
						<a href="${pageContext.request.contextPath}/followup/exportar?fileType=XLS&tipoExportacao=pendenciaNFE">
         					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
         				Arquivo
						</a>
                  </span>
                  <span class="bt_novos" title="Imprimir">                      
                      <a href="${pageContext.request.contextPath}/followup/exportar?fileType=PDF&tipoExportacao=pendenciaNFE">
         					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
         				Imprimir
						</a>
                  </span>    
               </fieldset>
            </div>
            
             <div id="tabCadastroParcial">
               <fieldset style="width:880px!important;">
               	<legend>Cadastro Parcial</legend>
                <table class="atualizacaoCadastralParcialGrid"></table>
                <div id="botoesArquivoCadastroParcial">
                  <span class="bt_novos" title="Gerar Arquivo">                      
						<a href="${pageContext.request.contextPath}/followup/exportar?fileType=XLS&tipoExportacao=cadastroParcial">
         					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
         				Arquivo
						</a>
                  </span>
                  <span class="bt_novos" title="Imprimir">                      
                      <a href="${pageContext.request.contextPath}/followup/exportar?fileType=PDF&tipoExportacao=cadastroParcial">
         					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
         				Imprimir
						</a>
                  </span> 
                  </div>   
               </fieldset>
            </div>
            
            <br clear="all" />
            <br />

		</div>
        <br clear="all" />	
      </fieldset>
      </div>
      <div class="linha_separa_fields">&nbsp;</div>

<script>
	$(function(){
		followUpSistemaController.init();
	});
</script>
</body>
</html>