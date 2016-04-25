package me.zjh.zframe.net.vo;

import java.util.Map;

/**
 * 公共请求头
 *
 * @author zjh
 */
public class BaseRequestHeader extends BaseCommunication {

    @Override
    public Map<String, String> exportAsDictionary() {
        Map<String, String> head = super.exportAsDictionary();

        return head;
    }

}
