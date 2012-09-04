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
    <td width="408" align="right" valign="middle"><h1>Mapa de Abastecimento por Rota &nbsp;</h1></td>
    </tr>
  <tr>
    <td colspan="3" align="center" valign="middle"></td>
  </tr>
</table>

<c:forEach items="${mapa}" var="box" varStatus="statusBox">
		
		<table width="800" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-top:5px;">
             
       
            <tr class="class_linha_3">
              <td class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000;"><strong>Publicação</strong></td>
              <td class="relatorios" style="padding-left:5px; border-right:1px solid #000; border-top:1px solid #000;"><strong>Edição</strong></td>
              <td width="560" rowspan="2" align="center" style=" border-right:1px solid #000; border-bottom:1px solid #000; border-top:1px solid #000;"><strong>${box.key}</strong></td>
            </tr>
            <tr class="class_linha_3">
              <td width="151" class="relatorios" style="padding-left:5px; border-bottom:1px solid #000; border-left:1px solid #000;"><strong>Código</strong></td>
              <td width="89" style="padding-left:5px; border-bottom:1px solid #000; border-right:1px solid #000;" class="relatorios"><strong>Preço Capa</strong></td>
            </tr>
            
            <c:forEach items="${box.value}" var="produto" varStatus="statusProduto">
	            
	            <tr class="class_linha_${statusProduto.index%2==0?1:2}">
	              <td style="border-left:1px solid #000;padding-left:5px; ">${produto.value.nomeProduto}</td>
	              <td style="border-right:1px solid #000;padding-left:5px; ">${produto.value.numeroEdicao}</td>
	              <td rowspan="2">
	              <table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:63px;">
	                <tr class="box_rel">
	                  
	                  <c:forEach items="${produto.value.rotasQtde}" var="qtdeRota" varStatus="statusQtdeRota">
	                  	 <td width="60" align="center" style="border-right:1px solid #000; border-bottom:1px solid #000;">${qtdeRota.key}</td>	                  </c:forEach>
	                  
	                  <td width="80" align="center" style=" border-right:1px solid #000; border-bottom:1px solid #000;"><strong>Reparte</strong></td>
	                </tr>
	                <tr class="box_dados">
	                  <c:forEach items="${produto.value.rotasQtde}" var="qtdeRota" varStatus="statusQtdeRota">
	                  	 <td align="center" style=" border-bottom:1px solid #000; border-right:1px solid #000;">${qtdeRota.value eq 0 ? '': qtdeRota.value}</td>
	                  </c:forEach>
	                  <td align="center"style=" border-bottom:1px solid #000; border-right:1px solid #000;">${produto.value.totalReparte}</td>
	                </tr>
	              </table></td>
	            </tr>
	            <tr class="class_linha_${statusProduto.index%2==0?1:2}">
	              <td style="padding-left:5px;  border-left:1px solid #000; border-bottom:1px solid #000;">${produto.value.codigoProduto}</td>
	              <td style="padding-left:5px; border-right:1px solid #000; border-bottom:1px solid #000;"><strong> R$</strong> ${produto.value.precoCapa}</td>
	            </tr>
	            
            </c:forEach>
            
		</table>

</c:forEach>

</body>
</html>
