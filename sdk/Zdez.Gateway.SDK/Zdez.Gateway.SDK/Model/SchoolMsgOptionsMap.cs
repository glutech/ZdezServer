using System.Collections.Generic;

namespace Zdez.Gateway.SDK.Model
{
    /// <summary>
    /// 读取发布参数时返回的结果集对象结构
    /// </summary>
    public class SchoolMsgOptionsMap
    {
        /// <summary>
        /// 毕业年度列表
        /// </summary>
        public IList<Grade> Grades { get; set; }
        /// <summary>
        /// 学业层次列表
        /// </summary>
        public IList<Degree> Degrees { get; set; }
        /// <summary>
        /// 学院列表
        /// </summary>
        public IList<Department> Departments { get; set; }
        /// <summary>
        /// 专业列表
        /// </summary>
        public IList<Major> Majors { set; get; }
        /// <summary>
        /// 抄送对象
        /// </summary>
        public SchoolMsgOptionsMapCarbonCopy CC { set; get; }
    }
}