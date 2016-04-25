package me.zjh.zframe.base;

/**
 * app的一些配置
 *
 * @author zjh
 */
public interface AppConfig {

    /**
     * 默认app缓存路径
     */
    String DEFAULT_APP_FOLDER = "zframe";

    /**
     * app缓存目录下的子目录，用于异常log日志存储
     */
    String FOLDER_CHILD_LOG = "log";

    /**
     * app缓存目录下的子目录，用于图片缓存
     */
    String FOLDER_CHILD_IMAGE = "images";

}
