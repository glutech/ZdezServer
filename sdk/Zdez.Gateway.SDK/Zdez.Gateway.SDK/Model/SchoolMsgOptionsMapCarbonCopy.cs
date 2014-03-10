using System.Collections.Generic;

namespace Zdez.Gateway.SDK.Model
{
    /// <summary>
    /// 读取发布参数时返回的对象的结果集中抄送对象结构
    /// </summary>
    public class SchoolMsgOptionsMapCarbonCopy
    {
        /// <summary>
        /// 学生处教师列表
        /// </summary>
        public IList<CarbonCopy> STO { get; set; }
        /// <summary>
        /// 就业处教师列表
        /// </summary>
        public IList<CarbonCopy> REO { get; set; }
        /// <summary>
        /// 团委教师列表
        /// </summary>
        public IList<CarbonCopy> YLC { get; set; }
        /// <summary>
        /// 保卫处教师列表
        /// </summary>
        public IList<CarbonCopy> SO { get; set; }
    }
}