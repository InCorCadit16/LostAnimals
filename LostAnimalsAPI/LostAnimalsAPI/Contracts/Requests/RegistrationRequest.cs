using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Contracts.Requests
{
    public class RegistrationRequest
    {
        [Required]
        [EmailAddress]
        public string Email { get; set; }

        [Required]
        public string FirstName { get; set; }
        

        public string LastName { get; set; }

        [Phone]
        public string Phone { get; set; }

        [Required]
        public string Password { get; set; }

        [Required]
        public string PasswordRepeat { get; set; }
    }
}
