<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<link rel="stylesheet" type="text/css" href="../css/NDS.css" />
<link rel="stylesheet" type="text/css" href="../css/menu_superior.css" />
<link rel="stylesheet" href="../scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.all.css" />
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.core.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.effects.core.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.effects.highlight.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.widget.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.position.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.accordion.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/NDS.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.dialog.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.tabs.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.datepicker.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>
<link rel="stylesheet" type="text/css" href="../scripts/flexigrid-1.1/css/flexigrid.pack.css" />
<script language="javascript" type="text/javascript" src="../scripts/romaneios.js"></script>
<script language="javascript" type="text/javascript">
$(function(){
	romaneiosController.init();
});
</script>
<style type="text/css">
  .dados, .dadosFiltro{display:none;}
</style>
</head>

<body>
<div class="corpo">
    <br clear="all"/>

   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Expedição < evento > com < status >.</b></p>
	</div>

	<br />

      <fieldset class="classFieldset">
   	    <legend>Romaneios de Entrega</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
         <tr>
              <td width="34">Box:</td>
              <td width="121">
	               <select name="idBox" id="idBox" style="width: 100px;">
						<option value="" selected="selected">Selecione...</option>
						<option value="-1" >Todos</option>
						<c:forEach var="box" items="${listaBox}">
							<option value="${box.key}">${box.value}</option>
						</c:forEach>
				  </select>
              </td>
                <td width="55">Roteiro:</td>
                <td width="277">
                	<select name="idRoteiro" id="idRoteiro" style="width: 200px;">
						<option value="" selected="selected">Selecione...</option>
						<option value="-1" >Todos</option>
						<c:forEach var="roteiro" items="${listaRoteiro}">
							<option value="${roteiro.key}">${roteiro.value}</option>
						</c:forEach>
				    </select>
                 
                </td>
                <td width="27">Rota:</td>
                <td width="296">
					<select name="idRota" id="idRota" style="width: 190px;">
						<option value="" selected="selected">Selecione...</option>
						<option value="-1" >Todos</option>
						<c:forEach var="rota" items="${listaRota}">
							<option value="${rota.key}">${rota.value}</option>
						</c:forEach>
				    </select>
                 
                </td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="romaneiosController.pesquisar();">Pesquisar</a></span></td>
            </tr>
  
  		</table>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
       <fieldset class="classFieldset">
       	  	<legend> Roteiros / Rotas</legend>
	        <div class="grids" style="display:none;">
			  	<table class="romaneiosGrid"></table>
	          	<span class="bt_novos" title="Gerar Arquivo">
	          		<a href="${pageContext.request.contextPath}/romaneio/exportar?fileType=XLS">
	          			<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
	          			Arquivo
	          		</a>
	          	</span>
	    		<span class="bt_novos" title="Imprimir">
	    			<a href="${pageContext.request.contextPath}/romaneio/exportar?fileType=PDF">
	    				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
	    				Imprimir Romaneio
	    			</a>
	    		</span>
			</div>		
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
    </div>
</div> 

</body>
</html>
