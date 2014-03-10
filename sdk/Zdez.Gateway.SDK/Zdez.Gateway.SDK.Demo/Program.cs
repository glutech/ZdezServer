using System;


namespace Zdez.Gateway.SDK.Demo
{
    /// <summary>
    /// <para>通知平台接口SDK(C#版)示例程序，适用于API1</para>
    /// <para>==============================================</para>
    /// <para>仅演示了获取授权码、取消授权码两个操作的同步调用方法</para>
    /// <para>更多信息，请参见Zdez.Gateway.SDK类库和《通知平台借口说明.pdf》文件</para>
    /// <para>==============================================</para>
    /// <para>昆明博客科技有限公司 service@zdez.com.cn</para>
    /// <seealso cref="Zdez.Gateway.SDK.SDK"/>
    /// </summary>
    class Program
    {
        static void Main(string[] args)
        {
            while (true)
            {
                Console.WriteLine("============================");
                Console.WriteLine("   通知平台接口演示程序");
                Console.WriteLine("   * 仅演示获取授权码和取消授权操作，其它操作调用方法相同");
                Console.WriteLine("1. 获取授权码");
                Console.WriteLine("2. 取消授权");
                Console.WriteLine("0. 关闭程序");
                Console.WriteLine("============================");
                Console.Write("输入所需功能序号：");
                string input = Console.ReadLine();
                if (input.StartsWith("1"))
                {
                    Console.Write("输入账号：");
                    string username = Console.ReadLine();
                    Console.Write("输入密码：");
                    string password = Console.ReadLine();
                    Model.SaAutuTokenResponseType saAuthTokenResponseType;
                    if (SDK.SaAuthToken(username, password, SDK.API_VERSION, out saAuthTokenResponseType))
                    {
                        Console.Write("成功与服务器通信，");
                        switch (saAuthTokenResponseType.Status)
                        {
                            case 0:
                                Console.Write("错误API版本或无效参数：" + saAuthTokenResponseType.Msg);
                                break;
                            case 1:
                                Console.Write("获取授权码成功：" + saAuthTokenResponseType.Token);
                                break;
                            case 2:
                                Console.Write("账号或密码错误：" + saAuthTokenResponseType.Msg);
                                break;
                            case -1:
                                Console.Write("此账号已绑定了其它授权码");
                                break;
                            default:
                                Console.Write("其他错误");
                                break;
                        }
                        Console.WriteLine();
                    }
                    else
                    {
                        Console.WriteLine("与服务器通信错误");
                    }
                }
                else if (input.StartsWith("2"))
                {
                    Console.Write("输入账号：");
                    string username = Console.ReadLine();
                    Console.Write("输入密码：");
                    string password = Console.ReadLine();
                    Model.SaCancelTokenResponseType saCancelTokenResponseType;
                    if (SDK.SaCancelToken(username, password, SDK.API_VERSION, out saCancelTokenResponseType))
                    {
                        Console.Write("成功与服务器通信，");
                        switch (saCancelTokenResponseType.Status)
                        {
                            case 0:
                                Console.Write("错误API版本或无效参数：" + saCancelTokenResponseType.Msg);
                                break;
                            case 1:
                                Console.Write("取消授权成功");
                                break;
                            case 2:
                                Console.Write("账号或密码错误：" + saCancelTokenResponseType.Msg);
                                break;
                            case -1:
                                Console.Write("此账号还未取得授权");
                                break;
                            default:
                                Console.Write("其他错误");
                                break;
                        }
                        Console.WriteLine();
                    }
                    else
                    {
                        Console.WriteLine("与服务器通信错误");
                    }
                }
                else if (input.StartsWith("0"))
                {
                    return;
                }
                else
                {
                    Console.WriteLine("错误输入");
                }
            }
        }
    }
}