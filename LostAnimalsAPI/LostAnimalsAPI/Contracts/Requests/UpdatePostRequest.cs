using LostAnimalsAPI.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Contracts.Requests
{
    public class UpdatePostRequest
    {
        public long PostId { get; set; }
        public SpeciesLookup Species { get; set; }
        public BreedLookup Breed { get; set; }
        public ColorLookup Color { get; set; }
        public Size Size { get; set; }
        public PostType PostType { get; set; }
        public string Content { get; set; }
        public Location Location { get; set; }
        public DateTime LostTime { get; set; }
        public byte[] ImageSource { get; set; }
    }
}
