using LostAnimalsAPI.Contracts.Requests;
using LostAnimalsAPI.Contracts.Responses;
using LostAnimalsAPI.Models.Auth;
using LostAnimalsAPI.Services.Interfaces;
using Microsoft.AspNetCore.Identity;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.JsonWebTokens;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Services
{
    public class AuthService : IAuthService
    {
        private IConfiguration _configuration;
        private SignInManager<ApplicationUser> _signInManager;
        private UserManager<ApplicationUser> _userManager;

        public AuthService
        (
            IConfiguration configuration,
            SignInManager<ApplicationUser> signInManager,
            UserManager<ApplicationUser> userManager
        )
        {
            _configuration = configuration;
            _signInManager = signInManager;
            _userManager = userManager;
        }

        public async Task<ApplicationUser> GetUser(string userEmail)
        {
            return await _userManager.FindByEmailAsync(userEmail);
        }


        public async Task<LoginResponse> LoginUser(LoginRequest request)
        {
            var user = await _userManager.FindByEmailAsync(request.Email);
            var result = await _signInManager.PasswordSignInAsync(user, request.Password, false, false);

            if (!result.Succeeded)
                return new LoginResponse { Result = result };
            else
                return new LoginResponse
                {
                    Result = result,
                    User = user,
                    Token = CreateToken(user)
                };
        }

        public async Task LogoutUser()
        {
            await _signInManager.SignOutAsync();
        }

        public async Task<RegistrationResponse> RegisterUser(RegistrationRequest request)
        {
            var user = new ApplicationUser
            {
                FirstName = request.FirstName,
                LastName = request.LastName,
                PhoneNumber = request.Phone,
                Email = request.Email,
                UserName = request.Email
            };

            var result = await _userManager.CreateAsync(user, request.Password);

            if (!result.Succeeded)
                return new RegistrationResponse { Result = result };
            else
                return new RegistrationResponse
                {
                    Result = result,
                    User = user,
                    Token = CreateToken(user)
                };
        }


        private string CreateToken(ApplicationUser user)
        {
            var claims = new List<Claim>()
            {
                new Claim(JwtRegisteredClaimNames.Email, user.Email),
                new Claim(JwtRegisteredClaimNames.GivenName, user.FirstName),
                new Claim(JwtRegisteredClaimNames.NameId, user.Id.ToString())
            };

            if (user.LastName != null)
                claims.Add(new Claim(JwtRegisteredClaimNames.FamilyName, user.LastName));

            var jwtSecret = _configuration["Jwt:Key"];
            var signingKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(jwtSecret));
            var signingCredentials = new SigningCredentials(signingKey, SecurityAlgorithms.HmacSha256);

            var utcNow = DateTime.UtcNow;
            var tokenHandler = new System.IdentityModel.Tokens.Jwt.JwtSecurityTokenHandler();
            var tokenDescriptor = new SecurityTokenDescriptor
            {
                Subject = new ClaimsIdentity(claims),
                Expires = utcNow.AddMonths(3),
                SigningCredentials = signingCredentials,
                Issuer = _configuration["Jwt:Issuer"],
                Audience = _configuration["Jwt:Audience"]
            };

            var token = tokenHandler.CreateToken(tokenDescriptor);
            var jwt = tokenHandler.WriteToken(token);

            return jwt;
            
        }
    }
}
