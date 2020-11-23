using LostAnimalsAPI.Models.Auth;
using System;
using System.ComponentModel.DataAnnotations.Schema;

namespace LostAnimalsAPI.Models
{
    public class Post : ModelBase
    {   
        public ApplicationUser Author { get; set; }
        public SpeciesLookup Species { get; set; }
        public BreedLookup Breed { get; set; }
        public ColorLookup Color { get; set; }
        public Size Size { get; set; }
        public string Content { get; set; }
        public PostType PostType { get; set; }
        public Location Address { get; set; }
        public DateTime LostTime { get; set; }
        public DateTime PostTime { get; set; }
        // public ICollection<Comment> Comments { get; set; }

        [NotMapped]
        public byte[] ImageSource { get; set; }
    }
}
