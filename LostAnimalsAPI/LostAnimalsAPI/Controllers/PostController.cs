using System.Linq;
using System.Threading.Tasks;
using LostAnimalsAPI.Database;
using LostAnimalsAPI.Helpers.Base;
using LostAnimalsAPI.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;


namespace LostAnimalsAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PostController : BaseController
    {
        private AnimalsDbContext _ctx;
        private IFileHelper _fileHelper;
        public PostController(
            AnimalsDbContext ctx,
            IFileHelper fileHelper) 
        {
            _ctx = ctx;
            _fileHelper = fileHelper;
        }
        
        [HttpGet]
        public async Task<IActionResult> Get([FromQuery] bool forMap = false)
        {
            if (!forMap){
                var posts = await _ctx.Posts
                    .Include(p => p.Breed)
                    .Include(p => p.Color)
                    .Include(p => p.Species)
                    .Include(p => p.Author)
                    .Include(p => p.Address)
                    .ToListAsync();

                posts.ForEach(async post => { post.ImageSource = await _fileHelper.LoadFileAsync(post.Id); });
                return Ok(posts);
            }
            else
            {
                await _ctx.Posts.ForEachAsync(async p => { p.ImageSource = await _fileHelper.LoadFileAsync(p.Id, true, false); });

                var posts = await _ctx.Posts
                    .Include(p => p.Address)
                    .Select(p => new
                    {
                        p.Id,
                        p.LostTime,
                        p.ImageSource,
                        p.Address
                    }).ToListAsync();


                return Ok(posts);
            }
        }


        [HttpPost]
        public async Task<IActionResult> Post([FromBody] Post post)
        {
            _ctx.Attach(post);
            _ctx.Posts.Add(post);
            await _ctx.SaveChangesAsync();
            return Ok();
        }
    }
}
