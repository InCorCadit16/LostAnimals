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
    [Route("api/[controller]")]
    [ApiController]
    public class CommentsController : BaseController
    {
        private AnimalsDbContext _ctx;
        private IFileHelper _fileHelper;

        public CommentsController(
            AnimalsDbContext ctx,
            IFileHelper fileHelper)
        {
            _ctx = ctx;
            _fileHelper = fileHelper;
        }

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
        public async Task<IActionResult> GetCommentById([FromRoute] long id)
        {
            var comment = await _ctx.Comments.FirstOrDefaultAsync(c => c.Id == id);
            return Ok(comment);
        }

        [HttpGet("post/{postId}")]
        public async Task<IActionResult> GetCommentByPostId(long postId)
        {
            var comments = await _ctx.Comments
                .Where(c => c.PostId == postId)
                .Include(c => c.Location)
                .Include(c => c.Author)
                .ToListAsync();

            foreach (var comment in comments)
            {
                comment.Author.ImageSource = await _fileHelper.LoadFileAsync(comment.Author.Id, false, false);
            }
            return Ok(comments);
        }


        [HttpPost]
        public async Task<IActionResult> AddComment([FromBody] Comment comment)
        {
            _ctx.Attach(comment);
            _ctx.Comments.Add(comment);
            await _ctx.SaveChangesAsync();
            return Ok();
        }


        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateComment([FromRoute] int id, [FromBody] Comment comment)
        {
            var item = await _ctx.Comments.FirstOrDefaultAsync(c => c.Id == id);
            item.Author = comment.Author;
            item.Content = comment.Content;
            item.Location = comment.Location;

            _ctx.Comments.Update(item);

            await _ctx.SaveChangesAsync();

            return NoContent();
        }

    }
}
