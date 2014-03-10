using System;
using System.Collections.Generic;
using System.Text;

namespace Zdez.Gateway.SDK.Service
{
    class ContactsSetting
    {

        private const string HOST = "http://www.zdez.com.cn:9080";
        //private const string HOST = "http://192.168.1.101:8080";

        public const string SA_AUTH_TOKEN = HOST + "/zdezServer/gateway/saauthtoken";
        public const string SCHOOL_MSG_OPTIONS = HOST + "/zdezServer/gateway/schoolmsgoptions";
        public const string POST_SCHOOL_MSG = HOST + "/zdezServer/gateway/postschoolmsg";
        public const string SA_CANCEL_TOKEN = HOST + "/zdezServer/gateway/sacanceltoken";

    }
}