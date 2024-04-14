package com.portfolio.www;

import java.text.DecimalFormat;
import java.util.Date;

import com.portfolio.www.domain.ElectricBillForm;

public class Calculation {
	private final Date measureDate; // 사용일
	private final int quantity; // 사용 전력량

	private boolean summer; // 여름인지 아닌지

	private int discountTypeAmt1; // 정액 할인
	private String discountType1Exp; // 정액 할인 내용
	private double discountTypeAmt2; // 정률 할인
	private int totalDiscountAmt; // 정액 + 정률 할인

	private double basic; // 기본 요금
	private double wattage; // 전력량 요금
	public static final double[] quantityByWattage = { 200.0, 200.0 }; // 전력량 구간 별 사용 전력량
	public static final double[] amtByWattage = { 120.0, 214.6, 307.3 }; // 전력량 구간 별 적용 요금
	private double climate; // 기후 환경 요금

	private double meter; // 전기요금계
	private double gst; // 부가가치세
	private double development; // 전력 기반 기금

	private double billingAmtBefDisc; // 할인 전 청구 금액
	private double billingAmtAftDisc; // 할인 후 청구 금액

	// 외부에서 new 키워드를 사용해 생성자를 호출할 수 없게 접근 제어자를 private으로 변경
	private Calculation(ElectricBillForm form) {
		this.measureDate = form.getMeasureDate();
		this.quantity = (int) form.getElecAmount();

		setSummer();
		setDiscountTypeAmtAndExp1(form.getDiscountType1());
		setBasic();
		setWattage();
		setClimate();
		setMeter();
		setGst();
		setDevelopment();
		setBillingAmtBefDisc();
		setDiscountTypeAmt2(form.getDiscountType1(), form.getDiscountType2());
		setTotalDiscountAmt();
		setBillingAmtAftDisc();
	}

	private static Calculation instance;

	// Calculation 인스턴스가 생성되지 않은 경우 (null)일 때 한번만 생성
	// 그 외는 기존에 생성된 객체만 리턴함
	public static Calculation getInstance(ElectricBillForm form) {
		if (instance == null) {
			instance = new Calculation(form);
		}

		return instance;
	}

	public Date getMeasureDate() {
		return measureDate;
	}

	public void setSummer() {
		boolean summer = false;

		int month = getMeasureDate().getMonth();
		if (6 <= month && month <= 8) {
			summer = true;
		}

		this.summer = summer;
	}

	public boolean getSummer() {
		return summer;
	}

	public int getDiscountTypeAmt1() {
		return discountTypeAmt1;
	}

	public void setDiscountTypeAmtAndExp1(int discountType1) {
		int discountTypeAmt1 = 0;
		
		this.discountType1Exp = "없음";

		switch (discountType1) {
		case 1:
		case 2:
			this.discountType1Exp = "월 1만6천원, 여름철 2만원 한도";
			
			if (summer)
				discountTypeAmt1 = 20000;
			else
				discountTypeAmt1 = 16000;
			break;
		case 3:
			this.discountType1Exp = "월 1만원, 여름철 1만2천원 한도";
			
			if (summer)
				discountTypeAmt1 = 10000;
			else
				discountTypeAmt1 = 12000;
			break;
		case 4:
			this.discountType1Exp = "월 8천원, 여름철 1만원 한도";
			
			if (summer)
				discountTypeAmt1 = 8000;
			else
				discountTypeAmt1 = 10000;
			break;
		}

		this.discountTypeAmt1 = discountTypeAmt1;
	}

	public double getDiscountTypeAmt2() {
		return discountTypeAmt2;
	}

	public String getDiscountType1Exp() {
		return discountType1Exp;
	}

	public void setDiscountTypeAmt2(int discountType1, int discountType2) {
		double discountTypeAmt2 = 0;

		switch (discountType2) {
		case 1:
		case 2:
		case 3:
			if (getBillingAmtBefDisc() * 0.3 > 16000.0) {
				discountTypeAmt2 = 16000.0;
			} else {
				discountTypeAmt2 = getBillingAmtBefDisc() * 0.3;
			}

			break;
		}

		// 기초생활수급자 및 차상위계층 정액할인은 정률할인(30%)과 중복 적용 가능
		// = 나머지는 정률할인 적용 안됨
		if (discountType1 == 1)
			discountTypeAmt2 = 0;

		this.discountTypeAmt2 = discountTypeAmt2;
	}

	public int getTotalDiscountAmt() {
		return totalDiscountAmt;
	}

