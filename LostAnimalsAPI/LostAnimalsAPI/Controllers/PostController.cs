using System;
using System.Linq;
using System.Threading.Tasks;
using LostAnimalsAPI.Contracts.Requests;
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
    public class PostController : BaseController
    {
        private AnimalsDbContext _ctx;
        private UserManager<ApplicationUser> _userManager;
        private IFileHelper _fileHelper;
        private ICurrentUserHelper _userHelper;

        public PostController(
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
        public async Task<IActionResult> Get([FromQuery] bool forMap = false)
        {
            if (!forMap){
                var posts = await _ctx.Posts
                    .Include(p => p.Breed)
                    .Include(p => p.Color)
                    .Include(p => p.Species)
                    .Include(p => p.Author)
                    .Include(p => p.Location)
                    .ToListAsync();

                posts.ForEach(async post => { post.ImageSource = await _fileHelper.LoadFileAsync(post.Id); });
                return Ok(posts);
            }
            else
            {
                await _ctx.Posts.ForEachAsync(async p => { p.ImageSource = await _fileHelper.LoadFileAsync(p.Id, true, false); });

                var posts = await _ctx.Posts
                    .Include(p => p.Location)
                    .Include(p => p.Species)
                    .Select(p => new
                    {
                        p.Id,
                        p.LostTime,
                        p.ImageSource,
                        p.Location,
                        p.Species
                    }).ToListAsync();


                return Ok(posts);
            }
        }


        [HttpGet("{id}")]
        public async Task<IActionResult> GetPostById([FromRoute] long id)
        {
            var post = await _ctx.Posts
                .Where(p => p.Id == id)
                .Include(p => p.Author)
                .Include(p => p.Breed)
                .Include(p => p.Color)
                .Include(p => p.Species)
                .Include(p => p.Location)
                .FirstOrDefaultAsync();

            post.ImageSource = await _fileHelper.LoadFileAsync(post.Id);

            return Ok(post);
        }

        [HttpPost]
        public async Task<IActionResult> Post([FromBody] CreatePostRequest postRequest)
        {
            var post = new Post
            {
                Species = await _ctx.Species.FirstAsync(s => s.Id == postRequest.Species.Id),
                Breed = await _ctx.Breeds.FirstAsync(b => b.Id == postRequest.Breed.Id),
                Color = await _ctx.Colors.FirstAsync(s => s.Id == postRequest.Color.Id),
                Size = postRequest.Size,
                PostType = postRequest.PostType,
                LostTime = postRequest.LostTime,
                Content = postRequest.Content,
                ImageSource = postRequest.ImageSource,
                Location = postRequest.Location,
                PostTime = postRequest.PostTime,
                Author = await _userManager.FindByEmailAsync(_userHelper.Email)
            };

            _ctx.Attach(post);
            _ctx.Posts.Add(post);
            await _ctx.SaveChangesAsync();

            if (postRequest.ImageSource != null)
            {
                _fileHelper.SaveFile(post.Id, postRequest.ImageSource, true);
            }
            
            return Ok(post.Id);
        }

        [HttpPut]
        public async Task<IActionResult> UpdatePost([FromBody] UpdatePostRequest request)
        {
            var post = await _ctx.Posts.Include(p => p.Author)
                                        .FirstOrDefaultAsync(p => p.Id == request.PostId);

            if (post.Author.Email != _userHelper.Email)
            {
                return Forbid();
            }

            post.Species = await _ctx.Species.FirstAsync(s => s.Id == request.Species.Id);
            post.Breed = await _ctx.Breeds.FirstAsync(b => b.Id == request.Breed.Id);
            post.Color = await _ctx.Colors.FirstAsync(s => s.Id == request.Color.Id);
            post.PostType = request.PostType;
            post.Size = request.Size;
            post.Content = request.Content;
            post.LostTime = request.LostTime;
            post.Location = request.Location;

            if (request.ImageSource != null)
            {
                post.ImageSource = request.ImageSource;
                _fileHelper.SaveFile(post.Id, post.ImageSource, true);
            }

            _ctx.Posts.Update(post);
            await _ctx.SaveChangesAsync();

            return Ok();
        }


        [HttpDelete("{id}")]
        public async Task<IActionResult> DeletePost([FromRoute] long id)
        {
            var post = await _ctx.Posts.Include(p => p.Author)
                                        .Include(p => p.Location)
                                        .FirstOrDefaultAsync(p => p.Id == id);

            if (post.Author.Email != _userHelper.Email)
            {
                return Forbid();
            }

            _ctx.Remove(post.Location);
            _ctx.Remove(post);
            await _ctx.SaveChangesAsync();

            return Ok();
        }
    }
}
