<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<script language="javascript" type="text/javascript" src="scripts/consultaConsignadoCota.js"></script>
<script language="javascript" type="text/javascript">
	$(function() {
		consultaConsignadoCotaController.init();
	});
</script>
<style type="text/css">

#detalhes input{float:left;}
#detalhes label, #dialog-detalhes label{width:auto !important; line-height:30px ; margin-bottom:0px!important;}
#dialog-detalhes fieldset{width:800px!important;}
</style>
</head>

<body>

<form id="form-detalhes">
<div id="dialog-detalhes" title="Detalhes" style="display:none;">
	<fieldset>
    	<legend><span name="numeroNomeCotaPopUp" id="numeroNomeCotaPopUp"></span></legend>
        
		<table class="consignadosCotaDetalhesGrid"></table>
	</fieldset>
</div>
</form>

<div class="corpo">  
    <br clear="all"/>
    <br />
   
    <div class="container">	
     <fieldset class="classFieldset">
   	    <legend>Pesquisar Consignados Cota
        </legend><table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
  <tr>
    <td width="30">Cota:</td>
    <td width="96"><input type="text" name="codigoCota" id="codigoCota" style="width:60px; float:left; margin-right:5px;" onblur="consultaConsignadoCotaController.pesquisarCota();" />
    	<input type="hidden" id="valorGrid" name="valorGrid" value="total" />
    </td>
    <td width="39">Nome:</td>    
    <td width="245"><span name="nomeCota" id="nomeCota"></span></td>
    <td width="67">Fornecedor:</td>
    <td width="159">    	
    	<select id="idFornecedor" name="idFornecedor" style="width:200px;" onchange="consultaConsignadoCotaController.detalharTodos(this.value);">
		    <option value="0" selected="selected">Selecione</option>
		    <option value="-1">Todos</option>
		    <c:forEach items="${listaFornecedores}" var="fornecedor">
		      		<option value="${fornecedor.key}">${fornecedor.value}</option>	
		    </c:forEach>
		</select>
    </td>
    <td width="169">
	    <div id="detalhes" style="display:none;">
	    <label><input name="opcaoDetalhe" id="opcaoDetalhe" type="checkbox" />Detalhar</label></div>
	</td>
    <td width="104"><span class="bt_pesquisar"><a href="javascript:;"  onclick="consultaConsignadoCotaController.pesquisar();">Pesquisar</a></span></td>
  </tr>
  </table>
      </fieldset>
          <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
      	<div class="pesqCota" style="display:none;">
       	  <legend><span name="numeroNomeCota" id="numeroNomeCota"></span></legend>
        <div class="grids">
       	  <table class="consignadosCotaGrid"></table>
			<span class="bt_novos" title="Gerar Arquivo">
				<a href="${pageContext.request.contextPath}/financeiro/consultaConsignadoCota/exportar?fileType=XLS">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo
				</a>
			</span>
			<span class="bt_novos" title="Imprimir">
				<a href="${pageContext.request.contextPath}/financeiro/consultaConsignadoCota/exportar?fileType=PDF">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir
				</a>
			</span>
			<span name="totalGeralCota" id="totalGeralCota" ></span>        
         </div>
         </div>
         <div class="pesqTodos" style="display:none;">
       	  <legend>Consignados</legend>
        <div class="grids" style="display:noneA;">
       	  <table class="consignadosGrid"></table>
         </div>
          <br />
         
         <span class="bt_novos" title="Gerar Arquivo">
				<a href="${pageContext.request.contextPath}/financeiro/consultaConsignadoCota/exportar?fileType=XLS">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo
				</a>
			</span>
			<span class="bt_novos" title="Imprimir">
				<a href="${pageContext.request.contextPath}/financeiro/consultaConsignadoCota/exportar?fileType=PDF">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir
				</a>
			</span>
			<div class="tabelaGeralDetalhado" style="display:none;">
				<span name="totalGeralDetalhado" id="totalGeralDetalhado" ></span>
			</div>
			<div class="tabelaGeralPorFornecedor" style="display:none;">
				<span name="totalGeralPorFornecedor" id="totalGeralPorFornecedor" ></span>
			</div>
 		</div>


      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
    </div>
</div> 
</body>
</html>
