package cn.com.zdez.service;

/**
 * WindowsPhone的通知方法类 <br />
 * 用于向Microsoft服务器发送信息，已通知其向App发送推送通知 <br />
 * 仅实现Toast通知 <br />
 * 仅支持未通过Microsoft验证的WEB服务 <br />
 * @version 1.0
 * @author wu.kui2@gmail.com
 * @since JDK-1.7, WindowsPhoneSDK-7.1
 * @see http://msdn.microsoft.com/zh-cn/library/ff402537(v=vs.92).aspx
 */
public class WindowsPhoneNotification {
    
    
    // TODO: 性能优化！！！
    
    
    /**
     * 响应状态枚举
     * @author wu.kui2@gmail.com
     * @version 1.0
     */
    public enum ResponseStatusEnum {
        /**
         * 正确请求，已接受请求
         */
        Success,
        /**
         * 设备异常，设备未接收信息达到上限或应用未正确注册通道，请求被忽略。本地程序可考虑稍后重新发送通知
         */
        DeviceWarning,
        /**
         * 远程服务器错误，本地程序应在稍后重新发送通知
         */
        ServerError,
        /**
         * 错误请求，请求HTTP头或内容错误
         */
        ContentError,
        /**
         * 错误请求，网络链接出现错误
         */
        IOError,
        /**
         * 未知错误
         */
        UnkownError
    }
    
    
    /**
     * 通知发送回调监听器
     * @author wu.kui2@gmail.com
     * @version 1.0
     */
    public interface NotificationCallbackListener {
        /**
         * 请求完成时的回调方法
         * @param result 请求的响应结果状态枚举
         */
        public void onCompleted(ResponseStatusEnum result);
    }
    
    
    
    /*
     * =======================================================
     * Toast通知
     */
    
    /**
     * 以同步的方法发送一个Toast通知请求，并通过返回值判断发送状态 <br />
     * 总是应该检查返回值确定响应状态，以确保稳定可用！ <br />
     * @param channelUri 通道URI，由APP提供
     * @param msgId 校园通知条目编号
     * @param msgTitle 校园通知标题
     * @return 响应状态枚举
     */
    public ResponseStatusEnum toastOnSync(String channelUri, int msgId, String msgTitle) {
        return new ToastSender(channelUri).post(msgTitle, "", "/Action/Main.xaml?mod=newnotice&id=" + msgId + "&timestamp=" + System.currentTimeMillis());
    }
    
    /**
     * 以异步的方法发送一个Toast通知请求，并通过监听器回调判断发送状态 <br />
     * 总是应该检查回调值确定响应状态，以确保稳定可用！ <br />
     * 这是toastOnSync方法的异步实现，实现相同的功能 <br />
     * @param channelUri 通道URI，由APP提供
     * @param msgId 校园通知条目编号
     * @param msgTitle 校园通知标题
     * @param callback 监听器实例
     */
    public void toastOnAsync(final String channelUri, final int msgId, final String msgTitle, final NotificationCallbackListener callback) {
        // TODO: 批量发送时应使用专用或与其他组件共用的线程池
        // TODO: 完成时应在调用线程上回调，而不是新开的线程上
        java.util.concurrent.ExecutorService pool = java.util.concurrent.Executors.newSingleThreadExecutor();
        pool.execute(new Runnable() {
            @Override
            public void run() {
                ResponseStatusEnum r = toastOnSync(channelUri, msgId, msgTitle);
                if(callback != null) {
                    callback.onCompleted(r);
                }
            }
        });
        pool.shutdown();
    }
    
    /*
     * Toast通知
     * =======================================================
     */
    
    
    
    /**
     * Toast通知实现
     * @author wu.kui2@gmail.com
     * @version 1.0
     */
    protected static class ToastSender {
    
        private String host;
        
        /**
         * 构造对象
         */
        public ToastSender(String host) {
            this.host = host;
        }
        
