using System.ComponentModel.DataAnnotations.Schema;

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
