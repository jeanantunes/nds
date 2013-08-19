<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/NDS.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Nota Promissória</title>
<style>
table {
	border: 2px solid #000;
}
</style>

</head>
<body>

	<table width="700" border="0" cellspacing="2" cellpadding="2">
		<tr>
			<td width="690" align="right"><strong>Vencimento <fmt:formatDate value="${nota.notaPromissoria.vencimento}" pattern="dd', de' MMMM 'de' yyyy"/></strong></td>
		</tr>
		<tr>
			<td>
				<div style="float: left">
					<strong>Número: </strong> ${nota.notaPromissoria.id }
				</div>

				<div style="float: right">
					<strong>Valor R$: </strong> <fmt:formatNumber type="NUMBER" value="${nota.notaPromissoria.valor}" minFractionDigits="2" ></fmt:formatNumber>
				</div>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>Ao(s)  <fmt:formatDate value="${nota.notaPromissoria.vencimento}" pattern="dd', de' MMMM 'de' yyyy"/> pagar(emos) por
				esta única via de <strong>NOTA PROMISSÓRIA</strong> a  ${nota.nomeBeneficiario} CPF ou CNPJ ${nota.documentoBeneficiario} ou à sua
				ordem, a quantia de <strong>R$ ${nota.notaPromissoria.valorExtenso}</strong> em moeda
				corrente deste país, pagável em <strong>${nota.pracaPagamento}</strong>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<div align="left">
					<strong>EMITENTE: </strong>${nota.nomeEmitente}
				</div>

				<div align="right">
					<strong>Data / Local: </strong><fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd', de' MMMM 'de' yyyy"/>, ${nota.pracaPagamento}
				</div>

			</td>

		</tr>

		<tr>

			<td><p>
					<strong>CPF / CNPJ: </strong>${nota.documentoEmitente}
				</p></td>

		</tr>

		<tr>

			<td>

				<div align="left">
					<strong>ENDEREÇO: </strong> ${nota.enderecoEmitente.tipoLogradouro}: ${nota.enderecoEmitente.logradouro}, ${nota.enderecoEmitente.numero} - ${nota.enderecoEmitente.bairro}, ${nota.enderecoEmitente.cidade} - ${nota.enderecoEmitente.uf} ${nota.enderecoEmitente.cep}
				</div>

				<div align="right">
					<strong>------------------------------------------------------------</strong>
				</div>

			</td>

		</tr>

		<tr>

			<td align="right">${nota.nomeEmitente}</td>

		</tr>

	</table>

</body>
</html>