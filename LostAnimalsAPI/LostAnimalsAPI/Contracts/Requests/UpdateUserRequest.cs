
namespace LostAnimalsAPI.Contracts.Requests
{
    public class UpdateUserRequest
    {
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string PhoneNumber { get; set; }
        public byte[] ImageResource { get; set; }
    }
}
