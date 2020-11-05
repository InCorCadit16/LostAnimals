using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Helpers.Base
{
    public interface ICurrentUserHelper
    {
        string Email { get; }
    }
}
