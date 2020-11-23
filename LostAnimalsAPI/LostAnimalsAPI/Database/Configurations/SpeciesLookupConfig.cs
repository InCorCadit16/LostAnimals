using LostAnimalsAPI.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace LostAnimalsAPI.Database.Configurations
{
    public class SpeciesLookupConfig : IEntityTypeConfiguration<SpeciesLookup>
    {
        public void Configure(EntityTypeBuilder<SpeciesLookup> builder)
        {
            builder.HasIndex(sl => sl.Name);
        }
    }
}
