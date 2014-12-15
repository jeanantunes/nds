<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numberformatter-1.2.3.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/vendaEncalhe.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.justInput.js"></script>

<script type="text/javascript">

var pesquisaCotaVendaEncalhe = new PesquisaCota();

var pesquisaProdutoVendaEncalhe = new PesquisaProduto();

$(function(){
	VENDA_PRODUTO.inicializar();
	bloquearItensEdicao(VENDA_PRODUTO.workspace);
});

</script>

</head>
<body>
	<form id="form-excluirVenda">
	<div id="dialog-excluirVenda" title="Atenção" style="display:none">
		<p>Confirma a Exclusão desta Venda de Encalhe / Suplementar?</p>
	</div>
	</form>

<div class="areaBts">
	<div class="area">
		      		
   		<span class="bt_novos">
   			<a isEdicao="true" onclick="VENDA_PRODUTO.novaVenda();" href="javascript:;" rel="tipsy" title="Venda Encalhe / Suplementar">
   				<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_copia_distrib.gif">
   			</a>
   		</span>
              
        <span style="display: none" id="infosRodape" class="infosRodape" >
		   <span id="divImprimirSuplementar" style="display: none">		            
			   <span id="btSuplementar" class="bt_novos">
					<a  href="javascript:;" onclick="VENDA_PRODUTO.imprimirSlipVenda()" rel="tipsy" title="Imprimir Slip de Suplementar">
						<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_impressora.gif">
					</a>
			   </span>
			</span>
		</span>
          
        <span id="divImprimirEncalhe" style="display: none">
			 <span style="" id="btEncalhe" class="bt_novos">
			     <a  href="javascript:;" onclick="VENDA_PRODUTO.imprimirSlipVenda()" rel="tipsy" title="Imprimir Slip de Encalhe">
					<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_impressora.gif">
				 </a>
			 </span>
		</span>
       
        <span class="bt_arq">
         		<a href="javascript:;" onclick="VENDA_PRODUTO.exportar('XLS')" rel="tipsy" title="Gerar Arquivo">
         			<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_excel.png">
          		</a>
        </span>

	    <span class="bt_arq">
				<a href="javascript:;" onclick="VENDA_PRODUTO.exportar('PDF')" rel="tipsy" title="Imprimir">
				<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_impressora.gif">
				</a>
	    </span>
	    
    </div>
</div>

<div class="linha_separa_fields">&nbsp;</div>
<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
   	   
   	   <legend> Pesquisar Encalhe</legend>
        <table width="950" cellspacing="1" cellpadding="2" border="0" class="filtro">
  			<tbody>
  				<tr>
				    <td width="53">Cota:</td>
				    
				    <td width="125">
				    	
				    	<input name="vend-suplementar-numCota" id="vend-suplementar-numCota" type="text" maxlength="11"
				               style="width:70px; 
				               float:left; margin-right:5px;"
				               onchange="pesquisaCotaVendaEncalhe.pesquisarPorNumeroCota('#vend-suplementar-numCota', '#vend-suplementar-descricaoCota',false,
				              	  									 VENDA_PRODUTO.pesquisarCotaSuccessCallBack, 
				              	  									 VENDA_PRODUTO.pesquisarCotaErrorCallBack);"/>
				    </td>
				    
				    <td width="46">Nome:</td>
				    
				    <td width="320">
				    	<input type="hidden" name="vend-suplementar-descricaoCota" id="vend-suplementar-descricaoCota"/>
				    	<span class="dadosFiltro" id="span_nome_cota" style="display: inline;"></span>     
				    </td>
				    
				    <td width="86">Tipo de Venda:</td>
				    
				    <td width="180">
				    	<select id="vend-suplementar-selectTipoVenda" name="vend-suplementar-selectTipoVenda">
				      		<option value="">Todas</option>
				      		<option value="ENCALHE">Venda de Encalhe</option>
				      		<option value="SUPLEMENTAR">Venda de Suplementar</option>
				    	</select>
				    </td>
				    
				    <td width="104">&nbsp;</td>
				    
			  	</tr>
			  	<tr>
				    <td>Período:</td>
				    <td>
				    	<input type="text" style="width:80px; " id="vend-suplementar-periodoDe" name="vend-suplementar-periodoDe">
				    </td>
				    <td>Até:</td>
				    <td>
				    	<input type="text" style="width:80px; " id="vend-suplementar-periodoAte" name="vend-suplementar-periodoAte">
				    </td>
				    <td>&nbsp;</td>
				    <td>&nbsp;</td>
				    <td>
				    	<span class="bt_pesquisar"><a onclick="VENDA_PRODUTO.pesquisarVendas();" href="javascript:;"></a></span>
				    </td>
			  </tr>
        	</tbody>
        </table>

      </fieldset>
      
      <div class="linha_separa_fields">&nbsp;</div>
      
      <fieldset class="fieldGrid">
       	  
       		<legend>Venda de Encalhes</legend>
        	
	    	<div class="gridVenda" id="gridVenda" style="display: none;">
				<table id="vendaEncalheGridCota" class="vendaEncalheGridCota"></table>
			</div>
	        
	       	<table width="100%" cellspacing="2" cellpadding="2" border="0">
	             <tbody><tr>
	               <td width="35%">
	               		&nbsp;
	             </td>
	               <td width="6%">&nbsp;</td>
	               <td width="14%">
               		<div style="display: none;" class="infosRodape" id="labelTotalGeral"><strong>Total Geral R$:</strong></div>
	               </td>
	               <td width="13%" align="left">
	               		<div style="display: none;" class="infosRodape" id="totalGeral" ></div>
	               </td>
	               <td width="11%"></td>
	             </tr>
	           </tbody>
	          </table>
	          
	          <jsp:include page="gridVenda.jsp"/>
	          
	          <iframe src="" id="download-iframe-vendaEncalhe" style="display:none;"></iframe>
    
      </fieldset>
</body>