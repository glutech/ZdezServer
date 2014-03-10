using System;
using System.Threading;
using Zdez.Gateway.SDK.Model;
using Zdez.Gateway.SDK.Service;

namespace Zdez.Gateway.SDK
{
    /// <summary>
    /// <para>通知平台接口SDK(C#版)，适用于API1</para>
    /// <para>==============================================</para>
    /// <para>使用前应仔细查阅 《通知平台借口说明.pdf》文件</para>
    /// <para>对调用方而言，仅需关注SDK类和Model层下面的结构</para>
    /// <para>===============================================</para>
    /// <para>昆明博客科技有限公司 service@zdez.com.cn</para>
    /// </summary>
    public static class SDK
    {

        /// <summary>
        /// 本SDK适用的API版本常量
        /// </summary>
        public const int API_VERSION = 1;

        /// <summary>
        /// 通过提交账号密码取得授权码
        /// </summary>
        /// <param name="username">账号</param>
        /// <param name="password">密码</param>
        /// <param name="api">API版本，应使用API_VERSION常量设置</param>
        /// <param name="saAuthTokenResponseType">作为输出参数的请求结果对象</param>
        /// <returns>是否成功请求，可能由于网络错误等原因返回false</returns>
        public static bool SaAuthToken(string username, string password, int api, out SaAutuTokenResponseType saAuthTokenResponseType)
        {
            if (username == null || password == null)
                throw new ArgumentNullException();
            saAuthTokenResponseType = new SaAuthTokenService().Service(username, password, api);
            return saAuthTokenResponseType != null;
        }

        /// <summary>
        /// 通过提交授权码读取发布通知时的参数选项
        /// </summary>
        /// <param name="token">授权码</param>
        /// <param name="api">API版本，应使用API_VERSION常量设置</param>
        /// <param name="schoolMsgOptionsResponseType">作为输出参数的请求结果对象</param>
        /// <returns>是否成功请求，可能由于网络错误等原因返回false</returns>
        public static bool SchoolMsgOptions(string token, int api, out SchoolMsgOptionsResponseType schoolMsgOptionsResponseType)
        {
            if (token == null)
                throw new ArgumentNullException();
            schoolMsgOptionsResponseType = new SchoolMsgOptionsService().Service(token, api);
            return schoolMsgOptionsResponseType != null;
        }

        /// <summary>
        /// 通过授权码发布文章
        /// </summary>
        /// <param name="token">授权码</param>
        /// <param name="title">文字标题</param>
        /// <param name="content">文章正文</param>
        /// <param name="grades">毕业年度ID列表</param>
        /// <param name="departments">学院ID列表</param>
        /// <param name="majors">专业ID列表</param>
        /// <param name="cc">抄送对象ID列表</param>
        /// <param name="debug">是否使用调试模式发布（仅用于测试）</param>
        /// <param name="api">API版本，应使用API_VERSION常量设置</param>
        /// <param name="postSchoolMsgResponseType">作为输出参数的请求结果对象</param>
        /// <returns>是否成功请求，可能由于网络错误等原因返回false</returns>
        public static bool PostSchoolMsg(string token, string title, string content, int[] grades, int[] departments, int[] majors, int[] cc, bool debug, int api, out PostSchoolMsgResponseType postSchoolMsgResponseType)
        {
            if (token == null || title == null || content == null || grades == null || departments == null || majors == null || cc == null)
                throw new ArgumentNullException();
            postSchoolMsgResponseType = new PostSchoolMsgService().Service(token, title, content, grades, departments, majors, cc, debug, api);
            return postSchoolMsgResponseType != null;
        }

        /// <summary>
        /// 通过提交账号密码取消之前所取得的授权
        /// </summary>
        /// <param name="username">账号</param>
        /// <param name="password">密码</param>
        /// <param name="api">API版本，应使用API_VERSION常量设置</param>
        /// <param name="saCancelTokenResponseType">作为输出参数的请求结果对象</param>
        /// <returns>是否成功请求，可能由于网络错误等原因返回false</returns>
        public static bool SaCancelToken(string username, string password, int api, out SaCancelTokenResponseType saCancelTokenResponseType)
        {
            if (username == null || password == null)
                throw new ArgumentNullException();
            saCancelTokenResponseType = new SaCancelTokenService().Service(username, password, api);
            return saCancelTokenResponseType != null;
        }

