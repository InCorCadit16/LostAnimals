using LostAnimalsAPI.Models.Auth;
using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Contracts.Responses
{
    public class LoginResponse
    {
        public string Token { get; set; }

        public ApplicationUser User { get; set; }

        public SignInResult Result { get; set; }
    }
}
