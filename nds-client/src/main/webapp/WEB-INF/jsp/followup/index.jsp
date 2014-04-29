<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/followUpSistema.js"></script>
</head>

<body>
<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">

	<div class="areaBts">
		<div class="area">
		
		<div id="btnsNegociacao" class="divButtonsWrapper" >
		<div class="areaBtsNegociacao">
			<span class="bt_arq" title="Gerar Arquivo">
			    <a href="${pageContext.request.contextPath}/followup/imprimirNegociacao?fileType=XLS" rel="bandeira">
			    	<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
		        </a>
			</span> 
			<span class="bt_arq" title="Imprimir"> 
				<a href="${pageContext.request.contextPath}/followup/imprimirNegociacao?fileType=PDF" >
				    <img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
			</span>
		</div>
		</div>
		
		<div id="btnsChamadao" class="divButtonsWrapper" style="display:none;">
	    <div id="botoesArquivoChamadao">
	        <span class="bt_arq" title="Gerar Arquivo">                      
				<a href="${pageContext.request.contextPath}/followup/imprimirChamadao?fileType=XLS">
		     		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
	        </span>
	        <span class="bt_arq" title="Imprimir">                      
		        <a href="${pageContext.request.contextPath}/followup/imprimirChamadao?fileType=PDF">
		       		<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
	        </span>  
        </div> 
		</div>
		
		<div id="btnsDistribuidor" class="divButtonsWrapper" style="display:none;">
	    <div id="botoesDistribuidor">
	        <span class="bt_arq" title="Gerar Arquivo">                      
				<a href="${pageContext.request.contextPath}/followup/exportar?fileType=XLS&tipoExportacao=distribuicao">
		     		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
	        </span>
	        <span class="bt_arq" title="Imprimir">                      
		        <a href="${pageContext.request.contextPath}/followup/exportar?fileType=PDF&tipoExportacao=distribuicao">
		       		<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
	        </span>  
        </div> 
		</div>
		
		<div id="btnsStatusCota" class="divButtonsWrapper" style="display:none;">
		<div id="botoesArquivoAlteracaoStatusCota">
	        <span class="bt_arq" title="Gerar Arquivo">                      
				<a href="${pageContext.request.contextPath}/followup/imprimirAlteracaoStatusCota?fileType=XLS">
		 			<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
	        </span>
	        <span class="bt_arq" title="Imprimir">                      
		        <a href="${pageContext.request.contextPath}/followup/imprimirAlteracaoStatusCota?fileType=PDF">
		 			<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
	        </span> 
        </div>
		</div>
		
		<div id="btnsAtualizacaoCadastral" class="divButtonsWrapper" style="display:none;">
		<div id="botoesArquivoAtualizacaoCadastral">
	        <span class="bt_arq" title="Gerar Arquivo">                      
				<a href="${pageContext.request.contextPath}/followup/imprimirAtualizacaoCadastral?fileType=XLS">
		        	<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
		        </a>
	        </span>
	        <span class="bt_arq" title="Imprimir">                      
		        <a href="${pageContext.request.contextPath}/followup/imprimirAtualizacaoCadastral?fileType=PDF">
		       		<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
		        </a>
	        </span>
        </div>
		</div>
		
		<div id="btnsArquivoPendencia" class="divButtonsWrapper" style="display:none;">
		<div id="botoesArquivoPendencia">
			<span class="bt_arq" title="Gerar Arquivo">                      
				<a href="${pageContext.request.contextPath}/followup/imprimirPendenciasNFe?fileType=XLS">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
			</span>
			<span class="bt_arq" title="Imprimir">                      
				<a href="${pageContext.request.contextPath}/followup/imprimirPendenciasNFe?fileType=PDF">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
	        </span> 
        </div>   
		</div>
		
		<div id="btnsParcial" class="divButtonsWrapper" style="display:none;">
		<div class="areaBtsParcial">
			<span class="bt_arq" title="Gerar Arquivo">
			    <a href="${pageContext.request.contextPath}/followup/imprimirCadastroParcial?fileType=XLS" rel="bandeira">
			        <img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
                </a>
			</span> 
			<span class="bt_arq" title="Imprimir"> 
				<a href="${pageContext.request.contextPath}/followup/imprimirCadastroParcial?fileType=PDF">
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
                <li><a href="#tabNegocia" 			onclick="followUpSistemaController.toggleButtons('btnsNegociacao', 'tabNegocia');">Negociação</a></li>
                <li><a href="#tabChamadao" 			onclick="followUpSistemaController.toggleButtons('btnsChamadao', 'tabChamadao');">Chamadão</a></li>
                <li><a href="#tabDistribuicao"      onclick="followUpSistemaController.toggleButtons('noBtns', 'tabDistribuicao');">Distribuição</a></li>
                <li><a href="#tabAlteracao" 		onclick="followUpSistemaController.toggleButtons('btnsStatusCota', 'tabAlteracao');">Alteração de Status Cota</a></li>
                <li><a href="#tabAtualizacao"		onclick="followUpSistemaController.toggleButtons('btnsAtualizacaoCadastral', 'tabAtualizacao');">Atualização Cadastral</a></li>
                <li><a href="#tabPendencia" 		onclick="followUpSistemaController.toggleButtons('btnsArquivoPendencia', 'tabPendencia');">Pend&ecirc;ncias NF-e Encalhe</a></li>
                <li><a href="#tabCadastroParcial" 	onclick="followUpSistemaController.toggleButtons('btnsParcial', 'tabCadastroParcial');">Cadastro Parcial</a></li>
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

				<div id="tabDistribuicao">
					<fieldset style="width: 880px !important;">
						<legend>Distribuição</legend>
						<table class="distribuicaoGrid"></table>
						<div id="botoesArquivoDistribuicao">
							<span class="bt_novos" title="Gerar Arquivo"> <a
								href="${pageContext.request.contextPath}/followup/exportar?fileType=XLS&tipoExportacao=distribuicao">
									<img
									src="${pageContext.request.contextPath}/images/ico_excel.png"
									hspace="5" border="0" /> Arquivo
							</a>
							</span> <span class="bt_novos" title="Imprimir"> <a
								href="${pageContext.request.contextPath}/followup/exportar?fileType=PDF&tipoExportacao=distribuicao">
									<img
									src="${pageContext.request.contextPath}/images/ico_impressora.gif"
									hspace="5" border="0" /> Imprimir
							</a>
							</span>
						</div>
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