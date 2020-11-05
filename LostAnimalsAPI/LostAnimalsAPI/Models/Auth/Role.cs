using LostAnimalsAPI.Models.Base;
using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Models.Auth
{
    public class Role : IdentityRole<long>, IModelBase
    {
        long IModelBase.Id { get => base.Id; }

        public ICollection<UserRole> UserRoles { get; set; }
    }
}
