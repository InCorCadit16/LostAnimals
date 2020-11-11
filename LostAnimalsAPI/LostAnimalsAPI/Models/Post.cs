using LostAnimalsAPI.Models.Auth;
using Microsoft.AspNetCore.Identity;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Data;
using System.Linq;
using System.Net.Sockets;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Models
{
    public class Post:ModelBase
    {
        public ApplicationUser Author { get; set; }
        public string Species { get; set; }
        public string Breed { get; set; }
        public string Color { get; set; }
        public Size Size { get; set; }
        public string Content { get; set; }
        public PostType PostType { get; set; }
        public Location Adress { get; set; }
        public DataType LostTime { get; set; }
        public DateTime PostTime { get; set; }
       // public ICollection<Comment> Comments { get; set; }
    }
}
