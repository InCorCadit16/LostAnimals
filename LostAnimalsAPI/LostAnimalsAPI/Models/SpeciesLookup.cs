using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace LostAnimalsAPI.Models
{
    public class SpeciesLookup : ModelBase
    {
        public string Name { get; set; }

        public ICollection<BreedLookup> Breeds { get; set; }
    }
}
