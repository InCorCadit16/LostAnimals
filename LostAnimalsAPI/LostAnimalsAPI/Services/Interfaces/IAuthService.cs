
using LostAnimalsAPI.Contracts.Requests;
using LostAnimalsAPI.Contracts.Responses;
using LostAnimalsAPI.Models.Auth;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Services.Interfaces
{
    public interface IAuthService
    {

        Task<RegistrationResponse> RegisterUser(RegistrationRequest request);

        Task<LoginResponse> LoginUser(LoginRequest request);

        Task LogoutUser();


        Task<ApplicationUser> GetUser(string userEmail);
    }
}
