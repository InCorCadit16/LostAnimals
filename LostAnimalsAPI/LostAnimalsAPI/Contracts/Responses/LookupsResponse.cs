using LostAnimalsAPI.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Contracts.Responses
{
    public class LookupsResponse
    {
        public SpeciesLookup[] species { get; set; }

        public ColorLookup[] colors { get; set; }
    }
}
