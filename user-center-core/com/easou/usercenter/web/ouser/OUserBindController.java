package com.easou.usercenter.web.ouser;

//@Controller
//@RequestMapping("/ouserBind")
public class OUserBindController {

//	private Logger log = Logger.getLogger(OUserBindController.class);
//
//	@Resource(name="ouserBindService")
//	private OutUserBindService outUserBindService;
//
//	@RequestMapping(method = RequestMethod.GET)
//	public String show(ModelMap model, final HttpServletRequest request,
//			final HttpServletResponse response) {
//		ThirdPartUserInfo tinfo = (ThirdPartUserInfo) request.getSession()
//				.getAttribute(OUserConstant.THIRDPART_INFO_SESSION);
//		if (null == tinfo) {
//			log.debug("第三方登录信息不存在");
//			return "redirect:/login";
//		} else {
//			model.addAttribute("formUser", new FormUser());
//			return "default/ui/viewBiding";
//		}
//	}
//
//	@RequestMapping(method = RequestMethod.POST)
//	public String submit(@ModelAttribute("formUser") FormUser formUser,
//			BindingResult result, final HttpServletRequest request,
//			final HttpServletResponse response) {
//        //客户端ESID
//		String clientEsid = ConditionUtil.getEsid(request);
//		//渠道号
//		String channel = ConditionUtil.getChannel(request);
//		//UID
//		String uid = ConditionUtil.getUid(request);
//		try {
//			boolean pass = true;
//			if (formUser.getUsername() == null
//					|| "".equals(formUser.getUsername().trim())) {
//				result.reject("FormUser.username[not.blank]", "登录名不能为空");
//			}
//			if (formUser.getPassword() == null
//					|| "".equals(formUser.getPassword().trim())) {
//				result.reject("FormUser.password[not.blank]", "密码不能为空");
//			}
//			if (!pass) {
//				return "default/ui/viewBiding";
//			}
//			ThirdPartUserInfo tinfo = (ThirdPartUserInfo) request
//					.getSession().getAttribute(
//							OUserConstant.THIRDPART_INFO_SESSION);
//			// 绑定用户信息
//			EucUser user = outUserBindService.bindingUser(formUser.getUsername(), formUser
//					.getPassword(), tinfo.getThirdId(), tinfo.getType(),tinfo.getNickName());
//			request.getSession().removeAttribute(OUserConstant.THIRDPART_INFO_SESSION);
//			// 设置easouId
//			tinfo.setEasouId(String.valueOf(user.getId()));
//			request.setAttribute(OUserConstant.THIRDPART_INFO_SESSION, tinfo);
//			if (null != request.getQueryString()) {
//				return new StringBuffer("forward:/extLogin?")
//				.append(request.getQueryString()).toString();
//			} else {
//				return "forward:/extLogin";
//			}
//		} catch (ValidateException vx) {
//			log.error("外部用户绑定失败");
//			String service = request.getParameter("service");
//			// 记录登录异常日志
//			BizLogUtil.loginLog("", formUser != null ? formUser.getUsername()
//					: "", Way.PAGE, LogConstant.RESULT_FAILURE,
//					LoginType.WEIBO_SINA, service != null ? service : "", "", "",channel,clientEsid,LogConstant.LOGIN_RESULT_TGC_ERROR,clientEsid,uid);
//			result.reject("validateFail", vx.getMessage());
//			return "default/ui/viewBiding";
//		}
//	}
}
