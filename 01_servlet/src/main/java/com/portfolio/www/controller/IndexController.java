package com.portfolio.www.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portfolio.www.Calculation;
import com.portfolio.www.domain.ElectricBillForm;

@Controller
public class IndexController {
	public IndexController() {
		System.out.println("--------------------생성됨--------------------");
	}
	
	@RequestMapping("/index.do")
	public String indexPage() {
		return "index";
	}
	
	@RequestMapping("/input.do")
	public String inputPage() {
		return "input";
	}
	
	@RequestMapping("/output.do")
//	public ModelAndView outputPage(
	public String outputPage(
//			@RequestParam HashMap<String, String> params
			@ModelAttribute ElectricBillForm form,
			Model model
			) {	
//		System.out.println(params);

//		ModelAndView mv = new ModelAndView();
//		mv.setViewName("output");
//		mv.addObject("measureDate", "2024-04-13");
//		return mv;
		
		Calculation calc = Calculation.getInstance(form);
				
		/*
		 * boolean summer = false; int month = form.getMeasureDate().getMonth(); if (6
		 * <= month && month <= 8) { summer = true; }
		 * 
		 * // 정액할인 int discountTypeAmt1 = 0;
		 * 
		 * switch(form.getDiscountType1()) { case 1, 2: if (summer) discountTypeAmt1 =
		 * 20000; else discountTypeAmt1 = 16000; break; case 3: if (summer)
		 * discountTypeAmt1 = 10000; else discountTypeAmt1 = 12000; break; case 4: if
		 * (summer) discountTypeAmt1 = 8000; else discountTypeAmt1 = 10000; break; }
		 * 
		 * // 정률 할인 double discountTypeAmt2 = calc.getBillingAmt() * 0.3;
		 * 
		 * switch(form.getDiscountType2()) { case 1, 2, 3: if (calc.getBillingAmt() *
		 * 0.3 > 16000.0) { discountTypeAmt2 = 16000.0; } break; }
		 * 
		 * // 기초생활수급자 및 차상위계층 정액할인은 정률할인(30%)과 중복 적용 가능 if(form.getDiscountType1() == 1)
		 * discountTypeAmt2 = 0;
		 * 
		 * model.addAttribute("discountTypeAmt1", discountTypeAmt1);
		 * model.addAttribute("discountTypeAmt2", discountTypeAmt2);
		 */
		model.addAttribute("calc", calc);
		model.addAttribute("wattageCalcProc", calc.getWattageCalProc());

		return "output";
	}
}