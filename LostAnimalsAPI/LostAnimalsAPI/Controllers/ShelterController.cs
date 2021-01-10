using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using LostAnimalsAPI.Database;
using LostAnimalsAPI.Helpers.Base;
using LostAnimalsAPI.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using static LostAnimalsAPI.Helpers.Base.IFileHelper;

namespace LostAnimalsAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ShelterController : BaseController
    {
        private AnimalsDbContext _ctx;
        private IFileHelper _fileHelper;

        public ShelterController(
            AnimalsDbContext ctx,
            IFileHelper fileHelper)
        {
            _ctx = ctx;
            _fileHelper = fileHelper;
        }

        [HttpGet]
        public async Task<IActionResult> GetShelters(bool forMap)
        {
            var shelters = await _ctx.Shelters
                    .Include(c => c.Location)
                    .ToListAsync();
            if (!forMap)
            {
                shelters.ForEach(async s => s.ImageSource = await _fileHelper.LoadFileAsync(s.Id, ObjectType.Shelter));
            }


            return Ok(shelters);
        }


        [HttpGet("{id}")]
        public async Task<IActionResult> GetShelterById([FromRoute] long id)
        {
            var shelter = await _ctx.Shelters
                .Include(c => c.Location)
                .FirstOrDefaultAsync(s => s.Id == id);

            if (shelter == null)
            {
                return NotFound();
            }

            shelter.ImageSource = await _fileHelper.LoadFileAsync(shelter.Id, ObjectType.Shelter);

            return Ok(shelter);
        }
    }
}
