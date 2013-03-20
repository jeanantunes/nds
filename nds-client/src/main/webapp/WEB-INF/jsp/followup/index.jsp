<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/followUpSistema.js"></script>
</head>

<body>


	<div class="areaBts">
		<div class="area">
		
		<div id="btnsNegociacao" class="divButtonsWrapper" >
		<div class="areaBtsNegociacao">
			<span class="bt_arq" title="Gerar Arquivo">
			    <a href="${pageContext.request.contextPath}/followup/exportar?fileType=XLS&tipoExportacao=negociacao" rel="bandeira">
			    	<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
		        </a>
			</span> 
			<span class="bt_arq" title="Imprimir"> 
				<a href="${pageContext.request.contextPath}/followup/exportar?fileType=PDF&tipoExportacao=negociacao" >
				    <img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
			</span>
		</div>
		</div>
		
		<div id="btnsChamadao" class="divButtonsWrapper" style="display:none;">
	    <div id="botoesArquivoChamadao">
	        <span class="bt_arq" title="Gerar Arquivo">                      
				<a href="${pageContext.request.contextPath}/followup/exportar?fileType=XLS&tipoExportacao=chamadao">
		     		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
	        </span>
	        <span class="bt_arq" title="Imprimir">                      
		        <a href="${pageContext.request.contextPath}/followup/exportar?fileType=PDF&tipoExportacao=chamadao">
		       		<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
	        </span>  
        </div> 
		</div>
		
		<div id="btnsStatusCota" class="divButtonsWrapper" style="display:none;">
		<div id="botoesArquivoAlteracaoStatusCota">
	        <span class="bt_arq" title="Gerar Arquivo">                      
				<a href="${pageContext.request.contextPath}/followup/exportar?fileType=XLS&tipoExportacao=alteracao">
		 			<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
	        </span>
	        <span class="bt_arq" title="Imprimir">                      
		        <a href="${pageContext.request.contextPath}/followup/exportar?fileType=PDF&tipoExportacao=alteracao">
		 			<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
	        </span> 
        </div>
		</div>
		
		<div id="btnsAtualizacaoCadastral" class="divButtonsWrapper" style="display:none;">
		<div id="botoesArquivoAtualizacaoCadastral">
	        <span class="bt_arq" title="Gerar Arquivo">                      
				<a href="${pageContext.request.contextPath}/followup/exportar?fileType=XLS&tipoExportacao=atualizacao">
		        	<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
		        </a>
	        </span>
	        <span class="bt_arq" title="Imprimir">                      
		        <a href="${pageContext.request.contextPath}/followup/exportar?fileType=PDF&tipoExportacao=atualizacao">
		       		<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
		        </a>
	        </span>
        </div>
		</div>
		
		<div id="btnsArquivoPendencia" class="divButtonsWrapper" style="display:none;">
		<div id="botoesArquivoPendencia">
			<span class="bt_arq" title="Gerar Arquivo">                      
				<a href="${pageContext.request.contextPath}/followup/exportar?fileType=XLS&tipoExportacao=pendenciaNFE">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
			</span>
			<span class="bt_arq" title="Imprimir">                      
				<a href="${pageContext.request.contextPath}/followup/exportar?fileType=PDF&tipoExportacao=pendenciaNFE">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
	        </span> 
        </div>   
		</div>
		
		<div id="btnsParcial" class="divButtonsWrapper" style="display:none;">
		<div class="areaBtsParcial">
			<span class="bt_arq" title="Gerar Arquivo">
			    <a href="${pageContext.request.contextPath}/followup/exportar?fileType=XLS&tipoExportacao=cadastroParcial" rel="bandeira">
			        <img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
                </a>
			</span> 
			<span class="bt_arq" title="Imprimir"> 
				<a href="${pageContext.request.contextPath}/followup/exportar?fileType=PDF&tipoExportacao=cadastroParcial">
				    <img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
			</span>
		</div>
		</div>
		
		</div>
	</div>

    <br clear="all"/>
    <br />
    <br />
    	<div id="divGeral" >
      	<fieldset class="classFieldset">
   	    <legend> Follow Up do Sistema </legend>
        <div id="tab-followup">
            <ul>
                <li><a href="#tabNegocia" 			onclick="followUpSistemaController.toggleButtons('btnsNegociacao');">Negociação</a></li>
                <li><a href="#tabChamadao" 			onclick="followUpSistemaController.toggleButtons('btnsChamadao');">Chamadão</a></li>
                <li><a href="#tabAlteracao" 		onclick="followUpSistemaController.toggleButtons('btnsStatusCota');">Alteração de Status Cota</a></li>
                <li><a href="#tabAtualizacao"		onclick="followUpSistemaController.toggleButtons('btnsAtualizacaoCadastral');">Atualização Cadastral</a></li>
                <li><a href="#tabPendencia" 		onclick="followUpSistemaController.toggleButtons('btnsArquivoPendencia');">Pend&ecirc;ncias NF-e Encalhe</a></li>
                <li><a href="#tabCadastroParcial" 	onclick="followUpSistemaController.toggleButtons('btnsParcial');">Cadastro Parcial</a></li>
            </ul>
            
            <div id="tabNegocia" style="height:auto !important; width:981px !important;">
            <br />
               <fieldset style="width:960px!important;">
               	    <legend>Negociação</legend>
                    <table class="negociacaoGrid"></table>
               </fieldset>
            </div>
            <div id="tabChamadao">
            <br />
                <fieldset style="width:960px!important;">
               	<legend>Chamadão</legend>
                <table class="chamadaoGrid"></table>
                          
               </fieldset>
            </div>
            <div id="tabAlteracao">
            <br />
                <fieldset style="width:960px!important;">
               	<legend>Alteração de Status Cota</legend>
                <table class="alteracaoStatusGrid"></table>
                
               </fieldset>
            </div>
            <div id="tabAtualizacao">
            <br />
               <fieldset style="width:960px!important;">
               	<legend>Atualização Cadastral</legend>
                <table class="atualizacaoCadastralGrid"></table>
               </fieldset>
            </div>            
            <div id="tabPendencia">
            <br />
               <fieldset style="width:960px!important;">
               	<legend>Pend&ecirc;ncias NF-e Encalhe</legend>
                <table class="pendenciasGrid"></table>
               </fieldset>
            </div>
            
             <div id="tabCadastroParcial">
             	<br />
               <fieldset style="width:960px!important;">
               
               	<legend>Cadastro Parcial</legend>
                          	
                <table class="atualizacaoCadastralParcialGrid"></table>
     
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