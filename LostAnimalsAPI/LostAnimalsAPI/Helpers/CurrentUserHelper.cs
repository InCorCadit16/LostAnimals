using LostAnimalsAPI.Helpers.Base;
using Microsoft.IdentityModel.JsonWebTokens;
using System.Linq;
using System.Security.Claims;
namespace LostAnimalsAPI.Helpers
{
    public class CurrentUserHelper : ICurrentUserHelper
    {
        private readonly ClaimsPrincipal _user;

        public string Email => _user.Claims.Single(claim => claim.Type == JwtRegisteredClaimNames.Email).Value;

        public CurrentUserHelper(ClaimsPrincipal user)
        {
            _user = user;
        }
    }
}
