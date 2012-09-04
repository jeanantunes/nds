<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/NDS.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/menu_superior.css" />
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<style type="text/css">
body{font-size:12px!important;}
h1{font-size:20px;}
h2{font-size:25px;}
p{margin:0px; padding:0px; font-size:11px;}
.capas tr{border:1px solid #000;}
.box_rel{line-height:15px!important; background:#fff;}
.box_dados{line-height:30px!important; font-size:16px; font-weight:bold;}

</style>
<script language="javascript" type="text/javascript">
function imprimir(){
	$( "#btImpressao", BaseController.workspace ).hide();
	window.print();
}
</script>
</head>

<body>

<table width="660" border="0" align="center" cellpadding="3" cellspacing="0" style="border:1px solid #000; margin-bottom:5px;">
  <tr>
    <td width="121" height="21" align="center"><span style="border-bottom:1px solid #000;"><span class="logo"><img src="${pageContext.request.contextPath}/images/logo_sistema.png" width="110" height="70" alt="Novo Distrib"  /></span></span></td>
    <td width="269" align="center" valign="middle"><h3>TREELOG S/A<br />
      LOGÍSTICA E DISTRIBUIÇÃO</h3></td>
    <td width="408" align="right" valign="middle"><h1>Mapa de Abastecimento por Produto&nbsp;</h1></td>
    </tr>
  <tr>
    <td colspan="3" align="center" valign="middle"></td>
  </tr>
</table>

<table width="660" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-top:5px;">
            <tr class="class_linha_3">
              <td colspan="2" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td width="9%"><strong>Produto</strong>: </td>
                  <td width="36%"><strong>${mapa.nomeProduto}</strong></td>
                  <td width="23%"><strong>Código:</strong></td>
                  <td width="32%">${mapa.codigoProduto}</td>
                </tr>
                <tr>
                  <td><strong>Edição:</strong></td>
                  <td>${mapa.numeroEdicao}</td>
                  <td><strong>Preço Capa R$:</strong></td>
                  <td>${mapa.precoCapa}</td>
                </tr>
              </table></td>
            </tr>
            
            
         <c:forEach items="${mapa.boxes}" var="box">
            
            <tr class="class_linha_1">
              <td width="177" style="border-left:1px solid #000;border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${box.key}</td>
              <td width="483">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:63px;">
              
              
                <tr class="box_rel">
                	
                  <c:forEach items="${box.value.rotasQtde}" var="rota" varStatus="statusRota" >
	                	<td width="60" align="center" style="border-right:1px solid #000; border-bottom:1px solid #000;">${rota.key}</td>
                  </c:forEach>
                  
                  <td width="80" align="center" style=" border-right:1px solid #000; border-bottom:1px solid #000;"><strong>Reparte</strong></td>
                </tr>
                <tr class="box_dados">
                	
                  <c:forEach items="${box.value.rotasQtde}" var="rota" varStatus="statusRota" >
                  	<td align="center" style=" border-bottom:1px solid #000; border-right:1px solid #000;">${rota.value}</td>
                  </c:forEach>
                  
                  
                  <td align="center"style=" border-bottom:1px solid #000; border-right:1px solid #000;">${box.value.qtdeTotal}</td>
                </tr>
               
                
              </table></td>
            </tr>
         </c:forEach>
           
</table>
</body>
</html>