	private void setTotalDiscountAmt() {
		this.totalDiscountAmt = (int) (getDiscountTypeAmt1() + getDiscountTypeAmt2());
	}

	public int getQuantity() {
		return quantity;
	}

	public double getBasic() {
		return basic;
	}

	// 기본 요금 구하기
	public void setBasic() {
		double basic;

		// 200kWh이하 사용
		if (quantity <= quantityByWattage[0])
			basic = 910;
		// 201~400kWh 사용
		else if (quantity <= quantityByWattage[0] + quantityByWattage[1])
			basic = 1600;
		// 400kWh 초과 사용
		else
			basic = 7300;

		this.basic = basic;
	}

	// 전력량 요금 구하기
	public void setWattage() {
		double wattage = .0;

		// 1단계: 처음 200kWh 까지
		if (quantity <= quantityByWattage[0]) {
			wattage += quantity * amtByWattage[0];
			// 2단계: 다음 200kWh 까지
		} else if (quantity <= quantityByWattage[0] + quantityByWattage[1]) { // 200 < quantity <= 400
			wattage += quantityByWattage[0] * amtByWattage[0];
			wattage += (quantity - quantityByWattage[0]) * amtByWattage[1];
			// 3단계: 400kWh 초과
		} else { // 400 < quantity
			wattage += quantityByWattage[0] * amtByWattage[0];
			wattage += quantityByWattage[1] * amtByWattage[1];
			wattage += (quantity - (quantityByWattage[0] + quantityByWattage[1])) * amtByWattage[2];
		}

		this.wattage = wattage;
	}

	public double getWattage() {
		return wattage;
	}

	public String getWattageCalProc() {
		StringBuilder sb = new StringBuilder();

		DecimalFormat formatter = new DecimalFormat("###,###");
		int tempQuantity = getQuantity();
		// 각 구간별로 전력량 요금을 계산하고 출력
		for (int i = 1; i <= amtByWattage.length; i++) {
			// 다음 단계를 진행할 필요 없을 경우
			if (tempQuantity == 0)
				break;

			sb.append("<li>");

			int 계산량 = Math.min(tempQuantity, 200);
			sb.append(String.format("%d단계 : %dkWh × %.1f원 = %s원", i, 계산량, amtByWattage[i - 1],
					formatter.format(계산량 * amtByWattage[i - 1])));
			tempQuantity -= 계산량; // 사용량 갱신

			sb.append("</li>");
		}

		return sb.toString();
	}

	public double[] getAmtByWattage() {
		return quantityByWattage;
	}

	public double getClimate() {
		return climate;
	}

	// 기후 환경 요금 구하기
	public void setClimate() {
		this.climate = quantity * 9;
	}

	public double getMeter() {
		return meter;
	}

	// 전기 요금계 구하기
	public void setMeter() {
		this.meter = getBasic() + getWattage() + getClimate();
	}

	public double getGst() {
		return gst;
	}

	// 부가 가치세 구하기
	public void setGst() {
		// 전기 요금계의 10% 후 원 미만 반올림
		this.gst = Math.round(getMeter() * 10.0 / 100.0);
	}

	public double getDevelopment() {
		return development;
	}

	// 전력 기반 기금 구하기
	public void setDevelopment() {
		// 전기 요금계의 3.7% 후 (10원 미만 절사)
		this.development = ((int) (getMeter() * 3.7 / 100.0)) / 10 * 10;
	}

	public double getBillingAmtBefDisc() {
		return billingAmtBefDisc;
	}

	// 청구 금액 구하기 (10원 미만 절사)
	public void setBillingAmtBefDisc() {
		this.billingAmtBefDisc = ((int) (getMeter() + getGst() + getDevelopment())) / 10 * 10;
	}

	public double getBillingAmtAftDisc() {
		return billingAmtAftDisc;
	}

	public void setBillingAmtAftDisc() {
		this.billingAmtAftDisc = ((int) (getBillingAmtBefDisc() - getTotalDiscountAmt())) / 10 * 10;
	}

//	@Override
//	public String toString() {
//		return String.format(
//				"기본요금 : %,.0f 원\n" + "전력량요금 : %,.0f 원\n" + "기후환경요금 : %,.0f 원\n" + "전기요금계 : %,.0f 원\n"
//						+ "부가가치세 : %,.0f 원 (원미만 반올림)\n" + "전력기반기금 : %,.0f 원 (10원미만절사)\n" + "청구금액 : %,.0f 원 (10원미만절사)",
//				getBasic(), getWattage(), getClimate(), getMeter(), getGst(), getDevelopment(), getBillingAmt());
//	}
}
