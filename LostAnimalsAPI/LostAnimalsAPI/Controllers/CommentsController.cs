using System.Linq;
using System.Threading.Tasks;
using LostAnimalsAPI.Database;
using LostAnimalsAPI.Helpers.Base;
using LostAnimalsAPI.Models;
using LostAnimalsAPI.Models.Auth;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;


namespace LostAnimalsAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CommentsController : BaseController
    {
        private readonly AnimalsDbContext _ctx;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly IFileHelper _fileHelper;
        private readonly ICurrentUserHelper _userHelper;

        public CommentsController(
            AnimalsDbContext ctx,
            UserManager<ApplicationUser> userManager,
            IFileHelper fileHelper,
            ICurrentUserHelper userHelper)
        {
            _ctx = ctx;
            _userManager = userManager;
            _fileHelper = fileHelper;
            _userHelper = userHelper;
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
                comment.Author.ImageSource = await _fileHelper.LoadFileAsync(comment.Author.Id, IFileHelper.ObjectType.User, false);
            }
            return Ok(comments);
        }


        [HttpPost]
        public async Task<IActionResult> AddComment([FromBody] Comment comment)
        {
            comment.Author = await _userManager.FindByEmailAsync(_userHelper.Email);
            comment.Post = await _ctx.Posts.FirstOrDefaultAsync(c => c.Id == comment.PostId);
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
