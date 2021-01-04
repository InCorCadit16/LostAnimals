using LostAnimalsAPI.Models.Auth;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Models
{
    public class Comment: ModelBase
    {
        public string Content { get; set; }
        public ApplicationUser Author { get; set; }
        public long PostId { get; set; }
        public Post Post { get; set; }
        public Location Location { get; set; }
        public DateTime SeenTime { get; set; }
        public DateTime CommentTime { get; set; }
    }
}
