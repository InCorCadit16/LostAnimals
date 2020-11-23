using LostAnimalsAPI.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LostAnimalsAPI.Database.Configurations
{
    public class ColorLookupConfig : IEntityTypeConfiguration<ColorLookup>
    {
        public void Configure(EntityTypeBuilder<ColorLookup> builder)
        {
            builder.HasIndex(c => c.Name);
        }
    }
}
