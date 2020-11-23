using LostAnimalsAPI.Models.Base;
using Microsoft.AspNetCore.Identity;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace LostAnimalsAPI.Models.Auth
{
    public class ApplicationUser: IdentityUser<long>, IModelBase
    {
        long IModelBase.Id { get => base.Id; }

        public string FirstName { get; set; }

        public string LastName { get; set; }


        public ICollection<UserRole> UserRoles { get; set; }

        [NotMapped]
        public byte[] ImageSource { get; set; }
    }
}
