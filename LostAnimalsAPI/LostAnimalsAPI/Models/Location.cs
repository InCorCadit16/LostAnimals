using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Models
{
    public class Location:ModelBase
    {
        public string Address { set; get; }
        public double Latitude { set; get; }
        public double Longtitude {set; get;}
    }
}
