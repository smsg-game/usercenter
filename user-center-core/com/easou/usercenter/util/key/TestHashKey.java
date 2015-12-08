package com.easou.usercenter.util.key;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestHashKey {
	public static void main(String args[]) throws Exception {
		long ct = System.currentTimeMillis();

		// 数据测试
		List<Key> testList = new ArrayList<Key>();
		TestKey k1 = new TestKey("黄网", "",'A');
		TestKey k2 = new TestKey("黄色书", "",'A');
		TestKey k3 = new TestKey("操他妈", "",'A');
		TestKey k4 = new TestKey("生活", "爱生活爱梵町",'A');
		TestKey k5 = new TestKey("装B", "",'A');
		TestKey k6 = new TestKey("我靠", "",'A');
		TestKey k7 = new TestKey("他妈的逼", "",'A');
		TestKey k8 = new TestKey("他妈的", "",'A');
		TestKey k9 = new TestKey("奶奶的", "",'A');
		TestKey k10 = new TestKey("黄军", "HJ",'A');

		TestKey k11 = new TestKey("必要", "手机",'B');
		TestKey k12 = new TestKey("充满", "",'C');
		TestKey k13 = new TestKey("成功", "爱生活爱梵町",'D');
		TestKey k14 = new TestKey("zc", "zzcc",'D');
		TestKey k15 = new TestKey("L0b0n", "",'D');
		TestKey k16 = new TestKey("L0B0N", "",'D');
		TestKey k17 = new TestKey("L0BON", "",'D');
		TestKey k18 = new TestKey("LOB0N", "",'D');
		TestKey k19 = new TestKey("ddd", "",'D');
		TestKey k20 = new TestKey("cn", "",'D');

		testList.add(k1);
		testList.add(k2);
		testList.add(k3);
		testList.add(k4);
		testList.add(k5);
		testList.add(k6);
		testList.add(k7);
		testList.add(k8);
		testList.add(k9);
		testList.add(k10);

		testList.add(k11);
		testList.add(k12);
		testList.add(k13);
		testList.add(k14);
		testList.add(k15);
		testList.add(k16);
		testList.add(k17);
		testList.add(k18);
		testList.add(k19);
		testList.add(k20);

		// 载入KEY
		long totalMem = java.lang.Runtime.getRuntime().freeMemory();
		HashKey hk = HashKey.getInstance();
		if(hk.getHashTable().isEmpty()){
			hk.clear();
			System.out.println("空的。。。。。。");
		}else{
			hk.clear();
			System.out.println("非空。。。。。");
		}
		System.out.println("k================"+hk);
		hk = HashKey.getInstance();
		System.out.println("k1================"+hk);

		hk.loadKey(testList);
		if(hk.getHashTable().isEmpty()){
			hk.clear();
			System.out.println("空的1。。。。。。");
		}else{
			hk.clear();
			System.out.println("非空1。。。。。");
		}
		hk.loadKey(testList);
		// 存储位置测试
		Iterator it = hk.getHashTable().keySet().iterator();;
		while (it.hasNext()) {
			Character c = (Character) it.next();
			if (hk.getHashTable().get(c) == null) {
				continue;
			}
			KeyPattern[] pArr = ((Upl) hk.getHashTable().get(c)).getUnionKeyPattern()
					.getKeyPattern();
			for (KeyPattern tp : pArr) {
				if (tp == null)
					continue;
				System.out.print(" ---" + tp.getWord());
			}
			System.out.println();
		}
		System.out.println("=============findMatchWordCount测试");
		int[] wordLen = hk.findMatchWordCount('z');
		if(wordLen!=null)
		for (int i : wordLen) {
			System.out.print("---" + i);
		}

		// word查找测试
		System.out.println("\n=============findMatchWord测试");
		System.out.println("test " + hk.findMatchByWord("奶奶的", "", false,"A"));

		// 文章Hash查找替换测试
		System.out
				.println("\n=============findOrRplMatchByContent文章Hash查找替换测试");
		String content = "为配合政府Zc部门扫黄，社区将推行z5678C厉的审帖政策//亲爱的友友们://   大家好!为配合政府部门扫黄,给广大网民和青少年提供一个绿色、健康、文明、和谐的网络环境,社区将推行严厉的审帖政策。近期,将会对社区论坛、歪歪吧涉黄帖进行清理,如给友友们带来不便,敬请谅解!另外,若发现有友友发黄贴三次或三张以上,将给予直接屏蔽ID一周的处罚,情节严重者一律以封IP处理。以上,希望友友们给予极力支持与配合,若有疑问者请及时留言于系管,谢谢!欢迎友友们及时举报有关涉黄帖!最后,祝友友们周末愉快!";
		//content="   好想有个妻子,为我做饭烧菜。现实却很无奈,让我仍需等待。也因寂寞难耐,谈过几次恋爱。谁知屡战屡败,轻轻松松被踹。其实我也奇怪,为啥总被淘汰。历尽打击伤害,总算知道大概。嫌我不讲穿戴,嫌我长得不帅。嫌我个头太矮,嫌我没有气派。熊猫长得不帅,却受世人关爱。丑是自然灾害,矮是因为缺钙。做人只求正派,讲啥穿戴气派!我们这个年代,注定缺少真爱。女人就是太坏,心胸还很狭隘。或许除此之外,还有部分可爱。只怕时至现在,早已有了后代。面对这种事态,不要气急败坏。我们除了忍耐,至少还能等待。只要相信真爱,她就一定存在。要么咱就不爱,爱就爱个痛快。没有爱的灌溉,生活百无聊赖。只有好的心态,才能保持愉快。爱情也有好赖,绝对不可草率,我是愿意等待,哪怕青春不常在.&nbsp; ";
		content="第三轮abABＡＢ用户反馈色情小说zc处理结果，及z345c奖名单公布啦！梵町小说搜索响应政府号召,坚持文明办网,促进互联网事业健康发展为目的的前提下，组织举行了举报问题小说得话费奖励的专项活动，得到了广大网友的积极响应，现根据活动规则评选出举报有用信息最多的三位梵町友，梵町号：zyd东东、大唐遗失的公主、zhijieru，三位梵町友将分别获得50块钱话费奖励。此次活动还会持续进行，还没获奖的用户还不马上参与进来。提醒大家一定要登录留言，有些用户反馈的很好，但是您没有登录，非常遗憾我们无法把话费奖励给您。//小说第三轮用户反馈色情小说删除屏蔽名单如下：都市邪修、无敌浪子、无赖群芳谱、情迷女人香、香村风月、乡村情欲、疯痴女人苦命汉、龙吟百美缘、少年大宝、天降神妻、天降神妻の午夜风流、摧花狂魔、昊家姊妹 2 • 摧花狂魔、热情小娇妻、总裁的暖床秘书、异界之极品色狼、风流秘史、艳遇修真、情天大帝、少龙风流、巫山云雨、邪气少年：巫山云雨、游龙戏凤、夜夜激情。";
//		content="MA==,uePW3Q==,0su0ug==,SzMyOA==,MTIwMw==,MA==,MQ==,MTE2,xcvSqw==,MTM2MjI2OTIxNjc=";
		content="废物！沦『女干』你妈！强［女干干你你干干女］你妈！(";
		System.out.println("替换测试==="
				+ hk.findOrRplMatchByContent(content, "爱生活,爱梵町", false,"ABCD"));
		System.out.println("替换测试1==="
				+ hk.findOrRplMatchByContent(content, HashKey.TAG.B, false,"ABCD"));
		System.out.println("替换测试2==="
				+ hk.findOrRplMatchByContent(content, HashKey.TAG.I, false,"ABCD"));
		System.out.println("替换测试3==="
				+ hk.findOrRplMatchByContent(content, HashKey.TAG.U, false,"ABCD"));
		System.out.println("替换测试4==="
				+ hk.findOrRplMatchByContent(content, HashKey.TAG.BIG, false,"ABCD"));
		System.out.println("替换测试5==="
				+ hk.findOrRplMatchByContent(content, HashKey.TAG.SMALL, false,"ABCD"));
		System.out.println("象形匹配==="
				+ hk.findOrRplMatchByContent(content, "", true,"B"));
		System.out.println("不过滤==="
				+ hk.findOrRplMatchByContent(content, "", false,"A"));
		System.out.println("为空==="
				+ hk.findOrRplMatchByContent(null, "", false,"A"));
		System.out.println("为空==="
				+ hk.findOrRplMatchByContent("([ddssssss", "", false,"ABCD"));
		System.out.println("T   "+FilterUtil.filterChar(content));

		//有效字符测试
		String vs="[a href=\"www.cnmb.cn\"]ccc[/a]１２３４５６７８９ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ！·#￥%……——*（）——+|、＝－‘；“：/。，？》《`~";
		System.out.println("\n"+FilterUtil.filterChar(vs));

		vs="老子(女干)你老马()ᄀᄰᅙ  ᆨᇱᇹ  ⅓ⅷⅿↂ ←↭⇪ ∀∐⋱ ①⑳⓪  ─♥☃♯  ❶➷➾ 、【〟 ぁぱㄩ ㄱ㈄㈜   ㌸㏾  가썵  一丁丄龥铄  ↓じ☆客(+)雅☆峰♂☆☆●↓";
		System.out.println("\n"+FilterUtil.filterChar(vs));

		System.out.println("\n init=="+hk.init(vs));

		System.out.println(FilterUtil.getNumberOrLetter("http://d7q.upw*a*p.c*o*m"));

		//是否存在网址
		System.out.println("网址：＝＝＝＝"+FilterUtil.isHasWebAddr("中国.cn"));
		System.out.println("网址：＝＝＝＝"+FilterUtil.isHasWebAddr("abC.Cn"));
		System.out.println("网址：＝＝＝＝"+FilterUtil.isHasWebAddr("中国.com"));
		System.out.println("网址：＝＝＝＝"+FilterUtil.isHasWebAddr("-!-－.net"));
		System.out.println("网址：＝＝＝＝"+FilterUtil.isHasWebAddr("888.cn"));
		System.out.println("网址：＝＝＝＝"+FilterUtil.isHasWebAddr("中国.c"));
		System.out.println("网址：＝＝＝＝"+FilterUtil.isHasWebAddr("中国abcd"));
		System.out.println("网址：＝＝＝＝"+FilterUtil.isHasWebAddr("中国abcd.中心"));
		System.out.println("网址：＝＝＝＝"+FilterUtil.isHasWebAddr("中国abcd.12"));

		//判断是否存在敏感字
		System.out.println("存在1＝＝＝＝＝＝＝"+hk.isExistKeyByContent(content, "ABCD"));
		System.out.println("存在＝＝＝＝＝＝＝"+hk.isExistKeyByContent(content, "B"));
		System.out.println("存在＝＝＝＝＝＝＝"+hk.isExistKeyByContent(content, "C"));
		System.out.println("存在＝＝＝＝＝＝＝"+hk.isExistKeyByContent(content, "D"));

		System.out.println("\n运行时间" + (float) (System.currentTimeMillis() - ct)
				/ 1000 + "S");
		runGC();
		System.out.println("使用内存....." + usedMemory());
		System.out.println("---------------");
		System.out.println((int)'）');
		System.out.println(Long.parseLong("65289",16));
	}

	private static final Runtime s_runtime = Runtime.getRuntime();

	private static long usedMemory() {
		return s_runtime.totalMemory() - s_runtime.freeMemory();
	}

	private static void runGC() throws Exception {
		long usedMem1 = usedMemory(), usedMem2 = Long.MAX_VALUE;
		for (int i = 0; usedMem1 < usedMem2; i++) {
			s_runtime.runFinalization();
			s_runtime.gc();
			Thread.currentThread().yield();
			usedMem2 = usedMem1;
			usedMem1 = usedMemory();
		}
	}
}

class TestKey implements Key {
	private String word;
	private String rplWord;
	private int level;

	public TestKey(String word, String rplWord, int level) {
		this.word = word;
		this.rplWord = rplWord;
		this.level = level;
	}

	public int getLevel() {
		// TODO Auto-generated method stub
		return this.level;
	}

	public String getRplWord() {
		// TODO Auto-generated method stub
		return this.rplWord;
	}

	public String getWord() {
		// TODO Auto-generated method stub
		return this.word;
	}

}
