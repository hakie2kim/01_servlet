<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="java.util.*, java.text.*, com.portfolio.www.domain.ElectricBillForm"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
String measureDate = (String) request.getParameter("measureDate");
System.out.println(measureDate);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>전기요금 계산</title>
<style>
body {
	font-size: 11px;
}

table {
	border-width: thin;
}
</style>
</head>
<body>
	<%=measureDate%>
	${measureDate}
	<table width="600" align="center" border="1">
		<tr>
			<th colspan="4">전기요금 계산 결과</th>
		</tr>
		<tr>
			<td colspan="2"><b>사용일</b></td>
			<td colspan="2"><fmt:formatDate value="${calc.measureDate}"
					pattern="yyyy-MM-dd" /></td>
		</tr>
		<tr>
			<td colspan="2"><b>사용량(KWh)</b></td>
			<td colspan="2">${calc.quantity}KWh</td>
		</tr>
		<tr>
			<td colspan="2"><b>정액할인</b></td>
			<td colspan="2"><fmt:formatNumber
					value="${calc.discountTypeAmt1}" pattern="#,###" /> 원</td>
		</tr>
		<tr>
			<td colspan="2"><b>정액할인 내용</b></td>
			<td colspan="2">${calc.discountType1Exp}</td>
		</tr>
		<tr>
			<td colspan="2"><b>정률할인(30%)</b></td>
			<td colspan="2"><fmt:formatNumber
					value="${calc.discountTypeAmt2}" pattern="#,###" /> 원</td>
		</tr>
		<tr>
			<td colspan="4" align="center"><b> 요금 계산 </b></td>
		</tr>
		<tr>
			<td colspan="4">① 기본요금 : <fmt:formatNumber value="${calc.basic}"
					pattern="#,###" /> 원 (3단계 단가)<br /> ② 전력량요금 : <fmt:formatNumber
					value="${calc.wattage}" pattern="#,###" /> 원 (1~3단계 요금의 합계, 원미만절사)
				<br />
				<ul>${wattageCalcProc}</ul> ③ 기후환경요금 : ${calc.quantity}kWh × 9원 = <fmt:formatNumber
					value="${calc.climate}" pattern="#,###" />원 <br /> ④ 연료비조정요금 :
				${calc.quantity}kWh × 5원 = <fmt:formatNumber
					value="${calc.quantity * 5}" pattern="#,###" />원 <br /> ⑤ 전기요금계 :
				<fmt:formatNumber value="${calc.meter}" pattern="#,###" />원 = <fmt:formatNumber
					value="${calc.basic}" pattern="#,###" />원(①) + <fmt:formatNumber
					value="${calc.wattage}" pattern="#,###" />원(②) + <fmt:formatNumber
					value="${calc.climate}" pattern="#,###" />원(③) <br /> ⑥ 부가가치세 : <fmt:formatNumber
					value="${calc.meter}" pattern="#,###" />원(⑤) × 10% = <fmt:formatNumber
					value="${calc.gst}" pattern="#,###" />원 (원미만 반올림)<br /> ⑦ 전력기반기금 <fmt:formatNumber
					value="${calc.meter}" pattern="#,###" />원(⑤) × 3.7% = <fmt:formatNumber
					value="${calc.development}" pattern="#,###" />원 (10원미만절사)<br /> ⑧
				할인금액 : <fmt:formatNumber value="${calc.totalDiscountAmt}"
					pattern="#,###" />원 <br /> ⑨ 청구금액 : <fmt:formatNumber
					value="${calc.meter}" pattern="#,###" />원(⑤) + <fmt:formatNumber
					value="${calc.gst}" pattern="#,###" />원(⑥) + <fmt:formatNumber
					value="${calc.development}" pattern="#,###" />원(⑦) - <fmt:formatNumber
					value="${calc.totalDiscountAmt}" pattern="#,###" />원(⑧) = <fmt:formatNumber
					value="${calc.billingAmtAftDisc}" pattern="#,###" />원 (10원미만절사)
			</td>
		</tr>
	</table>
</body>
</html>