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
	
<table width="800" border="0" align="center" cellpadding="3" cellspacing="0" style="border:1px solid #000; margin-bottom:5px;">
  <tr>
    <td colspan="3" align="center" valign="middle"><h2>${mensagem} </h1></td>
   
  </tr>
</table>
	
</body>
</html>
