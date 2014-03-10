using System;
using System.Collections.Generic;
using Zdez.Gateway.SDK.Helper;
using Zdez.Gateway.SDK.Model;

namespace Zdez.Gateway.SDK.Service
{
    class SaAuthTokenService
    {
        public SaAutuTokenResponseType Service(string username, string password, int api)
        {
            Uri requestUri = new Uri(ContactsSetting.SA_AUTH_TOKEN);
            IDictionary<string, string> requestParams = new Dictionary<string, string>();
            requestParams.Add("username", username);
            requestParams.Add("password", password);
            requestParams.Add("api", api + "");
            String responseContent;
            if (HttpHelper.Post(requestUri, requestParams, out responseContent))
            {
                return JsonHelper.FromJson<SaAutuTokenResponseType>(responseContent);
            }
            else
            {
                return null;
            }
        }
    }
}