package me.zjh.zframe.net.vo;

/**
 * 公共请求
 */
public class BaseRequest extends BaseCommunication {

    protected BaseRequestHeader header;

    protected String path;

    public BaseRequest() {
        header = new BaseRequestHeader();
        path = "";
    }

    public BaseRequestHeader getHeader() {
        return header;
    }

    public void setHeader(BaseRequestHeader header) {
        this.header = header;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
