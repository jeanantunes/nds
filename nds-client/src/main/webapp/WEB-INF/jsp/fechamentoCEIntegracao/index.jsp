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

<div class="corpo">  
    <br clear="all"/>
    <br />
   
    <div class="container">    
    	
      <fieldset class="classFieldset">
		<legend> Pesquisar Fechamento  CE</legend>			
 	    	<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="72">Fornecedor:</td>    
				  	<td colspan="3">
						<select id="idFornecedor" name="idFornecedor" style="width:170px;">
							<option value="-1"  selected="selected">Todos</option>
							<c:forEach items="${listaFornecedores}" var="fornecedor">
								<option value="${fornecedor.key}">${fornecedor.value}</option>	
							</c:forEach>
						</select>
					</td>
				  	<td width="52">
				  		Semana:
				  	</td>
				  	<td width="486">
				  		<input type="text" id="semana" name="semana" />
				  	</td>
				  	<td width="104">
				  		<span class="bt_pesquisar">
				  		<td width="107"><span id="btnPesquisar" class="bt_pesquisar"><a href="javascript:;" class="botaoPesquisar" >Pesquisar</a></span></td>
				  	</td>
				</tr>
			</table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
       <fieldset class="classFieldset">
       	  <legend> Fechamento CE</legend>
          
        <div class="grids" style="display:none;">
          <table class="fechamentoCeGrid"></table>
          
          <div class="linha_separa_fields">&nbsp;</div>
          
			<br clear="all" />
			
			<table width="950" border="0" cellspacing="1" cellpadding="1">
			  <tr>
			    <td width="472" valign="top">
			      <span class="bt_novos" title="Fechamento" id="btnFechamento" >
			      	<a href="javascript:;">
			      		<img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" id="imagemFechamento" />
			      		Fechamento
			      	</a>
			      </span>
			      
			      <span class="bt_novos" title="Reabertura" id="btnReabertura">
			      	<a href="javascript:;">
			      		<img src="${pageContext.request.contextPath}/images/ico_expedicao_box.gif" hspace="5" border="0" id="imagemReabertura" />Reabertura</a>
			      </span>
			      
					<span class="bt_novos" title="Gerar Arquivo">
					  	<a href="${pageContext.request.contextPath}/devolucao/fechamentoCEIntegracao/exportar?fileType=XLS">
					  		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					  			Arquivo
						</a>
					</span>
			      <br clear="all" /><br />
			
			
			      <span class="bt_novos" title="Imprimir">
	    			<a href="${pageContext.request.contextPath}/devolucao/fechamentoCEIntegracao/exportar?fileType=PDF">
	    				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
	    				Imprimir
	    			</a>
	    		</span>
			       <span class="bt_novos" title="Imprimir Boleto"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Boleto</a></span>
			       
			       <span class="bt_novos" title="Imprimir Boleto">
			       	<a href="${pageContext.request.contextPath}/devolucao/fechamentoCEIntegracao/imprimeBoleto?nossoNumero='123456789'">
			       		<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
			       		Boleto em Branco
			       	</a>
			       </span>
			    </td>			    
			  </tr>
			</table>
			<div class="tabelaTotal" style="display:none;">
					<span name="total" id="total" ></span>
				</div>
			</div>
		
      		</fieldset>
      		<div class="linha_separa_fields">&nbsp;</div>
      
    </div>
</div>
</body>
</html>
