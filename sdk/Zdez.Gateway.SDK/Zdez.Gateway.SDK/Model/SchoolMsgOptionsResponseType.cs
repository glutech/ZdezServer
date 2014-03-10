
namespace Zdez.Gateway.SDK.Model
{
    /// <summary>
    /// 读取发布参数时返回的对象结构
    /// </summary>
    public class SchoolMsgOptionsResponseType
    {
        public int Status { get; set; }
        public object Options { get; set; }
        public string Msg { get; set; }
    }
}