<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<script type="text/javascript" src="scripts/fechamentoCEIntegracao.js"></script>
<script language="javascript" type="text/javascript">

	$(function() {
		fechamentoCEIntegracaoController.init();		
	});	

</script>
<style type="text/css">
  .box_field{width:200px;}
</style>
</head>

<body>
    <div class="areaBts">
    	<div class="area">
    		<span class="bt_novos" id="btnFechamento" >
		      	<a href="javascript:;" title="Fechamento" rel="tipsy" title="Fechamento">
		      		<img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" id="imagemFechamento" />
		      	</a>
		      </span>
		      
		      <span class="bt_novos" id="btnReabertura">
		      	<a href="javascript:;" title="Reabertura" rel="tipsy">
		      		<img src="${pageContext.request.contextPath}/images/ico_expedicao_box.gif" hspace="5" border="0" id="imagemReabertura" /></a>
		      </span>
		
			
		    <span class="bt_novos">
		    	<a href="javascript:;" rel="tipsy" title="Imprimir Boleto">
		    		<img src="${pageContext.request.contextPath}/images/ico_negociar.png" hspace="5" border="0" />
		    	</a>
		    </span>
		       
		       <span class="bt_novos">
		       	<a href="javascript:;" rel="tipsy" title="Imprimir Boleto em Branco">
		       		<img src="${pageContext.request.contextPath}/images/ico_detalhes.png" hspace="5" border="0" />
		       	</a>
		       </span>
		       
		       
		       <span class="bt_arq">
				  	<a href="${pageContext.request.contextPath}/devolucao/fechamentoCEIntegracao/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
				  		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					</a>
			</span>
		    <span class="bt_arq">
    			<a href="${pageContext.request.contextPath}/devolucao/fechamentoCEIntegracao/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
    				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
    			</a>
    		</span>
    	</div>
    </div>
    <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="fieldFiltro">
		<legend> Pesquisar Fechamento  CE</legend>			
 	    	<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="69">Fornecedor:</td>    
				  	<td width="223">
						<select id="idFornecedor" name="idFornecedor" style="width:170px;">
							<option value="-1"  selected="selected">Todos</option>
							<c:forEach items="${listaFornecedores}" var="fornecedor">
								<option value="${fornecedor.key}">${fornecedor.value}</option>	
							</c:forEach>
						</select>
					</td>
				  	<td width="48">
				  		Semana:
				  	</td>
				  	<td width="151">
				  		<input type="text" id="semana" name="semana" />
				  	</td>
			  		<td width="433"><span id="btnPesquisar" class="bt_novos"><a href="javascript:;" class="botaoPesquisar" ><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
				</tr>
			</table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
       <fieldset class="fieldGrid">
       	  <legend> Fechamento CE</legend>
          
        <div class="grids" style="display:none;">
          <table class="fechamentoCeGrid"></table>
          
          <div class="linha_separa_fields">&nbsp;</div>
          
			<br clear="all" />
			<div class="tabelaTotal" style="display:none;">
					<span name="total" id="total" ></span>
				</div>
			</div>
		
      		</fieldset>
      		
</body>
</html>
