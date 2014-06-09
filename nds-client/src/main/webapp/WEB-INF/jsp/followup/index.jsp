<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/followUpSistema.js"></script>

<style type="text/css">
#followup-dialog-nfe, #followup-dialog-dadosNotaFiscal {
	display: none;
}

.dados,.dadosFiltro,.nfes {
	display: none;
}

#dialog-novo,#dialog-alterar,#dialog-excluir,#dialog-rejeitar,#dialog-confirm
	{
	display: none;
	font-size: 12px;
}

fieldset label {
	width: auto;
	margin-bottom: 0px !important;
}

#followup-dialog-dadosNotaFiscal fieldset {
	width: 810px !important;
}
</style>

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
		<form id="form-followup-nfe">
			<div id="followup-dialog-nfe" title="NF-e">
				<fieldset style="width: 310px !important;">
					<legend>Incluir NF-e</legend>
					<table width="280" border="0" cellspacing="1" cellpadding="0">
						<tr>
							<td width="84">Cota:</td>
							<td width="193">
								<input type="text" id="followupCotaCadastroNota" name="followupCotaCadastroNota" style="width: 80px; float: left; margin-right: 5px;" /> 
								<span class="classPesquisar">
									<a href="javascript:;" onclick="entradaNFETerceirosController.pesquisarCotaCadastroNota();">&nbsp;</a>
								</span>
							</td>
						</tr>
						<tr>
							<td>Nome:</td>
							<td>
								<input type="text" name="followupNomeCotaCadastroNota" id="followupNomeCotaCadastroNota" />
							</td>
						</tr>
						<tr>
							<td>NF-e:</td>
							<td>
								<input type="text" name="followupNumeroNotaCadastroNota" id="followupNumeroNotaCadastroNota" />
							</td>
						</tr>
						<tr>
							<td>Série:</td>
							<td>
								<input type="text" name="followupSerieNotaCadastroNota" id="followupSerieNotaCadastroNota" />
							</td>
						</tr>
						<tr>
							<td>Chave-Acesso:</td>
							<td>
								<input type="text" name="followupChaveAcessoCadastroNota" id="followupChaveAcessoCadastroNota" />
							</td>
						</tr>
						<tr>
							<td>Valor Nota R$:</td>
							<td>
								<input type="text" name="followupValorNotaCadastroNota" id="followupValorNotaCadastroNota" />
							</td>
						</tr>
					</table>
				</fieldset>
				</div>
			</form>
			<form id="form-followup-dadosNotaFiscal">
				<div id="followup-dialog-dadosNotaFiscal" title="Dados da Nota Fiscal">
					<fieldset>
						<legend>Nota Fiscal</legend>
						<table width="670" border="0" cellspacing="1" cellpadding="1" style="color: #666;">
							<tr>
								<td width="133">Núm. Nota Fiscal:</td>
								<td width="307" id="followupNumeroNotaFiscalPopUp"></td>
								<td width="106">Série:</td>
								<td width="111" id="followupSerieNotaFiscalPopUp"></td>
							</tr>
							<tr>
								<td>Data:</td>
								<td id="followupDataNotaFiscalPopUp"></td>
								<td>Valor Total R$:</td>
								<td id="followupValorNotaFiscalPopUp"></td>
							</tr>
							<tr>
								<td>Chave de Acesso:</td>
								<td colspan="3" id="followupChaveAcessoNotaFiscalPopUp"></td>
							</tr>
						</table>
					</fieldset>
					<br clear="all" /> <br />

					<fieldset>
						<legend>Produtos Nota Fiscal</legend>
						<table class="pesquisarProdutosNotaGrid"></table>
					</fieldset>
				</div>
			</form>
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