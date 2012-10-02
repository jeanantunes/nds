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
.class_linha_1, .class_linha_2{background:none!important;}
.class_linha_11 {background:none!important;}
.class_linha_21 {background:none!important;}
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
    <td width="120" height="21" align="center"><span style="border-bottom:1px solid #000;"><span class=""><img src="${pageContext.request.contextPath}/images/logo_sistema.png" width="110" height="70" alt="Novo Distrib"  /></span></span></td>
    <td width="301" align="center" valign="middle"><h3>TREELOG S/A<br />
      LOGÍSTICA E DISTRIBUIÇÃO</h3></td>
    <td width="359" align="right" valign="middle"><h1>Mapa de Abastecimento</h1>
     <strong>Distribuidor:</strong> ${distribuidor}</td>
  </tr>
  <tr>
    <td colspan="3" align="center" valign="middle"></td>
  </tr>
</table>
<table width="800" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-top:5px;">
  <tr class="class_linha_3">
    <td width="660"  style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><table width="100%" border="0" cellspacing="1" cellpadding="1">
      <tr>
        <td width="10%"><strong>Box:</strong></td>
        <td width="13%">${entregador.rota.roteiro.roteirizacao.box.codigo}</td>
        <td width="7%"><strong>Roteiro:</strong></td>
        <td width="20%">${entregador.rota.roteiro.descricaoRoteiro}</td>
        <td width="5%"><strong>Rota:</strong></td>
        <td width="28%">${entregador.rota.descricaoRota}</td>
        <td width="5%"><strong>Data:</strong></td>
        <td width="12%">${data}</td>
      </tr>
    </table></td>
  </tr>
</table>


<c:forEach items="${mapa}" var="produto">
	
	<table width="800" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-top:5px;">
	  <tr class="class_linha_3">
	    <td width="660"  style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><table width="793" border="0" cellspacing="1" cellpadding="1">
	      <tr>
	        <td width="52"><strong>Produto</strong>:</td>
	        <td width="195">${produto.value.nomeProduto}</td>
	        <td width="45"><strong>Edição:</strong></td>
	        <td width="44">${produto.value.numeroEdicao}</td>
	        <td width="119"><strong>Código de Barras:</strong></td>
	        <td width="154">${produto.value.codigoProduto}</td>
	        <td width="96"><strong>Preço Capa R$:</strong></td>
	        <td width="63">${produto.value.precoCapa}</td>
	      </tr>
	    </table></td>
	  </tr>
	</table>
	
	<c:set scope="session" var="qtdeColuna" value="${(produto.value.cotasQtdes.size() + (7==(7 - produto.value.cotasQtdes.size() % 7)? 0 : (7 - produto.value.cotasQtdes.size() % 7))) /7}"/>					 
	<c:set scope="session" var="qtdeTotal" value="${produto.value.cotasQtdes.size()}"/>
		
	<table width="800" align="center" cellpadding="0" cellspacing="0" border="0">
		
		<tr class="nivel0">
			 
			<c:forEach begin="0" end="${ qtdeTotal%qtdeColuna>0 ? qtdeTotal/qtdeColuna : qtdeTotal/qtdeColuna -1}" varStatus="coluna">
				
				<td width="158" valign="top">		
				
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0"  style="margin-top:5px;border-right:1px">
								  
					  <tr class="class_linha_3">
					    <td class="nivel1" width="48" style="padding-left:5px; border-left:1px solid #000;border-top:1px solid #000;border-bottom:1px solid #000;"><strong>Cota</strong></td>
					    <td class="nivel1" width="51" align="center" style=" border:1px solid #000;"><strong>Qtde</strong></td>
					  </tr> 
					 
					 <c:forEach items="${produto.value.cotasQtdes}" var="item" 
						 		begin="${coluna.index * qtdeColuna }" 
						 		end="${qtdeTotal > qtdeColuna * (coluna.index+1)-1 ? qtdeColuna * (coluna.index+1)-1 : qtdeTotal-1}" >
					  
					  <tr class="class_linha_1">
					    <td style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; ">${item.key}</td>
					   <td align="center" style="border-right:1px solid #000;border-left:1px solid #000;border-bottom:1px solid #000;">${item.value}</td>
					  </tr>
					  
					  </c:forEach>
					  
					</table>
					    
				</td>				 
				    
		 </c:forEach>
			
		</tr>
	
		
	</table>
	
</c:forEach>

</body>
</html>
