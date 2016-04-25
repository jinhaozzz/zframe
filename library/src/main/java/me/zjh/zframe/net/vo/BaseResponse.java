package me.zjh.zframe.net.vo;

/**
 * 请求响应公共实体。
 *
 * TODO mmm 为了测试，兼容多个类型的接口。此处定义了多种类型的基础返回类型。在实际项目中需要根据项目做对应修改。
 *
 * @author ZJH
 * created at 2015/12/2 9:45
 */
public class BaseResponse {

	// --------------儿童机-----------------------
	public int result;

	public String messageCode;

	public String message;
	// --------------儿童机-----------------------

	// --------------leancloud-----------------------
	public int code;

	public String error;
	// --------------leancloud-----------------------

}
