using LostAnimalsAPI.Models;
using LostAnimalsAPI.Repositories.Interfaces;
using LostAnimalsAPI.Services.Interfaces;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Services
{
    public class SpeciesService : ISpeciesService
    {
        private IRepositoryBase<SpeciesLookup> _speciesRepository;

        public SpeciesService(IRepositoryBase<SpeciesLookup> speciesRepository)
        {
            _speciesRepository = speciesRepository;
        }

        public async Task<IEnumerable<SpeciesLookup>> GetSpecies()
        {
            return await _speciesRepository.Get(); 
        }
    }
}
