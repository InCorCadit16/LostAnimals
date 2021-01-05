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
     [Route("api/shelter")]
     [ApiController]
     public class ShelterController : BaseController
     {
          private AnimalsDbContext _ctx;

          public ShelterController(AnimalsDbContext ctx)
          {
               _ctx = ctx;
          }

          [AllowAnonymous]
          [HttpGet]
          public async Task<IActionResult> getShelters()
          {
               var shelters = await _ctx.Shelters
                   .Include(c => c.Name)
                   .Include(c => c.Description)
                   .Include(c => c.Location)
                   .ToListAsync();

               return Ok(shelters);     
          }
     }
}
