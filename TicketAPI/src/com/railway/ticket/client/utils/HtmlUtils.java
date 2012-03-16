/**
 * 页面信息解析工具
 */
package com.railway.ticket.client.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 页面内容解析
 * @author ChenXiaohong
 *
 */
public class HtmlUtils {
	private static final Logger logger = LoggerFactory.getLogger(HtmlUtils.class);
	private static HtmlCleaner cleaner = new HtmlCleaner();
	private static final SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat simple_date_format = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat simple_time_format = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat simple_shorttime_format = new SimpleDateFormat("HH:mm");
	
	/**
	 * 通过{@link HtmlCleaner#clean(String)}方法返回{@link TagNode}对象
	 * @param html 网页内容
	 * @return {@link TagNode}
	 */
	public static TagNode clean(String html) {
		return cleaner.clean(html);
	}
	
	/** 取{@link TagNode}对象包含的网页内容
	 * @param tag
	 * @return Html内容
	 */
	public static String getInnerHtml(TagNode tag) {
		return cleaner.getInnerHtml(tag);
	}
	
	/**
	 * 取页面提示信息
	 * @param html
	 * @return 提示内容
	 */
	public static String getMessage(String html) {
		String msg = null;
		TagNode node = clean(html);
		//由Script脚本中取
		msg = getVar("message", html);
		if(null == msg || "".equals(msg)) {
			//由Html元素中取
			String[] msgs = evaluateXPath(node, "//span[@id='randErr']");
			if(msgs.length > 0) {
				msg = msgs[0];
			} else {
				//取系统维护的信息
				msgs = evaluateXPath(node, "//div[@class='pim_rightnew']//div[@class='sign_suc']");
				if(msgs.length > 0)
					msg = msgs[0];
			}
		}
		return msg;
	}
	
	/** 根据PATH获取字符内容数组
	 * @param html
	 * @param path
	 * @return {@link String}[] 数组
	 */
	public static String[] evaluateXPath(String html, String path) {
		TagNode tagNode = clean(html);
		return evaluateXPath(tagNode, path);
	}
	
	/**
	 * 取标签属性值
	 * @param html
	 * @param path
	 * @param attName 属性名称
	 * @return 属性包含的值
	 */
	public static String evaluateXpath(String html, String path, String attName) {
		String value = null;
		Object[] tags = evaluateXPathTag(html, path);
		if(tags.length > 0) {
			value = ((TagNode)tags[0]).getAttributeByName(attName);
		}
		return value;
	}
	
	/**
	 * 取标签属性值
	 * @param tag
	 * @param path 
	 * @param attName 属性名称
	 * @return 属性包含的值
	 */
	public static String evaluateXpath(TagNode tag, String path, String attName) {
		String value = null;
		Object[] tags = evaluateXPathTag(tag, path);
		if(tags.length > 0) {
			value = ((TagNode)tags[0]).getAttributeByName(attName);
		}
		return value;
	}

	/** 查找Path匹配的TagNode
	 * @param html
	 * @param path
	 * @return {@link TagNode}[]数组
	 */
	public static TagNode[] evaluateXPathTag(String html, String path) {
		TagNode tagNode = clean(html);
		return evaluateXPathTag(tagNode, path);
	}
	
	/**
	 * @param tag
	 * @param path
	 * @return {@link TagNode}[]数组
	 */
	public static TagNode[] evaluateXPathTag(TagNode tag, String path) {
		List<TagNode> tagList = new LinkedList<TagNode>();
		try {
			Object[] tagObjs = tag.evaluateXPath(path);
			for (Object tagObj : tagObjs) {
				tagList.add((TagNode) tagObj);
			}
		} catch (XPatherException e) {
			logger.debug("内容解析错误!");
		}
		return tagList.toArray(new TagNode[]{});
	}
	
	/**
	 * @param tag
	 * @param path
	 * @return {@link String}[]数组
	 */
	public static String[] evaluateXPath(TagNode tag, String path) {
		List<String> content = new LinkedList<String>();
		try {
			Object[] nodes = tag.evaluateXPath(path);
			if(nodes.length > 0) {
				for (Object node : nodes) {
					content.add(((TagNode)node).getText().toString().trim());
				}
			}
		} catch (XPatherException e) {
			logger.debug("解析错误!{}",e.getMessage());
		}
		return content.toArray(new String[]{});
	}
	
	/**
	 * 取页面Token
	 * @param html
	 * @return TOKEN
	 */
	public static String getToken(String html) {
		return getToken(HtmlUtils.clean(html));
	}
	
	/**
	 * 取页面Token
	 * @param tagNode
	 * @return TOKEN
	 */
	public static String getToken(TagNode tagNode) {
		return HtmlUtils.evaluateXpath(tagNode, "//input[@name='org.apache.struts.taglib.html.TOKEN']", "value");
	}
	
	/** 查找正则表达式匹配的值
	 * @param html
	 * @param regx
	 * @return  {@link String}[]数组
	 */
	public static String[] getMatcherValue(String html, String regx) {
		Matcher matcher = Pattern.compile(regx).matcher(html);
		List<String> values = new LinkedList<String>();
		if(matcher.find()) {
			for (int i = 1; i < matcher.groupCount(); i++) {
				values.add(matcher.group(i));
			}
		}
		return values.toArray(new String[]{});
	}
	
