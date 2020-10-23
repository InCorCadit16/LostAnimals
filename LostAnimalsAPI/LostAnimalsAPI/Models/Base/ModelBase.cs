using LostAnimalsAPI.Models.Base;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Models
{
    public abstract class ModelBase: IModelBase
    {
        public long Id { get; set; }
    }
}
