
namespace Zdez.Gateway.SDK.Model
{
    /// <summary>
    /// 获取授权码时返回的对象结构
    /// </summary>
    public class SaAutuTokenResponseType
    {
        public int Status { get; set; }
        public string Token { get; set; }
        public string Msg { get; set; }
    }
}