<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/vendaEncalhe.js"></script>

<script type="text/javascript">

var pesquisaCotaVendaEncalhe = new PesquisaCota();

var pesquisaProdutoVendaEncalhe = new PesquisaProduto();

$(function(){
	VENDA_PRODUTO.inicializar();
});

</script>

</head>
<body>
	<form id="form-excluirVenda">
	<div id="dialog-excluirVenda" title="Atenção" style="display:none">
		<p>Confirma a Exclusão desta Venda de Encalhe?</p>
	</div>
	</form>
	

<fieldset class="classFieldset">
   	   
   	   <legend> Pesquisar Encalhe</legend>
        <table width="950" cellspacing="1" cellpadding="2" border="0" class="filtro">
  			<tbody>
  				<tr>
				    <td width="53">Cota:</td>
				    
				    <td width="125">
				    	
				    	<input name="numCota" 
				               id="numCota" 
				               type="text"
				               maxlength="11"
				               style="width:70px; 
				               float:left; margin-right:5px;"
				               onchange="pesquisaCotaVendaEncalhe.pesquisarPorNumeroCota('#numCota', '#descricaoCota',false,
				              	  									 VENDA_PRODUTO.pesquisarCotaSuccessCallBack, 
				              	  									 VENDA_PRODUTO.pesquisarCotaErrorCallBack);"/>
				    </td>
				    
				    <td width="46">Nome:</td>
				    
				    <td width="320">
				    	<input type="hidden" name="descricaoCota" id="descricaoCota"/>
				    	<span class="dadosFiltro" id="span_nome_cota" style="display: inline;"></span>     
				    </td>
				    
				    <td width="86">Tipo de Venda:</td>
				    
				    <td width="180">
				    	<select id="selectTipoVenda" name="tipoVendaSelecionado">
				    		<option selected="selected" value="-1"> </option>
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
				    	<input type="text" style="width:80px; " id="periodoDe" name="periodoDe">
				    </td>
				    <td>Até:</td>
				    <td>
				    	<input type="text" style="width:80px; " id="periodoAte" name="periodoAte">
				    </td>
				    <td>&nbsp;</td>
				    <td>&nbsp;</td>
				    <td>
				    	<span class="bt_pesquisar"><a onclick="VENDA_PRODUTO.pesquisarVendas();" href="javascript:;">Pesquisar</a></span>
				    </td>
			  </tr>
        	</tbody>
        </table>

      </fieldset>
      
      <div class="linha_separa_fields">&nbsp;</div>
      
      <fieldset class="classFieldset">
       	  
       		<legend>Venda de Encalhes</legend>
        	
	    	<div class="gridVenda" id="gridVenda" style="display: none;">
				<table id="vendaEncalheGrid" class="vendaEncalheGrid"></table>
			</div>
	        
	       	<table width="100%" cellspacing="2" cellpadding="2" border="0">
	             <tbody><tr>
	               <td width="56%">
	               		<span title="Novo" class="bt_novos">
	               			<a onclick="VENDA_PRODUTO.novaVenda('SUPLEMENTAR');" href="javascript:;">
	               				<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_salvar.gif">
	               					Venda Suplementar
	               			</a>
	               		</span>
	               		
	               		<span title="Novo" class="bt_novos">
	               			<a onclick="VENDA_PRODUTO.novaVenda('ENCALHE');" href="javascript:;">
	               				<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_salvar.gif">
	               				Venda Encalhe
	               			</a>
	               		</span>
	               
	               	<div style="display: none" id="infosRodape" class="infosRodape" >
	               
		               	<span title="Gerar Arquivo" class="bt_novos">
		               		<a href="javascript:;" onclick="VENDA_PRODUTO.exportar('XLS')">
		               			<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_excel.png">
		               			Arquivo
		               		</a>
		               	</span>
		
						<span title="Imprimir" class="bt_novos">
							<a href="javascript:;" onclick="VENDA_PRODUTO.exportar('PDF')">
								<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_impressora.gif">
								Imprimir
							</a>
						</span>
		               
		               <div id="divImprimirSuplementar" style="display: none">		            
			               <span style="" id="btSuplementar" title="Imprimir" class="bt_novos">
			               		<a  href="javascript:;" onclick="VENDA_PRODUTO.imprimirSlipVenda()">
			               			<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_impressora.gif">
			               			Imprimir Slip de Suplementar
			               		</a>
			               </span>
		               </div>
		               
		               <div id="divImprimirEncalhe" style="display: none">
			               <span style="" id="btEncalhe" title="Imprimir" class="bt_novos">
			               		<a  href="javascript:;" onclick="VENDA_PRODUTO.imprimirSlipVenda()">
			               			<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_impressora.gif">
			               			Imprimir Slip de Encalhe
			               		</a>
			               </span>
		            </div>
	               </div>
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
    
      </fieldset>
</body>