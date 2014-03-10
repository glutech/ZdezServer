
namespace Zdez.Gateway.SDK.Model
{
    /// <summary>
    /// 描述ID、Name属性关系的对象结构
    /// </summary>
    public abstract class IdNamePair
    {
        public int Id { get; set; }
        public string Name { get; set; }
    }
}