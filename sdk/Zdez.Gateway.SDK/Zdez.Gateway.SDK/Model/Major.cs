
namespace Zdez.Gateway.SDK.Model
{
    /// <summary>
    /// 专业对象结构
    /// </summary>
    public class Major : IdNamePair
    {
        public int DepartmentId { get; set; }
        public int DegreeId { get; set; }
    }
}