        /// <summary>
        /// 异步执行方法的回调委托
        /// 调用异步时提供委托实例等待回调
        /// </summary>
        /// <typeparam name="T">执行成功时所解析的响应对象类型</typeparam>
        /// <param name="isTransferSuccess">是否在通信传输过程中一切正常（未发生网络错误等异常情况）</param>
        /// <param name="responseType">响应对象</param>
        public delegate void SDKCallbackDelegate<T>(bool isTransferSuccess, T responseTypeObject);

        /// <summary>
        /// 通过提交账号密码取得授权码，异步执行版本
        /// </summary>
        /// <param name="username">账号</param>
        /// <param name="password">密码</param>
        /// <param name="api">API版本，应使用API_VERSION常量设置</param>
        /// <param name="callbackDelegate">委托回调</param>
        public static void SaAuthTokenAsync(string username, string password, int api, SDKCallbackDelegate<SaAutuTokenResponseType> callbackDelegate)
        {
            ThreadPool.QueueUserWorkItem(new WaitCallback((param) =>
                {
                    SaAutuTokenResponseType saAuthTokenResponseType;
                    if (SaAuthToken(username, password, api, out saAuthTokenResponseType))
                    {
                        callbackDelegate.Invoke(true, saAuthTokenResponseType);
                    }
                    else
                    {
                        callbackDelegate.Invoke(false, null);
                    }
                }), null);
        }

        /// <summary>
        /// 通过提交授权码读取发布通知时的参数选项，异步执行版本
        /// </summary>
        /// <param name="token">授权码</param>
        /// <param name="api">API版本，应使用API_VERSION常量设置</param>
        /// <param name="callbackDelegate">委托回调</param>
        public static void SchoolMsgOptionsAsync(string token, int api, SDKCallbackDelegate<SchoolMsgOptionsResponseType> callbackDelegate)
        {
            ThreadPool.QueueUserWorkItem(new WaitCallback((param) =>
                {
                    SchoolMsgOptionsResponseType schoolMsgOptionsResponseType;
                    if (SchoolMsgOptions(token, api, out schoolMsgOptionsResponseType))
                    {
                        callbackDelegate.Invoke(true, schoolMsgOptionsResponseType);
                    }
                    else
                    {
                        callbackDelegate.Invoke(false, null);
                    }
                }), null);
        }

        /// <summary>
        /// 通过授权码发布文章，异步执行版本
        /// </summary>
        /// <param name="token">授权码</param>
        /// <param name="title">文章标题</param>
        /// <param name="content">文章正文</param>
        /// <param name="grades">毕业年度ID列表</param>
        /// <param name="departments">学院ID列表</param>
        /// <param name="majors">专业ID列表</param>
        /// <param name="cc">抄送对象ID列表</param>
        /// <param name="debug">是否使用调试模式发布（仅用于测试）</param>
        /// <param name="api">API版本，应使用API_VERSION常量设置</param>
        /// <param name="callbackDelegate">委托回调</param>
        public static void PostSchoolMsgAsync(string token, string title, string content, int[] grades, int[] departments, int[] majors, int[] cc, bool debug, int api, SDKCallbackDelegate<PostSchoolMsgResponseType> callbackDelegate)
        {
            ThreadPool.QueueUserWorkItem(new WaitCallback((param) =>
                {
                    PostSchoolMsgResponseType postSchoolMsgResponseType;
                    if (PostSchoolMsg(token, title, content, grades, departments, majors, cc, debug, api, out postSchoolMsgResponseType))
                    {
                        callbackDelegate.Invoke(true, postSchoolMsgResponseType);
                    }
                    else
                    {
                        callbackDelegate.Invoke(false, null);
                    }
                }), null);
        }

        /// <summary>
        /// 通过提交账号密码取消之前所取得的授权，异步执行版本
        /// </summary>
        /// <param name="username">账号</param>
        /// <param name="password">密码</param>
        /// <param name="api">API版本，应使用API_VERSION常量设置</param>
        /// <param name="callbackDelegate">委托回调</param>
        public static void SaCancelTokenAsync(string username, string password, int api, SDKCallbackDelegate<SaCancelTokenResponseType> callbackDelegate)
        {
            ThreadPool.QueueUserWorkItem(new WaitCallback((param) =>
                {
                    SaCancelTokenResponseType saCancelTokenResponseType;
                    if (SaCancelToken(username, password, api, out saCancelTokenResponseType))
                    {
                        callbackDelegate.Invoke(true, saCancelTokenResponseType);
                    }
                    else
                    {
                        callbackDelegate.Invoke(false, null);
                    }
                }), null);
        }

    }
}