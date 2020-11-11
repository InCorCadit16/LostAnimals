using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Security.Cryptography.X509Certificates;
using System.Threading.Tasks;
using LostAnimalsAPI.Database;
using LostAnimalsAPI.Models;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;


namespace LostAnimalsAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PostController : BaseController
    {
        private AnimalsDbContext _ctx;
        public PostController(AnimalsDbContext ctx) 
        {
            _ctx = ctx;
        }
        
        [HttpGet]
        public async Task<IActionResult> Get([FromQuery] bool forMap)
        {
            if (!forMap){
                var post = await _ctx.Posts.Where(u => u.Id >= 0).ToListAsync();
                return Ok(post);
            }

            else
            {
                var post = await _ctx.Posts.Select(u => new
                {
                    u.Id,
                    u.Color,
                    u.Breed,
                    u.Species,
                    u.PostType,
                    u.LostTime,
                    u.Adress
                }).ToListAsync();
                return Ok(post);
            }
        }


        [HttpPost]
        public async Task<IActionResult> Post([FromBody] Post post)
        {
            _ctx.Posts.Add(post);
            await _ctx.SaveChangesAsync();
            return Ok();
        }
    }
}
