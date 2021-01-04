using LostAnimalsAPI.Models;
using System;
namespace LostAnimalsAPI.Contracts.Requests
{
    public class CreatePostRequest
    {
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
