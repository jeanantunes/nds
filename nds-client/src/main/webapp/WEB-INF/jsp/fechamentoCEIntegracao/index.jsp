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
			      <span class="bt_novos" title="Fechamento"><a href="javascript:;"><img src="../images/ico_check.gif" hspace="5" border="0" />Fechamento</a></span>
			      
			      <span class="bt_novos" title="Reabertura"><a href="javascript:;"><img src="../images/ico_expedicao_box.gif" hspace="5" border="0" />Reabertura</a></span>
			      
			      <!--<span class="bt_novos" title="Resumo CE"><a href="resumo_ce.htm" target="_blank"><img src="../images/bt_expedicao.png" hspace="5" border="0" />Resumo CE</a></span>-->
			      
			      
			      <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
			      <br clear="all" /><br />
			
			
			      <span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
			       <span class="bt_novos" title="Imprimir Boleto"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Boleto</a></span>
			       
			       <span class="bt_novos" title="Imprimir Boleto"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Boleto em Branco</a></span>
			    </td>
			    <td width="88" valign="top"><strong>Total Bruto R$:</strong></td>
			    <td width="50" valign="top">3.014,00</td>
			    <td width="106" valign="top"><strong>Total Desconto R$:</strong></td>
			    <td width="49" valign="top">753,99</td>
			    <td width="93" valign="top"><strong>Total Líquido R$:</strong></td>
			    <td width="70" valign="top">2.260,00</td>
			  </tr>
			</table>

			</div>
		
      		</fieldset>
      		<div class="linha_separa_fields">&nbsp;</div>
      
    </div>
</div>
</body>
</html>
