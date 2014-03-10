using System;
using System.Collections.Generic;
using Zdez.Gateway.SDK.Helper;
using Zdez.Gateway.SDK.Model;

namespace Zdez.Gateway.SDK.Service
{
    class SaCancelTokenService
    {
        public SaCancelTokenResponseType Service(string username, string password, int api)
        {
            Uri requestUri = new Uri(ContactsSetting.SA_CANCEL_TOKEN);
            IDictionary<string, string> requestParams = new Dictionary<string, string>();
            requestParams.Add("username", username);
            requestParams.Add("password", password);
            requestParams.Add("api", api + "");
            string responseContent;
            if (HttpHelper.Post(requestUri, requestParams, out responseContent))
            {
                return JsonHelper.FromJson<SaCancelTokenResponseType>(responseContent);
            }
            else
            {
                return null;
            }
        }
    }
}