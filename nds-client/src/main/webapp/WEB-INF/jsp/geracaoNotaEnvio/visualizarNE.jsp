<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>STG - Sistema Treelog de GestÃ£o</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/NDS.css" />
<style type="text/css">

body { font-size: 11px!important; }
td { line-height: 20px!important; padding-left:3px; padding-right:3px; }
.titulo{ color:#333; font-size:10px;!important; }
.dadosNota{ color:#000; }
.class_linha_3 {
	background: #ccc;
	line-height: 28px;
}
</style>
</head>
<body>
<c:if test="${errorMessage != null}">
	<table width="760" border="0" align="center" cellpadding="0"
		cellspacing="0" style="margin-bottom: 10px; margin-top: 10px;">
		<tr>
			<td width="124" rowspan="8" valign="top"><span>
				<c:out value="${errorMessage}" />
				</span>
			</td>
		</tr>
	</table>
</c:if>

<c:if test="${notaEnvio != null}">

<c:forEach items="${notaEnvio}" var="notaEnvio">

	<table width="760" border="0" align="center" cellpadding="0"
		cellspacing="0" style="margin-bottom: 10px; margin-top: 10px;">
		<tr>
			<td width="124" rowspan="8" valign="middle"><span>
				<img width="120" align="middle" src="${pageContext.request.contextPath}/administracao/parametrosDistribuidor/getLogo?number=${pageContext.request.requestedSessionId}" border="0" />
				</span></td>
			<td colspan="4"
				style="border-left: 1px solid #000; border-top: 1px solid #000;"><span
				class="titulo">Razão Social<br />
			</span></td>
			<td width="194" align="center" style="border-left: 1px solid #000;"><span
				class="titulo" style="font-size: 13px !important;"><strong>NOTA
						DE ENVIO</strong></span></td>
			<td width="20" align="right"><span class="titulo"
				style="font-size: 13px !important;"><strong></strong> <c:if test="${notaEnvio.numero!= null}">Número</c:if></span></td>
			<td width="116" align="center"><span class="dadosNota"><strong
					style="font-size: 14px !important;"><c:out value="${notaEnvio.numero}"/> </strong></span></td>
		</tr>
		<tr>
			<td colspan="4"
				style="border-left: 1px solid #000; border-bottom: 1px solid #000;"><span
				class="dadosNota"><c:out value="${notaEnvio.emitente.nome}"/></span></td>
			<td rowspan="2" align="center"
				style="border-left: 1px solid #000; border-bottom: 1px solid #000;">Nota
				de Envio aprovada nos termos do Regime especial</td>
			<td rowspan="2" style="border-bottom: 1px solid #000;">&nbsp;</td>
			<td rowspan="2" align="center" style="border-bottom: 1px solid #000;"><span>Via
					de Transporte Rodoviário</span></td>
		</tr>
		<tr>
			<td width="119" style="border-left: 1px solid #000;"><span
				class="titulo">Endereço<br />
			</span></td>
			<th width="19">&nbsp;</th>
			<th width="62">&nbsp;</th>
			<td width="106" align="center"><span class="titulo">CNPJ</span></td>
		</tr>
		<tr>
			<td colspan="2"
				style="border-left: 1px solid #000; border-bottom: 1px solid #000;"><span
				class="dadosNota"><c:out value="${notaEnvio.emitente.endereco.tipoLogradouro}"/>&nbsp;<c:out value="${notaEnvio.emitente.endereco.logradouro}"/> <br />
			</span></td>
			<td style="border-bottom: 1px solid #000;"><span
				class="dadosNota"><c:out value="${notaEnvio.emitente.endereco.numero}"/></span></td>
			<td align="center" style="border-bottom: 1px solid #000;"><span
				class="dadosNota"><c:out value="${notaEnvio.emitente.documento}"/></span></td>
			<td style="border-left: 1px solid #000;">&nbsp;</td>
			<td>&nbsp;</td>
			<td style="border-right: 1px solid #000;">&nbsp;</td>
		</tr>
		<tr>
			<td style="border-left: 1px solid #000;"><span class="titulo">Cidade</span></td>
			<td align="center"><span class="titulo">UF</span></td>
			<td align="center"><span class="titulo">CEP</span></td>
			<td align="center"><span class="titulo">Inscrição Estadual</span></td>
			<td style="border-left: 1px solid #000;"><c:if test="${notaEnvio.chaveAcesso != null}">Chave NF-e</c:if>&nbsp;</td>
			<td>&nbsp;</td>
			<td style="border-right: 1px solid #000;">&nbsp;</td>
		</tr>
		<tr>
			<td
				style="border-left: 1px solid #000; border-bottom: 1px solid #000;"><span
				class="dadosNota"><c:out value="${notaEnvio.emitente.endereco.cidade}"/></span></td>
			<td align="center" style="border-bottom: 1px solid #000;"><span
				class="dadosNota"><c:out value="${notaEnvio.emitente.endereco.uf}"/></span></td>
			<td align="center" style="border-bottom: 1px solid #000;"><span
				class="dadosNota"><c:out value="${notaEnvio.emitente.endereco.cep}"/></span></td>
			<td align="center" style="border-bottom: 1px solid #000;"><span
				class="dadosNota"><c:out value="${notaEnvio.emitente.inscricaoEstadual}"/></span></td>
			<td style="border-left: 1px solid #000;  border-bottom: 1px solid #000;"><c:out value="${notaEnvio.chaveAcesso}"/>&nbsp;</td>
			<td style="border-bottom: 1px solid #000;">&nbsp;</td>
			<td style="border-right: 1px solid #000; border-bottom: 1px solid #000;">&nbsp;</td>
		</tr>
	</table>
	<table width="760" border="0" align="center" cellpadding="0"
		cellspacing="0" style="margin-bottom: 10px;">
		<tr>
			<td colspan="3"
				style="border-left: 1px solid #000; border-top: 1px solid #000;"><span
				class="titulo">Destinatário<br />
			</span></td>
			<td colspan="3"
				style="border-left: 1px solid #000; border-top: 1px solid #000;"><span
				class="titulo">Box / Rota</span></td>
			<td width="124"
				style="border-left: 1px solid #000; border-top: 1px solid #000; border-right: 1px solid #000;"><span
				class="titulo">Data de Lançamento</span></td>
		</tr>
		<tr>
			<td colspan="3"
				style="border-left: 1px solid #000; border-bottom: 1px solid #000;"><span style="font-size: 14px;"
				class="dadosNota"><strong><c:out value="${notaEnvio.destinatario.numeroCota}"/> - <c:out value="${notaEnvio.destinatario.nome}"/></strong></span></td>
			<td colspan="3"
				style="border-left: 1px solid #000; border-bottom: 1px solid #000;"><span
				class="dadosNota">BOX:<c:out value="${notaEnvio.destinatario.codigoBox}"/> - ROTA: <c:out value="${notaEnvio.destinatario.descricaoRota}"/></span></td>
			<td
				style="border-left: 1px solid #000; border-bottom: 1px solid #000; border-right: 1px solid #000;text-align: center;">
			<span class="dadosNota">${dataLancamento}</span></td>
		</tr>
		<tr>
			<td width="232" style="border-left: 1px solid #000;"><span
				class="titulo">Endereço </span></td>
			<td width="39" style="border-left: 1px solid #000;"><span
				class="titulo">Número</span></td>
			<td width="105"
				style="border-right: 1px solid #000; border-left: 1px solid #000;"><span
				class="titulo">Cidade</span></td>
			<td width="36" align="center" style="border-right: 1px solid #000;"><span
				class="titulo">UF</span></td>
			<td width="90" align="center" style="border-right: 1px solid #000;"><span
				class="titulo">CEP</span></td>
			<td width="134" align="center" style="border-right: 1px solid #000;"><span
				class="titulo">CPF/CNPJ</span></td>
			<td align="center" style="border-right: 1px solid #000;"><span
				class="titulo">RG/Inscrição Estadual</span></td>
		</tr>
		<tr>
			<td
				style="border-left: 1px solid #000; border-bottom: 1px solid #000;"><span
				class="dadosNota"><c:out value="${notaEnvio.destinatario.endereco.tipoLogradouro}"/>&nbsp;<c:out value="${notaEnvio.destinatario.endereco.logradouro}"/><br />
			</span></td>
			<td
				style="border-left: 1px solid #000; border-bottom: 1px solid #000;"><span
				class="dadosNota"><c:out value="${notaEnvio.destinatario.endereco.numero}"/></span></td>
			<td
				style="border-bottom: 1px solid #000; border-right: 1px solid #000; border-left: 1px solid #000;"><span
				class="dadosNota"><c:out value="${notaEnvio.destinatario.endereco.cidade}"/></span></td>
			<td align="center"
				style="border-bottom: 1px solid #000; border-right: 1px solid #000;"><span
				class="dadosNota"><c:out value="${notaEnvio.destinatario.endereco.uf}"/></span></td>
			<td align="center"
				style="border-bottom: 1px solid #000; border-right: 1px solid #000;"><span
				class="dadosNota"><c:out value="${notaEnvio.destinatario.endereco.cep}"/></span></td>
			<td align="center"
				style="border-bottom: 1px solid #000; border-right: 1px solid #000;"><span
				class="dadosNota"><c:out value="${notaEnvio.destinatario.documento}" /></span></td>
			<td align="center"
				style="border-bottom: 1px solid #000; border-right: 1px solid #000;"><span
				class="dadosNota"><c:out value="${notaEnvio.destinatario.inscricaoEstadual}"/></span></td>
		</tr>
	</table>
	<table width="760" border="0" align="center" cellpadding="0"
		cellspacing="0" style="margin-top: 5px;">
		<tr class="class_linha_3">
			<td class="relatorios"
				style="padding-left: 5px; border-left: 1px solid #000; border-top: 1px solid #000; border-bottom: 1px solid #000;"><strong>Código</strong></td>
			<td class="relatorios"
				style="padding-left: 5px; border-left: 1px solid #000; border-top: 1px solid #000; border-bottom: 1px solid #000;"><strong>Publicação</strong></td>
			<td class="relatorios"
				style="padding-left: 5px; border-left: 1px solid #000; border-top: 1px solid #000; border-bottom: 1px solid #000; border-right: 1px solid #000;"><strong>Edição</strong></td>
			<td align="right" class="relatorios"
				style="padding-left: 5px; border-top: 1px solid #000; border-bottom: 1px solid #000; border-right: 1px solid #000;"><strong>Preço
					Capa R$</strong></td>
			<td align="center" class="relatorios"
				style="padding-left: 5px; border-top: 1px solid #000; border-bottom: 1px solid #000;"><strong>Reparte</strong></td>
			<td align="center" class="relatorios"
				style="padding-left: 5px; border-left: 1px solid #000; border-top: 1px solid #000; border-bottom: 1px solid #000; border-right: 1px solid #000;"><strong>Sequência</strong></td>
		</tr>
		<c:set  var="totalPrecoCapa" value="0"/>
		<c:set  var="totalComDesconto" value="0"/>
		<c:set  var="totalRepartes" value="0"/>
		<c:forEach items="${notaEnvio.listaItemNotaEnvio}" var="itemNotaEnvio" varStatus="status">
			<c:set var="totalPrecoCapa" value="${totalPrecoCapa + (itemNotaEnvio.precoCapa * itemNotaEnvio.reparte)}" />
			<c:set var="totalComDesconto" value="${totalComDesconto + ((itemNotaEnvio.precoCapa * (100 - itemNotaEnvio.desconto) / 100) * itemNotaEnvio.reparte)}" />
			<c:set var="totalRepartes" value="${totalRepartes + itemNotaEnvio.reparte}" />
		
			<tr class="class_linha_${(status.index % 2) +1}">
				<td width="110"
					style="border-left: 1px solid #000; border-bottom: 1px solid #000; padding-left: 5px;"><c:out value="${itemNotaEnvio.codigoProduto}" /></td>
				<td width="331"
					style="border-left: 1px solid #000; border-bottom: 1px solid #000; padding-left: 5px;"><c:out value="${itemNotaEnvio.publicacao}" /></td>
				<td width="79"
					style="border-left: 1px solid #000; border-bottom: 1px solid #000; border-right: 1px solid #000; padding-left: 5px;"><c:out value="${itemNotaEnvio.numeroEdicao}" /></td>
				<td width="92" align="right"
					style="border-bottom: 1px solid #000; border-right: 1px solid #000; padding-left: 5px;"><fmt:formatNumber value="${itemNotaEnvio.precoCapa}" type="NUMBER"  minFractionDigits="2"  /></td>
				<td width="80" align="center"
					style="border-bottom: 1px solid #000; padding-left: 5px;"><c:out value="${itemNotaEnvio.reparte}" /></td>
				<td width="68" align="center"
					style="border-left: 1px solid #000; border-bottom: 1px solid #000; border-right: 1px solid #000; padding-left: 5px;"><c:out value="${itemNotaEnvio.sequenciaMatrizLancamento}" /></td>
			</tr>		
		</c:forEach>
		
	</table>
	<table width="760" border="0" align="center" cellpadding="1"
		cellspacing="1">
		<tr>
			<td>&nbsp;</td>
			<td align="right">&nbsp;</td>
			<td align="right">&nbsp;</td>
		</tr>
		<tr>
			<td width="430">&nbsp;</td>
			<td width="203" align="right"><strong>TOTAL PREÇO CAPA
					R$</strong></td>
			<td width="117" align="right" style="border-bottom: 1px solid #000;"><fmt:formatNumber value="${totalPrecoCapa}" type="NUMBER" minFractionDigits="2" maxFractionDigits="2" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="right"><strong>TOTAL PREÇO COM DESCONTO R$</strong></td>
			<td align="right" style="border-bottom: 1px solid #000;"><fmt:formatNumber value="${totalComDesconto}" type="NUMBER" minFractionDigits="2" maxFractionDigits="2"  /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="right"><strong>DESCONTO R$</strong></td>
			<td align="right" style="border-bottom: 1px solid #000;">
				<c:if test="${BigDecimal.ZERO.compareTo(totalPrecoCapa)!=0}">
					<fmt:formatNumber value="${(totalPrecoCapa - totalComDesconto)}" type="NUMBER" minFractionDigits="2" maxFractionDigits="2"/>
				</c:if>
				<c:if test="${BigDecimal.ZERO.compare(totalPrecoCapa)==0}">
					0,00
				</c:if>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="right"><strong>TOTAL REPARTE DO DIA</strong></td>
			<td align="right" style="border-bottom: 1px solid #000;"><c:out value="${totalRepartes}"></c:out>
				Exemplares</td>
		</tr>
	</table>
	
	<br></br>
	<br></br>
</c:forEach>
	
</c:if>

</body>
</html>