using LostAnimalsAPI.Contracts.Responses;
using LostAnimalsAPI.Database;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class LookupController : BaseController
    {

        private AnimalsDbContext _db;

        public LookupController(AnimalsDbContext db)
        {
            _db = db;
        }

        [HttpGet]
        public async Task<IActionResult> GetLookups()
        {
            var species = await _db.Species.Include(s => s.Breeds).ToArrayAsync();
            var colors = await _db.Colors.ToArrayAsync();

            return Ok(new LookupsResponse { species = species, colors = colors });
        }
    }
}
