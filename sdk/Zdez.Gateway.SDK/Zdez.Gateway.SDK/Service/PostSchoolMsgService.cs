using System;
using System.Collections.Generic;
using Zdez.Gateway.SDK.Helper;
using Zdez.Gateway.SDK.Model;

namespace Zdez.Gateway.SDK.Service
{
    class PostSchoolMsgService
    {

        public PostSchoolMsgResponseType Service(string token, string title, string content, int[] grades, int[] departments, int[] majors, int[] cc, bool debug, int api)
        {
            Uri requestUri = new Uri(ContactsSetting.POST_SCHOOL_MSG);
            IDictionary<string, string> requestParams = new Dictionary<string, string>();
            requestParams.Add("token", token);
            requestParams.Add("title", title);
            requestParams.Add("content", content);
            requestParams.Add("grades", JsonHelper.ToJson<int[]>(grades));
            requestParams.Add("departments", JsonHelper.ToJson<int[]>(departments));
            requestParams.Add("majors", JsonHelper.ToJson<int[]>(majors));
            requestParams.Add("cc", JsonHelper.ToJson<int[]>(cc));
            requestParams.Add("debug", debug + "");
            requestParams.Add("api", api + "");
            string responseContent;
            if (HttpHelper.Post(requestUri, requestParams, out responseContent))
            {
                return JsonHelper.FromJson<PostSchoolMsgResponseType>(responseContent);
            }
            else
            {
                return null;
            }
        }

    }
}