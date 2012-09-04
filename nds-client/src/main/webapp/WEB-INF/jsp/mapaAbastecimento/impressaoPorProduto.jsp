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
.class_sm{font-weight:bold; font-size:13px;}

.class_total{font-weight:bold; font-size:16px; background:#C0C0C0; }
.relatorios td{padding-left:5px;}
.class_total1 {font-weight:bold; font-size:16px; background:#C0C0C0; }
</style>
<script language="javascript" type="text/javascript">
function imprimir(){
	$( "#btImpressao", BaseController.workspace ).hide();
	window.print();
}
</script>
</head>

<body>

<table width="800" border="0" align="center" cellpadding="3" cellspacing="0" style="border:1px solid #000; margin-bottom:5px;">
  <tr>
    <td width="121" height="21" align="center"><span style="border-bottom:1px solid #000;"><span class="logo"><img src="${pageContext.request.contextPath}/images/logo_sistema.png" width="110" height="70" alt="Novo Distrib"  /></span></span></td>
    <td width="269" align="center" valign="middle"><h3>TREELOG S/A<br />
      LOGÍSTICA E DISTRIBUIÇÃO</h3></td>
    <td width="408" align="right" valign="middle"><h1>Mapa de Abastecimento&nbsp;</h1></td>
    </tr>
  <tr>
    <td colspan="3" align="center" valign="middle"></td>
  </tr>
</table>

<table width="800" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td width="390" align="left" valign="top"><table width="390" border="0" cellpadding="0" cellspacing="0" style="margin-top:5px;" class="relatorios">
      <tr class="class_linha_3">
        <td width="174" style="border-left:1px solid #000;border-top:1px solid #000;border-bottom:1px solid #000;"><strong>Publicação</strong></td>
        <td width="62" align="center" style=" border-bottom:1px solid #000; border-top:1px solid #000;border-left:1px solid #000;"><strong>Edição</strong></td>
        <td width="62" align="center" style=" border-bottom:1px solid #000; border-top:1px solid #000;border-left:1px solid #000;"><strong>SM</strong></td>
        <td width="62" align="center" style=" border-bottom:1px solid #000; border-top:1px solid #000;border-left:1px solid #000;border-right:1px solid #000;"><strong>Total</strong></td>
      </tr>
      
      <c:forEach items="${mapa.produtos}" var="produto" varStatus="statusProduto" end="${mapa.produtos.size()%2==0?(mapa.produtos.size()/2)-1:(mapa.produtos.size()/2)}">
      
      <tr class="class_linha_${statusProduto.index%2==0?1:2}">
        <td style="border-left:1px solid #000;border-bottom:1px solid #000;">${produto.value.nomeProduto}</td>
        <td align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;">${produto.value.numeroEdicao}</td>
        <td align="center" class="class_sm" style="border-left:1px solid #000;border-bottom:1px solid #000;"><strong>${produto.value.sm}</strong></td>
        <td align="center" class="class_total" style="border-right:1px solid #000;border-left:1px solid #000;border-bottom:1px solid #000;">${produto.value.total}</td>
      </tr>
      
      </c:forEach>
      
    </table></td>
    <td width="20">&nbsp;&nbsp;&nbsp;</td>
    <td width="390" valign="top">
    
    <table width="390" border="0" align="right" cellpadding="0" cellspacing="0" class="relatorios" style="margin-top:5px;">
      <tr class="class_linha_3">
        <td width="174" style="border-left:1px solid #000;border-top:1px solid #000;border-bottom:1px solid #000;"><strong>Publicação</strong></td>
        <td width="62" align="center" style=" border-bottom:1px solid #000; border-top:1px solid #000;border-left:1px solid #000;"><strong>Edição</strong></td>
        <td width="62" align="center" style=" border-bottom:1px solid #000; border-top:1px solid #000;border-left:1px solid #000;"><strong>SM</strong></td>
        <td width="62" align="center" style=" border-bottom:1px solid #000; border-top:1px solid #000;border-left:1px solid #000;border-right:1px solid #000;"><strong>Total</strong></td>
      </tr>
        <c:forEach items="${mapa.produtos}" var="produto" varStatus="statusProduto" begin="${mapa.produtos.size()%2==0?mapa.produtos.size()/2:(mapa.produtos.size()/2)+1}" >
      
      <tr class="class_linha_${statusProduto.index%2==0?1:2}">
        <td style="border-left:1px solid #000;border-bottom:1px solid #000;">${produto.value.nomeProduto}</td>
        <td align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;">${produto.value.numeroEdicao}</td>
        <td align="center" class="class_sm" style="border-left:1px solid #000;border-bottom:1px solid #000;"><strong>${produto.value.sm}</strong></td>
        <td align="center" class="class_total" style="border-right:1px solid #000;border-left:1px solid #000;border-bottom:1px solid #000;">${produto.value.total}</td>
      </tr>
      
      </c:forEach>
      
    </table></td>
  </tr>
</table>
</body>
</body>
</html>
