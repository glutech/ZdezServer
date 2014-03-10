using Newtonsoft.Json;
using System;
using System.IO;

namespace Zdez.Gateway.SDK.Helper
{
    /// <summary>
    /// JSON助手类
    /// 提供从对象到字符串的序列化、字符串到对象的反序列化工作
    /// 要求调用对象不为匿名类型，提供无参构造方法
    /// </summary>
    class JsonHelper
    {

        public static string ToJson<T>(T obj) where T : class
        {
            using (StringWriter stringWriter = new StringWriter())
            {
                try
                {
                    new JsonSerializer().Serialize(new JsonTextWriter(stringWriter), obj);
                    return stringWriter.GetStringBuilder().ToString();
                }
                catch (Exception)
                {
                    return null;
                }
            }

        }

        public static T FromJson<T>(string json) where T : class
        {
            using (StringReader stringReader = new StringReader(json))
            {
                try
                {
                    return (T)new JsonSerializer().Deserialize(new JsonTextReader(stringReader), typeof(T));
                }
                catch (Exception)
                {
                    return null;
                }
            }
        }
    }
}