        /**
         * 发送通知
         */
        public ResponseStatusEnum post(String title, String content, String param) {
            String message = build_message(title, content, param);
            java.net.HttpURLConnection conn = null;
            try {
                conn = (java.net.HttpURLConnection)new java.net.URL(host).openConnection();
                // 标记可附加内容
                conn.setDoOutput(true);
                // 标记自动维护重定向
                conn.setInstanceFollowRedirects(true);
                // 设置HTTP头
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "text/xml;charset=utf-8"); //标记内容格式
                conn.setRequestProperty("X-WindowsPhone-Target", "toast"); // 标记类型
                conn.setRequestProperty("X-NotificationClass", "2"); // 标记立即发送
                conn.setRequestProperty("Content-Length",Integer.toString(message.getBytes().length));
                // 写入请求正文
                java.io.OutputStream output = conn.getOutputStream();
                output.write(message.getBytes("utf-8"));
                output.flush();
                output.close();
                // 发送请求
                conn.connect();
            } catch (java.net.MalformedURLException e) {
                return ResponseStatusEnum.ContentError;
            } catch(java.io.IOException e) {
                return ResponseStatusEnum.IOError;
            } finally {
                if(conn != null) {
                    conn.disconnect();
                }
            }
            try {
                return parse_notification_status(conn.getResponseCode(), conn.getHeaderFields());
            } catch (java.io.IOException e) {
                return ResponseStatusEnum.IOError;
            }
        }
        
        /**
         * 拼接待发送消息的内容
         */
        private static String build_message(String title, String content, String param) {
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("(\\{__[A-Z]+__\\})")
                                                                     .matcher(TEMPLATE_WITH_FIELDS);
            StringBuffer result = new StringBuffer();
            matcher.find();  matcher.appendReplacement(result, escape_xml_characters(title));
            matcher.find();  matcher.appendReplacement(result, escape_xml_characters(content));
            matcher.find();  matcher.appendReplacement(result, escape_xml_characters(param));
            matcher.appendTail(result);
            return result.toString();
        }
        
        
        /**
         * 通知的XML正文模板
         */
        private static final String TEMPLATE_WITH_FIELDS =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
             +  "<wp:Notification xmlns:wp=\"WPNotification\">"
             +      "<wp:Toast>"
             +          "<wp:Text1>{__TITLE__}</wp:Text1>"
             +          "<wp:Text2>{__CONTENT__}</wp:Text2>"
             +          "<wp:Param>{__PARAM__}</wp:Param>"
             +      "</wp:Toast>"
             +  "</wp:Notification>";
        
    }
    
    
    /**
     * 根据响应头解析Microsoft服务器响应结果
     * @see http://msdn.microsoft.com/en-us/library/windowsphone/develop/ff941100(v=vs.105).aspx
     */
    private static ResponseStatusEnum parse_notification_status(int code, java.util.Map<String, java.util.List<String>> headers) {
        if(code == 200) {
            String notificationStatus = "";
            try {
                notificationStatus = headers.get("X-NotificationStatus").get(0);
            }catch(Exception e) {}
            if(notificationStatus.equals("Received")) {
                return ResponseStatusEnum.Success;
            } else if(notificationStatus.equals("QueueFull") || notificationStatus.equals("Suppressed")) {
                return ResponseStatusEnum.DeviceWarning;
            }
        } else if(code == 400) {
            return ResponseStatusEnum.ContentError;
        } else if(code == 503) {
            return ResponseStatusEnum.ServerError;
        }
        return ResponseStatusEnum.UnkownError;
    }
    
    /**
     * XML内容域的特殊字符转义
     */
    private static String escape_xml_characters(String source) {
        // &    =>    &amp;
        // <    =>    &lt;
        // >    =>    &gt;
        // ‘    =>    &apos;
        // “    =>    &quot;
        char[] sourceChars = source.toCharArray();
        StringBuilder result = new StringBuilder(sourceChars.length);
        for(char c : sourceChars) {
            switch(c) {
                case '&' : result.append("&amp;");  break;
                case '<' : result.append("&lt;");   break;
                case '>' : result.append("&gt;");   break;
                case '\'': result.append("&apos;"); break;
                case '"' : result.append("&quot;"); break;
                default: result.append(c);
            }
        }
        return result.toString();
    }
    
}