using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Text;
using System.Web;

namespace Zdez.Gateway.SDK.Helper
{

    /// <summary>
    /// HTTP助手类，仅支持UTF-8编码的标准POST请求
    /// 仅对顺利请求并返回200状态码的请求视为正确请求
    /// 请求时将参数视为请求实体
    /// 响应时仅读取响应正文
    /// </summary>
    class HttpHelper
    {

        public static bool Post(Uri requestUri, IDictionary<string, string> requestParams, out string responseContent)
        {
            try
            {
                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(requestUri);
                request.Method = "POST";
                request.ContentType = "application/x-www-form-urlencoded; charset=UTF-8";
                byte[] requestContent = BuildRequestParamsContent(requestParams);
                request.ContentLength = requestContent.Length;
                using (Stream stream = request.GetRequestStream())
                {
                    stream.Write(requestContent, 0, requestContent.Length);
                    stream.Close();
                }
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                if (response.StatusCode != HttpStatusCode.OK)
                {
                    responseContent = null;
                    return false;
                }
                using (Stream stream = response.GetResponseStream())
                {
                    using (MemoryStream memoryStream = new MemoryStream())
                    {
                        byte[] buffer = new byte[1024];
                        int readCount;
                        while ((readCount = stream.Read(buffer, 0, buffer.Length)) != 0)
                        {
                            memoryStream.Write(buffer, 0, readCount);
                        }
                        responseContent = Encoding.Unicode.GetString(Utf8ToUnicode(memoryStream.ToArray()));
                    }
                    return true;
                }
            }
            catch (Exception)
            {
                responseContent = null;
                return false;
            }
        }

        private static byte[] BuildRequestParamsContent(IDictionary<string, string> requestParams)
        {
            StringBuilder sb = new StringBuilder();
            foreach (KeyValuePair<string, string> x in requestParams)
            {
                sb.Append(x.Key);
                sb.Append("=");
                sb.Append(HttpUtility.UrlEncode(x.Value, Encoding.UTF8));
                sb.Append("&");
            }
            if (sb.Length != 0)
            {
                sb.Remove(sb.Length - 1, 1);
            }
            return UnicodeToUtf8(Encoding.Unicode.GetBytes(sb.ToString()));
        }

        private static byte[] UnicodeToUtf8(byte[] source)
        {
            return Encoding.Convert(Encoding.Unicode, Encoding.UTF8, source);
        }

        private static byte[] Utf8ToUnicode(byte[] source)
        {
            return Encoding.Convert(Encoding.UTF8, Encoding.Unicode, source);
        }

    }
}