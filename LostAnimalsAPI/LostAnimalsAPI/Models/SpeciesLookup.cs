using System.ComponentModel.DataAnnotations;

namespace LostAnimalsAPI.Models
{
    public class SpeciesLookup : ModelBase
    {
        [MaxLength(50)]
        public string Name { get; set; }
    }
}
