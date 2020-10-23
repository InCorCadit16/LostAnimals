using LostAnimalsAPI.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Services.Interfaces
{
    public interface ISpeciesService
    {
        Task<IEnumerable<SpeciesLookup>> GetSpecies();
    }
}
