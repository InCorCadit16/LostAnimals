using System.Linq;
using System.Threading.Tasks;
using LostAnimalsAPI.Database;
using LostAnimalsAPI.Helpers.Base;
using LostAnimalsAPI.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;


namespace LostAnimalsAPI.Controllers
{
    [Route("api/comment")]
    [ApiController]
    public class CommentsController : BaseController
    {
        private AnimalsDbContext _ctx;

        public CommentsController(AnimalsDbContext ctx)
        {
            _ctx = ctx;
        }

        [AllowAnonymous]
        [HttpGet]
        public async Task<IActionResult> GetComments()
        {
            var comments = await _ctx.Comments
                .Include(c => c.Post)
                .Include(c => c.Author)
                .Include(c => c.Location)
                .ToListAsync();

            return Ok(comments);
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> GetCommentById([FromRoute] int id)
        {
            var comment = await _ctx.Comments.FirstOrDefaultAsync(c => c.Id == id);
            return Ok(comment);
        }

        [AllowAnonymous]
        [HttpPost]
        public async Task<IActionResult> AddComment([FromBody] Comment comment)
        {
            _ctx.Attach(comment);
            _ctx.Comments.Add(comment);
            await _ctx.SaveChangesAsync();
            return Ok();
        }

        [AllowAnonymous]
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateComment([FromRoute] int id, [FromBody] Comment comment)
        {
            var item = await _ctx.Comments.FirstOrDefaultAsync(c => c.Id == id);
            item.Author = comment.Author;
            item.Content = comment.Content;
            item.Location = comment.Location;
            item.ImageResource = comment.ImageResource;

            _ctx.Comments.Update(item);

            await _ctx.SaveChangesAsync();

            return NoContent();
        }

    }
}
