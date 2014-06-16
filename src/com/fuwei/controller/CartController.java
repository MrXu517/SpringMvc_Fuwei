package com.fuwei.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.LoginedUser;
import com.fuwei.constant.Constants;


/**
 * 
 * @author huchao
 * 
 */
@RequestMapping("/cart")
@Controller
public class CartController extends BaseController {

//	@Resource
//	private GoodService goodService;
//
//	@Resource
//	private CartService cartService;
//
//	@Resource
//	private DeliveryService deliveryService;
//
//	@Resource
//	private OrderService orderService;

	/**
	 * 购物车页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response) {

//		LoginedUser user = (LoginedUser) request.getSession().getAttribute(
//				Constants.LOGIN_SESSION_NAME);

//		List<Cart> carts = null;

//		if (user == null) {
//			Cookie cartCookie = CookieUtils.getCookieByName(request,
//					Constants.CART_SESSION_NAME);
//			if (cartCookie != null) {
//				String cartsStr = cartCookie.getValue();
//				carts = SerializeTool.deserialize(cartsStr, List.class);
//			}
//		} else {
//			carts = cartService.list(user.getLoginedUser().getUname());
//		}

//		if (carts != null) {
//			for (Cart item : carts) {
//				Good good = goodService.findSimplebyId(item.getGoodid());
//				item.setGood(good);
//			}
//		}
//		request.setAttribute("cartList", carts);
		return new ModelAndView("cart/index");
	}
}
