using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Models.Auth
{
    public class UserRole: IdentityUserRole<long>
    {
    }
}
