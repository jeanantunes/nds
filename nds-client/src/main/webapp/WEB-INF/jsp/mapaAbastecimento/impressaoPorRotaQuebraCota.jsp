<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
		<td width="120" height="21" align="center">
			<span>
				<span class="">
					<img src="${pageContext.request.contextPath}/administracao/parametrosDistribuidor/getLogo?number=${pageContext.request.requestedSessionId}" 
						width="110" height="70" alt="Novo Distrib"/>
				</span>
			</span>
		</td>
		<td width="301" align="center" valign="middle">
			<h3>TREELOG S/A<br />
			LOG&Iacute;STICA E DISTRIBUI&Ccedil;&Atilde;O
			</h3>
		</td>
		<td width="359" align="right" valign="middle"><h1>Mapa de Abastecimento</h1>
			<strong>Distribuidor: </strong>${nomeDistribuidor}
		</td>
	</tr>
	<tr>
		<td colspan="3" align="center" valign="middle"></td>
	</tr>
</table>
<table style="width: 800px;" align="center">
<tr><td>
	<c:forEach items="${mapa}" var="box" varStatus="statusMapa">
			<c:forEach items="${box.value}" var="roteiro">
				<c:forEach items="${roteiro.value}" var="rota">
				
					<table width="800" border="0" align="center" cellpadding="0" cellspacing="0" style="padding-top: 5px;">
						<tr class="class_linha_3">
							<td width="660"  style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;">
								<table width="100%" border="0" cellspacing="1" cellpadding="1">
									<tr>
										<td width="10%"><strong>Box:</strong></td>
										<td width="13%">${box.key}</td>
										<td width="7%"><strong>Roteiro:</strong></td>
										<td width="20%">${roteiro.key}</td>
										<td width="5%"><strong>Rota:</strong></td>
										<td width="28%">${rota.key}</td>
										<td width="5%"><strong>Data:</strong></td>
										<td width="12%">${dataLancamento}</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					
					<c:forEach items="${rota.value}" var="produto">
					
						<table width="800" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-top:5px; padding-top: 5px;">
							<tr class="class_linha_3">
								<td width="660" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;">
									<table width="793" border="0" cellspacing="1" cellpadding="1">
										<tr>
		        							<td width="52"><strong>Produto</strong>:</td>
		        							<td width="195">${produto.key.nome}</td>
									        <td width="45"><strong>Edi&ccedil;&atilde;o:</strong></td>
									        <td width="44">${produto.key.edicao}</td>
									        <td width="119"><strong>C&oacute;digo de Barras:</strong></td>
									        <td width="154">${produto.key.codigoBarras}</td>
									        <td width="96"><strong>Pre&ccedil;o Capa R$:</strong></td>
									        <td width="63">${produto.key.precoCapaFormatado}</td>
									    </tr>
		    						</table>
		    					</td>
		    				</tr>
		    			</table>
		    			
		    			<table width="114" border="0" align="left" cellpadding="0" cellspacing="0" style="margin-top:5px;">
	    				
		    				<tr class="class_linha_3">
		   						<td width="48" style="padding-left:5px; border-left:1px solid #000;border-top:1px solid #000;border-bottom:1px solid #000;">
		   							<strong>Cota</strong>
		   						</td>
		   						<td width="51" align="center" style=" border-bottom:1px solid #000; border-top:1px solid #000;border-left:1px solid #000;border-right:1px solid #000;">
		   							<strong>Qtde</strong>
		   						</td>
		 					</tr>
			    			
							<c:forEach items="${produto.value}" var="cota" varStatus="cotaCount">
								
								<tr class="class_linha_1">
		    						<td style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; ">
		    							${cota.key}
		    						</td>
		    						<td align="center" style="border-right:1px solid #000;border-left:1px solid #000;border-bottom:1px solid #000;">
		    							${cota.value}
		    						</td>
		  						</tr>
		  						
		  						<c:if test="${cotaCount.count mod 6 == 0 && cotaCount.count < produto.value.size()}">
		  							</table>
		  							<table width="114" border="0" align="left" cellpadding="0" cellspacing="0" style="margin-top:5px;">
	    				
				    				<tr class="class_linha_3">
				   						<td width="48" style="padding-left:5px; border-left:1px solid #000;border-top:1px solid #000;border-bottom:1px solid #000;">
				   							<strong>Cota</strong>
				   						</td>
				   						<td width="51" align="center" style=" border-bottom:1px solid #000; border-top:1px solid #000;border-left:1px solid #000;border-right:1px solid #000;">
				   							<strong>Qtde</strong>
				   						</td>
				 					</tr>
		  						</c:if>
							</c:forEach>
						</table>
					</c:forEach>
				</c:forEach>
			</c:forEach>
	</c:forEach>
	</td></tr>
</table>
</body>
</html>
