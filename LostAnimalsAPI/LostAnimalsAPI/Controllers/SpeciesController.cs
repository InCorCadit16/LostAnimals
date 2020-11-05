using LostAnimalsAPI.Services.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Controllers
{
    
    public class SpeciesController: BaseController
    {
        private ISpeciesService _speciesService;

        public SpeciesController(ISpeciesService speciesService)
        {
            _speciesService = speciesService;
        }

        [HttpGet]
        [Authorize]
        public async Task<IActionResult> GetSpecies()
        {
            return Ok(await _speciesService.GetSpecies());
        }
    }
}
