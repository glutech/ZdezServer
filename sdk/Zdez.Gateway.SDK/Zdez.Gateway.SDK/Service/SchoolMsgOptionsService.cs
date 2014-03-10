using System;
using System.Collections.Generic;
using Zdez.Gateway.SDK.Helper;
using Zdez.Gateway.SDK.Model;

namespace Zdez.Gateway.SDK.Service
{
    class SchoolMsgOptionsService
    {

        public SchoolMsgOptionsResponseType Service(string token, int api)
        {
            Uri requestUri = new Uri(ContactsSetting.SCHOOL_MSG_OPTIONS);
            IDictionary<string, string> requestParams = new Dictionary<string, string>();
            requestParams.Add("token", token);
            requestParams.Add("api", api + "");
            string responseContent;
            if (HttpHelper.Post(requestUri, requestParams, out responseContent))
            {
                return JsonHelper.FromJson<SchoolMsgOptionsResponseType>(responseContent);
            }
            else
            {
                return null;
            }
        }
    }
}