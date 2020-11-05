using LostAnimalsAPI.Models.Auth;
using Microsoft.AspNetCore.Identity;

namespace LostAnimalsAPI.Contracts.Responses
{
    public class RegistrationResponse
    {
        public string Token { get; set; }

        public ApplicationUser User { get; set; }

        public IdentityResult Result { get; set; }
    }
}