	/** 取JavaScript变量值.
	 * 例:var message = "消息";
	 * @param name	变量名
	 * @param body	内容
	 * @return	变量值
	 */
	public static String getVar(String name, String body) {
		return getVar(name, body, "");
	}
	
	/**
	 * @param name
	 * @param body
	 * @param defaultValue
	 * @return	变量值
	 */
	public static String getVar(String name, String body, String defaultValue) {
		String value = defaultValue;
		Matcher matcher = Pattern.compile("var\\s*"+name.trim()+"\\s*=\\s*['\"]?(.*?)['\";]*[\r\n]").matcher(body);
		if(matcher.find()) {
			value = matcher.group(1);
		}
		return value;
	}
	
	/**
	 * @param cs
	 * @return 是否为空
	 */
	public static boolean isEmpty(CharSequence cs) {
		return cs == null || cs.length() == 0;
	}
	
	/**
	 * @param cs
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(CharSequence cs) {
		return !HtmlUtils.isEmpty(cs);
	}
	
	/**
	 * 根据正则表达式分割字符串
	 * @param html
	 * @param regx
	 * @return {@link String}[]数组
	 */
	public static String[] splitByRegx(String html, String regx) {
		List<String> arrs = new LinkedList<String>();
		Matcher matcher = Pattern.compile(regx).matcher(html);
		while(matcher.find()) {
			for (int i = 0; i <= matcher.groupCount(); i++) {
				arrs.add(matcher.group(i));
			}
		}
		return arrs.toArray(new String[]{});
	}
	
	/**
	 * 转换余票值
	 * @param vote
	 * @return 余票(0:已卖完，100:充足)
	 */
	public static int toVote(String vote) {
		int v = 0;
		if("--".equals(vote.trim()) || "无".equals(vote.trim())) {
			v = 0;
		} else if("有".equals(vote.trim())){
			v = 100;
		} else {
			try {
				v = Integer.parseInt(vote.trim());
			} catch (Exception e) {
				logger.debug("解析错误!{}",e.getMessage());
			}
		}
		return v;
	}
	
	/**
	 * @return 当前日期字符串
	 */
	public static String getDate() {
		return simple_date_format.format(System.currentTimeMillis());
	}
	
	/** 格式为日期
	 * @param date
	 * @return 日期 
	 */
	public static String getDate(String date) {
		try {
			return simple_date_format.format(simple_time_format.parse(date));
		} catch (ParseException e) {
			logger.debug("解析错误!{}",e.getMessage());
		}
		return null;
	}
	
	/** 将日期的Long形式转化为yyyy-MM-dd形式
	 * @param time_long
	 * @return yyyy-MM-dd格式日期
	 */
	public static String formatDate(String time_long) {
		try {
			return simple_date_format.format(new java.util.Date(Long.parseLong(time_long)));
		} catch (Exception e) {
			logger.debug("解析错误!{}",e.getMessage());
		}
		return "";
	}
	
	/** 转换CST日期
	 * @param date
	 * @return 日期
	 */
	public static String getDateCST(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.US);
		try {
			return simple_date_format.format(sdf.parse(date));
		} catch (ParseException e) {
			logger.debug("解析错误!{}",e.getMessage());
		}
		return null;
	}
	
	/** {@link TagNode#getText()}
	 * @param tag
	 * @return {@link TagNode#getText()}
	 */
	public static String getTagText(Object tag) {
		return (null != tag ? ((TagNode)tag).getText().toString() : "");
	}
	
	/**
	 * @return 当前时间
	 */
	public static String getTime() {
		return simple_time_format.format(System.currentTimeMillis());
	}
	
	/** 格式化时间
	 * @param time
	 * @return 时间
	 */
	public static String getTime(long time) {
		return simple_time_format.format(new java.util.Date(time));
	}
	
	/** 格式化时间
	 * @param time
	 * @return 时间
	 */
	public static String getTime(String time) {
		try {
			return simple_time_format.format(simple_time_format.parse(time));
		} catch (ParseException e) {
			logger.debug("解析错误!{}",e.getMessage());
		}
		return null;
	}
	
	/** 格式化CST时间
	 * @param time
	 * @return 时间
	 */
	public static String getShortTimeCST(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.US);
		try {
			return simple_shorttime_format.format(sdf.parse(time));
		} catch (ParseException e) {
			logger.debug("解析错误!{}",e.getMessage());
		}
		return null;
	}
	
	/** 格式化CST日期时间
	 * @param time
	 * @return 日期时间
	 */
	public static String getDateTimeCST(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.US);
		try {
			return date_format.format(sdf.parse(time));
		} catch (ParseException e) {
			logger.debug("解析错误!{}",e.getMessage());
		}
		return null;
	}
	
	
	/** 转换CST时间
	 * @param time
	 * @return 时间
	 */
	public static String getTimeCST(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.US);
		try {
			return simple_time_format.format(sdf.parse(time));
		} catch (ParseException e) {
			logger.debug("解析错误!{}",e.getMessage());
		}
		return null;
	}
}