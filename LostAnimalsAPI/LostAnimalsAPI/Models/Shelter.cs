using LostAnimalsAPI.Models.Auth;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Models
{
    public class Shelter: ModelBase
    {
        public string Name { get; set; }
        public string Description { get; set; }                   
        public Location Location { get; set; }

        [NotMapped]
        public byte[] ImageResource { get; set; }
    }
}
