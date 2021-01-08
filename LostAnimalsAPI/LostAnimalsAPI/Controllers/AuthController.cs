
using LostAnimalsAPI.Contracts.Requests;
using LostAnimalsAPI.Helpers.Base;
using LostAnimalsAPI.Services.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Controllers
{
    public class AuthController: BaseController
    {
       
        private IAuthService _authService;
        private ICurrentUserHelper _userHelper;      

        public AuthController
        (
            IAuthService authService,
            ICurrentUserHelper userHelper
        )
        {
            _authService = authService;
            _userHelper = userHelper;
        }

        [HttpPost("register")]
        [AllowAnonymous]
        public async Task<IActionResult> Register(RegistrationRequest request)
        {
            var result = await _authService.RegisterUser(request);
            return Ok(result);
        }

        [HttpPost("login")]
        [AllowAnonymous]
        public async Task<IActionResult> Login(LoginRequest request)
        {
            var result = await _authService.LoginUser(request);
            return Ok(result);
        }

        [HttpPost("logout")]
        public async Task<IActionResult> Logout()
        {
            await _authService.LogoutUser();
            return Ok();
        }

        [HttpGet("my")]
        public async Task<IActionResult> getUser()
        {
            var user = await _authService.GetUser(_userHelper.Email);
            return Ok(new {
                user.FirstName,
                user.LastName,
                user.Id,
                user.Email,
                user.PhoneNumber,
                user.ImageSource
            });
        }


    }
}
