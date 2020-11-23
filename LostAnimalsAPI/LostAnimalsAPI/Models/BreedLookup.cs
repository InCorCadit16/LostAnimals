using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Models
{
    public class BreedLookup: ModelBase
    {
        public string Name { get; set; }

        public SpeciesLookup Species { get; set; }
    }
}
